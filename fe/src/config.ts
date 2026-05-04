interface Config {
  baseUrl: string
  serverUrl: string
}

const config: Config = {
  baseUrl: import.meta.env.VITE_BASE,
  serverUrl: import.meta.env.VITE_SERVER
}

export default config
