import java.sql.*;

public class DatabaseClient {

  public enum UserRole {
    USER,
    ADMIN
  }

  public enum BillCategory {
    ELECTRIC,
    WATER,
    WIFI
  }

  public enum BillPeriod {
    MONTHLY,
    BI_MONTHLY,
    QUARTERLY
  }

  public enum BillStatus {
    UNPAID,
    PAID
  }

  private DatabaseConfig databaseConfig = DatabaseConfig.get();

  private static DatabaseClient instance;

  public static DatabaseClient get() {
    if (instance == null) {
      instance = new DatabaseClient();
    }
    return instance;
  }

  public ResultSet getUsers(UserRole role) {
    try {
      Connection conn = databaseConfig.connection();
      PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE `role`=?");
      stmt.setString(1, role.name());

      ResultSet rs = stmt.executeQuery();

      // conn.close();
      // stmt.close();

      return rs;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public ResultSet getUser(String username) {
    try {
      Connection conn = databaseConfig.connection();
      PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username=?");
      stmt.setString(1, username);

      ResultSet rs = stmt.executeQuery();

      // conn.close();
      // stmt.close();

      return rs;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public ResultSet getUser(String... values) {
    try {
      Connection conn = databaseConfig.connection();
      PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
      for (int i = 0; i < 2; i++) {
        stmt.setString(i + 1, values[i]);
      }

      ResultSet rs = stmt.executeQuery();

      // conn.close();
      // stmt.close();

      return rs;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public Integer createUser(String... values) {
    try {
      Connection conn = databaseConfig.connection();
      PreparedStatement stmt = conn.prepareStatement(
          "INSERT INTO users (username, password, email, first_name, last_name, phone_number, address) VALUES (?, ?, ?, ?, ?, ?, ?)");
      for (int i = 0; i < 7; i++) {
        stmt.setString(i + 1, values[i]);
      }

      int count = stmt.executeUpdate();

      // conn.close();
      // stmt.close();

      return count;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public Integer updateUser(int id, String... values) {
    try {
      Connection conn = databaseConfig.connection();
      PreparedStatement stmt = conn.prepareStatement(
          "UPDATE users SET username=?, password=?, email=?, first_name=?, last_name=?, phone_number=?, address=? WHERE id=?");
      for (int i = 0; i < 7; i++) {
        stmt.setString(i + 1, values[i]);
      }
      stmt.setInt(8, id);

      int count = stmt.executeUpdate();

      // conn.close();
      // stmt.close();

      return count;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public Integer changeUserPassword(String... values) {
    try {
      Connection conn = databaseConfig.connection();
      PreparedStatement stmt = conn.prepareStatement(
          "UPDATE users SET password=? WHERE username=?");
      for (int i = 0; i < 2; i++) {
        stmt.setString(i + 1, values[i]);
      }

      int count = stmt.executeUpdate();

      // conn.close();
      // stmt.close();

      return count;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public Integer deleteUser(int id) {
    try {
      Connection conn = databaseConfig.connection();
      PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id=?");
      stmt.setInt(1, id);

      int count = stmt.executeUpdate();

      // conn.close();
      // stmt.close();

      return count;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public ResultSet getBills(BillCategory category, String username) {
    try {
      Connection conn = databaseConfig.connection();
      PreparedStatement stmt = conn.prepareStatement(
          "SELECT * FROM " + category.name().toLowerCase() + "_bills WHERE username=?");
      stmt.setString(1, username);

      ResultSet rs = stmt.executeQuery();

      // conn.close();
      // stmt.close();

      return rs;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public ResultSet getBill(BillCategory category, int id) {
    try {
      Connection conn = databaseConfig.connection();
      PreparedStatement stmt = conn.prepareStatement(
          "SELECT * FROM " + category.name().toLowerCase() + "_bills WHERE id=?");
      stmt.setInt(1, id);

      ResultSet rs = stmt.executeQuery();

      // conn.close();
      // stmt.close();

      return rs;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public Integer createElectricBill(String username, int previous_reading, int present_reading,
      double unit_rate, BillPeriod period, Date date) {
    try {
      Connection conn = databaseConfig.connection();
      PreparedStatement stmt = conn.prepareStatement(
          "INSERT INTO electric_bills (previous_reading, present_reading, unit_rate, period, bill_date, username) VALUES (?, ?, ?, ?, ?, ?)");
      stmt.setInt(1, previous_reading);
      stmt.setInt(2, present_reading);
      stmt.setDouble(3, unit_rate);
      stmt.setString(4, period.name());
      stmt.setDate(5, date);
      stmt.setString(6, username);

      int count = stmt.executeUpdate();

      // conn.close();
      // stmt.close();

      return count;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public Integer createWaterBill(String username, int previous_reading, int present_reading,
      double basic_charge, BillPeriod period, Date date) {
    try {
      Connection conn = databaseConfig.connection();
      PreparedStatement stmt = conn.prepareStatement(
          "INSERT INTO water_bills (previous_reading, present_reading, basic_charge, period, bill_date, username) VALUES (?, ?, ?, ?, ?, ?)");
      stmt.setInt(1, previous_reading);
      stmt.setInt(2, present_reading);
      stmt.setDouble(3, basic_charge);
      stmt.setString(4, period.name());
      stmt.setDate(5, date);
      stmt.setString(6, username);

      int count = stmt.executeUpdate();

      // conn.close();
      // stmt.close();

      return count;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public Integer createWifiBill(String username, String provider, int plan, BillPeriod period, Date date) {
    try {
      Connection conn = databaseConfig.connection();
      PreparedStatement stmt = conn.prepareStatement(
          "INSERT INTO wifi_bills (provider, plan, period, bill_date, username) VALUES (?, ?, ?, ?, ?)");
      stmt.setString(1, provider);
      stmt.setInt(2, plan);
      stmt.setString(3, period.name());
      stmt.setDate(4, date);
      stmt.setString(5, username);

      int count = stmt.executeUpdate();

      // conn.close();
      // stmt.close();

      return count;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public Integer updateElectricBill(int id, int previous_reading, int present_reading,
      double unit_rate, BillPeriod period, Date date) {
    try {
      Connection conn = databaseConfig.connection();
      PreparedStatement stmt = conn.prepareStatement(
          "UPDATE electric_bills SET previous_reading=?, present_reading=?, unit_rate=?, period=?, bill_date=? WHERE id=?");
      stmt.setInt(1, previous_reading);
      stmt.setInt(2, present_reading);
      stmt.setDouble(3, unit_rate);
      stmt.setString(4, period.name());
      stmt.setDate(5, date);
      stmt.setInt(6, id);

      int count = stmt.executeUpdate();

      // conn.close();
      // stmt.close();

      return count;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public Integer updateWaterBill(int id, int previous_reading, int present_reading,
      double basic_charge, BillPeriod period, Date date) {
    try {
      Connection conn = databaseConfig.connection();
      PreparedStatement stmt = conn.prepareStatement(
          "UPDATE water_bills SET previous_reading=?, present_reading=?, basic_charge=?, period=?, bill_date=? WHERE id=?");
      stmt.setInt(1, previous_reading);
      stmt.setInt(2, present_reading);
      stmt.setDouble(3, basic_charge);
      stmt.setString(4, period.name());
      stmt.setDate(5, date);
      stmt.setInt(6, id);

      int count = stmt.executeUpdate();

      // conn.close();
      // stmt.close();

      return count;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public Integer updateWifiBill(int id, String provider, int plan, BillPeriod period, Date date) {
    try {
      Connection conn = databaseConfig.connection();
      PreparedStatement stmt = conn.prepareStatement(
          "UPDATE wifi_bills SET provider=?, plan=?, period=?, bill_date=? WHERE id=?");
      stmt.setString(1, provider);
      stmt.setInt(2, plan);
      stmt.setString(3, period.name());
      stmt.setDate(4, date);
      stmt.setInt(5, id);

      int count = stmt.executeUpdate();

      // conn.close();
      // stmt.close();

      return count;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public Integer payBill(BillCategory category, double cash, int id) {
    try {
      Connection conn = databaseConfig.connection();
      PreparedStatement stmt = conn.prepareStatement(
          "UPDATE " + category.name().toLowerCase() + "_bills SET cash=? WHERE id=?");
      stmt.setDouble(1, cash);
      stmt.setInt(2, id);

      int count = stmt.executeUpdate();

      // conn.close();
      // stmt.close();

      return count;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public Integer deleteBill(BillCategory category, int id) {
    try {
      Connection conn = databaseConfig.connection();
      PreparedStatement stmt = conn.prepareStatement(
          "DELETE FROM " + category.name().toLowerCase() + "_bills WHERE id=?");
      stmt.setInt(1, id);

      int count = stmt.executeUpdate();

      // conn.close();
      // stmt.close();

      return count;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }
}
