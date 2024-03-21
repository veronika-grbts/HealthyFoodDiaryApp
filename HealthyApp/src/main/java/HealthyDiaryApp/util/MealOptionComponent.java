package HealthyDiaryApp.util;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import HealthyDiaryApp.entity.MealOption;
import HealthyDiaryApp.enums.MealType;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
/*
 * MealOptionComponent class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description: Этот класс содержит методы для получения списка вариантов блюд по типу и получения информации
 * о вариантах блюд по их идентификатору.
 */

@Slf4j
public class MealOptionComponent {

    //Метод для списоку варіантів страв за типом страви
    public List<MealOption> getAllMealOptionsByType(MealType mealType) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<MealOption> criteria = builder.createQuery(MealOption.class);
            Root<MealOption> root = criteria.from(MealOption.class);
            criteria.select(root).where(builder.equal(root.get("mealType"), mealType));
            Query<MealOption> query = session.createQuery(criteria);
            return query.getResultList();
        } catch (HibernateException e) {
            log.error("An error occurred while getting meal options by type: {}",
                    mealType, e);
            return null;
        }
    }

    //Метод для отримання назви по айди
    public String getMealOptionNameById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        String mealOptionName = null;
        try {
            tx = session.beginTransaction();
            MealOption mealOption = session.get(MealOption.class, id);
            mealOptionName = mealOption.getName();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("An error occurred while getting meal option name for id {}",
                    id, e);
        }finally {
            session.close();
        }
        return mealOptionName;
    }


    public MealOption getMealOptionById(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        MealOption mealOption = null;
        try {
            tx = session.beginTransaction();
            mealOption = session.get(MealOption.class, id);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return mealOption;
    }


}
