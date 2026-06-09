package com.meftaul.aurum.web.rest;

import static com.meftaul.aurum.domain.KaratAsserts.*;
import static com.meftaul.aurum.web.rest.TestUtil.createUpdateProxyForBean;
import static com.meftaul.aurum.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meftaul.aurum.IntegrationTest;
import com.meftaul.aurum.domain.Karat;
import com.meftaul.aurum.repository.KaratRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private KaratRepository karatRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKaratMockMvc;

    private Karat karat;

    private Karat insertedKarat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Karat createEntity() {
        return new Karat().karatType(DEFAULT_KARAT_TYPE).purityPercent(DEFAULT_PURITY_PERCENT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Karat createUpdatedEntity() {
        return new Karat().karatType(UPDATED_KARAT_TYPE).purityPercent(UPDATED_PURITY_PERCENT);
    }

    @BeforeEach
    void initTest() {
        karat = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedKarat != null) {
            karatRepository.delete(insertedKarat);
            insertedKarat = null;
        }
    }

    @Test
    @Transactional
    void createKarat() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Karat
        var returnedKarat = om.readValue(
            restKaratMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(karat)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Karat.class
        );

        // Validate the Karat in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertKaratUpdatableFieldsEquals(returnedKarat, getPersistedKarat(returnedKarat));

        insertedKarat = returnedKarat;
    }

    @Test
    @Transactional
    void createKaratWithExistingId() throws Exception {
        // Create the Karat with an existing ID
        karat.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restKaratMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(karat)))
            .andExpect(status().isBadRequest());

        // Validate the Karat in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllKarats() throws Exception {
        // Initialize the database
        insertedKarat = karatRepository.saveAndFlush(karat);

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
        insertedKarat = karatRepository.saveAndFlush(karat);

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
        insertedKarat = karatRepository.saveAndFlush(karat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the karat
        Karat updatedKarat = karatRepository.findById(karat.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedKarat are not directly saved in db
        em.detach(updatedKarat);
        updatedKarat.karatType(UPDATED_KARAT_TYPE).purityPercent(UPDATED_PURITY_PERCENT);

        restKaratMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedKarat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedKarat))
            )
            .andExpect(status().isOk());

        // Validate the Karat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedKaratToMatchAllProperties(updatedKarat);
    }

    @Test
    @Transactional
    void putNonExistingKarat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        karat.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKaratMockMvc
            .perform(put(ENTITY_API_URL_ID, karat.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(karat)))
            .andExpect(status().isBadRequest());

        // Validate the Karat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchKarat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        karat.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKaratMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(karat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Karat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamKarat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        karat.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKaratMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(karat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Karat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateKaratWithPatch() throws Exception {
        // Initialize the database
        insertedKarat = karatRepository.saveAndFlush(karat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the karat using partial update
        Karat partialUpdatedKarat = new Karat();
        partialUpdatedKarat.setId(karat.getId());

        partialUpdatedKarat.purityPercent(UPDATED_PURITY_PERCENT);

        restKaratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKarat.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedKarat))
            )
            .andExpect(status().isOk());

        // Validate the Karat in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertKaratUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedKarat, karat), getPersistedKarat(karat));
    }

    @Test
    @Transactional
    void fullUpdateKaratWithPatch() throws Exception {
        // Initialize the database
        insertedKarat = karatRepository.saveAndFlush(karat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the karat using partial update
        Karat partialUpdatedKarat = new Karat();
        partialUpdatedKarat.setId(karat.getId());

        partialUpdatedKarat.karatType(UPDATED_KARAT_TYPE).purityPercent(UPDATED_PURITY_PERCENT);

        restKaratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKarat.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedKarat))
            )
            .andExpect(status().isOk());

        // Validate the Karat in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertKaratUpdatableFieldsEquals(partialUpdatedKarat, getPersistedKarat(partialUpdatedKarat));
    }

    @Test
    @Transactional
    void patchNonExistingKarat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        karat.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKaratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, karat.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(karat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Karat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchKarat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        karat.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKaratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(karat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Karat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamKarat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        karat.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKaratMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(karat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Karat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteKarat() throws Exception {
        // Initialize the database
        insertedKarat = karatRepository.saveAndFlush(karat);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the karat
        restKaratMockMvc
            .perform(delete(ENTITY_API_URL_ID, karat.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return karatRepository.count();
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

    protected Karat getPersistedKarat(Karat karat) {
        return karatRepository.findById(karat.getId()).orElseThrow();
    }

    protected void assertPersistedKaratToMatchAllProperties(Karat expectedKarat) {
        assertKaratAllPropertiesEquals(expectedKarat, getPersistedKarat(expectedKarat));
    }

    protected void assertPersistedKaratToMatchUpdatableProperties(Karat expectedKarat) {
        assertKaratAllUpdatablePropertiesEquals(expectedKarat, getPersistedKarat(expectedKarat));
    }
}
