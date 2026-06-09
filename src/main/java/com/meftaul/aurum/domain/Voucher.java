package com.meftaul.aurum.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.meftaul.aurum.domain.enumeration.VoucherStatus;

/**
 * A Voucher.
 */
@Entity
@Table(name = "voucher")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Voucher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany(mappedBy = "voucher")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AurumService> aurumServices = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public Voucher voucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
        return this;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Voucher customerId(Long customerId) {
        this.customerId = customerId;
        return this;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getCalculatedTotalAmount() {
        return calculatedTotalAmount;
    }

    public Voucher calculatedTotalAmount(BigDecimal calculatedTotalAmount) {
        this.calculatedTotalAmount = calculatedTotalAmount;
        return this;
    }

    public void setCalculatedTotalAmount(BigDecimal calculatedTotalAmount) {
        this.calculatedTotalAmount = calculatedTotalAmount;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public Voucher vat(BigDecimal vat) {
        this.vat = vat;
        return this;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }

    public BigDecimal getDisountAmount() {
        return disountAmount;
    }

    public Voucher disountAmount(BigDecimal disountAmount) {
        this.disountAmount = disountAmount;
        return this;
    }

    public void setDisountAmount(BigDecimal disountAmount) {
        this.disountAmount = disountAmount;
    }

    public VoucherStatus getStatus() {
        return status;
    }

    public Voucher status(VoucherStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(VoucherStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalPayableAmount() {
        return totalPayableAmount;
    }

    public Voucher totalPayableAmount(BigDecimal totalPayableAmount) {
        this.totalPayableAmount = totalPayableAmount;
        return this;
    }

    public void setTotalPayableAmount(BigDecimal totalPayableAmount) {
        this.totalPayableAmount = totalPayableAmount;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public Voucher dateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public Voucher addedBy(String addedBy) {
        this.addedBy = addedBy;
        return this;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getBoxNumber() {
        return boxNumber;
    }

    public Voucher boxNumber(String boxNumber) {
        this.boxNumber = boxNumber;
        return this;
    }

    public void setBoxNumber(String boxNumber) {
        this.boxNumber = boxNumber;
    }

    public Instant getDeliveryDate() {
        return deliveryDate;
    }

    public Voucher deliveryDate(Instant deliveryDate) {
        this.deliveryDate = deliveryDate;
        return this;
    }

    public void setDeliveryDate(Instant deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Boolean isDeliveryStatus() {
        return deliveryStatus;
    }

    public Voucher deliveryStatus(Boolean deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
        return this;
    }

    public void setDeliveryStatus(Boolean deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Set<AurumService> getAurumServices() {
        return aurumServices;
    }

    public Voucher aurumServices(Set<AurumService> aurumServices) {
        this.aurumServices = aurumServices;
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

    public void setAurumServices(Set<AurumService> aurumServices) {
        this.aurumServices = aurumServices;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Voucher)) {
            return false;
        }
        return id != null && id.equals(((Voucher) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

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
            ", deliveryStatus='" + isDeliveryStatus() + "'" +
            "}";
    }
}
