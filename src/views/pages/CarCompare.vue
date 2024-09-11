<script setup>//!!!
import { useLayout } from '@/layout/composables/layout';
// import { CountryService } from '@/service/CountryService';
import { getCarEvl, getCarSales, getCarSeries, getCarTirm } from '@/api';
import { onMounted, ref, watch } from 'vue';
import { useRouter } from 'vue-router';

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
const polarData1 = ref(null);

const polarOptions = ref(null);
var tirmDetail = new Array();
// const tirmDetail = ref([]);
let TirmNames = ref([]);
let YearlySales = [];
let TirmSales = [];
const tirm1 = ref('');
const tirm2 = ref('');
const tirm3 = ref('');
const tirm4 = ref('');
const tirm5 = ref('');

onMounted(() => {
    tirm1.value = localStorage.getItem('trim1');
    tirm2.value = localStorage.getItem('trim2');
    tirm3.value = localStorage.getItem('trim3');
    tirm4.value = localStorage.getItem('trim4');
    tirm5.value = localStorage.getItem('trim5');

    setColorOptions();
    // fetchAllCarTirms();
    // 从 localStorage 中获取存储的车型名称
    const tirmKeys = ['trim1', 'trim2', 'trim3', 'trim4', 'trim5'];
    tirmKeys.forEach(key => {
        const tirmName = localStorage.getItem(key);
        if (tirmName && tirmName.trim() !== '') {
            TirmNames.value.push(tirmName);
        }
    });

    showAllChartData();
    // console.log(TirmNames.value);

});
async function showAllChartData() {
    // 创建一个包含所有 fetchCarTirmDetail 调用的 Promise 数组
    // console.log(TirmNames.value);
    const promises = TirmNames.value.map(tirm => fetchCarTirmDetail(tirm));

    // 使用 Promise.all 等待所有请求完成
    await Promise.all(promises);
    // console.log(tirmDetail);
    updatePolarData(tirmDetail);
    updatePolarData1(YearlySales);
    updateLineData(TirmSales);
    updateBarData();
}
function updateBarData() {
    const documentStyle = getComputedStyle(document.documentElement);
    const colors = [documentStyle.getPropertyValue('--p-indigo-500'), documentStyle.getPropertyValue('--p-purple-500'), documentStyle.getPropertyValue('--p-teal-500'), documentStyle.getPropertyValue('--p-orange-500'), documentStyle.getPropertyValue('--p-red-500')];
    barData.value = {
        labels: ["综合评分", "空间评分", "驾驶感受", "能耗评分", "外观评分", "内饰评分", "性价比", "配置评分"],
        datasets: []
    }
    // console.log(tirmDetail.length);
    for (let i = 0; i < tirmDetail.length; ++i) {
        const color = colors[i];
        if (tirmDetail[i].evls != null) {
            const dataset = {
                label: tirmDetail[i].tirm,
                data: tirmDetail[i].evls,
                fill: false,
                backgroundColor: color,
                borderColor: color,
                tension: 0.4
            }
            barData.value.datasets.push(dataset);
        }
    }
}
// function updateLineData() {
//     lineData = formatLineData();
// }
function updateLineData(data) {
    const documentStyle = getComputedStyle(document.documentElement);
    const colors = [documentStyle.getPropertyValue('--p-indigo-500'), documentStyle.getPropertyValue('--p-purple-500'), documentStyle.getPropertyValue('--p-teal-500'), documentStyle.getPropertyValue('--p-orange-500'), documentStyle.getPropertyValue('--p-red-500')];
    lineData.value = {
        labels: ["1月", "2月", "3月", "4月", "5月", "6月", "7月"],
        datasets: []
    }
    for (let i = 0; i < tirmDetail.length; ++i) {
        const color = colors[i];
        if (tirmDetail[i].tirmSales != null) {
            const dataset = {
                label: tirmDetail[i].tirm,
                data: tirmDetail[i].tirmSales.map(item => item.totalSale),
                fill: false,
                backgroundColor: color,
                borderColor: color,
                tension: 0.4
            }
            lineData.value.datasets.push(dataset);
        }
    }
}
const fetchCarTirmDetail = async (tirm) => {
    try {
        const response = await getCarTirm(tirm);
        if (response.data.success) {
            let tirmInfo = response.data.data;
            // 将车型详细信息添加到 tirmDetail 数组中
            // tirmDetail.push(response.data.data);
            // console.log(tirmDetail);
            const id = response.data.data.id; // cartirm的id
            // 获取 CarSeries 信息
            const responseSeries = await getCarSeries(response.data.data.series);
            if (responseSeries.data.success) {
                const seriesDetail = responseSeries.data.data;
                const seriesId = seriesDetail.id;
                const responseCarSales = await getCarSales(seriesId, 2024)
                let sales = [];
                let YearlySale = 0;
                if (responseCarSales.data.success) {
                    sales = responseCarSales.data.data;
                    sales.forEach(sale => {
                        YearlySale += sale.totalSale;
                    });
                    tirmInfo.yearSale = YearlySale;
                    tirmInfo.tirmSales = sales;
                } else {
                    console.error('查询失败:', responseSeries.data.msg);
                }
                const responseEvl = await getCarEvl(seriesId);
                if (responseEvl.data.success) {
                    const evalData = responseEvl.data.data;
                    let detail = [];
                    // console.log(evalData);
                    // 使用从后端获取的数据替换 detail 数组的数值
                    if (evalData != null) {
                        detail = [
                            evalData.overallRating,
                            evalData.space,
                            evalData.driveFeel,
                            evalData.powerConsum,
                            evalData.outDecor,
                            evalData.inDecor,
                            evalData.qpRatio,
                            evalData.configure
                        ]
                    }
                    tirmInfo.evls = detail;
                } else {
                    console.error('查询失败:', response.data.msg)
                }
                // console.log(tirmDetail);
                tirmDetail.push(tirmInfo);
            } else {
                console.error('查询失败:', response.data.msg);
            }
        }
    } catch (error) {
        console.error('获取数据失败:', error);
    }
};
function updatePolarData(detail) {
    polarData.value = formatPolarData(detail);
}
function formatPolarData(data) {
    // const sortedData = data.sort((a, b) => b.price - a.price);
    const documentStyle = getComputedStyle(document.documentElement);
    return {
        labels: data.map(item => item.tirm),
        datasets: [
            {
                data: data.map(item => item.price),
                backgroundColor: [documentStyle.getPropertyValue('--p-indigo-500'), documentStyle.getPropertyValue('--p-purple-500'), documentStyle.getPropertyValue('--p-teal-500'), documentStyle.getPropertyValue('--p-orange-500'), documentStyle.getPropertyValue('--p-red-500')],
                // label: '车型价格'
            }
        ]
    };
}
function updatePolarData1(yearlySale) {
    polarData1.value = formatPolarData1(yearlySale);
}
function formatPolarData1(data) {
    // const sortedData = data.sort((a, b) => b.price - a.price);
    // console.log(data);
    const documentStyle = getComputedStyle(document.documentElement);
    return {
        labels: tirmDetail.map(item => item.tirm),
        datasets: [
            {
                data: tirmDetail.map(item => item.yearSale),
                backgroundColor: [documentStyle.getPropertyValue('--p-indigo-500'), documentStyle.getPropertyValue('--p-purple-500'), documentStyle.getPropertyValue('--p-teal-500'), documentStyle.getPropertyValue('--p-orange-500'), documentStyle.getPropertyValue('--p-red-500')],
                // label: '车型价格'
            }
        ]
    };
}
const fetchCarSales = async (id, year) => {
    try {
        const response = await getCarSales(id, year)
        let sales = [];
        let YearlySale = 0;
        if (response.data.success) {
            sales = response.data.data;
            sales.forEach(sale => {
                YearlySale += sale.totalSale;
            });
            TirmSales.push(sales);
            console.log(TirmSales);
            YearlySales.push({ sale: YearlySale });
            // console.log(YearlySales);
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
        } else {
            console.error('查询失败:', response.data.msg)
        }
    } catch (error) {
        console.error('获取数据失败:', error)
    }
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
        datasets: []
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
                data: [],
                backgroundColor: [documentStyle.getPropertyValue('--p-indigo-500'), documentStyle.getPropertyValue('--p-purple-500'), documentStyle.getPropertyValue('--p-teal-500'), documentStyle.getPropertyValue('--p-orange-500')],
                label: '车型价格'
            }
        ],
        labels: []
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

