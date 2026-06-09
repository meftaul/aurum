package com.meftaul.aurum.web.rest;

import static com.meftaul.aurum.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.meftaul.aurum.IntegrationTest;
import com.meftaul.aurum.domain.AurumService;
import com.meftaul.aurum.domain.Voucher;
import com.meftaul.aurum.domain.enumeration.Alloy;
import com.meftaul.aurum.repository.AurumServiceRepository;
import com.meftaul.aurum.service.AurumServiceService;
import com.meftaul.aurum.service.criteria.AurumServiceCriteria;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

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
            .hallMarkedText(DEFAULT_HALL_MARKED_TEXT)
            .weightOfFreeCheck(DEFAULT_WEIGHT_OF_FREE_CHECK);
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
            .hallMarkedText(UPDATED_HALL_MARKED_TEXT)
            .weightOfFreeCheck(UPDATED_WEIGHT_OF_FREE_CHECK);
        return aurumService;
    }

    @BeforeEach
    public void initTest() {
        aurumService = createEntity(em);
    }

    @Test
    @Transactional
    void createAurumService() throws Exception {
        int databaseSizeBeforeCreate = aurumServiceRepository.findAll().size();
        // Create the AurumService
        restAurumServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aurumService)))
            .andExpect(status().isCreated());

        // Validate the AurumService in the database
        List<AurumService> aurumServiceList = aurumServiceRepository.findAll();
        assertThat(aurumServiceList).hasSize(databaseSizeBeforeCreate + 1);
        AurumService testAurumService = aurumServiceList.get(aurumServiceList.size() - 1);
        assertThat(testAurumService.getServiceType()).isEqualTo(DEFAULT_SERVICE_TYPE);
        assertThat(testAurumService.getItemName()).isEqualTo(DEFAULT_ITEM_NAME);
        assertThat(testAurumService.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testAurumService.getWeight()).isEqualByComparingTo(DEFAULT_WEIGHT);
        assertThat(testAurumService.getRate()).isEqualByComparingTo(DEFAULT_RATE);
        assertThat(testAurumService.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testAurumService.getServiceName()).isEqualTo(DEFAULT_SERVICE_NAME);
        assertThat(testAurumService.getKaratType()).isEqualTo(DEFAULT_KARAT_TYPE);
        assertThat(testAurumService.getExpectedKaratType()).isEqualTo(DEFAULT_EXPECTED_KARAT_TYPE);
        assertThat(testAurumService.getAddedAlloy()).isEqualTo(DEFAULT_ADDED_ALLOY);
        assertThat(testAurumService.getAlloyQuantity()).isEqualByComparingTo(DEFAULT_ALLOY_QUANTITY);
        assertThat(testAurumService.getServiceCharge()).isEqualByComparingTo(DEFAULT_SERVICE_CHARGE);
        assertThat(testAurumService.getFreeCheck()).isEqualByComparingTo(DEFAULT_FREE_CHECK);
        assertThat(testAurumService.getHallMarkedText()).isEqualTo(DEFAULT_HALL_MARKED_TEXT);
        assertThat(testAurumService.getWeightOfFreeCheck()).isEqualTo(DEFAULT_WEIGHT_OF_FREE_CHECK);
    }

    @Test
    @Transactional
    void createAurumServiceWithExistingId() throws Exception {
        // Create the AurumService with an existing ID
        aurumService.setId(1L);

        int databaseSizeBeforeCreate = aurumServiceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAurumServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aurumService)))
            .andExpect(status().isBadRequest());

        // Validate the AurumService in the database
        List<AurumService> aurumServiceList = aurumServiceRepository.findAll();
        assertThat(aurumServiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAurumServices() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

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
        aurumServiceRepository.saveAndFlush(aurumService);

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
        aurumServiceRepository.saveAndFlush(aurumService);

        Long id = aurumService.getId();

        defaultAurumServiceShouldBeFound("id.equals=" + id);
        defaultAurumServiceShouldNotBeFound("id.notEquals=" + id);

        defaultAurumServiceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAurumServiceShouldNotBeFound("id.greaterThan=" + id);

        defaultAurumServiceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAurumServiceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceType equals to DEFAULT_SERVICE_TYPE
        defaultAurumServiceShouldBeFound("serviceType.equals=" + DEFAULT_SERVICE_TYPE);

        // Get all the aurumServiceList where serviceType equals to UPDATED_SERVICE_TYPE
        defaultAurumServiceShouldNotBeFound("serviceType.equals=" + UPDATED_SERVICE_TYPE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceTypeIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceType in DEFAULT_SERVICE_TYPE or UPDATED_SERVICE_TYPE
        defaultAurumServiceShouldBeFound("serviceType.in=" + DEFAULT_SERVICE_TYPE + "," + UPDATED_SERVICE_TYPE);

        // Get all the aurumServiceList where serviceType equals to UPDATED_SERVICE_TYPE
        defaultAurumServiceShouldNotBeFound("serviceType.in=" + UPDATED_SERVICE_TYPE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceType is not null
        defaultAurumServiceShouldBeFound("serviceType.specified=true");

        // Get all the aurumServiceList where serviceType is null
        defaultAurumServiceShouldNotBeFound("serviceType.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceTypeContainsSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceType contains DEFAULT_SERVICE_TYPE
        defaultAurumServiceShouldBeFound("serviceType.contains=" + DEFAULT_SERVICE_TYPE);

        // Get all the aurumServiceList where serviceType contains UPDATED_SERVICE_TYPE
        defaultAurumServiceShouldNotBeFound("serviceType.contains=" + UPDATED_SERVICE_TYPE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceTypeNotContainsSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceType does not contain DEFAULT_SERVICE_TYPE
        defaultAurumServiceShouldNotBeFound("serviceType.doesNotContain=" + DEFAULT_SERVICE_TYPE);

        // Get all the aurumServiceList where serviceType does not contain UPDATED_SERVICE_TYPE
        defaultAurumServiceShouldBeFound("serviceType.doesNotContain=" + UPDATED_SERVICE_TYPE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByItemNameIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where itemName equals to DEFAULT_ITEM_NAME
        defaultAurumServiceShouldBeFound("itemName.equals=" + DEFAULT_ITEM_NAME);

        // Get all the aurumServiceList where itemName equals to UPDATED_ITEM_NAME
        defaultAurumServiceShouldNotBeFound("itemName.equals=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    void getAllAurumServicesByItemNameIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where itemName in DEFAULT_ITEM_NAME or UPDATED_ITEM_NAME
        defaultAurumServiceShouldBeFound("itemName.in=" + DEFAULT_ITEM_NAME + "," + UPDATED_ITEM_NAME);

        // Get all the aurumServiceList where itemName equals to UPDATED_ITEM_NAME
        defaultAurumServiceShouldNotBeFound("itemName.in=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    void getAllAurumServicesByItemNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where itemName is not null
        defaultAurumServiceShouldBeFound("itemName.specified=true");

        // Get all the aurumServiceList where itemName is null
        defaultAurumServiceShouldNotBeFound("itemName.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByItemNameContainsSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where itemName contains DEFAULT_ITEM_NAME
        defaultAurumServiceShouldBeFound("itemName.contains=" + DEFAULT_ITEM_NAME);

        // Get all the aurumServiceList where itemName contains UPDATED_ITEM_NAME
        defaultAurumServiceShouldNotBeFound("itemName.contains=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    void getAllAurumServicesByItemNameNotContainsSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where itemName does not contain DEFAULT_ITEM_NAME
        defaultAurumServiceShouldNotBeFound("itemName.doesNotContain=" + DEFAULT_ITEM_NAME);

        // Get all the aurumServiceList where itemName does not contain UPDATED_ITEM_NAME
        defaultAurumServiceShouldBeFound("itemName.doesNotContain=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    void getAllAurumServicesByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where quantity equals to DEFAULT_QUANTITY
        defaultAurumServiceShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the aurumServiceList where quantity equals to UPDATED_QUANTITY
        defaultAurumServiceShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllAurumServicesByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultAurumServiceShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the aurumServiceList where quantity equals to UPDATED_QUANTITY
        defaultAurumServiceShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllAurumServicesByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where quantity is not null
        defaultAurumServiceShouldBeFound("quantity.specified=true");

        // Get all the aurumServiceList where quantity is null
        defaultAurumServiceShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where quantity is greater than or equal to DEFAULT_QUANTITY
        defaultAurumServiceShouldBeFound("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the aurumServiceList where quantity is greater than or equal to UPDATED_QUANTITY
        defaultAurumServiceShouldNotBeFound("quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllAurumServicesByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where quantity is less than or equal to DEFAULT_QUANTITY
        defaultAurumServiceShouldBeFound("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the aurumServiceList where quantity is less than or equal to SMALLER_QUANTITY
        defaultAurumServiceShouldNotBeFound("quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllAurumServicesByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where quantity is less than DEFAULT_QUANTITY
        defaultAurumServiceShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the aurumServiceList where quantity is less than UPDATED_QUANTITY
        defaultAurumServiceShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllAurumServicesByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where quantity is greater than DEFAULT_QUANTITY
        defaultAurumServiceShouldNotBeFound("quantity.greaterThan=" + DEFAULT_QUANTITY);

        // Get all the aurumServiceList where quantity is greater than SMALLER_QUANTITY
        defaultAurumServiceShouldBeFound("quantity.greaterThan=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllAurumServicesByWeightIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weight equals to DEFAULT_WEIGHT
        defaultAurumServiceShouldBeFound("weight.equals=" + DEFAULT_WEIGHT);

        // Get all the aurumServiceList where weight equals to UPDATED_WEIGHT
        defaultAurumServiceShouldNotBeFound("weight.equals=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByWeightIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weight in DEFAULT_WEIGHT or UPDATED_WEIGHT
        defaultAurumServiceShouldBeFound("weight.in=" + DEFAULT_WEIGHT + "," + UPDATED_WEIGHT);

        // Get all the aurumServiceList where weight equals to UPDATED_WEIGHT
        defaultAurumServiceShouldNotBeFound("weight.in=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByWeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weight is not null
        defaultAurumServiceShouldBeFound("weight.specified=true");

        // Get all the aurumServiceList where weight is null
        defaultAurumServiceShouldNotBeFound("weight.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByWeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weight is greater than or equal to DEFAULT_WEIGHT
        defaultAurumServiceShouldBeFound("weight.greaterThanOrEqual=" + DEFAULT_WEIGHT);

        // Get all the aurumServiceList where weight is greater than or equal to UPDATED_WEIGHT
        defaultAurumServiceShouldNotBeFound("weight.greaterThanOrEqual=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByWeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weight is less than or equal to DEFAULT_WEIGHT
        defaultAurumServiceShouldBeFound("weight.lessThanOrEqual=" + DEFAULT_WEIGHT);

        // Get all the aurumServiceList where weight is less than or equal to SMALLER_WEIGHT
        defaultAurumServiceShouldNotBeFound("weight.lessThanOrEqual=" + SMALLER_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByWeightIsLessThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weight is less than DEFAULT_WEIGHT
        defaultAurumServiceShouldNotBeFound("weight.lessThan=" + DEFAULT_WEIGHT);

        // Get all the aurumServiceList where weight is less than UPDATED_WEIGHT
        defaultAurumServiceShouldBeFound("weight.lessThan=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByWeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weight is greater than DEFAULT_WEIGHT
        defaultAurumServiceShouldNotBeFound("weight.greaterThan=" + DEFAULT_WEIGHT);

        // Get all the aurumServiceList where weight is greater than SMALLER_WEIGHT
        defaultAurumServiceShouldBeFound("weight.greaterThan=" + SMALLER_WEIGHT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByRateIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where rate equals to DEFAULT_RATE
        defaultAurumServiceShouldBeFound("rate.equals=" + DEFAULT_RATE);

        // Get all the aurumServiceList where rate equals to UPDATED_RATE
        defaultAurumServiceShouldNotBeFound("rate.equals=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByRateIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where rate in DEFAULT_RATE or UPDATED_RATE
        defaultAurumServiceShouldBeFound("rate.in=" + DEFAULT_RATE + "," + UPDATED_RATE);

        // Get all the aurumServiceList where rate equals to UPDATED_RATE
        defaultAurumServiceShouldNotBeFound("rate.in=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where rate is not null
        defaultAurumServiceShouldBeFound("rate.specified=true");

        // Get all the aurumServiceList where rate is null
        defaultAurumServiceShouldNotBeFound("rate.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where rate is greater than or equal to DEFAULT_RATE
        defaultAurumServiceShouldBeFound("rate.greaterThanOrEqual=" + DEFAULT_RATE);

        // Get all the aurumServiceList where rate is greater than or equal to UPDATED_RATE
        defaultAurumServiceShouldNotBeFound("rate.greaterThanOrEqual=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where rate is less than or equal to DEFAULT_RATE
        defaultAurumServiceShouldBeFound("rate.lessThanOrEqual=" + DEFAULT_RATE);

        // Get all the aurumServiceList where rate is less than or equal to SMALLER_RATE
        defaultAurumServiceShouldNotBeFound("rate.lessThanOrEqual=" + SMALLER_RATE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByRateIsLessThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where rate is less than DEFAULT_RATE
        defaultAurumServiceShouldNotBeFound("rate.lessThan=" + DEFAULT_RATE);

        // Get all the aurumServiceList where rate is less than UPDATED_RATE
        defaultAurumServiceShouldBeFound("rate.lessThan=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where rate is greater than DEFAULT_RATE
        defaultAurumServiceShouldNotBeFound("rate.greaterThan=" + DEFAULT_RATE);

        // Get all the aurumServiceList where rate is greater than SMALLER_RATE
        defaultAurumServiceShouldBeFound("rate.greaterThan=" + SMALLER_RATE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where amount equals to DEFAULT_AMOUNT
        defaultAurumServiceShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the aurumServiceList where amount equals to UPDATED_AMOUNT
        defaultAurumServiceShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultAurumServiceShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the aurumServiceList where amount equals to UPDATED_AMOUNT
        defaultAurumServiceShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where amount is not null
        defaultAurumServiceShouldBeFound("amount.specified=true");

        // Get all the aurumServiceList where amount is null
        defaultAurumServiceShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultAurumServiceShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the aurumServiceList where amount is greater than or equal to UPDATED_AMOUNT
        defaultAurumServiceShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where amount is less than or equal to DEFAULT_AMOUNT
        defaultAurumServiceShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the aurumServiceList where amount is less than or equal to SMALLER_AMOUNT
        defaultAurumServiceShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where amount is less than DEFAULT_AMOUNT
        defaultAurumServiceShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the aurumServiceList where amount is less than UPDATED_AMOUNT
        defaultAurumServiceShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where amount is greater than DEFAULT_AMOUNT
        defaultAurumServiceShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the aurumServiceList where amount is greater than SMALLER_AMOUNT
        defaultAurumServiceShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceNameIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceName equals to DEFAULT_SERVICE_NAME
        defaultAurumServiceShouldBeFound("serviceName.equals=" + DEFAULT_SERVICE_NAME);

        // Get all the aurumServiceList where serviceName equals to UPDATED_SERVICE_NAME
        defaultAurumServiceShouldNotBeFound("serviceName.equals=" + UPDATED_SERVICE_NAME);
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceNameIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceName in DEFAULT_SERVICE_NAME or UPDATED_SERVICE_NAME
        defaultAurumServiceShouldBeFound("serviceName.in=" + DEFAULT_SERVICE_NAME + "," + UPDATED_SERVICE_NAME);

        // Get all the aurumServiceList where serviceName equals to UPDATED_SERVICE_NAME
        defaultAurumServiceShouldNotBeFound("serviceName.in=" + UPDATED_SERVICE_NAME);
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceName is not null
        defaultAurumServiceShouldBeFound("serviceName.specified=true");

        // Get all the aurumServiceList where serviceName is null
        defaultAurumServiceShouldNotBeFound("serviceName.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceNameContainsSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceName contains DEFAULT_SERVICE_NAME
        defaultAurumServiceShouldBeFound("serviceName.contains=" + DEFAULT_SERVICE_NAME);

        // Get all the aurumServiceList where serviceName contains UPDATED_SERVICE_NAME
        defaultAurumServiceShouldNotBeFound("serviceName.contains=" + UPDATED_SERVICE_NAME);
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceNameNotContainsSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceName does not contain DEFAULT_SERVICE_NAME
        defaultAurumServiceShouldNotBeFound("serviceName.doesNotContain=" + DEFAULT_SERVICE_NAME);

        // Get all the aurumServiceList where serviceName does not contain UPDATED_SERVICE_NAME
        defaultAurumServiceShouldBeFound("serviceName.doesNotContain=" + UPDATED_SERVICE_NAME);
    }

    @Test
    @Transactional
    void getAllAurumServicesByKaratTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where karatType equals to DEFAULT_KARAT_TYPE
        defaultAurumServiceShouldBeFound("karatType.equals=" + DEFAULT_KARAT_TYPE);

        // Get all the aurumServiceList where karatType equals to UPDATED_KARAT_TYPE
        defaultAurumServiceShouldNotBeFound("karatType.equals=" + UPDATED_KARAT_TYPE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByKaratTypeIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where karatType in DEFAULT_KARAT_TYPE or UPDATED_KARAT_TYPE
        defaultAurumServiceShouldBeFound("karatType.in=" + DEFAULT_KARAT_TYPE + "," + UPDATED_KARAT_TYPE);

        // Get all the aurumServiceList where karatType equals to UPDATED_KARAT_TYPE
        defaultAurumServiceShouldNotBeFound("karatType.in=" + UPDATED_KARAT_TYPE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByKaratTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where karatType is not null
        defaultAurumServiceShouldBeFound("karatType.specified=true");

        // Get all the aurumServiceList where karatType is null
        defaultAurumServiceShouldNotBeFound("karatType.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByKaratTypeContainsSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where karatType contains DEFAULT_KARAT_TYPE
        defaultAurumServiceShouldBeFound("karatType.contains=" + DEFAULT_KARAT_TYPE);

        // Get all the aurumServiceList where karatType contains UPDATED_KARAT_TYPE
        defaultAurumServiceShouldNotBeFound("karatType.contains=" + UPDATED_KARAT_TYPE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByKaratTypeNotContainsSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where karatType does not contain DEFAULT_KARAT_TYPE
        defaultAurumServiceShouldNotBeFound("karatType.doesNotContain=" + DEFAULT_KARAT_TYPE);

        // Get all the aurumServiceList where karatType does not contain UPDATED_KARAT_TYPE
        defaultAurumServiceShouldBeFound("karatType.doesNotContain=" + UPDATED_KARAT_TYPE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByExpectedKaratTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where expectedKaratType equals to DEFAULT_EXPECTED_KARAT_TYPE
        defaultAurumServiceShouldBeFound("expectedKaratType.equals=" + DEFAULT_EXPECTED_KARAT_TYPE);

        // Get all the aurumServiceList where expectedKaratType equals to UPDATED_EXPECTED_KARAT_TYPE
        defaultAurumServiceShouldNotBeFound("expectedKaratType.equals=" + UPDATED_EXPECTED_KARAT_TYPE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByExpectedKaratTypeIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where expectedKaratType in DEFAULT_EXPECTED_KARAT_TYPE or UPDATED_EXPECTED_KARAT_TYPE
        defaultAurumServiceShouldBeFound("expectedKaratType.in=" + DEFAULT_EXPECTED_KARAT_TYPE + "," + UPDATED_EXPECTED_KARAT_TYPE);

        // Get all the aurumServiceList where expectedKaratType equals to UPDATED_EXPECTED_KARAT_TYPE
        defaultAurumServiceShouldNotBeFound("expectedKaratType.in=" + UPDATED_EXPECTED_KARAT_TYPE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByExpectedKaratTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where expectedKaratType is not null
        defaultAurumServiceShouldBeFound("expectedKaratType.specified=true");

        // Get all the aurumServiceList where expectedKaratType is null
        defaultAurumServiceShouldNotBeFound("expectedKaratType.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByExpectedKaratTypeContainsSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where expectedKaratType contains DEFAULT_EXPECTED_KARAT_TYPE
        defaultAurumServiceShouldBeFound("expectedKaratType.contains=" + DEFAULT_EXPECTED_KARAT_TYPE);

        // Get all the aurumServiceList where expectedKaratType contains UPDATED_EXPECTED_KARAT_TYPE
        defaultAurumServiceShouldNotBeFound("expectedKaratType.contains=" + UPDATED_EXPECTED_KARAT_TYPE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByExpectedKaratTypeNotContainsSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where expectedKaratType does not contain DEFAULT_EXPECTED_KARAT_TYPE
        defaultAurumServiceShouldNotBeFound("expectedKaratType.doesNotContain=" + DEFAULT_EXPECTED_KARAT_TYPE);

        // Get all the aurumServiceList where expectedKaratType does not contain UPDATED_EXPECTED_KARAT_TYPE
        defaultAurumServiceShouldBeFound("expectedKaratType.doesNotContain=" + UPDATED_EXPECTED_KARAT_TYPE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByAddedAlloyIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where addedAlloy equals to DEFAULT_ADDED_ALLOY
        defaultAurumServiceShouldBeFound("addedAlloy.equals=" + DEFAULT_ADDED_ALLOY);

        // Get all the aurumServiceList where addedAlloy equals to UPDATED_ADDED_ALLOY
        defaultAurumServiceShouldNotBeFound("addedAlloy.equals=" + UPDATED_ADDED_ALLOY);
    }

    @Test
    @Transactional
    void getAllAurumServicesByAddedAlloyIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where addedAlloy in DEFAULT_ADDED_ALLOY or UPDATED_ADDED_ALLOY
        defaultAurumServiceShouldBeFound("addedAlloy.in=" + DEFAULT_ADDED_ALLOY + "," + UPDATED_ADDED_ALLOY);

        // Get all the aurumServiceList where addedAlloy equals to UPDATED_ADDED_ALLOY
        defaultAurumServiceShouldNotBeFound("addedAlloy.in=" + UPDATED_ADDED_ALLOY);
    }

    @Test
    @Transactional
    void getAllAurumServicesByAddedAlloyIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where addedAlloy is not null
        defaultAurumServiceShouldBeFound("addedAlloy.specified=true");

        // Get all the aurumServiceList where addedAlloy is null
        defaultAurumServiceShouldNotBeFound("addedAlloy.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByAlloyQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where alloyQuantity equals to DEFAULT_ALLOY_QUANTITY
        defaultAurumServiceShouldBeFound("alloyQuantity.equals=" + DEFAULT_ALLOY_QUANTITY);

        // Get all the aurumServiceList where alloyQuantity equals to UPDATED_ALLOY_QUANTITY
        defaultAurumServiceShouldNotBeFound("alloyQuantity.equals=" + UPDATED_ALLOY_QUANTITY);
    }

    @Test
    @Transactional
    void getAllAurumServicesByAlloyQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where alloyQuantity in DEFAULT_ALLOY_QUANTITY or UPDATED_ALLOY_QUANTITY
        defaultAurumServiceShouldBeFound("alloyQuantity.in=" + DEFAULT_ALLOY_QUANTITY + "," + UPDATED_ALLOY_QUANTITY);

        // Get all the aurumServiceList where alloyQuantity equals to UPDATED_ALLOY_QUANTITY
        defaultAurumServiceShouldNotBeFound("alloyQuantity.in=" + UPDATED_ALLOY_QUANTITY);
    }

    @Test
    @Transactional
    void getAllAurumServicesByAlloyQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where alloyQuantity is not null
        defaultAurumServiceShouldBeFound("alloyQuantity.specified=true");

        // Get all the aurumServiceList where alloyQuantity is null
        defaultAurumServiceShouldNotBeFound("alloyQuantity.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByAlloyQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where alloyQuantity is greater than or equal to DEFAULT_ALLOY_QUANTITY
        defaultAurumServiceShouldBeFound("alloyQuantity.greaterThanOrEqual=" + DEFAULT_ALLOY_QUANTITY);

        // Get all the aurumServiceList where alloyQuantity is greater than or equal to UPDATED_ALLOY_QUANTITY
        defaultAurumServiceShouldNotBeFound("alloyQuantity.greaterThanOrEqual=" + UPDATED_ALLOY_QUANTITY);
    }

    @Test
    @Transactional
    void getAllAurumServicesByAlloyQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where alloyQuantity is less than or equal to DEFAULT_ALLOY_QUANTITY
        defaultAurumServiceShouldBeFound("alloyQuantity.lessThanOrEqual=" + DEFAULT_ALLOY_QUANTITY);

        // Get all the aurumServiceList where alloyQuantity is less than or equal to SMALLER_ALLOY_QUANTITY
        defaultAurumServiceShouldNotBeFound("alloyQuantity.lessThanOrEqual=" + SMALLER_ALLOY_QUANTITY);
    }

    @Test
    @Transactional
    void getAllAurumServicesByAlloyQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where alloyQuantity is less than DEFAULT_ALLOY_QUANTITY
        defaultAurumServiceShouldNotBeFound("alloyQuantity.lessThan=" + DEFAULT_ALLOY_QUANTITY);

        // Get all the aurumServiceList where alloyQuantity is less than UPDATED_ALLOY_QUANTITY
        defaultAurumServiceShouldBeFound("alloyQuantity.lessThan=" + UPDATED_ALLOY_QUANTITY);
    }

    @Test
    @Transactional
    void getAllAurumServicesByAlloyQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where alloyQuantity is greater than DEFAULT_ALLOY_QUANTITY
        defaultAurumServiceShouldNotBeFound("alloyQuantity.greaterThan=" + DEFAULT_ALLOY_QUANTITY);

        // Get all the aurumServiceList where alloyQuantity is greater than SMALLER_ALLOY_QUANTITY
        defaultAurumServiceShouldBeFound("alloyQuantity.greaterThan=" + SMALLER_ALLOY_QUANTITY);
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceChargeIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceCharge equals to DEFAULT_SERVICE_CHARGE
        defaultAurumServiceShouldBeFound("serviceCharge.equals=" + DEFAULT_SERVICE_CHARGE);

        // Get all the aurumServiceList where serviceCharge equals to UPDATED_SERVICE_CHARGE
        defaultAurumServiceShouldNotBeFound("serviceCharge.equals=" + UPDATED_SERVICE_CHARGE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceChargeIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceCharge in DEFAULT_SERVICE_CHARGE or UPDATED_SERVICE_CHARGE
        defaultAurumServiceShouldBeFound("serviceCharge.in=" + DEFAULT_SERVICE_CHARGE + "," + UPDATED_SERVICE_CHARGE);

        // Get all the aurumServiceList where serviceCharge equals to UPDATED_SERVICE_CHARGE
        defaultAurumServiceShouldNotBeFound("serviceCharge.in=" + UPDATED_SERVICE_CHARGE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceChargeIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceCharge is not null
        defaultAurumServiceShouldBeFound("serviceCharge.specified=true");

        // Get all the aurumServiceList where serviceCharge is null
        defaultAurumServiceShouldNotBeFound("serviceCharge.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceChargeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceCharge is greater than or equal to DEFAULT_SERVICE_CHARGE
        defaultAurumServiceShouldBeFound("serviceCharge.greaterThanOrEqual=" + DEFAULT_SERVICE_CHARGE);

        // Get all the aurumServiceList where serviceCharge is greater than or equal to UPDATED_SERVICE_CHARGE
        defaultAurumServiceShouldNotBeFound("serviceCharge.greaterThanOrEqual=" + UPDATED_SERVICE_CHARGE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceChargeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceCharge is less than or equal to DEFAULT_SERVICE_CHARGE
        defaultAurumServiceShouldBeFound("serviceCharge.lessThanOrEqual=" + DEFAULT_SERVICE_CHARGE);

        // Get all the aurumServiceList where serviceCharge is less than or equal to SMALLER_SERVICE_CHARGE
        defaultAurumServiceShouldNotBeFound("serviceCharge.lessThanOrEqual=" + SMALLER_SERVICE_CHARGE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceChargeIsLessThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceCharge is less than DEFAULT_SERVICE_CHARGE
        defaultAurumServiceShouldNotBeFound("serviceCharge.lessThan=" + DEFAULT_SERVICE_CHARGE);

        // Get all the aurumServiceList where serviceCharge is less than UPDATED_SERVICE_CHARGE
        defaultAurumServiceShouldBeFound("serviceCharge.lessThan=" + UPDATED_SERVICE_CHARGE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByServiceChargeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where serviceCharge is greater than DEFAULT_SERVICE_CHARGE
        defaultAurumServiceShouldNotBeFound("serviceCharge.greaterThan=" + DEFAULT_SERVICE_CHARGE);

        // Get all the aurumServiceList where serviceCharge is greater than SMALLER_SERVICE_CHARGE
        defaultAurumServiceShouldBeFound("serviceCharge.greaterThan=" + SMALLER_SERVICE_CHARGE);
    }

    @Test
    @Transactional
    void getAllAurumServicesByFreeCheckIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where freeCheck equals to DEFAULT_FREE_CHECK
        defaultAurumServiceShouldBeFound("freeCheck.equals=" + DEFAULT_FREE_CHECK);

        // Get all the aurumServiceList where freeCheck equals to UPDATED_FREE_CHECK
        defaultAurumServiceShouldNotBeFound("freeCheck.equals=" + UPDATED_FREE_CHECK);
    }

    @Test
    @Transactional
    void getAllAurumServicesByFreeCheckIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where freeCheck in DEFAULT_FREE_CHECK or UPDATED_FREE_CHECK
        defaultAurumServiceShouldBeFound("freeCheck.in=" + DEFAULT_FREE_CHECK + "," + UPDATED_FREE_CHECK);

        // Get all the aurumServiceList where freeCheck equals to UPDATED_FREE_CHECK
        defaultAurumServiceShouldNotBeFound("freeCheck.in=" + UPDATED_FREE_CHECK);
    }

    @Test
    @Transactional
    void getAllAurumServicesByFreeCheckIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where freeCheck is not null
        defaultAurumServiceShouldBeFound("freeCheck.specified=true");

        // Get all the aurumServiceList where freeCheck is null
        defaultAurumServiceShouldNotBeFound("freeCheck.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByFreeCheckIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where freeCheck is greater than or equal to DEFAULT_FREE_CHECK
        defaultAurumServiceShouldBeFound("freeCheck.greaterThanOrEqual=" + DEFAULT_FREE_CHECK);

        // Get all the aurumServiceList where freeCheck is greater than or equal to UPDATED_FREE_CHECK
        defaultAurumServiceShouldNotBeFound("freeCheck.greaterThanOrEqual=" + UPDATED_FREE_CHECK);
    }

    @Test
    @Transactional
    void getAllAurumServicesByFreeCheckIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where freeCheck is less than or equal to DEFAULT_FREE_CHECK
        defaultAurumServiceShouldBeFound("freeCheck.lessThanOrEqual=" + DEFAULT_FREE_CHECK);

        // Get all the aurumServiceList where freeCheck is less than or equal to SMALLER_FREE_CHECK
        defaultAurumServiceShouldNotBeFound("freeCheck.lessThanOrEqual=" + SMALLER_FREE_CHECK);
    }

    @Test
    @Transactional
    void getAllAurumServicesByFreeCheckIsLessThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where freeCheck is less than DEFAULT_FREE_CHECK
        defaultAurumServiceShouldNotBeFound("freeCheck.lessThan=" + DEFAULT_FREE_CHECK);

        // Get all the aurumServiceList where freeCheck is less than UPDATED_FREE_CHECK
        defaultAurumServiceShouldBeFound("freeCheck.lessThan=" + UPDATED_FREE_CHECK);
    }

    @Test
    @Transactional
    void getAllAurumServicesByFreeCheckIsGreaterThanSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where freeCheck is greater than DEFAULT_FREE_CHECK
        defaultAurumServiceShouldNotBeFound("freeCheck.greaterThan=" + DEFAULT_FREE_CHECK);

        // Get all the aurumServiceList where freeCheck is greater than SMALLER_FREE_CHECK
        defaultAurumServiceShouldBeFound("freeCheck.greaterThan=" + SMALLER_FREE_CHECK);
    }

    @Test
    @Transactional
    void getAllAurumServicesByHallMarkedTextIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where hallMarkedText equals to DEFAULT_HALL_MARKED_TEXT
        defaultAurumServiceShouldBeFound("hallMarkedText.equals=" + DEFAULT_HALL_MARKED_TEXT);

        // Get all the aurumServiceList where hallMarkedText equals to UPDATED_HALL_MARKED_TEXT
        defaultAurumServiceShouldNotBeFound("hallMarkedText.equals=" + UPDATED_HALL_MARKED_TEXT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByHallMarkedTextIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where hallMarkedText in DEFAULT_HALL_MARKED_TEXT or UPDATED_HALL_MARKED_TEXT
        defaultAurumServiceShouldBeFound("hallMarkedText.in=" + DEFAULT_HALL_MARKED_TEXT + "," + UPDATED_HALL_MARKED_TEXT);

        // Get all the aurumServiceList where hallMarkedText equals to UPDATED_HALL_MARKED_TEXT
        defaultAurumServiceShouldNotBeFound("hallMarkedText.in=" + UPDATED_HALL_MARKED_TEXT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByHallMarkedTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where hallMarkedText is not null
        defaultAurumServiceShouldBeFound("hallMarkedText.specified=true");

        // Get all the aurumServiceList where hallMarkedText is null
        defaultAurumServiceShouldNotBeFound("hallMarkedText.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByHallMarkedTextContainsSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where hallMarkedText contains DEFAULT_HALL_MARKED_TEXT
        defaultAurumServiceShouldBeFound("hallMarkedText.contains=" + DEFAULT_HALL_MARKED_TEXT);

        // Get all the aurumServiceList where hallMarkedText contains UPDATED_HALL_MARKED_TEXT
        defaultAurumServiceShouldNotBeFound("hallMarkedText.contains=" + UPDATED_HALL_MARKED_TEXT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByHallMarkedTextNotContainsSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where hallMarkedText does not contain DEFAULT_HALL_MARKED_TEXT
        defaultAurumServiceShouldNotBeFound("hallMarkedText.doesNotContain=" + DEFAULT_HALL_MARKED_TEXT);

        // Get all the aurumServiceList where hallMarkedText does not contain UPDATED_HALL_MARKED_TEXT
        defaultAurumServiceShouldBeFound("hallMarkedText.doesNotContain=" + UPDATED_HALL_MARKED_TEXT);
    }

    @Test
    @Transactional
    void getAllAurumServicesByWeightOfFreeCheckIsEqualToSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weightOfFreeCheck equals to DEFAULT_WEIGHT_OF_FREE_CHECK
        defaultAurumServiceShouldBeFound("weightOfFreeCheck.equals=" + DEFAULT_WEIGHT_OF_FREE_CHECK);

        // Get all the aurumServiceList where weightOfFreeCheck equals to UPDATED_WEIGHT_OF_FREE_CHECK
        defaultAurumServiceShouldNotBeFound("weightOfFreeCheck.equals=" + UPDATED_WEIGHT_OF_FREE_CHECK);
    }

    @Test
    @Transactional
    void getAllAurumServicesByWeightOfFreeCheckIsInShouldWork() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weightOfFreeCheck in DEFAULT_WEIGHT_OF_FREE_CHECK or UPDATED_WEIGHT_OF_FREE_CHECK
        defaultAurumServiceShouldBeFound("weightOfFreeCheck.in=" + DEFAULT_WEIGHT_OF_FREE_CHECK + "," + UPDATED_WEIGHT_OF_FREE_CHECK);

        // Get all the aurumServiceList where weightOfFreeCheck equals to UPDATED_WEIGHT_OF_FREE_CHECK
        defaultAurumServiceShouldNotBeFound("weightOfFreeCheck.in=" + UPDATED_WEIGHT_OF_FREE_CHECK);
    }

    @Test
    @Transactional
    void getAllAurumServicesByWeightOfFreeCheckIsNullOrNotNull() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weightOfFreeCheck is not null
        defaultAurumServiceShouldBeFound("weightOfFreeCheck.specified=true");

        // Get all the aurumServiceList where weightOfFreeCheck is null
        defaultAurumServiceShouldNotBeFound("weightOfFreeCheck.specified=false");
    }

    @Test
    @Transactional
    void getAllAurumServicesByWeightOfFreeCheckContainsSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weightOfFreeCheck contains DEFAULT_WEIGHT_OF_FREE_CHECK
        defaultAurumServiceShouldBeFound("weightOfFreeCheck.contains=" + DEFAULT_WEIGHT_OF_FREE_CHECK);

        // Get all the aurumServiceList where weightOfFreeCheck contains UPDATED_WEIGHT_OF_FREE_CHECK
        defaultAurumServiceShouldNotBeFound("weightOfFreeCheck.contains=" + UPDATED_WEIGHT_OF_FREE_CHECK);
    }

    @Test
    @Transactional
    void getAllAurumServicesByWeightOfFreeCheckNotContainsSomething() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        // Get all the aurumServiceList where weightOfFreeCheck does not contain DEFAULT_WEIGHT_OF_FREE_CHECK
        defaultAurumServiceShouldNotBeFound("weightOfFreeCheck.doesNotContain=" + DEFAULT_WEIGHT_OF_FREE_CHECK);

        // Get all the aurumServiceList where weightOfFreeCheck does not contain UPDATED_WEIGHT_OF_FREE_CHECK
        defaultAurumServiceShouldBeFound("weightOfFreeCheck.doesNotContain=" + UPDATED_WEIGHT_OF_FREE_CHECK);
    }

    @Test
    @Transactional
    void getAllAurumServicesByVoucherIsEqualToSomething() throws Exception {
        Voucher voucher;
        if (TestUtil.findAll(em, Voucher.class).isEmpty()) {
            aurumServiceRepository.saveAndFlush(aurumService);
            voucher = VoucherResourceIT.createEntity(em);
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
        aurumServiceRepository.saveAndFlush(aurumService);

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
            .hallMarkedText(UPDATED_HALL_MARKED_TEXT)
            .weightOfFreeCheck(UPDATED_WEIGHT_OF_FREE_CHECK);

        restAurumServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAurumService.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAurumService))
            )
            .andExpect(status().isOk());

        // Validate the AurumService in the database
        List<AurumService> aurumServiceList = aurumServiceRepository.findAll();
        assertThat(aurumServiceList).hasSize(databaseSizeBeforeUpdate);
        AurumService testAurumService = aurumServiceList.get(aurumServiceList.size() - 1);
        assertThat(testAurumService.getServiceType()).isEqualTo(UPDATED_SERVICE_TYPE);
        assertThat(testAurumService.getItemName()).isEqualTo(UPDATED_ITEM_NAME);
        assertThat(testAurumService.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testAurumService.getWeight()).isEqualByComparingTo(UPDATED_WEIGHT);
        assertThat(testAurumService.getRate()).isEqualByComparingTo(UPDATED_RATE);
        assertThat(testAurumService.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testAurumService.getServiceName()).isEqualTo(UPDATED_SERVICE_NAME);
        assertThat(testAurumService.getKaratType()).isEqualTo(UPDATED_KARAT_TYPE);
        assertThat(testAurumService.getExpectedKaratType()).isEqualTo(UPDATED_EXPECTED_KARAT_TYPE);
        assertThat(testAurumService.getAddedAlloy()).isEqualTo(UPDATED_ADDED_ALLOY);
        assertThat(testAurumService.getAlloyQuantity()).isEqualByComparingTo(UPDATED_ALLOY_QUANTITY);
        assertThat(testAurumService.getServiceCharge()).isEqualByComparingTo(UPDATED_SERVICE_CHARGE);
        assertThat(testAurumService.getFreeCheck()).isEqualByComparingTo(UPDATED_FREE_CHECK);
        assertThat(testAurumService.getHallMarkedText()).isEqualTo(UPDATED_HALL_MARKED_TEXT);
        assertThat(testAurumService.getWeightOfFreeCheck()).isEqualTo(UPDATED_WEIGHT_OF_FREE_CHECK);
    }

    @Test
    @Transactional
    void putNonExistingAurumService() throws Exception {
        int databaseSizeBeforeUpdate = aurumServiceRepository.findAll().size();
        aurumService.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAurumServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aurumService.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aurumService))
            )
            .andExpect(status().isBadRequest());

        // Validate the AurumService in the database
        List<AurumService> aurumServiceList = aurumServiceRepository.findAll();
        assertThat(aurumServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAurumService() throws Exception {
        int databaseSizeBeforeUpdate = aurumServiceRepository.findAll().size();
        aurumService.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAurumServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aurumService))
            )
            .andExpect(status().isBadRequest());

        // Validate the AurumService in the database
        List<AurumService> aurumServiceList = aurumServiceRepository.findAll();
        assertThat(aurumServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAurumService() throws Exception {
        int databaseSizeBeforeUpdate = aurumServiceRepository.findAll().size();
        aurumService.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAurumServiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aurumService)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AurumService in the database
        List<AurumService> aurumServiceList = aurumServiceRepository.findAll();
        assertThat(aurumServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAurumServiceWithPatch() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        int databaseSizeBeforeUpdate = aurumServiceRepository.findAll().size();

        // Update the aurumService using partial update
        AurumService partialUpdatedAurumService = new AurumService();
        partialUpdatedAurumService.setId(aurumService.getId());

        partialUpdatedAurumService
            .weight(UPDATED_WEIGHT)
            .serviceName(UPDATED_SERVICE_NAME)
            .expectedKaratType(UPDATED_EXPECTED_KARAT_TYPE)
            .alloyQuantity(UPDATED_ALLOY_QUANTITY)
            .serviceCharge(UPDATED_SERVICE_CHARGE)
            .hallMarkedText(UPDATED_HALL_MARKED_TEXT)
            .weightOfFreeCheck(UPDATED_WEIGHT_OF_FREE_CHECK);

        restAurumServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAurumService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAurumService))
            )
            .andExpect(status().isOk());

        // Validate the AurumService in the database
        List<AurumService> aurumServiceList = aurumServiceRepository.findAll();
        assertThat(aurumServiceList).hasSize(databaseSizeBeforeUpdate);
        AurumService testAurumService = aurumServiceList.get(aurumServiceList.size() - 1);
        assertThat(testAurumService.getServiceType()).isEqualTo(DEFAULT_SERVICE_TYPE);
        assertThat(testAurumService.getItemName()).isEqualTo(DEFAULT_ITEM_NAME);
        assertThat(testAurumService.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testAurumService.getWeight()).isEqualByComparingTo(UPDATED_WEIGHT);
        assertThat(testAurumService.getRate()).isEqualByComparingTo(DEFAULT_RATE);
        assertThat(testAurumService.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testAurumService.getServiceName()).isEqualTo(UPDATED_SERVICE_NAME);
        assertThat(testAurumService.getKaratType()).isEqualTo(DEFAULT_KARAT_TYPE);
        assertThat(testAurumService.getExpectedKaratType()).isEqualTo(UPDATED_EXPECTED_KARAT_TYPE);
        assertThat(testAurumService.getAddedAlloy()).isEqualTo(DEFAULT_ADDED_ALLOY);
        assertThat(testAurumService.getAlloyQuantity()).isEqualByComparingTo(UPDATED_ALLOY_QUANTITY);
        assertThat(testAurumService.getServiceCharge()).isEqualByComparingTo(UPDATED_SERVICE_CHARGE);
        assertThat(testAurumService.getFreeCheck()).isEqualByComparingTo(DEFAULT_FREE_CHECK);
        assertThat(testAurumService.getHallMarkedText()).isEqualTo(UPDATED_HALL_MARKED_TEXT);
        assertThat(testAurumService.getWeightOfFreeCheck()).isEqualTo(UPDATED_WEIGHT_OF_FREE_CHECK);
    }

    @Test
    @Transactional
    void fullUpdateAurumServiceWithPatch() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        int databaseSizeBeforeUpdate = aurumServiceRepository.findAll().size();

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
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAurumService))
            )
            .andExpect(status().isOk());

        // Validate the AurumService in the database
        List<AurumService> aurumServiceList = aurumServiceRepository.findAll();
        assertThat(aurumServiceList).hasSize(databaseSizeBeforeUpdate);
        AurumService testAurumService = aurumServiceList.get(aurumServiceList.size() - 1);
        assertThat(testAurumService.getServiceType()).isEqualTo(UPDATED_SERVICE_TYPE);
        assertThat(testAurumService.getItemName()).isEqualTo(UPDATED_ITEM_NAME);
        assertThat(testAurumService.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testAurumService.getWeight()).isEqualByComparingTo(UPDATED_WEIGHT);
        assertThat(testAurumService.getRate()).isEqualByComparingTo(UPDATED_RATE);
        assertThat(testAurumService.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testAurumService.getServiceName()).isEqualTo(UPDATED_SERVICE_NAME);
        assertThat(testAurumService.getKaratType()).isEqualTo(UPDATED_KARAT_TYPE);
        assertThat(testAurumService.getExpectedKaratType()).isEqualTo(UPDATED_EXPECTED_KARAT_TYPE);
        assertThat(testAurumService.getAddedAlloy()).isEqualTo(UPDATED_ADDED_ALLOY);
        assertThat(testAurumService.getAlloyQuantity()).isEqualByComparingTo(UPDATED_ALLOY_QUANTITY);
        assertThat(testAurumService.getServiceCharge()).isEqualByComparingTo(UPDATED_SERVICE_CHARGE);
        assertThat(testAurumService.getFreeCheck()).isEqualByComparingTo(UPDATED_FREE_CHECK);
        assertThat(testAurumService.getHallMarkedText()).isEqualTo(UPDATED_HALL_MARKED_TEXT);
        assertThat(testAurumService.getWeightOfFreeCheck()).isEqualTo(UPDATED_WEIGHT_OF_FREE_CHECK);
    }

    @Test
    @Transactional
    void patchNonExistingAurumService() throws Exception {
        int databaseSizeBeforeUpdate = aurumServiceRepository.findAll().size();
        aurumService.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAurumServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aurumService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aurumService))
            )
            .andExpect(status().isBadRequest());

        // Validate the AurumService in the database
        List<AurumService> aurumServiceList = aurumServiceRepository.findAll();
        assertThat(aurumServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAurumService() throws Exception {
        int databaseSizeBeforeUpdate = aurumServiceRepository.findAll().size();
        aurumService.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAurumServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aurumService))
            )
            .andExpect(status().isBadRequest());

        // Validate the AurumService in the database
        List<AurumService> aurumServiceList = aurumServiceRepository.findAll();
        assertThat(aurumServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAurumService() throws Exception {
        int databaseSizeBeforeUpdate = aurumServiceRepository.findAll().size();
        aurumService.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAurumServiceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(aurumService))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AurumService in the database
        List<AurumService> aurumServiceList = aurumServiceRepository.findAll();
        assertThat(aurumServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAurumService() throws Exception {
        // Initialize the database
        aurumServiceRepository.saveAndFlush(aurumService);

        int databaseSizeBeforeDelete = aurumServiceRepository.findAll().size();

        // Delete the aurumService
        restAurumServiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, aurumService.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AurumService> aurumServiceList = aurumServiceRepository.findAll();
        assertThat(aurumServiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
