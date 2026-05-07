import type { AxiosPromise } from 'axios'

import { AuthApiRoute } from '@/enum/RequestRoute'
import type { ApiResponse } from '@/types/common/ApiResponse'

import { useApiRequest } from './http'

export interface StoreItem {
  store_id: string
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
   * 取得所有門市
  */
  getAll: (): AxiosPromise<ApiResponse<StoreGetAllResponse>> => {
    return useApiRequest.get({
      url: AuthApiRoute.Store
    })
  },

  /*
   * 建立新門市
  */
  create: (payload: StoreCreatePayload): AxiosPromise<ApiResponse<StoreCreateResponse>> => {
    return useApiRequest.post({
      url: AuthApiRoute.Store,
      data: payload
    })
  },

  /*
   * 編輯門市資料
  */
  edit: (storeId: string, payload: StoreEditPayload): AxiosPromise<ApiResponse<StoreEditResponse>> => {
    return useApiRequest.patch({
      url: `${AuthApiRoute.Store}${storeId}`,
      data: payload
    })
  }
}
