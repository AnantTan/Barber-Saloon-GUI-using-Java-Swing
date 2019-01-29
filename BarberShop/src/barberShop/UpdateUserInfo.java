package barberShop;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.mysql.jdbc.Connection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateUserInfo {

	private ActionOfButtons actionOfButtons = new ActionOfButtons();
	private String roleOfUser;
	private static JFrame frame = new JFrame();
	private static JFrame verifyFrame = new JFrame();
	private static JFrame registerFrame = new JFrame();
	private Window barberWindow = BarberDashboard.barberFrame;
	private Window customerWindow = CustomerDashboard.customerFrame;
	private JButton update;
	private JLabel newPassword, confirmPassword;
	private static JTextField firstNameTextField, lastNameTextField, phoneNumberTextField;
	private static JTextField emailTextField, locationOfBarberField;
	private static JPasswordField newPassField, confirmPassField, password = new JPasswordField();
	private static Connection connection;

	public UpdateUserInfo() {
		roleOfUser = Connections.roleOfUser;
		connection = (Connection) Connections.connection;
	}

	public void authorisationWindow() {
		verifyFrame.setVisible(true);
		verifyFrame.setSize(150, 100);
		verifyFrame.setLocationRelativeTo(null);
		verifyFrame.setResizable(false);
		JPanel panel1 = new JPanel(new GridLayout(1, 1));
		JPanel panel2 = new JPanel(new GridLayout(0, 1));
		JLabel enterPassword = new JLabel("Password");
		password.setText("");
		JButton verify = new JButton("Verify");
		verifyFrame.getRootPane().setDefaultButton(verify);
		verify.addActionListener(actionOfButtons);
		verify.setActionCommand("verify");
		panel1.add(enterPassword);
		panel1.add(password);
		panel2.add(verify);
		verifyFrame.getContentPane().add(panel1, BorderLayout.NORTH);
		verifyFrame.getContentPane().add(panel2, BorderLayout.SOUTH);
		verifyFrame.revalidate();
	}

	public void verification() {
		if (Connections.password.equals(password.getText())) {
			verifyFrame.dispose();// close the verification window
			if (roleOfUser.equals("administrator")) {
				updateAdmin();// if it is a login get the details to be used in update info
			} else
				updateCustomerOrBarber();
		} else
			JOptionPane.showMessageDialog(verifyFrame, "Authorisation Error", "Error", JOptionPane.ERROR_MESSAGE);
	}

	public void updateAdmin() {
		frame.setVisible(true);
		frame.setTitle(LoginRegisterPage.roleOfUser);
		frame.setResizable(false);
		frame.setSize(300, 200);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);

		JPanel panelOne = new JPanel(new GridLayout(3, 3));
		JPanel panelTwo = new JPanel();
		JLabel emailLabel = new JLabel(" Email-ID:");
		newPassword = new JLabel("New Password:");
		confirmPassword = new JLabel("Confirm Password");
		emailTextField = new JTextField(Connections.emailID);
		newPassField = new JPasswordField();
		confirmPassField = new JPasswordField();
		update = new JButton("Update");
		update.addActionListener(actionOfButtons);
		update.setActionCommand("updateAdmin");
		frame.getRootPane().setDefaultButton(update);
		panelOne.add(emailLabel);
		panelOne.add(emailTextField);
		panelOne.add(newPassword);
		panelOne.add(newPassField);
		panelOne.add(confirmPassword);
		panelOne.add(confirmPassField);
		panelTwo.setLayout(new BoxLayout(panelTwo, BoxLayout.LINE_AXIS));
		panelTwo.setBorder(BorderFactory.createEmptyBorder(6, 8, 3, 1));
		panelTwo.add(Box.createHorizontalGlue());
		panelTwo.add(Box.createRigidArea(new Dimension(3, 0)));
		panelTwo.add(update);
		// setDetails();
		frame.getContentPane().add(panelOne, BorderLayout.CENTER);
		frame.getContentPane().add(panelTwo, BorderLayout.SOUTH);
	}

	public void updateCustomerOrBarber() {
		registerFrame.setVisible(true);
		registerFrame.setTitle("Update");
		JPanel panelOne = new JPanel();
		if (Connections.roleOfUser.equals("barber")) {
			panelOne = new JPanel(new GridLayout(7, 7));
		} else {
			panelOne = new JPanel(new GridLayout(6, 6));
		}
		registerFrame.setSize(335, 250);
		registerFrame.setResizable(false);
		registerFrame.setLocationRelativeTo(null);
		JPanel panelTwo = new JPanel();
		JLabel firstNameLabel = new JLabel("First Name");
		JLabel lastNameLabel = new JLabel("Last Name");
		JLabel phoneNumberLabel = new JLabel("Phone Number");
		JLabel emailLabel = new JLabel("Email-ID:");
		JLabel locationOfBarberLabel = new JLabel("Location");
		firstNameTextField = new JTextField(Connections.firstName);
		firstNameTextField.requestFocus();
		lastNameTextField = new JTextField(Connections.lastName);
		phoneNumberTextField = new JTextField(Connections.phoneNum);
		emailTextField = new JTextField(Connections.emailID);
		locationOfBarberField = new JTextField(Connections.location);
		newPassword = new JLabel("New Password");
		newPassField = new JPasswordField();
		confirmPassword = new JLabel("Confirm Password");
		confirmPassField = new JPasswordField();
		JButton update = new JButton("Update");
		update.addActionListener(actionOfButtons);
		update.setActionCommand("updateCustmerOrBarber");
		registerFrame.getRootPane().setDefaultButton(update);
		panelOne.add(firstNameLabel);
		panelOne.add(firstNameTextField);
		panelOne.add(lastNameLabel);
		panelOne.add(lastNameTextField);
		panelOne.add(phoneNumberLabel);
		panelOne.add(phoneNumberTextField);
		panelOne.add(emailLabel);
		panelOne.add(emailTextField);
		if (roleOfUser.equals("barber"))// because barber has 1 more column of loaction
		{
			panelOne.add(locationOfBarberLabel);
			panelOne.add(locationOfBarberField);
		}
		panelOne.add(newPassword);
		panelOne.add(newPassField);
		panelOne.add(confirmPassword);
		panelOne.add(confirmPassField);
		panelTwo.setLayout(new BoxLayout(panelTwo, BoxLayout.LINE_AXIS));
		panelTwo.setBorder(BorderFactory.createEmptyBorder(6, 10, 3, 3));// dimesions for the box layout
		panelTwo.add(Box.createHorizontalGlue());
		panelTwo.add(Box.createRigidArea(new Dimension(12, 0)));
		panelTwo.add(update);
		registerFrame.getContentPane().add(panelOne, BorderLayout.CENTER);
		registerFrame.getContentPane().add(panelTwo, BorderLayout.SOUTH);

	}

	// update the changes done in the profile
	public void updateInfoChanges() {
		String updatedEmail = emailTextField.getText();
		ActionOfButtons actionOfButtons = new ActionOfButtons();
		int count = 0; // take the execute update increment
		if (actionOfButtons.checkPassword(confirmPassField.getText()) == true) {
			if (newPassField.getText().equals(confirmPassField.getText())) {
				if (actionOfButtons.checkEmailAddress(updatedEmail) == true) {
					try {
						PreparedStatement preparedStatement = connection
								.prepareStatement("UPDATE credentials SET role=?,email_address=?,password=? "
										+ "WHERE email_address= '" + Connections.emailID + "'");
						preparedStatement.setString(1, Connections.roleOfUser);
						preparedStatement.setString(2, updatedEmail);
						preparedStatement.setString(3, confirmPassField.getText());// cannot be left
																					// empty
						count = preparedStatement.executeUpdate();
						if (count > 0) {
							JOptionPane.showMessageDialog(frame, "You need to log in again", "Security", 0);
							frame.dispose();
							AdminDashboard.logout();// close the main dashboard
							FrontPage.disposeWindow();// remove the login section
							new FrontPage();
						}
					} catch (SQLException e) {
						System.out.println(e);
					}

					if (roleOfUser.equals("customer")) {
						try {
							PreparedStatement preparedCustomer = connection.prepareStatement(
									"UPDATE customer SET first_name=?,last_name=?,phone_number=?,email_address=?"
											+ " WHERE email_address = '" + updatedEmail + "'");

							preparedCustomer.setString(1, firstNameTextField.getText());
							preparedCustomer.setString(2, lastNameTextField.getText());
							preparedCustomer.setString(3, phoneNumberTextField.getText());
							preparedCustomer.setString(4, emailTextField.getText());
							count = 0;
							count = preparedCustomer.executeUpdate();
							if (count > 0) {
								registerFrame.dispose();// close the update window
								customerWindow.dispose();// close the dashboard
							}

						} catch (Exception e) {
							System.out.println(e);
						}
					} else if (roleOfUser.equals("barber")) {
						try {
							System.out.println("barber");
							PreparedStatement preparedBarber = connection.prepareStatement(
									"UPDATE barber SET first_name=?,last_name=?,phone_number=?,email_address=?,location=?"
											+ " WHERE email_address = '" + updatedEmail + "'");

							preparedBarber.setString(1, firstNameTextField.getText());
							preparedBarber.setString(2, lastNameTextField.getText());
							preparedBarber.setString(3, phoneNumberTextField.getText());
							preparedBarber.setString(4, emailTextField.getText());
							preparedBarber.setString(5, locationOfBarberField.getText());
							count = 0;
							count = preparedBarber.executeUpdate();
							if (count > 0) {
								registerFrame.dispose();
								barberWindow.dispose();
							}

						} catch (SQLException e) {
							System.out.println(e);
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "Invalid Email-ID", "Invalid Email", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, "Password did not match", "Check Password",
						JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, "Length 8-12,1 Upper Case,1 Alpha Char,1 Special Char)",
					"Inalid Password", JOptionPane.ERROR_MESSAGE);
		}
	}
}