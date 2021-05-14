import com.app.dao.impl.UserDAOImpl;
import com.app.model.User;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InvalidUsernameTest {

    UserDAOImpl userDAO = new UserDAOImpl();

    @Test
    public void testusernamelogin(){

        User user = new User();

        user.setUsername("Johnnyisbad");
        user.setPassword("password");
        try {
            user = userDAO.loginUsername(user);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        assertEquals(user,null);

    }

}
