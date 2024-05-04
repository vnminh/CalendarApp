package dao;
import java.lang.reflect.*;
import java.util.*;


import javax.persistence.criteria.*;

import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

import util.*;
public class HQLutil {
	private static HQLutil Instance;
	public static HQLutil getInstance()
	{
		if (Instance == null)
		{
			Instance = new HQLutil();
		}
		return Instance;
	}
	public <T>int maxId(Class<T> c, Session s)
	{
		int res;
		CriteriaBuilder builder = s.getCriteriaBuilder();
		CriteriaQuery<String> criteria = builder.createQuery(String.class);
		Root<T> root = criteria.from(c);
		criteria.select(builder.max((Expression)root.get("id")));
		String maxid="";
		maxid = s.createQuery(criteria).getSingleResult();
		if (maxid == null || maxid.equals("")) return 0;
		res=Integer.parseInt(maxid.substring(2));
		return res;
	}
	@SuppressWarnings("deprecation")
	public <T> List<T> doSelectAll(Class<T> c, Session s)
	{
		List<T> res;
		Criteria cr = s.createCriteria(c);
		res = cr.list();
		return res;
	}
	public <T>	T doSelectById (Class<T> c, String id, Session s)
	{
		T res;
		res = s.get(c, id);
		return res;
	}
	public <T,V> List<T> doSelectByField (Class<T> c, String field_name, V value, Session s)
	{
		List<T> res;
		Criteria cr = s.createCriteria(c);
		cr.add(Restrictions.eq(field_name, value));
		res = cr.list();
		return res;
	}
	public <T> boolean doDeleteById (T ob, Session s)
	{
		boolean isSuccess = true;
		Transaction ts = s.getTransaction();
		try
		{
			ts.begin();
			s.remove(ob);
			ts.commit();
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			ts.rollback();
			isSuccess= false;
		}
		return isSuccess;
	}
	public <T> int doDeleteRange (List<T> obs, Session s) 
	{
		int res=0;
		for (T o:obs)
		{
			if (doDeleteById(o,s)) res++; 
		}
		return res;
	}
	public <T,V> int doDeleteAll(Class<T> c, String table_name, String field_name, V value, Session s)
	{
		int res;
		String hql = "delete from "+table_name+" o where o."+field_name+" = :value";
		res = s.createQuery(hql, c).setParameter("value",value).executeUpdate();
		return res;
	}
	
	
	public <T> boolean doInsert (T ob, Session s)
	{
		boolean isSuccess = true;
		Transaction ts = s.getTransaction();
		try
		{
			ts.begin();
			s.persist(ob);
			ts.commit();
		}
		catch (Exception ex)
		{
			ts.rollback();
			isSuccess= false;
		}
		return isSuccess;
	}
	public <T> int doInsertRange (List<T> obs, Session s)
	{
		int res=0;
		for (T o:obs)
		{
			if (doInsert(o,s)) res++; 
		}
		return res;
	}
	@SuppressWarnings("unchecked")
	public <T> T doUpdate (T ob, Session s)
	{
		T mergedob = null;
		Transaction ts = s.getTransaction();
		try
		{
			ts.begin();
			mergedob = (T)s.merge(ob);
			ts.commit();
		}
		catch (Exception ex)
		{
			ts.rollback();
		}
		
		return mergedob;
	}
	public <T> int doUpdateRange (List<T> obs, Session s)
	{
		int res=0;
		for (T o:obs)
		{
			if (doUpdate(o,s)!=null) res++; 
		}
		return res;
	}
}
