import java.util.Scanner;

public class Larry {
    public static void main(String[] args) {
        String banner = " _        _      ____    ____   __   __\n"
                + "| |      / \\    |  _ \\  |  _ \\  \\ \\ / /\n"
                + "| |     / _ \\   | |_) | | |_) |  \\ V / \n"
                + "| |___ / ___ \\  |  _ <  |  _ <    | |  \n"
                + "|_____/_/   \\_\\ |_| \\_\\ |_| \\_\\   |_|  \n";

        String line = "____________________________________________________________";

        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[100];
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
                System.out.println("Here are the tasks in your list:");

                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }

                System.out.println(line);

            } else if (input.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(input.substring(5));
                int index = taskNumber - 1;

                tasks[index].markAsDone();

                System.out.println(line);
                System.out.println("Nice! I've marked this task as done:");
                System.out.println(tasks[index]);
                System.out.println(line);

            } else if (input.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(input.substring(7));
                int index = taskNumber - 1;

                tasks[index].markAsNotDone();

                System.out.println(line);
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println(tasks[index]);
                System.out.println(line);

            } else if (input.startsWith("todo ")) {
                String description = input.substring(5);

                tasks[taskCount] = new Todo(description);
                taskCount++;

                System.out.println(line);
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + tasks[taskCount - 1]);
                System.out.println("Now you have " + taskCount + " tasks in the list.");
                System.out.println(line);

            } else if (input.startsWith("deadline ")) {
                int byIndex = input.indexOf(" /by ");

                String description = input.substring(9, byIndex);
                String by = input.substring(byIndex + 5);

                tasks[taskCount] = new Deadline(description, by);
                taskCount++;

                System.out.println(line);
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + tasks[taskCount - 1]);
                System.out.println("Now you have " + taskCount + " tasks in the list.");
                System.out.println(line);

            } else if (input.startsWith("event ")) {
                int fromIndex = input.indexOf(" /from ");
                int toIndex = input.indexOf(" /to ");

                String description = input.substring(6, fromIndex);
                String from = input.substring(fromIndex + 7, toIndex);
                String to = input.substring(toIndex + 5);

                tasks[taskCount] = new Event(description, from, to);
                taskCount++;

                System.out.println(line);
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + tasks[taskCount - 1]);
                System.out.println("Now you have " + taskCount + " tasks in the list.");
                System.out.println(line);
            }
        }

        System.out.println(line);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(line);

        scanner.close();
    }
}