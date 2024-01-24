import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import com.github.lgooddatepicker.components.*;

public class EditElectricBillForm {

  private DatabaseClient db = DatabaseClient.get();

  private Integer id;

  public JFrame frame = new JFrame("Edit Electric Bill");

  public JLabel editelectricbill_lbl = new JLabel("EDIT ELECTRIC BILL");
  public JLabel consumer_lbl = new JLabel("Consumer:");
  public JLabel previous_reading_lbl = new JLabel("Previous Reading:");
  public JLabel present_reading_lbl = new JLabel("Present Reading:");
  public JLabel unit_rate_lbl = new JLabel("Unit Rate");
  public JLabel period_lbl = new JLabel("Period:");
  public JLabel billdate_lbl = new JLabel("Date:");

  public DatabaseClient.BillPeriod[] period_cb_items = DatabaseClient.BillPeriod.values();
  public JComboBox<DatabaseClient.BillPeriod> period_cb = new JComboBox<>(period_cb_items);

  public JTextField consumer_tf = new JTextField();
  public JTextField previous_reading_tf = new JTextField();
  public JTextField present_reading_tf = new JTextField();
  public JTextField unit_rate_tf = new JTextField();
  public DatePicker billdate_tf = new DatePicker();

  public JButton edit_btn = new JButton("Save changes");

  private static EditElectricBillForm instance;

  public static EditElectricBillForm get() {
    if (instance == null) {
      instance = new EditElectricBillForm();
    }
    return instance;
  }

  public EditElectricBillForm() {
    // FRAME NG ELECTRICITY BILL
    frame.setBounds(500, 150, 400, 500);
    frame.setLocationRelativeTo(null);
    frame.setLayout(null);

    // mga add ng mga labels,textfield buttons sa ELECTRICITY BILL
    frame.add(editelectricbill_lbl);
    frame.add(consumer_lbl);
    frame.add(previous_reading_lbl);
    frame.add(present_reading_lbl);
    frame.add(unit_rate_lbl);
    frame.add(period_lbl);
    frame.add(billdate_lbl);

    frame.add(edit_btn);

    frame.add(consumer_tf);
    frame.add(previous_reading_tf);
    frame.add(present_reading_tf);
    frame.add(unit_rate_tf);
    frame.add(period_cb);
    frame.add(billdate_tf);

    addActionListeners();

    consumer_tf.setEditable(false);

    // setbounds ng mga LABEL,TEXTFIELD SA ELECTRICITY BILL
    editelectricbill_lbl.setBounds(100, 0, 150, 150);
    consumer_lbl.setBounds(25, 70, 100, 100);
    previous_reading_lbl.setBounds(25, 100, 150, 100);
    present_reading_lbl.setBounds(25, 130, 150, 100);
    unit_rate_lbl.setBounds(25, 160, 150, 100);
    period_lbl.setBounds(25, 190, 100, 100);
    billdate_lbl.setBounds(25, 220, 100, 100);
    edit_btn.setBounds(80, 300, 200, 25);

    consumer_tf.setBounds(130, 110, 120, 25);
    previous_reading_tf.setBounds(130, 140, 120, 25);
    present_reading_tf.setBounds(130, 170, 120, 25);
    unit_rate_tf.setBounds(130, 200, 120, 25);
    period_cb.setBounds(130, 230, 120, 25);
    billdate_tf.setBounds(130, 260, 180, 25);
    // billdate_tf5.setBounds(130, 260, 120, 25);
  }

  public void addActionListeners() {

    edit_btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (isFieldEmpty()) {
          return;
        }

        int previous_reading = Integer.parseInt(previous_reading_tf.getText());
        int present_reading = Integer.parseInt(present_reading_tf.getText());

        if (previous_reading > present_reading) {
          JOptionPane.showMessageDialog(null,
              "Previous reading can't be greater than present reading",
              "Validation Error", JOptionPane.ERROR_MESSAGE);
          return;
        }

        double unit_rate = Double.parseDouble(unit_rate_tf.getText());
        DatabaseClient.BillPeriod period = DatabaseClient.BillPeriod.valueOf(period_cb.getSelectedItem().toString());
        Date bill_date = Date.valueOf(billdate_tf.getDate());
        String username = consumer_tf.getText();

        db.updateElectricBill(id, previous_reading, present_reading, unit_rate, period, bill_date);

        JOptionPane.showMessageDialog(null,
            "Electric bill updated successfully!", "Bill Update Success",
            JOptionPane.INFORMATION_MESSAGE);

        cleanUp();
        AdminDashboard.get().render(username);
      }
    });
  }

  public boolean isFieldEmpty() {
    return Utility.isFieldEmpty(previous_reading_tf, "Previous Reading")
        || Utility.isFieldEmpty(present_reading_tf, "Present Reading")
        || Utility.isFieldEmpty(unit_rate_tf, "Unit Rate")
        || Utility.isFieldEmpty(billdate_tf, "Bill Date");
  }

  public void resetFields() {
    Utility.resetFields(consumer_tf, previous_reading_tf, present_reading_tf, unit_rate_tf, period_cb, billdate_tf);
  }

  public void cleanUp() {
    frame.setVisible(false);
    id = null;
    resetFields();
  }

  public EditElectricBillForm render(Integer id) {
    this.id = id;
    resetFields();

    if (id != null) {
      try {
        ResultSet billRS = db.getBill(DatabaseClient.BillCategory.ELECTRIC, id);
        billRS.next();

        consumer_tf.setText(billRS.getString("username"));
        previous_reading_tf.setText(String.valueOf(billRS.getInt("previous_reading")));
        present_reading_tf.setText(String.valueOf(billRS.getInt("present_reading")));
        unit_rate_tf.setText(String.valueOf(billRS.getDouble("unit_rate")));
        period_cb.setSelectedItem(DatabaseClient.BillPeriod.valueOf(billRS.getString("period")));
        billdate_tf.setDate(billRS.getDate("bill_date").toLocalDate());
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    frame.setVisible(true);
    return this;
  }

  public static void main(String[] args) {
    DatabaseConfig.get();
    get().render(null);
  }
}
