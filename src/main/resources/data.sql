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

INSERT INTO pagamento (id, id_cliente, valor, moeda, metodo_pagamento, origem, destino, created_at)
VALUES (
    '660e8400-e29b-41d4-a716-446655440000',
    'cliente-teste-001',
    '100.00',
    'BRL',
    'PIX',
    'conta-origem-001',
    'conta-destino-001',
    now()
)
ON CONFLICT (id) DO NOTHING;

INSERT INTO status_pagamento (id, id_pagamento, status, mensagem, created_at)
VALUES (
    '770e8400-e29b-41d4-a716-446655440000',
    '660e8400-e29b-41d4-a716-446655440000',
    'CREATED',
    'Pagamento criado com sucesso',
    now()
)
ON CONFLICT (id) DO NOTHING;

INSERT INTO status_pagamento (id, id_pagamento, status, mensagem, created_at)
VALUES (
    '770e8400-e29b-41d4-a716-446655440001',
    '660e8400-e29b-41d4-a716-446655440000',
    'PROCESSANDO',
    'Pagamento em processamento',
    now()
)
ON CONFLICT (id) DO NOTHING;

INSERT INTO status_pagamento (id, id_pagamento, status, mensagem, created_at)
VALUES (
    '770e8400-e29b-41d4-a716-446655440002',
    '660e8400-e29b-41d4-a716-446655440000',
    'CONCLUIDO',
    'Pagamento concluído com sucesso',
    now()
)
ON CONFLICT (id) DO NOTHING;
