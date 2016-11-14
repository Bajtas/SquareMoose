CREATE TABLE "btcategory" (
  "id" SERIAL PRIMARY KEY,
  "name" TEXT NOT NULL
);

CREATE TABLE "btdeliveryadress" (
  "id" SERIAL PRIMARY KEY,
  "address" TEXT NOT NULL,
  "town" TEXT NOT NULL,
  "zip_code" TEXT NOT NULL,
  "name" TEXT NOT NULL,
  "surname" TEXT NOT NULL,
  "contact_phone" TEXT NOT NULL,
  "currently_assigned" BOOLEAN NOT NULL
);

CREATE TABLE "btdeliverytype" (
  "id" SERIAL PRIMARY KEY,
  "name" TEXT NOT NULL,
  "price" DECIMAL(12, 2) NOT NULL,
  "time" TEXT NOT NULL,
  "image_src" TEXT NOT NULL
);

CREATE TABLE "btpaymentmethod" (
  "id" SERIAL PRIMARY KEY,
  "name" TEXT NOT NULL
);

CREATE TABLE "btproduct" (
  "id" SERIAL PRIMARY KEY,
  "name" TEXT NOT NULL,
  "price" DOUBLE PRECISION NOT NULL,
  "category" INTEGER,
  "description" TEXT NOT NULL,
  "added_on" TIMESTAMP NOT NULL,
  "lmod" TIMESTAMP NOT NULL
);

CREATE INDEX "idx_btproduct__category" ON "btproduct" ("category");

ALTER TABLE "btproduct" ADD CONSTRAINT "fk_btproduct__category" FOREIGN KEY ("category") REFERENCES "btcategory" ("id");

CREATE TABLE "btproductimage" (
  "id" SERIAL PRIMARY KEY,
  "image_src" TEXT NOT NULL,
  "added_on" TIMESTAMP NOT NULL
);

CREATE TABLE "btimage" (
  "product_id" INTEGER NOT NULL,
  "image_id" INTEGER NOT NULL,
  PRIMARY KEY ("product_id", "image_id")
);

CREATE INDEX "idx_btimage__image_id" ON "btimage" ("image_id");

ALTER TABLE "btimage" ADD CONSTRAINT "fk_btimage__image_id" FOREIGN KEY ("image_id") REFERENCES "btproductimage" ("id");

ALTER TABLE "btimage" ADD CONSTRAINT "fk_btimage__product_id" FOREIGN KEY ("product_id") REFERENCES "btproduct" ("id");

CREATE TABLE "btuserrole" (
  "id" SERIAL PRIMARY KEY,
  "name" TEXT UNIQUE NOT NULL,
  "lmod" TIMESTAMP NOT NULL
);

CREATE TABLE "btuser" (
  "id" SERIAL PRIMARY KEY,
  "login" TEXT UNIQUE NOT NULL,
  "email" TEXT UNIQUE NOT NULL,
  "password" TEXT NOT NULL,
  "added_on" TIMESTAMP NOT NULL,
  "is_online" BOOLEAN NOT NULL,
  "lmod" TIMESTAMP NOT NULL,
  "user_role" INTEGER NOT NULL
);

CREATE INDEX "idx_btuser__user_role" ON "btuser" ("user_role");

ALTER TABLE "btuser" ADD CONSTRAINT "fk_btuser__user_role" FOREIGN KEY ("user_role") REFERENCES "btuserrole" ("id");

CREATE TABLE "btdeliveryadressrow" (
  "id" SERIAL PRIMARY KEY,
  "user_id" INTEGER,
  "delivery_adress_id" INTEGER
);

CREATE INDEX "idx_btdeliveryadressrow__delivery_adress_id" ON "btdeliveryadressrow" ("delivery_adress_id");

CREATE INDEX "idx_btdeliveryadressrow__user_id" ON "btdeliveryadressrow" ("user_id");

