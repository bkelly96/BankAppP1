import com.app.BusinessException;
import com.app.dao.impl.TransactionsDAOImpl;
import com.app.model.Transaction;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import static org.mockito.Mockito.mock;

public class MockBadDeposit {

    @Mock
    TransactionsDAOImpl transactionsDAO = Mockito.mock(TransactionsDAOImpl.class);

    @Test
    public void testTransaction() {


        Transaction transactionMock = mock(Transaction.class);
        try {
            Calendar calendar = Calendar.getInstance();
            java.util.Date now = calendar.getTime();
            transactionMock.setTransactiontime(new Timestamp(now.getTime()));
            transactionMock.setValue(new BigDecimal(-1000));
            transactionMock.setBank_account_source_id(1);
            transactionMock.setBank_account_destination_id(1);
            transactionMock.setTransaction_type("deposit");

            transactionsDAO.addTransaction(transactionMock);
        } catch (SQLException | BusinessException err) {
            err.printStackTrace();

        }

    }
}
