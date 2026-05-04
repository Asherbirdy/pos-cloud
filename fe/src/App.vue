<script setup lang="ts">
import { NConfigProvider, NDialogProvider, NMessageProvider, NNotificationProvider } from 'naive-ui'

import themeOverides from '@/theme'

</script>
<template>
  <n-config-provider :theme-overrides="themeOverides">
    <n-dialog-provider>
      <n-notification-provider>
        <n-message-provider>
          <RouterView />
        </n-message-provider>
      </n-notification-provider>
    </n-dialog-provider>
  </n-config-provider>
</template>
