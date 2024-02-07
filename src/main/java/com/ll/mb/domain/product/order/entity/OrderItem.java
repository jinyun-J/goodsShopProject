package com.ll.mb.domain.product.order.entity;

import com.ll.mb.domain.product.product.entity.Product;
import com.ll.mb.domain.rebate.rebate.entity.RebateItem;
import com.ll.mb.global.jpa.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Setter
@Getter
@ToString(callSuper = true)
public class OrderItem extends BaseTime {
    @ManyToOne
    private Order order;
    @ManyToOne
    private Product product;
    private double rebateRate;
    private long payPrice;
    @ManyToOne
    private RebateItem rebateItem;

    public void setPaymentDone() {
        if (product != null && product.getRelTypeCode() != null) {
            switch (product.getRelTypeCode()) {
                case "book":
                    // book 관련 로직 처리
                    if (order != null && order.getBuyer() != null && product.getBook() != null) {
                        order.getBuyer().addMyBook(product.getBook());
                    }
                    break;
                // 다른 case들에 대한 처리
            }
        }
    }


    public void setCancelDone() {
    }

    public void setRefundDone() {
        switch (product.getRelTypeCode()) {
            case "book" -> order.getBuyer().removeMyBook(product.getBook());
        }
    }
}
