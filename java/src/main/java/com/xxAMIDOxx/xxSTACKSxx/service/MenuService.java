package com.xxAMIDOxx.xxSTACKSxx.service;

import com.xxAMIDOxx.xxSTACKSxx.model.Menu;

import java.util.List;
import java.util.UUID;

public interface MenuService {

    List<Menu> all(int pageNumber, int pageSize);

    Menu findById(UUID id);
}
