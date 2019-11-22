package com.meftaul.aurum.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.meftaul.aurum.domain.enumeration.Alloy;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;

/**
 * Criteria class for the {@link com.meftaul.aurum.domain.AurumService} entity. This class is used
 * in {@link com.meftaul.aurum.web.rest.AurumServiceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /aurum-services?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AurumServiceCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Alloy
     */
    public static class AlloyFilter extends Filter<Alloy> {

        public AlloyFilter() {
        }

        public AlloyFilter(AlloyFilter filter) {
            super(filter);
        }

        @Override
        public AlloyFilter copy() {
            return new AlloyFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter serviceType;

    private StringFilter itemName;

    private IntegerFilter quantity;

    private BigDecimalFilter weight;

    private BigDecimalFilter rate;

    private BigDecimalFilter amount;

    private StringFilter serviceName;

    private StringFilter karatType;

    private StringFilter expectedKaratType;

    private AlloyFilter addedAlloy;

    private BigDecimalFilter alloyQuantity;

    private BigDecimalFilter serviceCharge;

    private BigDecimalFilter freeCheck;

    private StringFilter hallMarkedText;

    private LongFilter voucherId;

    public AurumServiceCriteria(){
    }

    public AurumServiceCriteria(AurumServiceCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.serviceType = other.serviceType == null ? null : other.serviceType.copy();
        this.itemName = other.itemName == null ? null : other.itemName.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.weight = other.weight == null ? null : other.weight.copy();
        this.rate = other.rate == null ? null : other.rate.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.serviceName = other.serviceName == null ? null : other.serviceName.copy();
        this.karatType = other.karatType == null ? null : other.karatType.copy();
        this.expectedKaratType = other.expectedKaratType == null ? null : other.expectedKaratType.copy();
        this.addedAlloy = other.addedAlloy == null ? null : other.addedAlloy.copy();
        this.alloyQuantity = other.alloyQuantity == null ? null : other.alloyQuantity.copy();
        this.serviceCharge = other.serviceCharge == null ? null : other.serviceCharge.copy();
        this.freeCheck = other.freeCheck == null ? null : other.freeCheck.copy();
        this.hallMarkedText = other.hallMarkedText == null ? null : other.hallMarkedText.copy();
        this.voucherId = other.voucherId == null ? null : other.voucherId.copy();
    }

    @Override
    public AurumServiceCriteria copy() {
        return new AurumServiceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getServiceType() {
        return serviceType;
    }

    public void setServiceType(StringFilter serviceType) {
        this.serviceType = serviceType;
    }

    public StringFilter getItemName() {
        return itemName;
    }

    public void setItemName(StringFilter itemName) {
        this.itemName = itemName;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public BigDecimalFilter getWeight() {
        return weight;
    }

    public void setWeight(BigDecimalFilter weight) {
        this.weight = weight;
    }

    public BigDecimalFilter getRate() {
        return rate;
    }

    public void setRate(BigDecimalFilter rate) {
        this.rate = rate;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public StringFilter getServiceName() {
        return serviceName;
    }

    public void setServiceName(StringFilter serviceName) {
        this.serviceName = serviceName;
    }

    public StringFilter getKaratType() {
        return karatType;
    }

    public void setKaratType(StringFilter karatType) {
        this.karatType = karatType;
    }

    public StringFilter getExpectedKaratType() {
        return expectedKaratType;
    }

    public void setExpectedKaratType(StringFilter expectedKaratType) {
        this.expectedKaratType = expectedKaratType;
    }

    public AlloyFilter getAddedAlloy() {
        return addedAlloy;
    }

    public void setAddedAlloy(AlloyFilter addedAlloy) {
        this.addedAlloy = addedAlloy;
    }

    public BigDecimalFilter getAlloyQuantity() {
        return alloyQuantity;
    }

    public void setAlloyQuantity(BigDecimalFilter alloyQuantity) {
        this.alloyQuantity = alloyQuantity;
    }

    public BigDecimalFilter getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimalFilter serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public BigDecimalFilter getFreeCheck() {
        return freeCheck;
    }

    public void setFreeCheck(BigDecimalFilter freeCheck) {
        this.freeCheck = freeCheck;
    }

    public StringFilter getHallMarkedText() {
        return hallMarkedText;
    }

    public void setHallMarkedText(StringFilter hallMarkedText) {
        this.hallMarkedText = hallMarkedText;
    }

    public LongFilter getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(LongFilter voucherId) {
        this.voucherId = voucherId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AurumServiceCriteria that = (AurumServiceCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(serviceType, that.serviceType) &&
            Objects.equals(itemName, that.itemName) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(weight, that.weight) &&
            Objects.equals(rate, that.rate) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(serviceName, that.serviceName) &&
            Objects.equals(karatType, that.karatType) &&
            Objects.equals(expectedKaratType, that.expectedKaratType) &&
            Objects.equals(addedAlloy, that.addedAlloy) &&
            Objects.equals(alloyQuantity, that.alloyQuantity) &&
            Objects.equals(serviceCharge, that.serviceCharge) &&
            Objects.equals(freeCheck, that.freeCheck) &&
            Objects.equals(hallMarkedText, that.hallMarkedText) &&
            Objects.equals(voucherId, that.voucherId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        serviceType,
        itemName,
        quantity,
        weight,
        rate,
        amount,
        serviceName,
        karatType,
        expectedKaratType,
        addedAlloy,
        alloyQuantity,
        serviceCharge,
        freeCheck,
        hallMarkedText,
        voucherId
        );
    }

    @Override
    public String toString() {
        return "AurumServiceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (serviceType != null ? "serviceType=" + serviceType + ", " : "") +
                (itemName != null ? "itemName=" + itemName + ", " : "") +
                (quantity != null ? "quantity=" + quantity + ", " : "") +
                (weight != null ? "weight=" + weight + ", " : "") +
                (rate != null ? "rate=" + rate + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (serviceName != null ? "serviceName=" + serviceName + ", " : "") +
                (karatType != null ? "karatType=" + karatType + ", " : "") +
                (expectedKaratType != null ? "expectedKaratType=" + expectedKaratType + ", " : "") +
                (addedAlloy != null ? "addedAlloy=" + addedAlloy + ", " : "") +
                (alloyQuantity != null ? "alloyQuantity=" + alloyQuantity + ", " : "") +
                (serviceCharge != null ? "serviceCharge=" + serviceCharge + ", " : "") +
                (freeCheck != null ? "freeCheck=" + freeCheck + ", " : "") +
                (hallMarkedText != null ? "hallMarkedText=" + hallMarkedText + ", " : "") +
                (voucherId != null ? "voucherId=" + voucherId + ", " : "") +
            "}";
    }

}
