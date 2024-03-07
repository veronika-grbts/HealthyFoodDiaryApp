package HealthyDiaryApp.util;


import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import HealthyDiaryApp.entity.Drink;
import HealthyDiaryApp.entity.MealOption;
import HealthyDiaryApp.entity.User;
import HealthyDiaryApp.entity.UserSelectedMenu;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class UserSelectedMenuComponent {
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


}
