# 스프링부트로 직접 만들면서 배우는 대규모 시스템 설계 - 게시판

## Distributed Relational Database

### 수평 샤딩 VS 수직 샤딩

Scale-Up의 한계로 Scale-Out을 고려해볼 수 있다. 이때 샤딩을 이용하여 데이터를 여러 DB에 분산할 수 있다.

`수직 샤딩`

- 데이터를 수직으로 분할하는 방식(컬럼 단위)
- 각 샤드가 적은 수의 컬럼을 저장하므로, 성능 및 공간 이점
- 데이터의 분리로 인해 조인 및 트랜잭션 관리가 복잡
- 수직으로 분할되므로 수평적 확장에 제한
    - 컬럼 단위로 분할되기 때문에, 컬럼 수 만큼 확장 가능? 으로 생각해볼 수 있음

`수평 샤딩`

- 데이터를 수평으로 분할하는 방식(행 단위)
    - article_id: 1~5000, 5001~10000
- 각 샤드에 데이터가 분산 저장되므로 성능 및 공간 이점
- 데이터의 분리로 인해 조인 및 트랜잭션 관리 복잡
- 수평으로 분할되므로 수평적 확장에 용이

### 샤딩 전략

`Range-based Sharding(범위 기반 샤딩)`

- 데이터를 특정 값(Shard Key)의 특정 범위에 따라 분할하는 기법
    - article_id: 1~5000, 5001~10000
- 범위 데이터 조회 유리
    - article_id=100 부터 30개 조회한다면 좌측 샤드에서 모든 데이터를 찾을 수 있음
- 데이터 쏠림 현상 발생 가능
    - 데이터가 6000개 있다면, 한쪽 샤드에는 1000개만 저장

`Hash-based Sharding(해시 기반 샤딩)`

- 데이터를 특정 값(Shared Key)의 해시 함수에 따라 분할하는 기법
    - hash function: article_id -> article_id % 2
    - article_id = (1, 3, 5, ...), (2, 4, 6, ...)
- 균등한 분산을 위한 Shared Key와 해시 함수가 필요
    - 균등하게 분산되지 않으면, 데이터 쏠림 현상 발생 가능
- 범위 데이터 조회에 불리

`Directory-based Sharding(디렉토리 기반 샤딩)`

- 디렉토리를 이용하여 데이터가 저장된 샤드를 관리하는 기법
    - 매핑 테이블을 이용하여 각 데이터가 저장된 샤드를 관리
- 디렉토리 관리 비용 있으나, 데이터 규모에 따라 유연한 관리 가능

> 외에도 데이터의 특성 또는 시스템 요구사항에 따라 다양한 샤딩 기법 존재

### 물리적 샤드 VS 논리적 샤드

`물리적 샤드`

- 물리적으로 분리
- 샤드 증가 시
    - 균등한 분산을 위해 데이터 재배치 필요
    - 클라이언트는 4개의 샤드로 요청해야 하므로 애플리케이션 코드를 수정하는 등 새로운 샤드의 정보를 알아야 함
- 클라이언트의 수정 없이 DB만 유연하게 확장하는 방법은 없을까? -> 논리적 샤드

`논리적 샤드`

- 물리적으로 분리되지 않음
- 실제 물리적인 샤드의 개수와 관계없이, 개념적으로 데이터를 분할하는 가상의 샤드
    - 논리적 샤드에 맞는 데이터 분산(해시 함수 등)
- 클라이언트는 논리적 샤드의 접근 방법으로 DB에 접근
    - 논리적 샤드 개수에 맞게
- 논리적 샤드가 어떤 물리적 샤드에 속해있는지 알기 위해 DB 시스템 내부 또는 DB 와 클라이언트 사이에 독립적으로 라우터 구성
    - DB 시스템 내부에 논리적 샤드의 실제 물리적 샤드로 라우팅해주는, Shard Router
- 물리적 샤드가 증가하면 논리적 샤드를 재배치
- 라우터가 물리적으로 늘어난 샤드 정보를 알도록 하여 클라이언트는 어떠한 수정도 필요 없음

> 논리적 샤드 개념을 이용하여, 물리적 샤드를 확장하더라도 클라이언트의 어떠한 변경도 없이 DB를 유연하게 확장 가능

