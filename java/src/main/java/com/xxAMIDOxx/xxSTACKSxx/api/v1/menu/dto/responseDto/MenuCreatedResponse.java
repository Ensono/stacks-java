package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.responseDto;

import java.util.Objects;

/**
 * Response Object to map Once menu is created.
 *
 * @author Suresh Krishnan
 * @author ArathyKrishna
 */
public class MenuCreatedResponse {

    private String id;

    public MenuCreatedResponse() {
    }

    public MenuCreatedResponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuCreatedResponse)) return false;
        MenuCreatedResponse that = (MenuCreatedResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MenuCreatedResponse{" + "id=" + id + '}';
    }
}
