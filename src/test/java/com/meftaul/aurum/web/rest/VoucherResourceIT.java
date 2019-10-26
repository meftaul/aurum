package com.meftaul.aurum.web.rest;

import com.meftaul.aurum.AurumApp;
import com.meftaul.aurum.domain.Voucher;
import com.meftaul.aurum.repository.VoucherRepository;
import com.meftaul.aurum.service.VoucherService;
import com.meftaul.aurum.web.rest.errors.ExceptionTranslator;
import com.meftaul.aurum.service.dto.VoucherCriteria;
import com.meftaul.aurum.service.VoucherQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.meftaul.aurum.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.meftaul.aurum.domain.enumeration.VoucherStatus;
/**
 * Integration tests for the {@link VoucherResource} REST controller.
 */
@SpringBootTest(classes = AurumApp.class)
public class VoucherResourceIT {

    private static final String DEFAULT_VOUCHER_NO = "AAAAAAAAAA";
    private static final String UPDATED_VOUCHER_NO = "BBBBBBBBBB";

    private static final Long DEFAULT_CUSTOMER_ID = 1L;
    private static final Long UPDATED_CUSTOMER_ID = 2L;
    private static final Long SMALLER_CUSTOMER_ID = 1L - 1L;

    private static final BigDecimal DEFAULT_CALCULATED_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CALCULATED_TOTAL_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_CALCULATED_TOTAL_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_VAT = new BigDecimal(1);
    private static final BigDecimal UPDATED_VAT = new BigDecimal(2);
    private static final BigDecimal SMALLER_VAT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_DISOUNT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISOUNT_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_DISOUNT_AMOUNT = new BigDecimal(1 - 1);

    private static final VoucherStatus DEFAULT_STATUS = VoucherStatus.PAID;
    private static final VoucherStatus UPDATED_STATUS = VoucherStatus.DUE;

    private static final BigDecimal DEFAULT_TOTAL_PAYABLE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PAYABLE_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PAYABLE_AMOUNT = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_DATE_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_CREATED = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ADDED_BY = "AAAAAAAAAA";
    private static final String UPDATED_ADDED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_BOX_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_BOX_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DELIVERY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DELIVERY_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DELIVERY_DATE = LocalDate.ofEpochDay(-1L);

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private VoucherQueryService voucherQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restVoucherMockMvc;

