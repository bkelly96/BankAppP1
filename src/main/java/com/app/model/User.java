package com.app.model;
//Classes reflecting real world object go in model

import com.app.dbutil.PasswordHashing;

public class User {

    private int userid;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String userlevel;

    public User(){
    }

    public User(String username, String password) {
        this.username = username;
        this.password = PasswordHashing.hash(password);
    }

    //Constructor for user information. Leaving out the userid because it is automatically generated when data is inserted.
    public User(String username, String password, String firstname, String lastname, String userlevel) {
        this.username = username;
        this.password = PasswordHashing.hash(password);
        this.firstname = firstname;
        this.lastname = lastname;
        this.userlevel = userlevel;
    }

    public User(int userid, String username, String password, String firstname, String lastname, String userlevel) {
        this.userid = userid;
        this.username = username;
        this.password = PasswordHashing.hash(password);
        this.firstname = firstname;
        this.lastname = lastname;
        this.userlevel = userlevel;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public boolean setUsername(String username) {

        if (username == null)
            return false;

        // Removes leading and trailing whitespace characters
        username = username.trim();

        // Replaces extra spaces
        username = username.replaceAll(" +", " ");

        if (username.length() < 2 || username.length() > 20)
            return false;

        //only allows for the user to enter upper and lower case alphabetical values and numerical values
        if (!username.matches("[A-Za-z0-9]+"))
            return false;

        this.username = username;
        return true;
    }

    public String getPassword() {
        return password;
    }

    public boolean setPassword(String password) {

        if (password == null)
            return false;

        // Removes leading and trailing whitespace characters
        password = password.trim();

        // Replaces extra spaces
        password = password.replaceAll(" +", " ");

        if (password.length() < 8 || password.length() > 128)
            return false;

        //only allows for the user to enter upper and lower case alphabetical values, numbers, hyphens, apostrophes
        if (!password.matches("[/^(?=(?:[^a-z]*[a-z]){2})(?=(?:[^0-9]*[0-9]){2})(?=.*[!-\\/:-@\\[-`{-~]).{8,}$/i]+"))
            return false;

        //hashing password before storing it
        this.password = PasswordHashing.hash(password);
        return true;
    }

    public String getFirstname() { return firstname; }

    public boolean setFirstname(String firstname) {

        if (firstname == null)
            return false;

        // Removes leading and trailing whitespace characters
        firstname = firstname.trim();

        // Replaces extra spaces
        firstname = firstname.replaceAll(" +", " ");

        if (firstname.length() < 2 || firstname.length() > 20)
            return false;

        //only allows for the user to enter upper and lower case alphabetical values
        if (!firstname.matches("[A-Za-z]+"))
            return false;

        this.firstname = firstname;
        return true;
    }

    public String getLastname() { return lastname; }

    public boolean setLastname(String lastname) {

        if (lastname == null)
            return false;

        // Removes leading and trailing whitespace characters
        lastname = lastname.trim();

        // Replaces extra spaces
        lastname = lastname.replaceAll(" +", " ");

        if (lastname.length() < 2 || lastname.length() > 20)
            return false;

        //only allows for the user to enter upper and lower case alphabetical values
        if (!lastname.matches("[A-Za-z]+"))
            return false;

        this.lastname = lastname;
        return true; }

    public String getUserlevel() {
        return userlevel;
    }

    public void setUserlevel(String userlevel) {
        this.userlevel = userlevel;
    }

    @Override
    public String toString() {
        return "User{" +
                "userid=" + userid +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", customername='" + firstname + '\'' +
                ", customername='" + lastname + '\'' +
                ", userlevel='" + userlevel + '\'' +
                '}';
    }
}
