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

public class AddReminderForm extends JDialog{
	private String rvalue ="";
	private JButton button_add = new JButton("Add");
	private JLabel label = new JLabel("Before :");
	private SpinnerNumberModel snm_day = new SpinnerNumberModel(0, 0, 6, 1);
	private SpinnerNumberModel snm_hour = new SpinnerNumberModel(0, 0, 23, 1);
	private SpinnerNumberModel snm_mins = new SpinnerNumberModel(0, 0, 59, 1);
	private JSpinner day = new JSpinner(snm_day), hour = new JSpinner(snm_hour) , mins = new JSpinner(snm_mins);
	private JPanel p1 = new JPanel();
	void GUI()
	{
		//set preference size
		this.day.setPreferredSize(new Dimension(50,20));
		this.hour.setPreferredSize(new Dimension(50,20));
		this.mins.setPreferredSize(new Dimension(50,20));
		//layout
		p1.add(label);p1.add(day);p1.add(new JLabel("day(s)"));p1.add(hour);p1.add(new JLabel("hour(s)"));p1.add(mins);p1.add(new JLabel("min(s)"));
		p1.add(button_add);
		this.add(p1);
		this.setLocation(450, 250);
		this.pack();
		button_add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String d =  AddReminderForm.this.day.getValue().toString();
				String h = AddReminderForm.this.hour.getValue().toString();
				String m = AddReminderForm.this.mins.getValue().toString();
				if (!d.equals("0")||!h.equals("0")||!m.equals("0"))
					AddReminderForm.this.rvalue="Before "+d+" day(s) "+h+" hour(s) "+m+" min(s)" ;
				AddReminderForm.this.setVisible(false);
			}
		});
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				AddReminderForm.this.setVisible(false);
			}
		});
	}
	public String showDialog()
	{
		this.setVisible(true);
		return rvalue; 
	}
	public AddReminderForm(JDialog owner,boolean modal)
	{
		super(owner, modal);
		GUI();
	}
}
