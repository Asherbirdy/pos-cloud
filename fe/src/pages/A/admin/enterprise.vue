<route lang="yaml">
meta:
  layout: false
</route>

<script setup lang="ts">
import { NBadge, NButton, NCard, NDataTable, NDivider, NEl, NEmpty, NFlex, NForm, NFormItem, NH2, NInput, NInputNumber, NLayout, NLayoutContent, NLayoutHeader, NModal, NPopconfirm, NSpace, NSwitch, NTag, NText, NTooltip } from 'naive-ui'
import type { DataTableColumns, FormInst, FormRules } from 'naive-ui'
import { h } from 'vue'

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
	contact: string
	stores: StoreRow[]
	createdAt: string
	updatedAt: string
}

const message = useMessage()

const formRef = ref<FormInst | null>(null)

const state = ref({
	data: {
		keyword: '',
		list: [
			{
				enterprise_id: 'ent_0001',
				name: '星耀餐飲集團',
				contact: 'ops@stardine.com',
				createdAt: '2025-08-12 10:24',
				updatedAt: '2026-04-18 09:11',
				stores: [
					{ store_id: 'st_1001', name: '星耀信義旗艦店', active: true, running_devices_limit: 8, createdAt: '2025-08-15 11:00' },
					{ store_id: 'st_1002', name: '星耀內湖店', active: true, running_devices_limit: 4, createdAt: '2025-09-02 14:32' },
					{ store_id: 'st_1003', name: '星耀板橋店', active: false, running_devices_limit: 4, createdAt: '2025-10-21 16:18' }
				]
			},
			{
				enterprise_id: 'ent_0002',
				name: '甘茶手作飲品',
				contact: 'hello@gantea.tw',
				createdAt: '2025-09-03 14:02',
				updatedAt: '2026-03-10 17:45',
				stores: [
					{ store_id: 'st_2001', name: '甘茶南港車站店', active: true, running_devices_limit: 2, createdAt: '2025-09-10 09:00' },
					{ store_id: 'st_2002', name: '甘茶西門店', active: true, running_devices_limit: 2, createdAt: '2025-11-04 12:20' }
				]
			},
			{
				enterprise_id: 'ent_0003',
				name: '木森麵包工坊',
				contact: 'service@mosen.bakery',
				createdAt: '2025-11-21 09:48',
				updatedAt: '2026-04-29 22:01',
				stores: [
					{ store_id: 'st_3001', name: '木森大安總店', active: true, running_devices_limit: 3, createdAt: '2025-11-25 10:30' }
				]
			},
			{
				enterprise_id: 'ent_0004',
				name: '海風日式食堂',
				contact: 'admin@kaifu.co',
				createdAt: '2026-01-09 18:20',
				updatedAt: '2026-04-30 08:33',
				stores: []
			}
		] as EnterpriseRow[],
		form: {
			enterprise_id: '',
			name: '',
			contact: ''
		},
		storeForm: {
			store_id: '',
			name: '',
			active: true,
			running_devices_limit: 2
		},
		currentEnterpriseId: ''
	},
	feature: {
		mode: 'create' as 'create' | 'edit',
		showFormModal: false,
		storeMode: 'create' as 'create' | 'edit',
		showStoreModal: false,
		expandedKeys: [] as string[],
		rules: {
			name: [
				{ required: true, message: '請輸入企業名稱', trigger: 'blur' }
			],
			contact: [
				{ required: true, message: '請輸入聯絡 Email', trigger: 'blur' },
				{ type: 'email', message: 'Email 格式錯誤', trigger: ['blur', 'input'] }
			]
		} as FormRules,
		storeRules: {
			name: [
				{ required: true, message: '請輸入門市名稱', trigger: 'blur' }
			]
		} as FormRules
	}
})

const filteredList = computed(() => {
	const kw = state.value.data.keyword.trim().toLowerCase()
	if (!kw) return state.value.data.list
	return state.value.data.list.filter(item =>
		item.name.toLowerCase().includes(kw)
		|| item.contact.toLowerCase().includes(kw)
		|| item.enterprise_id.toLowerCase().includes(kw)
	)
})

const openCreate = () => {
	state.value.feature.mode = 'create'
	state.value.data.form = { enterprise_id: '', name: '', contact: '' }
	state.value.feature.showFormModal = true
}

