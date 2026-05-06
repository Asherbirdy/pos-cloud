<route lang="yaml">
meta:
  layout: default
</route>

<script setup lang="ts">
import { useMutation, useQuery } from '@tanstack/vue-query'
import { NAlert, NAvatar, NButton, NCard, NDescriptions, NDescriptionsItem, NFlex, NH2, NSpin, NTag, NText } from 'naive-ui'

import { useMemberApi } from '@/api/useMemberApi'
import { CookieEnum } from '@/enum/CookieEnum'
import { removeToken } from '@/utils/cookie'

const router = useRouter()
const message = useMessage()

const state = ref({
  data: {},
  feature: {}
})

const memberQuery = useQuery({
  queryKey: ['member', 'showMe'],
  queryFn: async () => {
    const res = await useMemberApi.showMe()
    return res.data.data
  }
})

const logoutMutation = useMutation({
  mutationFn: async () => {
    await useMemberApi.logout()
  },
  onSettled: () => {
    removeToken(CookieEnum.AccessToken)
    removeToken(CookieEnum.RefreshToken)
    message.success('已登出')
    router.push('/')
  }
})

const handleLogout = () => {
  logoutMutation.mutate()
}

const initial = computed(() => memberQuery.data.value?.name?.charAt(0).toUpperCase() ?? '?')
</script>

<template>
  <n-flex
    justify="center"
    style="min-height: 100vh; padding: 32px 16px; background: #f1f5f9;"
  >
    <n-card
      :bordered="false"
      style="max-width: 640px; width: 100%; height: fit-content; border-radius: 16px; box-shadow: 0 12px 32px rgba(15,23,42,0.08);"
    >
      <n-flex align="center" justify="space-between" style="margin-bottom: 16px;">
        <n-h2 style="margin: 0;">
          個人資料
        </n-h2>
        <n-button
          quaternary
          type="error"
          :loading="logoutMutation.isPending.value"
          @click="handleLogout"
        >
          登出
        </n-button>
      </n-flex>

      <n-spin :show="memberQuery.isPending.value">
        <n-alert
          v-if="memberQuery.isError.value"
          type="error"
          title="載入失敗"
        >
          無法取得會員資料，請重新登入。
        </n-alert>

        <n-flex
          v-else-if="memberQuery.data.value"
          vertical
          :size="20"
        >
          <n-flex align="center" :size="16">
            <n-avatar
              round
              :size="64"
              style="background: linear-gradient(135deg, #2563eb, #06b6d4); color: white; font-size: 24px;"
            >
              {{ initial }}
            </n-avatar>
            <n-flex vertical :size="4">
              <n-text strong style="font-size: 18px;">
                {{ memberQuery.data.value.name }}
              </n-text>
              <n-text depth="3" style="font-size: 13px;">
                {{ memberQuery.data.value.email }}
              </n-text>
            </n-flex>
          </n-flex>

          <n-descriptions
            label-placement="left"
            bordered
            :column="1"
          >
            <n-descriptions-item label="會員 ID">
              <n-text code>{{ memberQuery.data.value.memberId }}</n-text>
            </n-descriptions-item>
            <n-descriptions-item label="姓名">
              <n-text strong>{{ memberQuery.data.value.name }}</n-text>
            </n-descriptions-item>
            <n-descriptions-item label="Email">
              {{ memberQuery.data.value.email }}
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
        </n-flex>
      </n-spin>
    </n-card>
  </n-flex>
</template>
