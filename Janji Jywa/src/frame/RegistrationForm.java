package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import util.Connect;

public class RegistrationForm extends JFrame {

	JTextField txtID = new JTextField(SwingConstants.LEFT);

	private Connect con = new Connect();

	public void config() {
		setSize(550, 650);
		setTitle("Register Form");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
	}

	void initID() {
		String id;
		
		ResultSet rs = con.execQuery("SELECT COUNT(UserID) AS NumberOfRows FROM users");
		
		int number = 0;
		try {
			rs.next();
			number = rs.getInt(1) + 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 int a = 0;
	        int b = 0;
	        int c = number;
	        
	        if (number>9 && number <100) {
	            c = number % 10;
	            b = number / 10;
	        } else if (number > 99 && number < 1000) {
	            c = (number % 100) % 10;
	            b = (number/10) % 10;
	            a = number / 100;
	        }
	        
	       id = "US" + a + b + c;
	       
		txtID.setText(id);
		txtID.setEditable(false);
	}
	
	boolean checkPhoneValidity(String phone) {
		if (phone.length() < 12)
			return false;

		try {
			Double.parseDouble(phone);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	boolean checkEmailValidity(String email) {
		boolean dotExists = false;
		boolean atExists = false;
		int dotIndex = -1;
		int atIndex = 9999;

		if (email.indexOf('@') == 0 || email.indexOf('@') == email.length() - 1)
			return false;
		
		if (Math.abs(email.indexOf('@') - email.indexOf('.')) == 1)
			return false;

		if (email.indexOf('.') == 0 || email.indexOf('.') == email.length() - 1)
			return false;

		for (int i = 0; i < email.length(); i++) {
			if (email.charAt(i) == '@') {
				if (!atExists) {
					atExists = true;
					atIndex = i;
				} else
					return false;
			}
			if (email.charAt(i) == '.') {
				if (!dotExists) {
					dotExists = true;
					dotIndex = i;
				} else
					return false;
			}
		}

		if (Math.abs(email.indexOf('@') - email.indexOf('.')) == 1)
			return false;
		
		if (dotIndex < atIndex)
			return false;

		return true;
	}

	boolean checkPasswordValidity(String password) {

		if (password.length() < 5 || password.length() > 30)
			return false;

		boolean containCharacter = false;
		boolean containDigit = false;

		for (int i = 0; i < password.length(); i++) {
			if (password.charAt(i) >= 'A' && password.charAt(i) <= 'Z') {
				containCharacter = true;
			} else if (password.charAt(i) >= 'a' && password.charAt(i) <= 'z') {
				containCharacter = true;
			} else if (password.charAt(i) >= '0' && password.charAt(i) <= '9') {
				containDigit = true;
			}
		}

		if (containCharacter && containDigit)
			return true;

		return false;
	}

	public RegistrationForm() {
		config();
		initID();

		JPanel panelTitle = new JPanel();
		panelTitle.setBackground(Color.CYAN);

		JLabel lblTitle = new JLabel("Register Form");
		panelTitle.add(lblTitle);
		add(panelTitle, BorderLayout.NORTH);

		JPanel panel = new JPanel(new GridLayout(8, 2, 50, 20));
		add(panel, BorderLayout.CENTER);
		panel.setBackground(Color.BLUE);

		JLabel lblID = new JLabel("ID");
		panel.add(lblID);
		
		//TEXT
		panel.add(txtID);

		JLabel lblUsername = new JLabel("User Name");
		panel.add(lblUsername);

		JTextField txtUsername = new JTextField();
		panel.add(txtUsername);

		JLabel lblEmail = new JLabel("Email");
		panel.add(lblEmail);

		JTextField txtEmail = new JTextField();
		panel.add(txtEmail);

		JLabel lblPhone = new JLabel("Phone");
		panel.add(lblPhone);

		JTextField txtPhone = new JTextField();
		panel.add(txtPhone);

		JLabel lblAddress = new JLabel("Address");
		panel.add(lblAddress);

		JTextArea txtAddress = new JTextArea();
		panel.add(txtAddress);

		JLabel lblPass = new JLabel("Password");
		panel.add(lblPass);

		JPasswordField txtPass = new JPasswordField();
		panel.add(txtPass);

		JLabel lblGender = new JLabel("Gender");
		panel.add(lblGender);

		JPanel panelGender = new JPanel(new FlowLayout());
		panel.add(panelGender);
		panel.setBackground(Color.CYAN);

		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JRadioButton genderFemale = new JRadioButton("Female");
		panelGender.add(genderFemale);
		genderFemale.setBackground(Color.CYAN);
		JRadioButton genderMale = new JRadioButton("Male");
		panelGender.add(genderMale);
		genderMale.setBackground(Color.CYAN);
		ButtonGroup groupGender = new ButtonGroup();
		groupGender.add(genderFemale);
		groupGender.add(genderMale);

		JLabel lblRole = new JLabel("Role");
		panel.add(lblRole);
		panelGender.setBackground(Color.CYAN);

		String[] roles = { "-Choose a role-", "Admin", "Customer" };

		JComboBox cmbRole = new JComboBox(roles);
		panel.add(cmbRole);

		JPanel pnlBtns = new JPanel(new GridLayout(0, 1));
		panel.add(pnlBtns, BorderLayout.CENTER);
		panel.setBackground(Color.CYAN);

		JPanel pnlBtnRegister = new JPanel();
		JButton btnRegister = new JButton("Register");
		pnlBtnRegister.add(btnRegister);
		pnlBtns.add(pnlBtnRegister);

		pnlBtnRegister.setBackground(Color.CYAN);

		JPanel pnlSignIn = new JPanel();
		panel.add(pnlSignIn, BorderLayout.CENTER);
		pnlSignIn.setBackground(Color.CYAN);

		JLabel lblSignIn = new JLabel("Login Here");
		pnlSignIn.add(lblSignIn);
		pnlBtns.add(pnlSignIn);

		add(pnlBtns, BorderLayout.SOUTH);

		btnRegister.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String id = txtID.getText();
				String userName = txtUsername.getText();
				String email = txtEmail.getText();
				String phone = txtPhone.getText();
				String address = txtAddress.getText();
				String password = new String(txtPass.getPassword());
				String gender = genderMale.isSelected() ? genderMale.getText()
						: genderFemale.isSelected() ? genderFemale.getText() : null;
				String role = (String) cmbRole.getSelectedItem();

				if (userName.length() < 5 || userName.length() > 30) {
					JOptionPane.showMessageDialog(null, "User Name must be between 5 - 30 characters");
				} else if (!checkEmailValidity(email)) {
					JOptionPane.showMessageDialog(null, "Email is not valid");
				} else if (!checkPhoneValidity(phone)) {
					JOptionPane.showMessageDialog(null, "Phone must be numeric and more than equals 12 digits");
				} else if (!address.endsWith(" Street") || address.length() < 10) {
					JOptionPane.showMessageDialog(null,
							"Address must consist of 10 or more characters and ends with ‘ Street’");
				} else if (!checkPasswordValidity(password)) {
					JOptionPane.showMessageDialog(null,
							"Password must 5 - 30 length of character and digit (must at least contain 1 character and 1 digit)");
				} else if (gender == null) {
					JOptionPane.showMessageDialog(null, "Gender must be either “Male” or “Female”");
				} else if (cmbRole.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(null, "Role must be chosen either “Admin” or “Customer”");
				} else {
					con.rs = con.execQuery("SELECT * FROM users where UserEmail LIKE '" + email
							+ "' OR UserPhone LIKE '" + phone + "'");
					try {

						if (con.rs.next()) {
							JOptionPane.showMessageDialog(null, "Email or Phone already exist");
						} else {
							con.registerUser(id, userName, email, password, null, gender, address, phone, role);
							JOptionPane.showMessageDialog(null, "You have been successfully registered");
							dispose();
							new LoginForm();
						}
					} catch (SQLException err) {
						// TODO Auto-generated catch block
						err.printStackTrace();
					}
				}

			}
		});

		lblSignIn.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				dispose();
				new LoginForm();
			}
		});

		setVisible(true);
	}

}
