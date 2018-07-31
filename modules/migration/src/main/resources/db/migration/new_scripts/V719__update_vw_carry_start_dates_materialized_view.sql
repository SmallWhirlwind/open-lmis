
DO $do$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relkind = 'm' and relname = 'vw_carry_start_dates_tmp') THEN
    CREATE MATERIALIZED VIEW vw_carry_start_dates_tmp AS
    SELECT
    uuid_generate_v4() AS uuid,

    facilities.name                     AS facility_name,
    facilities.code                     AS facility_code,
    ZONE.name                           AS district_name,
    ZONE.code                           AS district_code,
    parent_zone.name                    AS province_name,
    parent_zone.code                    AS province_code,
    products.code                       AS drug_code,
    products.primaryname                AS drug_name,
    facilities.golivedate               AS facility_golive_date,
    facilities.godowndate               AS facility_godown_date,
    first_movement_date(stock_cards.id) AS carry_start_date
    FROM stock_cards
    JOIN facilities ON stock_cards.facilityid = facilities.id
    JOIN products ON stock_cards.productid = products.id
    JOIN geographic_zones AS ZONE ON facilities.geographiczoneid = ZONE.id
    JOIN geographic_zones AS parent_zone ON ZONE.parentid = parent_zone.id
  ORDER BY facility_code, carry_start_date;
  END IF;
END
$do$;

DROP MATERIALIZED VIEW vw_carry_start_dates;

ALTER MATERIALIZED VIEW vw_carry_start_dates_tmp RENAME TO vw_carry_start_dates;

CREATE UNIQUE INDEX idx_vw_carry_start_dates ON vw_carry_start_dates (uuid);