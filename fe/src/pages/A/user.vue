<script setup lang="ts">
import { useMutation, useQuery } from '@tanstack/vue-query'
import { NAlert, NAvatar, NButton, NDivider, NFlex, NH2, NSpin, NTag, NText } from 'naive-ui'

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

const fields = computed(() => {
  const m = memberQuery.data.value
  if (!m) return []
  return [
    { label: '會員 ID', value: m.memberId, mono: true },
    { label: '姓名', value: m.name },
    { label: 'Email', value: m.email },
    { label: '角色', value: m.role, isRole: true },
    { label: '建立時間', value: m.createdAt },
    { label: '更新時間', value: m.updatedAt }
  ]
})
</script>

<template>
  <n-flex
    vertical
    :size="0"
    style="min-height: 100vh; background: #ffffff; padding: 48px 24px;"
  >
    <n-flex
      vertical
      :size="32"
      style=" width: 100%; margin: 0 auto;"
    >
      <n-flex align="center" justify="space-between">
        <n-h2 style="margin: 0; font-weight: 600;">
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
          :size="32"
        >
          <n-flex align="center" :size="20">
            <n-avatar
              round
              :size="72"
              style="background: linear-gradient(135deg, #2563eb, #06b6d4); color: white; font-size: 26px; font-weight: 600;"
            >
              {{ initial }}
            </n-avatar>
            <n-flex vertical :size="6">
              <n-text strong style="font-size: 20px;">
                {{ memberQuery.data.value.name }}
              </n-text>
              <n-text depth="3" style="font-size: 14px;">
                {{ memberQuery.data.value.email }}
              </n-text>
            </n-flex>
          </n-flex>

          <n-flex vertical :size="0">
            <template v-for="(f, idx) in fields" :key="f.label">
              <n-divider v-if="idx === 0" style="margin: 0;" />
              <n-flex
                align="center"
                justify="space-between"
                :size="16"
                style="padding: 16px 4px;"
              >
                <n-text depth="3" style="font-size: 14px; min-width: 96px;">
                  {{ f.label }}
                </n-text>
                <n-tag
                  v-if="f.isRole"
                  round
                  :type="f.value === 'admin' ? 'warning' : 'default'"
                >
                  {{ f.value }}
                </n-tag>
                <n-text
                  v-else
                  :code="f.mono"
                  style="font-size: 14px; text-align: right;"
                >
                  {{ f.value }}
                </n-text>
              </n-flex>
              <n-divider style="margin: 0;" />
            </template>
          </n-flex>
        </n-flex>
      </n-spin>
    </n-flex>
  </n-flex>
</template>
