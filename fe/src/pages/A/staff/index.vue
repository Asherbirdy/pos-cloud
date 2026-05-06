<script setup lang="ts">
import { NButton, NCard, NForm, NFormItem, NInput } from 'naive-ui'
import type { FormInst, FormRules } from 'naive-ui'

import { useAuthApi } from '@/api/useAuthApi'

const router = useRouter()
const message = useMessage()

const formRef = ref<FormInst | null>(null)

const state = ref({
	data: {
		email: '',
		password: ''
	},
	feature: {
		loading: false,
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

const handleLogin = async () => {
	try {
		await formRef.value?.validate()
	}
	catch {
		return
	}

	state.value.feature.loading = true
	try {
		const { data } = await useAuthApi.login({
			email: state.value.data.email,
			password: state.value.data.password
		})
		message.success(`歡迎 ${data.name}`)
		router.push('/')
	}
	catch {
		message.error('登入失敗，請確認 Email 或密碼')
	}
	finally {
		state.value.feature.loading = false
	}
}
</script>

<template>
  <div
    min-h-screen
    flex
    items-center
    justify-center
    bg-gray-100
    p-4
  >
    <n-card style="max-width: 400px; width: 100%;" title="員工登入">
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
            placeholder="name@example.com"
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
          :loading="state.feature.loading"
          @click="handleLogin"
        >
          登入
        </n-button>
      </n-form>
    </n-card>
  </div>
</template>
