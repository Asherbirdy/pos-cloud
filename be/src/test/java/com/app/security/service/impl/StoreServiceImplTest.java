package com.app.security.service.impl;

import com.app.security.dao.StoreDao;
import com.app.security.model.Store;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StoreServiceImplTest {

    @Mock
    private StoreDao storeDao;

    @InjectMocks
    private StoreServiceImpl storeService;

    @Test
    @DisplayName("edit: runningDevicesLimit > 10 應丟 400 且不呼叫 DAO")
    public void edit_limitExceeded_throws() {
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> storeService.edit("s1", "name", true, 11)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("RUNNING_DEVICES_LIMIT_ERROR", ex.getReason());
        verifyNoInteractions(storeDao);
    }
}
