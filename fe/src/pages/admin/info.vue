<route lang="yaml">
meta:
  layout: false
</route>

<script setup lang="ts">
import { useQuery } from '@tanstack/vue-query'
import { NAlert, NCard, NDescriptions, NDescriptionsItem, NFlex, NH2, NSpin, NTag, NText } from 'naive-ui'

import { useMemberApi } from '@/api/useMemberApi'

const memberQuery = useQuery({
	queryKey: ['member', 'showMe'],
	queryFn: async () => {
		const { data } = await useMemberApi.showMe()
		return data
	}
})
</script>

<template>
  <n-flex
    justify="center"
    style="min-height: 100vh; padding: 32px 16px; background: #f1f5f9;"
  >
    <n-card
      :bordered="false"
      style="max-width: 640px; width: 100%; border-radius: 16px; box-shadow: 0 12px 32px rgba(15,23,42,0.08);"
    >
      <n-h2 style="margin: 0 0 16px;">
        個人資料
      </n-h2>

      <n-spin :show="memberQuery.isPending.value">
        <n-alert
          v-if="memberQuery.isError.value"
          type="error"
          title="載入失敗"
        >
          無法取得會員資料，請重新登入。
        </n-alert>

        <n-descriptions
          v-else-if="memberQuery.data.value"
          label-placement="left"
          bordered
          :column="1"
        >
          <n-descriptions-item label="姓名">
            <n-text strong>{{ memberQuery.data.value.name }}</n-text>
          </n-descriptions-item>
          <n-descriptions-item label="Email">
            {{ memberQuery.data.value.email }}
          </n-descriptions-item>
          <n-descriptions-item label="會員 ID">
            <n-text code>{{ memberQuery.data.value.memberId }}</n-text>
          </n-descriptions-item>
          <n-descriptions-item label="角色">
            <n-tag round :type="memberQuery.data.value.role === 'admin' ? 'warning' : 'default'">
              {{ memberQuery.data.value.role }}
            </n-tag>
          </n-descriptions-item>
          <n-descriptions-item label="建立時間">
            {{ memberQuery.data.value.createdAt }}
          </n-descriptions-item>
          <n-descriptions-item label="更新時間">
            {{ memberQuery.data.value.updatedAt }}
          </n-descriptions-item>
        </n-descriptions>
      </n-spin>
    </n-card>
  </n-flex>
</template>
