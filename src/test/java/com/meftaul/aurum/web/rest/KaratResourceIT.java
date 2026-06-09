package com.meftaul.aurum.web.rest;

import com.meftaul.aurum.AurumApp;
import com.meftaul.aurum.domain.Karat;
import com.meftaul.aurum.repository.KaratRepository;
import com.meftaul.aurum.service.KaratService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link KaratResource} REST controller.
 */
@SpringBootTest(classes = AurumApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class KaratResourceIT {

    private static final String DEFAULT_KARAT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_KARAT_TYPE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PURITY_PERCENT = new BigDecimal(0);
    private static final BigDecimal UPDATED_PURITY_PERCENT = new BigDecimal(1);

    @Autowired
    private KaratRepository karatRepository;

    @Autowired
    private KaratService karatService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKaratMockMvc;

    private Karat karat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Karat createEntity(EntityManager em) {
        Karat karat = new Karat()
            .karatType(DEFAULT_KARAT_TYPE)
            .purityPercent(DEFAULT_PURITY_PERCENT);
        return karat;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Karat createUpdatedEntity(EntityManager em) {
        Karat karat = new Karat()
            .karatType(UPDATED_KARAT_TYPE)
            .purityPercent(UPDATED_PURITY_PERCENT);
        return karat;
    }

    @BeforeEach
    public void initTest() {
        karat = createEntity(em);
    }

    @Test
    @Transactional
    public void createKarat() throws Exception {
        int databaseSizeBeforeCreate = karatRepository.findAll().size();
        // Create the Karat
        restKaratMockMvc.perform(post("/api/karats")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(karat)))
            .andExpect(status().isCreated());

        // Validate the Karat in the database
        List<Karat> karatList = karatRepository.findAll();
        assertThat(karatList).hasSize(databaseSizeBeforeCreate + 1);
        Karat testKarat = karatList.get(karatList.size() - 1);
        assertThat(testKarat.getKaratType()).isEqualTo(DEFAULT_KARAT_TYPE);
        assertThat(testKarat.getPurityPercent()).isEqualTo(DEFAULT_PURITY_PERCENT);
    }

    @Test
    @Transactional
    public void createKaratWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = karatRepository.findAll().size();

        // Create the Karat with an existing ID
        karat.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restKaratMockMvc.perform(post("/api/karats")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(karat)))
            .andExpect(status().isBadRequest());

        // Validate the Karat in the database
        List<Karat> karatList = karatRepository.findAll();
        assertThat(karatList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllKarats() throws Exception {
        // Initialize the database
        karatRepository.saveAndFlush(karat);

        // Get all the karatList
        restKaratMockMvc.perform(get("/api/karats?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(karat.getId().intValue())))
            .andExpect(jsonPath("$.[*].karatType").value(hasItem(DEFAULT_KARAT_TYPE)))
            .andExpect(jsonPath("$.[*].purityPercent").value(hasItem(DEFAULT_PURITY_PERCENT.intValue())));
    }
    
    @Test
    @Transactional
    public void getKarat() throws Exception {
        // Initialize the database
        karatRepository.saveAndFlush(karat);

        // Get the karat
        restKaratMockMvc.perform(get("/api/karats/{id}", karat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(karat.getId().intValue()))
            .andExpect(jsonPath("$.karatType").value(DEFAULT_KARAT_TYPE))
            .andExpect(jsonPath("$.purityPercent").value(DEFAULT_PURITY_PERCENT.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingKarat() throws Exception {
        // Get the karat
        restKaratMockMvc.perform(get("/api/karats/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateKarat() throws Exception {
        // Initialize the database
        karatService.save(karat);

        int databaseSizeBeforeUpdate = karatRepository.findAll().size();

        // Update the karat
        Karat updatedKarat = karatRepository.findById(karat.getId()).get();
        // Disconnect from session so that the updates on updatedKarat are not directly saved in db
        em.detach(updatedKarat);
        updatedKarat
            .karatType(UPDATED_KARAT_TYPE)
            .purityPercent(UPDATED_PURITY_PERCENT);

        restKaratMockMvc.perform(put("/api/karats")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedKarat)))
            .andExpect(status().isOk());

        // Validate the Karat in the database
        List<Karat> karatList = karatRepository.findAll();
        assertThat(karatList).hasSize(databaseSizeBeforeUpdate);
        Karat testKarat = karatList.get(karatList.size() - 1);
        assertThat(testKarat.getKaratType()).isEqualTo(UPDATED_KARAT_TYPE);
        assertThat(testKarat.getPurityPercent()).isEqualTo(UPDATED_PURITY_PERCENT);
    }

    @Test
    @Transactional
    public void updateNonExistingKarat() throws Exception {
        int databaseSizeBeforeUpdate = karatRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKaratMockMvc.perform(put("/api/karats")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(karat)))
            .andExpect(status().isBadRequest());

        // Validate the Karat in the database
        List<Karat> karatList = karatRepository.findAll();
        assertThat(karatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteKarat() throws Exception {
        // Initialize the database
        karatService.save(karat);

        int databaseSizeBeforeDelete = karatRepository.findAll().size();

        // Delete the karat
        restKaratMockMvc.perform(delete("/api/karats/{id}", karat.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Karat> karatList = karatRepository.findAll();
        assertThat(karatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
