CREATE TABLE "btcategory" (
  "id" SERIAL PRIMARY KEY,
  "name" TEXT NOT NULL
);

CREATE TABLE "btdeliveryadress" (
  "id" SERIAL PRIMARY KEY,
  "adress" TEXT NOT NULL,
  "town" TEXT NOT NULL,
  "zip_code" TEXT NOT NULL,
  "name" TEXT NOT NULL,
  "surname" TEXT NOT NULL,
  "contact_phone" TEXT NOT NULL
);

CREATE TABLE "btdeliverytype" (
  "id" SERIAL PRIMARY KEY,
  "name" TEXT NOT NULL,
  "price" DOUBLE PRECISION NOT NULL,
  "time" TEXT NOT NULL
);

CREATE TABLE "btorderstate" (
  "id" SERIAL PRIMARY KEY,
  "name" TEXT NOT NULL,
  "description" TEXT NOT NULL
);

CREATE TABLE "btorderstatehistory" (
  "id" SERIAL PRIMARY KEY,
  "lmod" TIMESTAMP NOT NULL,
  "name" TEXT NOT NULL,
  "description" TEXT NOT NULL
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
  "name" TEXT NOT NULL,
  "lmod" TIMESTAMP NOT NULL
);

CREATE TABLE "btuser" (
  "id" SERIAL PRIMARY KEY,
  "login" TEXT NOT NULL,
  "email" TEXT NOT NULL,
  "password" TEXT NOT NULL,
  "added_on" TIMESTAMP NOT NULL,
  "is_online" BOOLEAN NOT NULL,
  "lmod" TIMESTAMP NOT NULL,
  "user_role" INTEGER NOT NULL
);

CREATE INDEX "idx_btuser__user_role" ON "btuser" ("user_role");

ALTER TABLE "btuser" ADD CONSTRAINT "fk_btuser__user_role" FOREIGN KEY ("user_role") REFERENCES "btuserrole" ("id");

CREATE TABLE "btdeliveryadressrow" (
  "id" INTEGER NOT NULL,
  "user_id" INTEGER,
  "delivery_adress_id" INTEGER NOT NULL,
  PRIMARY KEY ("id", "delivery_adress_id")
);

CREATE INDEX "idx_btdeliveryadressrow__delivery_adress_id" ON "btdeliveryadressrow" ("delivery_adress_id");

CREATE INDEX "idx_btdeliveryadressrow__user_id" ON "btdeliveryadressrow" ("user_id");

ALTER TABLE "btdeliveryadressrow" ADD CONSTRAINT "fk_btdeliveryadressrow__delivery_adress_id" FOREIGN KEY ("delivery_adress_id") REFERENCES "btdeliveryadress" ("id");

ALTER TABLE "btdeliveryadressrow" ADD CONSTRAINT "fk_btdeliveryadressrow__user_id" FOREIGN KEY ("user_id") REFERENCES "btuser" ("id");

CREATE TABLE "btorder" (
  "id" SERIAL PRIMARY KEY,
  "delivery_type" INTEGER,
  "payment_method" INTEGER,
  "addedon" TIMESTAMP NOT NULL,
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
  "order" INTEGER,
  "name" TEXT NOT NULL,
  "description" TEXT NOT NULL,
  "order_state_id" INTEGER
);

CREATE INDEX "idx_btactualorderstate__order" ON "btactualorderstate" ("order");

CREATE INDEX "idx_btactualorderstate__order_state_id" ON "btactualorderstate" ("order_state_id");

ALTER TABLE "btactualorderstate" ADD CONSTRAINT "fk_btactualorderstate__order" FOREIGN KEY ("order") REFERENCES "btorder" ("id");

ALTER TABLE "btactualorderstate" ADD CONSTRAINT "fk_btactualorderstate__order_state_id" FOREIGN KEY ("order_state_id") REFERENCES "btorderstate" ("id");

CREATE TABLE "btorderhistoryrow" (
  "actual_order_state_id" INTEGER NOT NULL,
  "order_state_history_id" INTEGER NOT NULL,
  PRIMARY KEY ("actual_order_state_id", "order_state_history_id")
);

CREATE INDEX "idx_btorderhistoryrow__order_state_history_id" ON "btorderhistoryrow" ("order_state_history_id");

ALTER TABLE "btorderhistoryrow" ADD CONSTRAINT "fk_btorderhistoryrow__actual_order_state_id" FOREIGN KEY ("actual_order_state_id") REFERENCES "btactualorderstate" ("id");

ALTER TABLE "btorderhistoryrow" ADD CONSTRAINT "fk_btorderhistoryrow__order_state_history_id" FOREIGN KEY ("order_state_history_id") REFERENCES "btorderstatehistory" ("id");

CREATE TABLE "btorderitem" (
  "id" SERIAL PRIMARY KEY,
  "amount" INTEGER NOT NULL,
  "product" INTEGER NOT NULL,
  "order" INTEGER NOT NULL
);

CREATE INDEX "idx_btorderitem__order" ON "btorderitem" ("order");

CREATE INDEX "idx_btorderitem__product" ON "btorderitem" ("product");

ALTER TABLE "btorderitem" ADD CONSTRAINT "fk_btorderitem__order" FOREIGN KEY ("order") REFERENCES "btorder" ("id");

ALTER TABLE "btorderitem" ADD CONSTRAINT "fk_btorderitem__product" FOREIGN KEY ("product") REFERENCES "btproduct" ("id")