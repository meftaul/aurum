package com.meftaul.aurum.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TransactionHistoryCriteriaTest {

    @Test
    void newTransactionHistoryCriteriaHasAllFiltersNullTest() {
        var transactionHistoryCriteria = new TransactionHistoryCriteria();
        assertThat(transactionHistoryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void transactionHistoryCriteriaFluentMethodsCreatesFiltersTest() {
        var transactionHistoryCriteria = new TransactionHistoryCriteria();

        setAllFilters(transactionHistoryCriteria);

        assertThat(transactionHistoryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void transactionHistoryCriteriaCopyCreatesNullFilterTest() {
        var transactionHistoryCriteria = new TransactionHistoryCriteria();
        var copy = transactionHistoryCriteria.copy();

        assertThat(transactionHistoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(transactionHistoryCriteria)
        );
    }

    @Test
    void transactionHistoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var transactionHistoryCriteria = new TransactionHistoryCriteria();
        setAllFilters(transactionHistoryCriteria);

        var copy = transactionHistoryCriteria.copy();

        assertThat(transactionHistoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(transactionHistoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var transactionHistoryCriteria = new TransactionHistoryCriteria();

        assertThat(transactionHistoryCriteria).hasToString("TransactionHistoryCriteria{}");
    }

    private static void setAllFilters(TransactionHistoryCriteria transactionHistoryCriteria) {
        transactionHistoryCriteria.id();
        transactionHistoryCriteria.voucherNo();
        transactionHistoryCriteria.amount();
        transactionHistoryCriteria.dateCreated();
        transactionHistoryCriteria.tag();
        transactionHistoryCriteria.customerId();
        transactionHistoryCriteria.addedBy();
        transactionHistoryCriteria.distinct();
    }

    private static Condition<TransactionHistoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getVoucherNo()) &&
                condition.apply(criteria.getAmount()) &&
                condition.apply(criteria.getDateCreated()) &&
                condition.apply(criteria.getTag()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getAddedBy()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TransactionHistoryCriteria> copyFiltersAre(
        TransactionHistoryCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getVoucherNo(), copy.getVoucherNo()) &&
                condition.apply(criteria.getAmount(), copy.getAmount()) &&
                condition.apply(criteria.getDateCreated(), copy.getDateCreated()) &&
                condition.apply(criteria.getTag(), copy.getTag()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getAddedBy(), copy.getAddedBy()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
