package com.xxAMIDOxx.xxSTACKSxx.service.impl;

import com.microsoft.azure.spring.data.cosmosdb.core.query.CosmosPageRequest;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import com.xxAMIDOxx.xxSTACKSxx.repository.MenuRepository;
import com.xxAMIDOxx.xxSTACKSxx.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MenuServiceImpl implements MenuService {

    Logger logger = LoggerFactory.getLogger(MenuService.class);

    private final MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<Menu> all(int pageNumber, int pageSize) {

        logger.info("pageNumber: {}, pageSize: {}", pageNumber, pageSize);
        final Sort sort = Sort.by(Sort.Direction.ASC, "Name");
        final CosmosPageRequest pageRequest = new CosmosPageRequest(0, pageSize, null, sort);

        Page<Menu> page = this.menuRepository.findAll(pageRequest);
        int currentPage = 0;

        while (currentPage < pageNumber && page.hasNext()) {
            currentPage++;
            logger.info("Getting page {}", currentPage);
            Pageable nextPageable = page.nextPageable();
            page = this.menuRepository.findAll(nextPageable);
        }
        return page.getContent();
    }

    public Menu findById(UUID id) {
        return this.menuRepository.findById(id.toString()).orElse(null);
    }
}
