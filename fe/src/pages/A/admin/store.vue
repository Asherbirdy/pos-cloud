<script setup lang="ts">
import { useMutation, useQuery, useQueryClient } from '@tanstack/vue-query'
import { NButton, NCard, NDataTable, NEl, NEmpty, NFlex, NForm, NFormItem, NH2, NInput, NInputNumber, NLayout, NLayoutContent, NLayoutHeader, NModal, NSelect, NSpace, NSpin, NSwitch, NTag, NText, NTooltip } from 'naive-ui'
import type { DataTableColumns, FormInst, FormRules } from 'naive-ui'
import { h } from 'vue'

import { useAdminApi } from '@/api/useAdminApi'
import { useMemberStoreAccessApi } from '@/api/useMemberStoreAccessApi'
import type { MemberStoreAccessItem, StoreRole } from '@/api/useMemberStoreAccessApi'
import { useStoreApi } from '@/api/useStoreApi'
import type { StoreItem } from '@/api/useStoreApi'

const message = useMessage()
const queryClient = useQueryClient()

const formRef = ref<FormInst | null>(null)

const storeQueryKey = ['store', 'getAll']

const state = ref({
	data: {
		keyword: '',
		form: {
			store_id: '',
			name: '',
			active: true,
			running_devices_limit: 2
		},
		currentStoreId: '',
		currentStoreName: '',
		addAccessForm: {
			memberId: '',
			role: 'STORE_STAFF' as StoreRole
		}
	},
	feature: {
		mode: 'create' as 'create' | 'edit',
		showFormModal: false,
		showAccessModal: false,
		showAddAccessModal: false,
		rules: {
			name: [
				{ required: true, message: '請輸入門市名稱', trigger: 'blur' }
			]
		} as FormRules,
		roleOptions: [
			{ label: '店長 (STORE_MANAGER)', value: 'STORE_MANAGER' },
			{ label: '店員 (STORE_STAFF)', value: 'STORE_STAFF' }
		]
	}
})

const storeQuery = useQuery({
	queryKey: storeQueryKey,
	queryFn: async (): Promise<StoreItem[]> => {
		const res = await useStoreApi.getAll()
		return res.data.data ?? []
	}
})

const createMutation = useMutation({
	mutationFn: async (payload: { name: string }) => {
		await useStoreApi.create(payload)
	},
	onSuccess: () => {
		message.success('已新增門市')
		queryClient.invalidateQueries({ queryKey: storeQueryKey })
		state.value.feature.showFormModal = false
	},
	onError: () => {
		message.error('新增門市失敗')
	}
})

const accessQuery = useQuery({
	queryKey: computed(() => ['memberStoreAccess', state.value.data.currentStoreId]),
	queryFn: async (): Promise<MemberStoreAccessItem[]> => {
		const res = await useMemberStoreAccessApi.getByStoreId(state.value.data.currentStoreId)
		return res.data.data ?? []
	},
	enabled: computed(() => state.value.feature.showAccessModal && !!state.value.data.currentStoreId)
})

const allMembersQuery = useQuery({
	queryKey: ['admin', 'member', 'getAll'],
	queryFn: async () => {
		const res = await useAdminApi.getAllMembers()
		return res.data.data ?? []
	},
	enabled: computed(() => state.value.feature.showAddAccessModal)
})

const memberOptions = computed(() => {
	const list = allMembersQuery.data.value ?? []
	const assignedIds = new Set((accessQuery.data.value ?? []).map(a => a.memberId))
	return list
		.filter(m => !assignedIds.has(m.memberId))
		.map(m => ({
			label: `${m.name} · ${m.email}`,
			value: m.memberId
		}))
})

const createAccessMutation = useMutation({
	mutationFn: async (payload: { memberId: string, storeId: string, role: StoreRole }) => {
		await useMemberStoreAccessApi.create(payload)
	},
	onSuccess: () => {
		message.success('已加入人員')
		queryClient.invalidateQueries({ queryKey: ['memberStoreAccess', state.value.data.currentStoreId] })
		state.value.feature.showAddAccessModal = false
	},
	onError: () => {
		message.error('加入人員失敗')
	}
})

const editMutation = useMutation({
	mutationFn: async (payload: { storeId: string, name: string, isActive: boolean, running_devices_limit: number }) => {
		await useStoreApi.edit(payload.storeId, {
			name: payload.name,
			isActive: payload.isActive,
			running_devices_limit: payload.running_devices_limit
		})
	},
	onSuccess: () => {
		message.success('已更新門市')
		queryClient.invalidateQueries({ queryKey: storeQueryKey })
		state.value.feature.showFormModal = false
	},
	onError: () => {
		message.error('更新門市失敗')
	}
})

const storeList = computed<StoreItem[]>(() => storeQuery.data.value ?? [])

const filteredList = computed(() => {
	const kw = state.value.data.keyword.trim().toLowerCase()
	if (!kw) return storeList.value
	return storeList.value.filter(item =>
		item.name.toLowerCase().includes(kw)
		|| item.store_id.toLowerCase().includes(kw)
	)
})

