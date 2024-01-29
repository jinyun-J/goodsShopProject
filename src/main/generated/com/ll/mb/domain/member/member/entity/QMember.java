package com.ll.mb.domain.member.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -441103789L;

    public static final QMember member = new QMember("member1");

    public final com.ll.mb.global.jpa.QBaseTime _super = new com.ll.mb.global.jpa.QBaseTime(this);

    public final BooleanPath admin = createBoolean("admin");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final StringPath modelName = _super.modelName;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final ListPath<com.ll.mb.domain.member.myBook.entity.MyBook, com.ll.mb.domain.member.myBook.entity.QMyBook> myBooks = this.<com.ll.mb.domain.member.myBook.entity.MyBook, com.ll.mb.domain.member.myBook.entity.QMyBook>createList("myBooks", com.ll.mb.domain.member.myBook.entity.MyBook.class, com.ll.mb.domain.member.myBook.entity.QMyBook.class, PathInits.DIRECT2);

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final NumberPath<Long> restCash = createNumber("restCash", Long.class);

    public final StringPath username = createString("username");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

