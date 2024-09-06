<script setup>
import { useLayout } from '@/layout/composables/layout';
import { DashboardService } from '@/service/DashboardService';
import { onMounted, ref, watch } from 'vue';
import { useRouter } from 'vue-router';
const { getPrimary, getSurface, isDarkTheme } = useLayout();

const products = ref(null);
const chartData = ref(null);
const chartOptions = ref(null);
const router = useRouter();

const items = ref([
    { label: 'Add New', icon: 'pi pi-fw pi-plus' },
    { label: 'Remove', icon: 'pi pi-fw pi-trash' }
]);

onMounted(() => {
    DashboardService.getProductsSmall().then((data) => (products.value = data));
    chartData.value = setChartData();
    chartOptions.value = setChartOptions();
});

function setChartData() {
    const documentStyle = getComputedStyle(document.documentElement);

    return {
        labels: ['4月', '5月', '6月', '7月'],
        datasets: [
            {
                type: 'bar',
                label: '燃料电池汽车',
                backgroundColor: documentStyle.getPropertyValue('--p-primary-400'),
                data: [85.8, 86.3, 91, 84],
                barThickness: 32
            },
            {
                type: 'bar',
                label: '混合动力汽车',
                backgroundColor: documentStyle.getPropertyValue('--p-primary-300'),
                data: [33.1, 29.9, 43.6, 40],
                barThickness: 32
            },
            {
                type: 'bar',
                label: '新能源汽车',
                backgroundColor: documentStyle.getPropertyValue('--p-primary-200'),
                data: [85, 95.5, 104.9, 99.1],
                borderRadius: {
                    topLeft: 8,
                    topRight: 8
                },
                borderSkipped: true,
                barThickness: 32
            }
        ]
    };
}

function setChartOptions() {
    const documentStyle = getComputedStyle(document.documentElement);
    const borderColor = documentStyle.getPropertyValue('--surface-border');
    const textMutedColor = documentStyle.getPropertyValue('--text-color-secondary');

    return {
        maintainAspectRatio: false,
        aspectRatio: 0.8,
        scales: {
            x: {
                stacked: true,
                ticks: {
                    color: textMutedColor
                },
                grid: {
                    color: 'transparent',
                    borderColor: 'transparent'
                }
            },
            y: {
                stacked: true,
                ticks: {

                    color: textMutedColor
                },
                grid: {
                    color: borderColor,
                    borderColor: 'transparent',
                    drawTicks: false
                }
            }
        }
    };
}

const formatCurrency = (value) => {
    return value.toLocaleString('en-US', { style: 'currency', currency: 'USD' });
};

const goDetail = () => {
    router.push('/cardetail');
};

watch([getPrimary, getSurface, isDarkTheme], () => {
    chartData.value = setChartData();
    chartOptions.value = setChartOptions();
});
</script>

