package util;

import org.hibernate.*;
import org.hibernate.boot.*;
import org.hibernate.boot.registry.*;
import org.hibernate.service.*;

import dao.HQLutil;
import model.Appointment;
import model.Reminder;
import model.User;
 
public class HibernateUtil {
 
    private static final SessionFactory sessionFactory = buildSessionFactory();
 
    private HibernateUtil() {
    }
 
    private static SessionFactory buildSessionFactory() {
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder() //
                .configure() // Load hibernate.cfg.xml from resource folder by default
                .build();
        Metadata metadata = new MetadataSources(serviceRegistry).getMetadataBuilder().build();
        return metadata.getSessionFactoryBuilder().build();
    }
 
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
 
    public static void close() {
        getSessionFactory().close();
    }
    public static void main(String[] args) {
    	
    	Session s = HibernateUtil.getSessionFactory().openSession();
    	Appointment app = new Appointment();
    	app.getListRmd();
    	s.close();
    	HibernateUtil.close();
		
	}
}