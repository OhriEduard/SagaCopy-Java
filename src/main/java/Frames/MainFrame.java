package Frames;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import Panels.LoginPanel;
import Panels.RegisterPanel;
import Repository.EntityManagerFactoryCreator;

public class MainFrame extends JFrame{
	
	public MainFrame() {
        setTitle("SAGA EDY VERSION");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(0, 36, 131));
        setSize(750, 500);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);
        setVisible(true);
        
        JPanel cardPanel = new JPanel(new CardLayout());
        
        LoginPanel loginPanel = new LoginPanel((CardLayout)cardPanel.getLayout(),cardPanel, this);
        RegisterPanel registerPanel = new RegisterPanel((CardLayout)cardPanel.getLayout(),cardPanel);
        
        cardPanel.add(loginPanel, "loginForm");
        cardPanel.add(registerPanel,"registerForm");
        cardPanel.setPreferredSize(cardPanel.getComponent(0).getPreferredSize());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;  
        gbc.anchor = GridBagConstraints.CENTER;
        
        this.add(cardPanel, gbc);
        
    }

}
