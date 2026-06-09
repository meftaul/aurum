package com.meftaul.aurum.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Karat.
 */
@Entity
@Table(name = "karat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Karat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "karat_type")
    private String karatType;

    @DecimalMin(value = "0")
    @Column(name = "purity_percent", precision = 21, scale = 2)
    private BigDecimal purityPercent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Karat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKaratType() {
        return this.karatType;
    }

    public Karat karatType(String karatType) {
        this.setKaratType(karatType);
        return this;
    }

    public void setKaratType(String karatType) {
        this.karatType = karatType;
    }

    public BigDecimal getPurityPercent() {
        return this.purityPercent;
    }

    public Karat purityPercent(BigDecimal purityPercent) {
        this.setPurityPercent(purityPercent);
        return this;
    }

    public void setPurityPercent(BigDecimal purityPercent) {
        this.purityPercent = purityPercent;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Karat)) {
            return false;
        }
        return getId() != null && getId().equals(((Karat) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Karat{" +
            "id=" + getId() +
            ", karatType='" + getKaratType() + "'" +
            ", purityPercent=" + getPurityPercent() +
            "}";
    }
}
