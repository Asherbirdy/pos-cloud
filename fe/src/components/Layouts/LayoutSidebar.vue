<script setup lang='ts'>
import { BookOutline } from '@vicons/ionicons5'
import { NIcon, NLayoutSider, NMenu } from 'naive-ui'
import type { MenuOption } from 'naive-ui'
import type { Component } from 'vue'

const router = useRouter()

const renderIcon = (icon: Component) => {
  return () => h(NIcon, null, { default: () => h(icon) })
}

const inverted = ref(false)

const menuOptions: MenuOption[] = [
  {
    label: 'First Page',
    key: 'first-page',
    icon: renderIcon(BookOutline),
    onClick: () => router.push('/')
  },
  {
    label: 'Second Page',
    key: 'second page',
    icon: renderIcon(BookOutline),
    onClick: () => router.push('/second')
  }
]

</script>
<template>
  <n-layout-sider
    bordered
    collapse-mode="width"
    :collapsed-width="64"
    :width="240"
    show-trigger
    :inverted="inverted"
  >
    <n-menu
      :inverted="inverted"
      :collapsed-width="64"
      :collapsed-icon-size="22"
      :options="menuOptions"
    />
  </n-layout-sider>
</template>
