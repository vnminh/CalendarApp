package bll;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.hibernate.Session;

import dao.*;
import model.*;
import util.HibernateUtil;

public class  MaMeeting {
	private static MaMeeting Instance;
	public static MaMeeting getInstance()
	{
		if (Instance == null) Instance = new MaMeeting();
		return Instance;
	}
	public void addReminder (Meeting meet , List<Reminder> rlist)
	{
		meet.setListRmd(rlist);
		for (Reminder r: rlist)
		{
			r.setMeeting(meet);
		}
	}
	public void saveToDB(Meeting meet, Session s)
	{
		HQLutil.getInstance().doInsert(meet, s);
	}
	public Meeting checkRepeat(String u_id,Meeting meet, Session s)
	{
		List<Meeting> meets = MeetingDAO.getInstance().doSelectRepeat(meet.getDate(), meet.getFrom(), meet.getTo(), s);
		for (int i=0;i<meets.size();i++)
		{
			if (meets.get(i).containUser(u_id)) return meets.get(i);
		}
		return null;
		
	}
	public void setUser(List<User> u, Meeting meet)// set ko can update vi se lun sau
	{
		meet.setUser(u);
		if (meet instanceof Appointment)
		{
			for (int i=0;i<u.size();i++)
			{
				u.get(i).getListApp().add((Appointment)meet);
			}
		}
		else
		{
			for (int i=0;i<u.size();i++)
			{
				u.get(i).getListGrM().add((GroupMeeting)meet);
			}
		}
		
	}
	
	public void addUser(User u, Meeting meet)// add ko can update vi se lun sau
	{
		meet.addUser(u);
		if (meet instanceof Appointment)
		{
			u.getListApp().add((Appointment) meet);
		}
		else
		{
			u.getListGrM().add((GroupMeeting) meet);
		}
	}
	
	public void addListUser(List<User> u, Meeting meet,Session s)// add ko can update vi se lun sau
	{
		meet.addListUser(u);
		for(int i=0;i<u.size();i++)
		{
			if (meet instanceof Appointment)
			{
				u.get(i).getListApp().add((Appointment) meet);
			}
			else
			{
				u.get(i).getListGrM().add((GroupMeeting) meet);
			}
		}
	}
	public Vector<Vector<String>> searchByDate(String id, Date date, Session s)
	{
		Vector<Vector<String>> res = new Vector<Vector<String>>();
		List<Meeting> meets = MeetingDAO.getInstance().searchByDate(date, s);
		for (int i=0;i<meets.size();i++)
		{
			if (meets.get(i).containUser(id))
			{
				Vector<String> r = new Vector<String>();
				r.add(meets.get(i).getId());r.add(meets.get(i).getName());
				Date d = meets.get(i).getDate();
				r.add(d.getDate()+"/"+d.getMonth()+"/"+d.getYear());
				r.add(meets.get(i).getFrom().toString());r.add(meets.get(i).getTo().toString());
				r.add(meets.get(i).getLocation());
				if (meets.get(i) instanceof Appointment)
				{
					r.add("App");
				}
				else
				{
					r.add("GrM");
				}
				res.add(r);
			}
		}
		return res;
		
	}
	
}
