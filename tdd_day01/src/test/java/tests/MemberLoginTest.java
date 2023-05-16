package tests;

import models.member.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MemberLoginTest {
    private LoginService loginService;
    private JoinService joinService;

    private Member member;
    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void init() {
        MemberDao memberDao = new MemberDao();

        loginService = new LoginService(new LoginValidator());

        JoinValidator joinValidator = new JoinValidator();
        joinValidator.setMemberDao(memberDao);
        joinService = new JoinService(joinValidator, memberDao);

        member = memberDao.get("user01");
        member = new Member();
        member.setUserId("user01");
        member.setUserPw("a123456!!");
        member.setUserPwRe("a123456!!");
        member.setUserNm("사용자01");
        member.setUserEmail("user01@example.com");
        member.setUserPhone("01012345678");
        joinService.join(member);

    }

    private void SuccessData() {
        given(request.getParameter("userId")).willReturn(member.getUserId());
        given(request.getParameter("userPw")).willReturn(member.getUserPw());
    }

    private void WrongData(String userId, String userPw) {
        given(request.getParameter("userId")).willReturn(userId);
        given(request.getParameter("userPw")).willReturn(userPw);
    }

    @Test
    @DisplayName("로그인 테스트 - 성공시 예외없음")
    void LoginSuccessTest() {
        assertDoesNotThrow(() -> {
            SuccessData();
            loginService.login(request);
        });
    }

    @Test
    @DisplayName("필수 항목 체크 - 실패시 예외 발생")
    void RequiredFieldCheckTest() {
        assertAll(
                () -> assertThrows(LoginValidationException.class, () -> {
                    WrongData(null, member.getUserPw());
                    loginService.login(request);
                }),
                () -> assertThrows(LoginValidationException.class, () -> {
                    WrongData(" ", member.getUserPw());
                    loginService.login(request);
                }),
                () -> assertThrows(LoginValidationException.class, () -> {
                    WrongData(member.getUserId(),null);
                    loginService.login(request);
                }),
                () -> assertThrows(LoginValidationException.class, () -> {
                    WrongData(member.getUserId()," ");
                    loginService.login(request);
                })
        );
    }
}
