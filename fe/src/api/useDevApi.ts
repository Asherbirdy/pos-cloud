import type { AxiosPromise } from 'axios'

import { useApiRequest } from './http'

interface DevTestResponse {
  status: string
  service: string
  version: string
}

export const useDevApi = {
  /*
   * 測試 API
  */
  test: (): AxiosPromise<DevTestResponse> => {
    return useApiRequest.get({
      url: '/dev/test'
    })
  }
}
