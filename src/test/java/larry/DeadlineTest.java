package larry;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DeadlineTest {

    @Test
    public void toString_validDate_formatsDateCorrectly() {
        Deadline deadline = new Deadline("submit assignment", "2026-09-10");
        assertEquals(
                "[D][ ] submit assignment (by: Sept 10 2026)",
                deadline.toString()
        );
    }

    @Test
    public void markAsDone_deadline_updatesStatusAndKeepsDate() {
        Deadline deadline = new Deadline("submit assignment", "2026-09-10");
        deadline.markAsDone();
        assertEquals(
                "[D][X] submit assignment (by: Sept 10 2026)",
                deadline.toString()
        );
    }
}

