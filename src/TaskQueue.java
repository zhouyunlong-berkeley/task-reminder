import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class TaskQueue {
    private final PriorityQueue<Task> taskHeap;

    /**
     * Constructs an empty TaskQueue with a custom comparator to prioritize tasks.
     * The comparator prioritizes tasks by their priority level and creation time.
     */
    public TaskQueue() {
        Comparator<Task> comparator = (t1, t2) -> {
            int priorityCompare = t1.getPriority().compareTo(t2.getPriority());
            if (priorityCompare != 0) {
                return priorityCompare;
            }
            return t1.getCreatedTime().compareTo(t2.getCreatedTime());
        };
        this.taskHeap = new PriorityQueue<>(comparator);
    }

    /**
     * Adds a new task to the queue.
     *
     * @param task the task to be added
     */
    public void addTask(Task task) {
        taskHeap.offer(task);
    }

    /**
     * Retrieves, but does not remove, the highest-priority task.
     *
     * @return the highest-priority task
     * @throws NoSuchElementException if the queue is empty
     */
    public Task peek() {
        if (taskHeap.isEmpty()) {
            throw new NoSuchElementException("Task queue is empty");
        }
        return taskHeap.peek();
    }

    /**
     * Retrieves and removes the highest-priority task.
     *
     * @return the highest-priority task
     * @throws NoSuchElementException if the queue is empty
     */
    public Task poll() {
        if (taskHeap.isEmpty()) {
            throw new NoSuchElementException("Task queue is empty");
        }
        return taskHeap.poll();
    }

    /**
     * Updates the priority of a specific task.
     *
     * @param task        the task whose priority is to be updated
     * @param newPriority the new priority for the task
     */
    public void updateTaskPriority(Task task, Task.TaskPriority newPriority) {
        if (taskHeap.remove(task)) {
            task.setPriority(newPriority);
            taskHeap.offer(task);
        }
    }

    /**
     * Removes a specific task from the queue.
     *
     * @param task the task to be removed
     * @return true if the task was successfully removed; false otherwise
     */
    public boolean removeTask(Task task) {
        return taskHeap.remove(task);
    }

    /**
     * Returns the number of tasks in the queue.
     *
     * @return the current number of tasks
     */
    public int size() {
        return taskHeap.size();
    }

    /**
     * Checks if the queue is empty.
     *
     * @return true if the queue is empty; false otherwise
     */
    public boolean isEmpty() {
        return taskHeap.isEmpty();
    }

    /**
     * Clears all tasks from the queue.
     */
    public void clear() {
        taskHeap.clear();
    }

    /**
     * Retrieves all tasks in the queue as an array.
     * This method is primarily for debugging and may disrupt the heap structure.
     *
     * @return an array of all tasks in the queue
     */
    public Task[] getAllTasks() {
        return taskHeap.toArray(new Task[0]);
    }

    public void updateTask(Task task) {
        // Cannot update null task
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }

        // Remove the old task and add the updated one
        // Since PriorityQueue doesn't have a direct update method,
        // we need to remove and re-add to maintain heap properties
        boolean removed = taskHeap.remove(task);

        // Only add if the task was actually in the queue
        if (removed) {
            taskHeap.offer(task);
        } else {
            throw new IllegalArgumentException("Task not found in queue");
        }
    }
}
