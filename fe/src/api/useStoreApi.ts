import type { AxiosPromise } from 'axios'

import { AuthApiRoute } from '@/enum/RequestRoute'
import type { ApiResponse } from '@/types/common/ApiResponse'

import { useApiRequest } from './http'

interface StoreItem {
  store_id: string
  enterprise_id: string
  name: string
  active: boolean
  running_devices_limit: number
  createdAt: string
  updatedAt: string
}

type StoreGetAllResponse = StoreItem[]

interface StoreCreatePayload {
  name: string
}

type StoreCreateResponse = void

interface StoreEditPayload {
  name: string
  isActive?: boolean
  running_devices_limit?: number
}

type StoreEditResponse = void

export const useStoreApi = {
  /*
   * 取得指定企業底下的所有門市
  */
  getAll: (enterpriseId: string): AxiosPromise<ApiResponse<StoreGetAllResponse>> => {
    return useApiRequest.get({
      url: `${AuthApiRoute.Enterprise}${enterpriseId}/store/`
    })
  },

  /*
   * 在指定企業下建立新門市
  */
  create: (enterpriseId: string, payload: StoreCreatePayload): AxiosPromise<ApiResponse<StoreCreateResponse>> => {
    return useApiRequest.post({
      url: `${AuthApiRoute.Enterprise}${enterpriseId}/store/`,
      data: payload
    })
  },

  /*
   * 編輯門市資料
  */
  edit: (enterpriseId: string, storeId: string, payload: StoreEditPayload): AxiosPromise<ApiResponse<StoreEditResponse>> => {
    return useApiRequest.patch({
      url: `${AuthApiRoute.Enterprise}${enterpriseId}/store/${storeId}`,
      data: payload
    })
  }
}
