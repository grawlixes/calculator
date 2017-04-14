package project;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

public class Display extends JLabel {
	private static final long serialVersionUID = 1L;
	double value;
	
	public Display() {
		super();
		setOpaque(true);
		setHorizontalAlignment(JLabel.RIGHT);
		setBackground(new Color(45, 45, 45));
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		setValue(0.0);
	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(double value) {
		this.value = value;
		if (this.value==-0.0) this.value=0.0;
		setText(String.format("%18f", value));
		
	}
}


