package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import util.Connect;

public class LoginForm extends JFrame{
	JPanel pnlMain = new JPanel(new BorderLayout());
	JPanel pnlLblTitle = new JPanel();
	JPanel pnlCenter = new JPanel(new GridLayout(2,2,50,20));
	JPanel pnlSouth = new JPanel(new GridLayout(0,1));
	JPanel pnlBtnLogin = new JPanel();
	JPanel pnlLblSignUp = new JPanel();
	
	JLabel lblTitle, lblEmail, lblPassword, lblSignUp;
	JButton btnLogin;
	JTextField txtEmail;
	JPasswordField txtPassword;
	
	Connect con = new Connect();
	
	public LoginForm() {
		setSize(450, 650);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Login Form");
		setSize(500, 300);
		add(pnlMain);
		
		//NORTH
		lblTitle = new JLabel("Login Form");
		lblTitle.setFont(new Font("Monospace", Font.CENTER_BASELINE, 18));
		pnlLblTitle.add(lblTitle);
		pnlLblTitle.setBackground(Color.CYAN);
		pnlMain.add(pnlLblTitle, BorderLayout.NORTH);
		
		//CENTER
		lblEmail = new JLabel("Email");
		lblPassword = new JLabel("Password");
		
		txtEmail = new JTextField();
		txtPassword = new JPasswordField();
		
		pnlCenter.add(lblEmail);
		pnlCenter.add(txtEmail);
		pnlCenter.add(lblPassword);
		pnlCenter.add(txtPassword);
		pnlCenter.setBackground(Color.CYAN);
		pnlCenter.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		pnlMain.add(pnlCenter, BorderLayout.CENTER);
		
		//SOUTH
		btnLogin = new JButton("Login");
		lblSignUp = new JLabel("Sign Up Here");
		
		pnlBtnLogin.add(btnLogin);
		pnlLblSignUp.add(lblSignUp);
		
		pnlBtnLogin.setBackground(Color.CYAN);
		pnlLblSignUp.setBackground(Color.CYAN);
		
		pnlSouth.add(pnlBtnLogin);
		pnlSouth.add(pnlLblSignUp);
		
		pnlMain.add(pnlSouth, BorderLayout.SOUTH);
		
		btnLogin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				String email = txtEmail.getText();
				String password = new String(txtPassword.getPassword());
				if(email.length() == 0) {
					JOptionPane.showMessageDialog(null, "Email must be filled");
				}
				else if(password.length() == 0) {
					JOptionPane.showMessageDialog(null, "Password must be filled");
				}
				else {
					con.rs = con.execQuery("SELECT * FROM `users` where (UserEmail LIKE '"+ email + "') AND UserPassword LIKE '" + password + "'");
					try {
						
						if(con.rs.next()) {
							JOptionPane.showMessageDialog(null, "Welcome, " + con.rs.getObject("UserName"));
							LoggedUser.userID = (String) con.rs.getObject("UserID");
							LoggedUser.userEmail = (String) con.rs.getObject("UserEmail");
							LoggedUser.userName = (String) con.rs.getObject("UserName");
							LoggedUser.userGender = (String) con.rs.getObject("UserGender");
							LoggedUser.userPassword = (String) con.rs.getObject("UserPassword");
							LoggedUser.userPhone = (String) con.rs.getObject("UserPhone");
							LoggedUser.userRole = (String) con.rs.getObject("UserRole");
							LoggedUser.userAddress = (String) con.rs.getObject("UserAddress");
							
							dispose();
							new MainForm();
						}
						else {
							JOptionPane.showMessageDialog(null, "Wrong email/password!");
						}
					} catch (SQLException err) {
						// TODO Auto-generated catch block
						err.printStackTrace();
						}
				}
			}
		});
		
		lblSignUp.addMouseListener(new MouseListener() {
			
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
				new RegistrationForm();
			}
		});
		
		setVisible(true);
	}
	
}
