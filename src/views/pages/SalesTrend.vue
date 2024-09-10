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
const combinedData = ref(null);
const combinedOptions = ref(null);
const Sales = ref();
// const years = [2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022, 2023, 2024];
const columns = [
    { field: 'year', header: '年份' },
    { field: 'sales', header: '总销量(辆)' }
];
let dropdownValues = ref([
    { name: '近十年', code: [2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022, 2023, 2024, 2025] },
    { name: '近五年', code: [2020, 2021, 2022, 2023, 2024, 2025] },
    { name: '2015年', code: [2015] },
    { name: '2016年', code: [2016] },
    { name: '2017年', code: [2017] },
    { name: '2018年', code: [2018] },
    { name: '2019年', code: [2019] },
    { name: '2020年', code: [2020] },
    { name: '2021年', code: [2021] },
    { name: '2022年', code: [2022] },
    { name: '2023年', code: [2023] },
    { name: '2024年', code: [2024] },
    { name: '2025年（预测）', code: [2025] },
]);
let titleMonth = "近十年汽车月销量统计";
let dropdownValue = ref(null);

onMounted(async () => {
    const salesData = await fetchSalesData();
    Sales.value = JSON.parse(JSON.stringify(salesData)); // 深拷贝，避免修改原始数据
    Sales.value[10].year = "2025（预测）"; // 修改Sales副本中的2025年
    combinedData.value = {
        labels: salesData.map(item => item.year === 2025 ? [`${item.year}`, '（预测）'] : item.year), // 为2025年标签添加换行
        datasets: [
            {
                type: 'line', // 折线图类型
                label: '',
                data: salesData.map(item => item.sales), // 从数据中提取销量
                fill: false,
                backgroundColor: '#42A5F5',
                borderColor: '#42A5F5',
                tension: 0.4
            },
            {
                type: 'bar', // 条形图类型
                label: '(单位:辆)',
                backgroundColor: '#83dbea',
                borderColor: '#83dbea',
                data: salesData.map(item => item.sales) // 从数据中提取销量
            }
        ]
    };

    lineData.value = {
        labels: salesData.map(item => item.year), // 从数据中提取年份
        datasets: [
            {
                label: '(单位:辆)',
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
                label: '(单位:辆)',
                backgroundColor: '#42A5F5',
                borderColor: '#42A5F5',
                data: salesData.map(item => item.sales)
            }
        ]
    };
    showAllChartData(dropdownValues.value[0].code);
    setColorOptions(); // 调用 setColorOptions 来设置样式
});

async function showAllChartData(years) {
    try {
        const allSalesData = [];
        const colors = generateColors(years.length); // 生成颜色数组

        for (const year of years) {
            const salesData = await fetchMonthData(year);
            allSalesData.push(salesData);
        }

        // 更新折线图数据
        const uniqueLabels = Array.from(new Set(allSalesData.flat().map(item => item.month))).map(month => `${month}月`);

        lineData2.value = {
            labels: uniqueLabels,
            datasets: []
        };

        for (let i = 0; i < years.length; i++) {
            const year = years[i];
            const salesDataForYear = allSalesData[i];
            const color = colors[i];

            // 构建数据集
            const dataset = {
                label: year === 2025 ? '2025（预测）' : `${year}年销售量`, // 修改2025的label
                data: uniqueLabels.map(label => {
                    const month = parseInt(label.replace('月', ''), 10);
                    const saleItem = salesDataForYear.find(item => item.month === month);
                    return saleItem ? saleItem.sales : null;
                }),
                fill: false,
                backgroundColor: color,
                borderColor: color,
                tension: 0.4,
                borderDash: year === 2025 ? [5, 5] : [] // 为2025年添加虚线
            };

            // 添加到 datasets
            lineData2.value.datasets.push(dataset);
        }

        // 更新柱状图数据
        barData2.value = {
            labels: uniqueLabels,
            datasets: []
        };

        for (let i = 0; i < years.length; i++) {
            const year = years[i];
            const salesDataForYear = allSalesData[i];
            const color = colors[i];

            // 构建数据集
            const dataset = {
                label: year === 2025 ? '2025（预测）' : `${year}年销售量`, // 修改2025的label
                backgroundColor: color,
                borderColor: color,
                data: uniqueLabels.map(label => {
                    const month = parseInt(label.replace('月', ''), 10);
                    const saleItem = salesDataForYear.find(item => item.month === month);
                    return saleItem ? saleItem.sales : null;
                })
            };

            // 添加到 datasets
            barData2.value.datasets.push(dataset);
        }
    } catch (error) {
        console.error('Error updating chart data:', error.message);
    }
}
const fixedColors = [
    '#1f77b4', // 蓝色
    '#ff7f0e', // 橙色
    '#2ca02c', // 绿色
    '#d62728', // 红色
    '#9467bd', // 紫色
    '#8c564b', // 棕色
    '#e377c2', // 粉色
    '#7f7f7f', // 灰色
    '#bcbd22', // 黄绿色
    '#17becf'  // 青色
];
// 生成颜色数组
function generateColors(count) {
    const fixedColors = [
        '#1f77b4', // 蓝色
        '#ff7f0e', // 橙色
        '#2ca02c', // 绿色
        '#d62728', // 红色
        '#9467bd', // 紫色
        '#8c564b', // 棕色
        '#e377c2', // 粉色
        '#7f7f7f', // 灰色
        '#bcbd22', // 黄绿色
        '#17becf'  // 青色
    ];

    const colors = [];
    for (let i = 0; i < count; i++) {
        // 使用模运算来确保不会超出固定颜色数组的长度
        colors.push(fixedColors[i % fixedColors.length]);
    }
    return colors;
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
    combinedOptions.value = {
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

watch(dropdownValue, (newValue) => {
    if (newValue) {
        titleMonth = newValue.name + "汽车月销量统计";
        showAllChartData(newValue.code);

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
                <div class="font-semibold text-xl mb-4">近十年汽车年销量统计</div>
                <Chart type="line" :data="combinedData" :options="combinedOptions"></Chart>
            </div>
        </div>

        <div class="col-span-12 xl:col-span-6">
            <div class="card">
                <div class="font-semibold text-xl mb-4">近十年汽车年销量统计</div>
                <DataTable :value="Sales" scrollable scrollHeight="295px" tableStyle="min-width: 40rem">
                    <Column v-for="col of columns" :key="col.field" :field="col.field" :header="col.header"></Column>
                </DataTable>
            </div>
        </div>
        <div class="col-span-12 xl:col-span-12">
            <div class="card">
                <div class="flex items-center justify-between mb-4">
                    <div class="font-semibold text-xl">{{ titleMonth }}</div>
                    <div>
                        <Select v-model="dropdownValue" :options="dropdownValues" optionLabel="name" placeholder="选择年份"
                            style="margin-bottom: 0; margin-left: 10px;" />
                    </div>
                </div>
                <Chart type="line" :data="lineData2" :options="lineOptions"></Chart>
            </div>
            <div class="card">
                <div class="font-semibold text-xl mb-4">{{ titleMonth }}</div>
                <Chart type="bar" :data="barData2" :options="barOptions"></Chart>
            </div>
        </div>

    </Fluid>
</template>
