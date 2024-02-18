package util;

import entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;


@Slf4j
public class HibernateMethods {

    public void addUserAllergyForUser(long phoneNumber, int ingredientId) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                // Получаем пользователя по номеру телефона
                User user = session.get(User.class, phoneNumber);

                // Получаем ингредиент по его id
                Ingredients ingredient = session.get(Ingredients.class, ingredientId);

                // Проверяем, что пользователь и ингредиент существуют
                if (user != null && ingredient != null) {
                    // Создаем новую запись в таблице UserAllergy
                    UserAllergy userAllergy = UserAllergy.builder()
                            .user(user)
                            .ingredients(ingredient)
                            .build();

                    // Сохраняем запись в базе данных
                    session.save(userAllergy);
                    session.getTransaction().commit();

                    log.info("Allergy successfully added for user with phone number {}", phoneNumber);
                } else {
                    log.warn("User with phone number {} or ingredient with ID {} not found", phoneNumber, ingredientId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public Ingredients findIngredientByName(String ingredientName) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                Query<Ingredients> query = session.createQuery("FROM Ingredients WHERE nameIngredients = :name", Ingredients.class);
                query.setParameter("name", ingredientName);
                Ingredients ingredient = query.uniqueResult();
                session.getTransaction().commit();
                return ingredient;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addUserSelectedProduct(long phoneNumber, String name, double grams) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                User user = session.get(User.class, phoneNumber);

                Query<Products> query = session.createQuery("FROM Products WHERE name = :name", Products.class);
                query.setParameter("name", name);
                Products products = query.uniqueResult();

                if (user != null && products != null) {
                    UserSelectedProduct userSelectedProduct = UserSelectedProduct.builder()
                            .user(user)
                            .products(products)
                            .grams(grams)
                            .build();

                    session.save(userSelectedProduct);
                    session.getTransaction().commit();

                    log.info("Product successfully added for user with phone number {}", phoneNumber);
                } else {
                    log.warn("User with phone number {} or product with name {} not found", phoneNumber, name);
                }
            }

        }
    }

    public void createUser(long phoneNumber, String name, int age, double weight, double height, boolean gender, ActivityLevel activityLevel, boolean allergies, boolean cause, double totalCaloric, double totalProtein, double totalFat, double totalCarbs) {

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                User newUser = User.builder()
                        .phoneNumber(phoneNumber)
                        .name(name)
                        .age(age)
                        .weight(weight)
                        .height(height)
                        .gender(gender)
                        .activityLevel(activityLevel)
                        .allergies(allergies)
                        .cause(cause)
                        .totalCaloric(totalCaloric)
                        .totalProtein(totalProtein)
                        .totalFat(totalFat)
                        .totalCarbs(totalCarbs)
                        .build();

                session.save(newUser);
                session.getTransaction().commit();

                log.info("User successfully created with phone number {}", phoneNumber);

            }
        }
    }

    public User getUserInfo(long phoneNumber) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
                Root<User> root = criteriaQuery.from(User.class);
                criteriaQuery.select(root).where(builder.equal(root.get("phoneNumber"), phoneNumber));
                User user = session.createQuery(criteriaQuery).uniqueResult();

                if (user != null) {
                    log.info("User information with phone number {}: {}", phoneNumber, user);
                    return user;
                } else {
                    log.warn("User with phone number {} not found", phoneNumber);
                    return null;
                }
            }
        }
    }

    public boolean isUserExists(long phoneNumber) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                User user = session.get(User.class, phoneNumber);
                session.getTransaction().commit();
                return user != null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Ingredients> getAllIngredients() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                Query<Ingredients> query = session.createQuery("FROM Ingredients", Ingredients.class);
                List<Ingredients> ingredientsList = query.list();
                session.getTransaction().commit();
                return ingredientsList;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
    /*public void fillComboBoxWithIngredients(ComboBox<String> comboBox) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                // Создаем запрос к базе данных, чтобы получить все значения столбца nameIngredients
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<String> criteriaQuery = builder.createQuery(String.class);
                Root<Ingredients> root = criteriaQuery.from(Ingredients.class);
                criteriaQuery.select(root.get("nameIngredients"));
                List<String> names = session.createQuery(criteriaQuery).getResultList();

                // Закрываем транзакцию
                session.getTransaction().commit();

                // Добавляем полученные значения в ComboBox
                ObservableList<String> items = FXCollections.observableArrayList(names);
                comboBox.setItems(items);
            }
        }
    }
}*/

   /* public void createUserSelectedMenu(long phoneNumber){
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
                Root<User> root = criteriaQuery.from(User.class);
                criteriaQuery.select(root).where(builder.equal(root.get("phoneNumber"), phoneNumber));
                User user = session.createQuery(criteriaQuery).uniqueResult();

                if (user != null) {
                    // Создаем подзапросы для завтрака, обеда и ужина
                    CriteriaQuery<MealOption> breakfastQuery = builder.createQuery(MealOption.class);
                    Root<MealOption> breakfastRoot = breakfastQuery.from(MealOption.class);
                    breakfastQuery.select(breakfastRoot).where(builder.equal(breakfastRoot.get("typeMeal"), "Breakfast"))
                            .orderBy(builder.asc(breakfastRoot.get("id")));
                    MealOption breakfastOption = session.createQuery(breakfastQuery).setMaxResults(1).uniqueResult();

                    CriteriaQuery<MealOption> lunchQuery = builder.createQuery(MealOption.class);
                    Root<MealOption> lunchRoot = lunchQuery.from(MealOption.class);
                    lunchQuery.select(lunchRoot).where(builder.equal(lunchRoot.get("typeMeal"), "Lunch"))
                            .orderBy(builder.asc(lunchRoot.get("id")));
                    MealOption lunchOption = session.createQuery(lunchQuery).setMaxResults(1).uniqueResult();

                    CriteriaQuery<MealOption> dinnerQuery = builder.createQuery(MealOption.class);
                    Root<MealOption> dinnerRoot = dinnerQuery.from(MealOption.class);
                    dinnerQuery.select(dinnerRoot).where(builder.equal(dinnerRoot.get("typeMeal"), "Dinner"))
                            .orderBy(builder.asc(dinnerRoot.get("id")));
                    MealOption dinnerOption = session.createQuery(dinnerQuery).setMaxResults(1).uniqueResult();

                    // Создаем меню с учетом аллергий пользователя
                    UserSelectedMenu userSelectedMenu = UserSelectedMenu.builder()
                            .user(user)
                            .breakfastId(breakfastOption.getId())
                            .breakfastDrinkId(selectRandomDrinkId(session))
                            .lunchId(lunchOption.getId())
                            .lunchDrinkId(selectRandomDrinkId(session))
                            .dinnerId(dinnerOption.getId())
                            .dinnerDrinkId(selectRandomDrinkId(session))
                            .build();

                    session.save(userSelectedMenu);
                    session.getTransaction().commit();

                    log.info("Меню успешно создано для пользователя с номером телефона {}", phoneNumber);
                } else {
                    log.warn("Пользователь с номером телефона {} не найден", phoneNumber);
                }
            }
        }
    }*/
