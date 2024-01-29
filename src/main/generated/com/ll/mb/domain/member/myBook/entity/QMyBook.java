package com.ll.mb.domain.member.myBook.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMyBook is a Querydsl query type for MyBook
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMyBook extends EntityPathBase<MyBook> {

    private static final long serialVersionUID = 1725790025L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMyBook myBook = new QMyBook("myBook");

    public final com.ll.mb.global.jpa.QBaseTime _super = new com.ll.mb.global.jpa.QBaseTime(this);

    public final com.ll.mb.domain.book.book.entity.QBook book;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final StringPath modelName = _super.modelName;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final com.ll.mb.domain.member.member.entity.QMember owner;

    public QMyBook(String variable) {
        this(MyBook.class, forVariable(variable), INITS);
    }

    public QMyBook(Path<? extends MyBook> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMyBook(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMyBook(PathMetadata metadata, PathInits inits) {
        this(MyBook.class, metadata, inits);
    }

    public QMyBook(Class<? extends MyBook> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new com.ll.mb.domain.book.book.entity.QBook(forProperty("book"), inits.get("book")) : null;
        this.owner = inits.isInitialized("owner") ? new com.ll.mb.domain.member.member.entity.QMember(forProperty("owner")) : null;
    }

}

