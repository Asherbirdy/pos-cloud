import type { AxiosPromise } from 'axios'

import { useApiRequest } from './http'

type OrderStatus = 'COMPLETED' | 'CANCELLED' | 'REFUNDED'

interface StoreCheckoutItem {
  storeCheckoutId: string
  storeId: string
  storeShiftId: string
  memberId: string
  orderStatus: OrderStatus
  checkoutAt: string
  createdAt: string
  updatedAt: string
}

type StoreCheckoutGetAllResponse = StoreCheckoutItem[]

type StoreCheckoutGetAllByShiftResponse = StoreCheckoutItem[]

type StoreCheckoutGetByIdResponse = StoreCheckoutItem

interface StoreCheckoutCreateLine {
  storeProductItemId: string
  quantity: number
}

interface StoreCheckoutCreatePayload {
  storeShiftId: string
  checkoutItem: StoreCheckoutCreateLine[]
}

type StoreCheckoutCreateResponse = string

type StoreCheckoutCancelResponse = void

export const useStoreCheckoutApi = {
  /*
   * 取得指定 store 的所有結帳單
  */
  getAll: (storeId: string): AxiosPromise<StoreCheckoutGetAllResponse> => {
    return useApiRequest.get({
      url: `/store/${storeId}/checkout/`
    })
  },

  /*
   * 依班別查詢該班期間的所有結帳單
  */
  getAllByShift: (storeId: string, storeShiftId: string): AxiosPromise<StoreCheckoutGetAllByShiftResponse> => {
    return useApiRequest.get({
      url: `/store/${storeId}/checkout/by-shift/${storeShiftId}`
    })
  },

  /*
   * 查詢單筆結帳單詳細資料
  */
  getById: (storeId: string, storeCheckoutId: string): AxiosPromise<StoreCheckoutGetByIdResponse> => {
    return useApiRequest.get({
      url: `/store/${storeId}/checkout/${storeCheckoutId}`
    })
  },

  /*
   * 建立結帳單
  */
  create: (storeId: string, payload: StoreCheckoutCreatePayload): AxiosPromise<StoreCheckoutCreateResponse> => {
    return useApiRequest.post({
      url: `/store/${storeId}/checkout/`,
      data: payload
    })
  },

  /*
   * 取消結帳單
  */
  cancel: (storeId: string, storeCheckoutId: string): AxiosPromise<StoreCheckoutCancelResponse> => {
    return useApiRequest.post({
      url: `/store/${storeId}/checkout/${storeCheckoutId}/cancel`
    })
  }
}
