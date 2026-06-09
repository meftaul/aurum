package com.meftaul.aurum.service.criteria;

import com.meftaul.aurum.domain.enumeration.TransactionStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
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
        this.id = other.id == null ? null : other.id.copy();
        this.voucherNo = other.voucherNo == null ? null : other.voucherNo.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.dateCreated = other.dateCreated == null ? null : other.dateCreated.copy();
        this.tag = other.tag == null ? null : other.tag.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.addedBy = other.addedBy == null ? null : other.addedBy.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TransactionHistoryCriteria copy() {
        return new TransactionHistoryCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getVoucherNo() {
        return voucherNo;
    }

    public StringFilter voucherNo() {
        if (voucherNo == null) {
            voucherNo = new StringFilter();
        }
        return voucherNo;
    }

    public void setVoucherNo(StringFilter voucherNo) {
        this.voucherNo = voucherNo;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public BigDecimalFilter amount() {
        if (amount == null) {
            amount = new BigDecimalFilter();
        }
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public InstantFilter getDateCreated() {
        return dateCreated;
    }

    public InstantFilter dateCreated() {
        if (dateCreated == null) {
            dateCreated = new InstantFilter();
        }
        return dateCreated;
    }

    public void setDateCreated(InstantFilter dateCreated) {
        this.dateCreated = dateCreated;
    }

    public TransactionStatusFilter getTag() {
        return tag;
    }

    public TransactionStatusFilter tag() {
        if (tag == null) {
            tag = new TransactionStatusFilter();
        }
        return tag;
    }

    public void setTag(TransactionStatusFilter tag) {
        this.tag = tag;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public LongFilter customerId() {
        if (customerId == null) {
            customerId = new LongFilter();
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public StringFilter getAddedBy() {
        return addedBy;
    }

    public StringFilter addedBy() {
        if (addedBy == null) {
            addedBy = new StringFilter();
        }
        return addedBy;
    }

    public void setAddedBy(StringFilter addedBy) {
        this.addedBy = addedBy;
    }

    public Boolean getDistinct() {
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
            (id != null ? "id=" + id + ", " : "") +
            (voucherNo != null ? "voucherNo=" + voucherNo + ", " : "") +
            (amount != null ? "amount=" + amount + ", " : "") +
            (dateCreated != null ? "dateCreated=" + dateCreated + ", " : "") +
            (tag != null ? "tag=" + tag + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (addedBy != null ? "addedBy=" + addedBy + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
