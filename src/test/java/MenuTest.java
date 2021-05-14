import com.app.model.User;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

public class MenuTest {

    @Test
    public void Validlogin() {

        Scanner scanner = new Scanner(System.in);
        User user = new User();

        //this code will keep the user in the loop until they make a valid choice.
        int choice = 0;
        boolean valid = false;
        while (!valid) {
            System.out.println("Choice: ");
            try {
                choice = 1;
                if (choice >= 1 && choice <= 3) {
                    valid = true;
                } else {
                    System.out.println("Please enter a number between 1 and 3");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Please enter a number");
            }
            System.out.println("-------------------");
        }

    }
}
