package com.app.security.aspect;

import com.app.security.enums.StoreRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 標註於 controller method，表示此 endpoint 需要該 store 的 MemberStoreAccess，
 * 且 storeRole 須在 value() 列出的角色之內。
 *
 * Aspect 會從 method 參數讀取 storeId（@RequestParam）或 storeShiftId（@PathVariable），
 * 以 storeShiftId 反查所屬 storeId，再驗證當前登入 member 的 store role。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequireStoreRole {
    StoreRole[] value();
}
