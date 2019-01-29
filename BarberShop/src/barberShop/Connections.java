package barberShop;

import java.awt.Window;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Connections {

	static String firstName, lastName, phoneNum, emailID, password, location;
	static String roleOfUser;
	static Connection connection;
	private PreparedStatement prepareStatement;
	private Window userTypeWindow = LoginRegisterPage.userType;
	private Window loginWindow = LoginRegisterPage.loginFrame;
	private Window frontWindow = FrontPage.frame;
	private Window cusOrBarRegisterWindow = LoginRegisterPage.registerFrame;
	private Window adminRegisterWindow = LoginRegisterPage.adminRegisterFrame;

	public Connections() {
		roleOfUser = LoginRegisterPage.roleOfUser;
		emailID = LoginRegisterPage.emailTextField.getText();
		password = LoginRegisterPage.passwordField.getText();
		sqlConnect();
		if (!FrontPage.register) {
			populateUpdateWindow();
		} else
			detailsOFTheUser();
	}

	public void sqlConnect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:MySql://127.0.0.1:3306/barbershop", "root", "");
			if (!connection.isClosed()) {
				JOptionPane.showMessageDialog(null, "Welcome to barbershop");// ensuring the connectivity
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString());
			System.exit(0);
		}
	}

	private void populateUpdateWindow() {

		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT * FROM " + roleOfUser + " WHERE email_address = '" + emailID + "'"); // Statement
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				firstName = resultSet.getString(1);
				lastName = resultSet.getString(2);
				phoneNum = resultSet.getString(3);
				emailID = resultSet.getString(4);
				location = resultSet.getString(5);
			}
		} catch (SQLException e) {
		}
	}

	private void detailsOFTheUser() {

		password = LoginRegisterPage.passwordField.getText();
		firstName = LoginRegisterPage.firstNameTextField.getText();
		lastName = LoginRegisterPage.lastNameTextField.getText();
		phoneNum = LoginRegisterPage.phoneNumberTextField.getText();
		location = LoginRegisterPage.locationOfBarberField.getText();
	}

	public void login() {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM credentials where role = '"
					+ roleOfUser + "'" + "and email_address = '" + emailID + "'");
			ResultSet resultSet = preparedStatement.executeQuery();
			if (!resultSet.isBeforeFirst()) {
				JOptionPane.showMessageDialog(null, "Wrong Email or Password", "Access Declined",
						JOptionPane.ERROR_MESSAGE);
			} else {
				while (resultSet.next()) {
					if (resultSet.getString(2).equals(emailID) && resultSet.getString(3).equals(password)) {
						// ActionOfButtons.resetEmailPass();
						if (roleOfUser.equals("administrator")) {
							AdminDashboard adminDashboard = new AdminDashboard();
							loginWindow.dispose();// close the login frame
							frontWindow.dispose();
							userTypeWindow.dispose();
							adminDashboard.dashboard();
							return;// Once password Id match stop checking
						} else if (roleOfUser.equals("customer")) {
							CustomerDashboard customerDashboard = new CustomerDashboard();
							loginWindow.dispose();// close the login frame
							frontWindow.dispose();
							userTypeWindow.dispose();
							customerDashboard.dashboard();
							return;// Once password Id match stop checking
						} else if (roleOfUser.equals("barber")) {
							PreparedStatement preparedStatement2 = connection.prepareStatement(
									"SELECT verification FROM barber " + "WHERE email_address = '" + emailID + "'");
							ResultSet resultSet2 = preparedStatement2.executeQuery();
							while (resultSet2.next()) {
								if (resultSet2.getString(1).equals("Verified"))// check if barber is verified
								{
									loginWindow.dispose();// close the login frame
									frontWindow.dispose();
									userTypeWindow.dispose();
									new BarberDashboard();
									return;// Once password Id match stop checking
								} else {
									JOptionPane.showMessageDialog(null, "You are not verified by the Admin");
									loginWindow.dispose();
									userTypeWindow.dispose();
								}
							}
						}
					} else
						JOptionPane.showMessageDialog(null, "Wrong Email or Password", "Access Declined",
								JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	public void registeration() {
		int count = 0;
		try {
			prepareStatement = connection.prepareStatement("INSERT INTO credentials VALUES(?,?,?)");

			prepareStatement.setString(1, roleOfUser);
			prepareStatement.setString(2, emailID);
			prepareStatement.setString(3, password);
			count = prepareStatement.executeUpdate();

			if (roleOfUser.equals("administrator") && count > 0) {
				ActionOfButtons.resetEmailPass();
				JOptionPane.showMessageDialog(null, "New Administrator Successfully Added");
				adminRegisterWindow.dispose();
			}
			if (roleOfUser.equals("barber")) {

				prepareStatement = connection.prepareStatement("INSERT INTO barber VALUES(?,?,?,?,?,?)");
				prepareStatement.setString(1, firstName);
				prepareStatement.setString(2, lastName);
				prepareStatement.setString(3, phoneNum);
				prepareStatement.setString(4, emailID);
				prepareStatement.setString(5, location);
				prepareStatement.setString(6, "Pending");
				count = prepareStatement.executeUpdate();
				if (count > 0) {
					ActionOfButtons.resetEmailPass();
					JOptionPane.showMessageDialog(null, "Your Verfication is Pending");
					cusOrBarRegisterWindow.dispose();// remove the register window
					userTypeWindow.dispose();// remove the select user window

				}
			} else if (roleOfUser.equals("customer")) {
				prepareStatement = connection.prepareStatement("INSERT INTO customer VALUES(?,?,?,?)");
				prepareStatement.setString(1, firstName);
				prepareStatement.setString(2, lastName);
				prepareStatement.setString(3, phoneNum);
				prepareStatement.setString(4, emailID);
				count = prepareStatement.executeUpdate();
				if (count > 0) {
					ActionOfButtons.resetEmailPass();
					JOptionPane.showMessageDialog(null, "Registeration Successfull! Please Login");
					cusOrBarRegisterWindow.dispose();
					userTypeWindow.dispose();
				}
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Details You provided are invalid", "Invalid",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}