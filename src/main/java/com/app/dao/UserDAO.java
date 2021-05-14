package com.app.dao;

import com.app.BusinessException;
import com.app.model.User;

import java.sql.SQLException;

public interface UserDAO {

    //setting the rule that we want to create an employee
     User createUser(User user) throws SQLException, BusinessException;
     //setting the rule for logging into the account
     User loginUsername(User user) throws SQLException;
     //setting the rule to create an employee with exceptions to the information
    void showTransactions(User user) throws SQLException;
}
