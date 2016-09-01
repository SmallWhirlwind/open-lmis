CREATE OR REPLACE FUNCTION refresh_stockouts()
  RETURNS INT LANGUAGE plpgsql
AS $$
BEGIN
  REFRESH MATERIALIZED VIEW CONCURRENTLY vw_stockouts;
  RETURN 1;
END $$;

CREATE OR REPLACE FUNCTION refresh_start_carry_view()
  RETURNS INT LANGUAGE plpgsql
AS $$
BEGIN
  REFRESH MATERIALIZED VIEW CONCURRENTLY vw_carry_start_dates;
  RETURN 1;
END $$;

CREATE OR REPLACE FUNCTION refresh_weekly_tracer_soh()
  RETURNS INT LANGUAGE plpgsql
AS $$
BEGIN
  REFRESH MATERIALIZED VIEW CONCURRENTLY vw_weekly_tracer_soh;
  RETURN 1;
END $$;

CREATE OR REPLACE FUNCTION refresh_daily_full_soh()
  RETURNS INT LANGUAGE plpgsql
AS $$
BEGIN
  REFRESH MATERIALIZED VIEW CONCURRENTLY vw_daily_full_soh;
  RETURN 1;
END $$;