package HealthyDiaryApp.util;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import HealthyDiaryApp.entity.Drink;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;

@Slf4j
public class DrinkComponent {
    //Метод для отримання всіх елементів з таблиці Drink
    public List<Drink> getAllDrinks() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Drink> query = builder.createQuery(Drink.class);
            Root<Drink> root = query.from(Drink.class);
            query.select(root);
            return session.createQuery(query).getResultList();
        } catch (HibernateException e) {
            log.error("An error occurred while getting all drinks", e);
            return Collections.emptyList();
        }
    }



    //Метод для пошуку айди напою по назві
    public Integer findDrinkIdByName(String drinkName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Integer> criteriaQuery = builder.createQuery(Integer.class);
            Root<Drink> root = criteriaQuery.from(Drink.class);
            criteriaQuery.select(root.get("idDrink"))
                    .where(builder.equal(root.get("nameDrink"), drinkName));

            Query<Integer> query = session.createQuery(criteriaQuery);
            Integer drinkId = query.uniqueResult();

            session.getTransaction().commit();
            return drinkId;
        }catch (NoResultException e) {
            log.warn("No drink found with name: {}", drinkName, e);
            return null;
        }catch (HibernateException e) {
            log.error("An error occurred while finding drink ID by name: {}",
                    drinkName, e);
            return null;
        }
    }

    //Методи для отримання назви страви та напитка по айди
    public String getDrinkNameById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        String drinkName = null;
        try {
            tx = session.beginTransaction();
            Drink drink = session.get(Drink.class, id);
            drinkName = drink.getNameDrink();
            tx.commit();
        }  catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("An error occurred while getting drink name for id {}",
                    id, e);
        } finally {
            session.close();
        }
        return drinkName;
    }
}
