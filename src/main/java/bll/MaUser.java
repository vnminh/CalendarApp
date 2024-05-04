package bll;
import java.util.ArrayList;
import java.util.*;

import org.hibernate.Session;

import dao.*;
import model.*;
import util.HibernateUtil;
public class MaUser {
	private static MaUser Instance;
	public static MaUser getInstance()
	{
		if (Instance == null) Instance = new MaUser();
		return Instance;
	}
	public User getByEmail(String email, Session s)
	{
		List<User> users = HQLutil.getInstance().doSelectByField(User.class, "email", email, s);
		User res =null;
		if (users!=null && users.size()>0) res = users.get(0); 
		return res;
	}
	public List<User> getListByEmail(List<String> emails, Session s)
	{
		List<User> res = new ArrayList<User>();
		for (String e: emails)
		{
			User u = getByEmail(e,s);
			if (u!=null) res.add(u);
		}
		return res;
	}
	public void deleteMeet(User user, Meeting meet, Session s)
	{
		
		meet.deleteUser(user);
		HQLutil.getInstance().doUpdate(user, s);
		if (meet instanceof Appointment)
		{
			user.getListApp().remove(meet);
			MaApp.getInstance().delete((Appointment)meet, s);
		}
		else 
		{
			user.getListGrM().remove(meet);
			HQLutil.getInstance().doUpdate(meet, s);
		}
	}
	public Vector<Vector<String>> getAllMeetingData (String id, Session s)
	{
		Vector<Vector<String>> res = new Vector<Vector<String>>();
		User u = HQLutil.getInstance().doSelectById(User.class, id, s);
		for (Appointment a: u.getListApp())
		{
			Vector<String> r = new Vector<String>();
			r.add(a.getId());r.add(a.getName());
			Date d = a.getDate();
			r.add(d.getDate()+"/"+d.getMonth()+"/"+d.getYear());
			r.add(a.getFrom().toString());r.add(a.getTo().toString());
			r.add(a.getLocation());
			r.add("App");
			res.add(r);
		}
		for (GroupMeeting grm: u.getListGrM())
		{
			Vector<String> r = new Vector<String>();
			r.add(grm.getId());r.add(grm.getName());
			Date d = grm.getDate();
			r.add(d.getDate()+"/"+d.getMonth()+"/"+d.getYear());
			r.add(grm.getFrom().toString());r.add(grm.getTo().toString());
			r.add(grm.getLocation());
			r.add("Grm");
			res.add(r);
		}
		return res;
	}
	public User checkUser(String email, String pass, Session s)
	{
		User u = getByEmail(email, s);
		if (u == null) return null;
		if (!u.getPassword().equals(pass)) return null;
		return u;
	}
	public boolean addUser(String name, String email, String pass, Session s)
	{
		User u = getByEmail(email, s);
		if (u!=null) return false;
		User newu = new User();
		newu.setName(name);
		newu.setEmail(email);
		newu.setPassword(pass);
		HQLutil.getInstance().doInsert(newu, s);
		return true;
	}
}
