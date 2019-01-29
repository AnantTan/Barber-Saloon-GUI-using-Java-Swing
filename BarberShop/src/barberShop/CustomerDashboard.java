package barberShop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class CustomerDashboard {

	public static JFrame customerFrame = new JFrame("Barbershop");
	private static ActionOfButtons actionOfButtons = new ActionOfButtons();
	private static SlotsAndAppointments slotsAndAppointments;
	private ComplaintAndReview complaintAndReview;
	private JMenuBar menuBar;
	private JMenu appointments, other, person;
	private JMenuItem makeAppointment, viewAppointment, fileComplaint, review, update, logout;
	private static JComboBox barberLocationBox, barberComplaintAndReviewBox;
	private static JComboBox makeAppointmentBox;
	private static JComboBox viewAppointmentsBox;
	private JPanel appointmentPanel, appointmentPane;
	private JTextField complaintField = new JTextField();
	private JLabel barberEmail = new JLabel();
	private String complaintOnThisEmail;

	public CustomerDashboard() {

	}

	public void dashboard() {
		menuBar = new JMenuBar();
		appointments = new JMenu("Appointments");
		other = new JMenu("Other");
		String nameFromDb = Connections.firstName;
		String name = nameFromDb.toUpperCase().charAt(0) + nameFromDb.substring(1, nameFromDb.length());
		person = new JMenu(name);
		makeAppointment = new JMenuItem("Make Appointment");
		makeAppointment.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_MASK));// short cut keys
																									// CTRL+key
		makeAppointment.setMnemonic('M');
		makeAppointment.addActionListener(actionOfButtons);
		makeAppointment.setActionCommand("makeAppointment");

		viewAppointment = new JMenuItem("My Appointments");
		viewAppointment.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK));
		viewAppointment.setMnemonic('V');
		viewAppointment.addActionListener(actionOfButtons);
		viewAppointment.setActionCommand("viewOrCancelMyAppointments");

		fileComplaint = new JMenuItem("File Complaint");
		fileComplaint.addActionListener(actionOfButtons);
		fileComplaint.setActionCommand("fileComplaint");

		review = new JMenuItem("Review");
		review.addActionListener(actionOfButtons);
		review.setActionCommand("reviewWindow");

		update = new JMenuItem("Update Info");
		logout = new JMenuItem("Logout");
		appointments.add(makeAppointment);
		appointments.add(viewAppointment);
		other.add(fileComplaint);
		other.add(review);
		person.add(update);
		person.add(logout);
		menuBar.add(appointments);
		menuBar.add(other);
		menuBar.add(Box.createHorizontalGlue());// sticking to the right
		menuBar.add(person);
		update.addActionListener(actionOfButtons);
		update.setActionCommand("update");
		logout.addActionListener(actionOfButtons);
		logout.setActionCommand("logout");
		menuBar.setBackground(Color.pink);
		customerFrame.setJMenuBar(menuBar);
		customerFrame.setVisible(true);
		customerFrame.setTitle("Welcome");
		customerFrame.setSize(400, 400);
		customerFrame.setLocationRelativeTo(null);
		customerFrame.setResizable(false);
		customerFrame.setDefaultCloseOperation(customerFrame.EXIT_ON_CLOSE);// do something
	}

	public void getAllBarbersLocationWindow() {
		JPanel locationPanel = new JPanel();
		JPanel appointmentPanel = new JPanel();
		customerFrame.getContentPane().removeAll();// refresh the frame if in coming in after my appointment view
		// customerFrame.revalidate();
		slotsAndAppointments = new SlotsAndAppointments();
		barberLocationBox = new JComboBox(slotsAndAppointments.populateBarberLocationComboBox());
		barberLocationBox.addActionListener(actionOfButtons);
		barberLocationBox.setActionCommand("getLocation");
		locationPanel.setBorder(BorderFactory.createTitledBorder("Select Nearest Location"));
		locationPanel.add(barberLocationBox);
		customerFrame.getContentPane().add(locationPanel, BorderLayout.NORTH);
		customerFrame.revalidate();
	}

	int i = 0, j = 0;

	public void getSelectedLocation() {
		System.out.println(barberLocationBox.getSelectedItem().toString());
		// just to make the window appear live
		if (i == 0) {
			if (j == 1) {
				appointmentPane.setVisible(false);
				j = 0;
			}
			i = 1;
			String shopAddress = barberLocationBox.getSelectedItem().toString();
			makeAppointmentBox = new JComboBox(slotsAndAppointments.getAvailaleAppointmentList(shopAddress));
			JPanel buttonPanel = new JPanel();
			JButton bookButton = new JButton("Book");
			bookButton.addActionListener(actionOfButtons);
			bookButton.setActionCommand("bookAppointment");
			appointmentPanel = new JPanel();
			appointmentPanel.add(makeAppointmentBox);
			buttonPanel.add(bookButton);
			customerFrame.getContentPane().add(appointmentPanel, BorderLayout.CENTER);
			customerFrame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
			customerFrame.revalidate();
			appointmentPanel.setVisible(true);

		} else
			updateLocation();
	}

	// refresh the appointments every time new location has been selected
	private void updateLocation() {
		appointmentPanel.setVisible(false);
		j = 1;
		String shopAddress = barberLocationBox.getSelectedItem().toString();
		makeAppointmentBox = new JComboBox(slotsAndAppointments.getAvailaleAppointmentList(shopAddress));
		appointmentPane = new JPanel();
		appointmentPane.add(makeAppointmentBox);
		customerFrame.getContentPane().add(appointmentPane, BorderLayout.CENTER);
		i = 0;
		customerFrame.revalidate();
	}

	public void makeMyAppointmentDetails() {
		String shopAddress = barberLocationBox.getSelectedItem().toString();
		if (makeAppointmentBox.getSelectedItem() == null) {
			JOptionPane.showMessageDialog(null, "You did not select anything");
		} else {
			String bookingDetails = makeAppointmentBox.getSelectedItem().toString();
			int timeIndex = bookingDetails.lastIndexOf(" ");
			String time = bookingDetails.substring(timeIndex + 1);
			int dateIndex = bookingDetails.indexOf(" ");
			String date = bookingDetails.substring(dateIndex + 1, timeIndex);
			String barberEmail = bookingDetails.substring(0, dateIndex);
			slotsAndAppointments.bookMe(barberEmail, shopAddress, date, time);// Query method for booking confirmation
		}
	}

	public void manageBookings() {

		JPanel myAppointmentPanel = new JPanel();
		JLabel mangeBookings = new JLabel("Manage Bookings");
		slotsAndAppointments = new SlotsAndAppointments();
		customerFrame.getContentPane().removeAll();// refresh if coming in after make appointment view
		viewAppointmentsBox = new JComboBox(slotsAndAppointments.viewMyBooking(Connections.emailID));
		JButton cancelAppointment = new JButton("Cancel");
		cancelAppointment.addActionListener(actionOfButtons);
		cancelAppointment.setActionCommand("cancelAppointment");
		myAppointmentPanel.setBorder(BorderFactory.createTitledBorder("Listed as (Barber,Shop Address,Date,Time)"));
		myAppointmentPanel.add(mangeBookings);
		myAppointmentPanel.add(viewAppointmentsBox);
		myAppointmentPanel.add(cancelAppointment);
		customerFrame.add(myAppointmentPanel, BorderLayout.CENTER);
		customerFrame.revalidate();// show the changes made
	}

	public void cancelMyAppointmentDetails() {
		if (viewAppointmentsBox.getItemCount() == 0) {
			JOptionPane.showMessageDialog(null, "No bookings available to cancel");
		} else if (viewAppointmentsBox.getSelectedItem() == null) {
			JOptionPane.showMessageDialog(null, "You did not select anything");
		} else {
			String bookingDetails = viewAppointmentsBox.getSelectedItem().toString();
			System.out.println("deatilsss " + bookingDetails);
			int timeIndex = bookingDetails.lastIndexOf(" ");
			String time = bookingDetails.substring(timeIndex + 1);
			String newDetails = bookingDetails.substring(0, timeIndex);
			int dateIndex = newDetails.lastIndexOf(" ");
			String date = newDetails.substring(dateIndex + 1, timeIndex);
			int shopAddressIndex = newDetails.indexOf(" ");
			String shopAddress = newDetails.substring(shopAddressIndex + 1, dateIndex);
			String barberEmail = bookingDetails.substring(0, shopAddressIndex);
			int userConfrimation = JOptionPane.showConfirmDialog(null, "Are you sure you want cancel the appointment?",
					"Confirmation", JOptionPane.YES_NO_OPTION);
			System.out.println("msssss " + userConfrimation);
			if (userConfrimation == 1 || userConfrimation == -1) {
				return;// if user choice is no or closes the pop up step out of the method
			} else {
				slotsAndAppointments.cancelMyAppointment(Connections.emailID, barberEmail, shopAddress, date, time);
			}
		}
	}

	public void barbersLocationWindowForComplaint() {

		JPanel locationPanel = new JPanel();
		JPanel appointmentPanel = new JPanel();
		customerFrame.getContentPane().removeAll();// refresh the frame if in coming in after my appointment view
		complaintAndReview = new ComplaintAndReview();
		barberLocationBox = new JComboBox(complaintAndReview.complaintBarberLocationComboBox());
		barberLocationBox.addActionListener(actionOfButtons);
		barberLocationBox.setActionCommand("barberComplaintDetails");
		locationPanel.setBorder(BorderFactory.createTitledBorder("Select The Location"));
		locationPanel.add(barberLocationBox);
		customerFrame.getContentPane().add(locationPanel, BorderLayout.NORTH);
		customerFrame.revalidate();
	}

	public void getBarberDetailsForComplaint() {
		JPanel panel = new JPanel();
		JButton writeButton = new JButton("Write");
		writeButton.addActionListener(actionOfButtons);
		writeButton.setActionCommand("writeComplaint");
		String location = barberLocationBox.getSelectedItem().toString();
		// ComplaintAndReview complaintAndReview = new ComplaintAndReview();
		String[] data = complaintAndReview.populateComplaintBox(location);
		if (data == null) {
			JOptionPane.showMessageDialog(null, "No Barbers Found");

		} else {
			customerFrame.getContentPane().removeAll();// refresh the frame
			barberComplaintAndReviewBox = new JComboBox(data);
			panel.setBorder(BorderFactory.createTitledBorder("Listed as (First Name,Last Name,Email)"));
			panel.add(barberComplaintAndReviewBox);
			panel.add(writeButton);
			customerFrame.getContentPane().add(panel, BorderLayout.NORTH);
			customerFrame.revalidate();
		}
	}

	public void writeComplaintAndReview(String type) {
		if (barberComplaintAndReviewBox.getItemCount() == 0) {
			JOptionPane.showMessageDialog(null, "No Barbers Found");
			return;
		} else if (barberComplaintAndReviewBox.getSelectedItem() == null) {
			JOptionPane.showMessageDialog(null, "Please Select a Barber");
			return;
		}
		String details = barberComplaintAndReviewBox.getSelectedItem().toString();
		int lastIndex = details.lastIndexOf(" ");
		complaintOnThisEmail = details.substring(lastIndex + 1);
		barberEmail.setText(complaintOnThisEmail.concat(":"));
		complaintField.setText("");
		JPanel complaintPanel = new JPanel(new GridLayout(1, 1));
		JPanel buttonPanel = new JPanel();
		JButton send = new JButton("Send");
		send.addActionListener(actionOfButtons);
		if (type.equals("Complaint")) {
			send.setActionCommand("sendComplaint");
		} else
			send.setActionCommand("sendReview");
		complaintPanel.setBorder(BorderFactory.createTitledBorder("Max Letters (350)"));
		complaintPanel.add(barberEmail);
		complaintPanel.add(complaintField);
		buttonPanel.add(send);
		customerFrame.getContentPane().add(complaintPanel, BorderLayout.CENTER);
		customerFrame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		customerFrame.revalidate();
	}

	public void sendComplaintOrReview(String type) {
		String complaintOrReviewText = complaintField.getText().toString();
		complaintOrReviewText = complaintOrReviewText.trim();// remove all the spaces on the start and the end
		int length = complaintOrReviewText.length();
		if (length > 40 && length < 350) {
			complaintAndReview = new ComplaintAndReview();
			if (complaintAndReview.sendComplaintOrReview(Connections.emailID, complaintOrReviewText,
					complaintOnThisEmail, type)) {
				resetTextField();// once the complaint has been sent reset everything
			} else
				JOptionPane.showMessageDialog(null, "Cannot Send Complaint/Review At this moment", "Sorry",
						JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Letters (min 40 max 350)", "Invalid", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void resetTextField() {
		complaintField.setText("");
	}

	public void reviewWindow() {
		JPanel panel = new JPanel();
		JButton writeButton = new JButton("Write");
		writeButton.addActionListener(actionOfButtons);
		writeButton.setActionCommand("writeReview");
		ComplaintAndReview complaintAndReview = new ComplaintAndReview();
		String[] data = complaintAndReview.populateReviewBox(Connections.emailID);
		if (data == null) {
			JOptionPane.showMessageDialog(null, "No Barbers Found");// also check the validate in the method in
																	// verify
		} else {
			customerFrame.getContentPane().removeAll();// refresh the frame
			barberComplaintAndReviewBox = new JComboBox(data);
			panel.setBorder(BorderFactory.createTitledBorder("Listed as (First Name,Last Name,Email)"));
			panel.add(barberComplaintAndReviewBox);
			panel.add(writeButton);
			customerFrame.getContentPane().add(panel, BorderLayout.NORTH);
			customerFrame.revalidate();
		}
	}

	public static final void logout() {
		customerFrame.dispose();
	}
}
