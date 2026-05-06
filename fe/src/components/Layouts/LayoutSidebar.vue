<script setup lang='ts'>
import { NFlex, NH2, NLayoutSider, NMenu } from 'naive-ui'

import { useMenuStore } from '@/stores'

const menuStore = useMenuStore()
</script>
<template>
  <n-layout-sider
    bordered
    collapse-mode="width"
    :collapsed-width="64"
    :width="240"
    show-trigger
    :inverted="menuStore.inverted"
  >
    <n-flex
      align="center"
      justify="start"
      style="height: 64px; padding: 0 16px;"
    >
      <n-h2 style="margin: 0; font-weight: 600;">
        logo
      </n-h2>
    </n-flex>
    <n-menu
      :inverted="menuStore.inverted"
      :collapsed-width="64"
      :collapsed-icon-size="22"
      :options="menuStore.menuOptions"
    />
  </n-layout-sider>
</template>
