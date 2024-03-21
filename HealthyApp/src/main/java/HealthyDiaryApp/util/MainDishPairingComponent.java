package HealthyDiaryApp.util;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import HealthyDiaryApp.entity.MealOption;

import javax.persistence.NoResultException;
/*
 * MainDishPairingComponent class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description: Цей клас містить метод для знаходження додаткової страви за основною стравою.
 */
@Slf4j
public class MainDishPairingComponent {

    public Integer findAdditionalDishIdByMainDish(MealOption mainDish) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Query<Integer> query = session.createQuery(
                    "SELECT pairing.additionalDish.idOption " +
                            "FROM MainDishPairing pairing " +
                            "WHERE pairing.mainDish = :mainDish", Integer.class);
            query.setParameter("mainDish", mainDish);
            return query.uniqueResult();
        } catch (NoResultException e) {
            log.warn("No additional dish found for main dish: {}", mainDish.getName(), e);
            return null;
        } catch (HibernateException e) {
            log.error("An error occurred while finding additional dish ID by main dish: {}", mainDish.getName(), e);
            return null;
        }
    }


}