|     | 물리적 샤드                | 논리적 샤드                       |
|-----|-----------------------|------------------------------|
| 정의  | 데이터를 물리적으로 분산한 실제 단위  | 데이터를 논리적으로 분산한 가상의 단위        |
| 필요성 | 대규모 데이터 분산 저장 및 성능 향상 | 물리적 확장 시, 클라이언트 변경 없이 유연한 매핑 | 

### 데이터 복제

> 고가용성, 안정성, 성능

샤드에 장애가 발생

> 시스템 오류, 네트워크 순단에 의한 일시적 현상 혹은 자연 재해에 의한 영구적인 데이터 손실

이러한 문제를 해결하기 위해 데이터 복제본 관리

> Primary(주 데이터베이스)에 데이터를 쓰고, Replica(복제본)로 데이터를 복제  
> Primary/Replica, Leader/Follower, Master/Slave 등 유사한 개념이지만, 시스템이나 목적에 따라 다르게 사용되기도 함

복제는 동기적 또는 비동기적으로 처리

복제본은 고가용성을 위해 동일한 데이터 센터 혹은 다른 지역의 데이터 센터에서 관리

샤드에 장애가 발생하더라도 Replica에서 데이터 조회를 수행할 수 있고, Replica를 Primary로 재선출하여 쓰기 작업 이어서 수행 가능

고가용성(Primary 재선출을 통한 서비스 지속성), 데이터 유실 방지 및 데이터 백업(Replica 복제본 관리), 부하 분산(Replica 또는 각 샤드로 요청 분산)

## `article` 게시글

요구사항

- 각 게시판 단위로 게시글 서비스 이용
    - 사용자는 게시판에 게시글을 작성하고 조회한다.
- 게시글 조회, 생성, 수정, 삭제 API
- 게시글 목록 조회 API
    - 게시판별 최신순

> `article_id` Shard Key 가정

분산 데이터를 가정하기 때문에 게시글이 N개의 샤드로 분산되는 상황 고려

- Shard Key = board_id(게시판 ID)
    - 게시판 단위로 서비스를 이용
    - 개시판 단위로 게시글 목록 조회

Primary Key 선택

- 오름차순 유니크 숫자를 애플리케이션에서 직접 생성
    - AUTO_INCREMENT, UUID 사용 X
    - 분산 시스템과 인덱스의 구조 고려
- Snowflake ID 사용
    - 분산 시스템에서 고유한 64비트 ID 생성
        - \[1비트]\[41비트: 타임스탬프]\[10비트: 노드 ID]\[12비트: 시퀀스]
        - 분산 환경에서도 중복 없이 순차적 ID 생성하기 위한 규칙
            - 타임스탬프: 순차성
            - 노드 ID + 시퀀스 번호: 고유성

게시글 목록 조회

- 대규모 데이터에서 게시글 목록 조회는 왜 복잡할까?
    - 모든 데이터를 한 번에 보여줄 수는 없음
        - 메모리, 네트워크, 성능 등의 제약
    - 페이징 필요
- 전체 데이터에 대해 필터링 및 정렬, 즉 풀테이블 스캔을 피하기 위해 인덱스 사용

인덱스에 대한 이해

- 데이터를 빠르게 찾기 위한 방법
- 인덱스 관리를 위해 부가적인 쓰기 작업과 공간 필요
- 다양한 데이터 특성과 쿼리를 지원하는 다양한 자료구조: **B+ tree**, Hash, LSM tree, R tree, Bitmap 등
- RDB에서는 주로 B+ Tree 사용
    - 데이터가 정렬된 상태로 저장
    - 검색, 삽입, 삭제 연산을 로그 시간에 수행 가능
    - 트리 구조에서 리프 노드 간 연결되기 때문에 범위 검색 효율적
- 인덱스를 추가하면, 쓰기 시점에 B+ tree 구조의 정렬된 상태의 데이터가 생성
- 이미 인덱스로 지정된 컬럼에 대해 정렬된 상태를 가지고 있기 때문에
    - 조회 시점에 전체 데이터를 정렬하고 필터링할 필요 X
    - 따라서 조회 쿼리 성능 향상

