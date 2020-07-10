package com.xxAMIDOxx.xxSTACKSxx.service.impl;

import com.microsoft.azure.spring.data.cosmosdb.core.CosmosTemplate;
import com.microsoft.azure.spring.data.cosmosdb.core.query.CosmosPageRequest;
import com.microsoft.azure.spring.data.cosmosdb.core.query.Criteria;
import com.microsoft.azure.spring.data.cosmosdb.core.query.CriteriaType;
import com.microsoft.azure.spring.data.cosmosdb.core.query.DocumentQuery;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import com.xxAMIDOxx.xxSTACKSxx.repository.MenuRepository;
import com.xxAMIDOxx.xxSTACKSxx.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("checkstyle:Indentation")
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private CosmosTemplate cosmosTemplate;

    private static Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);

    private final MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<Menu> all(int pageNumber, int pageSize) {

        int currentPage = 0;
        final Sort sort = Sort.by(Sort.Direction.ASC, "Name");

        final CosmosPageRequest pageRequest = new CosmosPageRequest(
                currentPage, pageSize, null, sort);

        Page<Menu> page = this.menuRepository.findAll(pageRequest);
        logger.debug("Total Records: {}", page.getTotalElements());
        logger.debug("Total Pages: {}", page.getTotalPages());

        while (currentPage < pageNumber && page.hasNext()) {
            currentPage++;
            Pageable nextPageable = page.nextPageable();
            page = this.menuRepository.findAll(nextPageable);
        }
        return page.getContent();
    }

    public Optional<Menu> findById(UUID id) {
        return this.menuRepository.findById(id.toString());
    }

    @Override
    public Page<Menu> findAllByRestaurantId(UUID restaurantId, Pageable pageable) {
        return this.menuRepository.findAllByRestaurantId(restaurantId,pageable);
    }

//    @Override
//    public Page<Menu> findAllByNameContaining(String searchTerm, Pageable pageable) {
//        return this.menuRepository.findAllByNameContaining(searchTerm, pageable);
//    }


    @Override
    public Page<Menu> findAllByNameContaining(String searchTerm, Pageable pageable) {

        DocumentQuery query = new DocumentQuery(Criteria.getInstance(CriteriaType.CONTAINING, "name",
                Collections.singletonList(searchTerm))).with(pageable);
        return cosmosTemplate.paginationQuery(query, Menu.class, cosmosTemplate.getContainerName(Menu.class));

        //return this.menuRepository.findAllByNameContaining(searchTerm, pageable);
    }


    @Override
    public Page<Menu> findAllByRestaurantIdAndNameContaining(UUID restaurantId, String searchTerm, Pageable pageable) {
        return this.menuRepository.findAllByRestaurantIdAndNameContaining(restaurantId,searchTerm,pageable);
    }
}
