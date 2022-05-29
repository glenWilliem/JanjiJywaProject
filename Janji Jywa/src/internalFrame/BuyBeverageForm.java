package internalFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import frame.LoggedUser;
import util.Connect;

public class BuyBeverageForm extends JInternalFrame {
	int selectedCartIndex, selectedBeverageIndex;

	String selectedCartItem;

	JLabel lblTitle;

	JTextField txtBeverageID = new JTextField(), txtBeverageName = new JTextField(), txtBeverageType = new JTextField(),
			txtBeveragePrice = new JTextField(), txtBeverageStock = new JTextField();

	JButton btnAdd = new JButton("Add to Cart"), btnRemove = new JButton("Remove Selected Cart"),
			btnClear = new JButton("Clear Cart"), btnCheckout = new JButton("Checkout");

	JPanel pnlNorth, pnlCenter, pnlCenterLeft, pnlCenterRight, pnlSouth, pnlSouthBtn;

	String[] beverageColumns = { "Beverage ID", "Beverage Name", "Beverage Type", "Beverage Price", "Beverage Stock" },
			cartColumns = { "Beverage ID", "Beverage Name", "Beverage Type", "Beverage Price", "Beverage Stock",
					"Beverage Quantity", "Sub Total" };

	JTable tblBeverage, tblCart;

	JSpinner spinnerQuantity;
	SpinnerModel spmQuantity;

	DefaultTableModel dtmBeverage, dtmCart;

	private Connect con = new Connect();

	void setTextFields() {
		txtBeverageID.setEditable(false);
		txtBeverageName.setEditable(false);
		txtBeverageType.setEditable(false);
		txtBeveragePrice.setEditable(false);
		txtBeverageStock.setEditable(false);
	}

	void emptyTextFields() {
		txtBeverageID.setText("");
		txtBeverageName.setText("");
		txtBeverageType.setText("");
		txtBeveragePrice.setText("");
		txtBeverageStock.setText("");
		spinnerQuantity.setValue(1);
	}

