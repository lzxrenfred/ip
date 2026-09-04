package larry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Runs the Larry task management application.
 */
public class Larry {
    private static final String LINE = "____________________________________________________________";
    private static final String SAVE_FILE_PATH = "data/larry.txt";

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

    /**
     * Starts Larry and processes user commands until the user exits.
     *
     * @param args Command-line arguments supplied to the application.
     */
    public static void main(String[] args) {
        String banner = " _        _      ____    ____   __   __\n"
                + "| |      / \\    |  _ \\  |  _ \\  \\ \\ / /\n"
                + "| |     / _ \\   | |_) | | |_) |  \\ V / \n"
                + "| |___ / ___ \\  |  _ <  |  _ <    | |  \n"
                + "|_____/_/   \\_\\ |_| \\_\\ |_| \\_\\   |_|  \n";

        Scanner scanner = new Scanner(System.in);
        List<Task> tasks = new ArrayList<>();

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
                handleCommand(input, tasks);
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

    private static void handleCommand(String input, List<Task> tasks)
            throws LarryException {
        if (input.equals("list")) {
            printTaskList(tasks);
        } else if (input.equals("todo")) {
            throw new EmptyDescriptionException("todo");
        } else if (input.startsWith("todo ")) {
            addTodo(input, tasks);
        } else if (input.equals("deadline") || input.startsWith("deadline ")) {
            addDeadline(input, tasks);
        } else if (input.equals("event") || input.startsWith("event ")) {
            addEvent(input, tasks);
        } else if (input.startsWith("mark ")) {
            markTask(input, tasks);
        } else if (input.startsWith("unmark ")) {
            unmarkTask(input, tasks);
        } else if (input.startsWith("delete ")) {
            deleteTask(input, tasks);
        } else if (input.startsWith("find ")) {
            findTasks(input, tasks);
        } else {
            throw new UnknownCommandException();
        }
    }

    private static void printTaskList(List<Task> tasks) {
        System.out.println(LINE);
        System.out.println("Here are the tasks in your list:");

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }

        System.out.println(LINE);
    }

    private static void addTodo(String input, List<Task> tasks)
            throws EmptyDescriptionException {
        String description = input.substring(5).trim();

        if (description.isEmpty()) {
            throw new EmptyDescriptionException("todo");
        }

        Task task = new Todo(description);
        tasks.add(task);
        saveTasks(tasks);
        printTaskAdded(task, tasks.size());
    }

    private static void addDeadline(String input, List<Task> tasks)
            throws LarryException {
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
        saveTasks(tasks);
        printTaskAdded(task, tasks.size());
    }

    private static void addEvent(String input, List<Task> tasks)
            throws LarryException {
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
        saveTasks(tasks);
        printTaskAdded(task, tasks.size());
    }

    private static void markTask(String input, List<Task> tasks)
            throws InvalidTaskNumberException {
        int index = parseTaskIndex(input.substring(5), tasks.size());

        tasks.get(index).markAsDone();
        saveTasks(tasks);

        System.out.println(LINE);
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(tasks.get(index));
        System.out.println(LINE);
    }

    private static void unmarkTask(String input, List<Task> tasks)
            throws InvalidTaskNumberException {
        int index = parseTaskIndex(input.substring(7), tasks.size());

        tasks.get(index).markAsNotDone();
        saveTasks(tasks);

        System.out.println(LINE);
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(tasks.get(index));
        System.out.println(LINE);
    }

    private static void deleteTask(String input, List<Task> tasks)
            throws InvalidTaskNumberException {
        int index = parseTaskIndex(input.substring(7), tasks.size());
        Task removedTask = tasks.remove(index);
        saveTasks(tasks);

        System.out.println(LINE);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + removedTask);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        System.out.println(LINE);
    }

    private static void saveTasks(List<Task> tasks) {
        try {
            File file = new File(SAVE_FILE_PATH);
            file.getParentFile().mkdirs();

            FileWriter writer = new FileWriter(file);

            for (Task task : tasks) {
                writer.write(task.toString() + System.lineSeparator());
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Could not save tasks.");
        }
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

    private static void findTasks(String input, ArrayList<Task> tasks) {
        String keyword = input.substring(5).trim();

        System.out.println(LINE);
        System.out.println("Here are the matching tasks in your list:");

        int matchNumber = 1;
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                System.out.println(matchNumber + "." + task);
                matchNumber++;
            }
        }

        System.out.println(LINE);
    }
}