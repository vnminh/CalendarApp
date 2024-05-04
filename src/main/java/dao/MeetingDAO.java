package dao;

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

import model.Meeting;
import model.User;

public class MeetingDAO {
	private static MeetingDAO Instance;
	public static MeetingDAO getInstance()
	{
		if (Instance == null)
		{
			Instance = new MeetingDAO();
		}
		return Instance;
	}
	public int maxId(Session s)
	{
		int res;
		CriteriaBuilder builder = s.getCriteriaBuilder();
		CriteriaQuery<String> criteria = builder.createQuery(String.class);
		Root<Meeting> root = criteria.from(Meeting.class);
		criteria.select(builder.max((Expression)root.get("id")));
		String maxid = s.createQuery(criteria).getSingleResult();
		if (maxid == null || maxid.equals("")) return 0;
		res=Integer.parseInt(maxid.substring(2));
		return res;
	}

	public  List<Meeting> doSelectRepeat(Date d,Time f, Time t,Session s)
	{
		
		CriteriaBuilder builder = s.getCriteriaBuilder();
		CriteriaQuery<Meeting> criteria = builder.createQuery(Meeting.class);
		Root<Meeting> root = criteria.from(Meeting.class);
		Expression<Boolean> ex1 = builder.and(builder.equal(root.get("timeFrom"), f),builder.equal(root.get("timeTo"), t)) ;
		Expression<Boolean> ex2 = builder.equal(root.get("date"), d);
		criteria.where(builder.and(ex1,ex2));
		List<Meeting> res=null;
		try
		{
			res = s.createQuery(criteria).list();
		}
		catch (NoResultException ex)
		{
			System.out.println(ex.getMessage());
		}
		return res;
	}
	public List<Meeting> searchByDate(Date d, Session s)
	{
		CriteriaBuilder builder = s.getCriteriaBuilder();
		CriteriaQuery<Meeting> criteria = builder.createQuery(Meeting.class);
		Root<Meeting> root = criteria.from(Meeting.class);
		Expression<Boolean> ex = builder.equal(root.get("date"), d);
		criteria.where(ex);
		List<Meeting> res=null;
		try
		{
			res = s.createQuery(criteria).list();
		}
		catch (NoResultException e)
		{
			System.out.println(e.getMessage());
		}
		return res;
	}
	
}
