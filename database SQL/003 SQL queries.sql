-- look at all the ingredients that make up the dish

SELECT "name_ingredients"
FROM MealIngredients
JOIN Ingredients ON MealIngredients."ingredient_id" = Ingredients."id_ingredients"
WHERE "meal_id" = 112;

-- display allergies by phone number

SELECT type_meal FROM UserAllergy WHERE number_phone = 380973090484;

-- add the selected product by the user

INSERT INTO UserSelectedProduct ("phone_number", "product_id", "grams")
VALUES (
    380984269195,
    (SELECT "product_id" FROM Products WHERE "name" = 'Хурма Корольок'), 120
);

--show the entire list of selected products from the user
SELECT p.name FROM UserSelectedProduct usp JOIN Products p ON usp.product_id = p.product_id WHERE usp.phone_number = 380984269195;


--adding a menu for the user taking into account allergies

INSERT INTO UserSelectedMenu ("phone_number", "breakfast_id", "breakfast_drink_id", "lunch_id", "lunch_drink_id", "dinner_id", "dinner_drink_id")
SELECT 
    380973090484 AS "phone_number",
    COALESCE(bs."breakfast_id", 0) AS "breakfast_id",
    COALESCE(bs."breakfast_drink_id", 0) AS "breakfast_drink_id",
    COALESCE(ls."lunch_id", 0) AS "lunch_id",
    COALESCE(ls."lunch_drink_id", 0) AS "lunch_drink_id",
    COALESCE(ds."dinner_id", 0) AS "dinner_id",
    COALESCE(ds."dinner_drink_id", 0) AS "dinner_drink_id"
FROM (
    SELECT 
        bo.id_option AS "breakfast_id",
        (SELECT id_drink FROM Drinks ORDER BY RANDOM() LIMIT 1) AS "breakfast_drink_id"
    FROM MealOption AS bo
    WHERE bo.type_meal = 'Breakfast'
        AND bo.id_option BETWEEN (SELECT MIN(id_option) FROM BreakfastOptions) AND (SELECT MAX(id_option) FROM BreakfastOptions)
    ORDER BY RANDOM()
    LIMIT 1
) bs
CROSS JOIN (
    SELECT 
        lo.id_option AS "lunch_id",
        (SELECT id_drink FROM Drinks ORDER BY RANDOM() LIMIT 1) AS "lunch_drink_id"
    FROM MealOption AS lo
    WHERE lo.type_meal = 'Lunch'
        AND lo.id_option BETWEEN (SELECT MIN(id_option) FROM LunchOptions) AND (SELECT MAX(id_option) FROM LunchOptions)
    ORDER BY RANDOM()
    LIMIT 1
) ls
CROSS JOIN (
    SELECT 
        d.id_option AS "dinner_id",
        (SELECT id_drink FROM Drinks ORDER BY RANDOM() LIMIT 1) AS "dinner_drink_id"
    FROM MealOption AS d
    WHERE d.type_meal = 'Dinner'
        AND d.id_option BETWEEN (SELECT MIN(id_option) FROM DinnerOptions) AND (SELECT MAX(id_option) FROM DinnerOptions)
    ORDER BY RANDOM()
    LIMIT 1
) ds
WHERE
    CASE
        WHEN (SELECT allergies FROM  Users WHERE "phone_number" = 380973090484) = FALSE THEN
            TRUE -- Если аллергий нет, то пропускаем проверку
        ELSE
            NOT EXISTS (
                SELECT 1
                FROM MealIngredients AS bi
                JOIN Ingredients AS i ON bi.ingredient_id = i.id_ingredients
                JOIN UserAllergy AS ua ON i.name_ingredients = ua.type_meal
                WHERE ua.number_phone = 380973090484
                    AND (
                        bi.meal_id = bs."breakfast_id"
                        OR bi.meal_id = ls."lunch_id"
                        OR bi.meal_id = ds."dinner_id"
                    )
            ) AND
            NOT EXISTS (
                SELECT 1
                FROM MealIngredientsDrink AS md
                JOIN Ingredients AS i ON md.id_ingredients = i.id_ingredients
                JOIN UserAllergy AS ua ON i.name_ingredients = ua.type_meal
                WHERE ua.number_phone = 380973090484
                    AND (
                        md.id_drink = bs."breakfast_drink_id"
                        OR md.id_drink = ls."lunch_drink_id"
                        OR md.id_drink = ds."dinner_drink_id"
                    )
            )
           
   END;


 -- display menu for user

 SELECT 
    mo.name AS breakfast,
    d.name_drink AS breakfast_drink,
    ml.name AS lunch,
    dl.name_drink AS lunch_drink,
    md.name AS dinner,
    dd.name_drink AS dinner_drink
FROM 
    UserSelectedMenu usm
JOIN 
    MealOption mo ON usm.breakfast_id = mo.id_option
LEFT JOIN 
    Drinks d ON usm.breakfast_drink_id = d.id_drink
JOIN 
    MealOption ml ON usm.lunch_id = ml.id_option
LEFT JOIN 
    Drinks dl ON usm.lunch_drink_id = dl.id_drink
JOIN 
    MealOption md ON usm.dinner_id = md.id_option
LEFT JOIN 
    Drinks dd ON usm.dinner_drink_id = dd.id_drink
WHERE 
    usm.phone_number = 380973090484;  


-- display of the user's personal information
SELECT * FROM Users WHERE phone_number = 380973090484;
   