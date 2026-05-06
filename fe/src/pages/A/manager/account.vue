<script setup lang="ts">
import { NAvatar, NBadge, NButton, NCard, NDataTable, NDivider, NDrawer, NDrawerContent, NEl, NFlex, NForm, NFormItem, NGi, NGrid, NH2, NInput, NLayout, NLayoutContent, NLayoutHeader, NPopconfirm, NSelect, NSpace, NSwitch, NTag, NText, NTooltip } from 'naive-ui'
import type { DataTableColumns, FormInst, FormRules, SelectOption } from 'naive-ui'
import { h } from 'vue'

interface StaffRow {
	staff_id: string
	name: string
	email: string
	phone: string
	store_id: string
	storeName: string
	role: 'store_manager' | 'store_staff'
	active: boolean
	lastLogin: string
	createdAt: string
}

const message = useMessage()
const formRef = ref<FormInst | null>(null)

const state = ref({
	data: {
		keyword: '',
		filterStore: 'all',
		filterRole: 'all',
		form: {
			staff_id: '',
			name: '',
			email: '',
			phone: '',
			password: '',
			confirmPassword: '',
			store_id: '',
			role: 'store_staff' as 'store_manager' | 'store_staff',
			active: true,
			sendInvite: true
		},
		list: [
			{ staff_id: 'mb_0001', name: '林書豪', email: 'lin@stardine.com', phone: '0912-345-678', store_id: 'st_1001', storeName: '星耀信義旗艦店', role: 'store_manager', active: true, lastLogin: '2026-05-04 21:14', createdAt: '2025-08-15' },
			{ staff_id: 'mb_0002', name: '陳怡君', email: 'chen.yj@stardine.com', phone: '0922-118-093', store_id: 'st_1001', storeName: '星耀信義旗艦店', role: 'store_staff', active: true, lastLogin: '2026-05-05 09:01', createdAt: '2025-08-22' },
			{ staff_id: 'mb_0003', name: '王大明', email: 'wang.dm@stardine.com', phone: '0933-552-019', store_id: 'st_1001', storeName: '星耀信義旗艦店', role: 'store_staff', active: false, lastLogin: '2026-04-12 14:39', createdAt: '2025-09-01' },
			{ staff_id: 'mb_0004', name: '吳詩涵', email: 'wu.sh@stardine.com', phone: '0987-211-450', store_id: 'st_1002', storeName: '星耀內湖店', role: 'store_manager', active: true, lastLogin: '2026-05-04 18:22', createdAt: '2025-09-12' },
			{ staff_id: 'mb_0005', name: '黃柏翰', email: 'huang.bh@stardine.com', phone: '0918-660-271', store_id: 'st_1002', storeName: '星耀內湖店', role: 'store_staff', active: true, lastLogin: '2026-05-05 08:45', createdAt: '2025-10-03' },
			{ staff_id: 'mb_0006', name: '李芷涵', email: 'lee.zh@stardine.com', phone: '0976-883-552', store_id: 'st_1003', storeName: '星耀板橋店', role: 'store_staff', active: true, lastLogin: '2026-05-03 12:08', createdAt: '2025-11-10' }
		] as StaffRow[]
	},
	feature: {
		showDrawer: false,
		mode: 'create' as 'create' | 'edit',
		submitting: false,
		storeOptions: [
			{ label: '星耀信義旗艦店', value: 'st_1001' },
			{ label: '星耀內湖店', value: 'st_1002' },
			{ label: '星耀板橋店', value: 'st_1003' },
			{ label: '甘茶南港車站店', value: 'st_2001' },
			{ label: '甘茶西門店', value: 'st_2002' },
			{ label: '木森大安總店', value: 'st_3001' }
		] as SelectOption[],
		filterStoreOptions: [
			{ label: '全部店面', value: 'all' },
			{ label: '星耀信義旗艦店', value: 'st_1001' },
			{ label: '星耀內湖店', value: 'st_1002' },
			{ label: '星耀板橋店', value: 'st_1003' }
		] as SelectOption[],
		roleOptions: [
			{ label: '店長 Store Manager', value: 'store_manager' },
			{ label: '店員 Store Staff', value: 'store_staff' }
		] as SelectOption[],
		filterRoleOptions: [
			{ label: '全部角色', value: 'all' },
			{ label: '店長', value: 'store_manager' },
			{ label: '店員', value: 'store_staff' }
		] as SelectOption[],
		rules: {
			name: [{ required: true, message: '請輸入員工姓名', trigger: 'blur' }],
			email: [
				{ required: true, message: '請輸入 Email', trigger: 'blur' },
				{ type: 'email', message: 'Email 格式錯誤', trigger: ['blur', 'input'] }
			],
			phone: [{ required: true, message: '請輸入聯絡電話', trigger: 'blur' }],
			password: [
				{ required: true, message: '請輸入密碼', trigger: 'blur' },
				{ min: 8, message: '密碼至少 8 碼', trigger: 'blur' }
			],
			confirmPassword: [
				{ required: true, message: '請再次確認密碼', trigger: 'blur' },
				{
					validator: (_rule, value: string) => value === state.value.data.form.password,
					message: '兩次輸入的密碼不一致',
					trigger: ['blur', 'input']
				}
			],
			store_id: [{ required: true, message: '請選擇所屬店面', trigger: 'change' }],
			role: [{ required: true, message: '請選擇角色', trigger: 'change' }]
		} as FormRules
	}
})

