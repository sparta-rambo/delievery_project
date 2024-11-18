# Delivery Project

## 📌 프로젝트 목적 및 상세
**음식 배달 서비스 관리 시스템**을 구현하였습니다.

### 주요 기능
- 사용자: 주문 요청 생성 및 상태 확인, 결제
- 관리자: 배달 상태 조회 및 수정
- 가게 주인 : 배달 상태 변경 및 주문 승인,거절

---

## 🛠️ 기술 스택
- **Backend**: Java Spring Boot, JPA, MariaDB
- **Deployment**: AWS EC2, Docker
- **Testing**: JUnit5, Mockito
- **Documentation**: Spring REST Docs

---

## 👥 팀원 역할분담
| 이름        | 역할                  | 주요 담당 업무                                                   |
| ----------- | --------------------- | --------------------------------------------------------------- |
| 김성호      | LEADER                | 주문 관리, 리뷰 관리, 결제 서비스 관리                           |
| 김예원      | MEMBER                | 식당 서비스 관리, 카테고리 및 메뉴 관리                          |
| 김민주      | MEMBER                | 회원 관리 및 AI 연동                                            |

---

## 🗂️ 테이블 명세서 및 ERD (Entity Relationship Diagram)


![image](https://github.com/user-attachments/assets/81911ac3-ec35-4a4f-bb98-e68b3e70a8d4)

<details>

### p_users 테이블

| 필드 이름 | 데이터 타입 | 설명 |
| --- | --- | --- |
| `id` | `uuid` | PRIMARY KEY |
| `username` | `VARCHAR(50)` | 사용자 ID |
| `password` | `VARCHAR(50)` | 사용자 비밀번호 |
| `role` | `VARCHAR(20)` | 사용자 권한 |
| `address` | `VARCHAR(255)` | 사용자 주소 |
| `created_at` | `TIMESTAMP` | 레코드 생성 시점 |
| `created_by`  | `VARCHAR(255)` | 레코드 생성자(username) |
| `updated_at` | `TIMESTAMP` | 레코드 수정 시점 |
| `updated_by` | `VARCHAR(255)` | 레코드 수정자(username) |
| `deleted_at` | `TIMESTAMP` | 레코드 삭제 시점 |
| `deleted_by` | `VARCHAR(255)` | 레코드 삭제자(username) |

### p_categories 테이블

| 필드 이름 | 데이터 타입 | 설명 |
| --- | --- | --- |
| `id` | `uuid` | PRIMARY KEY |
| `name` | `VARCHAR(50)` | 카테고리 이름 |
| `created_at` | `TIMESTAMP` | 레코드 생성 시점 |
| `created_by`  | `VARCHAR(255)` | 레코드 생성자(username) |
| `updated_at` | `TIMESTAMP` | 레코드 수정 시점 |
| `updated_by` | `VARCHAR(255)` | 레코드 수정자(username) |
| `deleted_at` | `TIMESTAMP` | 레코드 삭제 시점 |
| `deleted_by` | `VARCHAR(255)` | 레코드 삭제자(username) |

### p_ai_descriptions 테이블

| 필드 이름 | 데이터 타입 | 설명 |
| --- | --- | --- |
| `id` | `uuid` | PRIMARY KEY |
| `ai_request` | `text` | AI 프롬프트에 입력한 내용 |
| `ai_response` | `text` | AI 프롬프트에서 나온 결과물 |
| `created_at` | `TIMESTAMP` | 레코드 생성 시점 |
| `created_by`  | `VARCHAR(255)` | 레코드 생성자(username) |
| `updated_at` | `TIMESTAMP` | 레코드 수정 시점 |
| `updated_by` | `VARCHAR(255)` | 레코드 수정자(username) |
| `deleted_at` | `TIMESTAMP` | 레코드 삭제 시점 |
| `deleted_by` | `VARCHAR(255)` | 레코드 삭제자(username) |

### p_menus 테이블

| 필드 이름 | 데이터 타입 | 설명 |
| --- | --- | --- |
| `id` | `uuid` | PRIMARY KEY |
| `restaurant_id` | `uuid` | 식당 ID, p_restaurants 참조 |
| `name` | `VARCHAR(255)` | 메뉴 이름 |
| `description` | `text` | AI API로 생성되는 메뉴 설명 |
| `price` | `int` | 메뉴 가격 |
| `is_hidden` | `boolean` | 메뉴의 숨김 여부 |
| `created_at` | `TIMESTAMP` | 레코드 생성 시점 |
| `created_by`  | `VARCHAR(255)` | 레코드 생성자(username) |
| `updated_at` | `TIMESTAMP` | 레코드 수정 시점 |
| `updated_by` | `VARCHAR(255)` | 레코드 수정자(username) |
| `deleted_at` | `TIMESTAMP` | 레코드 삭제 시점 |
| `deleted_by` | `VARCHAR(255)` | 레코드 삭제자(username) |

### p_order_items 테이블

| 필드 이름 | 데이터 타입 | 설명 |
| --- | --- | --- |
| `id` | `uuid` | PRIMARY KEY, 주문 항목 고유 식별자 |
| `order_id` | `uuid` | 주문 ID, p_orders 참조 |
| `menu_id` | `uuid` | 주문한 메뉴의 ID, p_menus 참조 |
| `quantity` | `int` | 메뉴 수량 |
| `created_at` | `TIMESTAMP` | 레코드 생성 시점 |
| `created_by`  | `VARCHAR(255)` | 레코드 생성자(username) |
| `updated_at` | `TIMESTAMP` | 레코드 수정 시점 |
| `updated_by` | `VARCHAR(255)` | 레코드 수정자(username) |
| `deleted_at` | `TIMESTAMP` | 레코드 삭제 시점 |
| `deleted_by` | `VARCHAR(255)` | 레코드 삭제자(username) |

### p_reviews 테이블

| 필드 이름 | 데이터 타입 | 설명 |
| --- | --- | --- |
| `id` | `uuid` | PRIMARY KEY |
| `order_id` | `uuid` | 주문 id, p_orders 참조 |
| `rating` | `int` | 별점 (1~5) |
| `comment` | `text` | 댓글 |
| `created_at` | `TIMESTAMP` | 레코드 생성 시점 |
| `created_by`  | `VARCHAR(255)` | 레코드 생성자(username) |
| `updated_at` | `TIMESTAMP` | 레코드 수정 시점 |
| `updated_by` | `VARCHAR(255)` | 레코드 수정자(username) |
| `deleted_at` | `TIMESTAMP` | 레코드 삭제 시점 |
| `deleted_by` | `VARCHAR(255)` | 레코드 삭제자(username) |

### p_restraunts 테이블

| 필드 이름 | 데이터 타입 | 설명 |
| --- | --- | --- |
| `id` | `uuid` | PRIMARY KEY |
| `name` | `VARCHAR(255)` | 가게 이름 |
| `category_id` | `uuid` | 가게가 속하고 있는 카테고리, p_categories.id 참조 |
| `owner_id` | `INTEGER` | 가게 소유자, p_users.id 참조 |
| `address` | `VARCHAR(255)` | 가게 주소지 |
| `is_hidden` | `BOOLEAN` | 가게의 숨김 처리 여부, default: `FALSE` |
| `created_at` | `TIMESTAMP` | 레코드 생성 시점 |
| `created_by` | `VARCHAR(255)` | 레코드 생성자(username) |
| `updated_at` | `TIMESTAMP` | 레코드 수정 시점 |
| `updated_by` | `VARCHAR(255)` | 레코드 수정자(username) |
| `deleted_at` | `TIMESTAMP` | 레코드 삭제 시점 |
| `deleted_by` | `VARCHAR(255)` | 레코드 삭제자(username) |

### p_orders 테이블

| 필드 이름 | 데이터 타입  | 설명 |
| --- | --- | --- |
| `id` | `UUID` | PRIMARY KEY |
| `user_id` | `INTEGER` | 사용자 ID, p_users 참조 |
| `restaurant_id` | `UUID` | 식당 ID, p_restaurants 참조 |
| `order_type` | `VARCHAR(20)` | 온라인 / 오프라인 주문 식별 |
| `status` | `VARCHAR(20)` | 현재 주문 상태 |
| `total_price` | `int` | 주문의 총 가격 |
| `delivery_address` | `VARCHAR(255)` | 사용자 배송지 |
| `delivery_request` | `text` | 사용자 배송 요청사항 |
| `created_at` | `TIMESTAMP` | 레코드 생성 시점 |
| `created_by` | `VARCHAR(255)` | 레코드 생성자(username) |
| `updated_at` | `TIMESTAMP` | 레코드 수정 시점 |
| `updated_by` | `VARCHAR(255)` | 레코드 수정자(username) |
| `cancelled_at`  | `TIMESTAMP`  | 레코드 취소 시점 |
| `deleted_at` | `TIMESTAMP` | 레코드 삭제 시점 |
| `deleted_by` | `VARCHAR(255)` | 레코드 삭제자(username) |
|  |  |  |

### p_payments 테이블

| 필드 이름 | 데이터 타입 | 설명 |
| --- | --- | --- |
| `id` | `uuid` | PRIMARY KEY |
| `order_id` | `uuid` | 결제와 연관된 주문 ID, p_orders 참조 |
| `amount` | `int` | 결제된 총 금액 |
| `payment_method` | `VARCHAR(50)` | 결제 방식을 나타내는 문자열 (카드 결제만 지원하므로 생략 가능) |
| `pg_response` | `TEXT` | 결제 게이트웨이(PG사)에서 받은 응답 저장 |
| `created_at` | `TIMESTAMP` | 레코드 생성 시점 |
| `created_by` | `VARCHAR(255)` | 레코드 생성자(username) |
| `updated_at` | `TIMESTAMP` | 레코드 수정 시점 |
| `updated_by` | `VARCHAR(255)` | 레코드 수정자(username) |
| `deleted_at` | `TIMESTAMP` | 레코드 삭제 시점 |
| `deleted_by` | `VARCHAR(255)` | 레코드 삭제자(username) |
</details>


---
