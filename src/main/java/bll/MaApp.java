package bll;

import org.hibernate.Session;

import dao.HQLutil;
import model.Appointment;
import model.User;
import util.HibernateUtil;

public class MaApp extends MaMeeting {
	private static MaApp Instance;
	public static MaApp getInstance()
	{
		if (Instance == null) Instance = new MaApp();
		return Instance;
	}
	public void delete(Appointment a,Session s) {
		HQLutil.getInstance().doDeleteById(a, s);
	}
	public void setUser(Appointment a, User u)
	{
		a.setUser(u);
		u.getListApp().add(a);
	}
	public void saveToDB(Appointment a, Session s)
	{
		HQLutil.getInstance().doInsert(a, s);
	}
}
