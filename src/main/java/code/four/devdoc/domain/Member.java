package code.four.devdoc.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString (exclude = "memberRoleList")
public class Member {

    @Id
    private String nickname;

    private String pw;


    private boolean social;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<MemberRole> memberRoleList = new ArrayList<>();

    public void addRole(MemberRole memberRole){

        memberRoleList.add(memberRole);
    }

    public void clearRole(){
        memberRoleList.clear();
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changePw(String pw){
        this.pw = pw;
    }

    public void changeSocial(boolean social) {
        this.social = social;
    }

}
