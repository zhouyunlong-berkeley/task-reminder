/**
 * TaskManagementApp 是任务管理系统的主类。
 * 它包含程序入口，负责启动任务管理图形界面。
 */
public class TaskManagementApp {
    public static void main(String[] args) {
        // 使用 SwingUtilities.invokeLater 确保 GUI 在事件调度线程中创建和显示
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                // 设置系统默认的界面风格
                javax.swing.UIManager.setLookAndFeel(
                        javax.swing.UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception e) {
                e.printStackTrace(); // 如果设置界面风格失败，打印错误
            }

            // 创建并显示主窗口
            TaskManagerGUI mainWindow = new TaskManagerGUI();
            mainWindow.setVisible(true);
        });
    }
}
