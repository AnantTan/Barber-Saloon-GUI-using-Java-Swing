package barberShop;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class FrontPage {
	private ActionOfButtons actionOfButtons = new ActionOfButtons();
	public static JFrame frame = new JFrame();
	private JPanel panelOne, panelTwo;
	private JButton loginButton, registerButton;
	static Boolean register = false;

	public FrontPage() {
		frame.getContentPane().removeAll();// just to fix the layout when the window reappears after logout
		frameOptions();
		frame.setVisible(true);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
	}

	private void frameOptions() {
		panelOne = new JPanel();
		panelTwo = new JPanel();
		loginButton = new JButton("Login");
		loginButton.addActionListener(actionOfButtons);
		loginButton.setActionCommand("loginButton");
		registerButton = new JButton("Register");
		registerButton.addActionListener(actionOfButtons);
		registerButton.setActionCommand("registerButton");
		panelOne.add(loginButton);
		panelTwo.add(registerButton);
		frame.getContentPane().add(panelOne, BorderLayout.NORTH);
		frame.getContentPane().add(panelTwo, BorderLayout.SOUTH);
		frame.revalidate();
	}

	public static void disposeWindow() {
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
}
