package models.member;

public class DuplicationException extends RuntimeException{
    public DuplicationException() {
        super("이미 가입된 회원입니다");
    }
}
