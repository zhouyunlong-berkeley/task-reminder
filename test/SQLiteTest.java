import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class SQLiteTest {
    public static void main(String[] args) {
        try {
            // 尝试加载驱动
            Class.forName("org.sqlite.JDBC");
            System.out.println("SQLite JDBC 驱动加载成功！");

            // 尝试建立连接
            Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
            System.out.println("成功连接到 SQLite 数据库！");
            conn.close();

        } catch (ClassNotFoundException e) {
            System.out.println("错误：SQLite JDBC 驱动未找到");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("错误：无法连接到数据库");
            e.printStackTrace();
        }
    }
}