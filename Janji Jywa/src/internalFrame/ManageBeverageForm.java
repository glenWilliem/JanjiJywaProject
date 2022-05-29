package internalFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import util.Connect;

public class ManageBeverageForm extends JInternalFrame {

	JTextField txtBevID, txtBevName, txtBevPrice, txtStock, txtID, txtName, txtPrice;

	JComboBox cmbBevType, cmbType;

	JSpinner spinnerBevStock, spinnerAddStock;

	String[] manageHeader = { "Beverage ID", "Beverage Name", " Beverage Type", "Beverage Price", "Beverage Stock" };

	DefaultTableModel dtmManageTbl;

	JTable tblManage;

	JScrollPane scrManageTbl;

	JPanel pnlTitle = new JPanel(), pnlCenter = new JPanel(), pnlSouth = new JPanel(new GridLayout(0, 2));

	SpinnerModel spmNewStock = new SpinnerNumberModel(1, 1, 999, 1), spmAddStock = new SpinnerNumberModel(1, 1, 999, 1);

	Connect con = new Connect();

	String generateBevID() {
		String newID;
		String latestBvgID = "";
		int number = 0;
		
		ResultSet rs = con.execQuery("SELECT COUNT(BeverageID) AS NumberOfRows, max(BeverageID) AS latestID FROM beverages");

		try {
			rs.next();
			number = rs.getInt(1) + 1;
			latestBvgID = rs.getString(2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int a = 0;
		int b = 0;
		int c = number;

		if (number > 9 && number < 100) {
			c = number % 10;
			b = number / 10;
		} else if (number > 99 && number < 1000) {
			c = (number % 100) % 10;
			b = (number / 10) % 10;
			a = number / 100;
		}

		newID = "BE" + a + b + c;
		
		if (newID.equalsIgnoreCase(latestBvgID)) {
			a = 0;
			b = 0;
			c = number+1;

			if (number > 9 && number < 100) {
				c = number % 10;
				b = number / 10;
			} else if (number > 99 && number < 1000) {
				c = (number % 100) % 10;
				b = (number / 10) % 10;
				a = number / 100;
			}
			
			return newID = "BE" + a + b + c;
		} else {
			return newID;
		}
	}
	
	void setTable() {
		tblManage = new JTable();
		tblManage.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				if (tblManage.getSelectedRow() > -1) {
					
					txtID.setText(tblManage.getValueAt(tblManage.getSelectedRow(), 0).toString());
					txtName.setText(tblManage.getValueAt(tblManage.getSelectedRow(), 1).toString());
					cmbType.setSelectedItem(tblManage.getValueAt(tblManage.getSelectedRow(), 2).toString());
					txtPrice.setText(tblManage.getValueAt(tblManage.getSelectedRow(), 3).toString());
					txtStock.setText(tblManage.getValueAt(tblManage.getSelectedRow(), 4).toString());
				}
			}
		});
		getBeverageData();
	}
	
	void getBeverageData() {
		dtmManageTbl = new DefaultTableModel(manageHeader, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		ResultSet rs =  con.execQuery("SELECT * FROM beverages");
		try {
			while (con.rs.next()) {
				Vector<Object> datas = new Vector<>();
				for (int i = 1; i <= con.rsm.getColumnCount(); i++) {
					datas.add(con.rs.getObject(i) + "");
				}
				dtmManageTbl.addRow(datas);
			}
			tblManage.setModel(dtmManageTbl);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void setTextFields() {
		txtBevID.setText(generateBevID());
		txtBevID.setEditable(false);
		txtID.setEditable(false);
		txtStock.setEditable(false);
	}
	
	void emptyTextFields() {
		txtBevID.setText(generateBevID());
		txtBevName.setText("");
		cmbBevType.setSelectedIndex(0);
		txtBevPrice.setText("");
		spinnerBevStock.setValue(1);
		spinnerAddStock.setValue(1);

		txtID.setText("");
		txtName.setText("");
		cmbType.setSelectedIndex(0);
		txtPrice.setText("");
		txtStock.setText("");
	}

	public ManageBeverageForm() {
		super("Manage Beverage", false, true, false);
		setSize(new Dimension(1050, 800));
		setLayout(new BorderLayout());
		
		setTable();

		// NORTH
		JPanel pnlNorth = new JPanel(new BorderLayout());
		pnlTitle.add(new JLabel("Manage Beverage"));
		pnlNorth.add(pnlTitle, BorderLayout.NORTH);

		scrManageTbl = new JScrollPane(tblManage);
		JPanel pnlTblManage = new JPanel();
		pnlTblManage.add(scrManageTbl, BorderLayout.SOUTH);

		pnlNorth.add(scrManageTbl);
		scrManageTbl.setPreferredSize(new Dimension(1000, 150));

		add(pnlNorth, BorderLayout.NORTH);
		pnlTitle.setBackground(Color.CYAN);
		pnlTblManage.setBackground(Color.CYAN);

		// CENTER
		JPanel pnlCenter = new JPanel(new FlowLayout());

		// CENTER WEST
		JPanel centerWest = new JPanel(new BorderLayout());
		JPanel panelNewBeverage = new JPanel(new GridLayout(0, 2));

		JLabel newBevID = new JLabel("New Beverage ID");

		panelNewBeverage.add(newBevID);

		txtBevID = new JTextField();
		panelNewBeverage.add(txtBevID);

		JLabel newBevName = new JLabel("New Beverage Name");
		panelNewBeverage.add(newBevName);

		txtBevName = new JTextField();
		panelNewBeverage.add(txtBevName);

		JLabel newBevType = new JLabel("New Beverage Type");
		panelNewBeverage.add(newBevType);

		String[] bevTypes = { "-Choose a type-", "Boba", "Coffee", "Tea", "Smoothies" };

		cmbBevType = new JComboBox(bevTypes);
		panelNewBeverage.add(cmbBevType);

		JLabel newBevPrice = new JLabel("New Beverage Price");
		panelNewBeverage.add(newBevPrice);

		txtBevPrice = new JTextField();
		panelNewBeverage.add(txtBevPrice);

		JLabel newBevStock = new JLabel("New Beverage Stock");
		panelNewBeverage.add(newBevStock);

		spinnerBevStock = new JSpinner(spmNewStock);
		panelNewBeverage.add(spinnerBevStock);

		JButton btnInsertBev = new JButton("Insert Beverage");

		JButton btnResetBev = new JButton("Reset");

		centerWest.add(panelNewBeverage, BorderLayout.NORTH);
		centerWest.add(btnInsertBev, BorderLayout.CENTER);
		centerWest.add(btnResetBev, BorderLayout.SOUTH);
		panelNewBeverage.setBackground(Color.CYAN);

		// CENTER EAST
		JPanel centerEast = new JPanel(new BorderLayout());

		JPanel MainPanel = new JPanel(new GridLayout(0, 2));

		centerEast.add(MainPanel);

		JLabel lblID = new JLabel("Beverage ID");
		MainPanel.add(lblID);

		txtID = new JTextField();
		MainPanel.add(txtID);

		JLabel lblName = new JLabel("Beverage Name");
		MainPanel.add(lblName);

		txtName = new JTextField();
		MainPanel.add(txtName);

		JLabel lblType = new JLabel("Beverage Type");
		MainPanel.add(lblType);

		String[] Type = { "-Choose a type-", "Boba", "Coffee", "Tea", "Smoothies" };

		cmbType = new JComboBox(Type);
		MainPanel.add(cmbType);

		JLabel lblPrice = new JLabel("Beverage Price");
		MainPanel.add(lblPrice);

		txtPrice = new JTextField();
		MainPanel.add(txtPrice);

		JLabel lblStock = new JLabel("Beverage Stock");
		MainPanel.add(lblStock);

		txtStock = new JTextField();
		MainPanel.add(txtStock);

		JButton btnUpdate = new JButton("Update Beverage");
		MainPanel.add(btnUpdate);

		JButton btnDelete = new JButton("Delete Beverage");
		MainPanel.add(btnDelete);

		//
		JPanel MainPanel1 = new JPanel(new GridLayout(0, 3));

		JLabel LabelAdd = new JLabel("Add Stock");
		MainPanel1.add(LabelAdd);

		spinnerAddStock = new JSpinner(spmAddStock);
		MainPanel1.add(spinnerAddStock);

		JButton btnAddStock = new JButton("Add Stock");
		MainPanel1.add(btnAddStock);

		centerEast.add(MainPanel, BorderLayout.NORTH);
		centerEast.add(MainPanel1, BorderLayout.CENTER);

		pnlCenter.add(centerWest);
		pnlCenter.add(centerEast);

		MainPanel.setBackground(Color.CYAN);
		pnlCenter.setBackground(Color.CYAN);
		MainPanel1.setBackground(Color.CYAN);

		setTextFields();
		add(pnlCenter, BorderLayout.CENTER);
		
		//BUTTONS
		btnUpdate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String beverageID = txtID.getText();
				String beverageName = txtName.getText();
				String beverageType = cmbType.getSelectedItem().toString();
				int beveragePrice = Integer.parseInt(txtPrice.getText());
				
				if(beverageID.length() == 0) {
					JOptionPane.showMessageDialog(null, "Please choose a beverage first");
				}
				else if(beverageName.length() < 3 || beverageName.length() > 25) {
					JOptionPane.showMessageDialog(null, "Beverage Name must consist of 5 - 30 characters");
				}
				else if(cmbType.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(null, "Beverage Type must be chosen either Boba, Coffee, Tea, or Smoothies");
				}
				else if(beveragePrice < 0) {
					JOptionPane.showMessageDialog(null, "Beverage Price must more than 0");
				}
				else {
					int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to update beverage?", "Update Confirmation", JOptionPane.YES_NO_OPTION);
					if(choice == JOptionPane.YES_OPTION) {
						con.updateBeverage(beverageID, beverageName, beverageType, beveragePrice, Integer.parseInt(txtStock.getText()));
						JOptionPane.showMessageDialog(null, "Update Success");
						emptyTextFields();
						getBeverageData();
					}
				}
			}
		});
		
		btnDelete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String beverageID = txtID.getText();
				if(beverageID.length() == 0) {
					JOptionPane.showMessageDialog(null, "Please choose a beverage first");
				}
				else {
					int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove beverage data ?", "Remove Confirmation", JOptionPane.YES_NO_OPTION);
					if(choice == JOptionPane.YES_OPTION) {
						con.removeBeverage(beverageID);
						JOptionPane.showMessageDialog(null, "Remove Success");
						emptyTextFields();
						getBeverageData();
					}
				}
			}
		});
		
		btnAddStock.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String beverageID = txtID.getText();
				String beverageName = tblManage.getValueAt(tblManage.getSelectedRow(), 1).toString();
				String beverageType = cmbType.getSelectedItem().toString();
				int beveragePrice = Integer.parseInt(tblManage.getValueAt(tblManage.getSelectedRow(), 3).toString());
				int beverageStock = Integer.parseInt(txtStock.getText());
				int stockAdded = (Integer)spinnerAddStock.getValue();
				
				if(beverageID.length() == 0) {
					JOptionPane.showMessageDialog(null, "Please choose a beverage first");
				}
				else if(stockAdded < 0) {
					JOptionPane.showMessageDialog(null, "Add Stock must more than 0");
				}
				else {
					int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to add beverage stock ?", "Add Confirmation", JOptionPane.YES_NO_OPTION);
					if(choice == JOptionPane.YES_OPTION) {
						con.updateBeverage(beverageID, beverageName, beverageType, beveragePrice, beverageStock+stockAdded);
						JOptionPane.showMessageDialog(null, "Stock has been successfully updated");
						emptyTextFields();
						getBeverageData();
					}
				}
			}
		});
		
		btnInsertBev.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String beverageID = txtBevID.getText();
				String beverageName = txtBevName.getText();
				String beverageType = cmbBevType.getSelectedItem().toString();
				int beveragePrice = txtBevPrice.getText().length() > 0 ? Integer.parseInt(txtBevPrice.getText()) : 0;
				int beverageStock = (Integer)spinnerBevStock.getValue();
				
				if(beverageID.length() == 0) {
					JOptionPane.showMessageDialog(null, "Please choose a beverage first");
				}
				else if(beverageName.length() < 5 || beverageName.length() > 30) {
					JOptionPane.showMessageDialog(null, "Beverage Name must consist of 5 - 30 characters");
				}
				else if(cmbBevType.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(null, "Beverage Type must be chosen either Boba, Coffee, Tea, or Smoothies");
				}
				else if(beveragePrice <= 0) {
					JOptionPane.showMessageDialog(null, "Beverage Price must more than 0");
				}
				else if(beverageStock <= 0) {
					JOptionPane.showMessageDialog(null, "New Beverage Stock must more than 0");
				}
				else {
					int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to insert new beverage?", "Update Confirmation", JOptionPane.YES_NO_OPTION);
					if(choice == JOptionPane.YES_OPTION) {
						con.insertBeverage(beverageID, beverageName, beverageType, beveragePrice, beverageStock);
						JOptionPane.showMessageDialog(null, "Update Success");
						emptyTextFields();
						getBeverageData();
					}
				}
				
			}
		});
		
		btnResetBev.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				emptyTextFields();
			}
		});
		
		setResizable(true);
	}
}
