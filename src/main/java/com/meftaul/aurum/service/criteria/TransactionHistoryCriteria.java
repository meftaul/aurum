package com.meftaul.aurum.service.criteria;

import com.meftaul.aurum.domain.enumeration.TransactionStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.meftaul.aurum.domain.TransactionHistory} entity. This class is used
 * in {@link com.meftaul.aurum.web.rest.TransactionHistoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transaction-histories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransactionHistoryCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TransactionStatus
     */
    public static class TransactionStatusFilter extends Filter<TransactionStatus> {

        public TransactionStatusFilter() {}

        public TransactionStatusFilter(TransactionStatusFilter filter) {
            super(filter);
        }

        @Override
        public TransactionStatusFilter copy() {
            return new TransactionStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter voucherNo;

    private BigDecimalFilter amount;

    private InstantFilter dateCreated;

    private TransactionStatusFilter tag;

    private LongFilter customerId;

    private StringFilter addedBy;

    private Boolean distinct;

    public TransactionHistoryCriteria() {}

    public TransactionHistoryCriteria(TransactionHistoryCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.voucherNo = other.optionalVoucherNo().map(StringFilter::copy).orElse(null);
        this.amount = other.optionalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.dateCreated = other.optionalDateCreated().map(InstantFilter::copy).orElse(null);
        this.tag = other.optionalTag().map(TransactionStatusFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(LongFilter::copy).orElse(null);
        this.addedBy = other.optionalAddedBy().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TransactionHistoryCriteria copy() {
        return new TransactionHistoryCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getVoucherNo() {
        return voucherNo;
    }

    public Optional<StringFilter> optionalVoucherNo() {
        return Optional.ofNullable(voucherNo);
    }

    public StringFilter voucherNo() {
        if (voucherNo == null) {
            setVoucherNo(new StringFilter());
        }
        return voucherNo;
    }

    public void setVoucherNo(StringFilter voucherNo) {
        this.voucherNo = voucherNo;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public Optional<BigDecimalFilter> optionalAmount() {
        return Optional.ofNullable(amount);
    }

    public BigDecimalFilter amount() {
        if (amount == null) {
            setAmount(new BigDecimalFilter());
        }
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public InstantFilter getDateCreated() {
        return dateCreated;
    }

    public Optional<InstantFilter> optionalDateCreated() {
        return Optional.ofNullable(dateCreated);
    }

    public InstantFilter dateCreated() {
        if (dateCreated == null) {
            setDateCreated(new InstantFilter());
        }
        return dateCreated;
    }

    public void setDateCreated(InstantFilter dateCreated) {
        this.dateCreated = dateCreated;
    }

    public TransactionStatusFilter getTag() {
        return tag;
    }

    public Optional<TransactionStatusFilter> optionalTag() {
        return Optional.ofNullable(tag);
    }

    public TransactionStatusFilter tag() {
        if (tag == null) {
            setTag(new TransactionStatusFilter());
        }
        return tag;
    }

    public void setTag(TransactionStatusFilter tag) {
        this.tag = tag;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public Optional<LongFilter> optionalCustomerId() {
        return Optional.ofNullable(customerId);
    }

    public LongFilter customerId() {
        if (customerId == null) {
            setCustomerId(new LongFilter());
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public StringFilter getAddedBy() {
        return addedBy;
    }

    public Optional<StringFilter> optionalAddedBy() {
        return Optional.ofNullable(addedBy);
    }

    public StringFilter addedBy() {
        if (addedBy == null) {
            setAddedBy(new StringFilter());
        }
        return addedBy;
    }

    public void setAddedBy(StringFilter addedBy) {
        this.addedBy = addedBy;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TransactionHistoryCriteria that = (TransactionHistoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(voucherNo, that.voucherNo) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(dateCreated, that.dateCreated) &&
            Objects.equals(tag, that.tag) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(addedBy, that.addedBy) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, voucherNo, amount, dateCreated, tag, customerId, addedBy, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionHistoryCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalVoucherNo().map(f -> "voucherNo=" + f + ", ").orElse("") +
            optionalAmount().map(f -> "amount=" + f + ", ").orElse("") +
            optionalDateCreated().map(f -> "dateCreated=" + f + ", ").orElse("") +
            optionalTag().map(f -> "tag=" + f + ", ").orElse("") +
            optionalCustomerId().map(f -> "customerId=" + f + ", ").orElse("") +
            optionalAddedBy().map(f -> "addedBy=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
