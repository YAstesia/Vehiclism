<script setup>
import { getAllCities, getCityTopSeries, getProvinceCities, getProvinces, getProvinceTopSeries, getRegionSales, getTopCities, getTopProvinces, getTopSeries } from '@/api'; // 导入接口
import { useLayout } from '@/layout/composables/layout';
import * as echarts from 'echarts';
import { onMounted, ref, watch } from 'vue';


const { getPrimary, getSurface, isDarkTheme } = useLayout();
const pieData = ref(null);
const barData = ref(null);
const barData2 = ref(null);
const barData4 = ref(null);
const barData3 = ref(null);
const pieOptions = ref(null);
const barOptions = ref(null);
const barOptions10 = ref(null);
const chartRef = ref(null);
let titleofBar3 = "全国销量前十车系";
let titleofBar2 = "全国销量前十城市";
let titleofBar4 = "请选择城市："
let ProvinceSale = [];
let dropdownValues = ref([]);
let dropdownValue = ref(null);
let dropdownValuePros = ref([]);
let dropdownValuePro = ref(null);
let chartInstance = null;

async function loadInitialData() {
    try {
        titleofBar3 = "近半年全国销量前十车系";
        titleofBar2 = "近半年全国销量前十城市";
        const ProvinceSaleResponse = await getProvinces();
        ProvinceSale = ProvinceSaleResponse.data.data;
        // 获取前10省份数据
        const provinceResponse = await getTopProvinces();
        barData.value = formatBarData(provinceResponse.data.data);

        // 获取地区销量数据
        const regionResponse = await getRegionSales();
        pieData.value = formatPieData(regionResponse.data.data);

        //获取全国前10城市数据
        const cityResponse = await getTopCities();
        barData2.value = formatBarData2(cityResponse.data.data);

        // 获取全国品牌销量数据、、change了  
        const seriesResponse = await getTopSeries();
        barData3.value = formatBarData3(seriesResponse.data.data);

        // // 获取车型销量数据
        // const vehicleResponse = await getVehicleSales();
        // barData4.value = formatPieData(vehicleResponse.data.data);

        // 获取所有城市数据
        const citiesResponse = await getAllCities();
        const allCities = citiesResponse.data.data;

        // 更新 dropdownValues
        dropdownValues.value = allCities.map(city => ({ name: city, code: city }));
        // 获取所有省份数据
        const provinces = ProvinceSale.map(province => ({
            name: province.province,
            code: province.province
        }));
        // 添加 '全国' 选项到省份列表的前面
        dropdownValuePros.value = [
            { name: '全国', code: '全国' },
            ...provinces
        ];
    } catch (error) {
        console.error("Error loading initial data: ", error);
    }
}

