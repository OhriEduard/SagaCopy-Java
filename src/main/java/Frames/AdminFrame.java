package Frames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Panels.AdminActionPanel;
import Panels.AdminActions;
import Panels.AdminDataPanel;

public class AdminFrame extends JFrame {
	JPanel actionPanel;
	JPanel dataPanel;
	
	public AdminFrame(){
		setTitle("Saga");
		setSize(750, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		getContentPane().setBackground(new Color(0, 36, 131));
		
		JPanel bodyPanel = new JPanel(new GridBagLayout());
		
		actionPanel = new AdminActionPanel();
		dataPanel = new AdminDataPanel();
		
		((AdminActionPanel) actionPanel).setAdminAction((AdminActions)dataPanel);
		((AdminDataPanel) dataPanel).setAdminAction((AdminActions)actionPanel);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		
		bodyPanel.add(actionPanel, gbc);
		
		gbc.gridx++;
		
		bodyPanel.add(dataPanel,gbc);
		
		setContentPane(bodyPanel);
		setVisible(true);
	}

}
