package com.meftaul.aurum.web.rest;

import com.meftaul.aurum.AurumApp;
import com.meftaul.aurum.domain.AurumService;
import com.meftaul.aurum.domain.Voucher;
import com.meftaul.aurum.repository.AurumServiceRepository;
import com.meftaul.aurum.service.AurumServiceService;
import com.meftaul.aurum.web.rest.errors.ExceptionTranslator;
import com.meftaul.aurum.service.dto.AurumServiceCriteria;
import com.meftaul.aurum.service.AurumServiceQueryService;

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
import java.util.List;

import static com.meftaul.aurum.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.meftaul.aurum.domain.enumeration.Alloy;
/**
 * Integration tests for the {@link AurumServiceResource} REST controller.
 */
@SpringBootTest(classes = AurumApp.class)
public class AurumServiceResourceIT {

    private static final String DEFAULT_SERVICE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 0;
    private static final Integer UPDATED_QUANTITY = 1;
    private static final Integer SMALLER_QUANTITY = 0 - 1;

    private static final BigDecimal DEFAULT_WEIGHT = new BigDecimal(0);
    private static final BigDecimal UPDATED_WEIGHT = new BigDecimal(1);
    private static final BigDecimal SMALLER_WEIGHT = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_RATE = new BigDecimal(0);
    private static final BigDecimal UPDATED_RATE = new BigDecimal(1);
    private static final BigDecimal SMALLER_RATE = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_SERVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_KARAT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_KARAT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_EXPECTED_KARAT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_EXPECTED_KARAT_TYPE = "BBBBBBBBBB";

    private static final Alloy DEFAULT_ADDED_ALLOY = Alloy.AU;
    private static final Alloy UPDATED_ADDED_ALLOY = Alloy.SI;

    private static final BigDecimal DEFAULT_ALLOY_QUANTITY = new BigDecimal(0);
    private static final BigDecimal UPDATED_ALLOY_QUANTITY = new BigDecimal(1);
    private static final BigDecimal SMALLER_ALLOY_QUANTITY = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_SERVICE_CHARGE = new BigDecimal(0);
    private static final BigDecimal UPDATED_SERVICE_CHARGE = new BigDecimal(1);
    private static final BigDecimal SMALLER_SERVICE_CHARGE = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_FREE_CHECK = new BigDecimal(0);
    private static final BigDecimal UPDATED_FREE_CHECK = new BigDecimal(1);
    private static final BigDecimal SMALLER_FREE_CHECK = new BigDecimal(0 - 1);

    private static final String DEFAULT_HALL_MARKED_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_HALL_MARKED_TEXT = "BBBBBBBBBB";

    @Autowired
    private AurumServiceRepository aurumServiceRepository;

    @Autowired
    private AurumServiceService aurumServiceService;

    @Autowired
    private AurumServiceQueryService aurumServiceQueryService;

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

    private MockMvc restAurumServiceMockMvc;

