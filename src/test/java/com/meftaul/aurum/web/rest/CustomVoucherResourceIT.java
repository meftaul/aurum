package com.meftaul.aurum.web.rest;

import com.meftaul.aurum.AurumApp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Test class for the CustomVoucherResource REST controller.
 *
 * @see CustomVoucherResource
 */
@SpringBootTest(classes = AurumApp.class)
public class CustomVoucherResourceIT {

    private MockMvc restMockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        CustomVoucherResource customVoucherResource = new CustomVoucherResource(voucherService, customVoucherService);
        restMockMvc = MockMvcBuilders
            .standaloneSetup(customVoucherResource)
            .build();
    }

    /**
     * Test saveVoucher
     */
    @Test
    public void testSaveVoucher() throws Exception {
        restMockMvc.perform(post("/api/custom-voucher/save-voucher"))
            .andExpect(status().isOk());
    }
}
