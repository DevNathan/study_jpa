조남호 | Nathan-Cho<br>
07.10.23 ~ 진행중

# JPA
### 목차
1. [JPA란?](#1-jpa란)
2. [JPA 사용](#2-jpa-사용)
	> 2-1 [JPA 테이블 등록하기](#2-1-jpa-테이블-등록하기)<br>
	> 2-2 [EntityManager와 메소드들](#2-2-EntityManager와-메소드들)<br>
 	> 2-3 [조합키 사용](#2-3-조합키-사용)<br>
 	> 2-4 [테이블 상속](#2-4-테이블-상속)<br>
 	> 2-5 [MappedSuperClass](#2-5-MappedSuperClass)
3. [JPA 구조](#3-jpa-구조)
4. [JPQL](#4-jpql)

***
## 1. JPA란?
    객체(JAVA)와 RDB의 패러다임 불일치 문제를 해소하기위해 RDB를 객체지향적으로 만들어 사용하자는
    취지에서 ORM이 탄생하였고 이 ORM을 사용하기 위한 JPA가 탄생하였다.

- ORM(Object Relational Mapping)

      객체지향 언어와 관계형 DB와의 데이터를 매핑해주는 기술 (클래스와 테이블을 그대로 맵핑)
	    객체지향 언어와 관계형 DB는 서로 다른 구조를 가지고 있기 때문에
	    자바가 가지는 여러 이점을 제대로 사용할 수 없다.
	    이를 해결하고자 나온 기술이 테이블과 객체를 매핑시켜주는 ORM이다.
- JPA(JAVA Persistent API)

      자바에서 ORM을 사용하기 위한 표준 API(자바 전용)
      JPA는 인터페이스를 제공하는 역할을 하며 실질적인 ORM 기술을 구현해 놓은 것이 Hibernate같은 ORM
- Spring Data JPA

      JPA를 기반으로 만든 모듈이다
      Repository라는 인터페이스를 제공하여 DB와의 상호작용을 더 쉽고 간결하게 만들어준다.
      내부적으로는 JPA를 사용하기 때문에 JPA에 대한 선행학습이 필요하다.
- Hibernate

      ORM기술을 구현해 놓은 오픈소스 프레임워크이다.


***
## 2. JPA 사용
  		JPA와 관련하여 상당히 많은 수의 어노테이션이 추가되게 된다.
  		모든 어노테이션을 알필요는 없으나 중요한 어노테이션은 반드시 알아두고
  		JPA와 DB의 관계가 어떤식으로 되어있는지 이해가 중요하다.

### 2-1 JPA 테이블 등록하기
	테이블과 DB를 매핑시키기 위해서는 Entity어노테이션을 이용해 
 	Entity로 테이블을 등록시켜줘야 한다.

	아래는 테이블을 만들기 위해 사용할 수 있는 어노테이션들이며 
 	必은 반드시 사용해야하는 어노테이션이다.
 
@Entity(必)

	테이블을 Entity로 등록한다. 테이블 이름은 Entity의 이름과 동일하게 만들어진다.
  	엔티티를 만드는 순간 오류가 나게 되는 이유는
	테이블을 만들 때 반드시 필요한 PK가 없기 때문이다. 
	추가적으로 Entity는 기본생성자가 필수이다.
@Table

	엔티티와 매핑할 테이블을 지정한다. 
 	생략할 시 매핑한 엔티티의 이름을 테이블 이름으로 사용한다.

	name = 매핑할 테이블 이름
@Id(必)

	해당 필드를 PK칼럼으로 등록한다.
@GeneratedValue

	해당 컬럼에 자동 증가되는 값이 들어가게 된다.
@Column

	객체 필드를 테이블 컬럼에 매핑한다.

 	name : 테이블 컬럼의 이름
  	nullable : null값의 허용 여부를 결정한다. (true/false)
   	unique : 유니크 제약조건을 적용한다. (true/false)
	length : 문자 길이 제약조건이며 String 타입에만 사용한다. (Default 225)
	precision, scale : 숫자의 형태를 조절한다. BigDecimal 타입에서 사용한다.
				precision은 소숫점을 포함한 전체 자릿수이며
				scale은 소수의 자릿수이다.

@Enumerated

	JAVA의 enum 타입을 컬럼에 매핑하기위해 사용한다.

	EnumType.ORDINAL : enum 순서를 값으로 DB에 저장한다.
 	EnumType.STRING : enum 이름을 값으로 DB에 저장한다.
@Lob

	데이터베이스의 BLOB, CLOB, TEXT 타입과 매핑이 된다.

### 2-2 EntityManager와 메소드들
JPA를 통해서 CRUD하기 위해서는 JPA에 대한 구조적 이해가 필요하다.<br>
이는 영속성 컨텍스트와 밀접한 관련이 있으며 이것에 대해서는<br>
[3. JPA 구조](#3-jpa-구조)에서 확인한다.

@PersistenceContext

	영속성 컨텍스트를 사용할 수 있도록 한다.
EntityMangerFactory

	EntityManager를 만들고 구성하는 법을 제공하는 interface이다
EntityManager

	DB 테이블과 매핑된 Entity에 대한 CRUD를 수행하는 method를 제공하며 
 	Entity의 영속성과 생명주기를 관리, 담당한다.
- persist()

		DB의 INSERT에 해당하는 메소드이지만 그 작동방식이 다르다.
  		EntityManager에게 Entity를 넘겨줘서 영속화 시키고 INSERT쿼리를 생성해주는
  		메소드가 persist()이다. 이 메소드는 INSERT쿼리문을 작동시키는 것은 해당하지 않는다.
- flush()

		쿼리문을 DB에 반영한다. 하지만 COMMIT된 상태는 아니다.
  		@Transactional과 @Commit을 붙혀서 COMMIT까지 완료시킬 수 있다.  
- find()

		Entity의 클래스와 PK를 알려주면 해당 엔티티를 1차 캐시에서 찾아온다.
  		만약 1차 캐시에 존재하지 않을 경우 SELECT문을 통해 조회한다.
  		이 때, SELECT문의 결과를 가져오는 것이 아닌 1차 캐시에 영속화 시킨 후
  		그 영속화 된 데이터를 가져오는 것이다.
- detach()

    		영속성 컨텍스트에 등록된 엔티티를 영속성 컨텍스트에서 제외시켜
  		해당 데이터를 준영속 상태로 만든다
- remove()

		해당 데이터를 캐시와 DB에서 모두 삭제한다.
- merge()

		준영속 상태가 된 데이터를 다시 영속화 시킬 때 사용한다.

### 2-3 테이블 간 연관관계
	테이블간의 연관관계는 JPA의 테이블 매핑에 있어 매우 중요한 개념이며,
 	테이블 간의 관계를 맺어 사용할 수 있어야 한다.

#### 관련 어노테이션
@OneToMany

		일대다 관계 매핑
@ManyToOne

 		다대일 관계 매핑
@OneToOne

		일대일 관계 매핑
@ManyToMany

		다대다 관계 매핑
@JoinColumn

		Entity의 연관관계에서 외래 키를 매핑하기 위해 사용된다.
  		보통 일대다 관계에서 사용.

    		name : 매핑할 FK의 이름을 설정
@JoinTable

		다대다 관계에서 사용하며
  		다대다 간의 관계에서 생겨나는 새로운 TABLE이 엔티티없이 생겨나게 된다.

		name : 중간 테이블의 이름을 지정할 수 있다.
  		joinColumns : 두개의 테이블이 중간테이블에 연결될때의 이름을 각각 지정해줄 수 있다
    			joinColumn, inverseJoinColumns로 가능.

#### 단방향과 양방향
	단방향 구조와 양방향 구조는 두 테이블 사이에서 
 	서로가 한측만 참조하는지 서로를 참조하는지에 따라 달라진다.

   	단방향을 기본적으로 구현하고 필요에 따라서 양방향으로 구현하는 방향으로 가는 것이 좋다.
1. 단방향

		단방향의 경우에는 어느 테이블에 pk를 지정해줘도 상관이 없으나
		보통 FK가 생기는 테이블에 만들어주는 경우가 대부분이다. 하지만 단방향인 만큼
		한쪽에서밖에 참조를 할수 밖에 없으며 반대쪽에서는 불가능하다는 문제가 생긴다.
3. 양방향

		양방향의 경우에는 상호 테이블간의 모두 작성해주게 되지만
   		양방향 참조시 한쪽에서는 반드시 toString 제어를 해주어야만
   		StackOverFlow가 발생하지 않으며
   		한 테이블은 무조건 주인 테이블로써 존재해야하므로
   		반드시 주인 테이블이 아닌쪽에 mappedBY를 지정해주어야만 한다.
   		이렇듯 양방향은 상호 참조가 가능하다는 장점에도 불구하고 구현시
   		제어해야될게 많아지고 복잡해지므로 보통 잘 사용하지는 않는다.

### 2-3 조합키 사용
	요구에 따라 조합키로 만들어야 하는 경우가 있다. 
 	이럴 경우에는 조합키로 사용할 객체 필드들에 @Id를 걸어주는 것 뿐만 아니라 해야할 일들이 있다.

<img width="471" alt="스크린샷 2023-11-19 090259" src="https://github.com/DevNathan/study_jpa/assets/142222091/ef1cb000-b071-4c44-8f73-16ef510f52d1">

	조합키만 따로 모아둔 클래스를 하나를 새로 만들어야 한다.
 	반드시 기본생성자와 hashCode(), equals() 재정의가 필요하며
  	Serializable 마커 인터페이스를 구현해주어야 한다.

<img width="490" alt="스크린샷 2023-11-19 090341" src="https://github.com/DevNathan/study_jpa/assets/142222091/9c9af39a-4476-441a-8539-aa1ff90a506b">

	다시 돌아와서 클래스에 @IdClass를 붙혀주고 바로전에 만든 
 	조합키 클래스를 직접 연결해주며 N:1 관계를 잘 잡아주면 완성된다.

### 2-4 테이블 상속
	객체지향언어의 장점중 하나인 상속 개념을 JPA를 통해 구현할 수 있다. 
 	상속개념을 RDB에서 구현하는 방식은 총 3가지이며 각각의 장단점이 있으니
  	알맞게 사용하는 것이 좋다.

1. 조인 전략(Joined)

		모든 엔티티를 테이블로 만들고 자식 테이블에서
		부모 테이블의 PK를 받아 PK+FK로 사용하는 전략
		테이블이 각각 나눠지므로 조회시 조인을 자주 사용해야한다.
		객체는 클래스를 타입으로 사용하지만 테이블에는 타입의 개념이 없으므로
		타입을 구분할 수 있는 칼럼을 부모테이블에 추가해야한다(DTYPE).

		PRO : 테이블이 정규화되고 저장공간을 효율적으로 사용할 수 있다.
		CON : 조회시 조인이 많이 사용되어 쿼리가 복잡해지고 INSERT를
			항상 2번 이상 해야하므로 성능이슈가 생길 수 있음.
2. 단일 테이블 전략(Single Table)

		부모와 모든 자식을 하나의 테이블로 만든다.
		어떤 데이터인지 알기 위해서 타입을 저장하며,
		여러 칼럼에 null을 허용해야한다.

		PRO : 반정규화 되어 INSERT쿼리를 한번만 날려도 되며 조인도 할 필요가 없게 된다.
			즉, 성능향상으로 이어진다.
   		CON : 데이터가 너무 광범위해지며 자식이 많으면 많을 수록,
   			자식의 컬럼이 많으면 많을 수록 불필요한 NULL값도 그에따라 많아지게 되니
   			메모리 낭비로 이어진다.

3. 구현 클래스마다 테이블 생성 전략(Table Per Class)

  		일반적인 RDB를 설계하는 방식이다. 

    		PRO : DB저장에 있어 직관적이다.
   		CON : 반복되는 값을 계속해서 집어 넣어줘야된다는 문제점이 있다.

#### 테이블 상속 관련 어노테이션
@Inheritance

		테이블간 상속관계를 매핑할 때 사용하게 된다.
  		부모 테이블 클래스에서 사용한다

		InheritanceType
  			.JOINED : 조인전략
			.SINGLE_TABLE : 단일 테이블 전략
			.TABLE_PER_CLASS : 구현 클래스마다 테이블 생성 전략
@DiscriminatorColumn

		부모테이블의 컬럼을 통해 어떤 종류의 자식테이블이 연결되어 있는지 구분하기 위해
  		타입을 저장할 컬럼을 설정한다.

		name : 구분컬럼의 이름을 지정한다.(Default = "DTYPE")
@DiscriminatorValue

		자식테이블에서 부모테이블의 구분컬럼에 어떻게 명시해줄 것인지 이름을 설정한다.

    		name : 부모테이블의 컬럼에 저장될 이름을 지정한다.
@PrimaryKeyJoinColumn

		PK이자 FK로 사용하고자 할 때 사용하는 어노테이션으로 상속테이블과 관련해서
  		자주 사용된다.

    		name : PK이자 FK가 될 컬럼의 이름을 지정해준다.
			지정해주지 않을 시 FK로 받게 되는 테이블의 PK 값을 그대로 가져온다.

### 2-5 MappedSuperclass
	테이블과 매핑하지 않고 단순히 필드를 상속하기 위한 용도의 클래스를 의미한다.
 	여러개의 테이블들이 동일하게 공유하게 되는 컬럼, 예를 들어 데이터 생성시간, 업데이트 시간과
  	같은 것들은 반복적으로 테이블에 넣어야 하니 그걸 대신해주는 클래스라고 할 수 있겠다.

#### 관련 어노테이션
@MappedSuperclass

	객체 상속을 할 수 있도록 해주는 어노테이션이다.
  	이 어노테이션이 있는 클래스를 확장하여 사용시 해당 컬럼들이 주입된다.
@PrePersist(Hibernate지원)

	entityManager의 persist() 직전에 실행되는 메소드로 지정한다.
@PreUpdate(Hibernate지원)

	update에 해당된다.

### 2-6 프록시와 지연 로딩
#### 프록시(Proxy)
	객체지향적으로 데이터를 가져오게되는 JPA의 단점은 불필요한 정보까지
 	JOIN해서 가져온다는 것이다. 불필요한 정보 조회를 방지하기 위하여 프록시가 필요하다.

  	프록시의 작동 방식은 '가짜 객체'를 만들어서 전달해주되, 그것을 실제로 사용하게 
   	될시 DB에서 조회하여 가져오도록 설계가 되어있다.

	'가짜 객체'는 테이블의 구조를 그대로 상속받았으나 채워넣기 용이므로 값이 없는 깡통이라고
 	할 수 있다.

  	여기서 알아두어야 하는 것은 프록시가 생성되기 이전에 찾고자 하는 정보가 영속성 컨텍스트에
	존재한다면 프록시는 만들어지지 않는다. 만들 필요없이 캐시에서 가져다 쓰면 되기 때문이다.

```java
Board board = entityManager.getReference(Board.class, 1L);
```
	getReference()를 사용하게 되면 프록시 객체를 불러오게 된다. 이렇게 하면
	좌측 board에는 아무런 값도 없는 "가짜 객체", 프록시 객체가 들어가게 되는 것이다.
 
	단, board가 영속성 컨텍스트의 캐시에 저장되어 있지 않다는 가정하에 "가짜 객체"가 들어간다.
 	또한 board를 실질적으로 사용하게 될 시 DB에서 정보 조회를 하게 되므로, 이 때부터는
  	프록시 객체가 아닌 진짜 엔티티 객체로 나타나게 된다. 
   
   	위 사항들은 아래에서 증명.
```java
    @Test
    void testt() {
        Board board = entityManager.getReference(Board.class, 1L);

        log.info("is It initialized: {}", Hibernate.isInitialized(board)); // 프록시 여부 확인

        log.info(board.getTitle());

	// board의 타이틀 출력을 위해 DB를 조회하게 되고 이로인해 더이상 프록시가 아니게 됨
        log.info("is It initialized: {}", Hibernate.isInitialized(board)); // 프록시 여부 확인
    }
```
	결과는 false / true 이다.
```java
    @Test
    void test() {
	// 캐시에 저장된 board 정보
        Board board = entityManager.find(Board.class, 1L);

        log.info("is It initialized: {}", Hibernate.isInitialized(board)); // 프록시 여부 확인

	// board가 저장되었기에 getReference를 통해 조회해도 프록시 객체는 생성되지 않음
        board = entityManager.getReference(Board.class, 1L);

        log.info("is It initialized: {}", Hibernate.isInitialized(board)); // 프록시 여부 확인

        log.info(board.getTitle());

        log.info("is It initialized: {}", Hibernate.isInitialized(board)); // 프록시 여부 확인
    }
```
	결과는 true / true / true 이다.

#### 지연로딩
	두개의 테이블이 있고 이 테이블은 서로 연결되어있다면 서로를 조회하는 시기를 정할 수 있다.
 	앞서 프록시에서 설명했듯 JOIN을 통한 불필요한 정보값을 불러오는 것은 방지할 필요성이 있다.

	이 조회 시기를 결정하는 것은 fetch = FetchType. 이다.
 	뒤에 EAGER와 LAZY를 붙힐 수 있으며
  	1. EAGER는 처음부터 조회를 할때부터 연관된 두 테이블을 JOIN해서 가져오는 방식이다.
   	2. LAZY는 특정 테이블을 조회할때 그 테이블만 존재하며 JPA 테이블 설계상 비워지게 되는 상대측
    		테이블 정보는 프록시 객체로 체우게 되는 방식이다.

	이 두가지 전략은 비즈니스 로직에 따라 결정이 되지만,
 	일반적으로 불필요한 JOIN은 필요한 것이 맞으므로 처음에는 LAZY로 설정하되, 후에 만들어지는 대부분의
  	코드가 두 테이블을 동시에 사용하는 것이 디폴트라면 EAGER로 설정하는 것이 훨씬 더 성능이 좋을 것이다.
   	SELECT문을 두번 날리는 것보단 JOIN쿼리 한번을 날리는 것이 성능상으로 훨씬 뛰어나니 말이다.
   	

***
## 3. JPA 구조

***
## 4. JPQL
