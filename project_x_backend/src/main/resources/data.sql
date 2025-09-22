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
    options, image_urls, created_at
) VALUES (
    '한정판 포토북', 
    'Project X 한정판 포토북입니다. 고품질 아트지를 사용하여 선명하고 생생한 이미지를 제공합니다.',
    25000, 30000, 17, 50,
    1, 1, 1, 4.8, 125,
    'A4 사이즈,100페이지',
    'https://example.com/photobook1.jpg,https://example.com/photobook1_detail.jpg', SYSTIMESTAMP
);

-- goods 카테고리 (category_id = 2)
INSERT INTO products (
    name, description, price, stock_quantity, category_id, is_new, has_event, 
    average_rating, review_count, options, image_urls, created_at
) VALUES (
    'Project X 굿즈 세트',
    '프로젝트 X 공식 굿즈 세트입니다. 스티커, 엽서, 키링이 포함된 올인원 패키지입니다.',
    18000, 100, 2, 1, 0, 
    4.5, 87, '기본 세트',
    'https://example.com/goods1.jpg,https://example.com/goods1_detail.jpg', SYSTIMESTAMP
);

-- sticker 카테고리 (category_id = 3)
INSERT INTO products (
    name, description, price, original_price, discount_rate, stock_quantity,
    category_id, is_new, has_event, average_rating, review_count,
    options, image_urls, created_at
) VALUES (
    '한정판 스티커팩',
    '홀로그램 스티커 10종 세트입니다. 방수 코팅으로 오래도록 사용 가능합니다.',
    8000, 10000, 20, 200,
    3, 0, 1, 4.7, 203,
    '홀로그램,방수 코팅',
    'https://example.com/sticker1.jpg', SYSTIMESTAMP
);

-- bag 카테고리 (category_id = 4)
INSERT INTO products (
    name, description, price, stock_quantity, category_id, is_new, has_event,
    average_rating, review_count, options, image_urls, created_at
) VALUES (
    'Project X 에코백',
    '친환경 캔버스 소재로 제작된 에코백입니다. 튼튼하고 실용적인 디자인입니다.',
    15000, 75, 4, 0, 0,
    4.3, 56, '캔버스 소재',
    'https://example.com/ecobag1.jpg', SYSTIMESTAMP
);

-- keyring 카테고리 (category_id = 5)
INSERT INTO products (
    name, description, price, original_price, discount_rate, stock_quantity,
    category_id, is_new, has_event, average_rating, review_count,
    options, image_urls, created_at
) VALUES (
    '한정판 키링',
    '메탈 키링 한정판입니다. 고급스러운 골드 도금 마감으로 제작되었습니다.',
    12000, 15000, 20, 150,
    5, 0, 1, 4.6, 92,
    '골드 도금,메탈',
    'https://example.com/keyring1.jpg', SYSTIMESTAMP
);

-- cup 카테고리 (category_id = 6)  
INSERT INTO products (
    name, description, price, stock_quantity, category_id, is_new, has_event,
    average_rating, review_count, options, image_urls, created_at
) VALUES (
    'Project X 머그컵',
    '세라믹 머그컵 화이트입니다. 일상에서 사용하기 좋은 심플한 디자인입니다.',
    20000, 80, 6, 0, 0,
    4.4, 73, '세라믹,350ml',
    'https://example.com/mug1.jpg', SYSTIMESTAMP
);

-- pouch 카테고리 (category_id = 7)
INSERT INTO products (
    name, description, price, original_price, discount_rate, stock_quantity,
    category_id, is_new, has_event, average_rating, review_count,
    options, image_urls, created_at
) VALUES (
    '한정판 파우치',
    '방수 파우치 블랙입니다. 여행이나 일상에서 소품을 깔끔하게 정리할 수 있습니다.',
    13000, 16000, 19, 120,
    7, 0, 1, 4.5, 64,
    '방수,지퍼',
    'https://example.com/pouch1.jpg', SYSTIMESTAMP
);

-- note 카테고리 (category_id = 8)
INSERT INTO products (
    name, description, price, stock_quantity, category_id, is_new, has_event,
    average_rating, review_count, options, image_urls, created_at
) VALUES (
    'Project X 노트',
    'A5 사이즈 노트입니다. 부드러운 종이 질감으로 필기하기 좋습니다.',
    9000, 300, 8, 1, 0,
    4.2, 45, 'A5,150페이지',
    'https://example.com/note1.jpg', SYSTIMESTAMP
);

-- badge 카테고리 (category_id = 9)
INSERT INTO products (
    name, description, price, original_price, discount_rate, stock_quantity,
    category_id, is_new, has_event, average_rating, review_count,
    options, image_urls, created_at
) VALUES (
    '한정판 배지 세트',
    '배지 5종 세트입니다. 가방이나 옷에 포인트로 활용하기 좋습니다.',
    7000, 9000, 22, 180,
    9, 0, 1, 4.3, 78,
    '5종 세트,핀 타입',
    'https://example.com/badge1.jpg', SYSTIMESTAMP
);

-- pen 카테고리 (category_id = 10)
INSERT INTO products (
    name, description, price, stock_quantity, category_id, is_new, has_event,
    average_rating, review_count, options, image_urls, created_at
) VALUES (
    'Project X 볼펜',
    '젤 볼펜 블랙입니다. 부드러운 필기감과 선명한 잉크가 특징입니다.',
    5000, 500, 10, 1, 0,
    4.1, 234, '0.5mm,젤 타입',
    'https://example.com/pen1.jpg', SYSTIMESTAMP
);

-- postcard 카테고리 (category_id = 11)
INSERT INTO products (
    name, description, price, original_price, discount_rate, stock_quantity,
    category_id, is_new, has_event, average_rating, review_count,
    options, image_urls, created_at
) VALUES (
    '한정판 엽서 세트',
    '엽서 8종 세트입니다. 고급 아트지에 인쇄된 아름다운 디자인입니다.',
    6000, 8000, 25, 250,
    11, 0, 1, 4.4, 156,
    '8종 세트,아트지',
    'https://example.com/postcard1.jpg', SYSTIMESTAMP
);

