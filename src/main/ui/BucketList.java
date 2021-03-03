package ui;

import Exceptions.AlreadyExists;
import model.Categories;
import model.Completed;
import model.Inventory;
import model.ToDoItem;

import java.util.Scanner;

// BucketList application
public class BucketList {
    private Inventory inv;
    private Categories comp;
    private Categories pend;
    private Scanner input;

    // EFFECTS: runs the Bucketlist application
    public BucketList() {
        runBucket();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runBucket() {
        boolean keepGoing = true;
        String command = null;

        init();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("a")) {
            addToDo();
        } else if (command.equals("d")) {
            deleteToDo();
        } else if (command.equals("m")) {
            completeItem();
        } else if (command.equals("l")) {
            showCurrentList();
        } else if (command.equals("c")) {
            showCompleteList();
        } else  if (command.equals("s")) {
            showAllCategories();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes Categories and Inventories
    private void init() {
        comp = new Categories("Completed");
        pend = new Categories("Current");
        inv = new Inventory();
        input = new Scanner(System.in);
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> Add ToDo");
        System.out.println("\td -> Delete ToDo");
        System.out.println("\tm -> Mark ToDo as complete");
        System.out.println("\tl -> Show ToDo's in Category");
        System.out.println("\tc -> Show Completed ToDo");
        System.out.println("\ts -> Show All Categories");
        System.out.println("\tq -> quit");
    }

    // CONSTRAINTS: name of ToDoItem can only take one string
    // MODIFIES: this
    // EFFECTS: adds a ToDoItem into list

    private void addToDo() {
        System.out.println("Enter name of ToDo : ");
        String name = input.next();
        ToDoItem td = new ToDoItem(name);
        System.out.println("Set a date : ");
        String date = input.next();
        td.setDate(date);
        System.out.println("Enter name of Category : ");
        String catName = input.next();
        Categories c = new Categories(catName);
        try {
            inv.addCategory(c);
        } catch (AlreadyExists alreadyExists) {
            alreadyExists.printStackTrace();
            System.out.println("Category Already Exists");
        }
        c.addToDoItemInCategory(td);
        System.out.println("ToDo added successfully");
    }

    // MODIFIES: this
    // EFFECTS: delete a ToDoItem from Category
    private void deleteToDo() {
        System.out.print("Enter name of Category ToDo is in : ");
        String name = input.next();
        Categories cat = inv.searchForCategory(name);
        if (cat == null) {
            System.out.println("Category not found...");
        } else {
            System.out.println("Enter name of ToDo");
            String toDoName = input.next();
            if (cat.searchForToDo(toDoName) == null) {
                System.out.println("Item Not Found...");
            } else {
                cat.deleteToDo(toDoName);
                System.out.println("To do Deleted Successfully!");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: shows all Current ToDoItems in Category
    private void showCurrentList() {
        System.out.println("Enter name of Category : ");
        String name = input.next();
        Categories cat = inv.searchForCategory(name);
        if (cat == null) {
            System.out.println("Category not Found...");
        } else {
            if (cat.getList().isEmpty()) {
                System.out.println("No more ToDo's!");
            } else {
                cat.printCategory(cat.getList());
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: show all Completed ToDoItems in list
    private void showCompleteList() {
        if (!comp.getList().isEmpty()) {
            comp.printCategory(comp.getList());
        } else {
            System.out.println("No Completed ToDo's!");
        }
    }

    //MODIFIES: this
    //EFFECTS: mark Item as completed
    private void completeItem() {
        System.out.println("Enter Category of Item : ");
        String name = input.next();
        Categories cat = inv.searchForCategory(name);
        if (cat == null) {
            System.out.println("Category Not Found...");
        } else {
            System.out.println("Enter Name of ToDo : ");
            String toDoName = input.next();
            if (cat.searchForToDo(toDoName) == null) {
                System.out.println("ToDo Item Not found");
            } else {
                cat.searchForToDo(toDoName).statusCompleted();
                comp.addToDoItemInCategory(cat.searchForToDo(toDoName));
                cat.deleteToDo(toDoName);
                System.out.println("Item marked as Completed!");
            }
        }
    }

    private void showAllCategories() {
        if (!inv.getCategories().isEmpty()) {
            inv.printAllCategory(inv.getCategories());
        } else {
            System.out.println("No Categories To Display");
        }
    }
}
