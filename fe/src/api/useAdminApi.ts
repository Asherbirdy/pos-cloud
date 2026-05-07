import type { AxiosPromise } from 'axios'

import { AuthApiRoute } from '@/enum/RequestRoute'
import type { ApiResponse } from '@/types/common/ApiResponse'

import { useApiRequest } from './http'

export type MemberRole = 'admin' | 'user'
export type StoreRole = 'STORE_MANAGER' | 'STORE_STAFF'

export interface AdminStoreAccessItem {
  storeId: string
  storeName: string | null
  storeActive: boolean | null
  role: StoreRole
  accessActive: boolean | null
}

export interface AdminMemberItem {
  memberId: string
  name: string
  email: string
  role: MemberRole
  createdAt: string
  updatedAt: string
  storeAccess: AdminStoreAccessItem[]
}

type AdminGetAllMembersResponse = AdminMemberItem[]

interface AdminCreateMemberPayload {
  name: string
  email: string
  password: string
  role?: MemberRole
}

type AdminCreateMemberResponse = void

export const useAdminApi = {
  /*
   * 取得所有會員清單
  */
  getAllMembers: (): AxiosPromise<ApiResponse<AdminGetAllMembersResponse>> => {
    return useApiRequest.get({
      url: AuthApiRoute.AdminMember
    })
  },

  /*
   * 由 admin 直接建立會員帳號
  */
  createMember: (payload: AdminCreateMemberPayload): AxiosPromise<ApiResponse<AdminCreateMemberResponse>> => {
    return useApiRequest.post({
      url: AuthApiRoute.AdminMember,
      data: payload
    })
  }
}
