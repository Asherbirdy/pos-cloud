package com.app.security.service;

import java.util.Map;

public interface EnterpriseService {
    Map<Object, String> getAll();
    Map<Object, String> create(String id);
    Map<Object, String> edit(String name);

}
