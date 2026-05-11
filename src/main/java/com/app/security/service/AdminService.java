package com.app.security.service;

import com.app.security.dto.Admin.AdminCreateMemberRequest;
import com.app.security.dto.Admin.AdminMemberItem;

import java.util.List;

public interface AdminService {

    List<AdminMemberItem> getAllMembers();

    String createMember(AdminCreateMemberRequest request);
}
