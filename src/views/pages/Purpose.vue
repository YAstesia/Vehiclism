<script setup>
import { useLayout } from '@/layout/composables/layout';
import { onMounted, ref, watch } from 'vue';

const { getPrimary, getSurface, isDarkTheme } = useLayout();

const pieData = ref(null);
const pieOptions = ref(null);
const barData = ref(null);
const barOptions = ref(null);

onMounted(() => {
    setColorOptions();
});

function setColorOptions() {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');

    pieData.value = {
        labels: ['A', 'B', 'C', 'D', 'E', 'F', 'G'],
        datasets: [
            {
                data: [540, 325, 702, 114, 514, 1919, 810],
                backgroundColor: [documentStyle.getPropertyValue('--p-indigo-500'), documentStyle.getPropertyValue('--p-purple-500'), documentStyle.getPropertyValue('--p-teal-500'), documentStyle.getPropertyValue('--p-red-500'), documentStyle.getPropertyValue('--p-blue-500'), documentStyle.getPropertyValue('--p-yellow-500'), documentStyle.getPropertyValue('--p-orange-500')],
                hoverBackgroundColor: [documentStyle.getPropertyValue('--p-indigo-400'), documentStyle.getPropertyValue('--p-purple-400'), documentStyle.getPropertyValue('--p-teal-400'), documentStyle.getPropertyValue('--p-red-400'), documentStyle.getPropertyValue('--p-blue-400'), documentStyle.getPropertyValue('--p-yellow-400'), documentStyle.getPropertyValue('--p-orange-400')],
            }
        ]
    };

    pieOptions.value = {
        plugins: {
            legend: {
                labels: {
                    usePointStyle: true,
                    color: textColor,
                    padding: 30,
                    position: 'top',
                }
            },
            datalabels: {
                formatter: (value, context) => {
                    return context.chart.data.labels[context.dataIndex] + ': ' + value;
                },
                color: textColor,
                align: 'end',
                anchor: 'end',
                clamp: true
            }
        },
        layout: {
            padding: {
                top: 0,    // 增加图表顶部间距
                bottom: 20  // 增大图例和图表之间的间隔
            }
        },
    };

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
                    fontColor: textColor,
                    padding: 20
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
        },
        layout: {
            padding: {
                top: 20,    // 增加图表顶部间距
                bottom: 20  // 增大图例和图表之间的间隔
            }
        },
        responsive: true             // 启用响应式
    };
}

const loading = ref([false, false, false]);

function load(index) {
    loading.value[index] = true;
    setTimeout(() => (loading.value[index] = false), 1000);
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
    <div class="page-container">
        <div class="search-container">
            <IconField iconPosition="left">
                <InputText type="text" placeholder="输入车系" />
                <Button type="button" class="mr-2 mb-2" label="查询" icon="pi pi-search" iconPos="right"
                    :loading="loading[1]" @click="load(1)" />
            </IconField>
        </div>
        <div class="grid grid-cols-12 gap-8 mt-8">
            <div class="col-span-12 xl:col-span-6">
                <div class="card">
                    <div class="font-semibold text-2xl mb-6 text-center">购车目的饼状图</div>
                    <Chart type="pie" :data="pieData" :options="pieOptions" style="width: 80%; height: 500px;"></Chart>
                </div>
            </div>
            <div class="col-span-12 xl:col-span-6">
                <div class="card flex flex-col">
                    <div class="font-semibold text-2xl mb-6">购车目的条形图</div>
                    <Chart type="bar" :data="barData" :options="barOptions"
                        style="width: 100%; height: 500px; margin-bottom: 0;"></Chart>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.search-container {
    margin-bottom: 20px;
    display: flex;
    justify-content: center;
    width: 100%;
}

.card {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20px;
    /* 增加卡片内边距 */
}

.font-semibold {
    font-size: 1.25rem;
    /* 增大标题字体 */
}

.grid {
    gap: 16px;
    /* 增大图表之间的间隔 */
}

.card+.card {
    margin-top: 16px;
    /* 增大卡片之间的间隔 */
}

.chart {
    margin: 20px 0;
    /* 增大图表上下的间隔 */
}

.chart .legend {
    margin-bottom: 910px;
    /* 增大图例和图表之间的间隔 */
}
</style>
