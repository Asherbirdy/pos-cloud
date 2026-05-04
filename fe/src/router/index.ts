// @ts-ignore
import { setupLayouts } from 'virtual:generated-layouts'
import generatedRoutes from 'virtual:generated-pages'
import { createRouter, createWebHistory } from 'vue-router'

import config from '@/config'
const routes = setupLayouts(generatedRoutes)
const router = createRouter({
  history: createWebHistory(config.baseUrl),
  routes
})

export default router