대규모 시스템이기 때문에 동일한 시간에 생성된 게시글이 많을 것

- 생성 시간보다 시간에 따라 증가하는 게시글 ID를 기준으로 정렬하는 것이 더 좋음
- 밀리/나노초 단위의 더욱 정밀한 데이터 타입을 가진다고 해도 시간 충돌 문제는 여전하고, 저장 공간만 더 필요할 수 있음

인덱스를 사용하더라도 뒷 페이지로 갈수록(오프셋 증가할수록) 성능 저하

> 이를 이해하기 위해 인덱스 종료 이해: `Clustered Index`, `Secondary Index`

MySQL 인덱스

- 기본 스토리지 엔진: InnoDB
- InnoDB는 테이블마다 Clustered Index를 자동 생성
    - Primary Key를 기준으로 정렬된 Clustered Index
        - Primary Index(주 인덱스)라고도 불림
        - 정확히는 규칙이 있지만, 일반적으로는 Primary Key에 생성
    - Clustered Index는 leaf node의 값으로 행 데이터 가짐
- 우리가 생성한 인덱스는 Secondary Index(보조 인덱스)
    - Non-Clustered Index라고도 불림
- Secondary Index의 leaf node는 다음 데이터를 가짐
    - 인덱스 컬럼 데이터
    - 데이터에 접근하기 위한 포인터
        - 데이터는 Clustered Index가 가짐
        - Clustered Index의 데이터에 접근하기 위한 포인터
        - Primary Key

|     | Clustered Index         | Secondary Index              |
|-----|-------------------------|------------------------------|
| 생성  | 테이블의 Primary Key로 자동 생성 | 테이블의 컬럼으로 직접 생성              |
| 데이터 | 행 데이터(row data)         | 데이터에 접근하기 위한 포인터, 인덱스 컬럼 데이터 |
| 개수  | 테이블당 1개                 | 테이블 당 여러개                    |

Secondary Index 를 통해 데이터가 조회되는 과정

- Secondary Index를 먼저 조회하여, 데이터가 접근할 수 있는 포인터(id)를 찾음
- 이를 이용하여 Clustered Index를 조회
    - 인덱스 트리를 두 번 타고 있는 것
- offset에 도달할 때까지 모든 Secondary Index와 Clustered Index를 통해 데이터 접근

> Secondary Index에서 필요한 30건에 대해서 article_id만 먼저 추출하고,
> 그 30건에 대해서만 Clustered Index에 접근하면 충분하지 않을까?
> article_id는 Clustered Index에 접근하지 않아도 가져올 수 있다. -> `커버링 인덱스`

커버링 인덱스

```sql
-- 커버링 인덱스 적용 전
select *
from article
where board_id = 1
order by article_id desc
limit 30 offset 1499970;

-- 커버링 인덱스 사용하는 쿼리
-- (board_id asc, article_id desc) 인덱스
select board_id, article_id
from article
where board_id = 1
order by article_id desc
limit 30 offset 1499970;

-- 커버링 인덱스 적용 후
select *
from (select article_id
      from article
      where board_id = 1
      order by article_id desc
      limit 30 offset 1499970) t
         left join article on t.article_id = article.article_id;
```

커버링 인덱스를 적용한 쿼리도 페이지(오프셋)이 더 커지면 조회 속도가 저하

- Secondary Index만 탄다고 하더라도, offset만큼 Index Scan이 필요
- 실제 데이터에 접근(Clustered Index)하지 않더라도 offset이 늘어날 수록 성능 저하

> 이를 해결하기 위한 방법은??

- 데이터를 한 번 더 분리
    - 예를 들면, 게시글을 1년 단위로 테이블 분리
        - 개별 테이블의 크기를 작게
        - 각 단위에 대해 전체 게시글 수를 관리
    - offset을 인덱스 페이지 단위 skip하는 것이 아니라, 1년 동안 작성된 게시글 수 단위로 즉시 skip
        - 조회하고자 하는 offset이 1년 동안 작성된 게시글 수보다 크다면,
            - 해당 개수 만큼 즉시 skip
            - 더 큰 단위로 skip을 수행하게 되는 것
        - 애플리케이션에서 이처럼 처리하기 위한 코드 작성 필요
