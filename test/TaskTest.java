import org.junit.jupiter.api.Test;
import static com.google.common.truth.Truth.assertThat;
import java.time.LocalDateTime;

public class TaskTest {

    @Test
    public void testTaskCreation() {
        Task task = new Task(
                "Complete Project",
                "Finish the final project for CS class.",
                LocalDateTime.of(2024, 12, 31, 23, 59),
                LocalDateTime.of(2024, 12, 30, 12, 0),
                Task.TaskPriority.HIGH
        );

        // Verify initial values
        assertThat(task.getTitle()).isEqualTo("Complete Project");
        assertThat(task.getDescription()).isEqualTo("Finish the final project for CS class.");
        assertThat(task.getDueDateTime()).isEqualTo(LocalDateTime.of(2024, 12, 31, 23, 59));
        assertThat(task.getReminderTime()).isEqualTo(LocalDateTime.of(2024, 12, 30, 12, 0));
        assertThat(task.getPriority()).isEqualTo(Task.TaskPriority.HIGH);
        assertThat(task.getStatus()).isEqualTo(Task.TaskStatus.NOT_STARTED);
    }

    @Test
    public void testTaskModification() {
        Task task = new Task(
                "Complete Project",
                "Finish the final project for CS class.",
                LocalDateTime.of(2024, 12, 31, 23, 59),
                LocalDateTime.of(2024, 12, 30, 12, 0),
                Task.TaskPriority.HIGH
        );

        // Modify task properties
        task.setTitle("Complete CS Project");
        task.setDescription("Finish the final project for Computer Science class.");
        task.setDueDateTime(LocalDateTime.of(2024, 12, 30, 18, 0));
        task.setReminderTime(LocalDateTime.of(2024, 12, 29, 9, 0));
        task.setPriority(Task.TaskPriority.MEDIUM);
        task.setStatus(Task.TaskStatus.IN_PROGRESS);

        // Verify modified values
        assertThat(task.getTitle()).isEqualTo("Complete CS Project");
        assertThat(task.getDescription()).isEqualTo("Finish the final project for Computer Science class.");
        assertThat(task.getDueDateTime()).isEqualTo(LocalDateTime.of(2024, 12, 30, 18, 0));
        assertThat(task.getReminderTime()).isEqualTo(LocalDateTime.of(2024, 12, 29, 9, 0));
        assertThat(task.getPriority()).isEqualTo(Task.TaskPriority.MEDIUM);
        assertThat(task.getStatus()).isEqualTo(Task.TaskStatus.IN_PROGRESS);
    }

    @Test
    public void testTaskCompletion() {
        Task task = new Task(
                "Complete Project",
                "Finish the final project for CS class.",
                LocalDateTime.of(2024, 12, 31, 23, 59),
                LocalDateTime.of(2024, 12, 30, 12, 0),
                Task.TaskPriority.HIGH
        );

        // Change task status to COMPLETED
        task.setStatus(Task.TaskStatus.COMPLETED);

        // Verify status
        assertThat(task.getStatus()).isEqualTo(Task.TaskStatus.COMPLETED);
    }
}
