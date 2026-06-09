package com.meftaul.aurum.web.rest;

import static com.meftaul.aurum.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.meftaul.aurum.IntegrationTest;
import com.meftaul.aurum.domain.Karat;
import com.meftaul.aurum.repository.KaratRepository;
import java.math.BigDecimal;
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
 * Integration tests for the {@link KaratResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class KaratResourceIT {

    private static final String DEFAULT_KARAT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_KARAT_TYPE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PURITY_PERCENT = new BigDecimal(0);
    private static final BigDecimal UPDATED_PURITY_PERCENT = new BigDecimal(1);

    private static final String ENTITY_API_URL = "/api/karats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private KaratRepository karatRepository;

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
        Karat karat = new Karat().karatType(DEFAULT_KARAT_TYPE).purityPercent(DEFAULT_PURITY_PERCENT);
        return karat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Karat createUpdatedEntity(EntityManager em) {
        Karat karat = new Karat().karatType(UPDATED_KARAT_TYPE).purityPercent(UPDATED_PURITY_PERCENT);
        return karat;
    }

    @BeforeEach
    public void initTest() {
        karat = createEntity(em);
    }

    @Test
    @Transactional
    void createKarat() throws Exception {
        int databaseSizeBeforeCreate = karatRepository.findAll().size();
        // Create the Karat
        restKaratMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(karat)))
            .andExpect(status().isCreated());

        // Validate the Karat in the database
        List<Karat> karatList = karatRepository.findAll();
        assertThat(karatList).hasSize(databaseSizeBeforeCreate + 1);
        Karat testKarat = karatList.get(karatList.size() - 1);
        assertThat(testKarat.getKaratType()).isEqualTo(DEFAULT_KARAT_TYPE);
        assertThat(testKarat.getPurityPercent()).isEqualByComparingTo(DEFAULT_PURITY_PERCENT);
    }

    @Test
    @Transactional
    void createKaratWithExistingId() throws Exception {
        // Create the Karat with an existing ID
        karat.setId(1L);

        int databaseSizeBeforeCreate = karatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restKaratMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(karat)))
            .andExpect(status().isBadRequest());

        // Validate the Karat in the database
        List<Karat> karatList = karatRepository.findAll();
        assertThat(karatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllKarats() throws Exception {
        // Initialize the database
        karatRepository.saveAndFlush(karat);

        // Get all the karatList
        restKaratMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(karat.getId().intValue())))
            .andExpect(jsonPath("$.[*].karatType").value(hasItem(DEFAULT_KARAT_TYPE)))
            .andExpect(jsonPath("$.[*].purityPercent").value(hasItem(sameNumber(DEFAULT_PURITY_PERCENT))));
    }

    @Test
    @Transactional
    void getKarat() throws Exception {
        // Initialize the database
        karatRepository.saveAndFlush(karat);

        // Get the karat
        restKaratMockMvc
            .perform(get(ENTITY_API_URL_ID, karat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(karat.getId().intValue()))
            .andExpect(jsonPath("$.karatType").value(DEFAULT_KARAT_TYPE))
            .andExpect(jsonPath("$.purityPercent").value(sameNumber(DEFAULT_PURITY_PERCENT)));
    }

    @Test
    @Transactional
    void getNonExistingKarat() throws Exception {
        // Get the karat
        restKaratMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingKarat() throws Exception {
        // Initialize the database
        karatRepository.saveAndFlush(karat);

        int databaseSizeBeforeUpdate = karatRepository.findAll().size();

        // Update the karat
        Karat updatedKarat = karatRepository.findById(karat.getId()).get();
        // Disconnect from session so that the updates on updatedKarat are not directly saved in db
        em.detach(updatedKarat);
        updatedKarat.karatType(UPDATED_KARAT_TYPE).purityPercent(UPDATED_PURITY_PERCENT);

        restKaratMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedKarat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedKarat))
            )
            .andExpect(status().isOk());

        // Validate the Karat in the database
        List<Karat> karatList = karatRepository.findAll();
        assertThat(karatList).hasSize(databaseSizeBeforeUpdate);
        Karat testKarat = karatList.get(karatList.size() - 1);
        assertThat(testKarat.getKaratType()).isEqualTo(UPDATED_KARAT_TYPE);
        assertThat(testKarat.getPurityPercent()).isEqualByComparingTo(UPDATED_PURITY_PERCENT);
    }

    @Test
    @Transactional
    void putNonExistingKarat() throws Exception {
        int databaseSizeBeforeUpdate = karatRepository.findAll().size();
        karat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKaratMockMvc
            .perform(
                put(ENTITY_API_URL_ID, karat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(karat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Karat in the database
        List<Karat> karatList = karatRepository.findAll();
        assertThat(karatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchKarat() throws Exception {
        int databaseSizeBeforeUpdate = karatRepository.findAll().size();
        karat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKaratMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(karat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Karat in the database
        List<Karat> karatList = karatRepository.findAll();
        assertThat(karatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamKarat() throws Exception {
        int databaseSizeBeforeUpdate = karatRepository.findAll().size();
        karat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKaratMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(karat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Karat in the database
        List<Karat> karatList = karatRepository.findAll();
        assertThat(karatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateKaratWithPatch() throws Exception {
        // Initialize the database
        karatRepository.saveAndFlush(karat);

        int databaseSizeBeforeUpdate = karatRepository.findAll().size();

        // Update the karat using partial update
        Karat partialUpdatedKarat = new Karat();
        partialUpdatedKarat.setId(karat.getId());

        partialUpdatedKarat.karatType(UPDATED_KARAT_TYPE).purityPercent(UPDATED_PURITY_PERCENT);

        restKaratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKarat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedKarat))
            )
            .andExpect(status().isOk());

        // Validate the Karat in the database
        List<Karat> karatList = karatRepository.findAll();
        assertThat(karatList).hasSize(databaseSizeBeforeUpdate);
        Karat testKarat = karatList.get(karatList.size() - 1);
        assertThat(testKarat.getKaratType()).isEqualTo(UPDATED_KARAT_TYPE);
        assertThat(testKarat.getPurityPercent()).isEqualByComparingTo(UPDATED_PURITY_PERCENT);
    }

    @Test
    @Transactional
    void fullUpdateKaratWithPatch() throws Exception {
        // Initialize the database
        karatRepository.saveAndFlush(karat);

        int databaseSizeBeforeUpdate = karatRepository.findAll().size();

        // Update the karat using partial update
        Karat partialUpdatedKarat = new Karat();
        partialUpdatedKarat.setId(karat.getId());

        partialUpdatedKarat.karatType(UPDATED_KARAT_TYPE).purityPercent(UPDATED_PURITY_PERCENT);

        restKaratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKarat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedKarat))
            )
            .andExpect(status().isOk());

        // Validate the Karat in the database
        List<Karat> karatList = karatRepository.findAll();
        assertThat(karatList).hasSize(databaseSizeBeforeUpdate);
        Karat testKarat = karatList.get(karatList.size() - 1);
        assertThat(testKarat.getKaratType()).isEqualTo(UPDATED_KARAT_TYPE);
        assertThat(testKarat.getPurityPercent()).isEqualByComparingTo(UPDATED_PURITY_PERCENT);
    }

    @Test
    @Transactional
    void patchNonExistingKarat() throws Exception {
        int databaseSizeBeforeUpdate = karatRepository.findAll().size();
        karat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKaratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, karat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(karat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Karat in the database
        List<Karat> karatList = karatRepository.findAll();
        assertThat(karatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchKarat() throws Exception {
        int databaseSizeBeforeUpdate = karatRepository.findAll().size();
        karat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKaratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(karat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Karat in the database
        List<Karat> karatList = karatRepository.findAll();
        assertThat(karatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamKarat() throws Exception {
        int databaseSizeBeforeUpdate = karatRepository.findAll().size();
        karat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKaratMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(karat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Karat in the database
        List<Karat> karatList = karatRepository.findAll();
        assertThat(karatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteKarat() throws Exception {
        // Initialize the database
        karatRepository.saveAndFlush(karat);

        int databaseSizeBeforeDelete = karatRepository.findAll().size();

        // Delete the karat
        restKaratMockMvc
            .perform(delete(ENTITY_API_URL_ID, karat.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Karat> karatList = karatRepository.findAll();
        assertThat(karatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
