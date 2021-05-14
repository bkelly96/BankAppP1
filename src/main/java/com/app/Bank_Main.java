package com.app;

import com.app.dao.BankAccountDao;
import com.app.dao.TransactionsDAO;
import com.app.dao.impl.BankAccountDAOImpl;
import com.app.dao.impl.TransactionsDAOImpl;
import com.app.dao.impl.UserDAOImpl;
import com.app.model.BankAccount;
import com.app.model.Transaction;
import com.app.model.User;
import io.javalin.Javalin;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.io.*;

import org.json.JSONObject;

/**
 * @author Brian Kelly
 * @since 5/13/2021
 * @version 2.0
 */

public class Bank_Main {

    private static Logger log = Logger.getLogger(Bank_Main.class);
    static UserDAOImpl userDAO = new UserDAOImpl();
    static BankAccountDao bankAccountDao = new BankAccountDAOImpl();
    static TransactionsDAO transactionsDAO = new TransactionsDAOImpl();


    public static void main(String[] args) {

        Javalin app=Javalin.create(config->{
            config.addStaticFiles("/public");
            config.enableCorsForAllOrigins();
        }).start(9000);

        app.get("/",ctx-> {
            FileReader fr = new FileReader("src/main/resources/public/B_Sign_Up_or_Sign_In_P1.html");
            int i;

            String file = "";

            while ((i=fr.read()) != -1)
                file += ((char) i);

            ctx.html(file);


        });

        // Sign up / Login

        app.post("/signup", ctx -> {

            JSONObject json = new JSONObject(ctx.body());

            String username = json.getString("username");
            String password = json.getString("password");
            String firstname = json.getString("firstname");
            String lastname = json.getString("lastname");
            String userlevel = "user";

            User user = new User(username, password, firstname, lastname, userlevel);

            try {
                user = userDAO.createUser(user);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            if (user == null){
                ctx.status(401);
            }
            else{
                ctx.cookieStore("user",user);
                ctx.status(200);
            }

            System.out.println(ctx.body());
        });

        app.post("/login", ctx -> {

            JSONObject json = new JSONObject(ctx.body());

            String username = json.getString("username");
            String password = json.getString("password");

            User user = new User(username, password);

            try {
                user = userDAO.loginUsername(user);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            if (user == null){
               ctx.status(401);
            }
            else{
                ctx.cookieStore("user",user.getUserid());
                if (user.getUserlevel().equals("employee")){
                    ctx.status(200);
                }
                else{
                    ctx.status(201);
                }
            }

            System.out.println(ctx.body());
        });

        // Customer Page
        //Withdraw and Depositing
        app.patch("/userTransactions", ctx -> {

            JSONObject json = new JSONObject(ctx.body());

            double amount = json.getDouble("amount");
            int bankaccountid = json.getInt("bankaccountid");

            BankAccount bankAccount = bankAccountDao.viewBankAccount(bankaccountid);
            if (bankAccount==null){
                ctx.status(406).result("bank account not found :(");
                return;
            }

            //checking to make sure there is enough money for a withdrawlal transaction
            if(bankAccount.getAccountBalance().doubleValue()+ amount < 0){

                ctx.status(406).result("no no no isss too much you ask for");
                return;

            }

            if(bankAccountDao.adjustBankAccount(amount, bankAccount)){
                log.info("Account updated successfully");
                Calendar calendar = Calendar.getInstance();
                Date now = calendar.getTime();
                Transaction transaction = new Transaction();
                transaction.setTransactiontime(new Timestamp(now.getTime()));
                transaction.setValue(new BigDecimal(amount));
                transaction.setBank_account_source_id(bankAccount.getBankaccountid());
                transaction.setBank_account_destination_id(bankAccount.getBankaccountid());
                transaction.setTransaction_type("deposit");

                transactionsDAO.addTransaction(transaction);
                ctx.status(200);
            }else{
                ctx.status(500);
            }



        });
        
        //Apply For Account

        app.post("/applyForAccount", ctx -> {
            
            JSONObject json = new JSONObject(ctx.body());
            
            double accountBalance = json.getDouble("accountBalance");
            int userId = ctx.cookieStore("user");

            if(accountBalance < 0){

                ctx.status(406).result("Please enter an amount greater than 0");
                return;
            }
            
            BankAccount bankAccount = new BankAccount();
            bankAccount.setAccountBalance(new BigDecimal(accountBalance));
            bankAccount.setUserid(userId);

            bankAccount = bankAccountDao.createBankAccount(bankAccount);
            if(bankAccount==null){

                ctx.status(406).result("Failed to Create account!");
                return;

            }

           ctx.status(200);
        });
     // Employee Page

        app.patch("/updateBankAccount",ctx ->{

            JSONObject json = new JSONObject(ctx.body());

            String status = json.getString("status");
            //User user = ctx.cookieStore("user").getClass(User.class);
           // System.out.println(ctx.cookieStore("user").toString());
            int bankAccountId = json.getInt("bankaccountid");
            int userid = json.getInt("userid");
            BigDecimal amount = json.getBigDecimal("amount");

            BankAccount bankAccount = new BankAccount(bankAccountId, userid, amount, status, 1);
            bankAccountDao.updateBankAccount(bankAccount);
        });


        app.get("/transactions", ctx -> {

            List<Transaction> transactions = transactionsDAO.getTransactions();
               ctx.json(transactions);

        });

        //Update Status
        app.patch("/updateStatus",ctx -> {

            JSONObject json = new JSONObject(ctx.body());

            int bankAccountId = json.getInt("bankAccountId");
            String statuses = json.getString("statuses");

            int userId = ctx.cookieStore("user");

            BankAccount bankAccount = new BankAccount();
            bankAccount.setReviewedBy(userId);
            bankAccount.setBankaccountid(bankAccountId);
            bankAccount.setStatuses(statuses);

            bankAccount = bankAccountDao.updateBankAccount(bankAccount);
            if(bankAccount==null){

                ctx.status(500).result("Failed to change status");
                return;

            }
            ctx.status(200);

        });




                /*
        app.post("/",ctx-> {
            ctx.result("Hello and welcome to JAVALIN with Post Method");
        });
        app.put("/",ctx-> {
            ctx.result("Hello and welcome to JAVALIN with Put Method");
        });
        app.delete("/",ctx-> {
            ctx.result("Hello and welcome to JAVALIN with Delete Method");
        });

        System.out.println("Check out http://localhost:9000/");


        Scanner scanner = new Scanner(System.in);
        User user = new User();

        log.info("Welcome to THE BANK! Did you want to create an account or log in?");
        log.info("Please select an option:");
        log.info("\n1)Log in\n 2)Sign up \n 3 Leave");

        //this code will keep the user in the loop until they make a valid choice.
        int choice = 0;
        boolean valid = false;
        while (!valid) {
            log.info("Choice: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= 3) {
                    valid = true;
                } else {
                    log.info("Please enter a number between 1 and 3");
                }
            } catch (NumberFormatException nfe) {
                log.info("Please enter a number");
            }
            log.info("-------------------");
        }

        switch (choice) {
            case 1://Login
                user = loginUsername(user, scanner);
                if (user == null) {
                    log.info("Log in failed");
                    break;
                }
                //checks for user inside of user object
                if (user.getUserlevel().equals("user")) {
                    userMenu(user, scanner);
                }
                //checks for employee level inside of user object
                if (user.getUserlevel().equals("employee")){
                   employeeMenu(user, scanner);
                }
                break;
            case 2://Signup
                createNewUser(user, scanner);
                break;
            case 3://Leave
                break;

        }
        scanner.close();

         */
    }

    //These are the methods that are a result of the initial choice
    //-------------------------------------------------------'


    //this is the result of choice 1 and which will have an existing user log in
    public static User loginUsername(User user, Scanner scanner) {

        //Asks user for Existing Username
        log.info("Enter Username");
        //user inputs Username
        String userInput = scanner.nextLine();

        //checks username against User class
        if (!user.setUsername(userInput)) {
            log.info("Username is invalid");
            return null;
        }

        // Ask for password
        log.info("Enter Password");
        //user inputs Username
        userInput = scanner.nextLine();

        //checks username against User class
        if (!user.setPassword(userInput)) {
            log.info("Password is invalid");
            return null;
        }

        try {
            user = userDAO.loginUsername(user);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if (user == null) {
            return null;
        }

        return user;

    }

    //This is the result of a user logging in

    public static void userMenu(User user, Scanner scanner) {

        System.out.println("Welcome to your Bank menu");
        log.info("\n1)Apply for Account\n 2)View Account \n 3 Deposit\n 4) Withdraw\n 5) Transfer \n 0) Exit");
        System.out.println("Please enter a number:");



        //this code will keep the user in the loop until they make a valid choice.
        int choice = 0;
        boolean valid = false;
        while (!valid) {
            System.out.println("Welcome to your Bank menu");
            log.info("\n1)Apply for Account\n 2)View Account \n 3 Deposit\n 4) Withdraw\n 5) Transfer \n 0) Exit");
            System.out.println("Please enter a number:");;
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= 3) {
                    valid = true;
                } else {
                    log.info("Please enter a number between 0 and 5");
                }
            } catch (NumberFormatException nfe) {
                log.info("Please enter a number");
            }
            log.info("-------------------");
        }

        switch (choice) {
            case 1://Apply for accounts
                applyForAccount(user, scanner);
                break;
            case 2://View Account
                viewAccount(user, scanner);
                break;
            case 3://Deposit
                depositAmount(user, scanner);
                break;
            case 4://Withdraw
                withdrawAmount(user, scanner);
                break;
                /*
            case 5://Transfer
                transferInformation(user, scanner);
                break;
            case 6://Leave
                break;

*/
        }
    }

    //this is the result of an employee logging in
    public static void employeeMenu(User user, Scanner scanner){
        System.out.println("Welcome to the Employee Interface");
        log.info("\n1) List all Active Accounts\n 2)View Accounts \n 3 List Pending Accounts\n 4) List of All Transactions \n 0) Exit");
        System.out.println("Please enter a number:");


        //this code will keep the user in the loop until they make a valid choice.
        int choice = 0;
        boolean valid = false;
        while (!valid) {
            System.out.println("Welcome to the Employee Interface");
            log.info("\n1) List all Active Accounts\n 2)View Accounts \n 3 List Pending Accounts\n 4) List of All Transactions \n 0) Exit");
            System.out.println("Please enter a number:");;
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= 3) {
                    valid = true;
                } else {
                    log.info("Please enter a number between 0 and 4");
                }
            } catch (NumberFormatException nfe) {
                log.info("Please enter a number");
            }
            log.info("-------------------");
        }

