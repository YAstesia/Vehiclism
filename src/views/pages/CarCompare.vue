<script setup>
import { useLayout } from '@/layout/composables/layout';
import { CountryService } from '@/service/CountryService';
import { computed, onMounted, ref, watch } from 'vue';

const { getPrimary, getSurface, isDarkTheme } = useLayout();
const barData = ref(null);
const barOptions = ref(null);
const selectedAutoValue = ref(null);
const selectedAutoValue2 = ref(null);
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
                <label for="name2">车型A</label>
                <AutoComplete v-model="selectedAutoValue" :suggestions="autoFilteredValue" optionLabel="name"
                    placeholder="Search" dropdown multiple display="chip" @complete="searchCountry($event)" />
            </div>
            <div class="flex flex-col grow basis-0 gap-2">
                <label for="email2">车型B</label>
                <AutoComplete v-model="selectedAutoValue2" :suggestions="autoFilteredValue" optionLabel="name"
                    placeholder="Search" dropdown multiple display="chip" @complete="searchCountry($event)" />
            </div>
        </div>
    </div>
    <div class="col-span-12 xl:col-span-6">
        <div class="card">
            <div class="font-semibold text-xl mb-4">Bar</div>
            <Chart type="bar" :data="barData" :options="barOptions"></Chart>
        </div>
    </div>
    <div class="grid grid-cols-12 gap-8 mt-8">
        <div class="col-span-12 xl:col-span-6">
            <div class="card">
                <div class="font-semibold text-2xl mb-6 text-center">车型详情</div>
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
        <div class="col-span-12 xl:col-span-6">
            <div class="card">
                <div class="font-semibold text-2xl mb-6 text-center">车型详情</div>
                <div class="w-full overflow-x-auto">
                    <table class="min-w-full border-2 border-gray-300 divide-y divide-gray-300">
                        <tbody class="bg-white divide-y divide-gray-300">
                            <tr v-for="(row, index) in rows2" :key="index">
                                <td v-for="(item, i) in row" :key="i" class="px-4 py-2 text-center">
                                    {{ item || '' }}
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</template>
