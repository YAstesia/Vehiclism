import AppLayout from '@/layout/AppLayout.vue';
import { createRouter, createWebHistory } from 'vue-router';

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            name: 'landing',
            component: () => import('@/views/pages/Landing.vue')
        },
        
        {
            path: '/',
            component: AppLayout,
            children: [
                {
                    path: '/cardetail/:series', // 添加参数 :series
                    name: 'cardetail',
                    component: () => import('@/views/pages/CarSeriesDetail.vue')
                },
                // {
                //     path: '/cardetail',
                //     name: 'cardetail',
                //     component: () => import('@/views/pages/CarSeriesDetail.vue')
                // },
                {
                    path: '/typedetail/:tirm',
                    name: 'typedetail',
                    component: () => import('@/views/pages/CarTrimDetail.vue')
                },
                {
                    path: '/pages/profile',
                    name: 'profile',
                    component: () => import('@/views/pages/Profile.vue')
                },
                {
                    path: '/carsearch',
                    name: 'carsearch',
                    component: () => import('@/views/pages/CarSearch.vue')
                },
                {
                    path: '/pricesearch',
                    name: 'pricesearch',
                    component: () => import('@/views/pages/PriceSearch.vue')
                },
                {
                    path: '/salesearch',
                    name: 'salesearch',
                    component: () => import('@/views/pages/SaleSearch.vue')
                },
                {
                    path: '/carcompare',
                    name: 'carcompare',
                    component: () => import('@/views/pages/CarCompare.vue')
                },
                {
                    path: '/hotsales',
                    name: 'hotsales',
                    component: () => import('@/views/pages/HotSales.vue')
                },
                {
                    path: '/pricecompare',
                    name: 'pricecompare',
                    component: () => import('@/views/pages/PriceCompare.vue')
                },
                {
                    path: '/purpose',
                    name: 'purpose',
                    component: () => import('@/views/pages/Purpose.vue')
                },
                {
                    path: '/salesanalysis',
                    name: 'salesanalysis',
                    component: () => import('@/views/pages/SalesAnalysis.vue')
                },
                {
                    path: '/salestrend',
                    name: 'salestrend',
                    component: () => import('@/views/pages/SalesTrend.vue')
                },
                {
                    path: '/dashboard',
                    name: 'dashboard',
                    component: () => import('@/views/Dashboard.vue')
                },
            ]
        },
        {
            path: '/auth/login',
            name: 'login',
            component: () => import('@/views/pages/auth/Login.vue')
        },
        {
            path: '/auth/register',
            name: 'register',
            component: () => import('@/views/pages/auth/Register.vue')
        },
        {
            path: '/auth/access',
            name: 'accessDenied',
            component: () => import('@/views/pages/auth/Access.vue')
        },
        {
            path: '/auth/error',
            name: 'error',
            component: () => import('@/views/pages/auth/Error.vue')
        }
    ]
});

export default router;
