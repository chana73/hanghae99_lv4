# Spring Lv.4

<aside>
🔥 단계별 퀘스트

</aside>

[Spring Lv.1](https://github.com/chana73/hanghae99.git)

[Spring Lv.2](https://github.com/chana73/hanghae99_lv2.git)

[Spring Lv.3](https://github.com/chana73/hanghae99_lv3.git)

[Spring Lv.4](https://github.com/chana73/hanghae99_lv4.git)

[Spring Lv.5](https://github.com/chana73/hanghae99_lv5.git)

<aside>
🏁 **Goal: "Spring Security를 적용한 나만의 항해 블로그 백엔드 서버 만들기"

</aside>

1. 회원 가입 API
    - username, password를 Client에서 전달받기
    - username은  `최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)`로 구성되어야 한다.
    - password는  `최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9)`로 구성되어야 한다.
    - DB에 중복된 username이 없다면 회원을 저장하고 Client 로 성공했다는 메시지, 상태코드 반환하기
    - 회원 권한 부여하기 (ADMIN, USER) - ADMIN 회원은 모든 게시글 수정 / 삭제 가능 
            
2. 로그인 API
    - username, password를 Client에서 전달받기
    - DB에서 username을 사용하여 저장된 회원의 유무를 확인하고 있다면 password 비교하기
    - 로그인 성공 시, 로그인에 성공한 유저의 정보와 JWT를 활용하여 토큰을 발급하고, 
    발급한 토큰을 Header에 추가하고 성공했다는 메시지, 상태코드 와 함께 Client에 반환하기
3. 전체 게시글 목록 조회 API
    - 제목, 작성자명(username), 작성 내용, 작성 날짜를 조회하기
    - 작성 날짜 기준 내림차순으로 정렬하기
4. 게시글 작성 API
    - ~~토큰을 검사하여, 유효한 토큰일 경우에만 게시글 작성 가능~~  ⇒ Spring Security를 사용하여 토큰 검사 및 인증하기!
    - 제목, 작성자명(username), 작성 내용을 저장하고
    - 저장된 게시글을 Client 로 반환하기
5. 선택한 게시글 조회 API
    - 선택한 게시글의 제목, 작성자명(username), 작성 날짜, 작성 내용을 조회하기 
    (검색 기능이 아닙니다. 간단한 게시글 조회만 구현해주세요.)
6. 선택한 게시글 수정 API
    - ~~토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 게시글만 수정 가능~~ ⇒ Spring Security를 사용하여 토큰 검사 및 인증하기!
    - 제목, 작성 내용을 수정하고 수정된 게시글을 Client 로 반환하기
7. 선택한 게시글 삭제 API  
    - ~~토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 게시글만 삭제 가능~~ ⇒ Spring Security를 사용하여 토큰 검사 및 인증하기!
    - 선택한 게시글을 삭제하고 Client 로 성공했다는 메시지, 상태코드 반환하기
    
## 1. [API 노션](https://www.notion.so/787477f318574ef2811a2e142389d6db?v=8e9529e9283841afa3f0f1ba08c6813e&pvs=4)



