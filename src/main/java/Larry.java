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

        System.out.println(line);
        System.out.print(banner);
        System.out.println(line);
        System.out.println("Hello! I'm Larry! :)");
        System.out.println("What can I do for you?");
        System.out.println(line);

        String input = scanner.nextLine();
        System.out.println(input);

        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(line);
    }
}