const filteredList = computed(() => {
	const kw = state.value.data.keyword.trim().toLowerCase()
	return state.value.data.list.filter((row) => {
		if (state.value.data.filterStore !== 'all' && row.store_id !== state.value.data.filterStore) return false
		if (state.value.data.filterRole !== 'all' && row.role !== state.value.data.filterRole) return false
		if (!kw) return true
		return row.name.toLowerCase().includes(kw)
			|| row.email.toLowerCase().includes(kw)
			|| row.phone.includes(kw)
	})
})

const activeCount = computed(() => state.value.data.list.filter(s => s.active).length)
const managerCount = computed(() => state.value.data.list.filter(s => s.role === 'store_manager').length)
const staffCount = computed(() => state.value.data.list.filter(s => s.role === 'store_staff').length)

const resetForm = () => {
	state.value.data.form = {
		staff_id: '',
		name: '',
		email: '',
		phone: '',
		password: '',
		confirmPassword: '',
		store_id: '',
		role: 'store_staff',
		active: true,
		sendInvite: true
	}
}

const openCreate = () => {
	state.value.feature.mode = 'create'
	resetForm()
	state.value.feature.showDrawer = true
}

const openEdit = (row: StaffRow) => {
	state.value.feature.mode = 'edit'
	state.value.data.form = {
		staff_id: row.staff_id,
		name: row.name,
		email: row.email,
		phone: row.phone,
		password: '',
		confirmPassword: '',
		store_id: row.store_id,
		role: row.role,
		active: row.active,
		sendInvite: false
	}
	state.value.feature.showDrawer = true
}

const handleSubmit = async () => {
	try {
		await formRef.value?.validate()
	}
	catch {
		return
	}

	state.value.feature.submitting = true
	setTimeout(() => {
		const storeName = state.value.feature.storeOptions.find(o => o.value === state.value.data.form.store_id)?.label as string

		if (state.value.feature.mode === 'create') {
			const newId = `mb_${String(state.value.data.list.length + 1).padStart(4, '0')}`
			state.value.data.list.unshift({
				staff_id: newId,
				name: state.value.data.form.name,
				email: state.value.data.form.email,
				phone: state.value.data.form.phone,
				store_id: state.value.data.form.store_id,
				storeName,
				role: state.value.data.form.role,
				active: state.value.data.form.active,
				lastLogin: '—',
				createdAt: new Date().toISOString().slice(0, 10)
			})
			message.success(state.value.data.form.sendInvite ? '已建立帳號並寄出邀請信' : '已建立帳號')
		}
		else {
			const target = state.value.data.list.find(s => s.staff_id === state.value.data.form.staff_id)
			if (target) {
				target.name = state.value.data.form.name
				target.email = state.value.data.form.email
				target.phone = state.value.data.form.phone
				target.store_id = state.value.data.form.store_id
				target.storeName = storeName
				target.role = state.value.data.form.role
				target.active = state.value.data.form.active
			}
			message.success('已更新員工資料')
		}
		state.value.feature.submitting = false
		state.value.feature.showDrawer = false
	}, 400)
}

