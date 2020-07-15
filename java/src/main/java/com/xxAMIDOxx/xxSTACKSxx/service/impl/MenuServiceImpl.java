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

import static com.xxAMIDOxx.xxSTACKSxx.utils.MenuResultUtils.pageRequestWithSort;

@Service
public class MenuServiceImpl implements MenuService {

    private static final String NAME = "name";
    private static Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);

    private MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public Optional<Menu> findById(UUID id) {
        return menuRepository.findById(id.toString());
    }

    public List<Menu> findAll(int pageNumber, int pageSize) {

        Page<Menu> page = menuRepository.findAll(
                pageRequestWithSort(Sort.Direction.ASC, NAME, 0, pageSize));

        int currentPage = 0;

        // This is specific and needed due to the way in which CosmosDB handles pagination
        // using a continuationToken and a limitation in the Swagger Specification.
        // See https://github.com/Azure/azure-sdk-for-java/issues/12726
        while (currentPage < pageNumber && page.hasNext()) {
            currentPage++;
            Pageable nextPageable = page.nextPageable();
            page = menuRepository.findAll(nextPageable);
        }

        return page.getContent();
    }

    @Override
    public List<Menu> findAllByRestaurantId(UUID restaurantId,
                                            Integer pageSize,
                                            Integer pageNumber) {

        return menuRepository.findAllByRestaurantId(
                restaurantId.toString(),
                pageRequestWithSort(Sort.Direction.ASC, NAME, pageNumber, pageSize))
                .getContent();
    }

    @Override
    public List<Menu> findAllByNameContaining(String searchTerm,
                                              Integer pageSize,
                                              Integer pageNumber) {

        return menuRepository.findAllByNameContaining(
                searchTerm,
                pageRequestWithSort(Sort.Direction.ASC, NAME, pageNumber, pageSize))
                .getContent();
    }

    @Override
    public List<Menu> findAllByRestaurantIdAndNameContaining(UUID restaurantId,
                                                             String searchTerm,
                                                             Integer pageSize,
                                                             Integer pageNumber) {

        return menuRepository.findAllByRestaurantIdAndNameContaining(
                restaurantId.toString(), searchTerm,
                pageRequestWithSort(Sort.Direction.ASC, NAME, pageNumber, pageSize))
                .getContent();
    }
}