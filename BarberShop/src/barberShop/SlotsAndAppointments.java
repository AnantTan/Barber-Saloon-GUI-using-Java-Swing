package barberShop;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SlotsAndAppointments {

	private static Connection connection;
	private String barberEmail, shopAddress, hour, minute, time;
	String slotDate;// to handle the dates
	private PreparedStatement preparedStatement;

	public SlotsAndAppointments() {
		connection = Connections.connection;
	}

	private void slotData() {
		barberEmail = Connections.emailID;
		shopAddress = Connections.location;
		slotDate = BarberDashboard.dateBox.getSelectedItem().toString();
		System.out.println(slotDate);
		time = BarberDashboard.hourBox.getSelectedItem().toString()
				.concat(":" + BarberDashboard.minuteBox.getSelectedItem().toString());
		System.out.println("time  " + time);
	}

	private boolean checkValidity() {
		// check if the date and time are the same
		boolean isDateAndTimeValid = false;
		try {
			preparedStatement = connection
					.prepareStatement("SELECT date,slot FROM booking WHERE booked_with='" + barberEmail + "'");
			ResultSet resultSet = preparedStatement.executeQuery();
			// if data exist check if same date and time already exist
			if (resultSet.next()) {
				do {
					if (slotDate.equals(resultSet.getString(1)) && time.equals(resultSet.getString(2))) {
						JOptionPane.showMessageDialog(null, slotDate + " And " + time + " Are Already in the slot",
								"Invalid", JOptionPane.ERROR_MESSAGE);
						isDateAndTimeValid = false;
						break;
					} else
						isDateAndTimeValid = true;

				} while (resultSet.next());
			} else
				isDateAndTimeValid = true;// if no data release the the selected slot

		} catch (SQLException e) {
			System.out.println(e);
		}
		return isDateAndTimeValid;
	}

	public void releaseSlot() {
		slotData();
		if (checkValidity() == false) {
			return;
		} else
			System.out.println(BarberDashboard.dateBox.getSelectedItem());
		try {

			preparedStatement = connection.prepareStatement(
					"INSERT INTO booking (booked_with,shop_address,date,slot,taken) VALUES" + "(?,?,?,?,?)");

			preparedStatement.setString(1, barberEmail);
			preparedStatement.setString(2, shopAddress);
			preparedStatement.setString(3, slotDate);
			preparedStatement.setString(4, time);
			preparedStatement.setString(5, "Not Taken");
			int count = preparedStatement.executeUpdate();
			if (count > 0) {
				JOptionPane.showMessageDialog(null, "Slot Released");
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	public String[] populateBarberLocationComboBox() {
		String[] locations = null;
		try {
			preparedStatement = connection.prepareStatement("SELECT COUNT(DISTINCT(shop_address)) FROM booking");
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			int arrayLength = resultSet.getInt(1);// getting the count from the query
			locations = new String[arrayLength];
		} catch (SQLException e) {
			System.out.println(e);
		}
		try {
			preparedStatement = connection.prepareStatement("SELECT DISTINCT shop_address FROM booking");
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

	public String[] getAvailaleAppointmentList(String shop_address) {
		String[] appointmentList = null;

		try {
			preparedStatement = connection.prepareStatement(
					"SELECT COUNT(shop_address) FROM booking WHERE taken = 'Not Taken' AND shop_address = '"
							+ shop_address + "'");
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			if (resultSet.isBeforeFirst()) {
				/////// to be written if query is empty
			}
			int arrayLength = resultSet.getInt(1);
			System.out.println("lenggg " + arrayLength);
			appointmentList = new String[arrayLength];

		} catch (SQLException e) {
			System.out.println(e);
		}
		try {
			preparedStatement = connection.prepareStatement(
					"SELECT booked_with,date,slot FROM booking WHERE taken ='Not Taken' AND shop_address ='"
							+ shop_address + "' AND status is NULL");
			ResultSet resultSet = preparedStatement.executeQuery();
			// if there is any data fill the array else pop up message
			if (resultSet.next()) {
				int index = 0;
				do {
					appointmentList[index] = resultSet.getString(1)
							.concat(" " + resultSet.getString(2).concat(" " + resultSet.getString(3)));
					index++;
				} while (resultSet.next());

				// no appointment available for the selected location
			} else {
				JOptionPane.showMessageDialog(null, "Appointments are currently unavailable for this location", "Sorry",
						JOptionPane.INFORMATION_MESSAGE);
			}

		} catch (SQLException e) {
			System.out.println(e);
		}
		return appointmentList;
	}

	public void bookMe(String barberEmail, String shopAddress, String bookingDate, String time) {
		try {
			System.out.println("sssss " + shopAddress);
			System.out.println("eem   " + barberEmail);
			System.out.println("dateee '" + bookingDate + "'");
			System.out.println("timmee '" + time + "'");
			PreparedStatement updateQuery = connection.prepareStatement(
					"UPDATE booking SET person_email = ?,booking_confirmation='Confirmed',taken='Taken',status='Due' WHERE person_email IS NULL "
							+ "AND booked_with = '" + barberEmail + "' AND shop_address = '" + shopAddress + "'"
							+ "AND booking_confirmation IS NULL AND date = '" + bookingDate + "'" + "AND slot = '"
							+ time + "' AND taken = 'Not Taken' AND status IS NULL LIMIT 1 ");
			System.out.println("myEMEEMEMEM " + Connections.emailID);

			updateQuery.setString(1, Connections.emailID);

			int count = updateQuery.executeUpdate();
			System.out.println("count   " + count);
			if (count > 0) {
				System.out.println("doneeeeee");
				JOptionPane.showMessageDialog(null, "Booking Confirmed");
				CustomerDashboard customerDashboard = new CustomerDashboard();
				customerDashboard.manageBookings();// go to view the booking
			} else {
				JOptionPane.showMessageDialog(null, "Can't make booking");
			}

		} catch (SQLException e) {
			System.out.println(e);
		}

	}

	public String[] viewMyBooking(String myEmail) {
		String[] appointments = null;
		try {
			preparedStatement = connection.prepareStatement(
					"SELECT count(booked_with) FROM booking WHERE person_email='" + myEmail + "' AND status='Due'");
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			int arrayILength = resultSet.getInt(1);
			appointments = new String[arrayILength];
			if (arrayILength == 0) {
				return appointments;// if array is 0 don't go ahead step out
			}

		} catch (SQLException e) {
			System.out.println(e);
		}
		try {
			// show the appointments that are are still pending
			preparedStatement = connection
					.prepareStatement("SELECT booked_with,shop_address,date,slot FROM booking WHERE person_email= '"
							+ myEmail + "' AND status= 'Due'");
			ResultSet resultSet = preparedStatement.executeQuery();
			int index = 0;
			while (resultSet.next()) {
				appointments[index] = resultSet.getString(1).concat(" " + resultSet.getString(2)
						.concat(" " + resultSet.getString(3)).concat(" " + resultSet.getString(4)));
				index++;
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		return appointments;
	}

	public void cancelMyAppointment(String myEmail, String barberEmail, String shopAddress, String date, String time) {
		try {
			preparedStatement = connection.prepareStatement(
					"UPDATE booking SET person_email = NULL, booking_confirmation = NULL, taken = 'Not Taken', "
							+ "status = NULL WHERE person_email = '" + myEmail + "' AND booked_with = '" + barberEmail
							+ "' AND " + "shop_address = '" + shopAddress
							+ "' AND booking_confirmation = 'Confirmed' AND " + "date = '" + date + "' AND slot = '"
							+ time + "' AND taken = 'Taken' AND " + "status = 'Due' LIMIT 1 ");
			int count = preparedStatement.executeUpdate();
			if (count > 0) {
				JOptionPane.showMessageDialog(null, "Your Appointment has been cancelled");
				CustomerDashboard customerDashboard = new CustomerDashboard();
				customerDashboard.manageBookings();
			}

		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	public String[] cancelAppointmentDetailsForAdmin() {
		String[] appointments = null;
		try {
			preparedStatement = connection
					.prepareStatement("SELECT count(booked_with) FROM booking WHERE status='Due'");
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			int arrayILength = resultSet.getInt(1);
			appointments = new String[arrayILength];
			if (arrayILength == 0) {
				return appointments;// if array is 0 don't go ahead step out
			}

		} catch (SQLException e) {
			System.out.println(e);
		}
		try {
			// show the appointments that are are still pending
			preparedStatement = connection.prepareStatement(
					"SELECT person_email,booked_with,shop_address,date,slot FROM booking WHERE status= 'Due'");// get
																												// all
																												// the
																												// bookings
			ResultSet resultSet = preparedStatement.executeQuery();
			int index = 0;
			while (resultSet.next()) {
				appointments[index] = resultSet.getString(1)
						.concat(" " + resultSet.getString(2).concat(" " + resultSet.getString(3))
								.concat(" " + resultSet.getString(4).concat(" " + resultSet.getString(5))));
				index++;
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		return appointments;
	}

	public void cancelAppointmentFromAdmin(String personEmail, String barberEmail, String location, String date,
			String time) {
		try {
			preparedStatement = connection
					.prepareStatement("UPDATE booking SET status = 'Cancelled' WHERE person_email = '" + personEmail
							+ "'" + "AND booked_with = '" + barberEmail + "' AND shop_address = '" + location + "'"
							+ "AND booking_confirmation = 'Confirmed' AND date = '" + date + "'" + "AND slot = '" + time
							+ "' AND taken = 'Taken' AND status = 'Due' LIMIT 1 ");
			int count = preparedStatement.executeUpdate();
			if (count > 0) {
				JOptionPane.showMessageDialog(null, "Appointment Successfully Cancelled");
				AdminDashboard adminDashboard = new AdminDashboard();
				adminDashboard.cancelAppointmentWindow();
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	public String[][] upcomingAppointmentsForTheBarber() {
		String[][] upcomingAppointments = null;
		try {
			preparedStatement = connection.prepareStatement("SELECT COUNT(person_email) FROM booking "
					+ "WHERE booked_with='" + Connections.emailID + "' AND status='Due'");
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.isBeforeFirst() && !resultSet.next()) {
				return upcomingAppointments;
			}
			int arrayLength = resultSet.getInt(1);
			if (arrayLength == 0) {
				return upcomingAppointments;
			}
			upcomingAppointments = new String[arrayLength][3];

		} catch (SQLException e) {
			System.out.println(e);
		}
		try {
			preparedStatement = connection.prepareStatement("SELECT person_email,date,slot FROM booking "
					+ "WHERE booked_with= '" + Connections.emailID + "' AND status= 'Due'");
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int i = 0;
				do {
					for (int j = 0; j < 3; j++) {
						upcomingAppointments[i][j] = resultSet.getString(j + 1);
					}
					i++;
				} while (resultSet.next());
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		return upcomingAppointments;
	}

	public String[][] viewAppointmentActivityForAdmin() {
		String[][] appointmentsActivity = null;
		try {
			preparedStatement = connection.prepareStatement(
					"SELECT COUNT(person_email) FROM booking WHERE booking_confirmation = 'Confirmed'");
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.isBeforeFirst() && !resultSet.next()) {
				return appointmentsActivity;
			}
			int arrayLength = resultSet.getInt(1);
			if (arrayLength == 0) {
				return appointmentsActivity;
			}
			appointmentsActivity = new String[arrayLength][6];// table is going to have 6 columns

		} catch (SQLException e) {
			System.out.println(e);
		}
		try {
			preparedStatement = connection.prepareStatement(
					"SELECT person_email,booked_with,shop_address,date,slot,status FROM booking WHERE booking_confirmation = 'Confirmed'");
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int i = 0;
				do {
					for (int j = 0; j < 6; j++) {
						appointmentsActivity[i][j] = resultSet.getString(j + 1);
					}
					i++;
				} while (resultSet.next());
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		return appointmentsActivity;
	}

	public String[] setStatus(String myEmail) {
		String[] customerDetails = null;
		try {
			preparedStatement = connection.prepareStatement(
					"SELECT count(person_email) FROM booking WHERE booked_with='" + myEmail + "' AND status='Due'");
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			int arrayILength = resultSet.getInt(1);
			customerDetails = new String[arrayILength];
		} catch (SQLException e) {
			System.out.println(e);
		}
		try {
			// show the appointments that are are still pending
			preparedStatement = connection
					.prepareStatement("SELECT person_email,date,slot FROM booking WHERE booked_with= '" + myEmail
							+ "' AND status= 'Due'");
			ResultSet resultSet = preparedStatement.executeQuery();
			int index = 0;
			while (resultSet.next()) {
				customerDetails[index] = resultSet.getString(1)
						.concat(" " + resultSet.getString(2).concat(" " + resultSet.getString(3)));
				index++;
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		return customerDetails;
	}

	public static void updateStatus(String status, String peronEmail, String barberEmail, String shopAddress,
			String date, String time) {
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("UPDATE booking SET status = '" + status + "' WHERE person_email = '" + peronEmail
							+ "'" + "AND booked_with = '" + barberEmail + "' AND shop_address = '" + shopAddress + "'"
							+ "AND booking_confirmation = 'Confirmed' AND date = '" + date + "'" + "AND slot = '" + time
							+ "' AND taken = 'Taken' AND status = 'Due' LIMIT 1 ");
			int count = preparedStatement.executeUpdate();
			if (count > 0) {
				JOptionPane.showMessageDialog(null, "Status Updated Successfully");
				BarberDashboard.setStatusOfCustomerAppointment();
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
}