const stats = computed(() => {
	const list = storeList.value
	return {
		total: list.length,
		active: list.filter(s => s.active).length,
		inactive: list.filter(s => !s.active).length
	}
})

const openCreate = () => {
	state.value.feature.mode = 'create'
	state.value.data.form = { store_id: '', name: '', active: true, running_devices_limit: 2 }
	state.value.feature.showFormModal = true
}

const openEdit = (row: StoreItem) => {
	state.value.feature.mode = 'edit'
	state.value.data.form = {
		store_id: row.store_id,
		name: row.name,
		active: row.active,
		running_devices_limit: row.running_devices_limit
	}
	state.value.feature.showFormModal = true
}

const openAccess = (row: StoreItem) => {
	state.value.data.currentStoreId = row.store_id
	state.value.data.currentStoreName = row.name
	state.value.feature.showAccessModal = true
}

const openAddAccess = () => {
	state.value.data.addAccessForm = { memberId: '', role: 'STORE_STAFF' }
	state.value.feature.showAddAccessModal = true
}

const handleSubmitAddAccess = () => {
	if (!state.value.data.addAccessForm.memberId) {
		message.error('請選擇人員')
		return
	}
	createAccessMutation.mutate({
		memberId: state.value.data.addAccessForm.memberId,
		storeId: state.value.data.currentStoreId,
		role: state.value.data.addAccessForm.role
	})
}

const accessColumns = computed<DataTableColumns<MemberStoreAccessItem>>(() => [
	{ title: '姓名', key: 'memberName', render: row => h(NText, { strong: true, style: 'color: #0f172a;' }, { default: () => row.memberName ?? '—' }) },
	{ title: 'Email', key: 'memberEmail', render: row => h(NText, { depth: 2, style: 'font-size: 13px;' }, { default: () => row.memberEmail ?? '—' }) },
	{
		title: '角色',
		key: 'role',
		width: 130,
		render: row => h(NTag, {
			type: row.role === 'STORE_MANAGER' ? 'info' : 'default',
			size: 'small',
			round: true,
			bordered: false
		}, { default: () => row.role === 'STORE_MANAGER' ? '店長' : '店員' })
	},
	{
		title: '狀態',
		key: 'isActive',
		width: 100,
		render: row => h(NTag, {
			type: row.isActive ? 'success' : 'default',
			size: 'small',
			round: true,
			bordered: false
		}, { default: () => row.isActive ? '啟用' : '停用' })
	}
])

const handleSubmit = async () => {
	try {
		await formRef.value?.validate()
	}
	catch {
		return
	}

	if (state.value.feature.mode === 'create') {
		createMutation.mutate({ name: state.value.data.form.name })
	}
	else {
		editMutation.mutate({
			storeId: state.value.data.form.store_id,
			name: state.value.data.form.name,
			isActive: state.value.data.form.active,
			running_devices_limit: state.value.data.form.running_devices_limit
		})
	}
}

