package com.amido.stacks.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import io.restassured.response.Response;
import org.junit.jupiter.api.Tag;
import org.junit.Test;
import com.amido.stacks.functional.models.MenusResponse;
import com.amido.stacks.functional.clients.MenuClient;

@Tag("Functional")
public class GetMenusTest {
    @Test
    public void getMenusTest() {
        //When I request all menus
        MenuClient menuClient = new MenuClient();
        Response response = menuClient.getMenus();

        //Then I get a list of all menus
        assertEquals(200, response.getStatusCode());

        MenusResponse menusResponse = response.as(MenusResponse.class);
        assertFalse(menusResponse.results.isEmpty());
    }
}