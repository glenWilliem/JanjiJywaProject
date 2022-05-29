package internalFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import frame.LoggedUser;
import util.Connect;

public class TransactionForm extends JInternalFrame{
	JTable tblTransaction, tblDetail;
	
	DefaultTableModel dtmTransaction, dtmDetail;
	
	JScrollPane scrTransaction, scrDetail;
	
	JTextField txtSelectedID = new JTextField(), txtTotal = new JTextField();
	
	String[] transactionHeader = {"Transaction ID", "User ID", "Transaction Date"}, 
			detailHeader = {"Transaction ID", "Beverage ID", "Beverage Name", "Beverage Type", "Beverage Price", "Beverage Quantity", "Sub Total"};
	
	private Connect con = new Connect();
	
	void setTables() {
		tblTransaction = new JTable();
		tblTransaction.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				if (tblTransaction.getSelectedRow() > -1) {
					getDetailData(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 0).toString());
					txtSelectedID.setText(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 0).toString());
				}
			}
		});
		getHeaderData();
		
		dtmDetail = new DefaultTableModel(detailHeader, 0){
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		
		tblDetail = new JTable(dtmDetail);
		
	}

	void getHeaderData() {
		dtmTransaction = new DefaultTableModel(transactionHeader, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		ResultSet rs =  con.execQuery("SELECT * FROM headertransactions WHERE UserID = '" + LoggedUser.userID + "'");
		try {
			while (con.rs.next()) {
				Vector<Object> datas = new Vector<>();
				for (int i = 1; i <= con.rsm.getColumnCount(); i++) {
					datas.add(con.rs.getObject(i) + "");
				}
				dtmTransaction.addRow(datas);
			}
			tblTransaction.setModel(dtmTransaction);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void getDetailData(String transactionID) {
		dtmDetail = new DefaultTableModel(detailHeader, 0){
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		ResultSet rs =  con.execQuery("SELECT TransactionID, beverages.BeverageID, beverages.BeverageName, beverages.BeverageType, beverages.BeveragePrice, detailtransactions.Quantity "
				+ "FROM detailtransactions JOIN beverages ON detailtransactions.BeverageID = beverages.BeverageID "
				+ "WHERE TransactionID = '" + transactionID + "'");
		try {
			int total = 0;
			while (con.rs.next()) {

				Vector<Object> datas = new Vector<>();
				for (int i = 1; i <= con.rsm.getColumnCount(); i++) {
					datas.add(con.rs.getObject(i) + "");
				}
				datas.add(Integer.parseInt(datas.get(4).toString()) * Integer.parseInt(datas.get(5).toString()));
				total += Integer.parseInt(datas.get(4).toString()) * Integer.parseInt(datas.get(5).toString());
				dtmDetail.addRow(datas);
			}
			tblDetail.setModel(dtmDetail);
			txtTotal.setText("" + total);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	void setTextFields() {
		txtSelectedID.setEditable(false);
		txtTotal.setEditable(false);
	}
	
	public TransactionForm() {
		super("Transaction History", false, true, false);
		setSize(new Dimension(1050, 800));
		setLayout(new BorderLayout());
		setTextFields();
		setTables();
		
		//NORTH
		JPanel pnlNorth = new JPanel();
		pnlNorth.add(new JLabel("Transaction History"));
		add(pnlNorth, BorderLayout.NORTH);
		
		//CENTER
		JPanel pnlCenter = new JPanel(new GridLayout(0,1));
		
		scrTransaction = new JScrollPane(tblTransaction);
		JPanel pnlTblTr = new JPanel();
		pnlTblTr.add(scrTransaction);
		pnlTblTr.setBackground(Color.CYAN);
		
		pnlCenter.add(pnlTblTr);
		scrTransaction.setPreferredSize(new Dimension(900, 150));
		
		JPanel pnlSelectedID = new JPanel(new FlowLayout(FlowLayout.LEFT));
		txtSelectedID.setPreferredSize(new Dimension(150, 20));
		pnlSelectedID.add(new JLabel("Selected ID"));
		pnlSelectedID.add(txtSelectedID);
		pnlCenter.add(pnlSelectedID);
		
		add(pnlCenter, BorderLayout.CENTER);
		
		//SOUTH
		JPanel pnlSouth = new JPanel(new GridLayout(0,1));
		dtmDetail = new DefaultTableModel(detailHeader, 0);
		tblDetail = new JTable(dtmDetail);
		tblDetail.setModel(dtmDetail);
		
		scrDetail = new JScrollPane(tblDetail);
		JPanel pnlTblDt = new JPanel();
		pnlTblDt.add(scrDetail);
		pnlTblDt.setBackground(Color.CYAN);
		
		pnlSouth.add(pnlTblDt);
		scrDetail.setPreferredSize(new Dimension(900, 150));
		
		JPanel pnlTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		txtTotal.setPreferredSize(new Dimension(150, 20));
		pnlTotal.add(new JLabel("Grand Total"));
		pnlTotal.add(txtTotal);
		pnlSouth.add(pnlTotal);
		
		add(pnlSouth, BorderLayout.SOUTH);
		
		pnlTotal.setBackground(Color.CYAN);
		pnlSelectedID.setBackground(Color.CYAN);
		pnlNorth.setBackground(Color.CYAN);
		
		setResizable(true);
	}

}
