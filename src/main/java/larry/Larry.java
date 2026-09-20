package larry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Runs the Larry task management application.
 */
public class Larry {
    private static final String SAVE_FILE_PATH = "data/larry.txt";
    private final List<Task> tasks = new ArrayList<>();

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
     * Returns Larry's response to a user command.
     *
     * @param input User command.
     * @return Larry's response.
     */
    public String getResponse(String input) {
        if (input.equals("bye")) {
            return "Bye. Hope to see you again soon!";
        }

        try {
            return handleCommand(input);
        } catch (LarryException e) {
            return "OOPS!!! " + e.getMessage();
        }
    }

    private String handleCommand(String input) throws LarryException {
        if (input.equals("list")) {
            return getTaskList();
        } else if (input.equals("todo")) {
            throw new EmptyDescriptionException("todo");
        } else if (input.startsWith("todo ")) {
            return addTodo(input);
        } else if (input.equals("deadline") || input.startsWith("deadline ")) {
            return addDeadline(input);
        } else if (input.equals("event") || input.startsWith("event ")) {
            return addEvent(input);
        } else if (input.startsWith("mark ")) {
            return markTask(input);
        } else if (input.startsWith("unmark ")) {
            return unmarkTask(input);
        } else if (input.startsWith("delete ")) {
            return deleteTask(input);
        } else if (input.startsWith("find ")) {
            return findTasks(input);
        } else {
            throw new UnknownCommandException();
        }
    }

    private String getTaskList() {
        StringBuilder response = new StringBuilder("Here are the tasks in your list:");

        for (int i = 0; i < tasks.size(); i++) {
            response.append(System.lineSeparator())
                    .append(i + 1)
                    .append(".")
                    .append(tasks.get(i));
        }

        return response.toString();
    }

    private String addTodo(String input) throws EmptyDescriptionException {
        String description = input.substring(5).trim();

        if (description.isEmpty()) {
            throw new EmptyDescriptionException("todo");
        }

        Task task = new Todo(description);
        return addTask(task);
    }

    private String addDeadline(String input) throws LarryException {
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
        return addTask(task);
    }

    private String addEvent(String input) throws LarryException {
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
        return addTask(task);
    }

    private String markTask(String input) throws InvalidTaskNumberException {
        int index = parseTaskIndex(input.substring(5), tasks.size());
        tasks.get(index).markAsDone();
        saveTasks();

        return "Nice! I've marked this task as done:"
                + System.lineSeparator() + tasks.get(index);
    }

    private String unmarkTask(String input) throws InvalidTaskNumberException {
        int index = parseTaskIndex(input.substring(7), tasks.size());
        tasks.get(index).markAsNotDone();
        saveTasks();

        return "OK, I've marked this task as not done yet:"
                + System.lineSeparator() + tasks.get(index);
    }

    private String deleteTask(String input) throws InvalidTaskNumberException {
        int index = parseTaskIndex(input.substring(7), tasks.size());
        Task removedTask = tasks.remove(index);
        saveTasks();

        return "Noted. I've removed this task:"
                + System.lineSeparator() + "  " + removedTask
                + System.lineSeparator() + "Now you have " + tasks.size()
                + " tasks in the list.";
    }

    private void saveTasks() {
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

    private String addTask(Task task) {
        tasks.add(task);
        saveTasks();
        return getTaskAddedResponse(task);
    }

    private String getTaskAddedResponse(Task task) {
        return "Got it. I've added this task:"
                + System.lineSeparator() + "  " + task
                + System.lineSeparator() + "Now you have " + tasks.size()
                + " tasks in the list.";
    }

    private String findTasks(String input) {
        String keyword = input.substring(5).trim();
        StringBuilder response = new StringBuilder(
                "Here are the matching tasks in your list:");

        int matchNumber = 1;
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                response.append(System.lineSeparator())
                        .append(matchNumber)
                        .append(".")
                        .append(task);
                matchNumber++;
            }
        }

        return response.toString();
    }
}
