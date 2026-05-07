package com.app.security.dto.Store;

import com.app.security.dto.MemberStoreAccess.StoreMemberAccessItem;

import java.util.Date;
import java.util.List;

public class StoreWithMembersResponse {

    private String store_id;
    private String name;
    private Boolean active;
    private Integer running_devices_limit;
    private Date createdAt;
    private Date updatedAt;
    private List<StoreMemberAccessItem> members;

    public StoreWithMembersResponse(String store_id, String name, Boolean active,
                                     Integer running_devices_limit, Date createdAt, Date updatedAt,
                                     List<StoreMemberAccessItem> members) {
        this.store_id = store_id;
        this.name = name;
        this.active = active;
        this.running_devices_limit = running_devices_limit;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.members = members;
    }

    public String getStore_id() {
        return store_id;
    }

    public String getName() {
        return name;
    }

    public Boolean getActive() {
        return active;
    }

    public Integer getRunning_devices_limit() {
        return running_devices_limit;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public List<StoreMemberAccessItem> getMembers() {
        return members;
    }
}