- 매우 큰 수의 페이지(300,000번 페이지)를 조회하는게 정상적인 사용자일까?
    - 데이터 수집을 목적으로 하는 어뷰저일 수 있음
    - 정책으로 풀어내기
        - 예를 들면, 게시글 목록 조회는 10,000번 페이지까지 제한
    - 시간 범위 또는 텍스트 검색 기능을 제공할 수 있음
        - 더 작은 데이터 집합 내에서 페이징을 수행
- 무한 스크롤
    - 페이지 번호 방식에서는 동작 특성 상, 뒷 페이지로 갈수록 속도가 느려질 수 밖에 없음
    - 하지만 무한 스크롤에서는 아무리 뒷 페이지로 가더라도 균등한 조회 속도를 가지도록 할 수 있음

게시글 개수

- 데이터 유무에 따라서 몇 페이지까지 활성화 할지 결정하기 위해 필요
    - 페이지 당 30개의 게시글을, 페이지 이동 버튼을 10개씩 노출한다면,
        - 게시글이 50개 있다면, 2페이지까지 활성화
        - 게시글이 301개 이상 있다면, 다음 버튼까지 활성화 -> 11페이지 이상 있다는 것을 의미
- `count(*)` 쿼리는 모든 데이터를 확인하므로 속도가 느림
- 모든 데이터 카운트가 필요할까?
    - 전체 데이터수가 필요한 것이 아니라 이동 가능한 페이지 번호 활성화가 필요
    - 1~10번 페이지에서는 1~10번 페이지와 다음 버튼만 활성화
    - 11~20번 페이지에서는 11~20번 페이지와 다음 버튼만 활성화
    - ...

```sql
select count(*)
from (select article_id
      from article
      where board_id = {board_id}
      limit
      {
      limit});
```

> `count`에서 limit 은 동작하지 않으므로 서브 쿼리와 커버링 인덱스 활용

전체 데이터 개수가 필요하더라도 조회 시점에 실시간으로 쿼리할 필요 없음

- 데이터 개수를 하나의 데이터로 미리 만들어 둘 수 있음
    - 생성/삭제 시점마다 개수 업데이트
    - 동시성 문제 해결 필요

게시글 목록 조회 - 무한 스크롤

- 새로운 방식의 쿼리가 필요
    - 페이지 번호 방식으로 사용하면 중간에 새로운 게시글이 추가 혹은 삭제되면서 데이터가 중복/누락될 수 있음
- 마지막으로 불러온 데이터를 기준점으로 사용할 수 있음
    - offset을 스캔하는 과정이 필요 X -> 아무리 뒷 페이지로 가더라도, 균등한 속도를 보장

```sql
-- 1번 페이지
select *
from article
where board_id = {board_id}
order by article_id desc
limit 30;

-- 2번 페이지 이상
select *
from article
where board_id = {board_id}
  and article_id < {last_article_id}
order by article_id desc
limit 30;
```

Primary Key 생성 전략

- DB auto_increment
- 유니크 문자열 또는 숫자
- 유니크 정렬 문자열
- 유니크 정렬 숫자

Primary Key 생성 전략 - DB auto_increment

- 분산 데이터베이스 환경에서 PK가 중복될 수 있기 때문에, 식별자의 유일성이 보장되지 않는다.
    - 여러 샤드에서 동일한 PK를 가지는 상황
- 클라이언트 측에 노출하면 보안 문제
    - 데이터의 개수 또는 특정 시점의 식별자 예측
- 간단하기 떄문에 다음 상황에서 유리
    - 보안적인 문제를 크게 고려하지 않는 상황
    - 단일 DB를 사용하거나 애플리케이션에서 PK의 중복을 직접 구분하는 상황
- 보안적인 문제만 염려된다면
    - PK는 데이터베이스 내에서의 식별자로만 사용하고
    - 애플리케이션에서의 식별자를 위해 별도 유니크 인덱스를 사용할 수 있음
    - 하지만 이 방식은 조회 비용 증가
        - Secondary Index로 포인터 찾은 후, Clustered Index로 데이터 접근

Primary Key 생성 전략 - 유니크 문자열 또는 숫자

- UUID 또는 난수를 생성하여 PK 지정
    - 정렬 데이터가 아니라 랜덤 데이터
    - 키 생성 방식 간단