-- mousepad 카테고리 (category_id = 12)
INSERT INTO products (
    name, description, price, stock_quantity, category_id, is_new, has_event,
    average_rating, review_count, options, image_urls, created_at
) VALUES (
    'Project X 마우스패드',
    '고무 마우스패드 대형입니다. 미끄럼 방지 바닥면으로 안정적인 사용이 가능합니다.',
    14000, 90, 12, 0, 0,
    4.0, 34, '대형,고무',
    'https://example.com/mousepad1.jpg', SYSTIMESTAMP
);

-- 더 많은 상품들 추가...
-- phonecase (category_id = 13)
INSERT INTO products (name, description, price, original_price, discount_rate, stock_quantity, category_id, is_new, has_event, average_rating, review_count, options, image_urls, created_at) 
VALUES ('한정판 폰케이스', '투명 하드케이스입니다. 충격 방지 기능이 있어 스마트폰을 안전하게 보호합니다.', 22000, 25000, 12, 110, 13, 0, 1, 4.2, 67, '투명,하드케이스', 'https://example.com/phonecase1.jpg', SYSTIMESTAMP);

-- calendar (category_id = 14)  
INSERT INTO products (name, description, price, stock_quantity, category_id, is_new, has_event, average_rating, review_count, options, image_urls, created_at)
VALUES ('Project X 캘린더', '2025 벽걸이 캘린더입니다. 매월 다른 디자인으로 구성되어 있습니다.', 11000, 60, 14, 1, 0, 4.3, 42, '벽걸이,12매', 'https://example.com/calendar1.jpg', SYSTIMESTAMP);

-- bookmark (category_id = 15)
INSERT INTO products (name, description, price, original_price, discount_rate, stock_quantity, category_id, is_new, has_event, average_rating, review_count, options, image_urls, created_at)
VALUES ('한정판 책갈피', '메탈 책갈피 골드입니다. 독서할 때 페이지를 표시하기 좋은 세련된 디자인입니다.', 4000, 5000, 20, 400, 15, 0, 1, 4.1, 189, '메탈,골드', 'https://example.com/bookmark1.jpg', SYSTIMESTAMP);

-- tumbler (category_id = 16)
INSERT INTO products (name, description, price, stock_quantity, category_id, is_new, has_event, average_rating, review_count, options, image_urls, created_at)
VALUES ('Project X 텀블러', '스테인리스 텀블러 500ml입니다. 보온/보냉 기능이 뛰어나 사계절 사용 가능합니다.', 28000, 45, 16, 0, 0, 4.6, 91, '스테인리스,500ml', 'https://example.com/tumbler1.jpg', SYSTIMESTAMP);

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


psycho_type insert문

