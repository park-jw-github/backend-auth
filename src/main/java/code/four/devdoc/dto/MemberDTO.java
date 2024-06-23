package code.four.devdoc.dto;

import java.util.*;
import java.util.stream.Collectors;

import jakarta.persistence.Column;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDTO extends User {

    private String nickname;

    private String pw;

    private boolean social;

    private List<String> roleNames = new ArrayList<>();

    public MemberDTO(String nickname, String pw, boolean social, List<String> roleNames) {
        super(
                nickname,
                pw,
                roleNames.stream().map(str -> new SimpleGrantedAuthority("ROLE_"+str)).collect(Collectors.toList()));

        this.nickname = nickname;
        this.pw = pw;
        this.social = social;
        this.roleNames = roleNames;
    }

    public Map<String, Object> getClaims() {

        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("nickname", nickname);
        dataMap.put("pw",pw);
        dataMap.put("social", social);
        dataMap.put("roleNames", roleNames);

        return dataMap;
    }

}
