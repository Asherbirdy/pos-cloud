<script setup lang="ts">
import { useMutation, useQuery, useQueryClient } from '@tanstack/vue-query'
import { NBadge, NButton, NCard, NDataTable, NEl, NEmpty, NFlex, NForm, NFormItem, NH2, NInput, NInputNumber, NLayout, NLayoutContent, NLayoutHeader, NModal, NSpace, NSpin, NSwitch, NTag, NText, NTooltip } from 'naive-ui'
import type { DataTableColumns, FormInst, FormRules } from 'naive-ui'
import { h } from 'vue'

import { useEnterpriseApi } from '@/api/useEnterpriseApi'
import { useMemberStoreAccessApi } from '@/api/useMemberStoreAccessApi'
import type { MemberStoreAccessItem } from '@/api/useMemberStoreAccessApi'
import { useStoreApi } from '@/api/useStoreApi'

interface StoreRow {
	store_id: string
	name: string
	active: boolean
	running_devices_limit: number
	createdAt: string
}

interface EnterpriseRow {
	enterprise_id: string
	name: string
	stores: StoreRow[]
	createdAt: string
	updatedAt: string
}

const message = useMessage()
const queryClient = useQueryClient()

const formRef = ref<FormInst | null>(null)

const enterpriseQueryKey = ['enterprise', 'getAll']

const state = ref({
	data: {
		keyword: '',
		form: {
			enterprise_id: '',
			name: ''
		},
		storeForm: {
			store_id: '',
			name: '',
			active: true,
			running_devices_limit: 2
		},
		currentEnterpriseId: '',
		currentStoreId: '',
		currentStoreName: ''
	},
	feature: {
		mode: 'create' as 'create' | 'edit',
		showFormModal: false,
		storeMode: 'create' as 'create' | 'edit',
		showStoreModal: false,
		showAccessModal: false,
		expandedKeys: [] as string[],
		rules: {
			name: [
				{ required: true, message: '請輸入企業名稱', trigger: 'blur' }
			]
		} as FormRules,
		storeRules: {
			name: [
				{ required: true, message: '請輸入門市名稱', trigger: 'blur' }
			]
		} as FormRules
	}
})

const enterpriseQuery = useQuery({
	queryKey: enterpriseQueryKey,
	queryFn: async (): Promise<EnterpriseRow[]> => {
		const res = await useEnterpriseApi.getAll()
		const payload = res.data.data ?? []
		return payload.map(item => ({
			enterprise_id: item.enterprise_id,
			name: item.name,
			createdAt: item.createdAt,
			updatedAt: item.updatedAt,
			stores: (item.stores ?? []).map(s => ({
				store_id: s.store_id,
				name: s.name,
				active: s.active,
				running_devices_limit: s.running_devices_limit,
				createdAt: s.createdAt
			}))
		}))
	}
})

const createMutation = useMutation({
	mutationFn: async (payload: { name: string }) => {
		await useEnterpriseApi.create(payload)
	},
	onSuccess: () => {
		message.success('已新增企業')
		queryClient.invalidateQueries({ queryKey: enterpriseQueryKey })
		state.value.feature.showFormModal = false
	},
	onError: () => {
		message.error('新增企業失敗')
	}
})

const editMutation = useMutation({
	mutationFn: async (payload: { enterpriseId: string, name: string }) => {
		await useEnterpriseApi.edit(payload.enterpriseId, { name: payload.name })
	},
	onSuccess: () => {
		message.success('已更新企業')
		queryClient.invalidateQueries({ queryKey: enterpriseQueryKey })
		state.value.feature.showFormModal = false
	},
	onError: () => {
		message.error('更新企業失敗')
	}
})