    private AurumService aurumService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AurumServiceResource aurumServiceResource = new AurumServiceResource(aurumServiceService, aurumServiceQueryService);
        this.restAurumServiceMockMvc = MockMvcBuilders.standaloneSetup(aurumServiceResource)
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
    public static AurumService createEntity(EntityManager em) {
        AurumService aurumService = new AurumService()
            .serviceType(DEFAULT_SERVICE_TYPE)
            .itemName(DEFAULT_ITEM_NAME)
            .quantity(DEFAULT_QUANTITY)
            .weight(DEFAULT_WEIGHT)
            .rate(DEFAULT_RATE)
            .amount(DEFAULT_AMOUNT)
            .serviceName(DEFAULT_SERVICE_NAME)
            .karatType(DEFAULT_KARAT_TYPE)
            .expectedKaratType(DEFAULT_EXPECTED_KARAT_TYPE)
            .addedAlloy(DEFAULT_ADDED_ALLOY)
            .alloyQuantity(DEFAULT_ALLOY_QUANTITY)
            .serviceCharge(DEFAULT_SERVICE_CHARGE)
            .freeCheck(DEFAULT_FREE_CHECK)
            .hallMarkedText(DEFAULT_HALL_MARKED_TEXT);
        return aurumService;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AurumService createUpdatedEntity(EntityManager em) {
        AurumService aurumService = new AurumService()
            .serviceType(UPDATED_SERVICE_TYPE)
            .itemName(UPDATED_ITEM_NAME)
            .quantity(UPDATED_QUANTITY)
            .weight(UPDATED_WEIGHT)
            .rate(UPDATED_RATE)
            .amount(UPDATED_AMOUNT)
            .serviceName(UPDATED_SERVICE_NAME)
            .karatType(UPDATED_KARAT_TYPE)
            .expectedKaratType(UPDATED_EXPECTED_KARAT_TYPE)
            .addedAlloy(UPDATED_ADDED_ALLOY)
            .alloyQuantity(UPDATED_ALLOY_QUANTITY)
            .serviceCharge(UPDATED_SERVICE_CHARGE)
            .freeCheck(UPDATED_FREE_CHECK)
            .hallMarkedText(UPDATED_HALL_MARKED_TEXT);
        return aurumService;
    }

    @BeforeEach
    public void initTest() {
        aurumService = createEntity(em);
    }

    @Test
    @Transactional
    public void createAurumService() throws Exception {
        int databaseSizeBeforeCreate = aurumServiceRepository.findAll().size();

        // Create the AurumService
        restAurumServiceMockMvc.perform(post("/api/aurum-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aurumService)))
            .andExpect(status().isCreated());

        // Validate the AurumService in the database
        List<AurumService> aurumServiceList = aurumServiceRepository.findAll();
        assertThat(aurumServiceList).hasSize(databaseSizeBeforeCreate + 1);
        AurumService testAurumService = aurumServiceList.get(aurumServiceList.size() - 1);
        assertThat(testAurumService.getServiceType()).isEqualTo(DEFAULT_SERVICE_TYPE);
        assertThat(testAurumService.getItemName()).isEqualTo(DEFAULT_ITEM_NAME);
        assertThat(testAurumService.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testAurumService.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testAurumService.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testAurumService.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testAurumService.getServiceName()).isEqualTo(DEFAULT_SERVICE_NAME);
        assertThat(testAurumService.getKaratType()).isEqualTo(DEFAULT_KARAT_TYPE);
        assertThat(testAurumService.getExpectedKaratType()).isEqualTo(DEFAULT_EXPECTED_KARAT_TYPE);
        assertThat(testAurumService.getAddedAlloy()).isEqualTo(DEFAULT_ADDED_ALLOY);
        assertThat(testAurumService.getAlloyQuantity()).isEqualTo(DEFAULT_ALLOY_QUANTITY);
        assertThat(testAurumService.getServiceCharge()).isEqualTo(DEFAULT_SERVICE_CHARGE);
        assertThat(testAurumService.getFreeCheck()).isEqualTo(DEFAULT_FREE_CHECK);
        assertThat(testAurumService.getHallMarkedText()).isEqualTo(DEFAULT_HALL_MARKED_TEXT);
    }

    @Test
    @Transactional
    public void createAurumServiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = aurumServiceRepository.findAll().size();

        // Create the AurumService with an existing ID
        aurumService.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAurumServiceMockMvc.perform(post("/api/aurum-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aurumService)))
            .andExpect(status().isBadRequest());

        // Validate the AurumService in the database
        List<AurumService> aurumServiceList = aurumServiceRepository.findAll();
        assertThat(aurumServiceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAurumServices() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList
        restAurumServiceMockMvc.perform(get("/api/aurum-services?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aurumService.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceType").value(hasItem(DEFAULT_SERVICE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.intValue())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME.toString())))
            .andExpect(jsonPath("$.[*].karatType").value(hasItem(DEFAULT_KARAT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].expectedKaratType").value(hasItem(DEFAULT_EXPECTED_KARAT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].addedAlloy").value(hasItem(DEFAULT_ADDED_ALLOY.toString())))
            .andExpect(jsonPath("$.[*].alloyQuantity").value(hasItem(DEFAULT_ALLOY_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].serviceCharge").value(hasItem(DEFAULT_SERVICE_CHARGE.intValue())))
            .andExpect(jsonPath("$.[*].freeCheck").value(hasItem(DEFAULT_FREE_CHECK.intValue())))
            .andExpect(jsonPath("$.[*].hallMarkedText").value(hasItem(DEFAULT_HALL_MARKED_TEXT.toString())));
    }
    
    @Test
    @Transactional
    public void getAurumService() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get the aurumService
        restAurumServiceMockMvc.perform(get("/api/aurum-services/{id}", aurumService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(aurumService.getId().intValue()))
            .andExpect(jsonPath("$.serviceType").value(DEFAULT_SERVICE_TYPE.toString()))
            .andExpect(jsonPath("$.itemName").value(DEFAULT_ITEM_NAME.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT.intValue()))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.serviceName").value(DEFAULT_SERVICE_NAME.toString()))
            .andExpect(jsonPath("$.karatType").value(DEFAULT_KARAT_TYPE.toString()))
            .andExpect(jsonPath("$.expectedKaratType").value(DEFAULT_EXPECTED_KARAT_TYPE.toString()))
            .andExpect(jsonPath("$.addedAlloy").value(DEFAULT_ADDED_ALLOY.toString()))
            .andExpect(jsonPath("$.alloyQuantity").value(DEFAULT_ALLOY_QUANTITY.intValue()))
            .andExpect(jsonPath("$.serviceCharge").value(DEFAULT_SERVICE_CHARGE.intValue()))
            .andExpect(jsonPath("$.freeCheck").value(DEFAULT_FREE_CHECK.intValue()))
            .andExpect(jsonPath("$.hallMarkedText").value(DEFAULT_HALL_MARKED_TEXT.toString()));
    }

    @Test
    @Transactional
    public void getAllAurumServicesByServiceTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceType equals to DEFAULT_SERVICE_TYPE
        defaultAurumServiceShouldBeFound("serviceType.equals=" + DEFAULT_SERVICE_TYPE);

        // Get all the aurumServiceList where serviceType equals to UPDATED_SERVICE_TYPE
        defaultAurumServiceShouldNotBeFound("serviceType.equals=" + UPDATED_SERVICE_TYPE);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByServiceTypeIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceType in DEFAULT_SERVICE_TYPE or UPDATED_SERVICE_TYPE
        defaultAurumServiceShouldBeFound("serviceType.in=" + DEFAULT_SERVICE_TYPE + "," + UPDATED_SERVICE_TYPE);

        // Get all the aurumServiceList where serviceType equals to UPDATED_SERVICE_TYPE
        defaultAurumServiceShouldNotBeFound("serviceType.in=" + UPDATED_SERVICE_TYPE);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByServiceTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceType is not null
        defaultAurumServiceShouldBeFound("serviceType.specified=true");

        // Get all the aurumServiceList where serviceType is null
        defaultAurumServiceShouldNotBeFound("serviceType.specified=false");
    }

    @Test
    @Transactional
    public void getAllAurumServicesByItemNameIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where itemName equals to DEFAULT_ITEM_NAME
        defaultAurumServiceShouldBeFound("itemName.equals=" + DEFAULT_ITEM_NAME);

        // Get all the aurumServiceList where itemName equals to UPDATED_ITEM_NAME
        defaultAurumServiceShouldNotBeFound("itemName.equals=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByItemNameIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where itemName in DEFAULT_ITEM_NAME or UPDATED_ITEM_NAME
        defaultAurumServiceShouldBeFound("itemName.in=" + DEFAULT_ITEM_NAME + "," + UPDATED_ITEM_NAME);

        // Get all the aurumServiceList where itemName equals to UPDATED_ITEM_NAME
        defaultAurumServiceShouldNotBeFound("itemName.in=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByItemNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where itemName is not null
        defaultAurumServiceShouldBeFound("itemName.specified=true");

        // Get all the aurumServiceList where itemName is null
        defaultAurumServiceShouldNotBeFound("itemName.specified=false");
    }

    @Test
    @Transactional
    public void getAllAurumServicesByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where quantity equals to DEFAULT_QUANTITY
        defaultAurumServiceShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the aurumServiceList where quantity equals to UPDATED_QUANTITY
        defaultAurumServiceShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultAurumServiceShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the aurumServiceList where quantity equals to UPDATED_QUANTITY
        defaultAurumServiceShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where quantity is not null
        defaultAurumServiceShouldBeFound("quantity.specified=true");

        // Get all the aurumServiceList where quantity is null
        defaultAurumServiceShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllAurumServicesByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where quantity is greater than or equal to DEFAULT_QUANTITY
        defaultAurumServiceShouldBeFound("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the aurumServiceList where quantity is greater than or equal to UPDATED_QUANTITY
        defaultAurumServiceShouldNotBeFound("quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where quantity is less than or equal to DEFAULT_QUANTITY
        defaultAurumServiceShouldBeFound("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the aurumServiceList where quantity is less than or equal to SMALLER_QUANTITY
        defaultAurumServiceShouldNotBeFound("quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where quantity is less than DEFAULT_QUANTITY
        defaultAurumServiceShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the aurumServiceList where quantity is less than UPDATED_QUANTITY
        defaultAurumServiceShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where quantity is greater than DEFAULT_QUANTITY
        defaultAurumServiceShouldNotBeFound("quantity.greaterThan=" + DEFAULT_QUANTITY);

        // Get all the aurumServiceList where quantity is greater than SMALLER_QUANTITY
        defaultAurumServiceShouldBeFound("quantity.greaterThan=" + SMALLER_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllAurumServicesByWeightIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weight equals to DEFAULT_WEIGHT
        defaultAurumServiceShouldBeFound("weight.equals=" + DEFAULT_WEIGHT);

        // Get all the aurumServiceList where weight equals to UPDATED_WEIGHT
        defaultAurumServiceShouldNotBeFound("weight.equals=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByWeightIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weight in DEFAULT_WEIGHT or UPDATED_WEIGHT
        defaultAurumServiceShouldBeFound("weight.in=" + DEFAULT_WEIGHT + "," + UPDATED_WEIGHT);

        // Get all the aurumServiceList where weight equals to UPDATED_WEIGHT
        defaultAurumServiceShouldNotBeFound("weight.in=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByWeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weight is not null
        defaultAurumServiceShouldBeFound("weight.specified=true");

        // Get all the aurumServiceList where weight is null
        defaultAurumServiceShouldNotBeFound("weight.specified=false");
    }

    @Test
    @Transactional
    public void getAllAurumServicesByWeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weight is greater than or equal to DEFAULT_WEIGHT
        defaultAurumServiceShouldBeFound("weight.greaterThanOrEqual=" + DEFAULT_WEIGHT);

        // Get all the aurumServiceList where weight is greater than or equal to UPDATED_WEIGHT
        defaultAurumServiceShouldNotBeFound("weight.greaterThanOrEqual=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByWeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weight is less than or equal to DEFAULT_WEIGHT
        defaultAurumServiceShouldBeFound("weight.lessThanOrEqual=" + DEFAULT_WEIGHT);

        // Get all the aurumServiceList where weight is less than or equal to SMALLER_WEIGHT
        defaultAurumServiceShouldNotBeFound("weight.lessThanOrEqual=" + SMALLER_WEIGHT);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByWeightIsLessThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weight is less than DEFAULT_WEIGHT
        defaultAurumServiceShouldNotBeFound("weight.lessThan=" + DEFAULT_WEIGHT);

        // Get all the aurumServiceList where weight is less than UPDATED_WEIGHT
        defaultAurumServiceShouldBeFound("weight.lessThan=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByWeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weight is greater than DEFAULT_WEIGHT
        defaultAurumServiceShouldNotBeFound("weight.greaterThan=" + DEFAULT_WEIGHT);

        // Get all the aurumServiceList where weight is greater than SMALLER_WEIGHT
        defaultAurumServiceShouldBeFound("weight.greaterThan=" + SMALLER_WEIGHT);
    }


    @Test
    @Transactional
    public void getAllAurumServicesByRateIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where rate equals to DEFAULT_RATE
        defaultAurumServiceShouldBeFound("rate.equals=" + DEFAULT_RATE);

        // Get all the aurumServiceList where rate equals to UPDATED_RATE
        defaultAurumServiceShouldNotBeFound("rate.equals=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByRateIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where rate in DEFAULT_RATE or UPDATED_RATE
        defaultAurumServiceShouldBeFound("rate.in=" + DEFAULT_RATE + "," + UPDATED_RATE);

        // Get all the aurumServiceList where rate equals to UPDATED_RATE
        defaultAurumServiceShouldNotBeFound("rate.in=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where rate is not null
        defaultAurumServiceShouldBeFound("rate.specified=true");

        // Get all the aurumServiceList where rate is null
        defaultAurumServiceShouldNotBeFound("rate.specified=false");
    }

    @Test
    @Transactional
    public void getAllAurumServicesByRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where rate is greater than or equal to DEFAULT_RATE
        defaultAurumServiceShouldBeFound("rate.greaterThanOrEqual=" + DEFAULT_RATE);

        // Get all the aurumServiceList where rate is greater than or equal to UPDATED_RATE
        defaultAurumServiceShouldNotBeFound("rate.greaterThanOrEqual=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where rate is less than or equal to DEFAULT_RATE
        defaultAurumServiceShouldBeFound("rate.lessThanOrEqual=" + DEFAULT_RATE);

        // Get all the aurumServiceList where rate is less than or equal to SMALLER_RATE
        defaultAurumServiceShouldNotBeFound("rate.lessThanOrEqual=" + SMALLER_RATE);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByRateIsLessThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where rate is less than DEFAULT_RATE
        defaultAurumServiceShouldNotBeFound("rate.lessThan=" + DEFAULT_RATE);

        // Get all the aurumServiceList where rate is less than UPDATED_RATE
        defaultAurumServiceShouldBeFound("rate.lessThan=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where rate is greater than DEFAULT_RATE
        defaultAurumServiceShouldNotBeFound("rate.greaterThan=" + DEFAULT_RATE);

        // Get all the aurumServiceList where rate is greater than SMALLER_RATE
        defaultAurumServiceShouldBeFound("rate.greaterThan=" + SMALLER_RATE);
    }


    @Test
    @Transactional
    public void getAllAurumServicesByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where amount equals to DEFAULT_AMOUNT
        defaultAurumServiceShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the aurumServiceList where amount equals to UPDATED_AMOUNT
        defaultAurumServiceShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultAurumServiceShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the aurumServiceList where amount equals to UPDATED_AMOUNT
        defaultAurumServiceShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where amount is not null
        defaultAurumServiceShouldBeFound("amount.specified=true");

        // Get all the aurumServiceList where amount is null
        defaultAurumServiceShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllAurumServicesByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultAurumServiceShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the aurumServiceList where amount is greater than or equal to UPDATED_AMOUNT
        defaultAurumServiceShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where amount is less than or equal to DEFAULT_AMOUNT
        defaultAurumServiceShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the aurumServiceList where amount is less than or equal to SMALLER_AMOUNT
        defaultAurumServiceShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where amount is less than DEFAULT_AMOUNT
        defaultAurumServiceShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the aurumServiceList where amount is less than UPDATED_AMOUNT
        defaultAurumServiceShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where amount is greater than DEFAULT_AMOUNT
        defaultAurumServiceShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the aurumServiceList where amount is greater than SMALLER_AMOUNT
        defaultAurumServiceShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllAurumServicesByServiceNameIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceName equals to DEFAULT_SERVICE_NAME
        defaultAurumServiceShouldBeFound("serviceName.equals=" + DEFAULT_SERVICE_NAME);

        // Get all the aurumServiceList where serviceName equals to UPDATED_SERVICE_NAME
        defaultAurumServiceShouldNotBeFound("serviceName.equals=" + UPDATED_SERVICE_NAME);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByServiceNameIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceName in DEFAULT_SERVICE_NAME or UPDATED_SERVICE_NAME
        defaultAurumServiceShouldBeFound("serviceName.in=" + DEFAULT_SERVICE_NAME + "," + UPDATED_SERVICE_NAME);

        // Get all the aurumServiceList where serviceName equals to UPDATED_SERVICE_NAME
        defaultAurumServiceShouldNotBeFound("serviceName.in=" + UPDATED_SERVICE_NAME);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByServiceNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceName is not null
        defaultAurumServiceShouldBeFound("serviceName.specified=true");

        // Get all the aurumServiceList where serviceName is null
        defaultAurumServiceShouldNotBeFound("serviceName.specified=false");
    }

    @Test
    @Transactional
    public void getAllAurumServicesByKaratTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where karatType equals to DEFAULT_KARAT_TYPE
        defaultAurumServiceShouldBeFound("karatType.equals=" + DEFAULT_KARAT_TYPE);

        // Get all the aurumServiceList where karatType equals to UPDATED_KARAT_TYPE
        defaultAurumServiceShouldNotBeFound("karatType.equals=" + UPDATED_KARAT_TYPE);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByKaratTypeIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where karatType in DEFAULT_KARAT_TYPE or UPDATED_KARAT_TYPE
        defaultAurumServiceShouldBeFound("karatType.in=" + DEFAULT_KARAT_TYPE + "," + UPDATED_KARAT_TYPE);

        // Get all the aurumServiceList where karatType equals to UPDATED_KARAT_TYPE
        defaultAurumServiceShouldNotBeFound("karatType.in=" + UPDATED_KARAT_TYPE);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByKaratTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where karatType is not null
        defaultAurumServiceShouldBeFound("karatType.specified=true");

        // Get all the aurumServiceList where karatType is null
        defaultAurumServiceShouldNotBeFound("karatType.specified=false");
    }

    @Test
    @Transactional
    public void getAllAurumServicesByExpectedKaratTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where expectedKaratType equals to DEFAULT_EXPECTED_KARAT_TYPE
        defaultAurumServiceShouldBeFound("expectedKaratType.equals=" + DEFAULT_EXPECTED_KARAT_TYPE);

        // Get all the aurumServiceList where expectedKaratType equals to UPDATED_EXPECTED_KARAT_TYPE
        defaultAurumServiceShouldNotBeFound("expectedKaratType.equals=" + UPDATED_EXPECTED_KARAT_TYPE);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByExpectedKaratTypeIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where expectedKaratType in DEFAULT_EXPECTED_KARAT_TYPE or UPDATED_EXPECTED_KARAT_TYPE
        defaultAurumServiceShouldBeFound("expectedKaratType.in=" + DEFAULT_EXPECTED_KARAT_TYPE + "," + UPDATED_EXPECTED_KARAT_TYPE);

        // Get all the aurumServiceList where expectedKaratType equals to UPDATED_EXPECTED_KARAT_TYPE
        defaultAurumServiceShouldNotBeFound("expectedKaratType.in=" + UPDATED_EXPECTED_KARAT_TYPE);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByExpectedKaratTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where expectedKaratType is not null
        defaultAurumServiceShouldBeFound("expectedKaratType.specified=true");

        // Get all the aurumServiceList where expectedKaratType is null
        defaultAurumServiceShouldNotBeFound("expectedKaratType.specified=false");
    }

    @Test
    @Transactional
    public void getAllAurumServicesByAddedAlloyIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where addedAlloy equals to DEFAULT_ADDED_ALLOY
        defaultAurumServiceShouldBeFound("addedAlloy.equals=" + DEFAULT_ADDED_ALLOY);

        // Get all the aurumServiceList where addedAlloy equals to UPDATED_ADDED_ALLOY
        defaultAurumServiceShouldNotBeFound("addedAlloy.equals=" + UPDATED_ADDED_ALLOY);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByAddedAlloyIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where addedAlloy in DEFAULT_ADDED_ALLOY or UPDATED_ADDED_ALLOY
        defaultAurumServiceShouldBeFound("addedAlloy.in=" + DEFAULT_ADDED_ALLOY + "," + UPDATED_ADDED_ALLOY);

        // Get all the aurumServiceList where addedAlloy equals to UPDATED_ADDED_ALLOY
        defaultAurumServiceShouldNotBeFound("addedAlloy.in=" + UPDATED_ADDED_ALLOY);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByAddedAlloyIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where addedAlloy is not null
        defaultAurumServiceShouldBeFound("addedAlloy.specified=true");

        // Get all the aurumServiceList where addedAlloy is null
        defaultAurumServiceShouldNotBeFound("addedAlloy.specified=false");
    }

    @Test
    @Transactional
    public void getAllAurumServicesByAlloyQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where alloyQuantity equals to DEFAULT_ALLOY_QUANTITY
        defaultAurumServiceShouldBeFound("alloyQuantity.equals=" + DEFAULT_ALLOY_QUANTITY);

        // Get all the aurumServiceList where alloyQuantity equals to UPDATED_ALLOY_QUANTITY
        defaultAurumServiceShouldNotBeFound("alloyQuantity.equals=" + UPDATED_ALLOY_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByAlloyQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where alloyQuantity in DEFAULT_ALLOY_QUANTITY or UPDATED_ALLOY_QUANTITY
        defaultAurumServiceShouldBeFound("alloyQuantity.in=" + DEFAULT_ALLOY_QUANTITY + "," + UPDATED_ALLOY_QUANTITY);

        // Get all the aurumServiceList where alloyQuantity equals to UPDATED_ALLOY_QUANTITY
        defaultAurumServiceShouldNotBeFound("alloyQuantity.in=" + UPDATED_ALLOY_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByAlloyQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where alloyQuantity is not null
        defaultAurumServiceShouldBeFound("alloyQuantity.specified=true");

        // Get all the aurumServiceList where alloyQuantity is null
        defaultAurumServiceShouldNotBeFound("alloyQuantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllAurumServicesByAlloyQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where alloyQuantity is greater than or equal to DEFAULT_ALLOY_QUANTITY
        defaultAurumServiceShouldBeFound("alloyQuantity.greaterThanOrEqual=" + DEFAULT_ALLOY_QUANTITY);

        // Get all the aurumServiceList where alloyQuantity is greater than or equal to UPDATED_ALLOY_QUANTITY
        defaultAurumServiceShouldNotBeFound("alloyQuantity.greaterThanOrEqual=" + UPDATED_ALLOY_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByAlloyQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where alloyQuantity is less than or equal to DEFAULT_ALLOY_QUANTITY
        defaultAurumServiceShouldBeFound("alloyQuantity.lessThanOrEqual=" + DEFAULT_ALLOY_QUANTITY);

        // Get all the aurumServiceList where alloyQuantity is less than or equal to SMALLER_ALLOY_QUANTITY
        defaultAurumServiceShouldNotBeFound("alloyQuantity.lessThanOrEqual=" + SMALLER_ALLOY_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByAlloyQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where alloyQuantity is less than DEFAULT_ALLOY_QUANTITY
        defaultAurumServiceShouldNotBeFound("alloyQuantity.lessThan=" + DEFAULT_ALLOY_QUANTITY);

        // Get all the aurumServiceList where alloyQuantity is less than UPDATED_ALLOY_QUANTITY
        defaultAurumServiceShouldBeFound("alloyQuantity.lessThan=" + UPDATED_ALLOY_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByAlloyQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where alloyQuantity is greater than DEFAULT_ALLOY_QUANTITY
        defaultAurumServiceShouldNotBeFound("alloyQuantity.greaterThan=" + DEFAULT_ALLOY_QUANTITY);

        // Get all the aurumServiceList where alloyQuantity is greater than SMALLER_ALLOY_QUANTITY
        defaultAurumServiceShouldBeFound("alloyQuantity.greaterThan=" + SMALLER_ALLOY_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllAurumServicesByServiceChargeIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceCharge equals to DEFAULT_SERVICE_CHARGE
        defaultAurumServiceShouldBeFound("serviceCharge.equals=" + DEFAULT_SERVICE_CHARGE);

        // Get all the aurumServiceList where serviceCharge equals to UPDATED_SERVICE_CHARGE
        defaultAurumServiceShouldNotBeFound("serviceCharge.equals=" + UPDATED_SERVICE_CHARGE);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByServiceChargeIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceCharge in DEFAULT_SERVICE_CHARGE or UPDATED_SERVICE_CHARGE
        defaultAurumServiceShouldBeFound("serviceCharge.in=" + DEFAULT_SERVICE_CHARGE + "," + UPDATED_SERVICE_CHARGE);

        // Get all the aurumServiceList where serviceCharge equals to UPDATED_SERVICE_CHARGE
        defaultAurumServiceShouldNotBeFound("serviceCharge.in=" + UPDATED_SERVICE_CHARGE);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByServiceChargeIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceCharge is not null
        defaultAurumServiceShouldBeFound("serviceCharge.specified=true");

        // Get all the aurumServiceList where serviceCharge is null
        defaultAurumServiceShouldNotBeFound("serviceCharge.specified=false");
    }

    @Test
    @Transactional
    public void getAllAurumServicesByServiceChargeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceCharge is greater than or equal to DEFAULT_SERVICE_CHARGE
        defaultAurumServiceShouldBeFound("serviceCharge.greaterThanOrEqual=" + DEFAULT_SERVICE_CHARGE);

        // Get all the aurumServiceList where serviceCharge is greater than or equal to UPDATED_SERVICE_CHARGE
        defaultAurumServiceShouldNotBeFound("serviceCharge.greaterThanOrEqual=" + UPDATED_SERVICE_CHARGE);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByServiceChargeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceCharge is less than or equal to DEFAULT_SERVICE_CHARGE
        defaultAurumServiceShouldBeFound("serviceCharge.lessThanOrEqual=" + DEFAULT_SERVICE_CHARGE);

        // Get all the aurumServiceList where serviceCharge is less than or equal to SMALLER_SERVICE_CHARGE
        defaultAurumServiceShouldNotBeFound("serviceCharge.lessThanOrEqual=" + SMALLER_SERVICE_CHARGE);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByServiceChargeIsLessThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceCharge is less than DEFAULT_SERVICE_CHARGE
        defaultAurumServiceShouldNotBeFound("serviceCharge.lessThan=" + DEFAULT_SERVICE_CHARGE);

        // Get all the aurumServiceList where serviceCharge is less than UPDATED_SERVICE_CHARGE
        defaultAurumServiceShouldBeFound("serviceCharge.lessThan=" + UPDATED_SERVICE_CHARGE);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByServiceChargeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceCharge is greater than DEFAULT_SERVICE_CHARGE
        defaultAurumServiceShouldNotBeFound("serviceCharge.greaterThan=" + DEFAULT_SERVICE_CHARGE);

        // Get all the aurumServiceList where serviceCharge is greater than SMALLER_SERVICE_CHARGE
        defaultAurumServiceShouldBeFound("serviceCharge.greaterThan=" + SMALLER_SERVICE_CHARGE);
    }


    @Test
    @Transactional
    public void getAllAurumServicesByFreeCheckIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where freeCheck equals to DEFAULT_FREE_CHECK
        defaultAurumServiceShouldBeFound("freeCheck.equals=" + DEFAULT_FREE_CHECK);

        // Get all the aurumServiceList where freeCheck equals to UPDATED_FREE_CHECK
        defaultAurumServiceShouldNotBeFound("freeCheck.equals=" + UPDATED_FREE_CHECK);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByFreeCheckIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where freeCheck in DEFAULT_FREE_CHECK or UPDATED_FREE_CHECK
        defaultAurumServiceShouldBeFound("freeCheck.in=" + DEFAULT_FREE_CHECK + "," + UPDATED_FREE_CHECK);

        // Get all the aurumServiceList where freeCheck equals to UPDATED_FREE_CHECK
        defaultAurumServiceShouldNotBeFound("freeCheck.in=" + UPDATED_FREE_CHECK);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByFreeCheckIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where freeCheck is not null
        defaultAurumServiceShouldBeFound("freeCheck.specified=true");

        // Get all the aurumServiceList where freeCheck is null
        defaultAurumServiceShouldNotBeFound("freeCheck.specified=false");
    }

    @Test
    @Transactional
    public void getAllAurumServicesByFreeCheckIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where freeCheck is greater than or equal to DEFAULT_FREE_CHECK
        defaultAurumServiceShouldBeFound("freeCheck.greaterThanOrEqual=" + DEFAULT_FREE_CHECK);

        // Get all the aurumServiceList where freeCheck is greater than or equal to UPDATED_FREE_CHECK
        defaultAurumServiceShouldNotBeFound("freeCheck.greaterThanOrEqual=" + UPDATED_FREE_CHECK);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByFreeCheckIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where freeCheck is less than or equal to DEFAULT_FREE_CHECK
        defaultAurumServiceShouldBeFound("freeCheck.lessThanOrEqual=" + DEFAULT_FREE_CHECK);

        // Get all the aurumServiceList where freeCheck is less than or equal to SMALLER_FREE_CHECK
        defaultAurumServiceShouldNotBeFound("freeCheck.lessThanOrEqual=" + SMALLER_FREE_CHECK);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByFreeCheckIsLessThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where freeCheck is less than DEFAULT_FREE_CHECK
        defaultAurumServiceShouldNotBeFound("freeCheck.lessThan=" + DEFAULT_FREE_CHECK);

        // Get all the aurumServiceList where freeCheck is less than UPDATED_FREE_CHECK
        defaultAurumServiceShouldBeFound("freeCheck.lessThan=" + UPDATED_FREE_CHECK);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByFreeCheckIsGreaterThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where freeCheck is greater than DEFAULT_FREE_CHECK
        defaultAurumServiceShouldNotBeFound("freeCheck.greaterThan=" + DEFAULT_FREE_CHECK);

        // Get all the aurumServiceList where freeCheck is greater than SMALLER_FREE_CHECK
        defaultAurumServiceShouldBeFound("freeCheck.greaterThan=" + SMALLER_FREE_CHECK);
    }


    @Test
    @Transactional
    public void getAllAurumServicesByHallMarkedTextIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where hallMarkedText equals to DEFAULT_HALL_MARKED_TEXT
        defaultAurumServiceShouldBeFound("hallMarkedText.equals=" + DEFAULT_HALL_MARKED_TEXT);

        // Get all the aurumServiceList where hallMarkedText equals to UPDATED_HALL_MARKED_TEXT
        defaultAurumServiceShouldNotBeFound("hallMarkedText.equals=" + UPDATED_HALL_MARKED_TEXT);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByHallMarkedTextIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where hallMarkedText in DEFAULT_HALL_MARKED_TEXT or UPDATED_HALL_MARKED_TEXT
        defaultAurumServiceShouldBeFound("hallMarkedText.in=" + DEFAULT_HALL_MARKED_TEXT + "," + UPDATED_HALL_MARKED_TEXT);

        // Get all the aurumServiceList where hallMarkedText equals to UPDATED_HALL_MARKED_TEXT
        defaultAurumServiceShouldNotBeFound("hallMarkedText.in=" + UPDATED_HALL_MARKED_TEXT);
    }

    @Test
    @Transactional
    public void getAllAurumServicesByHallMarkedTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where hallMarkedText is not null
        defaultAurumServiceShouldBeFound("hallMarkedText.specified=true");

        // Get all the aurumServiceList where hallMarkedText is null
        defaultAurumServiceShouldNotBeFound("hallMarkedText.specified=false");
    }

    @Test
    @Transactional
    public void getAllAurumServicesByVoucherIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);
        Voucher voucher = VoucherResourceIT.createEntity(em);
        em.persist(voucher);
        em.flush();
        aurumService.setVoucher(voucher);
        aurumServiceRepository.saveAndFlush(aurumService);
        Long voucherId = voucher.getId();

        // Get all the aurumServiceList where voucher equals to voucherId
        defaultAurumServiceShouldBeFound("voucherId.equals=" + voucherId);

        // Get all the aurumServiceList where voucher equals to voucherId + 1
        defaultAurumServiceShouldNotBeFound("voucherId.equals=" + (voucherId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAurumServiceShouldBeFound(String filter) throws Exception {
        restAurumServiceMockMvc.perform(get("/api/aurum-services?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aurumService.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceType").value(hasItem(DEFAULT_SERVICE_TYPE)))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.intValue())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME)))
            .andExpect(jsonPath("$.[*].karatType").value(hasItem(DEFAULT_KARAT_TYPE)))
            .andExpect(jsonPath("$.[*].expectedKaratType").value(hasItem(DEFAULT_EXPECTED_KARAT_TYPE)))
            .andExpect(jsonPath("$.[*].addedAlloy").value(hasItem(DEFAULT_ADDED_ALLOY.toString())))
            .andExpect(jsonPath("$.[*].alloyQuantity").value(hasItem(DEFAULT_ALLOY_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].serviceCharge").value(hasItem(DEFAULT_SERVICE_CHARGE.intValue())))
            .andExpect(jsonPath("$.[*].freeCheck").value(hasItem(DEFAULT_FREE_CHECK.intValue())))
            .andExpect(jsonPath("$.[*].hallMarkedText").value(hasItem(DEFAULT_HALL_MARKED_TEXT)));

        // Check, that the count call also returns 1
        restAurumServiceMockMvc.perform(get("/api/aurum-services/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAurumServiceShouldNotBeFound(String filter) throws Exception {
        restAurumServiceMockMvc.perform(get("/api/aurum-services?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAurumServiceMockMvc.perform(get("/api/aurum-services/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAurumService() throws Exception {
        // Get the aurumService
        restAurumServiceMockMvc.perform(get("/api/aurum-services/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAurumService() throws Exception {
        // Initialize the database
        aurumServiceService.save(aurumService);

        int databaseSizeBeforeUpdate = aurumServiceRepository.findAll().size();

        // Update the aurumService
        AurumService updatedAurumService = aurumServiceRepository.findById(aurumService.getId()).get();
        // Disconnect from session so that the updates on updatedAurumService are not directly saved in db
        em.detach(updatedAurumService);
        updatedAurumService
            .serviceType(UPDATED_SERVICE_TYPE)
            .itemName(UPDATED_ITEM_NAME)
            .quantity(UPDATED_QUANTITY)
            .weight(UPDATED_WEIGHT)
            .rate(UPDATED_RATE)
            .amount(UPDATED_AMOUNT)
            .serviceName(UPDATED_SERVICE_NAME)
            .karatType(UPDATED_KARAT_TYPE)
            .expectedKaratType(UPDATED_EXPECTED_KARAT_TYPE)
            .addedAlloy(UPDATED_ADDED_ALLOY)
            .alloyQuantity(UPDATED_ALLOY_QUANTITY)
            .serviceCharge(UPDATED_SERVICE_CHARGE)
            .freeCheck(UPDATED_FREE_CHECK)
            .hallMarkedText(UPDATED_HALL_MARKED_TEXT);

        restAurumServiceMockMvc.perform(put("/api/aurum-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAurumService)))
            .andExpect(status().isOk());

        // Validate the AurumService in the database
        List<AurumService> aurumServiceList = aurumServiceRepository.findAll();
        assertThat(aurumServiceList).hasSize(databaseSizeBeforeUpdate);
        AurumService testAurumService = aurumServiceList.get(aurumServiceList.size() - 1);
        assertThat(testAurumService.getServiceType()).isEqualTo(UPDATED_SERVICE_TYPE);
        assertThat(testAurumService.getItemName()).isEqualTo(UPDATED_ITEM_NAME);
        assertThat(testAurumService.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testAurumService.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testAurumService.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testAurumService.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testAurumService.getServiceName()).isEqualTo(UPDATED_SERVICE_NAME);
        assertThat(testAurumService.getKaratType()).isEqualTo(UPDATED_KARAT_TYPE);
        assertThat(testAurumService.getExpectedKaratType()).isEqualTo(UPDATED_EXPECTED_KARAT_TYPE);
        assertThat(testAurumService.getAddedAlloy()).isEqualTo(UPDATED_ADDED_ALLOY);
        assertThat(testAurumService.getAlloyQuantity()).isEqualTo(UPDATED_ALLOY_QUANTITY);
        assertThat(testAurumService.getServiceCharge()).isEqualTo(UPDATED_SERVICE_CHARGE);
        assertThat(testAurumService.getFreeCheck()).isEqualTo(UPDATED_FREE_CHECK);
        assertThat(testAurumService.getHallMarkedText()).isEqualTo(UPDATED_HALL_MARKED_TEXT);
    }

    @Test
    @Transactional
    public void updateNonExistingAurumService() throws Exception {
        int databaseSizeBeforeUpdate = aurumServiceRepository.findAll().size();

        // Create the AurumService

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAurumServiceMockMvc.perform(put("/api/aurum-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aurumService)))
            .andExpect(status().isBadRequest());

        // Validate the AurumService in the database
        List<AurumService> aurumServiceList = aurumServiceRepository.findAll();
        assertThat(aurumServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAurumService() throws Exception {
        // Initialize the database
        aurumServiceService.save(aurumService);

        int databaseSizeBeforeDelete = aurumServiceRepository.findAll().size();

        // Delete the aurumService
        restAurumServiceMockMvc.perform(delete("/api/aurum-services/{id}", aurumService.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AurumService> aurumServiceList = aurumServiceRepository.findAll();
        assertThat(aurumServiceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AurumService.class);
        AurumService aurumService1 = new AurumService();
        aurumService1.setId(1L);
        AurumService aurumService2 = new AurumService();
        aurumService2.setId(aurumService1.getId());
        assertThat(aurumService1).isEqualTo(aurumService2);
        aurumService2.setId(2L);
        assertThat(aurumService1).isNotEqualTo(aurumService2);
        aurumService1.setId(null);
        assertThat(aurumService1).isNotEqualTo(aurumService2);
    }
}
