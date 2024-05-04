package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JPopupMenu;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import org.hibernate.Session;

import com.toedter.calendar.JDateChooser;

import bll.MaApp;
import bll.MaGrM;
import bll.MaMeeting;
import bll.MaUser;
import dao.HQLutil;
import model.*;
import util.HibernateUtil;

public class RegisterForm extends JDialog {
	User user = new User(); 
	private JLabel l1 = new JLabel("Name"), l2 = new JLabel("Location"), lreminder = new JLabel("Reminder"), lfrom = new JLabel("From"), lto = new JLabel("To"), ldate = new JLabel("Date"), lattend = new JLabel("Attendant");
	private JTextField t1 = new JTextField(), t2 = new JTextField(); 
	private JPanel p = new JPanel(), pName = new JPanel(), pLocation = new JPanel(), pFrom = new JPanel(),pTo = new JPanel(), pDate = new JPanel(),pReminder = new JPanel(),pSubRemind  = new JPanel(),pSubAttend = new JPanel(),pAdd = new JPanel(),pAttend = new JPanel();
	private JList<String> listReminder = new JList<String>(), listAttend = new JList<String>();
	private JDateChooser dateChooser = new JDateChooser();
	private SpinnerNumberModel snm_fhour = new SpinnerNumberModel(0, 0, 23, 1);
	private SpinnerNumberModel snm_fmins = new SpinnerNumberModel(0, 0, 59, 1);
	private SpinnerNumberModel snm_thour = new SpinnerNumberModel(0, 0, 23, 1);
	private SpinnerNumberModel snm_tmins = new SpinnerNumberModel(0, 0, 59, 1);
	private JSpinner from_hour = new JSpinner(snm_fhour), from_mins = new JSpinner(snm_fmins);
	private JSpinner to_hour = new JSpinner(snm_thour), to_mins = new JSpinner(snm_tmins);
	private JButton button_add = new JButton("Add"), button_add_reminder = new JButton("Add reminder"), button_add_attend = new JButton("Add attendant");
	private JPopupMenu popup_re= new JPopupMenu();
	private JMenuItem pmItem_re = new JMenuItem("Delete");
	private JPopupMenu popup_at= new JPopupMenu();
	private JMenuItem pmItem_at = new JMenuItem("Delete");
	private AddAttendentForm atf=null;
	private AddReminderForm arf=null;
	void GUI()
	{
		//set size
		l1.setPreferredSize(new Dimension(60,20));
		l2.setPreferredSize(new Dimension(60,20));
		lreminder.setPreferredSize(new Dimension(60,20));
		lattend.setPreferredSize(new Dimension(60,20));
		lfrom.setPreferredSize(new Dimension(60,20));
		lto.setPreferredSize(new Dimension(60,20));
		ldate.setPreferredSize(new Dimension(60,20));
		t1.setPreferredSize(new Dimension(150,20));
		t2.setPreferredSize(new Dimension(150,20));
		dateChooser.setPreferredSize(new Dimension(150,20));
		dateChooser.getDateEditor().setEnabled(false);
		listAttend.setPreferredSize(new Dimension(200,60));
		listReminder.setPreferredSize(new Dimension(200,60));
		from_hour.setPreferredSize(new Dimension(50,20));
		from_mins.setPreferredSize(new Dimension(50,20));
		to_hour.setPreferredSize(new Dimension(50,20));
		to_mins.setPreferredSize(new Dimension(50,20));
		popup_re.add(pmItem_re);
		popup_at.add(pmItem_at);
		//listener
		this.button_add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Session session = HibernateUtil.getSessionFactory().openSession(); 
				if (t1.getText().equals("") || cmpTime((int)from_hour.getValue(), (int)from_mins.getValue(), (int)to_hour.getValue(), (int)to_mins.getValue())>=0 )
				{
					JOptionPane.showMessageDialog((JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, RegisterForm.this), "Can't add - Invalid appointment");
					return;
				}
				List<User> attends = MaUser.getInstance().getListByEmail(RegisterForm.this.getList(listAttend),session);
				RegisterForm.this.user = HQLutil.getInstance().doUpdate(RegisterForm.this.user, session);
				attends.add(RegisterForm.this.user);
				Meeting meet;
				if (attends.size()==1)
				{
					meet = new Appointment();
				}
				else
				{
					meet = new GroupMeeting();
				}
				meet.setDate(RegisterForm.this.dateChooser.getDate());
				meet.setName(t1.getText());
				meet.setFrom(new Time((int)RegisterForm.this.from_hour.getValue(), (int)RegisterForm.this.from_mins.getValue(), 0));
				meet.setTo(new Time((int)RegisterForm.this.to_hour.getValue(), (int)RegisterForm.this.to_mins.getValue(), 0));
				meet.setLocation(t2.getText());
				MaMeeting.getInstance().addReminder(meet, bll.MaReminder.getInstance().processReminder(getList(listReminder)));
				// check trung
				Meeting repeatMeet= MaMeeting.getInstance().checkRepeat(RegisterForm.this.user.getId(),meet,session);
				if (repeatMeet!=null)
				{
					int choice = JOptionPane.showConfirmDialog(RegisterForm.this, "Appointment with name "+repeatMeet.getName()+ " have same start and end time\nDelete the last one ?"); 
					if (choice == JOptionPane.OK_OPTION)
					{
						MaUser.getInstance().deleteMeet(user, repeatMeet,session);
						MaMeeting.getInstance().addListUser(attends, meet,session);
						MaMeeting.getInstance().saveToDB(meet, session);
						RegisterForm.this.setVisible(false);
					}
					
				}
				else
				{
					GroupMeeting joinM = MaGrM.getInstance().checkJoin(meet,session);
					if (joinM!=null)
					{
						int choice = JOptionPane.showConfirmDialog(RegisterForm.this, "There is a group meeting have same name as well as start and end time\nJoin this one ?"); 
						if (choice == JOptionPane.OK_OPTION)
						{			
							MaGrM.getInstance().addListUser(attends, joinM, session);
						}
						else 
						{
							MaMeeting.getInstance().setUser(attends,meet);
							MaMeeting.getInstance().saveToDB(meet,session);
						}
					}
					else
					{
						MaMeeting.getInstance().setUser(attends,meet);
						MaMeeting.getInstance().saveToDB(meet,session);
					}
					RegisterForm.this.setVisible(false);
				}
				session.close();
				return;
			}
		});
		this.button_add_attend.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (atf == null)
				{
					atf = new AddAttendentForm(RegisterForm.this, true);
				}
				String res = atf.showDialog();
				if (!res.equals("")&& !existInList(listAttend, res))
				{
					addList(listAttend, res);
				}
			}
		});
		this.button_add_reminder.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (arf == null)
				{
					arf = new AddReminderForm(RegisterForm.this, true);
				}
				String res = arf.showDialog();
				if (!res.equals("") && !existInList(listReminder, res))
				{
					addList(listReminder, res);
				}
			}
		});
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				RegisterForm.this.setVisible(false);
			}
		});
		this.listReminder.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				int index = listReminder.locationToIndex(e.getPoint());
				if (index!=-1 && e.getButton()==MouseEvent.BUTTON3)
				{
					listReminder.setSelectedIndex(index);
					popup_re.show(listReminder, e.getX(), e.getY());
				}
			}
		});
		this.pmItem_re.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int index = listReminder.getSelectedIndex();
				deleteList(listReminder,index);
			}
		});
		this.listAttend.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				int index = listAttend.locationToIndex(e.getPoint());
				if (index!=-1 && e.getButton()==MouseEvent.BUTTON3)
				{
					listAttend.setSelectedIndex(index);
					popup_at.show(listAttend, e.getX(), e.getY());
				}
			}
		});
		this.pmItem_at.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int index = listAttend.getSelectedIndex();
				deleteList(listAttend,index);
			}
		});
		//layout
		pName.add(l1); pName.add(t1);
		pDate.add(ldate);pDate.add(dateChooser);
		pFrom.add(lfrom);pFrom.add(from_hour);pFrom.add(new JLabel(":"));pFrom.add(from_mins);
		pTo.add(lto);pTo.add(to_hour);pTo.add(new JLabel(":"));pTo.add(to_mins);
		pLocation.add(l2);pLocation.add(t2);
		pReminder.setLayout(new BorderLayout());
		pReminder.add(lreminder,BorderLayout.NORTH);
		pReminder.add(listReminder);
		pSubRemind.add(button_add_reminder);
		pReminder.add(pSubRemind,BorderLayout.SOUTH);
		pAttend.setLayout(new BorderLayout());
		pAttend.add(lattend,BorderLayout.NORTH);
		pAttend.add(listAttend);
		pSubAttend.add(button_add_attend);
		pAttend.add(pSubAttend,BorderLayout.SOUTH);
		pAdd.add(button_add);
		p.setLayout(new GridBagLayout());
		addGridBagLayout(p, pName, 0, 0, 1, 2, GridBagConstraints.NONE, GridBagConstraints.WEST);
		addGridBagLayout(p, pDate, 0, 2, 1, 2, GridBagConstraints.NONE, GridBagConstraints.WEST);
		addGridBagLayout(p, pFrom, 0, 4, 1, 2, GridBagConstraints.NONE, GridBagConstraints.WEST);
		addGridBagLayout(p, pTo, 0, 6, 1, 2, GridBagConstraints.NONE, GridBagConstraints.WEST);
		addGridBagLayout(p, pLocation, 0, 8, 1, 2, GridBagConstraints.NONE, GridBagConstraints.WEST);
		addGridBagLayout(p, pReminder, 1, 0, 1, 5, GridBagConstraints.BOTH, -1);
		addGridBagLayout(p, pAttend, 1, 5, 1, 5, GridBagConstraints.BOTH, -1);
		addGridBagLayout(p, pAdd, 0, 10, 2, 1, GridBagConstraints.HORIZONTAL, -1);
		this.setLocation(450, 250);
		this.add(p);
		this.pack();
	}
	private static void addList(JList<String> l, String item)
	{
		List<String> list = getList(l);
		list.add(item);
		DefaultListModel<String> dlm = new DefaultListModel<String>();
		dlm.addAll(list);
		l.setModel(dlm);
	}
	private static void deleteList(JList<String> l, int index)
	{
		List<String> list = getList(l);
		list.remove(index);
		DefaultListModel<String> dlm = new DefaultListModel<String>();
		dlm.addAll(list);
		l.setModel(dlm);
	}
	private static boolean existInList(JList<String> l, String item)
	{
		List<String> list = getList(l);
		for (String r:list)
		{
			if (r.equals(item)) return true;
		}
		return false;
	}
	private static void deleteList(JList<String> l)
	{
		List<String> list = new ArrayList<String>();
		DefaultListModel<String> dlm = new DefaultListModel<String>();
		dlm.addAll(list);
		l.setModel(dlm);
	}
	private static List<String> getList(JList<String> list)
	{
		ListModel<String> lm = list.getModel();
		int len = lm.getSize();
		ArrayList<String> res = new ArrayList<String>();
		for (int i=0;i<len;i++)
		{
			res.add(lm.getElementAt(i));
		}
		return res;
	}
	static void addGridBagLayout(JPanel p,Component c, int x, int y, int width, int height, int fill, int anchor)
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.weightx=100;
		gbc.weighty=100;
		gbc.fill = fill;
		if (fill == GridBagConstraints.NONE) gbc.anchor = anchor;
		p.add(c, gbc);		
	}
	static int cmpTime(int fhour, int fmins, int thour, int tmins)
	{
		if (fhour != thour)
		{
			return fhour-thour;
		}
		else
		{
			return fmins-tmins;
		}
	}
	public void setInfo(Date date, User user)
	{
		this.user=user;
		this.dateChooser.setDate(date);
		this.from_hour.setValue(date.getHours());
		this.from_mins.setValue(date.getMinutes());
		this.to_hour.setValue(date.getHours()+(date.getMinutes()+30)/60);
		this.to_mins.setValue((date.getMinutes()+30)%60);
		deleteList(listAttend);
		deleteList(listReminder);
	}
	public RegisterForm(JDialog parent, boolean modal)
	{
		super(parent,modal);
		GUI();
	}
	public static void main(String[] args) {
		new RegisterForm(null, true);
	}
}
