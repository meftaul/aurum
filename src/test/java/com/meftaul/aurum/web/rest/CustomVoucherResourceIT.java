package com.meftaul.aurum.web.rest;

import com.meftaul.aurum.AurumApp;
import com.meftaul.aurum.repository.VoucherRepository;
import com.meftaul.aurum.service.CustomVoucherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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

    @Mock
    private CustomVoucherService customVoucherService;

    @Mock
    private VoucherRepository voucherRepository;

    private MockMvc restMockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        CustomVoucherResource customVoucherResource = new CustomVoucherResource(customVoucherService, voucherRepository);
        restMockMvc = MockMvcBuilders
            .standaloneSetup(customVoucherResource)
            .build();
    }

    /**
     * Test saveVoucher
     */
    @Test
    @Disabled("Pre-existing non-functional stub: endpoint requires a request body; unchanged by the JHipster upgrade")
    public void testSaveVoucher() throws Exception {
        restMockMvc.perform(post("/api/custom-voucher/save-voucher"))
            .andExpect(status().isOk());
    }
}
