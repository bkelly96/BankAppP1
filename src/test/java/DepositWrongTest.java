import com.app.BusinessException;
import com.app.dao.impl.TransactionsDAOImpl;
import com.app.model.BankAccount;
import com.app.model.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

public class DepositWrongTest {

    TransactionsDAOImpl transactionsDAO = new TransactionsDAOImpl();

    @Test
    public void testTransaction() {


        Transaction transaction = new Transaction();

        try {

            Calendar calendar = Calendar.getInstance();
            java.util.Date now = calendar.getTime();
            transaction.setTransactiontime(new Timestamp(now.getTime()));
            transaction.setValue(new BigDecimal(-1000));
            transaction.setBank_account_source_id(1);
            transaction.setBank_account_destination_id(1);
            transaction.setTransaction_type("deposit");

            transactionsDAO.addTransaction(transaction);
        } catch (SQLException | BusinessException err) {
            err.printStackTrace();

        }

    }
}
