import type { AxiosPromise } from 'axios'

import { AuthApiRoute } from '@/enum/RequestRoute'

import { useApiRequest } from './http'

interface StoreProductItemEntity {
  storeProductItemId: string
  storeProductCategoryId: string
  name: string
  currentPrice: number
  createdAt: string
  updatedAt: string
}

type StoreProductItemGetAllResponse = StoreProductItemEntity[]

type StoreProductItemGetByIdResponse = StoreProductItemEntity

interface StoreProductItemCreatePayload {
  name: string
  currentPrice: number
}

type StoreProductItemCreateResponse = void

interface StoreProductItemUpdatePayload {
  name: string
  currentPrice: number
}

type StoreProductItemUpdateResponse = void

type StoreProductItemDeleteResponse = void

export const useStoreProductItemApi = {
  /*
   * 取得指定分類下的所有商品
  */
  getAll: (productCategoryId: string): AxiosPromise<StoreProductItemGetAllResponse> => {
    return useApiRequest.get({
      url: AuthApiRoute.ProductItem,
      params: { productCategoryId }
    })
  },

  /*
   * 查詢單一商品詳細資料
  */
  getById: (storeProductItemId: string): AxiosPromise<StoreProductItemGetByIdResponse> => {
    return useApiRequest.get({
      url: `${AuthApiRoute.ProductItem}${storeProductItemId}`
    })
  },

  /*
   * 在指定分類下新增商品
  */
  create: (productCategoryId: string, payload: StoreProductItemCreatePayload): AxiosPromise<StoreProductItemCreateResponse> => {
    return useApiRequest.post({
      url: AuthApiRoute.ProductItem,
      params: { productCategoryId },
      data: payload
    })
  },

  /*
   * 更新商品資料
  */
  update: (storeProductItemId: string, payload: StoreProductItemUpdatePayload): AxiosPromise<StoreProductItemUpdateResponse> => {
    return useApiRequest.patch({
      url: `${AuthApiRoute.ProductItem}${storeProductItemId}`,
      data: payload
    })
  },

  /*
   * 刪除商品
  */
  delete: (storeProductItemId: string): AxiosPromise<StoreProductItemDeleteResponse> => {
    return useApiRequest.delete({
      url: `${AuthApiRoute.ProductItem}${storeProductItemId}`
    })
  }
}
