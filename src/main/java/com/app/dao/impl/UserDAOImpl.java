package com.app.dao.impl;

import com.app.BusinessException;
import com.app.dao.UserDAO;
import com.app.dbutil.PostGresConnection;
import com.app.model.Transaction;
import com.app.model.User;
import org.apache.log4j.Logger;

import java.sql.*;

public class UserDAOImpl implements UserDAO{

    private static Logger log = Logger.getLogger(UserDAOImpl.class);

    @Override
    public User createUser(User user) throws SQLException, BusinessException {



        //this is the page where we store the data into the table
        Connection connection = PostGresConnection.getConnection();
        String sql = "INSERT INTO bank_schema.user\n" +
                "(username, \"password\", firstname, lastname, userlevel)\n" +
                "VALUES(?, ?, ?, ?, ?);\n";
        PreparedStatement preparedStatement= connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3, user.getFirstname());
        preparedStatement.setString(4, user.getLastname());
        preparedStatement.setString(5, user.getUserlevel());

        int c=preparedStatement.executeUpdate();
        if(c==1){
            ResultSet resultSet=preparedStatement.getGeneratedKeys();
            if(resultSet.next()){
                user.setUserid(resultSet.getInt(1));
            }
        }else{
            throw new BusinessException("Failure in registration...please retry...");
        }

        preparedStatement.close();

        return user;
    }

    @Override
    public User loginUsername(User user) throws SQLException{

        try {
            //Call to establish connection to my database
            Connection connection = PostGresConnection.getConnection();

            Statement stmt = connection.createStatement();

            //sql Query to find username and password
            String SQL = "Select * from bank_schema.user Where username='" + user.getUsername() +"' and password='" + user.getPassword() +"'";

            //sending the string to the database and storing it in a result set
            ResultSet rs = stmt.executeQuery(SQL);
            boolean isloggedIn = false;

            //checks database for the entered username and returns success and sets users name to user if it is a success
            if (rs.next()) {
                //setting the rest of the user information by getting from RS Next
                isloggedIn = true;
                user.setUserid(rs.getInt(1));
                user.setFirstname(rs.getString(4));
                user.setLastname(rs.getString(5));
                user.setUserlevel(rs.getString(6));
                log.info("Successful log in attempt by " + user.getUsername());
            }
            else
                log.info("Failed Log in attempt by " + user.getUsername());

            rs.close();
            stmt.close();

            //if user is able to log in then this will return full user data.
            if (isloggedIn == true) {return user;}

            // You can also validate user by result size if its comes zero user is invalid else user is valid

        } catch (SQLException err) {
            System.out.println("Error: " + err.getMessage());;
        }

        return null;

    }

    //this will show the information
    @Override
    public void showTransactions(User user) throws SQLException {
        //lists all of the transactions on the transaction table
            try {

                //Establishing connection
                Connection connection = PostGresConnection.getConnection();

                Statement stmt = connection.createStatement();

                //SQL query for accessing all connection
                String sql = "select * from bank_schema.transactions where bank_account_src_id = '" + user.getUserid() + "' or bankaccount_destination_id = '"+ user.getUserid() +"'";

                //sending the string to the database and storing it in a result set
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    Transaction transaction = new Transaction();
                    transaction.setTransactionid(rs.getInt(1));
                    transaction.setTransactiontime(rs.getTimestamp(2));
                    transaction.setValue(rs.getBigDecimal(3));
                    transaction.setBank_account_source_id(rs.getInt(4));
                    transaction.setBank_account_destination_id(rs.getInt(5));
                    transaction.setTransaction_type(rs.getString(6));
                    log.info(transaction.toString());
                }

                // You can also validate user by result size if its comes zero user is invalid else user is valid

            } catch(SQLException err) {
                System.out.println("Error: " + err.getMessage());
            }
    }
}
