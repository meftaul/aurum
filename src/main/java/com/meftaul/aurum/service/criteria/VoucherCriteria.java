package com.meftaul.aurum.service.criteria;

import com.meftaul.aurum.domain.enumeration.VoucherStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
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
        this.id = other.id == null ? null : other.id.copy();
        this.voucherNo = other.voucherNo == null ? null : other.voucherNo.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.calculatedTotalAmount = other.calculatedTotalAmount == null ? null : other.calculatedTotalAmount.copy();
        this.vat = other.vat == null ? null : other.vat.copy();
        this.disountAmount = other.disountAmount == null ? null : other.disountAmount.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.totalPayableAmount = other.totalPayableAmount == null ? null : other.totalPayableAmount.copy();
        this.dateCreated = other.dateCreated == null ? null : other.dateCreated.copy();
        this.addedBy = other.addedBy == null ? null : other.addedBy.copy();
        this.boxNumber = other.boxNumber == null ? null : other.boxNumber.copy();
        this.deliveryDate = other.deliveryDate == null ? null : other.deliveryDate.copy();
        this.deliveryStatus = other.deliveryStatus == null ? null : other.deliveryStatus.copy();
        this.aurumServiceId = other.aurumServiceId == null ? null : other.aurumServiceId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public VoucherCriteria copy() {
        return new VoucherCriteria(this);
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

    public BigDecimalFilter getCalculatedTotalAmount() {
        return calculatedTotalAmount;
    }

    public BigDecimalFilter calculatedTotalAmount() {
        if (calculatedTotalAmount == null) {
            calculatedTotalAmount = new BigDecimalFilter();
        }
        return calculatedTotalAmount;
    }

    public void setCalculatedTotalAmount(BigDecimalFilter calculatedTotalAmount) {
        this.calculatedTotalAmount = calculatedTotalAmount;
    }

    public BigDecimalFilter getVat() {
        return vat;
    }

    public BigDecimalFilter vat() {
        if (vat == null) {
            vat = new BigDecimalFilter();
        }
        return vat;
    }

    public void setVat(BigDecimalFilter vat) {
        this.vat = vat;
    }

    public BigDecimalFilter getDisountAmount() {
        return disountAmount;
    }

    public BigDecimalFilter disountAmount() {
        if (disountAmount == null) {
            disountAmount = new BigDecimalFilter();
        }
        return disountAmount;
    }

    public void setDisountAmount(BigDecimalFilter disountAmount) {
        this.disountAmount = disountAmount;
    }

    public VoucherStatusFilter getStatus() {
        return status;
    }

    public VoucherStatusFilter status() {
        if (status == null) {
            status = new VoucherStatusFilter();
        }
        return status;
    }

    public void setStatus(VoucherStatusFilter status) {
        this.status = status;
    }

    public BigDecimalFilter getTotalPayableAmount() {
        return totalPayableAmount;
    }

    public BigDecimalFilter totalPayableAmount() {
        if (totalPayableAmount == null) {
            totalPayableAmount = new BigDecimalFilter();
        }
        return totalPayableAmount;
    }

    public void setTotalPayableAmount(BigDecimalFilter totalPayableAmount) {
        this.totalPayableAmount = totalPayableAmount;
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

    public StringFilter getBoxNumber() {
        return boxNumber;
    }

    public StringFilter boxNumber() {
        if (boxNumber == null) {
            boxNumber = new StringFilter();
        }
        return boxNumber;
    }

    public void setBoxNumber(StringFilter boxNumber) {
        this.boxNumber = boxNumber;
    }

    public InstantFilter getDeliveryDate() {
        return deliveryDate;
    }

    public InstantFilter deliveryDate() {
        if (deliveryDate == null) {
            deliveryDate = new InstantFilter();
        }
        return deliveryDate;
    }

    public void setDeliveryDate(InstantFilter deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public BooleanFilter getDeliveryStatus() {
        return deliveryStatus;
    }

    public BooleanFilter deliveryStatus() {
        if (deliveryStatus == null) {
            deliveryStatus = new BooleanFilter();
        }
        return deliveryStatus;
    }

    public void setDeliveryStatus(BooleanFilter deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public LongFilter getAurumServiceId() {
        return aurumServiceId;
    }

    public LongFilter aurumServiceId() {
        if (aurumServiceId == null) {
            aurumServiceId = new LongFilter();
        }
        return aurumServiceId;
    }

    public void setAurumServiceId(LongFilter aurumServiceId) {
        this.aurumServiceId = aurumServiceId;
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
            (id != null ? "id=" + id + ", " : "") +
            (voucherNo != null ? "voucherNo=" + voucherNo + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (calculatedTotalAmount != null ? "calculatedTotalAmount=" + calculatedTotalAmount + ", " : "") +
            (vat != null ? "vat=" + vat + ", " : "") +
            (disountAmount != null ? "disountAmount=" + disountAmount + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (totalPayableAmount != null ? "totalPayableAmount=" + totalPayableAmount + ", " : "") +
            (dateCreated != null ? "dateCreated=" + dateCreated + ", " : "") +
            (addedBy != null ? "addedBy=" + addedBy + ", " : "") +
            (boxNumber != null ? "boxNumber=" + boxNumber + ", " : "") +
            (deliveryDate != null ? "deliveryDate=" + deliveryDate + ", " : "") +
            (deliveryStatus != null ? "deliveryStatus=" + deliveryStatus + ", " : "") +
            (aurumServiceId != null ? "aurumServiceId=" + aurumServiceId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
