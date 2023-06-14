# [Spring Cloud로 개발하는 마이크로서비스 애플리케이션(MSA)](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%81%B4%EB%9D%BC%EC%9A%B0%EB%93%9C-%EB%A7%88%EC%9D%B4%ED%81%AC%EB%A1%9C%EC%84%9C%EB%B9%84%EC%8A%A4/dashboard)

> `inflearn` - `이도원` [소스코드](https://github.com/joneconsulting/msa_with_spring_cloud)

- [ ] 2022/12/27 ~ ing

## Projects

### Configuration + Service discovery + Api gateway

- discovery-service - Discovery Service
  - Eureka Server
- config-service - Configuration service
- apigateway-service - Spring Cloud Gateway
  - Spring Cloud Gateway

### Sample codes

- first-service - example for apigateway service
- second-service - example for apigateway service

### E-commerce codes

- catalog-service
- order-service
- user-service(./user-service)
  - Eureka Client

## Contents

### Service Discovery

> Spring Cloud Netflix Eureka

- 각 마이크로서비스들을 등록 및 검색
- 요청이 왔을 때 요청에 따라서 필요한 서비스 위치 알려줌
- 클라이언트는 Load Balancer 혹은 API Gateway 에 자신이 필요한 요청 정보를 전달하고 이 요청이 Service Discovery 에 전달되어 필요한 서비스의 위치를 알려주게 됨
- 로드밸런서 역할

### API Gateway Service

- 사용자나 외부 시스템으로부터 요청을 단일화 하여 처리할 수 있도록 해줌
- 사용자가 설정한 라우팅 설정에 따라서 각 Endpoint로 클라이언트를 대신해서 요청하고 클라이언트에 전달해주는 Proxy 역할
- 시스템 내부구조는 숨기고, 외부 요청에 대해 적절한 형태로 가공해서 응답 가능
- 클라이언트에서 마이크로서비스를 직접호출하면 새로운 마이크로서비스 추가되거나 기존 마이크로서비스에 주소 변경, 파라미터 변경 등 변경이 발생하면 클라이언트에서도 수정이 발생
- 필터를 통해 각 마이크로서비스가 호출될 때 사전에 호출될 작업(인증... 등), 사후에 호출될 작업(로깅... 등)을 처리 가능
- 기능
  - 인증 및 권한 부여
  - 서비스 검색 통합
  - 응답 캐싱
  - 정책, 회로 차단기 및 QoS 다시 시도
  - 속도 제한
  - 부하 분산
  - 로깅, 추적, 상관 관계
  - 헤더, 쿼리 문자열 및 청구 변환
  - IP 허용 목록에 추가

### Configuration Service

- 분산 시스템에서 서버, 클라이언트 구성에 필요한 설정 정보(application.yml)를 외부 시스템에서 관리
- 하나의 중앙화된 저장소에서 구성요소 관리 가능
- 각 서비스를 다시 빌드하지 않고 바로 적응 가능
- 애플리케이션 배포 파이프라인을 통해 DEV-UAT-PROD 환경에 맞는 구성 정보 사용

#### 각 서비스에서 변경된 설정 정보 가져오기

- 서버 재기동
  - 설정 정보 변경시 서비스(ex. user-service) 재기동
  - config server 를 사용하는 의미 X
- Actuator refresh
- Spring cloud bus 사용
