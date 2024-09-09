/** @type {import('tailwindcss').Config} */
module.exports = {
    darkMode: ['selector', '[class*="app-dark"]'],
    content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
    plugins: [require('tailwindcss-primeui')],
    theme: {
        screens: {
            sm: '576px',
            md: '768px',
            lg: '992px',
            xl: '1200px',
            '2xl': '1920px'
        }
    },
    configureWebpack: {
        optimization: {
          splitChunks: {
            chunks: 'all', // 处理所有类型的代码块，包括异步和同步
            minSize: 20000, // 只有大于这个大小的模块才会被分割
            maxSize: 0, // 如果设置为非零值，则会对分割块进行最大大小限制
            minChunks: 1, // 模块至少被引用的次数
            maxAsyncRequests: 30, // 最大异步请求数
            maxInitialRequests: 30, // 最大初始请求数
            automaticNameDelimiter: '~', // 文件名中使用的分隔符
            name: true, // 生成文件名
            cacheGroups: {
              vendors: {
                test: /[\\/]node_modules[\\/]/,
                priority: -10,
                reuseExistingChunk: true,
              },
              default: {
                minChunks: 2,
                priority: -20,
                reuseExistingChunk: true,
                enforce: true,
              },
            },
          },
        },
      }
};
