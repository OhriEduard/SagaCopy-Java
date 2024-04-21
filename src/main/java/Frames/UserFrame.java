package Frames;

import javax.swing.*;

import Entities.User;
import Panels.CompaniesPanel;
import Panels.ProdusePanel;
import Panels.SelectionListener;

import java.awt.*;

public class UserFrame extends JFrame {
	JPanel companiesPanel;
	JPanel productsPanel;
	
	public UserFrame(User user) {
		setTitle("Saga");
		setSize(1000, 550);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		getContentPane().setBackground(new Color(0, 36, 131));
		
		JPanel bodyPanel = new JPanel(new GridBagLayout());
		productsPanel = new ProdusePanel(user, null);
		companiesPanel = new CompaniesPanel(user, null);
		
		((ProdusePanel) productsPanel).setSelectionListener((SelectionListener) companiesPanel);
		((CompaniesPanel) companiesPanel).setSelectionListener((SelectionListener) productsPanel);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 0.45;
		gbc.fill = GridBagConstraints.BOTH;
		
		bodyPanel.add(companiesPanel, gbc);
		
		gbc.gridy++;
		gbc.weighty = 0.55;
		
		bodyPanel.add(productsPanel, gbc);
		
		setContentPane(bodyPanel);
		setVisible(true);
		
	}
	
}