ALTER TABLE "btdeliveryadressrow" ADD CONSTRAINT "fk_btdeliveryadressrow__delivery_adress_id" FOREIGN KEY ("delivery_adress_id") REFERENCES "btdeliveryadress" ("id");

ALTER TABLE "btdeliveryadressrow" ADD CONSTRAINT "fk_btdeliveryadressrow__user_id" FOREIGN KEY ("user_id") REFERENCES "btuser" ("id");

CREATE TABLE "btorder" (
  "id" SERIAL PRIMARY KEY,
  "delivery_type" INTEGER,
  "payment_method" INTEGER,
  "added_on" TIMESTAMP NOT NULL,
  "lmod" TIMESTAMP NOT NULL,
  "full_price" DOUBLE PRECISION NOT NULL,
  "items_amount" INTEGER NOT NULL,
  "delivery_adress" INTEGER,
  "user_id" INTEGER NOT NULL
);

CREATE INDEX "idx_btorder__delivery_adress" ON "btorder" ("delivery_adress");

CREATE INDEX "idx_btorder__delivery_type" ON "btorder" ("delivery_type");

CREATE INDEX "idx_btorder__payment_method" ON "btorder" ("payment_method");

CREATE INDEX "idx_btorder__user_id" ON "btorder" ("user_id");

ALTER TABLE "btorder" ADD CONSTRAINT "fk_btorder__delivery_adress" FOREIGN KEY ("delivery_adress") REFERENCES "btdeliveryadress" ("id");

ALTER TABLE "btorder" ADD CONSTRAINT "fk_btorder__delivery_type" FOREIGN KEY ("delivery_type") REFERENCES "btdeliverytype" ("id");

ALTER TABLE "btorder" ADD CONSTRAINT "fk_btorder__payment_method" FOREIGN KEY ("payment_method") REFERENCES "btpaymentmethod" ("id");

ALTER TABLE "btorder" ADD CONSTRAINT "fk_btorder__user_id" FOREIGN KEY ("user_id") REFERENCES "btuser" ("id");

CREATE TABLE "btactualorderstate" (
  "id" SERIAL PRIMARY KEY,
  "lmod" TIMESTAMP NOT NULL,
  "order_id" INTEGER NOT NULL,
  "name" TEXT NOT NULL,
  "description" TEXT NOT NULL
);

CREATE INDEX "idx_btactualorderstate__order_id" ON "btactualorderstate" ("order_id");

ALTER TABLE "btactualorderstate" ADD CONSTRAINT "fk_btactualorderstate__order_id" FOREIGN KEY ("order_id") REFERENCES "btorder" ("id");

CREATE TABLE "btorderitem" (
  "id" SERIAL PRIMARY KEY,
  "amount" INTEGER NOT NULL,
  "product_id" INTEGER NOT NULL,
  "order_id" INTEGER NOT NULL
);

CREATE INDEX "idx_btorderitem__order_id" ON "btorderitem" ("order_id");

CREATE INDEX "idx_btorderitem__product_id" ON "btorderitem" ("product_id");

ALTER TABLE "btorderitem" ADD CONSTRAINT "fk_btorderitem__order_id" FOREIGN KEY ("order_id") REFERENCES "btorder" ("id");

ALTER TABLE "btorderitem" ADD CONSTRAINT "fk_btorderitem__product_id" FOREIGN KEY ("product_id") REFERENCES "btproduct" ("id");

CREATE TABLE "btorderstatehistory" (
  "id" SERIAL PRIMARY KEY,
  "lmod" TIMESTAMP NOT NULL,
  "name" TEXT NOT NULL,
  "description" TEXT NOT NULL,
  "actual_order_state_id" INTEGER NOT NULL
);

CREATE INDEX "idx_btorderstatehistory__actual_order_state_id" ON "btorderstatehistory" ("actual_order_state_id");

ALTER TABLE "btorderstatehistory" ADD CONSTRAINT "fk_btorderstatehistory__actual_order_state_id" FOREIGN KEY ("actual_order_state_id") REFERENCES "btactualorderstate" ("id")