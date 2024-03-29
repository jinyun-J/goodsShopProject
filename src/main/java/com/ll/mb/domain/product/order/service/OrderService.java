package com.ll.mb.domain.product.order.service;

import com.ll.mb.domain.book.purchasedBook.service.PurchasedBookService;
import com.ll.mb.domain.cash.cash.entity.CashLog;
import com.ll.mb.domain.global.exceptions.GlobalException;
import com.ll.mb.domain.member.member.entity.Member;
import com.ll.mb.domain.member.member.service.MemberService;
import com.ll.mb.domain.product.cart.entity.CartItem;
import com.ll.mb.domain.product.cart.service.CartService;
import com.ll.mb.domain.product.order.entity.Order;
import com.ll.mb.domain.product.order.repository.OrderItemRepository;
import com.ll.mb.domain.product.order.repository.OrderRepository;
import com.ll.mb.domain.product.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final MemberService memberService;
    private final PurchasedBookService purchasedBookService;

    @Transactional
    public Order createFromProduct(Member buyer, Product product) {
        Order order = Order.builder()
                .buyer(buyer)
                .build();

        order.addItem(product);

        orderRepository.save(order);

        return order;
    }

    @Transactional
    public Order createFromCart(Member buyer) {
        List<CartItem> cartItems = cartService.findByBuyerOrderByIdDesc(buyer);

        Order order = Order.builder()
                .buyer(buyer)
                .build();

        cartItems.stream()
                .forEach(order::addItem);

        orderRepository.save(order);

        cartItems.stream()
                .forEach(cartService::delete);

        return order;
    }

    @Transactional
    public void payByCashOnly(Order order) {
        Member buyer = order.getBuyer();
        long restCash = buyer.getRestCash();
        long payPrice = order.calcPayPrice();

        if (payPrice > restCash) {
            throw new GlobalException("400-1", "예치금이 부족합니다.");
        }

        memberService.addCash(buyer, payPrice * -1, CashLog.EvenType.사용__예치금_주문결제, order);

        payDone(order);
    }

    @Transactional
    public void payByTossPayments(Order order, long pgPayPrice) {
        Member buyer = order.getBuyer();
        long restCash = buyer.getRestCash();
        long payPrice = order.calcPayPrice();

        long useRestCash = payPrice - pgPayPrice;

        memberService.addCash(buyer, pgPayPrice, CashLog.EvenType.충전__토스페이먼츠, order);
        memberService.addCash(buyer, pgPayPrice * -1, CashLog.EvenType.사용__토스페이먼츠_주문결제, order);

        if (useRestCash > 0) {
            if (useRestCash > restCash) {
                throw new RuntimeException("예치금이 부족합니다.");
            }

            memberService.addCash(buyer, useRestCash * -1, CashLog.EvenType.사용__예치금_주문결제, order);
        }

        payDone(order);
    }

    private void payDone(Order order) {
        order.setPaymentDone();

        order.getOrderItems()
                .stream()
                .forEach(orderItem -> {
                    Product product = orderItem.getProduct();

                    if (product.isBook()) {
                        purchasedBookService.add(order.getBuyer(), product.getBook());
                    }
                });
    }

    private void refund(Order order) {
        long payPrice = order.calcPayPrice();

        memberService.addCash(order.getBuyer(), payPrice, CashLog.EvenType.환불__예치금_주문결제, order);

        order.setRefundDone();

        order.getOrderItems()
                .stream()
                .forEach(orderItem -> {
                    Product product = orderItem.getProduct();

                    if (product.isBook()) {
                        purchasedBookService.delete(order.getBuyer(), product.getBook());
                    }
                });
    }

    public void checkCanPay(String orderCode, long pgPayPrice) {
        Order order = findByCode(orderCode).orElse(null);

        if (order == null)
            throw new GlobalException("400-1", "존재하지 않는 주문입니다.");

        checkCanPay(order, pgPayPrice);
    }

    public void checkCanPay(Order order, long pgPayPrice) {
        if (!canPay(order, pgPayPrice))
            throw new GlobalException("400-2", "PG결제금액 혹은 예치금이 부족하여 결제할 수 없습니다.");
    }

    public boolean canPay(Order order, long pgPayPrice) {
        if (!order.isPayable()) return false;

        long restCash = order.getBuyer().getRestCash();

        return order.calcPayPrice() <= restCash + pgPayPrice;
    }

    public Optional<Order> findById(long id) {
        return orderRepository.findById(id);
    }

    public boolean actorCanSee(Member actor, Order order) {
        return order.getBuyer().equals(actor);
    }

    public Optional<Order> findByCode(String code) {
        long id = Long.parseLong(code.split("__", 2)[1]);

        return findById(id);
    }

    public Page<Order> search(Member buyer, Boolean payStatus, Boolean cancelStatus, Boolean refundStatus, Pageable pageable) {
        return orderRepository.search(buyer, payStatus, cancelStatus, refundStatus, pageable);
    }

    @Transactional
    public void cancel(Order order) {
        if (!order.isCancelable())
            throw new GlobalException("400-1", "취소할 수 없는 주문입니다.");

        order.setCancelDone();

        if (order.isPayDone())
            refund(order);
    }

    public boolean canCancel(Member actor, Order order) {
        return actor.equals(order.getBuyer()) && order.isCancelable();
    }
}
