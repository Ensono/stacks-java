package com.amido.stacks.functional.clients;

import com.amido.stacks.functional.enums.AppStatus;
import com.amido.stacks.functional.enums.WebServiceEndPoints;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class MenuClient {

    final String baseUrl = WebServiceEndPoints.BASE_URL.getUrl();

    public AppStatus currentStatus() {
        String path = baseUrl + WebServiceEndPoints.STATUS.getUrl();
        int statusCode = RestAssured.get(path).statusCode();

        return (statusCode == 200) ? AppStatus.RUNNING : AppStatus.DOWN;
    }

    public Response getMenus() {
        String path = baseUrl + WebServiceEndPoints.MENU.getUrl();
        return RestAssured.get(path);
    }
}
