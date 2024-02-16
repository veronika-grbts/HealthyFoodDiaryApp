package util;

import entity.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


@Slf4j
public class HibernateMethods {

    public void addUserAllergy(long phoneNumber, String typeIngredients) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                User user = session.get(User.class, phoneNumber);
                if (user != null) {
                    boolean hasAllergy = user.isAllergies();
                    if (hasAllergy) {
                        UserAllergy userAllergy = UserAllergy.builder()
                                .user(user)
                                .typeIngredients(typeIngredients)
                                .build();

                        session.save(userAllergy);
                        session.getTransaction().commit();

                        log.info("Allergy successfully added for user with phone number {}", phoneNumber);
                    } else {
                        log.warn("User with phone number {} has no allergies", phoneNumber);
                    }
                } else {
                    log.warn("User with phone number {} not found", phoneNumber);
                }
            }
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
}


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
