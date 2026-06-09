package com.meftaul.aurum.web.rest;

import com.meftaul.aurum.AurumApp;
import com.meftaul.aurum.domain.TransactionHistory;
import com.meftaul.aurum.repository.TransactionHistoryRepository;
import com.meftaul.aurum.service.TransactionHistoryService;
import com.meftaul.aurum.web.rest.errors.ExceptionTranslator;
import com.meftaul.aurum.service.dto.TransactionHistoryCriteria;
import com.meftaul.aurum.service.TransactionHistoryQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.meftaul.aurum.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.meftaul.aurum.domain.enumeration.TransactionStatus;
/**
 * Integration tests for the {@link TransactionHistoryResource} REST controller.
 */
@SpringBootTest(classes = AurumApp.class)
public class TransactionHistoryResourceIT {

    private static final String DEFAULT_VOUCHER_NO = "AAAAAAAAAA";
    private static final String UPDATED_VOUCHER_NO = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_DATE_CREATED = Instant.ofEpochMilli(-1L);

    private static final TransactionStatus DEFAULT_TAG = TransactionStatus.RECEIVE;
    private static final TransactionStatus UPDATED_TAG = TransactionStatus.VAT;

    private static final Long DEFAULT_CUSTOMER_ID = 1L;
    private static final Long UPDATED_CUSTOMER_ID = 2L;
    private static final Long SMALLER_CUSTOMER_ID = 1L - 1L;

    private static final String DEFAULT_ADDED_BY = "AAAAAAAAAA";
    private static final String UPDATED_ADDED_BY = "BBBBBBBBBB";

    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    private TransactionHistoryService transactionHistoryService;

    @Autowired
    private TransactionHistoryQueryService transactionHistoryQueryService;

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

    private MockMvc restTransactionHistoryMockMvc;