const handleToggleActive = (row: StaffRow) => {
	row.active = !row.active
	message.success(`${row.name} 已${row.active ? '啟用' : '停用'}`)
}

const handleDelete = (row: StaffRow) => {
	state.value.data.list = state.value.data.list.filter(s => s.staff_id !== row.staff_id)
	message.success(`已刪除「${row.name}」帳號`)
}

const handleResetPassword = (row: StaffRow) => {
	message.success(`已寄出重設密碼信至 ${row.email}`)
}

const columns = computed<DataTableColumns<StaffRow>>(() => [
	{
		title: '員工',
		key: 'name',
		width: 220,
		render: row => h(NFlex, { align: 'center', size: 10 }, {
			default: () => [
				h(NAvatar, {
					round: true,
					size: 36,
					style: `background: ${row.role === 'store_manager' ? '#0f172a' : '#475569'}; color: white; font-size: 12px;`
				}, { default: () => row.name.slice(0, 1) }),
				h(NFlex, { vertical: true, size: 2 }, {
					default: () => [
						h(NText, { strong: true }, { default: () => row.name }),
						h(NText, { code: true, depth: 3, style: 'font-size: 11px;' }, { default: () => row.staff_id })
					]
				})
			]
		})
	},
	{ title: 'Email', key: 'email', render: row => h(NText, { depth: 2 }, { default: () => row.email }) },
	{ title: '電話', key: 'phone', width: 140, render: row => h(NText, { depth: 3 }, { default: () => row.phone }) },
	{
		title: '所屬店面',
		key: 'storeName',
		width: 180,
		render: row => h(NTag, { size: 'small', round: true, bordered: false, type: 'info' }, { default: () => row.storeName })
	},
	{
		title: '角色',
		key: 'role',
		width: 120,
		render: row => h(NTag, {
			size: 'small',
			round: true,
			type: row.role === 'store_manager' ? 'warning' : 'default'
		}, { default: () => row.role === 'store_manager' ? '店長' : '店員' })
	},
	{
		title: '狀態',
		key: 'active',
		width: 110,
		render: row => h(NSwitch, {
			value: row.active,
			'onUpdate:value': () => handleToggleActive(row),
			size: 'small'
		})
	},
	{
		title: '最後登入',
		key: 'lastLogin',
		width: 150,
		render: row => h(NText, { depth: 3, style: 'font-size: 12px;' }, { default: () => row.lastLogin })
	},
	{
		title: '操作',
		key: 'actions',
		width: 220,
		fixed: 'right',
		render: row => h(NSpace, { size: 'small' }, {
			default: () => [
				h(NButton, { size: 'small', secondary: true, type: 'primary', onClick: () => openEdit(row) }, { default: () => '編輯' }),
				h(NButton, { size: 'small', secondary: true, onClick: () => handleResetPassword(row) }, { default: () => '重設密碼' }),
				h(NPopconfirm, {
					onPositiveClick: () => handleDelete(row),
					positiveText: '刪除',
					negativeText: '取消'
				}, {
					trigger: () => h(NButton, { size: 'small', secondary: true, type: 'error' }, { default: () => '刪除' }),
					default: () => `確定要刪除「${row.name}」的帳號？`
				})
			]
		})
	}
])
</script>

