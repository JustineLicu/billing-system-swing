import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import com.github.lgooddatepicker.components.*;

public class AddWifiBillForm {

  private DatabaseClient db = DatabaseClient.get();

  public JFrame frame = new JFrame("Wifi Form");

  public JLabel add_wifibill_lbl = new JLabel("Add WiFi Bill");

  public JLabel wifi_consumer_lbl = new JLabel("Consumer:");
  public JLabel provider_lbl = new JLabel("Provider:");
  public JLabel plan_lbl = new JLabel("Plan:");
  public JLabel wifi_period_lbl = new JLabel("Period:");
  public JLabel billdate_lbl = new JLabel("Bill Date:");

  public String[] provider_cb_items = { "PLDT", "Globe", "Converge" };
  public JComboBox<String> provider_cb = new JComboBox<>(provider_cb_items);

  public Integer[] plan_cb_items = { 699, 999, 1299, 1599, 1999, 2299 };
  public JComboBox<Integer> plan_cb = new JComboBox<>(plan_cb_items);

  public DatabaseClient.BillPeriod[] period_cb_items = DatabaseClient.BillPeriod.values();
  public JComboBox<DatabaseClient.BillPeriod> period_cb = new JComboBox<>(period_cb_items);

  public JTextField wifi_consumer_tf = new JTextField();

  public DatePicker billdate_tf = new DatePicker();

  public JButton add_btn = new JButton("ADD");

  private static AddWifiBillForm instance;

  public static AddWifiBillForm get() {
    if (instance == null) {
      instance = new AddWifiBillForm();
    }
    return instance;
  }

  public AddWifiBillForm() {
    // FRAME NG WIFI BILL
    frame.setBounds(500, 150, 400, 500);
    frame.setLocationRelativeTo(null);
    frame.setLayout(null);

    // mga add ng mga labels,textfield buttons sa WATER BILL
    frame.add(add_wifibill_lbl);

    frame.add(wifi_consumer_lbl);
    frame.add(provider_lbl);
    frame.add(plan_lbl);
    frame.add(wifi_period_lbl);
    frame.add(billdate_lbl);

    frame.add(add_btn);

    frame.add(wifi_consumer_tf);
    frame.add(provider_cb);
    frame.add(plan_cb);
    frame.add(period_cb);
    frame.add(billdate_tf);

    addActionListeners();

    frame.getContentPane().setBackground(CustomColor.FRAME_BG);
    add_btn.setBackground(CustomColor.SUCCESS);

    add_btn.setForeground(Color.WHITE);

    wifi_consumer_tf.setEditable(false);

    // setbounds SA WATER BILL

    // BUTTON
    add_wifibill_lbl.setBounds(100, 0, 150, 150);

    // LABEL
    wifi_consumer_lbl.setBounds(25, 70, 100, 100);
    provider_lbl.setBounds(25, 100, 150, 100);
    plan_lbl.setBounds(25, 130, 150, 100);
    wifi_period_lbl.setBounds(25, 160, 150, 100);
    billdate_lbl.setBounds(25, 190, 100, 100);

    add_btn.setBounds(80, 300, 200, 25);

    // TEXTFIELD COMBO BOX
    wifi_consumer_tf.setBounds(130, 110, 120, 25);
    provider_cb.setBounds(130, 140, 130, 25);
    plan_cb.setBounds(130, 170, 130, 25);
    period_cb.setBounds(130, 200, 130, 25);
    billdate_tf.setBounds(130, 230, 180, 25);
  }

  public void addActionListeners() {

    add_btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (isFieldEmpty()) {
          return;
        }

        String provider = provider_cb.getSelectedItem().toString();
        int plan = Integer.parseInt(plan_cb.getSelectedItem().toString());
        DatabaseClient.BillPeriod period = DatabaseClient.BillPeriod.valueOf(period_cb.getSelectedItem().toString());
        Date bill_date = Date.valueOf(billdate_tf.getDate());
        String username = wifi_consumer_tf.getText();

        db.createWifiBill(username, provider, plan, period, bill_date);

        JOptionPane.showMessageDialog(null,
            "WiFi bill added successfully!", "Bill Addition Success",
            JOptionPane.INFORMATION_MESSAGE);

        cleanUp();
        AdminDashboard.get().render(username);
      }
    });
  }

  public boolean isFieldEmpty() {
    return Utility.isFieldEmpty(billdate_tf, "Bill Date");
  }

  public void resetFields() {
    Utility.resetFields(wifi_consumer_tf, provider_cb, plan_cb, period_cb, billdate_tf);
  }

  public void cleanUp() {
    frame.setVisible(false);
    resetFields();
  }

  public AddWifiBillForm render(String username) {
    resetFields();
    if (username != null) {
      wifi_consumer_tf.setText(username);
    }
    frame.setVisible(true);
    return this;
  }

  public static void main(String[] args) {
    DatabaseConfig.get();
    get().render(null);
  }
}
