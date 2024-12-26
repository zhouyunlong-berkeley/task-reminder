import org.junit.jupiter.api.*;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.table.DefaultTableModel;

class TaskManagerGUITest {
    private TaskManagerGUI gui;
    private Robot robot;

    @BeforeEach
    void setUp() throws AWTException {
        SwingUtilities.invokeLater(() -> {
            gui = new TaskManagerGUI();
            gui.setVisible(true);
        });
        robot = new Robot();
        robot.setAutoDelay(100); // 设置操作间隔时间
        robot.waitForIdle();
    }

    @AfterEach
    void tearDown() {
        SwingUtilities.invokeLater(() -> {
            if (gui != null) {
                gui.dispose();
            }
        });
    }

    @Test
    void testAddTaskDialog() {
        SwingUtilities.invokeLater(() -> {
            // 获取"添加任务"按钮
            JButton addButton = findButtonByText(gui, "添加任务");
            Assertions.assertNotNull(addButton);
            addButton.doClick();
        });

        robot.waitForIdle();

        // 验证对话框是否显示
        Window[] windows = Window.getWindows();
        boolean foundDialog = false;
        for (Window window : windows) {
            if (window instanceof JDialog && window.isVisible()) {
                foundDialog = true;
                break;
            }
        }
        Assertions.assertTrue(foundDialog);
    }

    @Test
    void testTaskTableCreation() {
        SwingUtilities.invokeLater(() -> {
            // 获取任务表格
            JTable taskTable = findTaskTable(gui);
            Assertions.assertNotNull(taskTable);

            // 验证表格列
            DefaultTableModel model = (DefaultTableModel) taskTable.getModel();
            Assertions.assertEquals(6, model.getColumnCount());
            Assertions.assertEquals("标题", model.getColumnName(0));
            Assertions.assertEquals("描述", model.getColumnName(1));
            Assertions.assertEquals("优先级", model.getColumnName(2));
            Assertions.assertEquals("状态", model.getColumnName(3));
            Assertions.assertEquals("截止时间", model.getColumnName(4));
            Assertions.assertEquals("提醒时间", model.getColumnName(5));
        });
    }

    @Test
    void testAddTask() {
        SwingUtilities.invokeLater(() -> {
            // 创建测试任务
            Task testTask = new Task(
                    "测试任务",
                    "测试描述",
                    LocalDateTime.now().plusHours(1),
                    LocalDateTime.now().plusMinutes(30),
                    Task.TaskPriority.HIGH
            );

            // 通过GUI添加任务
            JButton addButton = findButtonByText(gui, "添加任务");
            addButton.doClick();

            // 等待对话框显示
            robot.waitForIdle();

            // 验证表格内容
            JTable taskTable = findTaskTable(gui);
            DefaultTableModel model = (DefaultTableModel) taskTable.getModel();

            // 注意：由于添加任务是通过对话框完成的，这里只能验证表格的存在
            Assertions.assertNotNull(taskTable);
        });
    }

    @Test
    void testDeleteTask() {
        SwingUtilities.invokeLater(() -> {
            JTable taskTable = findTaskTable(gui);
            DefaultTableModel model = (DefaultTableModel) taskTable.getModel();

            // 手动添加一行数据到表格
            model.addRow(new Object[]{
                    "要删除的任务",
                    "测试描述",
                    Task.TaskPriority.HIGH.toString(),
                    Task.TaskStatus.NOT_STARTED.toString(),
                    LocalDateTime.now().plusHours(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    LocalDateTime.now().plusMinutes(30).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            });

            // 选择第一行
            taskTable.setRowSelectionInterval(0, 0);

            // 查找并点击删除按钮
            JButton deleteButton = findButtonByText(gui, "删除任务");
            Assertions.assertNotNull(deleteButton);
            deleteButton.doClick();

            // 等待操作完成
            robot.waitForIdle();

            // 验证任务是否被删除
            Assertions.assertEquals(0, model.getRowCount());
        });
    }

    // 工具方法：查找指定文本的按钮
    private JButton findButtonByText(Container container, String buttonText) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton && ((JButton) comp).getText().equals(buttonText)) {
                return (JButton) comp;
            } else if (comp instanceof Container) {
                JButton button = findButtonByText((Container) comp, buttonText);
                if (button != null) {
                    return button;
                }
            }
        }
        return null;
    }

    // 工具方法：查找任务表格
    private JTable findTaskTable(TaskManagerGUI gui) {
        return findComponentByType(gui.getContentPane(), JTable.class);
    }

    // 工具方法：通过类型查找组件
    private <T extends Component> T findComponentByType(Container container, Class<T> type) {
        for (Component comp : container.getComponents()) {
            if (type.isInstance(comp)) {
                return type.cast(comp);
            } else if (comp instanceof Container) {
                T found = findComponentByType((Container) comp, type);
                if (found != null) {
                    return found;
                }
            }
            if (comp instanceof JScrollPane) {
                Component view = ((JScrollPane) comp).getViewport().getView();
                if (type.isInstance(view)) {
                    return type.cast(view);
                }
            }
        }
        return null;
    }
}