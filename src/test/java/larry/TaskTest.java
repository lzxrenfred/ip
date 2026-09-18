package larry;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
public class TaskTest {

    @Test
    public void markAsDone_taskNotDone_changesStatusToDone() {
        Task task = new Todo("read book");
        task.markAsDone();
        assertEquals("[T][X] read book", task.toString());
    }

    @Test
    public void markAsNotDone_taskDone_changesStatusToNotDone() {
        Task task = new Todo("read book");
        task.markAsDone();
        task.markAsNotDone();
        assertEquals("[T][ ] read book", task.toString());
    }
}
