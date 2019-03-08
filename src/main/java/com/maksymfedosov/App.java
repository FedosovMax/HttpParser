package com.maksymfedosov;

import com.maksymfedosov.entity.Enum.Status;
import com.maksymfedosov.entity.Order;
import com.maksymfedosov.entity.Pet;
import com.maksymfedosov.entity.User;
import com.maksymfedosov.service.PetService;
import com.maksymfedosov.service.StoreService;
import com.maksymfedosov.service.UserService;
import lombok.Data;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Data
public class App {

    PetService petService;
    StoreService storeService;
    UserService userService;

    public App() throws IOException {

        petService = new PetService();
        storeService = new StoreService();
        userService = new UserService();

        Scanner scanner = new Scanner(System.in);
        String currentCommand = null;

        do {
            printMenu();
            currentCommand = scanner.nextLine();

            if (currentCommand.startsWith("add_pet")) {
                addNewPetWithOnlyName();
            } else if (currentCommand.startsWith("update")) {
                String[] commandParts = currentCommand.split(" ");
                int petId = Integer.parseInt(commandParts[1]);
                updatePetById(petId);
            } else if (currentCommand.startsWith("status")) {
                petService.findPetByStatus();
            } else if (currentCommand.startsWith("find")) {
                String[] commandParts = currentCommand.split(" ");
                long petId = Integer.parseInt(commandParts[1]);
                Pet pet = petService.getPetById(petId);
                System.out.println("Your per id is: " + petId);
                System.out.println("It's name is :" + pet.getName());
            } else if (currentCommand.startsWith("updatingById")) {
                String[] commandParts = currentCommand.split(" ");
                long petId = Integer.parseInt(commandParts[1]);
                updatePetNameAndStatusById(petId);
            } else if (currentCommand.startsWith("delete")) {
                String[] commandParts = currentCommand.split(" ");
                long petId = Integer.parseInt(commandParts[1]);
                petService.deletePetById(petId);
            } else if (currentCommand.startsWith("upload")) {
                petService.uploadAnImage();
            } else if (currentCommand.startsWith("get_inv")) {
                storeService.findInventoriesByStatus();
            } else if (currentCommand.startsWith("order")) {
                createOrder();
            } else if (currentCommand.startsWith("find_order")) {
                String[] commandParts = currentCommand.split(" ");
                long petId = Integer.parseInt(commandParts[1]);
                Order theOrder = storeService.findOrderById(petId);
                System.out.println(theOrder);
                System.out.println("\n");
            } else if (currentCommand.startsWith("delete_order")) {
                String[] commandParts = currentCommand.split(" ");
                long petId = Integer.parseInt(commandParts[1]);
                storeService.deleteOrderById(petId);
            } else if (currentCommand.equals("user")) {
                createUser();
            } else if (currentCommand.startsWith("user_array")) {
                createUserWithArray();
            } else if (currentCommand.startsWith("user_list")) {
                createUserWithList();
            } else if (currentCommand.startsWith("login")) {
                loginUser();
            } else if (currentCommand.startsWith("logout")) {
                userService.logoutUser();
            } else if (currentCommand.startsWith("getuser")) {
                String[] commandParts = currentCommand.split(" ");
                long userId = Integer.parseInt(commandParts[1]);
                User user = userService.getUserById(userId);
                System.out.println("Your user id is: " + userId);
                System.out.println("The username is :" + user.getUsername());
            } else if (currentCommand.startsWith("updateuser")) {
                String[] commandParts = currentCommand.split(" ");
                int userId = Integer.parseInt(commandParts[1]);
                updateById(userId);
            } else if (currentCommand.startsWith("deleteuser")) {
                String[] commandParts = currentCommand.split(" ");
                int userId = Integer.parseInt(commandParts[1]);
                userService.deleteById(userId);
            }
        } while (!currentCommand.equals("exit"));
        scanner.close();
    }

    public static void main(String[] args) throws IOException {
        new App();
    }

