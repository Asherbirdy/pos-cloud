import type { AxiosPromise } from 'axios'

import { AuthApiRoute } from '@/enum/RequestRoute'

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
      url: AuthApiRoute.Enterprise
    })
  },

  /*
   * 建立新的企業
  */
  create: (payload: EnterpriseCreatePayload): AxiosPromise<EnterpriseCreateResponse> => {
    return useApiRequest.post({
      url: AuthApiRoute.Enterprise,
      data: payload
    })
  },

  /*
   * 編輯企業名稱
  */
  edit: (enterpriseId: string, payload: EnterpriseEditPayload): AxiosPromise<EnterpriseEditResponse> => {
    return useApiRequest.patch({
      url: `${AuthApiRoute.Enterprise}${enterpriseId}`,
      data: payload
    })
  }
}
