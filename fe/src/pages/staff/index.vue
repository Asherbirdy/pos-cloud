<route lang="yaml">
meta:
  layout: false
</route>

<script setup lang="ts">
const router = useRouter()
const message = useMessage()

const STAFF_LIST = [
	{ id: '001', name: '王小明', pin: '1234' },
	{ id: '002', name: '陳美麗', pin: '2345' },
	{ id: '003', name: '林大華', pin: '3456' },
	{ id: '004', name: '張志強', pin: '4567' }
]

const selectedStaff = ref<typeof STAFF_LIST[number] | null>(null)
const pinInput = ref('')
const loading = ref(false)

function selectStaff (staff: typeof STAFF_LIST[number]) {
	selectedStaff.value = staff
	pinInput.value = ''
}

function appendPin (num: string) {
	if (pinInput.value.length >= 4)
		return
	pinInput.value += num
}

function clearPin () {
	pinInput.value = ''
}

function backspacePin () {
	pinInput.value = pinInput.value.slice(0, -1)
}

function login () {
	if (!selectedStaff.value) {
		message.warning('請先選擇員工')
		return
	}
	if (pinInput.value.length !== 4) {
		message.warning('請輸入 4 位數密碼')
		return
	}
	loading.value = true
	setTimeout(() => {
		if (pinInput.value === selectedStaff.value!.pin) {
			message.success(`歡迎 ${selectedStaff.value!.name}`)
			router.push('/')
		}
		else {
			message.error('密碼錯誤')
			pinInput.value = ''
		}
		loading.value = false
	}, 400)
}

const keypad = ['1', '2', '3', '4', '5', '6', '7', '8', '9']
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
    <n-card style="max-width: 720px; width: 100%;" title="員工登入">
      <div
        flex="~ gap-6"
        md="flex-row"
        flex-col
      >
        <div flex-1>
          <div text="sm gray-500" mb-2>
            選擇員工
          </div>
          <div grid="~ cols-2 gap-2">
            <n-button
              v-for="staff in STAFF_LIST"
              :key="staff.id"
              :type="selectedStaff?.id === staff.id ? 'primary' : 'default'"
              size="large"
              block
              @click="selectStaff(staff)"
            >
              {{ staff.name }}
            </n-button>
          </div>
        </div>

        <div flex-1>
          <div text="sm gray-500" mb-2>
            輸入 4 位數密碼
          </div>
          <n-input
            :value="pinInput.replace(/./g, '●')"
            readonly
            size="large"
            placeholder="● ● ● ●"
            mb-3
          />
          <div grid="~ cols-3 gap-2">
            <n-button
              v-for="n in keypad"
              :key="n"
              size="large"
              @click="appendPin(n)"
            >
              {{ n }}
            </n-button>
            <n-button size="large" @click="clearPin">
              清除
            </n-button>
            <n-button size="large" @click="appendPin('0')">
              0
            </n-button>
            <n-button size="large" @click="backspacePin">
              ←
            </n-button>
          </div>
          <n-button
            type="primary"
            size="large"
            block
            mt-4
            :loading="loading"
            @click="login"
          >
            登入
          </n-button>
        </div>
      </div>
    </n-card>
  </div>
</template>
