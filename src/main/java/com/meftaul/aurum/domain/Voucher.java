package com.meftaul.aurum.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.meftaul.aurum.domain.enumeration.VoucherStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Voucher.
 */
@Entity
@Table(name = "voucher")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Voucher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "voucher_no")
    private String voucherNo;

    @Column(name = "customer_id")
    private Long customerId;

    @NotNull
    @Column(name = "calculated_total_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal calculatedTotalAmount;

    @Column(name = "vat", precision = 21, scale = 2)
    private BigDecimal vat;

    @Column(name = "disount_amount", precision = 21, scale = 2)
    private BigDecimal disountAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VoucherStatus status;

    @NotNull
    @Column(name = "total_payable_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalPayableAmount;

    @Column(name = "date_created")
    private Instant dateCreated;

    @NotNull
    @Column(name = "added_by", nullable = false)
    private String addedBy;

    @Column(name = "box_number")
    private String boxNumber;

    @Column(name = "delivery_date")
    private Instant deliveryDate;

    @Column(name = "delivery_status")
    private Boolean deliveryStatus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "voucher")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "voucher" }, allowSetters = true)
    private Set<AurumService> aurumServices = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Voucher id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVoucherNo() {
        return this.voucherNo;
    }

    public Voucher voucherNo(String voucherNo) {
        this.setVoucherNo(voucherNo);
        return this;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public Long getCustomerId() {
        return this.customerId;
    }

    public Voucher customerId(Long customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getCalculatedTotalAmount() {
        return this.calculatedTotalAmount;
    }

    public Voucher calculatedTotalAmount(BigDecimal calculatedTotalAmount) {
        this.setCalculatedTotalAmount(calculatedTotalAmount);
        return this;
    }

    public void setCalculatedTotalAmount(BigDecimal calculatedTotalAmount) {
        this.calculatedTotalAmount = calculatedTotalAmount;
    }

    public BigDecimal getVat() {
        return this.vat;
    }

    public Voucher vat(BigDecimal vat) {
        this.setVat(vat);
        return this;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }

    public BigDecimal getDisountAmount() {
        return this.disountAmount;
    }

    public Voucher disountAmount(BigDecimal disountAmount) {
        this.setDisountAmount(disountAmount);
        return this;
    }

    public void setDisountAmount(BigDecimal disountAmount) {
        this.disountAmount = disountAmount;
    }

    public VoucherStatus getStatus() {
        return this.status;
    }

    public Voucher status(VoucherStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(VoucherStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalPayableAmount() {
        return this.totalPayableAmount;
    }

    public Voucher totalPayableAmount(BigDecimal totalPayableAmount) {
        this.setTotalPayableAmount(totalPayableAmount);
        return this;
    }

    public void setTotalPayableAmount(BigDecimal totalPayableAmount) {
        this.totalPayableAmount = totalPayableAmount;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public Voucher dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getAddedBy() {
        return this.addedBy;
    }

    public Voucher addedBy(String addedBy) {
        this.setAddedBy(addedBy);
        return this;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getBoxNumber() {
        return this.boxNumber;
    }

    public Voucher boxNumber(String boxNumber) {
        this.setBoxNumber(boxNumber);
        return this;
    }

    public void setBoxNumber(String boxNumber) {
        this.boxNumber = boxNumber;
    }

    public Instant getDeliveryDate() {
        return this.deliveryDate;
    }

    public Voucher deliveryDate(Instant deliveryDate) {
        this.setDeliveryDate(deliveryDate);
        return this;
    }

    public void setDeliveryDate(Instant deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Boolean getDeliveryStatus() {
        return this.deliveryStatus;
    }

    public Voucher deliveryStatus(Boolean deliveryStatus) {
        this.setDeliveryStatus(deliveryStatus);
        return this;
    }

    public void setDeliveryStatus(Boolean deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Set<AurumService> getAurumServices() {
        return this.aurumServices;
    }

    public void setAurumServices(Set<AurumService> aurumServices) {
        if (this.aurumServices != null) {
            this.aurumServices.forEach(i -> i.setVoucher(null));
        }
        if (aurumServices != null) {
            aurumServices.forEach(i -> i.setVoucher(this));
        }
        this.aurumServices = aurumServices;
    }

    public Voucher aurumServices(Set<AurumService> aurumServices) {
        this.setAurumServices(aurumServices);
        return this;
    }

    public Voucher addAurumService(AurumService aurumService) {
        this.aurumServices.add(aurumService);
        aurumService.setVoucher(this);
        return this;
    }

    public Voucher removeAurumService(AurumService aurumService) {
        this.aurumServices.remove(aurumService);
        aurumService.setVoucher(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Voucher)) {
            return false;
        }
        return getId() != null && getId().equals(((Voucher) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Voucher{" +
            "id=" + getId() +
            ", voucherNo='" + getVoucherNo() + "'" +
            ", customerId=" + getCustomerId() +
            ", calculatedTotalAmount=" + getCalculatedTotalAmount() +
            ", vat=" + getVat() +
            ", disountAmount=" + getDisountAmount() +
            ", status='" + getStatus() + "'" +
            ", totalPayableAmount=" + getTotalPayableAmount() +
            ", dateCreated='" + getDateCreated() + "'" +
            ", addedBy='" + getAddedBy() + "'" +
            ", boxNumber='" + getBoxNumber() + "'" +
            ", deliveryDate='" + getDeliveryDate() + "'" +
            ", deliveryStatus='" + getDeliveryStatus() + "'" +
            "}";
    }
}
