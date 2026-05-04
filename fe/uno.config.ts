import { defineConfig } from 'unocss'
import type { UserConfig } from 'unocss'

export default defineConfig({
  content: {
    pipeline: {
      exclude: ['node_modules', 'dist', '.git', 'public', 'build']
    }
  },
  /* shortcuts to multiple utilities */
  shortcuts: {
    'flex-center': 'flex items-center justify-center'
  },
  theme: {
    // 因為與 presets 的規則衝突，所以 'custom_dark' 不可取名為 'custom-dark'
    colors: {
      primary: '#32457B',
      secondary: '#4D9CFF',
      third: '#2471FB',
      commonBg: '#E8F2FF'
    },
    fontSize: {
      h1: '32px',
      h2: '24px',
      h3: '20px',
      h4: '18px',
      h5: '16px',
      h6: '14px'
    },
    breakpoints: {
      sm: '360px',
      md: '768px',
      lg: '1024px',
      xl: '1280px',
      xxl: '1440px'
    }
  }
} as UserConfig)