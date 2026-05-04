/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_BASE: string
  readonly VITE_SERVER: string
  readonly VITE_PORT: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}