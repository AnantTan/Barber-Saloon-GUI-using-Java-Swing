package barberShop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;

public class BarberDashboard {

	public static JFrame barberFrame = new JFrame("Barbershop");
	private static ActionOfButtons actionOfButtons = new ActionOfButtons();
	private JMenuBar menuBar;
	private JMenu manageAppointments, slots, review, person;
	private JMenuItem upcomingAppointments, status, updateSlots, viewReviews, update;
	public static JMenuItem logout;
	public static JComboBox dateBox, hourBox, minuteBox, appointmnetsBox, optionsBox;

	public BarberDashboard() {
		dashboard();
		barberFrame.setVisible(true);
		barberFrame.setSize(400, 400);
		barberFrame.setLocationRelativeTo(null);
		barberFrame.setDefaultCloseOperation(barberFrame.EXIT_ON_CLOSE);
	}

	private void dashboard() {
		menuBar = new JMenuBar();
		manageAppointments = new JMenu("Appointments");
		slots = new JMenu("Slots");
		review = new JMenu("Reviews");
		String nameFromDb = Connections.firstName;
		String name = nameFromDb.toUpperCase().charAt(0)+nameFromDb.substring(1,nameFromDb.length());
		person = new JMenu(name);
		upcomingAppointments = new JMenuItem("Appointments Due");
		upcomingAppointments.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK));//Short cut keys CTRL+key
		upcomingAppointments.setMnemonic('A');
		upcomingAppointments.addActionListener(actionOfButtons);
		upcomingAppointments.setActionCommand("dueAppointments");

		status = new JMenuItem("Status");
		status.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
		status.setMnemonic('S');
		status.addActionListener(actionOfButtons);
		status.setActionCommand("setStatus");

		updateSlots = new JMenuItem("Update Slots");
		updateSlots.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.CTRL_MASK));
		updateSlots.setMnemonic('U');
		updateSlots.addActionListener(actionOfButtons);
		updateSlots.setActionCommand("slotWindow");

		viewReviews = new JMenuItem("View Review");
		viewReviews.addActionListener(actionOfButtons);
		viewReviews.setActionCommand("viewReviews");

		update = new JMenuItem("Update");
		logout = new JMenuItem("Logout");
		person.add(update);
		update.addActionListener(actionOfButtons);
		update.setActionCommand("update");
		person.add(logout);
		logout.addActionListener(actionOfButtons);
		logout.setActionCommand("logout");
		manageAppointments.add(upcomingAppointments);
		manageAppointments.add(status);
		slots.add(updateSlots);
		review.add(viewReviews);
		menuBar.add(manageAppointments);
		menuBar.add(slots);
		menuBar.add(review);
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(person);
		menuBar.setBackground(Color.lightGray);
		barberFrame.setJMenuBar(menuBar);
	}

	public static void releaseSlots() {
		JPanel panel = new JPanel();
		JPanel buttonPanel = new JPanel();
		LocalDate date = LocalDate.now();// get todays date
		dateBox = new JComboBox();
		hourBox = new JComboBox();
		String[] minutes = { "00", "15", "30", "45" };//will have duration of 15 mins
		minuteBox = new JComboBox(minutes);
		barberFrame.getContentPane().removeAll();// refresh the frame
		JButton addSlot = new JButton("Release Slot");
		addSlot.addActionListener(actionOfButtons);
		addSlot.setActionCommand("releaseSlot");

		//get dates for the next 15 days 
		for (int i = 0; i < 14; ++i) {
			dateBox.addItem(date);// set the date inside combo box
			date = date.plusDays(1);// go on the next date
		}
		for (int i = 9; i < 19; i++) {
			hourBox.addItem(i);
		}
		panel.setBorder(BorderFactory.createTitledBorder("Date and Time"));
		panel.add(dateBox);
		panel.add(hourBox);
		panel.add(minuteBox);
		buttonPanel.add(addSlot);
		barberFrame.getContentPane().add(panel, BorderLayout.NORTH);
		barberFrame.getContentPane().add(buttonPanel, BorderLayout.CENTER);
		barberFrame.revalidate();// refreshing the frame
	}

	public static void upcomingAppointments() {
		JTable upcomingAppointmentsTable = null;
		String[] columnTitles = { "Person", "Date", "Time" };
		SlotsAndAppointments slotsAndAppointments = new SlotsAndAppointments();
		String[][] data = slotsAndAppointments.upcomingAppointmentsForTheBarber();
		if (data == null) {
			JOptionPane.showMessageDialog(null, "No Upcoming Appointments");
		} else {
			upcomingAppointmentsTable = new JTable(data, columnTitles);
			upcomingAppointmentsTable.setEnabled(false);// only readable now
			JScrollPane jScrollPane = new JScrollPane(upcomingAppointmentsTable);
			barberFrame.getContentPane().removeAll();// refresh the frame
			barberFrame.add(jScrollPane);
			barberFrame.revalidate();
		}
	}

	public static void setStatusOfCustomerAppointment() {
		JPanel optionPanel = new JPanel();
		JButton set = new JButton("Set");
		set.addActionListener(actionOfButtons);
		set.setActionCommand("updateStatus");
		String[] options = { "Completed", "Not Arrived" };
		SlotsAndAppointments slotsAndAppointments = new SlotsAndAppointments();
		appointmnetsBox = new JComboBox(slotsAndAppointments.setStatus(Connections.emailID));
		optionsBox = new JComboBox(options);
		barberFrame.getContentPane().removeAll();// refresh the frame
		optionPanel.add(appointmnetsBox);
		optionPanel.add(optionsBox);
		optionPanel.add(set);
		barberFrame.getContentPane().add(appointmnetsBox, BorderLayout.NORTH);
		barberFrame.getContentPane().add(optionPanel);
		barberFrame.revalidate();
	}

	public static void setDetails() {
		if (appointmnetsBox.getItemCount() == 0 || optionsBox.getItemCount() == 0) {
			JOptionPane.showMessageDialog(null, "No bookings available to update");
		} else if (appointmnetsBox.getSelectedItem() == null || optionsBox.getSelectedItem() == null) {
			JOptionPane.showMessageDialog(null, "Make sure you have selected the options");
		} else {
			String statusDetails = appointmnetsBox.getSelectedItem().toString();
			String statusSelected = optionsBox.getSelectedItem().toString();
			int timeIndex = statusDetails.lastIndexOf(" ");
			String time = statusDetails.substring(timeIndex + 1);
			int dateIndex = statusDetails.indexOf(" ");
			String date = statusDetails.substring(dateIndex + 1, timeIndex);
			String personEmail = statusDetails.substring(0, dateIndex);
			SlotsAndAppointments.updateStatus(statusSelected, personEmail, Connections.emailID, Connections.location,
					date, time);
		}
	}

	public static void viewReviews() {
		JTable reviewsTable = null;
		String[] columnTitles = { "Customer", "Description" };
		ComplaintAndReview complaintAndReview = new ComplaintAndReview();
		String[][] data = complaintAndReview.viewReviews(Connections.emailID);
		if (data == null) {
			JOptionPane.showMessageDialog(null, "No Reviews Available");
		} else {
			reviewsTable = new JTable(data, columnTitles);
			reviewsTable.setEnabled(false);// only readable now
			JScrollPane jScrollPane = new JScrollPane(reviewsTable);
			barberFrame.getContentPane().removeAll();// refresh the frame
			barberFrame.add(jScrollPane);
			barberFrame.revalidate();
		}
	}

	public static final void logout() {
		barberFrame.dispose();
	}

}
