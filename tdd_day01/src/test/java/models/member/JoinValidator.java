package models.member;

import validators.Validator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JoinValidator implements Validator<Member> {
    private MemberDao memberDao;

    public void setMemberDao(MemberDao memberDao) {
        this.memberDao = memberDao;
    }
    @Override
    public void check(Member member) {
        String userId = member.getUserId();
        String userPw = member.getUserPw();
        String userPwRe = member.getUserPwRe();
        String userNm = member.getUserNm();
        String userEmail = member.getUserEmail();
        String userPhone = member.getUserPhone();

        /** 필수 항목(유저 정보) 체크 */
        requiredCheck(userId, new JoinValidationException("아이디를 입력하세요"));
        requiredCheck(userPw, new JoinValidationException("비밀번호를 입력하세요"));
        requiredCheck(userPwRe, new JoinValidationException("비밀번호를 확인하세요"));
        requiredCheck(userNm, new JoinValidationException("사용자 이름를 입력하세요"));
        requiredCheck(userEmail, new JoinValidationException("사용자 이메일을 입력하세요"));
        requiredCheck(userPhone, new JoinValidationException("전화번호를 입력하세요"));


        /** 패스워드, 패스워드 확인 체크 */
        if (!userPw.equals(userPwRe)) {
            throw new JoinValidationException("비밀번호가 일치하지 않습니다");
        }


        /** 아이디, 패스워드, 전화번호 길이 체크 */
        lengthCheck(userId, 6, 16, new JoinValidationException("아이디는 6~16자리로 입력하세요"));
        lengthCheck(userPw, 8, new JoinValidationException("비밀번호는 8자리 이상 입력하세요"));
        lengthCheck(userPhone, 10, 11, new JoinValidationException("잘못된 전화번호 입니다"));


        // 필수 유저 정보 문자열 패턴 방식 추가
        Pattern alphaP = Pattern.compile("[a-zA-Z]"); // 대소문자 알파벳 포함 여부
        Pattern numP = Pattern.compile("[0-9]"); // 숫자 포함 여부
        Pattern alphaS = Pattern.compile("[_!@#\\$%^&\\*\\(\\)]"); // 특수 문자 포함 여부
        Pattern emailAlphaS = Pattern.compile("^[a-zA-Z0-9]+@[a-zA-Z]+\\.[a-zA-Z]{2,6}$"); // 이메일 양식
        Pattern emailAlphaS2 = Pattern.compile("^[a-zA-Z0-9]+@[a-zA-Z]+\\.[a-zA-Z]{2,6}+\\.[a-zA-Z]{2,6}$"); // 이메일 양식2



        Matcher matcher1 = alphaP.matcher(userPw);
        Matcher matcher2 = numP.matcher(userPw);
        Matcher matcher3 = alphaS.matcher(userPw);

        /** 패스워드가 알파벳, 숫자, 특수문자 각 1개 이상 포함 여부 체크 */
        if (!matcher1.find() || !matcher2.find() || !matcher3.find()) {
            throw new JoinValidationException("비밀번호는 알파벳, 숫자, 특수문자가 1개 이상 포함되어야 합니다.");
        }


        /** 아이디 특수문자 포함여부 체크 */
        Matcher matcher4 = alphaS.matcher(userId);
        if (matcher4.find()) {
            throw new JoinValidationException("아이디에 특수문자를 포함할 수 없습니다");
        }

        /** 이메일 양식 체크 */
        Matcher matcher5 = emailAlphaS.matcher(userEmail);
        if (!matcher5.find()) {
            throw new JoinValidationException("이메일이 올바르지 않습니다");
        }
        Matcher matcher6 = emailAlphaS2.matcher(userEmail);
        if (!matcher6.find()) {
            throw new JoinValidationException("이메일이 올바르지 않습니다");
        }



        /** 중복가입 체크 */
        if (memberDao.get(userId) != null) {
            throw new DuplicationException();
        }

    }
}
