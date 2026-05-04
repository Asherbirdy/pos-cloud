import type { AxiosPromise } from 'axios'

import { useApiRequest } from './http'

type ShiftStatus = 'OPEN' | 'CLOSED'

interface StoreShiftEntity {
  storeShiftId: string
  storeId: string
  memberId: string
  date: string
  status: ShiftStatus
  openTime: string
  closeTime: string | null
  createdAt: string
  updatedAt: string
}

type StoreShiftGetAllResponse = StoreShiftEntity[]

type StoreShiftGetByIdResponse = StoreShiftEntity

type StoreShiftOpenShiftResponse = string

type StoreShiftCloseShiftResponse = void

export const useStoreShiftApi = {
  /*
   * 取得指定 store 的所有班別紀錄
  */
  getAll: (storeId: string): AxiosPromise<StoreShiftGetAllResponse> => {
    return useApiRequest.get({
      url: '/storeShift/',
      params: { storeId }
    })
  },

  /*
   * 查詢單筆班別詳細資料
  */
  getById: (storeShiftId: string): AxiosPromise<StoreShiftGetByIdResponse> => {
    return useApiRequest.get({
      url: `/storeShift/${storeShiftId}`
    })
  },

  /*
   * 開班
  */
  openShift: (storeId: string): AxiosPromise<StoreShiftOpenShiftResponse> => {
    return useApiRequest.post({
      url: '/storeShift/open',
      params: { storeId }
    })
  },

  /*
   * 關班
  */
  closeShift: (storeShiftId: string): AxiosPromise<StoreShiftCloseShiftResponse> => {
    return useApiRequest.post({
      url: `/storeShift/${storeShiftId}/close`
    })
  }
}
