ALTER TABLE user_interests DROP COLUMN IF EXISTS topic_id;
DROP TABLE IF EXISTS card_topics;
DROP TABLE IF EXISTS topics;

INSERT INTO users (email, password, nickname, role, active, created_at, updated_at) VALUES
('kim@example.com', 'dummy-password', '이지훈', 'USER', true, now(), now()),
('lee@example.com', 'dummy-password', '이서연', 'USER', true, now(), now()),
('admin@example.com', 'dummy-password', '관리자', 'ADMIN', true, now(), now()),
('insight.pro@example.com', 'dummy-password', 'Insight Pro', 'USER', true, now(), now()),
('battery.insight@example.com', 'dummy-password', 'Battery Insight', 'USER', true, now(), now()),
('fx.research@example.com', 'dummy-password', 'FX Research', 'USER', true, now(), now())
ON CONFLICT (email) DO NOTHING;

UPDATE users SET nickname = '이지훈' WHERE email = 'kim@example.com';

INSERT INTO categories (name, slug, sort_order, active, created_at, updated_at) VALUES
('산업·기업', 'industry', 1, true, now(), now()),
('거시·경제', 'macro', 2, true, now(), now()),
('테크·AI', 'tech-ai', 3, true, now(), now()),
('소비자 트렌드', 'consumer-trend', 4, true, now(), now()),
('전기차', 'ev', 5, true, now(), now()),
('반도체', 'semiconductor', 6, true, now(), now()),
('에너지', 'energy', 7, true, now(), now())
ON CONFLICT (slug) DO NOTHING;

UPDATE categories SET name = '거시경제' WHERE slug = 'macro';
UPDATE categories SET active = false WHERE slug = 'consumer-trend';

INSERT INTO creator_profiles (
    user_id, verification_status, bio, total_published_count, total_evaluated_count,
    invalid_count, total_score, average_score, grade, created_at, updated_at
)
SELECT u.id, 'VERIFIED', b.bio, 0, 0, 0, 0, 0, 'UNRATED', now(), now()
FROM (VALUES
    ('kim@example.com', '반도체·전기차 산업 분석가'),
    ('lee@example.com', '거시경제 전문 애널리스트'),
    ('admin@example.com', '테크·AI 트렌드 리서처'),
    ('insight.pro@example.com', '산업·기업 섹터 심층 분석 리서치'),
    ('battery.insight@example.com', '배터리·소재 밸류체인 전문 리서치'),
    ('fx.research@example.com', '외환·원자재 시장 분석 전문')
) AS b(email, bio)
JOIN users u ON u.email = b.email
ON CONFLICT (user_id) DO NOTHING;

-- 카드 발행 몰림 방지: 일부 카드를 신규 더미 작성자로 재배정
UPDATE cards SET author_id = (SELECT id FROM users WHERE email = 'insight.pro@example.com')
WHERE claim LIKE 'HBM4 메모리 시장에서%';

UPDATE cards SET author_id = (SELECT id FROM users WHERE email = 'battery.insight@example.com')
WHERE claim LIKE '리튬 가격 하락과 LFP%';

UPDATE cards SET author_id = (SELECT id FROM users WHERE email = 'fx.research@example.com')
WHERE claim LIKE 'OPEC+의 감산 기조%';

-- 작성자 재배정에 맞춰 발행 카드 수 재계산
UPDATE creator_profiles cp
SET total_published_count = (
    SELECT count(*) FROM cards c WHERE c.author_id = cp.user_id AND c.status = 'PUBLISHED'
);
