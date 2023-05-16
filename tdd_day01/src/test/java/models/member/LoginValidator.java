package models.member;

import validators.Validator;

import javax.servlet.http.HttpServletRequest;

public class LoginValidator implements Validator<HttpServletRequest> {
    @Override
    public void check(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        String userPw = request.getParameter("userPw");

        requiredCheck(userId, new LoginValidationException("아이디를 입력하세요"));
        requiredCheck(userPw, new LoginValidationException("비밀번호를 입력하세요"));
    }
}