INSERT INTO psycho_type (psycho_type_name, description) VALUES ('cute+dodo', '무대에선 새침한 선으로 분위기를 세우고, 소소한 애교로 장면의 무게를 풀어준다. 무대 밖에선 자연스러운 리액션으로 마음을 풀어주고, 작은 제스처 하나에도 미소가 번진다. 팬들은 저절로 미소 짓고, 팀에선 결의 선을 무너지지 않게 붙든다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('chic+cute', '무대에선 담백한 템포로 여백의 미를 완성하고, 순간의 큐트로 하이라이트를 살린다. 무대 밖에선 자연스러운 리액션으로 마음을 풀어주고, 자연스러운 웃음이 따라온다. 팬들은 저절로 미소 짓고, 팀에선 과장을 덜어 화면을 청량하게 한다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('cute+lovely', '무대에선 사랑스러운 포즈로 하이라이트를 부드럽게 감싸고, 순간의 큐트로 하이라이트를 살린다. 무대 밖에선 작은 리액션으로도 공기를 부드럽게 바꾸고, 자연스러운 웃음이 따라온다. 팬들은 표정이 먼저 풀리고, 팀에선 팬서비스 에이스로 사랑을 모은다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('cute+leadership', '무대에선 정확한 큐로 전체 템포를 안내하고, 순간의 큐트로 하이라이트를 살린다. 무대 밖에선 작은 리액션으로도 공기를 부드럽게 바꾸고, 작은 제스처 하나에도 미소가 번진다. 팬들은 표정이 먼저 풀리고, 팀에선 자연스레 중심을 잡는 리더로 빛난다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('calm+cute', '무대에선 흔들림 없는 표정으로 중심을 단단히 붙잡고, 소소한 애교로 장면의 무게를 풀어준다. 무대 밖에선 자연스러운 리액션으로 마음을 풀어주고, 보는 이의 표정이 함께 풀린다. 팬들은 표정이 먼저 풀리고, 팀에선 밸런서로 안정감을 깔아준다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('cute+kind', '무대에선 부드러운 연결로 팀 합을 매만지고, 순간의 큐트로 하이라이트를 살린다.\n무대 밖에선 자연스러운 리액션으로 마음을 풀어주고, 작은 제스처 하나에도 미소가 번진다.\n팬들은 저절로 미소 짓고, 팀에선 배려로 팀의 온도를 지켜낸다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('cute+playful', '무대에선 타이밍 좋은 애드리브로 분위기를 터뜨리고, 순간의 큐트로 하이라이트를 살린다.\n무대 밖에선 자연스러운 리액션으로 마음을 풀어주고, 작은 제스처 하나에도 미소가 번진다.\n팬들은 큐티 포인트를 반복 재생하고, 팀에선 현장의 공기를 상큼하게 환기한다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('charisma+cute', '무대에선 강렬한 아이컨택으로 흐름을 제압하고, 한 번의 귀여운 포인트로 마침표를 찍는다.\n무대 밖에선 사소한 리액션에도 진심을 담아 웃음을 만들고, 작은 제스처 하나에도 미소가 번진다.\n팬들은 표정이 먼저 풀리고, 팀에선 무게감으로 주요 파트를 이끈다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('cute+intellect', '무대에선 구성의 핵심만 남겨 임팩트를 높이고, 소소한 애교로 장면의 무게를 풀어준다.\n무대 밖에선 사소한 리액션에도 진심을 담아 웃음을 만들고, 자연스러운 웃음이 따라온다.\n팬들은 큐티 포인트를 반복 재생하고, 팀에선 포지셔닝을 계산해 연결을 만든다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('cute+mystique', '무대에선 달빛 같은 톤으로 장면에 미스터리를 더하고, 소소한 애교로 장면의 무게를 풀어준다.\n무대 밖에선 사소한 리액션에도 진심을 담아 웃음을 만들고, 자연스러운 웃음이 따라온다.\n팬들은 큐티 포인트를 반복 재생하고, 팀에선 세계관의 감도를 높이는 축이 된다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('cute+passion', '무대에선 러닝타임 전체에 열기를 골고루 분산시키고, 순간의 큐트로 하이라이트를 살린다.\n무대 밖에선 사소한 리액션에도 진심을 담아 웃음을 만들고, 자연스러운 웃음이 따라온다.\n팬들은 표정이 먼저 풀리고, 팀에선 텐션을 끌어올리는 엔진 역할을 한다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('chic+dodo', '무대에선 말수 적은 제스처로 미니멀한 멋을 쌓고, 무정한 듯한 표정으로 긴장을 유지한다.\n무대 밖에선 말투와 표정을 절제해 긴장을 적당히 남기고, 선명한 결이 오래 기억된다.\n팬들은 새침한 무드에 더 집중하고, 팀에선 스타일 메이커로 무드를 정제한다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('dodo+lovely', '무대에선 도도한 시선 처리로 긴장을 팽팽히 잡고, 따뜻한 말투로 여운을 남긴다.\n무대 밖에선 사소한 순간까지 사랑스럽게 비추고, 주변의 표정이 눈에 띄게 부드러워진다.\n팬들은 보호해주고 싶다고 말하고, 팀에선 결의 선을 무너지지 않게 붙든다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('dodo+leadership', '무대에선 큐와 동선을 조율해 흐름을 잡고, 새침한 디테일로 콘셉트를 정교하게 고정한다.\n무대 밖에선 선을 지키는 새침함으로 적당한 거리를 유지하고, 적당한 긴장감이 매력으로 남는다.\n팬들은 밀당의 설렘을 느끼고, 팀에선 조율의 축이 되어 흐름을 정리한다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('calm+dodo', '무대에선 흔들림 없는 표정으로 중심을 단단히 붙잡고, 깔끔한 선으로 컨셉의 결을 지탱한다.\n무대 밖에선 새침하지만 예의를 지키며 자신만의 페이스를 유지하고, 거리감 속 시선이 더 머문다.\n팬들은 밀당의 설렘을 느끼고, 팀에선 밸런서로 안정감을 깔아준다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('dodo+kind', '무대에선 부드러운 연결로 팀 합을 매만지고, 무정한 듯한 표정으로 긴장을 유지한다.\n무대 밖에선 말투와 표정을 절제해 긴장을 적당히 남기고, 적당한 긴장감이 매력으로 남는다.\n팬들은 단정한 결에 매혹되고, 팀에선 세심함으로 합을 매만진다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('dodo+playful', '무대에선 타이밍 좋은 애드리브로 분위기를 터뜨리고, 무정한 듯한 표정으로 긴장을 유지한다.\n무대 밖에선 새침하지만 예의를 지키며 자신만의 페이스를 유지하고, 선명한 결이 오래 기억된다.\n팬들은 단정한 결에 매혹되고, 팀에선 현장의 공기를 상큼하게 환기한다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('charisma+dodo', '무대에선 강렬한 아이컨택으로 흐름을 제압하고, 무정한 듯한 표정으로 긴장을 유지한다.\n무대 밖에선 새침하지만 예의를 지키며 자신만의 페이스를 유지하고, 거리감 속 시선이 더 머문다.\n팬들은 새침한 무드에 더 집중하고, 팀에선 무게감으로 주요 파트를 이끈다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('dodo+intellect', '무대에선 센스 있는 선택으로 메시지를 또렷하게 하고, 깔끔한 선으로 컨셉의 결을 지탱한다.\n무대 밖에선 선을 지키는 새침함으로 적당한 거리를 유지하고, 선명한 결이 오래 기억된다.\n팬들은 단정한 결에 매혹되고, 팀에선 판단으로 상황을 간결히 정리한다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('dodo+mystique', '무대에선 새침한 선으로 분위기를 세우고, 빛과 그림자 사이로 서사를 숨긴다.\n무대 밖에선 정확한 말 대신 여운을 남기고, 호기심이 조용히 자란다.\n팬들은 수수께끼 같은 무드에 빠지고, 팀에선 결의 선을 무너지지 않게 붙든다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('dodo+passion', '무대에선 파워풀한 드라이브로 무대의 엔진이 되고, 무정한 듯한 표정으로 긴장을 유지한다.\n무대 밖에선 선을 지키는 새침함으로 적당한 거리를 유지하고, 선명한 결이 오래 기억된다.\n팬들은 새침한 무드에 더 집중하고, 팀에선 텐션을 끌어올리는 엔진 역할을 한다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('chic+lovely', '무대에선 말수 적은 제스처로 미니멀한 멋을 쌓고, 달콤한 제스처로 분위기를 부드럽게 전환한다.\n무대 밖에선 사소한 순간까지 사랑스럽게 비추고, 주변의 표정이 눈에 띄게 부드러워진다.\n팬들은 보호해주고 싶다고 말하고, 팀에선 스타일 메이커로 무드를 정제한다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('chic+leadership', '무대에선 큐와 동선을 조율해 흐름을 잡고, 차분한 시선 처리로 라인을 정돈한다.\n무대 밖에선 장식 없는 말투로 편안히 교감하고, 깔끔한 태도가 신뢰를 만든다.\n팬들은 미니멀한 멋을 음미하고, 팀에선 중심을 맡아 자연스레 모두를 이끈다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('calm+chic', '무대에선 흔들림 없는 표정으로 중심을 단단히 붙잡고, 여백을 남겨 감상 포인트를 또렷하게 한다.\n무대 밖에선 장식 없는 말투로 편안히 교감하고, 깔끔한 태도가 신뢰를 만든다.\n팬들은 깔끔한 라인에 끌리고, 팀에선 과열을 가라앉혀 집중을 돕는다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('chic+kind', '무대에선 포근한 터치로 장면 사이의 틈을 메우고, 차분한 시선 처리로 라인을 정돈한다.\n무대 밖에선 장식 없는 말투로 편안히 교감하고, 정돈된 인상이 상쾌하게 남는다.\n팬들은 깔끔한 라인에 끌리고, 팀에선 세심함으로 합을 매만진다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('chic+playful', '무대에선 즉흥적인 리액션으로 전개에 산소를 넣고, 담백한 마감으로 화면을 깨끗하게 정리한다.\n무대 밖에선 장식 없는 말투로 편안히 교감하고, 정돈된 인상이 상쾌하게 남는다.\n팬들은 깔끔한 라인에 끌리고, 팀에선 무드메이커로 긴장을 풀어준다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('charisma+chic', '무대에선 한 번의 제스처로 공기를 바꾸고, 담백한 마감으로 화면을 깨끗하게 정리한다.\n무대 밖에선 손짓과 말수를 줄여 깔끔하게 의사를 전하고, 정돈된 인상이 상쾌하게 남는다.\n팬들은 미니멀한 멋을 음미하고, 팀에선 임팩트 구간을 장악하는 에이스다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('chic+intellect', '무대에선 구성의 핵심만 남겨 임팩트를 높이고, 여백을 남겨 감상 포인트를 또렷하게 한다.\n무대 밖에선 손짓과 말수를 줄여 깔끔하게 의사를 전하고, 담백함이 오히려 친근하다.\n팬들은 담백한 멋에 빠지고, 팀에선 포지셔닝을 계산해 연결을 만든다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('chic+mystique', '무대에선 말수 적은 제스처로 미니멀한 멋을 쌓고, 빛과 그림자 사이로 서사를 숨긴다.\n무대 밖에선 말보단 눈빛으로 느낌을 건네고, 여운이 길게 이어진다.\n팬들은 남겨진 여운을 따라가고, 팀에선 라인을 정리해 미감을 지켜낸다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('chic+passion', '무대에선 러닝타임 전체에 열기를 골고루 분산시키고, 여백을 남겨 감상 포인트를 또렷하게 한다.\n무대 밖에선 과장 없는 쿨함으로 담백하게 소통하고, 담백함이 오히려 친근하다.\n팬들은 미니멀한 멋을 음미하고, 팀에선 추진력으로 팀의 페이스를 유지한다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('leadership+lovely', '무대에선 큐와 동선을 조율해 흐름을 잡고, 따뜻한 말투로 여운을 남긴다.\n무대 밖에선 상대의 말을 따라가며 포근한 반응을 보이고, 대화의 온도를 높인다.\n팬들은 보호해주고 싶다고 말하고, 팀에선 자연스레 중심을 잡는 리더로 빛난다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('calm+lovely', '무대에선 안정된 호흡으로 템포를 다스리고, 스윗한 미소로 경계선을 녹인다.\n무대 밖에선 사소한 순간까지 사랑스럽게 비추고, 대화의 온도를 높인다.\n팬들은 보호해주고 싶다고 말하고, 팀에선 밸런서로 안정감을 깔아준다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('kind+lovely', '무대에선 다정한 톤으로 파트를 매끈하게 이어주고, 따뜻한 말투로 여운을 남긴다.\n무대 밖에선 말끝마다 스윗함을 남겨 온도를 높이고, 주변의 표정이 눈에 띄게 부드러워진다.\n팬들은 스윗함에 마음이 녹고, 팀에선 케어러로 분위기를 다독인다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('lovely+playful', '무대에선 즉흥적인 리액션으로 전개에 산소를 넣고, 따뜻한 말투로 여운을 남긴다.\n무대 밖에선 말끝마다 스윗함을 남겨 온도를 높이고, 주변의 표정이 눈에 띄게 부드러워진다.\n팬들은 다정한 온기에 머문다, 팀에선 현장의 공기를 상큼하게 환기한다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('charisma+lovely', '무대에선 한 번의 제스처로 공기를 바꾸고, 달콤한 제스처로 분위기를 부드럽게 전환한다.\n무대 밖에선 말끝마다 스윗함을 남겨 온도를 높이고, 공기가 한결 포근해진다.\n팬들은 스윗함에 마음이 녹고, 팀에선 클라이맥스를 책임지는 센터형이다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('intellect+lovely', '무대에선 구성의 핵심만 남겨 임팩트를 높이고, 달콤한 제스처로 분위기를 부드럽게 전환한다.\n무대 밖에선 상대의 말을 따라가며 포근한 반응을 보이고, 주변의 표정이 눈에 띄게 부드러워진다.\n팬들은 다정한 온기에 머문다, 팀에선 전략가로 흐름을 설계한다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('lovely+mystique', '무대에선 의도적 침묵으로 호기심의 결을 세우고, 달콤한 제스처로 분위기를 부드럽게 전환한다.\n무대 밖에선 말끝마다 스윗함을 남겨 온도를 높이고, 주변의 표정이 눈에 띄게 부드러워진다.\n팬들은 보호해주고 싶다고 말하고, 팀에선 여백을 남겨 상상력을 확장한다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('lovely+passion', '무대에선 탄력 있는 에너지로 텐션을 끌어올리고, 달콤한 제스처로 분위기를 부드럽게 전환한다.\n무대 밖에선 말끝마다 스윗함을 남겨 온도를 높이고, 대화의 온도를 높인다.\n팬들은 다정한 온기에 머문다, 팀에선 텐션을 끌어올리는 엔진 역할을 한다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('calm+leadership', '무대에선 구성과 디테일을 주도해 무대를 정렬하고, 고르게 호흡시켜 균형을 맞춘다.\n무대 밖에선 말보다 호흡으로 공기를 가라앉히고, 주변이 편안해진다.\n팬들은 편안한 힐링을 느끼고, 팀에선 중심을 맡아 자연스레 모두를 이끈다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('kind+leadership', '무대에선 구성과 디테일을 주도해 무대를 정렬하고, 포근한 연결로 합을 자연스럽게 이어간다.\n무대 밖에선 작은 배려를 아끼지 않아 긴장을 풀어주고, 함께 있는 시간이 부드러워진다.\n팬들은 포근한 태도에 마음이 풀리고, 팀에선 조율의 축이 되어 흐름을 정리한다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('leadership+playful', '무대에선 구성과 디테일을 주도해 무대를 정렬하고, 기분 좋은 소란으로 다음 장면을 열어준다.\n무대 밖에선 센스 있는 장난으로 긴장을 풀어주고, 대화의 템포가 가벼워진다.\n팬들은 타이밍 좋은 장난에 환호하고, 팀에선 자연스레 중심을 잡는 리더로 빛난다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('charisma+leadership', '무대에선 구성과 디테일을 주도해 무대를 정렬하고, 강단 있는 표정으로 클라이맥스를 당긴다.\n무대 밖에선 과묵하게 서 있어도 공기를 정리하고, 낯섦 대신 든든함이 남는다.\n팬들은 포스 넘치는 순간을 저장하고, 팀에선 중심을 맡아 자연스레 모두를 이끈다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('intellect+leadership', '무대에선 큐와 동선을 조율해 흐름을 잡고, 핵심만 남긴 구성으로 임팩트를 높인다.\n무대 밖에선 맥락을 포착해 결론으로 부드럽게 잇고, 대화가 가볍고 명료해진다.\n팬들은 인터뷰 장인이라 부르고, 팀에선 조율의 축이 되어 흐름을 정리한다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('leadership+mystique', '무대에선 구성과 디테일을 주도해 무대를 정렬하고, 빛과 그림자 사이로 서사를 숨긴다.\n무대 밖에선 말보단 눈빛으로 느낌을 건네고, 해석의 가능성이 열린다.\n팬들은 수수께끼 같은 무드에 빠지고, 팀에선 조율의 축이 되어 흐름을 정리한다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('leadership+passion', '무대에선 큐와 동선을 조율해 흐름을 잡고, 무대의 푸시를 끝까지 유지한다.\n무대 밖에선 연습과 자기계발 얘기로 온기를 전하고, 열기가 따뜻하게 번진다.\n팬들은 훈련기와 무대 이야기에 공감하고, 팀에선 중심을 맡아 자연스레 모두를 이끈다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('calm+kind', '무대에선 다정한 톤으로 파트를 매끈하게 이어주고, 잔잔한 템포로 몰입을 오래 끌고 간다.\n무대 밖에선 말보다 호흡으로 공기를 가라앉히고, 조급함이 사라진다.\n팬들은 잔잔함에 숨이 고르고, 팀에선 세심함으로 합을 매만진다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('calm+playful', '무대에선 재치 있는 포즈로 현장의 온도를 올리고, 고르게 호흡시켜 균형을 맞춘다.\n무대 밖에선 말보다 호흡으로 공기를 가라앉히고, 주변이 편안해진다.\n팬들은 잔잔함에 숨이 고르고, 팀에선 현장의 공기를 상큼하게 환기한다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('calm+charisma', '무대에선 한 번의 제스처로 공기를 바꾸고, 잔잔한 템포로 몰입을 오래 끌고 간다.\n무대 밖에선 느긋한 페이스로 대화를 단단히 붙들고, 주변이 편안해진다.\n팬들은 차분한 무드에 머문다, 팀에선 무게감으로 주요 파트를 이끈다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('calm+intellect', '무대에선 센스 있는 선택으로 메시지를 또렷하게 하고, 고르게 호흡시켜 균형을 맞춘다.\n무대 밖에선 말보다 호흡으로 공기를 가라앉히고, 주변이 편안해진다.\n팬들은 편안한 힐링을 느끼고, 팀에선 판단으로 상황을 간결히 정리한다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('calm+mystique', '무대에선 흔들림 없는 표정으로 중심을 단단히 붙잡고, 미묘한 공백으로 상상력을 자극한다.\n무대 밖에선 단어를 아끼며 감정을 암시하고, 호기심이 조용히 자란다.\n팬들은 수수께끼 같은 무드에 빠지고, 팀에선 과열을 가라앉혀 집중을 돕는다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('calm+passion', '무대에선 탄력 있는 에너지로 텐션을 끌어올리고, 잔잔한 템포로 몰입을 오래 끌고 간다.\n무대 밖에선 잔잔한 톤으로 안정감을 나눠주고, 마음이 안정된다.\n팬들은 잔잔함에 숨이 고르고, 팀에선 텐션을 끌어올리는 엔진 역할을 한다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('kind+playful', '무대에선 부드러운 연결로 팀 합을 매만지고, 재치 있는 포즈로 순간을 살아나게 한다.\n무대 밖에선 상황에 맞춘 가벼운 드립으로 웃음을 만들고, 자연스러운 웃음이 이어진다.\n팬들은 수많은 밈을 만들어내고, 팀에선 케어러로 분위기를 다독인다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('charisma+kind', '무대에선 무게감 있는 스텝으로 시선을 고정하고, 다정한 제스처로 팀의 결을 유연하게 만든다.\n무대 밖에선 다정한 말투로 주변을 살피고, 온화한 무드가 퍼진다.\n팬들은 포근한 태도에 마음이 풀리고, 팀에선 임팩트 구간을 장악하는 에이스다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('intellect+kind', '무대에선 과감한 생략으로 포인트를 선명히 남기고, 부드러운 마감으로 긴장을 풀어준다.\n무대 밖에선 눈맞춤과 리액션으로 마음을 헤아리고, 사소한 긴장이 사라진다.\n팬들은 다정한 말투에 미소 짓고, 팀에선 판단으로 상황을 간결히 정리한다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('kind+mystique', '무대에선 포근한 터치로 장면 사이의 틈을 메우고, 미묘한 공백으로 상상력을 자극한다.\n무대 밖에선 단어를 아끼며 감정을 암시하고, 여운이 길게 이어진다.\n팬들은 해석 놀이를 멈추지 못하고, 팀에선 세심함으로 합을 매만진다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('kind+passion', '무대에선 파워풀한 드라이브로 무대의 엔진이 되고, 포근한 연결로 합을 자연스럽게 이어간다.\n무대 밖에선 다정한 말투로 주변을 살피고, 함께 있는 시간이 부드러워진다.\n팬들은 세심한 배려에 감동하고, 팀에선 텐션을 끌어올리는 엔진 역할을 한다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('charisma+playful', '무대에선 강렬한 아이컨택으로 흐름을 제압하고, 재치 있는 포즈로 순간을 살아나게 한다.\n무대 밖에선 타이밍 좋은 농담으로 분위기를 환기하고, 분위기가 산뜻하게 환기된다.\n팬들은 타이밍 좋은 장난에 환호하고, 팀에선 임팩트 구간을 장악하는 에이스다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('intellect+playful', '무대에선 구성의 핵심만 남겨 임팩트를 높이고, 가벼운 장난으로 현장에 산뜻함을 더한다.\n무대 밖에선 센스 있는 장난으로 긴장을 풀어주고, 분위기가 산뜻하게 환기된다.\n팬들은 애드리브를 찾아 듣는다, 팀에선 전략가로 흐름을 설계한다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('mystique+playful', '무대에선 즉흥적인 리액션으로 전개에 산소를 넣고, 여운을 남겨 해석의 문을 연다.\n무대 밖에선 말보단 눈빛으로 느낌을 건네고, 여운이 길게 이어진다.\n팬들은 해석 놀이를 멈추지 못하고, 팀에선 현장의 공기를 상큼하게 환기한다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('passion+playful', '무대에선 러닝타임 전체에 열기를 골고루 분산시키고, 재치 있는 포즈로 순간을 살아나게 한다.\n무대 밖에선 타이밍 좋은 농담으로 분위기를 환기하고, 대화의 템포가 가벼워진다.\n팬들은 애드리브를 찾아 듣는다, 팀에선 텐션을 끌어올리는 엔진 역할을 한다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('charisma+intellect', '무대에선 강렬한 아이컨택으로 흐름을 제압하고, 의미 있는 생략으로 속도를 붙인다.\n무대 밖에선 요점을 간단히 정리해 흐름을 가볍게 하고, 포인트가 선명해진다.\n팬들은 명료한 코멘트에 감탄하고, 팀에선 임팩트 구간을 장악하는 에이스다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('charisma+mystique', '무대에선 한 번의 제스처로 공기를 바꾸고, 빛과 그림자 사이로 서사를 숨긴다.\n무대 밖에선 단어를 아끼며 감정을 암시하고, 호기심이 조용히 자란다.\n팬들은 수수께끼 같은 무드에 빠지고, 팀에선 클라이맥스를 책임지는 센터형이다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('charisma+passion', '무대에선 무게감 있는 스텝으로 시선을 고정하고, 연쇄적인 에너지로 파트를 끌어올린다.\n무대 밖에선 시도와 개선의 이야기를 즐겁게 풀어놓고, 긍정적인 자극이 퍼져나간다.\n팬들은 훈련기와 무대 이야기에 공감하고, 팀에선 무게감으로 주요 파트를 이끈다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('intellect+mystique', '무대에선 센스 있는 선택으로 메시지를 또렷하게 하고, 미묘한 공백으로 상상력을 자극한다.\n무대 밖에선 단어를 아끼며 감정을 암시하고, 해석의 가능성이 열린다.\n팬들은 수수께끼 같은 무드에 빠지고, 팀에선 판단으로 상황을 간결히 정리한다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('intellect+passion', '무대에선 러닝타임 전체에 열기를 골고루 분산시키고, 핵심만 남긴 구성으로 임팩트를 높인다.\n무대 밖에선 핵심을 콕 짚는 코멘트로 대화를 정리하고, 대화가 가볍고 명료해진다.\n팬들은 인터뷰 장인이라 부르고, 팀에선 텐션을 끌어올리는 엔진 역할을 한다.');
INSERT INTO psycho_type (psycho_type_name, description) VALUES ('mystique+passion', '무대에선 파워풀한 드라이브로 무대의 엔진이 되고, 미묘한 공백으로 상상력을 자극한다.\n무대 밖에선 정확한 말 대신 여운을 남기고, 여운이 길게 이어진다.\n팬들은 해석 놀이를 멈추지 못하고, 팀에선 텐션을 끌어올리는 엔진 역할을 한다.');



member (Member_info) insert문

INSERT INTO member_info (MEMBER_ID, NAME, AGE, BIRTHDATE, CONSTELLATION, PROFILE_IMAGE_URL, HEIGHT, APPEARANCE)
VALUES (1, '류하', 20, DATE '2005-01-01', '사자자리', '/Character/류하.png', 165, '신비로움');

INSERT INTO member_info (MEMBER_ID, NAME, AGE, BIRTHDATE, CONSTELLATION, PROFILE_IMAGE_URL, HEIGHT, APPEARANCE)
VALUES (2, '다온', 21, DATE '2004-02-01', '물병자리', '/Character/다온.png', 168, '상냥함');

INSERT INTO member_info (MEMBER_ID, NAME, AGE, BIRTHDATE, CONSTELLATION, PROFILE_IMAGE_URL, HEIGHT, APPEARANCE)
VALUES (3, '채윤', 22, DATE '2003-03-01', '양자리', '/Character/채윤.png', 162, '장난기');

INSERT INTO member_info (MEMBER_ID, NAME, AGE, BIRTHDATE, CONSTELLATION, PROFILE_IMAGE_URL, HEIGHT, APPEARANCE)
VALUES (4, '세라', 23, DATE '2002-04-01', '황소자리', '/Character/세라.png', 167, '열정');

INSERT INTO member_info (MEMBER_ID, NAME, AGE, BIRTHDATE, CONSTELLATION, PROFILE_IMAGE_URL, HEIGHT, APPEARANCE)
VALUES (5, '수린', 24, DATE '2001-05-01', '쌍둥이자리', '/Character/수린.png', 170, '도도함');

INSERT INTO member_info (MEMBER_ID, NAME, AGE, BIRTHDATE, CONSTELLATION, PROFILE_IMAGE_URL, HEIGHT, APPEARANCE)
VALUES (6, '모아', 20, DATE '2005-06-01', '게자리', '/Character/모아.png', 160, '귀여움');

INSERT INTO member_info (MEMBER_ID, NAME, AGE, BIRTHDATE, CONSTELLATION, PROFILE_IMAGE_URL, HEIGHT, APPEARANCE)
VALUES (7, '지원', 21, DATE '2004-07-01', '사자자리', '/Character/지원.png', 166, '리더십');

INSERT INTO member_info (MEMBER_ID, NAME, AGE, BIRTHDATE, CONSTELLATION, PROFILE_IMAGE_URL, HEIGHT, APPEARANCE)
VALUES (8, '세인', 22, DATE '2003-08-01', '처녀자리', '/Character/세인.png', 164, '지성미');

INSERT INTO member_info (MEMBER_ID, NAME, AGE, BIRTHDATE, CONSTELLATION, PROFILE_IMAGE_URL, HEIGHT, APPEARANCE)
VALUES (9, '아린', 23, DATE '2002-09-01', '천칭자리', '/Character/아린.png', 169, '시크함');

INSERT INTO member_info (MEMBER_ID, NAME, AGE, BIRTHDATE, CONSTELLATION, PROFILE_IMAGE_URL, HEIGHT, APPEARANCE)
VALUES (10, '현', 24, DATE '2001-10-01', '전갈자리', '/Character/현.png', 171, '차분함');

INSERT INTO member_info (MEMBER_ID, NAME, AGE, BIRTHDATE, CONSTELLATION, PROFILE_IMAGE_URL, HEIGHT, APPEARANCE)
VALUES (11, '가온', 25, DATE '2000-11-01', '사수자리', '/Character/가온.png', 172, '카리스마');

INSERT INTO member_info (MEMBER_ID, NAME, AGE, BIRTHDATE, CONSTELLATION, PROFILE_IMAGE_URL, HEIGHT, APPEARANCE)
VALUES (12, '유나', 26, DATE '1999-12-01', '염소자리', '/Character/유나.png', 163, '사랑스러움');



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
    options, image_urls, created_at
) VALUES (
    '한정판 포토북', 
    'Project X 한정판 포토북입니다. 고품질 아트지를 사용하여 선명하고 생생한 이미지를 제공합니다.',
    25000, 30000, 17, 50,
    1, 1, 1, 4.8, 125,
    'A4 사이즈,100페이지',
    'https://example.com/photobook1.jpg,https://example.com/photobook1_detail.jpg', SYSTIMESTAMP
);

-- goods 카테고리 (category_id = 2)
INSERT INTO products (
    name, description, price, stock_quantity, category_id, is_new, has_event, 
    average_rating, review_count, options, image_urls, created_at
) VALUES (
    'Project X 굿즈 세트',
    '프로젝트 X 공식 굿즈 세트입니다. 스티커, 엽서, 키링이 포함된 올인원 패키지입니다.',
    18000, 100, 2, 1, 0, 
    4.5, 87, '기본 세트',
    'https://example.com/goods1.jpg,https://example.com/goods1_detail.jpg', SYSTIMESTAMP
);

-- sticker 카테고리 (category_id = 3)
INSERT INTO products (
    name, description, price, original_price, discount_rate, stock_quantity,
    category_id, is_new, has_event, average_rating, review_count,
    options, image_urls, created_at
) VALUES (
    '한정판 스티커팩',
    '홀로그램 스티커 10종 세트입니다. 방수 코팅으로 오래도록 사용 가능합니다.',
    8000, 10000, 20, 200,
    3, 0, 1, 4.7, 203,
    '홀로그램,방수 코팅',
    'https://example.com/sticker1.jpg', SYSTIMESTAMP
);

-- bag 카테고리 (category_id = 4)
INSERT INTO products (
    name, description, price, stock_quantity, category_id, is_new, has_event,
    average_rating, review_count, options, image_urls, created_at
) VALUES (
    'Project X 에코백',
    '친환경 캔버스 소재로 제작된 에코백입니다. 튼튼하고 실용적인 디자인입니다.',
    15000, 75, 4, 0, 0,
    4.3, 56, '캔버스 소재',
    'https://example.com/ecobag1.jpg', SYSTIMESTAMP
);

-- keyring 카테고리 (category_id = 5)
INSERT INTO products (
    name, description, price, original_price, discount_rate, stock_quantity,
    category_id, is_new, has_event, average_rating, review_count,
    options, image_urls, created_at
) VALUES (
    '한정판 키링',
    '메탈 키링 한정판입니다. 고급스러운 골드 도금 마감으로 제작되었습니다.',
    12000, 15000, 20, 150,
    5, 0, 1, 4.6, 92,
    '골드 도금,메탈',
    'https://example.com/keyring1.jpg', SYSTIMESTAMP
);

-- cup 카테고리 (category_id = 6)  
INSERT INTO products (
    name, description, price, stock_quantity, category_id, is_new, has_event,
    average_rating, review_count, options, image_urls, created_at
) VALUES (
    'Project X 머그컵',
    '세라믹 머그컵 화이트입니다. 일상에서 사용하기 좋은 심플한 디자인입니다.',
    20000, 80, 6, 0, 0,
    4.4, 73, '세라믹,350ml',
    'https://example.com/mug1.jpg', SYSTIMESTAMP
);

-- pouch 카테고리 (category_id = 7)
INSERT INTO products (
    name, description, price, original_price, discount_rate, stock_quantity,
    category_id, is_new, has_event, average_rating, review_count,
    options, image_urls, created_at
) VALUES (
    '한정판 파우치',
    '방수 파우치 블랙입니다. 여행이나 일상에서 소품을 깔끔하게 정리할 수 있습니다.',
    13000, 16000, 19, 120,
    7, 0, 1, 4.5, 64,
    '방수,지퍼',
    'https://example.com/pouch1.jpg', SYSTIMESTAMP
);

-- note 카테고리 (category_id = 8)
INSERT INTO products (
    name, description, price, stock_quantity, category_id, is_new, has_event,
    average_rating, review_count, options, image_urls, created_at
) VALUES (
    'Project X 노트',
    'A5 사이즈 노트입니다. 부드러운 종이 질감으로 필기하기 좋습니다.',
    9000, 300, 8, 1, 0,
    4.2, 45, 'A5,150페이지',
    'https://example.com/note1.jpg', SYSTIMESTAMP
);

-- badge 카테고리 (category_id = 9)
INSERT INTO products (
    name, description, price, original_price, discount_rate, stock_quantity,
    category_id, is_new, has_event, average_rating, review_count,
    options, image_urls, created_at
) VALUES (
    '한정판 배지 세트',
    '배지 5종 세트입니다. 가방이나 옷에 포인트로 활용하기 좋습니다.',
    7000, 9000, 22, 180,
    9, 0, 1, 4.3, 78,
    '5종 세트,핀 타입',
    'https://example.com/badge1.jpg', SYSTIMESTAMP
);

-- pen 카테고리 (category_id = 10)
INSERT INTO products (
    name, description, price, stock_quantity, category_id, is_new, has_event,
    average_rating, review_count, options, image_urls, created_at
) VALUES (
    'Project X 볼펜',
    '젤 볼펜 블랙입니다. 부드러운 필기감과 선명한 잉크가 특징입니다.',
    5000, 500, 10, 1, 0,
    4.1, 234, '0.5mm,젤 타입',
    'https://example.com/pen1.jpg', SYSTIMESTAMP
);

-- postcard 카테고리 (category_id = 11)
INSERT INTO products (
    name, description, price, original_price, discount_rate, stock_quantity,
    category_id, is_new, has_event, average_rating, review_count,
    options, image_urls, created_at
) VALUES (
    '한정판 엽서 세트',
    '엽서 8종 세트입니다. 고급 아트지에 인쇄된 아름다운 디자인입니다.',
    6000, 8000, 25, 250,
    11, 0, 1, 4.4, 156,
    '8종 세트,아트지',
    'https://example.com/postcard1.jpg', SYSTIMESTAMP
);

-- mousepad 카테고리 (category_id = 12)
INSERT INTO products (
    name, description, price, stock_quantity, category_id, is_new, has_event,
    average_rating, review_count, options, image_urls, created_at
) VALUES (
    'Project X 마우스패드',
    '고무 마우스패드 대형입니다. 미끄럼 방지 바닥면으로 안정적인 사용이 가능합니다.',
    14000, 90, 12, 0, 0,
    4.0, 34, '대형,고무',
    'https://example.com/mousepad1.jpg', SYSTIMESTAMP
);

-- 더 많은 상품들 추가...
-- phonecase (category_id = 13)
INSERT INTO products (name, description, price, original_price, discount_rate, stock_quantity, category_id, is_new, has_event, average_rating, review_count, options, image_urls, created_at) 
VALUES ('한정판 폰케이스', '투명 하드케이스입니다. 충격 방지 기능이 있어 스마트폰을 안전하게 보호합니다.', 22000, 25000, 12, 110, 13, 0, 1, 4.2, 67, '투명,하드케이스', 'https://example.com/phonecase1.jpg', SYSTIMESTAMP);

-- calendar (category_id = 14)  
INSERT INTO products (name, description, price, stock_quantity, category_id, is_new, has_event, average_rating, review_count, options, image_urls, created_at)
VALUES ('Project X 캘린더', '2025 벽걸이 캘린더입니다. 매월 다른 디자인으로 구성되어 있습니다.', 11000, 60, 14, 1, 0, 4.3, 42, '벽걸이,12매', 'https://example.com/calendar1.jpg', SYSTIMESTAMP);

-- bookmark (category_id = 15)
INSERT INTO products (name, description, price, original_price, discount_rate, stock_quantity, category_id, is_new, has_event, average_rating, review_count, options, image_urls, created_at)
VALUES ('한정판 책갈피', '메탈 책갈피 골드입니다. 독서할 때 페이지를 표시하기 좋은 세련된 디자인입니다.', 4000, 5000, 20, 400, 15, 0, 1, 4.1, 189, '메탈,골드', 'https://example.com/bookmark1.jpg', SYSTIMESTAMP);

-- tumbler (category_id = 16)
INSERT INTO products (name, description, price, stock_quantity, category_id, is_new, has_event, average_rating, review_count, options, image_urls, created_at)
VALUES ('Project X 텀블러', '스테인리스 텀블러 500ml입니다. 보온/보냉 기능이 뛰어나 사계절 사용 가능합니다.', 28000, 45, 16, 0, 0, 4.6, 91, '스테인리스,500ml', 'https://example.com/tumbler1.jpg', SYSTIMESTAMP);

-- 관리자 계정
INSERT INTO users (user_id, username, password_hash, email, name, nickname, age, address, is_admin, created_at) 
VALUES (1, 'admin', 'admin123', 'admin@projectx.com', '관리자', 'Admin', 30, '서울특별시 강남구', 1, CURRENT_TIMESTAMP);

-- 일반 사용자 계정들
INSERT INTO users (user_id, username, password_hash, email, name, nickname, age, address, is_admin, created_at) 
VALUES (2, 'user1', 'user123', 'user1@example.com', '홍길동', 'Gil', 25, '서울특별시 강남구 테헤란로 123', 0, CURRENT_TIMESTAMP);

INSERT INTO users (user_id, username, password_hash, email, name, nickname, age, address, is_admin, created_at) 
VALUES (3, 'user2', 'user123', 'user2@eFxample.com', '김철수', 'Chul', 28, '부산광역시 해운대구 센텀시티', 0, CURRENT_TIMESTAMP);

INSERT INTO users (user_id, username, password_hash, email, name, nickname, age, address, is_admin, created_at) 
VALUES (4, 'testuser', 'test123', 'test@test.com', '테스트유저', 'Tester', 22, '인천광역시 연수구', 0, CURRENT_TIMESTAMP);

-

COMMIT;