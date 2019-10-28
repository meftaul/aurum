package com.meftaul.aurum.web.rest;

import com.meftaul.aurum.AurumApp;
import com.meftaul.aurum.domain.AurumService;
import com.meftaul.aurum.repository.AurumServiceRepository;
import com.meftaul.aurum.service.AurumServiceService;
import com.meftaul.aurum.web.rest.errors.ExceptionTranslator;

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

/**
 * Integration tests for the {@link AurumServiceResource} REST controller.
 */
@SpringBootTest(classes = AurumApp.class)
public class AurumServiceResourceIT {

    private static final String DEFAULT_SERVICE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final BigDecimal DEFAULT_WEIGHT = new BigDecimal(1);
    private static final BigDecimal UPDATED_WEIGHT = new BigDecimal(2);
    private static final BigDecimal SMALLER_WEIGHT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_RATE = new BigDecimal(2);
    private static final BigDecimal SMALLER_RATE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_SERVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_KARAT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_KARAT_TYPE = "BBBBBBBBBB";

    @Autowired
    private AurumServiceRepository aurumServiceRepository;

    @Autowired
    private AurumServiceService aurumServiceService;

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
        final AurumServiceResource aurumServiceResource = new AurumServiceResource(aurumServiceService);
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
            .karatType(DEFAULT_KARAT_TYPE);
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
            .karatType(UPDATED_KARAT_TYPE);
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
            .andExpect(jsonPath("$.[*].karatType").value(hasItem(DEFAULT_KARAT_TYPE.toString())));
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
            .andExpect(jsonPath("$.karatType").value(DEFAULT_KARAT_TYPE.toString()));
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
            .karatType(UPDATED_KARAT_TYPE);

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
