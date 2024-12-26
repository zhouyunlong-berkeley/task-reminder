import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskScheduler {
    private final ScheduledExecutorService scheduler;
    private final TaskQueue taskQueue;
    private final Map<String, ScheduledFuture<?>> scheduledTasks;
    private final TaskReminderHandler reminderHandler;

    /**
     * Interface for handling task reminders.
     */
    public interface TaskReminderHandler {
        void onTaskReminder(Task task);
    }

    /**
     * Constructs a TaskScheduler with the specified reminder handler.
     *
     * @param reminderHandler the handler to be invoked when a task reminder is triggered
     */
    public TaskScheduler(TaskReminderHandler reminderHandler) {
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.taskQueue = new TaskQueue();
        this.scheduledTasks = new ConcurrentHashMap<>();
        this.reminderHandler = reminderHandler;
    }

    /**
     * Schedules a new task and sets up its reminder.
     *
     * @param task the task to be scheduled
     */
    public void scheduleTask(Task task) {
        taskQueue.addTask(task);
        scheduleReminder(task);
    }

    /**
     * Schedules a reminder for the given task.
     *
     * @param task the task for which the reminder is to be scheduled
     */
    private void scheduleReminder(Task task) {
        LocalDateTime reminderTime = task.getReminderTime();
        if (reminderTime != null && reminderTime.isAfter(LocalDateTime.now())) {
            long delay = java.time.Duration.between(LocalDateTime.now(), reminderTime).toSeconds();

            ScheduledFuture<?> future = scheduler.schedule(() -> {
                if (task.getStatus() != Task.TaskStatus.COMPLETED) {
                    reminderHandler.onTaskReminder(task);
                    checkTaskDueStatus(task);
                }
            }, delay, TimeUnit.SECONDS);

            scheduledTasks.put(task.getId(), future);
        }
    }

    /**
     * Checks if the task is overdue and updates its status accordingly.
     *
     * @param task the task to be checked
     */
    private void checkTaskDueStatus(Task task) {
        if (LocalDateTime.now().isAfter(task.getDueDateTime())) {
            task.setStatus(Task.TaskStatus.OVERDUE);
        }
    }

    /**
     * Cancels the reminder for the given task.
     *
     * @param task the task whose reminder is to be canceled
     */
    public void cancelReminder(Task task) {
        ScheduledFuture<?> future = scheduledTasks.remove(task.getId());
        if (future != null) {
            future.cancel(false);
        }
    }

    /**
     * Reschedules a reminder for the given task with a new reminder time.
     *
     * @param task            the task to be rescheduled
     * @param newReminderTime the new reminder time
     */
    public void rescheduleTask(Task task, LocalDateTime newReminderTime) {
        cancelReminder(task);
        task.setReminderTime(newReminderTime);
        scheduleReminder(task);
    }

    /**
     * Marks the given task as completed, cancels its reminder, and removes it from the queue.
     *
     * @param task the task to be completed
     */
    public void completeTask(Task task) {
        task.setStatus(Task.TaskStatus.COMPLETED);
        cancelReminder(task);
        taskQueue.removeTask(task);
    }

    /**
     * Retrieves the next pending task from the task queue.
     *
     * @return the next pending task, or null if the queue is empty
     */
    public Task getNextPendingTask() {
        return taskQueue.peek();
    }

    /**
     * Shuts down the task scheduler and terminates any scheduled tasks.
     */
    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

