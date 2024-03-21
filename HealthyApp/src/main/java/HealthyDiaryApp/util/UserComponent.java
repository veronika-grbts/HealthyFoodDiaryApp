package HealthyDiaryApp.util;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import HealthyDiaryApp.entity.*;
import HealthyDiaryApp.enums.ActivityLevel;

import javax.persistence.criteria.*;
import java.util.List;
/*
 * UserComponent class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description: Цей клас містить методи для додавання алергій, продуктів та створення,
 * оновлення та видалення користувачів. Також він надає методи для отримання
 * інформації про користувача, його алергії, останнього меню та оновлення даних
 * користувача за номером телефону.
 */

@Slf4j
public class UserComponent {
    public void addUserAllergyForUser(long phoneNumber, int ingredientId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            User user = session.get(User.class, phoneNumber);
            Ingredients ingredient = session.get(Ingredients.class, ingredientId);

            if (user != null && ingredient != null) {
                UserAllergy userAllergy = UserAllergy.builder()
                        .user(user)
                        .ingredients(ingredient)
                        .build();

                session.save(userAllergy);
                transaction.commit();

                log.info("Allergy successfully added for user with phone number {}", phoneNumber);
            } else {
                log.warn("User with phone number {} or ingredient with ID {} not found",
                        phoneNumber, ingredientId);
            }
        }catch (Exception e) {
            log.error("Error while adding allergy for user with phone number {}",
                    phoneNumber, e);
        }
    }

    //Додавання продукту в таблицю UserSelectedProduct
    public void addUserSelectedProduct(long phoneNumber, int productId,
                                       double grams, double remainingCalories,
                                       double remainingFat, double remainingProtein,
                                       double remainingCarbs) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = session.get(User.class, phoneNumber);
            Products products = session.get(Products.class, productId);

            if ((user != null) && (products != null)) {
                UserSelectedProduct userSelectedProduct = UserSelectedProduct.builder()
                        .user(user)
                        .products(products)
                        .gramsUserSelectedProduct(grams)
                        .fatUserSelectedProduct(remainingFat)
                        .proteinUserSelectedProduct(remainingProtein)
                        .carbsUserSelectedProduct(remainingCarbs)
                        .caloriesUserSelectedProduct(remainingCalories)
                        .build();
                session.save(userSelectedProduct);
                transaction.commit();

                log.info("Product successfully added for user with phone number {}",
                        phoneNumber);
            } else {
                log.warn("User with phone number {} or product with id {} not found",
                        phoneNumber, productId);
            }
        } catch (HibernateException e) {
            log.error("An error occurred while adding product for user with phone number {}",
                    phoneNumber, e);
        }
    }


    //Метод для пошуку  інформації о user
    public User getUserInfo(long phoneNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);

            Predicate phonePredicate = builder.equal(
                    root.get("phoneNumber"), phoneNumber);

            Predicate finalPredicate = builder.or(phonePredicate);
            criteriaQuery.select(root).where(finalPredicate);

            Query<User> query = session.createQuery(criteriaQuery);
            User user = query.uniqueResult();

            if (user != null) {
                log.info("User information found: {}", user);
                return user;
            } else {
                log.warn("User not found with provided phone number {} or name {}",
                        phoneNumber);
                return null;
            }
        } catch (HibernateException e) {
            log.error("An error occurred while getting user information", e);
            return null;
        }
    }

    //Створення нового user
    public void createUser(long phoneNumber, String name, int age, double weight,
                           double height, boolean gender, ActivityLevel activityLevel,
                           boolean allergies, boolean cause, double totalCaloric,
                           double totalProtein, double totalFat, double totalCarbs) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            User newUser = User.builder()
                    .phoneNumber(phoneNumber)
                    .nameUser(name)
                    .ageUser(age)
                    .weightUser(weight)
                    .heightUser(height)
                    .genderUser(gender)
                    .activityLevel(activityLevel)
                    .allergiesUser(allergies)
                    .causeUser(cause)
                    .totalCaloricUser(totalCaloric)
                    .totalProteinUser(totalProtein)
                    .totalFatUser(totalFat)
                    .totalCarbsUser(totalCarbs)
                    .build();

            session.save(newUser);
            transaction.commit();

            log.info("User successfully created with phone number {}",
                    phoneNumber);

        } catch (HibernateException e) {
            log.error("An error occurred while creating user with phone number {}",
                    phoneNumber, e);
        }
    }

    // Метод для отримання алергій користувача
    public List<UserAllergy> getUserAllergies(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<UserAllergy> criteriaQuery = builder
                    .createQuery(UserAllergy.class);
            Root<UserAllergy> root = criteriaQuery.from(UserAllergy.class);
            criteriaQuery.select(root).where(builder.equal(root.get("user"),
                    user));

            Query<UserAllergy> query = session.createQuery(criteriaQuery);
            List<UserAllergy> userAllergies = query.list();

            session.getTransaction().commit();
            return userAllergies;
        }  catch (HibernateException e) {
            log.error("An error occurred while getting user allergies for user {}",
                    user, e);
            return null;
        }
    }


    //Метод для отримання останього меню користувача
    public UserSelectedMenu getLastUserMenu(Long phoneNumber) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        UserSelectedMenu lastMenu = null;
        try {
            tx = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<UserSelectedMenu> criteria = builder.createQuery(UserSelectedMenu.class);
            Root<UserSelectedMenu> root = criteria.from(UserSelectedMenu.class);
            criteria.select(root);
            criteria.where(builder.equal(root.get("user")
                    .get("phoneNumber"), phoneNumber));
            criteria.orderBy(builder.desc(root.get("idSelectedMenu")));
            Query<UserSelectedMenu> query = session.createQuery(criteria);
            query.setMaxResults(1);
            lastMenu = query.uniqueResult();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("An error occurred while getting last user menu for phone number {}",
                    phoneNumber, e);
        } finally {
            session.close();
        }
        return lastMenu;
    }

    public void updateUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(user); // Обновляем пользователя в сессии

            transaction.commit(); // Фиксируем транзакцию
            log.info("User with phone number {} was cusses update", user.getPhoneNumber());
        } catch (HibernateException e) {
            // В случае ошибки выводим сообщение об ошибке и возвращаем false
            log.error("An error occurred while updating user information", e);
        }
    }


    //метод для получения листа с алергиями пользывателя
    public List<String> getUserAllergiesText(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            String hql = "SELECT i.nameIngredients FROM UserAllergy ua JOIN ua.ingredients i WHERE ua.user = :user";
            Query<String> query = session.createQuery(hql, String.class);
            query.setParameter("user", user);
            return query.list();
        }  catch (HibernateException e) {
            log.error("An error occurred while getting user allergies for user {}", user, e);
            return null;
        }
    }

    //метод для удаления алергии у пользователя
    public void deleteUserAllergy(User user, String allergyName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            // Получаем айди ингредиента по его имени
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Integer> criteriaQuery = builder.createQuery(Integer.class);
            Root<Ingredients> root = criteriaQuery.from(Ingredients.class);

            criteriaQuery.select(root.get("idIngredients"))
                    .where(builder.equal(root.get("nameIngredients"), allergyName));
            Query<Integer> query = session.createQuery(criteriaQuery);
            Integer ingredientId = query.uniqueResult();

            // Удаляем запись из UserAllergy
            if (ingredientId != null) {
                CriteriaDelete<UserAllergy> criteriaDelete = builder.createCriteriaDelete(UserAllergy.class);
                Root<UserAllergy> userAllergyRoot = criteriaDelete.from(UserAllergy.class);

                Predicate userPredicate = builder.equal(userAllergyRoot.get("user"), user);
                Predicate ingredientPredicate = builder
                        .equal(userAllergyRoot.get("ingredients")
                                .get("idIngredients"), ingredientId);

                criteriaDelete.where(builder.and(userPredicate, ingredientPredicate));

                int deletedCount = session.createQuery(criteriaDelete).executeUpdate();
                session.getTransaction().commit();

                if (deletedCount > 0) {
                    log.info("Successfully deleted allergy: {}", allergyName);
                } else {
                    log.warn("Allergy not found or not deleted: {}", allergyName);
                }
            } else {
                log.warn("Ingredient not found with name: {}", allergyName);
            }
        } catch (HibernateException e) {
            log.error("An error occurred while deleting allergy", e);
        }
    }


    //метод для удаления юзера и его связи
    public void deleteUserAndRelatedSelectedMenus(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();

            // Удаляем связанные выбранные меню пользователя
            CriteriaDelete<UserSelectedMenu> deleteMenuCriteria = builder
                    .createCriteriaDelete(UserSelectedMenu.class);
            Root<UserSelectedMenu> rootMenu = deleteMenuCriteria.from(UserSelectedMenu.class);
            deleteMenuCriteria.where(builder.equal(rootMenu.get("user"), user));
            int menuRowsAffected = session.createQuery(deleteMenuCriteria).executeUpdate();

            // Удаляем самого пользователя
            session.delete(user);
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    public void updateUserCause(long phoneNumber, boolean newCauseValue) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("phoneNumber", phoneNumber));

            User user = (User) criteria.uniqueResult();

            if (user != null) {
                user.setCauseUser(newCauseValue);
                session.update(user);
                transaction.commit();
                System.out.println("User cause updated successfully.");
            } else {
                log.info("User not found with phone number: {}", phoneNumber);
            }
        } catch (Exception e) {
            log.error("An error occurred while updating user cause: {}" , e.getMessage());
        }
    }

    public  void updateUserDataByPhoneNumber(Long phoneNumber, double totalCaloricUser,
                                             double totalProteinUser,
                                             double totalFatUser, double totalCarbsUser) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<User> criteriaUpdate = builder.createCriteriaUpdate(User.class);
            Root<User> root = criteriaUpdate.from(User.class);

            criteriaUpdate.set(root.get("totalCaloricUser"), totalCaloricUser);
            criteriaUpdate.set(root.get("totalProteinUser"), totalProteinUser);
            criteriaUpdate.set(root.get("totalFatUser"), totalFatUser);
            criteriaUpdate.set(root.get("totalCarbsUser"), totalCarbsUser);

            criteriaUpdate.where(builder.equal(root.get("phoneNumber"), phoneNumber));

            int updatedEntities = session.createQuery(criteriaUpdate).executeUpdate();
            transaction.commit();

            if(updatedEntities == 0) {
                log.warn("User was not find with phone number {}", phoneNumber);
            }
        }catch (Exception e) {
            log.error("An error occurred while updating user fat, protein, carbs : {}", e.getMessage());
        }
    }

    public User getUserByPhoneNumber(long phoneNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);

            Predicate predicate = criteriaBuilder
                    .equal(root.get("phoneNumber"), phoneNumber);

            criteriaQuery.select(root).where(predicate);
            Query<User> query = session.createQuery(criteriaQuery);

            return query.uniqueResult();
        } catch (HibernateException e) {
            log.error("An error occurred while getting user by phone number {}",
                    phoneNumber, e);
            return null;
        }
    }



}