const createStoreMutation = useMutation({
	mutationFn: async (payload: { enterpriseId: string, name: string }) => {
		await useStoreApi.create(payload.enterpriseId, { name: payload.name })
	},
	onSuccess: () => {
		message.success('已新增門市')
		queryClient.invalidateQueries({ queryKey: enterpriseQueryKey })
		state.value.feature.showStoreModal = false
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

const accessColumns = computed<DataTableColumns<MemberStoreAccessItem>>(() => [
	{ title: 'Member ID', key: 'memberId', width: 200, render: row => h(NText, { code: true, depth: 3, style: 'font-size: 12px;' }, { default: () => row.memberId }) },
	{
		title: '角色',
		key: 'role',
		width: 140,
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
	},
	{
		title: '建立時間',
		key: 'createdAt',
		render: row => h(NText, { depth: 3, style: 'font-size: 12px;' }, { default: () => row.createdAt })
	}
])

const editStoreMutation = useMutation({
	mutationFn: async (payload: { enterpriseId: string, storeId: string, name: string, isActive: boolean, running_devices_limit: number }) => {
		await useStoreApi.edit(payload.enterpriseId, payload.storeId, {
			name: payload.name,
			isActive: payload.isActive,
			running_devices_limit: payload.running_devices_limit
		})
	},
	onSuccess: () => {
		message.success('已更新門市')
		queryClient.invalidateQueries({ queryKey: enterpriseQueryKey })
		state.value.feature.showStoreModal = false
	},
	onError: () => {
		message.error('更新門市失敗')
	}
})



const enterpriseList = computed<EnterpriseRow[]>(() => enterpriseQuery.data.value ?? [])

const filteredList = computed(() => {
	const kw = state.value.data.keyword.trim().toLowerCase()
	if (!kw) return enterpriseList.value
	return enterpriseList.value.filter(item =>
		item.name.toLowerCase().includes(kw)
		|| item.enterprise_id.toLowerCase().includes(kw)
	)
})

const stats = computed(() => {
	const list = enterpriseList.value
	const totalStores = list.reduce((acc, e) => acc + e.stores.length, 0)
	const activeStores = list.reduce((acc, e) => acc + e.stores.filter(s => s.active).length, 0)
	return {
		enterprises: list.length,
		stores: totalStores,
		active: activeStores
	}
})

const openCreate = () => {
	state.value.feature.mode = 'create'
	state.value.data.form = { enterprise_id: '', name: '' }
	state.value.feature.showFormModal = true
}

const openEdit = (row: EnterpriseRow) => {
	state.value.feature.mode = 'edit'
	state.value.data.form = {
		enterprise_id: row.enterprise_id,
		name: row.name
	}
	state.value.feature.showFormModal = true
}

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
			enterpriseId: state.value.data.form.enterprise_id,
			name: state.value.data.form.name
		})
	}
}

const openAccess = (store: StoreRow) => {
	state.value.data.currentStoreId = store.store_id
	state.value.data.currentStoreName = store.name
	state.value.feature.showAccessModal = true
}

const openCreateStore = (row: EnterpriseRow) => {
	state.value.data.currentEnterpriseId = row.enterprise_id
	state.value.feature.storeMode = 'create'
	state.value.data.storeForm = { store_id: '', name: '', active: true, running_devices_limit: 2 }
	state.value.feature.showStoreModal = true
}

const openEditStore = (enterpriseId: string, store: StoreRow) => {
	state.value.data.currentEnterpriseId = enterpriseId
	state.value.feature.storeMode = 'edit'
	state.value.data.storeForm = { ...store }
	state.value.feature.showStoreModal = true
}

const handleSubmitStore = () => {
	if (!state.value.data.storeForm.name.trim()) {
		message.error('請輸入門市名稱')
		return
	}
	if (state.value.feature.storeMode === 'create') {
		createStoreMutation.mutate({
			enterpriseId: state.value.data.currentEnterpriseId,
			name: state.value.data.storeForm.name
		})
	}
	else {
		editStoreMutation.mutate({
			enterpriseId: state.value.data.currentEnterpriseId,
			storeId: state.value.data.storeForm.store_id,
			name: state.value.data.storeForm.name,
			isActive: state.value.data.storeForm.active,
			running_devices_limit: state.value.data.storeForm.running_devices_limit
		})
	}
}

