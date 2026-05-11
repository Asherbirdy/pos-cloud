package com.app.security.aspect;

import com.app.security.dao.StoreProductCategoryDao;
import com.app.security.dao.StoreProductItemDao;
import com.app.security.dao.StoreShiftDao;
import com.app.security.enums.StoreRole;
import com.app.security.model.StoreProductCategory;
import com.app.security.model.StoreProductItem;
import com.app.security.model.StoreShift;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Map;

@Aspect
@Component
public class StoreAccessAspect {

    private final StoreShiftDao storeShiftDao;
    private final StoreProductCategoryDao storeProductCategoryDao;
    private final StoreProductItemDao storeProductItemDao;

    public StoreAccessAspect(StoreShiftDao storeShiftDao,
                             StoreProductCategoryDao storeProductCategoryDao,
                             StoreProductItemDao storeProductItemDao) {
        this.storeShiftDao = storeShiftDao;
        this.storeProductCategoryDao = storeProductCategoryDao;
        this.storeProductItemDao = storeProductItemDao;
    }

    @Before("@annotation(requireStoreRole)")
    public void check(JoinPoint joinPoint, RequireStoreRole requireStoreRole) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getCredentials() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
        }

        String storeId = resolveStoreId(joinPoint);
        if (storeId == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "STORE_ID_NOT_RESOLVED");
        }

        Map<String, String> storeAccess = readStoreAccess(authentication);
        String roleName = storeAccess.get(storeId);
        if (roleName == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NO_STORE_ACCESS");
        }

        StoreRole memberRole;
        try {
            memberRole = StoreRole.valueOf(roleName);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "INVALID_STORE_ROLE");
        }

        for (StoreRole allowed : requireStoreRole.value()) {
            if (allowed == memberRole) {
                return;
            }
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "INSUFFICIENT_STORE_ROLE");
    }

    private String resolveStoreId(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < paramNames.length; i++) {
            if ("storeId".equals(paramNames[i]) && args[i] != null) {
                return (String) args[i];
            }
        }
        for (int i = 0; i < paramNames.length; i++) {
            if ("storeShiftId".equals(paramNames[i]) && args[i] != null) {
                StoreShift shift = storeShiftDao.getById((String) args[i]);
                if (shift == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "STORE_SHIFT_NOT_FOUND");
                }
                return shift.getStoreId();
            }
        }
        for (int i = 0; i < paramNames.length; i++) {
            if ("productCategoryId".equals(paramNames[i]) && args[i] != null) {
                StoreProductCategory category = storeProductCategoryDao.getById((String) args[i]);
                if (category == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "STORE_PRODUCT_CATEGORY_NOT_FOUND");
                }
                return category.getStoreId();
            }
        }
        for (int i = 0; i < paramNames.length; i++) {
            if ("storeProductItemId".equals(paramNames[i]) && args[i] != null) {
                StoreProductItem item = storeProductItemDao.getById((String) args[i]);
                if (item == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "STORE_PRODUCT_ITEM_NOT_FOUND");
                }
                StoreProductCategory category = storeProductCategoryDao.getById(item.getStoreProductCategoryId());
                if (category == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "STORE_PRODUCT_CATEGORY_NOT_FOUND");
                }
                return category.getStoreId();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> readStoreAccess(Authentication authentication) {
        Object details = authentication.getDetails();
        if (details instanceof Map) {
            return (Map<String, String>) details;
        }
        return Collections.emptyMap();
    }
}
