package com.meftaul.aurum.service.criteria;

import com.meftaul.aurum.domain.enumeration.Alloy;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.meftaul.aurum.domain.AurumService} entity. This class is used
 * in {@link com.meftaul.aurum.web.rest.AurumServiceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /aurum-services?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AurumServiceCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Alloy
     */
    public static class AlloyFilter extends Filter<Alloy> {

        public AlloyFilter() {}

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

    private StringFilter weightOfFreeCheck;

    private LongFilter voucherId;

    private Boolean distinct;

    public AurumServiceCriteria() {}

    public AurumServiceCriteria(AurumServiceCriteria other) {
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
        this.weightOfFreeCheck = other.weightOfFreeCheck == null ? null : other.weightOfFreeCheck.copy();
        this.voucherId = other.voucherId == null ? null : other.voucherId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AurumServiceCriteria copy() {
        return new AurumServiceCriteria(this);
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

    public StringFilter getServiceType() {
        return serviceType;
    }

    public StringFilter serviceType() {
        if (serviceType == null) {
            serviceType = new StringFilter();
        }
        return serviceType;
    }

    public void setServiceType(StringFilter serviceType) {
        this.serviceType = serviceType;
    }

    public StringFilter getItemName() {
        return itemName;
    }

    public StringFilter itemName() {
        if (itemName == null) {
            itemName = new StringFilter();
        }
        return itemName;
    }

    public void setItemName(StringFilter itemName) {
        this.itemName = itemName;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public IntegerFilter quantity() {
        if (quantity == null) {
            quantity = new IntegerFilter();
        }
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public BigDecimalFilter getWeight() {
        return weight;
    }

    public BigDecimalFilter weight() {
        if (weight == null) {
            weight = new BigDecimalFilter();
        }
        return weight;
    }

    public void setWeight(BigDecimalFilter weight) {
        this.weight = weight;
    }

    public BigDecimalFilter getRate() {
        return rate;
    }

    public BigDecimalFilter rate() {
        if (rate == null) {
            rate = new BigDecimalFilter();
        }
        return rate;
    }

    public void setRate(BigDecimalFilter rate) {
        this.rate = rate;
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

    public StringFilter getServiceName() {
        return serviceName;
    }

    public StringFilter serviceName() {
        if (serviceName == null) {
            serviceName = new StringFilter();
        }
        return serviceName;
    }

    public void setServiceName(StringFilter serviceName) {
        this.serviceName = serviceName;
    }

    public StringFilter getKaratType() {
        return karatType;
    }

    public StringFilter karatType() {
        if (karatType == null) {
            karatType = new StringFilter();
        }
        return karatType;
    }

    public void setKaratType(StringFilter karatType) {
        this.karatType = karatType;
    }

    public StringFilter getExpectedKaratType() {
        return expectedKaratType;
    }

    public StringFilter expectedKaratType() {
        if (expectedKaratType == null) {
            expectedKaratType = new StringFilter();
        }
        return expectedKaratType;
    }

    public void setExpectedKaratType(StringFilter expectedKaratType) {
        this.expectedKaratType = expectedKaratType;
    }

    public AlloyFilter getAddedAlloy() {
        return addedAlloy;
    }

    public AlloyFilter addedAlloy() {
        if (addedAlloy == null) {
            addedAlloy = new AlloyFilter();
        }
        return addedAlloy;
    }

    public void setAddedAlloy(AlloyFilter addedAlloy) {
        this.addedAlloy = addedAlloy;
    }

    public BigDecimalFilter getAlloyQuantity() {
        return alloyQuantity;
    }

    public BigDecimalFilter alloyQuantity() {
        if (alloyQuantity == null) {
            alloyQuantity = new BigDecimalFilter();
        }
        return alloyQuantity;
    }

    public void setAlloyQuantity(BigDecimalFilter alloyQuantity) {
        this.alloyQuantity = alloyQuantity;
    }

    public BigDecimalFilter getServiceCharge() {
        return serviceCharge;
    }

    public BigDecimalFilter serviceCharge() {
        if (serviceCharge == null) {
            serviceCharge = new BigDecimalFilter();
        }
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimalFilter serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public BigDecimalFilter getFreeCheck() {
        return freeCheck;
    }

    public BigDecimalFilter freeCheck() {
        if (freeCheck == null) {
            freeCheck = new BigDecimalFilter();
        }
        return freeCheck;
    }

    public void setFreeCheck(BigDecimalFilter freeCheck) {
        this.freeCheck = freeCheck;
    }

    public StringFilter getHallMarkedText() {
        return hallMarkedText;
    }

    public StringFilter hallMarkedText() {
        if (hallMarkedText == null) {
            hallMarkedText = new StringFilter();
        }
        return hallMarkedText;
    }

    public void setHallMarkedText(StringFilter hallMarkedText) {
        this.hallMarkedText = hallMarkedText;
    }

    public StringFilter getWeightOfFreeCheck() {
        return weightOfFreeCheck;
    }

    public StringFilter weightOfFreeCheck() {
        if (weightOfFreeCheck == null) {
            weightOfFreeCheck = new StringFilter();
        }
        return weightOfFreeCheck;
    }

    public void setWeightOfFreeCheck(StringFilter weightOfFreeCheck) {
        this.weightOfFreeCheck = weightOfFreeCheck;
    }

    public LongFilter getVoucherId() {
        return voucherId;
    }

    public LongFilter voucherId() {
        if (voucherId == null) {
            voucherId = new LongFilter();
        }
        return voucherId;
    }

    public void setVoucherId(LongFilter voucherId) {
        this.voucherId = voucherId;
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
        final AurumServiceCriteria that = (AurumServiceCriteria) o;
        return (
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
            Objects.equals(weightOfFreeCheck, that.weightOfFreeCheck) &&
            Objects.equals(voucherId, that.voucherId) &&
            Objects.equals(distinct, that.distinct)
        );
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
            weightOfFreeCheck,
            voucherId,
            distinct
        );
    }

    // prettier-ignore
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
            (weightOfFreeCheck != null ? "weightOfFreeCheck=" + weightOfFreeCheck + ", " : "") +
            (voucherId != null ? "voucherId=" + voucherId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