const columns = computed<DataTableColumns<EnterpriseRow>>(() => [
	{
		type: 'expand',
		expandable: () => true,
		renderExpand: row => h(NEl, { tag: 'div', style: 'padding: 0;' }, {
			default: () => h(StoreSubTable, {
				enterprise: row,
				onCreate: () => openCreateStore(row),
				onEdit: (store: StoreRow) => openEditStore(row.enterprise_id, store),
				onAccess: (store: StoreRow) => openAccess(store)
			})
		})
	},
	{
		title: 'Enterprise ID',
		key: 'enterprise_id',
		width: 140,
		render: row => h(NText, { code: true, depth: 3, style: 'font-size: 12px;' }, { default: () => row.enterprise_id })
	},
	{
		title: '企業名稱',
		key: 'name',
		render: row => h(NText, { strong: true, style: 'color: #0f172a;' }, { default: () => row.name })
	},
	{
		title: '門市數量',
		key: 'stores',
		width: 110,
		align: 'center',
		render: row => h(NBadge, {
			value: row.stores.length,
			showZero: true,
			color: row.stores.length === 0 ? '#cbd5e1' : '#6366f1',
			style: 'margin-right: 4px;'
		})
	},
	{
		title: '建立時間',
		key: 'createdAt',
		width: 160,
		render: row => h(NText, { depth: 3, style: 'font-size: 12px;' }, { default: () => row.createdAt })
	},
	{
		title: '更新時間',
		key: 'updatedAt',
		width: 160,
		render: row => h(NText, { depth: 3, style: 'font-size: 12px;' }, { default: () => row.updatedAt })
	},
	{
		title: '操作',
		key: 'actions',
		width: 220,
		fixed: 'right',
		render: row => h(NSpace, { size: 'small' }, {
			default: () => [
				h(NButton, { size: 'small', quaternary: true, type: 'primary', onClick: () => openEdit(row) }, { default: () => '編輯' }),
				h(NButton, { size: 'small', quaternary: true, onClick: () => openCreateStore(row) }, { default: () => '＋門市' })
			]
		})
	}
])

