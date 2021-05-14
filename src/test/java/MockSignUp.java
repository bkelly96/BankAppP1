import com.app.BusinessException;
import com.app.dao.UserDAO;
import com.app.dao.impl.UserDAOImpl;
import com.app.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MockSignUp {

    @Mock
    UserDAOImpl userDAO = Mockito.mock(UserDAOImpl.class);

    @InjectMocks
    UserDAOImpl userMock;

    @Test
    public void MockSignUp(){

        //create a Mock user
        User userMock = new User();

        userMock.setUsername("Basghetti");
        userMock.setPassword("password");

        try {
            userMock = userDAO.createUser(userMock);
            System.out.println(userMock);

        } catch (SQLException | BusinessException throwables) {
            throwables.printStackTrace();
        }


/*
        assertEquals(userMock.getUsername(), "Basghetti");
        assertEquals(userMock.getPassword(), "b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86");
*/
    }
}
