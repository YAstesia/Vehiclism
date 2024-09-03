<script setup>
import { getProvinceCities, getRegionSales, getRegionSalesY, getTopBrands, getTopBrandsY, getTopCitiesY, getTopProvinces, getTopProvincesY, getVehicleSales, getVehicleSalesY } from '@/api'; // 导入接口
import { useLayout } from '@/layout/composables/layout';
import * as echarts from 'echarts';
import { onMounted, ref, watch } from 'vue';


const { getPrimary, getSurface, isDarkTheme } = useLayout();
const pieData = ref(null);
const barData = ref(null);
const barData2 = ref(null);
const pieData2 = ref(null);
const barData3 = ref(null);
const pieOptions = ref(null);
const barOptions = ref(null);
const chartRef = ref(null);
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
let chartInstance = null;

onMounted(async () => {
    setColorOptions();
    chartInstance = echarts.init(chartRef.value);
    await loadMapData();
    await loadInitialData();
    updateCharts();
});

async function loadInitialData() {
    try {
        // 获取前10省份数据
        const provinceResponse = await getTopProvinces();
        barData.value = formatBarData(provinceResponse.data);

        // 获取地区销量数据
        const regionResponse = await getRegionSales();
        pieData.value = formatPieData(regionResponse.data);

        // 获取全国品牌销量数据
        const brandResponse = await getTopBrands();
        barData3.value = formatBarData(brandResponse.data);

        // 获取车型销量数据
        const vehicleResponse = await getVehicleSales();
        pieData2.value = formatPieData(vehicleResponse.data);
        updateChart(); // 更新图表显示

    } catch (error) {
        console.error("Error loading initial data: ", error);
    }
}

async function updateChartData(year) {
    try {
        // 获取省份销量数据
        const provinceResponse = await getTopProvincesY(year);
        barData.value = formatBarData(provinceResponse.data);

        // 获取地区销量数据
        const regionResponse = await getRegionSalesY(year);
        pieData.value = formatPieData(regionResponse.data);

        // 获取城市销量数据
        const cityResponse = await getTopCitiesY(year);
        barData2.value = formatBarData(cityResponse.data);

        // 获取品牌销量数据
        const brandResponse = await getTopBrandsY(year);
        barData3.value = formatBarData(brandResponse.data);

        // 获取车型销量数据
        const vehicleResponse = await getVehicleSalesY(year);
        pieData2.value = formatPieData(vehicleResponse.data);

        // 更新图表显示
        updateChart(); 
    } catch (error) {
        console.error("Error updating chart data: ", error);
    }
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

    // pieData.value = {
    //     labels: ['华北地区', '华南地区', '华中地区', '东北地区', '西北地区', '西南地区'],
    //     datasets: [
    //         {
    //             data: [540, 325, 702, 221, 145, 244],
    //             backgroundColor: [documentStyle.getPropertyValue('--p-indigo-500'), documentStyle.getPropertyValue('--p-purple-500'), documentStyle.getPropertyValue('--p-teal-500'), documentStyle.getPropertyValue('--p-yellow-500'), documentStyle.getPropertyValue('--p-red-500'), documentStyle.getPropertyValue('--p-pink-500')],
    //             hoverBackgroundColor: [documentStyle.getPropertyValue('--p-indigo-400'), documentStyle.getPropertyValue('--p-purple-400'), documentStyle.getPropertyValue('--p-teal-400'),, documentStyle.getPropertyValue('--p-yellow-400'), documentStyle.getPropertyValue('--p-red-400'), documentStyle.getPropertyValue('--p-pink-400')]
    //         }
    //     ]
    // };
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
}

function formatBarData(data) {
    return {
        labels: data.map(item => item.name),
        datasets: [
            {
                label: '销量',
                backgroundColor: getComputedStyle(document.documentElement).getPropertyValue('--p-primary-500'),
                borderColor: getComputedStyle(document.documentElement).getPropertyValue('--p-primary-500'),
                data: data.map(item => item.value)
            }
        ]
    };
}

function formatPieData(data) {
    return {
        labels: ['华北地区', '华南地区', '华中地区', '东北地区', '西北地区', '西南地区'],
        datasets: [
            {
                data: data,
                backgroundColor: ['#...', '#...'], // 定义颜色数组
                hoverBackgroundColor: ['#...', '#...'], // 定义悬停颜色数组
            }
        ]
    };
}

