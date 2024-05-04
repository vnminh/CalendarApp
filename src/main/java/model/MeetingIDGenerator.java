package model;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.Session;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import dao.HQLutil;
import dao.MeetingDAO;
import util.HibernateUtil;

public class MeetingIDGenerator implements IdentifierGenerator,Configurable {
	static final int idLen=10;
	int nextId=-1;
	String prefix;
	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		if (nextId == -1)
		{
			Session s = HibernateUtil.getSessionFactory().openSession();
			this.nextId = MeetingDAO.getInstance().maxId(s)+1;
			s.close();
		}
		String id = "0000000000" + Integer.toString(this.nextId);
		int numLen = idLen - prefix.length();
		id = prefix + id.substring(id.length()-numLen,id.length());
		this.nextId++;
		return id;
	}
	@Override
	public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
		// TODO Auto-generated method stub
		this.prefix = params.getProperty("prefix");
	}
}
