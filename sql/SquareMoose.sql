CREATE TABLE "btcategory" (
  "id" SERIAL PRIMARY KEY,
  "name" TEXT NOT NULL
);

CREATE TABLE "btdeliverytype" (
  "id" SERIAL PRIMARY KEY,
  "name" TEXT NOT NULL,
  "price" DOUBLE PRECISION NOT NULL,
  "time" TEXT NOT NULL
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

CREATE TABLE "btorder" (
  "id" SERIAL PRIMARY KEY,
  "delivery_type" INTEGER,
  "payment_method" INTEGER,
  "addedon" TIMESTAMP NOT NULL,
  "lmod" TIMESTAMP NOT NULL,
  "full_price" DOUBLE PRECISION NOT NULL,
  "items_amount" INTEGER NOT NULL
);

CREATE INDEX "idx_btorder__delivery_type" ON "btorder" ("delivery_type");

CREATE INDEX "idx_btorder__payment_method" ON "btorder" ("payment_method");

ALTER TABLE "btorder" ADD CONSTRAINT "fk_btorder__delivery_type" FOREIGN KEY ("delivery_type") REFERENCES "btdeliverytype" ("id");

ALTER TABLE "btorder" ADD CONSTRAINT "fk_btorder__payment_method" FOREIGN KEY ("payment_method") REFERENCES "btpaymentmethod" ("id");

CREATE TABLE "btactualorderstate" (
  "id" SERIAL PRIMARY KEY,
  "lmod" TIMESTAMP NOT NULL,
  "order" INTEGER,
  "name" TEXT NOT NULL,
  "description" TEXT NOT NULL
);

CREATE INDEX "idx_btactualorderstate__order" ON "btactualorderstate" ("order");

ALTER TABLE "btactualorderstate" ADD CONSTRAINT "fk_btactualorderstate__order" FOREIGN KEY ("order") REFERENCES "btorder" ("id");

CREATE TABLE "btorderhistoryrow" (
  "actual_order_state_id" INTEGER NOT NULL,
  "order_state_history_id" INTEGER NOT NULL,
  PRIMARY KEY ("actual_order_state_id", "order_state_history_id")
);

CREATE INDEX "idx_btorderhistoryrow__order_state_history_id" ON "btorderhistoryrow" ("order_state_history_id");

ALTER TABLE "btorderhistoryrow" ADD CONSTRAINT "fk_btorderhistoryrow__actual_order_state_id" FOREIGN KEY ("actual_order_state_id") REFERENCES "btactualorderstate" ("id");

ALTER TABLE "btorderhistoryrow" ADD CONSTRAINT "fk_btorderhistoryrow__order_state_history_id" FOREIGN KEY ("order_state_history_id") REFERENCES "btorderstatehistory" ("id");

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

CREATE TABLE "btorderitem" (
  "id" SERIAL PRIMARY KEY,
  "amount" INTEGER NOT NULL,
  "product" INTEGER NOT NULL,
  "order" INTEGER NOT NULL
);

CREATE INDEX "idx_btorderitem__order" ON "btorderitem" ("order");

CREATE INDEX "idx_btorderitem__product" ON "btorderitem" ("product");

ALTER TABLE "btorderitem" ADD CONSTRAINT "fk_btorderitem__order" FOREIGN KEY ("order") REFERENCES "btorder" ("id");

ALTER TABLE "btorderitem" ADD CONSTRAINT "fk_btorderitem__product" FOREIGN KEY ("product") REFERENCES "btproduct" ("id");

CREATE TABLE "btproductimage" (
  "id" SERIAL PRIMARY KEY,
  "image_src" TEXT NOT NULL
);

CREATE TABLE "btimage" (
  "product_id" INTEGER NOT NULL,
  "image_id" INTEGER NOT NULL,
  PRIMARY KEY ("product_id", "image_id")
);

CREATE INDEX "idx_btimage__image_id" ON "btimage" ("image_id");

ALTER TABLE "btimage" ADD CONSTRAINT "fk_btimage__image_id" FOREIGN KEY ("image_id") REFERENCES "btproductimage" ("id");

ALTER TABLE "btimage" ADD CONSTRAINT "fk_btimage__product_id" FOREIGN KEY ("product_id") REFERENCES "btproduct" ("id")