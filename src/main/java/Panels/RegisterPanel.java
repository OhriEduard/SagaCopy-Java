package Panels;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Entities.User;
import MyTextFields.MyPasswordField;
import MyTextFields.MyTextField;
import Repository.EntityManagerFactoryCreator;
import Repository.MyRepository;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Main.EmailValidator;
import Main.RegisterState;

public class RegisterPanel extends JPanel{
	
	JTextField username;
	JTextField email;
	JTextField password;
	JTextField confirmPassword;
	
	JButton backToLoginButton;
	JButton registerButton;
	
	public RegisterPanel(CardLayout cardLayout, JPanel cardPanel) {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(300, 400));
		setMinimumSize(new Dimension(300, 200));
		setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 0, 0, 0),
                BorderFactory.createLineBorder(Color.BLACK)
        ));
		
		JPanel backToLoginPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel credentialsPanelBody = new JPanel(new BorderLayout());
		JPanel credentialsPanel = new JPanel(new GridBagLayout());
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		add(backToLoginPanel, BorderLayout.NORTH);
		add(credentialsPanelBody, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		credentialsPanelBody.setBorder(new EmptyBorder(30,0,0,0));
		credentialsPanelBody.add(credentialsPanel, BorderLayout.NORTH);
		
		
		backToLoginButton = new JButton("<- Login");
		backToLoginButton.setSize(50, 25);
		backToLoginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.next(cardPanel);
				Component[] components = cardPanel.getComponents();
		        for (Component component : components) {
		            if (component.isVisible()) {
		                cardPanel.setPreferredSize(component.getPreferredSize());
		                cardPanel.revalidate();
		                cardPanel.repaint();
		                break;
		            }
		        }
			}
		});
		backToLoginPanel.add(backToLoginButton);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0,0,20,10);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		
		username = new MyTextField(30,25);
		username.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				if (username.getText().length() < 8) {
					setBorderColor(username, Color.RED);
				}
				else
				{
					setBorderColor(username, Color.GREEN);
				}
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				if (username.getText().length() < 8) {
					setBorderColor(username, Color.RED);
				}
				else
				{
					setBorderColor(username, Color.GREEN);
				}
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});
		JLabel usernameLabel = new JLabel("Username :");
		addLabelAndTextField(credentialsPanel, usernameLabel, username, gbc);
		
		email = new MyTextField(30,25);
		email.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				if(EmailValidator.isValidEmail(email.getText())) {
					setBorderColor(email, Color.GREEN);
				}
				else {
					setBorderColor(email, Color.RED);
				}
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				if(EmailValidator.isValidEmail(email.getText())) {
					setBorderColor(email, Color.GREEN);
				}
				else {
					setBorderColor(email, Color.RED);
				}
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});
		JLabel emailLabel = new JLabel("Email :");
		addLabelAndTextField(credentialsPanel, emailLabel, email, gbc);
		
		password = new MyPasswordField(30,25);
		JLabel passwordLabel = new JLabel("Password :");
		addLabelAndTextField(credentialsPanel, passwordLabel, password, gbc);
		
		confirmPassword = new MyPasswordField(30,25);
		JLabel confirmPasswordLabel = new JLabel("<html>Confirm<br>password :</html>");
		addLabelAndTextField(credentialsPanel, confirmPasswordLabel, confirmPassword, gbc);
		
		JLabel stateOfRegistration = new JLabel();
		stateOfRegistration.setFont(new Font("Arial", Font.ITALIC, 15));
		stateOfRegistration.setVisible(false);

		gbc.gridwidth = GridBagConstraints.REMAINDER; 
		gbc.weighty = 1.0;

		credentialsPanel.add(stateOfRegistration, gbc);
		
		registerButton = new JButton("Register !");
		registerButton.setFont(registerButton.getFont().deriveFont(20f));
		registerButton.setPreferredSize(new Dimension(200,50));
		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (username.getText().length() >= 8) {
					if (EmailValidator.isValidEmail(email.getText())) {
						if (password.getText().length() > 7) {
							if (password.getText().equals(confirmPassword.getText())) {
								setStateSituation(RegisterState.SUCCESS, stateOfRegistration);
								setBorderColor(password,Color.GREEN);
								setBorderColor(confirmPassword,Color.GREEN);
								User user = new User();
								user.setUsername(username.getText());
								user.setEmail(email.getText());
								user.setPassword(password.getText());
								
								MyRepository<User> userRepository = new MyRepository<>(User.class, 
										EntityManagerFactoryCreator.obtainOrCreateEntityManagerFactory());
								try {
									userRepository.save(user);
								} catch (Exception exc) {
									stateOfRegistration.setText("<html>Username already taken.<br>\"\r\n"
											+ "					+ \"Please enter another username!</html>");
									stateOfRegistration.setForeground(Color.RED);
									stateOfRegistration.setVisible(true);
								}
								
							}
							else{
								setStateSituation(RegisterState.CONFIRMPASSERR, stateOfRegistration);
								setBorderColor(password,Color.RED);
								setBorderColor(confirmPassword,Color.RED);
							}
						}
						else {
							setStateSituation(RegisterState.PASSERR, stateOfRegistration);
							setBorderColor(password,Color.RED);
							setBorderColor(confirmPassword,Color.RED);
						}
					}
					else {
						setStateSituation(RegisterState.EMAILNOTVALID, stateOfRegistration);
					}
				} else {
					setStateSituation(RegisterState.USERNAMENOTVALID, stateOfRegistration);
				}
			}
		});
		
		DocumentListener hideStateListener = new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				stateOfRegistration.setVisible(false);
				setBorderColor(password,Color.BLACK);
				setBorderColor(confirmPassword,Color.BLACK);
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				stateOfRegistration.setVisible(false);
				setBorderColor(password,Color.BLACK);
				setBorderColor(confirmPassword,Color.BLACK);
			}
			@Override
			public void changedUpdate(DocumentEvent e) {

			}
		};
		
		username.getDocument().addDocumentListener(hideStateListener);
		email.getDocument().addDocumentListener(hideStateListener);
		password.getDocument().addDocumentListener(hideStateListener);
		confirmPassword.getDocument().addDocumentListener(hideStateListener);
		
		buttonPanel.add(registerButton);
	}
	
	private void addLabelAndTextField (JPanel panel, JLabel label, JTextField textField, GridBagConstraints gbc) {
		panel.add(label, gbc);
		gbc.gridx++;
		panel.add(textField, gbc);
		gbc.gridx = 0;
		gbc.gridy++;
	}
	
	private void setBorderColor (JTextField textField, Color color) {
		Border newBorder = BorderFactory.createLineBorder(color);
		textField.setBorder(newBorder);
	}
	
	private void setStateSituation (RegisterState state, JLabel stateLabel) {
		if (state == RegisterState.SUCCESS) {
			stateLabel.setText("Registered successfully !");
			stateLabel.setForeground(Color.GREEN);
			stateLabel.setVisible(true);
		}
		
		if (state == RegisterState.CONFIRMPASSERR) {
			stateLabel.setText("<html>Password and confirmation password<br> don't match !</html>");
			stateLabel.setForeground(Color.RED);
			stateLabel.setVisible(true);
		}
		
		if (state == RegisterState.PASSERR) {
			stateLabel.setText("<html>Password is too weak or invalid.<br>"
					+ "Please enter a valid password !</html>");
			stateLabel.setForeground(Color.RED);
			stateLabel.setVisible(true);
		}
		
		if (state == RegisterState.EMAILNOTVALID) {
			stateLabel.setText("<html>Email is not valid.<br>"
					+ "Please enter a valid email !</html>");
			stateLabel.setForeground(Color.RED);
			stateLabel.setVisible(true);
		}
		
		if (state == RegisterState.USERNAMENOTVALID) {
			stateLabel.setText("<html>Username is not valid.<br>"
					+ "Please enter a valid username !</html>");
			stateLabel.setForeground(Color.RED);
			stateLabel.setVisible(true);
		}
	}
}
