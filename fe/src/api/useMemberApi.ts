import type { AxiosPromise } from 'axios'

import { useApiRequest } from './http'

interface MemberShowMeResponse {
  memberId: string
  name: string
  email: string
  role: string
  createdAt: string
  updatedAt: string
}

type MemberLogoutResponse = void

export const useMemberApi = {
  /*
   * 取得目前登入會員的個人資料
  */
  showMe: (): AxiosPromise<MemberShowMeResponse> => {
    return useApiRequest.get({
      url: '/member/showMe'
    })
  },

  /*
   * 登出
  */
  logout: (): AxiosPromise<MemberLogoutResponse> => {
    return useApiRequest.post({
      url: '/member/logout'
    })
  }
}