<template>
  <n-layout style="min-height: 100vh; background: #f1f5f9;">
    <n-layout-header
      bordered
      style="
        padding: 16px 32px;
        background: linear-gradient(135deg, #0f172a 0%, #1e293b 50%, #475569 100%);
      "
    >
      <n-flex align="center" justify="space-between">
        <n-flex align="center" :size="14">
          <n-el
            tag="div"
            style="
              width: 42px; height: 42px; border-radius: 12px;
              background: linear-gradient(135deg, #1e293b, #64748b);
              display: flex; align-items: center; justify-content: center;
              box-shadow: 0 6px 16px rgba(15,23,42,0.4);
            "
          >
            <n-text strong style="color: white; font-size: 14px; letter-spacing: 0.05em;">
              MGR
            </n-text>
          </n-el>
          <n-flex vertical :size="2">
            <n-h2 style="margin: 0; color: white; font-size: 18px;">
              員工帳號管理 Account
            </n-h2>
            <n-text style="color: rgba(255,255,255,0.7); font-size: 12px;">
              新增、編輯、停用 store 員工帳號
            </n-text>
          </n-flex>
        </n-flex>
        <n-button
          strong
          style="background: linear-gradient(135deg, #0ea5e9, #2563eb); color: white;"
          @click="openCreate"
        >
          ＋ 新增員工帳號
        </n-button>
      </n-flex>
    </n-layout-header>

    <n-layout-content style="padding: 24px 32px;">
      <n-grid :cols="3" :x-gap="16" :y-gap="16" responsive="screen">
        <n-gi>
          <n-card :bordered="false" style="border-radius: 14px; box-shadow: 0 4px 14px rgba(15,23,42,0.06);">
            <n-flex justify="space-between" align="center">
              <n-flex vertical :size="4">
                <n-text depth="3" style="font-size: 12px;">
                  啟用中帳號
                </n-text>
                <n-text strong style="font-size: 24px;">
                  {{ activeCount }}
                </n-text>
              </n-flex>
              <n-badge :value="state.data.list.length" :max="99" color="#0ea5e9" />
            </n-flex>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card :bordered="false" style="border-radius: 14px; box-shadow: 0 4px 14px rgba(15,23,42,0.06);">
            <n-flex justify="space-between" align="center">
              <n-flex vertical :size="4">
                <n-text depth="3" style="font-size: 12px;">
                  店長
                </n-text>
                <n-text strong style="font-size: 24px;">
                  {{ managerCount }}
                </n-text>
              </n-flex>
              <n-tag round type="warning">
                Store Manager
              </n-tag>
            </n-flex>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card :bordered="false" style="border-radius: 14px; box-shadow: 0 4px 14px rgba(15,23,42,0.06);">
            <n-flex justify="space-between" align="center">
              <n-flex vertical :size="4">
                <n-text depth="3" style="font-size: 12px;">
                  店員
                </n-text>
                <n-text strong style="font-size: 24px;">
                  {{ staffCount }}
                </n-text>
              </n-flex>
              <n-tag round>
                Store Staff
              </n-tag>
            </n-flex>
          </n-card>
        </n-gi>
      </n-grid>

      <n-card :bordered="false" style="margin-top: 20px; border-radius: 14px; box-shadow: 0 4px 14px rgba(15,23,42,0.06);">
        <n-flex justify="space-between" align="center" style="margin-bottom: 16px;">
          <n-flex align="center" :size="10">
            <n-input
              v-model:value="state.data.keyword"
              placeholder="搜尋姓名 / Email / 電話"
              clearable
              style="width: 280px;"
            />
            <n-select
              v-model:value="state.data.filterStore"
              :options="state.feature.filterStoreOptions"
              style="width: 180px;"
            />
            <n-select
              v-model:value="state.data.filterRole"
              :options="state.feature.filterRoleOptions"
              style="width: 140px;"
            />
            <n-divider vertical />
            <n-text depth="3" style="font-size: 13px;">
              共 {{ filteredList.length }} 筆
            </n-text>
          </n-flex>
          <n-tooltip>
            <template #trigger>
              <n-button secondary>
                匯入 CSV
              </n-button>
            </template>
            批次匯入員工帳號（規劃中）
          </n-tooltip>
        </n-flex>

        <n-data-table
          :columns="columns"
          :data="filteredList"
          :row-key="(row: StaffRow) => row.staff_id"
          :bordered="false"
          striped
          size="medium"
          :scroll-x="1280"
        />
      </n-card>
    </n-layout-content>

    <n-drawer v-model:show="state.feature.showDrawer" :width="520" placement="right">
      <n-drawer-content
        :title="state.feature.mode === 'create' ? '新增員工帳號' : '編輯員工帳號'"
        closable
      >
        <n-form
          ref="formRef"
          :model="state.data.form"
          :rules="state.feature.rules"
          label-placement="top"
        >
          <n-grid :cols="2" :x-gap="12">
            <n-gi>
              <n-form-item label="員工姓名" path="name">
                <n-input v-model:value="state.data.form.name" placeholder="王小明" />
              </n-form-item>
            </n-gi>
            <n-gi>
              <n-form-item label="聯絡電話" path="phone">
                <n-input v-model:value="state.data.form.phone" placeholder="09xx-xxx-xxx" />
              </n-form-item>
            </n-gi>
          </n-grid>

          <n-form-item label="Email（登入帳號）" path="email">
            <n-input v-model:value="state.data.form.email" placeholder="staff@example.com" />
          </n-form-item>

          <n-grid :cols="2" :x-gap="12">
            <n-gi>
              <n-form-item label="所屬店面" path="store_id">
                <n-select
                  v-model:value="state.data.form.store_id"
                  :options="state.feature.storeOptions"
                  placeholder="請選擇店面"
                />
              </n-form-item>
            </n-gi>
            <n-gi>
              <n-form-item label="角色" path="role">
                <n-select
                  v-model:value="state.data.form.role"
                  :options="state.feature.roleOptions"
                />
              </n-form-item>
            </n-gi>
          </n-grid>

          <n-divider style="margin: 8px 0 16px;">
            <n-text depth="3" style="font-size: 12px;">
              {{ state.feature.mode === 'create' ? '初始密碼' : '更新密碼（留空則不變更）' }}
            </n-text>
          </n-divider>

          <n-grid :cols="2" :x-gap="12">
            <n-gi>
              <n-form-item label="密碼" path="password">
                <n-input
                  v-model:value="state.data.form.password"
                  type="password"
                  show-password-on="click"
                  placeholder="至少 8 碼"
                />
              </n-form-item>
            </n-gi>
            <n-gi>
              <n-form-item label="確認密碼" path="confirmPassword">
                <n-input
                  v-model:value="state.data.form.confirmPassword"
                  type="password"
                  show-password-on="click"
                  placeholder="再次輸入密碼"
                />
              </n-form-item>
            </n-gi>
          </n-grid>

          <n-flex
            align="center"
            justify="space-between"
            style="
              margin-top: 8px; padding: 12px 14px;
              background: #f8fafc; border-radius: 10px;
            "
          >
            <n-flex vertical :size="2">
              <n-text strong style="font-size: 13px;">
                立即啟用此帳號
              </n-text>
              <n-text depth="3" style="font-size: 12px;">
                關閉後員工將無法登入 POS
              </n-text>
            </n-flex>
            <n-switch v-model:value="state.data.form.active" />
          </n-flex>

          <n-flex
            v-if="state.feature.mode === 'create'"
            align="center"
            justify="space-between"
            style="
              margin-top: 12px; padding: 12px 14px;
              background: #f8fafc; border-radius: 10px;
            "
          >
            <n-flex vertical :size="2">
              <n-text strong style="font-size: 13px;">
                寄送邀請信
              </n-text>
              <n-text depth="3" style="font-size: 12px;">
                以 Email 通知員工首次登入連結
              </n-text>
            </n-flex>
            <n-switch v-model:value="state.data.form.sendInvite" />
          </n-flex>
        </n-form>

        <template #footer>
          <n-flex justify="end" :size="8">
            <n-button @click="state.feature.showDrawer = false">
              取消
            </n-button>
            <n-button
              type="primary"
              :loading="state.feature.submitting"
              style="background: linear-gradient(135deg, #1e293b, #475569);"
              @click="handleSubmit"
            >
              {{ state.feature.mode === 'create' ? '建立帳號' : '儲存變更' }}
            </n-button>
          </n-flex>
        </template>
      </n-drawer-content>
    </n-drawer>
  </n-layout>
</template>
