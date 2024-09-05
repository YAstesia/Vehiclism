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
          </th>
          <th scope="col" class="px-6 py-3">
            <div class="font-semibold text-xl">车系</div>
          </th>
          <th scope="col" class="px-6 py-3">
            <div class="font-semibold text-xl">车型</div>
          </th>
          <th scope="col" class="px-6 py-3">
            <div class="font-semibold text-xl">汽车类型</div>
          </th>
          <th scope="col" class="px-6 py-3">
            <div class="font-semibold text-xl">能源类型</div>
          </th>
          <th scope="col" class="px-6 py-3">
            <div class="font-semibold text-xl">价格</div>
          </th>
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
    <<<<<<< HEAD <el-pagination v-model:current-page="currentPage1" :page-size="100" :size="size" :disabled="disabled"
      :background="background" layout="total, prev, pager, next" :total="1000" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />

    =======
    <el-pagination v-model:current-page="currentPage1" :page-size="pageSize.value" :size="size.value"
      :disabled="disabled.value" :background="background.value" layout="total, prev, pager, next"
      :total="totalRecords.value" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    >>>>>>> ad1fb95686863df7f51a363ab92828ee0dc69030
  </div>
</template>

<script setup>
import { SearchCarTirm } from "@/api";
// import { ElPagination } from 'element-plus';
// import 'element-plus/theme-chalk/src/pagination.scss'; // 引入分页组件的样式
// import { ELMessage } from 'element-plus';
import { onMounted, ref } from 'vue';
// export default {
//   components: {
//     ElPagination, // 注册 ElPagination 组件
//   },
//   data() {
//     return {
//       currentPage1: 1,
//       // 其他数据...
//     };
//   },
//   methods: {
//     handleSizeChange(val) {
//       console.log(`每页 ${val} 条`);
//       // 这里可以处理分页大小变化时的逻辑
//     },
//     handleCurrentChange(val) {
//       console.log(`当前页: ${val}`);
//       // 这里可以处理当前页码变化时的逻辑
//     }
//   }
// };
// 定义变量
const searchQuery = ref('');
const currentPage1 = ref(1);
const pageSize = ref(10);
const pages = ref(0);
const totalRecords = ref(0);
const displayedData = ref([]);
const isLoading = ref(false);
const hasData = ref(true);
const size = ref('default');
const disabled = ref(false);
const background = ref(false);

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

// 搜索函数
const search = async () => {
  try {
    isLoading.value = true;
    const response = await SearchCarTirm(currentPage1.value, pageSize.value, searchQuery.value);
    displayedData.value = response.data.data.records || [];
    totalRecords.value = response.data.data.total || 0;
    pages.value = response.data.data.pages || 0;
    hasData.value = displayedData.value.length > 0;
  } catch (error) {
    console.error('Error fetching data:', error);
  } finally {
    isLoading.value = false;
  }
};

// 分页改变时触发搜索
// const handlePageChange = (newPage) => {
//   console.log(OK);
//   currentPage.value = newPage;
//   search();
// };

// 初始化加载数据
onMounted(() => {
  // ELMessage.success("aaa");
  search();
});

// 处理分页大小变化
const handleSizeChange = (val) => {
  console.log(`每页 ${val} 条`);
  pageSize.value = val;
  search();
};
</script>

<!-- <script setup>
import axios from 'axios';
import { onMounted, ref } from 'vue';

// 数据引用
const displayedData = ref([]);
const searchQuery = ref('');
let currentPage = 1;
let pageSize = 10;
const totalRecords = ref(0);
const api = axios.create({
  baseURL: 'http://localhost:8081', // 校园网10.208.112.75，oasis192.168.43.129
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
      pageSize: pageSize.value, -->