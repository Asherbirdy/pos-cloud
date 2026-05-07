<script setup lang="ts">
import { useMutation, useQuery, useQueryClient } from '@tanstack/vue-query'
import { NButton, NCard, NDataTable, NEl, NFlex, NForm, NFormItem, NH2, NInput, NLayout, NLayoutContent, NLayoutHeader, NModal, NSelect, NSpin, NTag, NText, NTooltip } from 'naive-ui'
import type { DataTableColumns, FormInst, FormRules } from 'naive-ui'
import { h } from 'vue'

import { useAdminApi } from '@/api/useAdminApi'
import type { AdminMemberItem, MemberRole } from '@/api/useAdminApi'

const message = useMessage()
const queryClient = useQueryClient()

const formRef = ref<FormInst | null>(null)

const memberQueryKey = ['admin', 'member', 'getAll']

const state = ref({
	data: {
		keyword: '',
		roleFilter: 'all' as 'all' | MemberRole,
		form: {
			name: '',
			email: '',
			password: '',
			role: 'user' as MemberRole
		}
	},
	feature: {
		showFormModal: false,
		rules: {
			name: [
				{ required: true, message: '請輸入姓名', trigger: 'blur' }
			],
			email: [
				{ required: true, message: '請輸入 Email', trigger: 'blur' },
				{ type: 'email', message: 'Email 格式錯誤', trigger: ['blur', 'input'] }
			],
			password: [
				{ required: true, message: '請輸入密碼', trigger: 'blur' },
				{ min: 6, max: 64, message: '密碼長度需介於 6–64 字元', trigger: 'blur' }
			]
		} as FormRules,
		roleOptions: [
			{ label: 'User', value: 'user' },
			{ label: 'Admin', value: 'admin' }
		],
		roleFilterOptions: [
			{ label: '全部角色', value: 'all' },
			{ label: 'Admin', value: 'admin' },
			{ label: 'User', value: 'user' }
		]
	}
})

const memberQuery = useQuery({
	queryKey: memberQueryKey,
	queryFn: async (): Promise<AdminMemberItem[]> => {
		const res = await useAdminApi.getAllMembers()
		return res.data.data ?? []
	}
})

const createMutation = useMutation({
	mutationFn: async (payload: { name: string, email: string, password: string, role: MemberRole }) => {
		await useAdminApi.createMember(payload)
	},
	onSuccess: () => {
		message.success('已新增會員')
		queryClient.invalidateQueries({ queryKey: memberQueryKey })
		state.value.feature.showFormModal = false
	},
	onError: () => {
		message.error('新增會員失敗')
	}
})

const memberList = computed<AdminMemberItem[]>(() => memberQuery.data.value ?? [])

const filteredList = computed(() => {
	const kw = state.value.data.keyword.trim().toLowerCase()
	const role = state.value.data.roleFilter
	return memberList.value.filter((item) => {
		const matchKw = !kw
			|| item.name.toLowerCase().includes(kw)
			|| item.email.toLowerCase().includes(kw)
			|| item.memberId.toLowerCase().includes(kw)
		const matchRole = role === 'all' || item.role === role
		return matchKw && matchRole
	})
})

const stats = computed(() => {
	const list = memberList.value
	return {
		total: list.length,
		admins: list.filter(m => m.role === 'admin').length,
		users: list.filter(m => m.role === 'user').length
	}
})

const openCreate = () => {
	state.value.data.form = { name: '', email: '', password: '', role: 'user' }
	state.value.feature.showFormModal = true
}

const handleSubmit = async () => {
	try {
		await formRef.value?.validate()
	}
	catch {
		return
	}

	createMutation.mutate({
		name: state.value.data.form.name,
		email: state.value.data.form.email,
		password: state.value.data.form.password,
		role: state.value.data.form.role
	})
}

const columns = computed<DataTableColumns<AdminMemberItem>>(() => [
	{
		title: 'Member ID',
		key: 'memberId',
		width: 240,
		render: row => h(NText, { code: true, depth: 3, style: 'font-size: 12px;' }, { default: () => row.memberId })
	},
	{
		title: '姓名',
		key: 'name',
		render: row => h(NText, { strong: true, style: 'color: #0f172a;' }, { default: () => row.name })
	},
	{
		title: 'Email',
		key: 'email',
		render: row => h(NText, { depth: 2 }, { default: () => row.email })
	},
	{
		title: '角色',
		key: 'role',
		width: 110,
		render: row => h(NTag, {
			type: row.role === 'admin' ? 'warning' : 'default',
			size: 'small',
			round: true,
			bordered: false
		}, { default: () => row.role === 'admin' ? 'Admin' : 'User' })
	},
	{
		title: '建立時間',
		key: 'createdAt',
		width: 180,
		render: row => h(NText, { depth: 3, style: 'font-size: 12px;' }, { default: () => row.createdAt })
	},
	{
		title: '更新時間',
		key: 'updatedAt',
		width: 180,
		render: row => h(NText, { depth: 3, style: 'font-size: 12px;' }, { default: () => row.updatedAt })
	}
])
</script>

