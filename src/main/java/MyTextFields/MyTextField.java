package MyTextFields;

import java.awt.Dimension;

import javax.swing.*;

public class MyTextField extends JTextField {
	public MyTextField(int width, int height) {
		super(15);
		setPreferredSize(new Dimension(width, height));
	}
	
	public MyTextField(int width, int height, int columns) {
		super(columns);
		setPreferredSize(new Dimension(width, height));
	}
}
