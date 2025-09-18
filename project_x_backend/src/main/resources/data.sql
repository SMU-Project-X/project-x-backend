-- ===== 새 Oracle 스키마에 맞는 초기 데이터 =====
-- 테이블은 이미 생성되어 있다고 가정하고 데이터만 삽입

-- 1. 카테고리 데이터 먼저 삽입 (products에서 FK 참조)
INSERT INTO categories (name) VALUES ('photobook');
INSERT INTO categories (name) VALUES ('goods');
INSERT INTO categories (name) VALUES ('sticker');
INSERT INTO categories (name) VALUES ('bag');
INSERT INTO categories (name) VALUES ('keyring');
INSERT INTO categories (name) VALUES ('cup');
INSERT INTO categories (name) VALUES ('pouch');
INSERT INTO categories (name) VALUES ('note');
INSERT INTO categories (name) VALUES ('badge');
INSERT INTO categories (name) VALUES ('pen');
INSERT INTO categories (name) VALUES ('postcard');
INSERT INTO categories (name) VALUES ('mousepad');
INSERT INTO categories (name) VALUES ('phonecase');
INSERT INTO categories (name) VALUES ('calendar');
INSERT INTO categories (name) VALUES ('bookmark');
INSERT INTO categories (name) VALUES ('tumbler');
INSERT INTO categories (name) VALUES ('towel');
INSERT INTO categories (name) VALUES ('slipper');
INSERT INTO categories (name) VALUES ('coaster');
INSERT INTO categories (name) VALUES ('socks');
INSERT INTO categories (name) VALUES ('smarttok');
INSERT INTO categories (name) VALUES ('umbrella');
INSERT INTO categories (name) VALUES ('tshirt');
INSERT INTO categories (name) VALUES ('hoodie');

-- 2. 상품 데이터 삽입 (category_id는 위에서 생성된 카테고리 ID 참조)
-- photobook 카테고리 (category_id = 1)
INSERT INTO products (
    name, description, price, original_price, discount_rate, stock_quantity, 
    category_id, is_new, has_event, average_rating, review_count, 
    options, image_urls
) VALUES (
    '한정판 포토북', 
    'Project X 한정판 포토북입니다. 고품질 아트지를 사용하여 선명하고 생생한 이미지를 제공합니다.',
    25000, 30000, 17, 50,
    1, 1, 1, 4.8, 125,
    'A4 사이즈,100페이지',
    'https://example.com/photobook1.jpg,https://example.com/photobook1_detail.jpg'
);

-- goods 카테고리 (category_id = 2)
INSERT INTO products (
    name, description, price, stock_quantity, category_id, is_new, has_event, 
    average_rating, review_count, options, image_urls
) VALUES (
    'Project X 굿즈 세트',
    '프로젝트 X 공식 굿즈 세트입니다. 스티커, 엽서, 키링이 포함된 올인원 패키지입니다.',
    18000, 100, 2, 1, 0, 
    4.5, 87, '기본 세트',
    'https://example.com/goods1.jpg,https://example.com/goods1_detail.jpg'
);

-- sticker 카테고리 (category_id = 3)
INSERT INTO products (
    name, description, price, original_price, discount_rate, stock_quantity,
    category_id, is_new, has_event, average_rating, review_count,
    options, image_urls
) VALUES (
    '한정판 스티커팩',
    '홀로그램 스티커 10종 세트입니다. 방수 코팅으로 오래도록 사용 가능합니다.',
    8000, 10000, 20, 200,
    3, 0, 1, 4.7, 203,
    '홀로그램,방수 코팅',
    'https://example.com/sticker1.jpg'
);

-- bag 카테고리 (category_id = 4)
INSERT INTO products (
    name, description, price, stock_quantity, category_id, is_new, has_event,
    average_rating, review_count, options, image_urls
) VALUES (
    'Project X 에코백',
    '친환경 캔버스 소재로 제작된 에코백입니다. 튼튼하고 실용적인 디자인입니다.',
    15000, 75, 4, 0, 0,
    4.3, 56, '캔버스 소재',
    'https://example.com/ecobag1.jpg'
);

