
-- Создание ENUM с возможными вариантами
CREATE TYPE ActivityLevel AS ENUM ('Low', 'Medium', 'High');

CREATE TYPE MealType AS ENUM ('Breakfast', 'Lunch', 'Dinner');


CREATE TABLE Users (
	"phone_number" bigint NOT NULL UNIQUE,
	"name" varchar(50) NOT NULL,
	"age" integer NOT NULL,
	"weight" FLOAT NOT NULL,
	"height" FLOAT NOT NULL,
	"gender" BOOLEAN NOT NULL,
	"activity_level" ActivityLevel NOT NULL,
	"allergies" BOOLEAN NOT NULL,
	"cause" BOOLEAN NOT NULL,
	"total_caloric" FLOAT NOT NULL,
	"total_protein" FLOAT NOT NULL,
	"total_fat" integer NOT NULL,
	"total_carbohydrates" integer NOT NULL,
	CONSTRAINT "User_pk" PRIMARY KEY ("phone_number")
) WITH (
  OIDS=FALSE
);



CREATE TABLE UserAllergy (
	"id" serial NOT NULL,
	"number_phone" bigint NOT NULL,
	"id_ingredient" integer NOT NULL,
	CONSTRAINT "UserAllergy_pk" PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE Products (
	"product_id" serial NOT NULL,
	"name" varchar(100) NOT NULL,
	"calories" FLOAT NOT NULL,
	"fat" FLOAT NOT NULL,
	"protein" FLOAT NOT NULL,
	"carbs" FLOAT NOT NULL,
	CONSTRAINT "Products_pk" PRIMARY KEY ("product_id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE UserSelectedProduct (
	"id" serial NOT NULL,
	"phone_number" bigint NOT NULL,
	"product_id" integer NOT NULL,
	"grams" float NOT NULL
	CONSTRAINT "UserSelectedProduct_pk" PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);


CREATE TABLE MealOption (
    "id_option" serial NOT NULL,
    "name" varchar(100) NOT NULL,
    "calories" FLOAT NOT NULL,
    "fat" FLOAT NOT NULL,
    "protein" FLOAT NOT NULL,
    "carbs" FLOAT NOT NULL,
    "type_meal" MealType NOT NULL,
    CONSTRAINT "MealOption_pk" PRIMARY KEY ("id_option")
) WITH (
    OIDS=FALSE
);

CREATE VIEW BreakfastOptions AS
SELECT * FROM MealOption
WHERE "type_meal" = 'Breakfast';


CREATE VIEW LunchOptions AS
SELECT * FROM MealOption
WHERE "type_meal" = 'Lunch';


CREATE VIEW DinnerOptions AS
SELECT * FROM MealOption
WHERE "type_meal" = 'Dinner';

CREATE TABLE UserSelectedMenu (
  "id" serial NOT NULL,
  "phone_number" bigint NOT NULL,
  "breakfast_id" integer REFERENCES MealOption("id_option"),
  "breakfast_drink_id" integer REFERENCES Drinks("id_drink"),
  "lunch_id" integer REFERENCES MealOption("id_option"),
  "lunch_drink_id" integer REFERENCES Drinks("id_drink"),
  "dinner_id" integer REFERENCES MealOption("id_option"),
  "dinner_drink_id" integer REFERENCES Drinks("id_drink"),
  "calorie_breakfast" double NOT NULL,
  "calorie_lunch" double NOT NULL,
  "calorie_dinner" double NOT NULL,
  "lunch_additional_dish_id" REFERENCES MealOption("id_option"),
  "lunch_additional_dish_grams" double, 
  "snack_first_dish_id" REFERENCES MealOption("id_option"),
  "snack_first_dish_grams" double,
  "snack_second_dish_id" REFERENCES MealOption("id_option"),
  "snack_second_dish_grams" double,
  "dinner_additional_dish_id" REFERENCES MealOption("id_option"),
  "dinner_additional_dish_grams" double 
  CONSTRAINT "UserSelectedMenu_pk" PRIMARY KEY ("id"),
  CONSTRAINT "UserSelectedMenu_fk0" FOREIGN KEY ("phone_number") REFERENCES User("phone_number")
) WITH (
  OIDS=FALSE
);


CREATE TABLE Drinks (
	"id_drink" serial NOT NULL,
	"name_drink" varchar(50) NOT NULL,
	"calories" FLOAT NOT NULL,
	"fat" FLOAT NOT NULL,
	"protein" FLOAT NOT NULL,
	"carbs" FLOAT NOT NULL,
	CONSTRAINT "Drinks_pk" PRIMARY KEY ("id_drink")
) WITH (
  OIDS=FALSE
);


CREATE TABLE Ingredients (
	"id_ingredients" serial NOT NULL,
	"name_ingredients" varchar(50) NOT NULL,
	CONSTRAINT "Ingredients_pk" PRIMARY KEY ("id_ingredients")
) WITH (
  OIDS=FALSE
);


CREATE TABLE MealIngredients (
	"id_mi" serial NOT NULL,
	"meal_id" integer NOT NULL,
	"ingredient_id" integer NOT NULL,
	CONSTRAINT "MealIngredients_pk" PRIMARY KEY ("id_mi")
) WITH (
  OIDS=FALSE
);



CREATE TABLE MealIngredientsDrink (
	"id_ingredient_drink" serial NOT NULL,
	"id_drink" integer NOT NULL,
	"id_ingredients" integer NOT NULL,
	CONSTRAINT "MealIngredientsDrink_pk" PRIMARY KEY ("id_ingredient_drink")
) WITH (
  OIDS=FALSE
);



CREATE TABLE MainDishPairing (
    "id" serial NOT NULL,
    "main_dish_id" integer NOT NULL,
    "additional_dish_id" integer,
    CONSTRAINT "MainDishPairing_pk" PRIMARY KEY ("id")
) WITH (
    OIDS=FALSE
);

CREATE TABLE WeightLossGoals (
  "goal_id" serial NOT NULL,
  "phone_number" bigint NOT NULL,
  "current_weight" FLOAT NOT NULL,
  "target_weight" FLOAT NOT NULL,
  "target_caloric_deficit" FLOAT NOT NULL,
  "estimated_completion_time" INTERVAL,
  "caloric_deficit_rate" FLOAT,
  CONSTRAINT "WeightLossGoals_pk" PRIMARY KEY ("goal_id"),
  
) WITH (
  OIDS=FALSE
);

CREATE TABLE WeightLossProgress (
  "progress_id" serial NOT NULL,
  "goal_id" integer NOT NULL,
  "date" DATE NOT NULL,
  "current_weight" FLOAT NOT NULL,
  "caloric_intake" FLOAT,
  CONSTRAINT "WeightLossProgress_pk" PRIMARY KEY ("progress_id"),
) WITH (
  OIDS=FALSE
);

ALTER TABLE UserAllergy ADD CONSTRAINT "UserAllergy_fk0" FOREIGN KEY ("number_phone") REFERENCES User("phone_number") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE UserSelectedProduct ADD CONSTRAINT "UserSelectedProduct_fk0" FOREIGN KEY ("phone_number") REFERENCES User("phone_number") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE UserSelectedProduct ADD CONSTRAINT "UserSelectedProduct_fk1" FOREIGN KEY ("product_id") REFERENCES Products("product_id")ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE MealIngredients ADD CONSTRAINT "MealIngredients_fk0" FOREIGN KEY ("meal_id") REFERENCES MealOption("id_option")ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE MealIngredients ADD CONSTRAINT "MealIngredients_fk1" FOREIGN KEY ("ingredient_id") REFERENCES Ingredients("id_ingredients")ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE MealIngredientsDrink ADD CONSTRAINT "MealIngredientsDrink_fk0" FOREIGN KEY ("id_drink") REFERENCES Drinks("id_drink")ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE MealIngredientsDrink ADD CONSTRAINT "MealIngredientsDrink_fk1" FOREIGN KEY ("id_ingredients") REFERENCES Ingredients("id_ingredients")ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE UserAllergy ADD CONSTRAINT fk_userallergy_ingredients FOREIGN KEY ("id_ingredients") REFERENCES Ingredients ("id_ingredients") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABL MainDishPairing  ADD CONSTRAINT "MainDishPairing_fk_main_dish" FOREIGN KEY ("main_dish_id") REFERENCES MealOption("id_option") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE MainDishPairing ADD  CONSTRAINT "MainDishPairing_fk_additional_dish" FOREIGN KEY ("additional_dish_id") REFERENCES MealOption("id_option") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE WeightLossGoals ADD  CONSTRAINT "WeightLossGoals_fk0" FOREIGN KEY ("phone_number") REFERENCES Users("phone_number") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE WeightLossProgress ADD CONSTRAINT "WeightLossProgress_fk0" FOREIGN KEY ("goal_id") REFERENCES WeightLossGoals("goal_id") ON UPDATE CASCADE ON DELETE CASCADE;


