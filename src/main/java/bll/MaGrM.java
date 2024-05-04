package bll;


import java.util.List;

import org.hibernate.*;

import org.hibernate.Session;

import dao.GrMDAO;
import dao.HQLutil;
import model.Appointment;
import model.GroupMeeting;
import model.Meeting;
import model.User;
import util.HibernateUtil;

public class MaGrM extends MaMeeting{
	private static MaGrM Instance;
	public static MaGrM getInstance()
	{
		if (Instance == null) Instance = new MaGrM();
		return Instance;
	}
	public void delete(GroupMeeting grm, Session s) {
		HQLutil.getInstance().doDeleteById(grm, s);
	}
	public GroupMeeting checkJoin(Meeting meet, Session s)
	{
		GroupMeeting grm = GrMDAO.getInstance().doSelectRepeat(meet.getName(), meet.getFrom()	, meet.getTo(), s);
		return grm;
	}
	public void addListUser(List<User> users, GroupMeeting grm, Session s)
	{
		for (int i=0;i<users.size();i++)
		{
			grm.getListUser().add(users.get(i));
		 	users.get(i).getListGrM().add(grm);
		}
		HQLutil.getInstance().doUpdate(grm, s);
		// add thi chi can update 1 ob trong association
	}
	public void setAttendGrM(List<User> user, GroupMeeting grm, Session s)
	{
		grm.setListUser(user);
		for (User u:user)
		{
			u.getListGrM().add(grm);
		}
	}
}
