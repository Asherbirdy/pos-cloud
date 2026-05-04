import type { AxiosPromise } from 'axios'

import { useApiRequest } from './http'

interface AuthRegisterPayload {
  name: string
  email: string
  password: string
}

interface AuthRegisterResponse {
  name: string
  memberId: string
  role: string
}

interface AuthLoginPayload {
  email: string
  password: string
}

interface AuthLoginStoreAccessItem {
  storeId: string
  enterpriseId: string
  storeName: string
  storeActive: boolean
  role: string
  accessActive: boolean
}

interface AuthLoginResponse {
  name: string
  memberId: string
  role: string
  storeAccess: AuthLoginStoreAccessItem[]
}

interface AuthRegisterAdminPayload {
  name: string
  email: string
  password: string
}

interface AuthRegisterAdminResponse {
  name: string
  memberId: string
  role: string
}

export const useAuthApi = {
  /*
   * 一般使用者註冊
  */
  register: (payload: AuthRegisterPayload): AxiosPromise<AuthRegisterResponse> => {
    return useApiRequest.post({
      url: '/auth/register',
      data: payload
    })
  },

  /*
   * 登入
  */
  login: (payload: AuthLoginPayload): AxiosPromise<AuthLoginResponse> => {
    return useApiRequest.post({
      url: '/auth/login',
      data: payload
    })
  },

  /*
   * 建立 admin 帳號
  */
  registerAdmin: (payload: AuthRegisterAdminPayload): AxiosPromise<AuthRegisterAdminResponse> => {
    return useApiRequest.post({
      url: '/auth/register-admin',
      data: payload
    })
  }
}
