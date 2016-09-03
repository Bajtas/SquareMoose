CREATE TABLE "btcategory" (
  "id" SERIAL PRIMARY KEY,
  "name" TEXT NOT NULL
);

CREATE TABLE "btorder" (
  "id" SERIAL PRIMARY KEY
);

CREATE TABLE "btproduct" (
  "id" SERIAL PRIMARY KEY,
  "name" TEXT NOT NULL,
  "cost" DOUBLE PRECISION NOT NULL,
  "category" INTEGER,
  "description" TEXT NOT NULL,
  "added_on" TIMESTAMP NOT NULL,
  "lmod" TIMESTAMP NOT NULL
);

CREATE INDEX "idx_btproduct__category" ON "btproduct" ("category");

ALTER TABLE "btproduct" ADD CONSTRAINT "fk_btproduct__category" FOREIGN KEY ("category") REFERENCES "btcategory" ("id");

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

ALTER TABLE "btimage" ADD CONSTRAINT "fk_btimage__product_id" FOREIGN KEY ("product_id") REFERENCES "btproduct" ("id");

CREATE TABLE "orderitem" (
  "id" SERIAL PRIMARY KEY,
  "amount" INTEGER NOT NULL,
  "product" INTEGER NOT NULL,
  "b_t_order" INTEGER NOT NULL
);

CREATE INDEX "idx_orderitem__b_t_order" ON "orderitem" ("b_t_order");

CREATE INDEX "idx_orderitem__product" ON "orderitem" ("product");

ALTER TABLE "orderitem" ADD CONSTRAINT "fk_orderitem__b_t_order" FOREIGN KEY ("b_t_order") REFERENCES "btorder" ("id");

ALTER TABLE "orderitem" ADD CONSTRAINT "fk_orderitem__product" FOREIGN KEY ("product") REFERENCES "btproduct" ("id")