function updateChart() {
    chartInstance.setOption({
        series: [
            { data: barData.value.datasets[0].data }, // 更新柱状图1
            { data: pieData.value.datasets[0].data }, // 更新饼图1
            { data: barData2.value.datasets[0].data }, // 更新柱状图2
            { data: barData3.value.datasets[0].data }, // 更新柱状图3
            { data: pieData2.value.datasets[0].data }, // 更新饼图2
        ]
    });
}

// 生成随机颜色
function getRandomColor() {
  const letters = '0123456789ABCDEF';
  let color = '#';
  for (let i = 0; i < 6; i++) {
    color += letters[Math.floor(Math.random() * 16)];
  }
  return color;
}

async function loadMapData() {
  const response = await fetch('https://geo.datav.aliyun.com/areas/bound/100000_full.json');
  const chinaGeoJson = await response.json();

  echarts.registerMap('china', chinaGeoJson);

  const provinces = [
  '北京',     // 直辖市
  '天津',     // 直辖市
  '上海',     // 直辖市
  '重庆',     // 直辖市
  '河北',     // 省
  '山西',     // 省
  '辽宁',     // 省
  '吉林',     // 省
  '黑龙江',   // 省
  '江苏',     // 省
  '浙江',     // 省
  '安徽',     // 省
  '福建',     // 省
  '江西',     // 省
  '山东',     // 省
  '河南',     // 省
  '湖北',     // 省
  '湖南',     // 省
  '广东',     // 省
  '海南',     // 省
  '四川',     // 省
  '贵州',     // 省
  '云南',     // 省
  '陕西',     // 省
  '甘肃',     // 省
  '青海',     // 省
  '台湾',     // 省
  '广西',     // 自治区
  '内蒙古',   // 自治区
  '西藏',     // 自治区
  '宁夏',     // 自治区
  '新疆',     // 自治区
  '香港',     // 特别行政区
  '澳门'      // 特别行政区
];


  // 为每个省份生成随机颜色并创建 mapData
  const mapData = provinces.map(province => ({
    name: province,
    value: Math.floor(Math.random() * 500), // 模拟随机数据
    itemStyle: {
      areaColor: getRandomColor() // 随机颜色
    }
  }));

  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{b}<br/>数据：{c}',
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
  });
}

async function handleProvinceClick(provinceName) {
    try {
        const cityResponse = await getProvinceCities(provinceName);
        barData2.value = formatBarData(cityResponse.data);
    } catch (error) {
        console.error("Error loading province cities data: ", error);
    }
  alert(`您点击了：${provinceName}`);
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
        updateChartData(newValue.code); // 使用选中的年份更新图表数据
    }
}, { immediate: true });

</script>

<template>
    <Fluid class="grid grid-cols-12 gap-8">
        <div class="col-span-12 xl:col-span-6">
            <div class="card">
                <div class="font-semibold text-xl mb-4">中国地图，点击可以切换省份</div>
                <div ref="chartRef" style="width: 100%; height: 520px;"></div>
            </div>
        </div>
        <div class="col-span-12 xl:col-span-6">
            <Select v-model="dropdownValue" :options="dropdownValues" optionLabel="name" placeholder="选择年份" style="margin-bottom: 40px;"/>
            <div class="card">
                <div class="font-semibold text-xl mb-4">top10省份</div>
                <Chart type="bar" :data="barData" :options="barOptions"></Chart>
            </div>
        </div>
        <div class="col-span-12 xl:col-span-6">
            <div class="card flex flex-col items-center">
                <div class="font-semibold text-xl mb-4">六个地区销量占比</div>
                <Chart type="pie" :data="pieData" :options="pieOptions"></Chart>
            </div>
        </div>
        <div class="col-span-12 xl:col-span-6">
            <div class="card">
                <div class="font-semibold text-xl mb-4">默认为空（全国城市销量top?），根据地图选择的省份，显示该省份城市销量top?</div>
                <Chart type="bar" :data="barData2" :options="barOptions"></Chart>
            </div>
        </div>
        <div class="col-span-12 xl:col-span-6">
            <div class="card">
                <div class="font-semibold text-xl mb-4">默认为全国品牌销量top?，同样根据省份切换</div>
                <Chart type="bar" :data="barData3" :options="barOptions"></Chart>
            </div>
        </div>
        <div class="col-span-12 xl:col-span-6">
            <div class="card flex flex-col items-center">
                <div class="font-semibold text-xl mb-4">默认为全国车型销量占比，同样根据省份切换</div>
                <Chart type="pie" :data="pieData2" :options="pieOptions"></Chart>
            </div>
        </div>
    </Fluid>
</template>
