package ua.ponarin.rally.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;

@Service
public class UserInputService {
    private final List<Integer> availableUserOptions = List.of(0, 1, 2, 3);

//    public Integer requestUserOption() {
//        var console = System.console();
//        Integer userOptionAsInteger = 0;
//        var done = false;
//        do {
//            var userOption = console.readLine("Choose option:%n1 - Print UserStories with different iterations%n2 - Iteration to PIP Iteration%n3 - PIP Iteration to Iteration%n0 - Exit%n");
//            try {
//                userOptionAsInteger = Integer.parseInt(userOption);
//                done = availableUserOptions.contains(userOptionAsInteger);
//            } catch (NumberFormatException ex) {
//                console.printf("Your input is incorrect, try again.\n");
//            }
//        } while (done);
//        return userOptionAsInteger;
//    }

    public int requestUserOption() {
        Scanner scanner = new Scanner(System.in);
        int userOption = requestUserInput(scanner);
        while (!availableUserOptions.contains(userOption)) {
            userOption = requestUserInput(scanner);
        }
        return userOption;
    }

    private int requestUserInput(Scanner scanner) {
        System.out.printf("Choose option:%n1 - Print UserStories with different iterations%n2 - Iteration to PIP Iteration%n3 - PIP Iteration to Iteration%n0 - Exit%n");
        return scanner.nextInt();
    }
}
