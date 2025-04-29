const { run } = require('runjs')
const chalk = require('chalk')
const config = require('../vue.config.js')
const rawArgv = process.argv.slice(2)
const args = rawArgv.join(' ')

// 默认值
let baseApi = "/prod-api"
let proxyPort = 7318

// 解析命令行参数
rawArgv.forEach(arg => {
  if (arg.startsWith('--baseApi=')) {
    baseApi = arg.split('=')[1]
  } else if (arg.startsWith('--proxyPort=')) {
    proxyPort = parseInt(arg.split('=')[1], 10)
  }
});

// 直接引入 http-proxy-middleware
const proxy = require('http-proxy-middleware');

if (process.env.npm_config_preview || rawArgv.includes('--preview')) {
  const report = rawArgv.includes('--report')

  run(`vue-cli-service build ${args}`)

  const port = 9526
  const publicPath = config.publicPath

  var connect = require('connect')
  var serveStatic = require('serve-static')
  const app = connect()

  // 添加代理中间件
  app.use(proxy(baseApi, {
    target: `http://localhost:${proxyPort}`,
    changeOrigin: true,
    pathRewrite: {
      [`^${baseApi}`]: ''
    }
  }))

  app.use(
    publicPath,
    serveStatic('./dist', {
      index: ['index.html', '/']
    })
  )

  app.listen(port, function () {
    console.log(chalk.green(`> Preview at  http://localhost:${port}${publicPath}`))
    if (report) {
      console.log(chalk.green(`> Report at  http://localhost:${port}${publicPath}report.html`))
    }

  })
} else {
  run(`vue-cli-service build ${args}`)
}
