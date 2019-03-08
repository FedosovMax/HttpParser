package com.maksymfedosov.service;

import com.maksymfedosov.converter.JsonConverter;
import com.maksymfedosov.entity.Enum.Status;
import com.maksymfedosov.entity.Pet;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class PetService {

    JsonConverter jsonConverter = new JsonConverter();

    private String BASE_URL = "https://petstore.swagger.io/v2";
    private String FIND_BY_URL = BASE_URL + "/pet/";

    public void addPet(Pet pet) throws IOException {

        String jsonPet = jsonConverter.convertObjectToJson(pet);

        String REQUEST_URL = BASE_URL + "/pet";

        Document document = Jsoup
                .connect(REQUEST_URL)
                .header("Content-Type", "application/json")
                .ignoreContentType(true)
                .requestBody(jsonPet)
                .method(Connection.Method.POST)
                .execute()
                .parse();

        System.out.println(document.text());
    }

    public Pet getPetById(long id) throws IOException {
        String jsonString =  Jsoup.connect(FIND_BY_URL + id).ignoreContentType(true).get().text();
        return (Pet)jsonConverter.convertJsonToObj(jsonString);
    }

    public void updatePet(Pet pet) throws IOException {
        String jsonPet = jsonConverter.convertObjectToJson(pet);

        String PUT_URL = BASE_URL + "/pet";

        Document document = Jsoup.connect(PUT_URL).header("Content-Type", "application/json").ignoreContentType(true)
                .requestBody(jsonPet).method(Connection.Method.PUT).execute().parse();
    }

    public List<Pet> findPetByStatus() throws IOException {
        String status = choosePetStatus();
        //ToDo make method receive array of String statuses
        String FIND_BY_STATUS = "https://petstore.swagger.io/v2/pet/findByStatus?status=" + status;
        String jsonString = Jsoup.connect(FIND_BY_STATUS).ignoreContentType(true).get().text();
        return jsonConverter.convertJsonToPetList(jsonString);
    }

    public String choosePetStatus() {

        Scanner input = new Scanner(System.in);

        Status status = null;
        String numberObj;

        System.out.println("Choose status:" +
                "\n 1. available (available)" +
                "\n 2. pending (pending)" +
                "\n 3. sold (sold)");

        System.out.print("\nEnter number of the status: ");
        numberObj = input.next();

        switch (numberObj) {
            case "1":
                status = Status.fromText("available");
                break;
            case "2":
                status = Status.fromText("pending");
                break;
            case "3":
                status = Status.fromText("sold");
                break;
            default:
                System.out.println("This command is wrong");
                choosePetStatus();
                break;
        }
        return String.valueOf(status);
    }

    public void updatePetNameAndStatus(Pet pet) throws IOException {
        String jsonPet = jsonConverter.convertObjectToJson(pet);

        String PUT_URL = FIND_BY_URL + pet.getId();

        Document document = Jsoup.connect(PUT_URL).header("Content-Type", "application/json").ignoreContentType(true)
                .requestBody(jsonPet).method(Connection.Method.POST).execute().parse();

        System.out.println("Your pet now has id: " + pet.getId() + " the new name is: " + pet.getName() + "new status is: " + pet.getStatus());

    }

    public void deletePetById(long petId) throws IOException {

        String DELETE_URL = FIND_BY_URL + petId;

        Jsoup.connect(DELETE_URL).header("api_key", "special-key").ignoreContentType(true).method(Connection.Method.DELETE).execute();

        System.out.println("Your pet with id: " + petId + " was successfully deleted \n");
    }

    public void uploadAnImage() throws IOException {

        File file = new File("H:\\3131.jpg");
//        String path = file.getAbsolutePath();
        FileInputStream fileStream = new FileInputStream(file);
        System.out.println(file.getAbsolutePath());

        Scanner inputId = new Scanner(System.in);
        System.out.println("Select the id of the Pet you want to change: ");

        long petId = inputId.nextLong();

        Pet pet = getPetById(petId);

        Scanner inputMessage = new Scanner(System.in);
        System.out.println("Write an additonal message if exist: ");

        String newMessage = inputMessage.next();
//        String photoFilePath = getFilePathFromInputLine(pet);
//
        String jsonPet = jsonConverter.convertObjectToJson(pet);

        String UPLOAD_URL = FIND_BY_URL + petId + "/uploadImage";

        System.out.println("Attempting to upload file...");
        Document document = Jsoup.connect(UPLOAD_URL)
                .data("additionalMetadata", newMessage)
                .data("file", file.getName(), fileStream)
                .ignoreContentType(true)
                .method(Connection.Method.POST)
                .execute()
                .parse();

        System.out.println("Your pet is id: " + pet.getId() + ". The new message is: " + newMessage +
                ". The Photo url is: " + file.getAbsolutePath() + "\n");

    }

    //ToDo Create some comfortable form to look for a file from this method
     public String getFilePathFromInputLine(Pet pet) {

        String photoFilePath = null;

        try {
            System.out.println("Select a path for the image: \n");

            Scanner input = new Scanner(System.in);
            File file = new File(input.nextLine());
            input = new Scanner(file);

        while (input.hasNextLine()) {
            photoFilePath = input.nextLine();
            pet.getProtoUrls().add(photoFilePath);
        }
        input.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return photoFilePath;
    }


}
