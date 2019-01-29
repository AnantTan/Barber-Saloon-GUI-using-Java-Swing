package barberShop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class VerifyOrDeletePerson {

	private static Connection connection;
	private static String user;// used in admin dashboard delete user window (user)

	public VerifyOrDeletePerson() {
		connection = Connections.connection;
	}

	public void setUser(String user) {
		VerifyOrDeletePerson.user = user;
	}

	private String getUser() {
		return user;
	}

	public String[] populateVerifyBarber() {
		String[] verification = null;
		// get the count to set array length
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT COUNT(email_address) FROM barber WHERE verification = 'Pending'");
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			int arrayLength = resultSet.getInt(1);// get the array length
			if (resultSet.isBeforeFirst()) {
				return verification;
			} else {
				try {
					verification = new String[arrayLength];
					PreparedStatement preparedSTatement = connection
							.prepareStatement("SELECT first_name,last_name,email_address,location"
									+ " FROM barber WHERE verification = 'Pending' ORDER BY 'ASC'");
					ResultSet resultSEt = preparedSTatement.executeQuery();
					int index = 0;
					while (resultSEt.next()) {
						verification[index] = resultSEt.getString(1).concat(" " + resultSEt.getString(2)
								.concat(" " + resultSEt.getString(3).concat(" " + resultSEt.getString(4))));
						index++;
					}
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		return verification;
	}

	public void verifyBarber(String barberEmail) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(
					"UPDATE barber SET verification = 'Verified' " + "WHERE email_address = '" + barberEmail + "'");
			int count = preparedStatement.executeUpdate();
			if (count > 0) {
				JOptionPane.showMessageDialog(null, barberEmail + " Is now a verified Barber");
				AdminDashboard adminDashboard = new AdminDashboard();
				adminDashboard.populateVerifyBox();
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	public String[] populateDeleteUserBox(String role) {
		String[] users = null;
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT COUNT(email_address) FROM credentials " + "WHERE role = '" + role + "'");
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			int arrayLength = resultSet.getInt(1);
			if (resultSet.isBeforeFirst()) {
				return users;
			} else {
				users = new String[arrayLength];
				try {
					PreparedStatement preparedSTatement = connection.prepareStatement(
							"SELECT email_address FROM credentials " + "WHERE role = '" + role + "' ORDER BY 'ASC'");
					ResultSet resultSEt = preparedSTatement.executeQuery();
					int index = 0;
					while (resultSEt.next()) {
						users[index] = resultSEt.getString(1);
						index++;
					}

				} catch (SQLException e) {
					System.out.println(e);
				}

			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		return users;
	}

	public void deleteUser(String email) {
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("DELETE FROM credentials WHERE email_address = '" + email + "'");
			int count = preparedStatement.executeUpdate();
			if (count > 0) {
				JOptionPane.showMessageDialog(null, "User Deleted Successfully");
				AdminDashboard adminDashboard = new AdminDashboard();
				System.out.println("getuser '" + getUser());
				adminDashboard.deleteUserWindow(getUser());// return to window being refreshed

			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	public String[][] newBarber() {
		String[][] newBarberList = null;
		// get the count to set array length
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT COUNT(email_address) FROM barber WHERE verification = 'Pending'");
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			int arrayLength = resultSet.getInt(1);// get the array length
			if (resultSet.isBeforeFirst() || arrayLength == 0) {
				return newBarberList;
			} else {
				try {
					newBarberList = new String[arrayLength][5];
					PreparedStatement preparedSTatement = connection
							.prepareStatement("SELECT first_name,last_name,phone_number,email_address,location"
									+ " FROM barber WHERE verification = 'Pending' ORDER BY 'ASC'");
					ResultSet resultSEt = preparedSTatement.executeQuery();
					if (resultSEt.next()) {
						int i = 0;
						do {
							for (int j = 0; j < 5; j++) {
								newBarberList[i][j] = resultSEt.getString(j + 1);
							}
							i++;
						} while (resultSEt.next());
					}
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		return newBarberList;
	}
}
