import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a task with various attributes such as title, description, due date, and priority.
 */
public class Task {
    private String id;

    private String title;

    private String description;

    private LocalDateTime dueDateTime;

    private LocalDateTime reminderTime;

    private TaskPriority priority;

    private TaskStatus status;

    private LocalDateTime createdTime;

    private LocalDateTime lastModifiedTime;

    /**
     * Represents the priority levels for a task.
     */
    public enum TaskPriority {
        HIGH, MEDIUM, LOW
    }

    /**
     * Represents the status of a task.
     */
    public enum TaskStatus {
        NOT_STARTED, IN_PROGRESS, COMPLETED, OVERDUE
    }

    /**
     * Creates a new Task object.
     *
     * @param title        the title of the task
     * @param description  the description of the task
     * @param dueDateTime  the due date and time of the task
     * @param reminderTime the reminder date and time for the task
     * @param priority     the priority level of the task
     * @throws IllegalArgumentException if any required parameter is null
     */
    public Task(String title, String description, LocalDateTime dueDateTime, LocalDateTime reminderTime, TaskPriority priority) {
        if (title == null || description == null || dueDateTime == null || reminderTime == null || priority == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.dueDateTime = dueDateTime;
        this.reminderTime = reminderTime;
        this.priority = priority;
        this.status = TaskStatus.NOT_STARTED;
        this.createdTime = LocalDateTime.now();
        this.lastModifiedTime = LocalDateTime.now();
    }

    /**
     * Gets the unique identifier of the task.
     *
     * @return the task ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the title of the task.
     *
     * @return the task title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the task.
     *
     * @param title the new title of the task
     */
    public void setTitle(String title) {
        this.title = title;
        updateLastModifiedTime();
    }

    /**
     * Gets the description of the task.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the task.
     *
     * @param description the new description of the task
     */
    public void setDescription(String description) {
        this.description = description;
        updateLastModifiedTime();
    }

    /**
     * Gets the due date and time of the task.
     *
     * @return the due date and time
     */
    public LocalDateTime getDueDateTime() {
        return dueDateTime;
    }

    /**
     * Sets the due date and time of the task.
     *
     * @param dueDateTime the new due date and time
     */
    public void setDueDateTime(LocalDateTime dueDateTime) {
        this.dueDateTime = dueDateTime;
        updateLastModifiedTime();
    }

    /**
     * Gets the reminder date and time of the task.
     *
     * @return the reminder date and time
     */
    public LocalDateTime getReminderTime() {
        return reminderTime;
    }

    /**
     * Sets the reminder date and time of the task.
     *
     * @param reminderTime the new reminder date and time
     */
    public void setReminderTime(LocalDateTime reminderTime) {
        this.reminderTime = reminderTime;
        updateLastModifiedTime();
    }

    /**
     * Gets the priority level of the task.
     *
     * @return the task priority
     */
    public TaskPriority getPriority() {
        return priority;
    }

    /**
     * Sets the priority level of the task.
     *
     * @param priority the new priority level
     */
    public void setPriority(TaskPriority priority) {
        this.priority = priority;
        updateLastModifiedTime();
    }

    /**
     * Gets the current status of the task.
     *
     * @return the task status
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the task.
     *
     * @param status the new status of the task
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
        updateLastModifiedTime();
    }

    /**
     * Gets the creation time of the task.
     *
     * @return the creation time
     */
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    /**
     * Gets the last modified time of the task.
     *
     * @return the last modified time
     */
    public LocalDateTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    /**
     * Updates the last modified time of the task.
     */
    private void updateLastModifiedTime() {
        this.lastModifiedTime = LocalDateTime.now();
    }

    /**
     * Returns a string representation of the task.
     *
     * @return a string containing the task details
     */
    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dueDateTime=" + dueDateTime +
                ", reminderTime=" + reminderTime +
                ", priority=" + priority +
                ", status=" + status +
                ", createdTime=" + createdTime +
                ", lastModifiedTime=" + lastModifiedTime +
                '}';
    }
}
