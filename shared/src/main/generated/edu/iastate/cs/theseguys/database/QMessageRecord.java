package edu.iastate.cs.theseguys.database;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QMessageRecord is a Querydsl query type for MessageRecord
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QMessageRecord extends EntityPathBase<MessageRecord> {

    private static final long serialVersionUID = -510611911L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMessageRecord messageRecord = new QMessageRecord("messageRecord");

    public final QMessageRecord father;

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final SetPath<MessageRecord, QMessageRecord> leftChildren = this.<MessageRecord, QMessageRecord>createSet("leftChildren", MessageRecord.class, QMessageRecord.class, PathInits.DIRECT2);

    public final StringPath messageBody = createString("messageBody");

    public final QMessageRecord mother;

    public final SetPath<MessageRecord, QMessageRecord> rightChildren = this.<MessageRecord, QMessageRecord>createSet("rightChildren", MessageRecord.class, QMessageRecord.class, PathInits.DIRECT2);

    public final ArrayPath<byte[], Byte> signature = createArray("signature", byte[].class);

    public final DateTimePath<java.sql.Timestamp> timestamp = createDateTime("timestamp", java.sql.Timestamp.class);

    public final ComparablePath<java.util.UUID> userId = createComparable("userId", java.util.UUID.class);

    public QMessageRecord(String variable) {
        this(MessageRecord.class, forVariable(variable), INITS);
    }

    public QMessageRecord(Path<? extends MessageRecord> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QMessageRecord(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QMessageRecord(PathMetadata<?> metadata, PathInits inits) {
        this(MessageRecord.class, metadata, inits);
    }

    public QMessageRecord(Class<? extends MessageRecord> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.father = inits.isInitialized("father") ? new QMessageRecord(forProperty("father"), inits.get("father")) : null;
        this.mother = inits.isInitialized("mother") ? new QMessageRecord(forProperty("mother"), inits.get("mother")) : null;
    }

}

