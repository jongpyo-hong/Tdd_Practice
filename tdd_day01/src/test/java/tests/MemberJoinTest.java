package tests;

import models.member.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MemberJoinTest {
    private Member member;
    private JoinService joinService;

    @BeforeEach
    void init() {
        MemberDao memberDao = new MemberDao();
        JoinValidator joinValidator = new JoinValidator();
        joinValidator.setMemberDao(memberDao);
        joinService = new JoinService(joinValidator, memberDao);
        member = getMember();
    }

    public Member getMember() {
        member = new Member();
        member.setUserId("user01");
        member.setUserPw("a123456!!");
        member.setUserPwRe("a123456!!");
        member.setUserNm("사용자01");
        member.setUserEmail("user01@example.com");
        member.setUserPhone("01012345678");

        return member;
    }

    @Test
    @DisplayName("회원가입 테스트 - 성공시 예외없음")
    void JoinSuccessTest() {
        assertDoesNotThrow(() -> {
            joinService.join(member);
        });
    }

    @Test
    @DisplayName("필수 항목 체크(유저 정보) 테스트 - 실패시 예외발생")
    void RequiredFieldsTest() {
        assertAll(
                () -> assertThrows(JoinValidationException.class, () -> {
                   member.setUserId(null);
                   joinService.join(member);
                }),
                () -> assertThrows(JoinValidationException.class, () -> {
                    member.setUserId(" ");
                    joinService.join(member);
                }),
                () -> assertThrows(JoinValidationException.class, () -> {
                    member.setUserPw(null);
                    joinService.join(member);
                }),
                () -> assertThrows(JoinValidationException.class, () -> {
                    member.setUserPw(" ");
                    joinService.join(member);
                }),
                () -> assertThrows(JoinValidationException.class, () -> {
                    member.setUserPwRe(null);
                    joinService.join(member);
                }),
                () -> assertThrows(JoinValidationException.class, () -> {
                    member.setUserPwRe(" ");
                    joinService.join(member);
                }),
                () -> assertThrows(JoinValidationException.class, () -> {
                    member.setUserNm(null);
                    joinService.join(member);
                }),
                () -> assertThrows(JoinValidationException.class, () -> {
                    member.setUserNm(" ");
                    joinService.join(member);
                }),
                () -> assertThrows(JoinValidationException.class, () -> {
                    member.setUserEmail(null);
                    joinService.join(member);
                }),
                () -> assertThrows(JoinValidationException.class, () -> {
                    member.setUserEmail(" ");
                    joinService.join(member);
                }),
                () -> assertThrows(JoinValidationException.class, () -> {
                    member.setUserPhone(null);
                    joinService.join(member);
                }),
                () -> assertThrows(JoinValidationException.class, () -> {
                    member.setUserPhone(" ");
                    joinService.join(member);
                })
        );
    }

    @Test
    @DisplayName("패스워드, 패스워드 확인 일치 테스트 - 실패시 예외발생")
    void passwordCheckTest1() {
        JoinValidationException thrown = assertThrows(JoinValidationException.class, () -> {
           member.setUserPwRe("a456456!!");
           joinService.join(member);
        });

        String message = thrown.getMessage();
        assertTrue(message.contains("비밀번호가 일치하지"));
    }

    @Test
    @DisplayName("유저 정보 길이 테스트 - 실패시 예외발생")
    void lengthCheckTest() {
        assertAll(
                () -> assertThrows(JoinValidationException.class, () -> {
                    member.setUserId("use");
                    joinService.join(member);
                }),
                () -> assertThrows(JoinValidationException.class, () -> {
                    member.setUserId("user123123123123123123123123");
                    joinService.join(member);
                }),
                () -> {
                    JoinValidationException thrown = assertThrows(JoinValidationException.class, () -> {
                        member = getMember();
                        member.setUserPw("a124!");
                        member.setUserPwRe("a124!");
                        joinService.join(member);
                    });

                    String message = thrown.getMessage();
                    assertTrue(message.contains("비밀번호는 8자리 이상"));
                },
                () -> {
                    JoinValidationException thrown = assertThrows(JoinValidationException.class, () -> {
                        member = getMember();
                        member.setUserPhone("010123412341234");
                        joinService.join(member);
                    });

                    String message = thrown.getMessage();
                    assertTrue(message.contains("잘못된 전화번호"));
                }

        );
    }

    @Test
    @DisplayName("패스워드 문자열 패턴 테스트(숫자, 문자, 특수문자 각 1개이상) - 실패시 예외발생")
    void passwordCheckTest2() {
        assertAll(
                () -> assertThrows(JoinValidationException.class, () -> {
                    member.setUserPw("a!3");
                    member.setUserPwRe("a!3");
                    joinService.join(member);
                }),
                () -> assertThrows(JoinValidationException.class, () -> {
                    member.setUserPw("!!!!!!!!!");
                    member.setUserPwRe("!!!!!!!!!");
                    joinService.join(member);
                })
        );
    }

    @Test
    @DisplayName("아이디 문자열 패턴 테스트(특수문자 X) - 실패시 예외발생")
    void idCheckTest() {
            assertThrows(JoinValidationException.class, () -> {
            member.setUserId("!!user01");
            joinService.join(member);
        });
    }

    @Test
    @DisplayName("이메일 문자열 패턴 테스트(@를 제외한 특수문자 X) - 실패시 예외발생")
    void emailCheckTest() {
        assertThrows(JoinValidationException.class, () -> {
            member.setUserEmail("user01@!@#@example.com");
            joinService.join(member);
        });
    }

    @Test
    @DisplayName("중복 가입 테스트 - 실패시 예외발생")
    void memberExistsTest() {
           assertThrows(DuplicationException.class, () -> {
           member.setUserId("user123");
           joinService.join(member);

           joinService.join(member);
        });
    }
}
