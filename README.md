# Signal Market — Signal-BE

AI 검증 정보 카드 마켓플레이스 **Signal Market**의 백엔드 서버입니다.
작성자가 결과를 예측할 수 있는 형태로 정보를 발행하면, AI 에이전트가 근거·반대논리·기존 카드와의 중복도·작성자 과거 성과를 종합해 검증하고 가격을 제안합니다. 이후 예측 결과가 실제로 확인되면 그 성과가 작성자의 신뢰도 지표에 누적 반영됩니다.

## 아키텍처

기능 전체는 2개 저장소로 나뉘어 있습니다.

| 저장소 | 역할 | 스택 |
| --- | --- | --- |
| **Signal-BE** (이 저장소) | 도메인/비즈니스 로직, DB, API 서버 | Spring Boot 4.1.0, Java 17, PostgreSQL |
| [Signal-AI](../Signal-AI) | AI 에이전트(LangGraph) 서버 | Python, FastAPI, LangGraph, Groq(Llama) |

Signal-BE가 카드 검증/가격 산정/결과 판정/브리핑 생성이 필요할 때마다 HTTP로 Signal-AI를 호출하는 구조입니다.

```
Frontend → Signal-BE (Spring Boot, PostgreSQL) → Signal-AI (FastAPI, LangGraph, Groq)
```

## 주요 기능

- **카드 라이프사이클**: 초안 작성 → AI 검토(근거 연결성/반대논리/누락변수/실제 카드 중복도 종합 판정) → 가격 설정 → 발행
- **구매/저장**: 고정가 구매, 북마크, 구매 전/후 콘텐츠 마스킹(paywall)
- **결과 판정**: 실제 결과 제출 시 AI가 완전적중/부분적중/방향만적중/실패/판정불가 5단계로 판정
- **작성자 성과**: 판정 결과를 100/70/40/0점으로 누적해 적중률·등급(A/B/C)·추세(상승/하락) 산출, AI 가격 산정에도 반영
- **오늘의 AI 브리핑**: 최근 발행 카드 기반 홈 화면 인사이트 3건 자동 생성(하루 1회 캐시)
- **관심 주제 / 추천 카드**: 관심 토픽 기반 추천, 홈·마이페이지 통계 요약

## API

총 27개 REST API 중 프론트엔드가 실제로 사용하는 23개의 상세 명세는 [`docs/api-spec.md`](docs/api-spec.md)에 정리되어 있습니다 (기본정보/요구사항/Request/시스템처리/Response 형식).

## 로컬 실행

### 요구사항
- JDK 17
- Docker (PostgreSQL 로컬 구동용)
- [Signal-AI](../Signal-AI) 서버 (AI 검증/브리핑 기능 테스트 시 필요, `http://localhost:8001`)

### 1. DB 실행
```bash
docker compose up -d
```

### 2. 서버 실행
```bash
./gradlew bootRun
```
기본적으로 `http://localhost:8080`에서 뜨고, 애플리케이션 기동 시 `data.sql`로 더미 유저/카테고리/토픽/작성자 프로필이 시드됩니다. (로그인/회원가입은 구현되어 있지 않으며, 모든 API는 `userId` 등 파라미터로 사용자를 지정하는 더미 데이터 기반 구조입니다.)

### 환경 변수

| 변수 | 기본값 | 설명 |
| --- | --- | --- |
| `DB_URL` | `jdbc:postgresql://localhost:5432/signal` | PostgreSQL 접속 URL |
| `DB_USERNAME` | `signal` | DB 계정 |
| `DB_PASSWORD` | `signal` | DB 비밀번호 |
| `SIGNAL_AI_BASE_URL` | `http://localhost:8001` | Signal-AI 서버 주소 |
| `PORT` | `8080` | 서버 포트 (배포 환경에서 플랫폼이 주입) |

## 배포

Railway에 Signal-BE / Signal-AI / PostgreSQL 각각 별도 서비스로 배포되어 있으며, `main` 브랜치 push 시 자동 재배포됩니다.

- Signal-BE: `https://signal-be-production-10a9.up.railway.app`
- Signal-AI: `https://signal-ai-production-d7dd.up.railway.app`

## 브랜치 전략

기능 단위로 `feature/*` 브랜치를 파서 작업 후 `develop`에 머지하고, `develop`을 `main`에 머지하는 흐름을 따릅니다. 커밋은 기능별로 1~2개로 유지합니다.

## 기술 스택

- Java 17, Spring Boot 4.1.0 (Spring Data JPA, Validation, Web MVC)
- PostgreSQL 16
- Lombok
- Gradle
- Docker / Railway
