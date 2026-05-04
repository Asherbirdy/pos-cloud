import type { AxiosPromise } from 'axios'

import { useApiRequest } from './http'

type StoreRole = 'STORE_MANAGER' | 'STORE_STAFF'

interface MemberStoreAccessCreatePayload {
  memberId: string
  storeId: string
  role: StoreRole
}

type MemberStoreAccessCreateResponse = void

interface MemberStoreAccessItem {
  memberStoreAccessId: string
  memberId: string
  enterpriseId: string
  storeId: string
  role: StoreRole
  isActive: boolean
  createdAt: string
}

type MemberStoreAccessGetByStoreIdResponse = MemberStoreAccessItem[]

interface MemberStoreAccessUpdatePayload {
  role?: StoreRole
  isActive?: boolean
}

type MemberStoreAccessUpdateResponse = void

export const useMemberStoreAccessApi = {
  /*
   * 指派 member 到某個 store
  */
  create: (payload: MemberStoreAccessCreatePayload): AxiosPromise<MemberStoreAccessCreateResponse> => {
    return useApiRequest.post({
      url: '/member-store-access/',
      data: payload
    })
  },

  /*
   * 取得指定門市目前所有的成員授權清單
  */
  getByStoreId: (storeId: string): AxiosPromise<MemberStoreAccessGetByStoreIdResponse> => {
    return useApiRequest.get({
      url: `/member-store-access/${storeId}`
    })
  },

  /*
   * 更新成員在某門市的角色或啟用狀態
  */
  update: (memberStoreAccessId: string, payload: MemberStoreAccessUpdatePayload): AxiosPromise<MemberStoreAccessUpdateResponse> => {
    return useApiRequest.patch({
      url: `/member-store-access/${memberStoreAccessId}`,
      data: payload
    })
  }
}
