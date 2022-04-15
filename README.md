# Thymeleaf

---
## 특징

- JSP와 유사하게 ${ }를 별도 처리 없이 이용할 수 있다.
- Model에 담긴 객체를 화면에서 JavaScript로 처리하기 편리
- 연산이나 포맷과 관련된 기능을 추가적인 개발 없이 지원
- 개발 도구를 이용할 때 .html 파일로 생성하는데 문제가 없고 별도의 확장자를 이용하지 않음.
---
## spring boot 프로젝트 생성

- Java Version : 8
- Type : gradle
- packaging : war
- 의존성 설정
    - Spring Boot DevTools
    - Lombok
    - Spring Web
    - Thymeleaf

- application.properties 설정
    ```java
    //변경 후 만들어진 결과를 보관(캐싱) 하지 않도록 설정
    spring.thymeleaf.cache=false
    ```

- IntelliJ설정
    1. 우측 상단 🔨아이콘 옆에 `Edit Configurations`
    2. On update action : **Update classes and resources**
    3. On frame deactivation : **Update classes and resources**
---
## 기본사용법

### 반복문 처리

- `th:each`라는 속성을 사용
- 사용법 : `th:each = "변수, 상태변수 : ${목록}"`

1. SampleDTO 생성

    ```java
    @Data
    @Builder(toBuilder = true)
    public class SampleDTO {
        private Long sno;
        private String first;
        private String last;
        private LocalDateTime regTime;
    }
    ```  

2. Dummy Data 생성
    
    ```java
    @GetMapping("/loop")
    public void loop(Model model) {
        List<SampleDTO> list = new ArrayList<>();
    
        for (long i = 1; i < 21; i++) {
            SampleDTO dto = SampleDTO.builder()
                    .sno(i)
                    .first("First - " + i)
                    .last("Lat - " + i)
                    .regTime(LocalDateTime.now())
                    .build();
            list.add(dto);
        }
        model.addAttribute("list", list);
    }
    ```

3. `th:each`를 이용해 출력

    ```html
    <!DOCTYPE html>
    <html lang="ko" xmlns:th="http://www.thymeleaf.org">
    <head>
      <meta charset="UTF-8">
      <title>Title</title>
    </head>
    <body>
      <ul>
        <li th:each="dto, status : ${list}"> 
                [[${status.index} + ' - ' + ${dto}]] 
            </li>
      </ul>
    </body>
    </html>
    
    <!-- 출력 결과 
    0 - SampleDTO(sno=1, first=First - 1, last=Lat - 1, regTime=2022-04-13T23:55:43.370)
    1 - SampleDTO(sno=2, first=First - 2, last=Lat - 2, regTime=2022-04-13T23:55:43.370)
    ...
    18 - SampleDTO(sno=19, first=First - 19, last=Lat - 19, regTime=2022-04-13T23:55:43.370)
    19 - SampleDTO(sno=20, first=First - 20, last=Lat - 20, regTime=2022-04-13T23:55:43.371) -->
    ```

- 상태객체 (위의 status)
  - count : 순번 (1부터)
  - index : 인덱스 (0부터)
  - odd : 홀수 true/false
  - even : 짝수 true/false
  - first : 첫번째 true/false
  - last : 마지막 true/false
  - size : 총 요소 수
  - current : 현재 요소

### 제어문처리

- `th:if ~ unless` , `삼항연산자 스타일`
- **sno 값이 5의 배수인 것은 sno, 나머지는 first 출력**

    ```html
    <!-- 제어문 처리 처리 - th:if ~ unless -->
    <ul>
      <li th:each="dto, status : ${list}"> 
    		<span  th:if="${dto.sno % 5 == 0}">[[${dto.sno}]]</span>
    		<span  th:unless="${dto.sno % 5 == 0}">[[${dto.first}]]</span>
    	</li>
    </ul>
    
    <!-- 제어문 처리 처리 - 삼항연산자 -->
    <ul>
      <li th:each="dto, status : ${list}" 
    			th:text="${dto.sno % 5 == 0} ? ${dto.sno} : ${dto.first}">
      </li>
    </ul>
    ```


### Inline 속성

- inline 속성은 주로 javaScript 처리에 유용하다

    ```java
    /**
    * 생성된 dto의 데이터를 심어서 전달
    */
    @GetMapping("/inline")
    public String inline(RedirectAttributes redirectAttributes) {
        log.info("inlineTest..................");
        long sno = 100L;
        SampleDTO dto = SampleDTO.builder()
                .sno(sno)
                .first("First - " + sno)
                .last("Lat - " + sno)
                .regTime(LocalDateTime.now())
                .build();
        redirectAttributes.addFlashAttribute("result", "success");
        redirectAttributes.addFlashAttribute("dto", dto);
        return "redirect:/thymeleaf/inlineView";
    }
    ```

    ```html
    <!-- th:inline 속성 사용 -->
    <script th:inline="javascript">
      // 심어진 데이터가 이곳에서 객체로 변환된다.
      var message = [[${result}]];
      var dto = [[${dto}]];
    
      console.log(message);
      console.log(dto);
    
    </script>
    ```

  ### `th:block`

  - `th:block`은 실제화면에서 html로 처리되지 않음 (태그가 출력되지않음)

