package org.zerock.club.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.club.entity.ClubMember;
import org.zerock.club.entity.ClubMemberRole;
import org.zerock.club.repository.ClubMemberRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class ClubMemberTests {

    @Autowired
    private ClubMemberRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertDummies() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            ClubMember clubMember = ClubMember.builder()
                    .email("user" + i + "@test.com")
                    .name("USER" + i)
                    .fromSocial(false)
                    .roleSet(new HashSet<ClubMemberRole>())
                    .password(passwordEncoder.encode("1234"))
                    .build();

            // 1 - 80: USER
            // 81 - 90: USER, MANAGER
            // 91 - 100: USER, MANAGER, ADMIN
            clubMember.addMemberRole(ClubMemberRole.USER);
            if (i > 80) {
                clubMember.addMemberRole(ClubMemberRole.MANAGER);
            }
            if (i > 90) {
                clubMember.addMemberRole(ClubMemberRole.ADMIN);
            }
            repository.save(clubMember);
        });
    }
}