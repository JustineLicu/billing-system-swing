import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ConsumerDashboard {

  private DatabaseClient db = DatabaseClient.get();

  public String user;

  public JFrame frame = new JFrame("Consumer Dashboard | Billing System");

  public JLabel heading_lbl = new JLabel("Consumer Dashboard");
  public JLabel bills_lbl = new JLabel("BILLS:");

  public String[][] bill_tr = {};
  public String[] bill_tc = { "Category", "ID", "Amount", "Bill Date", "Due Date", "Status" };
  public DefaultTableModel bill_dtm = new DefaultTableModel(bill_tr, bill_tc);
  public JTable bill_table = new JTable(bill_dtm);
  public JScrollPane bill_tsp = new JScrollPane(bill_table);

  public JButton changepass_btn = new JButton("Change Password");
  public JButton logout_btn = new JButton("Logout");
  public JButton refresh_btn = new JButton("Refresh");
  public JButton paybill_btn = new JButton("Pay Bill");

  private static ConsumerDashboard instance;

  public static ConsumerDashboard get() {
    if (instance == null) {
      instance = new ConsumerDashboard();
    }
    return instance;
  }

  public ConsumerDashboard() {
    frame.setBounds(70, 70, 650, 700);
    frame.setLocationRelativeTo(null);
    frame.setLayout(null);

    frame.add(heading_lbl);
    frame.add(bills_lbl);
    frame.add(bill_tsp);
    frame.add(changepass_btn);
    frame.add(logout_btn);
    frame.add(refresh_btn);
    frame.add(paybill_btn);

    addActionListeners();

    frame.getContentPane().setBackground(new Color(0xe7f5fe));

    heading_lbl.setFont(new Font(heading_lbl.getFont().getName(), heading_lbl.getFont().getStyle(), 24));
    bills_lbl.setFont(new Font(bills_lbl.getFont().getName(), bills_lbl.getFont().getStyle(), 22));

    heading_lbl.setBounds(140, 30, 300, 40);
    bills_lbl.setBounds(10, 100, 80, 30);

    bill_tsp.setBounds(10, 140, 550, 300);

    changepass_btn.setBounds(280, 80, 170, 50);
    logout_btn.setBounds(460, 80, 100, 50);
    refresh_btn.setBounds(10, 450, 100, 50);
    paybill_btn.setBounds(460, 450, 100, 50);
  }

  public void addActionListeners() {

    changepass_btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ChangeUserPasswordForm.get().render(user);
        PayElectricBill.get().cleanUp();
        PayWaterBill.get().cleanUp();
        PayWifiBill.get().cleanUp();
      }
    });

    logout_btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int isSure = JOptionPane.showConfirmDialog(null,
            "Are you absolutely sure?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);
        if (isSure != JOptionPane.YES_OPTION) {
          return;
        }

        cleanUp();
        LoginForm.get().render();
      }
    });

    refresh_btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        renderBillTable(user);
      }
    });

    paybill_btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int selectedRowIdx = bill_table.getSelectedRow();
        if (selectedRowIdx < 0) {
          JOptionPane.showMessageDialog(null, "No selected row",
              "Bill Update Error", JOptionPane.ERROR_MESSAGE);
          return;
        }

        String category = bill_table.getValueAt(selectedRowIdx, 0).toString();
        int id = Integer.parseInt(bill_table.getValueAt(selectedRowIdx, 1).toString());

        if (category.equals(DatabaseClient.BillCategory.ELECTRIC.name())) {
          ChangeUserPasswordForm.get().cleanUp();
          PayElectricBill.get().render(id);
          PayWaterBill.get().cleanUp();
          PayWifiBill.get().cleanUp();
          return;
        }

        if (category.equals(DatabaseClient.BillCategory.WATER.name())) {
          ChangeUserPasswordForm.get().cleanUp();
          PayElectricBill.get().cleanUp();
          PayWaterBill.get().render(id);
          PayWifiBill.get().cleanUp();
          return;
        }

        if (category.equals(DatabaseClient.BillCategory.WIFI.name())) {
          ChangeUserPasswordForm.get().cleanUp();
          PayElectricBill.get().cleanUp();
          PayWaterBill.get().cleanUp();
          PayWifiBill.get().render(id);
          return;
        }
      }
    });
  }

  public void renderBillTable(String username) {
    try {
      ResultSet electricRS = db.getBills(DatabaseClient.BillCategory.ELECTRIC, username);
      ResultSet waterRS = db.getBills(DatabaseClient.BillCategory.WATER, username);
      ResultSet wifiRS = db.getBills(DatabaseClient.BillCategory.WIFI, username);

      bill_dtm.setRowCount(0);

      while (electricRS.next()) {
        // { "Category", "Amount", "Bill Date", "Due Date", "Status" };
        String category = DatabaseClient.BillCategory.ELECTRIC.name();
        int id = electricRS.getInt("id");
        double amount = Utility.computeElectricBill(electricRS, true);
        Date bill_date = electricRS.getDate("bill_date");
        Date due_date = Utility.getDueDate(electricRS);
        String status = Utility.getStatus(amount, electricRS.getDouble("cash"));

        String[] row = { category, String.valueOf(id), String.valueOf(amount), bill_date.toString(),
            due_date.toString(), status };
        bill_dtm.addRow(row);
      }

      while (waterRS.next()) {
        // { "Category", "Amount", "Bill Date", "Due Date", "Status" };
        String category = DatabaseClient.BillCategory.WATER.name();
        int id = waterRS.getInt("id");
        double amount = Utility.computeWaterBill(waterRS);
        Date bill_date = waterRS.getDate("bill_date");
        Date due_date = Utility.getDueDate(waterRS);
        String status = Utility.getStatus(amount, waterRS.getDouble("cash"));

        String[] row = { category, String.valueOf(id), String.valueOf(amount), bill_date.toString(),
            due_date.toString(), status };
        bill_dtm.addRow(row);
      }

      while (wifiRS.next()) {
        // { "Category", "Amount", "Bill Date", "Due Date", "Status" };
        String category = DatabaseClient.BillCategory.WIFI.name();
        int id = wifiRS.getInt("id");
        double amount = Utility.computeWifiBill(wifiRS, true);
        Date bill_date = wifiRS.getDate("bill_date");
        Date due_date = Utility.getDueDate(wifiRS);
        String status = Utility.getStatus(amount, wifiRS.getDouble("cash"));

        String[] row = { category, String.valueOf(id), String.valueOf(amount), bill_date.toString(),
            due_date.toString(), status };
        bill_dtm.addRow(row);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void cleanUp() {
    frame.setVisible(false);
    user = null;
    bill_dtm.setRowCount(0);
  }

  public ConsumerDashboard render() {
    if (user != null) {
      try {
        ResultSet userRS = db.getUser(user);
        userRS.next();

        heading_lbl.setText("Hi, " + userRS.getString("first_name") + " " + userRS.getString("last_name") + "!");
        heading_lbl.setBounds((frame.getWidth() - heading_lbl.getPreferredSize().width) / 2, 30,
            heading_lbl.getPreferredSize().width, 40);
        renderBillTable(user);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    frame.setVisible(true);
    return this;
  }

  public ConsumerDashboard render(String username) {
    user = username;
    return render();
  }

  public static void main(String[] args) {
    DatabaseConfig.get();
    get().render(null);
  }
}
