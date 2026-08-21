import java.util.ArrayList;
import java.util.Scanner;

public class Larry {
    private static final String LINE = "____________________________________________________________";

    private static class LarryException extends Exception {
        public LarryException(String message) {
            super(message);
        }
    }

    private static class EmptyDescriptionException extends LarryException {
        public EmptyDescriptionException(String taskType) {
            super(taskType + " needs a description.");
        }
    }

    private static class UnknownCommandException extends LarryException {
        public UnknownCommandException() {
            super("I don't recognise that command.");
        }
    }

    private static class InvalidTaskNumberException extends LarryException {
        public InvalidTaskNumberException() {
            super("That task number doesn't exist.");
        }
    }

    private static class InvalidFormatException extends LarryException {
        public InvalidFormatException(String message) {
            super(message);
        }
    }

    public static void main(String[] args) {
        String banner = " _        _      ____    ____   __   __\n"
                + "| |      / \\    |  _ \\  |  _ \\  \\ \\ / /\n"
                + "| |     / _ \\   | |_) | | |_) |  \\ V / \n"
                + "| |___ / ___ \\  |  _ <  |  _ <    | |  \n"
                + "|_____/_/   \\_\\ |_| \\_\\ |_| \\_\\   |_|  \n";

        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();

        System.out.println(LINE);
        System.out.print(banner);
        System.out.println(LINE);
        System.out.println("Hello! I'm Larry! :)");
        System.out.println("What can I do for you?");
        System.out.println(LINE);

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                break;
            }

            try {
                if (input.equals("list")) {
                    System.out.println(LINE);
                    System.out.println("Here are the tasks in your list:");

                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i + 1) + "." + tasks.get(i));
                    }

                    System.out.println(LINE);

                } else if (input.equals("todo")) {
                    throw new EmptyDescriptionException("todo");

                } else if (input.startsWith("todo ")) {
                    String description = input.substring(5).trim();

                    if (description.isEmpty()) {
                        throw new EmptyDescriptionException("todo");
                    }

                    Task task = new Todo(description);
                    tasks.add(task);
                    printTaskAdded(task, tasks.size());

                } else if (input.equals("deadline") || input.startsWith("deadline ")) {
                    int byIndex = input.indexOf(" /by ");

                    if (byIndex == -1) {
                        throw new InvalidFormatException(
                                "Use: deadline <description> /by <time>");
                    }

                    String description = input.substring(9, byIndex).trim();
                    String by = input.substring(byIndex + 5).trim();

                    if (description.isEmpty()) {
                        throw new EmptyDescriptionException("deadline");
                    }

                    if (by.isEmpty()) {
                        throw new InvalidFormatException(
                                "Please specify when the deadline is due after /by.");
                    }

                    Task task = new Deadline(description, by);
                    tasks.add(task);
                    printTaskAdded(task, tasks.size());

                } else if (input.equals("event") || input.startsWith("event ")) {
                    int fromIndex = input.indexOf(" /from ");
                    int toIndex = input.indexOf(" /to ");

                    if (fromIndex == -1 || toIndex == -1 || toIndex < fromIndex) {
                        throw new InvalidFormatException(
                                "Use: event <description> /from <start> /to <end>");
                    }

                    String description = input.substring(6, fromIndex).trim();
                    String from = input.substring(fromIndex + 7, toIndex).trim();
                    String to = input.substring(toIndex + 5).trim();

                    if (description.isEmpty()) {
                        throw new EmptyDescriptionException("event");
                    }

                    if (from.isEmpty() || to.isEmpty()) {
                        throw new InvalidFormatException(
                                "Please specify both the event start and end times.");
                    }

                    Task task = new Event(description, from, to);
                    tasks.add(task);
                    printTaskAdded(task, tasks.size());

                } else if (input.startsWith("mark ")) {
                    int index = parseTaskIndex(input.substring(5), tasks.size());

                    tasks.get(index).markAsDone();

                    System.out.println(LINE);
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println(tasks.get(index));
                    System.out.println(LINE);

                } else if (input.startsWith("unmark ")) {
                    int index = parseTaskIndex(input.substring(7), tasks.size());

                    tasks.get(index).markAsNotDone();

                    System.out.println(LINE);
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println(tasks.get(index));
                    System.out.println(LINE);

                } else if (input.startsWith("delete ")) {
                    int index = parseTaskIndex(input.substring(7), tasks.size());
                    Task removedTask = tasks.remove(index);

                    System.out.println(LINE);
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + removedTask);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println(LINE);

                } else {
                    throw new UnknownCommandException();
                }

            } catch (LarryException e) {
                System.out.println(LINE);
                System.out.println("OOPS!!! " + e.getMessage());
                System.out.println(LINE);
            }
        }

        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);

        scanner.close();
    }

    private static int parseTaskIndex(String numberText, int taskCount)
            throws InvalidTaskNumberException {
        try {
            int taskNumber = Integer.parseInt(numberText.trim());
            int index = taskNumber - 1;

            if (index < 0 || index >= taskCount) {
                throw new InvalidTaskNumberException();
            }

            return index;

        } catch (NumberFormatException e) {
            throw new InvalidTaskNumberException();
        }
    }

    private static void printTaskAdded(Task task, int taskCount) {
        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(LINE);
    }
}