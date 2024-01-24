import java.sql.*;
import java.sql.Date;
import java.time.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;

import com.github.lgooddatepicker.components.*;

public class Utility {

  public static double tax = 0.2; // 20%
  public static double penalty = 0.1; // 10%

  public static boolean isFieldEmpty(JComponent field, String name) {
    boolean isEmpty = false;

    if (field instanceof JTextComponent) {
      isEmpty = ((JTextComponent) field).getText().trim().isEmpty();
    }

    if (field instanceof DatePicker) {
      isEmpty = ((DatePicker) field).getDate() == null;
    }

    if (isEmpty) {
      JOptionPane.showMessageDialog(null, name + " is required",
          "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    return isEmpty;
  }

  public static String[] getTexts(JTextComponent... fields) {
    List<String> texts = new ArrayList<>();

    for (JTextComponent field : fields) {
      texts.add(field.getText());
    }

    return texts.toArray(new String[0]);
  }

  public static void resetFields(JComponent... fields) {
    for (JComponent field : fields) {
      if (field instanceof JTextComponent) {
        ((JTextComponent) field).setText("");
      }
      if (field instanceof JComboBox) {
        ((JComboBox) field).setSelectedIndex(0);
      }
      if (field instanceof DatePicker) {
        ((DatePicker) field).setDateToToday();
      }
    }
  }

  public static Double computeElectricBill(ResultSet rs, boolean includeOtherCharges) {
    try {
      int prev_reading = rs.getInt("previous_reading");
      int pres_reading = rs.getInt("present_reading");
      double unit_rate = rs.getDouble("unit_rate");
      double amount = (pres_reading - prev_reading) * unit_rate;

      if (includeOtherCharges) {
        if (isOverdue(rs)) {
          amount += amount * penalty;
        }
        amount += amount * tax;
      }

      return amount;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static Double computeWaterBill(ResultSet rs) {
    try {
      double basic_charge = rs.getDouble("basic_charge");

      if (isOverdue(rs)) {
        basic_charge += basic_charge * penalty;
      }
      basic_charge += basic_charge * tax;

      return basic_charge;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static Double computeWifiBill(ResultSet rs, boolean includeOtherCharges) {
    try {
      double plan = rs.getInt("plan");
      String period = rs.getString("period");

      if (period.equals(DatabaseClient.BillPeriod.BI_MONTHLY.name())) {
        plan *= 2;
      }

      if (period.equals(DatabaseClient.BillPeriod.QUARTERLY.name())) {
        plan *= 3;
      }

      if (includeOtherCharges) {
        if (isOverdue(rs)) {
          plan += plan * penalty;
        }
        // plan += plan * tax;
      }

      return plan;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static double computePenalty(double bill_amount, boolean isOverdue) {
    return isOverdue ? bill_amount * penalty : 0;
  }

  public static Double computeTax(double bill_amount, double penalty) {
    return (bill_amount + penalty) * tax;
  }

  public static Date getDueDate(ResultSet rs) {
    try {
      LocalDate bill_date = rs.getDate("bill_date").toLocalDate();
      String period = rs.getString("period");

      if (period.equals(DatabaseClient.BillPeriod.MONTHLY.name())) {
        return Date.valueOf(bill_date.plusMonths(1));
      }

      if (period.equals(DatabaseClient.BillPeriod.BI_MONTHLY.name())) {
        return Date.valueOf(bill_date.plusMonths(2));
      }

      if (period.equals(DatabaseClient.BillPeriod.QUARTERLY.name())) {
        return Date.valueOf(bill_date.plusMonths(3));
      }

      return Date.valueOf(bill_date);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static Boolean isOverdue(ResultSet rs) {
    Date current_date = Date.valueOf(LocalDate.now());
    Date due_date = getDueDate(rs);
    return current_date.compareTo(due_date) > 0;
  }

  public static String getStatus(Double amount, Double cash) {
    if (cash == null || cash < amount) {
      return DatabaseClient.BillStatus.UNPAID.name();
    }
    return DatabaseClient.BillStatus.PAID.name();
  }
}
