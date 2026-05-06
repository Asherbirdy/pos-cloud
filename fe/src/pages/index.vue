<route lang="yaml">
  name: 'Index'
  meta:
    layout: false
</route>

<script setup lang='ts'>
import { NButton, NCard, NSpin, NText } from 'naive-ui'
import { useDevApi } from '@/api'

const state = ref({
  data: {
    result: null as { status: string, service: string, version: string } | null
  },
  feature: {
    loading: false,
    error: ''
  }
})

const fetchTest = async () => {
  state.value.feature.loading = true
  state.value.feature.error = ''
  try {
    const { data } = await useDevApi.test()
    state.value.data.result = data
  } catch (e: any) {
    state.value.feature.error = e?.message ?? 'Request failed'
  } finally {
    state.value.feature.loading = false
  }
}

onMounted(() => {
  fetchTest()
})
</script>

<template>
  <n-card title="Dev Test">
    <n-spin :show="state.feature.loading">
      <n-flex vertical>
        <n-button type="primary" :loading="state.feature.loading" @click="fetchTest">
          重新呼叫 useDevApi.test
        </n-button>
        <n-text v-if="state.feature.error" type="error">
          {{ state.feature.error }}
        </n-text>
        <n-card v-if="state.data.result" size="small">
          <n-flex vertical>
            <n-text>status: {{ state.data.result.status }}</n-text>
            <n-text>service: {{ state.data.result.service }}</n-text>
            <n-text>version: {{ state.data.result.version }}</n-text>
          </n-flex>
        </n-card>
      </n-flex>
    </n-spin>
  </n-card>
</template>
