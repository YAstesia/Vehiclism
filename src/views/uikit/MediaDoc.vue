<script setup>
import { PhotoService } from '@/service/PhotoService';
import { ProductService } from '@/service/ProductService';
import { onMounted, ref } from 'vue';

const products = ref([]);
const images = ref([]);
const avatarSrc = ref('https://primefaces.org/cdn/primevue/images/galleria/galleria10.jpg');
const galleriaResponsiveOptions = ref([
    {
        breakpoint: '1024px',
        numVisible: 5
    },
    {
        breakpoint: '960px',
        numVisible: 4
    },
    {
        breakpoint: '768px',
        numVisible: 3
    },
    {
        breakpoint: '560px',
        numVisible: 1
    }
]);
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
    ProductService.getProductsSmall().then((data) => (products.value = data));
    PhotoService.getImages().then((data) => (images.value = data));
});

function getSeverity(status) {
    switch (status) {
        case 'INSTOCK':
            return 'success';
        case 'LOWSTOCK':
            return 'warning';
        case 'OUTOFSTOCK':
            return 'danger';
        default:
            return null;
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

function useEditableFields() {
  const fields = ref([
    { id: 'name1', label: '用户名', value: '默认用户名', isEditable: false },
    { id: 'password1', label: '密码', value: '默认密码', isEditable: false },
    { id: 'email1', label: '邮箱', value: '默认邮箱', isEditable: false },
    { id: 'phone1', label: '电话', value: '默认电话', isEditable: false },
  ]);

  async function toggleEditMode(index) {
    fields.value[index].isEditable = !fields.value[index].isEditable;
    if (!fields.value[index].isEditable) {
      // Send updated value to the backend
      try {
        const response = await fields.value[index].apiCall(fields.value[index].value);
        if (response.data.success) {
          alert(response.data.msg); // 修改成功！
        } else {
          alert(response.data.msg); // 修改失败！
        }
      } catch (error) {
        alert('修改失败，请稍后再试！');
      }
    }
  }

  function updateFieldValue(index, event) {
    fields.value[index].value = event.target.value;
  }

  return { fields, toggleEditMode, updateFieldValue };
}

// 在 setup 中调用 useEditableFields 并解构其返回值
const { fields, toggleEditMode, updateFieldValue } = useEditableFields();
</script>

<template>
    <div class="card">
        <div class="font-semibold text-xl mb-4">我的信息</div>
        <Splitter style="height: 400px" class="mb-8">
            <SplitterPanel :size="30" :minSize="10" class="flex flex-col items-center justify-center">
                <div class="font-semibold" style="font-size: 20pt; margin-bottom: 5px;">我的头像</div>
                <img 
                    :src="avatarSrc" 
                    alt="Image" 
                    style="width: 250px; height: 250px; border-radius: 50%; margin-top: 10px; object-fit: cover; margin-bottom: 20px;" 
                    class="mx-auto avatar-image" />
                <input type="file" id="file-input" style="display:none;" @change="onFileChange" />
                <button class="p-button p-component p-button-primary" @click="triggerFileInput">修改头像</button>
            </SplitterPanel>
            <SplitterPanel :size="50">
                <Splitter layout="vertical">
                    <SplitterPanel :size="50">
                    <div class="card flex flex-col gap-4">
                        <div v-for="(field, index) in fields" :key="index" class="flex flex-col gap-2">
        <label :for="field.id" style="font-size: 14pt; margin-bottom: 5px;">{{ field.label }}</label>
        <div class="flex flex-wrap items-start gap-4">
          <InputText
            :id="field.id"
            type="text"
            :value="field.value"
            :disabled="!field.isEditable"
            style="width: 300px; margin-right: 20px;"
            @input="updateFieldValue(index, $event)"
          />
          <Button
            :label="field.isEditable ? '完成' : '修改'"
            :class="field.isEditable ? 'p-button-danger' : ''"
            @click="toggleEditMode(index)"
            :fluid="false"
          ></Button>
        </div>
      </div>
                    </div>
                    </SplitterPanel>
                </Splitter>
            </SplitterPanel>
            <SplitterPanel :size="40" :minSize="30" class="flex flex-col items-center justify-center">
                //不知道放什么
            </SplitterPanel>
        </Splitter>
    </div>

    <div class="card">
        <div class="font-semibold text-xl mb-4">我的收藏</div>
        <Carousel :value="products" :numVisible="3" :numScroll="3" :responsiveOptions="carouselResponsiveOptions">
            <template #item="slotProps">
                <div class="border border-surface-200 dark:border-surface-700 rounded m-2 p-4">
                    <div class="mb-4">
                        <div class="relative mx-auto">
                            <img :src="'https://primefaces.org/cdn/primevue/images/product/' + slotProps.data.image" :alt="slotProps.data.name" class="w-full rounded" />
                            <div class="dark:bg-surface-900 absolute rounded-border" style="left: 5px; top: 5px">
                                <Tag :value="slotProps.data.inventoryStatus" :severity="getSeverity(slotProps.data.inventoryStatus)" />
                            </div>
                        </div>
                    </div>
                    <div class="mb-4 font-medium">{{ slotProps.data.name }}</div>
                    <div class="flex justify-between items-center">
                        <div class="mt-0 font-semibold text-xl">${{ slotProps.data.price }}</div>
                        <span>
                            <Button icon="pi pi-heart" severity="secondary" outlined />
                            <Button icon="pi pi-shopping-cart" class="ml-2" />
                        </span>
                    </div>
                </div>
            </template>
        </Carousel>
    </div>
</template>