	void setTables() {

		tblBeverage = new JTable();
		tblBeverage.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				if (tblBeverage.getSelectedRow() > -1
						&& Integer.parseInt(tblBeverage.getValueAt(tblBeverage.getSelectedRow(), 4).toString()) > 0) {
					txtBeverageID.setText(tblBeverage.getValueAt(tblBeverage.getSelectedRow(), 0).toString());
					txtBeverageName.setText(tblBeverage.getValueAt(tblBeverage.getSelectedRow(), 1).toString());
					txtBeverageType.setText(tblBeverage.getValueAt(tblBeverage.getSelectedRow(), 2).toString());
					txtBeveragePrice.setText(tblBeverage.getValueAt(tblBeverage.getSelectedRow(), 3).toString());
					txtBeverageStock.setText(tblBeverage.getValueAt(tblBeverage.getSelectedRow(), 4).toString());
				} else
					emptyTextFields();
			}
		});
		tblCart = new JTable();
		tblCart.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				if (tblCart.getSelectedRow() > -1) {
					selectedCartItem = tblCart.getValueAt(tblCart.getSelectedRow(), 0).toString();

				}
			}
		});

		getBeverageData();
		getCartData();
	}

	void getBeverageData() {
		dtmBeverage = new DefaultTableModel(beverageColumns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		ResultSet rs = con.execQuery("SELECT * FROM beverages");
		try {
			while (con.rs.next()) {
				Vector<Object> datas = new Vector<>();
				for (int i = 1; i <= con.rsm.getColumnCount(); i++) {
					datas.add(con.rs.getObject(i) + "");
				}
				dtmBeverage.addRow(datas);
			}
			tblBeverage.setModel(dtmBeverage);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void getCartData() {
		dtmCart = new DefaultTableModel(cartColumns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		ResultSet rs = con.execQuery(
				"SELECT beverages.BeverageID, beverages.BeverageName, beverages.BeverageType, beverages.BeveragePrice, beverages.BeverageStock, carts.Quantity FROM carts JOIN beverages ON carts.BeverageID = beverages.BeverageID where UserID = '"
						+ LoggedUser.userID + "'");
		try {
			while (con.rs.next()) {

				Vector<Object> datas = new Vector<>();
				for (int i = 1; i <= con.rsm.getColumnCount(); i++) {
					datas.add(con.rs.getObject(i) + "");
				}
				datas.add(Integer.parseInt(datas.get(3).toString()) * Integer.parseInt(datas.get(5).toString()));
				dtmCart.addRow(datas);
			}
			tblCart.setModel(dtmCart);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BuyBeverageForm() {
		super("Buy Beverage", false, true, false);
		setSize(new Dimension(1050, 800));
		setLayout(new BorderLayout());

		setTables();

		// NORTH
		pnlNorth = new JPanel();
		pnlNorth.setLayout(new BoxLayout(pnlNorth, BoxLayout.Y_AXIS));
		JPanel pnlTitle = new JPanel();
		lblTitle = new JLabel("Buy Beverage");
		pnlTitle.add(lblTitle);

		JScrollPane scrBvg = new JScrollPane(tblBeverage);

		JPanel pnlBvgTbl = new JPanel();
		pnlBvgTbl.add(scrBvg);
		scrBvg.setPreferredSize(new Dimension(900, 150));

		pnlNorth.add(pnlTitle);
		pnlNorth.add(pnlBvgTbl);
		pnlBvgTbl.setBackground(Color.CYAN);

		add(pnlNorth, BorderLayout.NORTH);

		// CENTER LEFT
		pnlCenter = new JPanel(new GridLayout(0, 2, 20, 20));
		pnlCenterLeft = new JPanel(new GridLayout(0, 2, 10, 100));

		pnlCenterLeft.add(new JLabel("Beverage ID"));
		pnlCenterLeft.add(txtBeverageID);
		pnlCenterLeft.add(new JLabel("Beverage Name"));
		pnlCenterLeft.add(txtBeverageName);
		pnlCenterLeft.add(new JLabel("Beverage Type"));
		pnlCenterLeft.add(txtBeverageType);

		pnlCenter.add(pnlCenterLeft);

		// CENTER RIGHT
		pnlCenterRight = new JPanel(new GridLayout(0, 2, 10, 100));
		spmQuantity = new SpinnerNumberModel(1, 1, 999, 1);
		spinnerQuantity = new JSpinner(spmQuantity);

		pnlCenterRight.add(new JLabel("Beverage Price"));
		pnlCenterRight.add(txtBeveragePrice);
		pnlCenterRight.add(new JLabel("Beverage Stock"));
		pnlCenterRight.add(txtBeverageStock);
		pnlCenterRight.add(new JLabel("Beverage Quantity"));
		pnlCenterRight.add(spinnerQuantity);
		pnlCenter.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		pnlCenter.add(pnlCenterRight);

		add(pnlCenter, BorderLayout.CENTER);

		// SOUTH
		pnlSouth = new JPanel();
		pnlSouthBtn = new JPanel(new FlowLayout());
		pnlSouth.setLayout(new BoxLayout(pnlSouth, BoxLayout.Y_AXIS));

		JPanel pnlBtnAdd = new JPanel();
		pnlBtnAdd.add(btnAdd);
		pnlSouth.add(pnlBtnAdd);

		JScrollPane scrCart = new JScrollPane(tblCart);

		JPanel pnlCartTbl = new JPanel();
		pnlCartTbl.add(scrCart);
		scrCart.setPreferredSize(new Dimension(900, 150));
		pnlCartTbl.setBackground(Color.CYAN);

		pnlSouth.add(pnlCartTbl);

		pnlSouthBtn.add(btnRemove);
		pnlSouthBtn.add(btnClear);
		pnlSouthBtn.add(btnCheckout);

		pnlSouth.add(pnlSouthBtn);
		add(pnlSouth, BorderLayout.SOUTH);

		pnlTitle.setBackground(Color.CYAN);
		pnlCenterLeft.setBackground(Color.CYAN);
		pnlCenterRight.setBackground(Color.CYAN);
		pnlCenter.setBackground(Color.CYAN);
		pnlSouthBtn.setBackground(Color.CYAN);
		pnlBtnAdd.setBackground(Color.CYAN);

		setTextFields();

		setVisible(true);

		// BTUTTONS
		btnAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				selectedBeverageIndex = tblBeverage.getSelectedRow();
				if (selectedBeverageIndex == -1)
					JOptionPane.showMessageDialog(null, "Please select a beverage!");
				else if (Integer
						.parseInt(tblBeverage.getModel().getValueAt(selectedBeverageIndex, 4).toString()) == 0) {
					JOptionPane.showMessageDialog(null, "There is no more stock for this beverage!");
				} else if (Integer.parseInt(tblBeverage.getModel().getValueAt(selectedBeverageIndex, 4)
						.toString()) < (Integer) spinnerQuantity.getValue()) {
					JOptionPane.showMessageDialog(null, "There is not enough stock for this beverage!");
				} else {
					ResultSet rs = con.execQuery("SELECT * FROM carts WHERE BeverageID LIKE '"
							+ tblBeverage.getModel().getValueAt(selectedBeverageIndex, 0) + "' AND UserID LIKE '"
							+ LoggedUser.userID + "'");
					try {
						if (rs.next()) {
							con.updateCart(txtBeverageID.getText(), LoggedUser.userID,
									Integer.parseInt(con.rs.getObject("quantity").toString())
											+ (Integer) spinnerQuantity.getValue());

							con.updateBeverage(txtBeverageID.getText(), txtBeverageName.getText(),
									txtBeverageType.getText(), Integer.parseInt(txtBeveragePrice.getText().toString()),
									Integer.parseInt(tblBeverage.getValueAt(tblBeverage.getSelectedRow(), 4).toString())
											- (Integer) spinnerQuantity.getValue());

							JOptionPane.showMessageDialog(null, "Beverage quantity added!");
						} else {
							con.insertCart(txtBeverageID.getText(), LoggedUser.userID,
									(Integer) spinnerQuantity.getValue());
							con.updateBeverage(txtBeverageID.getText(), txtBeverageName.getText(),
									txtBeverageType.getText(), Integer.parseInt(txtBeveragePrice.getText().toString()),
									Integer.parseInt(tblBeverage.getValueAt(tblBeverage.getSelectedRow(), 4).toString())
											- (Integer) spinnerQuantity.getValue());

							JOptionPane.showMessageDialog(null, "Sucessfully insert Cart!");
						}
						getBeverageData();
						getCartData();
						emptyTextFields();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});

		btnRemove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ResultSet rs = con.execQuery(
						"SELECT beverages.BeverageID, beverages.BeverageName, beverages.BeverageType, beverages.BeveragePrice, beverages.BeverageStock, carts.Quantity FROM carts JOIN beverages ON carts.BeverageID = beverages.BeverageID WHERE beverages.BeverageID LIKE '"
								+ selectedCartItem + "' AND UserID LIKE '" + LoggedUser.userID + "'");
				try {
					while (rs.next()) {
						con.updateBeverage(selectedCartItem, rs.getObject(2).toString(), rs.getObject(3).toString(),
								Integer.parseInt(rs.getObject(4).toString()),
								Integer.parseInt(rs.getObject(5).toString())
										+ Integer.parseInt(rs.getObject(6).toString()));
						con.removeCart(selectedCartItem, LoggedUser.userID,
								Integer.parseInt(rs.getObject(6).toString()));
					}

					getBeverageData();
					emptyTextFields();
					getCartData();
					
					JOptionPane.showMessageDialog(null, "Beverage has been removed!");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		btnCheckout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to checkout cart?",
						"Confirmation Message", JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {

					String transID = con.insertHeaderTransaction(LoggedUser.userID);

					ResultSet rs = con.execQuery(
							"SELECT beverages.BeverageID," + " beverages.BeverageName," + " beverages.BeverageType,"
									+ " beverages.BeveragePrice," + " beverages.BeverageStock," + " carts.Quantity  "
									+ "FROM carts JOIN beverages ON carts.BeverageID = beverages.BeverageID "
									+ "WHERE UserID LIKE '" + LoggedUser.userID + "'");

					try {
						while (rs.next()) {
							con.insertDetailTransaction(transID, rs.getObject(1).toString(),
									Integer.parseInt(rs.getObject(6).toString()));
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					JOptionPane.showMessageDialog(null, "Checkout success!");
					con.clearCart(LoggedUser.userID);
					emptyTextFields();
					getBeverageData();
					getCartData();
				}
			}
		});

		btnClear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int choice = JOptionPane.showConfirmDialog(null, "Are you sure want to clear cart ?",
						"Confirmation Message", JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {
					ResultSet rs = con.execQuery(
							"SELECT beverages.BeverageID," + " beverages.BeverageName," + " beverages.BeverageType,"
									+ " beverages.BeveragePrice," + " beverages.BeverageStock," + " carts.Quantity  "
									+ "FROM carts JOIN beverages ON carts.BeverageID = beverages.BeverageID "
									+ "WHERE UserID LIKE '" + LoggedUser.userID + "'");

					try {
						while (rs.next()) {
							con.updateBeverage(rs.getObject(1).toString(), rs.getObject(2).toString(),
									rs.getObject(3).toString(), Integer.parseInt(rs.getObject(4).toString()),
									Integer.parseInt(rs.getObject(5).toString())
											+ Integer.parseInt(rs.getObject(6).toString()));
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					con.clearCart(LoggedUser.userID);
					JOptionPane.showMessageDialog(null, "Cleared");
					emptyTextFields();
					getBeverageData();
					getCartData();
				}
			}
		});

		setResizable(true);
	}

}
