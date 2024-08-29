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
                            { label: '车型查询', icon: 'pi pi-fw pi-search-plus' },
                            { label: '参数配置查询', icon: 'pi pi-fw pi-search-plus' },
                            { label: '价格配置查询', icon: 'pi pi-fw pi-search-plus' },
                            { label: '零售信息查询', icon: 'pi pi-fw pi-search-plus' }
                        ]
                    },
                    {
                        label: '汽车大数据分析',
                        icon: 'pi pi-fw pi-globe',
                        items: [
                            { label: '汽车销售趋势', icon: 'pi pi-fw pi-chart-bar' },
                            { label: '汽车销量分析', icon: 'pi pi-fw pi-chart-bar' },
                            { label: '销售热点分析', icon: 'pi pi-fw pi-chart-bar' },
                            { label: '购车目的分析', icon: 'pi pi-fw pi-chart-bar' },
                            { label: '价格对比分析', icon: 'pi pi-fw pi-chart-bar' },
                            { label: '大数据可视化大屏', icon: 'pi pi-fw pi-chart-bar' }
                        ]
                    },
                    {
                        label: '工具',
                        icon: 'pi pi-fw pi-wrench',
                        items: [
                            { label: '车型比较', icon: 'pi pi-fw pi-comments' }
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
            //{ label: 'Form Layout', icon: 'pi pi-fw pi-id-card', to: '/uikit/formlayout' },
            //{ label: 'Input', icon: 'pi pi-fw pi-check-square', to: '/uikit/input' },
            //{ label: 'Button', icon: 'pi pi-fw pi-mobile', to: '/uikit/button', class: 'rotated-icon' },
            //{ label: 'Table', icon: 'pi pi-fw pi-table', to: '/uikit/table' },
            //{ label: 'List', icon: 'pi pi-fw pi-list', to: '/uikit/list' },
            //{ label: 'Tree', icon: 'pi pi-fw pi-share-alt', to: '/uikit/tree' },
            //{ label: 'Panel', icon: 'pi pi-fw pi-tablet', to: '/uikit/panel' },
            //{ label: 'Overlay', icon: 'pi pi-fw pi-clone', to: '/uikit/overlay' },
            //{ label: 'Media', icon: 'pi pi-fw pi-image', to: '/uikit/media' },
            //{ label: 'Message', icon: 'pi pi-fw pi-comment', to: '/uikit/message' },
            //{ label: 'File', icon: 'pi pi-fw pi-file', to: '/uikit/file' },
            //{ label: 'Chart', icon: 'pi pi-fw pi-chart-bar', to: '/uikit/charts' },
            //{ label: 'Timeline', icon: 'pi pi-fw pi-calendar', to: '/uikit/timeline' },
            //{ label: 'Misc', icon: 'pi pi-fw pi-circle', to: '/uikit/misc' }
        ]
    },
    /*{
        label: '主界面',
        items: [{ label: 'Vehiclism主界面', icon: 'pi pi-fw pi-home', to: '/' }]
    },
    {
        label: '汽车大数据分析',
        icon: 'pi pi-fw pi-briefcase',
        to: '/pages',
        items: [
            {
                label: 'Landing',
                icon: 'pi pi-fw pi-globe',
                to: '/landing'
            },
            {
                label: 'Auth',
                icon: 'pi pi-fw pi-user',
                items: [
                    {
                        label: 'Login',
                        icon: 'pi pi-fw pi-sign-in',
                        to: '/auth/login'
                    },
                    {
                        label: 'Error',
                        icon: 'pi pi-fw pi-times-circle',
                        to: '/auth/error'
                    },
                    {
                        label: 'Access Denied',
                        icon: 'pi pi-fw pi-lock',
                        to: '/auth/access'
                    }
                ]
            },
            {
                label: 'Crud',
                icon: 'pi pi-fw pi-pencil',
                to: '/pages/crud'
            },
            {
                label: 'Not Found',
                icon: 'pi pi-fw pi-exclamation-circle',
                to: '/pages/notfound'
            },
            {
                label: 'Empty',
                icon: 'pi pi-fw pi-circle-off',
                to: '/pages/empty'
            }
        ]
    },
    {
        label: '大数据可视化大屏',
        items: [
            {
                label: 'Submenu 1',
                icon: 'pi pi-fw pi-bookmark',
                items: [
                    {
                        label: 'Submenu 1.1',
                        icon: 'pi pi-fw pi-bookmark',
                        items: [
                            { label: 'Submenu 1.1.1', icon: 'pi pi-fw pi-bookmark' },
                            { label: 'Submenu 1.1.2', icon: 'pi pi-fw pi-bookmark' },
                            { label: 'Submenu 1.1.3', icon: 'pi pi-fw pi-bookmark' }
                        ]
                    },
                    {
                        label: 'Submenu 1.2',
                        icon: 'pi pi-fw pi-bookmark',
                        items: [{ label: 'Submenu 1.2.1', icon: 'pi pi-fw pi-bookmark' }]
                    }
                ]
            },
            {
                label: 'Submenu 2',
                icon: 'pi pi-fw pi-bookmark',
                items: [
                    {
                        label: 'Submenu 2.1',
                        icon: 'pi pi-fw pi-bookmark',
                        items: [
                            { label: 'Submenu 2.1.1', icon: 'pi pi-fw pi-bookmark' },
                            { label: 'Submenu 2.1.2', icon: 'pi pi-fw pi-bookmark' }
                        ]
                    },
                    {
                        label: 'Submenu 2.2',
                        icon: 'pi pi-fw pi-bookmark',
                        items: [{ label: 'Submenu 2.2.1', icon: 'pi pi-fw pi-bookmark' }]
                    }
                ]
            }
        ]
    },*/
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
