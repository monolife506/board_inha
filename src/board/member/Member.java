package board.member;

// MemberDb: 사용자에 대한 DTO
public class Member
{
    private String id; // ID
    private String email; // 이메일
    private String pwd; // 암호화된 비밀번호

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPwd() { return pwd; }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