        switch (choice) {
            case 1://List of All Transactions
                try {
                    transactionsDAO.getTransactions();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (BusinessException e) {
                    e.printStackTrace();
                }
                break;
/*            case 2://View Accounts
                viewAccount(user, scanner);
                break;
            case 3://List all Active Accounts
                break;
            case 4://List Pending Accounts
                transactionsDAO.listTransaction();
                break;
            case 0://Leave
                break;

        }*/
        }
    }

    private static void applyForAccount(User user, Scanner scanner) {
        System.out.println("What do you want the starting balance to be?");
        double startingBalance = scanner.nextDouble();

        BankAccount bankAccount = new BankAccount();

        bankAccount.setUserid(user.getUserid());

        if (bankAccount.setAccountBalance(new BigDecimal(startingBalance)) == false)
            log.info("This is amount is invalid");

        try {
            bankAccount = bankAccountDao.createBankAccount(bankAccount);
                System.out.println("Amount is valid");
                System.out.println(bankAccount);

        }catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (BusinessException e) {
            e.printStackTrace();
        }


    }

    //method for viewing account
    private static void viewAccount(User user, Scanner scanner) {
        System.out.println("Please enter the bankaccountid of the account you would like to view");
        int choice = scanner.nextInt();

        try {
            BankAccount bankAccount = bankAccountDao.viewBankAccount(choice);
            if (bankAccount == null){
                return;
            }
            log.info(bankAccount.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (BusinessException e) {
            e.printStackTrace();
        }


    }

    //method for depositing ammount
    private static void depositAmount(User user, Scanner scanner) {
        System.out.println("Which account do you want to deposit too?");
        int choice = scanner.nextInt();

        try {
            BankAccount bankaccount = bankAccountDao.viewBankAccount(choice);
            if (bankaccount==null){
                return;
            }
            System.out.println("How much would you like to deposit");
            double amount = scanner.nextDouble();
            if (amount < 0)
                log.info("Invalid amount");
            else if (bankAccountDao.adjustBankAccount(amount, bankaccount)==true) {
                log.info("Account updated successfully");
                Calendar calendar = Calendar.getInstance();
                java.util.Date now = calendar.getTime();
                Transaction transaction = new Transaction();
                transaction.setTransactiontime(new Timestamp(now.getTime()));
                transaction.setValue(new BigDecimal(amount));
                transaction.setBank_account_source_id(bankaccount.getBankaccountid());
                transaction.setBank_account_destination_id(bankaccount.getBankaccountid());
                transaction.setTransaction_type("deposit");

                transactionsDAO.addTransaction(transaction);
            }
            else{
                log.info("Account did not update");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (BusinessException e) {
            e.printStackTrace();
        }

    }

    //method for withdrawing amount, make front end change input to negative
    private static void withdrawAmount(User user, Scanner scanner) {
        System.out.println("Which account do you want to withdraw from?");
        int choice = scanner.nextInt();

        try {
            BankAccount bankaccount = bankAccountDao.viewBankAccount(choice);
            if (bankaccount==null){
                return;
            }
            System.out.println("How much would you like to withdraw");
            double amount = scanner.nextDouble();
            if (amount > 0)
                log.info("Invalid amount");
            else if (bankAccountDao.adjustBankAccount(amount, bankaccount)==true) {
                log.info("Account updated successfully");
                Calendar calendar = Calendar.getInstance();
                java.util.Date now = calendar.getTime();
                Transaction transaction = new Transaction();
                transaction.setTransactiontime(new Timestamp(now.getTime()));
                transaction.setValue(new BigDecimal(amount));
                transaction.setBank_account_source_id(bankaccount.getBankaccountid());
                transaction.setBank_account_destination_id(bankaccount.getBankaccountid());
                transaction.setTransaction_type("withdrawal");

                transactionsDAO.addTransaction(transaction);
            }
            else{
                log.info("Account did not update");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (BusinessException e) {
            e.printStackTrace();
        }

    }

    //making a to account and a from account, pass data to the Impl
    public static void transferInformation(User user, Scanner scanner){

    }

    //this is the result of choice 2 and will create a new user

    public static void createNewUser(User user, Scanner scanner){

        //Asks user for Username
        log.info("Enter Username");

        //This will create a temporary string that is sent to the User class to ensure that it meets
        //our requirements for user input; Specifically Username
        boolean userNameOK = false;
        do {

            log.info("Please create your username (only enter alphabetical characters and numerical values, between 2 - 20)");
            String temp = scanner.nextLine();

            //Stores the temporary String into the contact
            userNameOK = user.setUsername(temp);

            //This will print out additional instructions if the user fails to enter qualifying information
            if(!userNameOK) {

                System.out.println("\nPlease only use alphabetical characters and numerical values!\n");
            }

        } while(!userNameOK);

        //Asks user for Password
        log.info("Enter password");

        //This will create a temporary string that is sent to the User class to ensure that it meets
        //our requirements for user input; Specifically Password
        boolean passwordOK = false;
        do {

            log.info("Please create your password (enter any value, between 8 - 128 characters)");
            String temp = scanner.nextLine();

            //Stores the temporary String into the contact
            passwordOK = user.setPassword(temp);

            //This will print out additional instructions if the user fails to enter qualifying information
            if(!passwordOK) {

                System.out.println("\nPlease only use alphabetical characters, numbers, hyphens, and apostrophes!\n");
            }

        } while(!passwordOK);

        //Asks user for first name
        log.info("Enter first name");

        //This will create a temporary string that is sent to the User class to ensure that it meets
        //our requirements for user input; Specifically firstname
        boolean firstNameOK = false;
        do {

            log.info("Please create your First Name (only enter alphabetical characters, between 2 - 20)");
            String temp = scanner.nextLine();

            //Stores the temporary String into the contact
            firstNameOK = user.setFirstname(temp);

            //This will print out additional instructions if the user fails to enter qualifying information
            if(!firstNameOK) {

                System.out.println("\nPlease only use alphabetical characters!\n");
            }

        } while(!firstNameOK);

        //Asks user for last name
        log.info("Enter last name");

        //This will create a temporary string that is sent to the User class to ensure that it meets
        //our requirements for user input; Specifically lastname
        boolean lastNameOK = false;
        do {

            log.info("Please create your username (only enter alphabetical characters, between 2 - 20)");
            String temp = scanner.nextLine();

            //Stores the temporary String into the contact
            lastNameOK = user.setLastname(temp);

            //This will print out additional instructions if the user fails to enter qualifying information
            if(!lastNameOK) {

                System.out.println("\nPlease only use alphabetical characters!\n");
            }

        } while(!lastNameOK);

        //this sets the userlevel to user. When a new account is created their default access level will be user.
        user.setUserlevel("user");


        //instatiatng a new object and implementing UserDao
        //UserDAO userDAO=new UserDAOImpl();

        try {
            //passing the user
            user=userDAO.createUser(user);
            if(user.getUserid()!=0){
                System.out.println("User registered successfully");
                System.out.println(user);
            }
        } catch (SQLException | BusinessException e) {
            System.out.println("Internal error occurred...please reach out");;
        }

    }

}
