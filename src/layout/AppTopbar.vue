<script setup>
import { chatAll, chatProvince, chatSeries } from '@/api';
import { useLayout } from '@/layout/composables/layout';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { useToast } from 'primevue/usetoast';
import { nextTick, ref } from 'vue';
import { useRouter } from 'vue-router';
import AppConfigurator from './AppConfigurator.vue';

const { onMenuToggle, toggleDarkMode, isDarkTheme } = useLayout();

const router = useRouter();
const showTooltip = ref(false);
const toast = useToast();

const handleLogout = () => {
    router.push('/');
    localStorage.removeItem('user_id');
    localStorage.removeItem('user_name');
    localStorage.removeItem('user_phone');
    localStorage.removeItem('user_email');
    localStorage.removeItem('searchQuery');
    localStorage.removeItem('salesQuery');
    localStorage.removeItem('currentPage');
    localStorage.removeItem('trim1');
    localStorage.removeItem('trim2');
    localStorage.removeItem('trim3');
    localStorage.removeItem('trim4');
    localStorage.removeItem('trim5');
};


function showSuccess() {
    toast.add({ severity: 'success', summary: '欢迎来到Vehiclism汽车主义大数据平台！', detail: '我们希望这个平台能满足您的所有需求。\n 如果有任何问题请致电：17321612271', life: 5000 });
}

// 定义响应式变量
const showWindow = ref(false);
const activeButton = ref('A');
const inputValue = ref('');
const dropdownValue = ref(null);
const smallInputValue = ref('');
const responseData = ref('');
const dropdownOptions = ref([
    { name: '北京市', code: '北京市' },
    { name: '天津市', code: '天津市' },
    { name: '上海市', code: '上海市' },
    { name: '重庆市', code: '重庆市' },
    { name: '河北省', code: '河北省' },
    { name: '山西省', code: '山西省' },
    { name: '辽宁省', code: '辽宁省' },
    { name: '吉林省', code: '吉林省' },
    { name: '黑龙江省', code: '黑龙江省' },
    { name: '江苏省', code: '江苏省' },
    { name: '浙江省', code: '浙江省' },
    { name: '安徽省', code: '安徽省' },
    { name: '福建省', code: '福建省' },
    { name: '江西省', code: '江西省' },
    { name: '山东省', code: '山东省' },
    { name: '河南省', code: '河南省' },
    { name: '湖北省', code: '湖北省' },
    { name: '湖南省', code: '湖南省' },
    { name: '广东省', code: '广东省' },
    { name: '海南省', code: '海南省' },
    { name: '四川省', code: '四川省' },
    { name: '贵州省', code: '贵州省' },
    { name: '云南省', code: '云南省' },
    { name: '陕西省', code: '陕西省' },
    { name: '甘肃省', code: '甘肃省' },
    { name: '青海省', code: '青海省' },
    { name: '台湾省', code: '台湾省' },
    { name: '内蒙古自治区', code: '内蒙古自治区' },
    { name: '广西壮族自治区', code: '广西壮族自治区' },
    { name: '西藏自治区', code: '西藏自治区' },
    { name: '宁夏回族自治区', code: '宁夏回族自治区' },
    { name: '新疆维吾尔自治区', code: '新疆维吾尔自治区' },
    { name: '香港特别行政区', code: '香港特别行政区' },
    { name: '澳门特别行政区', code: '澳门特别行政区' }
]);
const popupStyle = ref({});  // 用于动态调整窗口位置
const button = ref(null);    // 引用按钮元素

// 切换窗口显示状态
const toggleWindow = async () => {
    showWindow.value = !showWindow.value;

    // 弹窗显示时，动态调整弹窗位置
    if (showWindow.value) {
        await nextTick(); // 等待 DOM 更新
        // const buttonRect = button.value.getBoundingClientRect();
        popupStyle.value = {
            position: 'absolute',
            top: `0px`,  // 按钮下方
            right: `0px`,
            zIndex: 9999,                   // 保证在页面顶层
        };
    }
};

// 切换按钮状态
const setActiveButton = (button) => {
    activeButton.value = button;
};

// 发送数据到后端
const sendData = async () => {
    responseData.value = "等待回复…………";
    if (activeButton.value === 'A') {
        const response = await chatAll({ prompt: inputValue.value });
        if (response.data.success) { responseData.value = response.data.data; }
        else {
            responseData.value = "网络错误，功能暂时无法使用！";
        }
    } else if (activeButton.value === 'B') {
        const response = await chatProvince({ region: dropdownValue.value.name, saleGroup: smallInputValue.value });
        if (response.data.success) { responseData.value = response.data.data; }
        else {
            responseData.value = "网络错误，功能暂时无法使用！";
        }
    } else if (activeButton.value === 'C') {
        const response = await chatSeries({ seriesName: inputValue.value });
        if (response.data.success) { responseData.value = response.data.data; }
        else {
            responseData.value = "网络错误，功能暂时无法使用！";
        }
    }
};

// 清空对话框内容
const clearResponse = () => {
    responseData.value = '';
};

const clearInput = () => {
    inputValue.value = "";
    smallInputValue.value = "";
};

</script>

