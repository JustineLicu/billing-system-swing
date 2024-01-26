import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class RegisterForm {

	private DatabaseClient db = DatabaseClient.get();

	public JFrame frame = new JFrame("Register now");

	public JLabel heading_lbl = new JLabel("Register now");
	public JLabel username_lbl = new JLabel("Username");
	public JLabel password_lbl = new JLabel("Password");
	public JLabel email_lbl = new JLabel("Email Address");
	public JLabel firstname_lbl = new JLabel("FirstName");
	public JLabel lastname_lbl = new JLabel("LastName");
	public JLabel phoneno_lbl = new JLabel("Phone Number");
	public JLabel address_lbl = new JLabel("Address");

	public JTextField username_tf = new JTextField();
	public JPasswordField password_tf = new JPasswordField();
	public JTextField email_tf = new JTextField();
	public JTextField firstname_tf = new JTextField();
	public JTextField lastname_tf = new JTextField();
	public JTextField phoneno_tf = new JTextField();
	public JTextField address_tf = new JTextField();

	public JButton register_btn = new JButton("Register");
	public JRadioButton login_link_rb = new JRadioButton("Already have an account? Login");

	private static RegisterForm instance;

	public static RegisterForm get() {
		if (instance == null) {
			instance = new RegisterForm();
		}
		return instance;
	}

	public RegisterForm() {
		frame.setBounds(400, 50, 500, 600);
		frame.setLocationRelativeTo(null);
		frame.setLayout(null);

		frame.add(heading_lbl);
		frame.add(username_lbl);
		frame.add(password_lbl);
		frame.add(email_lbl);
		frame.add(firstname_lbl);
		frame.add(lastname_lbl);
		frame.add(phoneno_lbl);
		frame.add(address_lbl);
		frame.add(username_tf);
		frame.add(password_tf);
		frame.add(email_tf);
		frame.add(firstname_tf);
		frame.add(lastname_tf);
		frame.add(phoneno_tf);
		frame.add(address_tf);
		frame.add(register_btn);
		frame.add(login_link_rb);

		addActionListeners();

		frame.getContentPane().setBackground(CustomColor.FRAME_BG);
		register_btn.setBackground(CustomColor.PRIMARY);

		register_btn.setForeground(Color.WHITE);
		login_link_rb.setForeground(CustomColor.PRIMARY);

		heading_lbl.setBounds(200, 50, 100, 100);
		username_lbl.setBounds(100, 82, 100, 100);
		password_lbl.setBounds(100, 110, 100, 100);
		email_lbl.setBounds(100, 140, 100, 100);
		firstname_lbl.setBounds(100, 168, 100, 100);
		lastname_lbl.setBounds(100, 198, 100, 100);
		phoneno_lbl.setBounds(100, 225, 100, 100);
		address_lbl.setBounds(100, 250, 100, 100);

		username_tf.setBounds(200, 125, 160, 25);
		password_tf.setBounds(200, 153, 160, 25);
		email_tf.setBounds(200, 180, 160, 25);
		firstname_tf.setBounds(200, 207, 160, 25);
		lastname_tf.setBounds(200, 234, 160, 25);
		phoneno_tf.setBounds(200, 260, 160, 25);
		address_tf.setBounds(200, 287, 160, 25);

		register_btn.setBounds(180, 320, 100, 30);
		login_link_rb.setBounds(110, 360, 250, 30);
	}

	public void addActionListeners() {

		register_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isFieldEmpty()) {
					return;
				}

				String[] texts = getTexts();

				try {
					boolean userExists = db.getUser(texts[0]).next();
					if (userExists) {
						JOptionPane.showMessageDialog(null,
								"Username is already taken", "Registration Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					db.createUser(texts);

					JOptionPane.showMessageDialog(null,
							"Registered successfully!", "Registration Success",
							JOptionPane.INFORMATION_MESSAGE);

					cleanUp();
					LoginForm.get().render();
				} catch (SQLException sqlException) {
					sqlException.printStackTrace();
				}
			}
		});

		login_link_rb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cleanUp();
				LoginForm.get().render();
			}
		});
	}

	public boolean isFieldEmpty() {
		return Utility.isFieldEmpty(username_tf, "Username") ||
				Utility.isFieldEmpty(password_tf, "Password") ||
				Utility.isFieldEmpty(email_tf, "Email Address") ||
				Utility.isFieldEmpty(firstname_tf, "First Name") ||
				Utility.isFieldEmpty(lastname_tf, "Last Name") ||
				Utility.isFieldEmpty(phoneno_tf, "Phone Number") ||
				Utility.isFieldEmpty(address_tf, "Address");
	}

	public String[] getTexts() {
		return Utility.getTexts(username_tf, password_tf, email_tf, firstname_tf, lastname_tf,
				phoneno_tf, address_tf);
	}

	public void resetFields() {
		Utility.resetFields(username_tf, password_tf, email_tf, firstname_tf, lastname_tf, phoneno_tf, address_tf);
	}

	public void cleanUp() {
		frame.setVisible(false);
		resetFields();
	}

	public RegisterForm render() {
		frame.setVisible(true);
		return this;
	}

	public static void main(String[] args) {
		DatabaseConfig.get();
		get().render();
	}
}