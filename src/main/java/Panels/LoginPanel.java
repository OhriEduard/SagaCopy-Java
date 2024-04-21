package Panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Entities.User;
import Frames.AdminFrame;
import Frames.MainFrame;
import Frames.UserFrame;
import MyTextFields.MyPasswordField;
import MyTextFields.MyTextField;
import Repository.EntityManagerFactoryCreator;
import Repository.MyRepository;

public class LoginPanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	JTextField username;
    JTextField password;
    JButton loginButton;
    JButton openRegisterButton;
    JFrame parentFrame;
    
    JLabel loginFailed;

    public LoginPanel(CardLayout cardLayout, JPanel cardPanel, JFrame mainFrame) {
    	parentFrame = mainFrame;
    	
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(400, 200));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 0, 0, 0),
                BorderFactory.createLineBorder(Color.BLACK)
        ));
        
        JPanel credentialsPanel = new JPanel(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 10, 0, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel usernameLabel = new JLabel("Username: ");
        username = new MyTextField(30, 25);
        credentialsPanel.add(usernameLabel, gbc);
        gbc.insets = new Insets(0, 10, 10, 10);
        gbc.gridy++;
        credentialsPanel.add(username, gbc);

        JLabel passwordLabel = new JLabel("Password: ");
        gbc.insets = new Insets(0, 10, 0, 10);
        gbc.gridy++;
        credentialsPanel.add(passwordLabel, gbc);
        gbc.insets = new Insets(0, 10, 10, 10);
        gbc.gridy++;
        password = new MyPasswordField(30,25);
        credentialsPanel.add(password, gbc);
        
        loginFailed = new JLabel("Username or password are wrong !");
        loginFailed.setVisible(false);
        loginFailed.setForeground(Color.RED);
        
        GridBagConstraints gbcFailedLogin = new GridBagConstraints();
        gbcFailedLogin.gridx = 0;
        gbcFailedLogin.gridy = ++gbc.gridy;
        gbcFailedLogin.gridwidth = 2;
        gbcFailedLogin.fill = GridBagConstraints.CENTER;
        credentialsPanel.add(loginFailed, gbcFailedLogin);
        
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        buttonsPanel.setPreferredSize(new Dimension(400,50));
        
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100,25));
        loginButton.addActionListener(this);
        
        openRegisterButton = new JButton("Register");
        openRegisterButton.setPreferredSize(new Dimension(100,25));
        
        openRegisterButton.addActionListener(new ActionListener() {
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
        
        gbc.insets = new Insets(0, 10, 0, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        
        buttonsPanel.add(loginButton, gbc);
        gbc.gridx++;
        buttonsPanel.add(openRegisterButton, gbc);
        
        DocumentListener hideStateListener = new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				loginFailed.setVisible(false);
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				loginFailed.setVisible(false);
			}
			@Override
			public void changedUpdate(DocumentEvent e) {

			}
		};
		
		username.getDocument().addDocumentListener(hideStateListener);
		password.getDocument().addDocumentListener(hideStateListener);
		
        
        this.add(credentialsPanel, BorderLayout.CENTER);
        this.add(buttonsPanel, BorderLayout.SOUTH);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginButton) {
			MyRepository<User> userRepository = new MyRepository<>(User.class, 
					EntityManagerFactoryCreator.obtainOrCreateEntityManagerFactory());
			
			if (username.getText().toLowerCase().equals("admin"))
				if (password.getText().toLowerCase().equals("admin")) {
					parentFrame.dispose();
					AdminFrame newWindow = new AdminFrame();
					return;
				}
			
			User user = userRepository.findById(username.getText());
			if (user != null) {
				if (user.getPassword().equals(password.getText()))
				{
					parentFrame.dispose();
					UserFrame newWindow = new UserFrame(user);
				}
			}
			loginFailed.setVisible(true);
		}
	}
}



