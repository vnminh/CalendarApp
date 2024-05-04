package ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.toedter.calendar.JDateChooser;

public class AddAttendentForm extends JDialog{
	private String rvalue="";
	private JButton button_add = new JButton("Add");
	private JLabel label_email = new JLabel("Email :");
	private JTextField t = new JTextField();
	private JPanel p1 = new JPanel();
	void GUI()
	{
		//set preference size
		t.setPreferredSize(new Dimension(150,20));
		//layout
		p1.add(label_email);p1.add(t);
		p1.add(button_add);
		this.setLayout(new GridLayout(1,2));
		this.add(p1);
		this.setLocation(450, 250);
		this.pack();
		button_add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				rvalue = AddAttendentForm.this.t.getText();
				AddAttendentForm.this.setVisible(false);
			}
		});
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				AddAttendentForm.this.setVisible(false);
			}
		});
	}
	public String showDialog()
	{
		this.setVisible(true);
		return rvalue;
	}
	public AddAttendentForm(JDialog owner,boolean modal)
	{
		super(owner, modal);
		GUI();
	}
}

