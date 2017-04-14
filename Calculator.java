package project;

import java.lang.Math;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane; //for div by zero error message
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Calculator extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static Font fontLarge;
	private boolean isFirstCalc = true; //is this the first button press?
	private double previousValue; //previous value
	private boolean previous = false; //is previous button an operation?
	private boolean decimal = false; //is there a decimal point?
	private int zeroes = 0; //accounts for zeroes after decimal point
	private String prevOp; //previous operation
	Display display;
	JPanel buttons;
	
	public Calculator() {
		Font font = getFont();
		fontLarge = font.deriveFont((float) (2.5*font.getSize2D()));
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createLineBorder(new Color(20, 20, 20),5,true));
		display=new Display();
		display.setFont(fontLarge);
		display.setForeground(new Color(225, 235, 255));
		
		add(display,BorderLayout.PAGE_START);
		buttons = new JPanel(new GridLayout(5,4,5,5));
		
		addButton("CE", "Clear only this entry");
		addButton("C", "Clear all");
		addButton("<X", "Backspace");		
		addButton("/", "Divide");	
		addButton("sin", "Radial sine function");
		addButton("aSin", "Radial inverse sine");
		addButton("lnx", "Natural logarithm (base e)");
		
		addButton("7", "7");
		addButton("8", "8");
		addButton("9", "9");
		addButton("x", "Multiply");
		addButton("cos", "Radial cosine function");
		addButton("aCos", "Radial inverse cosine");
		addButton("logx", "Logarithm (base 10)");
		
		addButton("4", "4");
		addButton("5", "5");
		addButton("6", "6");
		addButton("-", "Subtract");
		addButton("tan", "Radial tangent fuction");
		addButton("aTan", "Radial inverse tangent");
		addButton("sqrt", "Take square root");
		
		addButton("1", "1");
		addButton("2", "2");
		addButton("3", "3");
		addButton("+", "Add");
		addButton("pi", "Pi rounded to 6 digits");
		addButton("1/x", "Take multiplicative inverse");
		addButton("cbrt", "Take cube root");
		
		addButton("+/-", "Negate");		
		addButton("0", "0");
		addButton(".", "Add decimal point");
		addButton("=", "Solve operation");
		addButton("e^x", "Raise e to display power");
		addButton("10^x", "Raise 10 to display power");
		addButton("toRad", "Convert degrees to radians");
		
		add(buttons,BorderLayout.PAGE_END);
	}
	
	private void addButton(String name, String text) {
		JButton button = new JButton(name);
		button.setToolTipText(text);
		button.setBackground(new Color(45, 45, 45));
		button.setForeground(new Color(225, 235, 255));
		button.setFont(fontLarge);
		button.addActionListener(this);
		button.setActionCommand(name);
		buttons.add(button);
	}
	
	
	
	public void actionPerformed(ActionEvent e) { //TOMORROW KYLE: fix operator algorithm first, then nums
		String cmd = e.getActionCommand();       //algorithm. rest shouldnt be too hard (?)
		if (isFirstCalc == true) {
			previousValue = 0;
		}
		if (cmd == "CE") {
			zeroes = 0;
			decimal = false;
			display.setValue(0);
		} else if (cmd == "C") {
			display.setValue(0);
			previous = false;
			decimal = false;
			zeroes = 0;
			previousValue = 0;
		} else if (cmd == "<X") {
			if (display.value != 0 || decimal == true) {
				/* StringBuilder is something I learned about online. To me, it seems like StringBuilder
				   is to String as ArrayList is to Array. StringBuilder is much more convenient than
				   String in this case because StringBuilder is mutable. Here is my source:
				   https://www.tutorialspoint.com/java/lang/java_lang_stringbuilder.htm                 */
				Double valueProper = new Double(display.value); //double -> string and vice versa only works
																//on proper Double objects, not primitives
				StringBuilder valString = new StringBuilder(valueProper.toString());
				char secondLast = valString.charAt(valString.length()-2);
				char last = valString.charAt(valString.length()-1);
				if (secondLast == '.' && last == '0') {
					if (decimal == true) {
						decimal = false;
					} else {
						if (valString.length() == 3) {
							valString = new StringBuilder("0");
						} else {
							valString.delete(valString.length()-2, valString.length());
							for (int i = valString.length()-1 ; i != -1 ; i--) {
								if (i != 0) {
									valString.deleteCharAt(i);
									break;
								}
							}
						}
						valString.append(".0");
					}
				} else {
					valString.replace(valString.length()-1, valString.length(), "0");
				}
				String s = "";
				for (int i = 0 ; i < valString.length() ; i++) {
					s += valString.charAt(i);
				}
				display.setValue(Double.parseDouble(s));
			}
		} else if (cmd == "pi") {
			display.setValue(Math.PI);
		} else if (cmd == "sin") {
			display.setValue(Math.sin(display.value));
		} else if (cmd == "cos") {
			display.setValue(Math.cos(display.value));
		} else if (cmd == "tan") {
			display.setValue(Math.tan(display.value));
		} else if (cmd == "aSin") {
			display.setValue(Math.asin(display.value));
			if (Double.isNaN(display.value)) {
				display.value = 0; //if you get infinity, no problem, just continue doing what you're doing
			}					   //(check with static Double method)
		} else if (cmd == "aCos") {
			display.setValue(Math.acos(display.value));
			if (Double.isNaN(display.value)) {
				display.value = 0;
			}
		} else if (cmd == "aTan") {
			display.setValue(Math.atan(display.value));
			if (Double.isNaN(display.value)) {
				display.value = 0;
			}
		} else if (cmd == "e^x") {
			display.setValue(Math.exp(display.value));
		} else if (cmd == "1/x") {
			if (display.value != 0) {
				display.setValue(1/display.value);
			} else {
				JOptionPane.showMessageDialog(new JPanel(), "Cannot divide by zero", "Warning",
				        JOptionPane.WARNING_MESSAGE);
				display.setValue(0); //1/x is useful, but can't have 1/0
			}
		} else if (cmd == "10^x") {
			display.setValue(Math.pow(10, display.value)); 
		} else if (cmd == "lnx") {
			display.setValue(Math.log(display.value)); //ln(x) is undefined iff x<=0
			if (Double.isNaN(display.value)) {
				display.value = 0;
			}
		} else if (cmd == "logx") {
			display.setValue(Math.log10(display.value)); //log10(x) is undefined iff x<0
			if (Double.isNaN(display.value)) {
				display.value = 0;
			}
		} else if (cmd == "sqrt") {
			display.setValue(Math.sqrt(display.value));
			if (Double.isNaN(display.value)) {
				display.value = 0;
			}
		} else if (cmd == "cbrt") {
			display.setValue(Math.cbrt(display.value));
		} else if (cmd == "toRad") {
			display.setValue(Math.toRadians(display.value));
		} else if (cmd == "+" || cmd == "-" || cmd == "x" || cmd == "/") {
			zeroes = 0;
			decimal = false;
			if (previous == false) {
				previousValue = display.value;
				prevOp = cmd;
			} else {
				prevOp = cmd;
			}
			previous = true;
		} else if (cmd == "=") {
			zeroes = 0;
			decimal = false;
			if (prevOp == "+") {
				display.setValue(previousValue+display.value);
				previousValue = display.value;
			} else if (prevOp == "-") {
				display.setValue(previousValue-display.value);
				previousValue = display.value;
			} else if (prevOp == "x") {
				display.setValue(previousValue*display.value);
				previousValue = display.value;
			} else {
				if (display.value != 0) {
					display.setValue(previousValue/display.value);
					previousValue = display.value;
				} else {
					JOptionPane.showMessageDialog(new JPanel(), "Cannot divide by zero", "Warning",
					        JOptionPane.WARNING_MESSAGE);
					display.setValue(0); //catch divide by zero error, then set value to default 0.0
				}
			}
			previous = true;
		} else if (cmd == ".") {
			if (previous == true) {
				display.setValue(0);
			}
			decimal = true;
			previous = false;
		} else if (cmd == "+/-") {
			if (display.value != 0) {
				display.setValue(-display.value);
			}
		} else {
			if (previous == true) {
				display.value = 0;
				zeroes = 0;
			}
			Double valueProper = display.value;
			StringBuilder valString;
			if (valueProper != 0.0) {
				valString = new StringBuilder(valueProper.toString());
				if (valString.charAt(valString.length()-2) == '.' && valString.charAt(valString.length()-1) 
						== '0' && decimal == false) {
					valString.delete(valString.length()-2, valString.length());
					valString.append(cmd);
				} else if (valString.charAt(valString.length()-2) == '.' && valString.charAt(valString.length()-1) 
						== '0' && decimal == true) {
					if (cmd == "0") {
						zeroes += 1;
					} else {
						valString.delete(valString.length()-1, valString.length());
						for (int i=0 ; i<zeroes ; i++) {
							valString.append(0);
						}
						valString.append(cmd);
						zeroes = 0;
					}
				} else {
					if (cmd == "0") {
						zeroes += 1;
					} else {
						for (int i=0 ; i<zeroes ; i++) {
							valString.append(0);
						}
						valString.append(cmd);
						zeroes = 0;
					}
				}
			} else {
				if (decimal == false) {
					valString = new StringBuilder(cmd);
				} else {
					if (cmd == "0") {
						zeroes += 1;
						valString = new StringBuilder("0.0");
					} else {
						valString = new StringBuilder("0.");
						for (int i=0 ; i<zeroes ; i++) {
							valString.append(0);
						}
						valString.append(cmd);
						zeroes = 0;
					}
				}
			}
			String s = "";
			for (int i = 0 ; i < valString.length() ; i++) {
				s += valString.charAt(i);
			}
			valueProper = Double.parseDouble(s);
			display.setValue(valueProper);
			previous = false;
		}
		isFirstCalc = false;
	}
	
	private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Prof. Bartenstein's Personal Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new Calculator());
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
	}

	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {            	
                createAndShowGUI();
            }
        });

	}

}
