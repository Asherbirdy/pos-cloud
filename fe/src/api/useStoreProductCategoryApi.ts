import type { AxiosPromise } from 'axios'

import { useApiRequest } from './http'

interface StoreProductCategoryItem {
  productCategoryId: string
  name: string
  storeId: string
  createdAt: string
  updatedAt: string
}

type StoreProductCategoryGetAllResponse = StoreProductCategoryItem[]

type StoreProductCategoryGetByIdResponse = StoreProductCategoryItem

interface StoreProductCategoryCreatePayload {
  name: string
}

type StoreProductCategoryCreateResponse = void

interface StoreProductCategoryUpdatePayload {
  name: string
}

type StoreProductCategoryUpdateResponse = void

type StoreProductCategoryDeleteResponse = void

export const useStoreProductCategoryApi = {
  /*
   * 取得指定門市的所有商品分類
  */
  getAll: (storeId: string): AxiosPromise<StoreProductCategoryGetAllResponse> => {
    return useApiRequest.get({
      url: '/product-category/',
      params: { storeId }
    })
  },

  /*
   * 查詢單一商品分類詳細資料
  */
  getById: (productCategoryId: string): AxiosPromise<StoreProductCategoryGetByIdResponse> => {
    return useApiRequest.get({
      url: `/product-category/${productCategoryId}`
    })
  },

  /*
   * 為指定門市新增商品分類
  */
  create: (storeId: string, payload: StoreProductCategoryCreatePayload): AxiosPromise<StoreProductCategoryCreateResponse> => {
    return useApiRequest.post({
      url: '/product-category/',
      params: { storeId },
      data: payload
    })
  },

  /*
   * 更新商品分類名稱
  */
  update: (productCategoryId: string, payload: StoreProductCategoryUpdatePayload): AxiosPromise<StoreProductCategoryUpdateResponse> => {
    return useApiRequest.patch({
      url: `/product-category/${productCategoryId}`,
      data: payload
    })
  },

  /*
   * 刪除商品分類
  */
  delete: (productCategoryId: string): AxiosPromise<StoreProductCategoryDeleteResponse> => {
    return useApiRequest.delete({
      url: `/product-category/${productCategoryId}`
    })
  }
}
