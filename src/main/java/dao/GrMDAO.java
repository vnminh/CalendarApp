package dao;

import java.sql.Time;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;

import model.Appointment;
import model.GroupMeeting;
import model.User;
import util.HibernateUtil;

public class GrMDAO {

	private static GrMDAO Instance;
	public static GrMDAO getInstance()
	{
		if (Instance == null)
		{
			Instance = new GrMDAO();
		}
		return Instance;
	}
	public GroupMeeting doSelectRepeat(String name,Time f, Time t,Session s)
	{
		CriteriaBuilder builder = s.getCriteriaBuilder();
		CriteriaQuery<GroupMeeting> criteria = builder.createQuery(GroupMeeting.class);
		Root<GroupMeeting> root = criteria.from(GroupMeeting.class);
		criteria.where(builder.and(builder.equal(root.get("name"), name) ,builder.and(builder.equal(root.get("timeFrom"), f),builder.equal(root.get("timeTo"), t))));
		GroupMeeting res=null;
		try
		{
			res = s.createQuery(criteria).getSingleResult();
		}
		catch (Exception ex)
		{
			
		}
		return res;
	}
	public void addToGrM(User user, GroupMeeting grm,Session s)
	{
		Transaction t = s.beginTransaction();
		s.update(grm);
		grm.getListUser().add(user);
		s.update(grm);
		t.commit();
	}
}
