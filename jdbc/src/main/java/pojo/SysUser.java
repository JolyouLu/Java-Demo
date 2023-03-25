package pojo;

import java.util.StringJoiner;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/25 13:25
 * @Description
 */
public class SysUser {
    private Integer id;
    private String account;
    private String nickname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SysUser.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("account='" + account + "'")
                .add("nickname='" + nickname + "'")
                .toString();
    }
}