### 링크처리

- `@{ }` 를 이용하여 처리한다.
- 파라미터를 전달해야 하는 상황에서 좀 더 가독성 좋은 코드를 만들 수 있다.
  - URL 뒤에 쿼리스트링으로 파라미터가 붙는 형태 ( `/thymeleaf/test?idx=1` )

    ```jsx
    <ul>
      <li th:each="dto, status : ${list}">
        <a th:href="@{/thymeleaf/link(sno=${dto.sno})}">[[${dto.first}]]</a>
      </li>
    </ul>
    ```

  - 파라미터를 path로 이용하는 형태 ( `/thymeleaf/1/test` )

    ```jsx
    <ul>
      <li th:each="dto, status : ${list}">
        <a th:href="@{/thymeleaf/{sno}/link(sno=${dto.sno})}">[[${dto.first}]]</a>
      </li>
    </ul>
    ```


---

## Thymeleaf 기본객체와 LocalDateTime

- Thymeleaf는 내부적으로 여러종류의 **기본 객체**를 지원함
- **기본객체**란 문자나 숫자, 웹에서 사용되는 파라미터 등 다양하다
  - Thymeleaf에서는 JSP처럼 숫자나 날짜처리를 위해 별도의 JSTL 설정이 필요하지 않다
  - `#numbers`나 `#dates`등을 별도의 설정없이 사용할 수 있다

      ```jsx
      //숫자를 5자리로 만들어야하는 상황
      [[${#numbers.formatInteger(1, 5)}]]
      
      //결과
      00001
      ```


---
## Thymeleaf 레이아웃

- Thymeleaf 레이아웃 기능은 크게 2가지 형태
  1. **JSP**의 `include` 와 같이 특정 부분을 외부 혹은 내부에서 가져와서 포함하는 형태
  2. 특정 부분을 파라미터로 전달해서 내용에 포함하는 형태

### include방식의 처리

- 특정한 부분을 다른내용으로 변경
- `th:insert` : 기존내용의 바깥형태는 그대로 유지하면서 추가
- `th:replace` : 기존의 내용을 완전히 **대체**하는 방식

    ```html
    <!-- fragment1.html-->
    ...
    <div th:fragment="part1">
        <h2>PART 1</h2>
    </div>
    <div th:fragment="part2">
        <h2>PART 2</h2>
    </div>
    <div th:fragment="part3">
        <h2>PART 3</h2>
    </div>
    ...
    <!-- th:fragment 로 작성된 태그를 다른 html에서 가져다 쓴다. -->
    ```

    ```html
    <!--  fragment2.html -->
    <hr>
      <h2>Fragment2 File</h2>
      <h2>Fragment2 File</h2>
      <h2>Fragment2 File</h2>
    <hr>
    ```

    ```html
    <!-- layout.html -->
    
    	<!-- th:replace : div태그를 완전히 대체-->
    <h1>Layout - 1</h1>
    <div th:replace="~{/thymeleaf/fragment/fragment1 :: part1}"></div>
    
    	<!-- th:insert : div 태그 안에 part2를 삽입-->
    <h1>Layout - 2</h1>
    <div th:insert="~{/thymeleaf/fragment/fragment1 :: part2}"></div>
    
    	<!-- th:block태그 사용-->
    <h1>Layout - 3</h1>
    <th:block th:replace="~{/thymeleaf/fragment/fragment1 :: part3}"></th:block>
    
    	<!-- fragment2.html 전체를 가져옴-->
    <h1>Fragment Test</h1>
    <div style="border: 1px solid blue">
    	<th:block th:replace="~{/fragments/fragment2}"></th:block>
    </div>
    ```


### 파라미터 방식의 처리

- JSP와 달리 Thymeleaf를 이용하면 특정한 태그를 파라미터처럼 전달하여 사용 가능

    ```html
    <!--  fragment2.html -->
    <div th:fragment="target(first,second)">
      <style>
        .c1{ background: red;}
        .c2{ background: blue;}
      </style>
      <div class="c1">
        <th:block th:replace="${first}"></th:block>
      </div>
      <div class="c2">
        <th:block th:replace="${second}"></th:block>
      </div>
    </div>
    ```

  - 선언된 target부분에는 `first`와 `second`라는 파라미터를 받을 수 있도록 구성

    ```html
    <!-- layoutParam.html -->
    <th:block th:replace="~{/thymeleaf/fragment/fragment3 :: target(~{this::#ulFirst}, ~{this::#ulSecond})}">
        <ul id="ulFirst">
            <li>AAA</li>
            <li>BBB</li>
            <li>CCC</li>
        </ul>
        <ul id="ulSecond">
            <li>111</li>
            <li>222</li>
            <li>333</li>
        </ul>
    </th:block>
    ```

  - target 사용시 파라미터 2개를 사용
    - `~{this::#ulFirst}`  : this는  현재페이지를 의미할 때 사용. 현재페이지의 #ulFirst태그 선택
  - fragment3을 대체 하면서 `#ulFirst`요소를 fragment3의 `${first}` 부분에 대체
  - 어지렁...

