<script setup lang="ts">
import { NAvatar, NBadge, NButton, NCard, NDataTable, NDatePicker, NDivider, NEl, NEmpty, NFlex, NGi, NGrid, NH2, NH3, NLayout, NLayoutContent, NLayoutHeader, NProgress, NSelect, NStatistic, NTabPane, NTabs, NTag, NText, NTooltip } from 'naive-ui'
import type { DataTableColumns, SelectOption } from 'naive-ui'
import { h } from 'vue'

interface StorePerformance {
	store_id: string
	name: string
	revenue: number
	orders: number
	avgTicket: number
	deltaPct: number
	devices: number
	status: 'normal' | 'warning' | 'closed'
}

interface ProductPerformance {
	product_id: string
	name: string
	category: string
	store: string
	qty: number
	revenue: number
	share: number
	trend: 'up' | 'down' | 'flat'
}

const state = ref({
	data: {
		dateRange: [Date.now() - 86400000 * 6, Date.now()] as [number, number],
		storeFilter: 'all',
		categoryFilter: 'all',
		stores: [
			{ store_id: 'st_1001', name: '星耀信義旗艦店', revenue: 184320, orders: 412, avgTicket: 447, deltaPct: 12.4, devices: 8, status: 'normal' },
			{ store_id: 'st_1002', name: '星耀內湖店', revenue: 96100, orders: 238, avgTicket: 404, deltaPct: 4.1, devices: 4, status: 'normal' },
			{ store_id: 'st_1003', name: '星耀板橋店', revenue: 41200, orders: 121, avgTicket: 341, deltaPct: -8.6, devices: 4, status: 'warning' },
			{ store_id: 'st_2001', name: '甘茶南港車站店', revenue: 72400, orders: 510, avgTicket: 142, deltaPct: 18.9, devices: 2, status: 'normal' },
			{ store_id: 'st_2002', name: '甘茶西門店', revenue: 58800, orders: 408, avgTicket: 144, deltaPct: -2.3, devices: 2, status: 'normal' },
			{ store_id: 'st_3001', name: '木森大安總店', revenue: 33500, orders: 178, avgTicket: 188, deltaPct: 6.7, devices: 3, status: 'normal' }
		] as StorePerformance[],
		products: [
			{ product_id: 'pd_001', name: '招牌牛肉麵', category: '麵食', store: '星耀信義旗艦店', qty: 312, revenue: 78000, share: 22.4, trend: 'up' },
			{ product_id: 'pd_002', name: '醬燒雞腿便當', category: '便當', store: '星耀信義旗艦店', qty: 268, revenue: 53600, share: 15.4, trend: 'up' },
			{ product_id: 'pd_003', name: '經典珍珠奶茶 L', category: '飲品', store: '甘茶南港車站店', qty: 540, revenue: 32400, share: 12.1, trend: 'flat' },
			{ product_id: 'pd_004', name: '四季青茶 M', category: '飲品', store: '甘茶西門店', qty: 482, revenue: 19280, share: 8.7, trend: 'down' },
			{ product_id: 'pd_005', name: '紅豆吐司', category: '烘焙', store: '木森大安總店', qty: 198, revenue: 9900, share: 4.4, trend: 'up' },
			{ product_id: 'pd_006', name: '蜂蜜燕麥麵包', category: '烘焙', store: '木森大安總店', qty: 142, revenue: 8520, share: 3.8, trend: 'flat' },
			{ product_id: 'pd_007', name: '招牌沙拉碗', category: '輕食', store: '星耀內湖店', qty: 176, revenue: 26400, share: 5.9, trend: 'up' },
			{ product_id: 'pd_008', name: '美式咖啡 L', category: '飲品', store: '星耀板橋店', qty: 220, revenue: 17600, share: 4.2, trend: 'down' }
		] as ProductPerformance[]
	},
	feature: {
		storeOptions: [
			{ label: '全部店面', value: 'all' },
			{ label: '星耀信義旗艦店', value: 'st_1001' },
			{ label: '星耀內湖店', value: 'st_1002' },
			{ label: '星耀板橋店', value: 'st_1003' },
			{ label: '甘茶南港車站店', value: 'st_2001' },
			{ label: '甘茶西門店', value: 'st_2002' },
			{ label: '木森大安總店', value: 'st_3001' }
		] as SelectOption[],
		categoryOptions: [
			{ label: '全部分類', value: 'all' },
			{ label: '麵食', value: '麵食' },
			{ label: '便當', value: '便當' },
			{ label: '飲品', value: '飲品' },
			{ label: '烘焙', value: '烘焙' },
			{ label: '輕食', value: '輕食' }
		] as SelectOption[]
	}
})

const totalRevenue = computed(() => state.value.data.stores.reduce((s, x) => s + x.revenue, 0))
const totalOrders = computed(() => state.value.data.stores.reduce((s, x) => s + x.orders, 0))
const avgTicket = computed(() => totalOrders.value === 0 ? 0 : Math.round(totalRevenue.value / totalOrders.value))
const activeStores = computed(() => state.value.data.stores.filter(s => s.status !== 'closed').length)

