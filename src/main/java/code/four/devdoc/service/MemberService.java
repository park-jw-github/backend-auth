package code.four.devdoc.service;

import code.four.devdoc.domain.Member;
import code.four.devdoc.dto.MemberDTO;
import code.four.devdoc.dto.MemberModifyDTO;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;

@Transactional
public interface MemberService {

    MemberDTO getKakaoMember(String accessToken);

    void modifyMember(MemberModifyDTO memberModifyDTO);

    default MemberDTO entityToDTO(Member member) {

        MemberDTO dto = new MemberDTO(
                member.getNickname(),
                member.getPw(),
                member.isSocial(),
                member.getMemberRoleList().stream().map(memberRole -> memberRole.name()).collect(Collectors.toList()));

        return dto;
    }

}