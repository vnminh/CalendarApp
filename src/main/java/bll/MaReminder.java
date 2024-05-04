package bll;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import dao.HQLutil;
import model.Appointment;
import model.Meeting;
import model.Reminder;
import model.User;
import util.HibernateUtil;

public class MaReminder {
	private static MaReminder Instance;
	public static MaReminder getInstance()
	{
		if (Instance == null) Instance = new MaReminder();
		return Instance;
	}
	public List<Reminder> processReminder(List<String> list)
	{
		List<Reminder> res= new ArrayList<Reminder>();
		for (String r: list)
		{
			String[] tok = r.split(" ");
			int[] t =new int[3];
			t[0] = Integer.parseInt(tok[1]);
			t[1] = Integer.parseInt(tok[3]);
			t[2] = Integer.parseInt(tok[5]);
			Reminder re = new Reminder();
			re.setSecondBeforeAlarm(t[0]*24*60+t[1]*60+t[2]);
			res.add(re);
		}
		return res;
		
	
	}
	public void saveToDB(Reminder reminder, Session s)
	{
		HQLutil.getInstance().doInsert(reminder, s);
	}
	
}
