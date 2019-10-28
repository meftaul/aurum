package com.meftaul.aurum.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A Karat.
 */
@Entity
@Table(name = "karat")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Karat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "karat_type")
    private String karatType;

    @Column(name = "purity_percent", precision = 21, scale = 2)
    private BigDecimal purityPercent;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKaratType() {
        return karatType;
    }

    public Karat karatType(String karatType) {
        this.karatType = karatType;
        return this;
    }

    public void setKaratType(String karatType) {
        this.karatType = karatType;
    }

    public BigDecimal getPurityPercent() {
        return purityPercent;
    }

    public Karat purityPercent(BigDecimal purityPercent) {
        this.purityPercent = purityPercent;
        return this;
    }

    public void setPurityPercent(BigDecimal purityPercent) {
        this.purityPercent = purityPercent;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Karat)) {
            return false;
        }
        return id != null && id.equals(((Karat) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Karat{" +
            "id=" + getId() +
            ", karatType='" + getKaratType() + "'" +
            ", purityPercent=" + getPurityPercent() +
            "}";
    }
}
