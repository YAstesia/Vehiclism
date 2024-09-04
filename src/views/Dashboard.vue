<script setup>
import { useLayout } from '@/layout/composables/layout';
import { ProductService } from '@/service/ProductService';
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
    ProductService.getProductsSmall().then((data) => (products.value = data));
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
                data: [8000, 10000, 15000, 8000],
                barThickness: 32
            },
            {
                type: 'bar',
                label: '混合动力汽车',
                backgroundColor: documentStyle.getPropertyValue('--p-primary-300'),
                data: [2100, 4400, 2400, 3500],
                barThickness: 32
            },
            {
                type: 'bar',
                label: '新能源汽车',
                backgroundColor: documentStyle.getPropertyValue('--p-primary-200'),
                data: [4100, 5200, 6400, 7400],
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
                <div class="font-semibold text-xl mb-4">近期汽车销量</div>
                <DataTable :value="products" :rows="5" :paginator="true" responsiveLayout="scroll">
                    <Column style="width: 15%" header="图片">
                        <template #body="slotProps">
                            <img :src="`https://primefaces.org/cdn/primevue/images/product/${slotProps.data.image}`"
                                :alt="slotProps.data.image" width="50" class="shadow" />
                        </template>
                    </Column>
                    <Column field="name" header="车型" :sortable="true" style="width: 35%"></Column>
                    <Column field="price" header="价格" :sortable="true" style="width: 35%">
                        <template #body="slotProps">
                            {{ formatCurrency(slotProps.data.price) }}
                        </template>
                    </Column>
                    <Column style="width: 15%" header="查看详情">
                        <template #body>
                            <Button icon="pi pi-search" type="button" class="p-button-text" @click="goDetail"></Button>
                        </template>
                    </Column>
                </DataTable>
            </div>
            <div class="card">
                <div class="flex justify-between items-center mb-6">
                    <div class="font-semibold text-xl">当月热销品牌 Top3</div>
                </div>
                <ul class="list-none p-0 m-0">
                    <li class="flex flex-col md:flex-row md:items-center md:justify-between mb-6">
                        <div>
                            <span class="text-surface-900 dark:text-surface-0 font-medium mr-2 mb-1 md:mb-0">Space
                                T-Shirt</span>
                            <div class="mt-1 text-muted-color">Clothing</div>
                        </div>
                        <div class="mt-2 md:mt-0 flex items-center">
                            <div class="bg-surface-300 dark:bg-surface-500 rounded-border overflow-hidden w-40 lg:w-24"
                                style="height: 8px">
                                <div class="bg-orange-500 h-full" style="width: 35%"></div>
                            </div>
                            <span class="text-orange-500 ml-4 font-medium">35%</span>
                        </div>
                    </li>
                    <li class="flex flex-col md:flex-row md:items-center md:justify-between mb-6">
                        <div>
                            <span class="text-surface-900 dark:text-surface-0 font-medium mr-2 mb-1 md:mb-0">Portal
                                Sticker</span>
                            <div class="mt-1 text-muted-color">Accessories</div>
                        </div>
                        <div class="mt-2 md:mt-0 ml-0 md:ml-20 flex items-center">
                            <div class="bg-surface-300 dark:bg-surface-500 rounded-border overflow-hidden w-40 lg:w-24"
                                style="height: 8px">
                                <div class="bg-cyan-500 h-full" style="width: 22%"></div>
                            </div>
                            <span class="text-cyan-500 ml-4 font-medium">22%</span>
                        </div>
                    </li>
                    <li class="flex flex-col md:flex-row md:items-center md:justify-between mb-6">
                        <div>
                            <span class="text-surface-900 dark:text-surface-0 font-medium mr-2 mb-1 md:mb-0">Supernova
                                Sticker</span>
                            <div class="mt-1 text-muted-color">Accessories</div>
                        </div>
                        <div class="mt-2 md:mt-0 ml-0 md:ml-20 flex items-center">
                            <div class="bg-surface-300 dark:bg-surface-500 rounded-border overflow-hidden w-40 lg:w-24"
                                style="height: 8px">
                                <div class="bg-pink-500 h-full" style="width: 14%"></div>
                            </div>
                            <span class="text-pink-500 ml-4 font-medium">14%</span>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
        <div class="col-span-12 xl:col-span-6">
            <div class="card">
                <div class="font-semibold text-xl mb-4">月度销售行情（单位：辆）</div>
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
                            <span class="text-surface-700 dark:text-surface-100 font-bold">2024年6月汽车销量 <span
                                    class="text-primary font-bold"
                                    style="font-size: 22pt; margin-left: 5px; margin-right: 5px;">177.1</span>万辆</span>
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
                            <span class="text-surface-700 dark:text-surface-100 font-bold">2024年6月新能源汽车销量<span
                                    class="text-primary font-bold"
                                    style="font-size: 22pt; margin-left: 5px; margin-right: 5px;">83.1</span>万辆</span>
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
                                    style="font-size: 22pt; margin-left: 5px; margin-right: 5px;">992.5</span>万辆</span>
                        </span>
                    </li>
                </ul>


                <ul class="p-0 m-0 list-none mb-6">
                    <li class="flex items-center py-2 border-b border-surface">
                        <div
                            class="w-12 h-12 flex items-center justify-center bg-green-100 dark:bg-blue-400/10 rounded-full mr-4 shrink-0">
                            <i class="pi pi-dollar !text-xl text-green-500"></i>
                        </div>
                        <span class="text-surface-900 dark:text-surface-0 leading-normal">
                            <span class="text-surface-700 dark:text-surface-100 font-bold">2024年新能源汽车累计销量 <span
                                    class="text-primary font-bold"
                                    style="font-size: 22pt; margin-left: 5px; margin-right: 5px;">405.2</span>万辆</span>
                        </span>
                    </li>
                </ul>

            </div>
        </div>
    </div>
</template>
