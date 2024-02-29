package project.util;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import project.entity.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class HibernateMethods{

    //Додавання алергії до user
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
                log.warn("User with phone number {} or ingredient with ID {} not found", phoneNumber, ingredientId);
            }
        } catch (Exception e) {
            log.error("Error while adding allergy for user with phone number {}", phoneNumber, e);
        }
    }

    //Пошук інгредієнтів на ім'я
    public Ingredients findIngredientByName(String ingredientName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Query<Ingredients> query = session.createQuery("FROM Ingredients WHERE nameIngredients = :name", Ingredients.class);
            query.setParameter("name", ingredientName);
            Ingredients ingredient = query.uniqueResult();
            session.getTransaction().commit();
            return ingredient;
        }catch (NoResultException e) {
            log.warn("No ingredient found with name: {}", ingredientName, e);
            return null;
        }  catch (HibernateException e) {
            log.error("An error occurred while finding ingredient by name: {}", ingredientName, e);
            return null;
        }
    }

    //Пошук продукту на ім'я
    public Products findProductByName(String productName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Query<Products> query = session.createQuery("FROM Products WHERE nameProduct = :name", Products.class);
            query.setParameter("name", productName);
            Products products = query.uniqueResult();
            session.getTransaction().commit();
            return products;
        }catch (NoResultException e) {
            log.warn("No product found with name: {}", productName, e);
            return null;
        } catch (HibernateException e) {
            log.error("An error occurred while finding product by name: {}", productName, e);
            return null;
        }
    }

    //Додавання продукту в таблицю UserSelectedProduct
    public void addUserSelectedProduct(long phoneNumber, int productId, double grams,
                                       double remainingCalories, double remainingFat,
                                       double remainingProtein, double remainingCarbs) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            User user = session.get(User.class, phoneNumber);
            Products products = session.get(Products.class, productId);

            if (user != null && products != null) {
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

                log.info("Product successfully added for user with phone number {}", phoneNumber);
            } else {
                log.warn("User with phone number {} or product with id {} not found", phoneNumber, productId);
            }
        } catch (HibernateException e) {
            log.error("An error occurred while adding product for user with phone number {}", phoneNumber, e);
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

            log.info("User successfully created with phone number {}", phoneNumber);

        } catch (HibernateException e) {
            log.error("An error occurred while creating user with phone number {}", phoneNumber, e);
        }
    }

    //Метод для пошуку  інформації о user
    public User getUserInfo(long phoneNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);

            Predicate phonePredicate = builder.equal(root.get("phoneNumber"), phoneNumber);
          //  Predicate namePredicate = builder.equal(root.get("nameUser"), name);
            Predicate finalPredicate = builder.or(phonePredicate);

            criteriaQuery.select(root).where(finalPredicate);

            Query<User> query = session.createQuery(criteriaQuery);
            User user = query.uniqueResult();

            if (user != null) {
                log.info("User information found: {}", user);
                return user;
            } else {
                log.warn("User not found with provided phone number {} or name {}", phoneNumber);
                return null;
            }
        } catch (HibernateException e) {
            log.error("An error occurred while getting user information", e);
            return null;
        }
    }

    //Метод для отримання всіх елементів з таблиці Ingredients
    public List<Ingredients> getAllIngredients() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Query<Ingredients> query = session.createQuery("FROM Ingredients", Ingredients.class);
            List<Ingredients> ingredientsList = query.list();
            session.getTransaction().commit();
            return ingredientsList;
        } catch (HibernateException e) {
            log.error("An error occurred while getting all ingredients", e);
            return null;
        }
    }

    //Метод для отримання всіх елементів з таблиці Products
    public List<Products> getAllProduct() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Query<Products> query = session.createQuery("FROM Products", Products.class);
            List<Products> productList = query.list();
            session.getTransaction().commit();
            return productList;
        } catch (HibernateException e) {
            log.error("An error occurred while getting all products", e);
            return null;
        }
    }

    public List<Products> getUserSelectedProduct(long phoneNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Query<Products> query = session.createQuery("FROM Products ", Products.class);
            List<Products> productList = query.list();
            session.getTransaction().commit();
            return productList;
        } catch (HibernateException e) {
            log.error("An error occurred while getting user selected products for user with phone number {}", phoneNumber, e);
            return null;
        }
    }

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

    //Метод для отримання продуктів конкретного user
    public List<UserSelectedProduct> getUserSelectedProductsForNumberPhone(long phoneNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<UserSelectedProduct> criteriaQuery = criteriaBuilder.createQuery(UserSelectedProduct.class);
            Root<UserSelectedProduct> root = criteriaQuery.from(UserSelectedProduct.class);

            Predicate predicate = criteriaBuilder.equal(root.get("user").get("phoneNumber"), phoneNumber);
            criteriaQuery.where(predicate);

            List<UserSelectedProduct> userSelectedProducts = session.createQuery(criteriaQuery).getResultList();
            session.getTransaction().commit();
            return userSelectedProducts;
        } catch (HibernateException e) {
            log.error("An error occurred while getting user selected products for user with phone number {}", phoneNumber, e);
            return null;
        }
    }

    //метод для видалення всіх продуктів у одного користувача
    public void DropProductUser(long phoneNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

            CriteriaDelete<UserSelectedProduct> criteriaDelete = criteriaBuilder.createCriteriaDelete(UserSelectedProduct.class);
            Root<UserSelectedProduct> root = criteriaDelete.from(UserSelectedProduct.class);

            criteriaDelete.where(criteriaBuilder.equal(root.get("user").get("phoneNumber"), phoneNumber));
            session.createQuery(criteriaDelete).executeUpdate();

            session.getTransaction().commit();
        }catch (HibernateException e) {
            log.error("An error occurred while dropping products for user with phone number {}", phoneNumber, e);
        }
    }

    //Повертаемо калорійність останнього продукту для user
    public double getTotalCaloriesForUser(long phoneNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Double> criteriaQuery = criteriaBuilder.createQuery(Double.class);
            Root<UserSelectedProduct> root = criteriaQuery.from(UserSelectedProduct.class);

            Predicate predicate = criteriaBuilder.equal(root.get("user").get("phoneNumber"), phoneNumber);

            // Сортируем результаты по убыванию и выбираем только первый элемент
            criteriaQuery.select(root.get("caloriesUserSelectedProduct")).where(predicate)
                    .orderBy(criteriaBuilder.desc(root.get("idUserSelectedProduct")));
            Query<Double> query = session.createQuery(criteriaQuery).setMaxResults(1);

            Double totalCalories = query.uniqueResult();

            // Если для пользователя нет продуктов в таблице UserSelectedProduct, вернуть 0
            if (totalCalories != null) {
                return totalCalories;
            } else {
                return 0;
            }
        } catch (HibernateException e) {
            log.error("An error occurred while getting total calories for user with phone number {}", phoneNumber, e);
            return 0;
        }
    }

    //Повертаемо кількість жирів останнього продукту для user
    public double getTotalFatForUser(long phoneNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Double> criteriaQuery = criteriaBuilder.createQuery(Double.class);
            Root<UserSelectedProduct> root = criteriaQuery.from(UserSelectedProduct.class);

            // Добавляем условие для выборки продуктов конкретного пользователя
            Predicate predicate = criteriaBuilder.equal(root.get("user").get("phoneNumber"), phoneNumber);

            // Сортируем результаты по убыванию и выбираем только первый элемент
            criteriaQuery.select(root.get("fatUserSelectedProduct")).where(predicate)
                    .orderBy(criteriaBuilder.desc(root.get("idUserSelectedProduct")));
            Query<Double> query = session.createQuery(criteriaQuery).setMaxResults(1);

            Double totalFat = query.uniqueResult();

            // Если для пользователя нет продуктов в таблице UserSelectedProduct, вернуть 0
            if (totalFat != null) {
                return totalFat;
            } else {
                return 0;
            }
        } catch (HibernateException e) {
            log.error("An error occurred while getting total fat for user with phone number {}", phoneNumber, e);
            return 0;
        }
    }

    //Повертаемо кількість білків останнього продукту для user
    public double getTotalProteinForUser(long phoneNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Double> criteriaQuery = criteriaBuilder.createQuery(Double.class);
            Root<UserSelectedProduct> root = criteriaQuery.from(UserSelectedProduct.class);

            // Добавляем условие для выборки продуктов конкретного пользователя
            Predicate predicate = criteriaBuilder.equal(root.get("user").get("phoneNumber"), phoneNumber);

            // Сортируем результаты по убыванию и выбираем только первый элемент
            criteriaQuery.select(root.get("proteinUserSelectedProduct")).where(predicate)
                    .orderBy(criteriaBuilder.desc(root.get("idUserSelectedProduct")));
            Query<Double> query = session.createQuery(criteriaQuery).setMaxResults(1);

            Double totalProtein = query.uniqueResult();

            // Если для пользователя нет продуктов в таблице UserSelectedProduct, вернуть 0
            if (totalProtein != null) {
                return totalProtein;
            } else {
                return 0;
            }
        } catch (HibernateException e) {
            log.error("An error occurred while getting total protein for user with phone number {}", phoneNumber, e);
            return 0;
        }
    }

    //Повертаемо кількість вуглеводів останнього продукту для user
    public double getTotalCarbsForUser(long phoneNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Double> criteriaQuery = criteriaBuilder.createQuery(Double.class);
            Root<UserSelectedProduct> root = criteriaQuery.from(UserSelectedProduct.class);

            // Добавляем условие для выборки продуктов конкретного пользователя
            Predicate predicate = criteriaBuilder.equal(root.get("user").get("phoneNumber"), phoneNumber);

            // Сортируем результаты по убыванию и выбираем только первый элемент
            criteriaQuery.select(root.get("carbsUserSelectedProduct")).where(predicate)
                    .orderBy(criteriaBuilder.desc(root.get("idUserSelectedProduct")));
            Query<Double> query = session.createQuery(criteriaQuery).setMaxResults(1);

            Double totalCarbs = query.uniqueResult();

            // Если для пользователя нет продуктов в таблице UserSelectedProduct, вернуть 0
            if (totalCarbs != null) {
                return totalCarbs;
            } else {
                return 0;
            }
        } catch (HibernateException e) {
            log.error("An error occurred while getting total carbs for user with phone number {}", phoneNumber, e);
            return 0;
        }
    }

    //Метод для створення ногово продукту
    public void addNewProducts(String nameProduct, double calorieProduct,
                               double fatProduct, double proteinProduct,
                               double carbsProduct) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            // Проверяем существующие продукты с таким же именем
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Products> criteriaQuery = criteriaBuilder.createQuery(Products.class);
            Root<Products> root = criteriaQuery.from(Products.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("nameProduct"), nameProduct));
            Query<Products> query = session.createQuery(criteriaQuery);
            List<Products> existingProducts = query.getResultList();

            if (existingProducts.isEmpty()) {
                // Создаем новый продукт без указания product_id
                Products product = Products.builder()
                        .nameProduct(nameProduct)
                        .caloriesProducts(calorieProduct)
                        .fatProducts(fatProduct)
                        .proteinProducts(proteinProduct)
                        .carbsProducts(carbsProduct)
                        .build();

                session.save(product);
                session.getTransaction().commit();
                log.info("Product with {} name was created", nameProduct);
            } else {
                log.warn("Product with the same name {} has already been created", nameProduct);
            }

        } catch (HibernateException e) {
            log.error("An error occurred while adding new product with name {}", nameProduct, e);
        }
    }

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
            log.error("An error occurred while finding meal ID by name: {}", mealName, e);
            return null;
        }
    }


    //Метод для пошуку айди напою по назві
    public Integer findDrinkIdByName(String drinkName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Query<Integer> query = session.createQuery(
                    "SELECT d.idDrink FROM Drink d WHERE d.nameDrink = :drinkName", Integer.class);
            query.setParameter("drinkName", drinkName);
            return query.uniqueResult();
        } catch (NoResultException e) {
            log.warn("No drink found with name: {}", drinkName, e);
            return null;
        } catch (HibernateException  e) {
            log.error("An error occurred while finding drink ID by name: {}", drinkName, e);
            return null;
        }
    }


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
            log.error("An error occurred while getting meal options by type: {}", mealType, e);
            return null;
        }
    }

    //Метод для збереження меню для користувача
    public void saveUserSelectedMenu(long phoneNumber,
                                     int breakfastId, int snackId, int snackSecondId,
                                     int lunchId, int dinnerId, Integer additionalDishId,
                                     Integer dinnerAdditionalDishId, int breakfastDrinkId,
                                     int lunchDrinkId, int dinnerDrinkId,
                                     double gramsForBreakfast, double gramsForDinner,
                                     double gramsForLunch, Double additionalDishGrams,
                                     Double dinnerAdditionalDishGrams,
                                     double snackFirstGrams,  double snackSecondGrams){

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                // Получаем пользователя по номеру телефона
                User user = session.get(User.class, phoneNumber);
                if (user != null) {
                    UserSelectedMenu userSelectedMenu = UserSelectedMenu.builder()
                            .user(user)
                            .breakfast(session.get(MealOption.class, breakfastId))
                            .breakfastDrink(session.get(Drink.class, breakfastDrinkId))
                            .lunch(session.get(MealOption.class, lunchId))
                            .lunchDrink(session.get(Drink.class, lunchDrinkId))
                            .dinner(session.get(MealOption.class, dinnerId))
                            .dinnerDrink(session.get(Drink.class, dinnerDrinkId))
                            .gramsForBreakfastSelectedMenu(gramsForBreakfast)
                            .gramsForLunchSelectedMenu(gramsForLunch)
                            .gramsForDinnerSelectedMenu(gramsForDinner)
                            .snackDishId(session.get(MealOption.class, snackId))
                            .gramsForSnackFirstDishGrams(snackFirstGrams)
                            .snackSecondDishId(session.get(MealOption.class, snackSecondId))
                            .gramsForSnackSecondDishGrams(snackSecondGrams)
                            .build();
                    // Проверка, не равно ли additionalDishId нулю (или null)
                  if (additionalDishId != null) {
                        userSelectedMenu.setAdditionalDishId(session.get(MealOption.class, additionalDishId));
                        userSelectedMenu.setLunchAdditionalDishGrams(additionalDishGrams);
                  }

                    if (dinnerAdditionalDishId != null) {
                        userSelectedMenu.setAdditionalDinnerDishId(session.get(MealOption.class, dinnerAdditionalDishId));
                        userSelectedMenu.setDinnerAdditionalDishGrams(dinnerAdditionalDishGrams);
                    }
                    session.save(userSelectedMenu);

                    tx.commit();
                } else {
                    log.warn("User with number phone {} did not find", phoneNumber);
                }
            } catch (HibernateException e) {
                log.error("An error occurred while saving user selected menu for user with phone number {}", phoneNumber, e);
            }
        }
    }

    // Метод для отримання алергій користувача
    public List<UserAllergy> getUserAllergies(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            String hql = "FROM UserAllergy WHERE user = :user";
            Query<UserAllergy> query = session.createQuery(hql, UserAllergy.class);
            query.setParameter("user", user);
            return query.list();
        }  catch (HibernateException e) {
            log.error("An error occurred while getting user allergies for user {}", user, e);
            return null;
        }
    }

    // Метод для отримання інгрідієнтів для блюда
    public List<Ingredients> getIngredientsByMealOption(MealOption mealOption) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            String hql = "SELECT mi.ingredients FROM MealIngredients mi WHERE mi.mealOption = :mealOption";
            Query<Ingredients> query = session.createQuery(hql, Ingredients.class);
            query.setParameter("mealOption", mealOption);
            return query.list();
        } catch (HibernateException e) {
            log.error("An error occurred while getting ingredients for meal option: {}", mealOption, e);
            return null;
        }
    }

    // Метод для отримання інгрідієнтів напою
    public List<Ingredients> getIngredientsByDrink(Drink drink) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            String hql = "SELECT mdi.ingredients FROM MealIngredientsDrink mdi WHERE mdi.drink = :drink";
            Query<Ingredients> query = session.createQuery(hql, Ingredients.class);
            query.setParameter("drink", drink);
            return query.list();
        } catch (HibernateException e) {
            log.error("An error occurred while getting ingredients for drink: {}", drink, e);
            return null;
        }
    }


    /*public Set<Ingredients> getAllergicIngredientsForUser(User user) {
        Set<Ingredients> allergicIngredients = new HashSet<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            // Получаем список аллергий пользователя
            List<UserAllergy> allergies = session.createQuery(
                    "SELECT ua FROM UserAllergy ua WHERE ua.user = :user", UserAllergy.class)
                    .setParameter("user", user)
                    .list();
            // Добавляем ингредиенты из аллергий пользователя в список
            for (UserAllergy allergy : allergies) {
                allergicIngredients.add(allergy.getIngredients());
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allergicIngredients;
    }
*/

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
            criteria.where(builder.equal(root.get("user").get("phoneNumber"), phoneNumber));
            criteria.orderBy(builder.desc(root.get("idSelectedMenu")));
            Query<UserSelectedMenu> query = session.createQuery(criteria);
            query.setMaxResults(1);
            lastMenu = query.uniqueResult();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("An error occurred while getting last user menu for phone number {}", phoneNumber, e);
        } finally {
            session.close();
        }
        return lastMenu;
    }

    public List<UserSelectedMenu> getAllUserMenus(Long phoneNumber, int numberOfDays) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<UserSelectedMenu> userMenus = new ArrayList<>();
        try {
            tx = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<UserSelectedMenu> criteria = builder.createQuery(UserSelectedMenu.class);
            Root<UserSelectedMenu> root = criteria.from(UserSelectedMenu.class);
            criteria.select(root);
            criteria.where(builder.equal(root.get("user").get("phoneNumber"), phoneNumber));
            criteria.orderBy(builder.desc(root.get("idSelectedMenu"))); // сортировка по убыванию idSelectedMenu
            Query<UserSelectedMenu> query = session.createQuery(criteria);
            query.setMaxResults(numberOfDays); // ограничение количества результатов
            userMenus = query.getResultList();
            Collections.reverse(userMenus); // переворачиваем список, чтобы меню было в обратном порядке
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("An error occurred while getting user menus for phone number {}", phoneNumber, e);
        } finally {
            session.close();
        }
        return userMenus;
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
            log.error("An error occurred while getting meal option name for id {}", id, e);
        } finally {
            session.close();
        }
        return mealOptionName;
    }

    //Метод для отримання назви по айди
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
            log.error("An error occurred while getting drink name for id {}", id, e);
        } finally {
            session.close();
        }
        return drinkName;
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

    public void deleteUserMenus(Long phoneNumber, int numberOfMenusToDelete) {
        // Открытие сессии
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            // Получение всех меню пользователя по номеру телефона
            Query<UserSelectedMenu> query = session.createQuery("FROM UserSelectedMenu WHERE user.phoneNumber = :phoneNumber", UserSelectedMenu.class);
            query.setParameter("phoneNumber", phoneNumber);
            List<UserSelectedMenu> userMenus = query.getResultList();

            // Сортировка меню в порядке убывания
            userMenus.sort((menu1, menu2) -> menu2.getIdSelectedMenu().compareTo(menu1.getIdSelectedMenu()));

            // Удаление указанного количества меню
            int count = 0;
            for (UserSelectedMenu menu : userMenus) {
                session.delete(menu);
                count++;
                if (count >= numberOfMenusToDelete) {
                    break;
                }
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            // Закрытие сессии
            session.close();
        }
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

    public void deleteUserAllergy(User user, String allergyName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            // Получаем идентификатор ингредиента по его имени
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Integer> criteriaQuery = builder.createQuery(Integer.class);
            Root<Ingredients> root = criteriaQuery.from(Ingredients.class);

            criteriaQuery.select(root.get("idIngredients")).where(builder.equal(root.get("nameIngredients"), allergyName));
            Query<Integer> query = session.createQuery(criteriaQuery);
            Integer ingredientId = query.uniqueResult();

            // Удаляем запись из UserAllergy
            if (ingredientId != null) {
                CriteriaDelete<UserAllergy> criteriaDelete = builder.createCriteriaDelete(UserAllergy.class);
                Root<UserAllergy> userAllergyRoot = criteriaDelete.from(UserAllergy.class);

                Predicate userPredicate = builder.equal(userAllergyRoot.get("user"), user);
                Predicate ingredientPredicate = builder.equal(userAllergyRoot.get("ingredients").get("idIngredients"), ingredientId);

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
    public void deleteUser(User user) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.delete(user);
            transaction.commit();
        }
        catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void deleteUserAndRelatedSelectedMenus(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            String sql = "DELETE FROM UserSelectedMenu WHERE phone_number = :phoneNumber";
            Query query = session.createSQLQuery(sql);
            query.setParameter("phoneNumber", user.getPhoneNumber());
            int rowsAffected = query.executeUpdate();

            session.delete(user);

            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

}
