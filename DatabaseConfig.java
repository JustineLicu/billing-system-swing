import java.sql.*;

public class DatabaseConfig {

  private String DB_URL = "jdbc:mysql://jus@localhost:3306/bs1";
  private Connection connection;

  private static DatabaseConfig instance;

  public static DatabaseConfig get() {
    if (instance == null) {
      instance = new DatabaseConfig();
    }
    return instance;
  }

  public Connection connection() {
    try {
      if (connection == null || connection.isClosed()) {
        connection = DriverManager.getConnection(DB_URL);
      }
      return connection;
    } catch (SQLException e) {
      e.printStackTrace();
      return connection;
    }
  }

  public DatabaseConfig() {
    try {
      Connection conn = connection();
      Statement stmt = conn.createStatement();

      stmt.execute("CREATE TABLE IF NOT EXISTS `users` (\r\n" + //
          "    `id` INTEGER NOT NULL AUTO_INCREMENT,\r\n" + //
          "    `username` VARCHAR(191) NOT NULL,\r\n" + //
          "    `password` VARCHAR(191) NOT NULL,\r\n" + //
          "    `email` VARCHAR(191) NOT NULL,\r\n" + //
          "    `first_name` VARCHAR(191) NOT NULL,\r\n" + //
          "    `last_name` VARCHAR(191) NOT NULL,\r\n" + //
          "    `phone_number` VARCHAR(191) NOT NULL,\r\n" + //
          "    `address` VARCHAR(191) NOT NULL,\r\n" + //
          "    `role` ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',\r\n" + //
          "\r\n" + //
          "    UNIQUE INDEX `users_username_key`(`username`),\r\n" + //
          "    PRIMARY KEY (`id`)\r\n" + //
          ") DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");

      stmt.execute("CREATE TABLE IF NOT EXISTS `electric_bills` (\r\n" + //
          "    `id` INTEGER NOT NULL AUTO_INCREMENT,\r\n" + //
          "    `previous_reading` INTEGER NOT NULL,\r\n" + //
          "    `present_reading` INTEGER NOT NULL,\r\n" + //
          "    `unit_rate` DOUBLE NOT NULL,\r\n" + //
          "    `cash` DOUBLE NULL,\r\n" + //
          "    `period` ENUM('MONTHLY', 'BI_MONTHLY', 'QUARTERLY') NOT NULL DEFAULT 'MONTHLY',\r\n" + //
          "    `bill_date` DATETIME(3) NOT NULL,\r\n" + //
          "    `username` VARCHAR(191) NOT NULL,\r\n" + //
          "\r\n" + //
          "    PRIMARY KEY (`id`)\r\n" + //
          ") DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");

      stmt.execute("CREATE TABLE IF NOT EXISTS `water_bills` (\r\n" + //
          "    `id` INTEGER NOT NULL AUTO_INCREMENT,\r\n" + //
          "    `previous_reading` INTEGER NOT NULL,\r\n" + //
          "    `present_reading` INTEGER NOT NULL,\r\n" + //
          "    `basic_charge` DOUBLE NOT NULL,\r\n" + //
          "    `cash` DOUBLE NULL,\r\n" + //
          "    `period` ENUM('MONTHLY', 'BI_MONTHLY', 'QUARTERLY') NOT NULL DEFAULT 'MONTHLY',\r\n" + //
          "    `bill_date` DATETIME(3) NOT NULL,\r\n" + //
          "    `username` VARCHAR(191) NOT NULL,\r\n" + //
          "\r\n" + //
          "    PRIMARY KEY (`id`)\r\n" + //
          ") DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");

      stmt.execute("CREATE TABLE IF NOT EXISTS `wifi_bills` (\r\n" + //
          "    `id` INTEGER NOT NULL AUTO_INCREMENT,\r\n" + //
          "    `provider` VARCHAR(191) NOT NULL,\r\n" + //
          "    `plan` INTEGER NOT NULL,\r\n" + //
          "    `cash` DOUBLE NULL,\r\n" + //
          "    `period` ENUM('MONTHLY', 'BI_MONTHLY', 'QUARTERLY') NOT NULL DEFAULT 'MONTHLY',\r\n" + //
          "    `bill_date` DATETIME(3) NOT NULL,\r\n" + //
          "    `username` VARCHAR(191) NOT NULL,\r\n" + //
          "\r\n" + //
          "    PRIMARY KEY (`id`)\r\n" + //
          ") DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");

      try {
        stmt.execute(
            "ALTER TABLE `electric_bills` ADD CONSTRAINT `electric_bills_username_fkey` FOREIGN KEY (`username`) REFERENCES `users`(`username`) ON DELETE CASCADE ON UPDATE CASCADE");
      } catch (SQLException e) {
        System.out.println("Constraint already exists");
      }

      try {
        stmt.execute(
            "ALTER TABLE `water_bills` ADD CONSTRAINT `water_bills_username_fkey` FOREIGN KEY (`username`) REFERENCES `users`(`username`) ON DELETE CASCADE ON UPDATE CASCADE");
      } catch (SQLException e) {
        System.out.println("Constraint already exists");
      }

      try {
        stmt.execute(
            "ALTER TABLE `wifi_bills` ADD CONSTRAINT `wifi_bills_username_fkey` FOREIGN KEY (`username`) REFERENCES `users`(`username`) ON DELETE CASCADE ON UPDATE CASCADE");
      } catch (SQLException e) {
        System.out.println("Constraint already exists");
      }

      stmt.executeUpdate("INSERT INTO `users` (`username`, `password`, `email`,\r\n" + //
          "`first_name`, `last_name`, `phone_number`, `address`, `role`) \r\n" + //
          "VALUES ('admin', 'password', 'admin@example.com', 'Admin', 'User',\r\n" + //
          "'09223456789', 'Here & There St.', 'ADMIN')\r\n" + //
          "ON DUPLICATE KEY UPDATE\r\n" + //
          "   `username` = VALUES(`username`),\r\n" + //
          "   `email` = VALUES(`email`),\r\n" + //
          "   `first_name` = VALUES(`first_name`),\r\n" + //
          "   `last_name` = VALUES(`last_name`),\r\n" + //
          "   `phone_number` = VALUES(`phone_number`),\r\n" + //
          "   `address` = VALUES(`address`),\r\n" + //
          "   `role` = VALUES(`role`)");

      stmt.executeUpdate("INSERT INTO `users` (`username`, `password`, `email`,\r\n" + //
          "`first_name`, `last_name`, `phone_number`, `address`) \r\n" + //
          "VALUES ('johndog', 'password', 'johndog@example.com', 'John', 'Dog',\r\n" + //
          "'09223456790', 'Here St.')\r\n" + //
          "ON DUPLICATE KEY UPDATE\r\n" + //
          "   `username` = VALUES(`username`),\r\n" + //
          "   `email` = VALUES(`email`),\r\n" + //
          "   `first_name` = VALUES(`first_name`),\r\n" + //
          "   `last_name` = VALUES(`last_name`),\r\n" + //
          "   `phone_number` = VALUES(`phone_number`),\r\n" + //
          "   `address` = VALUES(`address`)");

      stmt.executeUpdate("INSERT INTO `users` (`username`, `password`, `email`,\r\n" + //
          "`first_name`, `last_name`, `phone_number`, `address`) \r\n" + //
          "VALUES ('user2', 'password', 'user2@example.com', 'User', 'Two',\r\n" + //
          "'09223456791', 'There St.')\r\n" + //
          "ON DUPLICATE KEY UPDATE\r\n" + //
          "   `username` = VALUES(`username`),\r\n" + //
          "   `email` = VALUES(`email`),\r\n" + //
          "   `first_name` = VALUES(`first_name`),\r\n" + //
          "   `last_name` = VALUES(`last_name`),\r\n" + //
          "   `phone_number` = VALUES(`phone_number`),\r\n" + //
          "   `address` = VALUES(`address`)");

      // Close the connections
      conn.close();
      stmt.close();

      System.out.println("Database migrated successfully");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