    private void printMenu() {
        System.out.println("Insert the command and press Enter:\n" +
                "1. Create a new pet (add_pet)\n" +
                "2. Update an existing pet (update <petId>)\n" +
                "3. Get pets by status (status)\n" +
                "4. Find a pet by id (find_pet <petId>)\n" +
                "5. Update pet's id, name and status (updatingById <petId>)\n" +
                "7. Delete a pet by id (delete <petId>)\n" +
                "8. Upload an image for a pet (upload)\n" +
                "\n" +
                "9. Find inventories by a status (get_inv)\n" +
                "10. Place an order for a pet (order)\n" +
                "11. Find order by id (find_order <petId>)\n" +
                "12. Delete order by id (delete_order <orderId>)\n" +
                "\n" +
                "13. Create user (user)\n" +
                "14. Create users with array (user_array)   IT'S BROKEN\n" +
                "15. Create users with list (user_list)\n" +
                "16. Login user into the system (login)\n" +
                "17. Log out from current session (logout)\n" +
                "18. Get user by username (getuser <userId>)\n" +
                "19. Update user (updateuser <userId>)\n" +
                "20. Delete user (deleteuser <userId>)\n" +
                "21. Выход (exit)\n");
    }

    private void addNewPetWithOnlyName() throws IOException {
        Scanner input = new Scanner(System.in);

        System.out.println("Select the name of the new Pet: ");

        String petName = input.next();
        Pet pet = new Pet(petName);
        petService.addPet(pet);
        System.out.println("Your pet with name " + petName + " was created");
        System.out.println("\n");
    }

    private void updatePetById(long petId) throws IOException {
        Pet pet = petService.getPetById(petId);
        petService.addPet(pet);
        System.out.println("Your pet with id " + petId + " was updated");
        System.out.println("\n");
    }

    private void updatePetNameAndStatusById(long petId) throws IOException {
        Pet pet = petService.getPetById(petId);

        //ToDo create cycle for another try to input right information
        //ToDo create a right exception about something like there is an existing id there
        Scanner inputId = new Scanner(System.in);
        System.out.println("Select new id for the Pet: ");

        long newId = inputId.nextLong();
        pet.setId(newId);

        Scanner inputName = new Scanner(System.in);
        System.out.println("Select new name for the Pet: ");

        String newName = inputName.next();

        Scanner inputStatus = new Scanner(System.in);
        System.out.println("Select new status for the Pet: ");

        String newStatus = inputStatus.next();

        petService.updatePetNameAndStatus(pet);
    }

    private void createOrder() throws IOException {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter the order Id: ");

        Long petId = input.nextLong();
        Order order = new Order(petId);
        storeService.createOrder(order);

        System.out.println("Your order was created");
        System.out.println("\n");
    }

    private void createUser() throws IOException {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter the user Id: ");

        String userName = input.next();
        User user = new User(userName);
        userService.createUser(user);

        System.out.println("The user with username: " + user.getUsername() + "was created");
        System.out.println("\n");
    }

    private void createUserWithArray() throws IOException {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter the size of an Array");

        int num = input.nextInt();                  //input array size

        System.out.println("Enter the Element " + num + " of an Array");

        User[] users = new User[num];

            for (int i = 0; i < users.length; i++) {
                System.out.println("Enter new user: ");
                users[i] = new User(input.next());
            }
        userService.createUserFromArray(users);
    }

    private void createUserWithList() throws IOException {
        List<User> users = new ArrayList<>();

        Scanner reader = new Scanner(System.in);
        String username;
        long longInput = -31;
        int userStatus;
        System.out.println("Enter id 0 to exit menu");

        while(longInput != 0){
            User user = new User();

            System.out.println("Enter user id: ");
            longInput = reader.nextLong();
            user.setId(longInput);

            System.out.println("Enter username: ");
            username = reader.next();
            user.setUsername(username);

            System.out.println("Enter userStatus: ");
            userStatus = reader.nextInt();
            user.setUserStatus(userStatus);

            users.add(user);

            System.out.println("The user has been created with id: " + user.getId());

        }
        userService.createUserWithList(users);
    }

    private void loginUser() throws IOException {

        Scanner reader = new Scanner(System.in);

        System.out.println("Enter your login: ");
        String login = reader.next();

        System.out.println("Enter your password: ");
        String password = reader.next();

        userService.loginUser(login, password);
    }

    private void updateById(int userId) throws IOException {
        User user = userService.getUserById(userId);
        userService.updateUser(user);
    }

}
