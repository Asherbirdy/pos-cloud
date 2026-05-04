<script setup lang="ts">
import { NLayout } from 'naive-ui'
import { RouterView } from 'vue-router'

</script>

<template>
  <div>
    <n-layout>
      <LayoutsHeader />
    </n-layout>
    <n-layout has-sider>
      <LayoutSidebar />
      <router-view
        v-slot="{ Component }"
        class="w-full"
      >
        <transition name="pagefade">
          <component :is="Component" />
        </transition>
      </router-view>
    </n-layout>
  </div>
</template>