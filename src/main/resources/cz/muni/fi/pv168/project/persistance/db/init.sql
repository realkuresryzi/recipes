--
-- Category table definition
--
CREATE TABLE IF NOT EXISTS Category
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    name
    VARCHAR
(
    250
) NOT NULL UNIQUE,
    description VARCHAR
(
    250
) NOT NULL,
    color INT,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS BaseUnit
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    name
    VARCHAR
(
    250
) NOT NULL UNIQUE,
    abbreviation VARCHAR
(
    250
) NOT NULL,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

--
-- Unit table definition
--
CREATE TABLE IF NOT EXISTS Unit
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    name
    VARCHAR
(
    250
) NOT NULL UNIQUE,
    abbreviation VARCHAR
(
    250
) NOT NULL,
    convertionFactor INT,
    baseUnitID BIGINT REFERENCES BaseUnit
(
    id
),
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

--
-- Unit table definition
--
CREATE TABLE IF NOT EXISTS Ingredient
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    name
    VARCHAR
(
    250
) NOT NULL UNIQUE,
    nutritionalValue INT,
    unitID BIGINT REFERENCES Unit
(
    id
),
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

--
-- Unit table definition
--
CREATE TABLE IF NOT EXISTS Recipe
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    name
    VARCHAR
(
    250
) NOT NULL,
    description VARCHAR
(
    250
) NOT NULL,
    instructions VARCHAR
(
    250
) NOT NULL,
    categoryID BIGINT REFERENCES Category
(
    id
),
    portions INT,
    duration INT,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

--
-- Unit table definition
--
CREATE TABLE IF NOT EXISTS IngredientRecipe
(
    ingredientID BIGINT REFERENCES Ingredient
(
    id
),
    recipeID BIGINT REFERENCES Recipe
(
    id
),
    amount INT,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

MERGE INTO BaseUnit
    (name, abbreviation)
    KEY(name)
    VALUES ('Gram', 'g'),
           ('Liter', 'l'),
           ('Cup', 'cup'),
           ('Piece', 'pc'),
           ('Pound', 'lb');

MERGE INTO Unit
    (name, abbreviation, convertionFactor, baseUnitID)
    KEY(name)
    VALUES ('Gram', 'g', 1, 1),
            ('Liter', 'l', 1, 2),
            ('Cup', 'cup', 1, 3),
            ('Piece', 'pc', 1, 4),
            ('Pound', 'lb', 1, 5);


