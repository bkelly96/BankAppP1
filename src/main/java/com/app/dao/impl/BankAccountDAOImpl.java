package com.app.dao.impl;

import com.app.BusinessException;
import com.app.dao.BankAccountDao;
import com.app.dbutil.PostGresConnection;
import com.app.model.BankAccount;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;


public class BankAccountDAOImpl implements BankAccountDao {

    private static Logger log = Logger.getLogger(UserDAOImpl.class);

    @Override
    public BankAccount createBankAccount(BankAccount bankAccount) throws SQLException, BusinessException {
        //this is the page where we store the data into the table
        Connection connection = PostGresConnection.getConnection();

        String sql = "INSERT INTO bank_schema.bankaccount\n" +
                "(accountbalance, userid, statuses)\n" +
                "VALUES(?, ?, ?);\n";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setBigDecimal(1, bankAccount.getAccountBalance());
        preparedStatement.setInt(2, bankAccount.getUserid());
        preparedStatement.setString(3, bankAccount.getStatuses());

        int c = preparedStatement.executeUpdate();
        if (c == 1) {
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                bankAccount.setBankaccountid(resultSet.getInt(1));
            }
        } else {
            throw new BusinessException("Failure in registration...please retry...");
        }

        preparedStatement.close();


        return bankAccount;
    }

    @Override
    public BankAccount viewBankAccount(int bankaccountid) throws SQLException, BusinessException {

        try {
            //Establishing connection
            Connection connection = PostGresConnection.getConnection();

            BankAccount bankAccount = new BankAccount();
            Statement stmt = connection.createStatement();

            //sql Query to find username and password

            String sql = "select * from bank_schema.bankaccount where bankaccountid = '" + bankaccountid + "'";

            ResultSet rs = stmt.executeQuery(sql);
            boolean doesAccountExist = false;

            if (rs.next()) {
                //setting the rest of the user information by getting from RS Next
                doesAccountExist = true;
                bankAccount.setBankaccountid(rs.getInt(1));
                bankAccount.setAccountBalance(rs.getBigDecimal(2));
                bankAccount.setUserid(rs.getInt(3));
                bankAccount.setStatuses(rs.getString(4));
                bankAccount.setReviewedBy(rs.getInt(5));

                log.info("Here is your account!");
            } else
                log.info("Failed viewing of account " + bankaccountid);

            rs.close();
            stmt.close();

            //if user is able to log in then this will return full user data.
            if (doesAccountExist == true) {
                return bankAccount;
            }

            // You can also validate user by result size if its comes zero user is invalid else user is valid

        } catch (SQLException err) {
            System.out.println("Error: " + err.getMessage());
            ;
        }

        return null;

    }

    @Override
    public boolean adjustBankAccount(double amount, BankAccount bankAccount) throws SQLException, BusinessException {

        try {
            //Call to establish connection to my database
            Connection connection = PostGresConnection.getConnection();

            Statement stmt = connection.createStatement();

            //sql Query to find username and password
            String SQL = "Update bank_schema.bankaccount set accountbalance = '" + (new BigDecimal(amount).add(bankAccount.getAccountBalance())) +
                    "' where bankaccountid ='" + bankAccount.getBankaccountid() + "'";

            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public BankAccount updateBankAccount(BankAccount bankAccount) throws SQLException, BusinessException {
        try {
            //Call to establish connection to my database
            Connection connection = PostGresConnection.getConnection();

            Statement stmt = connection.createStatement();

            //sql Query to update statuses, Reviewed, bankaccount id
            String SQL = "Update bank_schema.bankaccount set statuses ='" + bankAccount.getStatuses() + "',reviewedby = '"
                    + bankAccount.getReviewedBy()+ "' where bankaccountid ='" + bankAccount.getBankaccountid()+"'";

            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.executeUpdate();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

            return bankAccount;
        }
    }

    /*@Override to account and from account
    public boolean transferAmount(double amount, BankAccount bankAccount) throws SQLException, BusinessException {
        try {
            //Call to establish connection to my database
            Connection connection = PostGresConnection.getConnection();

            Statement stmt = connection.createStatement();

            //SQL query for accessing all connection
            String sql = "select * from bank_schema.bankaccount;";

            //sending the string to the database and storing it in a result set
            ResultSet rs = stmt.executeQuery(sql);

            if (amount > ResultSet.(rs.getBigDecimal(3));

            //sql Query to find username and password
            String SQL = "Update bank_schema.bankaccount set accountbalance = '" + (new BigDecimal(amount).add(bankAccount.getAccountbalance())) +
                    "' where bankaccountid ='" + bankAccount.getBankaccountid() + "'";

            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    */

