package ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.hibernate.HibernateError;
import org.hibernate.Session;

import bll.MaUser;
import util.HibernateUtil;

public class SignupForm extends JDialog{
	private JLabel lemail = new JLabel("Email"), lpass = new JLabel("Password"), lname = new JLabel("Full name");
	private JButton button_cancel = new JButton("Cancel"), button_signup = new JButton("Sign up");
	private JTextField t1 = new JTextField(), t2 = new JTextField(),  t3 = new JTextField();
	private JPanel p1 = new JPanel(),p2 = new JPanel(), p3= new JPanel(), p4= new JPanel();
	void GUI()
	{
		//size
		lemail.setPreferredSize(new Dimension(80,20));
		lpass.setPreferredSize(new Dimension(80,20));
		lname.setPreferredSize(new Dimension(80,20));
		t1.setPreferredSize(new Dimension(150,20));
		t2.setPreferredSize(new Dimension(150,20));
		t3.setPreferredSize(new Dimension(150,20));
		
		this.button_signup.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		this.button_signup.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (t1.getText().equals(""))
				{
					JOptionPane.showMessageDialog(SignupForm.this, "Name is empty");
				}
				if (t2.getText().equals(""))
				{
					JOptionPane.showMessageDialog(SignupForm.this, "Email is empty");
				}
				if (t3.getText().equals(""))
				{
					JOptionPane.showMessageDialog(SignupForm.this, "Password is empty");
				}
				Session s = HibernateUtil.getSessionFactory().openSession();
				if (MaUser.getInstance().addUser(t1.getText(), t2.getText(), t3.getText(), s))
				{
					JOptionPane.showMessageDialog(SignupForm.this, "Sign up successfully");
					SignupForm.this.dispose();
				}
				else
				{
					JOptionPane.showMessageDialog(SignupForm.this, "Email has been used by another user");
				}
				s.close();
			}
		});
		this.button_cancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				SignupForm.this.dispose();
			}
		});
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				SignupForm.this.dispose();
			}		
		});
		//layout
		p2.add(lemail);p2.add(t2);
		p3.add(lpass);p3.add(t3);
		p1.add(lname);p1.add(t1);
		p4.add(button_signup);p4.add(button_cancel);
		this.setLayout(new GridLayout(4,1));
		this.add(p1);this.add(p2);this.add(p3);this.add(p4);
		this.setLocation(450, 250);
		this.pack();
		this.setVisible(true);
	}
	public SignupForm(JFrame parent, boolean modal)
	{
		super(parent, modal);
		GUI();
	}
}
