package HealthyDiaryApp.util;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import HealthyDiaryApp.entity.User;
import HealthyDiaryApp.entity.WeightLossGoals;
import HealthyDiaryApp.entity.WeightLossProgress;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;

@Slf4j
public class WeightLossGoalsComponent {

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

    public static List<Double> fetchWeightLossProgress(Long phoneNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            // Находим goalId по номеру телефона пользователя
            Query<Integer> goalIdQuery = session.createQuery(
                    "SELECT goal.idWeightLossGoals FROM WeightLossGoals goal WHERE goal.user.phoneNumber = :phoneNumber", Integer.class);
            goalIdQuery.setParameter("phoneNumber", phoneNumber);
            Integer goalId = goalIdQuery.uniqueResult();
            if (goalId == null) {
                log.warn("user with phone number {} was not find", phoneNumber);
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
                log.warn("user with phone number {} was not find", phoneNumber);
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
