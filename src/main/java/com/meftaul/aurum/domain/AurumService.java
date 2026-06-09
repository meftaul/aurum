package com.meftaul.aurum.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.meftaul.aurum.domain.enumeration.Alloy;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AurumService.
 */
@Entity
@Table(name = "aurum_service")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AurumService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "service_type")
    private String serviceType;

    @Column(name = "item_name")
    private String itemName;

    @Min(value = 0)
    @Column(name = "quantity")
    private Integer quantity;

    @DecimalMin(value = "0")
    @Column(name = "weight", precision = 21, scale = 2)
    private BigDecimal weight;

    @DecimalMin(value = "0")
    @Column(name = "rate", precision = 21, scale = 2)
    private BigDecimal rate;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "karat_type")
    private String karatType;

    @Column(name = "expected_karat_type")
    private String expectedKaratType;

    @Enumerated(EnumType.STRING)
    @Column(name = "added_alloy")
    private Alloy addedAlloy;

    @DecimalMin(value = "0")
    @Column(name = "alloy_quantity", precision = 21, scale = 2)
    private BigDecimal alloyQuantity;

    @DecimalMin(value = "0")
    @Column(name = "service_charge", precision = 21, scale = 2)
    private BigDecimal serviceCharge;

    @DecimalMin(value = "0")
    @Column(name = "free_check", precision = 21, scale = 2)
    private BigDecimal freeCheck;

    @Column(name = "hall_marked_text")
    private String hallMarkedText;

    @Column(name = "weight_of_free_check")
    private String weightOfFreeCheck;

    @ManyToOne
    @JsonIgnoreProperties(value = { "aurumServices" }, allowSetters = true)
    private Voucher voucher;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AurumService id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceType() {
        return this.serviceType;
    }

    public AurumService serviceType(String serviceType) {
        this.setServiceType(serviceType);
        return this;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getItemName() {
        return this.itemName;
    }

    public AurumService itemName(String itemName) {
        this.setItemName(itemName);
        return this;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public AurumService quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getWeight() {
        return this.weight;
    }

    public AurumService weight(BigDecimal weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getRate() {
        return this.rate;
    }

    public AurumService rate(BigDecimal rate) {
        this.setRate(rate);
        return this;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public AurumService amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public AurumService serviceName(String serviceName) {
        this.setServiceName(serviceName);
        return this;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getKaratType() {
        return this.karatType;
    }

    public AurumService karatType(String karatType) {
        this.setKaratType(karatType);
        return this;
    }

    public void setKaratType(String karatType) {
        this.karatType = karatType;
    }

    public String getExpectedKaratType() {
        return this.expectedKaratType;
    }

    public AurumService expectedKaratType(String expectedKaratType) {
        this.setExpectedKaratType(expectedKaratType);
        return this;
    }

    public void setExpectedKaratType(String expectedKaratType) {
        this.expectedKaratType = expectedKaratType;
    }

    public Alloy getAddedAlloy() {
        return this.addedAlloy;
    }

    public AurumService addedAlloy(Alloy addedAlloy) {
        this.setAddedAlloy(addedAlloy);
        return this;
    }

    public void setAddedAlloy(Alloy addedAlloy) {
        this.addedAlloy = addedAlloy;
    }

    public BigDecimal getAlloyQuantity() {
        return this.alloyQuantity;
    }

    public AurumService alloyQuantity(BigDecimal alloyQuantity) {
        this.setAlloyQuantity(alloyQuantity);
        return this;
    }

    public void setAlloyQuantity(BigDecimal alloyQuantity) {
        this.alloyQuantity = alloyQuantity;
    }

    public BigDecimal getServiceCharge() {
        return this.serviceCharge;
    }

    public AurumService serviceCharge(BigDecimal serviceCharge) {
        this.setServiceCharge(serviceCharge);
        return this;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public BigDecimal getFreeCheck() {
        return this.freeCheck;
    }

    public AurumService freeCheck(BigDecimal freeCheck) {
        this.setFreeCheck(freeCheck);
        return this;
    }

    public void setFreeCheck(BigDecimal freeCheck) {
        this.freeCheck = freeCheck;
    }

    public String getHallMarkedText() {
        return this.hallMarkedText;
    }

    public AurumService hallMarkedText(String hallMarkedText) {
        this.setHallMarkedText(hallMarkedText);
        return this;
    }

    public void setHallMarkedText(String hallMarkedText) {
        this.hallMarkedText = hallMarkedText;
    }

    public String getWeightOfFreeCheck() {
        return this.weightOfFreeCheck;
    }

    public AurumService weightOfFreeCheck(String weightOfFreeCheck) {
        this.setWeightOfFreeCheck(weightOfFreeCheck);
        return this;
    }

    public void setWeightOfFreeCheck(String weightOfFreeCheck) {
        this.weightOfFreeCheck = weightOfFreeCheck;
    }

    public Voucher getVoucher() {
        return this.voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public AurumService voucher(Voucher voucher) {
        this.setVoucher(voucher);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AurumService)) {
            return false;
        }
        return id != null && id.equals(((AurumService) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AurumService{" +
            "id=" + getId() +
            ", serviceType='" + getServiceType() + "'" +
            ", itemName='" + getItemName() + "'" +
            ", quantity=" + getQuantity() +
            ", weight=" + getWeight() +
            ", rate=" + getRate() +
            ", amount=" + getAmount() +
            ", serviceName='" + getServiceName() + "'" +
            ", karatType='" + getKaratType() + "'" +
            ", expectedKaratType='" + getExpectedKaratType() + "'" +
            ", addedAlloy='" + getAddedAlloy() + "'" +
            ", alloyQuantity=" + getAlloyQuantity() +
            ", serviceCharge=" + getServiceCharge() +
            ", freeCheck=" + getFreeCheck() +
            ", hallMarkedText='" + getHallMarkedText() + "'" +
            ", weightOfFreeCheck='" + getWeightOfFreeCheck() + "'" +
            "}";
    }
}
