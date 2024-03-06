
-- Created user
INSERT INTO Users (
    "phone_number",
    "name",
    "age",
    "weight",
    "height",
    "gender",
    "activity_level",
    "allergies",
    "cause",
    "total_caloric",
    "total_protein",
    "total_fat",
    "total_carbohydrates"
) VALUES (
    380984269195, 
    'СЕРГІЙ',
    29,
    75.5,
    175.0,
    TRUE, -- TRUE для мужского пола, FALSE для женского
    'High', 
    FALSE,
    FALSE,
    2050.0,
    76.0,
    50,
    200
);


INSERT INTO Users (
    "phone_number",
    "name",
    "age",
    "weight",
    "height",
    "gender",
    "activity_level",
    "allergies",
    "cause",
    "total_caloric",
    "total_protein",
    "total_fat",
    "total_carbohydrates"
) VALUES (
    380973090484, 
    'Катерина',
    22,
    55.5,
    155.0,
    FALSE, 
    'High', 
    TRUE,
    FALSE,
    1650.0,
    56.0,
    50,
    180
);

INSERT INTO User (
    "phone_number",
    "name",
    "age",
    "weight",
    "height",
    "gender",
    "activity_level",
    "allergies",
    "cause",
    "total_caloric",
    "total_protein",
    "total_fat",
    "total_carbohydrates"
) VALUES (
    380989930042, 
    'Юлія',
    23,
    77.3,
    160.0,
    False, 
    'Low', 
    FALSE,
    FALSE,
    1560.0,
    100.0,
    30,
    70
);

INSERT INTO "public.User" (
    "phone_number",
    "name",
    "age",
    "weight",
    "height",
    "gender",
    "activity_level",
    "allergies",
    "cause",
    "total_caloric",
    "total_protein",
    "total_fat",
    "total_carbohydrates"
) VALUES (
    380507730042, 
    'Костя',
    33,
    97.3,
    180.0,
    TRUE, 
    'High', 
    FALSE,
    FALSE,
    1660.0,
    110.0,
    40,
    70
);

INSERT INTO "public.User" (
    "phone_number",
    "name",
    "age",
    "weight",
    "height",
    "gender",
    "activity_level",
    "allergies",
    "cause",
    "total_caloric",
    "total_protein",
    "total_fat",
    "total_carbohydrates"
) VALUES (
    380973399488, 
    'Павло',
    30,
    104,
    181.0,
    TRUE, 
    'Low', 
    TRUE,
    TRUE,
    1659.0,
    100.0,
    66,
    57
);

SELECT * from Users

-- Created allergies for user
INSERT INTO UserAllergy ("number_phone", "id_ingredient")
VALUES
    (380973090484, 122);

SELECT * from UserAllergy
    
-- add data to the "Products" table 
COPY Products  FROM 'C:/products.csv' DELIMITER ';' CSV HEADER;

-- add data to the "Ingredients" table 
COPY Ingredients FROM 'C:/ingredients.csv' DELIMITER ';' CSV HEADER;

--add data to the "MealOption" table
COPY MealOption FROM 'C:/food.csv' DELIMITER ';' CSV HEADER;

--add data to the "Drink" table
COPY Drinks FROM 'C:/drink.csv' DELIMITER ';' CSV HEADER;

--add data to the MealIngredients

INSERT INTO MealIngredients ("meal_id", "ingredient_id")
VALUES 
    
    (116, 139),
    (116, 76),
    (116, 82),
   
    (116, 42),
    (116, 178),
    (116, 107); 

