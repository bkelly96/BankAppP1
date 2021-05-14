package com.app.dao.impl;

import com.app.BusinessException;
import com.app.dao.TransactionsDAO;
import com.app.dbutil.PostGresConnection;
import com.app.model.Transaction;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionsDAOImpl implements TransactionsDAO {

    private static Logger log = Logger.getLogger(UserDAOImpl.class);

    @Override
    public void addTransaction(Transaction transaction) throws SQLException, BusinessException {

        try {
            Connection connection = PostGresConnection.getConnection();
            String sql = "INSERT INTO bank_schema.transactions\n" +
                    "(transactiontime, value, bank_account_src_id, bankaccount_destination_id, transaction_type)\n" +
                    "VALUES(?, ?, ?, ?, ?);\n";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setTimestamp(1, transaction.getTransactiontime());
            preparedStatement.setBigDecimal(2, transaction.getValue());
            preparedStatement.setInt(3, transaction.getBank_account_source_id());
            preparedStatement.setInt(4, transaction.getBank_account_destination_id());
            preparedStatement.setString(5, transaction.getTransaction_type());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException err) {
            System.out.println("Error: " + err.getMessage());;
        }

    }

    //lists all of the transactions on the transaction table
    @Override
    public List<Transaction> getTransactions() throws SQLException, BusinessException {

        List<Transaction> transactionList = new ArrayList<>();

        try {

            //Establishing connection
            Connection connection = PostGresConnection.getConnection();

            Statement stmt = connection.createStatement();

            //SQL query for accessing all connection
            String sql = "select * from bank_schema.transactions;";

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
                transactionList.add(transaction);
            }

            // You can also validate user by result size if its comes zero user is invalid else user is valid

        } catch(SQLException err) {
            System.out.println("Error: " + err.getMessage());;
        }
        return transactionList;
    }
}
