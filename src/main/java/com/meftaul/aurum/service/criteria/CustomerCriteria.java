package com.meftaul.aurum.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.meftaul.aurum.domain.Customer} entity. This class is used
 * in {@link com.meftaul.aurum.web.rest.CustomerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /customers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter phone;

    private StringFilter email;

    private StringFilter address;

    private LongFilter totalPoint;

    private StringFilter reference;

    private StringFilter customId;

    private Boolean distinct;

    public CustomerCriteria() {}

    public CustomerCriteria(CustomerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.totalPoint = other.totalPoint == null ? null : other.totalPoint.copy();
        this.reference = other.reference == null ? null : other.reference.copy();
        this.customId = other.customId == null ? null : other.customId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CustomerCriteria copy() {
        return new CustomerCriteria(this);
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

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address() {
        if (address == null) {
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public LongFilter getTotalPoint() {
        return totalPoint;
    }

    public LongFilter totalPoint() {
        if (totalPoint == null) {
            totalPoint = new LongFilter();
        }
        return totalPoint;
    }

    public void setTotalPoint(LongFilter totalPoint) {
        this.totalPoint = totalPoint;
    }

    public StringFilter getReference() {
        return reference;
    }

    public StringFilter reference() {
        if (reference == null) {
            reference = new StringFilter();
        }
        return reference;
    }

    public void setReference(StringFilter reference) {
        this.reference = reference;
    }

    public StringFilter getCustomId() {
        return customId;
    }

    public StringFilter customId() {
        if (customId == null) {
            customId = new StringFilter();
        }
        return customId;
    }

    public void setCustomId(StringFilter customId) {
        this.customId = customId;
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
        final CustomerCriteria that = (CustomerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(email, that.email) &&
            Objects.equals(address, that.address) &&
            Objects.equals(totalPoint, that.totalPoint) &&
            Objects.equals(reference, that.reference) &&
            Objects.equals(customId, that.customId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, phone, email, address, totalPoint, reference, customId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (totalPoint != null ? "totalPoint=" + totalPoint + ", " : "") +
            (reference != null ? "reference=" + reference + ", " : "") +
            (customId != null ? "customId=" + customId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
