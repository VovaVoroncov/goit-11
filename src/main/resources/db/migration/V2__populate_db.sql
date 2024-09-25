INSERT INTO Client (name)
VALUES
    ('John Doe'),
    ('Jane Doe'),
    ('Bob Johnson'),
    ('Alice Lee'),
    ('John Carmack'),
    ('Emily Brown'),
    ('Gabe Newell'),
    ('Linda Chen'),
    ('David Kim'),
    ('Lisa Su');

INSERT INTO Planet (id, name)
VALUES
    ('ZEP', 'Zephyria'),
    ('ELY', 'Elysium'),
    ('VIR', 'Virelia'),
    ('SOL', 'Solara'),
    ('LUN', 'Lunaris');

INSERT INTO Ticket (created_at, client_id, from_planet_id, to_planet_id)
SELECT
    CURRENT_TIMESTAMP - INTERVAL '1' DAY * (RAND() * 30 + 1) AS created_at,
    c.id AS client_id,
    p1.id AS from_planet_id,
    p2.id AS to_planet_id
FROM
    Client c
    CROSS JOIN Planet p1
    CROSS JOIN Planet p2
WHERE
    p1.id != p2.id
ORDER BY
    RAND()
    LIMIT
    10;
