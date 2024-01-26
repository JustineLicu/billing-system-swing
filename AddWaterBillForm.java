import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import com.github.lgooddatepicker.components.*;

public class AddWaterBillForm {

  private DatabaseClient db = DatabaseClient.get();

  public JFrame frame = new JFrame("Water Form");

  public JLabel addwaterbill_lbl = new JLabel("ADD WATER BILL");

  public JLabel water_consumer_lbl = new JLabel("Consumer:");
  public JLabel water_previous_reading_lbl = new JLabel("Previous Reading:");
  public JLabel water_present_reading_lbl = new JLabel("Present Reading:");
  public JLabel basic_charge_lbl = new JLabel("Basic Charge:");
  public JLabel water_period_lbl = new JLabel("Period:");
  public JLabel water_billdate_lbl = new JLabel("Date:");

  public DatabaseClient.BillPeriod[] period_cb_items = DatabaseClient.BillPeriod.values();
  public JComboBox<DatabaseClient.BillPeriod> period_cb = new JComboBox<>(period_cb_items);

  public JTextField water_consumer_tf = new JTextField();
  public JTextField water_previous_reading_tf = new JTextField();
  public JTextField water_present_reading_tf = new JTextField();
  public JTextField basic_charge_tf = new JTextField();
  public DatePicker water_billdate_tf = new DatePicker();

  public JButton add_btn = new JButton("ADD");

  private static AddWaterBillForm instance;

  public static AddWaterBillForm get() {
    if (instance == null) {
      instance = new AddWaterBillForm();
    }
    return instance;
  }

  public AddWaterBillForm() {
    // FRAME NG water BILL
    frame.setBounds(500, 150, 400, 500);
    frame.setLocationRelativeTo(null);
    frame.setLayout(null);

    // mga add ng mga labels,textfield buttons sa ELECTRICITY BILL
    frame.add(addwaterbill_lbl);

    // label
    frame.add(water_consumer_lbl);
    frame.add(water_previous_reading_lbl);
    frame.add(water_present_reading_lbl);
    frame.add(basic_charge_lbl);
    frame.add(water_period_lbl);
    frame.add(water_billdate_lbl);

    // combo box
    frame.add(period_cb);

    // button
    frame.add(add_btn);

    // textfield
    frame.add(water_consumer_tf);
    frame.add(water_previous_reading_tf);
    frame.add(water_present_reading_tf);
    frame.add(basic_charge_tf);
    frame.add(period_cb);
    frame.add(water_billdate_tf);

    addActionListeners();

    frame.getContentPane().setBackground(CustomColor.FRAME_BG);
    add_btn.setBackground(CustomColor.SUCCESS);

    add_btn.setForeground(Color.WHITE);

    water_consumer_tf.setEditable(false);

    // setbounds ng mga water BILL

    // label
    addwaterbill_lbl.setBounds(100, 0, 150, 150);
    water_consumer_lbl.setBounds(25, 70, 100, 100);
    water_previous_reading_lbl.setBounds(25, 100, 150, 100);
    water_present_reading_lbl.setBounds(25, 130, 150, 100);
    basic_charge_lbl.setBounds(25, 160, 150, 100);
    water_period_lbl.setBounds(25, 190, 100, 100);
    water_billdate_lbl.setBounds(25, 220, 100, 100);

    // button
    add_btn.setBounds(80, 300, 200, 25);

    // textfield combo box
    water_consumer_tf.setBounds(130, 110, 120, 25);
    water_previous_reading_tf.setBounds(130, 140, 120, 25);
    water_present_reading_tf.setBounds(130, 170, 120, 25);
    basic_charge_tf.setBounds(130, 200, 120, 25);
    period_cb.setBounds(130, 230, 120, 25);
    water_billdate_tf.setBounds(130, 260, 180, 25);
  }

  public void addActionListeners() {

    add_btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (isFieldEmpty()) {
          return;
        }

        int previous_reading = Integer.parseInt(water_previous_reading_tf.getText());
        int present_reading = Integer.parseInt(water_present_reading_tf.getText());

        if (previous_reading > present_reading) {
          JOptionPane.showMessageDialog(null,
              "Previous reading can't be greater than present reading",
              "Validation Error", JOptionPane.ERROR_MESSAGE);
          return;
        }

        double basic_charge = Double.parseDouble(basic_charge_tf.getText());
        DatabaseClient.BillPeriod period = DatabaseClient.BillPeriod.valueOf(period_cb.getSelectedItem().toString());
        Date bill_date = Date.valueOf(water_billdate_tf.getDate());
        String username = water_consumer_tf.getText();

        db.createWaterBill(username, previous_reading, present_reading, basic_charge, period, bill_date);

        JOptionPane.showMessageDialog(null,
            "Water bill added successfully!", "Bill Addition Success",
            JOptionPane.INFORMATION_MESSAGE);

        cleanUp();
        AdminDashboard.get().render(username);
      }
    });
  }

  public boolean isFieldEmpty() {
    return Utility.isFieldEmpty(water_previous_reading_tf, "Previous Reading")
        || Utility.isFieldEmpty(water_present_reading_tf, "Present Reading")
        || Utility.isFieldEmpty(basic_charge_tf, "Basic Charge")
        || Utility.isFieldEmpty(water_billdate_tf, "Bill Date");
  }

  public void resetFields() {
    Utility.resetFields(water_consumer_tf, water_previous_reading_tf, water_present_reading_tf,
        basic_charge_tf, period_cb, water_billdate_tf);
  }

  public void cleanUp() {
    frame.setVisible(false);
    resetFields();
  }

  public AddWaterBillForm render(String username) {
    resetFields();
    if (username != null) {
      water_consumer_tf.setText(username);
    }
    frame.setVisible(true);
    return this;
  }

  public static void main(String[] args) {
    DatabaseConfig.get();
    get().render(null);
  }
}