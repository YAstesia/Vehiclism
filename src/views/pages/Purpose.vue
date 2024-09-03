<script setup>
import { useLayout } from '@/layout/composables/layout';
import { onMounted, ref, watch } from 'vue';

const { getPrimary, getSurface, isDarkTheme } = useLayout();

const pieData = ref(null);
const pieOptions = ref(null);

onMounted(() => {
    setColorOptions();
});

function setColorOptions() {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');

    pieData.value = {
        labels: ['A', 'B', 'C'],
        datasets: [
            {
                data: [540, 325, 702],
                backgroundColor: [documentStyle.getPropertyValue('--p-indigo-500'), documentStyle.getPropertyValue('--p-purple-500'), documentStyle.getPropertyValue('--p-teal-500')],
                hoverBackgroundColor: [documentStyle.getPropertyValue('--p-indigo-400'), documentStyle.getPropertyValue('--p-purple-400'), documentStyle.getPropertyValue('--p-teal-400')]
            }
        ]
    };

    pieOptions.value = {
        plugins: {
            legend: {
                labels: {
                    usePointStyle: true,
                    color: textColor
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
        }
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
    <IconField iconPosition="left">
        <InputText type="text" placeholder="输入车系" />
        <Button type="button" class="mr-2 mb-2" label="查询" icon="pi pi-search" iconPos="right" :loading="loading[1]"
            @click="load(1)" style="margin-left: 20px;" />
    </IconField>

    <div class="col-span-12 xl:col-span-6">
        <div class="card flex flex-col items-center">
            <div class="font-semibold text-xl mb-4">购车目的饼状图</div>
            <Chart type="pie" :data="pieData" :options="pieOptions" style="width: 600px; height: 600px;"></Chart>
        </div>
    </div>
</template>