const filteredStores = computed(() => {
	if (state.value.data.storeFilter === 'all') return state.value.data.stores
	return state.value.data.stores.filter(s => s.store_id === state.value.data.storeFilter)
})

const filteredProducts = computed(() => {
	let list = state.value.data.products
	if (state.value.data.storeFilter !== 'all') {
		const target = state.value.feature.storeOptions.find(o => o.value === state.value.data.storeFilter)
		if (target) list = list.filter(p => p.store === target.label)
	}
	if (state.value.data.categoryFilter !== 'all') {
		list = list.filter(p => p.category === state.value.data.categoryFilter)
	}
	return [...list].sort((a, b) => b.revenue - a.revenue)
})

const maxStoreRevenue = computed(() => Math.max(...filteredStores.value.map(s => s.revenue), 1))

const formatMoney = (n: number) => `NT$ ${n.toLocaleString()}`

const storeColumns = computed<DataTableColumns<StorePerformance>>(() => [
	{
		title: '店面',
		key: 'name',
		render: row => h(NFlex, { align: 'center', size: 8 }, {
			default: () => [
				h(NAvatar, { round: true, size: 'small', style: 'background: #475569; color: #fff; font-size: 11px;' }, { default: () => row.name.slice(0, 2) }),
				h(NText, { strong: true }, { default: () => row.name })
			]
		})
	},
	{
		title: '營收',
		key: 'revenue',
		width: 240,
		render: row => h(NFlex, { vertical: true, size: 4 }, {
			default: () => [
				h(NText, { strong: true }, { default: () => formatMoney(row.revenue) }),
				h(NProgress, {
					type: 'line',
					percentage: Math.round(row.revenue / maxStoreRevenue.value * 100),
					height: 6,
					color: '#475569',
					railColor: '#e5e7eb',
					showIndicator: false
				})
			]
		})
	},
	{ title: '訂單數', key: 'orders', width: 100, align: 'right', render: row => row.orders.toLocaleString() },
	{ title: '客單價', key: 'avgTicket', width: 110, align: 'right', render: row => formatMoney(row.avgTicket) },
	{
		title: '週成長',
		key: 'deltaPct',
		width: 110,
		align: 'right',
		render: row => h(NTag, {
			size: 'small',
			round: true,
			type: row.deltaPct >= 0 ? 'success' : 'error'
		}, { default: () => `${row.deltaPct >= 0 ? '▲' : '▼'} ${Math.abs(row.deltaPct)}%` })
	},
	{ title: '裝置', key: 'devices', width: 80, align: 'center' },
	{
		title: '狀態',
		key: 'status',
		width: 100,
		render: row => h(NTag, {
			size: 'small',
			round: true,
			type: row.status === 'normal' ? 'success' : row.status === 'warning' ? 'warning' : 'default'
		}, { default: () => row.status === 'normal' ? '正常' : row.status === 'warning' ? '需關注' : '已停業' })
	}
])

