package barberShop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;

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

public class AdminDashboard {

	private ActionOfButtons actionOfButtons = new ActionOfButtons();
	private static VerifyOrDeletePerson verifyOrDeletePerson;
	public static JFrame adminFrame = new JFrame();
	private JMenuBar menuBar;
	private JMenu actvities, manage, complaints, admin;
	private JMenuItem appointmentsMade, newBarber, verifyBarber, manageAppointments, removeBarber, removeCustomer,
			viewComplaints, update, registerAdmin, logout;
	private static JComboBox verifyBox, deleteBox, viewAppointmentsBox;

	public AdminDashboard() {

	}

	public void dashboard() {

		menuBar = new JMenuBar();
		actvities = new JMenu("Activites");
		manage = new JMenu("Manage");
		complaints = new JMenu("Complaints");
		admin = new JMenu("Admin");

		newBarber = new JMenuItem("New Barber");
		newBarber.addActionListener(actionOfButtons);
		newBarber.setActionCommand("newActivityBarber");

		appointmentsMade = new JMenuItem("Appointments Made");
		appointmentsMade.addActionListener(actionOfButtons);
		appointmentsMade.setActionCommand("appointmentsActivity");

		update = new JMenuItem("Update Info");
		update.addActionListener(actionOfButtons);
		update.setActionCommand("update");

		registerAdmin = new JMenuItem("Add Admin");
		registerAdmin.addActionListener(actionOfButtons);
		registerAdmin.setActionCommand("registerAdmin");

		logout = new JMenuItem("Logout");
		logout.addActionListener(actionOfButtons);
		logout.setActionCommand("logout");

		verifyBarber = new JMenuItem("Verify Barber");
		verifyBarber.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK));// short cut key ctrl+key
		verifyBarber.setMnemonic('V');// mark the letter related in the menu item
		verifyBarber.addActionListener(actionOfButtons);
		verifyBarber.setActionCommand("verifyBarberInfo");

		manageAppointments = new JMenuItem("Cancel Appointments");
		manageAppointments.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK));
		manageAppointments.setMnemonic('A');
		manageAppointments.addActionListener(actionOfButtons);
		manageAppointments.setActionCommand("cancelAppointmentAdmin");

		removeBarber = new JMenuItem("Remove Barber");
		removeBarber.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.SHIFT_MASK));// short cut key
																								// shift+key
		removeBarber.setMnemonic('B');
		removeBarber.addActionListener(actionOfButtons);
		removeBarber.setActionCommand("removeBarber");

		removeCustomer = new JMenuItem("Remove Customer");
		removeCustomer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.SHIFT_MASK));
		removeCustomer.setMnemonic('C');
		removeCustomer.addActionListener(actionOfButtons);
		removeCustomer.setActionCommand("removeCustomer");

		viewComplaints = new JMenuItem("View Compplaints");
		viewComplaints.addActionListener(actionOfButtons);
		viewComplaints.setActionCommand("viewComplaintAdmin");

		actvities.add(newBarber);
		actvities.add(appointmentsMade);
		manage.add(verifyBarber);
		manage.add(manageAppointments);
		manage.add(removeBarber);
		manage.add(removeCustomer);
		complaints.add(viewComplaints);
		admin.add(update);
		admin.add(registerAdmin);
		admin.add(logout);
		menuBar.add(actvities);
		menuBar.add(manage);
		menuBar.add(complaints);
		menuBar.add(Box.createHorizontalGlue());// sticking to right
		menuBar.add(admin);
		menuBar.setBackground(Color.cyan);
		adminFrame.setJMenuBar(menuBar);
		adminFrame.setVisible(true);
		adminFrame.setTitle("Administrator");
		adminFrame.setSize(500, 500);
		adminFrame.setLocationRelativeTo(null);
		adminFrame.setDefaultCloseOperation(adminFrame.EXIT_ON_CLOSE);
	}

	public void populateVerifyBox() {
		JPanel panel = new JPanel();
		JButton verifyButton = new JButton("Verify");
		verifyButton.addActionListener(actionOfButtons);
		verifyButton.setActionCommand("verifyBarber");
		verifyOrDeletePerson = new VerifyOrDeletePerson();
		String[] data = verifyOrDeletePerson.populateVerifyBarber();
		if (data == null) {
			JOptionPane.showMessageDialog(null, "No Barbers To VERIFY");// also check the validate in the method in
																		// verify
		} else {
			adminFrame.getContentPane().removeAll();// refresh the frame
			verifyBox = new JComboBox(data);
			panel.setBorder(BorderFactory.createTitledBorder("Listed as (First Name,Last Name,Email,Location)"));
			panel.add(verifyBox);
			panel.add(verifyButton);
			adminFrame.getContentPane().add(panel, BorderLayout.NORTH);
			adminFrame.revalidate();
		}
	}

	public void verifyBarberDetails() {
		if (verifyBox.getItemCount() == 0) {
			JOptionPane.showMessageDialog(null, "No Barbers to VERIFY");
		} else if (verifyBox.getSelectedItem() == null) {
			JOptionPane.showMessageDialog(null, "You did not select anything");
		} else {
			String selectedBarberDetails = verifyBox.getSelectedItem().toString();
			int location = selectedBarberDetails.lastIndexOf(" ");
			String newBarberDetails = selectedBarberDetails.substring(0, location);
			int emailIndex = newBarberDetails.lastIndexOf(" ");
			String barberEmail = newBarberDetails.substring(emailIndex + 1);
			System.out.println("emmmm '" + barberEmail + "'");
			verifyOrDeletePerson.verifyBarber(barberEmail);
		}
	}

	public void deleteUserWindow(String user) {
		JPanel panel = new JPanel();
		System.out.println("innnnnn");
		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(actionOfButtons);
		deleteButton.setActionCommand("deleteUser");
		adminFrame.getContentPane().removeAll();
		if (user.equals("barber")) {
			panel.setBorder(BorderFactory.createTitledBorder("All Barbers Email-ID"));
		} else
			panel.setBorder(BorderFactory.createTitledBorder("All Customers Email-ID"));
		verifyOrDeletePerson = new VerifyOrDeletePerson();
		String[] data = verifyOrDeletePerson.populateDeleteUserBox(user);
		if (data == null) {
			JOptionPane.showMessageDialog(null, "Sorry! Cannot find Users");
		} else {
			deleteBox = new JComboBox(data);
			panel.add(deleteBox);
			panel.add(deleteButton);
			adminFrame.getContentPane().add(panel, BorderLayout.NORTH);
			adminFrame.revalidate();
		}
	}

	public void deleteUser() {

		if (deleteBox.getItemCount() == 0) {
			JOptionPane.showMessageDialog(null, "No Barbers to Delete");
		} else if (deleteBox.getSelectedItem() == null) {
			JOptionPane.showMessageDialog(null, "You did not select anything");
		} else {
			String deleteUserEmail = deleteBox.getSelectedItem().toString();
			int userConfrimation = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this user?",
					"Confirmation", JOptionPane.YES_NO_OPTION);
			if (userConfrimation == 1 || userConfrimation == -1) {
				return;// if user choice is no or closes the pop up step out of the method
			} else {
				verifyOrDeletePerson.deleteUser(deleteUserEmail);
			}
		}
	}

	public void appointmentActivity() {
		JTable appointmentsTable = null;
		String[] columnTitles = { "Person-ID", "Barber-ID", "Location", "Date", "Time", "Status" };
		SlotsAndAppointments slotsAndAppointments = new SlotsAndAppointments();
		String[][] data = slotsAndAppointments.viewAppointmentActivityForAdmin();
		if (data == null) {
			JOptionPane.showMessageDialog(null, "No Activity Available To Show");
		} else {
			appointmentsTable = new JTable(data, columnTitles);
			appointmentsTable.setEnabled(false);// only readable now
			JScrollPane jScrollPane = new JScrollPane(appointmentsTable);
			adminFrame.getContentPane().removeAll();// refresh the frame
			adminFrame.add(jScrollPane);
			adminFrame.revalidate();
		}
	}

	public void newBarberActivity() {
		JTable newBarberTable = null;
		String[] columnTitles = { "First Name", "Last Name", "Phone No.", "Email-ID", "Location" };
		VerifyOrDeletePerson verifyOrDeletePerson = new VerifyOrDeletePerson();
		String[][] data = verifyOrDeletePerson.newBarber();
		if (data == null) {
			JOptionPane.showMessageDialog(null, "No Activity Available To Show");
		} else {
			newBarberTable = new JTable(data, columnTitles);
			newBarberTable.setEnabled(false);// only readable now
			JScrollPane jScrollPane = new JScrollPane(newBarberTable);
			adminFrame.getContentPane().removeAll();// refresh the frame
			adminFrame.getContentPane().add(jScrollPane);
			adminFrame.revalidate();
		}
	}

	public void cancelAppointmentWindow() {
		JPanel myAppointmentPanel = new JPanel();
		SlotsAndAppointments slotsAndAppointments = new SlotsAndAppointments();
		viewAppointmentsBox = new JComboBox(slotsAndAppointments.cancelAppointmentDetailsForAdmin());
		if (viewAppointmentsBox.getItemCount() == 0) {
			JOptionPane.showMessageDialog(null, "No Appointments Available");
			return;
		}
		adminFrame.getContentPane().removeAll();// refresh if coming in after make appointment view
		JButton cancelAppointment = new JButton("Cancel");
		cancelAppointment.addActionListener(actionOfButtons);
		cancelAppointment.setActionCommand("adminCancelAppointment");
		myAppointmentPanel.setBorder(BorderFactory.createTitledBorder("Listed as (Person,Barber,Location,Date,Time)"));
		myAppointmentPanel.add(viewAppointmentsBox);
		myAppointmentPanel.add(cancelAppointment);
		adminFrame.add(myAppointmentPanel, BorderLayout.NORTH);
		adminFrame.revalidate();
	}

	public void cancelAppointment() {
		String details = viewAppointmentsBox.getSelectedItem().toString();
		int personIndex = details.indexOf(" ");
		String personEmail = details.substring(0, personIndex);
		String subDetails = details.substring(personIndex + 1);
		int barberIndex = subDetails.indexOf(" ");
		String barberEmail = subDetails.substring(0, barberIndex);
		String subsubDetails = subDetails.substring(barberIndex + 1);
		int locationIndex = subsubDetails.indexOf(" ");
		int dateIndex = subsubDetails.lastIndexOf(" ");
		String location = subsubDetails.substring(0, locationIndex);
		String date = subsubDetails.substring(locationIndex + 1, dateIndex);
		String time = subsubDetails.substring(dateIndex + 1);
		int userConfrimation = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel this appointment?",
				"Confirmation", JOptionPane.YES_NO_OPTION);
		if (userConfrimation == 1 || userConfrimation == -1) {
			return;// if user choice is no or closes the pop up step out of the method
		} else {
			SlotsAndAppointments slotsAndAppointments = new SlotsAndAppointments();
			slotsAndAppointments.cancelAppointmentFromAdmin(personEmail, barberEmail, location, date, time);
		}
	}

	public void viewComplaints() {

		String[] title = { "Customer Name", "Description", "Barber" };
		ComplaintAndReview complaintAndReview = new ComplaintAndReview();
		String[][] data = complaintAndReview.viewComplaintsByAdmin();
		JTable allComplaints = new JTable(data, title);
		allComplaints.setEnabled(false);// only readable now
		JScrollPane jScrollPane = new JScrollPane(allComplaints);
		adminFrame.getContentPane().removeAll();// refresh the frame
		adminFrame.getContentPane().add(jScrollPane);
		adminFrame.revalidate();
	}

	public static final void logout() {
		System.out.println("dddd");
		adminFrame.dispose();
	}
}