-- keyring 카테고리 (category_id = 5)
INSERT INTO products (
    name, description, price, original_price, discount_rate, stock_quantity,
    category_id, is_new, has_event, average_rating, review_count,
    options, image_urls
) VALUES (
    '한정판 키링',
    '메탈 키링 한정판입니다. 고급스러운 골드 도금 마감으로 제작되었습니다.',
    12000, 15000, 20, 150,
    5, 0, 1, 4.6, 92,
    '골드 도금,메탈',
    'https://example.com/keyring1.jpg'
);

-- cup 카테고리 (category_id = 6)  
INSERT INTO products (
    name, description, price, stock_quantity, category_id, is_new, has_event,
    average_rating, review_count, options, image_urls
) VALUES (
    'Project X 머그컵',
    '세라믹 머그컵 화이트입니다. 일상에서 사용하기 좋은 심플한 디자인입니다.',
    20000, 80, 6, 0, 0,
    4.4, 73, '세라믹,350ml',
    'https://example.com/mug1.jpg'
);

-- pouch 카테고리 (category_id = 7)
INSERT INTO products (
    name, description, price, original_price, discount_rate, stock_quantity,
    category_id, is_new, has_event, average_rating, review_count,
    options, image_urls
) VALUES (
    '한정판 파우치',
    '방수 파우치 블랙입니다. 여행이나 일상에서 소품을 깔끔하게 정리할 수 있습니다.',
    13000, 16000, 19, 120,
    7, 0, 1, 4.5, 64,
    '방수,지퍼',
    'https://example.com/pouch1.jpg'
);

-- note 카테고리 (category_id = 8)
INSERT INTO products (
    name, description, price, stock_quantity, category_id, is_new, has_event,
    average_rating, review_count, options, image_urls
) VALUES (
    'Project X 노트',
    'A5 사이즈 노트입니다. 부드러운 종이 질감으로 필기하기 좋습니다.',
    9000, 300, 8, 1, 0,
    4.2, 45, 'A5,150페이지',
    'https://example.com/note1.jpg'
);

-- badge 카테고리 (category_id = 9)
INSERT INTO products (
    name, description, price, original_price, discount_rate, stock_quantity,
    category_id, is_new, has_event, average_rating, review_count,
    options, image_urls
) VALUES (
    '한정판 배지 세트',
    '배지 5종 세트입니다. 가방이나 옷에 포인트로 활용하기 좋습니다.',
    7000, 9000, 22, 180,
    9, 0, 1, 4.3, 78,
    '5종 세트,핀 타입',
    'https://example.com/badge1.jpg'
);

-- pen 카테고리 (category_id = 10)
INSERT INTO products (
    name, description, price, stock_quantity, category_id, is_new, has_event,
    average_rating, review_count, options, image_urls
) VALUES (
    'Project X 볼펜',
    '젤 볼펜 블랙입니다. 부드러운 필기감과 선명한 잉크가 특징입니다.',
    5000, 500, 10, 1, 0,
    4.1, 234, '0.5mm,젤 타입',
    'https://example.com/pen1.jpg'
);

-- postcard 카테고리 (category_id = 11)
INSERT INTO products (
    name, description, price, original_price, discount_rate, stock_quantity,
    category_id, is_new, has_event, average_rating, review_count,
    options, image_urls
) VALUES (
    '한정판 엽서 세트',
    '엽서 8종 세트입니다. 고급 아트지에 인쇄된 아름다운 디자인입니다.',
    6000, 8000, 25, 250,
    11, 0, 1, 4.4, 156,
    '8종 세트,아트지',
    'https://example.com/postcard1.jpg'
);

