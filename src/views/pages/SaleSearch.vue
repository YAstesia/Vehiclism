<script setup>
import { ProductService } from '@/service/ProductService';
import { SaleService } from '@/service/SalesService';
import Paginator from 'primevue/paginator';
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

</script>

<template>
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
</template>

<style scoped lang="scss">
:deep(.p-datatable-frozen-tbody) {
    font-weight: bold;
}

:deep(.p-datatable-scrollable .p-frozen-column) {
    font-weight: bold;
}
</style>
