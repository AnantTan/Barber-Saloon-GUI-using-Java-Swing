package barberShop;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class ActionOfButtons implements ActionListener {

	private SlotsAndAppointments slotsAndBookings;
	private CustomerDashboard customerDashboard;
	private static UpdateUserInfo updateUserInfo;
	private static AdminDashboard adminDashboard;
	private Window barberWindow = BarberDashboard.barberFrame;
	private Window customerWindow = CustomerDashboard.customerFrame;
	private Window adminWindow = AdminDashboard.adminFrame;
	private Pattern passwordPattern, emailPattern;// takes the pattern that need to be check
	private Matcher passwordMatcher, emailMatcher;// takes the pattern and check it against the input
	private String passwordRegex = "((?=.*\\d)(?=.*[A-Z])(?=.*[!@#$%]).{8,12})";// this should be in the password
	// (?=.*[a-z])
	private String emailRegex = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";// this should be there in
																								// the email

	public static int resetEmailPass() {
		LoginRegisterPage.firstNameTextField.setText("");
		LoginRegisterPage.lastNameTextField.setText("");
		LoginRegisterPage.phoneNumberTextField.setText("");
		LoginRegisterPage.emailTextField.setText("");
		LoginRegisterPage.passwordField.setText("");
		LoginRegisterPage.locationOfBarberField.setText("");
		LoginRegisterPage.firstNameTextField.requestFocus();
		return 1;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		if (e.getActionCommand().equals("loginButton") || e.getActionCommand().equals("registerButton")) {
			if (e.getActionCommand().equals("registerButton")) {
				FrontPage.register = true;
			} else {
				FrontPage.register = false;
			}
			new LoginRegisterPage();
		} else if (e.getActionCommand().equals("resetLogin") || e.getActionCommand().equals("resetRegister")) {
			resetEmailPass();
		} else if (e.getActionCommand().equals("register")) {
			if (generalInfoCheck() == true) {
				if (checkEmailAddress(LoginRegisterPage.emailTextField.getText()) == true) {
					if (checkPassword(LoginRegisterPage.passwordField.getText()) == true) {
						Connections connections = new Connections();
						connections.registeration();
					} else
						JOptionPane.showMessageDialog(null, "Length 8-12,1 Upper Case,1 Alpha Char,1 Special Char)",
								"Inalid Password", JOptionPane.ERROR_MESSAGE);
				} else
					JOptionPane.showMessageDialog(null, "Invalid Email-ID", "Invalid Email", JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getActionCommand().equals("login")) {
			Connections connections = new Connections();
			connections.login();
		} else if (e.getActionCommand().equals("update")) {
			updateUserInfo = new UpdateUserInfo();
			updateUserInfo.authorisationWindow();
		} else if (e.getActionCommand().equals("verify")) {
			updateUserInfo.verification();
		} else if (e.getActionCommand().equals("updateAdmin") || e.getActionCommand().equals("updateCustmerOrBarber")) {
			updateUserInfo.updateInfoChanges();
		} else if (e.getActionCommand().equals("registerAdmin")) {
			LoginRegisterPage.adminRegisterForm();
		} else if (e.getActionCommand().equals("registerAdminButton")) {
			if (checkEmailAddress(LoginRegisterPage.emailTextField.getText()) == true) {
				if (checkPassword(LoginRegisterPage.passwordField.getText()) == true) {
					Connections connections = new Connections();
					connections.registeration();
				} else
					JOptionPane.showMessageDialog(null, "Length 8-12,1 Upper Case,1 Alpha Char,1 Special Char)",
							"Inalid Password", JOptionPane.ERROR_MESSAGE);
			} else
				JOptionPane.showMessageDialog(null, "Invalid Email-ID", "Invalid Email", JOptionPane.ERROR_MESSAGE);

		} else if (e.getActionCommand().equals("logout")) {
			adminWindow.dispose();// disposing the window having the frame on logout
			customerWindow.dispose();
			barberWindow.dispose();
			new FrontPage();// if logged out properly show the login and window again
		} else if (e.getActionCommand().equals("slotWindow")) {
			BarberDashboard.releaseSlots();
		} else if (e.getActionCommand().equals("releaseSlot")) {
			slotsAndBookings = new SlotsAndAppointments();
			slotsAndBookings.releaseSlot();
		} else if (e.getActionCommand().equals("dueAppointments")) {
			BarberDashboard.upcomingAppointments();
		} else if (e.getActionCommand().equals("setStatus")) {
			BarberDashboard.setStatusOfCustomerAppointment();
		} else if (e.getActionCommand().equals("updateStatus")) {
			BarberDashboard.setDetails();
		} else if (e.getActionCommand().equals("makeAppointment")) {
			customerDashboard = new CustomerDashboard();
			customerDashboard.getAllBarbersLocationWindow();
		} else if (e.getActionCommand().equals("getLocation")) {
			customerDashboard.getSelectedLocation();
		} else if (e.getActionCommand().equals("bookAppointment")) {
			customerDashboard.makeMyAppointmentDetails();
		} else if (e.getActionCommand().equals("viewOrCancelMyAppointments")) {
			customerDashboard = new CustomerDashboard();
			customerDashboard.manageBookings();
		} else if (e.getActionCommand().equals("cancelAppointment")) {
			customerDashboard.cancelMyAppointmentDetails();
		} else if (e.getActionCommand().equals("fileComplaint")) {
			customerDashboard = new CustomerDashboard();
			customerDashboard.barbersLocationWindowForComplaint();
		} else if (e.getActionCommand().equals("barberComplaintDetails")) {
			customerDashboard.getBarberDetailsForComplaint();
		} else if (e.getActionCommand().equals("getEmailAndNameLocationForComplaint")) {
			customerDashboard.getBarberDetailsForComplaint();
		} else if (e.getActionCommand().equals("writeComplaint")) {
			customerDashboard.writeComplaintAndReview("Complaint");
		} else if (e.getActionCommand().equals("writeReview")) {
			customerDashboard.writeComplaintAndReview("Review");
		} else if (e.getActionCommand().equals("reviewWindow")) {
			customerDashboard = new CustomerDashboard();
			customerDashboard.reviewWindow();
		} else if (e.getActionCommand().equals("sendComplaint")) {
			customerDashboard.sendComplaintOrReview("Complaint");
		} else if (e.getActionCommand().equals("sendReview")) {
			customerDashboard.sendComplaintOrReview("Review");
		} else if (e.getActionCommand().equals("viewReviews")) {
			BarberDashboard.viewReviews();
		} else if (e.getActionCommand().equals("verifyBarberInfo")) {
			adminDashboard = new AdminDashboard();
			adminDashboard.populateVerifyBox();
		} else if (e.getActionCommand().equals("verifyBarber")) {
			adminDashboard.verifyBarberDetails();
		} else if (e.getActionCommand().equals("removeBarber")) {
			VerifyOrDeletePerson verifyOrDeletePerson = new VerifyOrDeletePerson();
			verifyOrDeletePerson.setUser("barber");// user value to be used in keeping the right window in focus
			adminDashboard = new AdminDashboard();
			adminDashboard.deleteUserWindow("barber");
		} else if (e.getActionCommand().equals("removeCustomer")) {
			VerifyOrDeletePerson verifyOrDeletePerson = new VerifyOrDeletePerson();
			verifyOrDeletePerson.setUser("customer");// user value to be used in keeping the right window in focus
			adminDashboard = new AdminDashboard();
			adminDashboard.deleteUserWindow("customer");
		} else if (e.getActionCommand().equals("deleteUser")) {
			adminDashboard.deleteUser();
		} else if (e.getActionCommand().equals("newActivityBarber")) {
			adminDashboard = new AdminDashboard();
			adminDashboard.newBarberActivity();
		} else if (e.getActionCommand().equals("appointmentsActivity")) {
			adminDashboard = new AdminDashboard();
			adminDashboard.appointmentActivity();
		} else if (e.getActionCommand().equals("cancelAppointmentAdmin")) {
			adminDashboard = new AdminDashboard();
			adminDashboard.cancelAppointmentWindow();
		} else if (e.getActionCommand().equals("adminCancelAppointment")) {
			adminDashboard.cancelAppointment();
		} else if (e.getActionCommand().equals("viewComplaintAdmin")) {
			adminDashboard = new AdminDashboard();
			adminDashboard.viewComplaints();
		}
	}

	private boolean generalInfoCheck() {

		boolean isValid = false;
		String firstName = LoginRegisterPage.firstNameTextField.getText();
		String lastName = LoginRegisterPage.lastNameTextField.getText();
		String phoneNumber = LoginRegisterPage.phoneNumberTextField.getText();
		int phoneNumberLength = phoneNumber.trim().length();
		String location = LoginRegisterPage.locationOfBarberField.getText().toString();

		if (firstName.trim().equals("") || lastName.trim().equals("") || phoneNumber.equals("")) {
			JOptionPane.showMessageDialog(null, "No entry should be empty", "Inavlid entry/s",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} else {
			if (phoneNumberLength > 8 && phoneNumberLength < 13) {
				isValid = true;// if customer register he does not provide location
				if (LoginRegisterPage.roleOfUser.equals("barber")) {
					if (location.trim().equals("")) {
						JOptionPane.showMessageDialog(null, "Cannot be empty", "Location", JOptionPane.ERROR_MESSAGE);
						return false;
					} else {
						return true;
					}
				}
			} else {
				JOptionPane.showMessageDialog(null, "Invalid (9-13) digits", "Phone Number", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		return isValid;
	}

	public Boolean checkPassword(String password) {

		// String password = LoginRegisterPage.passwordField.getText();
		passwordPattern = Pattern.compile(passwordRegex);// takes the pattern that need to be check
		passwordMatcher = passwordPattern.matcher(password);// takes the pattern and check it against the input
		return passwordMatcher.matches();
	}

	public boolean checkEmailAddress(String email) {

		// String email = LoginRegisterPage.emailTextField.getText();
		emailPattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
		emailMatcher = emailPattern.matcher(email);
		return emailMatcher.matches();
	}
}