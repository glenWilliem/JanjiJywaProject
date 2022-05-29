package internalFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.mysql.cj.util.StringUtils;

import frame.LoggedUser;
import util.Connect;

public class EditProfileForm extends JInternalFrame {

	JPanel panelLeft = new JPanel(new BorderLayout()), panelRight = new JPanel(new BorderLayout()),
			panelLeftGrid = new JPanel(new GridLayout(0, 2, 50, 20)),
			panelRightGrid = new JPanel(new GridLayout(0, 2, 50, 20)), panelRad = new JPanel(new FlowLayout()),
			panelTitleLeft = new JPanel(), panelTitleRight = new JPanel(), panelButtonUpdate = new JPanel(),
			panelButtonChange = new JPanel();

	JTextField txtUsername = new JTextField(), txtEmail = new JTextField(), txtPhone = new JTextField();

	JPasswordField txtOldPassword = new JPasswordField(), txtNewPassword = new JPasswordField(),
			txtConfirmPassword = new JPasswordField();

	ButtonGroup groupGender = new ButtonGroup();

	JRadioButton radMale = new JRadioButton("Male"), radFemale = new JRadioButton("Female");

	JButton btnUpdate = new JButton("Update Profile"), btnChange = new JButton("Change Password");

	JTextArea txtAreaAddress = new JTextArea();

	private Connect con = new Connect();

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

	// BUTTONS

	public EditProfileForm() {
		super("Edit Profile", false, true, false);
		setSize(new Dimension(1050, 800));
		setLayout(new GridLayout(0, 2, 0, 10));

		txtUsername.setText(LoggedUser.userName);
		txtEmail.setText(LoggedUser.userEmail);
		txtAreaAddress.setText(LoggedUser.userAddress);
		txtPhone.setText(LoggedUser.userPhone);

		if (LoggedUser.userGender.equalsIgnoreCase("Male")) {
			radMale.setSelected(true);
		} else {
			radFemale.setSelected(true);
		}

		// LEFT
		groupGender.add(radFemale);
		groupGender.add(radMale);
		panelRad.add(radFemale);
		panelRad.add(radMale);
		panelTitleLeft.add(new JLabel("Update Profile"));
		panelButtonUpdate.add(btnUpdate);

		panelLeftGrid.add(new JLabel("Username"));
		panelLeftGrid.add(txtUsername);
		panelLeftGrid.add(new JLabel("User Email"));
		panelLeftGrid.add(txtEmail);
		panelLeftGrid.add(new JLabel("User Phone"));
		panelLeftGrid.add(txtPhone);
		panelLeftGrid.add(new JLabel("User Address"));
		panelLeftGrid.add(txtAreaAddress);
		panelLeftGrid.add(new JLabel("User Gender"));
		panelLeftGrid.add(panelRad);

		panelLeft.add(panelTitleLeft, BorderLayout.NORTH);
		panelLeft.add(panelLeftGrid, BorderLayout.CENTER);
		panelLeft.add(panelButtonUpdate, BorderLayout.SOUTH);

		// RIGHT
		panelTitleRight.add(new JLabel("Change Password"));
		panelButtonChange.add(btnChange);

		panelRightGrid.add(new JLabel("Old Password"));
		panelRightGrid.add(txtOldPassword);
		panelRightGrid.add(new JLabel("New Password"));
		panelRightGrid.add(txtNewPassword);
		panelRightGrid.add(new JLabel("Confirm Password"));
		panelRightGrid.add(txtConfirmPassword);

		panelRight.add(panelTitleRight, BorderLayout.NORTH);
		panelRight.add(panelRightGrid, BorderLayout.CENTER);
		panelRight.add(panelButtonChange, BorderLayout.SOUTH);

		panelLeft.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panelRight.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		add(panelLeft);
		add(panelRight);

		panelLeftGrid.setBackground(Color.CYAN);
		panelRightGrid.setBackground(Color.CYAN);
		panelButtonChange.setBackground(Color.CYAN);
		panelButtonUpdate.setBackground(Color.CYAN);
		panelTitleLeft.setBackground(Color.CYAN);
		panelTitleRight.setBackground(Color.CYAN);
		panelLeft.setBackground(Color.CYAN);
		panelRight.setBackground(Color.CYAN);

		// BUTTONS
		btnUpdate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String userName = txtUsername.getText();
				String phone = txtPhone.getText();
				String email = txtEmail.getText();
				String address = txtAreaAddress.getText();
				String gender = radMale.isSelected() ? radMale.getText()
						: radFemale.isSelected() ? radFemale.getText() : null;

				if (userName.length() < 5 || userName.length() > 30) {
					JOptionPane.showMessageDialog(null, "User Name must be between 5 - 30 characters");
				} else if (!checkEmailValidity(email)) {
					JOptionPane.showMessageDialog(null, "Email is not valid");
				} else if (!checkPhoneValidity(phone)) {
					JOptionPane.showMessageDialog(null, "Phone must be numeric and more than equals 12 digits");
				} else if (!address.endsWith(" Street") || address.length() < 10) {
					JOptionPane.showMessageDialog(null,
							"Address must consist of 10 or more characters and ends with ‘ Street’");
				} else {
					int confirm = JOptionPane.showConfirmDialog(null, "Are you sure want to update profile?", "Confirmation Message",
							JOptionPane.OK_CANCEL_OPTION);
					System.out.println(confirm);
					if (confirm == JOptionPane.OK_OPTION) {
						con.updateUser(LoggedUser.userID, userName, email, phone, address, LoggedUser.userPassword,
								gender, LoggedUser.userRole);
						JOptionPane.showMessageDialog(null, "Update Success");
						LoggedUser.userName = userName;
						LoggedUser.userEmail = email;
						LoggedUser.userPhone = phone;
						LoggedUser.userAddress = address;
						LoggedUser.userGender = gender;
					}

				}

			}
		});

		btnChange.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String oldPassword = new String(txtOldPassword.getPassword());
				String newPassword = new String(txtNewPassword.getPassword());
				String confirmPassword = new String(txtConfirmPassword.getPassword());

				if (!oldPassword.equals(LoggedUser.userPassword)) {
					JOptionPane.showMessageDialog(null, "Old Password must match with user current password");
				} else if (!checkPasswordValidity(newPassword)) {
					JOptionPane.showMessageDialog(null,
							"New Password must 5 - 30 length of character and digit (must at least contain 1 character and 1 digit)");
				} else if (!newPassword.equals(confirmPassword)) {
					JOptionPane.showMessageDialog(null, "Confirmation Password must match with New Password");
				} else {
					int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to update ?", "Confirmation Message",
							JOptionPane.OK_CANCEL_OPTION);
					System.out.println(confirm);
					if (confirm == JOptionPane.OK_OPTION) {
						con.updateUser(LoggedUser.userID, LoggedUser.userName, LoggedUser.userEmail,
								LoggedUser.userPhone, LoggedUser.userAddress, newPassword, LoggedUser.userGender,
								LoggedUser.userRole);
						JOptionPane.showMessageDialog(null, "Update Success!");
						LoggedUser.userPassword = newPassword;
					}

				}

			}
		});

		setResizable(true);

	}

}