const StoreSubTable = defineComponent({
	props: {
		enterprise: { type: Object as PropType<EnterpriseRow>, required: true }
	},
	emits: ['create', 'edit', 'access'],
	setup: (props, { emit }) => {
		const subColumns = computed<DataTableColumns<StoreRow>>(() => [
			{ title: 'Store ID', key: 'store_id', width: 140, render: row => h(NText, { code: true, depth: 3, style: 'font-size: 12px;' }, { default: () => row.store_id }) },
			{ title: '門市名稱', key: 'name', render: row => h(NText, { style: 'color: #0f172a;' }, { default: () => row.name }) },
			{
				title: '狀態',
				key: 'active',
				width: 100,
				render: row => h(NTag, {
					type: row.active ? 'success' : 'default',
					size: 'small',
					round: true,
					bordered: false
				}, { default: () => row.active ? '營運中' : '停用' })
			},
			{ title: '裝置上限', key: 'running_devices_limit', width: 110, align: 'center' },
			{ title: '建立時間', key: 'createdAt', width: 160, render: row => h(NText, { depth: 3, style: 'font-size: 12px;' }, { default: () => row.createdAt }) },
			{
				title: '操作',
				key: 'actions',
				width: 200,
				render: row => h(NSpace, { size: 'small' }, {
					default: () => [
						h(NButton, { size: 'tiny', quaternary: true, type: 'primary', onClick: () => emit('edit', row) }, { default: () => '編輯' }),
						h(NButton, { size: 'tiny', quaternary: true, onClick: () => emit('access', row) }, { default: () => '成員' })
					]
				})
			}
		])

		return () => h(NEl, {
			tag: 'div',
			style: 'padding: 16px 24px 20px; background: #f8fafc; border-left: 3px solid #6366f1;'
		}, {
			default: () => h(NFlex, { vertical: true, size: 12 }, {
				default: () => [
					h(NFlex, { justify: 'space-between', align: 'center' }, {
						default: () => [
							h(NFlex, { align: 'center', size: 8 }, {
								default: () => [
									h(NText, { strong: true, style: 'color: #0f172a; font-size: 13px;' }, { default: () => '門市列表' }),
									h(NText, { depth: 3, style: 'font-size: 12px;' }, { default: () => `· ${props.enterprise.name}` })
								]
							}),
							h(NButton, { size: 'small', type: 'primary', ghost: true, onClick: () => emit('create') }, { default: () => '＋ 新增門市' })
						]
					}),
					props.enterprise.stores.length === 0
						? h(NEmpty, { description: '此企業底下尚未建立任何門市', size: 'small', style: 'padding: 24px 0;' })
						: h(NDataTable, {
							columns: subColumns.value,
							data: props.enterprise.stores,
							size: 'small',
							bordered: false,
							rowKey: (row: StoreRow) => row.store_id
						})
				]
			})
		})
	}
})
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
              ENT
            </n-text>
          </n-el>
          <n-flex vertical :size="2">
            <n-h2 style="margin: 0; color: #0f172a; font-size: 20px; letter-spacing: -0.01em;">
              企業管理
            </n-h2>
            <n-text depth="3" style="font-size: 12px;">
              管理客戶集團與其底下的門市資源
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
                企業數
              </n-text>
              <n-text strong style="color: #0f172a; font-size: 18px;">
                {{ stats.enterprises }}
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
                門市總數
              </n-text>
              <n-text strong style="color: #0f172a; font-size: 18px;">
                {{ stats.stores }}
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
                營運中
              </n-text>
              <n-text strong style="color: #4f46e5; font-size: 18px;">
                {{ stats.active }}
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
              placeholder="搜尋企業名稱或 ID"
              clearable
              style="width: 320px;"
            />
            <n-text depth="3" style="font-size: 13px;">
              共 <n-text strong style="color: #0f172a;">
                {{ filteredList.length }}
              </n-text> 間企業
            </n-text>
          </n-flex>
          <n-tooltip>
            <template #trigger>
              <n-button
                type="primary"
                strong
                @click="openCreate"
              >
                ＋ 新增企業
              </n-button>
            </template>
            建立一個新的客戶集團
          </n-tooltip>
        </n-flex>

        <n-spin :show="enterpriseQuery.isPending.value">
          <n-data-table
            :columns="columns"
            :data="filteredList"
            :row-key="(row: EnterpriseRow) => row.enterprise_id"
            :bordered="false"
            size="medium"
            :scroll-x="1200"
          />
        </n-spin>
      </n-card>

      <n-flex justify="center" style="margin-top: 20px;">
        <n-text depth="3" style="font-size: 12px;">
          展開列以查看 / 管理該企業底下的門市
        </n-text>
      </n-flex>
    </n-layout-content>

    <n-modal
      v-model:show="state.feature.showFormModal"
      preset="card"
      :title="state.feature.mode === 'create' ? '新增企業' : '編輯企業'"
      style="width: 480px; border-radius: 14px;"
      :bordered="false"
    >
      <n-form
        ref="formRef"
        :model="state.data.form"
        :rules="state.feature.rules"
        label-placement="top"
      >
        <n-form-item v-if="state.feature.mode === 'edit'" label="Enterprise ID">
          <n-input :value="state.data.form.enterprise_id" disabled />
        </n-form-item>
        <n-form-item label="企業名稱" path="name">
          <n-input v-model:value="state.data.form.name" placeholder="例如：星耀餐飲集團" />
        </n-form-item>
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
      :title="`成員權限 · ${state.data.currentStoreName}`"
      style="width: 720px; border-radius: 14px;"
      :bordered="false"
    >
      <n-spin :show="accessQuery.isFetching.value">
        <n-flex vertical :size="12">
          <n-flex align="center" justify="space-between">
            <n-text depth="3" style="font-size: 12px;">
              Store ID
              <n-text code depth="2" style="font-size: 12px; margin-left: 6px;">
                {{ state.data.currentStoreId }}
              </n-text>
            </n-text>
            <n-text depth="3" style="font-size: 12px;">
              共 <n-text strong style="color: #0f172a;">{{ accessQuery.data.value?.length ?? 0 }}</n-text> 位成員
            </n-text>
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
      v-model:show="state.feature.showStoreModal"
      preset="card"
      :title="state.feature.storeMode === 'create' ? '新增門市' : '編輯門市'"
      style="width: 460px; border-radius: 14px;"
      :bordered="false"
    >
      <n-form
        :model="state.data.storeForm"
        :rules="state.feature.storeRules"
        label-placement="top"
      >
        <n-form-item v-if="state.feature.storeMode === 'edit'" label="Store ID">
          <n-input :value="state.data.storeForm.store_id" disabled />
        </n-form-item>
        <n-form-item label="門市名稱" path="name">
          <n-input v-model:value="state.data.storeForm.name" placeholder="例如：信義旗艦店" />
        </n-form-item>
        <n-form-item label="同時運行裝置數上限">
          <n-input-number
            v-model:value="state.data.storeForm.running_devices_limit"
            :min="1"
            :max="64"
            style="width: 100%;"
          />
        </n-form-item>
        <n-form-item label="營運狀態">
          <n-switch v-model:value="state.data.storeForm.active">
            <template #checked>
              營運中
            </template>
            <template #unchecked>
              停用
            </template>
          </n-switch>
        </n-form-item>
      </n-form>
      <template #footer>
        <n-flex justify="end" :size="8">
          <n-button @click="state.feature.showStoreModal = false">
            取消
          </n-button>
          <n-button
            type="primary"
            :loading="createStoreMutation.isPending.value || editStoreMutation.isPending.value"
            @click="handleSubmitStore"
          >
            {{ state.feature.storeMode === 'create' ? '建立' : '儲存' }}
          </n-button>
        </n-flex>
      </template>
    </n-modal>
  </n-layout>
</template>
