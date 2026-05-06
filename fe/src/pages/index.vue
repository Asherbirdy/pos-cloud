<route lang="yaml">
  name: 'Index'
  meta:
    layout: false
</route>

<script setup lang='ts'>
import { useMutation } from '@tanstack/vue-query'
import { NButton, NCard, NEl, NFlex, NForm, NFormItem, NH2, NInput, NText } from 'naive-ui'
import type { FormInst, FormRules } from 'naive-ui'

import { useAuthApi } from '@/api/useAuthApi'
import { CookieEnum } from '@/enum/CookieEnum'
import { setToken } from '@/utils/cookie'

const router = useRouter()
const message = useMessage()

const formRef = ref<FormInst | null>(null)

const state = ref({
  data: {
    email: '',
    password: ''
  },
  feature: {
    rules: {
      email: [
        { required: true, message: '請輸入 Email', trigger: 'blur' },
        { type: 'email', message: 'Email 格式錯誤', trigger: ['blur', 'input'] }
      ],
      password: [
        { required: true, message: '請輸入密碼', trigger: 'blur' }
      ]
    } as FormRules
  }
})

const loginMutation = useMutation({
  mutationFn: async () => {
    const res = await useAuthApi.login({
      email: state.value.data.email,
      password: state.value.data.password
    })
    return res.data
  },
  onSuccess: (data) => {
    setToken(CookieEnum.AccessToken, data.tokenPair.accessToken)
    setToken(CookieEnum.RefreshToken, data.tokenPair.refreshToken)
    message.success(`歡迎 ${data.name}`)
    router.push('/A/user')
  },
  onError: () => {
    message.error('登入失敗，請確認 Email 或密碼')
  }
})

const handleLogin = async () => {
  try {
    await formRef.value?.validate()
  }
  catch {
    return
  }
  loginMutation.mutate()
}
</script>

<template>
  <n-flex
    align="center"
    justify="center"
    style="
      min-height: 100vh;
      padding: 16px;
      background: linear-gradient(135deg, #0f172a 0%, #1e3a8a 50%, #2563eb 100%);
    "
  >
    <n-card
      :bordered="false"
      style="max-width: 420px; width: 100%; border-radius: 18px; box-shadow: 0 20px 50px rgba(0,0,0,0.3);"
    >
      <n-flex vertical align="center" :size="6" style="margin-bottom: 24px;">
        <n-el
          tag="div"
          style="
            width: 56px; height: 56px; border-radius: 14px;
            background: linear-gradient(135deg, #2563eb, #06b6d4);
            display: flex; align-items: center; justify-content: center;
            box-shadow: 0 6px 16px rgba(37,99,235,0.35);
          "
        >
          <n-text strong style="color: white; font-size: 18px; letter-spacing: 0.05em;">
            POS
          </n-text>
        </n-el>
        <n-h2 style="margin: 0; font-size: 22px; color: #1f2937;">
          會員登入
        </n-h2>
        <n-text depth="3" style="font-size: 13px;">
          請輸入您的帳號密碼以繼續
        </n-text>
      </n-flex>

      <n-form
        ref="formRef"
        :model="state.data"
        :rules="state.feature.rules"
        label-placement="top"
        @keyup.enter="handleLogin"
      >
        <n-form-item label="Email" path="email">
          <n-input
            v-model:value="state.data.email"
            placeholder="user@example.com"
            size="large"
          />
        </n-form-item>
        <n-form-item label="密碼" path="password">
          <n-input
            v-model:value="state.data.password"
            type="password"
            show-password-on="click"
            placeholder="請輸入密碼"
            size="large"
          />
        </n-form-item>

        <n-button
          type="primary"
          size="large"
          block
          strong
          :loading="loginMutation.isPending.value"
          style="height: 48px; font-size: 16px; background: linear-gradient(135deg, #2563eb, #06b6d4); margin-top: 8px;"
          @click="handleLogin"
        >
          登入
        </n-button>
      </n-form>
    </n-card>
  </n-flex>
</template>
