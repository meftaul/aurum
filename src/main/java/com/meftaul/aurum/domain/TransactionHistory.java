package com.meftaul.aurum.domain;

import com.meftaul.aurum.domain.enumeration.TransactionStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TransactionHistory.
 */
@Entity
@Table(name = "transaction_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransactionHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "voucher_no", nullable = false)
    private String voucherNo;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(name = "date_created", nullable = false)
    private Instant dateCreated;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tag", nullable = false)
    private TransactionStatus tag;

    @NotNull
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @NotNull
    @Column(name = "added_by", nullable = false)
    private String addedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TransactionHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVoucherNo() {
        return this.voucherNo;
    }

    public TransactionHistory voucherNo(String voucherNo) {
        this.setVoucherNo(voucherNo);
        return this;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public TransactionHistory amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public TransactionHistory dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public TransactionStatus getTag() {
        return this.tag;
    }

    public TransactionHistory tag(TransactionStatus tag) {
        this.setTag(tag);
        return this;
    }

    public void setTag(TransactionStatus tag) {
        this.tag = tag;
    }

    public Long getCustomerId() {
        return this.customerId;
    }

    public TransactionHistory customerId(Long customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getAddedBy() {
        return this.addedBy;
    }

    public TransactionHistory addedBy(String addedBy) {
        this.setAddedBy(addedBy);
        return this;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionHistory)) {
            return false;
        }
        return id != null && id.equals(((TransactionHistory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionHistory{" +
            "id=" + getId() +
            ", voucherNo='" + getVoucherNo() + "'" +
            ", amount=" + getAmount() +
            ", dateCreated='" + getDateCreated() + "'" +
            ", tag='" + getTag() + "'" +
            ", customerId=" + getCustomerId() +
            ", addedBy='" + getAddedBy() + "'" +
            "}";
    }
}