- 랜덤 데이터로 인해 성능 저하 발생 가능
    - Clustered Index는 정렬된 상태 유지
    - 데이터 삽입 필요한 인덱스 페이지가 가득 찼다면, B+ tree 재구성 및 페이지 분할로 디스크 I/O 증가
    - PK를 이용한 범위 조회가 필요하다면, 디스크에서 랜덤 I/O가 발생하기 때문에, 순차 I/O에 비해 성능 저하

Primary Key 생성 전략 - 유니크 정렬 문자열

- 분산 환경에서 PK 중복 문제 해결
- 보안 문제 해결
- 랜덤 데이터에 의한 성능 문제 해결
- UUID v7, ULID 등의 알고리즘
    - 일반적으로 알려진 알고리즘은 128비트를 사용
- 데이터 크기에 따라, 공간 및 성능 효율이 달라짐
    - Clustered Index는 PK를 기준으로 만들어짐
    - Secondary Index는 데이터에 접근할 수 있는 포인터를 가짐
        - 즉, PK를 가짐
    - PK가 크면 클수록 데이터는 더 많은 공간을 차지하고 비교 연산에 의한 정렬/조회에 더 많은 비용 소모

Primary Key 생성 전략 - 유니크 정렬 숫자

- 분산 환경에 대한 PK 중복 문제 해결
- 보안 문제 해결
- 랜덤 데이터에 의한 성능 문제 해결
- Snowflake, TSID 등의 알고리즘
    - 64비트 사용 (BIGINT)
    - 정렬을 위한 타임스탬프를 나타내는 비트 수 제한
        - 키 생성을 위한 시간적인 한계
        - 문자열 알고리즘에서도 동일한 한계가 있으나 비트 수 많을 수록 제한이 덜함
- 앞서 살펴본 문자열 방식보다 적은 공간 사용

## `comment` 댓글

요구사항

- 댓글 조회, 생성, 삭제 API
- 댓글 목록 조회 API
    - 계층형
        - 최대 2depth 대댓글 -> Adjacency list(인접 리스트)
        - 무한 depth 대댓글 -> Path enumeration(경로 열거)
    - 계층별 오래된순 정렬
    - 페이지 번호, 무한 스크롤
- 하위 댓글이 모두 삭제되어야 상위 댓글 삭제
    - 하위 댓글이 없으면, 댓글 즉시 삭제
    - 하위 댓글이 있으면, 댓글 삭제 표시

> `article_id` 칼럼 Shard Key

댓글 목록 조회- 최대 2depth

- 댓글 목록은 오래된 댓글이 먼저 노출 -> 계층 내에서 순서 유효
- parent_comment_id 오름차순, comment_id 오름차순 정렬구조
- article_id asc, parent_comment_id asc, comment_id asc 인덱스 생성
    - article_id 는 Shard Key 이기 때문에, 단일 샤드에서 게시글별 댓글 목록 조회 가능

```sql
-- 댓글 목록 조회 - 페이지 번호
select *
from (
    select comment_id
    from comment
    where article_id = {articleId}
    order by parent_comment_id asc, comment_id asc
    limit {limit} offset {offset}
) t left join comment on t.comment_id = comment.comment_id;

-- 댓글 개수 조회
select count(*)
from (
    select comment_id
    from comment
    where article_id = :articleId
    limit :limit
) t;

-- 댓글 목록 조회 - 무한 스크롤 1번 페이지
select *
from comment
where article_id = {articleId}
order by parent_comment_id asc, comment_id asc
limit {limit}

-- 댓글 목록 조회 - 무한 스크롤 2번 페이지 이상
select *
from comment
where article_id = {articleId}
  and (parent_comment_id > {lastParentCommentId} or (parent_comment_id = {lastParentCommentId} and comment_id > {lastCommentId}))
order by parent_comment_id asc, comment_id asc
limit :limit
```

댓글 목록 조회 - 무한 depth

- 상위 댓글은 항상 하위 댓글보다 먼저 생성되고, 하위 댓글은 상위 댓글 별 순서대로 생성
    - 무한 depth 에서는 상하위 댓글이 재귀적으로 무한할 수 있으므로, 정렬 순을 나타내기 위해 모든 상위 댓글의 정보가 필요
