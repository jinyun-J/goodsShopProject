package com.ll.mb.domain.product.product.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductBookmark is a Querydsl query type for ProductBookmark
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductBookmark extends EntityPathBase<ProductBookmark> {

    private static final long serialVersionUID = 1140638314L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductBookmark productBookmark = new QProductBookmark("productBookmark");

    public final com.ll.mb.global.jpa.QBaseEntity _super = new com.ll.mb.global.jpa.QBaseEntity(this);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final com.ll.mb.domain.member.member.entity.QMember member;

    public final QProduct product;

    public QProductBookmark(String variable) {
        this(ProductBookmark.class, forVariable(variable), INITS);
    }

    public QProductBookmark(Path<? extends ProductBookmark> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductBookmark(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductBookmark(PathMetadata metadata, PathInits inits) {
        this(ProductBookmark.class, metadata, inits);
    }

    public QProductBookmark(Class<? extends ProductBookmark> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.ll.mb.domain.member.member.entity.QMember(forProperty("member")) : null;
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

