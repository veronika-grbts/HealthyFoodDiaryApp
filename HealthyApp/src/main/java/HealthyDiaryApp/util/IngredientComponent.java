package HealthyDiaryApp.util;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import HealthyDiaryApp.entity.*;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;
/*
 * IngredientComponent class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description: Цей клас містить методи для пошуку інгредієнтів за назвою, отримання всіх інгредієнтів
 * з таблиці Ingredients та отримання інгредієнтів для блюда чи напою.
 */
@Slf4j
public class IngredientComponent {
    //Пошук інгредієнтів на ім'я
    public Ingredients findIngredientByName(String ingredientName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Ingredients> criteriaQuery = builder.createQuery(Ingredients.class);
            Root<Ingredients> root = criteriaQuery.from(Ingredients.class);
            criteriaQuery.select(root)
                    .where(builder.equal(root.get("nameIngredients"), ingredientName));

            Query<Ingredients> query = session.createQuery(criteriaQuery);
            Ingredients ingredient = query.uniqueResult();

            session.getTransaction().commit();
            return ingredient;
        } catch (NoResultException e) {
            log.warn("No ingredient found with name: {}", ingredientName, e);
            return null;
        } catch (HibernateException e) {
            log.error("An error occurred while finding ingredient by name: {}",
                    ingredientName, e);
            return null;
        }
    }

    //Метод для отримання всіх елементів з таблиці Ingredients
    public List<Ingredients> getAllIngredients() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Query<Ingredients> query = session.createQuery(
                    "FROM Ingredients", Ingredients.class);

            List<Ingredients> ingredientsList = query.list();
            session.getTransaction().commit();
            return ingredientsList;
        } catch (HibernateException e) {
            log.error("An error occurred while getting all ingredients", e);
            return null;
        }
    }

    // Метод для отримання інгрідієнтів для блюда
    public List<Ingredients> getIngredientsByMealOption(MealOption mealOption) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Ingredients> criteriaQuery = builder.createQuery(Ingredients.class);
            Root<MealIngredients> root = criteriaQuery.from(MealIngredients.class);

            Join<MealIngredients, Ingredients> joinIngredients = root.join("ingredients");
            criteriaQuery.select(joinIngredients)
                    .where(builder.equal(root.get("mealOption"), mealOption));

            Query<Ingredients> query = session.createQuery(criteriaQuery);
            List<Ingredients> ingredientsList = query.list();

            session.getTransaction().commit();
            return ingredientsList;
        } catch (HibernateException e) {
            log.error("An error occurred while getting ingredients for meal option: {}",
                    mealOption, e);
            return null;
        }
    }

    // Метод для отримання інгрідієнтів напою
    public List<Ingredients> getIngredientsByDrink(Drink drink) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Ingredients> criteriaQuery = builder.createQuery(Ingredients.class);
            Root<MealIngredientsDrink> root = criteriaQuery.from(MealIngredientsDrink.class);

            Join<MealIngredientsDrink, Ingredients> joinIngredients = root.join("ingredients");
            criteriaQuery.select(joinIngredients).where(builder.equal(root.get("drink"), drink));

            Query<Ingredients> query = session.createQuery(criteriaQuery);
            List<Ingredients> ingredientsList = query.list();

            session.getTransaction().commit();
            return ingredientsList;
        } catch (HibernateException e) {
            log.error("An error occurred while getting ingredients for drink: {}", drink, e);
            return null;
        }
    }


}
