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

CREATE TABLE "btpaymentmethod" (
  "id" SERIAL PRIMARY KEY,
  "name" TEXT NOT NULL
);

CREATE TABLE "btorder" (
  "id" SERIAL PRIMARY KEY,
  "delivery_type" INTEGER,
  "payment_method" INTEGER,
  "addedon" TIMESTAMP NOT NULL,
  "lmod" TIMESTAMP NOT NULL
);

CREATE INDEX "idx_btorder__delivery_type" ON "btorder" ("delivery_type");

CREATE INDEX "idx_btorder__payment_method" ON "btorder" ("payment_method");

ALTER TABLE "btorder" ADD CONSTRAINT "fk_btorder__delivery_type" FOREIGN KEY ("delivery_type") REFERENCES "btdeliverytype" ("id");

ALTER TABLE "btorder" ADD CONSTRAINT "fk_btorder__payment_method" FOREIGN KEY ("payment_method") REFERENCES "btpaymentmethod" ("id");

CREATE TABLE "btactualorderstate" (
  "id" SERIAL PRIMARY KEY,
  "lmod" TIMESTAMP NOT NULL,
  "order" INTEGER
);

CREATE INDEX "idx_btactualorderstate__order" ON "btactualorderstate" ("order");

ALTER TABLE "btactualorderstate" ADD CONSTRAINT "fk_btactualorderstate__order" FOREIGN KEY ("order") REFERENCES "btorder" ("id");

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

CREATE TABLE "OrderItem" (
  "id" SERIAL PRIMARY KEY,
  "amount" INTEGER NOT NULL,
  "product" INTEGER NOT NULL,
  "order" INTEGER NOT NULL
);

CREATE INDEX "idx_orderitem__order" ON "OrderItem" ("order");

CREATE INDEX "idx_orderitem__product" ON "OrderItem" ("product");

ALTER TABLE "OrderItem" ADD CONSTRAINT "fk_orderitem__order" FOREIGN KEY ("order") REFERENCES "btorder" ("id");

ALTER TABLE "OrderItem" ADD CONSTRAINT "fk_orderitem__product" FOREIGN KEY ("product") REFERENCES "btproduct" ("id");

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