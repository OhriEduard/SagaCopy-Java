package Panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AdminActionPanel extends JPanel implements AdminActions, ActionListener {
	
	AdminActions adminDataPanelActions;
	
	JButton adaugaButton;
	JButton modificaButton;
	JButton stergeButton;
	
	public AdminActionPanel() {
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		
		JLabel label = new JLabel("STORE MANAGEMENT");
		label.setFont(label.getFont().deriveFont(20f));
		label.setMaximumSize(new Dimension(200,50));
		label.setMinimumSize(new Dimension(50,50));
		this.add(label, gbc);
		gbc.gridy++;
		
		adaugaButton = new JButton("Adauga");
		adaugaButton.setPreferredSize(new Dimension(200,50));
		adaugaButton.addActionListener(this);
		this.add(adaugaButton, gbc);
		gbc.gridy++;
		
		modificaButton = new JButton("Modifica");
		modificaButton.setPreferredSize(new Dimension(200,50));
		modificaButton.addActionListener(this);
		this.add(modificaButton, gbc);
		gbc.gridy++;
		
		stergeButton = new JButton("Sterge");
		stergeButton.setPreferredSize(new Dimension(200,50));
		stergeButton.addActionListener(this);
		this.add(stergeButton, gbc);
		gbc.gridy++;
		
		this.setVisible(true);
	}
	
	public void setAdminAction(AdminActions adminDataPanelActions) {
		this.adminDataPanelActions = adminDataPanelActions;
	}

	@Override
	public void adaugaAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void modificaAction() {
		if (modificaButton.getText().equals("Salveaza")) {
			modificaButton.setText("Modifica");
		} else {
			modificaButton.setText("Salveaza");
		}
		
	}

	@Override
	public void stergeAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == adaugaButton) {
			adminDataPanelActions.adaugaAction();
		}
		
		if (e.getSource() == modificaButton) {
			adminDataPanelActions.modificaAction();
		}
		
		if (e.getSource() == stergeButton) {
			adminDataPanelActions.stergeAction();
		}
		
	}
}
