package ui;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.hibernate.Session;
import org.hibernate.internal.build.AllowSysOut;
import java.util.*;
import com.toedter.calendar.*;

import bll.MaMeeting;
import bll.MaUser;
import dao.HQLutil;
import model.Meeting;
import model.User;
import util.HibernateUtil;

public class MainForm extends JDialog
{
	private JFrame parent;
	private User user;
	private JButton button_add = new JButton("Add new"), button_allmeet = new JButton("All Meeting");
	private JDateChooser datechooser = new JDateChooser(new Date());
	private SpinnerNumberModel snm_hour = new SpinnerNumberModel(new Date().getHours(), 0, 23, 1);
	private SpinnerNumberModel snm_mins = new SpinnerNumberModel(new Date().getMinutes(), 0, 59, 1);
	private JSpinner hour = new JSpinner(snm_hour) , mins = new JSpinner(snm_mins);
	private JLabel label_date = new JLabel("Date :"), label_time = new JLabel("Time :"), label_del = new JLabel(":");
	private JPanel p1 = new JPanel(), p2 = new JPanel();
	private RegisterForm rform = null;
	private JMenuBar menubar = new JMenuBar();
	private JMenu userop;
	private JMenuItem logout = new JMenuItem("Log out");
	private JTable tab = new JTable();
	private MyTableModel tm;
	private JScrollPane jsp  = new JScrollPane(tab);
	void GUI()
	{
		//set menubar
		userop = new JMenu(user.getName());
		this.setJMenuBar(menubar);
		menubar.add(userop);
		userop.add(logout);
		
		//set preference size
		this.datechooser.setPreferredSize(new Dimension(150,20));
		this.hour.setPreferredSize(new Dimension(50,20));
		this.mins.setPreferredSize(new Dimension(50,20));
		this.jsp.setPreferredSize(new Dimension(600,150));
		//
		this.datechooser.getDateEditor().setEnabled(false);
		this.hour.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				int h = (Integer)MainForm.this.hour.getValue();
				if (h>23) MainForm.this.hour.setValue(23);
				if (h<0) MainForm.this.hour.setValue(0);
			}
		});
		this.mins.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				int m = (Integer)MainForm.this.mins.getValue();
				if (m>59) MainForm.this.mins.setValue(59);
				if (m<0)  MainForm.this.mins.setValue(0);
				
			}
		});
		this.button_add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (rform == null) rform = new RegisterForm(MainForm.this, true);
				Date date = new Date(MainForm.this.datechooser.getDate().getYear(),MainForm.this.datechooser.getDate().getMonth(),MainForm.this.datechooser.getDate().getDay());
				date.setHours((Integer)MainForm.this.hour.getValue());
				date.setMinutes((Integer)MainForm.this.mins.getValue());
				rform.setInfo(date,MainForm.this.user);
				rform.setVisible(true);
			}
		});
		logout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				MainForm.this.dispose();
				MainForm.this.parent.setVisible(true);
			}
		});
		this.addWindowListener(new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
			MainForm.this.dispose();
			MainForm.this.parent.setVisible(true);		}
		});
		this.datechooser.getDateEditor().addPropertyChangeListener("date",new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// TODO Auto-generated method stub
				Session s = HibernateUtil.getSessionFactory().openSession();
				MainForm.this.tm = new MyTableModel(MaMeeting.getInstance().searchByDate(MainForm.this.user.getId(), MainForm.this.datechooser.getDate(), s));
				MainForm.this.tab.setModel(tm);
				s.close();
			}
		});
		this.button_allmeet.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Session s = HibernateUtil.getSessionFactory().openSession();
				MainForm.this.tm = new MyTableModel(MaUser.getInstance().getAllMeetingData(MainForm.this.user.getId(), s));
				MainForm.this.tab.setModel(tm);
				s.close();
			}
		});

		//set layout
		p2.add(button_add);p2.add(button_allmeet);
		p1.add(label_date);p1.add(datechooser);
		p1.add(label_time);p1.add(hour);p1.add(label_del);p1.add(mins);
		this.setLayout(new BorderLayout());
		this.add(p1,BorderLayout.NORTH);this.add(jsp);
		this.add(p2,BorderLayout.SOUTH);
		this.setLocation(450, 250);
		this.pack();
		this.setVisible(true);
	}
	public MainForm(JFrame parent,  User u, boolean modal)
	{
		super(parent,modal);
		this.parent = parent;
		MainForm.this.user = u;
		GUI();
	}
}
