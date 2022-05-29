package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import internalFrame.BuyBeverageForm;
import internalFrame.EditProfileForm;
import internalFrame.ManageBeverageForm;
import internalFrame.TransactionForm;

public class MainForm extends JFrame {

	JLabel lblMessage;
	JPanel pnlMessage;

	JMenuBar menuBar = new JMenuBar();

	JMenu menuProfile = new JMenu("Profile");
	JMenu menuManage = new JMenu("Manage");
	JMenu menuTransaction = new JMenu("Transaction");

	JMenuItem menuItemEditProfile = new JMenuItem("Edit Profile");
	JMenuItem menuItemLogoff = new JMenuItem("Log Off");
	JMenuItem menuItemExit = new JMenuItem("Exit");
	JMenuItem menuItemBuyBeverage = new JMenuItem("Buy Beverage");
	JMenuItem menuItemViewHistory = new JMenuItem("View Transaction History");
	JMenuItem menuItemManage = new JMenuItem("Manage Beverage");

	JDesktopPane desktopPane;

	EditProfileForm profileInternalFrame;
	BuyBeverageForm buyInternalFrame;
	TransactionForm transactionInternalFrame;
	ManageBeverageForm manageInternalFrame;

	void setMenuBar() {
		menuBar.add(menuProfile);
		menuProfile.add(menuItemEditProfile);
		menuProfile.add(menuItemLogoff);
		menuProfile.add(menuItemExit);

		if (LoggedUser.userRole.equalsIgnoreCase("Customer")) {
			menuBar.add(menuTransaction);

			menuTransaction.add(menuItemBuyBeverage);
			menuItemBuyBeverage.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					buyInternalFrame = new BuyBeverageForm();
					desktopPane.add(buyInternalFrame);
					buyInternalFrame.setLocation((desktopPane.getWidth() - buyInternalFrame.getWidth()) / 2,
							(desktopPane.getHeight() - buyInternalFrame.getHeight()) / 2);
					buyInternalFrame.setVisible(true);
				}
			});

			menuItemViewHistory.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					transactionInternalFrame = new TransactionForm();
					desktopPane.add(transactionInternalFrame);
					transactionInternalFrame.setLocation(
							(desktopPane.getWidth() - transactionInternalFrame.getWidth()) / 2,
							(desktopPane.getHeight() - transactionInternalFrame.getHeight()) / 2);
					transactionInternalFrame.setVisible(true);
				}
			});

			menuTransaction.add(menuItemViewHistory);
		}

		else if (LoggedUser.userRole.equalsIgnoreCase("Admin")) {
			menuBar.add(menuManage);
			menuManage.add(menuItemManage);
			menuItemManage.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					manageInternalFrame = new ManageBeverageForm();
					desktopPane.add(manageInternalFrame);
					manageInternalFrame.setLocation((desktopPane.getWidth() - manageInternalFrame.getWidth()) / 2,
							(desktopPane.getHeight() - manageInternalFrame.getHeight()) / 2);
					manageInternalFrame.setVisible(true);
				}
			});

		}

		setJMenuBar(menuBar);

		menuItemEditProfile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				profileInternalFrame = new EditProfileForm();
				desktopPane.add(profileInternalFrame);
				profileInternalFrame.setLocation((desktopPane.getWidth() - profileInternalFrame.getWidth()) / 2,
						(desktopPane.getHeight() - profileInternalFrame.getHeight()) / 2);
				profileInternalFrame.setVisible(true);
			}
		});

		menuItemLogoff.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();

				new LoginForm();
			}
		});

		menuItemExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});

	}
	
	void setMessage() {
		String userName = LoggedUser.userName;
		lblMessage = new JLabel("Welcome to Janji Jywa, " + userName, SwingConstants.CENTER);
		lblMessage.setFont(new Font("Monospace", Font.CENTER_BASELINE, 30));
		lblMessage.setForeground(Color.WHITE);
		pnlMessage = new JPanel(new BorderLayout());
		pnlMessage.add(lblMessage);
		pnlMessage.setBackground(Color.BLUE);		
		add(pnlMessage, BorderLayout.NORTH);
	}

	public MainForm() {
		setMenuBar();
		desktopPane = new JDesktopPane();
		desktopPane.setBackground(Color.BLUE);
		
		setSize(new Dimension(1600, 950));
		setLayout(new BorderLayout());
		setTitle("Janji Jywa");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		add(desktopPane, BorderLayout.CENTER);
		
		setMessage();
		
		setVisible(true);
	}
}
