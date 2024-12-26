import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

class TaskSchedulerTest {
    private TaskScheduler scheduler;
    private TestReminderHandler reminderHandler;

    // 测试用的提醒处理器
    private static class TestReminderHandler implements TaskScheduler.TaskReminderHandler {
        private Task lastRemindedTask;
        private CountDownLatch latch;

        public TestReminderHandler() {
            this.latch = new CountDownLatch(1);
        }

        @Override
        public void onTaskReminder(Task task) {
            this.lastRemindedTask = task;
            latch.countDown();
        }

        public Task getLastRemindedTask() {
            return lastRemindedTask;
        }

        public void resetLatch() {
            this.latch = new CountDownLatch(1);
        }

        public boolean waitForReminder(long timeout, TimeUnit unit) throws InterruptedException {
            return latch.await(timeout, unit);
        }
    }

    @BeforeEach
    void setUp() {
        reminderHandler = new TestReminderHandler();
        scheduler = new TaskScheduler(reminderHandler);
    }

    @AfterEach
    void tearDown() {
        scheduler.shutdown();
    }

    @Test
    void testScheduleTask() {
        Task task = createTestTask("Test Task", Task.TaskPriority.HIGH);
        scheduler.scheduleTask(task);

        Task nextTask = scheduler.getNextPendingTask();
        Assertions.assertEquals(task, nextTask);
    }

    @Test
    void testTaskReminder() throws InterruptedException {
        // 创建一个5秒后提醒的任务
        Task task = new Task(
                "Quick Reminder",
                "Test Description",
                LocalDateTime.now().plusMinutes(10),
                LocalDateTime.now().plusSeconds(2),
                Task.TaskPriority.HIGH
        );

        scheduler.scheduleTask(task);

        // 等待提醒触发
        boolean reminded = reminderHandler.waitForReminder(5, TimeUnit.SECONDS);
        Assertions.assertTrue(reminded);
        Assertions.assertEquals(task, reminderHandler.getLastRemindedTask());
    }

    @Test
    void testCancelReminder() {
        Task task = createTestTask("Cancel Test", Task.TaskPriority.MEDIUM);
        scheduler.scheduleTask(task);
        scheduler.cancelReminder(task);

        // 验证任务仍在队列中
        Assertions.assertEquals(task, scheduler.getNextPendingTask());
    }

    @Test
    void testCompleteTask() {
        Task task = createTestTask("Complete Test", Task.TaskPriority.LOW);
        scheduler.scheduleTask(task);
        scheduler.completeTask(task);

        // 验证任务已从队列中移除
        Assertions.assertThrows(NoSuchElementException.class, () -> scheduler.getNextPendingTask());
    }

    @Test
    void testRescheduleTask() throws InterruptedException {
        LocalDateTime originalReminder = LocalDateTime.now().plusSeconds(2);
        LocalDateTime newReminder = LocalDateTime.now().plusSeconds(4);

        Task task = new Task(
                "Reschedule Test",
                "Test Description",
                LocalDateTime.now().plusMinutes(10),
                originalReminder,
                Task.TaskPriority.HIGH
        );

        scheduler.scheduleTask(task);
        scheduler.rescheduleTask(task, newReminder);

        // 等待原始提醒时间
        Thread.sleep(2500);
        Assertions.assertNull(reminderHandler.getLastRemindedTask());

        // 等待新的提醒时间
        boolean reminded = reminderHandler.waitForReminder(3, TimeUnit.SECONDS);
        Assertions.assertTrue(reminded);
        Assertions.assertEquals(task, reminderHandler.getLastRemindedTask());
    }

    @Test
    void testTaskStatusUpdate() {
        Task task = new Task(
                "Overdue Test",
                "Test Description",
                LocalDateTime.now().minusMinutes(1), // 已过期
                LocalDateTime.now().plusSeconds(1),
                Task.TaskPriority.HIGH
        );

        scheduler.scheduleTask(task);
        Assertions.assertEquals(Task.TaskStatus.OVERDUE, task.getStatus());
    }

    private Task createTestTask(String title, Task.TaskPriority priority) {
        return new Task(
                title,
                "Test Description",
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusMinutes(30),
                priority
        );
    }
}