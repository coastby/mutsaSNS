### 프로젝트 목표

- SpringBoot + JPA + JWT 를 이용하여 유사한 SNS 서비스를 만든다.


### 프로젝트 결과

[📎 노션 프로젝트 페이지](https://www.notion.so/0111dd15420243a19ed94263bc07932d)

[📎 Swagger-ui](http://ec2-13-125-252-76.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/)

[📎 서비스 UI](http://ec2-13-125-252-76.ap-northeast-2.compute.amazonaws.com:8080/)

[📎 테스트코드 결과](https://mutsasns-test-results.netlify.app/)

### 미션 요구사항 분석 & 체크리스트

#### 📌 프로젝트 기술스택
- 에디터 : Intellij Ultimate
- 개발 툴 : SpringBoot 2.7.6
- 자바 : JAVA 11
- 빌드 : Gradle 6.8
- 서버 : AWS EC2
- 데이터베이스 : MySql 8.0
- 필수 라이브러리 : SpringBoot Web, MySQL, Spring Data JPA, Lombok, Spring Security, JWT, Swagger, Oauth2-client

#### 📌 기능명세서

![img_1.png](img_1.png)

#### 📌 ERD ([erdcloud](https://www.erdcloud.com/d/QRsmFiJDNvBxfQXqm))

![img.png](img.png)

#### 📌 ERROR CODE
| 에러 코드 | 설명                                                                           | HTTP status |
| --- |------------------------------------------------------------------------------|-------------|
| DUPLICATED_USER_NAME | 중복된 아이디로 가입을 요청한 경우                                                          | 409         |
| USERNAME_NOT_FOUND | 가입되지 않은 아이디로 요청한 경우                                                          | 404         |
| INVALID_PASSWORD | 비밀번호가 틀린 경우                                                                  | 401         |
| INVALID_TOKEN | 유효하지 않은 토큰으로 요청한 경우                                                          | 401         |
| INVALID_PERMISSION | 해당 API에 대한 요청 권한이 없는 경우,<br> 올바르지 않은 헤더로 요청한 경우,<br> 게시글에 대한 수정/삭제 권한이 없는 경우 | 401         |
| POST_NOT_FOUND | 존재하지 않는 게시물을 요청한 경우                                                          | 404         |
| COMMENT_NOT_FOUND | 존재하지 않는 댓글을 요청한 경우                                                           | 404         |
| DATABASE_ERROR | DB와의 연결 이상인 경우                                                               | 500         |
| INTERNAL_SERVER_ERROR | 서버에서 요청 처리 중 오류가 발생한 경우                                                      | 500         |
| INVALID_VALUE | 지원하지 않는 포맷 입력                                                                | 400         |
| INVALID_REQUEST | 올바르지 않은 방법으로 요청한 경우, <br>좋아요를 2회이상 요청한 경우 | 409         |


### 1️⃣ 체크리스트
- [x] 요구사항정의서 작성

#### ‣ 인프라
- [x] [EC2 생성 및 도커 설치](https://velog.io/@coastby/AWS-Docker-aws-ec2-instance%EC%97%90-docker-%EC%84%A4%EC%B9%98%ED%95%98%EA%B8%B0)
- [x] 프로젝트 빌드
- [x] [GitLab CI/CD 구축](https://gaudy-vein-afc.notion.site/GitLab-CI-CD-20a04d8afcd0448bba16d7844d10f038)

#### ‣ DB
- [x] [DB 설계](https://gaudy-vein-afc.notion.site/DB-abf6dc6e5d2b43dcb3174cc459a95af0)
- [x] [MySql 구축](https://velog.io/@coastby/AWS-Docker-aws-ec2-instance%EC%97%90-docker-%EC%84%A4%EC%B9%98%ED%95%98%EA%B8%B0)

#### ‣ 프로젝트 설정
- [x] [entity 생성](https://gaudy-vein-afc.notion.site/entity-b93c520ad9564eeba3a03f3ec58a658b)
- [x] [security 설정](https://gaudy-vein-afc.notion.site/security-79dcd316427c47cdb1e03e1848642b68)
- [x] [예외처리, custom exception](https://gaudy-vein-afc.notion.site/d97e891832194deeab9895892dc9673e)

#### ‣ 기능 개발
회원
- [x] [회원 가입](https://gaudy-vein-afc.notion.site/36bcc1fbd6b5482a8a900d1f2adc2417) / 테스트코드
- [x] 로그인 / 테스트코드
- [x] [권한변경](https://gaudy-vein-afc.notion.site/ADMIN-73b969019ea344e78d0f21de600381e6) / 테스트코드

포스트
- [x] 포스트 리스트 조회 / 테스트코드
- [x] 포스트 작성 / [테스트코드](https://gaudy-vein-afc.notion.site/15ad2c245bba46b7a49d5eff63487af9)
- [x] [포스트 수정/삭제](https://gaudy-vein-afc.notion.site/ec91f4f5fe0c47b69c1820dbbd5ba947) / [테스트코드](https://gaudy-vein-afc.notion.site/fac2f76f2468463d8ea83e2c84bd1be3)
- [x] [포스트 상세 조회](https://gaudy-vein-afc.notion.site/235378165a0e42fd941893de7f1bd0cc) / 테스트코드

문서
- [x] [Swagger annotaion](https://gaudy-vein-afc.notion.site/swagger-annotaion-9d2df2a64508411bb3394a1b296ddf1d)
- [x] API 기능 명세서 작성


#### ‣ 기능 개발
댓글
- [x] 리스트 조회
- [x] 댓글 작성
- [x] 댓글 수정/삭제

좋아요
- [x] 좋아요 누르기
- [x] 좋아요 개수 조회


- [x] 알람
- [x] 마이피드

#### ‣ [OAuth](https://gaudy-vein-afc.notion.site/OAuth-d362d0e5ca634527af779c1564550053)
- [x] 기능 구현

#### ‣ [UI 개발](https://gaudy-vein-afc.notion.site/UI-ea15ce629ddb44458d1287f70f52695f)
- [x] 기능 개발
  ![img_4.png](img_4.png)



### 1️⃣ 주차 미션 요약

---

**[접근 방법]**

[📎 노션 프로젝트 페이지](https://www.notion.so/0111dd15420243a19ed94263bc07932d)


- GitLab, Docker, EC2를 이용한 <u>CI/CD 구현</u>
- DB 설계 및 ERD 작성
- 포스트 논리삭제 (soft delete) 구현

- Security
  - <u>Jwt</u>를 이용한 인증 구현
  - FilterChain 상의 예외 처리
  - URI별로 인증, 권한 설정

- Test Code
  - Security를 포함하는 테스트코드 작성
  - @WithMockCustomUser를 이용하여 authentication 커스텀
  - JwtFilter 자체를 테스트하기 위한 코드 작성
  - 테스트코드 관련 annotation 활용
  - MockMvc를 이용한 테스트 코드 구현

- ADMIN 권한 설정
  - security를 이용하여 권한에 따른 접근 범위 설정

- Swagger
  - annotation
  - API 문서 설계 시 참조

- OAuth2 적용
  - 구글, 네이버 로그인을 진행하면 기존의 로그인 방식대로 JWT 토큰 발급

- UI 개발
  - 포스트 조회, 댓글 조회, 댓글 작성, 좋아요 누르기 가능
  - 로그인, 포스트/댓글 작성/수정/삭제 추후 개발 예정

**[특이사항]**

#### 신경썼던 부분
구현하면서 가장 어렵고 시간이 많이 걸렸던 부분은 아무래도 🔒 **Security**와 ✅ **테스트코드**였다. 특히나 Security가 맞물린 테스트코드는 몹시 힘들었다.
그래도 해보고 싶은 것들은 다 해보아서 뿌듯하다.
<br> 또한 이번에 **CI/CD**를 처음 적용해보았는데, 너무나 편하고 잡무가 확 줄었다. `.gitlab-ci.yml`, `Dockerfile`, 배포 스크립트 등을 직접 찾아보고 개선하는 과정에서 새로운 문법을 익히느라 시간은 오래 걸렸지만 재미있기는 했다.

#### 개선해야할 부분
수많은 **DTO와 메서드들의 명칭**들을 좀 더 명확하게 손보고 싶다.
또한 복잡한 부분은 주석을 사용하기는 했지만, 다른 사람과 공유하기에는 부족한 부분이 많은 듯하다. 한 눈에 <u>알아보기 쉽게 주석과 변수 명칭을 개선</u>할 것이다.



### 2️⃣ 주차 미션 요약

---

**[접근 방법]**
- 댓글, 좋아요, 알람 개발
  - ERD를 이용하여 엔티티간의 관계 정리
  - 단일 책임 원칙을 준수하기 위한 고민

- OAuth2 적용
  - 구글(/oauth2/authorization/google), 네이버(/oauth2/authorization/naver) 로그인을 진행하면 기존의 로그인 방식대로 JWT 토큰 발급
  - 발급 후 리다이렉트 및 리프레시 토큰 적용 보완 예정

- UI 개발
  - [AJAX](https://velog.io/@coastby/2%EC%A3%BC%EC%B0%A8%EC%9B%B9%EA%B0%9C%EB%B0%9C-%EA%B0%9C%EB%B0%9C%EC%9D%BC%EC%A7%80-ajax)(Asynchronous Javascript and XML)를 이용하여 RESTful 웹서비스를 호출하는 HTML페이지 작성
  - AJAX를 쉽게 구현하기 위해 jquery 이용
  - 포스트 조회/작성, 댓글 조회/작성, 좋아요 누르기/조회, 로그인, 알람 기능 구현

- 테스트코드 결과 화면
  - 테스트 결과 html export
  - netlify를 통해 html 화면 배포

**[특이사항]**

#### 신경썼던 부분
이번에도 spring-security 관련하여 이해해야할 부분이 많았다. 디버깅을 하여 security 과정을 몇 번이나 보면서 조금 더 이해를 하게되었다. <br>
<br>


#### 개선해야할 부분
1. **토큰 저장 및 안전성 확보**

   token을 어디다 저장할 지는 많은 사람들이 이미 고민을 하였다. 일단 먼저 구현하기 위해 제일 간편한 쿠키에 저장을 하였으나 보안에 취약하다. 사실 쿠키, 세션, 로컬 스토리지 어디에 보관하여도 위험성은 있다. 이를 보완하기 위해 refresh token을 사용하는데 아직 이해가 부족하여 제대로 구현하지 못하였다. 또한 현재 로직에서는 refresh token을 유저 엔티티에 같이 저장하는데 이도 수정하여 Redis 서버 이용 등을 고려해 볼 것이다.


2. **회원가입 유저와 OAuth 유저 구분**

   OAuth 서버에서 받아올 수 있는 정보가 한정되어 유저의 정보가 회원가입한 유저와 많이 다르다. 이를 통일시키기 위해 nickname 등을 설정할 수 있도록 해야할 것이다.


3. **로그인 후 중복되는 controller 로직**

   controller에서 로그인된 사용자의 정보를 Authentication에서 가져오는 로직이 매우 중복된다. 이를 개선하기 위해 interceptor에서 인가처리를 구현할 수 있었다. 그러면 controller단과 service단의 로직이 간소화될 것이다.

4. **UI 사용자 경험 개선**

   UI를 구현하며 새로고침을 남발하였다. 비동기통신을 지향했음에도 그렇지 못한 부분들은 개선해야 한다. 댓글 및 좋아요 후 새로고침 되는 부분이 특히나 그렇다. 또한 아직 덜 완성된 알람기능은 SSE를 통해 구현할 예정이다.

5. **OAuth 관련 로직 개선**

   OAuthJWT 토큰 발급은 가능하지만 예외 상황 핸들링, 리다이렉트 등을 더 개선해야 한다. (깔끔하게 하기 위해 최선을 다했으나) 보안 로직 및 클래스들이 OAUth를 적용하면서 어수선해졌지만, 이는 리팩토링을 하며 깔끔하게 개선해 나갈 것이다.

6. **통합테스트**

   단위테스트는 mock으로 넣어주는 부분이 많아서 유기적인 흐름은 테스트하기가 어렵다고 느꼈다. 이러한 불안감을 해소하기 위해 시나리오 기반 테스트를 작성할 예정이었으나 다른 기능에 시간이 많이 들어가며 진행하지 못하였다. BDD까지는 아니더라도 추후의 리팩토링을 위해 필요한 테스트라 생각된다.


#### 회고
댓글, 좋아요, 알람 등 다양한 엔티티들이 생기면서 서로 간의 관계를 풀어가는 것이 어려웠다. 아직도 이들의 관계를 어느 클래스에서 다뤄야할 지는 명확하지 않다. 지금은 크게 문제가 없더라도 서비스를 키운다면 반드시 규칙을 만들어야겠다고 느껴졌다.<br>
OAuth를 적용하고 UI를 만들면서, (라이브러리들이 도와주었지만) 간접적이게나마 서버간의 통신을 경험할 수 있었다. 특히 UI를 만들며 API spec을 확인하기 위해 swagger를 유용하게 사용하였다. 프로젝트가 더 복잡해지면 이러한 문서화는 꼭 필요하겠다는 실감할 수 있었다.<br>
미리 설계를 하고 개발을 시작하지만 그럼에도 변경사항들이 많아서 처참하게 되어버린 코드들이 많다. 이를 리팩토링하는데에도 꽤 많은 시간이 소모되었다. 하지만 구현하면서 보이는 버그들도 많아서 설계에만 비중을 두기에는 비효율적인 부분도 있다. 나만의 개발 루틴을 만들어서 효율적으로 시간을 써야겠다는 생각을 했다.