<script setup>
import { useLayout } from '@/layout/composables/layout';
import { CountryService } from '@/service/CountryService';
import { computed, onMounted, ref, watch } from 'vue';

const { getPrimary, getSurface, isDarkTheme } = useLayout();
const barData = ref(null);
const barOptions = ref(null);
const radarData = ref(null);
const radarOptions = ref(null);
const selectedAutoValue = ref(null);
const autoFilteredValue = ref([]);
const autoValue = ref(null);

onMounted(() => {
    setColorOptions();
    CountryService.getCountries().then((data) => (autoValue.value = data));
});


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


function searchCountry(event) {
    setTimeout(() => {
        if (!event.query.trim().length) {
            autoFilteredValue.value = [...autoValue.value];
        } else {
            autoFilteredValue.value = autoValue.value.filter((country) => {
                return country.name.toLowerCase().startsWith(event.query.toLowerCase());
            });
        }
    }, 250);
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
</script>

<template>
    <div class="card">
        <div class="font-semibold text-xl" style="margin-bottom: 30px;">详情比较</div>
        <div class="flex flex-wrap gap-4">
            <div class="flex flex-col grow basis-0 gap-2">
                <label for="name2">选择车型</label>
                <div class="flex grow basis-0 gap-2 flex-row">
                    <AutoComplete class="w-3/4" v-model="selectedAutoValue" :suggestions="autoFilteredValue"
                        optionLabel="name" placeholder="选择需要进行比较的车型" dropdown multiple display="chip"
                        @complete="searchCountry($event)" />
                    <Button type="button" class="mr-2 mb-2 w-1/12" label="比较" icon="pi pi-search" iconPos="right"
                        style="margin-left: 25px;" />
                </div>
            </div>
        </div>
    </div>
    <div class="grid grid-cols-12 gap-8 mt-8">
        <div class="col-span-12 xl:col-span-6">
            <div class="card" style="height: 500px;">
                <div class="font-semibold text-xl mb-4">条形图</div>
                <Chart type="bar" :data="barData" :options="barOptions" style="margin-top: 100px;"></Chart>
            </div>
        </div>
        <div class="col-span-12 xl:col-span-6">
            <div class="card flex flex-col justify-center items-center" style="height: 500px;">
                <div class="font-semibold text-xl mb-4">雷达图</div>
                <Chart type="radar" :data="radarData" :options="radarOptions"></Chart>
            </div>
        </div>
    </div>
    <div class="grid grid-cols-12 gap-8 mt-8">
        <div class="col-span-12 xl:col-span-6">
            <div class="card" style="height: 500px;">
                <div class="font-semibold text-xl mb-4">条形图</div>
                <Chart type="bar" :data="barData" :options="barOptions" style="margin-top: 100px;"></Chart>
            </div>
        </div>
        <div class="col-span-12 xl:col-span-6">
            <div class="card" style="height: 500px;">
                <div class="font-semibold text-xl mb-4">条形图</div>
                <Chart type="bar" :data="barData" :options="barOptions" style="margin-top: 100px;"></Chart>
            </div>
        </div>
    </div>
    <div class="grid grid-cols-12 gap-8 mt-8">
        <div class="col-span-12 xl:col-span-6">
            <div class="card">
                <div class="w-full overflow-x-auto">
                    <table class="min-w-full border-2 border-gray-300 divide-y divide-gray-300">
                        <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400"
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
                                <td class="px-6" @click="navigateToCarSeriesDetail(item)"><span
                                        style="font-size: 10pt;">
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
        <div class="col-span-12 xl:col-span-6">
            <div class="card">
                <div class="w-full overflow-x-auto">
                    <table class="min-w-full border-2 border-gray-300 divide-y divide-gray-300">
                        <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400"
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
                                <td class="px-6" @click="navigateToCarSeriesDetail(item)"><span
                                        style="font-size: 10pt;">
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
</template>
