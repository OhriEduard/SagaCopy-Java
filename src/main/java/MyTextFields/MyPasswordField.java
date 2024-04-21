package MyTextFields;

import javax.swing.*;
import java.awt.*;

public class MyPasswordField extends JPasswordField {
	public MyPasswordField(int width, int height) {
		super(15);
		setPreferredSize(new Dimension(width, height));
	}
	
	public MyPasswordField(int width, int height, int columns) {
		super(columns);
		setPreferredSize(new Dimension(width, height));
	}
}
