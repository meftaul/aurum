package com.meftaul.aurum.web.rest;

import static com.meftaul.aurum.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.meftaul.aurum.IntegrationTest;
import com.meftaul.aurum.domain.AurumService;
import com.meftaul.aurum.domain.Voucher;
import com.meftaul.aurum.domain.enumeration.VoucherStatus;
import com.meftaul.aurum.repository.VoucherRepository;
import com.meftaul.aurum.service.criteria.VoucherCriteria;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VoucherResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VoucherResourceIT {

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

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ADDED_BY = "AAAAAAAAAA";
    private static final String UPDATED_ADDED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_BOX_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_BOX_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_DELIVERY_STATUS = false;
    private static final Boolean UPDATED_DELIVERY_STATUS = true;

    private static final String ENTITY_API_URL = "/api/vouchers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVoucherMockMvc;

    private Voucher voucher;

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
            .deliveryDate(DEFAULT_DELIVERY_DATE)
            .deliveryStatus(DEFAULT_DELIVERY_STATUS);
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
            .deliveryDate(UPDATED_DELIVERY_DATE)
            .deliveryStatus(UPDATED_DELIVERY_STATUS);
        return voucher;
    }

    @BeforeEach
    public void initTest() {
        voucher = createEntity(em);
    }

    @Test
    @Transactional
    void createVoucher() throws Exception {
        int databaseSizeBeforeCreate = voucherRepository.findAll().size();
        // Create the Voucher
        restVoucherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voucher)))
            .andExpect(status().isCreated());

        // Validate the Voucher in the database
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeCreate + 1);
        Voucher testVoucher = voucherList.get(voucherList.size() - 1);
        assertThat(testVoucher.getVoucherNo()).isEqualTo(DEFAULT_VOUCHER_NO);
        assertThat(testVoucher.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testVoucher.getCalculatedTotalAmount()).isEqualByComparingTo(DEFAULT_CALCULATED_TOTAL_AMOUNT);
        assertThat(testVoucher.getVat()).isEqualByComparingTo(DEFAULT_VAT);
        assertThat(testVoucher.getDisountAmount()).isEqualByComparingTo(DEFAULT_DISOUNT_AMOUNT);
        assertThat(testVoucher.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testVoucher.getTotalPayableAmount()).isEqualByComparingTo(DEFAULT_TOTAL_PAYABLE_AMOUNT);
        assertThat(testVoucher.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testVoucher.getAddedBy()).isEqualTo(DEFAULT_ADDED_BY);
        assertThat(testVoucher.getBoxNumber()).isEqualTo(DEFAULT_BOX_NUMBER);
        assertThat(testVoucher.getDeliveryDate()).isEqualTo(DEFAULT_DELIVERY_DATE);
        assertThat(testVoucher.getDeliveryStatus()).isEqualTo(DEFAULT_DELIVERY_STATUS);
    }

    @Test
    @Transactional
    void createVoucherWithExistingId() throws Exception {
        // Create the Voucher with an existing ID
        voucher.setId(1L);

        int databaseSizeBeforeCreate = voucherRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVoucherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voucher)))
            .andExpect(status().isBadRequest());

        // Validate the Voucher in the database
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCalculatedTotalAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = voucherRepository.findAll().size();
        // set the field null
        voucher.setCalculatedTotalAmount(null);

        // Create the Voucher, which fails.

        restVoucherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voucher)))
            .andExpect(status().isBadRequest());

        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = voucherRepository.findAll().size();
        // set the field null
        voucher.setStatus(null);

        // Create the Voucher, which fails.

        restVoucherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voucher)))
            .andExpect(status().isBadRequest());

        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPayableAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = voucherRepository.findAll().size();
        // set the field null
        voucher.setTotalPayableAmount(null);

        // Create the Voucher, which fails.

        restVoucherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voucher)))
            .andExpect(status().isBadRequest());

        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = voucherRepository.findAll().size();
        // set the field null
        voucher.setAddedBy(null);

        // Create the Voucher, which fails.

        restVoucherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voucher)))
            .andExpect(status().isBadRequest());

        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVouchers() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList
        restVoucherMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voucher.getId().intValue())))
            .andExpect(jsonPath("$.[*].voucherNo").value(hasItem(DEFAULT_VOUCHER_NO)))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID.intValue())))
            .andExpect(jsonPath("$.[*].calculatedTotalAmount").value(hasItem(sameNumber(DEFAULT_CALCULATED_TOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].vat").value(hasItem(sameNumber(DEFAULT_VAT))))
            .andExpect(jsonPath("$.[*].disountAmount").value(hasItem(sameNumber(DEFAULT_DISOUNT_AMOUNT))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].totalPayableAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_PAYABLE_AMOUNT))))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)))
            .andExpect(jsonPath("$.[*].boxNumber").value(hasItem(DEFAULT_BOX_NUMBER)))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryStatus").value(hasItem(DEFAULT_DELIVERY_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    void getVoucher() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get the voucher
        restVoucherMockMvc
            .perform(get(ENTITY_API_URL_ID, voucher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(voucher.getId().intValue()))
            .andExpect(jsonPath("$.voucherNo").value(DEFAULT_VOUCHER_NO))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID.intValue()))
            .andExpect(jsonPath("$.calculatedTotalAmount").value(sameNumber(DEFAULT_CALCULATED_TOTAL_AMOUNT)))
            .andExpect(jsonPath("$.vat").value(sameNumber(DEFAULT_VAT)))
            .andExpect(jsonPath("$.disountAmount").value(sameNumber(DEFAULT_DISOUNT_AMOUNT)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.totalPayableAmount").value(sameNumber(DEFAULT_TOTAL_PAYABLE_AMOUNT)))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.addedBy").value(DEFAULT_ADDED_BY))
            .andExpect(jsonPath("$.boxNumber").value(DEFAULT_BOX_NUMBER))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()))
            .andExpect(jsonPath("$.deliveryStatus").value(DEFAULT_DELIVERY_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    void getVouchersByIdFiltering() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        Long id = voucher.getId();

        defaultVoucherShouldBeFound("id.equals=" + id);
        defaultVoucherShouldNotBeFound("id.notEquals=" + id);

        defaultVoucherShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVoucherShouldNotBeFound("id.greaterThan=" + id);

        defaultVoucherShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVoucherShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVouchersByVoucherNoIsEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where voucherNo equals to DEFAULT_VOUCHER_NO
        defaultVoucherShouldBeFound("voucherNo.equals=" + DEFAULT_VOUCHER_NO);

        // Get all the voucherList where voucherNo equals to UPDATED_VOUCHER_NO
        defaultVoucherShouldNotBeFound("voucherNo.equals=" + UPDATED_VOUCHER_NO);
    }

    @Test
    @Transactional
    void getAllVouchersByVoucherNoIsInShouldWork() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where voucherNo in DEFAULT_VOUCHER_NO or UPDATED_VOUCHER_NO
        defaultVoucherShouldBeFound("voucherNo.in=" + DEFAULT_VOUCHER_NO + "," + UPDATED_VOUCHER_NO);

        // Get all the voucherList where voucherNo equals to UPDATED_VOUCHER_NO
        defaultVoucherShouldNotBeFound("voucherNo.in=" + UPDATED_VOUCHER_NO);
    }

    @Test
    @Transactional
    void getAllVouchersByVoucherNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where voucherNo is not null
        defaultVoucherShouldBeFound("voucherNo.specified=true");

        // Get all the voucherList where voucherNo is null
        defaultVoucherShouldNotBeFound("voucherNo.specified=false");
    }

    @Test
    @Transactional
    void getAllVouchersByVoucherNoContainsSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where voucherNo contains DEFAULT_VOUCHER_NO
        defaultVoucherShouldBeFound("voucherNo.contains=" + DEFAULT_VOUCHER_NO);

        // Get all the voucherList where voucherNo contains UPDATED_VOUCHER_NO
        defaultVoucherShouldNotBeFound("voucherNo.contains=" + UPDATED_VOUCHER_NO);
    }

    @Test
    @Transactional
    void getAllVouchersByVoucherNoNotContainsSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where voucherNo does not contain DEFAULT_VOUCHER_NO
        defaultVoucherShouldNotBeFound("voucherNo.doesNotContain=" + DEFAULT_VOUCHER_NO);

        // Get all the voucherList where voucherNo does not contain UPDATED_VOUCHER_NO
        defaultVoucherShouldBeFound("voucherNo.doesNotContain=" + UPDATED_VOUCHER_NO);
    }

    @Test
    @Transactional
    void getAllVouchersByCustomerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where customerId equals to DEFAULT_CUSTOMER_ID
        defaultVoucherShouldBeFound("customerId.equals=" + DEFAULT_CUSTOMER_ID);

        // Get all the voucherList where customerId equals to UPDATED_CUSTOMER_ID
        defaultVoucherShouldNotBeFound("customerId.equals=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllVouchersByCustomerIdIsInShouldWork() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where customerId in DEFAULT_CUSTOMER_ID or UPDATED_CUSTOMER_ID
        defaultVoucherShouldBeFound("customerId.in=" + DEFAULT_CUSTOMER_ID + "," + UPDATED_CUSTOMER_ID);

        // Get all the voucherList where customerId equals to UPDATED_CUSTOMER_ID
        defaultVoucherShouldNotBeFound("customerId.in=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllVouchersByCustomerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where customerId is not null
        defaultVoucherShouldBeFound("customerId.specified=true");

        // Get all the voucherList where customerId is null
        defaultVoucherShouldNotBeFound("customerId.specified=false");
    }

    @Test
    @Transactional
    void getAllVouchersByCustomerIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where customerId is greater than or equal to DEFAULT_CUSTOMER_ID
        defaultVoucherShouldBeFound("customerId.greaterThanOrEqual=" + DEFAULT_CUSTOMER_ID);

        // Get all the voucherList where customerId is greater than or equal to UPDATED_CUSTOMER_ID
        defaultVoucherShouldNotBeFound("customerId.greaterThanOrEqual=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllVouchersByCustomerIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where customerId is less than or equal to DEFAULT_CUSTOMER_ID
        defaultVoucherShouldBeFound("customerId.lessThanOrEqual=" + DEFAULT_CUSTOMER_ID);

        // Get all the voucherList where customerId is less than or equal to SMALLER_CUSTOMER_ID
        defaultVoucherShouldNotBeFound("customerId.lessThanOrEqual=" + SMALLER_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllVouchersByCustomerIdIsLessThanSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where customerId is less than DEFAULT_CUSTOMER_ID
        defaultVoucherShouldNotBeFound("customerId.lessThan=" + DEFAULT_CUSTOMER_ID);

        // Get all the voucherList where customerId is less than UPDATED_CUSTOMER_ID
        defaultVoucherShouldBeFound("customerId.lessThan=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllVouchersByCustomerIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where customerId is greater than DEFAULT_CUSTOMER_ID
        defaultVoucherShouldNotBeFound("customerId.greaterThan=" + DEFAULT_CUSTOMER_ID);

        // Get all the voucherList where customerId is greater than SMALLER_CUSTOMER_ID
        defaultVoucherShouldBeFound("customerId.greaterThan=" + SMALLER_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllVouchersByCalculatedTotalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where calculatedTotalAmount equals to DEFAULT_CALCULATED_TOTAL_AMOUNT
        defaultVoucherShouldBeFound("calculatedTotalAmount.equals=" + DEFAULT_CALCULATED_TOTAL_AMOUNT);

        // Get all the voucherList where calculatedTotalAmount equals to UPDATED_CALCULATED_TOTAL_AMOUNT
        defaultVoucherShouldNotBeFound("calculatedTotalAmount.equals=" + UPDATED_CALCULATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllVouchersByCalculatedTotalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where calculatedTotalAmount in DEFAULT_CALCULATED_TOTAL_AMOUNT or UPDATED_CALCULATED_TOTAL_AMOUNT
        defaultVoucherShouldBeFound("calculatedTotalAmount.in=" + DEFAULT_CALCULATED_TOTAL_AMOUNT + "," + UPDATED_CALCULATED_TOTAL_AMOUNT);

        // Get all the voucherList where calculatedTotalAmount equals to UPDATED_CALCULATED_TOTAL_AMOUNT
        defaultVoucherShouldNotBeFound("calculatedTotalAmount.in=" + UPDATED_CALCULATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllVouchersByCalculatedTotalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where calculatedTotalAmount is not null
        defaultVoucherShouldBeFound("calculatedTotalAmount.specified=true");

        // Get all the voucherList where calculatedTotalAmount is null
        defaultVoucherShouldNotBeFound("calculatedTotalAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllVouchersByCalculatedTotalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where calculatedTotalAmount is greater than or equal to DEFAULT_CALCULATED_TOTAL_AMOUNT
        defaultVoucherShouldBeFound("calculatedTotalAmount.greaterThanOrEqual=" + DEFAULT_CALCULATED_TOTAL_AMOUNT);

        // Get all the voucherList where calculatedTotalAmount is greater than or equal to UPDATED_CALCULATED_TOTAL_AMOUNT
        defaultVoucherShouldNotBeFound("calculatedTotalAmount.greaterThanOrEqual=" + UPDATED_CALCULATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllVouchersByCalculatedTotalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where calculatedTotalAmount is less than or equal to DEFAULT_CALCULATED_TOTAL_AMOUNT
        defaultVoucherShouldBeFound("calculatedTotalAmount.lessThanOrEqual=" + DEFAULT_CALCULATED_TOTAL_AMOUNT);

        // Get all the voucherList where calculatedTotalAmount is less than or equal to SMALLER_CALCULATED_TOTAL_AMOUNT
        defaultVoucherShouldNotBeFound("calculatedTotalAmount.lessThanOrEqual=" + SMALLER_CALCULATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllVouchersByCalculatedTotalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where calculatedTotalAmount is less than DEFAULT_CALCULATED_TOTAL_AMOUNT
        defaultVoucherShouldNotBeFound("calculatedTotalAmount.lessThan=" + DEFAULT_CALCULATED_TOTAL_AMOUNT);

        // Get all the voucherList where calculatedTotalAmount is less than UPDATED_CALCULATED_TOTAL_AMOUNT
        defaultVoucherShouldBeFound("calculatedTotalAmount.lessThan=" + UPDATED_CALCULATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllVouchersByCalculatedTotalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where calculatedTotalAmount is greater than DEFAULT_CALCULATED_TOTAL_AMOUNT
        defaultVoucherShouldNotBeFound("calculatedTotalAmount.greaterThan=" + DEFAULT_CALCULATED_TOTAL_AMOUNT);

        // Get all the voucherList where calculatedTotalAmount is greater than SMALLER_CALCULATED_TOTAL_AMOUNT
        defaultVoucherShouldBeFound("calculatedTotalAmount.greaterThan=" + SMALLER_CALCULATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllVouchersByVatIsEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where vat equals to DEFAULT_VAT
        defaultVoucherShouldBeFound("vat.equals=" + DEFAULT_VAT);

        // Get all the voucherList where vat equals to UPDATED_VAT
        defaultVoucherShouldNotBeFound("vat.equals=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    void getAllVouchersByVatIsInShouldWork() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where vat in DEFAULT_VAT or UPDATED_VAT
        defaultVoucherShouldBeFound("vat.in=" + DEFAULT_VAT + "," + UPDATED_VAT);

        // Get all the voucherList where vat equals to UPDATED_VAT
        defaultVoucherShouldNotBeFound("vat.in=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    void getAllVouchersByVatIsNullOrNotNull() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where vat is not null
        defaultVoucherShouldBeFound("vat.specified=true");

        // Get all the voucherList where vat is null
        defaultVoucherShouldNotBeFound("vat.specified=false");
    }

    @Test
    @Transactional
    void getAllVouchersByVatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where vat is greater than or equal to DEFAULT_VAT
        defaultVoucherShouldBeFound("vat.greaterThanOrEqual=" + DEFAULT_VAT);

        // Get all the voucherList where vat is greater than or equal to UPDATED_VAT
        defaultVoucherShouldNotBeFound("vat.greaterThanOrEqual=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    void getAllVouchersByVatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where vat is less than or equal to DEFAULT_VAT
        defaultVoucherShouldBeFound("vat.lessThanOrEqual=" + DEFAULT_VAT);

        // Get all the voucherList where vat is less than or equal to SMALLER_VAT
        defaultVoucherShouldNotBeFound("vat.lessThanOrEqual=" + SMALLER_VAT);
    }

    @Test
    @Transactional
    void getAllVouchersByVatIsLessThanSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where vat is less than DEFAULT_VAT
        defaultVoucherShouldNotBeFound("vat.lessThan=" + DEFAULT_VAT);

        // Get all the voucherList where vat is less than UPDATED_VAT
        defaultVoucherShouldBeFound("vat.lessThan=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    void getAllVouchersByVatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where vat is greater than DEFAULT_VAT
        defaultVoucherShouldNotBeFound("vat.greaterThan=" + DEFAULT_VAT);

        // Get all the voucherList where vat is greater than SMALLER_VAT
        defaultVoucherShouldBeFound("vat.greaterThan=" + SMALLER_VAT);
    }

    @Test
    @Transactional
    void getAllVouchersByDisountAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where disountAmount equals to DEFAULT_DISOUNT_AMOUNT
        defaultVoucherShouldBeFound("disountAmount.equals=" + DEFAULT_DISOUNT_AMOUNT);

        // Get all the voucherList where disountAmount equals to UPDATED_DISOUNT_AMOUNT
        defaultVoucherShouldNotBeFound("disountAmount.equals=" + UPDATED_DISOUNT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllVouchersByDisountAmountIsInShouldWork() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where disountAmount in DEFAULT_DISOUNT_AMOUNT or UPDATED_DISOUNT_AMOUNT
        defaultVoucherShouldBeFound("disountAmount.in=" + DEFAULT_DISOUNT_AMOUNT + "," + UPDATED_DISOUNT_AMOUNT);

        // Get all the voucherList where disountAmount equals to UPDATED_DISOUNT_AMOUNT
        defaultVoucherShouldNotBeFound("disountAmount.in=" + UPDATED_DISOUNT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllVouchersByDisountAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where disountAmount is not null
        defaultVoucherShouldBeFound("disountAmount.specified=true");

        // Get all the voucherList where disountAmount is null
        defaultVoucherShouldNotBeFound("disountAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllVouchersByDisountAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where disountAmount is greater than or equal to DEFAULT_DISOUNT_AMOUNT
        defaultVoucherShouldBeFound("disountAmount.greaterThanOrEqual=" + DEFAULT_DISOUNT_AMOUNT);

        // Get all the voucherList where disountAmount is greater than or equal to UPDATED_DISOUNT_AMOUNT
        defaultVoucherShouldNotBeFound("disountAmount.greaterThanOrEqual=" + UPDATED_DISOUNT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllVouchersByDisountAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where disountAmount is less than or equal to DEFAULT_DISOUNT_AMOUNT
        defaultVoucherShouldBeFound("disountAmount.lessThanOrEqual=" + DEFAULT_DISOUNT_AMOUNT);

        // Get all the voucherList where disountAmount is less than or equal to SMALLER_DISOUNT_AMOUNT
        defaultVoucherShouldNotBeFound("disountAmount.lessThanOrEqual=" + SMALLER_DISOUNT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllVouchersByDisountAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where disountAmount is less than DEFAULT_DISOUNT_AMOUNT
        defaultVoucherShouldNotBeFound("disountAmount.lessThan=" + DEFAULT_DISOUNT_AMOUNT);

        // Get all the voucherList where disountAmount is less than UPDATED_DISOUNT_AMOUNT
        defaultVoucherShouldBeFound("disountAmount.lessThan=" + UPDATED_DISOUNT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllVouchersByDisountAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where disountAmount is greater than DEFAULT_DISOUNT_AMOUNT
        defaultVoucherShouldNotBeFound("disountAmount.greaterThan=" + DEFAULT_DISOUNT_AMOUNT);

        // Get all the voucherList where disountAmount is greater than SMALLER_DISOUNT_AMOUNT
        defaultVoucherShouldBeFound("disountAmount.greaterThan=" + SMALLER_DISOUNT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllVouchersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where status equals to DEFAULT_STATUS
        defaultVoucherShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the voucherList where status equals to UPDATED_STATUS
        defaultVoucherShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllVouchersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultVoucherShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the voucherList where status equals to UPDATED_STATUS
        defaultVoucherShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllVouchersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where status is not null
        defaultVoucherShouldBeFound("status.specified=true");

        // Get all the voucherList where status is null
        defaultVoucherShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllVouchersByTotalPayableAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where totalPayableAmount equals to DEFAULT_TOTAL_PAYABLE_AMOUNT
        defaultVoucherShouldBeFound("totalPayableAmount.equals=" + DEFAULT_TOTAL_PAYABLE_AMOUNT);

        // Get all the voucherList where totalPayableAmount equals to UPDATED_TOTAL_PAYABLE_AMOUNT
        defaultVoucherShouldNotBeFound("totalPayableAmount.equals=" + UPDATED_TOTAL_PAYABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllVouchersByTotalPayableAmountIsInShouldWork() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where totalPayableAmount in DEFAULT_TOTAL_PAYABLE_AMOUNT or UPDATED_TOTAL_PAYABLE_AMOUNT
        defaultVoucherShouldBeFound("totalPayableAmount.in=" + DEFAULT_TOTAL_PAYABLE_AMOUNT + "," + UPDATED_TOTAL_PAYABLE_AMOUNT);

        // Get all the voucherList where totalPayableAmount equals to UPDATED_TOTAL_PAYABLE_AMOUNT
        defaultVoucherShouldNotBeFound("totalPayableAmount.in=" + UPDATED_TOTAL_PAYABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllVouchersByTotalPayableAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where totalPayableAmount is not null
        defaultVoucherShouldBeFound("totalPayableAmount.specified=true");

        // Get all the voucherList where totalPayableAmount is null
        defaultVoucherShouldNotBeFound("totalPayableAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllVouchersByTotalPayableAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where totalPayableAmount is greater than or equal to DEFAULT_TOTAL_PAYABLE_AMOUNT
        defaultVoucherShouldBeFound("totalPayableAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_PAYABLE_AMOUNT);

        // Get all the voucherList where totalPayableAmount is greater than or equal to UPDATED_TOTAL_PAYABLE_AMOUNT
        defaultVoucherShouldNotBeFound("totalPayableAmount.greaterThanOrEqual=" + UPDATED_TOTAL_PAYABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllVouchersByTotalPayableAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where totalPayableAmount is less than or equal to DEFAULT_TOTAL_PAYABLE_AMOUNT
        defaultVoucherShouldBeFound("totalPayableAmount.lessThanOrEqual=" + DEFAULT_TOTAL_PAYABLE_AMOUNT);

        // Get all the voucherList where totalPayableAmount is less than or equal to SMALLER_TOTAL_PAYABLE_AMOUNT
        defaultVoucherShouldNotBeFound("totalPayableAmount.lessThanOrEqual=" + SMALLER_TOTAL_PAYABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllVouchersByTotalPayableAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where totalPayableAmount is less than DEFAULT_TOTAL_PAYABLE_AMOUNT
        defaultVoucherShouldNotBeFound("totalPayableAmount.lessThan=" + DEFAULT_TOTAL_PAYABLE_AMOUNT);

        // Get all the voucherList where totalPayableAmount is less than UPDATED_TOTAL_PAYABLE_AMOUNT
        defaultVoucherShouldBeFound("totalPayableAmount.lessThan=" + UPDATED_TOTAL_PAYABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllVouchersByTotalPayableAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where totalPayableAmount is greater than DEFAULT_TOTAL_PAYABLE_AMOUNT
        defaultVoucherShouldNotBeFound("totalPayableAmount.greaterThan=" + DEFAULT_TOTAL_PAYABLE_AMOUNT);

        // Get all the voucherList where totalPayableAmount is greater than SMALLER_TOTAL_PAYABLE_AMOUNT
        defaultVoucherShouldBeFound("totalPayableAmount.greaterThan=" + SMALLER_TOTAL_PAYABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllVouchersByDateCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where dateCreated equals to DEFAULT_DATE_CREATED
        defaultVoucherShouldBeFound("dateCreated.equals=" + DEFAULT_DATE_CREATED);

        // Get all the voucherList where dateCreated equals to UPDATED_DATE_CREATED
        defaultVoucherShouldNotBeFound("dateCreated.equals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    void getAllVouchersByDateCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where dateCreated in DEFAULT_DATE_CREATED or UPDATED_DATE_CREATED
        defaultVoucherShouldBeFound("dateCreated.in=" + DEFAULT_DATE_CREATED + "," + UPDATED_DATE_CREATED);

        // Get all the voucherList where dateCreated equals to UPDATED_DATE_CREATED
        defaultVoucherShouldNotBeFound("dateCreated.in=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    void getAllVouchersByDateCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where dateCreated is not null
        defaultVoucherShouldBeFound("dateCreated.specified=true");

        // Get all the voucherList where dateCreated is null
        defaultVoucherShouldNotBeFound("dateCreated.specified=false");
    }

    @Test
    @Transactional
    void getAllVouchersByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where addedBy equals to DEFAULT_ADDED_BY
        defaultVoucherShouldBeFound("addedBy.equals=" + DEFAULT_ADDED_BY);

        // Get all the voucherList where addedBy equals to UPDATED_ADDED_BY
        defaultVoucherShouldNotBeFound("addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllVouchersByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where addedBy in DEFAULT_ADDED_BY or UPDATED_ADDED_BY
        defaultVoucherShouldBeFound("addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY);

        // Get all the voucherList where addedBy equals to UPDATED_ADDED_BY
        defaultVoucherShouldNotBeFound("addedBy.in=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllVouchersByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where addedBy is not null
        defaultVoucherShouldBeFound("addedBy.specified=true");

        // Get all the voucherList where addedBy is null
        defaultVoucherShouldNotBeFound("addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllVouchersByAddedByContainsSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where addedBy contains DEFAULT_ADDED_BY
        defaultVoucherShouldBeFound("addedBy.contains=" + DEFAULT_ADDED_BY);

        // Get all the voucherList where addedBy contains UPDATED_ADDED_BY
        defaultVoucherShouldNotBeFound("addedBy.contains=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllVouchersByAddedByNotContainsSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where addedBy does not contain DEFAULT_ADDED_BY
        defaultVoucherShouldNotBeFound("addedBy.doesNotContain=" + DEFAULT_ADDED_BY);

        // Get all the voucherList where addedBy does not contain UPDATED_ADDED_BY
        defaultVoucherShouldBeFound("addedBy.doesNotContain=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllVouchersByBoxNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where boxNumber equals to DEFAULT_BOX_NUMBER
        defaultVoucherShouldBeFound("boxNumber.equals=" + DEFAULT_BOX_NUMBER);

        // Get all the voucherList where boxNumber equals to UPDATED_BOX_NUMBER
        defaultVoucherShouldNotBeFound("boxNumber.equals=" + UPDATED_BOX_NUMBER);
    }

    @Test
    @Transactional
    void getAllVouchersByBoxNumberIsInShouldWork() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where boxNumber in DEFAULT_BOX_NUMBER or UPDATED_BOX_NUMBER
        defaultVoucherShouldBeFound("boxNumber.in=" + DEFAULT_BOX_NUMBER + "," + UPDATED_BOX_NUMBER);

        // Get all the voucherList where boxNumber equals to UPDATED_BOX_NUMBER
        defaultVoucherShouldNotBeFound("boxNumber.in=" + UPDATED_BOX_NUMBER);
    }

    @Test
    @Transactional
    void getAllVouchersByBoxNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where boxNumber is not null
        defaultVoucherShouldBeFound("boxNumber.specified=true");

        // Get all the voucherList where boxNumber is null
        defaultVoucherShouldNotBeFound("boxNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllVouchersByBoxNumberContainsSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where boxNumber contains DEFAULT_BOX_NUMBER
        defaultVoucherShouldBeFound("boxNumber.contains=" + DEFAULT_BOX_NUMBER);

        // Get all the voucherList where boxNumber contains UPDATED_BOX_NUMBER
        defaultVoucherShouldNotBeFound("boxNumber.contains=" + UPDATED_BOX_NUMBER);
    }

    @Test
    @Transactional
    void getAllVouchersByBoxNumberNotContainsSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where boxNumber does not contain DEFAULT_BOX_NUMBER
        defaultVoucherShouldNotBeFound("boxNumber.doesNotContain=" + DEFAULT_BOX_NUMBER);

        // Get all the voucherList where boxNumber does not contain UPDATED_BOX_NUMBER
        defaultVoucherShouldBeFound("boxNumber.doesNotContain=" + UPDATED_BOX_NUMBER);
    }

    @Test
    @Transactional
    void getAllVouchersByDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where deliveryDate equals to DEFAULT_DELIVERY_DATE
        defaultVoucherShouldBeFound("deliveryDate.equals=" + DEFAULT_DELIVERY_DATE);

        // Get all the voucherList where deliveryDate equals to UPDATED_DELIVERY_DATE
        defaultVoucherShouldNotBeFound("deliveryDate.equals=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    void getAllVouchersByDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where deliveryDate in DEFAULT_DELIVERY_DATE or UPDATED_DELIVERY_DATE
        defaultVoucherShouldBeFound("deliveryDate.in=" + DEFAULT_DELIVERY_DATE + "," + UPDATED_DELIVERY_DATE);

        // Get all the voucherList where deliveryDate equals to UPDATED_DELIVERY_DATE
        defaultVoucherShouldNotBeFound("deliveryDate.in=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    void getAllVouchersByDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where deliveryDate is not null
        defaultVoucherShouldBeFound("deliveryDate.specified=true");

        // Get all the voucherList where deliveryDate is null
        defaultVoucherShouldNotBeFound("deliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllVouchersByDeliveryStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where deliveryStatus equals to DEFAULT_DELIVERY_STATUS
        defaultVoucherShouldBeFound("deliveryStatus.equals=" + DEFAULT_DELIVERY_STATUS);

        // Get all the voucherList where deliveryStatus equals to UPDATED_DELIVERY_STATUS
        defaultVoucherShouldNotBeFound("deliveryStatus.equals=" + UPDATED_DELIVERY_STATUS);
    }

    @Test
    @Transactional
    void getAllVouchersByDeliveryStatusIsInShouldWork() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where deliveryStatus in DEFAULT_DELIVERY_STATUS or UPDATED_DELIVERY_STATUS
        defaultVoucherShouldBeFound("deliveryStatus.in=" + DEFAULT_DELIVERY_STATUS + "," + UPDATED_DELIVERY_STATUS);

        // Get all the voucherList where deliveryStatus equals to UPDATED_DELIVERY_STATUS
        defaultVoucherShouldNotBeFound("deliveryStatus.in=" + UPDATED_DELIVERY_STATUS);
    }

    @Test
    @Transactional
    void getAllVouchersByDeliveryStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where deliveryStatus is not null
        defaultVoucherShouldBeFound("deliveryStatus.specified=true");

        // Get all the voucherList where deliveryStatus is null
        defaultVoucherShouldNotBeFound("deliveryStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllVouchersByAurumServiceIsEqualToSomething() throws Exception {
        AurumService aurumService;
        if (TestUtil.findAll(em, AurumService.class).isEmpty()) {
            voucherRepository.saveAndFlush(voucher);
            aurumService = AurumServiceResourceIT.createEntity(em);
        } else {
            aurumService = TestUtil.findAll(em, AurumService.class).get(0);
        }
        em.persist(aurumService);
        em.flush();
        voucher.addAurumService(aurumService);
        voucherRepository.saveAndFlush(voucher);
        Long aurumServiceId = aurumService.getId();

        // Get all the voucherList where aurumService equals to aurumServiceId
        defaultVoucherShouldBeFound("aurumServiceId.equals=" + aurumServiceId);

        // Get all the voucherList where aurumService equals to (aurumServiceId + 1)
        defaultVoucherShouldNotBeFound("aurumServiceId.equals=" + (aurumServiceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVoucherShouldBeFound(String filter) throws Exception {
        restVoucherMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voucher.getId().intValue())))
            .andExpect(jsonPath("$.[*].voucherNo").value(hasItem(DEFAULT_VOUCHER_NO)))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID.intValue())))
            .andExpect(jsonPath("$.[*].calculatedTotalAmount").value(hasItem(sameNumber(DEFAULT_CALCULATED_TOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].vat").value(hasItem(sameNumber(DEFAULT_VAT))))
            .andExpect(jsonPath("$.[*].disountAmount").value(hasItem(sameNumber(DEFAULT_DISOUNT_AMOUNT))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].totalPayableAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_PAYABLE_AMOUNT))))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)))
            .andExpect(jsonPath("$.[*].boxNumber").value(hasItem(DEFAULT_BOX_NUMBER)))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryStatus").value(hasItem(DEFAULT_DELIVERY_STATUS.booleanValue())));

        // Check, that the count call also returns 1
        restVoucherMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVoucherShouldNotBeFound(String filter) throws Exception {
        restVoucherMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVoucherMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVoucher() throws Exception {
        // Get the voucher
        restVoucherMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVoucher() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

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
            .deliveryDate(UPDATED_DELIVERY_DATE)
            .deliveryStatus(UPDATED_DELIVERY_STATUS);

        restVoucherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVoucher.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVoucher))
            )
            .andExpect(status().isOk());

        // Validate the Voucher in the database
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeUpdate);
        Voucher testVoucher = voucherList.get(voucherList.size() - 1);
        assertThat(testVoucher.getVoucherNo()).isEqualTo(UPDATED_VOUCHER_NO);
        assertThat(testVoucher.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testVoucher.getCalculatedTotalAmount()).isEqualByComparingTo(UPDATED_CALCULATED_TOTAL_AMOUNT);
        assertThat(testVoucher.getVat()).isEqualByComparingTo(UPDATED_VAT);
        assertThat(testVoucher.getDisountAmount()).isEqualByComparingTo(UPDATED_DISOUNT_AMOUNT);
        assertThat(testVoucher.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testVoucher.getTotalPayableAmount()).isEqualByComparingTo(UPDATED_TOTAL_PAYABLE_AMOUNT);
        assertThat(testVoucher.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testVoucher.getAddedBy()).isEqualTo(UPDATED_ADDED_BY);
        assertThat(testVoucher.getBoxNumber()).isEqualTo(UPDATED_BOX_NUMBER);
        assertThat(testVoucher.getDeliveryDate()).isEqualTo(UPDATED_DELIVERY_DATE);
        assertThat(testVoucher.getDeliveryStatus()).isEqualTo(UPDATED_DELIVERY_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingVoucher() throws Exception {
        int databaseSizeBeforeUpdate = voucherRepository.findAll().size();
        voucher.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoucherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, voucher.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voucher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Voucher in the database
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVoucher() throws Exception {
        int databaseSizeBeforeUpdate = voucherRepository.findAll().size();
        voucher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoucherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voucher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Voucher in the database
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVoucher() throws Exception {
        int databaseSizeBeforeUpdate = voucherRepository.findAll().size();
        voucher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoucherMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voucher)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Voucher in the database
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVoucherWithPatch() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        int databaseSizeBeforeUpdate = voucherRepository.findAll().size();

        // Update the voucher using partial update
        Voucher partialUpdatedVoucher = new Voucher();
        partialUpdatedVoucher.setId(voucher.getId());

        partialUpdatedVoucher
            .customerId(UPDATED_CUSTOMER_ID)
            .vat(UPDATED_VAT)
            .disountAmount(UPDATED_DISOUNT_AMOUNT)
            .status(UPDATED_STATUS)
            .totalPayableAmount(UPDATED_TOTAL_PAYABLE_AMOUNT)
            .boxNumber(UPDATED_BOX_NUMBER);

        restVoucherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVoucher.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVoucher))
            )
            .andExpect(status().isOk());

        // Validate the Voucher in the database
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeUpdate);
        Voucher testVoucher = voucherList.get(voucherList.size() - 1);
        assertThat(testVoucher.getVoucherNo()).isEqualTo(DEFAULT_VOUCHER_NO);
        assertThat(testVoucher.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testVoucher.getCalculatedTotalAmount()).isEqualByComparingTo(DEFAULT_CALCULATED_TOTAL_AMOUNT);
        assertThat(testVoucher.getVat()).isEqualByComparingTo(UPDATED_VAT);
        assertThat(testVoucher.getDisountAmount()).isEqualByComparingTo(UPDATED_DISOUNT_AMOUNT);
        assertThat(testVoucher.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testVoucher.getTotalPayableAmount()).isEqualByComparingTo(UPDATED_TOTAL_PAYABLE_AMOUNT);
        assertThat(testVoucher.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testVoucher.getAddedBy()).isEqualTo(DEFAULT_ADDED_BY);
        assertThat(testVoucher.getBoxNumber()).isEqualTo(UPDATED_BOX_NUMBER);
        assertThat(testVoucher.getDeliveryDate()).isEqualTo(DEFAULT_DELIVERY_DATE);
        assertThat(testVoucher.getDeliveryStatus()).isEqualTo(DEFAULT_DELIVERY_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateVoucherWithPatch() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        int databaseSizeBeforeUpdate = voucherRepository.findAll().size();

        // Update the voucher using partial update
        Voucher partialUpdatedVoucher = new Voucher();
        partialUpdatedVoucher.setId(voucher.getId());

        partialUpdatedVoucher
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
            .deliveryDate(UPDATED_DELIVERY_DATE)
            .deliveryStatus(UPDATED_DELIVERY_STATUS);

        restVoucherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVoucher.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVoucher))
            )
            .andExpect(status().isOk());

        // Validate the Voucher in the database
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeUpdate);
        Voucher testVoucher = voucherList.get(voucherList.size() - 1);
        assertThat(testVoucher.getVoucherNo()).isEqualTo(UPDATED_VOUCHER_NO);
        assertThat(testVoucher.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testVoucher.getCalculatedTotalAmount()).isEqualByComparingTo(UPDATED_CALCULATED_TOTAL_AMOUNT);
        assertThat(testVoucher.getVat()).isEqualByComparingTo(UPDATED_VAT);
        assertThat(testVoucher.getDisountAmount()).isEqualByComparingTo(UPDATED_DISOUNT_AMOUNT);
        assertThat(testVoucher.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testVoucher.getTotalPayableAmount()).isEqualByComparingTo(UPDATED_TOTAL_PAYABLE_AMOUNT);
        assertThat(testVoucher.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testVoucher.getAddedBy()).isEqualTo(UPDATED_ADDED_BY);
        assertThat(testVoucher.getBoxNumber()).isEqualTo(UPDATED_BOX_NUMBER);
        assertThat(testVoucher.getDeliveryDate()).isEqualTo(UPDATED_DELIVERY_DATE);
        assertThat(testVoucher.getDeliveryStatus()).isEqualTo(UPDATED_DELIVERY_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingVoucher() throws Exception {
        int databaseSizeBeforeUpdate = voucherRepository.findAll().size();
        voucher.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoucherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, voucher.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(voucher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Voucher in the database
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVoucher() throws Exception {
        int databaseSizeBeforeUpdate = voucherRepository.findAll().size();
        voucher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoucherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(voucher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Voucher in the database
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVoucher() throws Exception {
        int databaseSizeBeforeUpdate = voucherRepository.findAll().size();
        voucher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoucherMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(voucher)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Voucher in the database
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVoucher() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        int databaseSizeBeforeDelete = voucherRepository.findAll().size();

        // Delete the voucher
        restVoucherMockMvc
            .perform(delete(ENTITY_API_URL_ID, voucher.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
