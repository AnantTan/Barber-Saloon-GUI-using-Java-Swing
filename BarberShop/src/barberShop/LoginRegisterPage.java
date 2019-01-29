package barberShop;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginRegisterPage {

	private static ActionOfButtons actionOfButtons;
	public static JFrame userType = new JFrame();
	public static JFrame loginFrame = new JFrame();
	public static JFrame registerFrame = new JFrame("Register");
	public static JFrame adminRegisterFrame = new JFrame("Register");
	private JButton adminButton, barberButton, customerButton;
	private static JButton reset, login, register;
	public static JPasswordField passwordField = new JPasswordField();
	public static JTextField emailTextField = new JTextField(), firstNameTextField = new JTextField(),
			lastNameTextField = new JTextField(), phoneNumberTextField = new JTextField(),
			locationOfBarberField = new JTextField(), captchaText = new JTextField(),
			enterCaptchaField = new JTextField();
	public static String roleOfUser;
	public static int captchaNum;

	public LoginRegisterPage() {
		loginRegisterWindow();
		buttonAction();
		userType.setVisible(true);
		userType.pack();
		userType.setResizable(false);
		userType.setLocationRelativeTo(null);
	}

	private void loginRegisterWindow() {
		JPanel panelOne, panelTwo, panelThree;
		panelOne = new JPanel();
		panelOne.setBorder(BorderFactory.createEmptyBorder());
		panelTwo = new JPanel();
		panelTwo.setBorder(BorderFactory.createEmptyBorder());
		panelThree = new JPanel();
		panelThree.setBorder(BorderFactory.createEmptyBorder());
		adminButton = new JButton("Administrator");
		barberButton = new JButton("Barber");
		customerButton = new JButton("Customer");
		panelOne.add(adminButton);
		if (FrontPage.register == false) {
			userType.getContentPane().add(panelOne, BorderLayout.NORTH);
		}
		panelTwo.add(barberButton);
		panelThree.add(customerButton);
		userType.getContentPane().add(panelTwo, BorderLayout.CENTER);
		userType.getContentPane().add(panelThree, BorderLayout.SOUTH);
	}

	private void buttonAction() {
		adminButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				roleOfUser = "administrator";
				loginForm();
			}
		});

		barberButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				roleOfUser = "barber";
				if (FrontPage.register == true) {
					registerForm();
				} else
					loginForm();
			}
		});
		customerButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				roleOfUser = "customer";
				if (FrontPage.register == true) {
					registerForm();
				} else
					loginForm();
			}
		});
	}

	public static final void adminRegisterForm() {
		actionOfButtons = new ActionOfButtons();
		adminRegisterFrame.setVisible(true);
		adminRegisterFrame.setSize(310, 150);
		adminRegisterFrame.setResizable(false);
		adminRegisterFrame.setLocationRelativeTo(null);
		adminRegisterFrame.setDefaultCloseOperation(ActionOfButtons.resetEmailPass());// reset when frame closes
		JPanel panelOne = new JPanel(new GridLayout(2, 2));
		JPanel panelTwo = new JPanel();
		JLabel emailLabel = new JLabel(" Email-ID:");
		JLabel passwordLabel = new JLabel(" Password:");
		reset = new JButton("Reset");
		reset.addActionListener(actionOfButtons);
		reset.setActionCommand("resetRegister");
		register = new JButton("Register");
		register.addActionListener(actionOfButtons);
		register.setActionCommand("registerAdminButton");
		adminRegisterFrame.getRootPane().setDefaultButton(register);
		panelOne.add(emailLabel);
		emailTextField.setText("");// prevents to show the current information in new administrator form
		panelOne.add(emailTextField);
		panelOne.add(passwordLabel);
		passwordField.setText("");
		panelOne.add(passwordField);
		panelTwo.setLayout(new BoxLayout(panelTwo, BoxLayout.LINE_AXIS));
		panelTwo.setBorder(BorderFactory.createEmptyBorder(6, 8, 3, 1));
		panelTwo.add(Box.createHorizontalGlue());
		panelTwo.add(reset);
		panelTwo.add(Box.createRigidArea(new Dimension(3, 0)));
		panelTwo.add(register);
		adminRegisterFrame.getContentPane().add(panelOne, BorderLayout.CENTER);
		adminRegisterFrame.getContentPane().add(panelTwo, BorderLayout.SOUTH);
	}

	private void loginForm() {
		loginFrame.setVisible(true);
		loginFrame.setTitle(roleOfUser.toUpperCase());
		loginFrame.setSize(300, 150);
		loginFrame.setResizable(false);
		loginFrame.setLocationRelativeTo(null);
		actionOfButtons = new ActionOfButtons();
		JPanel panelOne = new JPanel(new GridLayout(2, 2));
		JPanel panelTwo = new JPanel();
		JLabel emailLabel = new JLabel(" Email-ID:");
		emailTextField.setText("");
		JLabel passwordLabel = new JLabel(" Password:");
		passwordField.setText("");
		reset = new JButton("Reset");
		reset.addActionListener(actionOfButtons);
		reset.setActionCommand("resetLogin");
		login = new JButton("Login");
		login.addActionListener(actionOfButtons);
		login.setActionCommand("login");
		loginFrame.getRootPane().setDefaultButton(login);
		panelOne.add(emailLabel);
		panelOne.add(emailTextField);
		panelOne.add(passwordLabel);
		panelOne.add(passwordField);
		panelTwo.setLayout(new BoxLayout(panelTwo, BoxLayout.LINE_AXIS));
		panelTwo.setBorder(BorderFactory.createEmptyBorder(6, 10, 3, 3));
		panelTwo.add(Box.createHorizontalGlue());
		panelTwo.add(reset);
		panelTwo.add(Box.createRigidArea(new Dimension(12, 0)));
		panelTwo.add(login);
		loginFrame.getContentPane().removeAll();
		loginFrame.getContentPane().add(panelOne, BorderLayout.CENTER);
		loginFrame.getContentPane().add(panelTwo, BorderLayout.SOUTH);
		loginFrame.revalidate();
	}

	public static void registerForm() {
		JPanel panelOne = new JPanel();
		registerFrame.setVisible(true);
		if (roleOfUser.equals("barber")) {
			panelOne = new JPanel(new GridLayout(7, 7));
		} else {
			panelOne = new JPanel(new GridLayout(6, 6));
		}
		registerFrame.setSize(330, 250);
		registerFrame.setResizable(false);
		registerFrame.setLocationRelativeTo(null);
		actionOfButtons = new ActionOfButtons();
		registerFrame.setDefaultCloseOperation(ActionOfButtons.resetEmailPass());// reset everything when the window is
																					// closed
		JPanel panelTwo = new JPanel();
		JLabel firstNameLabel = new JLabel("First Name");
		JLabel lastNameLabel = new JLabel("Last Name");
		JLabel phoneNumberLabel = new JLabel("Phone Number");
		JLabel emailLabel = new JLabel("Email-ID:");
		JLabel passwordLabel = new JLabel("Password");
		JLabel locationOfBarberLabel = new JLabel("Location");
		firstNameTextField.requestFocus();
		reset = new JButton("Reset");
		reset.addActionListener(actionOfButtons);
		reset.setActionCommand("resetRegister");
		register = new JButton("Register");
		register.addActionListener(actionOfButtons);
		register.setActionCommand("register");
		registerFrame.getRootPane().setDefaultButton(register);
		panelOne.add(firstNameLabel);
		panelOne.add(firstNameTextField);
		panelOne.add(lastNameLabel);
		panelOne.add(lastNameTextField);
		panelOne.add(phoneNumberLabel);
		panelOne.add(phoneNumberTextField);
		panelOne.add(emailLabel);
		panelOne.add(emailTextField);
		panelOne.add(passwordLabel);
		panelOne.add(passwordField);
		if (roleOfUser.equals("barber"))// because barber has 1 more column of location
		{
			panelOne.add(locationOfBarberLabel);
			panelOne.add(locationOfBarberField);
		}
		panelTwo.setLayout(new BoxLayout(panelTwo, BoxLayout.LINE_AXIS));
		panelTwo.setBorder(BorderFactory.createEmptyBorder(6, 10, 3, 3));
		panelTwo.add(Box.createHorizontalGlue());
		panelTwo.add(reset);
		panelTwo.add(Box.createRigidArea(new Dimension(12, 0)));
		panelTwo.add(register);
		registerFrame.getContentPane().removeAll();
		registerFrame.getContentPane().add(panelOne, BorderLayout.CENTER);
		registerFrame.getContentPane().add(panelTwo, BorderLayout.SOUTH);
		registerFrame.revalidate();
	}
}
