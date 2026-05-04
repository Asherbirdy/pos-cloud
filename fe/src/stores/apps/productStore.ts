import { defineStore } from 'pinia'

export const useProductStore = defineStore('productStore', () => {
  // ** State Data
  const state = ref({
    products: []
  })
  // ** Actions

  // ** Getters
  const products = computed(() => state.value.products)

  return {
    state,
    products
  }
})