
-- Создание ENUM с возможными вариантами
CREATE TYPE ActivityLevel AS ENUM ('Low', 'Medium', 'High');

CREATE TYPE MealType AS ENUM ('Breakfast', 'Lunch', 'Dinner');


CREATE TABLE "public.User" (
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



CREATE TABLE "public.UserAllergy" (
	"id" serial NOT NULL,
	"number_phone" bigint NOT NULL,
	"type_meal" VARCHAR(50) NOT NULL,
	CONSTRAINT "UserAllergy_pk" PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "public.Products" (
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



CREATE TABLE "public.UserSelectedProduct" (
	"id" serial NOT NULL,
	"phone_number" bigint NOT NULL,
	"product_id" integer NOT NULL,
	"grams" float NOT NULL
	CONSTRAINT "UserSelectedProduct_pk" PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);


CREATE TABLE "public.MealOption" (
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
SELECT * FROM "public.MealOption"
WHERE "type_meal" = 'Breakfast';


CREATE VIEW LunchOptions AS
SELECT * FROM "public.MealOption"
WHERE "type_meal" = 'Lunch';


CREATE VIEW DinnerOptions AS
SELECT * FROM "public.MealOption"
WHERE "type_meal" = 'Dinner';

CREATE TABLE "public.UserSelectedMenu" (
  "id" serial NOT NULL,
  "phone_number" bigint NOT NULL,
  "breakfast_id" integer REFERENCES "public.MealOption"("id_option"),
  "breakfast_drink_id" integer REFERENCES "public.Drinks"("id_drink"),
  "lunch_id" integer REFERENCES "public.MealOption"("id_option"),
  "lunch_drink_id" integer REFERENCES "public.Drinks"("id_drink"),
  "dinner_id" integer REFERENCES "public.MealOption"("id_option"),
  "dinner_drink_id" integer REFERENCES "public.Drinks"("id_drink"),
  CONSTRAINT "UserSelectedMenu_pk" PRIMARY KEY ("id"),
  CONSTRAINT "UserSelectedMenu_fk0" FOREIGN KEY ("phone_number") REFERENCES "public.User"("phone_number")
) WITH (
  OIDS=FALSE
);


CREATE TABLE "public.Drinks" (
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


CREATE TABLE "public.Ingredients" (
	"id_ingredients" serial NOT NULL,
	"name_ingredients" varchar(50) NOT NULL,
	CONSTRAINT "Ingredients_pk" PRIMARY KEY ("id_ingredients")
) WITH (
  OIDS=FALSE
);


CREATE TABLE "public.MealIngredients" (
	"id_mi" serial NOT NULL,
	"meal_id" integer NOT NULL,
	"ingredient_id" integer NOT NULL,
	CONSTRAINT "MealIngredients_pk" PRIMARY KEY ("id_mi")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "public.MealIngredientsDrink" (
	"id_ingredient_drink" serial NOT NULL,
	"id_drink" integer NOT NULL,
	"id_ingredients" integer NOT NULL,
	CONSTRAINT "MealIngredientsDrink_pk" PRIMARY KEY ("id_ingredient_drink")
) WITH (
  OIDS=FALSE
);



ALTER TABLE "public.UserAllergy" ADD CONSTRAINT "UserAllergy_fk0" FOREIGN KEY ("number_phone") REFERENCES "public.User"("phone_number") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE "public.UserSelectedProduct" ADD CONSTRAINT "UserSelectedProduct_fk0" FOREIGN KEY ("phone_number") REFERENCES "public.User"("phone_number") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "public.UserSelectedProduct" ADD CONSTRAINT "UserSelectedProduct_fk1" FOREIGN KEY ("product_id") REFERENCES "public.Products"("product_id")ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE "public.MealIngredients" ADD CONSTRAINT "MealIngredients_fk0" FOREIGN KEY ("meal_id") REFERENCES "public.MealOption"("id_option")ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "public.MealIngredients" ADD CONSTRAINT "MealIngredients_fk1" FOREIGN KEY ("ingredient_id") REFERENCES "public.Ingredients"("id_ingredients")ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE "public.MealIngredientsDrink" ADD CONSTRAINT "MealIngredientsDrink_fk0" FOREIGN KEY ("id_drink") REFERENCES "public.Drinks"("id_drink")ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "public.MealIngredientsDrink" ADD CONSTRAINT "MealIngredientsDrink_fk1" FOREIGN KEY ("id_ingredients") REFERENCES "public.Ingredients"("id_ingredients")ON UPDATE CASCADE ON DELETE CASCADE;

