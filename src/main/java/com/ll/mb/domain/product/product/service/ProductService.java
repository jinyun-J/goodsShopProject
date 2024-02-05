package com.ll.mb.domain.product.product.service;

import com.ll.mb.domain.base.genFile.service.GenFileService;
import com.ll.mb.domain.book.book.entity.Book;
import com.ll.mb.domain.member.member.entity.Member;
import com.ll.mb.domain.product.product.entity.Product;
import com.ll.mb.domain.product.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductBookmarkService productBookmarkService;
    private final GenFileService genFileService; // GenFileService 주입

    @Transactional
    public Product createProduct(Book book, boolean published, MultipartFile productImage) {
        if (book.getProduct() != null) return book.getProduct();

        Product product = Product.builder()
                .maker(book.getAuthor())
                .relTypeCode(book.getModelName())
                .relId(book.getId())
                .name(book.getTitle())
                .price(book.getPrice())
                .published(published)
                .build();

        productRepository.save(product);

        return product;
    }

    public Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }

    public Page<Product> search(Member maker, Boolean published, List<String> kwTypes, String kw, Pageable pageable) {
        return productRepository.search(maker, published, kwTypes, kw, pageable);
    }

    public boolean canBookmark(Member actor, Product product) {
        if (actor == null) return false;

        return productBookmarkService.canBookmark(actor, product);
    }

    public boolean canCancelBookmark(Member actor, Product product) {
        if (actor == null) return false;

        return productBookmarkService.canCancelBookmark(actor, product);
    }

    @Transactional
    public void bookmark(Member member, Product product) {
        productBookmarkService.bookmark(member, product);
    }

    @Transactional
    public void cancelBookmark(Member member, Product product) {
        productBookmarkService.cancelBookmark(member, product);
    }
    @Transactional
    public void saveProductImage(long productId, MultipartFile productImage) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        genFileService.save(product.getModelName(), product.getId(), "product", "mainImage", 0, productImage);
    }

    public Optional<String> getProductImageUrl(long productId) {
        return genFileService.findBy("Product", productId, "product", "mainImage", 1)
                .map(file -> file.getUrl());
    }

    @Transactional
    public void removeProductImage(long productId) {
        genFileService.remove("Product", productId, "product", "mainImage", 1);
    }

}
