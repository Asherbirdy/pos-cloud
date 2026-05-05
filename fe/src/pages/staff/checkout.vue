<route lang="yaml">
meta:
  layout: false
</route>

<script setup lang="ts">
import { NAvatar, NBadge, NButton, NCard, NDivider, NEl, NEmpty, NFlex, NGi, NGrid, NH2, NIcon, NInputNumber, NLayout, NLayoutContent, NLayoutHeader, NModal, NScrollbar, NTabPane, NTabs, NTag, NText } from 'naive-ui'

interface Product {
	id: string
	name: string
	price: number
	emoji: string
	category: string
}

interface CartItem {
	product: Product
	qty: number
}

const message = useMessage()

const state = ref({
	data: {
		categories: [
			{ key: 'all', label: '全部' },
			{ key: 'drink', label: '飲料' },
			{ key: 'food', label: '餐點' },
			{ key: 'dessert', label: '甜點' },
			{ key: 'other', label: '其他' }
		] as { key: string; label: string }[],
		products: [
			{ id: 'P001', name: '美式咖啡', price: 80, emoji: '☕', category: 'drink' },
			{ id: 'P002', name: '拿鐵咖啡', price: 110, emoji: '🥛', category: 'drink' },
			{ id: 'P003', name: '抹茶拿鐵', price: 130, emoji: '🍵', category: 'drink' },
			{ id: 'P004', name: '柳橙汁', price: 90, emoji: '🍊', category: 'drink' },
			{ id: 'P005', name: '可樂', price: 50, emoji: '🥤', category: 'drink' },
			{ id: 'P006', name: '經典漢堡', price: 180, emoji: '🍔', category: 'food' },
			{ id: 'P007', name: '雙層起司堡', price: 220, emoji: '🍔', category: 'food' },
			{ id: 'P008', name: '炸雞餐', price: 160, emoji: '🍗', category: 'food' },
			{ id: 'P009', name: '薯條', price: 60, emoji: '🍟', category: 'food' },
			{ id: 'P010', name: '熱狗堡', price: 90, emoji: '🌭', category: 'food' },
			{ id: 'P011', name: '披薩切片', price: 120, emoji: '🍕', category: 'food' },
			{ id: 'P012', name: '巧克力蛋糕', price: 140, emoji: '🍰', category: 'dessert' },
			{ id: 'P013', name: '草莓塔', price: 150, emoji: '🍓', category: 'dessert' },
			{ id: 'P014', name: '冰淇淋', price: 70, emoji: '🍨', category: 'dessert' },
			{ id: 'P015', name: '甜甜圈', price: 50, emoji: '🍩', category: 'dessert' },
			{ id: 'P016', name: '塑膠袋', price: 2, emoji: '🛍️', category: 'other' },
			{ id: 'P017', name: '紙吸管', price: 5, emoji: '🥤', category: 'other' }
		] as Product[],
		cart: [] as CartItem[]
	},
	feature: {
		activeCategory: 'all',
		showPayModal: false,
		paying: false,
		paymentMethod: 'cash' as 'cash' | 'card' | 'mobile'
	}
})

const filteredProducts = computed(() => {
	const cat = state.value.feature.activeCategory
	if (cat === 'all') return state.value.data.products
	return state.value.data.products.filter(p => p.category === cat)
})

const subtotal = computed(() =>
	state.value.data.cart.reduce((sum, it) => sum + it.product.price * it.qty, 0)
)

const totalQty = computed(() =>
	state.value.data.cart.reduce((sum, it) => sum + it.qty, 0)
)

const tax = computed(() => Math.round(subtotal.value * 0.05))
const total = computed(() => subtotal.value + tax.value)

const formatCurrency = (n: number) => `NT$ ${n.toLocaleString()}`

const addToCart = (product: Product) => {
	const item = state.value.data.cart.find(it => it.product.id === product.id)
	if (item) item.qty += 1
	else state.value.data.cart.push({ product, qty: 1 })
}

const updateQty = (id: string, qty: number | null) => {
	const item = state.value.data.cart.find(it => it.product.id === id)
	if (!item) return
	if (!qty || qty <= 0) {
		state.value.data.cart = state.value.data.cart.filter(it => it.product.id !== id)
		return
	}
	item.qty = qty
}

