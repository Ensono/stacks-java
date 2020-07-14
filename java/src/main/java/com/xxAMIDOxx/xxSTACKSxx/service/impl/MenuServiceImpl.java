package com.xxAMIDOxx.xxSTACKSxx.service.impl;

import com.microsoft.azure.spring.data.cosmosdb.core.query.CosmosPageRequest;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.SearchMenuResultItem;
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
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    private static Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);

    @Autowired
    private MenuRepository menuRepository;

    public List<Menu> all(int pageNumber, int pageSize) {

        int currentPage = 0;
        //TODO:  Validate all UUID's in DB and change "Name" back to "name"
        final Sort sort = Sort.by(Sort.Direction.ASC, "Name");

        final CosmosPageRequest pageRequest = new CosmosPageRequest(
                currentPage, pageSize, null, sort);

        Page<Menu> page = menuRepository.findAll(pageRequest);
        logger.debug("Total Records: {}", page.getTotalElements());
        logger.debug("Total Pages: {}", page.getTotalPages());

        while (currentPage < pageNumber && page.hasNext()) {
            currentPage++;
            Pageable nextPageable = page.nextPageable();
            page = menuRepository.findAll(nextPageable);
        }
        return page.getContent();
    }

    public Optional<Menu> findById(UUID id) {
        return menuRepository.findById(id.toString());
    }

    @Override
    public List<SearchMenuResultItem> findAllByRestaurantId(UUID restaurantId, Integer pageSize, Integer pageNumber) {
        final Sort sort = Sort.by(Sort.Direction.ASC, "restaurantId");
        final CosmosPageRequest pageRequest = new CosmosPageRequest(pageNumber, pageSize, null, sort);
        final Page<Menu> allByRestaurantId = menuRepository.findAllByRestaurantId(restaurantId.toString(), pageRequest);
        return getSearchMenuResultItems(Optional.of(allByRestaurantId));
    }

    @Override
    public List<SearchMenuResultItem> findAllByNameContaining(String searchTerm, Integer pageSize, Integer pageNumber) {
        final Sort sort = Sort.by(Sort.Direction.ASC, "name");
        final CosmosPageRequest pageRequest = new CosmosPageRequest(pageNumber, pageSize, null, sort);
        final Page<Menu> allByNameContaining = menuRepository.findAllByNameContaining(searchTerm, pageRequest);
        return getSearchMenuResultItems(Optional.of(allByNameContaining));
    }


    @Override
    public List<SearchMenuResultItem> findAllByRestaurantIdAndNameContaining(UUID restaurantId, String searchTerm, Integer pageSize, Integer pageNumber) {
        final Sort sort = Sort.by(Sort.Direction.ASC, "name");
        final CosmosPageRequest pageRequest = new CosmosPageRequest(pageNumber, pageSize, null, sort);
        final Page<Menu> allByRestaurantIdAndNameContaining = menuRepository.findAllByRestaurantIdAndNameContaining(restaurantId.toString(), searchTerm, pageRequest);
        return getSearchMenuResultItems(Optional.of(allByRestaurantIdAndNameContaining));
    }


    private List<SearchMenuResultItem> getSearchMenuResultItems(Optional<Page<Menu>> pages) {
        return pages.map(menus -> menus.stream().map(SearchMenuResultItem::new)
                .collect(Collectors.toList())).orElse(Collections.emptyList());
    }


}
