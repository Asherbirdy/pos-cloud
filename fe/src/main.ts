import { VueQueryPlugin } from '@tanstack/vue-query'
import { createPinia } from 'pinia'
import { createApp } from 'vue'

import App from './App.vue'
import 'uno.css'
import router from './router'
import { vueQueryPluginOptions } from './plugins/vueQuery'
import './assets/scss/global.scss'
import './assets/scss/transition.scss'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(VueQueryPlugin, vueQueryPluginOptions)

app.mount('#app')
