import type { AxiosPromise } from 'axios'

import { useApiRequest } from './http'

interface EnterpriseItem {
  enterprise_id: string
  name: string
  createdAt: string
  updatedAt: string
}

type EnterpriseGetAllResponse = EnterpriseItem[]

interface EnterpriseCreatePayload {
  name: string
}

type EnterpriseCreateResponse = void

interface EnterpriseEditPayload {
  name: string
}

type EnterpriseEditResponse = void

export const useEnterpriseApi = {
  /*
   * 取得所有企業清單
  */
  getAll: (): AxiosPromise<EnterpriseGetAllResponse> => {
    return useApiRequest.get({
      url: '/enterprise/'
    })
  },

  /*
   * 建立新的企業
  */
  create: (payload: EnterpriseCreatePayload): AxiosPromise<EnterpriseCreateResponse> => {
    return useApiRequest.post({
      url: '/enterprise/',
      data: payload
    })
  },

  /*
   * 編輯企業名稱
  */
  edit: (enterpriseId: string, payload: EnterpriseEditPayload): AxiosPromise<EnterpriseEditResponse> => {
    return useApiRequest.patch({
      url: `/enterprise/${enterpriseId}`,
      data: payload
    })
  }
}
