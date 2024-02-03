import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PayWifiBill {

  private DatabaseClient db = DatabaseClient.get();

  private Integer id;

  public JFrame frame = new JFrame("Internet");
  public JLabel bg_image = new JLabel(new ImageIcon("wifi.png"));

  // label ng wifi
  public JLabel f3_wifilab1 = new JLabel("Client Details:");
  public JLabel f3_wifilab2 = new JLabel("User:");

  public JLabel f3_wifilab3 = new JLabel("First Name:");
  public JLabel f3_wifilab4 = new JLabel("Last Name:");
  public JLabel f3_wifilab5 = new JLabel("Phone Number:");
  public JLabel f3_wifilab6 = new JLabel("Address:");

  public JLabel bill_date_lbl = new JLabel("Bill Date:");
  public JLabel due_date_lbl = new JLabel("Due Date:");
  public JLabel invoice_no_lbl = new JLabel("Invoice No:");
  public JLabel period_lbl = new JLabel("Period:");

  public JLabel f3_wifilab11 = new JLabel("WIFI BILL");

  public JLabel f3_wifilab12 = new JLabel("Internet Provider:");
  public JLabel f3_wifilab13 = new JLabel("Internet Plan:");
  public JLabel f3_wifilab14 = new JLabel("");
  public JLabel f3_wifilab15 = new JLabel("Bill Amount");
  public JLabel f3_wifilab16 = new JLabel("Penalty");
  public JLabel f3_wifilab17 = new JLabel("Total Bill");
  public JLabel f3_wifilab18 = new JLabel("Cash");
  public JLabel f3_wifilab19 = new JLabel("Change");
  public JLabel f3_wifilab20 = new JLabel("Total PHP:");
  public JLabel f3_wifilab21 = new JLabel("Tax (" + String.valueOf((int) (Utility.tax * 100)) + "%)");

  // textfield ng wifi
  public JTextField consumer_tf = new JTextField();
  public JTextField f3_wifi_username_tf = new JTextField();
  public JTextField f3_wifi_lastname_tf = new JTextField();
  public JTextField f3_wifi_phonenumber_tf = new JTextField();
  public JTextField f3_wifi_address_tf = new JTextField();
  public JLabel bill_date_tf = new JLabel();
  public JLabel due_date_tf = new JLabel();
  public JLabel invoice_no_tf = new JLabel();
  public JLabel period_tf = new JLabel();

  public JTextField provider_tf = new JTextField();
  public JTextField plan_tf = new JTextField();
  public JTextField bill_amount_tf = new JTextField();
  public JTextField penalty_tf = new JTextField();
  public JTextField tax_tf = new JTextField("INCLUDED");
  public JTextField total_bill_tf = new JTextField();
  public JTextField cash_tf = new JTextField();
  public JTextField change_tf = new JTextField();
  public JTextField totalphp_tf = new JTextField();

  // button ng wifi
  public JButton add_btn = new JButton("ADD");
  public JButton pay_btn = new JButton("PAY");
  public JButton back_btn = new JButton("BACK");

  private static PayWifiBill instance;

  public static PayWifiBill get() {
    if (instance == null) {
      instance = new PayWifiBill();
    }
    return instance;
  }

  public PayWifiBill() {
    // frame ng wifi bill
    frame.setBounds(50, 50, 920, 600);
    frame.setLocationRelativeTo(null);
    frame.setLayout(null);

    // add ng label sa wifi
    frame.add(bg_image);
    frame.add(f3_wifilab1);
    frame.add(f3_wifilab2);
    frame.add(f3_wifilab3);
    frame.add(f3_wifilab4);
    frame.add(f3_wifilab5);
    frame.add(f3_wifilab6);
    frame.add(bill_date_lbl);
    frame.add(due_date_lbl);
    frame.add(invoice_no_lbl);
    frame.add(period_lbl);
    frame.add(f3_wifilab11);
    frame.add(f3_wifilab12);
    frame.add(f3_wifilab13);
    frame.add(f3_wifilab14);
    frame.add(f3_wifilab15);
    frame.add(f3_wifilab16);
    frame.add(f3_wifilab17);
    frame.add(f3_wifilab18);
    frame.add(f3_wifilab19);
    frame.add(f3_wifilab20);
    frame.add(f3_wifilab21);

    // add ng textfield sa wifi
    frame.add(f3_wifi_username_tf);
    frame.add(f3_wifi_lastname_tf);
    frame.add(f3_wifi_phonenumber_tf);
    frame.add(f3_wifi_address_tf);
    frame.add(bill_date_tf);
    frame.add(due_date_tf);
    frame.add(invoice_no_tf);
    frame.add(period_tf);

    frame.add(bill_amount_tf);
    frame.add(penalty_tf);
    frame.add(tax_tf);
    frame.add(total_bill_tf);
    frame.add(cash_tf);
    frame.add(totalphp_tf);
    frame.add(change_tf);

    // add ng combo box
    frame.add(consumer_tf);
    frame.add(provider_tf);
    frame.add(plan_tf);

    // add ng button sa wifi
    frame.add(add_btn);
    frame.add(pay_btn);
    frame.add(back_btn);

    addActionListeners();

    // background color
    frame.getContentPane().setBackground(CustomColor.FRAME_BG);
    // bg_image.setBounds(0, 0, 920, 600);
    add_btn.setBackground(CustomColor.PRIMARY);
    pay_btn.setBackground(CustomColor.SUCCESS);
    back_btn.setBackground(CustomColor.WARNING);

    add_btn.setForeground(Color.WHITE);
    pay_btn.setForeground(Color.WHITE);

    // Disabled Textfields
    consumer_tf.setEditable(false);
    f3_wifi_username_tf.setEditable(false);
    f3_wifi_lastname_tf.setEditable(false);
    f3_wifi_phonenumber_tf.setEditable(false);
    f3_wifi_address_tf.setEditable(false);
    provider_tf.setEditable(false);
    plan_tf.setEditable(false);
    bill_amount_tf.setEditable(false);
    penalty_tf.setEditable(false);
    tax_tf.setEditable(false);
    total_bill_tf.setEditable(false);
    change_tf.setEditable(false);
    totalphp_tf.setEditable(false);

    totalphp_tf.setFont(new Font(totalphp_tf.getFont().getName(), totalphp_tf.getFont().getStyle(), 32));
    totalphp_tf.setHorizontalAlignment(JTextField.CENTER);

    // setbound ng label sa wifi
    f3_wifilab1.setBounds(20, 5, 100, 90);
    f3_wifilab2.setBounds(20, 23, 100, 90);

    f3_wifilab3.setBounds(215, 7, 100, 90);
    f3_wifilab4.setBounds(215, 35, 100, 90);
    f3_wifilab5.setBounds(515, 7, 100, 90);
    f3_wifilab6.setBounds(515, 35, 100, 90);

    bill_date_lbl.setBounds(20, 80, 70, 90);
    due_date_lbl.setBounds(215, 80, 80, 90);
    invoice_no_lbl.setBounds(475, 80, 80, 90);
    period_lbl.setBounds(675, 80, 60, 90);

    bill_date_tf.setBounds(100, 80, 100, 90);
    due_date_tf.setBounds(305, 80, 100, 90);
    invoice_no_tf.setBounds(565, 80, 100, 90);
    period_tf.setBounds(745, 80, 100, 90);

    f3_wifilab11.setBounds(100, 130, 100, 90);

    f3_wifilab12.setBounds(20, 155, 115, 90);
    f3_wifilab13.setBounds(20, 175, 100, 90);
    f3_wifilab15.setBounds(20, 225, 100, 90);
    f3_wifilab16.setBounds(20, 255, 100, 90);
    f3_wifilab17.setBounds(20, 315, 100, 90);
    f3_wifilab18.setBounds(20, 350, 100, 90);
    f3_wifilab19.setBounds(20, 385, 100, 90);
    f3_wifilab20.setBounds(400, 130, 100, 90);
    f3_wifilab21.setBounds(20, 285, 100, 90);

    // setbound ng textfield sa wifi
    f3_wifi_username_tf.setBounds(287, 40, 185, 25);
    f3_wifi_lastname_tf.setBounds(287, 70, 185, 25);
    f3_wifi_phonenumber_tf.setBounds(615, 40, 200, 25);
    f3_wifi_address_tf.setBounds(615, 70, 200, 25);

    bill_amount_tf.setBounds(90, 255, 200, 25);
    penalty_tf.setBounds(90, 285, 200, 25);
    tax_tf.setBounds(90, 315, 200, 25);
    total_bill_tf.setBounds(90, 350, 200, 25);
    cash_tf.setBounds(90, 385, 200, 25);
    totalphp_tf.setBounds(400, 195, 292, 250);
    change_tf.setBounds(90, 415, 200, 25);

    // setbound ng combo box
    consumer_tf.setBounds(70, 60, 100, 25);
    provider_tf.setBounds(130, 188, 200, 25);
    plan_tf.setBounds(130, 215, 200, 25);

    // setbound ng button sa wifi
    add_btn.setBounds(400, 450, 85, 50);
    pay_btn.setBounds(500, 450, 85, 50);
    back_btn.setBounds(605, 450, 85, 50);
  }

  public void addActionListeners() {

    add_btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (!total_bill_tf.getText().trim().isEmpty()) {
          return;
        }

        try {
          ResultSet billRS = db.getBill(DatabaseClient.BillCategory.WIFI, id);
          billRS.next();

          Double total_bill = Utility.computeWifiBill(billRS, true);

          total_bill_tf.setText(String.valueOf(total_bill));
          totalphp_tf.setText("PHP " + String.valueOf(total_bill));
          totalphp_tf.setForeground(Color.ORANGE);

          JOptionPane.showMessageDialog(null,
              "Total Bill computed successfully!", "Computation Success",
              JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException sqlException) {
          sqlException.printStackTrace();
        }
      }
    });

    pay_btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (!change_tf.getText().trim().isEmpty()) {
          return;
        }

        if (isFieldEmpty()) {
          return;
        }

        double total_bill = Double.parseDouble(total_bill_tf.getText());
        double cash = Double.parseDouble(cash_tf.getText());

        if (cash < total_bill) {
          JOptionPane.showMessageDialog(null, "Cash is insufficient",
              "Validation Error", JOptionPane.ERROR_MESSAGE);
          return;
        }

        db.payBill(DatabaseClient.BillCategory.WIFI, cash, id);

        change_tf.setText(String.valueOf(cash - total_bill));
        totalphp_tf.setForeground(Color.GREEN);
        cash_tf.setEditable(false);

        JOptionPane.showMessageDialog(null,
            "Bill paid successfully!", "Bill Payment Success",
            JOptionPane.INFORMATION_MESSAGE);
      }
    });

    back_btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cleanUp();
        ConsumerDashboard.get().render();
      }
    });
  }

  public boolean isFieldEmpty() {
    if (total_bill_tf.getText().trim().isEmpty()) {
      JOptionPane.showMessageDialog(null, "Total Bill isn't computed yet",
          "Validation Error", JOptionPane.ERROR_MESSAGE);
      return true;
    }
    return Utility.isFieldEmpty(cash_tf, "Cash");
  }

  public void resetFields() {
    Utility.resetFields(total_bill_tf, cash_tf, change_tf, totalphp_tf);
    totalphp_tf.setForeground(Color.BLACK);
    cash_tf.setEditable(true);
  }

  public void cleanUp() {
    frame.setVisible(false);
    id = null;
    resetFields();
  }

  public PayWifiBill render(Integer id) {
    this.id = id;
    resetFields();

    if (id != null) {
      try {
        ResultSet billRS = db.getBill(DatabaseClient.BillCategory.WIFI, id);
        billRS.next();

        ResultSet userRS = db.getUser(billRS.getString("username"));
        userRS.next();

        boolean isOverdue = Utility.isOverdue(billRS);
        double bill_amount = Utility.computeWifiBill(billRS, false);

        consumer_tf.setText(billRS.getString("username"));
        f3_wifi_username_tf.setText(userRS.getString("first_name"));
        f3_wifi_lastname_tf.setText(userRS.getString("last_name"));
        f3_wifi_phonenumber_tf.setText(userRS.getString("phone_number"));
        f3_wifi_address_tf.setText(userRS.getString("address"));

        bill_date_tf.setText(billRS.getDate("bill_date").toLocalDate().toString());
        due_date_tf.setText(Utility.getDueDate(billRS).toLocalDate().toString());
        due_date_tf.setForeground(isOverdue ? Color.RED : Color.GREEN);
        invoice_no_tf.setText(String.valueOf(id));
        period_tf.setText(billRS.getString("period"));

        provider_tf.setText(billRS.getString("provider"));
        plan_tf.setText("Plan " + String.valueOf(billRS.getInt("plan")));
        bill_amount_tf.setText(String.valueOf(bill_amount));
        penalty_tf.setText(String.valueOf(Utility.computePenalty(bill_amount, isOverdue)));

        Double total_bill = Utility.computeWifiBill(billRS, true);
        Double cash = billRS.getDouble("cash");

        if (Utility.getStatus(total_bill, cash).equals(DatabaseClient.BillStatus.PAID.name())) {
          total_bill_tf.setText(String.valueOf(total_bill));
          totalphp_tf.setText("PHP " + String.valueOf(total_bill));
          cash_tf.setText(String.valueOf(cash));
          change_tf.setText(String.valueOf(cash - total_bill));

          totalphp_tf.setForeground(Color.GREEN);
          cash_tf.setEditable(false);
        }
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
