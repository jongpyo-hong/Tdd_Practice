package models.member;

import validators.Validator;

import javax.servlet.http.HttpServletRequest;

public class LoginService {
    private Validator<HttpServletRequest> validator;
    private MemberDao memberDao;
    public LoginService (Validator<HttpServletRequest> validator) {
        this.validator  = validator;
    }

    public void login(HttpServletRequest request) {
        validator.check(request);
    }
}
