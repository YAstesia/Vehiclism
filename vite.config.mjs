import { fileURLToPath, URL } from 'node:url';

import { PrimeVueResolver } from '@primevue/auto-import-resolver';
import vue from '@vitejs/plugin-vue';
import Components from 'unplugin-vue-components/vite';
import { defineConfig } from 'vite';
import compression from 'vite-plugin-compression';

// https://vitejs.dev/config/
export default defineConfig({
    optimizeDeps: {
        noDiscovery: true
    },
    plugins: [
        compression({
            // 默认开启 gzip 压缩
            algorithm: 'gzip',
            // 也可以设置为 'brotliCompress' 以使用 Brotli 压缩
            // algorithm: 'brotliCompress',
            threshold: 10240, // 只压缩大于 10KB 的文件
            deleteOriginFile: false, // 是否删除原始文件
          }),
        vue(),
        Components({
            resolvers: [PrimeVueResolver()]
        })
    ],
    build: {
        terserOptions: {
          compress: {
            drop_console: true,  // 删除 console.log 语句
            drop_debugger: true  // 删除 debugger 语句
          },
          output: {
            comments: false,  // 删除代码中的注释
          }
        },
        minify: 'terser',  // 使用 Terser 进行压缩
      },
    resolve: {
        alias: {
            '@': fileURLToPath(new URL('./src', import.meta.url))
        }
    }
});