watch(
    [getPrimary, getSurface, isDarkTheme],
    () => {
        setColorOptions();
    },
    { immediate: true }
);

const router = useRouter();
const navigateToCarTirmDetail = (tirm) => {
    router.push(`/typedetail/${tirm}`);
};

</script>

<template>
    <div class="card">
        <div class="font-semibold text-xl" style="margin-bottom: 30px;">车型比较（点击车型名称可跳转至详情页面）</div>
        <div class="flex ">
            <div class="font-semibold text-xl">已加入比较的车型：</div>
            <div class="  text-xl mr-8" style=" text-decoration: underline;" @click="navigateToCarTirmDetail(tirm1)">{{
                tirm1 }}</div>
            <div class="  text-xl mr-8" style=" text-decoration: underline;" @click="navigateToCarTirmDetail(tirm2)">{{
                tirm2 }}</div>
            <div class="  text-xl mr-8" style=" text-decoration: underline;" @click="navigateToCarTirmDetail(tirm3)">{{
                tirm3 }}</div>
            <div class="  text-xl mr-8" style=" text-decoration: underline;" @click="navigateToCarTirmDetail(tirm4)">{{
                tirm4 }}</div>
            <div class="  text-xl mr-8" style=" text-decoration: underline;" @click="navigateToCarTirmDetail(tirm5)">{{
                tirm5 }}</div>
        </div>
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
    <!-- 
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
    </div> -->
</template>