function getGradientColor(value, maxValue) {
    const green = { r: 0, g: 128, b: 0 };
    const orange = { r: 255, g: 165, b: 0 };
    const red = { r: 255, g: 0, b: 0 };
    const ratio = value / maxValue;

    let r, g, b;

    if (ratio >= 0.5) {
        // 从绿色到橙色的渐变
        const adjustedRatio = (ratio - 0.5) / 0.5;
        r = Math.round(orange.r - (orange.r - green.r) * adjustedRatio);
        g = Math.round(orange.g - (orange.g - green.g) * adjustedRatio);
        b = Math.round(orange.b - (orange.b - green.b) * adjustedRatio);
    } else {
        // 从橙色到红色的渐变
        const adjustedRatio = ratio / 0.5;
        r = Math.round(red.r - (red.r - orange.r) * adjustedRatio);
        g = Math.round(red.g - (red.g - orange.g) * adjustedRatio);
        b = Math.round(red.b - (red.b - orange.b) * adjustedRatio);
    }

    // 返回颜色的 rgb 代码
    return `rgb(${r}, ${g}, ${b})`;
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
    pieOptions.value = {
        plugins: {
            legend: {
                labels: {
                    usePointStyle: true,
                    color: textColor
                }
            },
            tooltip: {
                callbacks: {
                    label: function (context) {
                        // 获取数据项的标签和数据值
                        const label = context.label || '';
                        const value = context.raw || 0;
                        // 计算总值
                        const total = context.chart.data.datasets[0].data.reduce((acc, curr) => acc + curr, 0);

                        // 计算百分比
                        const percentage = total > 0 ? ((value / total) * 100).toFixed(2) : 0;

                        return `销量: ${value} (${percentage}%)`;
                    }
                }
            }
        }
    };

    barOptions10.value = {
        plugins: {
            legend: {
                labels: {
                    fontColor: textColor
                }
            }
            // tooltip: {
            //     callbacks: {
            //         label: function (context) {
            //             console.log(context.raw);
            //             const value = context.raw || 0;
            //             const ranking = context.raw.ranking || 0;
            //             const percentage = context.raw.percentage || 0;

            //             return `销量: ${value} (${percentage}%)\n排名：${ranking}`;
            //         }
            //     }
            // }
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

function formatBarData(data) {
    return {
        labels: data.map(item => item.province),
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
function formatBarData2(data) {
    return {
        labels: data.map(item => item.region),
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

function formatBarData3(data) {
    return {
        labels: data.map(item => item.series),
        datasets: [
            {
                label: '销量',
                backgroundColor: ["#e95938", "#ff6519", "#f28104", "#ebaf26", "#d4b640", "#b6b34f", "#94a949", "#729a19", "#628f00", "#339a33"],
                borderColor: getComputedStyle(document.documentElement).getPropertyValue('--p-primary-500'),
                data: data.map(item => item.totalSale)
            }
        ]
    };
}

function formatPieData(data) {
    const documentStyle = getComputedStyle(document.documentElement);
    return {
        labels: ['华北地区', '华南地区', '华中地区', '东北地区', '西北地区', '西南地区', '华东地区'],
        datasets: [
            {
                data: data.map(item => item.totalSale),
                backgroundColor: [documentStyle.getPropertyValue('--p-indigo-500'), documentStyle.getPropertyValue('--p-purple-500'), documentStyle.getPropertyValue('--p-teal-500'), documentStyle.getPropertyValue('--p-red-500'), documentStyle.getPropertyValue('--p-blue-500'), documentStyle.getPropertyValue('--p-yellow-500'), documentStyle.getPropertyValue('--p-orange-500')],
                hoverBackgroundColor: [documentStyle.getPropertyValue('--p-indigo-400'), documentStyle.getPropertyValue('--p-purple-400'), documentStyle.getPropertyValue('--p-teal-400'), documentStyle.getPropertyValue('--p-red-400'), documentStyle.getPropertyValue('--p-blue-400'), documentStyle.getPropertyValue('--p-yellow-400'), documentStyle.getPropertyValue('--p-orange-400')],
            }
        ],
    };
}

async function loadMapData() {
    const response = await fetch('https://geo.datav.aliyun.com/areas/bound/100000_full.json');
    const chinaGeoJson = await response.json();

    echarts.registerMap('china', chinaGeoJson);

    const provinces = [
        '北京市',     // 直辖市
        '天津市',     // 直辖市
        '上海市',     // 直辖市
        '重庆市',     // 直辖市
        '河北省',     // 省
        '山西省',     // 省
        '辽宁省',     // 省
        '吉林省',     // 省
        '黑龙江省',   // 省
        '江苏省',     // 省
        '浙江省',     // 省
        '安徽省',     // 省
        '福建省',     // 省
        '江西省',     // 省
        '山东省',     // 省
        '河南省',     // 省
        '湖北省',     // 省
        '湖南省',     // 省
        '广东省',     // 省
        '海南省',     // 省
        '四川省',     // 省
        '贵州省',     // 省
        '云南省',     // 省
        '陕西省',     // 省
        '甘肃省',     // 省
        '青海省',     // 省
        '台湾省',     // 省
        '广西壮族自治区',     // 自治区
        '内蒙古自治区',   // 自治区
        '西藏自治区',     // 自治区
        '宁夏回族自治区',     // 自治区
        '新疆维吾尔自治区',     // 自治区
        '香港特别行政区',     // 特别行政区
        '澳门特别行政区'      // 特别行政区
    ];

    const maxValue = 31; // 最大值为1000000


    // 为每个省份生成随机颜色并创建 mapData
    const mapData = provinces.map(province => {
        console.log(ProvinceSale.value);
        // 查找与 province 匹配的对象
        const saleData = ProvinceSale.find(item => item.province === province);

        // 如果找到了匹配项，则设置 value 为 totalSale，否则为 0 或者某个默认值
        const value = saleData ? saleData.totalSale : 0;

        const ranking = saleData ? saleData.ranking : 31;

        const percentage = saleData ? saleData.percentage : 0;

        return {
            name: province,
            value: value,
            ranking: ranking,  // 添加 ranking 属性
            percentage: percentage,  // 添加 percentage 属性
            itemStyle: {
                areaColor: getGradientColor(ranking, maxValue)
            }
        };
    });

    const option = {
        tooltip: {
            trigger: 'item',
            formatter: function (params) {
                return `
                ${params.name}<br/>
                销量：${params.value}<br/>
                销量排名：${params.data.ranking}<br/>
                销量占比：${params.data.percentage}%
            `;
            }
        },
        series: [
            {
                name: '中国地图',
                type: 'map',
                map: 'china',
                roam: true,
                label: {
                    show: false,
                    formatter: '{b}',
                },
                itemStyle: {
                    borderColor: '#111',  // 省份边界颜色
                    emphasis: {
                        areaColor: '#FFD700', // 悬停时的颜色
                    },
                },
                data: mapData,
            },
        ],
    };

    chartInstance.setOption(option);

    // 添加点击事件
    chartInstance.on('click', function (params) {
        console.log('点击了省份：', params.name);
        handleProvinceClick(params.name);
        // dropdownValuePro.value = params.name;

    });
}

async function handleProvinceClick(provinceName) {
    try {
        titleofBar3 = provinceName + "销量前十车系";
        titleofBar2 = provinceName + "城市销量";
        const cityResponse = await getProvinceCities(provinceName);
        // 更新 dropdownValues
        dropdownValues.value = cityResponse.data.data.map(city => ({ name: city.region, code: city.region }));
        barData2.value = formatBarData2(cityResponse.data.data);
        const seriesResponse = await getProvinceTopSeries(provinceName);
        barData3.value = formatBarData3(seriesResponse.data.data);
    } catch (error) {
        console.error("Error loading province cities data: ", error);
    }
}

async function handleCityClick(cityName) {
    try {
        titleofBar4 = cityName + "市销量前十车系"
        const seriesResponse = await getCityTopSeries(cityName);
        barData4.value = formatBarData3(seriesResponse.data.data);
    } catch (error) {
        console.error("Error loading province cities data: ", error);
    }
}

watch(
    [getPrimary, getSurface, isDarkTheme],
    () => {
        setColorOptions();
    },
    { immediate: true }
);
watch(dropdownValue, (newValue) => {
    if (newValue) {
        handleCityClick(newValue.code);

    }
}, { immediate: true });
watch(dropdownValuePro, (newValue) => {
    if (newValue) {
        if (newValue.name === '全国') {
            loadInitialData();
        } else {
            handleProvinceClick(newValue.code);
        }
    }
}, { immediate: true });

onMounted(async () => {
    setColorOptions();
    chartInstance = echarts.init(chartRef.value, { width: '65%', height: '100%' });
    await loadInitialData();
    await loadMapData();
});
</script>

<template>
    <Fluid class="grid grid-cols-12 gap-8">
        <div class="col-span-12 xl:col-span-6">
            <div class="card flex flex-col items-center">
                <div class="font-semibold text-xl mb-4">近半年中国地区总体销量占比</div>
                <Chart type="pie" :data="pieData" :options="pieOptions" style="width: 55%; height: 400px;"></Chart>
            </div>
        </div>
        <div class="col-span-12 xl:col-span-6">
            <!-- <Select v-model="dropdownValue" :options="dropdownValues" optionLabel="name" placeholder="选择年份" style="margin-bottom: 40px;"/> -->
            <div class="card">
                <div class="font-semibold text-xl mb-4">近半年中国汽车销售量前十省份</div>
                <Chart type="bar" :data="barData" :options="barOptions10" style="width: 100%; height: 400px;"></Chart>
            </div>
        </div>
        <div class="col-span-12 xl:col-span-6">
            <div class="card">
                <!-- <div class="font-semibold text-xl mb-4">中国地图，点击可以切换省份</div> -->
                <div ref="chartRef" style="width: 100%; height: 440px;"></div>
            </div>
        </div>
        <div class="col-span-12 xl:col-span-6">
            <div class="card">
                <div class="font-semibold text-xl mb-4">{{ titleofBar2 }}</div>
                <Chart type="bar" :data="barData2" :options="barOptions" style="width: 100%; height: 400px;"></Chart>
            </div>
        </div>
        <div class="col-span-12 xl:col-span-6">
            <div class="card flex flex-col items-center">
                <div class="flex items-center justify-between mb-4">
                    <div class="font-semibold text-xl mb-4">{{ titleofBar3 }}</div>
                    <div>
                        <Select v-model="dropdownValuePro" :options="dropdownValuePros" optionLabel="name"
                            placeholder="选择省份" style="margin-bottom: 0; margin-left: 10px;" />
                    </div>
                </div>
                <Chart type="bar" :data="barData3" :options="barOptions" style="width: 100%; height: 400px;"></Chart>
            </div>
        </div>
        <div class="col-span-12 xl:col-span-6">
            <div class="card flex flex-col items-center">
                <div class="flex items-center justify-between mb-4">
                    <div class="font-semibold text-xl mb-4">{{ titleofBar4 }}</div>
                    <div>
                        <Select v-model="dropdownValue" :options="dropdownValues" optionLabel="name" placeholder="选择城市"
                            style="margin-bottom: 0; margin-left: 10px;" />
                    </div>
                </div>
                <Chart type="bar" :data="barData4" :options="barOptions" style="width: 100%; height: 400px;"></Chart>
                <!-- <Chart type="pie" :data="barData4" :options="pieOptions" style="width: 100%; height: 400px;"></Chart> -->
            </div>
        </div>
    </Fluid>
</template>
