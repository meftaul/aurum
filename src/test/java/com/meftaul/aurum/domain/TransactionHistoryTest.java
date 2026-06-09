package com.meftaul.aurum.domain;

import static com.meftaul.aurum.domain.TransactionHistoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.meftaul.aurum.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransactionHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionHistory.class);
        TransactionHistory transactionHistory1 = getTransactionHistorySample1();
        TransactionHistory transactionHistory2 = new TransactionHistory();
        assertThat(transactionHistory1).isNotEqualTo(transactionHistory2);

        transactionHistory2.setId(transactionHistory1.getId());
        assertThat(transactionHistory1).isEqualTo(transactionHistory2);

        transactionHistory2 = getTransactionHistorySample2();
        assertThat(transactionHistory1).isNotEqualTo(transactionHistory2);
    }
}
