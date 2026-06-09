package com.meftaul.aurum.web.rest;

import static com.meftaul.aurum.domain.VoucherAsserts.*;
import static com.meftaul.aurum.web.rest.TestUtil.createUpdateProxyForBean;
import static com.meftaul.aurum.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meftaul.aurum.IntegrationTest;
import com.meftaul.aurum.domain.Voucher;
import com.meftaul.aurum.domain.enumeration.VoucherStatus;
import com.meftaul.aurum.repository.VoucherRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
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
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVoucherMockMvc;

    private Voucher voucher;

    private Voucher insertedVoucher;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Voucher createEntity() {
        return new Voucher()
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
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Voucher createUpdatedEntity() {
        return new Voucher()
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
    }

    @BeforeEach
    void initTest() {
        voucher = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedVoucher != null) {
            voucherRepository.delete(insertedVoucher);
            insertedVoucher = null;
        }
    }

    @Test
    @Transactional
    void createVoucher() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Voucher
        var returnedVoucher = om.readValue(
            restVoucherMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(voucher)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Voucher.class
        );

        // Validate the Voucher in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertVoucherUpdatableFieldsEquals(returnedVoucher, getPersistedVoucher(returnedVoucher));

        insertedVoucher = returnedVoucher;
    }

    @Test
    @Transactional
    void createVoucherWithExistingId() throws Exception {
        // Create the Voucher with an existing ID
        voucher.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVoucherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(voucher)))
            .andExpect(status().isBadRequest());

        // Validate the Voucher in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCalculatedTotalAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        voucher.setCalculatedTotalAmount(null);

        // Create the Voucher, which fails.

        restVoucherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(voucher)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        voucher.setStatus(null);

        // Create the Voucher, which fails.

        restVoucherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(voucher)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPayableAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        voucher.setTotalPayableAmount(null);

        // Create the Voucher, which fails.

        restVoucherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(voucher)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        voucher.setAddedBy(null);

        // Create the Voucher, which fails.

        restVoucherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(voucher)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVouchers() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

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
            .andExpect(jsonPath("$.[*].deliveryStatus").value(hasItem(DEFAULT_DELIVERY_STATUS)));
    }

    @Test
    @Transactional
    void getVoucher() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

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
            .andExpect(jsonPath("$.deliveryStatus").value(DEFAULT_DELIVERY_STATUS));
    }

    @Test
    @Transactional
    void getVouchersByIdFiltering() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        Long id = voucher.getId();

        defaultVoucherFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultVoucherFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultVoucherFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVouchersByVoucherNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where voucherNo equals to
        defaultVoucherFiltering("voucherNo.equals=" + DEFAULT_VOUCHER_NO, "voucherNo.equals=" + UPDATED_VOUCHER_NO);
    }

    @Test
    @Transactional
    void getAllVouchersByVoucherNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where voucherNo in
        defaultVoucherFiltering("voucherNo.in=" + DEFAULT_VOUCHER_NO + "," + UPDATED_VOUCHER_NO, "voucherNo.in=" + UPDATED_VOUCHER_NO);
    }

    @Test
    @Transactional
    void getAllVouchersByVoucherNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where voucherNo is not null
        defaultVoucherFiltering("voucherNo.specified=true", "voucherNo.specified=false");
    }

    @Test
    @Transactional
    void getAllVouchersByVoucherNoContainsSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where voucherNo contains
        defaultVoucherFiltering("voucherNo.contains=" + DEFAULT_VOUCHER_NO, "voucherNo.contains=" + UPDATED_VOUCHER_NO);
    }

    @Test
    @Transactional
    void getAllVouchersByVoucherNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where voucherNo does not contain
        defaultVoucherFiltering("voucherNo.doesNotContain=" + UPDATED_VOUCHER_NO, "voucherNo.doesNotContain=" + DEFAULT_VOUCHER_NO);
    }

    @Test
    @Transactional
    void getAllVouchersByCustomerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where customerId equals to
        defaultVoucherFiltering("customerId.equals=" + DEFAULT_CUSTOMER_ID, "customerId.equals=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllVouchersByCustomerIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where customerId in
        defaultVoucherFiltering("customerId.in=" + DEFAULT_CUSTOMER_ID + "," + UPDATED_CUSTOMER_ID, "customerId.in=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllVouchersByCustomerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where customerId is not null
        defaultVoucherFiltering("customerId.specified=true", "customerId.specified=false");
    }

    @Test
    @Transactional
    void getAllVouchersByCustomerIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where customerId is greater than or equal to
        defaultVoucherFiltering(
            "customerId.greaterThanOrEqual=" + DEFAULT_CUSTOMER_ID,
            "customerId.greaterThanOrEqual=" + UPDATED_CUSTOMER_ID
        );
    }

    @Test
    @Transactional
    void getAllVouchersByCustomerIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where customerId is less than or equal to
        defaultVoucherFiltering("customerId.lessThanOrEqual=" + DEFAULT_CUSTOMER_ID, "customerId.lessThanOrEqual=" + SMALLER_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllVouchersByCustomerIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where customerId is less than
        defaultVoucherFiltering("customerId.lessThan=" + UPDATED_CUSTOMER_ID, "customerId.lessThan=" + DEFAULT_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllVouchersByCustomerIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where customerId is greater than
        defaultVoucherFiltering("customerId.greaterThan=" + SMALLER_CUSTOMER_ID, "customerId.greaterThan=" + DEFAULT_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllVouchersByCalculatedTotalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where calculatedTotalAmount equals to
        defaultVoucherFiltering(
            "calculatedTotalAmount.equals=" + DEFAULT_CALCULATED_TOTAL_AMOUNT,
            "calculatedTotalAmount.equals=" + UPDATED_CALCULATED_TOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllVouchersByCalculatedTotalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where calculatedTotalAmount in
        defaultVoucherFiltering(
            "calculatedTotalAmount.in=" + DEFAULT_CALCULATED_TOTAL_AMOUNT + "," + UPDATED_CALCULATED_TOTAL_AMOUNT,
            "calculatedTotalAmount.in=" + UPDATED_CALCULATED_TOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllVouchersByCalculatedTotalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where calculatedTotalAmount is not null
        defaultVoucherFiltering("calculatedTotalAmount.specified=true", "calculatedTotalAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllVouchersByCalculatedTotalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where calculatedTotalAmount is greater than or equal to
        defaultVoucherFiltering(
            "calculatedTotalAmount.greaterThanOrEqual=" + DEFAULT_CALCULATED_TOTAL_AMOUNT,
            "calculatedTotalAmount.greaterThanOrEqual=" + UPDATED_CALCULATED_TOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllVouchersByCalculatedTotalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where calculatedTotalAmount is less than or equal to
        defaultVoucherFiltering(
            "calculatedTotalAmount.lessThanOrEqual=" + DEFAULT_CALCULATED_TOTAL_AMOUNT,
            "calculatedTotalAmount.lessThanOrEqual=" + SMALLER_CALCULATED_TOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllVouchersByCalculatedTotalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where calculatedTotalAmount is less than
        defaultVoucherFiltering(
            "calculatedTotalAmount.lessThan=" + UPDATED_CALCULATED_TOTAL_AMOUNT,
            "calculatedTotalAmount.lessThan=" + DEFAULT_CALCULATED_TOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllVouchersByCalculatedTotalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where calculatedTotalAmount is greater than
        defaultVoucherFiltering(
            "calculatedTotalAmount.greaterThan=" + SMALLER_CALCULATED_TOTAL_AMOUNT,
            "calculatedTotalAmount.greaterThan=" + DEFAULT_CALCULATED_TOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllVouchersByVatIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where vat equals to
        defaultVoucherFiltering("vat.equals=" + DEFAULT_VAT, "vat.equals=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    void getAllVouchersByVatIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where vat in
        defaultVoucherFiltering("vat.in=" + DEFAULT_VAT + "," + UPDATED_VAT, "vat.in=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    void getAllVouchersByVatIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where vat is not null
        defaultVoucherFiltering("vat.specified=true", "vat.specified=false");
    }

    @Test
    @Transactional
    void getAllVouchersByVatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where vat is greater than or equal to
        defaultVoucherFiltering("vat.greaterThanOrEqual=" + DEFAULT_VAT, "vat.greaterThanOrEqual=" + UPDATED_VAT);
    }

    @Test
    @Transactional
    void getAllVouchersByVatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where vat is less than or equal to
        defaultVoucherFiltering("vat.lessThanOrEqual=" + DEFAULT_VAT, "vat.lessThanOrEqual=" + SMALLER_VAT);
    }

    @Test
    @Transactional
    void getAllVouchersByVatIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where vat is less than
        defaultVoucherFiltering("vat.lessThan=" + UPDATED_VAT, "vat.lessThan=" + DEFAULT_VAT);
    }

    @Test
    @Transactional
    void getAllVouchersByVatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where vat is greater than
        defaultVoucherFiltering("vat.greaterThan=" + SMALLER_VAT, "vat.greaterThan=" + DEFAULT_VAT);
    }

    @Test
    @Transactional
    void getAllVouchersByDisountAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where disountAmount equals to
        defaultVoucherFiltering("disountAmount.equals=" + DEFAULT_DISOUNT_AMOUNT, "disountAmount.equals=" + UPDATED_DISOUNT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllVouchersByDisountAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where disountAmount in
        defaultVoucherFiltering(
            "disountAmount.in=" + DEFAULT_DISOUNT_AMOUNT + "," + UPDATED_DISOUNT_AMOUNT,
            "disountAmount.in=" + UPDATED_DISOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllVouchersByDisountAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where disountAmount is not null
        defaultVoucherFiltering("disountAmount.specified=true", "disountAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllVouchersByDisountAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where disountAmount is greater than or equal to
        defaultVoucherFiltering(
            "disountAmount.greaterThanOrEqual=" + DEFAULT_DISOUNT_AMOUNT,
            "disountAmount.greaterThanOrEqual=" + UPDATED_DISOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllVouchersByDisountAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where disountAmount is less than or equal to
        defaultVoucherFiltering(
            "disountAmount.lessThanOrEqual=" + DEFAULT_DISOUNT_AMOUNT,
            "disountAmount.lessThanOrEqual=" + SMALLER_DISOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllVouchersByDisountAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where disountAmount is less than
        defaultVoucherFiltering("disountAmount.lessThan=" + UPDATED_DISOUNT_AMOUNT, "disountAmount.lessThan=" + DEFAULT_DISOUNT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllVouchersByDisountAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where disountAmount is greater than
        defaultVoucherFiltering(
            "disountAmount.greaterThan=" + SMALLER_DISOUNT_AMOUNT,
            "disountAmount.greaterThan=" + DEFAULT_DISOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllVouchersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where status equals to
        defaultVoucherFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllVouchersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where status in
        defaultVoucherFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllVouchersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where status is not null
        defaultVoucherFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllVouchersByTotalPayableAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where totalPayableAmount equals to
        defaultVoucherFiltering(
            "totalPayableAmount.equals=" + DEFAULT_TOTAL_PAYABLE_AMOUNT,
            "totalPayableAmount.equals=" + UPDATED_TOTAL_PAYABLE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllVouchersByTotalPayableAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where totalPayableAmount in
        defaultVoucherFiltering(
            "totalPayableAmount.in=" + DEFAULT_TOTAL_PAYABLE_AMOUNT + "," + UPDATED_TOTAL_PAYABLE_AMOUNT,
            "totalPayableAmount.in=" + UPDATED_TOTAL_PAYABLE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllVouchersByTotalPayableAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where totalPayableAmount is not null
        defaultVoucherFiltering("totalPayableAmount.specified=true", "totalPayableAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllVouchersByTotalPayableAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where totalPayableAmount is greater than or equal to
        defaultVoucherFiltering(
            "totalPayableAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_PAYABLE_AMOUNT,
            "totalPayableAmount.greaterThanOrEqual=" + UPDATED_TOTAL_PAYABLE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllVouchersByTotalPayableAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where totalPayableAmount is less than or equal to
        defaultVoucherFiltering(
            "totalPayableAmount.lessThanOrEqual=" + DEFAULT_TOTAL_PAYABLE_AMOUNT,
            "totalPayableAmount.lessThanOrEqual=" + SMALLER_TOTAL_PAYABLE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllVouchersByTotalPayableAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where totalPayableAmount is less than
        defaultVoucherFiltering(
            "totalPayableAmount.lessThan=" + UPDATED_TOTAL_PAYABLE_AMOUNT,
            "totalPayableAmount.lessThan=" + DEFAULT_TOTAL_PAYABLE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllVouchersByTotalPayableAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where totalPayableAmount is greater than
        defaultVoucherFiltering(
            "totalPayableAmount.greaterThan=" + SMALLER_TOTAL_PAYABLE_AMOUNT,
            "totalPayableAmount.greaterThan=" + DEFAULT_TOTAL_PAYABLE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllVouchersByDateCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where dateCreated equals to
        defaultVoucherFiltering("dateCreated.equals=" + DEFAULT_DATE_CREATED, "dateCreated.equals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    void getAllVouchersByDateCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where dateCreated in
        defaultVoucherFiltering(
            "dateCreated.in=" + DEFAULT_DATE_CREATED + "," + UPDATED_DATE_CREATED,
            "dateCreated.in=" + UPDATED_DATE_CREATED
        );
    }

    @Test
    @Transactional
    void getAllVouchersByDateCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where dateCreated is not null
        defaultVoucherFiltering("dateCreated.specified=true", "dateCreated.specified=false");
    }

    @Test
    @Transactional
    void getAllVouchersByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where addedBy equals to
        defaultVoucherFiltering("addedBy.equals=" + DEFAULT_ADDED_BY, "addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllVouchersByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where addedBy in
        defaultVoucherFiltering("addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY, "addedBy.in=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllVouchersByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where addedBy is not null
        defaultVoucherFiltering("addedBy.specified=true", "addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllVouchersByAddedByContainsSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where addedBy contains
        defaultVoucherFiltering("addedBy.contains=" + DEFAULT_ADDED_BY, "addedBy.contains=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllVouchersByAddedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where addedBy does not contain
        defaultVoucherFiltering("addedBy.doesNotContain=" + UPDATED_ADDED_BY, "addedBy.doesNotContain=" + DEFAULT_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllVouchersByBoxNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where boxNumber equals to
        defaultVoucherFiltering("boxNumber.equals=" + DEFAULT_BOX_NUMBER, "boxNumber.equals=" + UPDATED_BOX_NUMBER);
    }

    @Test
    @Transactional
    void getAllVouchersByBoxNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where boxNumber in
        defaultVoucherFiltering("boxNumber.in=" + DEFAULT_BOX_NUMBER + "," + UPDATED_BOX_NUMBER, "boxNumber.in=" + UPDATED_BOX_NUMBER);
    }

    @Test
    @Transactional
    void getAllVouchersByBoxNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where boxNumber is not null
        defaultVoucherFiltering("boxNumber.specified=true", "boxNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllVouchersByBoxNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where boxNumber contains
        defaultVoucherFiltering("boxNumber.contains=" + DEFAULT_BOX_NUMBER, "boxNumber.contains=" + UPDATED_BOX_NUMBER);
    }

    @Test
    @Transactional
    void getAllVouchersByBoxNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where boxNumber does not contain
        defaultVoucherFiltering("boxNumber.doesNotContain=" + UPDATED_BOX_NUMBER, "boxNumber.doesNotContain=" + DEFAULT_BOX_NUMBER);
    }

    @Test
    @Transactional
    void getAllVouchersByDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where deliveryDate equals to
        defaultVoucherFiltering("deliveryDate.equals=" + DEFAULT_DELIVERY_DATE, "deliveryDate.equals=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    void getAllVouchersByDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where deliveryDate in
        defaultVoucherFiltering(
            "deliveryDate.in=" + DEFAULT_DELIVERY_DATE + "," + UPDATED_DELIVERY_DATE,
            "deliveryDate.in=" + UPDATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllVouchersByDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where deliveryDate is not null
        defaultVoucherFiltering("deliveryDate.specified=true", "deliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllVouchersByDeliveryStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where deliveryStatus equals to
        defaultVoucherFiltering("deliveryStatus.equals=" + DEFAULT_DELIVERY_STATUS, "deliveryStatus.equals=" + UPDATED_DELIVERY_STATUS);
    }

    @Test
    @Transactional
    void getAllVouchersByDeliveryStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where deliveryStatus in
        defaultVoucherFiltering(
            "deliveryStatus.in=" + DEFAULT_DELIVERY_STATUS + "," + UPDATED_DELIVERY_STATUS,
            "deliveryStatus.in=" + UPDATED_DELIVERY_STATUS
        );
    }

    @Test
    @Transactional
    void getAllVouchersByDeliveryStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList where deliveryStatus is not null
        defaultVoucherFiltering("deliveryStatus.specified=true", "deliveryStatus.specified=false");
    }

    private void defaultVoucherFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultVoucherShouldBeFound(shouldBeFound);
        defaultVoucherShouldNotBeFound(shouldNotBeFound);
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
            .andExpect(jsonPath("$.[*].deliveryStatus").value(hasItem(DEFAULT_DELIVERY_STATUS)));

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
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the voucher
        Voucher updatedVoucher = voucherRepository.findById(voucher.getId()).orElseThrow();
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
                    .content(om.writeValueAsBytes(updatedVoucher))
            )
            .andExpect(status().isOk());

        // Validate the Voucher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVoucherToMatchAllProperties(updatedVoucher);
    }

    @Test
    @Transactional
    void putNonExistingVoucher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        voucher.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoucherMockMvc
            .perform(put(ENTITY_API_URL_ID, voucher.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(voucher)))
            .andExpect(status().isBadRequest());

        // Validate the Voucher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVoucher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        voucher.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoucherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(voucher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Voucher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVoucher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        voucher.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoucherMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(voucher)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Voucher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVoucherWithPatch() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the voucher using partial update
        Voucher partialUpdatedVoucher = new Voucher();
        partialUpdatedVoucher.setId(voucher.getId());

        partialUpdatedVoucher
            .calculatedTotalAmount(UPDATED_CALCULATED_TOTAL_AMOUNT)
            .disountAmount(UPDATED_DISOUNT_AMOUNT)
            .status(UPDATED_STATUS)
            .totalPayableAmount(UPDATED_TOTAL_PAYABLE_AMOUNT)
            .boxNumber(UPDATED_BOX_NUMBER)
            .deliveryStatus(UPDATED_DELIVERY_STATUS);

        restVoucherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVoucher.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVoucher))
            )
            .andExpect(status().isOk());

        // Validate the Voucher in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVoucherUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVoucher, voucher), getPersistedVoucher(voucher));
    }

    @Test
    @Transactional
    void fullUpdateVoucherWithPatch() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        long databaseSizeBeforeUpdate = getRepositoryCount();

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
                    .content(om.writeValueAsBytes(partialUpdatedVoucher))
            )
            .andExpect(status().isOk());

        // Validate the Voucher in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVoucherUpdatableFieldsEquals(partialUpdatedVoucher, getPersistedVoucher(partialUpdatedVoucher));
    }

    @Test
    @Transactional
    void patchNonExistingVoucher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        voucher.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoucherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, voucher.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(voucher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Voucher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVoucher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        voucher.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoucherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(voucher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Voucher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVoucher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        voucher.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoucherMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(voucher)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Voucher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVoucher() throws Exception {
        // Initialize the database
        insertedVoucher = voucherRepository.saveAndFlush(voucher);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the voucher
        restVoucherMockMvc
            .perform(delete(ENTITY_API_URL_ID, voucher.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return voucherRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Voucher getPersistedVoucher(Voucher voucher) {
        return voucherRepository.findById(voucher.getId()).orElseThrow();
    }

    protected void assertPersistedVoucherToMatchAllProperties(Voucher expectedVoucher) {
        assertVoucherAllPropertiesEquals(expectedVoucher, getPersistedVoucher(expectedVoucher));
    }

    protected void assertPersistedVoucherToMatchUpdatableProperties(Voucher expectedVoucher) {
        assertVoucherAllUpdatablePropertiesEquals(expectedVoucher, getPersistedVoucher(expectedVoucher));
    }
}