<template>
  <n-layout style="min-height: 100vh; background: #ffffff;">
    <n-layout-header
      style="
        padding: 28px 40px 24px;
        background: #ffffff;
        border-bottom: 1px solid #eef2f7;
      "
      :bordered="false"
    >
      <n-flex align="center" justify="space-between">
        <n-flex align="center" :size="14">
          <n-el
            tag="div"
            style="
              width: 44px; height: 44px; border-radius: 12px;
              background: #f1f5ff;
              display: flex; align-items: center; justify-content: center;
              border: 1px solid #e0e7ff;
            "
          >
            <n-text strong style="color: #4f46e5; font-size: 13px; letter-spacing: 0.08em;">
              MBR
            </n-text>
          </n-el>
          <n-flex vertical :size="2">
            <n-h2 style="margin: 0; color: #0f172a; font-size: 20px; letter-spacing: -0.01em;">
              會員管理
            </n-h2>
            <n-text depth="3" style="font-size: 12px;">
              管理系統會員帳號與角色權限
            </n-text>
          </n-flex>
        </n-flex>

        <n-flex :size="10">
          <n-el
            tag="div"
            style="
              padding: 10px 18px; border-radius: 10px;
              background: #f8fafc; border: 1px solid #eef2f7;
              min-width: 110px;
            "
          >
            <n-flex vertical :size="2">
              <n-text depth="3" style="font-size: 11px; letter-spacing: 0.04em;">
                會員總數
              </n-text>
              <n-text strong style="color: #0f172a; font-size: 18px;">
                {{ stats.total }}
              </n-text>
            </n-flex>
          </n-el>
          <n-el
            tag="div"
            style="
              padding: 10px 18px; border-radius: 10px;
              background: #fff7ed; border: 1px solid #fed7aa;
              min-width: 110px;
            "
          >
            <n-flex vertical :size="2">
              <n-text style="font-size: 11px; color: #c2410c; letter-spacing: 0.04em;">
                Admin
              </n-text>
              <n-text strong style="color: #c2410c; font-size: 18px;">
                {{ stats.admins }}
              </n-text>
            </n-flex>
          </n-el>
          <n-el
            tag="div"
            style="
              padding: 10px 18px; border-radius: 10px;
              background: #f1f5ff; border: 1px solid #e0e7ff;
              min-width: 110px;
            "
          >
            <n-flex vertical :size="2">
              <n-text style="font-size: 11px; color: #6366f1; letter-spacing: 0.04em;">
                User
              </n-text>
              <n-text strong style="color: #4f46e5; font-size: 18px;">
                {{ stats.users }}
              </n-text>
            </n-flex>
          </n-el>
        </n-flex>
      </n-flex>
    </n-layout-header>

    <n-layout-content style="padding: 28px 40px 40px; background: #ffffff;">
      <n-card
        :bordered="false"
        style="
          border-radius: 16px;
          background: #ffffff;
          border: 1px solid #eef2f7;
          box-shadow: 0 1px 2px rgba(15, 23, 42, 0.03);
        "
        content-style="padding: 20px 24px 8px;"
      >
        <n-flex
          justify="space-between"
          align="center"
          style="margin-bottom: 18px;"
        >
          <n-flex align="center" :size="12">
            <n-input
              v-model:value="state.data.keyword"
              placeholder="搜尋姓名、Email 或 ID"
              clearable
              style="width: 320px;"
            />
            <n-select
              v-model:value="state.data.roleFilter"
              :options="state.feature.roleFilterOptions"
              style="width: 140px;"
            />
            <n-text depth="3" style="font-size: 13px;">
              共 <n-text strong style="color: #0f172a;">
                {{ filteredList.length }}
              </n-text> 位會員
            </n-text>
          </n-flex>
          <n-tooltip>
            <template #trigger>
              <n-button
                type="primary"
                strong
                @click="openCreate"
              >
                ＋ 新增會員
              </n-button>
            </template>
            建立一個新的系統會員帳號
          </n-tooltip>
        </n-flex>

        <n-spin :show="memberQuery.isPending.value">
          <n-data-table
            :columns="columns"
            :data="filteredList"
            :row-key="(row: AdminMemberItem) => row.memberId"
            :bordered="false"
            size="medium"
            :scroll-x="1100"
          />
        </n-spin>
      </n-card>
    </n-layout-content>

    <n-modal
      v-model:show="state.feature.showFormModal"
      preset="card"
      title="新增會員"
      style="width: 480px; border-radius: 14px;"
      :bordered="false"
    >
      <n-form
        ref="formRef"
        :model="state.data.form"
        :rules="state.feature.rules"
        label-placement="top"
      >
        <n-form-item label="姓名" path="name">
          <n-input v-model:value="state.data.form.name" placeholder="例如：王小明" />
        </n-form-item>
        <n-form-item label="Email" path="email">
          <n-input v-model:value="state.data.form.email" placeholder="user@example.com" />
        </n-form-item>
        <n-form-item label="密碼" path="password">
          <n-input
            v-model:value="state.data.form.password"
            type="password"
            show-password-on="click"
            placeholder="6–64 字元"
          />
        </n-form-item>
        <n-form-item label="角色">
          <n-select
            v-model:value="state.data.form.role"
            :options="state.feature.roleOptions"
          />
        </n-form-item>
      </n-form>
      <template #footer>
        <n-flex justify="end" :size="8">
          <n-button @click="state.feature.showFormModal = false">
            取消
          </n-button>
          <n-button
            type="primary"
            :loading="createMutation.isPending.value"
            @click="handleSubmit"
          >
            建立
          </n-button>
        </n-flex>
      </template>
    </n-modal>
  </n-layout>
</template>
