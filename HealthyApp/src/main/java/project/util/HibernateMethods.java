package project.util;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import project.entity.*;
import project.enums.ActivityLevel;
import project.enums.MealType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
                log.warn("User with phone number {} or ingredient with ID {} not found",
                        phoneNumber, ingredientId);
            }
        } catch (Exception e) {
            log.error("Error while adding allergy for user with phone number {}",
                    phoneNumber, e);
        }
    }

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

    //Пошук продукту на ім'я
    public Products findProductByName(String productName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Products> criteriaQuery = builder.createQuery(Products.class);
            Root<Products> root = criteriaQuery.from(Products.class);
            criteriaQuery.select(root)
                    .where(builder.equal(root.get("nameProduct"), productName));

            Query<Products> query = session.createQuery(criteriaQuery);
            Products product = query.uniqueResult();
            session.getTransaction().commit();
            return product;
        } catch (NoResultException e) {
            log.warn("No product found with name: {}", productName, e);
            return null;
        } catch (HibernateException e) {
            log.error("An error occurred while finding product by name: {}",
                    productName, e);
            return null;
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

    //Метод для отримання всіх елементів з таблиці Products
    public List<Products> getAllProduct() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Query<Products> query = session.createQuery(
                    "FROM Products", Products.class);
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
            Query<Products> query = session.createQuery(
                    "FROM Products ", Products.class);
            List<Products> productList = query.list();
            session.getTransaction().commit();
            return productList;
        } catch (HibernateException e) {
            log.error("An error occurred while getting user selected products for user with phone number {}",
                    phoneNumber, e);
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

    //Метод для створення ногово продукту
    public void addNewProducts(String nameProduct, double calorieProduct,
                               double fatProduct, double proteinProduct,
                               double carbsProduct) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Products> criteriaQuery = criteriaBuilder.createQuery(Products.class);
            Root<Products> root = criteriaQuery.from(Products.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(
                    root.get("nameProduct"), nameProduct)
            );
            Query<Products> query = session.createQuery(criteriaQuery);
            List<Products> existingProducts = query.getResultList();

            if (existingProducts.isEmpty()) {
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
                log.warn("Product with the same name {} has already been created",
                        nameProduct);
            }

        } catch (HibernateException e) {
            log.error("An error occurred while adding new product with name {}",
                    nameProduct, e);
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
            log.error("An error occurred while finding meal ID by name: {}",
                    mealName, e);
            return null;
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
                  if (additionalDishId != null) {
                        userSelectedMenu.setAdditionalDishId(
                                session.get(MealOption.class, additionalDishId)
                        );
                        userSelectedMenu.setLunchAdditionalDishGrams(additionalDishGrams);
                  }

                    if (dinnerAdditionalDishId != null) {
                        userSelectedMenu.setAdditionalDinnerDishId(
                                session.get(MealOption.class, dinnerAdditionalDishId)
                        );
                        userSelectedMenu.setDinnerAdditionalDishGrams(dinnerAdditionalDishGrams);
                    }
                    session.save(userSelectedMenu);

                    tx.commit();
                } else {
                    log.warn("User with number phone {} did not find", phoneNumber);
                }
            } catch (HibernateException e) {
                log.error("An error occurred while saving user selected menu for user with phone number {}",
                        phoneNumber, e);
            }
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

    public List<UserSelectedMenu> getAllUserMenus(Long phoneNumber, int numberOfDays) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<UserSelectedMenu> userMenus = new ArrayList<>();
        try {
            tx = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<UserSelectedMenu> criteria = builder
                    .createQuery(UserSelectedMenu.class);

            Root<UserSelectedMenu> root = criteria.from(UserSelectedMenu.class);
            criteria.select(root);
            criteria.where(builder.equal(root.get("user")
                    .get("phoneNumber"), phoneNumber));
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
            log.error("An error occurred while getting meal option name for id {}",
                    id, e);
        }finally {
            session.close();
        }
        return mealOptionName;
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

    //метод для сохранения цели похудения
    public static void saveWeightLossGoal(User user, double currentWeight,
                                          double targetWeight, double targetCaloricDeficit,
                                          short estimatedCompletionTime,
                                          double caloriesWithLosingWeight) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            WeightLossGoals weightLossGoal = new WeightLossGoals();
            weightLossGoal.setUser(user);
            weightLossGoal.setCurrentWeightUserLossGoals(currentWeight);
            weightLossGoal.setTargetWeightUserLossGoals(targetWeight);
            weightLossGoal.setTargetCaloricDeficitUserLossGoals(targetCaloricDeficit);
            weightLossGoal.setEstimatedCompletionTimeLossGoals(estimatedCompletionTime);
            weightLossGoal.setCaloriesDayWithDeficitLossGoals(caloriesWithLosingWeight);

            session.save(weightLossGoal);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    //метод для получения лучшего веса для пользователя
    public double getTargetWeightByPhoneNumber(long phoneNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Double> criteriaQuery = builder.createQuery(Double.class);
            Root<WeightLossGoals> root = criteriaQuery.from(WeightLossGoals.class);

            criteriaQuery.select(root.get("targetWeightUserLossGoals"))
                    .where(builder.equal(root.get("user").get("phoneNumber"),
                            phoneNumber));

            Query<Double> query = session.createQuery(criteriaQuery);
            Double targetWeight = query.uniqueResult();

            if (targetWeight != null) {
                log.info("Target weight found for phone number {}: {}",
                        phoneNumber, targetWeight);
            } else {
                log.warn("Target weight not found for phone number {}", phoneNumber);
            }

            return targetWeight != null ? targetWeight : 0.0; // Возвращаем целевой вес или 0.0, если он не найден
        } catch (HibernateException e) {
            log.error("An error occurred while getting target weight by phone number", e);
            return 0.0;
        }
    }

    public double getNowWeightByPhoneNumber(long phoneNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Double> criteriaQuery = builder.createQuery(Double.class);
            Root<WeightLossGoals> root = criteriaQuery.from(WeightLossGoals.class);

            criteriaQuery.select(root.get("currentWeightUserLossGoals"))
                    .where(builder.equal(root.get("user").get("phoneNumber"),
                            phoneNumber));

            Query<Double> query = session.createQuery(criteriaQuery);
            Double targetWeight = query.uniqueResult();

            if (targetWeight != null) {
                log.info("Current weight found for phone number {}: {}",
                        phoneNumber, targetWeight);
            } else {
                log.warn("current weight not found for phone number {}", phoneNumber);
            }

            return targetWeight != null ? targetWeight : 0.0; // Возвращаем целевой вес или 0.0, если он не найден
        } catch (HibernateException e) {
            log.error("An error occurred while getting current weight by phone number", e);
            return 0.0;
        }
    }

    public Short getEstimatedCompletionTimeByPhoneNumber(long phoneNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Short> criteriaQuery = builder.createQuery(Short.class);
            Root<WeightLossGoals> root = criteriaQuery.from(WeightLossGoals.class);

            criteriaQuery.select(root.get("estimatedCompletionTimeLossGoals"))
                    .where(builder.equal(root.get("user").get("phoneNumber"),
                            phoneNumber));

            Query<Short> query = session.createQuery(criteriaQuery);
            Short targetWeight = query.uniqueResult();

            if (targetWeight != null) {
                log.info("estimatedCompletionTimeLossGoals found for phone number {}: {}",
                        phoneNumber, targetWeight);
            } else {
                log.warn("estimatedCompletionTimeLossGoals  not found for phone number {}", phoneNumber);
            }

            return targetWeight != null ? targetWeight : 0; // Возвращаем целевой вес или 0.0, если он не найден
        } catch (HibernateException e) {
            log.error("An error occurred while getting estimatedCompletionTimeLossGoals by phone number", e);
            return 0;
        }
    }

    //метод для получения калорий с учетом дефецита(когда пользователь худеет)
    public double getCalociesDayWithDeficitByPhoneNumber(long phoneNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Double> criteriaQuery = builder.createQuery(Double.class);
            Root<WeightLossGoals> root = criteriaQuery.from(WeightLossGoals.class);

            criteriaQuery.select(root.get("caloriesDayWithDeficitLossGoals"))
                    .where(builder.equal(root.get("user").get("phoneNumber"), phoneNumber));

            Query<Double> query = session.createQuery(criteriaQuery);
            Double targetWeight = query.uniqueResult();

            if (targetWeight != null) {
                log.info("calories with deficit found for phone number {}: {}",
                        phoneNumber, targetWeight);
            } else {
                log.warn("calories with deficit not found for phone number {}",
                        phoneNumber);
            }

            return targetWeight != null ? targetWeight : 0.0; // Возвращаем целевой вес или 0.0, если он не найден
        } catch (HibernateException e) {
            log.error("An error occurred while getting calories with deficit by phone number", e);
            return 0.0;
        }
    }

    //метод для изменения найлучшего веса для пользоателя
    public void updateTargetWeightByPhoneNumber(long phoneNumber, double newTargetWeight) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<WeightLossGoals> criteriaUpdate = builder
                    .createCriteriaUpdate(WeightLossGoals.class);
            Root<WeightLossGoals> root = criteriaUpdate.from(WeightLossGoals.class);

            criteriaUpdate.set(root.get("targetWeightUserLossGoals"), newTargetWeight)
                    .where(builder.equal(root.get("user").get("phoneNumber"), phoneNumber));

            session.createQuery(criteriaUpdate).executeUpdate();

            session.getTransaction().commit();
            log.info("Target weight updated successfully for phone number {}: {}",
                    phoneNumber, newTargetWeight);
        } catch (HibernateException e) {
            log.error("An error occurred while updating target weight by phone number", e);
        }
    }


    public Double getCurrentWeightByPhoneNumber(long phoneNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Double> criteriaQuery = builder.createQuery(Double.class);
            Root<WeightLossProgress> root = criteriaQuery.from(WeightLossProgress.class);
            Join<WeightLossProgress, WeightLossGoals> joinGoal = root.join("goal");
            Join<WeightLossGoals, User> joinUser = joinGoal.join("user");

            criteriaQuery.select(root.get("currentWeight"))
                    .where(builder.equal(joinUser.get("phoneNumber"), phoneNumber))
                    .orderBy(builder.desc(root.get("date")));

            Query<Double> query = session.createQuery(criteriaQuery);
            query.setMaxResults(1);

            Double currentWeight = query.uniqueResult();
            return currentWeight;
        } catch (Exception e) {
            // Обработка ошибок
            e.printStackTrace();
            return null;
        }
    }

    public Double getCaloricIntakeByPhoneNumber(long phoneNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Double> criteriaQuery = builder.createQuery(Double.class);
            Root<WeightLossProgress> root = criteriaQuery.from(WeightLossProgress.class);
            Join<WeightLossProgress, WeightLossGoals> joinGoal = root.join("goal");
            Join<WeightLossGoals, User> joinUser = joinGoal.join("user");

            criteriaQuery.select(root.get("caloricIntake"))
                    .where(builder.equal(joinUser.get("phoneNumber"), phoneNumber))
                    .orderBy(builder.desc(root.get("date")));

            Query<Double> query = session.createQuery(criteriaQuery);
            query.setMaxResults(1);

            Double currentWeight = query.uniqueResult();
            return currentWeight;
        } catch (Exception e) {
            // Обработка ошибок
            e.printStackTrace();
            return null;
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

    public static List<Double> fetchWeightLossProgress(Long phoneNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            // Находим goalId по номеру телефона пользователя
            Query<Integer> goalIdQuery = session.createQuery(
                    "SELECT goal.idWeightLossGoals FROM WeightLossGoals goal WHERE goal.user.phoneNumber = :phoneNumber", Integer.class);
            goalIdQuery.setParameter("phoneNumber", phoneNumber);
            Integer goalId = goalIdQuery.uniqueResult();
            if (goalId == null) {
                System.out.println("Пользователь с указанным номером телефона не найден.");
                return null;
            }

            // Получаем значения currentWeight для найденного goalId
            Query<Double> currentWeightQuery = session.createQuery(
                    "SELECT progress.currentWeight FROM WeightLossProgress progress WHERE progress.goal.idWeightLossGoals = :goalId", Double.class);
            currentWeightQuery.setParameter("goalId", goalId);
            List<Double> currentWeightList = currentWeightQuery.list();

            session.getTransaction().commit();
            return currentWeightList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Date> fetchWeightLossDates(Long phoneNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            // Находим goalId по номеру телефона пользователя
            Query<Integer> goalIdQuery = session.createQuery(
                    "SELECT goal.idWeightLossGoals FROM WeightLossGoals goal WHERE goal.user.phoneNumber = :phoneNumber", Integer.class);
            goalIdQuery.setParameter("phoneNumber", phoneNumber);
            Integer goalId = goalIdQuery.uniqueResult();
            if (goalId == null) {
                System.out.println("Пользователь с указанным номером телефона не найден.");
                return null;
            }

            // Получаем значения дат для найденного goalId
            Query<Date> dateQuery = session.createQuery(
                    "SELECT progress.date FROM WeightLossProgress progress WHERE progress.goal.idWeightLossGoals = :goalId", Date.class);
            dateQuery.setParameter("goalId", goalId);
            List<Date> dateList = dateQuery.list();

            session.getTransaction().commit();
            return dateList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveWeightLossProgress(Long phoneNumber, Date date,
                                       Double currentWeight, Double caloricIntake,
                                       Double deficitCaloric) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            // Находим goal_id по номеру телефона пользователя
            Query<Integer> goalIdQuery = session.createQuery(
                    "SELECT goal.idWeightLossGoals FROM WeightLossGoals goal WHERE goal.user.phoneNumber = :phoneNumber", Integer.class);
            goalIdQuery.setParameter("phoneNumber", phoneNumber);
            Integer goalId = goalIdQuery.uniqueResult();
            if (goalId == null) {
                System.out.println("Пользователь с указанным номером телефона не найден.");
                return;
            }

            // Создаем объект WeightLossProgress и сохраняем его в базу данных
            WeightLossGoals goal = session.load(WeightLossGoals.class, goalId);
            WeightLossProgress progress = new WeightLossProgress();
            progress.setGoal(goal);
            progress.setDate(date);
            progress.setCurrentWeight(currentWeight);
            progress.setCaloricIntake(caloricIntake);
            progress.setDeficitCaloric(deficitCaloric);

            session.save(progress);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
