package com.bitc.gotrip.service;

import com.bitc.gotrip.constant.MemberRole;
import com.bitc.gotrip.entity.Member;
import com.bitc.gotrip.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberSecurityService implements UserDetailsService {
  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
    Optional<Member> _member = this.memberRepository.findByMemberId(memberId);
    if (_member.isEmpty()) {
      throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
    }
    Member member = _member.get();
    List<GrantedAuthority> authorities = new ArrayList<>();
    if ("admin".equals(memberId)) {
      authorities.add(new SimpleGrantedAuthority(MemberRole.ADMIN.getValue()));
    } else {
      authorities.add(new SimpleGrantedAuthority(MemberRole.USER.getValue()));
    }
    return new User(member.getMemberId(), member.getMemberPw(), authorities);
  }
}
