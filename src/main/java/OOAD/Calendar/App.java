package OOAD.Calendar;

import util.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	HibernateUtil.getSessionFactory();
        new ui.LoginForm("");
    }
}
