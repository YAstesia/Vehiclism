<script setup>
import { editEmail, editName, editPhone } from '@/api';
//import { ProductService } from '@/service/ProductService';
import { getProductImage, getProducts } from '@/api'; // 引入 api.js 中定义的函数
import { useToast } from 'primevue/usetoast';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const toast = useToast();
const products = ref([]);
const images = ref([]);
const avatarSrc = ref('https://primefaces.org/cdn/primevue/images/galleria/galleria10.jpg');
const pieData = ref(null);
const pieOptions = ref(null);
const userId = ref('');
const userName = ref('');
const userPhone = ref('');
const userEmail = ref('');
const defaultImageUrl = 'https://www.sucaijishi.com/uploadfile/2017/0510/20170510104938756.gif';
let energyTypeCount = [{ "energyType": "传统能源", "cnt": 0 }, { "energyType": "混合能源", "cnt": 0 }, { "energyType": "新能源", "cnt": 0 }];

const carouselResponsiveOptions = ref([
  {
    breakpoint: '1024px',
    numVisible: 3,
    numScroll: 3
  },
  {
    breakpoint: '768px',
    numVisible: 2,
    numScroll: 2
  },
  {
    breakpoint: '560px',
    numVisible: 1,
    numScroll: 1
  }
]);

onMounted(() => {
  //ProductService.getProductsSmall().then((data) => (products.value = data));
  userId.value = localStorage.getItem('user_id') || '';
  userName.value = localStorage.getItem('user_name') || '';
  userPhone.value = localStorage.getItem('user_phone') || '';
  userEmail.value = localStorage.getItem('user_email') || '';
  getProducts(userId.value)
    .then(data => {
      products.value = data;
      data.forEach(product => {
        getProductImage(product.id)
          .then(url => {
            if (url.data.data === "") {
              images.value[product.id] = defaultImageUrl;
            } else {
              images.value[product.id] = url.data.data;
            }
          })
          .catch(error => {
            images.value[product.id] = defaultImageUrl; // 设置默认图片 URL
          });
        let energyType = getSeverity(product.energyType);
        energyTypeCount[energyType].cnt++;
        updataChartData(energyTypeCount);
      });
    })
    .catch(error => {
      console.error('请求失败:', error.message);
    });
  //PhotoService.getImages().then((data) => (images.value = data));
  setColorOptions();
  // 从 localStorage 读取用户信息

});

// 获取能源类型
function getSeverity(status) {
  switch (status) {
    case '汽油':
      return 0;
    case '柴油':
      return 0;
    case '-':
      return 0;
    case '纯电动':
      return 2;
    case '氢燃料':
      return 2;
    case 'CNG':
      return 2;
    case '甲醇混动':
      return 2;
    case '汽油+48V轻混系统':
      return 1;
    case '插电式混合动力':
      return 1;
    case '汽油+48V轻混系统':
      return 1;
    case '增程式':
      return 1;
    case '油电混合':
      return 1;
    case '汽油+CNG':
      return 1;
    case '汽油+汽油电驱':
      return 1;
    case '柴油+48V轻混系统':
      return 1;
    case '汽油+24V轻混系统':
      return 1;
  }
}
function setColorOptions() {
  const documentStyle = getComputedStyle(document.documentElement);
  const textColor = documentStyle.getPropertyValue('--text-color');

  // pieData.value = {
  //   labels: ['纯电动汽车', '燃料电池汽车', '混合动力汽车'],
  //   datasets: [
  //     {
  //       data: [17, 24, 5],
  //       backgroundColor: [documentStyle.getPropertyValue('--p-indigo-500'), documentStyle.getPropertyValue('--p-purple-500'), documentStyle.getPropertyValue('--p-teal-500')],
  //       hoverBackgroundColor: [documentStyle.getPropertyValue('--p-indigo-400'), documentStyle.getPropertyValue('--p-purple-400'), documentStyle.getPropertyValue('--p-teal-400')]
  //     }
  //   ]
  // };

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
}
function updataChartData(energyType) {
  pieData.value = formatPieData(energyType);
}
function formatPieData(data) {
  const documentStyle = getComputedStyle(document.documentElement);
  return {
    labels: data.map(item => item.energyType),
    datasets: [{
      data: data.map(item => item.cnt),
      backgroundColor: [documentStyle.getPropertyValue('--p-red-500'), documentStyle.getPropertyValue('--p-yellow-500'), documentStyle.getPropertyValue('--p-teal-500'), documentStyle.getPropertyValue('--p-red-500'), documentStyle.getPropertyValue('--p-blue-500'), documentStyle.getPropertyValue('--p-yellow-500'), documentStyle.getPropertyValue('--p-orange-500')],
      hoverBackgroundColor: [documentStyle.getPropertyValue('--p-red-400'), documentStyle.getPropertyValue('--p-yellow-400'), documentStyle.getPropertyValue('--p-teal-400'), documentStyle.getPropertyValue('--p-red-400'), documentStyle.getPropertyValue('--p-blue-400'), documentStyle.getPropertyValue('--p-yellow-400'), documentStyle.getPropertyValue('--p-orange-400')],
    }]
  }
}
// 用于处理头像图片的更换功能
function triggerFileInput() {
  document.getElementById('file-input').click();
}

function onFileChange(event) {
  const file = event.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      avatarSrc.value = e.target.result; // 更新 avatarSrc 的值
    };
    reader.readAsDataURL(file);
  }
}