-- mousepad 카테고리 (category_id = 12)
INSERT INTO products (
    name, description, price, stock_quantity, category_id, is_new, has_event,
    average_rating, review_count, options, image_urls
) VALUES (
    'Project X 마우스패드',
    '고무 마우스패드 대형입니다. 미끄럼 방지 바닥면으로 안정적인 사용이 가능합니다.',
    14000, 90, 12, 0, 0,
    4.0, 34, '대형,고무',
    'https://example.com/mousepad1.jpg'
);

-- 더 많은 상품들 추가...
-- phonecase (category_id = 13)
INSERT INTO products (name, description, price, original_price, discount_rate, stock_quantity, category_id, is_new, has_event, average_rating, review_count, options, image_urls) 
VALUES ('한정판 폰케이스', '투명 하드케이스입니다. 충격 방지 기능이 있어 스마트폰을 안전하게 보호합니다.', 22000, 25000, 12, 110, 13, 0, 1, 4.2, 67, '투명,하드케이스', 'https://example.com/phonecase1.jpg');

-- calendar (category_id = 14)  
INSERT INTO products (name, description, price, stock_quantity, category_id, is_new, has_event, average_rating, review_count, options, image_urls)
VALUES ('Project X 캘린더', '2025 벽걸이 캘린더입니다. 매월 다른 디자인으로 구성되어 있습니다.', 11000, 60, 14, 1, 0, 4.3, 42, '벽걸이,12매', 'https://example.com/calendar1.jpg');

-- bookmark (category_id = 15)
INSERT INTO products (name, description, price, original_price, discount_rate, stock_quantity, category_id, is_new, has_event, average_rating, review_count, options, image_urls)
VALUES ('한정판 책갈피', '메탈 책갈피 골드입니다. 독서할 때 페이지를 표시하기 좋은 세련된 디자인입니다.', 4000, 5000, 20, 400, 15, 0, 1, 4.1, 189, '메탈,골드', 'https://example.com/bookmark1.jpg');

-- tumbler (category_id = 16)
INSERT INTO products (name, description, price, stock_quantity, category_id, is_new, has_event, average_rating, review_count, options, image_urls)
VALUES ('Project X 텀블러', '스테인리스 텀블러 500ml입니다. 보온/보냉 기능이 뛰어나 사계절 사용 가능합니다.', 28000, 45, 16, 0, 0, 4.6, 91, '스테인리스,500ml', 'https://example.com/tumbler1.jpg');

-- 관리자 계정
INSERT INTO users (user_id, username, password_hash, email, name, nickname, age, address, is_admin, created_at) 
VALUES (1, 'admin', 'admin123', 'admin@projectx.com', '관리자', 'Admin', 30, '서울특별시 강남구', 1, CURRENT_TIMESTAMP);

-- 일반 사용자 계정들
INSERT INTO users (user_id, username, password_hash, email, name, nickname, age, address, is_admin, created_at) 
VALUES (2, 'user1', 'user123', 'user1@example.com', '홍길동', 'Gil', 25, '서울특별시 강남구 테헤란로 123', 0, CURRENT_TIMESTAMP);

INSERT INTO users (user_id, username, password_hash, email, name, nickname, age, address, is_admin, created_at) 
VALUES (3, 'user2', 'user123', 'user2@example.com', '김철수', 'Chul', 28, '부산광역시 해운대구 센텀시티', 0, CURRENT_TIMESTAMP);

INSERT INTO users (user_id, username, password_hash, email, name, nickname, age, address, is_admin, created_at) 
VALUES (4, 'testuser', 'test123', 'test@test.com', '테스트유저', 'Tester', 22, '인천광역시 연수구', 0, CURRENT_TIMESTAMP);

-- user_id 시퀀스 값 조정 (다음 삽입시 5부터 시작하도록)
-- Oracle의 경우 IDENTITY 컬럼이므로 별도 시퀀스 조정 불필요


COMMIT;