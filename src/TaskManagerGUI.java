import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Vector;

/**
 * 任务管理系统的图形用户界面类。
 * 提供任务的创建、编辑、删除和完成功能，并集成任务调度和提醒机制。
 * 此类继承自 JFrame，并使用 Swing 构建界面。
 */
public class TaskManagerGUI extends JFrame {

    /** 任务调度器，用于管理任务的提醒功能。 */
    private final TaskScheduler scheduler;

    /** 显示任务信息的表格。 */
    private final JTable taskTable;

    /** 表格的数据模型，管理任务表格中的数据。 */
    private final DefaultTableModel tableModel;

    /** 日期和时间的格式化器，用于显示任务的时间信息。 */
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /** 数据访问对象，用于与存储的任务数据进行交互。 */
    private final TaskDAO taskDAO;

    /**
     * 构造方法，初始化任务管理系统的图形用户界面。
     * 配置窗口属性，设置表格和按钮的事件逻辑，并加载已保存的任务。
     */
    public TaskManagerGUI() {
        super("任务管理系统");
        this.taskDAO = new TaskDAO();

        // 初始化任务调度器
        scheduler = new TaskScheduler(task -> {
            // 显示系统通知
            displayNotification("任务提醒", "任务 '" + task.getTitle() + "' 需要处理了！");
        });

        // 配置窗口属性
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // 创建并配置主界面
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 初始化任务表格
        String[] columnNames = {"标题", "描述", "优先级", "状态", "截止时间", "提醒时间"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 禁止用户直接编辑表格中的内容
            }
        };
        taskTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(taskTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("添加任务");
        JButton editButton = new JButton("编辑任务");
        JButton completeButton = new JButton("完成任务");
        JButton deleteButton = new JButton("删除任务");

        // 添加按钮到面板
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(completeButton);
        buttonPanel.add(deleteButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 为按钮设置事件监听
        addButton.addActionListener(e -> showAddTaskDialog());
        editButton.addActionListener(e -> handleEditTask());
        completeButton.addActionListener(e -> handleCompleteTask());
        deleteButton.addActionListener(e -> handleDeleteTask());

        add(mainPanel);

        // 加载保存的任务
        loadSavedTasks();
    }

    /**
     * 从数据访问对象加载已保存的任务，并将其添加到调度器和表格中。
     */
    private void loadSavedTasks() {
        List<Task> savedTasks = taskDAO.getAllTasks();
        for (Task task : savedTasks) {
            scheduler.scheduleTask(task); // 添加到任务调度器
            addTaskToTable(task);        // 添加到表格
        }
    }

    /**
     * 显示添加任务的对话框，用于创建新任务。
     */
    private void showAddTaskDialog() {
        // 对话框的实现逻辑（详见代码中的实现）
    }

    /**
     * 创建并返回一个日期时间选择器。
     *
     * @return 日期时间选择器组件。
     */
    private JSpinner createDateTimeSpinner() {
        SpinnerDateModel model = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(model);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "yyyy-MM-dd HH:mm"));
        return spinner;
    }

    /**
     * 从日期时间选择器中获取用户选择的日期时间。
     *
     * @param spinner 日期时间选择器。
     * @return 用户选择的 LocalDateTime。
     */
    private LocalDateTime getDateTimeFromSpinner(JSpinner spinner) {
        return LocalDateTime.ofInstant(
                ((java.util.Date) spinner.getValue()).toInstant(),
                java.time.ZoneId.systemDefault()
        );
    }

    /**
     * 将任务添加到表格中。
     *
     * @param task 要添加的任务对象。
     */
    private void addTaskToTable(Task task) {
        Vector<String> row = new Vector<>();
        row.add(task.getTitle());
        row.add(task.getDescription());
        row.add(task.getPriority().toString());
        row.add(task.getStatus().toString());
        row.add(task.getDueDateTime().format(dateFormatter));
        row.add(task.getReminderTime().format(dateFormatter));
        tableModel.addRow(row);
    }

    /**
     * 显示编辑任务的对话框。
     *
     * @param row 被选中的任务行号。
     */
    private void showEditTaskDialog(int row) {
        // TODO: 实现编辑功能的对话框
    }

    /**
     * 显示系统通知或对话框，用于任务提醒。
     *
     * @param title 通知的标题。
     * @param message 通知的内容。
     */
    private void displayNotification(String title, String message) {
        // 系统托盘或弹出对话框的实现
    }

    /**
     * 处理编辑任务的操作逻辑。
     */
    private void handleEditTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            showEditTaskDialog(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "请选择要编辑的任务");
        }
    }

    /**
     * 处理完成任务的操作逻辑。
     */
    private void handleCompleteTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            // TODO: 实现完成任务的逻辑
        } else {
            JOptionPane.showMessageDialog(this, "请选择要完成的任务");
        }
    }

    /**
     * 处理删除任务的操作逻辑。
     */
    private void handleDeleteTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            // TODO: 实现删除任务的逻辑
        } else {
            JOptionPane.showMessageDialog(this, "请选择要删除的任务");
        }
    }
}
