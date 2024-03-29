package com.ll.mb.domain.member.member.entity;

import com.ll.mb.domain.book.book.entity.Book;
import com.ll.mb.domain.member.myBook.entity.MyBook;
import com.ll.mb.domain.product.product.entity.Product;
import com.ll.mb.global.jpa.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Setter
@Getter
@ToString(callSuper = true)
public class Member extends BaseTime {
    private String username;
    private String password;
    private String nickname;
    private long restCash;

    @OneToMany(mappedBy = "owner", cascade = ALL, orphanRemoval = true)
    @Builder.Default
    private List<MyBook> myBooks = new ArrayList<>();

    public void addMyBook(Book book) {
        MyBook myBook = MyBook.builder()
                .owner(this)
                .book(book)
                .build();

        myBooks.add(myBook);
    }

    public void removeMyBook(Book book) {
        myBooks.removeIf(myBook -> myBook.getBook().equals(book));
    }

    public boolean hasBook(Book book) {
        return myBooks
                .stream()
                .anyMatch(myBook -> myBook.getBook().equals(book));
    }

    public boolean has(Product product) {
        // product.getRelTypeCode()가 null이 아닌 경우에만 비교 진행
        if (product.getRelTypeCode() != null) {
            return switch (product.getRelTypeCode()) {
                case "book" -> product.getBook() != null && hasBook(product.getBook());
                default -> false;
            };
        }
        // product.getRelTypeCode()가 null인 경우, false 반환
        return false;
    }

    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));

        if (List.of("system", "admin").contains(username)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return authorities;
    }

    public boolean isAdmin() {
        return getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
