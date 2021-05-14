import com.app.model.User;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

public class InvalidMenuTest {

    @Test
    public void InValidMenuTest() {

        Scanner scanner = new Scanner(System.in);
        User user = new User();

        //this code will keep the user in the loop until they make a valid choice.
        int choice = 0;
        int count = 10;
        boolean valid = false;
        while (count > 0) {
            System.out.println("Choice: ");
            try {
                choice = 0;
                if (choice >= 1 && choice <= 3) {
                    valid = true;
                } else {
                    System.out.println("Please enter a number between 1 and 3");
                    count --;
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Please enter a number");
            }
            System.out.println("-------------------");
        }

    }
}
