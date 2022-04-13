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

...ing

