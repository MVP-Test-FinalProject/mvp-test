

## 프로젝트 개요



# 📖 MVP 테스트 플랫폼 MVPQUEST


![image (1)](https://github.com/user-attachments/assets/8d0ba67e-dd69-4d81-990e-c203ff13b922)

<br>

## 프로젝트 소개

#### 새로운 기능이나 서비스를 테스트하는 MVP 테스트 플랫폼 입니다. 기업은 새로운 기능 또는 제품을 테스트하여 신속하게 결함을 발견할 수 있고,
사용자는 단계별 테스트를 완료하고 리워드를 받을 수 있습니다.<br>

<br>

## 팀원 구성

<div align="center">

| **김민수(리더)** | **이승현(부리더)** | **정재범** | **이지원** |**한동헌**|
| :------: |  :------: | :------: | :------: | :------: |
| [<img src="https://ca.slack-edge.com/T06B9PCLY1E-U06UJKABTJQ-ceccb463739e-512" height=150 width=150> <br/> @AWKRID](https://github.com/yeon1615) | [<img src="https://ca.slack-edge.com/T06B9PCLY1E-U06TKQ52RGT-060a60402423-192" height=150 width=150> <br/> @seunghyun](https://github.com/Cheorizzang) | [<img src="https://ca.slack-edge.com/T06B9PCLY1E-U06NVBLH9FA-365d628c1140-512" height=150 width=150> <br/> @Poqcqc](https://github.com/heejiyang) | [<img src="https://ca.slack-edge.com/T06B9PCLY1E-U06PN7MAZK3-b624ee8a13e7-512" height=150 width=150> <br/> @gooddle](https://github.com/journey-ji) |[<img src="https://ca.slack-edge.com/T06B9PCLY1E-U06NTKSN5CJ-ec0e721cf435-512" height=150 width=150> <br/> @dongheon0827](https://github.com/journey-ji)

</div>

<br>

## 1. 기술 스택


<br>

## 2. 주요 기능

- 기업
  - MVP 테스트 업로드 기능
    - 기업은 런칭한 서비스의 테스트를 업로드할 수 있습니다. 각 테스트는 step 이 나누어져 있으며, 각 step 마다 리워드 지급 규정을 가집니다.
  - 테스트 종료 시 리워드 일괄 지급
    - 테스트 일정 종료 시, 리워드 규정과 각 참여자의 진행 단계에 따라 리워드를 일괄 지급합니다. 
  - 테스트 참여자의 승인/거부
    - 일부 테스트의 경우(특정한 소비층이 필요한 경우) 기업에서 테스트 참여자의 정보를 판단하여 테스터로서의 참여 여부를 승인/거절할 수 있습니다. 일반적으로는 선착순으로 참여가 결정됩니다.
- 사용자
  - 테스트 참여 후 단계별 보고서 작성
    - 사용자(일반 회원)는 테스트에 참여 후, 어떤 단계(step)까지 진행했는지에 대한 결과를 보고서 형태로 작성합니다. 보고서는 글과 사진으로 테스트 진행 내용을 증명할 수 있습니다.
- 관리자
  - 기업 회원/일반 회원의 가입 관리 및 제재
    - 관리자는 일반 회원과 기업 회원의 가입을 관리하며, 부정한 사용이 의심되는 사용자/기업회원을 제재할 수 있습니다.


<br>

## 3. 서비스 아키텍쳐 및 배포 전략

### 서비스 아키텍쳐
![image (2)](https://github.com/user-attachments/assets/5877f897-2ab4-437e-9af1-249df0177655)


### 배포 전략
![image (3)](https://github.com/user-attachments/assets/76d72631-1ff6-4d28-b414-4ecefb373227)


<br>
### API 명세서
https://leather-antimony-86c.notion.site/MVPQuest-API-dc845bb76a4545889965a55837c2d49a?pvs=25

<br>
## 4. 프로젝트 구조
```
.
├── main
│   ├── kotlin
│   │   └── com
│   │       └── team1
│   │           └── mvp_test
│   │               ├── MvpTestApplication.kt
│   │               ├── admin
│   │               │   ├── controller
│   │               │   │   ├── AdminController.kt
│   │               │   │   └── AdminRoleController.kt
│   │               │   ├── dto
│   │               │   │   ├── AdminLoginRequest.kt
│   │               │   │   ├── AdminLoginResponse.kt
│   │               │   │   └── adminauthority
│   │               │   │       ├── MvpTestListResponse.kt
│   │               │   │       └── RejectRequest.kt
│   │               │   ├── model
│   │               │   │   └── Admin.kt
│   │               │   ├── repository
│   │               │   │   └── AdminRepository.kt
│   │               │   └── service
│   │               │       ├── AdminAuthService.kt
│   │               │       └── AdminService.kt
│   │               ├── batch
│   │               │   ├── BatchScheduler.kt
│   │               │   ├── Job.kt
│   │               │   ├── Step.kt
│   │               │   ├── exception
│   │               │   │   └── BatchJobAlreadyDoneException.kt
│   │               │   ├── model
│   │               │   │   ├── BatchJobExecution.kt
│   │               │   │   ├── BatchJobInstance.kt
│   │               │   │   └── BatchStepExecution.kt
│   │               │   ├── repository
│   │               │   │   ├── BatchJobExecutionRepository.kt
│   │               │   │   ├── BatchJobInstanceRepository.kt
│   │               │   │   └── BatchStepExecutionRepository.kt
│   │               │   ├── reward
│   │               │   │   ├── CreateSettlementDataStep.kt
│   │               │   │   ├── PayMemberRewardStep.kt
│   │               │   │   └── RewardSettlementJob.kt
│   │               │   └── service
│   │               │       └── BatchService.kt
│   │               ├── common
│   │               │   ├── Role.kt
│   │               │   ├── error
│   │               │   │   ├── AdminErrorMessage.kt
│   │               │   │   ├── CategoryErrorMessage.kt
│   │               │   │   ├── EnterpriseErrorMessage.kt
│   │               │   │   ├── MemberErrorMessage.kt
│   │               │   │   ├── MvpTestErrorMessage.kt
│   │               │   │   ├── ReportErrorMessage.kt
│   │               │   │   ├── S3ErrorMessage.kt
│   │               │   │   └── StepErrorMessage.kt
│   │               │   └── exception
│   │               │       ├── GlobalExceptionHandler.kt
│   │               │       ├── ModelNotFoundException.kt
│   │               │       ├── NoPermissionException.kt
│   │               │       ├── PasswordIncorrectException.kt
│   │               │       └── dto
│   │               │           └── ErrorResponse.kt
│   │               ├── domain
│   │               │   ├── category
│   │               │   │   ├── controller
│   │               │   │   │   └── CategoryController.kt
│   │               │   │   ├── dto
│   │               │   │   │   └── CreateCategoryRequest.kt
│   │               │   │   ├── model
│   │               │   │   │   └── Category.kt
│   │               │   │   ├── repository
│   │               │   │   │   └── CategoryRepository.kt
│   │               │   │   └── service
│   │               │   │       └── CategoryService.kt
│   │               │   ├── enterprise
│   │               │   │   ├── controller
│   │               │   │   │   └── EnterpriseController.kt
│   │               │   │   ├── dto
│   │               │   │   │   ├── EnterpriseLoginResponse.kt
│   │               │   │   │   ├── EnterpriseResponse.kt
│   │               │   │   │   ├── EnterpriseSignUpRequest.kt
│   │               │   │   │   ├── LoginRequest.kt
│   │               │   │   │   └── UpdateEnterpriseRequest.kt
│   │               │   │   ├── model
│   │               │   │   │   ├── Enterprise.kt
│   │               │   │   │   └── EnterpriseState.kt
│   │               │   │   ├── repository
│   │               │   │   │   └── EnterpriseRepository.kt
│   │               │   │   └── service
│   │               │   │       ├── EnterpriseAuthService.kt
│   │               │   │       └── EnterpriseService.kt
│   │               │   ├── member
│   │               │   │   ├── controller
│   │               │   │   │   └── MemberController.kt
│   │               │   │   ├── dto
│   │               │   │   │   ├── LoginResponse.kt
│   │               │   │   │   ├── MemberResponse.kt
│   │               │   │   │   ├── MemberUpdateRequest.kt
│   │               │   │   │   ├── MemberUpdateResponse.kt
│   │               │   │   │   └── SignUpInfoRequest.kt
│   │               │   │   ├── model
│   │               │   │   │   ├── Member.kt
│   │               │   │   │   ├── MemberReward.kt
│   │               │   │   │   ├── MemberState.kt
│   │               │   │   │   ├── MemberTest.kt
│   │               │   │   │   ├── MemberTestState.kt
│   │               │   │   │   └── Sex.kt
│   │               │   │   ├── repository
│   │               │   │   │   ├── MemberRepository.kt
│   │               │   │   │   ├── MemberRewardRepository.kt
│   │               │   │   │   └── MemberTestRepository.kt
│   │               │   │   └── service
│   │               │   │       └── MemberService.kt
│   │               │   ├── message
│   │               │   │   ├── VerifyCodeResponse.kt
│   │               │   │   ├── controller
│   │               │   │   │   └── MessageController.kt
│   │               │   │   └── service
│   │               │   │       └── MessageService.kt
│   │               │   ├── mvptest
│   │               │   │   ├── controller
│   │               │   │   │   └── MvpTestController.kt
│   │               │   │   ├── dto
│   │               │   │   │   ├── CreateMvpTestRequest.kt
│   │               │   │   │   ├── MemberInfoResponse.kt
│   │               │   │   │   ├── MvpTestResponse.kt
│   │               │   │   │   └── UpdateMvpTestRequest.kt
│   │               │   │   ├── model
│   │               │   │   │   ├── MvpTest.kt
│   │               │   │   │   ├── MvpTestCategoryMap.kt
│   │               │   │   │   ├── MvpTestState.kt
│   │               │   │   │   ├── RecruitType.kt
│   │               │   │   │   └── UpdateMvpTestObject.kt
│   │               │   │   ├── repository
│   │               │   │   │   ├── MvpTestCategoryMapRepository.kt
│   │               │   │   │   └── MvpTestRepository.kt
│   │               │   │   └── service
│   │               │   │       └── MvpTestService.kt
│   │               │   ├── oauth
│   │               │   │   ├── client
│   │               │   │   │   ├── OAuthClient.kt
│   │               │   │   │   ├── OAuthClientService.kt
│   │               │   │   │   ├── OAuthLoginUserInfo.kt
│   │               │   │   │   ├── google
│   │               │   │   │   │   ├── GoogleOAuthClient.kt
│   │               │   │   │   │   └── dto
│   │               │   │   │   │       ├── GoogleOAuthUserInfo.kt
│   │               │   │   │   │       └── GoogleTokenResponse.kt
│   │               │   │   │   └── naver
│   │               │   │   │       ├── NaverOAuthClient.kt
│   │               │   │   │       └── dto
│   │               │   │   │           ├── NaverOAuthUserInfo.kt
│   │               │   │   │           └── NaverTokenResponse.kt
│   │               │   │   ├── config
│   │               │   │   │   └── RestClientConfig.kt
│   │               │   │   ├── controller
│   │               │   │   │   └── MemberOAuthController.kt
│   │               │   │   ├── exception
│   │               │   │   │   ├── InvalidOAuthUserException.kt
│   │               │   │   │   └── OAuthTokenRetrieveException.kt
│   │               │   │   ├── provider
│   │               │   │   │   ├── OAuthProvider.kt
│   │               │   │   │   └── OAuthProviderConverter.kt
│   │               │   │   └── service
│   │               │   │       └── OAuthLoginService.kt
│   │               │   ├── report
│   │               │   │   ├── controller
│   │               │   │   │   └── ReportController.kt
│   │               │   │   ├── dto
│   │               │   │   │   ├── ApproveReportRequest.kt
│   │               │   │   │   ├── ReportRequest.kt
│   │               │   │   │   └── ReportResponse.kt
│   │               │   │   ├── model
│   │               │   │   │   ├── Report.kt
│   │               │   │   │   ├── ReportMedia.kt
│   │               │   │   │   └── ReportState.kt
│   │               │   │   ├── repository
│   │               │   │   │   ├── ReportMediaRepository.kt
│   │               │   │   │   └── ReportRepository.kt
│   │               │   │   └── service
│   │               │   │       └── ReportService.kt
│   │               │   ├── settlement
│   │               │   │   ├── Settlement.kt
│   │               │   │   └── SettlementRepository.kt
│   │               │   └── step
│   │               │       ├── controller
│   │               │       │   └── StepController.kt
│   │               │       ├── dto
│   │               │       │   ├── CreateStepRequest.kt
│   │               │       │   ├── ReportStatusResponse.kt
│   │               │       │   ├── StepListResponse.kt
│   │               │       │   ├── StepOverviewResponse.kt
│   │               │       │   ├── StepResponse.kt
│   │               │       │   └── UpdateStepRequest.kt
│   │               │       ├── model
│   │               │       │   └── Step.kt
│   │               │       ├── repository
│   │               │       │   └── StepRepository.kt
│   │               │       └── service
│   │               │           ├── MemberReportService.kt
│   │               │           └── StepService.kt
│   │               └── infra
│   │                   ├── healthcheck
│   │                   │   └── HealthCheckController.kt
│   │                   ├── querydsl
│   │                   │   └── QueryDslConfig.kt
│   │                   ├── redis
│   │                   │   ├── RedisConfig.kt
│   │                   │   └── RedisService.kt
│   │                   ├── redisson
│   │                   │   ├── RedissonConfig.kt
│   │                   │   └── RedissonService.kt
│   │                   ├── s3
│   │                   │   ├── s3config
│   │                   │   │   └── S3Config.kt
│   │                   │   └── s3service
│   │                   │       └── S3Service.kt
│   │                   ├── security
│   │                   │   ├── CustomAuthenticationEntryPoint.kt
│   │                   │   ├── PasswordEncodeConfig.kt
│   │                   │   ├── SecurityConfig.kt
│   │                   │   ├── UserPrincipal.kt
│   │                   │   └── jwt
│   │                   │       ├── JwtAuthenticationFilter.kt
│   │                   │       ├── JwtAuthenticationToken.kt
│   │                   │       └── JwtHelper.kt
│   │                   └── swagger
│   │                       └── SwaggerConfig.kt
│   └── resources
│       └── application.yml
└── test
    ├── kotlin
    │   └── com
    │       └── team1
    │           └── mvp_test
    │               └── domain
    │                   ├── batch
    │                   │   ├── RewardSettlementJobFailTest.kt
    │                   │   ├── RewardSettlementJobIntegrateTest.kt
    │                   │   └── RewardSettlementJobUnitTest.kt
    │                   ├── enterprise
    │                   │   ├── dto
    │                   │   │   └── EnterpriseDtoTest.kt
    │                   │   └── service
    │                   │       ├── EnterpriseAuthServiceTest.kt
    │                   │       └── EnterpriseServiceTest.kt
    │                   ├── member
    │                   │   ├── dto
    │                   │   │   └── MemberDtoTest.kt
    │                   │   └── service
    │                   │       └── MemberServiceUnitTest.kt
    │                   ├── mvptest
    │                   │   ├── dto
    │                   │   │   └── MvpTestDtoTest.kt
    │                   │   └── service
    │                   │       ├── MvpTestIntegrationTest.kt
    │                   │       ├── MvpTestServiceTest.kt
    │                   │       ├── MvpTestTestConfig.kt
    │                   │       └── apply
    │                   │           ├── ConcurrencyControl.kt
    │                   │           └── RedissonTestConfig.kt
    │                   ├── report
    │                   │   ├── dto
    │                   │   │   └── ReportDtoTest.kt
    │                   │   └── service
    │                   │       └── ReportServiceTest.kt
    │                   └── step
    │                       ├── dto
    │                       │   └── StepDtoTest.kt
    │                       └── service
    │                           ├── StepIntegrationTest.kt
    │                           ├── StepServiceTest.kt
    │                           └── StepTestConfig.kt
    └── resources
        ├── application-test.yml
        └── redisson.yml

```




