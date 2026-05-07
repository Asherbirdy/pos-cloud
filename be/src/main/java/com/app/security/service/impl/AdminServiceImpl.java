package com.app.security.service.impl;

import com.app.security.dao.MemberDao;
import com.app.security.dao.MemberStoreAccessDao;
import com.app.security.dto.Admin.AdminCreateMemberRequest;
import com.app.security.dto.Admin.AdminMemberItem;
import com.app.security.dto.Auth.StoreAccessItem;
import com.app.security.model.Member;
import com.app.security.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class AdminServiceImpl implements AdminService {

    private static final Set<String> ALLOWED_ROLES = Set.of("admin", "user");

    private final MemberDao memberDao;
    private final MemberStoreAccessDao memberStoreAccessDao;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(MemberDao memberDao, MemberStoreAccessDao memberStoreAccessDao,
                             PasswordEncoder passwordEncoder) {
        this.memberDao = memberDao;
        this.memberStoreAccessDao = memberStoreAccessDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<AdminMemberItem> getAllMembers() {
        List<Member> members = memberDao.getAllMembers();
        List<AdminMemberItem> result = new ArrayList<>();
        for (Member member : members) {
            List<StoreAccessItem> storeAccess = memberStoreAccessDao
                    .getAllStoreAccessItemsByMemberId(member.getMemberId());
            result.add(new AdminMemberItem(
                    member.getMemberId(),
                    member.getName(),
                    member.getEmail(),
                    member.getRole(),
                    member.getCreatedAt(),
                    member.getUpdatedAt(),
                    storeAccess
            ));
        }
        return result;
    }

    @Override
    public String createMember(AdminCreateMemberRequest request) {
        String role = request.getRole() == null || request.getRole().isBlank() ? "user" : request.getRole();
        if (!ALLOWED_ROLES.contains(role)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "INVALID_ROLE");
        }

        Member existing = memberDao.getMemberByEmail(request.getEmail());
        if (existing != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "EMAIL_ALREADY_USED");
        }

        Member member = new Member();
        member.setName(request.getName());
        member.setEmail(request.getEmail());
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        member.setRole(role);

        return memberDao.createMember(member);
    }
}
