package com.meftaul.aurum.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;

import com.meftaul.aurum.domain.enumeration.Alloy;

/**
 * A AurumService.
 */
@Entity
@Table(name = "aurum_service")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AurumService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne
    @JsonIgnoreProperties("aurumServices")
    private Voucher voucher;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceType() {
        return serviceType;
    }

    public AurumService serviceType(String serviceType) {
        this.serviceType = serviceType;
        return this;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getItemName() {
        return itemName;
    }

    public AurumService itemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public AurumService quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public AurumService weight(BigDecimal weight) {
        this.weight = weight;
        return this;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public AurumService rate(BigDecimal rate) {
        this.rate = rate;
        return this;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public AurumService amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getServiceName() {
        return serviceName;
    }

    public AurumService serviceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getKaratType() {
        return karatType;
    }

    public AurumService karatType(String karatType) {
        this.karatType = karatType;
        return this;
    }

    public void setKaratType(String karatType) {
        this.karatType = karatType;
    }

    public String getExpectedKaratType() {
        return expectedKaratType;
    }

    public AurumService expectedKaratType(String expectedKaratType) {
        this.expectedKaratType = expectedKaratType;
        return this;
    }

    public void setExpectedKaratType(String expectedKaratType) {
        this.expectedKaratType = expectedKaratType;
    }

    public Alloy getAddedAlloy() {
        return addedAlloy;
    }

    public AurumService addedAlloy(Alloy addedAlloy) {
        this.addedAlloy = addedAlloy;
        return this;
    }

    public void setAddedAlloy(Alloy addedAlloy) {
        this.addedAlloy = addedAlloy;
    }

    public BigDecimal getAlloyQuantity() {
        return alloyQuantity;
    }

    public AurumService alloyQuantity(BigDecimal alloyQuantity) {
        this.alloyQuantity = alloyQuantity;
        return this;
    }

    public void setAlloyQuantity(BigDecimal alloyQuantity) {
        this.alloyQuantity = alloyQuantity;
    }

    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public AurumService serviceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
        return this;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public BigDecimal getFreeCheck() {
        return freeCheck;
    }

    public AurumService freeCheck(BigDecimal freeCheck) {
        this.freeCheck = freeCheck;
        return this;
    }

    public void setFreeCheck(BigDecimal freeCheck) {
        this.freeCheck = freeCheck;
    }

    public String getHallMarkedText() {
        return hallMarkedText;
    }

    public AurumService hallMarkedText(String hallMarkedText) {
        this.hallMarkedText = hallMarkedText;
        return this;
    }

    public void setHallMarkedText(String hallMarkedText) {
        this.hallMarkedText = hallMarkedText;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public AurumService voucher(Voucher voucher) {
        this.voucher = voucher;
        return this;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

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
        return 31;
    }

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
            "}";
    }
}