const removeItem = (id: string) => {
	state.value.data.cart = state.value.data.cart.filter(it => it.product.id !== id)
}

const clearCart = () => {
	state.value.data.cart = []
}

const openPay = () => {
	if (state.value.data.cart.length === 0) {
		message.warning('購物車是空的')
		return
	}
	state.value.feature.showPayModal = true
}

const confirmPay = async () => {
	state.value.feature.paying = true
	await new Promise(resolve => setTimeout(resolve, 700))
	state.value.feature.paying = false
	state.value.feature.showPayModal = false
	message.success(`結帳成功！共 ${formatCurrency(total.value)}`)
	state.value.data.cart = []
}

const setPaymentMethod = (m: 'cash' | 'card' | 'mobile') => {
	state.value.feature.paymentMethod = m
}
</script>

<template>
  <n-layout style="height: 100vh; background: #f3f4f6;">
    <n-layout-header
      style="background: white; padding: 16px 24px; box-shadow: 0 1px 3px rgba(0,0,0,0.05);"
    >
      <n-flex justify="space-between" align="center">
        <n-flex align="center" :size="12">
          <n-el
            tag="div"
            style="
              width: 40px; height: 40px; border-radius: 10px;
              background: linear-gradient(135deg, #4f46e5, #9333ea);
              display: flex; align-items: center; justify-content: center;
              color: white; font-weight: 700; font-size: 18px;
            "
          >
            <n-text style="color: white; font-weight: 700;">
              POS
            </n-text>
          </n-el>
          <n-flex vertical :size="0">
            <n-h2 style="margin: 0; font-size: 18px; color: #1f2937;">
              收銀結帳
            </n-h2>
            <n-text depth="3" style="font-size: 12px;">
              信義旗艦店　|　收銀員：王小明
            </n-text>
          </n-flex>
        </n-flex>

        <n-flex align="center" :size="8">
          <n-tag round :bordered="false" type="success">
            營業中
          </n-tag>
          <n-button quaternary @click="$router.push('/staff/user')">
            返回
          </n-button>
        </n-flex>
      </n-flex>
    </n-layout-header>

    <n-layout-content style="height: calc(100vh - 73px); background: #f3f4f6;">
      <n-flex :size="16" :wrap="false" style="height: 100%; padding: 16px;">
        <n-card
          :bordered="false"
          style="flex: 1; border-radius: 16px; box-shadow: 0 4px 20px rgba(0,0,0,0.04); overflow: hidden;"
          content-style="padding: 16px; height: 100%; display: flex; flex-direction: column;"
        >
          <n-tabs
            v-model:value="state.feature.activeCategory"
            type="segment"
            animated
            size="large"
            style="margin-bottom: 16px;"
          >
            <n-tab-pane
              v-for="cat in state.data.categories"
              :key="cat.key"
              :name="cat.key"
              :tab="cat.label"
            />
          </n-tabs>

          <n-scrollbar style="flex: 1;">
            <n-grid :cols="4" :x-gap="12" :y-gap="12" responsive="screen" item-responsive>
              <n-gi v-for="p in filteredProducts" :key="p.id" span="2 s:2 m:3 l:4">
                <n-el
                  tag="div"
                  :style="{
                    cursor: 'pointer',
                    borderRadius: '14px',
                    background: 'white',
                    border: '1px solid #f1f5f9',
                    padding: '16px 12px',
                    transition: 'all 0.15s ease',
                    boxShadow: '0 1px 2px rgba(0,0,0,0.03)'
                  }"
                  class="product-card"
                  @click="addToCart(p)"
                >
                  <n-flex vertical align="center" :size="6">
                    <n-text style="font-size: 36px; line-height: 1;">
                      {{ p.emoji }}
                    </n-text>
                    <n-text strong style="font-size: 14px; color: #1f2937;">
                      {{ p.name }}
                    </n-text>
                    <n-text style="font-size: 14px; color: #4f46e5; font-weight: 600;">
                      {{ formatCurrency(p.price) }}
                    </n-text>
                  </n-flex>
                </n-el>
              </n-gi>
            </n-grid>
          </n-scrollbar>
        </n-card>

        <n-card
          :bordered="false"
          style="width: 400px; flex-shrink: 0; border-radius: 16px; box-shadow: 0 4px 20px rgba(0,0,0,0.04); overflow: hidden;"
          content-style="padding: 0; height: 100%; display: flex; flex-direction: column;"
        >
          <n-flex
            justify="space-between"
            align="center"
            style="padding: 16px 20px; border-bottom: 1px solid #f1f5f9;"
          >
            <n-flex align="center" :size="8">
              <n-icon size="22" color="#4f46e5">
                <svg viewBox="0 0 24 24" fill="currentColor"><path d="M7 18c-1.1 0-1.99.9-1.99 2S5.9 22 7 22s2-.9 2-2-.9-2-2-2zM1 2v2h2l3.6 7.59-1.35 2.45c-.16.28-.25.61-.25.96 0 1.1.9 2 2 2h12v-2H7.42c-.14 0-.25-.11-.25-.25l.03-.12L8.1 13h7.45c.75 0 1.41-.41 1.75-1.03l3.58-6.49c.08-.14.12-.31.12-.48 0-.55-.45-1-1-1H5.21l-.94-2H1zm16 16c-1.1 0-1.99.9-1.99 2s.89 2 1.99 2 2-.9 2-2-.9-2-2-2z" /></svg>
              </n-icon>
              <n-h2 style="margin: 0; font-size: 16px; color: #1f2937;">
                結帳清單
              </n-h2>
              <n-badge :value="totalQty" :max="99" type="info" />
            </n-flex>
            <n-button
              text
              type="error"
              :disabled="state.data.cart.length === 0"
              @click="clearCart"
            >
              清空
            </n-button>
          </n-flex>

          <n-scrollbar style="flex: 1;">
            <n-flex
              v-if="state.data.cart.length === 0"
              align="center"
              justify="center"
              style="height: 100%; min-height: 240px;"
            >
              <n-empty description="尚未選擇商品" />
            </n-flex>

            <n-flex v-else vertical :size="0">
              <n-el
                v-for="item in state.data.cart"
                :key="item.product.id"
                tag="div"
                style="padding: 12px 20px; border-bottom: 1px dashed #f1f5f9;"
              >
                <n-flex align="center" :size="12" :wrap="false">
                  <n-avatar
                    :size="44"
                    color="#f5f5f5"
                    style="font-size: 22px;"
                  >
                    {{ item.product.emoji }}
                  </n-avatar>
                  <n-flex vertical :size="2" style="flex: 1; min-width: 0;">
                    <n-text strong style="font-size: 14px; color: #1f2937;">
                      {{ item.product.name }}
                    </n-text>
                    <n-text depth="3" style="font-size: 12px;">
                      {{ formatCurrency(item.product.price) }} × {{ item.qty }}
                    </n-text>
                  </n-flex>
                  <n-flex vertical align="end" :size="4">
                    <n-text strong style="font-size: 14px; color: #4f46e5;">
                      {{ formatCurrency(item.product.price * item.qty) }}
                    </n-text>
                    <n-input-number
                      :value="item.qty"
                      size="small"
                      :min="0"
                      :max="999"
                      style="width: 110px;"
                      button-placement="both"
                      @update:value="(v) => updateQty(item.product.id, v)"
                    />
                  </n-flex>
                  <n-button
                    text
                    type="error"
                    size="small"
                    @click="removeItem(item.product.id)"
                  >
                    <n-icon size="18">
                      <svg viewBox="0 0 24 24" fill="currentColor"><path d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z" /></svg>
                    </n-icon>
                  </n-button>
                </n-flex>
              </n-el>
            </n-flex>
          </n-scrollbar>

          <n-el
            tag="div"
            style="padding: 16px 20px; background: #fafafa; border-top: 1px solid #f1f5f9;"
          >
            <n-flex vertical :size="6">
              <n-flex justify="space-between">
                <n-text depth="3" style="font-size: 13px;">
                  小計
                </n-text>
                <n-text style="font-size: 13px;">
                  {{ formatCurrency(subtotal) }}
                </n-text>
              </n-flex>
              <n-flex justify="space-between">
                <n-text depth="3" style="font-size: 13px;">
                  稅金 (5%)
                </n-text>
                <n-text style="font-size: 13px;">
                  {{ formatCurrency(tax) }}
                </n-text>
              </n-flex>
              <n-divider style="margin: 6px 0;" />
              <n-flex justify="space-between" align="center">
                <n-text strong style="font-size: 16px; color: #1f2937;">
                  合計
                </n-text>
                <n-text strong style="font-size: 24px; color: #4f46e5;">
                  {{ formatCurrency(total) }}
                </n-text>
              </n-flex>
              <n-button
                type="primary"
                size="large"
                strong
                block
                style="margin-top: 8px; height: 52px; font-size: 16px; background: linear-gradient(135deg, #4f46e5, #9333ea);"
                :disabled="state.data.cart.length === 0"
                @click="openPay"
              >
                <template #icon>
                  <n-icon>
                    <svg viewBox="0 0 24 24" fill="currentColor"><path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z" /></svg>
                  </n-icon>
                </template>
                結帳
              </n-button>
            </n-flex>
          </n-el>
        </n-card>
      </n-flex>
    </n-layout-content>

    <n-modal
      v-model:show="state.feature.showPayModal"
      preset="card"
      title="選擇付款方式"
      style="width: 440px;"
      :bordered="false"
    >
      <n-flex vertical :size="16">
        <n-flex justify="space-between" align="center">
          <n-text depth="3">
            應付金額
          </n-text>
          <n-text strong style="font-size: 28px; color: #4f46e5;">
            {{ formatCurrency(total) }}
          </n-text>
        </n-flex>

        <n-grid :cols="3" :x-gap="12">
          <n-gi>
            <n-el
              tag="div"
              :style="{
                cursor: 'pointer',
                padding: '16px 8px',
                borderRadius: '12px',
                textAlign: 'center',
                border: state.feature.paymentMethod === 'cash' ? '2px solid #4f46e5' : '2px solid #f1f5f9',
                background: state.feature.paymentMethod === 'cash' ? '#eef2ff' : 'white'
              }"
              @click="setPaymentMethod('cash')"
            >
              <n-flex vertical align="center" :size="4">
                <n-text style="font-size: 28px;">
                  💵
                </n-text>
                <n-text strong style="font-size: 13px;">
                  現金
                </n-text>
              </n-flex>
            </n-el>
          </n-gi>
          <n-gi>
            <n-el
              tag="div"
              :style="{
                cursor: 'pointer',
                padding: '16px 8px',
                borderRadius: '12px',
                textAlign: 'center',
                border: state.feature.paymentMethod === 'card' ? '2px solid #4f46e5' : '2px solid #f1f5f9',
                background: state.feature.paymentMethod === 'card' ? '#eef2ff' : 'white'
              }"
              @click="setPaymentMethod('card')"
            >
              <n-flex vertical align="center" :size="4">
                <n-text style="font-size: 28px;">
                  💳
                </n-text>
                <n-text strong style="font-size: 13px;">
                  信用卡
                </n-text>
              </n-flex>
            </n-el>
          </n-gi>
          <n-gi>
            <n-el
              tag="div"
              :style="{
                cursor: 'pointer',
                padding: '16px 8px',
                borderRadius: '12px',
                textAlign: 'center',
                border: state.feature.paymentMethod === 'mobile' ? '2px solid #4f46e5' : '2px solid #f1f5f9',
                background: state.feature.paymentMethod === 'mobile' ? '#eef2ff' : 'white'
              }"
              @click="setPaymentMethod('mobile')"
            >
              <n-flex vertical align="center" :size="4">
                <n-text style="font-size: 28px;">
                  📱
                </n-text>
                <n-text strong style="font-size: 13px;">
                  行動支付
                </n-text>
              </n-flex>
            </n-el>
          </n-gi>
        </n-grid>

        <n-flex justify="end" :size="8">
          <n-button @click="state.feature.showPayModal = false">
            取消
          </n-button>
          <n-button
            type="primary"
            :loading="state.feature.paying"
            style="background: linear-gradient(135deg, #4f46e5, #9333ea);"
            @click="confirmPay"
          >
            確認付款
          </n-button>
        </n-flex>
      </n-flex>
    </n-modal>
  </n-layout>
</template>

<style scoped>
.product-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(79, 70, 229, 0.12) !important;
  border-color: #c7d2fe !important;
}
.product-card:active {
  transform: translateY(0);
}
</style>
