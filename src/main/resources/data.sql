INSERT INTO users (email, password, nickname, role, active, created_at, updated_at) VALUES
('kim@example.com', 'dummy-password', '김지훈', 'USER', true, now(), now()),
('lee@example.com', 'dummy-password', '이서연', 'USER', true, now(), now()),
('admin@example.com', 'dummy-password', '관리자', 'ADMIN', true, now(), now())
ON CONFLICT (email) DO NOTHING;

INSERT INTO categories (name, slug, sort_order, active, created_at, updated_at) VALUES
('산업·기업', 'industry', 1, true, now(), now()),
('거시·경제', 'macro', 2, true, now(), now()),
('테크·AI', 'tech-ai', 3, true, now(), now()),
('소비자 트렌드', 'consumer-trend', 4, true, now(), now())
ON CONFLICT (slug) DO NOTHING;

INSERT INTO topics (category_id, name, slug, active, created_at, updated_at)
SELECT c.id, t.name, t.slug, true, now(), now()
FROM (VALUES
    ('industry', '반도체', 'semiconductor'),
    ('industry', '전기차', 'ev'),
    ('macro', '금리', 'interest-rate'),
    ('macro', '환율', 'fx'),
    ('tech-ai', 'AI 에이전트', 'ai-agent')
) AS t(category_slug, name, slug)
JOIN categories c ON c.slug = t.category_slug
ON CONFLICT (slug) DO NOTHING;

INSERT INTO creator_profiles (
    user_id, verification_status, bio, total_published_count, total_evaluated_count,
    invalid_count, total_score, average_score, grade, created_at, updated_at
)
SELECT u.id, 'VERIFIED', b.bio, 0, 0, 0, 0, 0, 'UNRATED', now(), now()
FROM (VALUES
    ('kim@example.com', '반도체·전기차 산업 분석가'),
    ('lee@example.com', '거시경제 전문 애널리스트'),
    ('admin@example.com', '테크·AI 트렌드 리서처')
) AS b(email, bio)
JOIN users u ON u.email = b.email
ON CONFLICT (user_id) DO NOTHING;