- Path enumeration(경로 열거) 방식으로 한 칼럼에 각 depth 에서의 순서를 문자열로 나타내고 이러한 문자열을 순서대로 결합하여 경로를 나타냄
- 문자열이기 때문에, 반드시 10개의 숫자로 나타낼 필요 X
    - 0~9(10개), a~z(26개), A~Z(26개) -> 62개 사용 가능
    - 한 경로에 5개 문자 사용한다면 각 경로의 범위가 경로 별로 62^5(916,132,832)
    - 대소 문자를 구분하는 콜레이션 사용 필요
- 설계 구조 상 depth 는 무한할 수 있지만, 개발 편의 및 서비스 제한 사항으로서 5 depth로 제한

무한 depth path 생성 방법

![댓글 목록 조회 - 무한 depth path.jpg](./images/%E1%84%83%E1%85%A2%E1%86%BA%E1%84%80%E1%85%B3%E1%86%AF%20%E1%84%86%E1%85%A9%E1%86%A8%E1%84%85%E1%85%A9%E1%86%A8%20%E1%84%8C%E1%85%A9%E1%84%92%E1%85%AC%20-%20%E1%84%86%E1%85%AE%E1%84%92%E1%85%A1%E1%86%AB%20depth%20path.jpg)![댓글 목록 조회 - 무한 depth path.jpg](
./images/댓글 목록 조회 - 무한 depth path.jpg)

- 00a0z 댓글 하위로 신규 댓글 요청
- 하위 댓글 중 가장 큰 path(childrenTopPath)를 찾고, 여기에 1을 더한 문자열이 신규 댓글의 path
- childrenTopPath 를 찾는 법
    - 각 댓글의 모든 자손 댓글은 prefix가 상위 댓글의 path(parentPath)로 시작
    - parentPath prefix를 가지는 모든 자손 댓글에서, 가장 큰 path(descendantsTopPath)를 찾음
    - descendantsTopPath는 신규 댓글의 depth와 다를 수 있지만, childTopPath를 포함
    - descendantsTopPath에서 신규 댓글의 depth 까지만 남기고 날라냄

```sql
select path
from comment_v2
where article_id = {article_id}
  and path > {parentPath}
  and path like {parentPath} %
order by path desc
limit 1;
```

Backward index scan

- 인덱스를 역순으로 스캔하는 것
- 인덱스 트리 leaf node 간에 연결된 양방향 포인터 활용

해당 경로의 댓글을 모두 생성했을 경우

- 값을 표현할 수 있는 범위(62^5)개를 벗어나면 더 이상 생성될 수 없음
- 문자의 표현 개수(0-9, a-z, A-Z)나 각 depth 별 문자 개수(5개)를 늘림으로써 해결해볼 수 있음

댓글 목록 조회 - 무한 depth - 쿼리

```sql
-- 댓글 목록 조회 - 페이지 번호
select *
from (
    select comment_id
    from comment_v2
    where article_id = {article_id}
    order by path asc
    limit {limit} offset {offset}
) t left join comment_v2 on t.comment_id = comment_v2.comment_id;

-- 카운트 조회
select count(*)
from (select comment_if from comment_v2 where article_id = {article_id} limit {limit}) t;

-- 댓글 목록 조회 - 무한 스크롤 1번 페이지
select *
from comment_v2
where article_id = {article_id}
order by path asc
limit {limit};

-- 댓글 목록 조회 - 무한 스크롤 2번 페이지 이상 (기준점 lath_path)
select *
from comment_v2
where article_id = {article_id} and > {last_path}
order by path asc
limit {limit};
```
## `like` 좋아요

요구사항

- 게시글 좋아요
  - 각 사용자는 각 게시글에 1회 좋아요
- 좋아요 수

```sql
create table article_like
(
    article_like_id bigint auto_increment primary key,
    article_id bigint not null,
    user_id bigint not null,
    created_at datetime not null
);

create unique index idx_article_id_user_id on article_like (article_id asc, user_id asc);
```

> Shard Key = article_id  
> 쿼리 패턴에 대한 요구 사항은 없지만, 적절한 분산의 단위로서 선정