    private TransactionHistory transactionHistory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TransactionHistoryResource transactionHistoryResource = new TransactionHistoryResource(transactionHistoryService, transactionHistoryQueryService);
        this.restTransactionHistoryMockMvc = MockMvcBuilders.standaloneSetup(transactionHistoryResource)
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
    public static TransactionHistory createEntity(EntityManager em) {
        TransactionHistory transactionHistory = new TransactionHistory()
            .voucherNo(DEFAULT_VOUCHER_NO)
            .amount(DEFAULT_AMOUNT)
            .dateCreated(DEFAULT_DATE_CREATED)
            .tag(DEFAULT_TAG)
            .customerId(DEFAULT_CUSTOMER_ID)
            .addedBy(DEFAULT_ADDED_BY);
        return transactionHistory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionHistory createUpdatedEntity(EntityManager em) {
        TransactionHistory transactionHistory = new TransactionHistory()
            .voucherNo(UPDATED_VOUCHER_NO)
            .amount(UPDATED_AMOUNT)
            .dateCreated(UPDATED_DATE_CREATED)
            .tag(UPDATED_TAG)
            .customerId(UPDATED_CUSTOMER_ID)
            .addedBy(UPDATED_ADDED_BY);
        return transactionHistory;
    }

    @BeforeEach
    public void initTest() {
        transactionHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransactionHistory() throws Exception {
        int databaseSizeBeforeCreate = transactionHistoryRepository.findAll().size();

        // Create the TransactionHistory
        restTransactionHistoryMockMvc.perform(post("/api/transaction-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionHistory)))
            .andExpect(status().isCreated());

        // Validate the TransactionHistory in the database
        List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.findAll();
        assertThat(transactionHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        TransactionHistory testTransactionHistory = transactionHistoryList.get(transactionHistoryList.size() - 1);
        assertThat(testTransactionHistory.getVoucherNo()).isEqualTo(DEFAULT_VOUCHER_NO);
        assertThat(testTransactionHistory.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testTransactionHistory.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testTransactionHistory.getTag()).isEqualTo(DEFAULT_TAG);
        assertThat(testTransactionHistory.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testTransactionHistory.getAddedBy()).isEqualTo(DEFAULT_ADDED_BY);
    }

    @Test
    @Transactional
    public void createTransactionHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transactionHistoryRepository.findAll().size();

        // Create the TransactionHistory with an existing ID
        transactionHistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionHistoryMockMvc.perform(post("/api/transaction-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionHistory)))
            .andExpect(status().isBadRequest());

        // Validate the TransactionHistory in the database
        List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.findAll();
        assertThat(transactionHistoryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkVoucherNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionHistoryRepository.findAll().size();
        // set the field null
        transactionHistory.setVoucherNo(null);

        // Create the TransactionHistory, which fails.

        restTransactionHistoryMockMvc.perform(post("/api/transaction-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionHistory)))
            .andExpect(status().isBadRequest());

        List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.findAll();
        assertThat(transactionHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionHistoryRepository.findAll().size();
        // set the field null
        transactionHistory.setAmount(null);

        // Create the TransactionHistory, which fails.

        restTransactionHistoryMockMvc.perform(post("/api/transaction-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionHistory)))
            .andExpect(status().isBadRequest());

        List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.findAll();
        assertThat(transactionHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionHistoryRepository.findAll().size();
        // set the field null
        transactionHistory.setDateCreated(null);

        // Create the TransactionHistory, which fails.

        restTransactionHistoryMockMvc.perform(post("/api/transaction-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionHistory)))
            .andExpect(status().isBadRequest());

        List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.findAll();
        assertThat(transactionHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTagIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionHistoryRepository.findAll().size();
        // set the field null
        transactionHistory.setTag(null);

        // Create the TransactionHistory, which fails.

        restTransactionHistoryMockMvc.perform(post("/api/transaction-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionHistory)))
            .andExpect(status().isBadRequest());

        List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.findAll();
        assertThat(transactionHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionHistoryRepository.findAll().size();
        // set the field null
        transactionHistory.setCustomerId(null);

        // Create the TransactionHistory, which fails.

        restTransactionHistoryMockMvc.perform(post("/api/transaction-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionHistory)))
            .andExpect(status().isBadRequest());

        List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.findAll();
        assertThat(transactionHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionHistoryRepository.findAll().size();
        // set the field null
        transactionHistory.setAddedBy(null);

        // Create the TransactionHistory, which fails.

        restTransactionHistoryMockMvc.perform(post("/api/transaction-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionHistory)))
            .andExpect(status().isBadRequest());

        List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.findAll();
        assertThat(transactionHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTransactionHistories() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList
        restTransactionHistoryMockMvc.perform(get("/api/transaction-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].voucherNo").value(hasItem(DEFAULT_VOUCHER_NO.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG.toString())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID.intValue())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY.toString())));
    }
    
    @Test
    @Transactional
    public void getTransactionHistory() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get the transactionHistory
        restTransactionHistoryMockMvc.perform(get("/api/transaction-histories/{id}", transactionHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transactionHistory.getId().intValue()))
            .andExpect(jsonPath("$.voucherNo").value(DEFAULT_VOUCHER_NO.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.tag").value(DEFAULT_TAG.toString()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID.intValue()))
            .andExpect(jsonPath("$.addedBy").value(DEFAULT_ADDED_BY.toString()));
    }

    @Test
    @Transactional
    public void getAllTransactionHistoriesByVoucherNoIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList where voucherNo equals to DEFAULT_VOUCHER_NO
        defaultTransactionHistoryShouldBeFound("voucherNo.equals=" + DEFAULT_VOUCHER_NO);

        // Get all the transactionHistoryList where voucherNo equals to UPDATED_VOUCHER_NO
        defaultTransactionHistoryShouldNotBeFound("voucherNo.equals=" + UPDATED_VOUCHER_NO);
    }

    @Test
    @Transactional
    public void getAllTransactionHistoriesByVoucherNoIsInShouldWork() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList where voucherNo in DEFAULT_VOUCHER_NO or UPDATED_VOUCHER_NO
        defaultTransactionHistoryShouldBeFound("voucherNo.in=" + DEFAULT_VOUCHER_NO + "," + UPDATED_VOUCHER_NO);

        // Get all the transactionHistoryList where voucherNo equals to UPDATED_VOUCHER_NO
        defaultTransactionHistoryShouldNotBeFound("voucherNo.in=" + UPDATED_VOUCHER_NO);
    }

    @Test
    @Transactional
    public void getAllTransactionHistoriesByVoucherNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList where voucherNo is not null
        defaultTransactionHistoryShouldBeFound("voucherNo.specified=true");

        // Get all the transactionHistoryList where voucherNo is null
        defaultTransactionHistoryShouldNotBeFound("voucherNo.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionHistoriesByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList where amount equals to DEFAULT_AMOUNT
        defaultTransactionHistoryShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the transactionHistoryList where amount equals to UPDATED_AMOUNT
        defaultTransactionHistoryShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllTransactionHistoriesByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultTransactionHistoryShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the transactionHistoryList where amount equals to UPDATED_AMOUNT
        defaultTransactionHistoryShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllTransactionHistoriesByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList where amount is not null
        defaultTransactionHistoryShouldBeFound("amount.specified=true");

        // Get all the transactionHistoryList where amount is null
        defaultTransactionHistoryShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionHistoriesByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultTransactionHistoryShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the transactionHistoryList where amount is greater than or equal to UPDATED_AMOUNT
        defaultTransactionHistoryShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllTransactionHistoriesByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList where amount is less than or equal to DEFAULT_AMOUNT
        defaultTransactionHistoryShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the transactionHistoryList where amount is less than or equal to SMALLER_AMOUNT
        defaultTransactionHistoryShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllTransactionHistoriesByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList where amount is less than DEFAULT_AMOUNT
        defaultTransactionHistoryShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the transactionHistoryList where amount is less than UPDATED_AMOUNT
        defaultTransactionHistoryShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllTransactionHistoriesByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList where amount is greater than DEFAULT_AMOUNT
        defaultTransactionHistoryShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the transactionHistoryList where amount is greater than SMALLER_AMOUNT
        defaultTransactionHistoryShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllTransactionHistoriesByDateCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList where dateCreated equals to DEFAULT_DATE_CREATED
        defaultTransactionHistoryShouldBeFound("dateCreated.equals=" + DEFAULT_DATE_CREATED);

        // Get all the transactionHistoryList where dateCreated equals to UPDATED_DATE_CREATED
        defaultTransactionHistoryShouldNotBeFound("dateCreated.equals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllTransactionHistoriesByDateCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList where dateCreated in DEFAULT_DATE_CREATED or UPDATED_DATE_CREATED
        defaultTransactionHistoryShouldBeFound("dateCreated.in=" + DEFAULT_DATE_CREATED + "," + UPDATED_DATE_CREATED);

        // Get all the transactionHistoryList where dateCreated equals to UPDATED_DATE_CREATED
        defaultTransactionHistoryShouldNotBeFound("dateCreated.in=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllTransactionHistoriesByDateCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList where dateCreated is not null
        defaultTransactionHistoryShouldBeFound("dateCreated.specified=true");

        // Get all the transactionHistoryList where dateCreated is null
        defaultTransactionHistoryShouldNotBeFound("dateCreated.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionHistoriesByTagIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList where tag equals to DEFAULT_TAG
        defaultTransactionHistoryShouldBeFound("tag.equals=" + DEFAULT_TAG);

        // Get all the transactionHistoryList where tag equals to UPDATED_TAG
        defaultTransactionHistoryShouldNotBeFound("tag.equals=" + UPDATED_TAG);
    }

    @Test
    @Transactional
    public void getAllTransactionHistoriesByTagIsInShouldWork() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList where tag in DEFAULT_TAG or UPDATED_TAG
        defaultTransactionHistoryShouldBeFound("tag.in=" + DEFAULT_TAG + "," + UPDATED_TAG);

        // Get all the transactionHistoryList where tag equals to UPDATED_TAG
        defaultTransactionHistoryShouldNotBeFound("tag.in=" + UPDATED_TAG);
    }

    @Test
    @Transactional
    public void getAllTransactionHistoriesByTagIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList where tag is not null
        defaultTransactionHistoryShouldBeFound("tag.specified=true");

        // Get all the transactionHistoryList where tag is null
        defaultTransactionHistoryShouldNotBeFound("tag.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionHistoriesByCustomerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList where customerId equals to DEFAULT_CUSTOMER_ID
        defaultTransactionHistoryShouldBeFound("customerId.equals=" + DEFAULT_CUSTOMER_ID);

        // Get all the transactionHistoryList where customerId equals to UPDATED_CUSTOMER_ID
        defaultTransactionHistoryShouldNotBeFound("customerId.equals=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    public void getAllTransactionHistoriesByCustomerIdIsInShouldWork() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList where customerId in DEFAULT_CUSTOMER_ID or UPDATED_CUSTOMER_ID
        defaultTransactionHistoryShouldBeFound("customerId.in=" + DEFAULT_CUSTOMER_ID + "," + UPDATED_CUSTOMER_ID);

        // Get all the transactionHistoryList where customerId equals to UPDATED_CUSTOMER_ID
        defaultTransactionHistoryShouldNotBeFound("customerId.in=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    public void getAllTransactionHistoriesByCustomerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList where customerId is not null
        defaultTransactionHistoryShouldBeFound("customerId.specified=true");

        // Get all the transactionHistoryList where customerId is null
        defaultTransactionHistoryShouldNotBeFound("customerId.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionHistoriesByCustomerIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList where customerId is greater than or equal to DEFAULT_CUSTOMER_ID
        defaultTransactionHistoryShouldBeFound("customerId.greaterThanOrEqual=" + DEFAULT_CUSTOMER_ID);

        // Get all the transactionHistoryList where customerId is greater than or equal to UPDATED_CUSTOMER_ID
        defaultTransactionHistoryShouldNotBeFound("customerId.greaterThanOrEqual=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    public void getAllTransactionHistoriesByCustomerIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList where customerId is less than or equal to DEFAULT_CUSTOMER_ID
        defaultTransactionHistoryShouldBeFound("customerId.lessThanOrEqual=" + DEFAULT_CUSTOMER_ID);

        // Get all the transactionHistoryList where customerId is less than or equal to SMALLER_CUSTOMER_ID
        defaultTransactionHistoryShouldNotBeFound("customerId.lessThanOrEqual=" + SMALLER_CUSTOMER_ID);
    }

    @Test
    @Transactional
    public void getAllTransactionHistoriesByCustomerIdIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList where customerId is less than DEFAULT_CUSTOMER_ID
        defaultTransactionHistoryShouldNotBeFound("customerId.lessThan=" + DEFAULT_CUSTOMER_ID);

        // Get all the transactionHistoryList where customerId is less than UPDATED_CUSTOMER_ID
        defaultTransactionHistoryShouldBeFound("customerId.lessThan=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    public void getAllTransactionHistoriesByCustomerIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList where customerId is greater than DEFAULT_CUSTOMER_ID
        defaultTransactionHistoryShouldNotBeFound("customerId.greaterThan=" + DEFAULT_CUSTOMER_ID);

        // Get all the transactionHistoryList where customerId is greater than SMALLER_CUSTOMER_ID
        defaultTransactionHistoryShouldBeFound("customerId.greaterThan=" + SMALLER_CUSTOMER_ID);
    }


    @Test
    @Transactional
    public void getAllTransactionHistoriesByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList where addedBy equals to DEFAULT_ADDED_BY
        defaultTransactionHistoryShouldBeFound("addedBy.equals=" + DEFAULT_ADDED_BY);

        // Get all the transactionHistoryList where addedBy equals to UPDATED_ADDED_BY
        defaultTransactionHistoryShouldNotBeFound("addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    public void getAllTransactionHistoriesByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList where addedBy in DEFAULT_ADDED_BY or UPDATED_ADDED_BY
        defaultTransactionHistoryShouldBeFound("addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY);

        // Get all the transactionHistoryList where addedBy equals to UPDATED_ADDED_BY
        defaultTransactionHistoryShouldNotBeFound("addedBy.in=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    public void getAllTransactionHistoriesByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        // Get all the transactionHistoryList where addedBy is not null
        defaultTransactionHistoryShouldBeFound("addedBy.specified=true");

        // Get all the transactionHistoryList where addedBy is null
        defaultTransactionHistoryShouldNotBeFound("addedBy.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransactionHistoryShouldBeFound(String filter) throws Exception {
        restTransactionHistoryMockMvc.perform(get("/api/transaction-histories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].voucherNo").value(hasItem(DEFAULT_VOUCHER_NO)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG.toString())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID.intValue())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));

        // Check, that the count call also returns 1
        restTransactionHistoryMockMvc.perform(get("/api/transaction-histories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransactionHistoryShouldNotBeFound(String filter) throws Exception {
        restTransactionHistoryMockMvc.perform(get("/api/transaction-histories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransactionHistoryMockMvc.perform(get("/api/transaction-histories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTransactionHistory() throws Exception {
        // Get the transactionHistory
        restTransactionHistoryMockMvc.perform(get("/api/transaction-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransactionHistory() throws Exception {
        // Initialize the database
        transactionHistoryService.save(transactionHistory);

        int databaseSizeBeforeUpdate = transactionHistoryRepository.findAll().size();

        // Update the transactionHistory
        TransactionHistory updatedTransactionHistory = transactionHistoryRepository.findById(transactionHistory.getId()).get();
        // Disconnect from session so that the updates on updatedTransactionHistory are not directly saved in db
        em.detach(updatedTransactionHistory);
        updatedTransactionHistory
            .voucherNo(UPDATED_VOUCHER_NO)
            .amount(UPDATED_AMOUNT)
            .dateCreated(UPDATED_DATE_CREATED)
            .tag(UPDATED_TAG)
            .customerId(UPDATED_CUSTOMER_ID)
            .addedBy(UPDATED_ADDED_BY);

        restTransactionHistoryMockMvc.perform(put("/api/transaction-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTransactionHistory)))
            .andExpect(status().isOk());

        // Validate the TransactionHistory in the database
        List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.findAll();
        assertThat(transactionHistoryList).hasSize(databaseSizeBeforeUpdate);
        TransactionHistory testTransactionHistory = transactionHistoryList.get(transactionHistoryList.size() - 1);
        assertThat(testTransactionHistory.getVoucherNo()).isEqualTo(UPDATED_VOUCHER_NO);
        assertThat(testTransactionHistory.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testTransactionHistory.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testTransactionHistory.getTag()).isEqualTo(UPDATED_TAG);
        assertThat(testTransactionHistory.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testTransactionHistory.getAddedBy()).isEqualTo(UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingTransactionHistory() throws Exception {
        int databaseSizeBeforeUpdate = transactionHistoryRepository.findAll().size();

        // Create the TransactionHistory

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionHistoryMockMvc.perform(put("/api/transaction-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionHistory)))
            .andExpect(status().isBadRequest());

        // Validate the TransactionHistory in the database
        List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.findAll();
        assertThat(transactionHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTransactionHistory() throws Exception {
        // Initialize the database
        transactionHistoryService.save(transactionHistory);

        int databaseSizeBeforeDelete = transactionHistoryRepository.findAll().size();

        // Delete the transactionHistory
        restTransactionHistoryMockMvc.perform(delete("/api/transaction-histories/{id}", transactionHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.findAll();
        assertThat(transactionHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionHistory.class);
        TransactionHistory transactionHistory1 = new TransactionHistory();
        transactionHistory1.setId(1L);
        TransactionHistory transactionHistory2 = new TransactionHistory();
        transactionHistory2.setId(transactionHistory1.getId());
        assertThat(transactionHistory1).isEqualTo(transactionHistory2);
        transactionHistory2.setId(2L);
        assertThat(transactionHistory1).isNotEqualTo(transactionHistory2);
        transactionHistory1.setId(null);
        assertThat(transactionHistory1).isNotEqualTo(transactionHistory2);
    }
}
