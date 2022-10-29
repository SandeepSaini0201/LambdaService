package com.crptm.lambdaservice.dao.entity;

import com.crptm.lambdaservice.enums.EnumCurrency;
import com.crptm.lambdaservice.enums.EnumRefundStatus;
import com.crptm.lambdaservice.enums.EnumTxnType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity(name = "refund_txn")
@Table(indexes = {
        @Index(name = "merchant_user_payment_intent_id_idx", columnList = "merchant_id,user_id,payment_intent_id"),
        @Index(name = "status_txn_date_idx", columnList = "status,txn_date"),
        @Index(name = "user_txn_id_fk_idx", columnList = "user_txn_id")
})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundTxnEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "merchant_id", nullable = false)
    private String merchantId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "payment_intent_id", nullable = false)
    private String paymentIntentId;

    @Column(name = "vendor_payment_intent_id", nullable = false)
    private String vendorPaymentIntentId;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "currency", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private EnumCurrency currency;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private EnumRefundStatus status;

    @Column(name = "txn_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private EnumTxnType txnType;

    @Column(name = "metadata")
    private String metadata;

    @Column(name = "txn_date", nullable = false)
    private Instant txnDate;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;

    @ManyToOne(targetEntity = UserTxnEntity.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_txn_id", referencedColumnName = "id", nullable = false)
    private UserTxnEntity userTxnEntity;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}
