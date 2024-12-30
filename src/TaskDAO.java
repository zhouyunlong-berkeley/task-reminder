import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * TaskDAO is responsible for managing database operations related to tasks.
 * It supports CRUD operations for task storage and retrieval.
 */
public class TaskDAO {

    /**
     * The database connection URL.
     */
    private static final String DB_URL = "jdbc:sqlite:tasks.db";

    /**
     * Static block to initialize the database by creating the tasks table if it does not exist.
     */
    static {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            createTaskTable(conn);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    /**
     * Creates the tasks table in the database if it does not already exist.
     *
     * @param conn the database connection.
     * @throws SQLException if a database access error occurs.
     */
    private static void createTaskTable(Connection conn) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS tasks (
                id TEXT PRIMARY KEY,
                title TEXT NOT NULL,
                description TEXT,
                due_date_time TEXT NOT NULL,
                reminder_time TEXT NOT NULL,
                priority TEXT NOT NULL,
                status TEXT NOT NULL,
                created_time TEXT NOT NULL,
                last_modified_time TEXT NOT NULL
            )
            """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    /**
     * Saves a new task to the database.
     *
     * @param task the task to be saved.
     */
    public void saveTask(Task task) {
        String sql = """
            INSERT INTO tasks (id, title, description, due_date_time, reminder_time,
                             priority, status, created_time, last_modified_time)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, task.getId());
            pstmt.setString(2, task.getTitle());
            pstmt.setString(3, task.getDescription());
            pstmt.setString(4, task.getDueDateTime().toString());
            pstmt.setString(5, task.getReminderTime().toString());
            pstmt.setString(6, task.getPriority().toString());
            pstmt.setString(7, task.getStatus().toString());
            pstmt.setString(8, task.getCreatedTime().toString());
            pstmt.setString(9, task.getLastModifiedTime().toString());

            pstmt.executeUpdate();
            System.out.println("Task saved successfully: " + task.getId());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save task", e);
        }
    }

    /**
     * Updates an existing task in the database.
     *
     * @param task the task to be updated.
     */
    public void updateTask(Task task) {
        String sql = """
            UPDATE tasks 
            SET title = ?, description = ?, due_date_time = ?, reminder_time = ?,
                priority = ?, status = ?, last_modified_time = ?
            WHERE id = ?
            """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getDueDateTime().toString());
            pstmt.setString(4, task.getReminderTime().toString());
            pstmt.setString(5, task.getPriority().toString());
            pstmt.setString(6, task.getStatus().toString());
            pstmt.setString(7, task.getLastModifiedTime().toString());
            pstmt.setString(8, task.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update task", e);
        }
    }

    /**
     * Deletes a task from the database by its ID.
     *
     * @param taskId the ID of the task to be deleted.
     */
    public void deleteTask(String taskId) {
        String sql = "DELETE FROM tasks WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, taskId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete task", e);
        }
    }

    /**
     * Retrieves all tasks from the database.
     *
     * @return a list of all tasks.
     */
    public List<Task> getAllTasks() {
        String sql = "SELECT * FROM tasks";
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tasks.add(createTaskFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch tasks", e);
        }

        return tasks;
    }

    /**
     * Retrieves a task from the database by its ID.
     *
     * @param taskId the ID of the task to retrieve.
     * @return the task with the specified ID, or null if no such task exists.
     */
    public Task getTaskById(String taskId) {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        System.out.println("Executing SQL: " + sql);
        System.out.println("Task ID: " + taskId);
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, taskId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Task task = createTaskFromResultSet(rs);
                    System.out.println("Task found: " + task);
                    return task;
                } else {
                    System.out.println("No task found with ID: " + taskId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a Task object from a ResultSet.
     *
     * @param rs the ResultSet containing task data.
     * @return a Task object with data from the ResultSet.
     * @throws SQLException if a database access error occurs.
     */
    private Task createTaskFromResultSet(ResultSet rs) throws SQLException {
        Task task = new Task(
                rs.getString("title"),
                rs.getString("description"),
                LocalDateTime.parse(rs.getString("due_date_time")),
                LocalDateTime.parse(rs.getString("reminder_time")),
                Task.TaskPriority.valueOf(rs.getString("priority"))
        );

        task.setStatus(Task.TaskStatus.valueOf(rs.getString("status")));
        return task;
    }
}
