<script setup>
import { useLayout } from '@/layout/composables/layout';
import { onMounted, ref, watch } from 'vue';

const { getPrimary, getSurface, isDarkTheme } = useLayout();
const lineData = ref(null);
const barData = ref(null);
const lineOptions = ref(null);
const barOptions = ref(null);
const dropdownValues = ref([
    { name: '2015', code: '2015' },
    { name: '2016', code: '2016' },
    { name: '2017', code: '2017' },
    { name: '2018', code: '2018' },
    { name: '2019', code: '2019' },
    { name: '2020', code: '2020' },
    { name: '2021', code: '2021' },
    { name: '2022', code: '2022' },
    { name: '2023', code: '2023' },
    { name: '2024', code: '2024' },
]);
const dropdownValue = ref(null);

onMounted(() => {
    setColorOptions();
});

function setColorOptions() {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');

    barData.value = {
        labels: ['2015', '2016', '2017', '2018', '2019', '2020', '2021', '2022', '2023', '2024'],
        datasets: [
            {
                label: '(单位：百万辆)',
                backgroundColor: documentStyle.getPropertyValue('--p-primary-500'),
                borderColor: documentStyle.getPropertyValue('--p-primary-500'),
                data: [65, 59, 80, 81, 56, 55, 58, 42, 38, 56]
            },
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
        labels: ['2015', '2016', '2017', '2018', '2019', '2020', '2021', '2022', '2023', '2024'],
        datasets: [
            {
                label: '(单位：百万辆)',
                data: [65, 59, 80, 81, 56, 55, 58, 42, 38, 56],
                fill: false,
                backgroundColor: documentStyle.getPropertyValue('--p-primary-500'),
                borderColor: documentStyle.getPropertyValue('--p-primary-500'),
                tension: 0.4
            },
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
    <Fluid class="grid grid-cols-12 gap-8">
        <div class="col-span-12 xl:col-span-6">
            <div class="card">
                <div class="font-semibold text-xl mb-4">汽车年销量统计（折线图）</div>
                <Chart type="line" :data="lineData" :options="lineOptions"></Chart>
            </div>
        </div>
        <div class="col-span-12 xl:col-span-6">
            <div class="card">
                <div class="font-semibold text-xl mb-4">汽车月销量统计</div>
                <Select v-model="dropdownValue" :options="dropdownValues" optionLabel="name" placeholder="选择年份" />
                <Chart type="line" :data="lineData" :options="lineOptions"></Chart>
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
                <Select v-model="dropdownValue" :options="dropdownValues" optionLabel="name" placeholder="选择年份" />
                <Chart type="bar" :data="barData" :options="barOptions"></Chart>
            </div>
        </div>
    </Fluid>
</template>
