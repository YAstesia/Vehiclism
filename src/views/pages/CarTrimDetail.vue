<script setup>
import { addToFavorite, checkLike, deleteFavorite, getCarEvl, getCarSales, getCarSeries, getCarTirm, getCarTirmBySeriesId, getCarTirmConfig, getCarTirmImg, getSeriesPurpose } from '@/api';
import { computed, onBeforeMount, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();
const pieData = ref(null);
const pieOptions = ref(null);
const lineData = ref(null);
const lineOptions = ref(null);
const radarData = ref(null);
const radarOptions = ref(null);
const displayedData = ref([]);
const configData = ref([]);
const data = ref([213, 414, 4241, 24124, 42531, 12312, 154251, 1312, 333, 312]);
const seriesDetail = ref({ brand: '', series: '', priceMin: 0, priceMax: 0, type: '' })
const tirmDetail = ref({ brand: '', series: '', tirm: '', energyType: '', type: '', price: '' })
const defaultImageUrl = 'https://www.sucaijishi.com/uploadfile/2017/0510/20170510104938756.gif';
const detail = ref([0, 0, 0, 0, 0, 0, 0, 0]);
const value1 = computed(() => detail.value[0]);
const value2 = computed(() => detail.value[1]);
const value3 = computed(() => detail.value[2]);
const value4 = computed(() => detail.value[3]);
const value5 = computed(() => detail.value[4]);
const value6 = computed(() => detail.value[5]);
const value7 = computed(() => detail.value[6]);
const value8 = computed(() => detail.value[7]);
let id = null;
let seriesId = null;
let userId = localStorage.getItem('user_id');
// let year=null;
let YearlySale = null;
let imgsrc = ref(null);
const liked = ref(null);
const compared = ref(null);
// 状态严重性映射
const statuses = ["汽油",
    "纯电动",
    "汽油+48V轻混系统",
    "-",
    "插电式混合动力",
    "柴油",
    "增程式",
    "氢燃料",
    "油电混合",
    "汽油+CNG",
    "汽油电驱",
    "柴油+48V轻混系统",
    "甲醇混动",
    "CNG",
    "汽油+24V轻混系统"];

// 获取严重性
function getSeverity(status) {
    switch (status) {
        case '汽油':
            return 'danger';
        case '柴油':
            return 'danger';
        case '-':
            return 'danger';
        case '纯电动':
            return 'success';
        case '氢燃料':
            return 'info';
        case 'CNG':
            return 'info';
        case '甲醇混动':
            return 'info';
        case '汽油+48V轻混系统':
            return 'warn';
        case '插电式混合动力':
            return 'warn';
        case '汽油+48V轻混系统':
            return 'warn';
        case '增程式':
            return 'warn';
        case '油电混合':
            return 'warn';
        case '汽油+CNG':
            return 'warn';
        case '汽油+汽油电驱':
            return 'warn';
        case '柴油+48V轻混系统':
            return 'warn';
        case '汽油+24V轻混系统':
            return 'warn';
    }
}
const navigateToCarSeriesDetail = (item) => {
    router.push(`/cardetail/${item.series}`);
};
// 格式化货币
function formatCurrency(value) {
    if (value === null) {
        return '暂无数据';
    }
    const formattedValue = (value).toLocaleString();
    return formattedValue + '万元';
}
onBeforeMount(() => {
    setColorOptions();
    fetchCarTirmDetail(route.params.tirm);
});

watch(() => route.params.tirm, (newTirm) => {
    fetchCarTirmDetail(newTirm);
    // fetchCarSeriesPurpose(newSeries);
});
watch(() => liked.value, (newLiked) => {
    // 当 liked 发生变化时，可以在这里执行一些额外的操作
    // 例如：console.log(`liked changed to ${newLiked}`);
}, { immediate: true });
async function LikeCheck() {
    const responseLike = await checkLike(userId, id);
    if (responseLike.data.success) {
        liked.value = responseLike.data.data;
    } else {
        console.error('查询失败:', responseLike.data.msg)
    }
}
const fetchCarTirmDetail = async (tirm) => {
    try {
        const response = await getCarTirm(tirm)
        if (response.data.success) {
            tirmDetail.value = response.data.data;
            id = response.data.data.id;//cartirm的id
            LikeCheck();
            compareCheck();
            const responseSeries = await getCarSeries(response.data.data.series)
            if (responseSeries.data.success) {
                seriesDetail.value = responseSeries.data.data;
                seriesId = responseSeries.data.data.id;
                fetchCarEvaluation(seriesId);
                fetchCarSales(seriesId, 2024);
                fetchCarTirms(seriesId);
                fetchCarSeriesPurpose(response.data.data.series);
            } else {
                console.error('查询失败:', responseSeries.data.msg)
            }
            fetchCarTirmImage(id);
            fetchCarTirmConfig(id);
        } else {
            console.error('查询失败:', response.data.msg)
        }
    } catch (error) {
        console.error('获取数据失败:', error)
    }
}
const fetchCarTirms = async (id) => {
    try {
        const response = await getCarTirmBySeriesId(id)
        if (response.data.success) {
            displayedData.value = response.data.data;
        } else {
            console.error('查询失败:', response.data.msg)
        }
    } catch (error) {
        console.error('获取数据失败:', error)
    }
}
const fetchCarTirmImage = async (id) => {
    try {
        const response = await getCarTirmImg(id)
        if (response.data.success && response.data.data != null) {
            imgsrc = response.data.data;
        } else {
            imgsrc.value = defaultImageUrl;
            console.error('查询失败:', response.data.msg)
        }
    } catch (error) {
        console.error('获取数据失败:', error)
    }
}
const fetchCarTirmConfig = async (id) => {
    try {
        const response = await getCarTirmConfig(id)
        if (response.data.success) {
            configData.value = response.data.data;
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

const fetchCarSeriesPurpose = async (series) => {
    try {
        const response = await getSeriesPurpose(series)
        let purpose = [];
        if (response.data.success) {
            purpose = response.data.data;
            updataChartData(purpose);
        }
        else {
            console.error('查询失败:', response.data.msg)
        }
    } catch (error) {
        console.error('获取数据失败:', error)
    }
}
async function addToUserFavorite() {
    const response = await addToFavorite(userId, id);
    LikeCheck();
}
async function deleteUserFavorite() {
    const response = await deleteFavorite(userId, id);
    LikeCheck();
}

async function compareCheck() {
    compared.value = false;
    for (let i = 1; i <= 5; i++) {
        let trimKey = `trim${i}`;
        let storedValue = localStorage.getItem(trimKey);

        if (storedValue === tirmDetail.value.tirm) {  // 如果找到相同的值
            compared.value = true;
            return;
        }
    }
    console.log(compared.value);
}

function addCompare() {
    for (let i = 1; i <= 5; i++) {
        let trimKey = `trim${i}`;
        let storedValue = localStorage.getItem(trimKey);

        if (!storedValue && !compared.value) {  // 检查localStorage的值是否为空
            localStorage.setItem(trimKey, tirmDetail.value.tirm);  // 将tirmDetail.trim的值赋给该localStorage
            compared.value = true;
        }
    }
}

function deleteCompare() {
    console.log(tirmDetail.value.tirm);
    for (let i = 1; i <= 5; i++) {
        let trimKey = `trim${i}`;
        let storedValue = localStorage.getItem(trimKey);

        if (storedValue === tirmDetail.value.tirm) {  // 如果找到相同的值
            localStorage.setItem(trimKey, '');  // 清空该localStorage
            compared.value = false;
        }
    }
}

function updataYearlySale(sales) {
    sales.forEach(sale => {
        YearlySale += sale.totalSale;
    });
}
function updataChartData(purpose) {
    pieData.value = formatPieData(purpose);
}

function updataRadarData(detail) {
    radarData.value = formatRadarData(detail);
}
function updataLineData(sales) {
    lineData.value = formatLineData(sales);
}

function formatPieData(data) {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');
    return {
        labels: data.map(item => item.purpose),
        datasets: [{
            data: data.map(item => item.percentage),
            backgroundColor: [documentStyle.getPropertyValue('--p-indigo-500'), documentStyle.getPropertyValue('--p-purple-500'), documentStyle.getPropertyValue('--p-teal-500'), documentStyle.getPropertyValue('--p-red-500'), documentStyle.getPropertyValue('--p-blue-500'), documentStyle.getPropertyValue('--p-yellow-500'), documentStyle.getPropertyValue('--p-orange-500')],
            hoverBackgroundColor: [documentStyle.getPropertyValue('--p-indigo-400'), documentStyle.getPropertyValue('--p-purple-400'), documentStyle.getPropertyValue('--p-teal-400'), documentStyle.getPropertyValue('--p-red-400'), documentStyle.getPropertyValue('--p-blue-400'), documentStyle.getPropertyValue('--p-yellow-400'), documentStyle.getPropertyValue('--p-orange-400')],
        }]
    }
}

function formatRadarData(detail) {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');
    return {
        labels: ['综合评分', '空间评分', '驾驶感受', '能耗评分', '外观评分', '内饰评分', '性价比', '配置评分'],
        datasets: [{
            label: '评分',
            data: detail,
            borderColor: documentStyle.getPropertyValue('--p-primary-400'),
            pointBackgroundColor: documentStyle.getPropertyValue('--p-primary-400'),
            pointBorderColor: documentStyle.getPropertyValue('--p-primary-400'),
            pointHoverBackgroundColor: textColor,
            pointHoverBorderColor: documentStyle.getPropertyValue('--p-primary-400'),
        }]
    }
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

const rows = computed(() => {
    const numberOfColumns = 4;
    const result = [];
    for (let i = 0; i < data.value.length; i += numberOfColumns) {
        result.push(data.value.slice(i, i + numberOfColumns));
    }
    return result;
});

function setColorOptions() {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');

    pieOptions.value = {
        plugins: {
            legend: {
                labels: {
                    usePointStyle: true,
                    color: textColor
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
                beginAtZero: true,  // 中心值从 0 开始
                min: 0,  // 中心值
                max: 5,  // 最外圈值
                ticks: {
                    stepSize: 1,  // 刻度间隔
                    color: '#000',  // 刻度颜色
                },
                grid: {
                    color: textColorSecondary
                }
            }
        }
    };
}

function goBack() {
    router.go(-1)
}
</script>

<style scoped>
/* 你的样式 */
</style>

<template>
    <div class="flex flex-col">
        <div class="card">
            <span class="text-surface-900 dark:text-surface-0 font-medium text-2xl leading-normal mr-10">车型详情</span>
            <!-- 根据 liked 的值显示不同的按钮 -->
            <template v-if="liked">
                <Button label="取消收藏" class="layout-menu-button layout-topbar-action"
                    style="background-color: crimson; margin-right: 10px; border-color: crimson;"
                    @click="deleteUserFavorite"></Button>
            </template>
            <template v-else>
                <Button label="收藏" class="layout-menu-button layout-topbar-action"
                    style="background-color: crimson; margin-right: 10px; border-color: crimson;"
                    @click="addToUserFavorite"></Button>
            </template>

            <template v-if="compared">
                <Button label="取消对比" class="layout-menu-button layout-topbar-action"
                    style="background-color: dodgerblue; margin-right: 10px; border-color: dodgerblue;"
                    @click="deleteCompare()"></Button>
            </template>
            <template v-else>
                <Button label="加入对比" class="layout-menu-button layout-topbar-action"
                    style="background-color:dodgerblue; margin-right: 10px; border-color: dodgerblue;"
                    @click="addCompare()"></Button>
            </template>
            <Button label="返回" class="layout-menu-button layout-topbar-action" @click="goBack()"></Button>

        </div>

        <div class="card">
            <div class="flex flex-col md:flex-row gap-4 mt-6">
                <div class="md:w-1/2 p-4">
                    <div class="card p-4 border rounded shadow-lg">
                        <!-- 图片 :src="imgsrc"-->
                        <meta name="referrer" content="no-referrer">
                        <img :src="imgsrc || defaultImageUrl" alt="Image" class="w-full h-auto mb-4">

                        <!-- 两行居中的文字 -->
                        <div class="text-center mb-4">
                            <div class="font-bold" style="font-size: 24pt; margin-bottom: 15px; margin-top: 15px;">
                                {{ seriesDetail.brand }} &nbsp; &nbsp; {{ seriesDetail.series }}</div>
                        </div>

                        <div class="text-center mb-4">
                            <div class="font-bold" style="font-size: 24pt; margin-bottom: 15px; margin-top: 15px;">
                                {{ tirmDetail.tirm }}</div>
                            <div class="text-gray-600" style="font-size: 24pt;">￥{{ tirmDetail.price }} 万</div>
                        </div>

                        <!-- 填充了字的文本框 -->
                        <Card>
                            <template v-slot:content>
                                <p class="leading-normal m-0 text-center" style="font-size: 24px;">
                                    {{ seriesDetail.type }}
                                </p>
                            </template>
                        </Card>
                    </div>
                    <div class="font-bold" style="font-size: 20pt; margin-bottom: 20px; margin-left: 45%;">
                        车系信息
                    </div>
                    <div class="flex flex-wrap">
                        <div class="md:w-1/4 p-2">
                            <div class="card flex flex-col items-center relative">
                                <div class="absolute top-0 left-1/2 transform -translate-x-1/2 font-bold"
                                    style="font-size: 16px;">
                                    综合评分
                                </div>
                                <Knob v-model="value1" :min="0.00" :max="5.00" valueColor="#10b981"
                                    rangeColor="SlateGray" readonly />
                            </div>
                        </div>
                        <div class="md:w-1/4 p-2">
                            <div class="card flex flex-col items-center relative">
                                <div class="absolute top-0 left-1/2 transform -translate-x-1/2 font-bold"
                                    style="font-size: 16px;">
                                    空间评分</div>
                                <Knob v-model="value2" :min="0.00" :max="5.00" valueColor="#10b981"
                                    rangeColor="SlateGray" readonly />
                            </div>
                        </div>
                        <div class="md:w-1/4 p-2">
                            <div class="card flex flex-col items-center relative">
                                <div class="absolute top-0 left-1/2 transform -translate-x-1/2 font-bold"
                                    style="font-size: 16px;">
                                    驾驶感受</div>
                                <Knob v-model="value3" :min="0.00" :max="5.00" valueColor="#10b981"
                                    rangeColor="SlateGray" readonly />
                            </div>
                        </div>
                        <div class="md:w-1/4 p-2">
                            <div class="card flex flex-col items-center relative">
                                <div class="absolute top-0 left-1/2 transform -translate-x-1/2 font-bold"
                                    style="font-size: 16px;">
                                    能耗评分</div>
                                <Knob v-model="value4" :min="0.00" :max="5.00" valueColor="#10b981"
                                    rangeColor="SlateGray" readonly />
                            </div>
                        </div>
                        <div class="md:w-1/4 p-2">
                            <div class="card flex flex-col items-center relative">
                                <div class="absolute top-0 left-1/2 transform -translate-x-1/2 font-bold"
                                    style="font-size: 16px;">
                                    外观评分</div>
                                <Knob v-model="value5" :min="0.00" :max="5.00" valueColor="#10b981"
                                    rangeColor="SlateGray" readonly />
                            </div>
                        </div>
                        <div class="md:w-1/4 p-2">
                            <div class="card flex flex-col items-center relative">
                                <div class="absolute top-0 left-1/2 transform -translate-x-1/2 font-bold"
                                    style="font-size: 16px;">
                                    内饰评分</div>
                                <Knob v-model="value6" :min="0.00" :max="5.00" valueColor="#10b981"
                                    rangeColor="SlateGray" readonly />
                            </div>
                        </div>
                        <div class="md:w-1/4 p-2">
                            <div class="card flex flex-col items-center relative">
                                <div class="absolute top-0 left-1/2 transform -translate-x-1/2 font-bold"
                                    style="font-size: 16px;">
                                    性价比</div>
                                <Knob v-model="value7" :min="0.00" :max="5.00" valueColor="#10b981"
                                    rangeColor="SlateGray" readonly />
                            </div>
                        </div>
                        <div class="md:w-1/4 p-2">
                            <div class="card flex flex-col items-center relative">
                                <div class="absolute top-0 left-1/2 transform -translate-x-1/2 font-bold"
                                    style="font-size: 16px;">
                                    配置评分</div>
                                <Knob v-model="value8" :min="0.00" :max="5.00" valueColor="#10b981"
                                    rangeColor="SlateGray" readonly />
                            </div>
                        </div>

                        <div class="w-full border-2 border-gray-300 my-4"></div>

                        <div class="card flex flex-row w-full">
                            <div class="md:w-1/2 p-2 text-center">
                                <div class="font-bold" style="font-size: 20pt; margin-top: -20px">2024年销量</div>
                                <div class="font-bold" style="font-size: 32pt; color: red; margin-bottom: -40px">
                                    {{ YearlySale }}
                                </div>
                            </div>
                            <div class="md:w-1/2 p-2 text-center">
                                <div class="font-bold" style="font-size: 20pt; margin-top: -20px">综合评分</div>
                                <div class="font-bold" style="font-size: 32pt; color: crimson; margin-bottom: -40px">{{
                                    value1 }}</div>
                            </div>
                        </div>

                        <div class="w-full border-2 border-gray-300 my-4"></div>

                        <div class="card flex flex-row w-full">
                            <div class="md:w-1/2 p-2 flex flex-col items-center">
                                <div class="font-bold" style="font-size: 20pt; margin-bottom: 0px;">车型参数图</div>
                                <Chart type="radar" style="margin-top: 20px;" :data="radarData" :options="radarOptions">
                                </Chart>
                            </div>
                            <div class="md:w-1/2 p-2 flex flex-col items-center">
                                <div class="font-bold" style="font-size: 20pt; margin-bottom: 10px;">购车目的</div>
                                <Chart type="pie" :data="pieData" :options="pieOptions"
                                    style="width: 320px; height: 320px;">
                                </Chart>
                            </div>
                        </div>

                        <div class="font-bold" style="font-size: 20pt; margin-bottom: 20px; margin-top: 40px;">车系所包含车型
                        </div>
                        <div class="w-full overflow-x-auto">
                            <table class="min-w-full border-2 border-gray-300 divide-y divide-gray-300">
                                <thead
                                    class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
                                    <tr>
                                        <th scope="col" class="px-2 py-3">
                                            <div class="font-semibold text-xl">品牌</div>
                                        </th>
                                        <th scope="col" class="px-2 py-3">
                                            <div class="font-semibold text-xl">车系</div>
                                        </th>
                                        <th scope="col" class="px-2 py-3">
                                            <div class="font-semibold text-xl">车型</div>
                                        </th>
                                        <th scope="col" class="px-2 py-3">
                                            <div class="font-semibold text-xl">汽车类型</div>
                                        </th>
                                        <th scope="col" class="px-2 py-3">
                                            <div class="font-semibold text-xl">能源类型</div>
                                        </th>
                                        <th scope="col" class="px-2 py-3">
                                            <div class="font-semibold text-xl">价格</div>
                                        </th>
                                    </tr>
                                </thead>
                                <tbody class="bg-white divide-y divide-gray-300">
                                    <tr v-for="item in displayedData" :key="item.id"
                                        class="bg-white border-b dark:bg-gray-800 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600">
                                        <td class="px-2 py-4"><span class="font-bold" style="font-size: 16px; ">{{
                                            item.brand
                                                }}</span></td>
                                        <td class="px-2 py-4" @click="navigateToCarSeriesDetail(item)"><span
                                                class="font-bold" style="font-size: 16px; text-decoration: underline;">
                                                {{ item.series
                                                }}</span></td>
                                        <td class="px-2 py-4"><span class="font-bold" style="font-size: 16px; ">
                                                {{ item.tirm }}</span></td>
                                        <td class="px-2 py-4"><span class="font-bold" style="font-size: 16px;">{{
                                            item.type
                                                }}</span></td>
                                        <td class="px-2 py-4">
                                            <Tag :options="statuses" :value="item.energyType"
                                                :severity="getSeverity(item.energyType)" style="font-size: 16px;"></Tag>
                                        </td>
                                        <td class="px-2 py-4"><span class="font-bold" style="font-size: 16px;">{{
                                            formatCurrency(item.price)
                                                }}</span>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="md:w-1/2 p-4">
                    <div class="w-full overflow-x-auto">
                        <table class="min-w-full border-2 border-gray-300 divide-y divide-gray-300">
                            <thead
                                class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400"
                                style="text-align: left;">
                                <tr>
                                    <th scope="col" class="px-6 py-3 w-1/5">
                                        <div class="font-semibold text-xl">参数类型</div>
                                    </th>
                                    <th scope="col" class="px-6 py-3 w-2/5">
                                        <div class="font-semibold text-xl">参数名称</div>
                                    </th>
                                    <th scope="col" class="px-6 py-3 w-2/5">
                                        <div class="font-semibold text-xl">配置</div>
                                    </th>
                                </tr>
                            </thead>
                            <tbody class="bg-white divide-y divide-gray-300">
                                <tr v-for="item in configData" :key="item.id"
                                    class="bg-white border-b dark:bg-gray-800 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600">
                                    <td class="px-6"><span style="font-size: 10pt; ">{{
                                        item.configType
                                            }}</span></td>
                                    <td class="px-6"><span style="font-size: 10pt;">
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
        </div>
    </div>
</template>
