package com.maksymfedosov.service;

import com.maksymfedosov.converter.JsonConverter;
import com.maksymfedosov.entity.User;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UserService {

    JsonConverter jsonConverter = new JsonConverter();
    PetService petService = new PetService();

    private String BASE_URL = "https://petstore.swagger.io/v2";
    private String CREATE_URL = BASE_URL + "/user/";
    private String FIND_URL = BASE_URL + "/user/";

    public void createUser(User user) throws IOException {

        String jsonOrder = jsonConverter.convertObjectToJson(user);

        String REQUEST_URL = CREATE_URL;

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

    public void createUserFromArray(User[] users) throws IOException {
        String jsonUsers = jsonConverter.convertObjectToJson(users);    //convert array to json String
        String REQUEST_URL = CREATE_URL + "createWithArray";        //request url for creating users

        System.out.println(jsonUsers);

        Document document = Jsoup
                .connect(REQUEST_URL)
                .header("Content-Type", "application/json")
                .ignoreContentType(true)
                .requestBody(jsonUsers)
                .method(Connection.Method.POST)
                .execute()
                .parse();

        System.out.println(document.text());
    }


    public void createUserWithList(List<User> users) throws IOException {
        String jsonUsers = jsonConverter.convertObjectToJson(users);
        String REQUEST_URL = CREATE_URL + "createWithList";

        System.out.println(jsonUsers);

        Document document = Jsoup
                .connect(REQUEST_URL)
                .header("Content-Type", "application/json")
                .ignoreContentType(true)
                .requestBody(jsonUsers)
                .method(Connection.Method.POST)
                .execute()
                .parse();

        System.out.println(document.text());


    }

    public void loginUser(String login, String password) throws IOException {

        String LOGIN_URL = "https://petstore.swagger.io/v2/user/login?login=" + login + "&password =" + password;

        String jsonString = Jsoup.connect(LOGIN_URL).ignoreContentType(true).get().text();

        System.out.println("you successfully loged with login: " + login + " and password " + password + ".");
        System.out.println(jsonString);
        System.out.println("\n");
    }

    public void logoutUser() throws IOException {

        String LOGOUT = "https://petstore.swagger.io/v2/user/logout";

        String jsonString = Jsoup.connect(LOGOUT).ignoreContentType(true).get().text();

        System.out.println("you successfully logged out");
        System.out.println(jsonString);
        System.out.println("\n");
    }

    public User getUserById(long userId) throws IOException {

        String jsonString =  Jsoup.connect(FIND_URL + userId).ignoreContentType(true).get().text();
        return (User)jsonConverter.convertJsonToObj(jsonString);

    }

    public void updateUser(User user) throws IOException {
        String jsonUser = jsonConverter.convertObjectToJson(user);
        String PUT_URL = FIND_URL + user.getId();

        Document document = Jsoup.connect(PUT_URL).header("Content-Type", "application/json").ignoreContentType(true)
                .requestBody(jsonUser).method(Connection.Method.POST).execute().parse();

        System.out.println("Your user now has id: " + user.getId() + " the new name is: " + user.getUsername());
        System.out.println(document);
    }

    public void deleteById(int userId) throws IOException {
        String DELETE_URL = FIND_URL + userId;

        Jsoup.connect(DELETE_URL).header("api_key", "special-key").ignoreContentType(true).method(Connection.Method.DELETE).execute();

        System.out.println("Your user with id: " + userId + " was successfully deleted \n");
    }
}
