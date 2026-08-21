import java.util.Scanner;

public class Larry {
    public static void main(String[] args) {
        String banner = " _        _      ____    ____   __   __\n"
                + "| |      / \\    |  _ \\  |  _ \\  \\ \\ / /\n"
                + "| |     / _ \\   | |_) | | |_) |  \\ V / \n"
                + "| |___ / ___ \\  |  _ <  |  _ <    | |  \n"
                + "|_____/_/   \\_\\ |_| \\_\\ |_| \\_\\   |_|  \n";

        String line = "____________________________________________________________";

        // initiate instance of scanner
        Scanner scanner = new Scanner(System.in);
        String[] tasks = new String[100];
        int taskCount = 0;

        System.out.println(line);
        System.out.print(banner);
        System.out.println(line);
        System.out.println("Hello! I'm Larry! :)");
        System.out.println("What can I do for you?");
        System.out.println(line);

        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            }

            if (input.equals("list")) {
                System.out.println(line);
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
                System.out.println(line);
            } else {
                tasks[taskCount] = input;
                taskCount++;
                System.out.println(line);
                System.out.println("added: " + input);
                System.out.println(line);
            }
        }

        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(line);

        // close instance
        scanner.close();
    }
}