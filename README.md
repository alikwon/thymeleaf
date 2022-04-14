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

