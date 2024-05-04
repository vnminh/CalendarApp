package dao;

import java.sql.Time;
import java.util.Date;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import model.Appointment;
import model.User;

public class AppointmentDAO {
	private static AppointmentDAO Instance;
	public static AppointmentDAO getInstance()
	{
		if (Instance == null)
		{
			Instance = new AppointmentDAO();
		}
		return Instance;
	}
	public  Appointment doSelectRepeat(User u,Date d,Time f, Time t,Session s)
	{
		CriteriaBuilder builder = s.getCriteriaBuilder();
		CriteriaQuery<Appointment> criteria = builder.createQuery(Appointment.class);
		Root<Appointment> root = criteria.from(Appointment.class);
		Expression<Boolean> ex1 = builder.and(builder.equal(root.get("timeFrom"), f),builder.equal(root.get("timeTo"), t)) ;
		Expression<Boolean> ex2 = builder.and(builder.equal(root.get("user"), u),builder.equal(root.get("date"), d)) ;
		criteria.where(builder.and(ex1,ex2));
		Appointment res=null;
		try
		{
			res = s.createQuery(criteria).getSingleResult();
		}
		catch (NoResultException ex)
		{
			System.out.println(ex.getMessage());
		}
		return res;
	}
}
