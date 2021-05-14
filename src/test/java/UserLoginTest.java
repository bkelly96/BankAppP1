import com.app.dao.UserDAO;
import com.app.dao.impl.UserDAOImpl;
import com.app.model.User;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserLoginTest {

    UserDAOImpl userDAO = new UserDAOImpl();

    @Test
    public void testvalidlogin(){

        User user = new User();

        user.setUsername("Johnny");
        user.setPassword("password");
        try {
            user = userDAO.loginUsername(user);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        assertEquals(user.getUserid(), 9);
        assertEquals(user.getUsername(), "Johnny");
        assertEquals(user.getPassword(), "b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86");
        assertEquals(user.getFirstname(), "Johnny");
        assertEquals(user.getLastname(), "Lastname");
        assertEquals(user.getUserlevel(), "user");

    }
}
