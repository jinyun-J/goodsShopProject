package com.ll.mb.domain.product.product.service;

import com.ll.mb.domain.base.genFile.service.GenFileService;
import com.ll.mb.domain.book.book.entity.Book;
import com.ll.mb.domain.member.member.entity.Member;
import com.ll.mb.domain.product.product.entity.Product;
import com.ll.mb.domain.product.product.repository.ProductRepository;
import com.ll.mb.global.app.AppConfig;
import com.ll.mb.global.rsData.RsData;
import com.ll.mb.standard.util.Ut;
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
    public RsData<Product> createProduct(String name, long price, boolean published, MultipartFile productImage) {
        // 제품 객체 생성 및 속성 설정
        Product product = Product.builder()
                .name(name)
                .price(price)
                .published(published)
                .build();
        // 제품 정보 저장
        productRepository.save(product);

        // 제품 이미지 처리 로직
        if (productImage != null && !productImage.isEmpty()) {
            String productImgFilePath = Ut.file.toFile(productImage, AppConfig.getTempDirPath());
            genFileService.save(product.getModelName(), product.getId(), "product", "mainImage", 1, productImgFilePath);
        }

        // 성공 응답 반환
        return RsData.of("200", "제품이 성공적으로 생성되었습니다.", product);
    }

    @Transactional
    public void updateProductImage(long productId, MultipartFile productImage) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        String productImgFilePath = Ut.file.toFile(productImage, AppConfig.getTempDirPath());
        genFileService.save(product.getModelName(), product.getId(), "product", "mainImage", 1, productImgFilePath);
    }

    @Transactional
    public void removeProductImage(long productId) {
        genFileService.remove("Product", productId, "product", "mainImage", 1);
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

    public String getProductImageUrl(long productId) {
        return genFileService.findBy("Product", productId, "product", "mainImage", 1)
                .map(file -> file.getUrl())
                .orElse(null); // 이미지가 없을 경우 null 반환
    }
}
