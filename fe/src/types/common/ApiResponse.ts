// 後端統一回傳格式 { msg, data }
export interface ApiResponse<T> {
  msg: string
  data: T
}
