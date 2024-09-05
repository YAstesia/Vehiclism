<script setup>
import { getBrandSaleMonth, getBrandSaleYear, getSeriesSaleMonth, getSeriesSaleYear } from '@/api';
import { useLayout } from '@/layout/composables/layout';
import { onMounted, ref, watch } from 'vue';


const { getPrimary, getSurface, isDarkTheme } = useLayout();
const barData = ref(null);
const barData2 = ref(null);
const barData3 = ref(null);
const barData4 = ref(null);
const barOptions = ref(null);
onMounted(() => {
    setColorOptions();
});

const loading = ref([false, false, false]);

function load(index) {
    loading.value[index] = true;
    setTimeout(() => (loading.value[index] = false), 1000);
}

function setColorOptions() {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');

    // barData.value = {
    //     labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
    //     datasets: [
    //         {
    //             label: 'My First dataset',
    //             backgroundColor: documentStyle.getPropertyValue('--p-primary-500'),
    //             borderColor: documentStyle.getPropertyValue('--p-primary-500'),
    //             data: [65, 59, 80, 81, 56, 55, 40]
    //         },
    //         {
    //             label: 'My Second dataset',
    //             backgroundColor: documentStyle.getPropertyValue('--p-primary-200'),
    //             borderColor: documentStyle.getPropertyValue('--p-primary-200'),
    //             data: [28, 48, 40, 19, 86, 27, 90]
    //         }
    //     ]
    // };
    barOptions.value = {
        plugins: {
            legend: {
                labels: {
                    fontColor: textColor
                }
            }
        },
        scales: {
            x: {
                ticks: {
                    color: textColorSecondary,
                    font: {
                        weight: 500
                    }
                },
                grid: {
                    display: false,
                    drawBorder: false
                }
            },
            y: {
                ticks: {
                    color: textColorSecondary
                },
                grid: {
                    color: surfaceBorder,
                    drawBorder: false
                }
            }
        }
    };
}

const dropdownValues = ref([
    // { name: '2015', code: 2015 },
    // { name: '2016', code: 2016 },
    // { name: '2017', code: 2017 },
    // { name: '2018', code: 2018 },
    { name: '2019', code: 2019 },
    { name: '2020', code: 2020 },
    { name: '2021', code: 2021 },
    { name: '2022', code: 2022 },
    { name: '2023', code: 2023 },
    { name: '2024', code: 2024 },
]);
const dropdownValue = ref(null);

const dropdownValues2 = ref([
    { name: '一月', code: 1 },
    { name: '二月', code: 2 },
    { name: '三月', code: 3 },
    { name: '四月', code: 4 },
    { name: '五月', code: 5 },
    { name: '六月', code: 6 },
    { name: '七月', code: 7 },
    { name: '八月', code: 8 },
    { name: '九月', code: 9 },
    { name: '十月', code: 10 },
    { name: '十一月', code: 11 },
    { name: '十二月', code: 12 },
]);
const dropdownValue2 = ref(null);
async function handleSearch() {
    loading.value[1] = true;
    try {
        const year = dropdownValue.value?.code || null;
        const month = dropdownValue2.value?.code || null;

        let yearlyData = [];
        let monthlyData = [];
        let yearlySeriesData = [];
        let monthlySeriesData = [];

        if (year) {
            yearlyData = await getBrandSaleYear(year);
            yearlyData = yearlyData.data.data;
            yearlySeriesData = await getSeriesSaleYear(year);
            yearlySeriesData = yearlySeriesData.data.data;
        }

        if (month && year) {
            monthlyData = await getBrandSaleMonth(year, month);
            monthlyData = monthlyData.data.data;
            monthlySeriesData = await getSeriesSaleMonth(year, month);
            monthlySeriesData = monthlySeriesData.data.data;
        }

        updateChartData(yearlyData, monthlyData, yearlySeriesData, monthlySeriesData);
    } catch (error) {
        console.error("Error fetching data: ", error);
    } finally {
        loading.value[1] = false;
    }
}

function updateChartData(yearlyData, monthlyData, yearlySeriesData, monthlySeriesData) {
    barData.value = formatBarData(yearlyData);
    barData2.value = formatBarData(monthlyData);
    barData3.value = formatBarDataSeries(yearlySeriesData);
    barData4.value = formatBarDataSeries(monthlySeriesData);

    // barData3.value = formatBarData3(monthlyData);
}
function formatBarData(data) {
    return {
        labels: data.map(item => item.brand),
        datasets: [
            {
                label: '销量',
                backgroundColor: ["#e95938", "#ff6519", "#f28104", "#ebaf26", "#d4b640", "#b6b34f", "#94a949", "#729a19", "#628f00", "#339a33"],
                borderColor: getComputedStyle(document.documentElement).getPropertyValue('--p-primary-500'),
                data: data.map(item => item.totalSale),
                ranking: data.map(item => item.ranking)
            }
        ]
    };
}
function formatBarDataSeries(data) {
    return {
        labels: data.map(item => item.series),
        datasets: [
            {
                label: '销量',
                backgroundColor: ["#e95938", "#ff6519", "#f28104", "#ebaf26", "#d4b640", "#b6b34f", "#94a949", "#729a19", "#628f00", "#339a33"],
                borderColor: getComputedStyle(document.documentElement).getPropertyValue('--p-primary-500'),
                data: data.map(item => item.totalSale),
                ranking: data.map(item => item.ranking)
            }
        ]
    };
}
watch(
    [getPrimary, getSurface, isDarkTheme],
    () => {
        setColorOptions();
    },
    { immediate: true }
);
</script>

<template>
    <div class="card">
        <IconField iconPosition="left">
            <div class="font-semibold text-xl mb-4">汽车销售排行榜</div>
            <Select v-model="dropdownValue" :options="dropdownValues" optionLabel="name" placeholder="选择年份"
                style="margin-bottom: 20px; margin-right: 20px;" />
            <Select v-model="dropdownValue2" :options="dropdownValues2" optionLabel="name" placeholder="选择月份"
                style="margin-bottom: 20px; margin-right: 20px;" />
            <Button type="button" class="mr-2 mb-2" label="查询" icon="pi pi-search" iconPos="right" :loading="loading[1]"
                @click="handleSearch" style="margin-left: 20px;" />
        </IconField>
    </div>
    <Fluid class="grid grid-cols-12 gap-8">
        <div class="col-span-12 xl:col-span-6">
            <div class="card">
                <div class="font-semibold text-xl mb-4">年度品牌销售排行</div>
                <Chart type="bar" :data="barData" :options="barOptions"></Chart>
            </div>
        </div>
        <div class="col-span-12 xl:col-span-6">
            <div class="card">
                <div class="font-semibold text-xl mb-4">年度车系销售排行</div>
                <Chart type="bar" :data="barData3" :options="barOptions"></Chart>
            </div>
        </div>
        <div class="col-span-12 xl:col-span-6">
            <div class="card">
                <div class="font-semibold text-xl mb-4">月度品牌销售排行</div>
                <Chart type="bar" :data="barData2" :options="barOptions"></Chart>
            </div>
        </div>
        <div class="col-span-12 xl:col-span-6">
            <div class="card">
                <div class="font-semibold text-xl mb-4">月度车系销售排行</div>
                <Chart type="bar" :data="barData4" :options="barOptions"></Chart>
            </div>
        </div>
    </Fluid>
</template>
