package util;

import entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.List;


@Slf4j
public class HibernateMethods {

    public void addUserAllergyForUser(long phoneNumber, int ingredientId) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                // Получаем пользователя по номеру телефона
                User user = session.get(User.class, phoneNumber);
                Ingredients ingredient = session.get(Ingredients.class, ingredientId);

                // Проверяем, что пользователь и ингредиент существуют
                if (user != null && ingredient != null) {

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

    public Products findProductByName(String productName) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                Query<Products> query = session.createQuery("FROM Products WHERE nameProduct = :name", Products.class);
                query.setParameter("name", productName);
                Products products = query.uniqueResult();
                session.getTransaction().commit();
                return products;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addUserSelectedProduct(long phoneNumber, int productId, double grams, double remainingCalories,
                                       double remainingFat, double remainingProtein, double remainingCarbs) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                // Получаем пользователя по номеру телефона
                User user = session.get(User.class, phoneNumber);

                Products products = session.get(Products.class, productId);

                // Проверяем, что пользователь и ингредиент существуют
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

                    // Сохраняем запись в базе данных
                    session.save(userSelectedProduct);
                    session.getTransaction().commit();

                    log.info("Product successfully added for user with phone number {}", phoneNumber);
                } else {
                    log.warn("User with phone number {} or product with id {} not found", phoneNumber, productId);
                }
            }

        }
    }


    public void createUser(long phoneNumber, String name, int age, double weight, double height, boolean gender,
                           ActivityLevel activityLevel, boolean allergies, boolean cause, double totalCaloric,
                           double totalProtein, double totalFat, double totalCarbs) {

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
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

    public List<Products> getUserSelectedProduct(long phoneNumber) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                Query<Products> query = session.createQuery("FROM Products ", Products.class);
                List<Products> productList = query.list();
                session.getTransaction().commit();
                return productList;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<UserSelectedProduct> getUserSelectedProductsForNumberPhone(long phoneNumber) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            List<UserSelectedProduct> userSelectedProducts = null;
            try (Session session = sessionFactory.openSession()) {
                CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                CriteriaQuery<UserSelectedProduct> criteriaQuery = criteriaBuilder.createQuery(UserSelectedProduct.class);
                Root<UserSelectedProduct> root = criteriaQuery.from(UserSelectedProduct.class);

                // Добавляем условие для выборки продуктов конкретного пользователя
                Predicate predicate = criteriaBuilder.equal(root.get("user").get("phoneNumber"), phoneNumber);
                criteriaQuery.where(predicate);

                userSelectedProducts = session.createQuery(criteriaQuery).getResultList();
            } catch (HibernateException e) {
                e.printStackTrace();
            }
            return userSelectedProducts;
        }
    }

    public List<Products> getAllProduct() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                Query<Products> query = session.createQuery("FROM Products", Products.class);
                List<Products> productList = query.list();
                session.getTransaction().commit();
                return productList;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void DropProductUser(long phoneNumber) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

                // Создаем критерий запроса для удаления
                CriteriaDelete<UserSelectedProduct> criteriaDelete = criteriaBuilder.createCriteriaDelete(UserSelectedProduct.class);
                Root<UserSelectedProduct> root = criteriaDelete.from(UserSelectedProduct.class);

                // Добавляем условие для выборки записей для указанного номера телефона
                criteriaDelete.where(criteriaBuilder.equal(root.get("user").get("phoneNumber"), phoneNumber));
                // Выполняем запрос на удаление
                 session.createQuery(criteriaDelete).executeUpdate();

                session.getTransaction().commit();

                session.getTransaction().commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getTotalCaloriesForUser(long phoneNumber) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                CriteriaQuery<Double> criteriaQuery = criteriaBuilder.createQuery(Double.class);
                Root<UserSelectedProduct> root = criteriaQuery.from(UserSelectedProduct.class);

                // Добавляем условие для выборки продуктов конкретного пользователя
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
            }
        }catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public double getTotalFatForUser(long phoneNumber) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public double getTotalProteinForUser(long phoneNumber) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public double getTotalCarbsForUser(long phoneNumber) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public void addNewProducts(String nameProduct, double calorieProduct, double fatProduct, double proteinProduct, double carbsProduct) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
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

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