const openEdit = (row: EnterpriseRow) => {
	state.value.feature.mode = 'edit'
	state.value.data.form = {
		enterprise_id: row.enterprise_id,
		name: row.name,
		contact: row.contact
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

	const now = new Date().toISOString().replace('T', ' ').slice(0, 16)
	if (state.value.feature.mode === 'create') {
		const newId = `ent_${String(state.value.data.list.length + 1).padStart(4, '0')}`
		state.value.data.list.unshift({
			enterprise_id: newId,
			name: state.value.data.form.name,
			contact: state.value.data.form.contact,
			stores: [],
			createdAt: now,
			updatedAt: now
		})
		message.success('已新增企業')
	}
	else {
		const target = state.value.data.list.find(item => item.enterprise_id === state.value.data.form.enterprise_id)
		if (target) {
			target.name = state.value.data.form.name
			target.contact = state.value.data.form.contact
			target.updatedAt = now
		}
		message.success('已更新企業')
	}
	state.value.feature.showFormModal = false
}

const handleDelete = (row: EnterpriseRow) => {
	state.value.data.list = state.value.data.list.filter(item => item.enterprise_id !== row.enterprise_id)
	message.success(`已刪除「${row.name}」`)
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
	const target = state.value.data.list.find(item => item.enterprise_id === state.value.data.currentEnterpriseId)
	if (!target) return

	if (state.value.feature.storeMode === 'create') {
		const newId = `st_${Date.now()}`
		target.stores.push({
			store_id: newId,
			name: state.value.data.storeForm.name,
			active: state.value.data.storeForm.active,
			running_devices_limit: state.value.data.storeForm.running_devices_limit,
			createdAt: new Date().toISOString().replace('T', ' ').slice(0, 16)
		})
		message.success('已新增門市')
	}
	else {
		const idx = target.stores.findIndex(s => s.store_id === state.value.data.storeForm.store_id)
		if (idx >= 0) {
			target.stores[idx] = { ...target.stores[idx], ...state.value.data.storeForm }
			message.success('已更新門市')
		}
	}
	state.value.feature.showStoreModal = false
}

const handleDeleteStore = (enterpriseId: string, storeId: string) => {
	const target = state.value.data.list.find(item => item.enterprise_id === enterpriseId)
	if (!target) return
	target.stores = target.stores.filter(s => s.store_id !== storeId)
	message.success('已刪除門市')
}

const columns = computed<DataTableColumns<EnterpriseRow>>(() => [
	{
		type: 'expand',
		expandable: () => true,
		renderExpand: row => h(NEl, { tag: 'div', style: 'padding: 8px 4px 4px; background: #fafbfc; border-radius: 8px;' }, {
			default: () => h(StoreSubTable, {
				enterprise: row,
				onCreate: () => openCreateStore(row),
				onEdit: (store: StoreRow) => openEditStore(row.enterprise_id, store),
				onDelete: (storeId: string) => handleDeleteStore(row.enterprise_id, storeId)
			})
		})
	},
	{
		title: 'Enterprise ID',
		key: 'enterprise_id',
		width: 140,
		render: row => h(NText, { code: true, depth: 3 }, { default: () => row.enterprise_id })
	},
	{
		title: '企業名稱',
		key: 'name',
		render: row => h(NText, { strong: true }, { default: () => row.name })
	},
	{
		title: '聯絡 Email',
		key: 'contact',
		render: row => h(NText, { depth: 2 }, { default: () => row.contact })
	},
	{
		title: '門市數量',
		key: 'stores',
		width: 110,
		align: 'center',
		render: row => h(NBadge, {
			value: row.stores.length,
			showZero: true,
			color: row.stores.length === 0 ? '#d1d5db' : '#b91c1c',
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
				h(NButton, { size: 'small', secondary: true, type: 'primary', onClick: () => openEdit(row) }, { default: () => '編輯' }),
				h(NButton, { size: 'small', secondary: true, onClick: () => openCreateStore(row) }, { default: () => '＋門市' }),
				h(NPopconfirm, {
					onPositiveClick: () => handleDelete(row),
					positiveText: '刪除',
					negativeText: '取消'
				}, {
					trigger: () => h(NButton, { size: 'small', secondary: true, type: 'error' }, { default: () => '刪除' }),
					default: () => `確定要刪除企業「${row.name}」？此操作無法復原。`
				})
			]
		})
	}
])

const StoreSubTable = defineComponent({
	props: {
		enterprise: { type: Object as PropType<EnterpriseRow>, required: true }
	},
	emits: ['create', 'edit', 'delete'],
	setup: (props, { emit }) => {
		const subColumns = computed<DataTableColumns<StoreRow>>(() => [
			{ title: 'Store ID', key: 'store_id', width: 140, render: row => h(NText, { code: true, depth: 3 }, { default: () => row.store_id }) },
			{ title: '門市名稱', key: 'name' },
			{
				title: '狀態',
				key: 'active',
				width: 100,
				render: row => h(NTag, { type: row.active ? 'success' : 'default', size: 'small', round: true }, { default: () => row.active ? '營運中' : '停用' })
			},
			{ title: '裝置上限', key: 'running_devices_limit', width: 110, align: 'center' },
			{ title: '建立時間', key: 'createdAt', width: 160, render: row => h(NText, { depth: 3, style: 'font-size: 12px;' }, { default: () => row.createdAt }) },
			{
				title: '操作',
				key: 'actions',
				width: 160,
				render: row => h(NSpace, { size: 'small' }, {
					default: () => [
						h(NButton, { size: 'tiny', secondary: true, type: 'primary', onClick: () => emit('edit', row) }, { default: () => '編輯' }),
						h(NPopconfirm, {
							onPositiveClick: () => emit('delete', row.store_id),
							positiveText: '刪除',
							negativeText: '取消'
						}, {
							trigger: () => h(NButton, { size: 'tiny', secondary: true, type: 'error' }, { default: () => '刪除' }),
							default: () => `確定要刪除門市「${row.name}」？`
						})
					]
				})
			}
		])

		return () => h(NFlex, { vertical: true, size: 12, style: 'padding: 12px 16px 16px;' }, {
			default: () => [
				h(NFlex, { justify: 'space-between', align: 'center' }, {
					default: () => [
						h(NText, { strong: true, depth: 1 }, { default: () => `「${props.enterprise.name}」底下的門市` }),
						h(NButton, { size: 'small', type: 'primary', secondary: true, onClick: () => emit('create') }, { default: () => '＋ 新增門市' })
					]
				}),
				props.enterprise.stores.length === 0
					? h(NEmpty, { description: '此企業底下尚未建立任何門市', size: 'small', style: 'padding: 16px 0;' })
					: h(NDataTable, {
						columns: subColumns.value,
						data: props.enterprise.stores,
						size: 'small',
						bordered: false,
						rowKey: (row: StoreRow) => row.store_id
					})
			]
		})
	}
})
</script>

