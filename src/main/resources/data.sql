-- Roda automaticamente toda vez que a aplicação sobe (spring.sql.init.mode=always).
-- ON CONFLICT DO NOTHING evita erro de constraint única em reinicializações repetidas.

INSERT INTO idempotency_key (id, chave, status, created_at, updated_at)
VALUES (
    '550e8400-e29b-41d4-a716-446655440000',
    'teste-concluido-001',
    'CONCLUIDO',
    now(),
    now()
)
ON CONFLICT (chave) DO NOTHING;

INSERT INTO idempotency_key (id, chave, status, created_at)
VALUES (
    '550e8400-e29b-41d4-a716-446655440001',
    'teste-processando-001',
    'PROCESSANDO',
    now()
)
ON CONFLICT (chave) DO NOTHING;
