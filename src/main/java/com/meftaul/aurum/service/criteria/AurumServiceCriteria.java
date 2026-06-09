package com.meftaul.aurum.service.criteria;

import com.meftaul.aurum.domain.enumeration.Alloy;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
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
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.serviceType = other.optionalServiceType().map(StringFilter::copy).orElse(null);
        this.itemName = other.optionalItemName().map(StringFilter::copy).orElse(null);
        this.quantity = other.optionalQuantity().map(IntegerFilter::copy).orElse(null);
        this.weight = other.optionalWeight().map(BigDecimalFilter::copy).orElse(null);
        this.rate = other.optionalRate().map(BigDecimalFilter::copy).orElse(null);
        this.amount = other.optionalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.serviceName = other.optionalServiceName().map(StringFilter::copy).orElse(null);
        this.karatType = other.optionalKaratType().map(StringFilter::copy).orElse(null);
        this.expectedKaratType = other.optionalExpectedKaratType().map(StringFilter::copy).orElse(null);
        this.addedAlloy = other.optionalAddedAlloy().map(AlloyFilter::copy).orElse(null);
        this.alloyQuantity = other.optionalAlloyQuantity().map(BigDecimalFilter::copy).orElse(null);
        this.serviceCharge = other.optionalServiceCharge().map(BigDecimalFilter::copy).orElse(null);
        this.freeCheck = other.optionalFreeCheck().map(BigDecimalFilter::copy).orElse(null);
        this.hallMarkedText = other.optionalHallMarkedText().map(StringFilter::copy).orElse(null);
        this.weightOfFreeCheck = other.optionalWeightOfFreeCheck().map(StringFilter::copy).orElse(null);
        this.voucherId = other.optionalVoucherId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AurumServiceCriteria copy() {
        return new AurumServiceCriteria(this);
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

    public StringFilter getServiceType() {
        return serviceType;
    }

    public Optional<StringFilter> optionalServiceType() {
        return Optional.ofNullable(serviceType);
    }

    public StringFilter serviceType() {
        if (serviceType == null) {
            setServiceType(new StringFilter());
        }
        return serviceType;
    }

    public void setServiceType(StringFilter serviceType) {
        this.serviceType = serviceType;
    }

    public StringFilter getItemName() {
        return itemName;
    }

    public Optional<StringFilter> optionalItemName() {
        return Optional.ofNullable(itemName);
    }

    public StringFilter itemName() {
        if (itemName == null) {
            setItemName(new StringFilter());
        }
        return itemName;
    }

    public void setItemName(StringFilter itemName) {
        this.itemName = itemName;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public Optional<IntegerFilter> optionalQuantity() {
        return Optional.ofNullable(quantity);
    }

    public IntegerFilter quantity() {
        if (quantity == null) {
            setQuantity(new IntegerFilter());
        }
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public BigDecimalFilter getWeight() {
        return weight;
    }

    public Optional<BigDecimalFilter> optionalWeight() {
        return Optional.ofNullable(weight);
    }

    public BigDecimalFilter weight() {
        if (weight == null) {
            setWeight(new BigDecimalFilter());
        }
        return weight;
    }

    public void setWeight(BigDecimalFilter weight) {
        this.weight = weight;
    }

    public BigDecimalFilter getRate() {
        return rate;
    }

    public Optional<BigDecimalFilter> optionalRate() {
        return Optional.ofNullable(rate);
    }

    public BigDecimalFilter rate() {
        if (rate == null) {
            setRate(new BigDecimalFilter());
        }
        return rate;
    }

    public void setRate(BigDecimalFilter rate) {
        this.rate = rate;
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

    public StringFilter getServiceName() {
        return serviceName;
    }

    public Optional<StringFilter> optionalServiceName() {
        return Optional.ofNullable(serviceName);
    }

    public StringFilter serviceName() {
        if (serviceName == null) {
            setServiceName(new StringFilter());
        }
        return serviceName;
    }

    public void setServiceName(StringFilter serviceName) {
        this.serviceName = serviceName;
    }

    public StringFilter getKaratType() {
        return karatType;
    }

    public Optional<StringFilter> optionalKaratType() {
        return Optional.ofNullable(karatType);
    }

    public StringFilter karatType() {
        if (karatType == null) {
            setKaratType(new StringFilter());
        }
        return karatType;
    }

    public void setKaratType(StringFilter karatType) {
        this.karatType = karatType;
    }

    public StringFilter getExpectedKaratType() {
        return expectedKaratType;
    }

    public Optional<StringFilter> optionalExpectedKaratType() {
        return Optional.ofNullable(expectedKaratType);
    }

    public StringFilter expectedKaratType() {
        if (expectedKaratType == null) {
            setExpectedKaratType(new StringFilter());
        }
        return expectedKaratType;
    }

    public void setExpectedKaratType(StringFilter expectedKaratType) {
        this.expectedKaratType = expectedKaratType;
    }

    public AlloyFilter getAddedAlloy() {
        return addedAlloy;
    }

    public Optional<AlloyFilter> optionalAddedAlloy() {
        return Optional.ofNullable(addedAlloy);
    }

    public AlloyFilter addedAlloy() {
        if (addedAlloy == null) {
            setAddedAlloy(new AlloyFilter());
        }
        return addedAlloy;
    }

    public void setAddedAlloy(AlloyFilter addedAlloy) {
        this.addedAlloy = addedAlloy;
    }

    public BigDecimalFilter getAlloyQuantity() {
        return alloyQuantity;
    }

    public Optional<BigDecimalFilter> optionalAlloyQuantity() {
        return Optional.ofNullable(alloyQuantity);
    }

    public BigDecimalFilter alloyQuantity() {
        if (alloyQuantity == null) {
            setAlloyQuantity(new BigDecimalFilter());
        }
        return alloyQuantity;
    }

    public void setAlloyQuantity(BigDecimalFilter alloyQuantity) {
        this.alloyQuantity = alloyQuantity;
    }

    public BigDecimalFilter getServiceCharge() {
        return serviceCharge;
    }

    public Optional<BigDecimalFilter> optionalServiceCharge() {
        return Optional.ofNullable(serviceCharge);
    }

    public BigDecimalFilter serviceCharge() {
        if (serviceCharge == null) {
            setServiceCharge(new BigDecimalFilter());
        }
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimalFilter serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public BigDecimalFilter getFreeCheck() {
        return freeCheck;
    }

    public Optional<BigDecimalFilter> optionalFreeCheck() {
        return Optional.ofNullable(freeCheck);
    }

    public BigDecimalFilter freeCheck() {
        if (freeCheck == null) {
            setFreeCheck(new BigDecimalFilter());
        }
        return freeCheck;
    }

    public void setFreeCheck(BigDecimalFilter freeCheck) {
        this.freeCheck = freeCheck;
    }

    public StringFilter getHallMarkedText() {
        return hallMarkedText;
    }

    public Optional<StringFilter> optionalHallMarkedText() {
        return Optional.ofNullable(hallMarkedText);
    }

    public StringFilter hallMarkedText() {
        if (hallMarkedText == null) {
            setHallMarkedText(new StringFilter());
        }
        return hallMarkedText;
    }

    public void setHallMarkedText(StringFilter hallMarkedText) {
        this.hallMarkedText = hallMarkedText;
    }

    public StringFilter getWeightOfFreeCheck() {
        return weightOfFreeCheck;
    }

    public Optional<StringFilter> optionalWeightOfFreeCheck() {
        return Optional.ofNullable(weightOfFreeCheck);
    }

    public StringFilter weightOfFreeCheck() {
        if (weightOfFreeCheck == null) {
            setWeightOfFreeCheck(new StringFilter());
        }
        return weightOfFreeCheck;
    }

    public void setWeightOfFreeCheck(StringFilter weightOfFreeCheck) {
        this.weightOfFreeCheck = weightOfFreeCheck;
    }

    public LongFilter getVoucherId() {
        return voucherId;
    }

    public Optional<LongFilter> optionalVoucherId() {
        return Optional.ofNullable(voucherId);
    }

    public LongFilter voucherId() {
        if (voucherId == null) {
            setVoucherId(new LongFilter());
        }
        return voucherId;
    }

    public void setVoucherId(LongFilter voucherId) {
        this.voucherId = voucherId;
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
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalServiceType().map(f -> "serviceType=" + f + ", ").orElse("") +
            optionalItemName().map(f -> "itemName=" + f + ", ").orElse("") +
            optionalQuantity().map(f -> "quantity=" + f + ", ").orElse("") +
            optionalWeight().map(f -> "weight=" + f + ", ").orElse("") +
            optionalRate().map(f -> "rate=" + f + ", ").orElse("") +
            optionalAmount().map(f -> "amount=" + f + ", ").orElse("") +
            optionalServiceName().map(f -> "serviceName=" + f + ", ").orElse("") +
            optionalKaratType().map(f -> "karatType=" + f + ", ").orElse("") +
            optionalExpectedKaratType().map(f -> "expectedKaratType=" + f + ", ").orElse("") +
            optionalAddedAlloy().map(f -> "addedAlloy=" + f + ", ").orElse("") +
            optionalAlloyQuantity().map(f -> "alloyQuantity=" + f + ", ").orElse("") +
            optionalServiceCharge().map(f -> "serviceCharge=" + f + ", ").orElse("") +
            optionalFreeCheck().map(f -> "freeCheck=" + f + ", ").orElse("") +
            optionalHallMarkedText().map(f -> "hallMarkedText=" + f + ", ").orElse("") +
            optionalWeightOfFreeCheck().map(f -> "weightOfFreeCheck=" + f + ", ").orElse("") +
            optionalVoucherId().map(f -> "voucherId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
