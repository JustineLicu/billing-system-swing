import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PayWaterBill {

  private DatabaseClient db = DatabaseClient.get();

  private Integer id;

  // frame ng water
  public JFrame frame = new JFrame("Water");
  public JLabel bg_image = new JLabel(new ImageIcon("maynilad.png"));

  // label ng water
  public JLabel f2_water_lab1 = new JLabel("Client Details:");
  public JLabel f2_water_lab2 = new JLabel("User:");

  public JLabel f2_water_lab3 = new JLabel("First Name:");
  public JLabel f2_water_lab4 = new JLabel("Last Name:");
  public JLabel f2_water_lab5 = new JLabel("Phone Number:");
  public JLabel f2_water_lab6 = new JLabel("Address:");

  public JLabel bill_date_lbl = new JLabel("Bill Date:");
  public JLabel due_date_lbl = new JLabel("Due Date:");
  public JLabel invoice_no_lbl = new JLabel("Invoice No:");
  public JLabel period_lbl = new JLabel("Period:");

  public JLabel f2_water_lab11 = new JLabel("WATER BILL");

  public JLabel f2_water_lab12 = new JLabel("Previous Reading:");
  public JLabel f2_water_lab13 = new JLabel("Present Reading:");
  public JLabel f2_water_lab14 = new JLabel("Consumption:");
  public JLabel f2_water_lab15 = new JLabel("Basic Charge");
  public JLabel f2_water_lab16 = new JLabel("Penalty");
  public JLabel f2_water_lab17 = new JLabel("Total Bill");
  public JLabel f2_water_lab18 = new JLabel("Cash");
  public JLabel f2_water_lab19 = new JLabel("Change");
  public JLabel f2_water_lab20 = new JLabel("Total PHP:");
  public JLabel f2_water_lab21 = new JLabel("Tax (" + String.valueOf((int) (Utility.tax * 100)) + "%)");

  // textfield ng water

  public JTextField consumer_tf = new JTextField();
  public JTextField f2_water_username_tf = new JTextField();
  public JTextField f2_water_lastname_tf = new JTextField();
  public JTextField f2_water_phonenumber_tf = new JTextField();
  public JTextField f2_water_address_tf = new JTextField();
  public JLabel bill_date_tf = new JLabel();
  public JLabel due_date_tf = new JLabel();
  public JLabel invoice_no_tf = new JLabel();
  public JLabel period_tf = new JLabel();

  // PreviousReading
  public JTextField f2_water_previousreading_tf = new JTextField();
  // PresentReading
  public JTextField f2_water_presentreading_tf = new JTextField();
  // Consumption
  public JTextField f2_water_consumption_tf = new JTextField();
  // BasicCharge
  public JTextField f2_water_basic_charge_tf = new JTextField();
  // Penalty
  public JTextField f2_water_penalty_tf = new JTextField();
  // TotalBill
  public JTextField total_bill_tf = new JTextField();
  // Cash
  public JTextField cash_tf = new JTextField();
  // Change
  public JTextField change_tf = new JTextField();
  // Total PHP
  public JTextField totalphp_tf = new JTextField();
  // Tax
  public JTextField tax_tf = new JTextField();

  // button ng water
  public JButton add_btn = new JButton("ADD");
  public JButton pay_btn = new JButton("PAY");
  public JButton back_btn = new JButton("BACK");

  private static PayWaterBill instance;

  public static PayWaterBill get() {
    if (instance == null) {
      instance = new PayWaterBill();
    }
    return instance;
  }

  public PayWaterBill() {
    // set bounds ng frame
    frame.setBounds(50, 50, 920, 600);
    frame.setLocationRelativeTo(null);
    frame.setLayout(null);

    // frame add ng combo box sa water
    frame.add(bg_image);
    frame.add(consumer_tf);

    // frame add ng label sa water
    frame.add(f2_water_lab1);
    frame.add(f2_water_lab2);

    frame.add(f2_water_lab3);
    frame.add(f2_water_lab4);
    frame.add(f2_water_lab5);
    frame.add(f2_water_lab6);

    frame.add(bill_date_lbl);
    frame.add(due_date_lbl);
    frame.add(invoice_no_lbl);
    frame.add(period_lbl);

    frame.add(f2_water_lab11);
    frame.add(f2_water_lab12);
    frame.add(f2_water_lab13);
    frame.add(f2_water_lab14);
    frame.add(f2_water_lab15);
    frame.add(f2_water_lab16);
    frame.add(f2_water_lab17);
    frame.add(f2_water_lab18);
    frame.add(f2_water_lab19);
    frame.add(f2_water_lab20);
    frame.add(f2_water_lab21);

    // frame add sa textfield ng water
    frame.add(f2_water_username_tf);
    frame.add(f2_water_lastname_tf);
    frame.add(f2_water_phonenumber_tf);
    frame.add(f2_water_address_tf);
    frame.add(bill_date_tf);
    frame.add(due_date_tf);
    frame.add(invoice_no_tf);
    frame.add(period_tf);

    frame.add(f2_water_previousreading_tf);
    frame.add(f2_water_presentreading_tf);
    frame.add(f2_water_consumption_tf);
    frame.add(f2_water_basic_charge_tf);
    frame.add(f2_water_penalty_tf);
    frame.add(tax_tf);
    frame.add(total_bill_tf);
    frame.add(cash_tf);
    frame.add(totalphp_tf);
    frame.add(change_tf);

    // frame add sa button ng water
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
    f2_water_username_tf.setEditable(false);
    f2_water_lastname_tf.setEditable(false);
    f2_water_phonenumber_tf.setEditable(false);
    f2_water_address_tf.setEditable(false);
    f2_water_previousreading_tf.setEditable(false);
    f2_water_presentreading_tf.setEditable(false);
    f2_water_consumption_tf.setEditable(false);
    f2_water_basic_charge_tf.setEditable(false);
    f2_water_penalty_tf.setEditable(false);
    tax_tf.setEditable(false);
    total_bill_tf.setEditable(false);
    change_tf.setEditable(false);
    totalphp_tf.setEditable(false);

    totalphp_tf.setFont(new Font(totalphp_tf.getFont().getName(), totalphp_tf.getFont().getStyle(), 32));
    totalphp_tf.setHorizontalAlignment(JTextField.CENTER);

    // combo box water
    consumer_tf.setBounds(70, 60, 100, 25);

    // setbounds ng label sa water
    f2_water_lab1.setBounds(20, 5, 100, 90);
    f2_water_lab2.setBounds(20, 23, 100, 90);

    f2_water_lab3.setBounds(215, 7, 100, 90);
    f2_water_lab4.setBounds(215, 35, 100, 90);
    f2_water_lab5.setBounds(515, 7, 100, 90);
    f2_water_lab6.setBounds(515, 35, 100, 90);

    bill_date_lbl.setBounds(20, 80, 70, 90);
    due_date_lbl.setBounds(215, 80, 80, 90);
    invoice_no_lbl.setBounds(475, 80, 80, 90);
    period_lbl.setBounds(675, 80, 60, 90);

    bill_date_tf.setBounds(100, 80, 100, 90);
    due_date_tf.setBounds(305, 80, 100, 90);
    invoice_no_tf.setBounds(565, 80, 100, 90);
    period_tf.setBounds(745, 80, 100, 90);

    f2_water_lab11.setBounds(100, 130, 100, 90);

    f2_water_lab12.setBounds(20, 155, 115, 90);
    f2_water_lab13.setBounds(20, 175, 100, 90);
    f2_water_lab14.setBounds(20, 195, 100, 90);
    f2_water_lab15.setBounds(20, 225, 100, 90);
    f2_water_lab16.setBounds(20, 255, 100, 90);
    f2_water_lab17.setBounds(20, 315, 100, 90);
    f2_water_lab18.setBounds(20, 350, 100, 90);
    f2_water_lab19.setBounds(20, 385, 100, 90);
    f2_water_lab20.setBounds(400, 130, 100, 90);
    f2_water_lab21.setBounds(20, 285, 100, 90);

    // setbounds ng textfield sa water
    f2_water_username_tf.setBounds(287, 40, 185, 25);
    f2_water_lastname_tf.setBounds(287, 70, 185, 25);
    f2_water_phonenumber_tf.setBounds(615, 40, 200, 25);
    f2_water_address_tf.setBounds(615, 70, 200, 25);

    f2_water_previousreading_tf.setBounds(130, 185, 200, 20);
    f2_water_presentreading_tf.setBounds(130, 206, 200, 20);
    f2_water_consumption_tf.setBounds(130, 228, 200, 20);
    f2_water_basic_charge_tf.setBounds(90, 255, 200, 25);
    f2_water_penalty_tf.setBounds(90, 285, 200, 25);
    tax_tf.setBounds(90, 315, 200, 25);
    total_bill_tf.setBounds(90, 350, 200, 25);
    cash_tf.setBounds(90, 385, 200, 25);
    totalphp_tf.setBounds(400, 195, 292, 250);
    change_tf.setBounds(90, 415, 200, 25);

    // setbounds ng button sa water
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
          ResultSet billRS = db.getBill(DatabaseClient.BillCategory.WATER, id);
          billRS.next();

          Double total_bill = Utility.computeWaterBill(billRS);

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

        db.payBill(DatabaseClient.BillCategory.WATER, cash, id);

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

  public PayWaterBill render(Integer id) {
    this.id = id;
    resetFields();

    if (id != null) {
      try {
        ResultSet billRS = db.getBill(DatabaseClient.BillCategory.WATER, id);
        billRS.next();

        ResultSet userRS = db.getUser(billRS.getString("username"));
        userRS.next();

        boolean isOverdue = Utility.isOverdue(billRS);
        int previous_reading = billRS.getInt("previous_reading");
        int present_reading = billRS.getInt("present_reading");
        double basic_charge = billRS.getDouble("basic_charge");
        double penalty = Utility.computePenalty(basic_charge, isOverdue);

        consumer_tf.setText(billRS.getString("username"));
        f2_water_username_tf.setText(userRS.getString("first_name"));
        f2_water_lastname_tf.setText(userRS.getString("last_name"));
        f2_water_phonenumber_tf.setText(userRS.getString("phone_number"));
        f2_water_address_tf.setText(userRS.getString("address"));

        bill_date_tf.setText(billRS.getDate("bill_date").toLocalDate().toString());
        due_date_tf.setText(Utility.getDueDate(billRS).toLocalDate().toString());
        due_date_tf.setForeground(isOverdue ? Color.RED : Color.GREEN);
        invoice_no_tf.setText(String.valueOf(id));
        period_tf.setText(billRS.getString("period"));

        f2_water_previousreading_tf.setText(String.valueOf(previous_reading));
        f2_water_presentreading_tf.setText(String.valueOf(present_reading));
        f2_water_consumption_tf.setText(String.valueOf(present_reading - previous_reading));
        f2_water_basic_charge_tf.setText(String.valueOf(basic_charge));
        f2_water_penalty_tf.setText(String.valueOf(penalty));
        tax_tf.setText(String.valueOf(Utility.computeTax(basic_charge, penalty)));

        Double total_bill = Utility.computeWaterBill(billRS);
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