const productColumns = computed<DataTableColumns<ProductPerformance>>(() => [
	{ title: '#', key: 'rank', width: 60, render: (_row, idx) => h(NText, { depth: 3 }, { default: () => idx + 1 }) },
	{
		title: '商品',
		key: 'name',
		render: row => h(NFlex, { vertical: true, size: 2 }, {
			default: () => [
				h(NText, { strong: true }, { default: () => row.name }),
				h(NText, { depth: 3, style: 'font-size: 12px;' }, { default: () => row.store })
			]
		})
	},
	{
		title: '分類',
		key: 'category',
		width: 100,
		render: row => h(NTag, { size: 'small', round: true, bordered: false }, { default: () => row.category })
	},
	{ title: '銷售量', key: 'qty', width: 110, align: 'right', render: row => row.qty.toLocaleString() },
	{ title: '營收', key: 'revenue', width: 140, align: 'right', render: row => h(NText, { strong: true }, { default: () => formatMoney(row.revenue) }) },
	{
		title: '佔比',
		key: 'share',
		width: 200,
		render: row => h(NFlex, { align: 'center', size: 8 }, {
			default: () => [
				h(NProgress, {
					type: 'line',
					percentage: Math.min(row.share * 4, 100),
					height: 6,
					color: '#0ea5e9',
					railColor: '#e5e7eb',
					showIndicator: false,
					style: 'flex: 1;'
				}),
				h(NText, { depth: 2, style: 'font-size: 12px; min-width: 42px; text-align: right;' }, { default: () => `${row.share}%` })
			]
		})
	},
	{
		title: '趨勢',
		key: 'trend',
		width: 90,
		align: 'center',
		render: row => h(NTag, {
			size: 'small',
			round: true,
			type: row.trend === 'up' ? 'success' : row.trend === 'down' ? 'error' : 'default'
		}, { default: () => row.trend === 'up' ? '上升' : row.trend === 'down' ? '下滑' : '持平' })
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
              營運報表 Performance
            </n-h2>
            <n-text style="color: rgba(255,255,255,0.7); font-size: 12px;">
              各店面與商品銷售總覽 · 即時掌握集團營運動態
            </n-text>
          </n-flex>
        </n-flex>
        <n-flex align="center" :size="8">
          <n-date-picker
            v-model:value="state.data.dateRange"
            type="daterange"
            clearable
            style="width: 280px;"
          />
          <n-button strong style="background: linear-gradient(135deg, #1e293b, #64748b); color: white;">
            匯出報表
          </n-button>
        </n-flex>
      </n-flex>
    </n-layout-header>

    <n-layout-content style="padding: 24px 32px;">
      <n-grid
        :cols="4"
        :x-gap="16"
        :y-gap="16"
        responsive="screen"
      >
        <n-gi>
          <n-card :bordered="false" style="border-radius: 14px; box-shadow: 0 4px 14px rgba(15,23,42,0.06);">
            <n-statistic
              label="本期總營收"
              :value="totalRevenue"
              :precision="0"
            >
              <template #prefix>
                <n-text depth="3">
                  NT$
                </n-text>
              </template>
              <template #suffix>
                <n-tag
                  size="small"
                  round
                  type="success"
                  style="margin-left: 8px;"
                >
                  ▲ 9.8%
                </n-tag>
              </template>
            </n-statistic>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card :bordered="false" style="border-radius: 14px; box-shadow: 0 4px 14px rgba(15,23,42,0.06);">
            <n-statistic label="總訂單數" :value="totalOrders">
              <template #suffix>
                <n-text depth="3" style="font-size: 12px; margin-left: 4px;">
                  筆
                </n-text>
              </template>
            </n-statistic>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card :bordered="false" style="border-radius: 14px; box-shadow: 0 4px 14px rgba(15,23,42,0.06);">
            <n-statistic label="平均客單價" :value="avgTicket">
              <template #prefix>
                <n-text depth="3">
                  NT$
                </n-text>
              </template>
            </n-statistic>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card :bordered="false" style="border-radius: 14px; box-shadow: 0 4px 14px rgba(15,23,42,0.06);">
            <n-statistic label="活躍店面" :value="activeStores">
              <template #suffix>
                <n-text depth="3" style="font-size: 12px; margin-left: 4px;">
                  / {{ state.data.stores.length }} 間
                </n-text>
              </template>
            </n-statistic>
          </n-card>
        </n-gi>
      </n-grid>

      <n-card :bordered="false" style="margin-top: 20px; border-radius: 14px; box-shadow: 0 4px 14px rgba(15,23,42,0.06);">
        <n-flex
          align="center"
          :size="12"
          style="margin-bottom: 16px;"
        >
          <n-text strong style="font-size: 15px;">
            篩選條件
          </n-text>
          <n-divider vertical />
          <n-select
            v-model:value="state.data.storeFilter"
            :options="state.feature.storeOptions"
            style="width: 220px;"
          />
          <n-select
            v-model:value="state.data.categoryFilter"
            :options="state.feature.categoryOptions"
            style="width: 180px;"
          />
        </n-flex>

        <n-tabs type="line" animated>
          <n-tab-pane name="store" tab="店面表現">
            <n-flex
              justify="space-between"
              align="center"
              style="margin-bottom: 12px;"
            >
              <n-h3 style="margin: 0; font-size: 16px;">
                各店面營收排行
              </n-h3>
              <n-text depth="3" style="font-size: 12px;">
                共 {{ filteredStores.length }} 間
              </n-text>
            </n-flex>
            <n-data-table
              :columns="storeColumns"
              :data="filteredStores"
              :row-key="(row: StorePerformance) => row.store_id"
              :bordered="false"
              striped
              size="medium"
            />
          </n-tab-pane>

          <n-tab-pane name="product" tab="商品表現">
            <n-flex
              justify="space-between"
              align="center"
              style="margin-bottom: 12px;"
            >
              <n-h3 style="margin: 0; font-size: 16px;">
                熱銷商品排行
              </n-h3>
              <n-flex :size="8" align="center">
                <n-badge
                  :value="filteredProducts.length"
                  :max="999"
                  color="#475569"
                />
                <n-text depth="3" style="font-size: 12px;">
                  項商品
                </n-text>
              </n-flex>
            </n-flex>
            <n-empty v-if="filteredProducts.length === 0" description="目前條件下沒有銷售資料" />
            <n-data-table
              v-else
              :columns="productColumns"
              :data="filteredProducts"
              :row-key="(row: ProductPerformance) => row.product_id"
              :bordered="false"
              striped
              size="medium"
            />
          </n-tab-pane>
        </n-tabs>
      </n-card>

      <n-flex justify="center" style="margin-top: 16px;">
        <n-tooltip>
          <template #trigger>
            <n-text depth="3" style="font-size: 12px;">
              資料更新時間：剛剛 · 來源：POS 集中式 ETL
            </n-text>
          </template>
          每 15 分鐘自動同步一次
        </n-tooltip>
      </n-flex>
    </n-layout-content>
  </n-layout>
</template>
