<script setup lang="ts">
import {
	NAvatar, NBadge, NButton, NCard, NDivider, NEl, NFlex, NGi, NGrid, NH2, NIcon, NLayout, NLayoutContent, NLayoutHeader, NModal, NSpace, NTag, NText
} from 'naive-ui'

const router = useRouter()
const message = useMessage()

const state = ref({
	data: {
		store: {
			name: '信義旗艦店',
			code: 'XY-001',
			address: '台北市信義區松高路 19 號 3 樓',
			phone: '02-2345-6789',
			businessHours: '10:00 - 22:00',
			manager: '陳美麗'
		},
		staff: {
			name: '王小明',
			role: '收銀員',
			employeeId: 'EMP-2025-014',
			avatar: ''
		},
		shift: {
			status: 'closed' as 'open' | 'closed',
			startedAt: '',
			openingCash: 0,
			currentCash: 0,
			orderCount: 0,
			revenue: 0
		}
	},
	feature: {
		now: new Date(),
		showOpenModal: false,
		showCloseModal: false,
		openLoading: false,
		closeLoading: false,
		storeFields: [
			{ key: 'name', label: '店名', icon: 'M12 2L2 7v2h20V7L12 2zM4 10v9h2v-7h2v7h2v-7h4v7h2v-7h2v7h2v-9H4z', bg: '#eef2ff', color: '#6366f1' },
			{ key: 'address', label: '地址', icon: 'M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z', bg: '#faf5ff', color: '#a855f7' },
			{ key: 'phone', label: '聯絡電話', icon: 'M6.62 10.79c1.44 2.83 3.76 5.14 6.59 6.59l2.2-2.2c.27-.27.67-.36 1.02-.24 1.12.37 2.33.57 3.57.57.55 0 1 .45 1 1V20c0 .55-.45 1-1 1-9.39 0-17-7.61-17-17 0-.55.45-1 1-1h3.5c.55 0 1 .45 1 1 0 1.25.2 2.45.57 3.57.11.35.03.74-.25 1.02l-2.2 2.2z', bg: '#ecfdf5', color: '#10b981' },
			{ key: 'businessHours', label: '營業時間', icon: 'M11.99 2C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8zm.5-13H11v6l5.25 3.15.75-1.23-4.5-2.67z', bg: '#fff7ed', color: '#f97316' },
			{ key: 'manager', label: '店長', icon: 'M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z', bg: '#fdf2f8', color: '#ec4899' }
		] as const
	}
})