<template>
  <n-layout style="min-height: 100vh; background: #f3f4f6;">
    <n-layout-header
      bordered
      style="
        padding: 16px 32px;
        background: linear-gradient(135deg, #0f172a 0%, #7c2d12 60%, #b91c1c 100%);
      "
    >
      <n-flex align="center" justify="space-between">
        <n-flex align="center" :size="14">
          <n-el
            tag="div"
            style="
              width: 42px; height: 42px; border-radius: 12px;
              background: linear-gradient(135deg, #b91c1c, #f59e0b);
              display: flex; align-items: center; justify-content: center;
              box-shadow: 0 6px 16px rgba(185,28,28,0.35);
            "
          >
            <n-text strong style="color: white; font-size: 14px; letter-spacing: 0.05em;">
              ADM
            </n-text>
          </n-el>
          <n-flex vertical :size="2">
            <n-h2 style="margin: 0; color: white; font-size: 18px;">
              企業管理 Enterprise
            </n-h2>
            <n-text style="color: rgba(255,255,255,0.7); font-size: 12px;">
              POS 開發者後台 · 管理客戶集團與其底下的門市
            </n-text>
          </n-flex>
        </n-flex>
      </n-flex>
    </n-layout-header>

    <n-layout-content style="padding: 24px 32px;">
      <n-card :bordered="false" style="border-radius: 14px; box-shadow: 0 4px 20px rgba(0,0,0,0.04);">
        <n-flex
          justify="space-between"
          align="center"
          style="margin-bottom: 16px;"
        >
          <n-flex align="center" :size="12">
            <n-input
              v-model:value="state.data.keyword"
              placeholder="搜尋企業名稱、Email 或 ID"
              clearable
              style="width: 320px;"
            />
            <n-text depth="3" style="font-size: 13px;">
              共 {{ filteredList.length }} 間企業
            </n-text>
          </n-flex>
          <n-tooltip>
            <template #trigger>
              <n-button
                type="primary"
                strong
                style="background: linear-gradient(135deg, #b91c1c, #f59e0b);"
                @click="openCreate"
              >
                ＋ 新增企業
              </n-button>
            </template>
            建立一個新的客戶集團
          </n-tooltip>
        </n-flex>

        <n-data-table
          :columns="columns"
          :data="filteredList"
          :row-key="(row: EnterpriseRow) => row.enterprise_id"
          :bordered="false"
          striped
          size="medium"
          :scroll-x="1200"
        />
      </n-card>

      <n-divider style="margin: 28px 0 12px;">
        <n-text depth="3" style="font-size: 12px;">
          展開列以查看 / 管理該企業底下的門市
        </n-text>
      </n-divider>
    </n-layout-content>

    <n-modal
      v-model:show="state.feature.showFormModal"
      preset="card"
      :title="state.feature.mode === 'create' ? '新增企業' : '編輯企業'"
      style="width: 480px;"
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
        <n-form-item label="聯絡 Email" path="contact">
          <n-input v-model:value="state.data.form.contact" placeholder="ops@example.com" />
        </n-form-item>
      </n-form>
      <template #footer>
        <n-flex justify="end" :size="8">
          <n-button @click="state.feature.showFormModal = false">
            取消
          </n-button>
          <n-button type="primary" @click="handleSubmit">
            {{ state.feature.mode === 'create' ? '建立' : '儲存' }}
          </n-button>
        </n-flex>
      </template>
    </n-modal>

    <n-modal
      v-model:show="state.feature.showStoreModal"
      preset="card"
      :title="state.feature.storeMode === 'create' ? '新增門市' : '編輯門市'"
      style="width: 460px;"
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
          <n-button type="primary" @click="handleSubmitStore">
            {{ state.feature.storeMode === 'create' ? '建立' : '儲存' }}
          </n-button>
        </n-flex>
      </template>
    </n-modal>
  </n-layout>
</template>
