<script setup>
import { ref } from 'vue';

import AppMenuItem from './AppMenuItem.vue';

const model = ref([
    {
        label: 'Vehiclism',
        items: [
            { label: '功能导航', icon: 'pi pi-fw pi-bars', 
            items: [
                    {
                        label: '首页',
                        icon: 'pi pi-fw pi-home',
                        to: '/dashboard'
                    },
                    {
                        label: '汽车数据查询',
                        icon: 'pi pi-fw pi-search',
                        items: [
                            { label: '车型查询', icon: 'pi pi-fw pi-search-plus' , to: '/carsearch'},
                            { label: '价格数据查询', icon: 'pi pi-fw pi-search-plus' , to: '/pricesearch'},
                            { label: '销量信息查询', icon: 'pi pi-fw pi-search-plus' , to: '/salesearch'}
                        ]
                    },
                    {
                        label: '汽车大数据分析',
                        icon: 'pi pi-fw pi-globe',
                        items: [
                            { label: '汽车销售趋势', icon: 'pi pi-fw pi-chart-bar' , to: '/salestrend'},
                            { label: '汽车销量分析', icon: 'pi pi-fw pi-chart-bar' , to: '/salesanalysis'},
                            { label: '销售热点分析', icon: 'pi pi-fw pi-chart-bar' , to: '/hotsales'},
                            { label: '购车目的分析', icon: 'pi pi-fw pi-chart-bar' , to: '/purpose'},
                            { label: '价格对比分析', icon: 'pi pi-fw pi-chart-bar' , to: '/pricecompare'},
                            { label: '大数据可视化大屏', icon: 'pi pi-fw pi-chart-bar' }
                        ]
                    },
                    {
                        label: '工具',
                        icon: 'pi pi-fw pi-wrench',
                        items: [
                            { label: '车型比较', icon: 'pi pi-fw pi-comments' , to: '/carcompare'}
                        ]
                    },
                    {
                        label: '我的',
                        icon: 'pi pi-fw pi-user',
                        items: [
                            { label: '个人中心', icon: 'pi pi-fw pi-star' , to: '/uikit/media'}
                        ]
                    },
                ]
            },
        ]
    },
]);
</script>

<template>
    <ul class="layout-menu">
        <template v-for="(item, i) in model" :key="item">
            <app-menu-item v-if="!item.separator" :item="item" :index="i"></app-menu-item>
            <li v-if="item.separator" class="menu-separator"></li>
        </template>
    </ul>
</template>

<style lang="scss" scoped></style>
