package com.amido.stacks.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;

import com.amido.stacks.functional.enums.WebServiceEndPoints;
import com.amido.stacks.functional.clients.MenuClient;
import com.amido.stacks.functional.enums.AppStatus;
import org.junit.jupiter.api.Tag;

@Tag("Functional")
@Tag("Smoke")
public class AppStatusTest {
    @Test
    //todo: should be tagged Smoke and Functional
    public void checkApplicationStatus() {
        //Given the application is running
        MenuClient applicationStatus = new MenuClient();
        assertEquals(AppStatus.RUNNING, applicationStatus.currentStatus());

        //When I check the application status
        Response response = RestAssured.get(WebServiceEndPoints.BASE_URL.getUrl() + WebServiceEndPoints.STATUS.getUrl());

        //Then the API should return "Healthy" status
        assertTrue(response.getBody().asString().contains("UP"));
    }
}