<template>
  <div class="card">
    <div class="font-semibold text-xl mb-4">车型查询</div>
    <div class="flex justify-between">
      <!-- 关键词搜索 -->
      <div class="flex flex-col md:flex-row gap-4">
        <InputGroup>
          <Button label="搜索" @click="search" />
          <InputText v-model="searchQuery" placeholder="Keyword" />
        </InputGroup>
      </div>
      <!-- 清空过滤器 -->
      <Button type="button" icon="pi pi-filter-slash" label="清空" outlined @click="clearFilter" />
    </div>
    <table class="w-full text-sm text-left text-gray-500 dark:text-gray-400">
      <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
        <tr>
          <th scope="col" class="px-6 py-3">
            <div class="font-semibold text-xl">品牌</div>
            <!-- <MultiSelect v-model="selectedBrands" :options="brands" optionLabel="name" placeholder="选择品牌"
              class="inline-block ml-2" /> -->
            <!-- <Button label="确定" @click="applyFilter('brands')" /> -->
          </th>
          <th scope="col" class="px-6 py-3">
            <div class="font-semibold text-xl">车系</div>
            <!-- <MultiSelect v-model="selectedSeries" :options="series" optionLabel="name" placeholder="选择车系"
              class="inline-block ml-2" /> -->
            <!-- <Button label="确定" @click="applyFilter('brands')" /> -->
          </th>
          <th scope="col" class="px-6 py-3">
            <div class="font-semibold text-xl">车型</div>
            <!-- <MultiSelect v-model="selectedTirms" :options="tirms" optionLabel="name" placeholder="选择车型"
              class="inline-block ml-2" /> -->
            <!-- <Button label="确定" @click="applyFilter('brands')" /> -->
          </th>
          <th scope="col" class="px-6 py-3">
            <div class="font-semibold text-xl">汽车类型</div>
            <!-- <MultiSelect v-model="selectedTypes" :options="carTypes" optionLabel="name" placeholder="选择汽车类型"
              class="inline-block ml-2" /> -->
            <!-- <Button label="确定" @click="applyFilter('brands')" /> -->
          </th>
          <th scope="col" class="px-6 py-3">
            <div class="font-semibold text-xl">能源类型</div>
            <!-- <MultiSelect v-model="selectedEnergyTypes" :options="energyTypes" optionLabel="name" placeholder="选择能源类型" -->
            <!-- class="inline-block ml-2" /> -->
            <!-- <Button label="确定" @click="applyFilter('brands')" /> -->
          </th>
          <th scope="col" class="px-6 py-3">
            <div class="font-semibold text-xl">价格</div>
            <!-- <InputText v-model="priceRange.min" type="number" placeholder="底价" class="inline-block mr-2" />
            <InputText v-model="priceRange.max" type="number" placeholder="顶价" class="inline-block ml-2" /> -->
            <!-- <Button label="确定" @click="applyFilter('brands')" /> -->
          </th>
          <!-- <th scope="col" class="px-6 py-3">
            <Button label="确定" @click="applyFilter('brands')" />
          </th> -->

        </tr>
      </thead>
      <tbody>
        <tr v-for="item in displayedData" :key="item.id"
          class="bg-white border-b dark:bg-gray-800 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600">
          <td class="px-6 py-4">{{ item.brand }}</td>
          <td class="px-6 py-4">{{ item.series }}</td>
          <td class="px-6 py-4">{{ item.tirm }}</td>
          <td class="px-6 py-4">{{ item.type }}</td>
          <td class="px-6 py-4">
            <Tag :value="item.energyType" :severity="getSeverity(item.status)"></Tag>
          </td>
          <td class="px-6 py-4">{{ formatCurrency(item.price) }}</td>
        </tr>
      </tbody>
    </table>
    <div v-if="!hasData" class="mt-4">找不到符合条件的数据。</div>
    <div v-if="isLoading" class="mt-4">正在筛选，请等待……</div>
    <Paginator v-model:page="currentPage" :first=1 :rows="pageSize" :totalRecords="totalRecords"
      @page-change="console.log('Page change triggered'); fetchSearchResults"></Paginator>
  </div>
</template>

<script setup>
import axios from 'axios';
import { onMounted, ref } from 'vue';

// 数据引用
const displayedData = ref([]);
const searchQuery = ref('');
const currentPage = ref(1);
const pageSize = ref(10);
const totalRecords = ref(0);
const api = axios.create({
  baseURL: 'http://192.168.43.129:8081', // 校园网10.208.112.75，oasis192.168.43.129
  headers: {
    'Content-Type': 'application/json',
  },
});
// 状态
const hasData = ref(true);
const isLoading = ref(false);
const selectedBrands = ref([]);
const selectedSeries = ref([]);
const selectedTirms = ref([]);
const selectedTypes = ref([]);
const selectedEnergyTypes = ref([]);
const priceRange = ref({ min: null, max: null });
const currentFilter = ref({});
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
function formatCurrency(value) {
  if (value === null) {
    return '暂无数据';
  }
  const formattedValue = (value).toLocaleString();
  return formattedValue + '万元';
}

// 清除过滤器
function clearFilter() {
  searchQuery.value = '';
  currentPage.value = 1;
  selectedBrands.value = [];
  selectedSeries.value = [];
  selectedTirms.value = [];
  selectedTypes.value = [];
  selectedEnergyTypes.value = [];
  priceRange.value.min = null;
  priceRange.value.max = null;
  fetchCarsWithFilter(currentFilter);
}

// 搜索
async function search() {
  currentPage.value = 1;
  fetchSearchResults();
}
// 筛选获取车型数据
async function fetchCarsWithFilter(filters) {
  isLoading.value = true;
  try {
    const response = await api.post('/search/filter', {
      pageNum: currentPage.value,
      pageSize: pageSize.value,
      ...filters,
    });
    displayedData.value = response.data.data.records;
    totalRecords.value = response.data.data.total;
    isLoading.value = false;
    hasData.value = displayedData.value.length > 0;
  } catch (error) {
    console.error('Error fetching data:', error);
    isLoading.value = false;
    hasData.value = false;
  }
}

// 获取搜索结果
async function fetchSearchResults() {
  isLoading.value = true;
  try {
    const response = await api.post('/search/keyword', {
      pageNum: currentPage.value,
      pageSize: pageSize.value,
      keyword: searchQuery.value,
    });
    displayedData.value = response.data.data.records;
    totalRecords.value = response.data.data.total;
    isLoading.value = false;
    hasData.value = displayedData.value.length > 0;
  } catch (error) {
    console.error('Error fetching search results:', error);
    isLoading.value = false;
    hasData.value = false;
  }
}

// 初始化加载
onMounted(() => {
  fetchCarsWithFilter(currentFilter);
});

</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>

<style scoped lang="scss">
:deep(.p-datatable-frozen-tbody) {
  font-weight: bold;
}

:deep(.p-datatable-scrollable .p-frozen-column) {
  font-weight: bold;
}

:deep(.p-paginator) {
  margin-top: 1rem;
}
</style>