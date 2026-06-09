package com.meftaul.aurum.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AurumServiceCriteriaTest {

    @Test
    void newAurumServiceCriteriaHasAllFiltersNullTest() {
        var aurumServiceCriteria = new AurumServiceCriteria();
        assertThat(aurumServiceCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void aurumServiceCriteriaFluentMethodsCreatesFiltersTest() {
        var aurumServiceCriteria = new AurumServiceCriteria();

        setAllFilters(aurumServiceCriteria);

        assertThat(aurumServiceCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void aurumServiceCriteriaCopyCreatesNullFilterTest() {
        var aurumServiceCriteria = new AurumServiceCriteria();
        var copy = aurumServiceCriteria.copy();

        assertThat(aurumServiceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(aurumServiceCriteria)
        );
    }

    @Test
    void aurumServiceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var aurumServiceCriteria = new AurumServiceCriteria();
        setAllFilters(aurumServiceCriteria);

        var copy = aurumServiceCriteria.copy();

        assertThat(aurumServiceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(aurumServiceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var aurumServiceCriteria = new AurumServiceCriteria();

        assertThat(aurumServiceCriteria).hasToString("AurumServiceCriteria{}");
    }

    private static void setAllFilters(AurumServiceCriteria aurumServiceCriteria) {
        aurumServiceCriteria.id();
        aurumServiceCriteria.serviceType();
        aurumServiceCriteria.itemName();
        aurumServiceCriteria.quantity();
        aurumServiceCriteria.weight();
        aurumServiceCriteria.rate();
        aurumServiceCriteria.amount();
        aurumServiceCriteria.serviceName();
        aurumServiceCriteria.karatType();
        aurumServiceCriteria.expectedKaratType();
        aurumServiceCriteria.addedAlloy();
        aurumServiceCriteria.alloyQuantity();
        aurumServiceCriteria.serviceCharge();
        aurumServiceCriteria.freeCheck();
        aurumServiceCriteria.hallMarkedText();
        aurumServiceCriteria.weightOfFreeCheck();
        aurumServiceCriteria.voucherId();
        aurumServiceCriteria.distinct();
    }

    private static Condition<AurumServiceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getServiceType()) &&
                condition.apply(criteria.getItemName()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getWeight()) &&
                condition.apply(criteria.getRate()) &&
                condition.apply(criteria.getAmount()) &&
                condition.apply(criteria.getServiceName()) &&
                condition.apply(criteria.getKaratType()) &&
                condition.apply(criteria.getExpectedKaratType()) &&
                condition.apply(criteria.getAddedAlloy()) &&
                condition.apply(criteria.getAlloyQuantity()) &&
                condition.apply(criteria.getServiceCharge()) &&
                condition.apply(criteria.getFreeCheck()) &&
                condition.apply(criteria.getHallMarkedText()) &&
                condition.apply(criteria.getWeightOfFreeCheck()) &&
                condition.apply(criteria.getVoucherId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AurumServiceCriteria> copyFiltersAre(
        AurumServiceCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getServiceType(), copy.getServiceType()) &&
                condition.apply(criteria.getItemName(), copy.getItemName()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getWeight(), copy.getWeight()) &&
                condition.apply(criteria.getRate(), copy.getRate()) &&
                condition.apply(criteria.getAmount(), copy.getAmount()) &&
                condition.apply(criteria.getServiceName(), copy.getServiceName()) &&
                condition.apply(criteria.getKaratType(), copy.getKaratType()) &&
                condition.apply(criteria.getExpectedKaratType(), copy.getExpectedKaratType()) &&
                condition.apply(criteria.getAddedAlloy(), copy.getAddedAlloy()) &&
                condition.apply(criteria.getAlloyQuantity(), copy.getAlloyQuantity()) &&
                condition.apply(criteria.getServiceCharge(), copy.getServiceCharge()) &&
                condition.apply(criteria.getFreeCheck(), copy.getFreeCheck()) &&
                condition.apply(criteria.getHallMarkedText(), copy.getHallMarkedText()) &&
                condition.apply(criteria.getWeightOfFreeCheck(), copy.getWeightOfFreeCheck()) &&
                condition.apply(criteria.getVoucherId(), copy.getVoucherId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
