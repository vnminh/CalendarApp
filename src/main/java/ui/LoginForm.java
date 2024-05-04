package ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.hibernate.Session;

import bll.MaUser;
import model.*;
import util.HibernateUtil;

public class LoginForm extends JFrame{
	private JLabel lemail = new JLabel("Email"), lpass = new JLabel("Password");
	private JButton button_login = new JButton("Login"), button_signup = new JButton("Sign up");
	private JPasswordField t2 = new JPasswordField();
	private JTextField t1 = new JTextField();
	private JPanel p1 = new JPanel(),p2 = new JPanel(), p3= new JPanel();
	void GUI()
	{
		// size
		lemail.setPreferredSize(new Dimension(80,20));
		lpass.setPreferredSize(new Dimension(80,20));
		t1.setPreferredSize(new Dimension(150,20));
		t2.setPreferredSize(new Dimension(150,20));
		
		//
		this.button_login.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Session s =HibernateUtil.getSessionFactory().openSession();
				User u = MaUser.getInstance().checkUser(t1.getText(),t2.getText(),s); 
				if (u!=null)
				{
					LoginForm.this.setVisible(false);
					new MainForm(LoginForm.this, u, true);
				}
				else
				{
					JOptionPane.showMessageDialog(LoginForm.this, "Email or password is incorrect");
				}
				s.close();
			}
		});
		this.button_signup.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new SignupForm(LoginForm.this, true);
			}
		});
		// layout
		p1.add(lemail);p1.add(t1);
		p2.add(lpass);p2.add(t2);
		p3.add(button_login);p3.add(button_signup);
		this.setLayout(new GridLayout(3,1));
		this.add(p1);this.add(p2);this.add(p3);
		this.setLocation(450, 250);
		this.pack();
		this.setVisible(true);
	}
	public LoginForm(String s)
	{
		super(s);
		GUI();
	}
	
}
