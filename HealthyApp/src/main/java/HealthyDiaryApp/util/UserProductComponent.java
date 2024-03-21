package HealthyDiaryApp.util;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import HealthyDiaryApp.entity.UserSelectedProduct;

import javax.persistence.criteria.*;
import java.util.List;
/*
 * UserProductComponent class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description: Цей клас містить методи для отримання продуктів конкретного користувача,
 * видалення всіх продуктів для користувача, а також отримання інформації про
 * калорійність, жири, білки та вуглеводи останнього продукту для користувача.
 * Використовується для роботи з продуктами, обраними користувачем.
 */

@Slf4j
public class UserProductComponent {

    //Метод для отримання продуктів конкретного user
    public List<UserSelectedProduct> getUserSelectedProductsForNumberPhone(long phoneNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<UserSelectedProduct> criteriaQuery = criteriaBuilder.createQuery(
                    UserSelectedProduct.class
            );
            Root<UserSelectedProduct> root = criteriaQuery.from(UserSelectedProduct.class);
            Predicate predicate = criteriaBuilder.equal(
                    root.get("user").get("phoneNumber"),
                    phoneNumber
            );
            criteriaQuery.where(predicate);
            List<UserSelectedProduct> userSelectedProducts = session.createQuery(
                    criteriaQuery).getResultList();

            session.getTransaction().commit();
            return userSelectedProducts;
        } catch (HibernateException e) {
            log.error("An error occurred while getting user selected products for user with phone number {}",
                    phoneNumber, e);
            return null;
        }
    }
    //метод для видалення всіх продуктів у одного користувача
    public void DropProductUser(long phoneNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

            CriteriaDelete<UserSelectedProduct> criteriaDelete = criteriaBuilder
                    .createCriteriaDelete(UserSelectedProduct.class);

            Root<UserSelectedProduct> root = criteriaDelete
                    .from(UserSelectedProduct.class);

            criteriaDelete.where(criteriaBuilder.equal(root.get("user")
                    .get("phoneNumber"), phoneNumber));
            session.createQuery(criteriaDelete).executeUpdate();

            session.getTransaction().commit();
        }catch (HibernateException e) {
            log.error("An error occurred while dropping products for user with phone number {}",
                    phoneNumber, e);
        }
    }


    //Повертаемо калорійність останнього продукту для user
    public double getTotalCaloriesForUser(long phoneNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Double> criteriaQuery = criteriaBuilder.createQuery(Double.class);
            Root<UserSelectedProduct> root = criteriaQuery.from(UserSelectedProduct.class);

            Predicate predicate = criteriaBuilder.equal(root.get("user")
                    .get("phoneNumber"), phoneNumber);

            //Сортуємо результати по спаданню та вибираємо тільки перший елемент
            criteriaQuery.select(root.get("caloriesUserSelectedProduct")).where(predicate)
                    .orderBy(criteriaBuilder.desc(root.get("idUserSelectedProduct")));
            Query<Double> query = session.createQuery(criteriaQuery).setMaxResults(1);
            Double totalCalories = query.uniqueResult();

            if (totalCalories != null) {
                return totalCalories;
            }else {
                return 0;
            }
        } catch (HibernateException e) {
            log.error("An error occurred while getting total calories for user with phone number {}",
                    phoneNumber, e);
            return 0;
        }
    }



    //Повертаемо кількість жирів останнього продукту для user
    public double getTotalFatForUser(long phoneNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Double> criteriaQuery = criteriaBuilder.createQuery(Double.class);
            Root<UserSelectedProduct> root = criteriaQuery.from(UserSelectedProduct.class);

            Predicate predicate = criteriaBuilder.equal(root.get("user")
                    .get("phoneNumber"), phoneNumber);

            criteriaQuery.select(root.get("fatUserSelectedProduct")).where(predicate)
                    .orderBy(criteriaBuilder.desc(root.get("idUserSelectedProduct")));
            Query<Double> query = session.createQuery(criteriaQuery).setMaxResults(1);
            Double totalFat = query.uniqueResult();

            if (totalFat != null) {
                return totalFat;
            } else {
                return 0;
            }
        } catch (HibernateException e) {
            log.error("An error occurred while getting total fat for user with phone number {}",
                    phoneNumber, e);
            return 0;
        }
    }

    //Повертаемо кількість білків останнього продукту для user
    public double getTotalProteinForUser(long phoneNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Double> criteriaQuery = criteriaBuilder.createQuery(Double.class);
            Root<UserSelectedProduct> root = criteriaQuery.from(UserSelectedProduct.class);

            Predicate predicate = criteriaBuilder.equal(root.get("user")
                    .get("phoneNumber"), phoneNumber);

            criteriaQuery.select(root.get("proteinUserSelectedProduct")).where(predicate)
                    .orderBy(criteriaBuilder.desc(root.get("idUserSelectedProduct")));
            Query<Double> query = session.createQuery(criteriaQuery).setMaxResults(1);

            Double totalProtein = query.uniqueResult();

            if (totalProtein != null) {
                return totalProtein;
            } else {
                return 0;
            }
        } catch (HibernateException e) {
            log.error("An error occurred while getting total protein for user with phone number {}",
                    phoneNumber, e);
            return 0;
        }
    }

    //Повертаемо кількість вуглеводів останнього продукту для user
    public double getTotalCarbsForUser(long phoneNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Double> criteriaQuery = criteriaBuilder.createQuery(Double.class);
            Root<UserSelectedProduct> root = criteriaQuery.from(UserSelectedProduct.class);

            Predicate predicate = criteriaBuilder.equal(root.get("user")
                    .get("phoneNumber"), phoneNumber);

            criteriaQuery.select(root.get("carbsUserSelectedProduct")).where(predicate)
                    .orderBy(criteriaBuilder.desc(root.get("idUserSelectedProduct")));
            Query<Double> query = session.createQuery(criteriaQuery).setMaxResults(1);

            Double totalCarbs = query.uniqueResult();

            if (totalCarbs != null) {
                return totalCarbs;
            } else {
                return 0;
            }
        } catch (HibernateException e) {
            log.error("An error occurred while getting total carbs for user with phone number {}",
                    phoneNumber, e);
            return 0;
        }
    }


}
