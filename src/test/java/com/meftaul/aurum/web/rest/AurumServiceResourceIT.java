package com.meftaul.aurum.web.rest;

import static com.meftaul.aurum.domain.AurumServiceAsserts.*;
import static com.meftaul.aurum.web.rest.TestUtil.createUpdateProxyForBean;
import static com.meftaul.aurum.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meftaul.aurum.IntegrationTest;
import com.meftaul.aurum.domain.AurumService;
import com.meftaul.aurum.domain.Voucher;
import com.meftaul.aurum.domain.enumeration.Alloy;
import com.meftaul.aurum.repository.AurumServiceRepository;
import com.meftaul.aurum.service.AurumServiceService;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AurumServiceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AurumServiceResourceIT {

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

    private static final String DEFAULT_WEIGHT_OF_FREE_CHECK = "AAAAAAAAAA";
    private static final String UPDATED_WEIGHT_OF_FREE_CHECK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/aurum-services";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AurumServiceRepository aurumServiceRepository;

    @Mock
    private AurumServiceRepository aurumServiceRepositoryMock;

    @Mock
    private AurumServiceService aurumServiceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAurumServiceMockMvc;

    private AurumService aurumService;

    private AurumService insertedAurumService;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AurumService createEntity() {
        return new AurumService()
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
            .hallMarkedText(DEFAULT_HALL_MARKED_TEXT)
            .weightOfFreeCheck(DEFAULT_WEIGHT_OF_FREE_CHECK);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AurumService createUpdatedEntity() {
        return new AurumService()
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
            .hallMarkedText(UPDATED_HALL_MARKED_TEXT)
            .weightOfFreeCheck(UPDATED_WEIGHT_OF_FREE_CHECK);
    }

    @BeforeEach
    void initTest() {
        aurumService = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAurumService != null) {
            aurumServiceRepository.delete(insertedAurumService);
            insertedAurumService = null;
        }
    }

    @Test
    @Transactional
    void createAurumService() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AurumService
        var returnedAurumService = om.readValue(
            restAurumServiceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aurumService)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AurumService.class
        );

        // Validate the AurumService in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAurumServiceUpdatableFieldsEquals(returnedAurumService, getPersistedAurumService(returnedAurumService));

        insertedAurumService = returnedAurumService;
    }

    @Test
    @Transactional
    void createAurumServiceWithExistingId() throws Exception {
        // Create the AurumService with an existing ID
        aurumService.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAurumServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aurumService)))
            .andExpect(status().isBadRequest());

        // Validate the AurumService in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAurumServices() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList
        restAurumServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aurumService.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceType").value(hasItem(DEFAULT_SERVICE_TYPE)))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(sameNumber(DEFAULT_WEIGHT))))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(sameNumber(DEFAULT_RATE))))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME)))
            .andExpect(jsonPath("$.[*].karatType").value(hasItem(DEFAULT_KARAT_TYPE)))
            .andExpect(jsonPath("$.[*].expectedKaratType").value(hasItem(DEFAULT_EXPECTED_KARAT_TYPE)))
            .andExpect(jsonPath("$.[*].addedAlloy").value(hasItem(DEFAULT_ADDED_ALLOY.toString())))
            .andExpect(jsonPath("$.[*].alloyQuantity").value(hasItem(sameNumber(DEFAULT_ALLOY_QUANTITY))))
            .andExpect(jsonPath("$.[*].serviceCharge").value(hasItem(sameNumber(DEFAULT_SERVICE_CHARGE))))
            .andExpect(jsonPath("$.[*].freeCheck").value(hasItem(sameNumber(DEFAULT_FREE_CHECK))))
            .andExpect(jsonPath("$.[*].hallMarkedText").value(hasItem(DEFAULT_HALL_MARKED_TEXT)))
            .andExpect(jsonPath("$.[*].weightOfFreeCheck").value(hasItem(DEFAULT_WEIGHT_OF_FREE_CHECK)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAurumServicesWithEagerRelationshipsIsEnabled() throws Exception {
        when(aurumServiceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAurumServiceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(aurumServiceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAurumServicesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(aurumServiceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAurumServiceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(aurumServiceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAurumService() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get the aurumService
        restAurumServiceMockMvc
            .perform(get(ENTITY_API_URL_ID, aurumService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aurumService.getId().intValue()))
            .andExpect(jsonPath("$.serviceType").value(DEFAULT_SERVICE_TYPE))
            .andExpect(jsonPath("$.itemName").value(DEFAULT_ITEM_NAME))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.weight").value(sameNumber(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.rate").value(sameNumber(DEFAULT_RATE)))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.serviceName").value(DEFAULT_SERVICE_NAME))
            .andExpect(jsonPath("$.karatType").value(DEFAULT_KARAT_TYPE))
            .andExpect(jsonPath("$.expectedKaratType").value(DEFAULT_EXPECTED_KARAT_TYPE))
            .andExpect(jsonPath("$.addedAlloy").value(DEFAULT_ADDED_ALLOY.toString()))
            .andExpect(jsonPath("$.alloyQuantity").value(sameNumber(DEFAULT_ALLOY_QUANTITY)))
            .andExpect(jsonPath("$.serviceCharge").value(sameNumber(DEFAULT_SERVICE_CHARGE)))
            .andExpect(jsonPath("$.freeCheck").value(sameNumber(DEFAULT_FREE_CHECK)))
            .andExpect(jsonPath("$.hallMarkedText").value(DEFAULT_HALL_MARKED_TEXT))
            .andExpect(jsonPath("$.weightOfFreeCheck").value(DEFAULT_WEIGHT_OF_FREE_CHECK));
    }

    @Test
    @Transactional
    void getAurumServicesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        Long id = aurumService.getId();

        defaultAurumServiceFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAurumServiceFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAurumServiceFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceType equals to
        defaultAurumServiceFiltering("serviceType.equals=" + DEFAULT_SERVICE_TYPE, "serviceType.equals=" + UPDATED_SERVICE_TYPE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceType in
        defaultAurumServiceFiltering(
            "serviceType.in=" + DEFAULT_SERVICE_TYPE + "," + UPDATED_SERVICE_TYPE,
            "serviceType.in=" + UPDATED_SERVICE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceType is not null
        defaultAurumServiceFiltering("serviceType.specified=true", "serviceType.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceType contains
        defaultAurumServiceFiltering("serviceType.contains=" + DEFAULT_SERVICE_TYPE, "serviceType.contains=" + UPDATED_SERVICE_TYPE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceType does not contain
        defaultAurumServiceFiltering(
            "serviceType.doesNotContain=" + UPDATED_SERVICE_TYPE,
            "serviceType.doesNotContain=" + DEFAULT_SERVICE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByItemNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where itemName equals to
        defaultAurumServiceFiltering("itemName.equals=" + DEFAULT_ITEM_NAME, "itemName.equals=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    void getAllAurumServicesByItemNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where itemName in
        defaultAurumServiceFiltering("itemName.in=" + DEFAULT_ITEM_NAME + "," + UPDATED_ITEM_NAME, "itemName.in=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    void getAllAurumServicesByItemNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where itemName is not null
        defaultAurumServiceFiltering("itemName.specified=true", "itemName.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByItemNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where itemName contains
        defaultAurumServiceFiltering("itemName.contains=" + DEFAULT_ITEM_NAME, "itemName.contains=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    void getAllAurumServicesByItemNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where itemName does not contain
        defaultAurumServiceFiltering("itemName.doesNotContain=" + UPDATED_ITEM_NAME, "itemName.doesNotContain=" + DEFAULT_ITEM_NAME);
    }

    @Test
    @Transactional
    void getAllAurumServicesByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where quantity equals to
        defaultAurumServiceFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllAurumServicesByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where quantity in
        defaultAurumServiceFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllAurumServicesByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where quantity is not null
        defaultAurumServiceFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where quantity is greater than or equal to
        defaultAurumServiceFiltering("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY, "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllAurumServicesByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where quantity is less than or equal to
        defaultAurumServiceFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllAurumServicesByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where quantity is less than
        defaultAurumServiceFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllAurumServicesByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where quantity is greater than
        defaultAurumServiceFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllAurumServicesByWeightIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weight equals to
        defaultAurumServiceFiltering("weight.equals=" + DEFAULT_WEIGHT, "weight.equals=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByWeightIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weight in
        defaultAurumServiceFiltering("weight.in=" + DEFAULT_WEIGHT + "," + UPDATED_WEIGHT, "weight.in=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByWeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weight is not null
        defaultAurumServiceFiltering("weight.specified=true", "weight.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByWeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weight is greater than or equal to
        defaultAurumServiceFiltering("weight.greaterThanOrEqual=" + DEFAULT_WEIGHT, "weight.greaterThanOrEqual=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByWeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weight is less than or equal to
        defaultAurumServiceFiltering("weight.lessThanOrEqual=" + DEFAULT_WEIGHT, "weight.lessThanOrEqual=" + SMALLER_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByWeightIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weight is less than
        defaultAurumServiceFiltering("weight.lessThan=" + UPDATED_WEIGHT, "weight.lessThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByWeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weight is greater than
        defaultAurumServiceFiltering("weight.greaterThan=" + SMALLER_WEIGHT, "weight.greaterThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where rate equals to
        defaultAurumServiceFiltering("rate.equals=" + DEFAULT_RATE, "rate.equals=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where rate in
        defaultAurumServiceFiltering("rate.in=" + DEFAULT_RATE + "," + UPDATED_RATE, "rate.in=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where rate is not null
        defaultAurumServiceFiltering("rate.specified=true", "rate.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where rate is greater than or equal to
        defaultAurumServiceFiltering("rate.greaterThanOrEqual=" + DEFAULT_RATE, "rate.greaterThanOrEqual=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where rate is less than or equal to
        defaultAurumServiceFiltering("rate.lessThanOrEqual=" + DEFAULT_RATE, "rate.lessThanOrEqual=" + SMALLER_RATE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where rate is less than
        defaultAurumServiceFiltering("rate.lessThan=" + UPDATED_RATE, "rate.lessThan=" + DEFAULT_RATE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where rate is greater than
        defaultAurumServiceFiltering("rate.greaterThan=" + SMALLER_RATE, "rate.greaterThan=" + DEFAULT_RATE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where amount equals to
        defaultAurumServiceFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where amount in
        defaultAurumServiceFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where amount is not null
        defaultAurumServiceFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where amount is greater than or equal to
        defaultAurumServiceFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where amount is less than or equal to
        defaultAurumServiceFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where amount is less than
        defaultAurumServiceFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where amount is greater than
        defaultAurumServiceFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceName equals to
        defaultAurumServiceFiltering("serviceName.equals=" + DEFAULT_SERVICE_NAME, "serviceName.equals=" + UPDATED_SERVICE_NAME);
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceName in
        defaultAurumServiceFiltering(
            "serviceName.in=" + DEFAULT_SERVICE_NAME + "," + UPDATED_SERVICE_NAME,
            "serviceName.in=" + UPDATED_SERVICE_NAME
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceName is not null
        defaultAurumServiceFiltering("serviceName.specified=true", "serviceName.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceName contains
        defaultAurumServiceFiltering("serviceName.contains=" + DEFAULT_SERVICE_NAME, "serviceName.contains=" + UPDATED_SERVICE_NAME);
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceName does not contain
        defaultAurumServiceFiltering(
            "serviceName.doesNotContain=" + UPDATED_SERVICE_NAME,
            "serviceName.doesNotContain=" + DEFAULT_SERVICE_NAME
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByKaratTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where karatType equals to
        defaultAurumServiceFiltering("karatType.equals=" + DEFAULT_KARAT_TYPE, "karatType.equals=" + UPDATED_KARAT_TYPE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByKaratTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where karatType in
        defaultAurumServiceFiltering("karatType.in=" + DEFAULT_KARAT_TYPE + "," + UPDATED_KARAT_TYPE, "karatType.in=" + UPDATED_KARAT_TYPE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByKaratTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where karatType is not null
        defaultAurumServiceFiltering("karatType.specified=true", "karatType.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByKaratTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where karatType contains
        defaultAurumServiceFiltering("karatType.contains=" + DEFAULT_KARAT_TYPE, "karatType.contains=" + UPDATED_KARAT_TYPE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByKaratTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where karatType does not contain
        defaultAurumServiceFiltering("karatType.doesNotContain=" + UPDATED_KARAT_TYPE, "karatType.doesNotContain=" + DEFAULT_KARAT_TYPE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByExpectedKaratTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where expectedKaratType equals to
        defaultAurumServiceFiltering(
            "expectedKaratType.equals=" + DEFAULT_EXPECTED_KARAT_TYPE,
            "expectedKaratType.equals=" + UPDATED_EXPECTED_KARAT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByExpectedKaratTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where expectedKaratType in
        defaultAurumServiceFiltering(
            "expectedKaratType.in=" + DEFAULT_EXPECTED_KARAT_TYPE + "," + UPDATED_EXPECTED_KARAT_TYPE,
            "expectedKaratType.in=" + UPDATED_EXPECTED_KARAT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByExpectedKaratTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where expectedKaratType is not null
        defaultAurumServiceFiltering("expectedKaratType.specified=true", "expectedKaratType.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByExpectedKaratTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where expectedKaratType contains
        defaultAurumServiceFiltering(
            "expectedKaratType.contains=" + DEFAULT_EXPECTED_KARAT_TYPE,
            "expectedKaratType.contains=" + UPDATED_EXPECTED_KARAT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByExpectedKaratTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where expectedKaratType does not contain
        defaultAurumServiceFiltering(
            "expectedKaratType.doesNotContain=" + UPDATED_EXPECTED_KARAT_TYPE,
            "expectedKaratType.doesNotContain=" + DEFAULT_EXPECTED_KARAT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByAddedAlloyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where addedAlloy equals to
        defaultAurumServiceFiltering("addedAlloy.equals=" + DEFAULT_ADDED_ALLOY, "addedAlloy.equals=" + UPDATED_ADDED_ALLOY);
    }

    @Test
    @Transactional
    void getAllAurumServicesByAddedAlloyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where addedAlloy in
        defaultAurumServiceFiltering(
            "addedAlloy.in=" + DEFAULT_ADDED_ALLOY + "," + UPDATED_ADDED_ALLOY,
            "addedAlloy.in=" + UPDATED_ADDED_ALLOY
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByAddedAlloyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where addedAlloy is not null
        defaultAurumServiceFiltering("addedAlloy.specified=true", "addedAlloy.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByAlloyQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where alloyQuantity equals to
        defaultAurumServiceFiltering("alloyQuantity.equals=" + DEFAULT_ALLOY_QUANTITY, "alloyQuantity.equals=" + UPDATED_ALLOY_QUANTITY);
    }

    @Test
    @Transactional
    void getAllAurumServicesByAlloyQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where alloyQuantity in
        defaultAurumServiceFiltering(
            "alloyQuantity.in=" + DEFAULT_ALLOY_QUANTITY + "," + UPDATED_ALLOY_QUANTITY,
            "alloyQuantity.in=" + UPDATED_ALLOY_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByAlloyQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where alloyQuantity is not null
        defaultAurumServiceFiltering("alloyQuantity.specified=true", "alloyQuantity.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByAlloyQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where alloyQuantity is greater than or equal to
        defaultAurumServiceFiltering(
            "alloyQuantity.greaterThanOrEqual=" + DEFAULT_ALLOY_QUANTITY,
            "alloyQuantity.greaterThanOrEqual=" + UPDATED_ALLOY_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByAlloyQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where alloyQuantity is less than or equal to
        defaultAurumServiceFiltering(
            "alloyQuantity.lessThanOrEqual=" + DEFAULT_ALLOY_QUANTITY,
            "alloyQuantity.lessThanOrEqual=" + SMALLER_ALLOY_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByAlloyQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where alloyQuantity is less than
        defaultAurumServiceFiltering(
            "alloyQuantity.lessThan=" + UPDATED_ALLOY_QUANTITY,
            "alloyQuantity.lessThan=" + DEFAULT_ALLOY_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByAlloyQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where alloyQuantity is greater than
        defaultAurumServiceFiltering(
            "alloyQuantity.greaterThan=" + SMALLER_ALLOY_QUANTITY,
            "alloyQuantity.greaterThan=" + DEFAULT_ALLOY_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceChargeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceCharge equals to
        defaultAurumServiceFiltering("serviceCharge.equals=" + DEFAULT_SERVICE_CHARGE, "serviceCharge.equals=" + UPDATED_SERVICE_CHARGE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceChargeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceCharge in
        defaultAurumServiceFiltering(
            "serviceCharge.in=" + DEFAULT_SERVICE_CHARGE + "," + UPDATED_SERVICE_CHARGE,
            "serviceCharge.in=" + UPDATED_SERVICE_CHARGE
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceChargeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceCharge is not null
        defaultAurumServiceFiltering("serviceCharge.specified=true", "serviceCharge.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceChargeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceCharge is greater than or equal to
        defaultAurumServiceFiltering(
            "serviceCharge.greaterThanOrEqual=" + DEFAULT_SERVICE_CHARGE,
            "serviceCharge.greaterThanOrEqual=" + UPDATED_SERVICE_CHARGE
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceChargeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceCharge is less than or equal to
        defaultAurumServiceFiltering(
            "serviceCharge.lessThanOrEqual=" + DEFAULT_SERVICE_CHARGE,
            "serviceCharge.lessThanOrEqual=" + SMALLER_SERVICE_CHARGE
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceChargeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceCharge is less than
        defaultAurumServiceFiltering(
            "serviceCharge.lessThan=" + UPDATED_SERVICE_CHARGE,
            "serviceCharge.lessThan=" + DEFAULT_SERVICE_CHARGE
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceChargeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceCharge is greater than
        defaultAurumServiceFiltering(
            "serviceCharge.greaterThan=" + SMALLER_SERVICE_CHARGE,
            "serviceCharge.greaterThan=" + DEFAULT_SERVICE_CHARGE
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByFreeCheckIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where freeCheck equals to
        defaultAurumServiceFiltering("freeCheck.equals=" + DEFAULT_FREE_CHECK, "freeCheck.equals=" + UPDATED_FREE_CHECK);
    }

    @Test
    @Transactional
    void getAllAurumServicesByFreeCheckIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where freeCheck in
        defaultAurumServiceFiltering("freeCheck.in=" + DEFAULT_FREE_CHECK + "," + UPDATED_FREE_CHECK, "freeCheck.in=" + UPDATED_FREE_CHECK);
    }

    @Test
    @Transactional
    void getAllAurumServicesByFreeCheckIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where freeCheck is not null
        defaultAurumServiceFiltering("freeCheck.specified=true", "freeCheck.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByFreeCheckIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where freeCheck is greater than or equal to
        defaultAurumServiceFiltering(
            "freeCheck.greaterThanOrEqual=" + DEFAULT_FREE_CHECK,
            "freeCheck.greaterThanOrEqual=" + UPDATED_FREE_CHECK
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByFreeCheckIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where freeCheck is less than or equal to
        defaultAurumServiceFiltering("freeCheck.lessThanOrEqual=" + DEFAULT_FREE_CHECK, "freeCheck.lessThanOrEqual=" + SMALLER_FREE_CHECK);
    }

    @Test
    @Transactional
    void getAllAurumServicesByFreeCheckIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where freeCheck is less than
        defaultAurumServiceFiltering("freeCheck.lessThan=" + UPDATED_FREE_CHECK, "freeCheck.lessThan=" + DEFAULT_FREE_CHECK);
    }

    @Test
    @Transactional
    void getAllAurumServicesByFreeCheckIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where freeCheck is greater than
        defaultAurumServiceFiltering("freeCheck.greaterThan=" + SMALLER_FREE_CHECK, "freeCheck.greaterThan=" + DEFAULT_FREE_CHECK);
    }

    @Test
    @Transactional
    void getAllAurumServicesByHallMarkedTextIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where hallMarkedText equals to
        defaultAurumServiceFiltering(
            "hallMarkedText.equals=" + DEFAULT_HALL_MARKED_TEXT,
            "hallMarkedText.equals=" + UPDATED_HALL_MARKED_TEXT
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByHallMarkedTextIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where hallMarkedText in
        defaultAurumServiceFiltering(
            "hallMarkedText.in=" + DEFAULT_HALL_MARKED_TEXT + "," + UPDATED_HALL_MARKED_TEXT,
            "hallMarkedText.in=" + UPDATED_HALL_MARKED_TEXT
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByHallMarkedTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where hallMarkedText is not null
        defaultAurumServiceFiltering("hallMarkedText.specified=true", "hallMarkedText.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByHallMarkedTextContainsSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where hallMarkedText contains
        defaultAurumServiceFiltering(
            "hallMarkedText.contains=" + DEFAULT_HALL_MARKED_TEXT,
            "hallMarkedText.contains=" + UPDATED_HALL_MARKED_TEXT
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByHallMarkedTextNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where hallMarkedText does not contain
        defaultAurumServiceFiltering(
            "hallMarkedText.doesNotContain=" + UPDATED_HALL_MARKED_TEXT,
            "hallMarkedText.doesNotContain=" + DEFAULT_HALL_MARKED_TEXT
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByWeightOfFreeCheckIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weightOfFreeCheck equals to
        defaultAurumServiceFiltering(
            "weightOfFreeCheck.equals=" + DEFAULT_WEIGHT_OF_FREE_CHECK,
            "weightOfFreeCheck.equals=" + UPDATED_WEIGHT_OF_FREE_CHECK
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByWeightOfFreeCheckIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weightOfFreeCheck in
        defaultAurumServiceFiltering(
            "weightOfFreeCheck.in=" + DEFAULT_WEIGHT_OF_FREE_CHECK + "," + UPDATED_WEIGHT_OF_FREE_CHECK,
            "weightOfFreeCheck.in=" + UPDATED_WEIGHT_OF_FREE_CHECK
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByWeightOfFreeCheckIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weightOfFreeCheck is not null
        defaultAurumServiceFiltering("weightOfFreeCheck.specified=true", "weightOfFreeCheck.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByWeightOfFreeCheckContainsSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weightOfFreeCheck contains
        defaultAurumServiceFiltering(
            "weightOfFreeCheck.contains=" + DEFAULT_WEIGHT_OF_FREE_CHECK,
            "weightOfFreeCheck.contains=" + UPDATED_WEIGHT_OF_FREE_CHECK
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByWeightOfFreeCheckNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weightOfFreeCheck does not contain
        defaultAurumServiceFiltering(
            "weightOfFreeCheck.doesNotContain=" + UPDATED_WEIGHT_OF_FREE_CHECK,
            "weightOfFreeCheck.doesNotContain=" + DEFAULT_WEIGHT_OF_FREE_CHECK
        );
    }

    @Test
    @Transactional
    void getAllAurumServicesByVoucherIsEqualToSomething() throws Exception {
        Voucher voucher;
        if (TestUtil.findAll(em, Voucher.class).isEmpty()) {
            aurumServiceRepository.saveAndFlush(aurumService);
            voucher = VoucherResourceIT.createEntity();
        } else {
            voucher = TestUtil.findAll(em, Voucher.class).get(0);
        }
        em.persist(voucher);
        em.flush();
        aurumService.setVoucher(voucher);
        aurumServiceRepository.saveAndFlush(aurumService);
        Long voucherId = voucher.getId();
        // Get all the aurumServiceList where voucher equals to voucherId
        defaultAurumServiceShouldBeFound("voucherId.equals=" + voucherId);

        // Get all the aurumServiceList where voucher equals to (voucherId + 1)
        defaultAurumServiceShouldNotBeFound("voucherId.equals=" + (voucherId + 1));
    }

    private void defaultAurumServiceFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAurumServiceShouldBeFound(shouldBeFound);
        defaultAurumServiceShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAurumServiceShouldBeFound(String filter) throws Exception {
        restAurumServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aurumService.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceType").value(hasItem(DEFAULT_SERVICE_TYPE)))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(sameNumber(DEFAULT_WEIGHT))))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(sameNumber(DEFAULT_RATE))))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME)))
            .andExpect(jsonPath("$.[*].karatType").value(hasItem(DEFAULT_KARAT_TYPE)))
            .andExpect(jsonPath("$.[*].expectedKaratType").value(hasItem(DEFAULT_EXPECTED_KARAT_TYPE)))
            .andExpect(jsonPath("$.[*].addedAlloy").value(hasItem(DEFAULT_ADDED_ALLOY.toString())))
            .andExpect(jsonPath("$.[*].alloyQuantity").value(hasItem(sameNumber(DEFAULT_ALLOY_QUANTITY))))
            .andExpect(jsonPath("$.[*].serviceCharge").value(hasItem(sameNumber(DEFAULT_SERVICE_CHARGE))))
            .andExpect(jsonPath("$.[*].freeCheck").value(hasItem(sameNumber(DEFAULT_FREE_CHECK))))
            .andExpect(jsonPath("$.[*].hallMarkedText").value(hasItem(DEFAULT_HALL_MARKED_TEXT)))
            .andExpect(jsonPath("$.[*].weightOfFreeCheck").value(hasItem(DEFAULT_WEIGHT_OF_FREE_CHECK)));

        // Check, that the count call also returns 1
        restAurumServiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAurumServiceShouldNotBeFound(String filter) throws Exception {
        restAurumServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAurumServiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAurumService() throws Exception {
        // Get the aurumService
        restAurumServiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAurumService() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aurumService
        AurumService updatedAurumService = aurumServiceRepository.findById(aurumService.getId()).orElseThrow();
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
            .hallMarkedText(UPDATED_HALL_MARKED_TEXT)
            .weightOfFreeCheck(UPDATED_WEIGHT_OF_FREE_CHECK);

        restAurumServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAurumService.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAurumService))
            )
            .andExpect(status().isOk());

        // Validate the AurumService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAurumServiceToMatchAllProperties(updatedAurumService);
    }

    @Test
    @Transactional
    void putNonExistingAurumService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aurumService.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAurumServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aurumService.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aurumService))
            )
            .andExpect(status().isBadRequest());

        // Validate the AurumService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAurumService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aurumService.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAurumServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aurumService))
            )
            .andExpect(status().isBadRequest());

        // Validate the AurumService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAurumService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aurumService.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAurumServiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aurumService)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AurumService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAurumServiceWithPatch() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aurumService using partial update
        AurumService partialUpdatedAurumService = new AurumService();
        partialUpdatedAurumService.setId(aurumService.getId());

        partialUpdatedAurumService
            .itemName(UPDATED_ITEM_NAME)
            .quantity(UPDATED_QUANTITY)
            .weight(UPDATED_WEIGHT)
            .rate(UPDATED_RATE)
            .serviceName(UPDATED_SERVICE_NAME)
            .karatType(UPDATED_KARAT_TYPE)
            .addedAlloy(UPDATED_ADDED_ALLOY)
            .alloyQuantity(UPDATED_ALLOY_QUANTITY);

        restAurumServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAurumService.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAurumService))
            )
            .andExpect(status().isOk());

        // Validate the AurumService in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAurumServiceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAurumService, aurumService),
            getPersistedAurumService(aurumService)
        );
    }

    @Test
    @Transactional
    void fullUpdateAurumServiceWithPatch() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aurumService using partial update
        AurumService partialUpdatedAurumService = new AurumService();
        partialUpdatedAurumService.setId(aurumService.getId());

        partialUpdatedAurumService
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
            .hallMarkedText(UPDATED_HALL_MARKED_TEXT)
            .weightOfFreeCheck(UPDATED_WEIGHT_OF_FREE_CHECK);

        restAurumServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAurumService.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAurumService))
            )
            .andExpect(status().isOk());

        // Validate the AurumService in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAurumServiceUpdatableFieldsEquals(partialUpdatedAurumService, getPersistedAurumService(partialUpdatedAurumService));
    }

    @Test
    @Transactional
    void patchNonExistingAurumService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aurumService.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAurumServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aurumService.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aurumService))
            )
            .andExpect(status().isBadRequest());

        // Validate the AurumService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAurumService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aurumService.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAurumServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aurumService))
            )
            .andExpect(status().isBadRequest());

        // Validate the AurumService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAurumService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aurumService.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAurumServiceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(aurumService)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AurumService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAurumService() throws Exception {
        // Initialize the database
        insertedAurumService = aurumServiceRepository.saveAndFlush(aurumService);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the aurumService
        restAurumServiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, aurumService.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return aurumServiceRepository.count();
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

    protected AurumService getPersistedAurumService(AurumService aurumService) {
        return aurumServiceRepository.findById(aurumService.getId()).orElseThrow();
    }

    protected void assertPersistedAurumServiceToMatchAllProperties(AurumService expectedAurumService) {
        assertAurumServiceAllPropertiesEquals(expectedAurumService, getPersistedAurumService(expectedAurumService));
    }

    protected void assertPersistedAurumServiceToMatchUpdatableProperties(AurumService expectedAurumService) {
        assertAurumServiceAllUpdatablePropertiesEquals(expectedAurumService, getPersistedAurumService(expectedAurumService));
    }
}
