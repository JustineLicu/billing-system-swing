import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminDashboard {

  private DatabaseClient db = DatabaseClient.get();

  public String user;

  public JFrame frame = new JFrame("Admin Dashboard | Billing System");

  public JLabel heading_lbl = new JLabel("Admin Dashboard");
  public JLabel user_lbl = new JLabel("User:");
  public JLabel addbill_lbl = new JLabel("Add a bill:");

  public String[][] bill_tr = {};
  public String[] bill_tc = { "Category", "ID", "Amount", "Bill Date", "Due Date", "Status" };
  public DefaultTableModel bill_dtm = new DefaultTableModel(bill_tr, bill_tc);
  public JTable bill_table = new JTable(bill_dtm);
  public JScrollPane bill_tsp = new JScrollPane(bill_table);

  public JButton changepass_btn = new JButton("Change Password");
  public JButton logout_btn = new JButton("Logout");
  public JButton editbill_btn = new JButton("Edit");
  public JButton deletebill_btn = new JButton("Delete");

  public String[] username_cb_items = { "Choose a user..." };
  public JComboBox<String> username_cb = new JComboBox<>(username_cb_items);

  public DatabaseClient.BillCategory[] addbill_cb_items = DatabaseClient.BillCategory.values();
  public JComboBox<DatabaseClient.BillCategory> addbill_cb = new JComboBox<>(addbill_cb_items);

  private static AdminDashboard instance;

  public static AdminDashboard get() {
    if (instance == null) {
      instance = new AdminDashboard();
    }
    return instance;
  }

  public AdminDashboard() {
    frame.setBounds(70, 70, 650, 700);
    frame.setLocationRelativeTo(null);
    frame.setLayout(null);

    frame.add(heading_lbl);
    frame.add(user_lbl);
    frame.add(addbill_lbl);
    frame.add(bill_tsp);
    frame.add(changepass_btn);
    frame.add(logout_btn);
    frame.add(editbill_btn);
    frame.add(deletebill_btn);
    frame.add(username_cb);
    frame.add(addbill_cb);

    addActionListeners();

    frame.getContentPane().setBackground(CustomColor.FRAME_BG);
    changepass_btn.setBackground(CustomColor.INFO);
    logout_btn.setBackground(CustomColor.DANGER);
    editbill_btn.setBackground(CustomColor.WARNING);
    deletebill_btn.setBackground(CustomColor.DANGER);

    logout_btn.setForeground(Color.WHITE);
    deletebill_btn.setForeground(Color.WHITE);

    heading_lbl.setFont(new Font(heading_lbl.getFont().getName(), heading_lbl.getFont().getStyle(), 24));

    heading_lbl.setBounds(170, 30, 300, 40);
    user_lbl.setBounds(10, 95, 40, 20);
    addbill_lbl.setBounds(10, 465, 80, 20);

    bill_tsp.setBounds(10, 140, 550, 300);

    changepass_btn.setBounds(280, 80, 170, 50);
    logout_btn.setBounds(460, 80, 100, 50);
    editbill_btn.setBounds(350, 450, 100, 50);
    deletebill_btn.setBounds(460, 450, 100, 50);

    username_cb.setBounds(60, 80, 150, 50);
    addbill_cb.setBounds(100, 450, 100, 50);
  }

  public void addActionListeners() {

    changepass_btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ChangeUserPasswordForm.get().render(user);
        AddElectricBillForm.get().cleanUp();
        AddWaterBillForm.get().cleanUp();
        AddWifiBillForm.get().cleanUp();
        EditElectricBillForm.get().cleanUp();
        EditWaterBillForm.get().cleanUp();
        EditWifiBillForm.get().cleanUp();
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

    editbill_btn.addActionListener(new ActionListener() {
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
          AddElectricBillForm.get().cleanUp();
          AddWaterBillForm.get().cleanUp();
          AddWifiBillForm.get().cleanUp();
          EditElectricBillForm.get().render(id);
          EditWaterBillForm.get().cleanUp();
          EditWifiBillForm.get().cleanUp();
          return;
        }

        if (category.equals(DatabaseClient.BillCategory.WATER.name())) {
          ChangeUserPasswordForm.get().cleanUp();
          AddElectricBillForm.get().cleanUp();
          AddWaterBillForm.get().cleanUp();
          AddWifiBillForm.get().cleanUp();
          EditElectricBillForm.get().cleanUp();
          EditWaterBillForm.get().render(id);
          EditWifiBillForm.get().cleanUp();
          return;
        }

        if (category.equals(DatabaseClient.BillCategory.WIFI.name())) {
          ChangeUserPasswordForm.get().cleanUp();
          AddElectricBillForm.get().cleanUp();
          AddWaterBillForm.get().cleanUp();
          AddWifiBillForm.get().cleanUp();
          EditElectricBillForm.get().cleanUp();
          EditWaterBillForm.get().cleanUp();
          EditWifiBillForm.get().render(id);
          return;
        }
      }
    });

    deletebill_btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int selectedRowIdx = bill_table.getSelectedRow();
        if (selectedRowIdx < 0) {
          JOptionPane.showMessageDialog(null, "No selected row",
              "Deletion Error", JOptionPane.ERROR_MESSAGE);
          return;
        }

        int isSure = JOptionPane.showConfirmDialog(null,
            "Are you absolutely sure?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);
        if (isSure != JOptionPane.YES_OPTION) {
          return;
        }

        DatabaseClient.BillCategory category = DatabaseClient.BillCategory
            .valueOf(bill_table.getValueAt(selectedRowIdx, 0).toString());
        int id = Integer.parseInt(bill_table.getValueAt(selectedRowIdx, 1).toString());

        db.deleteBill(category, id);
        bill_dtm.removeRow(selectedRowIdx);
      }
    });

    username_cb.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (username_cb.getItemCount() < 1) {
          username_cb.addItem(username_cb_items[0]);
        }

        String username = username_cb.getSelectedItem().toString();

        if (username.equals(username_cb_items[0])) {
          bill_dtm.setRowCount(0);
          return;
        }

        renderBillTable(username);
      }
    });

    addbill_cb.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String username = username_cb.getSelectedItem().toString();
        if (username.equals(username_cb_items[0])) {
          JOptionPane.showMessageDialog(null, "No user selected",
              "Bill Addition Error", JOptionPane.ERROR_MESSAGE);
          return;
        }

        String category = addbill_cb.getSelectedItem().toString();

        if (category.equals(DatabaseClient.BillCategory.ELECTRIC.name())) {
          ChangeUserPasswordForm.get().cleanUp();
          AddElectricBillForm.get().render(username);
          AddWaterBillForm.get().cleanUp();
          AddWifiBillForm.get().cleanUp();
          EditElectricBillForm.get().cleanUp();
          EditWaterBillForm.get().cleanUp();
          EditWifiBillForm.get().cleanUp();
          return;
        }

        if (category.equals(DatabaseClient.BillCategory.WATER.name())) {
          ChangeUserPasswordForm.get().cleanUp();
          AddElectricBillForm.get().cleanUp();
          AddWaterBillForm.get().render(username);
          AddWifiBillForm.get().cleanUp();
          EditElectricBillForm.get().cleanUp();
          EditWaterBillForm.get().cleanUp();
          EditWifiBillForm.get().cleanUp();
          return;
        }

        if (category.equals(DatabaseClient.BillCategory.WIFI.name())) {
          ChangeUserPasswordForm.get().cleanUp();
          AddElectricBillForm.get().cleanUp();
          AddWaterBillForm.get().cleanUp();
          AddWifiBillForm.get().render(username);
          EditElectricBillForm.get().cleanUp();
          EditWaterBillForm.get().cleanUp();
          EditWifiBillForm.get().cleanUp();
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
    username_cb.removeAllItems();
    bill_dtm.setRowCount(0);
  }

  public AdminDashboard render(String selectedUsername) {
    username_cb.removeAllItems();

    try {
      ResultSet usersRS = db.getUsers(DatabaseClient.UserRole.USER);
      while (usersRS.next()) {
        username_cb.addItem(usersRS.getString("username"));
      }

      if (selectedUsername != null) {
        username_cb.setSelectedItem(selectedUsername);
      }

      frame.setVisible(true);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return this;
  }

  public AdminDashboard render(String username, String selectedUsername) {
    user = username;
    return render(selectedUsername);
  }

  public static void main(String[] args) {
    DatabaseConfig.get();
    get().render(null);
  }
}
