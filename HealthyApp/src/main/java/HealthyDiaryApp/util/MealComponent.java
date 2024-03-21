package HealthyDiaryApp.util;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import HealthyDiaryApp.entity.MealOption;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
/*
 * MealComponent class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description: Цей клас містить методи для пошуку айдішок їжі за назвою.
 */
@Slf4j
public class MealComponent {
    //Метод для пошуку айди їжі по назві
    public Integer findMealIdByName(String mealName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Integer> criteriaQuery = criteriaBuilder.createQuery(Integer.class);
            Root<MealOption> root = criteriaQuery.from(MealOption.class);

            criteriaQuery.select(root.get("idOption"))
                    .where(criteriaBuilder.equal(root.get("name"), mealName));

            return session.createQuery(criteriaQuery).uniqueResult();
        } catch (NoResultException e) {
            log.warn("No meal found with name: {}", mealName, e);
            return null;
        } catch (HibernateException e) {
            log.error("An error occurred while finding meal ID by name: {}",
                    mealName, e);
            return null;
        }
    }

}
