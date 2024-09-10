<script setup>
import { useLayout } from '@/layout/composables/layout';
// import { CountryService } from '@/service/CountryService';
import { getAllCarTirms, getCarEvl, getCarSales, getCarSeries, getCarTirm, getCarTirmConfig } from '@/api';
import { computed, onMounted, ref, watch } from 'vue';

const { getPrimary, getSurface, isDarkTheme } = useLayout();
const pieData = ref(null);
const pieOptions = ref(null);
const barData = ref(null);
const barOptions = ref(null);
const radarData = ref(null);
const radarOptions = ref(null);
const lineData = ref(null);
const lineOptions = ref(null);
const polarData = ref(null);
const polarOptions = ref(null);
const selectedAutoValue = ref(null);
const autoFilteredValue = ref([]);
const autoValue = ref(null);
const tirmDetail = ref([{ brand: '', series: '', tirm: '', energyType: '', type: '', price: '' }]);
let selectedCars = [];

onMounted(() => {
    setColorOptions();
    fetchAllCarTirms();
    // CountryService.getCountries().then((data) => (autoValue.value = data));
});

async function fetchAllCarTirms() {
    let response = await getAllCarTirms();
    autoValue.value = response.data.data.map(item => {
        // 我们可以简化 `code` 为车型名称的前几个字符，例如取前三个字符
        // const code = item.substring(0, 3);
        return {
            name: item
            // code: item
        };
    });
}
const handleClick = () => {
    selectedAutoValue.value.forEach(async (tirm) => {
        await fetchCarTirmDetail(tirm);
    });
    updataChartData(tirmDetail);
};
const fetchCarTirmDetail = async (tirm) => {
    try {
        const response = await getCarTirm(tirm);
        if (response.data.success) {
            tirmDetail.value.push({
                brand: response.data.data.brand,
                series: response.data.data.series,
                tirm: response.data.data.tirm,
                energyType: response.data.data.energyType,
                type: response.data.data.type,
                price: response.data.data.price
            });
            const id = response.data.data.id; // cartirm的id
            // LikeCheck();
            const responseSeries = await getCarSeries(response.data.data.series);
            if (responseSeries.data.success) {
                const seriesDetail = responseSeries.data.data;
                const seriesId = seriesDetail.id;
                fetchCarEvaluation(seriesId);
                fetchCarSales(seriesId, 2024);
                fetchCarTirms(seriesId);
                fetchCarSeriesPurpose(seriesDetail.purpose);
            } else {
                console.error('查询失败:', responseSeries.data.msg);
            }
            fetchCarTirmImage(id);
            fetchCarTirmConfig(id);
        } else {
            console.error('查询失败:', response.data.msg);
        }
    } catch (error) {
        console.error('获取数据失败:', error);
    }
};
const fetchCarTirmConfig = async (id) => {
    try {
        const response = await getCarTirmConfig(id)
        if (response.data.success) {
            configData.value.push(response.data.data);
        } else {
            console.error('查询失败:', response.data.msg)
        }
    } catch (error) {
        console.error('获取数据失败:', error)
    }
}
const fetchCarSales = async (id, year) => {
    try {
        const response = await getCarSales(id, year)
        let sales = [];
        if (response.data.success) {
            sales = response.data.data;
            updataLineData(sales);
            updataYearlySale(sales);
        }
        else {
            console.error('查询失败:', response.data.msg)
        }
    } catch (error) {
        console.error('获取数据失败:', error)
    }
}
const fetchCarEvaluation = async (id) => {
    try {
        const response = await getCarEvl(id)
        if (response.data.success) {
            const evalData = response.data.data
            // 使用从后端获取的数据替换 detail 数组的数值
            detail.value = [
                evalData.overallRating,
                evalData.space,
                evalData.driveFeel,
                evalData.powerConsum,
                evalData.outDecor,
                evalData.inDecor,
                evalData.qpRatio,
                evalData.configure
            ]
            updataRadarData(detail);
        } else {
            console.error('查询失败:', response.data.msg)
        }
    } catch (error) {
        console.error('获取数据失败:', error)
    }
}
const data = ref([213, 414, 4241, 24124, 42531, 12312, 154251, 1312, 333, 312]);
const data2 = ref([2139, 4149, 42419, 241249, 425319, 123192, 1542591, 13192, 3933, 3912]);

const rows = computed(() => {
    const numberOfColumns = 2;
    const result = [];
    for (let i = 0; i < data.value.length; i += numberOfColumns) {
        result.push(data.value.slice(i, i + numberOfColumns));
    }
    return result;
});

