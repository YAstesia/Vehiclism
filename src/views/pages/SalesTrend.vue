<script setup>
import { fetchMonthData, fetchSalesData } from '@/api';
import { useLayout } from '@/layout/composables/layout';
import { onMounted, ref, watch } from 'vue';

const { getPrimary, getSurface, isDarkTheme } = useLayout();
const lineData = ref(null);
const barData = ref(null);
const lineData2 = ref(null);
const barData2 = ref(null);
const lineOptions = ref(null);
const barOptions = ref(null);
const dropdownValues = ref([
    { name: '2015', code: 2015 },
    { name: '2016', code: 2016 },
    { name: '2017', code: 2017 },
    { name: '2018', code: 2018 },
    { name: '2019', code: 2019 },
    { name: '2020', code: 2020 },
    { name: '2021', code: 2021 },
    { name: '2022', code: 2022 },
    { name: '2023', code: 2023 },
    { name: '2024', code: 2024 },
]);
const dropdownValue = ref(null);

onMounted(async () => {
    const salesData = await fetchSalesData();
    
    lineData.value = {
        labels: salesData.map(item => item.year), // 从数据中提取年份
        datasets: [
            {
                label: '(单位：销量)',
                data: salesData.map(item => item.sales), // 从数据中提取销量
                fill: false,
                backgroundColor: '#42A5F5',
                borderColor: '#42A5F5',
                tension: 0.4
            }
        ]
    };

    barData.value = {
        labels: salesData.map(item => item.year),
        datasets: [
            {
                label: '(单位：销量)',
                backgroundColor: '#42A5F5',
                borderColor: '#42A5F5',
                data: salesData.map(item => item.sales)
            }
        ]
    };

    setColorOptions(); // 调用 setColorOptions 来设置样式
});

async function updateChartData(year) {
    try {
        const salesData2 = await fetchMonthData(year);
        
        // 更新折线图数据
        lineData2.value = {
            labels: salesData2.map(item => item.month),
            datasets: [
                {
                    label: '月销售额',
                    data: salesData2.map(item => item.sales),
                    fill: false,
                    backgroundColor: '#42A5F5',
                    borderColor: '#42A5F5',
                    tension: 0.4
                }
            ]
        };

        // 更新柱状图数据
        barData2.value = {
            labels: salesData2.map(item => item.month),
            datasets: [
                {
                    label: '月销售额',
                    backgroundColor: '#42A5F5',
                    borderColor: '#42A5F5',
                    data: salesData2.map(item => item.sales)
                }
            ]
        };
    } catch (error) {
        console.error('Error updating chart data:', error.message);
    }
}

function setColorOptions() {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');

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

    lineOptions.value = {
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
                    color: textColorSecondary
                },
                grid: {
                    color: surfaceBorder,
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

watch(dropdownValue, (newValue) => {
    if (newValue) {
        updateChartData(newValue.code); // 使用选中的年份更新图表数据
    }
}, { immediate: true });


watch(
    [getPrimary, getSurface, isDarkTheme],
    () => {
        setColorOptions();
    },
    { immediate: true }
);
</script>

<template>
    <Fluid class="grid grid-cols-12 gap-8">
        <div class="col-span-12 xl:col-span-6">
            <div class="card">
                <div class="font-semibold text-xl mb-4">汽车年销量统计（折线图）</div>
                <Chart type="line" :data="lineData" :options="lineOptions"></Chart>
            </div>
        </div>
        <div class="col-span-12 xl:col-span-6">
            <div class="card">
                <Select v-model="dropdownValue" :options="dropdownValues" optionLabel="name" placeholder="选择年份" style="margin-bottom: 20px;"/>
                <div class="font-semibold text-xl mb-4">汽车月销量统计（折线图）</div>
                <Chart type="line" :data="lineData2" :options="lineOptions"></Chart>
            </div>
        </div>
        <div class="col-span-12 xl:col-span-6">
            <div class="card">
                <div class="font-semibold text-xl mb-4">汽车年销量统计（柱状图）</div>
                <Chart type="bar" :data="barData" :options="barOptions"></Chart>
            </div>
        </div>
        <div class="col-span-12 xl:col-span-6">
            <div class="card">
                <div class="font-semibold text-xl mb-4">汽车月销量统计（柱状图）</div>
                <Chart type="bar" :data="barData2" :options="barOptions"></Chart>
            </div>
        </div>
    </Fluid>
</template>
