import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ChangeUserPasswordForm {

  private DatabaseClient db = DatabaseClient.get();

  private String user;

  public JFrame frame = new JFrame("Change Password | Billing System");

  public JLabel heading_lbl = new JLabel("Change Password");
  public JLabel currentpass_lbl = new JLabel("Current Password:");
  public JLabel newpass_lbl = new JLabel("New Password:");
  public JLabel confirm_newpass_lbl = new JLabel("Confirm New Password:");

  public JPasswordField currentpass_tf = new JPasswordField();
  public JPasswordField newpass_tf = new JPasswordField();
  public JPasswordField confirm_newpass_tf = new JPasswordField();

  public JButton changepass_btn = new JButton("Change Password");

  private static ChangeUserPasswordForm instance;

  public static ChangeUserPasswordForm get() {
    if (instance == null) {
      instance = new ChangeUserPasswordForm();
    }
    return instance;
  }

  public ChangeUserPasswordForm() {
    frame.setBounds(70, 70, 500, 360);
    frame.setLocationRelativeTo(null);
    frame.setLayout(null);

    frame.add(heading_lbl);
    frame.add(currentpass_lbl);
    frame.add(newpass_lbl);
    frame.add(confirm_newpass_lbl);
    frame.add(currentpass_tf);
    frame.add(newpass_tf);
    frame.add(confirm_newpass_tf);
    frame.add(changepass_btn);

    addActionListeners();

    frame.getContentPane().setBackground(CustomColor.FRAME_BG);
    changepass_btn.setBackground(CustomColor.PRIMARY);

    changepass_btn.setForeground(Color.WHITE);

    heading_lbl.setFont(new Font(heading_lbl.getFont().getName(), heading_lbl.getFont().getStyle(), 24));

    heading_lbl.setBounds(90, 30, 240, 40);
    currentpass_lbl.setBounds(50, 95, 140, 20);
    newpass_lbl.setBounds(70, 125, 110, 20);
    confirm_newpass_lbl.setBounds(10, 155, 170, 20);

    currentpass_tf.setBounds(190, 95, 220, 20);
    newpass_tf.setBounds(190, 125, 220, 20);
    confirm_newpass_tf.setBounds(190, 155, 220, 20);

    changepass_btn.setBounds(10, 190, 400, 50);
  }

  public void addActionListeners() {

    changepass_btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (isFieldEmpty()) {
          return;
        }

        String[] texts = getTexts();

        if (!texts[1].equals(texts[2])) {
          JOptionPane.showMessageDialog(null,
              "'New Password' & 'Confirm New Password' doesn't match",
              "Change Password Error", JOptionPane.ERROR_MESSAGE);
          return;
        }

        try {
          ResultSet userRS = db.getUser(user, texts[0]);
          if (!userRS.next()) {
            JOptionPane.showMessageDialog(null,
                "Current Password is incorrect", "Change Password Error",
                JOptionPane.ERROR_MESSAGE);
            return;
          }

          db.changeUserPassword(texts[1], user);

          JOptionPane.showMessageDialog(null,
              "Password changed successfully!", "Change Password Success",
              JOptionPane.INFORMATION_MESSAGE);

          cleanUp();
          AdminDashboard.get().render(null);
        } catch (SQLException sqlException) {
          sqlException.printStackTrace();
        }
      }
    });
  }

  public boolean isFieldEmpty() {
    return Utility.isFieldEmpty(currentpass_tf, "Current Password")
        || Utility.isFieldEmpty(newpass_tf, "New Password")
        || Utility.isFieldEmpty(confirm_newpass_tf, "Confirm New Password");
  }

  public String[] getTexts() {
    return Utility.getTexts(currentpass_tf, newpass_tf, confirm_newpass_tf);
  }

  public void resetFields() {
    Utility.resetFields(currentpass_tf, newpass_tf, confirm_newpass_tf);
  }

  public void cleanUp() {
    frame.setVisible(false);
    user = null;
    resetFields();
  }

  public ChangeUserPasswordForm render(String username) {
    user = username;
    frame.setVisible(true);
    return this;
  }

  public static void main(String[] args) {
    DatabaseConfig.get();
    get().render(null);
  }
}
