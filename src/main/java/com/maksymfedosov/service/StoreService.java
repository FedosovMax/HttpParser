package com.maksymfedosov.service;

import com.maksymfedosov.converter.JsonConverter;
import com.maksymfedosov.entity.Order;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;


public class StoreService {

    JsonConverter jsonConverter = new JsonConverter();
    PetService petService = new PetService();

    private String BASE_URL = "https://petstore.swagger.io/v2";
    private String FIND_BY_URL = BASE_URL + "/store/";

    public Map<String, Long> findInventoriesByStatus() throws IOException {

        String FIND_BY_STATUS = "https://petstore.swagger.io/v2/store/inventory";

        String jsonString = Jsoup.connect(FIND_BY_STATUS).ignoreContentType(true).get().text();

        Map<String, Long> values = jsonConverter.convertJsonToInventoryMap(jsonString);

        System.out.println(Collections.singletonList(values));

        return values;
    }


    public void createOrder(Order order) throws IOException {
        String jsonOrder = jsonConverter.convertObjectToJson(order);

        String REQUEST_URL = BASE_URL + "/pet";

        Document document = Jsoup
                .connect(REQUEST_URL)
                .header("Content-Type", "application/json")
                .ignoreContentType(true)
                .requestBody(jsonOrder)
                .method(Connection.Method.POST)
                .execute()
                .parse();

        System.out.println(document.text());

    }

    public Order findOrderById(long petId) throws IOException {
        String jsonString =  Jsoup.connect(FIND_BY_URL + "order" + petId).ignoreContentType(true).get().text();
        return (Order)jsonConverter.convertJsonToObj(jsonString);
    }

    public void deleteOrderById(long orderId) throws IOException {

        String DELETE_URL = FIND_BY_URL + "order" + orderId;

        Jsoup.connect(DELETE_URL).ignoreContentType(true).method(Connection.Method.DELETE).execute();

        System.out.println("Your pet with id: " +orderId + " was successfully deleted \n");
    }


}
