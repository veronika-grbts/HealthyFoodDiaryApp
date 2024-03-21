package HealthyDiaryApp.util;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
/*
 * HibernateUtil class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description: Цей клас забезпечує доступ до об'єктів сесії для взаємодії з базою даних
 * за допомогою Hibernate framework. Він використовує налаштування з файлу
 * hibernate.cfg.xml для підключення до бази даних.
 */
public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.configure();
        return configuration.buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}



