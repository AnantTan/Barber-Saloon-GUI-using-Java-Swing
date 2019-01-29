package barberShop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class ComplaintAndReview {

	private static Connection connection;

	public ComplaintAndReview() {
		connection = Connections.connection;
	}

	public String[] complaintBarberLocationComboBox() {
		String[] locations = null;
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT COUNT(DISTINCT(location)) FROM barber WHERE verification = 'Verified'");
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			int arrayLength = resultSet.getInt(1);// getting the count from the query
			locations = new String[arrayLength];
		} catch (SQLException e) {
			System.out.println(e);
		}
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT DISTINCT location FROM barber WHERE verification ='Verified'");
			ResultSet resultSet = preparedStatement.executeQuery();
			int index = 0;
			while (resultSet.next()) {
				locations[index] = resultSet.getString(1);
				index++;
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		return locations;
	}

	public String[] populateComplaintBox(String location) {
		String[] barberDetails = null;
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(email_address) FROM barber "
					+ "WHERE verification = 'Verified' AND location = '" + location + "'");
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			int arrayLength = resultSet.getInt(1);
			barberDetails = new String[arrayLength];
			if (resultSet.isBeforeFirst() || arrayLength == 0) {
				return barberDetails;
			} else {
				try {
					PreparedStatement preparedSTatement = connection
							.prepareStatement("SELECT first_name,last_name,email_address"
									+ " FROM barber WHERE verification = 'Verified' AND location = '" + location + "'");
					ResultSet resultSEt = preparedSTatement.executeQuery();
					int index = 0;
					while (resultSEt.next()) {
						barberDetails[index] = resultSEt.getString(1)
								.concat(" " + resultSEt.getString(2).concat(" " + resultSEt.getString(3)));
						index++;
					}

				} catch (SQLException e) {
					System.out.println(e);
				}
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		return barberDetails;
	}

	public boolean sendComplaintOrReview(String customerEmail, String description, String barberEmail, String type) {
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("INSERT INTO complaintandreview VALUES (?,?,?,?)");
			preparedStatement.setString(1, customerEmail);
			preparedStatement.setString(2, description);
			preparedStatement.setString(3, barberEmail);
			preparedStatement.setString(4, type);
			int count = preparedStatement.executeUpdate();
			if (count > 0) {
				JOptionPane.showMessageDialog(null, type + " Sent!");
				return true;
			}

		} catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}

	public String[][] viewComplaintsByAdmin() {
		String[][] complaints = null;
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT COUNT(by_email) FROM complaintandreview WHERE type = 'Complaint'");
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			int arrayLength = resultSet.getInt(1);
			complaints = new String[arrayLength][3];
			if (resultSet.isBeforeFirst() || arrayLength == 0) {
				JOptionPane.showMessageDialog(null, "No Complaints To Show");
				return complaints;
			}
			try {
				PreparedStatement preparedSTatement = connection
						.prepareStatement("SELECT * FROM complaintandreview WHERE type = 'Complaint'");
				ResultSet resultSEt = preparedSTatement.executeQuery();
				int i = 0;
				while (resultSEt.next()) {
					for (int j = 0; j < 3; j++) {
						complaints[i][j] = resultSEt.getString(j + 1);
					}
					i++;
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		return complaints;
	}

	public String[] populateReviewBox(String email) {
		String[] completedAppointment = null;
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(person_email) FROM booking "
					+ "WHERE status = 'Completed' " + "AND person_email = '" + email + "'");
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			int arrayLength = resultSet.getInt(1);
			completedAppointment = new String[arrayLength];
			if (resultSet.isBeforeFirst() || arrayLength == 0) {
				JOptionPane.showMessageDialog(null, "No Bookings Completed");
				return completedAppointment;
			}
			try {
				PreparedStatement preparedSTatement = connection
						.prepareStatement("SELECT booked_with FROM booking WHERE status = 'Completed' "
								+ "AND person_email = '" + email + "'");
				ResultSet resultSEt = preparedSTatement.executeQuery();
				int index = 0;
				while (resultSEt.next()) {
					completedAppointment[index] = resultSEt.getString(1);
					System.out.println("ddddd " + completedAppointment[index]);
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		} catch (SQLException e) {

		}
		return completedAppointment;
	}

	public String[][] viewReviews(String email) {
		String[][] reviews = null;
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT COUNT(by_email) FROM complaintandreview WHERE type = 'Review'"
							+ " AND barber_email = '" + email + "'");
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			int arrayLength = resultSet.getInt(1);
			reviews = new String[arrayLength][2];
			if (resultSet.isBeforeFirst() || arrayLength == 0) {
				JOptionPane.showMessageDialog(null, "No Reviews Available");
				return reviews;
			}
			try {
				PreparedStatement preparedSTatement = connection
						.prepareStatement("SELECT by_email,description FROM complaintandreview WHERE type = 'Review'"
								+ " AND barber_email = '" + email + "'");
				ResultSet resultSEt = preparedSTatement.executeQuery();
				int i = 0;
				while (resultSEt.next()) {
					for (int j = 0; j < 2; j++) {
						reviews[i][j] = resultSEt.getString(j + 1);
					}
					i++;
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		return reviews;

	}
}
