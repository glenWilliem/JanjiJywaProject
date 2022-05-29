package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Connect {

	private Connection con;
	private Statement st;

	public ResultSet rs;
	public ResultSetMetaData rsm;

	public PreparedStatement ps;

	public Connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/janji_jywa", "root", "");

			st = con.createStatement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ResultSet execQuery(String query) {
		try {
			rs = st.executeQuery(query);
			rsm = rs.getMetaData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public void registerUser(String id, String username, String email, String password, String dob, String gender, String address,
			String phone, String role) {
		try {
			ps = con.prepareStatement("INSERT INTO users VALUES (?,?,?,?,?,?,?,?,?)");
			ps.setString(1, id);
			ps.setString(2, username);
			ps.setString(3, email);
			ps.setString(4, password);
			ps.setString(5, dob);
			ps.setString(6, gender);
			ps.setString(7, address);
			ps.setString(8, phone);
			ps.setString(9, role);

			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertBeverage(String BeverageID, String BeverageName, String BeverageType, int BeveragePrice,
			int BeverageStock) {
		try {

			String query = "INSERT INTO beverages VALUES (?, ?, ?, ?, ?)";

			ps = con.prepareStatement(query);
			ps.setString(1, BeverageID);
			ps.setString(2, BeverageName);
			ps.setString(3, BeverageType);
			ps.setInt(4, BeveragePrice);
			ps.setInt(5, BeverageStock);

			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void removeBeverage(String BeverageID) {
		try {

			String query = "DELETE FROM beverages WHERE beverageID = ?";

			ps = con.prepareStatement(query);

			ps.setString(1, BeverageID);

			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateBeverage(String BeverageID, String BeverageName, String BeverageType, int BeveragePrice,
			int BeverageStock) {
		try {

			String query = "UPDATE beverages SET " + "BeverageName = ?, " + "BeverageType = ?, " + "BeveragePrice = ?, "
					+ "BeverageStock = ? " + " WHERE BeverageID = ?";

			ps = con.prepareStatement(query);

			ps.setString(1, BeverageName);
			ps.setString(2, BeverageType);
			ps.setInt(3, BeveragePrice);
			ps.setInt(4, BeverageStock);
			ps.setString(5, BeverageID);

			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void insertCart(String BeverageID, String UserID, int BeverageQuantity) {
		try {

			String query = "INSERT INTO carts VALUES (?, ?, ?)";

			ps = con.prepareStatement(query);

			ps.setString(1, UserID);
			ps.setString(2, BeverageID);
			ps.setInt(3, BeverageQuantity);

			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateCart(String BeverageID, String UserID, int BeverageQuantity) {
		try {

			String query = "UPDATE carts SET " + "Quantity = ? " + " WHERE UserID = ? AND BeverageID = ?";

			ps = con.prepareStatement(query);

			ps.setInt(1, BeverageQuantity);
			ps.setString(2, UserID);
			ps.setString(3, BeverageID);

			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void removeCart(String BeverageID, String UserID, int BeverageQuantity) {
		try {

			String query = "DELETE FROM carts WHERE UserID = ? AND BeverageID = ?";

			ps = con.prepareStatement(query);

			ps.setString(1, UserID);
			ps.setString(2, BeverageID);

			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void clearCart(String UserID) {
		try {

			String query = "DELETE FROM carts WHERE UserID = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, UserID);

			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String insertHeaderTransaction(String UserID) {

		String transactionID = "TR000";
		Date date = new Date();
		try {
			transactionID = "TR";
			ResultSet rs = execQuery(
					"SELECT RIGHT(TransactionID, 3) AS TransactionID FROM headertransactions ORDER BY TransactionID DESC");
			if (rs.next()) {
				transactionID += String.format("%03d", (Integer.parseInt(rs.getObject(1).toString()) + 1));
			} else
				transactionID += "001";

			String query = "INSERT INTO headertransactions VALUES (?, ?, ?)";

			ps = con.prepareStatement(query);

			ps.setString(1, transactionID);
			ps.setString(2, UserID);
			ps.setDate(3, new java.sql.Date(date.getTime()));

			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return transactionID;
	}

	public void insertDetailTransaction(String TransactionID, String BeverageID, int Quantity) {
		try {
			String query = "INSERT INTO detailtransactions VALUES (?, ?, ?)";

			ps = con.prepareStatement(query);

			ps.setString(1, TransactionID);
			ps.setString(2, BeverageID);
			ps.setInt(3, Quantity);

			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateUser(String UserID, String UserName, String UserEmail, String UserPhone, String UserAddress,
			String UserPassword, String UserGender, String UserRole) {
		try {

			String query = "UPDATE users SET " + "UserName = ?, " + "UserEmail = ?, " + "UserPhone = ?, "
					+ "UserAddress = ?, " + "UserPassword = ?, " + "UserGender = ? " + " WHERE UserID = ?";

			ps = con.prepareStatement(query);

			ps.setString(1, UserName);
			ps.setString(2, UserEmail);
			ps.setString(3, UserPhone);
			ps.setString(4, UserAddress);
			ps.setString(5, UserPassword);
			ps.setString(6, UserGender);
			ps.setString(7, UserID);

			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
