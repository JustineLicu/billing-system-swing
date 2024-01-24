import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class LoginForm {

  private DatabaseClient db = DatabaseClient.get();

  public JFrame frame = new JFrame("Login");

  public JLabel heading_lbl = new JLabel("LOGIN");
  public JLabel username_lbl = new JLabel("Username:");
  public JLabel password_lbl = new JLabel("Password:");

  public JTextField username_tf = new JTextField();
  public JPasswordField password_tf = new JPasswordField();

  public JButton register_link_btn = new JButton("Register Here");
  public JButton login_btn = new JButton("Login");

  private static LoginForm instance;

  public static LoginForm get() {
    if (instance == null) {
      instance = new LoginForm();
    }
    return instance;
  }

  public LoginForm() {
    frame.setBounds(450, 150, 500, 300);
    frame.setLocationRelativeTo(null);
    frame.setLayout(null);

    frame.add(heading_lbl);
    frame.add(username_lbl);
    frame.add(password_lbl);
    frame.add(username_tf);
    frame.add(password_tf);
    frame.add(register_link_btn);
    frame.add(login_btn);

    addActionListeners();

    heading_lbl.setBounds(170, 10, 100, 100);
    username_lbl.setBounds(100, 40, 100, 100);
    password_lbl.setBounds(100, 80, 100, 100);

    username_tf.setBounds(175, 77, 150, 25);
    password_tf.setBounds(175, 114, 150, 25);

    register_link_btn.setBounds(100, 150, 130, 30);
    login_btn.setBounds(250, 150, 135, 30);
  }

  public void addActionListeners() {

    login_btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (isFieldEmpty()) {
          return;
        }

        String[] texts = getTexts();

        try {
          ResultSet userRS = db.getUser(texts);
          if (!userRS.next()) {
            JOptionPane.showMessageDialog(null,
                "Username or password is incorrect", "Authentication Error",
                JOptionPane.ERROR_MESSAGE);
            return;
          }

          JOptionPane.showMessageDialog(null,
              "Login successfully!", "Authentication Success",
              JOptionPane.INFORMATION_MESSAGE);

          cleanUp();

          if (userRS.getString("role").equals(DatabaseClient.UserRole.ADMIN.name())) {
            AdminDashboard.get().render(texts[0], null);
          } else {
            ConsumerDashboard.get().render(texts[0]);
          }
        } catch (SQLException sqlException) {
          sqlException.printStackTrace();
        }
      }
    });

    register_link_btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cleanUp();
        RegisterForm.get().render();
      }
    });
  }

  public boolean isFieldEmpty() {
    return Utility.isFieldEmpty(username_tf, "Username") || Utility.isFieldEmpty(password_tf, "Password");
  }

  public String[] getTexts() {
    return Utility.getTexts(username_tf, password_tf);
  }

  public void resetFields() {
    Utility.resetFields(username_tf, password_tf);
  }

  public void cleanUp() {
    frame.setVisible(false);
    resetFields();
  }

  public LoginForm render() {
    frame.setVisible(true);
    return this;
  }

  public static void main(String[] args) {
    DatabaseConfig.get();
    get().render();
  }
}