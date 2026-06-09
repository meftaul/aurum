package com.meftaul.aurum.service.impl;

import com.meftaul.aurum.domain.Item;
import com.meftaul.aurum.repository.ItemRepository;
import com.meftaul.aurum.service.ItemService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Item}.
 */
@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    private final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Item save(Item item) {
        log.debug("Request to save Item : {}", item);
        return itemRepository.save(item);
    }

    @Override
    public Item update(Item item) {
        log.debug("Request to update Item : {}", item);
        return itemRepository.save(item);
    }

    @Override
    public Optional<Item> partialUpdate(Item item) {
        log.debug("Request to partially update Item : {}", item);

        return itemRepository
            .findById(item.getId())
            .map(existingItem -> {
                if (item.getName() != null) {
                    existingItem.setName(item.getName());
                }
                if (item.getDescription() != null) {
                    existingItem.setDescription(item.getDescription());
                }
                if (item.getCode() != null) {
                    existingItem.setCode(item.getCode());
                }

                return existingItem;
            })
            .map(itemRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Item> findAll(Pageable pageable) {
        log.debug("Request to get all Items");
        return itemRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Item> findOne(Long id) {
        log.debug("Request to get Item : {}", id);
        return itemRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Item : {}", id);
        itemRepository.deleteById(id);
    }
}