const columns = computed<DataTableColumns<StoreItem>>(() => [
	{
		title: 'Store ID',
		key: 'store_id',
		width: 240,
		render: row => h(NText, { code: true, depth: 3, style: 'font-size: 12px;' }, { default: () => row.store_id })
	},
	{
		title: '門市名稱',
		key: 'name',
		render: row => h(NText, { strong: true, style: 'color: #0f172a;' }, { default: () => row.name })
	},
	{
		title: '狀態',
		key: 'active',
		width: 110,
		render: row => h(NTag, {
			type: row.active ? 'success' : 'default',
			size: 'small',
			round: true,
			bordered: false
		}, { default: () => row.active ? '營運中' : '停用' })
	},
	{
		title: '裝置上限',
		key: 'running_devices_limit',
		width: 110,
		align: 'center'
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
	},
	{
		title: '操作',
		key: 'actions',
		width: 180,
		fixed: 'right',
		render: row => h(NSpace, { size: 'small' }, {
			default: () => [
				h(NButton, { size: 'small', quaternary: true, type: 'primary', onClick: () => openEdit(row) }, { default: () => '編輯' }),
				h(NButton, { size: 'small', quaternary: true, onClick: () => openAccess(row) }, { default: () => '成員' })
			]
		})
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
              STR
            </n-text>
          </n-el>
          <n-flex vertical :size="2">
            <n-h2 style="margin: 0; color: #0f172a; font-size: 20px; letter-spacing: -0.01em;">
              門市管理
            </n-h2>
            <n-text depth="3" style="font-size: 12px;">
              管理所有門市資訊與營運狀態
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
                門市總數
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
              background: #ecfdf5; border: 1px solid #a7f3d0;
              min-width: 110px;
            "
          >
            <n-flex vertical :size="2">
              <n-text style="font-size: 11px; color: #059669; letter-spacing: 0.04em;">
                營運中
              </n-text>
              <n-text strong style="color: #047857; font-size: 18px;">
                {{ stats.active }}
              </n-text>
            </n-flex>
          </n-el>
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
                停用
              </n-text>
              <n-text strong style="color: #64748b; font-size: 18px;">
                {{ stats.inactive }}
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
              placeholder="搜尋門市名稱或 ID"
              clearable
              style="width: 320px;"
            />
            <n-text depth="3" style="font-size: 13px;">
              共 <n-text strong style="color: #0f172a;">
                {{ filteredList.length }}
              </n-text> 間門市
            </n-text>
          </n-flex>
          <n-tooltip>
            <template #trigger>
              <n-button
                type="primary"
                strong
                @click="openCreate"
              >
                ＋ 新增門市
              </n-button>
            </template>
            建立一個新門市
          </n-tooltip>
        </n-flex>

        <n-spin :show="storeQuery.isPending.value">
          <n-data-table
            :columns="columns"
            :data="filteredList"
            :row-key="(row: StoreItem) => row.store_id"
            :bordered="false"
            size="medium"
            :scroll-x="1200"
          />
        </n-spin>
      </n-card>
    </n-layout-content>

    <n-modal
      v-model:show="state.feature.showFormModal"
      preset="card"
      :title="state.feature.mode === 'create' ? '新增門市' : '編輯門市'"
      style="width: 480px; border-radius: 14px;"
      :bordered="false"
    >
      <n-form
        ref="formRef"
        :model="state.data.form"
        :rules="state.feature.rules"
        label-placement="top"
      >
        <n-form-item v-if="state.feature.mode === 'edit'" label="Store ID">
          <n-input :value="state.data.form.store_id" disabled />
        </n-form-item>
        <n-form-item label="門市名稱" path="name">
          <n-input v-model:value="state.data.form.name" placeholder="例如：信義旗艦店" />
        </n-form-item>
        <template v-if="state.feature.mode === 'edit'">
          <n-form-item label="同時運行裝置數上限">
            <n-input-number
              v-model:value="state.data.form.running_devices_limit"
              :min="1"
              :max="10"
              style="width: 100%;"
            />
          </n-form-item>
          <n-form-item label="營運狀態">
            <n-switch v-model:value="state.data.form.active">
              <template #checked>
                營運中
              </template>
              <template #unchecked>
                停用
              </template>
            </n-switch>
          </n-form-item>
        </template>
      </n-form>
      <template #footer>
        <n-flex justify="end" :size="8">
          <n-button @click="state.feature.showFormModal = false">
            取消
          </n-button>
          <n-button
            type="primary"
            :loading="createMutation.isPending.value || editMutation.isPending.value"
            @click="handleSubmit"
          >
            {{ state.feature.mode === 'create' ? '建立' : '儲存' }}
          </n-button>
        </n-flex>
      </template>
    </n-modal>

    <n-modal
      v-model:show="state.feature.showAccessModal"
      preset="card"
      :title="`門市成員 · ${state.data.currentStoreName}`"
      style="width: 720px; border-radius: 14px;"
      :bordered="false"
    >
      <n-spin :show="accessQuery.isFetching.value">
        <n-flex vertical :size="14">
          <n-flex align="center" justify="space-between">
            <n-text depth="3" style="font-size: 12px;">
              共 <n-text strong style="color: #0f172a;">{{ accessQuery.data.value?.length ?? 0 }}</n-text> 位成員
            </n-text>
            <n-button
              type="primary"
              ghost
              size="small"
              @click="openAddAccess"
            >
              ＋ 加人員
            </n-button>
          </n-flex>
          <n-data-table
            v-if="(accessQuery.data.value?.length ?? 0) > 0"
            :columns="accessColumns"
            :data="accessQuery.data.value ?? []"
            :row-key="(row: MemberStoreAccessItem) => row.memberStoreAccessId"
            :bordered="false"
            size="small"
          />
          <n-empty
            v-else-if="!accessQuery.isPending.value"
            description="此門市尚未指派任何成員"
            size="small"
            style="padding: 24px 0;"
          />
        </n-flex>
      </n-spin>
    </n-modal>

    <n-modal
      v-model:show="state.feature.showAddAccessModal"
      preset="card"
      :title="`加人員 · ${state.data.currentStoreName}`"
      style="width: 480px; border-radius: 14px;"
      :bordered="false"
    >
      <n-form label-placement="top">
        <n-form-item label="選擇人員">
          <n-select
            v-model:value="state.data.addAccessForm.memberId"
            :options="memberOptions"
            :loading="allMembersQuery.isPending.value"
            filterable
            placeholder="搜尋姓名或 Email"
          />
        </n-form-item>
        <n-form-item label="角色">
          <n-select
            v-model:value="state.data.addAccessForm.role"
            :options="state.feature.roleOptions"
          />
        </n-form-item>
      </n-form>
      <template #footer>
        <n-flex justify="end" :size="8">
          <n-button @click="state.feature.showAddAccessModal = false">
            取消
          </n-button>
          <n-button
            type="primary"
            :loading="createAccessMutation.isPending.value"
            @click="handleSubmitAddAccess"
          >
            加入
          </n-button>
        </n-flex>
      </template>
    </n-modal>
  </n-layout>
</template>
