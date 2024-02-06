package com.ll.mb.domain.product.product.controller;

import com.ll.mb.domain.global.exceptions.GlobalException;
import com.ll.mb.domain.product.product.entity.Product;
import com.ll.mb.domain.product.product.entity.ProductBookmark;
import com.ll.mb.domain.product.product.service.ProductBookmarkService;
import com.ll.mb.domain.product.product.service.ProductService;
import com.ll.mb.global.rq.Rq;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final Rq rq;
    private final ProductService productService;
    private final ProductBookmarkService productBookmarkService;

    @Getter
    @Setter
    public static class ProductForm {
        private String name;
        private long price;
        private boolean published;
        private MultipartFile productImage;
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String CreateProduct(ProductForm productForm) {
        return "domain/product/form";
    }
    @PostMapping("/create")
    public String createProduct(@ModelAttribute ProductForm productForm) {
        try {
            // 제품 생성 로직을 처리합니다. 필요한 경우 여기에 추가 데이터 변환 로직을 포함할 수 있습니다.
            productService.createProduct(
                    productForm.getName(),
                    productForm.getPrice(),
                    productForm.isPublished(),
                    productForm.getProductImage()
            );
            return "redirect:/product/list";
        } catch (Exception e) {
            // 에러 처리 로직. 실제 사용 시 적절한 예외 처리와 로깅을 구현해야 합니다.
            return "redirect:/product/create?error";
        }
    }

    @GetMapping("/bookmarkList")
    public String showBookmarkList() {
        List<ProductBookmark> productBookmarks = productBookmarkService.findByMember(rq.getMember());

        rq.attr("productBookmarks", productBookmarks);

        return "domain/product/bookmarkList";
    }

    @GetMapping("/list")
    public String showList(
            @RequestParam(value = "kwType", defaultValue = "name") List<String> kwTypes,
            @RequestParam(value = "kw", defaultValue = "") String kw,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model
    ) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(sorts));

        Map<String, Boolean> kwTypesMap = kwTypes
                .stream()
                .collect(Collectors.toMap(
                        kwType -> kwType,
                        kwType -> true
                ));

        Page<Product> itemsPage = productService.search(null, true, kwTypes, kw, pageable);
        model.addAttribute("itemPage", itemsPage);
        model.addAttribute("kwTypesMap", kwTypesMap);
        model.addAttribute("page", page);

        return "domain/product/list";
    }

    @GetMapping("/myList")
    public String showMyList(
            @RequestParam(value = "kwType", defaultValue = "name") List<String> kwTypes,
            @RequestParam(value = "kw",defaultValue = "") String kw,
            @RequestParam(value = "page",defaultValue = "1") int page,
            Model model
    ) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(sorts));

        Map<String, Boolean> kwTypesMap = kwTypes
                .stream()
                .collect(Collectors.toMap(
                        kwType -> kwType,
                        kwType -> true
                ));

        Page<Product> itemsPage = productService.search(rq.getMember(), null, kwTypes, kw, pageable);
        model.addAttribute("itemPage", itemsPage);
        model.addAttribute("kwTypesMap", kwTypesMap);
        model.addAttribute("page", page);

        return "domain/product/myList";
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String showDetail(@PathVariable long id) {
        return null;
    }

    @PostMapping("/{id}/bookmark")
    @PreAuthorize("isAuthenticated()")
    public String bookmark(
            @PathVariable("id") long id,
            @RequestParam(value = "redirectUrl", defaultValue = "/") String redirectUrl
    ) {
        Product product = productService.findById(id).orElseThrow(() -> new GlobalException("400", "존재하지 않는 상품입니다."));
        productService.bookmark(rq.getMember(), product);
        System.out.println(redirectUrl);

        return "redirect:/product/list";
//        return rq.redirect(redirectUrl, null);
    }

    @DeleteMapping("/{id}/cancelBookmark")
    @PreAuthorize("isAuthenticated()")
    public String cancelBookmark(
            @PathVariable("id") long id,
            @RequestParam(value = "redirectUrl", defaultValue = "/") String redirectUrl
    ) {
        Product product = productService.findById(id).orElseThrow(() -> new GlobalException("400", "존재하지 않는 상품입니다."));
        productService.cancelBookmark(rq.getMember(), product);

        System.out.println(redirectUrl);

        return "redirect:/product/list";
    }
}
