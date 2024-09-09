<!-- <script setup>

import { ProductService } from '@/service/ProductService';
import { SaleService } from '@/service/SalesService';
import { onBeforeMount, reactive, ref } from 'vue';


const Sales1 = ref(null);
const Sales2 = ref(null);
const Sales3 = ref(null);
const loading1 = ref(null);
const products = ref(null);
const statuses = reactive(['unqualified', 'qualified', 'new', 'negotiation', 'renewal', 'proposal']);

function getSeverity(status) {
    switch (status) {
        case 'unqualified':
            return 'danger';

        case 'qualified':
            return 'success';

        case 'new':
            return 'info';

        case 'negotiation':
            return 'warn';

        case 'renewal':
            return null;
    }
}

onBeforeMount(() => {
    ProductService.getProductsWithOrdersSmall().then((data) => (products.value = data));
    SaleService.getSalesLarge().then((data) => {
        Sales1.value = data;
        loading1.value = false;
        Sales1.value.forEach((Sale) => (Sale.date = new Date(Sale.date)));
    });
    SaleService.getSalesLarge().then((data) => (Sales2.value = data));
    SaleService.getSalesMedium().then((data) => (Sales3.value = data));

});

function formatCurrency(value) {
    return value.toLocaleString('en-US', { style: 'currency', currency: 'USD' });
}

</script> -->
<script setup>
import { SearchCarSeriesSale } from "@/api";
import CustomPagination from '@/views/uikit/CustomePaginator.vue';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

// 定义变量
const salesQuery = ref(localStorage.getItem('salesQuery') || '');
const currentPage = ref(parseInt(localStorage.getItem('currentPage')) || 1);
const pageSize = ref(10);
const pages = ref(0);
const totalRecords = ref(0);
const displayedData = ref([]);
const isLoading = ref(false);
const hasData = ref(true);

// 状态严重性映射
const statuses = ['unqualified', 'qualified', 'new', 'negotiation', 'renewal', 'proposal'];

// 获取严重性
function getSeverity(status) {
    switch (status) {
        case 'unqualified':
            return 'danger';
        case 'qualified':
            return 'success';
        case 'new':
            return 'info';
        case 'negotiation':
            return 'warn';
        case 'renewal':
            return null;
    }
}

// 格式化货币
function formatCurrency(value1, value2) {
    if (value1 === null && value2 === null) {
        return '暂无数据';
    }
    const formattedValue = (value1).toLocaleString() + '~' + (value2).toLocaleString();
    return formattedValue + '万元';
}
function formatTime(year, month) {
    if (year === null && month === null) {
        return '暂无数据';
    }
    const formattedValue = (year).toLocaleString() + '年' + (month).toLocaleString() + "月";
    return formattedValue;
}

function ClearFilter() {
    salesQuery.value = '';
    localStorage.removeItem('salesQuery');
    currentPage.value = 1;
    search();
}

const router = useRouter();
const navigateToCarSeriesDetail = (item) => {
    router.push(`/cardetail/${item.series}`);
};
// 搜索函数
const search = async () => {
    try {
        currentPage.value = 1;
        isLoading.value = true;
        localStorage.setItem('salesQuery', salesQuery.value);
        localStorage.setItem('currentPage', currentPage.value);
        const response = await SearchCarSeriesSale(currentPage.value, pageSize.value, salesQuery.value);
        displayedData.value = response.data.data.records || [];
        totalRecords.value = response.data.data.total || 0;
        pages.value = Math.ceil(totalRecords.value / pageSize.value);
        hasData.value = displayedData.value.length > 0;
    } catch (error) {
        console.error('Error fetching data:', error);
    } finally {
        isLoading.value = false;
    }
};

const searchPage = async () => {
    try {
        isLoading.value = true;
        localStorage.setItem('salesQuery', salesQuery.value);
        localStorage.setItem('currentPage', currentPage.value);
        const response = await SearchCarSeriesSale(currentPage.value, pageSize.value, salesQuery.value);
        displayedData.value = response.data.data.records || [];
        totalRecords.value = response.data.data.total || 0;
        pages.value = Math.ceil(totalRecords.value / pageSize.value);
        hasData.value = displayedData.value.length > 0;
    } catch (error) {
        console.error('Error fetching data:', error);
    } finally {
        isLoading.value = false;
    }
};


// 分页改变时触发搜索
const handlePageChange = (newPage) => {
    currentPage.value = newPage;
    searchPage();
};

// 初始化加载数据
onMounted(() => {
    searchPage();
});
</script>
<template>
    <div class="card">
        <div class="font-semibold text-xl mb-4">销量信息查询</div>
        <div class="flex justify-between">
            <!-- 关键词搜索 -->
            <div class="flex flex-col md:flex-row gap-4">
                <InputGroup>
                    <Button label="搜索" @click="search" />
                    <InputText v-model="salesQuery" placeholder="Keyword" />
                    <div v-if="isLoading" class="mt-4" style="margin-left: 20px">正在筛选，请等待……</div>
                </InputGroup>
            </div>
            <!-- 清空过滤器 -->
            <Button type="button" icon="pi pi-filter-slash" label="清空" outlined @click="ClearFilter" />
        </div>
        <table class="w-full text-sm text-left text-gray-500 dark:text-gray-400">
            <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
                <tr>
                    <th scope="col" class="px-6 py-3">
                        <div class="font-semibold text-xl">品牌</div>
                    </th>
                    <th scope="col" class="px-6 py-3">
                        <div class="font-semibold text-xl">车系</div>
                    </th>
                    <th scope="col" class="px-6 py-3">
                        <div class="font-semibold text-xl">汽车类型</div>
                    </th>
                    <th scope="col" class="px-6 py-3">
                        <div class="font-semibold text-xl">价格</div>
                    </th>
                    <th scope="col" class="px-6 py-3">
                        <div class="font-semibold text-xl">销售时间</div>
                    </th>
                    <th scope="col" class="px-6 py-3">
                        <div class="font-semibold text-xl">销量</div>
                    </th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="item in displayedData" :key="item.id"
                    class="bg-white border-b dark:bg-gray-800 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600">
                    <td class="px-6 py-4"><span class="font-bold" style="font-size: 14px;">{{ item.brand }}</span></td>
                    <td class="px-6 py-4"><span class="font-bold" style="font-size: 14px;  text-decoration: underline;"
                            @click="navigateToCarSeriesDetail(item)">{{ item.series }}</span></td>
                    <td class="px-6 py-4"><span class="font-bold" style="font-size: 14px;">{{ item.type }}</span></td>
                    <td class="px-6 py-4"><span class="font-bold" style="font-size: 14px;">{{
                        formatCurrency(item.priceMin, item.priceMax) }}</span></td>
                    <td class="px-6 py-4"><span class="font-bold" style="font-size: 14px;">{{ formatTime(item.year,
                        item.month) }}</span></td>
                    <td class="px-6 py-4"><span class="font-bold" style="font-size: 14px;">{{ item.totalSale }}</span>
                    </td>
                </tr>
            </tbody>
        </table>
        <div v-if="!hasData" class="mt-4">找不到符合条件的数据。</div>
        <custom-pagination :currentPage="currentPage" :totalPages="pages" @page-change="handlePageChange" />
    </div>
</template>


<!-- <style scoped lang="scss">
:deep(.p-datatable-frozen-tbody) {
    font-weight: bold;
}

:deep(.p-datatable-scrollable .p-frozen-column) {
    font-weight: bold;
}
</style> -->