<template>
    <div class="layout-topbar">
        <div class="layout-topbar-logo-container">
            <button class="layout-menu-button layout-topbar-action" @click="onMenuToggle">
                <i class="pi pi-bars"></i>
            </button>
            <div @click="showSuccess" class="layout-topbar-logo" role="button" tabindex="0">
                <img src="/src/assets/Vehiclism_logo.png" alt="Logo" class="logo-image">
                <em class="art-text">Vehiclism汽车主义</em>
            </div>
        </div>

        <div class="layout-topbar-actions">
            <div class="layout-config-menu">
                <button type="button" class="layout-topbar-action" @click="toggleWindow">
                    <font-awesome-icon icon="fa-solid fa-robot" />
                </button>
                <!-- 弹出窗口 -->
                <div v-if="showWindow" class="popup-window">
                    <div class="font-semibold text-xl mb-2 text-center" style="color: black;">🤖 Vehiclism小助手</div>
                    <!-- textarea 展示后端数据 -->
                    <Textarea v-model="responseData" placeholder="" style="height: 500px; width: 100%; resize: none;"
                        readonly />

                    <!-- A, B, C 切换按钮 -->
                    <div class="button-group">
                        <Button type="button" class="mr-2 mb-2" :disabled="activeButton === 'A'"
                            @click="setActiveButton('A')" label="智能问答" style="margin-right: 20px;" />
                        <Button type="button" class="mr-2 mb-2" :disabled="activeButton === 'B'"
                            @click="setActiveButton('B')" label="个性化定制建议" style="margin-right: 20px;" />
                        <Button type="button" class="mr-2 mb-2" :disabled="activeButton === 'C'"
                            @click="setActiveButton('C')" label="提供销售方案" />
                    </div>

                    <!-- A 或 C 激活时显示 -->
                    <div v-if="activeButton === 'A'">
                        <InputText type="text" v-model="inputValue" placeholder="请在此输入想提问的内容，例如“什么是发动机排量？”"
                            style="width: 100%;" />
                    </div>

                    <div v-if="activeButton === 'C'">
                        <InputText type="text" v-model="inputValue" placeholder="请在此输入想要分析的车系" style="width: 100%;" />
                    </div>

                    <!-- B 激活时显示 -->
                    <div v-if="activeButton === 'B'">
                        <Select v-model="dropdownValue" style="width: 40%;" :options="dropdownOptions"
                            optionLabel="name" placeholder="选择省份" append-to="self">
                        </select>
                        <InputText type="text" v-model="smallInputValue" placeholder="输入您的身份/职业" style="width: 60%;" />
                    </div>

                    <!-- 发送和清空按钮 -->
                    <div class="action-buttons">
                        <Button type="button" class="mr-2 mb-2" @click="sendData">发送提问</button>
                        <Button type="button" class="mr-2 mb-2" @click="clearInput"
                            style="background-color: crimson; margin-left:70px;">清空输入框</button>
                        <Button type="button" class="mr-2 mb-2" @click="clearResponse"
                            style="background-color: crimson;">清空对话框</button>
                    </div>
                </div>
                <button type="button" class="layout-topbar-action" @click="toggleDarkMode">
                    <i :class="['pi', { 'pi-moon': isDarkTheme, 'pi-sun': !isDarkTheme }]"></i>
                </button>
                <div class="relative">
                    <button
                        v-styleclass="{ selector: '@next', enterFromClass: 'hidden', enterActiveClass: 'animate-scalein', leaveToClass: 'hidden', leaveActiveClass: 'animate-fadeout', hideOnOutsideClick: true }"
                        type="button" class="layout-topbar-action layout-topbar-action-highlight">
                        <i class="pi pi-palette"></i>
                    </button>
                    <AppConfigurator />
                </div>
            </div>

            <button class="layout-topbar-menu-button layout-topbar-action"
                v-styleclass="{ selector: '@next', enterFromClass: 'hidden', enterActiveClass: 'animate-scalein', leaveToClass: 'hidden', leaveActiveClass: 'animate-fadeout', hideOnOutsideClick: true }">
                <i class="pi pi-ellipsis-v"></i>
            </button>

            <div class="layout-topbar-menu hidden lg:block">
                <div class="layout-topbar-menu-content">
                    <button type="button" class="layout-topbar-action" @mouseover="showTooltip = true"
                        @mouseleave="showTooltip = false" @click="handleLogout">
                        <i class="pi pi-sign-out"></i>
                        <span>退出登录</span>
                        <div v-if="showTooltip" class="tooltip">退出登录</div>
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.layout-topbar-action {
    position: relative;
}

.tooltip {
    position: absolute;
    top: 100%;
    left: 50%;
    transform: translateX(-50%);
    background-color: #333;
    color: #fff;
    padding: 5px;
    border-radius: 3px;
    white-space: nowrap;
    font-size: 12px;
    z-index: 10;
    pointer-events: none;
}
</style>
<style scoped>
.art-text {
    display: inline-block;
    white-space: nowrap;
    /* 强制文本在同一行内显示 */
    font-family: 'Arial Unicode MS', sans-serif;
    /* 或者使用其他艺术字体 */
    font-size: 1.0em;
    /* 放大字体*/
    color: var(hoverBackgroundColor);
    /* 字体颜色 */
    /* text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5); */
    /* 添加阴影 */
    font-weight: bold;
    /* 加粗字体 */
    letter-spacing: 1px;
    /* 字母间距 */
    transition: transform 0.3s ease-in-out;
    /* 过渡动画 */
}

.art-text:hover {
    transform: scale(1.1);
    /* 鼠标悬停放大 */
}

.popup-window {
    position: absolute;
    top: 60px;
    background: white;
    border: 1px solid #000000;
    padding: 20px;
    right: 10px;
    width: 400px;
    height: 700px;
}

.button-group button {
    margin-right: 10px;
}

.action-buttons {
    margin-top: 10px;
}

.logo-image {
    max-width: 100%;
    height: 45px;
    margin-top: 10%;
    margin-bottom: 10%;
}
</style>