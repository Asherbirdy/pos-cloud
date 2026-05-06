import { BriefcaseOutline, BusinessOutline, CartOutline, InformationCircleOutline, PeopleOutline, PersonCircleOutline, PersonOutline, ShieldCheckmarkOutline, StatsChartOutline } from '@vicons/ionicons5'
import { NIcon } from 'naive-ui'
import type { MenuOption } from 'naive-ui'
import { defineStore } from 'pinia'
import type { Component } from 'vue'

const renderIcon = (icon: Component) => {
  return () => h(NIcon, null, { default: () => h(icon) })
}

export const useMenuStore = defineStore('menuStore', () => {
  const router = useRouter()

  const state = ref({
    data: {
      menuOptions: [] as MenuOption[]
    },
    feature: {
      inverted: false
    }
  })

  state.value.data.menuOptions = [
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

  const menuOptions = computed(() => state.value.data.menuOptions)
  const inverted = computed(() => state.value.feature.inverted)

  return {
    state,
    menuOptions,
    inverted
  }
})
