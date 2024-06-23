package code.four.devdoc.service;

import code.four.devdoc.domain.Member;
import code.four.devdoc.domain.MemberRole;
import code.four.devdoc.dto.MemberDTO;
import code.four.devdoc.dto.MemberModifyDTO;
import code.four.devdoc.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberDTO getKakaoMember(String accessToken) {
        String nickname = getNicknameFromKakaoAccessToken(accessToken);

        log.info("nickname: " + nickname);

        Optional<Member> result = memberRepository.findById(nickname);

        // 기존의 회원
        if (result.isPresent()) {
            MemberDTO memberDTO = entityToDTO(result.get());
            return memberDTO;
        }

        // 회원이 아니었다면
        // 닉네임은 '소셜회원'으로
        // 패스워드는 임의로 생성
        Member socialMember = makeSocialMember(nickname);
        memberRepository.save(socialMember);

        MemberDTO memberDTO = entityToDTO(socialMember);
        return memberDTO;
    }

    private String getNicknameFromKakaoAccessToken(String accessToken) {
        String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me";

        if (accessToken == null) {
            throw new RuntimeException("Access Token is null");
        }
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(kakaoGetUserURL).build();

        ResponseEntity<LinkedHashMap> response = restTemplate.exchange(
                uriBuilder.toString(),
                HttpMethod.GET,
                entity,
                LinkedHashMap.class);

        log.info(response);

        LinkedHashMap<String, Object> bodyMap = response.getBody();

        log.info("------------------------------------");
        log.info(bodyMap);

        LinkedHashMap<String, Object> properties = (LinkedHashMap<String, Object>) bodyMap.get("properties");
        LinkedHashMap<String, Object> kakaoAccount = (LinkedHashMap<String, Object>) bodyMap.get("kakao_account");

        log.info("kakaoAccount: " + kakaoAccount);

        String nickname = null;
        if (properties != null) {
            nickname = (String) properties.get("nickname");
        }
        if (nickname == null && kakaoAccount != null) {
            LinkedHashMap<String, Object> profile = (LinkedHashMap<String, Object>) kakaoAccount.get("profile");
            if (profile != null) {
                nickname = (String) profile.get("nickname");
            }
        }

        if (nickname == null) {
            log.warn("Nickname not found in Kakao account, setting default nickname");
            nickname = "소셜회원";
        }

        return nickname;
    }

    private Member makeSocialMember(String nickname) {
        String tempPassword = makeTempPassword();

        log.info("tempPassword: " + tempPassword);

        Member member = Member.builder()
                .nickname(nickname)
                .pw(passwordEncoder.encode(tempPassword))
                .social(true)
                .build();

        member.addRole(MemberRole.USER);

        return member;
    }

    private String makeTempPassword() {
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < 10; i++) {
            buffer.append((char) ((int) (Math.random() * 55) + 65));
        }
        return buffer.toString();
    }

    @Override
    public void modifyMember(MemberModifyDTO memberModifyDTO) {
        Optional<Member> result = memberRepository.findById(memberModifyDTO.getNickname());

        Member member = result.orElseThrow();

        member.changeNickname(memberModifyDTO.getNickname());
        member.changePw(passwordEncoder.encode(memberModifyDTO.getPw()));
        member.changeSocial(false);

        memberRepository.save(member);
    }
}


