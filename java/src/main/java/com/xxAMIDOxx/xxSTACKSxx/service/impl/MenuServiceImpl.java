package com.xxAMIDOxx.xxSTACKSxx.service.impl;

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
import java.util.Optional;
import java.util.UUID;

import static com.xxAMIDOxx.xxSTACKSxx.utils.UtilityMethods.pageRequestWithSort;

@Service
public class MenuServiceImpl implements MenuService {

    private static final String RESTAURANT_ID = "restaurantId";
    private static final String NAME = "name";
    private static Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);

    private MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<Menu> findAll(int pageNumber, int pageSize) {
        int currentPage = 0;
        //TODO:  Validate all UUID's in DB and change "Name" back to "name"

        Page<Menu> page = menuRepository.findAll(pageRequestWithSort(Sort.Direction.ASC, "Name", currentPage, pageSize));
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
    public List<Menu> findAllByRestaurantId(UUID restaurantId,
                                                            Integer pageSize,
                                                            Integer pageNumber) {
        final Page<Menu> allByRestaurantId =
                menuRepository.findAllByRestaurantId(
                        restaurantId.toString(), pageRequestWithSort(Sort.Direction.ASC,
                                RESTAURANT_ID, pageNumber, pageSize));

        return allByRestaurantId.getContent();
    }

    @Override
    public List<Menu> findAllByNameContaining(String searchTerm,
                                              Integer pageSize,
                                              Integer pageNumber) {

        final Page<Menu> name = menuRepository.findAllByNameContaining(
                searchTerm, pageRequestWithSort(Sort.Direction.ASC, NAME, pageNumber, pageSize));

        return name.getContent();
    }

    @Override
    public List<Menu> findAllByRestaurantIdAndNameContaining(
            UUID restaurantId, String searchTerm, Integer pageSize,
            Integer pageNumber) {
        final Page<Menu> allByRestaurantIdAndNameContaining =
                menuRepository.findAllByRestaurantIdAndNameContaining(
                        restaurantId.toString(), searchTerm, pageRequestWithSort(Sort.Direction.ASC, NAME, pageNumber, pageSize));
        return allByRestaurantIdAndNameContaining.getContent();
    }

}