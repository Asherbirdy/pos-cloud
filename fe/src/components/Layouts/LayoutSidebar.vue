<script setup lang='ts'>
import { BriefcaseOutline, BusinessOutline, CartOutline, InformationCircleOutline, PeopleOutline, PersonCircleOutline, PersonOutline, ShieldCheckmarkOutline, StatsChartOutline } from '@vicons/ionicons5'
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
    label: 'User',
    key: 'a-user',
    icon: renderIcon(PersonOutline),
    onClick: () => router.push('/A/user')
  },
  {
    label: 'Admin',
    key: 'a-admin',
    icon: renderIcon(ShieldCheckmarkOutline),
    children: [
      {
        label: 'Enterprise',
        key: 'a-admin-enterprise',
        icon: renderIcon(BusinessOutline),
        onClick: () => router.push('/A/admin/enterprise')
      },
      {
        label: 'Info',
        key: 'a-admin-info',
        icon: renderIcon(InformationCircleOutline),
        onClick: () => router.push('/A/admin/info')
      }
    ]
  },
  {
    label: 'Manager',
    key: 'a-manager',
    icon: renderIcon(BriefcaseOutline),
    children: [
      {
        label: 'Account',
        key: 'a-manager-account',
        icon: renderIcon(PersonCircleOutline),
        onClick: () => router.push('/A/manager/account')
      },
      {
        label: 'Performance',
        key: 'a-manager-performance',
        icon: renderIcon(StatsChartOutline),
        onClick: () => router.push('/A/manager/performance')
      }
    ]
  },
  {
    label: 'Staff',
    key: 'a-staff',
    icon: renderIcon(PeopleOutline),
    children: [
      {
        label: 'User',
        key: 'a-staff-user',
        icon: renderIcon(PersonOutline),
        onClick: () => router.push('/A/staff/user')
      },
      {
        label: 'Checkout',
        key: 'a-staff-checkout',
        icon: renderIcon(CartOutline),
        onClick: () => router.push('/A/staff/checkout')
      }
    ]
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
