package com.meftaul.aurum.service.criteria;

import com.meftaul.aurum.domain.enumeration.VoucherStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.meftaul.aurum.domain.Voucher} entity. This class is used
 * in {@link com.meftaul.aurum.web.rest.VoucherResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vouchers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VoucherCriteria implements Serializable, Criteria {

    /**
     * Class for filtering VoucherStatus
     */
    public static class VoucherStatusFilter extends Filter<VoucherStatus> {

        public VoucherStatusFilter() {}

        public VoucherStatusFilter(VoucherStatusFilter filter) {
            super(filter);
        }

        @Override
        public VoucherStatusFilter copy() {
            return new VoucherStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter voucherNo;

    private LongFilter customerId;

    private BigDecimalFilter calculatedTotalAmount;

    private BigDecimalFilter vat;

    private BigDecimalFilter disountAmount;

    private VoucherStatusFilter status;

    private BigDecimalFilter totalPayableAmount;

    private InstantFilter dateCreated;

    private StringFilter addedBy;

    private StringFilter boxNumber;

    private InstantFilter deliveryDate;

    private BooleanFilter deliveryStatus;

    private LongFilter aurumServiceId;

    private Boolean distinct;

    public VoucherCriteria() {}

    public VoucherCriteria(VoucherCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.voucherNo = other.optionalVoucherNo().map(StringFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(LongFilter::copy).orElse(null);
        this.calculatedTotalAmount = other.optionalCalculatedTotalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.vat = other.optionalVat().map(BigDecimalFilter::copy).orElse(null);
        this.disountAmount = other.optionalDisountAmount().map(BigDecimalFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(VoucherStatusFilter::copy).orElse(null);
        this.totalPayableAmount = other.optionalTotalPayableAmount().map(BigDecimalFilter::copy).orElse(null);
        this.dateCreated = other.optionalDateCreated().map(InstantFilter::copy).orElse(null);
        this.addedBy = other.optionalAddedBy().map(StringFilter::copy).orElse(null);
        this.boxNumber = other.optionalBoxNumber().map(StringFilter::copy).orElse(null);
        this.deliveryDate = other.optionalDeliveryDate().map(InstantFilter::copy).orElse(null);
        this.deliveryStatus = other.optionalDeliveryStatus().map(BooleanFilter::copy).orElse(null);
        this.aurumServiceId = other.optionalAurumServiceId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public VoucherCriteria copy() {
        return new VoucherCriteria(this);
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

    public BigDecimalFilter getCalculatedTotalAmount() {
        return calculatedTotalAmount;
    }

    public Optional<BigDecimalFilter> optionalCalculatedTotalAmount() {
        return Optional.ofNullable(calculatedTotalAmount);
    }

    public BigDecimalFilter calculatedTotalAmount() {
        if (calculatedTotalAmount == null) {
            setCalculatedTotalAmount(new BigDecimalFilter());
        }
        return calculatedTotalAmount;
    }

    public void setCalculatedTotalAmount(BigDecimalFilter calculatedTotalAmount) {
        this.calculatedTotalAmount = calculatedTotalAmount;
    }

    public BigDecimalFilter getVat() {
        return vat;
    }

    public Optional<BigDecimalFilter> optionalVat() {
        return Optional.ofNullable(vat);
    }

    public BigDecimalFilter vat() {
        if (vat == null) {
            setVat(new BigDecimalFilter());
        }
        return vat;
    }

    public void setVat(BigDecimalFilter vat) {
        this.vat = vat;
    }

    public BigDecimalFilter getDisountAmount() {
        return disountAmount;
    }

    public Optional<BigDecimalFilter> optionalDisountAmount() {
        return Optional.ofNullable(disountAmount);
    }

    public BigDecimalFilter disountAmount() {
        if (disountAmount == null) {
            setDisountAmount(new BigDecimalFilter());
        }
        return disountAmount;
    }

    public void setDisountAmount(BigDecimalFilter disountAmount) {
        this.disountAmount = disountAmount;
    }

    public VoucherStatusFilter getStatus() {
        return status;
    }

    public Optional<VoucherStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public VoucherStatusFilter status() {
        if (status == null) {
            setStatus(new VoucherStatusFilter());
        }
        return status;
    }

    public void setStatus(VoucherStatusFilter status) {
        this.status = status;
    }

    public BigDecimalFilter getTotalPayableAmount() {
        return totalPayableAmount;
    }

    public Optional<BigDecimalFilter> optionalTotalPayableAmount() {
        return Optional.ofNullable(totalPayableAmount);
    }

    public BigDecimalFilter totalPayableAmount() {
        if (totalPayableAmount == null) {
            setTotalPayableAmount(new BigDecimalFilter());
        }
        return totalPayableAmount;
    }

    public void setTotalPayableAmount(BigDecimalFilter totalPayableAmount) {
        this.totalPayableAmount = totalPayableAmount;
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

    public StringFilter getBoxNumber() {
        return boxNumber;
    }

    public Optional<StringFilter> optionalBoxNumber() {
        return Optional.ofNullable(boxNumber);
    }

    public StringFilter boxNumber() {
        if (boxNumber == null) {
            setBoxNumber(new StringFilter());
        }
        return boxNumber;
    }

    public void setBoxNumber(StringFilter boxNumber) {
        this.boxNumber = boxNumber;
    }

    public InstantFilter getDeliveryDate() {
        return deliveryDate;
    }

    public Optional<InstantFilter> optionalDeliveryDate() {
        return Optional.ofNullable(deliveryDate);
    }

    public InstantFilter deliveryDate() {
        if (deliveryDate == null) {
            setDeliveryDate(new InstantFilter());
        }
        return deliveryDate;
    }

    public void setDeliveryDate(InstantFilter deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public BooleanFilter getDeliveryStatus() {
        return deliveryStatus;
    }

    public Optional<BooleanFilter> optionalDeliveryStatus() {
        return Optional.ofNullable(deliveryStatus);
    }

    public BooleanFilter deliveryStatus() {
        if (deliveryStatus == null) {
            setDeliveryStatus(new BooleanFilter());
        }
        return deliveryStatus;
    }

    public void setDeliveryStatus(BooleanFilter deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public LongFilter getAurumServiceId() {
        return aurumServiceId;
    }

    public Optional<LongFilter> optionalAurumServiceId() {
        return Optional.ofNullable(aurumServiceId);
    }

    public LongFilter aurumServiceId() {
        if (aurumServiceId == null) {
            setAurumServiceId(new LongFilter());
        }
        return aurumServiceId;
    }

    public void setAurumServiceId(LongFilter aurumServiceId) {
        this.aurumServiceId = aurumServiceId;
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
        final VoucherCriteria that = (VoucherCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(voucherNo, that.voucherNo) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(calculatedTotalAmount, that.calculatedTotalAmount) &&
            Objects.equals(vat, that.vat) &&
            Objects.equals(disountAmount, that.disountAmount) &&
            Objects.equals(status, that.status) &&
            Objects.equals(totalPayableAmount, that.totalPayableAmount) &&
            Objects.equals(dateCreated, that.dateCreated) &&
            Objects.equals(addedBy, that.addedBy) &&
            Objects.equals(boxNumber, that.boxNumber) &&
            Objects.equals(deliveryDate, that.deliveryDate) &&
            Objects.equals(deliveryStatus, that.deliveryStatus) &&
            Objects.equals(aurumServiceId, that.aurumServiceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            voucherNo,
            customerId,
            calculatedTotalAmount,
            vat,
            disountAmount,
            status,
            totalPayableAmount,
            dateCreated,
            addedBy,
            boxNumber,
            deliveryDate,
            deliveryStatus,
            aurumServiceId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VoucherCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalVoucherNo().map(f -> "voucherNo=" + f + ", ").orElse("") +
            optionalCustomerId().map(f -> "customerId=" + f + ", ").orElse("") +
            optionalCalculatedTotalAmount().map(f -> "calculatedTotalAmount=" + f + ", ").orElse("") +
            optionalVat().map(f -> "vat=" + f + ", ").orElse("") +
            optionalDisountAmount().map(f -> "disountAmount=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalTotalPayableAmount().map(f -> "totalPayableAmount=" + f + ", ").orElse("") +
            optionalDateCreated().map(f -> "dateCreated=" + f + ", ").orElse("") +
            optionalAddedBy().map(f -> "addedBy=" + f + ", ").orElse("") +
            optionalBoxNumber().map(f -> "boxNumber=" + f + ", ").orElse("") +
            optionalDeliveryDate().map(f -> "deliveryDate=" + f + ", ").orElse("") +
            optionalDeliveryStatus().map(f -> "deliveryStatus=" + f + ", ").orElse("") +
            optionalAurumServiceId().map(f -> "aurumServiceId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
