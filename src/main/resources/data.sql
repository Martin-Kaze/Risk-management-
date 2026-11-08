-- Sample data, loaded automatically at startup (H2 in-memory DB).
-- API keys are demo values only, not real secrets.

-- Two organizations (tenants), each with its own thresholds
INSERT INTO organization (id, name, api_key, review_threshold, block_threshold) VALUES
  ('ORG_A', 'Shop A', 'demo-key-a', 40, 70),
  ('ORG_B', 'Shop B', 'demo-key-b', 25, 40);

-- ORG_A / client C1: 10 payments of 40 EUR, always to R-12 (scenario 1: normal payment)
INSERT INTO payment_record (organization_id, client_id, recipient_id, country, amount_eur, timestamp, decision, score) VALUES
  ('ORG_A', 'C1', 'R-12', 'LT', 40.00, '2026-11-01 10:00:00', 'APPROVE', 0),
  ('ORG_A', 'C1', 'R-12', 'LT', 40.00, '2026-11-02 10:00:00', 'APPROVE', 0),
  ('ORG_A', 'C1', 'R-12', 'LT', 40.00, '2026-11-03 10:00:00', 'APPROVE', 0),
  ('ORG_A', 'C1', 'R-12', 'LT', 40.00, '2026-11-04 10:00:00', 'APPROVE', 0),
  ('ORG_A', 'C1', 'R-12', 'LT', 40.00, '2026-11-05 10:00:00', 'APPROVE', 0),
  ('ORG_A', 'C1', 'R-12', 'LT', 40.00, '2026-11-06 10:00:00', 'APPROVE', 0),
  ('ORG_A', 'C1', 'R-12', 'LT', 40.00, '2026-11-07 10:00:00', 'APPROVE', 0),
  ('ORG_A', 'C1', 'R-12', 'LT', 40.00, '2026-11-08 10:00:00', 'APPROVE', 0),
  ('ORG_A', 'C1', 'R-12', 'LT', 40.00, '2026-11-09 10:00:00', 'APPROVE', 0),
  ('ORG_A', 'C1', 'R-12', 'LT', 40.00, '2026-11-09 18:00:00', 'APPROVE', 0);

-- ORG_A / client C2: 8 payments of 300 EUR to R-10, average 300 (scenario 2: boundary case)
INSERT INTO payment_record (organization_id, client_id, recipient_id, country, amount_eur, timestamp, decision, score) VALUES
  ('ORG_A', 'C2', 'R-10', 'LT', 300.00, '2026-11-01 12:00:00', 'APPROVE', 0),
  ('ORG_A', 'C2', 'R-10', 'LT', 300.00, '2026-11-02 12:00:00', 'APPROVE', 0),
  ('ORG_A', 'C2', 'R-10', 'LT', 300.00, '2026-11-03 12:00:00', 'APPROVE', 0),
  ('ORG_A', 'C2', 'R-10', 'LT', 300.00, '2026-11-04 12:00:00', 'APPROVE', 0),
  ('ORG_A', 'C2', 'R-10', 'LT', 300.00, '2026-11-05 12:00:00', 'APPROVE', 0),
  ('ORG_A', 'C2', 'R-10', 'LT', 300.00, '2026-11-06 12:00:00', 'APPROVE', 0),
  ('ORG_A', 'C2', 'R-10', 'LT', 300.00, '2026-11-07 12:00:00', 'APPROVE', 0),
  ('ORG_A', 'C2', 'R-10', 'LT', 300.00, '2026-11-08 12:00:00', 'APPROVE', 0);

-- ORG_B / client C2: same client ID, but ORG_B's own separate history
INSERT INTO payment_record (organization_id, client_id, recipient_id, country, amount_eur, timestamp, decision, score) VALUES
  ('ORG_B', 'C2', 'R-50', 'LT', 100.00, '2026-11-05 12:00:00', 'APPROVE', 0),
  ('ORG_B', 'C2', 'R-50', 'LT', 100.00, '2026-11-06 12:00:00', 'APPROVE', 0);
