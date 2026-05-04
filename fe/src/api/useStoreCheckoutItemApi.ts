import type { AxiosPromise } from 'axios'

import { useApiRequest } from './http'

type OrderStatus = 'COMPLETED' | 'CANCELLED' | 'REFUNDED'

interface StoreCheckoutItemEntity {
  storeCheckoutItemId: string
  storeCheckoutId: string
  storeProductItemId: string
  quantity: number
  unitPrice: number
  status: OrderStatus
  createdAt: string
}

type StoreCheckoutItemGetAllResponse = StoreCheckoutItemEntity[]

type StoreCheckoutItemGetByIdResponse = StoreCheckoutItemEntity

interface StoreCheckoutItemCreatePayload {
  storeProductItemId: string
  quantity: number
  unitPrice: number
}

type StoreCheckoutItemCreateResponse = string

type StoreCheckoutItemDeleteResponse = void

export const useStoreCheckoutItemApi = {
  /*
   * 取得指定結帳單下的所有商品明細
  */
  getAll: (storeCheckoutId: string): AxiosPromise<StoreCheckoutItemGetAllResponse> => {
    return useApiRequest.get({
      url: `/checkout/${storeCheckoutId}/item/`
    })
  },

  /*
   * 查詢單筆結帳明細
  */
  getById: (storeCheckoutId: string, storeCheckoutItemId: string): AxiosPromise<StoreCheckoutItemGetByIdResponse> => {
    return useApiRequest.get({
      url: `/checkout/${storeCheckoutId}/item/${storeCheckoutItemId}`
    })
  },

  /*
   * 新增一筆結帳明細到指定結帳單
  */
  create: (storeCheckoutId: string, payload: StoreCheckoutItemCreatePayload): AxiosPromise<StoreCheckoutItemCreateResponse> => {
    return useApiRequest.post({
      url: `/checkout/${storeCheckoutId}/item/`,
      data: payload
    })
  },

  /*
   * 刪除一筆結帳明細
  */
  delete: (storeCheckoutId: string, storeCheckoutItemId: string): AxiosPromise<StoreCheckoutItemDeleteResponse> => {
    return useApiRequest.delete({
      url: `/checkout/${storeCheckoutId}/item/${storeCheckoutItemId}`
    })
  }
}
