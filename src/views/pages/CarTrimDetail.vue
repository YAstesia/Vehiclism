<script setup>
import { } from '@/api';
import { computed, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

const tableData = [
    { /* 表格数据1 */ },
    { /* 表格数据2 */ }
];

const router = useRouter()
const pieData = ref(null);
const pieOptions = ref(null);
const lineData = ref(null);
const lineOptions = ref(null);

onMounted(() => {
    setColorOptions();
});


const data = ref([[111, 444], [222, 555], [333, 666]]);
const detail = ref([4.13, 4.14, 2.49, 2.4, 4, 3.36, 5.00, 1.19]);
const value1 = computed(() => detail.value[0]);
const value2 = computed(() => detail.value[1]);
const value3 = computed(() => detail.value[2]);
const value4 = computed(() => detail.value[3]);
const value5 = computed(() => detail.value[4]);
const value6 = computed(() => detail.value[5]);
const value7 = computed(() => detail.value[6]);
const value8 = computed(() => detail.value[7]);


const rows = computed(() => {
    const flatData = data.value.flat(); // 将二维数组展平为一维数组
    const numberOfColumns = 4; // 每行的列数
    const result = [];

    for (let i = 0; i < flatData.length; i += numberOfColumns) {
        result.push(flatData.slice(i, i + numberOfColumns));
    }

    return result;
});

function setColorOptions() {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');

    pieData.value = {
        datasets: [
            {
                data: [79, 100],
                backgroundColor: [documentStyle.getPropertyValue('--p-indigo-500'), documentStyle.getPropertyValue('--p-purple-500')],
                hoverBackgroundColor: [documentStyle.getPropertyValue('--p-indigo-400'), documentStyle.getPropertyValue('--p-purple-400')]
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
            <Button label="返回" class="layout-menu-button layout-topbar-action" @click="goBack()"></Button>
        </div>

        <div class="card">
            <div class="flex flex-col md:flex-row gap-16 mt-6">
                <div class="md:w-1/3 p-4">
                    <div class="card p-4 border rounded shadow-lg">
                        <!-- 图片 -->
                        <img src="https://via.placeholder.com/150" alt="Image" class="w-full h-auto mb-4">

                        <!-- 两行居中的文字 -->
                        <div class="text-center mb-4">
                            <div class="font-bold" style="font-size: 24pt; margin-bottom: 15px; margin-top: 15px;">
                                东南大学校园接驳车</div>
                            <div class="text-gray-600" style="font-size: 16pt;">￥114,514 ~ ￥1,919,810</div>
                        </div>

                        <!-- 填充了字的文本框 -->
                        <Card>
                            <template v-slot:content>
                                <p class="leading-normal m-0" style="font-size: 20px;">
                                    啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊
                                </p>
                            </template>
                        </Card>
                    </div>
                </div>

                <div class="w-px bg-gray-300 mx-2"></div>

                <div class="flex flex-wrap md:w-2/3">
                    <div class="md:w-1/4 p-2">
                        <div class="card flex flex-col items-center relative">
                            <div class="flex flex-col items-center mb-4">
                                <div class="absolute top-0 left-1/2 transform -translate-x-1/2 font-bold text-xl">
                                    参数1</div>
                            </div>
                            <Knob v-model="value1" :min="0.00" :max="5.00" valueColor="MediumTurquoise"
                                rangeColor="SlateGray" readonly />
                        </div>
                    </div>
                    <div class="md:w-1/4 p-2">
                        <div class="card flex flex-col items-center relative">
                            <div class="flex flex-col items-center mb-4">
                                <div class="absolute top-0 left-1/2 transform -translate-x-1/2 font-bold text-xl">
                                    参数2</div>
                            </div>
                            <Knob v-model="value2" :min="0.00" :max="5.00" valueColor="MediumTurquoise"
                                rangeColor="SlateGray" readonly />
                        </div>
                    </div>
                    <div class="md:w-1/4 p-2">
                        <div class="card flex flex-col items-center relative">
                            <div class="flex flex-col items-center mb-4">
                                <div class="absolute top-0 left-1/2 transform -translate-x-1/2 font-bold text-xl">
                                    参数3</div>
                            </div>
                            <Knob v-model="value3" :min="0.00" :max="5.00" valueColor="MediumTurquoise"
                                rangeColor="SlateGray" readonly />
                        </div>
                    </div>
                    <div class="md:w-1/4 p-2">
                        <div class="card flex flex-col items-center relative">
                            <div class="flex flex-col items-center mb-4">
                                <div class="absolute top-0 left-1/2 transform -translate-x-1/2 font-bold text-xl">
                                    参数4</div>
                            </div>
                            <Knob v-model="value4" :min="0.00" :max="5.00" valueColor="MediumTurquoise"
                                rangeColor="SlateGray" readonly />
                        </div>
                    </div>
                    <div class="md:w-1/4 p-2">
                        <div class="card flex flex-col items-center relative">
                            <div class="flex flex-col items-center mb-4">
                                <div class="absolute top-0 left-1/2 transform -translate-x-1/2 font-bold text-xl">
                                    参数5</div>
                            </div>
                            <Knob v-model="value5" :min="0.00" :max="5.00" valueColor="MediumTurquoise"
                                rangeColor="SlateGray" readonly />
                        </div>
                    </div>
                    <div class="md:w-1/4 p-2">
                        <div class="card flex flex-col items-center relative">
                            <div class="flex flex-col items-center mb-4">
                                <div class="absolute top-0 left-1/2 transform -translate-x-1/2 font-bold text-xl">
                                    参数6</div>
                            </div>
                            <Knob v-model="value6" :min="0.00" :max="5.00" valueColor="MediumTurquoise"
                                rangeColor="SlateGray" readonly />
                        </div>
                    </div>
                    <div class="md:w-1/4 p-2">
                        <div class="card flex flex-col items-center relative">
                            <div class="flex flex-col items-center mb-4">
                                <div class="absolute top-0 left-1/2 transform -translate-x-1/2 font-bold text-xl">
                                    参数7</div>
                            </div>
                            <Knob v-model="value7" :min="0.00" :max="5.00" valueColor="MediumTurquoise"
                                rangeColor="SlateGray" readonly />
                        </div>
                    </div>
                    <div class="md:w-1/4 p-2">
                        <div class="card flex flex-col items-center relative">
                            <div class="flex flex-col items-center mb-4">
                                <div class="absolute top-0 left-1/2 transform -translate-x-1/2 font-bold text-xl">
                                    参数8</div>
                            </div>
                            <Knob v-model="value8" :min="0.00" :max="5.00" valueColor="MediumTurquoise"
                                rangeColor="SlateGray" readonly />
                        </div>
                    </div>

                    <div class="w-full border-2 border-gray-300 my-4"></div>

                    <div class="card flex flex-row w-full">
                        <div class="md:w-1/2 p-2 text-center">
                            <div class="font-bold" style="font-size: 20pt;">总销量</div>
                            <div class="font-bold" style="font-size: 20pt;">114,514</div>
                        </div>
                        <div class="md:w-1/2 p-2 text-center">
                            <div class="font-bold" style="font-size: 20pt;">总销量排行</div>
                            <div class="font-bold" style="font-size: 20pt;">2</div>
                        </div>
                    </div>

                    <div class="w-full border-2 border-gray-300 my-4"></div>

                    <div class="card flex flex-row w-full">
                        <div class="md:w-1/2 p-2 flex flex-col items-center">
                            <div class="font-bold" style="font-size: 20pt; margin-bottom: 10px;">销售趋势</div>
                            <Chart type="line" :data="lineData" :options="lineOptions"
                                style="width: 400px; height: 200px;"></Chart>
                        </div>
                        <div class="md:w-1/2 p-2 flex flex-col items-center">
                            <div class="font-bold" style="font-size: 20pt; margin-bottom: 10px;">购车目的</div>
                            <Chart type="pie" :data="pieData" :options="pieOptions"
                                style="width: 200px; height: 200px;"></Chart>
                        </div>
                    </div>
                </div>
            </div>
            <div class="font-bold" style="font-size: 20pt; margin-bottom: 20px; margin-top: 40px;">车型参数</div>
            <div class="w-full overflow-x-auto">
                <table class="min-w-full border-2 border-gray-300 divide-y divide-gray-300">
                    <tbody class="bg-white divide-y divide-gray-300">
                        <tr v-for="(row, index) in rows" :key="index">
                            <td v-for="(item, i) in row" :key="i" class="px-4 py-2 text-center">
                                {{ item || '' }}
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</template>