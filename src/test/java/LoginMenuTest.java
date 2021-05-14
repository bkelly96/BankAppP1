import com.app.model.User;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static com.app.Bank_Main.*;

public class LoginMenuTest {

    @Test
    public void InValidMenuTest() {

        int choice = 4;
        User user = new User();

        Scanner scanner = null;
        switch (choice) {
            case 1://Login
                user = loginUsername(user, scanner);
                if (user == null) {
                    System.out.println("Log in failed");
                    break;
                }
                //checks for user inside of user object
                if (user.getUserlevel().equals("user")) {
                    userMenu(user, scanner);
                }
                //checks for employee level inside of user object
                if (user.getUserlevel().equals("employee")) {
                    employeeMenu(user, scanner);
                }
                break;
            case 2://Signup
                createNewUser(user, scanner);
                break;
            case 3://Leave
                break;

        }
}
}
