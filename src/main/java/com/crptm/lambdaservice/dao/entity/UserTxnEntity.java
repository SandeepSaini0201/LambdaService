package com.crptm.lambdaservice.dao.entity;

import com.crptm.lambdaservice.constants.UserTxnEntityConstants;
import com.crptm.lambdaservice.enums.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@NamedQueries({
        @NamedQuery(name = UserTxnEntityConstants.FIND_BY_STATUS_AND_BEFORE_INSTANT, query = "from user_txn as u where u.status in :status and u.txnDate < :instant"),
        @NamedQuery(name = UserTxnEntityConstants.UPDATE_STATUS_BY_STATUS_AND_BEFORE_INSTANT, query = "update user_txn as u set u.status = :newStatus where u.status = :oldStatus and u.txnDate < :instant"),
        @NamedQuery(name = UserTxnEntityConstants.UPDATE_STATUS_BY_ID, query = "update user_txn as u set u.status = :status where u.id = :txnId")
})
@Entity(name = "user_txn")
@Table(uniqueConstraints = {@UniqueConstraint(name = "merchant_user_piid_unique", columnNames = {"merchant_id", "user_id", "payment_intent_id"}), @UniqueConstraint(name = "merchant_user_vendor_piid_unique", columnNames = {"merchant_id", "user_id", "vendor_payment_intent_id"})}, indexes = {
        @Index(name = "merchant_piid_status_idx", columnList = "merchant_id,payment_intent_id,status"),
        @Index(name = "vendor_piid_status_idx", columnList = "vendor_payment_intent_id,status")
})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTxnEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "merchant_id", nullable = false)
    private String merchantId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "payment_intent_id")
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
    private EnumPaymentStatus status;

    @Column(name = "txn_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private EnumTxnType txnType;

    @Column(name = "vendor_name")
    @Enumerated(value = EnumType.STRING)
    private EnumPaymentVendor paymentVendor;

    @Column(name = "method_name")
    @Enumerated(value = EnumType.STRING)
    private EnumPaymentMethod paymentMethod;

    @Column(name = "future_usage")
    @Enumerated(value = EnumType.STRING)
    private EnumPaymentMethodFutureUsage futureUsage;

    @Column(name = "metadata")
    private String metadata;

    @Column(name = "return_url")
    private String returnURL;

    @Column(name = "txn_date", nullable = false)
    private Instant txnDate;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;

    @OneToMany(targetEntity = RefundTxnEntity.class, cascade = CascadeType.ALL, mappedBy = "userTxnEntity")
    private Set<RefundTxnEntity> refundTxnEntities;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}
