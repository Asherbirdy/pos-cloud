package com.app.security.controller;

import com.app.security.dto.Response;
import com.app.security.model.StoreShift;
import com.app.security.service.StoreShiftService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store/{storeId}/shift")
public class StoreShiftController {

    private final StoreShiftService storeShiftService;

    public StoreShiftController(StoreShiftService storeShiftService) {
        this.storeShiftService = storeShiftService;
    }

    /**
     * 取得指定 store 的所有班別紀錄（依開班時間新到舊排序）。
     * 用於後台檢視班表、稽核未關班的紀錄。
     */
    @GetMapping("/")
    public Response<List<StoreShift>> getAll(@PathVariable String storeId) {
        List<StoreShift> list = storeShiftService.getAllByStoreId(storeId);
        return new Response<>("Success", list, HttpStatus.OK);
    }

    /**
     * 查詢單筆班別詳細資料。
     */
    @GetMapping("/{storeShiftId}")
    public Response<StoreShift> getById(@PathVariable String storeShiftId) {
        StoreShift shift = storeShiftService.getById(storeShiftId);
        return new Response<>("Success", shift, HttpStatus.OK);
    }

    /**
     * 開班：由目前登入的 member 開啟一筆 OPEN 班別。
     * 開班前會檢查該 store 目前 OPEN 數量是否已達 store.running_devices_limit，
     * 若已達上限則回傳 409 SHIFT_LIMIT_REACHED。
     */
    @PostMapping("/open")
    public Response<String> openShift(@PathVariable String storeId) {
        String storeShiftId = storeShiftService.openShift(storeId);
        return new Response<>("Shift Open", storeShiftId, HttpStatus.CREATED);
    }

    /**
     * 關班：將指定班別由 OPEN 改為 CLOSED 並寫入 close_time。
     * 若有人忘記關班，可由 STORE_MANAGER 代為關班。
     * 已 CLOSED 的班別會回傳 409 SHIFT_ALREADY_CLOSED。
     */
    @PostMapping("/{storeShiftId}/close")
    public Response<Void> closeShift(@PathVariable String storeShiftId) {
        storeShiftService.closeShift(storeShiftId);
        return new Response<>("Shift Close", null, HttpStatus.OK);
    }
}
