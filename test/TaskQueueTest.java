import org.junit.jupiter.api.Test;
import static com.google.common.truth.Truth.assertThat;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

public class TaskQueueTest {

    @Test
    public void testAddAndPeekTask() {
        TaskQueue taskQueue = new TaskQueue();

        Task task1 = new Task("Task1", "Description1", LocalDateTime.now().plusDays(1), LocalDateTime.now(), Task.TaskPriority.MEDIUM);
        Task task2 = new Task("Task2", "Description2", LocalDateTime.now().plusDays(2), LocalDateTime.now(), Task.TaskPriority.HIGH);

        taskQueue.addTask(task1);
        taskQueue.addTask(task2);

        // 检查最高优先级任务
        assertThat(taskQueue.peek()).isEqualTo(task2); // 高优先级任务应在前
        assertThat(taskQueue.size()).isEqualTo(2);    // 确保队列中有两个任务
    }

    @Test
    public void testPollTask() {
        TaskQueue taskQueue = new TaskQueue();

        Task task1 = new Task("Task1", "Description1", LocalDateTime.now().plusDays(1), LocalDateTime.now(), Task.TaskPriority.MEDIUM);
        Task task2 = new Task("Task2", "Description2", LocalDateTime.now().plusDays(2), LocalDateTime.now(), Task.TaskPriority.HIGH);

        taskQueue.addTask(task1);
        taskQueue.addTask(task2);

        // 移除并检查最高优先级任务
        assertThat(taskQueue.poll()).isEqualTo(task2); // 高优先级任务应先被移除
        assertThat(taskQueue.poll()).isEqualTo(task1); // 然后是下一个任务
        assertThat(taskQueue.isEmpty()).isTrue();      // 队列应为空
    }

    @Test
    public void testUpdateTaskPriority() {
        TaskQueue taskQueue = new TaskQueue();

        Task task1 = new Task("Task1", "Description1", LocalDateTime.now().plusDays(1), LocalDateTime.now(), Task.TaskPriority.LOW);
        Task task2 = new Task("Task2", "Description2", LocalDateTime.now().plusDays(2), LocalDateTime.now(), Task.TaskPriority.MEDIUM);

        taskQueue.addTask(task1);
        taskQueue.addTask(task2);

        // 更新任务优先级
        task1.setPriority(Task.TaskPriority.HIGH);
        taskQueue.updateTaskPriority(task1, Task.TaskPriority.HIGH); // 假设队列支持更新操作

        // 检查最高优先级任务
        assertThat(taskQueue.peek()).isEqualTo(task1); // task1 的优先级提高，应在前
    }

    @Test
    public void testRemoveTask() {
        TaskQueue taskQueue = new TaskQueue();

        Task task1 = new Task("Task1", "Description1", LocalDateTime.now().plusDays(1), LocalDateTime.now(), Task.TaskPriority.LOW);
        Task task2 = new Task("Task2", "Description2", LocalDateTime.now().plusDays(2), LocalDateTime.now(), Task.TaskPriority.MEDIUM);

        taskQueue.addTask(task1);
        taskQueue.addTask(task2);

        // 删除任务
        assertThat(taskQueue.removeTask(task1)).isTrue(); // 成功删除任务
        assertThat(taskQueue.size()).isEqualTo(1);        // 队列剩余一个任务
        assertThat(taskQueue.peek()).isEqualTo(task2);    // 检查剩余任务
    }

    @Test
    public void testClearQueue() {
        TaskQueue taskQueue = new TaskQueue();

        Task task1 = new Task("Task1", "Description1", LocalDateTime.now().plusDays(1), LocalDateTime.now(), Task.TaskPriority.LOW);
        Task task2 = new Task("Task2", "Description2", LocalDateTime.now().plusDays(2), LocalDateTime.now(), Task.TaskPriority.HIGH);

        taskQueue.addTask(task1);
        taskQueue.addTask(task2);

        // 清空队列
        taskQueue.clear();

        // 检查队列状态
        assertThat(taskQueue.isEmpty()).isTrue(); // 队列应为空
        assertThat(taskQueue.size()).isEqualTo(0);
    }

    @Test
    public void testPeekOrPollEmptyQueue() {
        TaskQueue taskQueue = new TaskQueue();

        // 检查在空队列上调用 peek 和 poll
        try {
            taskQueue.peek();
        } catch (NoSuchElementException e) {
            assertThat(e).hasMessageThat().isEqualTo("Task queue is empty");
        }

        try {
            taskQueue.poll();
        } catch (NoSuchElementException e) {
            assertThat(e).hasMessageThat().isEqualTo("Task queue is empty");
        }
    }
}
