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
    <custom-pagination :currentPage="currentPage1" :totalPages="pages" @page-change="handlePageChange" />
  </div>
</template>

<script setup>
import { SearchCarTirm } from "@/api";
import CustomPagination from '@/views/uikit/CustomePaginator.vue';
import { onMounted, ref } from 'vue';

// 定义变量
const searchQuery = ref('');
const currentPage1 = ref(1);
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
  currentPage1.value = newPage;
  search();
};

// 初始化加载数据
onMounted(() => {
  search();
});
</script>

<!-- <template>
    <div class="card">
        <div class="font-semibold text-xl mb-4">销量信息查询</div>
        <DataTable
            :value="Sales1"
            :paginator="true"
            :rows="10"
            dataKey="id"
            :rowHover="true"
            filterDisplay="menu"
            :loading="loading1"
            :globalFilterFields="['name', 'country.name', 'representative.name', 'balance', 'status']"
            showGridlines
        >
            <template #header>
                <div class="flex justify-between">
                    <IconField>
                        <InputIcon>
                            <i class="pi pi-search" />
                        </InputIcon>
                        <InputText placeholder="搜索" />
                    </IconField>
                    <Button type="button" icon="pi pi-filter-slash" label="清空" outlined @click="clearFilter()" />
                </div>
            </template>

            <template #paginator="{ state }">
        <Paginator :rows=10 :first=1 :totalRecords=200>
          <template #start>
            Page: {{ state.page }}
          </template>
        </Paginator>
      </template>

            <template #empty> 找不到符合条件的数据。 </template>
            <template #loading> 正在筛选，请等待…… </template>
            <Column field="name" header="品牌" style="min-width: 12rem">
                <template #body="{ data }">
                    {{ data.brand }}
                </template>
            </Column>
            <Column header="车系" filterField="country.name" style="min-width: 12rem">
                <template #body="{ data }">
                    <div class="flex items-center gap-2">
                        <span>{{ data.series }}</span>
                    </div>
                </template>
            </Column>
            <Column header="汽车类型" filterField="representative" :showFilterMatchModes="false" :filterMenuStyle="{ width: '14rem' }" style="min-width: 14rem">
                <template #body="{ data }">
                    <div class="flex items-center gap-2">
                        <span>{{ data.type }}</span>
                    </div>
                </template>
            </Column>
            <Column header="销售时间" filterField="date" dataType="date" style="min-width: 10rem">
                <template #body="{ data }">
                    <div class="flex items-center gap-2">
                        <span>{{ data.year }}.{{ data.month }}</span>
                    </div>
                </template>
            </Column>
            <Column header="销量" filterField="balance" dataType="numeric" style="min-width: 10rem">
                <template #body="{ data }">
                    <Tag :value="data.totalSale" :severity="getSeverity(data.status)" />
                </template>
            </Column>
            <Column header="价格" field="status" :filterMenuStyle="{ width: '14rem' }" style="min-width: 12rem">
                <template #body="{ data }">
                    {{ formatCurrency(data.price) }}
                </template>
            </Column>
        </DataTable>
    </div>
</template> -->