<template>
    <div class="grid grid-cols-12 gap-8">
        <div class="col-span-12 xl:col-span-6">
            <div class="card">
                <div class="font-semibold text-xl mb-4">当月热销车系</div>
                <DataTable :value="products" :rows="5" :paginator="true" responsiveLayout="scroll">
                    <Column field="rank" header="排名" :sortable="false" style="width: 15%">
                        <template #body="slotProps">
                            <span class="font-bold" style="font-size: 14pt;">{{ slotProps.data.rank }}</span>
                        </template>
                    </Column>
                    <Column field="series" header="品牌" :sortable="false" style="width: 25%">
                        <template #body="slotProps">
                            <span class="font-semibold" style="font-size: 12pt;">{{ slotProps.data.series }}</span>
                        </template>
                    </Column>
                    <Column field="type" header="车系" :sortable="false" style="width: 30%">
                        <template #body="slotProps">
                            <span class="font-bold" style="font-size: 12pt;">{{ slotProps.data.type }}</span>
                        </template>
                    </Column>
                    <Column field="sales" header="月销量" :sortable="false" style="width: 30%">
                        <template #body="slotProps">
                            <span class="font-bold" style="font-size: 14pt; color: crimson;">{{ slotProps.data.sales
                                }}</span>
                        </template>
                    </Column>
                </DataTable>
            </div>
            <div class="card">
                <div class="flex justify-between items-center mb-6">
                    <div class="font-semibold text-xl">热销品牌</div>
                    <div class="font-semibold text-xl">年销量</div>
                    <div class="font-semibold text-xl">当月销量</div>
                </div>
                <ul class="list-none p-0 m-0">
                    <li class="flex flex-col md:flex-row md:items-center md:justify-between mb-6">
                        <div>
                            <span class="text-surface-900 dark:text-surface-0 mr-2 mb-1 md:mb-0 font-bold"
                                style="font-size: 14pt;">比亚迪</span>
                            <div class="mt-1 text-muted-color">月销量No.1</div>
                        </div>
                        <div class="mt-2 md:mt-0 flex justify-center items-center h-full">
                            <span class="text-red-500 ml-4 font-bold" style="font-size: 20pt;">1601022</span>
                        </div>
                        <div class="mt-2 md:mt-0 flex items-center">
                            <span class="text-red-500 ml-4 font-bold" style="font-size: 20pt;">298893</span>
                        </div>
                    </li>
                    <li class="flex flex-col md:flex-row md:items-center md:justify-between mb-6">
                        <div>
                            <span class="text-surface-900 dark:text-surface-0 font-bold mr-2 mb-1 md:mb-0"
                                style="font-size: 14pt;">大众</span>
                            <div class="mt-1 text-muted-color">月销量No.2</div>
                        </div>
                        <div class="mt-2 md:mt-0 ml-0   flex justify-center items-center h-full">
                            <span class="text-red-400 ml-4 font-bold" style="font-size: 20pt;">1069775</span>
                        </div>
                        <div class="mt-2 md:mt-0 ml-0   flex items-center">
                            <span class="text-red-400 ml-4 font-bold" style="font-size: 20pt;">149007</span>
                        </div>
                    </li>
                    <li class="flex flex-col md:flex-row md:items-center md:justify-between mb-6">
                        <div>
                            <span class="text-surface-900 dark:text-surface-0 font-bold mr-2 mb-1 md:mb-0"
                                style="font-size: 14pt;">丰田</span>
                            <div class="mt-1 text-muted-color">月销量No.3</div>
                        </div>
                        <div class="mt-2 md:mt-0 ml-0   flex justify-center items-center h-full">
                            <span class="text-orange-400 ml-4 font-bold"
                                style="font-size: 20pt; margin-right: -15px;">810968</span>
                        </div>
                        <div class="mt-2 md:mt-0 ml-0   flex items-center">
                            <span class="text-orange-400 ml-4 font-bold" style="font-size: 20pt;">125027</span>
                        </div>
                    </li>
                    <li class="flex flex-col md:flex-row md:items-center md:justify-between mb-6">
                        <div>
                            <span class="text-surface-900 dark:text-surface-0 font-bold mr-2 mb-1 md:mb-0"
                                style="font-size: 14pt;">吉利汽车</span>
                            <div class="mt-1 text-muted-color">月销量No.4</div>
                        </div>
                        <div class="mt-2 md:mt-0 ml-0   flex justify-center items-center h-full">
                            <span class="text-yellow-500 ml-4 font-bold"
                                style="font-size: 20pt; margin-right: 5px;">509588</span>
                        </div>
                        <div class="mt-2 md:mt-0 ml-0   flex items-center">
                            <span class="text-yellow-500 ml-4 font-bold" style="font-size: 20pt;">63458</span>
                        </div>
                    </li>
                    <li class="flex flex-col md:flex-row md:items-center md:justify-between mb-6">
                        <div>
                            <span class="text-surface-900 dark:text-surface-0 font-bold mr-2 mb-1 md:mb-0"
                                style="font-size: 14pt;">本田</span>
                            <div class="mt-1 text-muted-color">月销量No.7</div>
                        </div>
                        <div class="mt-2 md:mt-0 ml-0 flex justify-center items-center h-full">
                            <span class="text-green-500 font-bold" style="font-size: 20pt;">498106</span>
                        </div>
                        <div class="mt-2 md:mt-0 ml-0 flex items-center">
                            <span class="text-green-500 font-bold" style="font-size: 20pt;">51487</span>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
        <div class="col-span-12 xl:col-span-6">
            <div class="card">
                <div class="font-semibold text-xl mb-4">月度销售行情（单位：万辆）</div>
                <Chart type="bar" :data="chartData" :options="chartOptions" class="h-80" />
            </div>
            <div class="card">
                <div class="flex items-center justify-between mb-6">
                    <div class="font-semibold text-xl">最新汽车销量数据</div>
                </div>

                <ul class="p-0 m-0 list-none mb-6">
                    <li class="flex items-center py-2 border-b border-surface">
                        <div
                            class="w-12 h-12 flex items-center justify-center bg-blue-100 dark:bg-blue-400/10 rounded-full mr-4 shrink-0">
                            <i class="pi pi-dollar !text-xl text-blue-500"></i>
                        </div>
                        <span class="text-surface-900 dark:text-surface-0 leading-normal">
                            <span class="text-surface-700 dark:text-surface-100 font-bold">2024年7月汽车销量 <span
                                    class="text-primary font-bold"
                                    style="font-size: 22pt; margin-left: 5px; margin-right: 5px;">226.2</span>万辆</span>
                        </span>
                    </li>
                </ul>

                <ul class="p-0 m-0 list-none mb-6">
                    <li class="flex items-center py-2 border-b border-surface">
                        <div
                            class="w-12 h-12 flex items-center justify-center bg-pink-100 dark:bg-pink-400/10 rounded-full mr-4 shrink-0">
                            <i class="pi pi-dollar !text-xl text-pink-500"></i>
                        </div>
                        <span class="text-surface-900 dark:text-surface-0 leading-normal">
                            <span class="text-surface-700 dark:text-surface-100 font-bold">2024年7月新能源汽车销量<span
                                    class="text-primary font-bold"
                                    style="font-size: 22pt; margin-left: 5px; margin-right: 5px;">99.1</span>万辆</span>
                        </span>
                    </li>
                </ul>

                <ul class="p-0 m-0 list-none mb-6">
                    <li class="flex items-center py-2 border-b border-surface">
                        <div
                            class="w-12 h-12 flex items-center justify-center bg-red-100 dark:bg-blue-400/10 rounded-full mr-4 shrink-0">
                            <i class="pi pi-dollar !text-xl text-red-500"></i>
                        </div>
                        <span class="text-surface-900 dark:text-surface-0 leading-normal">
                            <span class="text-surface-700 dark:text-surface-100 font-bold">2024年汽车累计销量 <span
                                    class="text-primary font-bold"
                                    style="font-size: 22pt; margin-left: 5px; margin-right: 5px;">1631</span>万辆</span>
                        </span>
                    </li>
                </ul>


                <ul class="p-0 m-0 list-none mb-4">
                    <li class=" flex items-center py-2 border-b border-surface">
                        <div
                            class="w-12 h-12 flex items-center justify-center bg-green-100 dark:bg-blue-400/10 rounded-full mr-4 shrink-0">
                            <i class="pi pi-dollar !text-xl text-green-500"></i>
                        </div>
                        <span class="text-surface-900 dark:text-surface-0 leading-normal">
                            <span class="text-surface-700 dark:text-surface-100 font-bold">2024年新能源汽车累计销量 <span
                                    class="text-primary font-bold"
                                    style="font-size: 22pt; margin-left: 5px; margin-right: 5px;">593.4</span>万辆</span>
                        </span>
                    </li>
                </ul>
            </div>
            <div class="card" style="margin-top: -20px;">
                <li class="flex items-center border-b border-surface">
                    <div
                        class="w-12 h-12 flex items-center justify-center bg-blue-100 dark:bg-blue-400/10 rounded-full mr-4 shrink-0">
                        <i class="pi pi-car !text-xl text-blue-500"></i>
                    </div>
                    <span class="text-surface-900 dark:text-surface-0 leading-normal">
                        <span class="text-surface-700 dark:text-surface-100 font-bold">本网站共统计汽车类型信息 <span
                                class="text-primary font-bold"
                                style="font-size: 22pt; margin-left: 5px; margin-right: 5px; color: crimson;">12765</span>条</span>
                    </span>
                </li>
            </div>
        </div>
    </div>
</template>