const formatTime = (d: Date) => {
	const pad = (n: number) => n.toString().padStart(2, '0')
	return `${d.getFullYear()}/${pad(d.getMonth() + 1)}/${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
}

const formatCurrency = (n: number) => `NT$ ${n.toLocaleString()}`

let timer: ReturnType<typeof setInterval> | null = null

onMounted(() => {
	timer = setInterval(() => {
		state.value.feature.now = new Date()
	}, 1000)
})

onUnmounted(() => {
	if (timer) clearInterval(timer)
})

const openShift = () => {
	state.value.feature.showOpenModal = true
}

const confirmOpenShift = async () => {
	state.value.feature.openLoading = true
	await new Promise(resolve => setTimeout(resolve, 600))
	state.value.data.shift.status = 'open'
	state.value.data.shift.startedAt = formatTime(new Date())
	state.value.data.shift.openingCash = 5000
	state.value.data.shift.currentCash = 5000
	state.value.data.shift.orderCount = 0
	state.value.data.shift.revenue = 0
	state.value.feature.openLoading = false
	state.value.feature.showOpenModal = false
	message.success('開班成功，祝您今天順利！')
}

const closeShift = () => {
	state.value.feature.showCloseModal = true
}

const confirmCloseShift = async () => {
	state.value.feature.closeLoading = true
	await new Promise(resolve => setTimeout(resolve, 600))
	state.value.data.shift.status = 'closed'
	state.value.data.shift.startedAt = ''
	state.value.feature.closeLoading = false
	state.value.feature.showCloseModal = false
	message.success('關班成功，辛苦了！')
}

const logout = () => {
	router.push('/staff')
}
</script>

<template>
  <n-layout style="min-height: 100vh; background: #f9fafb;">
    <n-layout-header
      style="
        background: linear-gradient(135deg, #4f46e5 0%, #9333ea 50%, #ec4899 100%);
        color: white;
        position: relative;
        overflow: hidden;
        padding: 0;
      "
    >
      <n-el
        tag="div"
        style="
          position: absolute;
          inset: 0;
          opacity: 0.2;
          background: radial-gradient(circle at 20% 30%, white, transparent 40%);
          pointer-events: none;
        "
      />
      <n-flex
        justify="space-between"
        align="center"
        :size="16"
        style="position: relative; max-width: 1200px; margin: 0 auto; padding: 32px 24px;"
      >
        <n-flex align="center" :size="16">
          <n-avatar
            round
            :size="64"
            color="rgba(255,255,255,0.2)"
            style="font-size: 24px; font-weight: 600;"
          >
            {{ state.data.staff.name.charAt(0) }}
          </n-avatar>
          <n-flex vertical :size="2">
            <n-text style="color: rgba(255,255,255,0.8); font-size: 12px;">
              歡迎回來
            </n-text>
            <n-text strong style="color: white; font-size: 24px;">
              {{ state.data.staff.name }}
            </n-text>
            <n-flex
              align="center"
              :size="8"
              style="margin-top: 4px;"
            >
              <n-tag
                size="small"
                round
                :bordered="false"
                :color="{ color: 'rgba(255,255,255,0.25)', textColor: 'white' }"
              >
                {{ state.data.staff.role }}
              </n-tag>
              <n-text style="color: rgba(255,255,255,0.7); font-size: 12px;">
                {{ state.data.staff.employeeId }}
              </n-text>
            </n-flex>
          </n-flex>
        </n-flex>

        <n-flex
          vertical
          align="end"
          :size="4"
        >
          <n-text style="color: rgba(255,255,255,0.8); font-size: 12px;">
            目前時間
          </n-text>
          <n-text
            strong
            style="color: white; font-size: 20px; font-family: monospace; letter-spacing: 0.08em;"
          >
            {{ formatTime(state.feature.now) }}
          </n-text>
          <n-button
            quaternary
            size="small"
            color="white"
            @click="logout"
          >
            登出
          </n-button>
        </n-flex>
      </n-flex>
    </n-layout-header>

    <n-layout-content style="background: #f9fafb;">
      <n-flex
        vertical
        :size="24"
        style="max-width: 1200px; margin: 0 auto; padding: 32px 24px; color: #1f2937;"
      >
        <n-card
          :bordered="false"
          size="large"
          style="border-radius: 16px; box-shadow: 0 4px 20px rgba(0,0,0,0.04);"
        >
          <n-flex
            justify="space-between"
            align="center"
            :size="24"
          >
            <n-flex
              vertical
              :size="8"
              style="flex: 1;"
            >
              <n-flex align="center" :size="12">
                <n-h2 style="margin: 0; font-size: 20px; color: #1f2937;">
                  目前班別狀態
                </n-h2>
                <n-badge
                  :color="state.data.shift.status === 'open' ? '#18a058' : '#d03050'"
                  processing
                  dot
                />
                <n-tag
                  :type="state.data.shift.status === 'open' ? 'success' : 'error'"
                  round
                  :bordered="false"
                >
                  {{ state.data.shift.status === 'open' ? '營業中' : '尚未開班' }}
                </n-tag>
              </n-flex>
              <n-text
                v-if="state.data.shift.status === 'open'"
                depth="3"
                style="font-size: 14px;"
              >
                開班時間：{{ state.data.shift.startedAt }}　|　起始現金：{{ formatCurrency(state.data.shift.openingCash) }}
              </n-text>
              <n-text
                v-else
                depth="3"
                style="font-size: 14px;"
              >
                請先點擊「開始上班」開始今日班別。
              </n-text>
            </n-flex>

            <n-space>
              <n-button
                v-if="state.data.shift.status === 'closed'"
                type="primary"
                size="large"
                strong
                round
                style="background: linear-gradient(135deg, #18a058 0%, #36ad6a 100%); padding: 0 32px;"
                @click="openShift"
              >
                <template #icon>
                  <n-icon>
                    <svg viewBox="0 0 24 24" fill="currentColor">
                      <path d="M8 5v14l11-7z" />
                    </svg>
                  </n-icon>
                </template>
                開始上班
              </n-button>
              <n-button
                v-else
                type="error"
                size="large"
                strong
                round
                style="padding: 0 32px;"
                @click="closeShift"
              >
                <template #icon>
                  <n-icon>
                    <svg viewBox="0 0 24 24" fill="currentColor">
                      <path d="M6 6h12v12H6z" />
                    </svg>
                  </n-icon>
                </template>
                結束下班
              </n-button>
            </n-space>
          </n-flex>
        </n-card>

        <n-card
          :bordered="false"
          size="large"
          style="border-radius: 16px; box-shadow: 0 4px 20px rgba(0,0,0,0.04);"
        >
          <n-flex
            justify="space-between"
            align="center"
            style="margin-bottom: 16px;"
          >
            <n-h2 style="margin: 0; font-size: 20px; color: #1f2937;">
              店面資料
            </n-h2>
            <n-tag
              :bordered="false"
              type="info"
              round
            >
              {{ state.data.store.code }}
            </n-tag>
          </n-flex>

          <n-grid
            :cols="1"
            :x-gap="32"
            :y-gap="16"
            responsive="screen"
            item-responsive
          >
            <n-gi
              v-for="field in state.feature.storeFields"
              :key="field.key"
              span="1 m:1"
            >
              <n-flex align="flex-start" :size="12">
                <n-el
                  tag="div"
                  :style="{
                    width: '40px',
                    height: '40px',
                    borderRadius: '8px',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    flexShrink: 0,
                    background: field.bg,
                    color: field.color
                  }"
                >
                  <n-icon size="20">
                    <svg viewBox="0 0 24 24" fill="currentColor">
                      <path :d="field.icon" />
                    </svg>
                  </n-icon>
                </n-el>
                <n-flex vertical :size="2">
                  <n-text depth="3" style="font-size: 12px;">
                    {{ field.label }}
                  </n-text>
                  <n-text strong style="font-size: 16px; color: #1f2937;">
                    {{ state.data.store[field.key] }}
                  </n-text>
                </n-flex>
              </n-flex>
            </n-gi>
          </n-grid>
        </n-card>
      </n-flex>
    </n-layout-content>

    <n-modal
      v-model:show="state.feature.showOpenModal"
      preset="dialog"
      title="確認開始上班？"
      positive-text="確認開班"
      negative-text="取消"
      :loading="state.feature.openLoading"
      @positive-click="confirmOpenShift"
    >
      <n-text depth="2" style="font-size: 14px;">
        將以起始現金
        <n-text strong type="primary">
          {{ formatCurrency(5000) }}
        </n-text>
        開始今日班別。
      </n-text>
      <n-divider style="margin: 12px 0;" />
      <n-text depth="3" style="font-size: 12px;">
        開班後將開始記錄訂單、現金、退款等資料。
      </n-text>
    </n-modal>

    <n-modal
      v-model:show="state.feature.showCloseModal"
      preset="dialog"
      type="warning"
      title="確認結束下班？"
      positive-text="確認關班"
      negative-text="取消"
      :loading="state.feature.closeLoading"
      @positive-click="confirmCloseShift"
    >
      <n-text depth="2" style="font-size: 14px;">
        關班後將結算本班資料並停止接單，是否繼續？
      </n-text>
    </n-modal>
  </n-layout>
</template>
