package models.member;

public class JoinService {
    private JoinValidator joinValidator;
    private MemberDao memberDao;

    public JoinService(JoinValidator joinValidator, MemberDao memberDao) {
        this.joinValidator = joinValidator;
        this.memberDao = memberDao;
    }

    public void join(Member member) {
        joinValidator.check(member);

        memberDao.insert(member);
    }
}