const fields = ref([
  {
    id: 'username',
    label: '用户名',
    value: localStorage.getItem('user_name') || '',
    isEditable: false,
  },
  {
    id: 'email',
    label: '电子邮箱',
    value: localStorage.getItem('user_email') || '',
    isEditable: false,
  },
  {
    id: 'phone',
    label: '电话号码',
    value: localStorage.getItem('user_phone') || '',
    isEditable: false,
  },
]);

function updateFieldValue(index, event) {
  fields.value[index].value = event.target.value;
}

async function toggleEditMode(index) {
  const field = fields.value[index];
  let response = {};
  if (!field.isEditable) {
    // 当 field.isEditable 为 false 时，直接允许切换为 true，不检查后端响应
    field.isEditable = true;
    return;
  } else {
    // 当 field.isEditable 为 true 时，进行保存并检查后端响应
    localStorage.setItem(field.id, field.value);


    if (field.id === 'username') {
      response = await editName(userId.value, field.value);
    } else if (field.id === 'email') {
      response = await editEmail(userId.value, field.value);
    } else if (field.id === 'phone') {
      response = await editPhone(userId.value, field.value);
    }
  }

  if (response.data.success) {
    showSuccess(response.data.msg);
    field.isEditable = false; // 仅在成功时切换为不可编辑
  } else {
    showError(response.data.msg);
    // 保持为可编辑状态，不执行切换
  }
}

function showSuccess(message) {
  toast.add({ severity: 'success', summary: '修改成功', detail: message, life: 5000 });
}

function showError(message) {
  toast.add({ severity: 'error', summary: '修改失败', detail: message, life: 5000 });
}

const navigateToCarTirmDetail = (item) => {
  router.push(`/typedetail/${item.tirm}`);
};

</script>

<template>
  <div class="card">
    <div class="font-semibold text-xl mb-4">我的信息</div>
    <Splitter style="height: 300px" class="mb-8">
      <SplitterPanel :size="30" :minSize="30" class="flex flex-col items-center justify-center">
        <img :src="avatarSrc" alt="Image"
          style="width: 200px; height: 200px; border-radius: 50%; margin-top: 10px; object-fit: cover; margin-bottom: 25px;"
          class="mx-auto avatar-image" />
        <input type="file" id="file-input" style="display:none;" @change="onFileChange" />
        <button class="p-button p-component p-button-primary" @click="triggerFileInput">修改头像</button>
      </SplitterPanel>
      <SplitterPanel :size="40" :minSize="40">
        <Splitter layout="vertical">
          <SplitterPanel :size="40" :minSize="40">
            <div class="card flex flex-col gap-4">
              <div v-for="(field, index) in fields" :key="index" class="flex flex-col gap-2">
                <label :for="field.id" style="font-size: 10pt; margin-top: 3px;">{{ field.label }}</label>
                <div class="flex flex-wrap items-start gap-4">
                  <InputText :id="field.id" type="text" :value="field.value" :disabled="!field.isEditable"
                    style="width: 300px; margin-right: 20px;" @input="updateFieldValue(index, $event)" />
                  <Button :label="field.isEditable ? '完成' : '修改'" :class="field.isEditable ? 'p-button-danger' : ''"
                    @click="toggleEditMode(index)" :fluid="false"></Button>
                </div>
              </div>
            </div>
          </SplitterPanel>
        </Splitter>
      </SplitterPanel>
      <SplitterPanel :size="30" :minSize="30" class="flex flex-col items-center justify-center">
        <div class="col-span-12 xl:col-span-6">
          <div class="card flex flex-col items-center">
            <div class="font-semibold text-xl mb-4" style="margin-top: 20px; margin-bottom: 10px;">我的收藏统计</div>
            <Chart type="pie" :data="pieData" :options="pieOptions" style="height: 240px; width: 240px;"></Chart>
          </div>
        </div>
      </SplitterPanel>
    </Splitter>
  </div>

  <div class="card">
    <div class="font-semibold text-xl mb-4">我的收藏</div>
    <div v-if="products.length === 0" class="text-center text-gray-500">暂未收藏汽车</div>
    <Carousel v-else :value="products" :numVisible="4" :numScroll="4" :responsiveOptions="carouselResponsiveOptions">
      <template #item="slotProps">
        <div class="border border-surface-200 dark:border-surface-700 rounded m-2 p-4">
          <div class="mb-4">
            <div class="relative mx-auto">
              <!-- E:\vscode_my_code\Vehiclism\src\assets\R-C.jpg -->
              <meta name="referrer" content="no-referrer">
              <img :src="images[slotProps.data.id] || defaultImageUrl" :alt="slotProps.data.tirm"
                class="w-full rounded" />
              <div class="dark:bg-surface-900 absolute rounded-border" style="left: 5px; top: 5px">
                <Tag :value="slotProps.data.type" />
              </div>
            </div>
          </div>
          <div class="mb-4 font-medium">{{ slotProps.data.brand }} {{ slotProps.data.series }} {{ slotProps.data.tirm }}
          </div>
          <div class="flex justify-between items-center">
            <div class="mt-0 font-semibold text-xl">{{ slotProps.data.price }} 万元</div>
            <span>
              <Button icon="pi pi-search" class="ml-2" @click="navigateToCarTirmDetail(slotProps.data)" />
            </span>
          </div>
        </div>
      </template>
    </Carousel>
  </div>
</template>
