package com.meftaul.aurum.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.meftaul.aurum.IntegrationTest;
import com.meftaul.aurum.domain.Customer;
import com.meftaul.aurum.repository.CustomerRepository;
import com.meftaul.aurum.service.criteria.CustomerCriteria;
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
 * Integration tests for the {@link CustomerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Long DEFAULT_TOTAL_POINT = 0L;
    private static final Long UPDATED_TOTAL_POINT = 1L;
    private static final Long SMALLER_TOTAL_POINT = 0L - 1L;

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOM_ID = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOM_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerMockMvc;

    private Customer customer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createEntity(EntityManager em) {
        Customer customer = new Customer()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL)
            .address(DEFAULT_ADDRESS)
            .totalPoint(DEFAULT_TOTAL_POINT)
            .reference(DEFAULT_REFERENCE)
            .customId(DEFAULT_CUSTOM_ID);
        return customer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createUpdatedEntity(EntityManager em) {
        Customer customer = new Customer()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .totalPoint(UPDATED_TOTAL_POINT)
            .reference(UPDATED_REFERENCE)
            .customId(UPDATED_CUSTOM_ID);
        return customer;
    }

    @BeforeEach
    public void initTest() {
        customer = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();
        // Create the Customer
        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testCustomer.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testCustomer.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCustomer.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCustomer.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCustomer.getTotalPoint()).isEqualTo(DEFAULT_TOTAL_POINT);
        assertThat(testCustomer.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testCustomer.getCustomId()).isEqualTo(DEFAULT_CUSTOM_ID);
    }

    @Test
    @Transactional
    void createCustomerWithExistingId() throws Exception {
        // Create the Customer with an existing ID
        customer.setId(1L);

        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setPhone(null);

        // Create the Customer, which fails.

        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomers() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].totalPoint").value(hasItem(DEFAULT_TOTAL_POINT.intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].customId").value(hasItem(DEFAULT_CUSTOM_ID)));
    }

    @Test
    @Transactional
    void getCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get the customer
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL_ID, customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customer.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.totalPoint").value(DEFAULT_TOTAL_POINT.intValue()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.customId").value(DEFAULT_CUSTOM_ID));
    }

    @Test
    @Transactional
    void getCustomersByIdFiltering() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        Long id = customer.getId();

        defaultCustomerShouldBeFound("id.equals=" + id);
        defaultCustomerShouldNotBeFound("id.notEquals=" + id);

        defaultCustomerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCustomerShouldNotBeFound("id.greaterThan=" + id);

        defaultCustomerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCustomerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where firstName equals to DEFAULT_FIRST_NAME
        defaultCustomerShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the customerList where firstName equals to UPDATED_FIRST_NAME
        defaultCustomerShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultCustomerShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the customerList where firstName equals to UPDATED_FIRST_NAME
        defaultCustomerShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where firstName is not null
        defaultCustomerShouldBeFound("firstName.specified=true");

        // Get all the customerList where firstName is null
        defaultCustomerShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where firstName contains DEFAULT_FIRST_NAME
        defaultCustomerShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the customerList where firstName contains UPDATED_FIRST_NAME
        defaultCustomerShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where firstName does not contain DEFAULT_FIRST_NAME
        defaultCustomerShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the customerList where firstName does not contain UPDATED_FIRST_NAME
        defaultCustomerShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastName equals to DEFAULT_LAST_NAME
        defaultCustomerShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the customerList where lastName equals to UPDATED_LAST_NAME
        defaultCustomerShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultCustomerShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the customerList where lastName equals to UPDATED_LAST_NAME
        defaultCustomerShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastName is not null
        defaultCustomerShouldBeFound("lastName.specified=true");

        // Get all the customerList where lastName is null
        defaultCustomerShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastName contains DEFAULT_LAST_NAME
        defaultCustomerShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the customerList where lastName contains UPDATED_LAST_NAME
        defaultCustomerShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastName does not contain DEFAULT_LAST_NAME
        defaultCustomerShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the customerList where lastName does not contain UPDATED_LAST_NAME
        defaultCustomerShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phone equals to DEFAULT_PHONE
        defaultCustomerShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the customerList where phone equals to UPDATED_PHONE
        defaultCustomerShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultCustomerShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the customerList where phone equals to UPDATED_PHONE
        defaultCustomerShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phone is not null
        defaultCustomerShouldBeFound("phone.specified=true");

        // Get all the customerList where phone is null
        defaultCustomerShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByPhoneContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phone contains DEFAULT_PHONE
        defaultCustomerShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the customerList where phone contains UPDATED_PHONE
        defaultCustomerShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomersByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phone does not contain DEFAULT_PHONE
        defaultCustomerShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the customerList where phone does not contain UPDATED_PHONE
        defaultCustomerShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where email equals to DEFAULT_EMAIL
        defaultCustomerShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the customerList where email equals to UPDATED_EMAIL
        defaultCustomerShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultCustomerShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the customerList where email equals to UPDATED_EMAIL
        defaultCustomerShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where email is not null
        defaultCustomerShouldBeFound("email.specified=true");

        // Get all the customerList where email is null
        defaultCustomerShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByEmailContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where email contains DEFAULT_EMAIL
        defaultCustomerShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the customerList where email contains UPDATED_EMAIL
        defaultCustomerShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where email does not contain DEFAULT_EMAIL
        defaultCustomerShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the customerList where email does not contain UPDATED_EMAIL
        defaultCustomerShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCustomersByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where address equals to DEFAULT_ADDRESS
        defaultCustomerShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the customerList where address equals to UPDATED_ADDRESS
        defaultCustomerShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultCustomerShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the customerList where address equals to UPDATED_ADDRESS
        defaultCustomerShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where address is not null
        defaultCustomerShouldBeFound("address.specified=true");

        // Get all the customerList where address is null
        defaultCustomerShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByAddressContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where address contains DEFAULT_ADDRESS
        defaultCustomerShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the customerList where address contains UPDATED_ADDRESS
        defaultCustomerShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where address does not contain DEFAULT_ADDRESS
        defaultCustomerShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the customerList where address does not contain UPDATED_ADDRESS
        defaultCustomerShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByTotalPointIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where totalPoint equals to DEFAULT_TOTAL_POINT
        defaultCustomerShouldBeFound("totalPoint.equals=" + DEFAULT_TOTAL_POINT);

        // Get all the customerList where totalPoint equals to UPDATED_TOTAL_POINT
        defaultCustomerShouldNotBeFound("totalPoint.equals=" + UPDATED_TOTAL_POINT);
    }

    @Test
    @Transactional
    void getAllCustomersByTotalPointIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where totalPoint in DEFAULT_TOTAL_POINT or UPDATED_TOTAL_POINT
        defaultCustomerShouldBeFound("totalPoint.in=" + DEFAULT_TOTAL_POINT + "," + UPDATED_TOTAL_POINT);

        // Get all the customerList where totalPoint equals to UPDATED_TOTAL_POINT
        defaultCustomerShouldNotBeFound("totalPoint.in=" + UPDATED_TOTAL_POINT);
    }

    @Test
    @Transactional
    void getAllCustomersByTotalPointIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where totalPoint is not null
        defaultCustomerShouldBeFound("totalPoint.specified=true");

        // Get all the customerList where totalPoint is null
        defaultCustomerShouldNotBeFound("totalPoint.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByTotalPointIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where totalPoint is greater than or equal to DEFAULT_TOTAL_POINT
        defaultCustomerShouldBeFound("totalPoint.greaterThanOrEqual=" + DEFAULT_TOTAL_POINT);

        // Get all the customerList where totalPoint is greater than or equal to UPDATED_TOTAL_POINT
        defaultCustomerShouldNotBeFound("totalPoint.greaterThanOrEqual=" + UPDATED_TOTAL_POINT);
    }

    @Test
    @Transactional
    void getAllCustomersByTotalPointIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where totalPoint is less than or equal to DEFAULT_TOTAL_POINT
        defaultCustomerShouldBeFound("totalPoint.lessThanOrEqual=" + DEFAULT_TOTAL_POINT);

        // Get all the customerList where totalPoint is less than or equal to SMALLER_TOTAL_POINT
        defaultCustomerShouldNotBeFound("totalPoint.lessThanOrEqual=" + SMALLER_TOTAL_POINT);
    }

    @Test
    @Transactional
    void getAllCustomersByTotalPointIsLessThanSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where totalPoint is less than DEFAULT_TOTAL_POINT
        defaultCustomerShouldNotBeFound("totalPoint.lessThan=" + DEFAULT_TOTAL_POINT);

        // Get all the customerList where totalPoint is less than UPDATED_TOTAL_POINT
        defaultCustomerShouldBeFound("totalPoint.lessThan=" + UPDATED_TOTAL_POINT);
    }

    @Test
    @Transactional
    void getAllCustomersByTotalPointIsGreaterThanSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where totalPoint is greater than DEFAULT_TOTAL_POINT
        defaultCustomerShouldNotBeFound("totalPoint.greaterThan=" + DEFAULT_TOTAL_POINT);

        // Get all the customerList where totalPoint is greater than SMALLER_TOTAL_POINT
        defaultCustomerShouldBeFound("totalPoint.greaterThan=" + SMALLER_TOTAL_POINT);
    }

    @Test
    @Transactional
    void getAllCustomersByReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where reference equals to DEFAULT_REFERENCE
        defaultCustomerShouldBeFound("reference.equals=" + DEFAULT_REFERENCE);

        // Get all the customerList where reference equals to UPDATED_REFERENCE
        defaultCustomerShouldNotBeFound("reference.equals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllCustomersByReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where reference in DEFAULT_REFERENCE or UPDATED_REFERENCE
        defaultCustomerShouldBeFound("reference.in=" + DEFAULT_REFERENCE + "," + UPDATED_REFERENCE);

        // Get all the customerList where reference equals to UPDATED_REFERENCE
        defaultCustomerShouldNotBeFound("reference.in=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllCustomersByReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where reference is not null
        defaultCustomerShouldBeFound("reference.specified=true");

        // Get all the customerList where reference is null
        defaultCustomerShouldNotBeFound("reference.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByReferenceContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where reference contains DEFAULT_REFERENCE
        defaultCustomerShouldBeFound("reference.contains=" + DEFAULT_REFERENCE);

        // Get all the customerList where reference contains UPDATED_REFERENCE
        defaultCustomerShouldNotBeFound("reference.contains=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllCustomersByReferenceNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where reference does not contain DEFAULT_REFERENCE
        defaultCustomerShouldNotBeFound("reference.doesNotContain=" + DEFAULT_REFERENCE);

        // Get all the customerList where reference does not contain UPDATED_REFERENCE
        defaultCustomerShouldBeFound("reference.doesNotContain=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomIdIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customId equals to DEFAULT_CUSTOM_ID
        defaultCustomerShouldBeFound("customId.equals=" + DEFAULT_CUSTOM_ID);

        // Get all the customerList where customId equals to UPDATED_CUSTOM_ID
        defaultCustomerShouldNotBeFound("customId.equals=" + UPDATED_CUSTOM_ID);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomIdIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customId in DEFAULT_CUSTOM_ID or UPDATED_CUSTOM_ID
        defaultCustomerShouldBeFound("customId.in=" + DEFAULT_CUSTOM_ID + "," + UPDATED_CUSTOM_ID);

        // Get all the customerList where customId equals to UPDATED_CUSTOM_ID
        defaultCustomerShouldNotBeFound("customId.in=" + UPDATED_CUSTOM_ID);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customId is not null
        defaultCustomerShouldBeFound("customId.specified=true");

        // Get all the customerList where customId is null
        defaultCustomerShouldNotBeFound("customId.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByCustomIdContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customId contains DEFAULT_CUSTOM_ID
        defaultCustomerShouldBeFound("customId.contains=" + DEFAULT_CUSTOM_ID);

        // Get all the customerList where customId contains UPDATED_CUSTOM_ID
        defaultCustomerShouldNotBeFound("customId.contains=" + UPDATED_CUSTOM_ID);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomIdNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customId does not contain DEFAULT_CUSTOM_ID
        defaultCustomerShouldNotBeFound("customId.doesNotContain=" + DEFAULT_CUSTOM_ID);

        // Get all the customerList where customId does not contain UPDATED_CUSTOM_ID
        defaultCustomerShouldBeFound("customId.doesNotContain=" + UPDATED_CUSTOM_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerShouldBeFound(String filter) throws Exception {
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].totalPoint").value(hasItem(DEFAULT_TOTAL_POINT.intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].customId").value(hasItem(DEFAULT_CUSTOM_ID)));

        // Check, that the count call also returns 1
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerShouldNotBeFound(String filter) throws Exception {
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer
        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        // Disconnect from session so that the updates on updatedCustomer are not directly saved in db
        em.detach(updatedCustomer);
        updatedCustomer
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .totalPoint(UPDATED_TOTAL_POINT)
            .reference(UPDATED_REFERENCE)
            .customId(UPDATED_CUSTOM_ID);

        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCustomer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCustomer))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCustomer.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCustomer.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCustomer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCustomer.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCustomer.getTotalPoint()).isEqualTo(UPDATED_TOTAL_POINT);
        assertThat(testCustomer.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testCustomer.getCustomId()).isEqualTo(UPDATED_CUSTOM_ID);
    }

    @Test
    @Transactional
    void putNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerWithPatch() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer using partial update
        Customer partialUpdatedCustomer = new Customer();
        partialUpdatedCustomer.setId(customer.getId());

        partialUpdatedCustomer.firstName(UPDATED_FIRST_NAME).address(UPDATED_ADDRESS).customId(UPDATED_CUSTOM_ID);

        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomer))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCustomer.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testCustomer.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCustomer.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCustomer.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCustomer.getTotalPoint()).isEqualTo(DEFAULT_TOTAL_POINT);
        assertThat(testCustomer.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testCustomer.getCustomId()).isEqualTo(UPDATED_CUSTOM_ID);
    }

    @Test
    @Transactional
    void fullUpdateCustomerWithPatch() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer using partial update
        Customer partialUpdatedCustomer = new Customer();
        partialUpdatedCustomer.setId(customer.getId());

        partialUpdatedCustomer
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .totalPoint(UPDATED_TOTAL_POINT)
            .reference(UPDATED_REFERENCE)
            .customId(UPDATED_CUSTOM_ID);

        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomer))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCustomer.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCustomer.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCustomer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCustomer.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCustomer.getTotalPoint()).isEqualTo(UPDATED_TOTAL_POINT);
        assertThat(testCustomer.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testCustomer.getCustomId()).isEqualTo(UPDATED_CUSTOM_ID);
    }

    @Test
    @Transactional
    void patchNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeDelete = customerRepository.findAll().size();

        // Delete the customer
        restCustomerMockMvc
            .perform(delete(ENTITY_API_URL_ID, customer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
