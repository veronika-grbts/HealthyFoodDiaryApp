package HealthyDiaryApp.util;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import HealthyDiaryApp.entity.Products;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
/*
 * ProductComponent class
 *
 * Version: 1.0
 * Date: 2024-03-07
 * Author: Veronika Horobets
 *
 * Description: Цей клас містить методи для пошуку, отримання, та створення продуктів.
 */

@Slf4j
public class ProductComponent {
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
}