    private Voucher voucher;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VoucherResource voucherResource = new VoucherResource(voucherService, voucherQueryService);
        this.restVoucherMockMvc = MockMvcBuilders.standaloneSetup(voucherResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Voucher createEntity(EntityManager em) {
        Voucher voucher = new Voucher()
            .voucherNo(DEFAULT_VOUCHER_NO)
            .customerId(DEFAULT_CUSTOMER_ID)
            .calculatedTotalAmount(DEFAULT_CALCULATED_TOTAL_AMOUNT)
            .vat(DEFAULT_VAT)
            .disountAmount(DEFAULT_DISOUNT_AMOUNT)
            .status(DEFAULT_STATUS)
            .totalPayableAmount(DEFAULT_TOTAL_PAYABLE_AMOUNT)
            .dateCreated(DEFAULT_DATE_CREATED)
            .addedBy(DEFAULT_ADDED_BY)
            .boxNumber(DEFAULT_BOX_NUMBER)
            .deliveryDate(DEFAULT_DELIVERY_DATE);
        return voucher;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Voucher createUpdatedEntity(EntityManager em) {
        Voucher voucher = new Voucher()
            .voucherNo(UPDATED_VOUCHER_NO)
            .customerId(UPDATED_CUSTOMER_ID)
            .calculatedTotalAmount(UPDATED_CALCULATED_TOTAL_AMOUNT)
            .vat(UPDATED_VAT)
            .disountAmount(UPDATED_DISOUNT_AMOUNT)
            .status(UPDATED_STATUS)
            .totalPayableAmount(UPDATED_TOTAL_PAYABLE_AMOUNT)
            .dateCreated(UPDATED_DATE_CREATED)
            .addedBy(UPDATED_ADDED_BY)
            .boxNumber(UPDATED_BOX_NUMBER)
            .deliveryDate(UPDATED_DELIVERY_DATE);
        return voucher;
    }

    @BeforeEach
    public void initTest() {
        voucher = createEntity(em);
    }

    @Test
    @Transactional
    public void createVoucher() throws Exception {
        int databaseSizeBeforeCreate = voucherRepository.findAll().size();

        // Create the Voucher
        restVoucherMockMvc.perform(post("/api/vouchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voucher)))
            .andExpect(status().isCreated());

        // Validate the Voucher in the database
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeCreate + 1);
        Voucher testVoucher = voucherList.get(voucherList.size() - 1);
        assertThat(testVoucher.getVoucherNo()).isEqualTo(DEFAULT_VOUCHER_NO);
        assertThat(testVoucher.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testVoucher.getCalculatedTotalAmount()).isEqualTo(DEFAULT_CALCULATED_TOTAL_AMOUNT);
        assertThat(testVoucher.getVat()).isEqualTo(DEFAULT_VAT);
        assertThat(testVoucher.getDisountAmount()).isEqualTo(DEFAULT_DISOUNT_AMOUNT);
        assertThat(testVoucher.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testVoucher.getTotalPayableAmount()).isEqualTo(DEFAULT_TOTAL_PAYABLE_AMOUNT);
        assertThat(testVoucher.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testVoucher.getAddedBy()).isEqualTo(DEFAULT_ADDED_BY);
        assertThat(testVoucher.getBoxNumber()).isEqualTo(DEFAULT_BOX_NUMBER);
        assertThat(testVoucher.getDeliveryDate()).isEqualTo(DEFAULT_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void createVoucherWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = voucherRepository.findAll().size();

        // Create the Voucher with an existing ID
        voucher.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVoucherMockMvc.perform(post("/api/vouchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voucher)))
            .andExpect(status().isBadRequest());

        // Validate the Voucher in the database
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCalculatedTotalAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = voucherRepository.findAll().size();
        // set the field null
        voucher.setCalculatedTotalAmount(null);

        // Create the Voucher, which fails.

        restVoucherMockMvc.perform(post("/api/vouchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voucher)))
            .andExpect(status().isBadRequest());

        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = voucherRepository.findAll().size();
        // set the field null
        voucher.setStatus(null);

        // Create the Voucher, which fails.

        restVoucherMockMvc.perform(post("/api/vouchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voucher)))
            .andExpect(status().isBadRequest());

        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalPayableAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = voucherRepository.findAll().size();
        // set the field null
        voucher.setTotalPayableAmount(null);

        // Create the Voucher, which fails.

        restVoucherMockMvc.perform(post("/api/vouchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voucher)))
            .andExpect(status().isBadRequest());

        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = voucherRepository.findAll().size();
        // set the field null
        voucher.setAddedBy(null);

        // Create the Voucher, which fails.

        restVoucherMockMvc.perform(post("/api/vouchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voucher)))
            .andExpect(status().isBadRequest());

        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVouchers() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList
        restVoucherMockMvc.perform(get("/api/vouchers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voucher.getId().intValue())))
            .andExpect(jsonPath("$.[*].voucherNo").value(hasItem(DEFAULT_VOUCHER_NO.toString())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID.intValue())))
            .andExpect(jsonPath("$.[*].calculatedTotalAmount").value(hasItem(DEFAULT_CALCULATED_TOTAL_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].vat").value(hasItem(DEFAULT_VAT.intValue())))
            .andExpect(jsonPath("$.[*].disountAmount").value(hasItem(DEFAULT_DISOUNT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].totalPayableAmount").value(hasItem(DEFAULT_TOTAL_PAYABLE_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY.toString())))
            .andExpect(jsonPath("$.[*].boxNumber").value(hasItem(DEFAULT_BOX_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getVoucher() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get the voucher
        restVoucherMockMvc.perform(get("/api/vouchers/{id}", voucher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(voucher.getId().intValue()))
            .andExpect(jsonPath("$.voucherNo").value(DEFAULT_VOUCHER_NO.toString()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID.intValue()))
            .andExpect(jsonPath("$.calculatedTotalAmount").value(DEFAULT_CALCULATED_TOTAL_AMOUNT.intValue()))
            .andExpect(jsonPath("$.vat").value(DEFAULT_VAT.intValue()))
            .andExpect(jsonPath("$.disountAmount").value(DEFAULT_DISOUNT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.totalPayableAmount").value(DEFAULT_TOTAL_PAYABLE_AMOUNT.intValue()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.addedBy").value(DEFAULT_ADDED_BY.toString()))
            .andExpect(jsonPath("$.boxNumber").value(DEFAULT_BOX_NUMBER.toString()))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllVouchersByVoucherNoIsEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where voucherNo equals to DEFAULT_VOUCHER_NO
        defaultVoucherShouldBeFound("voucherNo.equals=" + DEFAULT_VOUCHER_NO);

        // Get all the voucherList where voucherNo equals to UPDATED_VOUCHER_NO
        defaultVoucherShouldNotBeFound("voucherNo.equals=" + UPDATED_VOUCHER_NO);
    }

    @Test
    @Transactional
    public void getAllVouchersByVoucherNoIsInShouldWork() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where voucherNo in DEFAULT_VOUCHER_NO or UPDATED_VOUCHER_NO
        defaultVoucherShouldBeFound("voucherNo.in=" + DEFAULT_VOUCHER_NO + "," + UPDATED_VOUCHER_NO);

        // Get all the voucherList where voucherNo equals to UPDATED_VOUCHER_NO
        defaultVoucherShouldNotBeFound("voucherNo.in=" + UPDATED_VOUCHER_NO);
    }

    @Test
    @Transactional
    public void getAllVouchersByVoucherNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where voucherNo is not null
        defaultVoucherShouldBeFound("voucherNo.specified=true");

        // Get all the voucherList where voucherNo is null
        defaultVoucherShouldNotBeFound("voucherNo.specified=false");
    }

    @Test
    @Transactional
    public void getAllVouchersByCustomerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where customerId equals to DEFAULT_CUSTOMER_ID
        defaultVoucherShouldBeFound("customerId.equals=" + DEFAULT_CUSTOMER_ID);

        // Get all the voucherList where customerId equals to UPDATED_CUSTOMER_ID
        defaultVoucherShouldNotBeFound("customerId.equals=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    public void getAllVouchersByCustomerIdIsInShouldWork() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where customerId in DEFAULT_CUSTOMER_ID or UPDATED_CUSTOMER_ID
        defaultVoucherShouldBeFound("customerId.in=" + DEFAULT_CUSTOMER_ID + "," + UPDATED_CUSTOMER_ID);

        // Get all the voucherList where customerId equals to UPDATED_CUSTOMER_ID
        defaultVoucherShouldNotBeFound("customerId.in=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    public void getAllVouchersByCustomerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where customerId is not null
        defaultVoucherShouldBeFound("customerId.specified=true");

        // Get all the voucherList where customerId is null
        defaultVoucherShouldNotBeFound("customerId.specified=false");
    }

    @Test
    @Transactional
    public void getAllVouchersByCustomerIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where customerId is greater than or equal to DEFAULT_CUSTOMER_ID
        defaultVoucherShouldBeFound("customerId.greaterThanOrEqual=" + DEFAULT_CUSTOMER_ID);

        // Get all the voucherList where customerId is greater than or equal to UPDATED_CUSTOMER_ID
        defaultVoucherShouldNotBeFound("customerId.greaterThanOrEqual=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    public void getAllVouchersByCustomerIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where customerId is less than or equal to DEFAULT_CUSTOMER_ID
        defaultVoucherShouldBeFound("customerId.lessThanOrEqual=" + DEFAULT_CUSTOMER_ID);

        // Get all the voucherList where customerId is less than or equal to SMALLER_CUSTOMER_ID
        defaultVoucherShouldNotBeFound("customerId.lessThanOrEqual=" + SMALLER_CUSTOMER_ID);
    }

    @Test
    @Transactional
    public void getAllVouchersByCustomerIdIsLessThanSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where customerId is less than DEFAULT_CUSTOMER_ID
        defaultVoucherShouldNotBeFound("customerId.lessThan=" + DEFAULT_CUSTOMER_ID);

        // Get all the voucherList where customerId is less than UPDATED_CUSTOMER_ID
        defaultVoucherShouldBeFound("customerId.lessThan=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    public void getAllVouchersByCustomerIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where customerId is greater than DEFAULT_CUSTOMER_ID
        defaultVoucherShouldNotBeFound("customerId.greaterThan=" + DEFAULT_CUSTOMER_ID);

        // Get all the voucherList where customerId is greater than SMALLER_CUSTOMER_ID
        defaultVoucherShouldBeFound("customerId.greaterThan=" + SMALLER_CUSTOMER_ID);
    }


    @Test
    @Transactional
    public void getAllVouchersByCalculatedTotalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where calculatedTotalAmount equals to DEFAULT_CALCULATED_TOTAL_AMOUNT
        defaultVoucherShouldBeFound("calculatedTotalAmount.equals=" + DEFAULT_CALCULATED_TOTAL_AMOUNT);

        // Get all the voucherList where calculatedTotalAmount equals to UPDATED_CALCULATED_TOTAL_AMOUNT
        defaultVoucherShouldNotBeFound("calculatedTotalAmount.equals=" + UPDATED_CALCULATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllVouchersByCalculatedTotalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where calculatedTotalAmount in DEFAULT_CALCULATED_TOTAL_AMOUNT or UPDATED_CALCULATED_TOTAL_AMOUNT
        defaultVoucherShouldBeFound("calculatedTotalAmount.in=" + DEFAULT_CALCULATED_TOTAL_AMOUNT + "," + UPDATED_CALCULATED_TOTAL_AMOUNT);

        // Get all the voucherList where calculatedTotalAmount equals to UPDATED_CALCULATED_TOTAL_AMOUNT
        defaultVoucherShouldNotBeFound("calculatedTotalAmount.in=" + UPDATED_CALCULATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllVouchersByCalculatedTotalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where calculatedTotalAmount is not null
        defaultVoucherShouldBeFound("calculatedTotalAmount.specified=true");

        // Get all the voucherList where calculatedTotalAmount is null
        defaultVoucherShouldNotBeFound("calculatedTotalAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllVouchersByCalculatedTotalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where calculatedTotalAmount is greater than or equal to DEFAULT_CALCULATED_TOTAL_AMOUNT
        defaultVoucherShouldBeFound("calculatedTotalAmount.greaterThanOrEqual=" + DEFAULT_CALCULATED_TOTAL_AMOUNT);

        // Get all the voucherList where calculatedTotalAmount is greater than or equal to UPDATED_CALCULATED_TOTAL_AMOUNT
        defaultVoucherShouldNotBeFound("calculatedTotalAmount.greaterThanOrEqual=" + UPDATED_CALCULATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllVouchersByCalculatedTotalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where calculatedTotalAmount is less than or equal to DEFAULT_CALCULATED_TOTAL_AMOUNT
        defaultVoucherShouldBeFound("calculatedTotalAmount.lessThanOrEqual=" + DEFAULT_CALCULATED_TOTAL_AMOUNT);

        // Get all the voucherList where calculatedTotalAmount is less than or equal to SMALLER_CALCULATED_TOTAL_AMOUNT
        defaultVoucherShouldNotBeFound("calculatedTotalAmount.lessThanOrEqual=" + SMALLER_CALCULATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllVouchersByCalculatedTotalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where calculatedTotalAmount is less than DEFAULT_CALCULATED_TOTAL_AMOUNT
        defaultVoucherShouldNotBeFound("calculatedTotalAmount.lessThan=" + DEFAULT_CALCULATED_TOTAL_AMOUNT);

        // Get all the voucherList where calculatedTotalAmount is less than UPDATED_CALCULATED_TOTAL_AMOUNT
        defaultVoucherShouldBeFound("calculatedTotalAmount.lessThan=" + UPDATED_CALCULATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllVouchersByCalculatedTotalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where calculatedTotalAmount is greater than DEFAULT_CALCULATED_TOTAL_AMOUNT
        defaultVoucherShouldNotBeFound("calculatedTotalAmount.greaterThan=" + DEFAULT_CALCULATED_TOTAL_AMOUNT);

        // Get all the voucherList where calculatedTotalAmount is greater than SMALLER_CALCULATED_TOTAL_AMOUNT
        defaultVoucherShouldBeFound("calculatedTotalAmount.greaterThan=" + SMALLER_CALCULATED_TOTAL_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllVouchersByVatIsEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where vat equals to DEFAULT_VAT
        defaultVoucherShouldBeFound("vat.equals=" + DEFAULT_VAT);

        // Get all the voucherList where vat equals to UPDATED_VAT
        defaultVoucherShouldNotBeFound("vat.equals=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    public void getAllVouchersByVatIsInShouldWork() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where vat in DEFAULT_VAT or UPDATED_VAT
        defaultVoucherShouldBeFound("vat.in=" + DEFAULT_VAT + "," + UPDATED_VAT);

        // Get all the voucherList where vat equals to UPDATED_VAT
        defaultVoucherShouldNotBeFound("vat.in=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    public void getAllVouchersByVatIsNullOrNotNull() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where vat is not null
        defaultVoucherShouldBeFound("vat.specified=true");

        // Get all the voucherList where vat is null
        defaultVoucherShouldNotBeFound("vat.specified=false");
    }

    @Test
    @Transactional
    public void getAllVouchersByVatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where vat is greater than or equal to DEFAULT_VAT
        defaultVoucherShouldBeFound("vat.greaterThanOrEqual=" + DEFAULT_VAT);

        // Get all the voucherList where vat is greater than or equal to UPDATED_VAT
        defaultVoucherShouldNotBeFound("vat.greaterThanOrEqual=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    public void getAllVouchersByVatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where vat is less than or equal to DEFAULT_VAT
        defaultVoucherShouldBeFound("vat.lessThanOrEqual=" + DEFAULT_VAT);

        // Get all the voucherList where vat is less than or equal to SMALLER_VAT
        defaultVoucherShouldNotBeFound("vat.lessThanOrEqual=" + SMALLER_VAT);
    }

    @Test
    @Transactional
    public void getAllVouchersByVatIsLessThanSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where vat is less than DEFAULT_VAT
        defaultVoucherShouldNotBeFound("vat.lessThan=" + DEFAULT_VAT);

        // Get all the voucherList where vat is less than UPDATED_VAT
        defaultVoucherShouldBeFound("vat.lessThan=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    public void getAllVouchersByVatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where vat is greater than DEFAULT_VAT
        defaultVoucherShouldNotBeFound("vat.greaterThan=" + DEFAULT_VAT);

        // Get all the voucherList where vat is greater than SMALLER_VAT
        defaultVoucherShouldBeFound("vat.greaterThan=" + SMALLER_VAT);
    }


    @Test
    @Transactional
    public void getAllVouchersByDisountAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where disountAmount equals to DEFAULT_DISOUNT_AMOUNT
        defaultVoucherShouldBeFound("disountAmount.equals=" + DEFAULT_DISOUNT_AMOUNT);

        // Get all the voucherList where disountAmount equals to UPDATED_DISOUNT_AMOUNT
        defaultVoucherShouldNotBeFound("disountAmount.equals=" + UPDATED_DISOUNT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllVouchersByDisountAmountIsInShouldWork() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where disountAmount in DEFAULT_DISOUNT_AMOUNT or UPDATED_DISOUNT_AMOUNT
        defaultVoucherShouldBeFound("disountAmount.in=" + DEFAULT_DISOUNT_AMOUNT + "," + UPDATED_DISOUNT_AMOUNT);

        // Get all the voucherList where disountAmount equals to UPDATED_DISOUNT_AMOUNT
        defaultVoucherShouldNotBeFound("disountAmount.in=" + UPDATED_DISOUNT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllVouchersByDisountAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where disountAmount is not null
        defaultVoucherShouldBeFound("disountAmount.specified=true");

        // Get all the voucherList where disountAmount is null
        defaultVoucherShouldNotBeFound("disountAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllVouchersByDisountAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where disountAmount is greater than or equal to DEFAULT_DISOUNT_AMOUNT
        defaultVoucherShouldBeFound("disountAmount.greaterThanOrEqual=" + DEFAULT_DISOUNT_AMOUNT);

        // Get all the voucherList where disountAmount is greater than or equal to UPDATED_DISOUNT_AMOUNT
        defaultVoucherShouldNotBeFound("disountAmount.greaterThanOrEqual=" + UPDATED_DISOUNT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllVouchersByDisountAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where disountAmount is less than or equal to DEFAULT_DISOUNT_AMOUNT
        defaultVoucherShouldBeFound("disountAmount.lessThanOrEqual=" + DEFAULT_DISOUNT_AMOUNT);

        // Get all the voucherList where disountAmount is less than or equal to SMALLER_DISOUNT_AMOUNT
        defaultVoucherShouldNotBeFound("disountAmount.lessThanOrEqual=" + SMALLER_DISOUNT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllVouchersByDisountAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where disountAmount is less than DEFAULT_DISOUNT_AMOUNT
        defaultVoucherShouldNotBeFound("disountAmount.lessThan=" + DEFAULT_DISOUNT_AMOUNT);

        // Get all the voucherList where disountAmount is less than UPDATED_DISOUNT_AMOUNT
        defaultVoucherShouldBeFound("disountAmount.lessThan=" + UPDATED_DISOUNT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllVouchersByDisountAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where disountAmount is greater than DEFAULT_DISOUNT_AMOUNT
        defaultVoucherShouldNotBeFound("disountAmount.greaterThan=" + DEFAULT_DISOUNT_AMOUNT);

        // Get all the voucherList where disountAmount is greater than SMALLER_DISOUNT_AMOUNT
        defaultVoucherShouldBeFound("disountAmount.greaterThan=" + SMALLER_DISOUNT_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllVouchersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where status equals to DEFAULT_STATUS
        defaultVoucherShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the voucherList where status equals to UPDATED_STATUS
        defaultVoucherShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllVouchersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultVoucherShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the voucherList where status equals to UPDATED_STATUS
        defaultVoucherShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllVouchersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where status is not null
        defaultVoucherShouldBeFound("status.specified=true");

        // Get all the voucherList where status is null
        defaultVoucherShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllVouchersByTotalPayableAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where totalPayableAmount equals to DEFAULT_TOTAL_PAYABLE_AMOUNT
        defaultVoucherShouldBeFound("totalPayableAmount.equals=" + DEFAULT_TOTAL_PAYABLE_AMOUNT);

        // Get all the voucherList where totalPayableAmount equals to UPDATED_TOTAL_PAYABLE_AMOUNT
        defaultVoucherShouldNotBeFound("totalPayableAmount.equals=" + UPDATED_TOTAL_PAYABLE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllVouchersByTotalPayableAmountIsInShouldWork() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where totalPayableAmount in DEFAULT_TOTAL_PAYABLE_AMOUNT or UPDATED_TOTAL_PAYABLE_AMOUNT
        defaultVoucherShouldBeFound("totalPayableAmount.in=" + DEFAULT_TOTAL_PAYABLE_AMOUNT + "," + UPDATED_TOTAL_PAYABLE_AMOUNT);

        // Get all the voucherList where totalPayableAmount equals to UPDATED_TOTAL_PAYABLE_AMOUNT
        defaultVoucherShouldNotBeFound("totalPayableAmount.in=" + UPDATED_TOTAL_PAYABLE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllVouchersByTotalPayableAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where totalPayableAmount is not null
        defaultVoucherShouldBeFound("totalPayableAmount.specified=true");

        // Get all the voucherList where totalPayableAmount is null
        defaultVoucherShouldNotBeFound("totalPayableAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllVouchersByTotalPayableAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where totalPayableAmount is greater than or equal to DEFAULT_TOTAL_PAYABLE_AMOUNT
        defaultVoucherShouldBeFound("totalPayableAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_PAYABLE_AMOUNT);

        // Get all the voucherList where totalPayableAmount is greater than or equal to UPDATED_TOTAL_PAYABLE_AMOUNT
        defaultVoucherShouldNotBeFound("totalPayableAmount.greaterThanOrEqual=" + UPDATED_TOTAL_PAYABLE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllVouchersByTotalPayableAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where totalPayableAmount is less than or equal to DEFAULT_TOTAL_PAYABLE_AMOUNT
        defaultVoucherShouldBeFound("totalPayableAmount.lessThanOrEqual=" + DEFAULT_TOTAL_PAYABLE_AMOUNT);

        // Get all the voucherList where totalPayableAmount is less than or equal to SMALLER_TOTAL_PAYABLE_AMOUNT
        defaultVoucherShouldNotBeFound("totalPayableAmount.lessThanOrEqual=" + SMALLER_TOTAL_PAYABLE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllVouchersByTotalPayableAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where totalPayableAmount is less than DEFAULT_TOTAL_PAYABLE_AMOUNT
        defaultVoucherShouldNotBeFound("totalPayableAmount.lessThan=" + DEFAULT_TOTAL_PAYABLE_AMOUNT);

        // Get all the voucherList where totalPayableAmount is less than UPDATED_TOTAL_PAYABLE_AMOUNT
        defaultVoucherShouldBeFound("totalPayableAmount.lessThan=" + UPDATED_TOTAL_PAYABLE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllVouchersByTotalPayableAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where totalPayableAmount is greater than DEFAULT_TOTAL_PAYABLE_AMOUNT
        defaultVoucherShouldNotBeFound("totalPayableAmount.greaterThan=" + DEFAULT_TOTAL_PAYABLE_AMOUNT);

        // Get all the voucherList where totalPayableAmount is greater than SMALLER_TOTAL_PAYABLE_AMOUNT
        defaultVoucherShouldBeFound("totalPayableAmount.greaterThan=" + SMALLER_TOTAL_PAYABLE_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllVouchersByDateCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where dateCreated equals to DEFAULT_DATE_CREATED
        defaultVoucherShouldBeFound("dateCreated.equals=" + DEFAULT_DATE_CREATED);

        // Get all the voucherList where dateCreated equals to UPDATED_DATE_CREATED
        defaultVoucherShouldNotBeFound("dateCreated.equals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllVouchersByDateCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where dateCreated in DEFAULT_DATE_CREATED or UPDATED_DATE_CREATED
        defaultVoucherShouldBeFound("dateCreated.in=" + DEFAULT_DATE_CREATED + "," + UPDATED_DATE_CREATED);

        // Get all the voucherList where dateCreated equals to UPDATED_DATE_CREATED
        defaultVoucherShouldNotBeFound("dateCreated.in=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllVouchersByDateCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where dateCreated is not null
        defaultVoucherShouldBeFound("dateCreated.specified=true");

        // Get all the voucherList where dateCreated is null
        defaultVoucherShouldNotBeFound("dateCreated.specified=false");
    }

    @Test
    @Transactional
    public void getAllVouchersByDateCreatedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where dateCreated is greater than or equal to DEFAULT_DATE_CREATED
        defaultVoucherShouldBeFound("dateCreated.greaterThanOrEqual=" + DEFAULT_DATE_CREATED);

        // Get all the voucherList where dateCreated is greater than or equal to UPDATED_DATE_CREATED
        defaultVoucherShouldNotBeFound("dateCreated.greaterThanOrEqual=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllVouchersByDateCreatedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where dateCreated is less than or equal to DEFAULT_DATE_CREATED
        defaultVoucherShouldBeFound("dateCreated.lessThanOrEqual=" + DEFAULT_DATE_CREATED);

        // Get all the voucherList where dateCreated is less than or equal to SMALLER_DATE_CREATED
        defaultVoucherShouldNotBeFound("dateCreated.lessThanOrEqual=" + SMALLER_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllVouchersByDateCreatedIsLessThanSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where dateCreated is less than DEFAULT_DATE_CREATED
        defaultVoucherShouldNotBeFound("dateCreated.lessThan=" + DEFAULT_DATE_CREATED);

        // Get all the voucherList where dateCreated is less than UPDATED_DATE_CREATED
        defaultVoucherShouldBeFound("dateCreated.lessThan=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllVouchersByDateCreatedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where dateCreated is greater than DEFAULT_DATE_CREATED
        defaultVoucherShouldNotBeFound("dateCreated.greaterThan=" + DEFAULT_DATE_CREATED);

        // Get all the voucherList where dateCreated is greater than SMALLER_DATE_CREATED
        defaultVoucherShouldBeFound("dateCreated.greaterThan=" + SMALLER_DATE_CREATED);
    }


    @Test
    @Transactional
    public void getAllVouchersByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where addedBy equals to DEFAULT_ADDED_BY
        defaultVoucherShouldBeFound("addedBy.equals=" + DEFAULT_ADDED_BY);

        // Get all the voucherList where addedBy equals to UPDATED_ADDED_BY
        defaultVoucherShouldNotBeFound("addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    public void getAllVouchersByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where addedBy in DEFAULT_ADDED_BY or UPDATED_ADDED_BY
        defaultVoucherShouldBeFound("addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY);

        // Get all the voucherList where addedBy equals to UPDATED_ADDED_BY
        defaultVoucherShouldNotBeFound("addedBy.in=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    public void getAllVouchersByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where addedBy is not null
        defaultVoucherShouldBeFound("addedBy.specified=true");

        // Get all the voucherList where addedBy is null
        defaultVoucherShouldNotBeFound("addedBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllVouchersByBoxNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where boxNumber equals to DEFAULT_BOX_NUMBER
        defaultVoucherShouldBeFound("boxNumber.equals=" + DEFAULT_BOX_NUMBER);

        // Get all the voucherList where boxNumber equals to UPDATED_BOX_NUMBER
        defaultVoucherShouldNotBeFound("boxNumber.equals=" + UPDATED_BOX_NUMBER);
    }

    @Test
    @Transactional
    public void getAllVouchersByBoxNumberIsInShouldWork() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where boxNumber in DEFAULT_BOX_NUMBER or UPDATED_BOX_NUMBER
        defaultVoucherShouldBeFound("boxNumber.in=" + DEFAULT_BOX_NUMBER + "," + UPDATED_BOX_NUMBER);

        // Get all the voucherList where boxNumber equals to UPDATED_BOX_NUMBER
        defaultVoucherShouldNotBeFound("boxNumber.in=" + UPDATED_BOX_NUMBER);
    }

    @Test
    @Transactional
    public void getAllVouchersByBoxNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where boxNumber is not null
        defaultVoucherShouldBeFound("boxNumber.specified=true");

        // Get all the voucherList where boxNumber is null
        defaultVoucherShouldNotBeFound("boxNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllVouchersByDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where deliveryDate equals to DEFAULT_DELIVERY_DATE
        defaultVoucherShouldBeFound("deliveryDate.equals=" + DEFAULT_DELIVERY_DATE);

        // Get all the voucherList where deliveryDate equals to UPDATED_DELIVERY_DATE
        defaultVoucherShouldNotBeFound("deliveryDate.equals=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllVouchersByDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where deliveryDate in DEFAULT_DELIVERY_DATE or UPDATED_DELIVERY_DATE
        defaultVoucherShouldBeFound("deliveryDate.in=" + DEFAULT_DELIVERY_DATE + "," + UPDATED_DELIVERY_DATE);

        // Get all the voucherList where deliveryDate equals to UPDATED_DELIVERY_DATE
        defaultVoucherShouldNotBeFound("deliveryDate.in=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllVouchersByDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where deliveryDate is not null
        defaultVoucherShouldBeFound("deliveryDate.specified=true");

        // Get all the voucherList where deliveryDate is null
        defaultVoucherShouldNotBeFound("deliveryDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllVouchersByDeliveryDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where deliveryDate is greater than or equal to DEFAULT_DELIVERY_DATE
        defaultVoucherShouldBeFound("deliveryDate.greaterThanOrEqual=" + DEFAULT_DELIVERY_DATE);

        // Get all the voucherList where deliveryDate is greater than or equal to UPDATED_DELIVERY_DATE
        defaultVoucherShouldNotBeFound("deliveryDate.greaterThanOrEqual=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllVouchersByDeliveryDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where deliveryDate is less than or equal to DEFAULT_DELIVERY_DATE
        defaultVoucherShouldBeFound("deliveryDate.lessThanOrEqual=" + DEFAULT_DELIVERY_DATE);

        // Get all the voucherList where deliveryDate is less than or equal to SMALLER_DELIVERY_DATE
        defaultVoucherShouldNotBeFound("deliveryDate.lessThanOrEqual=" + SMALLER_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllVouchersByDeliveryDateIsLessThanSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where deliveryDate is less than DEFAULT_DELIVERY_DATE
        defaultVoucherShouldNotBeFound("deliveryDate.lessThan=" + DEFAULT_DELIVERY_DATE);

        // Get all the voucherList where deliveryDate is less than UPDATED_DELIVERY_DATE
        defaultVoucherShouldBeFound("deliveryDate.lessThan=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllVouchersByDeliveryDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where deliveryDate is greater than DEFAULT_DELIVERY_DATE
        defaultVoucherShouldNotBeFound("deliveryDate.greaterThan=" + DEFAULT_DELIVERY_DATE);

        // Get all the voucherList where deliveryDate is greater than SMALLER_DELIVERY_DATE
        defaultVoucherShouldBeFound("deliveryDate.greaterThan=" + SMALLER_DELIVERY_DATE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVoucherShouldBeFound(String filter) throws Exception {
        restVoucherMockMvc.perform(get("/api/vouchers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voucher.getId().intValue())))
            .andExpect(jsonPath("$.[*].voucherNo").value(hasItem(DEFAULT_VOUCHER_NO)))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID.intValue())))
            .andExpect(jsonPath("$.[*].calculatedTotalAmount").value(hasItem(DEFAULT_CALCULATED_TOTAL_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].vat").value(hasItem(DEFAULT_VAT.intValue())))
            .andExpect(jsonPath("$.[*].disountAmount").value(hasItem(DEFAULT_DISOUNT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].totalPayableAmount").value(hasItem(DEFAULT_TOTAL_PAYABLE_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)))
            .andExpect(jsonPath("$.[*].boxNumber").value(hasItem(DEFAULT_BOX_NUMBER)))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));

        // Check, that the count call also returns 1
        restVoucherMockMvc.perform(get("/api/vouchers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVoucherShouldNotBeFound(String filter) throws Exception {
        restVoucherMockMvc.perform(get("/api/vouchers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVoucherMockMvc.perform(get("/api/vouchers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingVoucher() throws Exception {
        // Get the voucher
        restVoucherMockMvc.perform(get("/api/vouchers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVoucher() throws Exception {
        // Initialize the database
        voucherService.save(voucher);

        int databaseSizeBeforeUpdate = voucherRepository.findAll().size();

        // Update the voucher
        Voucher updatedVoucher = voucherRepository.findById(voucher.getId()).get();
        // Disconnect from session so that the updates on updatedVoucher are not directly saved in db
        em.detach(updatedVoucher);
        updatedVoucher
            .voucherNo(UPDATED_VOUCHER_NO)
            .customerId(UPDATED_CUSTOMER_ID)
            .calculatedTotalAmount(UPDATED_CALCULATED_TOTAL_AMOUNT)
            .vat(UPDATED_VAT)
            .disountAmount(UPDATED_DISOUNT_AMOUNT)
            .status(UPDATED_STATUS)
            .totalPayableAmount(UPDATED_TOTAL_PAYABLE_AMOUNT)
            .dateCreated(UPDATED_DATE_CREATED)
            .addedBy(UPDATED_ADDED_BY)
            .boxNumber(UPDATED_BOX_NUMBER)
            .deliveryDate(UPDATED_DELIVERY_DATE);

        restVoucherMockMvc.perform(put("/api/vouchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVoucher)))
            .andExpect(status().isOk());

        // Validate the Voucher in the database
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeUpdate);
        Voucher testVoucher = voucherList.get(voucherList.size() - 1);
        assertThat(testVoucher.getVoucherNo()).isEqualTo(UPDATED_VOUCHER_NO);
        assertThat(testVoucher.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testVoucher.getCalculatedTotalAmount()).isEqualTo(UPDATED_CALCULATED_TOTAL_AMOUNT);
        assertThat(testVoucher.getVat()).isEqualTo(UPDATED_VAT);
        assertThat(testVoucher.getDisountAmount()).isEqualTo(UPDATED_DISOUNT_AMOUNT);
        assertThat(testVoucher.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testVoucher.getTotalPayableAmount()).isEqualTo(UPDATED_TOTAL_PAYABLE_AMOUNT);
        assertThat(testVoucher.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testVoucher.getAddedBy()).isEqualTo(UPDATED_ADDED_BY);
        assertThat(testVoucher.getBoxNumber()).isEqualTo(UPDATED_BOX_NUMBER);
        assertThat(testVoucher.getDeliveryDate()).isEqualTo(UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingVoucher() throws Exception {
        int databaseSizeBeforeUpdate = voucherRepository.findAll().size();

        // Create the Voucher

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoucherMockMvc.perform(put("/api/vouchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voucher)))
            .andExpect(status().isBadRequest());

        // Validate the Voucher in the database
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVoucher() throws Exception {
        // Initialize the database
        voucherService.save(voucher);

        int databaseSizeBeforeDelete = voucherRepository.findAll().size();

        // Delete the voucher
        restVoucherMockMvc.perform(delete("/api/vouchers/{id}", voucher.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Voucher.class);
        Voucher voucher1 = new Voucher();
        voucher1.setId(1L);
        Voucher voucher2 = new Voucher();
        voucher2.setId(voucher1.getId());
        assertThat(voucher1).isEqualTo(voucher2);
        voucher2.setId(2L);
        assertThat(voucher1).isNotEqualTo(voucher2);
        voucher1.setId(null);
        assertThat(voucher1).isNotEqualTo(voucher2);
    }
}
