### 미션 요구사항 분석 & 체크리스트

---
[📎 노션 프로젝트 페이지](https://www.notion.so/0111dd15420243a19ed94263bc07932d)

[📎 Swagger-ui](http://ec2-13-125-252-76.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/)


---
![img.png](img.png)
![img_1.png](img_1.png)

- [x] 요구사항정의서 작성

#### 인프라
- [x] EC2 생성 및 도커 설치
- [x] 프로젝트 빌드
- [x] GitLab CI/CD 구축

#### DB
- [x] DB 설계
- [x] MySql 구축

#### 프로젝트 설정
- [x] entity 생성
- [x] security 설정
- [x] 예외처리, custom exception

#### 기능 개발
##### 회원
- [x] 회원 가입
- [x] 로그인

##### 포스트
- [x] 포스트 리스트 조회
- [x] 포스트 작성
- [x] 포스트 수정 / 삭제
- [x] 포스트 상세 조회

#### 문서
- [x] API 기능 명세서 작성





### 1주차 미션 요약

---

**[접근 방법]**

[📎 노션 프로젝트 페이지](https://www.notion.so/0111dd15420243a19ed94263bc07932d)

- 프로젝트 설계
  - GitLab, Docker, EC2를 이용한 CI/CD 구현
  - DB 설계 및 ERD 작성

- Security
  - Jwt를 이용한 인증 구현
  - FilterChain 상의 예외 처리
  - URI별로 인증, 권한 설정

- TestCode
  - Security를 포함하는 테스트코드 작성
  - CustomMockUser를 이용하여 athentication 자체제작
  - JwtFilter 자체를 테스트하기 위한 코드 작성
  - 테스트코드 관련 annotation 활용
  - MockMvc를 이용한 테스트 코드 구현

- ADMIN 권한 설정
  - security를 이용하여 권한에 따른 접근 범위 설정

- Swagger
  - annotation
  - API 문서 설계 시 참조

**[특이사항]**

구현하면서 가장 어렵고 시간이 많이 걸렸던 부분은 아무래도 Security와 테스트코드였다. 특히나 Security가 맞물린 테스트코드는 몹시 힘들었다.
해보고 싶은 것들은 다 해보긴 했지만, 아직 테스트 코드를 풍부하게 작성하지는 못했던 것 같다. 그리고 수많은 DTO들의 명칭들을 좀 더 명확하게 손보고 싶다.
또한 주석을 복잡한 부분은 사용하기는 했지만, 부족한 부분이 많은 듯하다. 한 눈에 알아보기 쉽게 주석도 신경을 써야겠다.