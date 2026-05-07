import type { AxiosPromise } from 'axios'

import { AuthApiRoute } from '@/enum/RequestRoute'
import type { ApiResponse } from '@/types/common/ApiResponse'

import { useApiRequest } from './http'

export type StoreRole = 'STORE_MANAGER' | 'STORE_STAFF'

interface MemberStoreAccessCreatePayload {
  memberId: string
  storeId: string
  role: StoreRole
}

type MemberStoreAccessCreateResponse = void

export interface MemberStoreAccessItem {
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
  create: (payload: MemberStoreAccessCreatePayload): AxiosPromise<ApiResponse<MemberStoreAccessCreateResponse>> => {
    return useApiRequest.post({
      url: AuthApiRoute.MemberStoreAccess,
      data: payload
    })
  },

  /*
   * 取得指定門市目前所有的成員授權清單
  */
  getByStoreId: (storeId: string): AxiosPromise<ApiResponse<MemberStoreAccessGetByStoreIdResponse>> => {
    return useApiRequest.get({
      url: `${AuthApiRoute.MemberStoreAccess}${storeId}`
    })
  },

  /*
   * 更新成員在某門市的角色或啟用狀態
  */
  update: (memberStoreAccessId: string, payload: MemberStoreAccessUpdatePayload): AxiosPromise<ApiResponse<MemberStoreAccessUpdateResponse>> => {
    return useApiRequest.patch({
      url: `${AuthApiRoute.MemberStoreAccess}${memberStoreAccessId}`,
      data: payload
    })
  }
}
