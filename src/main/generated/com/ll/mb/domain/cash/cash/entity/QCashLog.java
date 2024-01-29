package com.ll.mb.domain.cash.cash.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCashLog is a Querydsl query type for CashLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCashLog extends EntityPathBase<CashLog> {

    private static final long serialVersionUID = -234234152L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCashLog cashLog = new QCashLog("cashLog");

    public final com.ll.mb.global.jpa.QBaseTime _super = new com.ll.mb.global.jpa.QBaseTime(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final EnumPath<CashLog.EvenType> eventType = createEnum("eventType", CashLog.EvenType.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final com.ll.mb.domain.member.member.entity.QMember member;

    //inherited
    public final StringPath modelName = _super.modelName;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public final NumberPath<Long> relId = createNumber("relId", Long.class);

    public final StringPath relTypeCode = createString("relTypeCode");

    public QCashLog(String variable) {
        this(CashLog.class, forVariable(variable), INITS);
    }

    public QCashLog(Path<? extends CashLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCashLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCashLog(PathMetadata metadata, PathInits inits) {
        this(CashLog.class, metadata, inits);
    }

    public QCashLog(Class<? extends CashLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.ll.mb.domain.member.member.entity.QMember(forProperty("member")) : null;
    }

}