const rows2 = computed(() => {
    const numberOfColumns = 2;
    const result = [];
    for (let i = 0; i < data2.value.length; i += numberOfColumns) {
        result.push(data2.value.slice(i, i + numberOfColumns));
    }
    return result;
});


function searchCarTirms(event) {
    setTimeout(() => {
        if (!event.query.trim().length) {
            autoFilteredValue.value = [...autoValue.value];
        } else {
            autoFilteredValue.value = autoValue.value.filter((tirm) => {
                return tirm.name.toLowerCase().startsWith(event.query.toLowerCase());
            });
        }
    }, 600);
    console.log(selectedAutoValue.value);
}

function setColorOptions() {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');

    barData.value = {
        labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
        datasets: [
            {
                label: 'My First dataset',
                backgroundColor: documentStyle.getPropertyValue('--p-primary-500'),
                borderColor: documentStyle.getPropertyValue('--p-primary-500'),
                data: [65, 59, 80, 81, 56, 55, 40]
            },
            {
                label: 'My Second dataset',
                backgroundColor: documentStyle.getPropertyValue('--p-primary-200'),
                borderColor: documentStyle.getPropertyValue('--p-primary-200'),
                data: [28, 48, 40, 19, 86, 27, 90]
            }
        ]
    };
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
    lineData.value = {
        labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
        datasets: [
            {
                label: 'First Dataset',
                data: [65, 59, 80, 81, 56, 55, 40],
                fill: false,
                backgroundColor: documentStyle.getPropertyValue('--p-primary-500'),
                borderColor: documentStyle.getPropertyValue('--p-primary-500'),
                tension: 0.4
            },
            {
                label: 'Second Dataset',
                data: [28, 48, 40, 19, 86, 27, 90],
                fill: false,
                backgroundColor: documentStyle.getPropertyValue('--p-primary-200'),
                borderColor: documentStyle.getPropertyValue('--p-primary-200'),
                tension: 0.4
            }
        ]
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

    polarData.value = {
        datasets: [
            {
                data: [11, 16, 7, 3],
                backgroundColor: [documentStyle.getPropertyValue('--p-indigo-500'), documentStyle.getPropertyValue('--p-purple-500'), documentStyle.getPropertyValue('--p-teal-500'), documentStyle.getPropertyValue('--p-orange-500')],
                label: 'My dataset'
            }
        ],
        labels: ['Indigo', 'Purple', 'Teal', 'Orange']
    };

    polarOptions.value = {
        maintainAspectRatio: false,
        plugins: {
            legend: {
                labels: {
                    color: textColor
                }
            }
        },
        scales: {
            r: {
                grid: {
                    color: surfaceBorder
                }
            }
        }
    };
    radarData.value = {
        labels: ['Eating', 'Drinking', 'Sleeping', 'Designing', 'Coding', 'Cycling', 'Running'],
        datasets: [
            {
                label: 'My First dataset',
                borderColor: documentStyle.getPropertyValue('--p-indigo-400'),
                pointBackgroundColor: documentStyle.getPropertyValue('--p-indigo-400'),
                pointBorderColor: documentStyle.getPropertyValue('--p-indigo-400'),
                pointHoverBackgroundColor: textColor,
                pointHoverBorderColor: documentStyle.getPropertyValue('--p-indigo-400'),
                data: [65, 59, 90, 81, 56, 55, 40]
            },
            {
                label: 'My Second dataset',
                borderColor: documentStyle.getPropertyValue('--p-purple-400'),
                pointBackgroundColor: documentStyle.getPropertyValue('--p-purple-400'),
                pointBorderColor: documentStyle.getPropertyValue('--p-purple-400'),
                pointHoverBackgroundColor: textColor,
                pointHoverBorderColor: documentStyle.getPropertyValue('--p-purple-400'),
                data: [28, 48, 40, 19, 96, 27, 100]
            }
        ]
    };

    radarOptions.value = {
        plugins: {
            legend: {
                labels: {
                    fontColor: textColor
                }
            }
        },
        scales: {
            r: {
                grid: {
                    color: textColorSecondary
                }
            }
        }
    };
}
function formatLineData(data) {
    const documentStyle = getComputedStyle(document.documentElement);
    return {
        labels: data.map(item => item.month),
        datasets: [
            {
                label: data[0].year + '月销量',
                data: data.map(item => item.totalSale),
                fill: false,
                backgroundColor: documentStyle.getPropertyValue('--p-primary-500'),
                borderColor: documentStyle.getPropertyValue('--p-primary-500'),
                tension: 0.4
            },
        ]
    }
}
function updataChartData(purpose) {
    console.log(purpose.value);
    pieData.value = formatPieData(purpose);
}
function formatPieData(data) {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');
    return {
        labels: data.map(item => item.tirm),
        datasets: [{
            data: data.map(item => item.price),
            backgroundColor: [documentStyle.getPropertyValue('--p-indigo-500'), documentStyle.getPropertyValue('--p-purple-500'), documentStyle.getPropertyValue('--p-teal-500'), documentStyle.getPropertyValue('--p-red-500'), documentStyle.getPropertyValue('--p-blue-500'), documentStyle.getPropertyValue('--p-yellow-500'), documentStyle.getPropertyValue('--p-orange-500')],
            hoverBackgroundColor: [documentStyle.getPropertyValue('--p-indigo-400'), documentStyle.getPropertyValue('--p-purple-400'), documentStyle.getPropertyValue('--p-teal-400'), documentStyle.getPropertyValue('--p-red-400'), documentStyle.getPropertyValue('--p-blue-400'), documentStyle.getPropertyValue('--p-yellow-400'), documentStyle.getPropertyValue('--p-orange-400')],
        }]
    }
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
        <div class="font-semibold text-xl" style="margin-bottom: 30px;">详情比较</div>
    </div>
    <div class="grid grid-cols-12 gap-8 mt-8">
        <div class="col-span-12 xl:col-span-6">
            <div class="card flex flex-col justify-center items-center" style="height: 500px;">
                <div class="font-semibold text-xl mb-4">价格对比</div>
                <Chart type="polarArea" :data="polarData" :options="polarOptions"
                    style="height: 100%; width: 100%; padding: 0;">
                </Chart>
            </div>
        </div>
        <div class="col-span-12 xl:col-span-6">
            <div class="card flex flex-col justify-center items-center" style="height: 500px;">
                <div class="font-semibold text-xl mb-4">销量对比(2024年)</div>
                <Chart type="polarArea" :data="polarData1" :options="polarOptions"
                    style="height: 100%; width: 100%; padding: 0;">
                </Chart>
            </div>
        </div>
        <div class=" col-span-12 xl:col-span-6">
            <div class="card flex flex-col justify-center items-center" style="height: 500px;">
                <div class="font-semibold text-xl mb-4">销售趋势</div>
                <Chart type="line" :data="lineData" :options="lineOptions"
                    style="height: 100%; width: 100%; padding: 0;margin-top: 50px;"></Chart>
            </div>
        </div>
        <div class=" col-span-12 xl:col-span-6">

            <div class="card flex flex-col justify-center items-center" style="height: 500px;">
                <div class="font-semibold text-xl mb-4">指标比较</div>
                <Chart type="bar" :data="barData" :options="barOptions"
                    style="height: 100%; width: 100%; padding: 0;margin-top: 50px;"></Chart>
            </div>
        </div>
    </div>

    <div class="col-span-12 xl:col-span-6">
        <div class="card">
            <div class="w-full overflow-x-auto">
                <table class="min-w-full border-2 border-gray-300 divide-y divide-gray-300">
                    <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400"
                        style="text-align: left;">
                        <tr>
                            <th scope="col" class="px-6 py-3 w-1/10">
                                <div class="font-semibold text-xl">参数类型</div>
                            </th>
                            <th scope="col" class="px-6 py-3 w-7/30">
                                <div class="font-semibold text-xl">参数名称</div>
                            </th>
                            <th scope="col" class="px-6 py-3 w-1/6">
                                <div class="font-semibold text-xl">配置1</div>
                            </th>
                            <th scope="col" class="px-6 py-3 w-1/6">
                                <div class="font-semibold text-xl">配置2</div>
                            </th>
                            <th scope="col" class="px-6 py-3 w-1/6">
                                <div class="font-semibold text-xl">配置3</div>
                            </th>
                            <th scope="col" class="px-6 py-3 w-1/6">
                                <div class="font-semibold text-xl">配置4</div>
                            </th>
                        </tr>
                    </thead>
                    <tbody class="bg-white divide-y divide-gray-300">
                        <tr v-for="item in configData" :key="item.id"
                            class="bg-white border-b dark:bg-gray-800 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600">
                            <td class="px-6"><span style="font-size: 10pt; ">{{
                                item.configType
                                    }}</span></td>
                            <td class="px-6" @click="navigateToCarSeriesDetail(item)"><span style="font-size: 10pt;">
                                    {{ item.configName
                                    }}</span></td>
                            <td><span style="font-size: 10pt; ">
                                    {{ item.configValue }}</span></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</template>
