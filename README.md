# delievery_project

**김민주 담당 기능**
1. 회원 관리 기능
2. AI API 연동
3. WebSecurityConfig 보안 설정

### 1. User Management
- **회원가입**: 사용자 계정 생성 기능. 사용자명, 비밀번호, 주소, 역할 등을 입력받아 저장합니다.
- **로그인 및 인증**: JWT 토큰 기반 인증을 통해 사용자 로그인 기능을 구현했습니다.
- **회원 정보 조회**: 사용자 개인 정보를 조회할 수 있습니다.
- **회원 정보 수정**: 사용자명, 주소, 역할 등을 업데이트할 수 있습니다.
- **회원 탈퇴**: 사용자를 삭제 상태로 변경(soft delete)하여 탈퇴 처리합니다.

### 2. AI 연동
- **AI API 연동**: Google AI API를 활용해 질문에 간결한 응답을 제공하며, 응답 데이터를 DB에 저장하는 기능을 구현했습니다.
- **비용 절감 처리**: 요청 시 글자 수 제한 및 간결한 답변 요청을 통해 API 호출 비용을 최적화했습니다.

### 3. WebSecurityConfig 보안 설정
- **Spring Security 설정**
  - CSRF 비활성화: REST API의 특성을 고려해 CSRF 보호를 비활성화했습니다.
  - 세션 사용 안 함: JWT 기반 인증 방식을 사용하여 세션을 생성하지 않고, `STATELESS`로 설정했습니다.

- **JWT 인증 및 권한 관리**
  - `JwtAuthenticationFilter`와 `JwtAuthorizationFilter`를 추가하여 로그인 시 JWT 토큰 생성 및 요청 인증을 처리합니다.
  - 사용자 권한(Role)에 따라 API 접근 권한을 분리했습니다.

- **API별 접근 권한 설정**
  - `requestMatchers`를 통해 API별로 필요한 권한을 정의했습니다.
  - Ex: 
    - `/api/user/signup`, `/api/user/{username}`: 모든 사용자 접근 가능
    - `/api/order/`: `CUSTOMER` 권한 필요
    - `/api/review`: `CUSTOMER`, `OWNER` 등 다양한 권한 필요

- **필터 체인 관리**
  - `JwtAuthenticationFilter`는 로그인 요청 시 토큰을 발급하고, 
    `JwtAuthorizationFilter`는 각 요청의 JWT 유효성을 검사합니다.

## 특이사항
- **권한 관리**: MANAGER, MASTER와 같은 특정 권한은 고유 토큰을 통해 부여받도록 구현했습니다.
- **보안 강화**: 비밀번호 암호화 및 JWT 인증 방식을 활용하여 사용자 데이터를 보호합니다.
