<script setup>
function smoothScroll(id) {
    document.body.click();
    document.querySelector(id).scrollIntoView({
        behavior: 'smooth'
    });
}
</script>

<template>
    <div class="bg-surface-0 dark:bg-surface-900">
        <div id="home" class="landing-wrapper overflow-hidden">
            <div class="py-6 px-6 mx-0 md:mx-12 lg:mx-20 lg:px-20 flex items-center justify-between relative lg:static">
                <a class="flex items-center" href="#">
                    <img src="/src/assets/Vehiclism_logo.png" alt="Logo" class="logo-image">
                    <!-- <span class="art-text">Vehiclism</span> -->
                </a>
                <Button class="lg:!hidden" text severity="secondary" rounded
                    v-styleclass="{ selector: '@next', enterFromClass: 'hidden', enterActiveClass: 'animate-scalein', leaveToClass: 'hidden', leaveActiveClass: 'animate-fadeout', hideOnOutsideClick: true }">
                    <i class="pi pi-bars !text-2xl"></i>
                </Button>
                <div
                    class="items-center bg-surface-0 dark:bg-surface-900 grow justify-between hidden lg:flex absolute lg:static w-full left-0 top-full px-12 lg:px-0 z-20 rounded-border">
                    <ul
                        class="list-none p-0 m-0 flex lg:items-center select-none flex-col lg:flex-row cursor-pointer gap-8">
                        <li>
                            <a @click="smoothScroll('#hero')"
                                class="px-0 py-4 text-surface-900 dark:text-surface-0 font-medium text-xl">
                                <span>首页</span>
                            </a>
                        </li>
                        <li>
                            <a @click="smoothScroll('#features')"
                                class="px-0 py-4 text-surface-900 dark:text-surface-0 font-medium text-xl">
                                <span>网页介绍</span>
                            </a>
                        </li>
                        <!-- <li>
                            <a @click="smoothScroll('#highlights')"
                                class="px-0 py-4 text-surface-900 dark:text-surface-0 font-medium text-xl">
                                <span>网站亮点</span>
                            </a>
                        </li> -->
                    </ul>
                    <div class="flex border-t lg:border-t-0 border-surface py-4 lg:py-0 mt-4 lg:mt-0 gap-2">
                        <Button label="登录" text as="router-link" to="/auth/login" rounded></Button>
                        <Button label="注册" as="router-link" to="/auth/register" rounded></Button>
                    </div>
                </div>
            </div>

            <div id="hero" class="flex flex-col pt-6 px-6 lg:px-20 overflow-hidden"
                style="background: linear-gradient(0deg, rgba(255, 255, 255, 0.2), rgba(255, 255, 255, 0.2)), radial-gradient(77.36% 256.97% at 77.36% 57.52%, rgb(238, 239, 175) 0%, rgb(195, 227, 250) 100%); clip-path: ellipse(150% 87% at 93% 13%)">
                <div class="mx-6 md:mx-20 mt-0 md:mt-6">
                    <h1 class="text-6xl font-bold text-gray-900 leading-tight"><span
                            class="font-light block">为您提供</span>权威的汽车大数据分析</h1>
                    <p class="font-normal text-2xl leading-normal md:mt-4 text-gray-700">从数据查询到大数据分析，我们Vehiclism应有尽有</p>
                    <Button label="进入网站" as="router-link" to="/auth/login" rounded class="!text-xl mt-8 !px-4"></Button>
                </div>
                <div class="flex justify-center md:justify-end">
                    <img rel="preload" src="/demo/images/landing/screen-2.webp" alt="Hero Image"
                        class="w-9/12 md:w-auto transform transition-transform duration-1000 ease-in-out opacity-0 translate-y-10 animate-fade-in"
                        style="margin-top: -150px;" loading="lazy" />
                </div>
            </div>

            <div id="features" class="py-6 px-6 lg:px-20 mt-8 mx-0 lg:mx-20">
                <div class="grid grid-cols-12 gap-4 justify-center" style="margin-bottom: -20px;">
                    <div class="col-span-12 text-center mt-20 mb-6">
                        <div class="text-surface-900 dark:text-surface-0 font-normal mb-2 text-4xl"
                            style="margin-top: -80px;">网页介绍</div>
                        <span class="text-muted-color text-2xl">丰富的数据查询与分析功能</span>
                    </div>

                    <div class="col-span-12 md:col-span-12 lg:col-span-4 p-0 lg:pr-8 lg:pb-8 mt-6 lg:mt-0">
                        <div
                            style="height: 160px; padding: 2px; border-radius: 10px; background: linear-gradient(90deg, rgba(253, 228, 165, 0.2), rgba(187, 199, 205, 0.2)), linear-gradient(180deg, rgba(253, 228, 165, 0.2), rgba(187, 199, 205, 0.2))">
                            <div class="p-4 bg-surface-0 dark:bg-surface-900 h-full" style="border-radius: 8px">
                                <div class="flex items-center justify-center bg-yellow-200 mb-4"
                                    style="width: 3.5rem; height: 3.5rem; border-radius: 10px">
                                    <i class="pi pi-fw pi-users !text-2xl text-yellow-700"></i>
                                </div>
                                <h5 class="mb-2 text-surface-900 dark:text-surface-0 text-xl font-semibold">便捷使用</h5>
                                <span class="text-surface-600 dark:text-surface-200">用户登录即可使用全部功能。</span>
                            </div>
                        </div>
                    </div>

                    <div class="col-span-12 md:col-span-12 lg:col-span-4 p-0 lg:pr-8 lg:pb-8 mt-6 lg:mt-0">
                        <div
                            style="height: 160px; padding: 2px; border-radius: 10px; background: linear-gradient(90deg, rgba(145, 226, 237, 0.2), rgba(251, 199, 145, 0.2)), linear-gradient(180deg, rgba(253, 228, 165, 0.2), rgba(172, 180, 223, 0.2))">
                            <div class="p-4 bg-surface-0 dark:bg-surface-900 h-full" style="border-radius: 8px">
                                <div class="flex items-center justify-center bg-cyan-200 mb-4"
                                    style="width: 3.5rem; height: 3.5rem; border-radius: 10px">
                                    <i class="pi pi-fw pi-palette !text-2xl text-cyan-700"></i>
                                </div>
                                <h5 class="mb-2 text-surface-900 dark:text-surface-0 text-xl font-semibold">简洁设计</h5>
                                <span class="text-surface-600 dark:text-surface-200">简洁的图表与数据展示界面。</span>
                            </div>
                        </div>
                    </div>

                    <div class="col-span-12 md:col-span-12 lg:col-span-4 p-0 lg:pb-8 mt-6 lg:mt-0">
                        <div
                            style="height: 160px; padding: 2px; border-radius: 10px; background: linear-gradient(90deg, rgba(145, 226, 237, 0.2), rgba(172, 180, 223, 0.2)), linear-gradient(180deg, rgba(172, 180, 223, 0.2), rgba(246, 158, 188, 0.2))">
                            <div class="p-4 bg-surface-0 dark:bg-surface-900 h-full" style="border-radius: 8px">
                                <div class="flex items-center justify-center bg-indigo-200"
                                    style="width: 3.5rem; height: 3.5rem; border-radius: 10px">
                                    <i class="pi pi-fw pi-map !text-2xl text-indigo-700"></i>
                                </div>
                                <div class="mt-6 mb-1 text-surface-900 dark:text-surface-0 text-xl font-semibold">海量数据
                                </div>
                                <span class="text-surface-600 dark:text-surface-200">大数据分析以得到可靠结果。</span>
                            </div>
                        </div>
                    </div>

                    <div class="col-span-12 md:col-span-12 lg:col-span-4 p-0 lg:pr-8 lg:pb-8 mt-6 lg:mt-0">
                        <div
                            style="height: 160px; padding: 2px; border-radius: 10px; background: linear-gradient(90deg, rgba(187, 199, 205, 0.2), rgba(251, 199, 145, 0.2)), linear-gradient(180deg, rgba(253, 228, 165, 0.2), rgba(145, 210, 204, 0.2))">
                            <div class="p-4 bg-surface-0 dark:bg-surface-900 h-full" style="border-radius: 8px">
                                <div class="flex items-center justify-center bg-slate-200 mb-4"
                                    style="width: 3.5rem; height: 3.5rem; border-radius: 10px">
                                    <i class="pi pi-fw pi-id-card !text-2xl text-slate-700"></i>
                                </div>
                                <div class="mt-6 mb-1 text-surface-900 dark:text-surface-0 text-xl font-semibold">个性化服务
                                </div>
                                <span class="text-surface-600 dark:text-surface-200">一键查看购车趋势与目的。</span>
                            </div>
                        </div>
                    </div>

                    <div class="col-span-12 md:col-span-12 lg:col-span-4 p-0 lg:pr-8 lg:pb-8 mt-6 lg:mt-0">
                        <div
                            style="height: 160px; padding: 2px; border-radius: 10px; background: linear-gradient(90deg, rgba(187, 199, 205, 0.2), rgba(246, 158, 188, 0.2)), linear-gradient(180deg, rgba(145, 226, 237, 0.2), rgba(160, 210, 250, 0.2))">
                            <div class="p-4 bg-surface-0 dark:bg-surface-900 h-full" style="border-radius: 8px">
                                <div class="flex items-center justify-center bg-orange-200 mb-4"
                                    style="width: 3.5rem; height: 3.5rem; border-radius: 10px">
                                    <i class="pi pi-fw pi-star !text-2xl text-orange-700"></i>
                                </div>
                                <div class="mt-6 mb-1 text-surface-900 dark:text-surface-0 text-xl font-semibold">丰富功能
                                </div>
                                <span class="text-surface-600 dark:text-surface-200">搜索、筛选、对比、分析。</span>
                            </div>
                        </div>
                    </div>

                    <div class="col-span-12 md:col-span-12 lg:col-span-4 p-0 lg:pb-8 mt-6 lg:mt-0">
                        <div
                            style="height: 160px; padding: 2px; border-radius: 10px; background: linear-gradient(90deg, rgba(251, 199, 145, 0.2), rgba(246, 158, 188, 0.2)), linear-gradient(180deg, rgba(172, 180, 223, 0.2), rgba(212, 162, 221, 0.2))">
                            <div class="p-4 bg-surface-0 dark:bg-surface-900 h-full" style="border-radius: 8px">
                                <div class="flex items-center justify-center bg-pink-200 mb-4"
                                    style="width: 3.5rem; height: 3.5rem; border-radius: 10px">
                                    <i class="pi pi-fw pi-moon !text-2xl text-pink-700"></i>
                                </div>
                                <div class="mt-6 mb-1 text-surface-900 dark:text-surface-0 text-xl font-semibold">切换模式
                                </div>
                                <span class="text-surface-600 dark:text-surface-200">页面可切换暗色模式或更改主题色。</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 
            <div id="highlights" class="py-6 px-6 lg:px-20 mx-0 my-12 lg:mx-20">
                <div class="text-center">
                    <div class="text-surface-900 dark:text-surface-0 font-normal mb-2 text-4xl"
                        style="margin-top: -20px;">网站亮点</div>
                    <span class="text-muted-color text-2xl">为您带来全新的大数据体验</span>
                </div>

                <div class="grid grid-cols-12 gap-4 mt-20 pb-2 md:pb-20">
                    <div class="flex justify-center col-span-12 lg:col-span-6 bg-purple-100 p-0 order-1 lg:order-none"
                        style="border-radius: 8px">
                        <img src="\src\assets\load3.png" class="w-11/12" alt="mockup mobile" loading="lazy" />
                    </div>

                    <div
                        class="col-span-12 lg:col-span-6 my-auto flex flex-col lg:items-end text-center lg:text-right gap-4">
                        <div class="leading-none text-surface-900 dark:text-surface-0 text-3xl font-normal">汽车销量数据查询与展示
                        </div>
                        <span class="text-surface-700 dark:text-surface-100 text-2xl leading-normal ml-0 md:ml-2"
                            style="max-width: 650px">还没做</span>
                    </div>
                </div>

                <div class="grid grid-cols-12 gap-4 my-20 pt-2 md:pt-20">
                    <div
                        class="col-span-12 lg:col-span-6 my-auto flex flex-col text-center lg:text-left lg:items-start gap-4">
                        <div class="leading-none text-surface-900 dark:text-surface-0 text-3xl font-normal">汽车销量数据汇总与分析
                        </div>
                        <span class="text-surface-700 dark:text-surface-100 text-2xl leading-normal mr-0 md:mr-2"
                            style="max-width: 650px">还没做</span>
                    </div>

                    <div class="flex justify-center order-1 sm:order-2 col-span-12 lg:col-span-6 bg-yellow-100 p-0"
                        style="border-radius: 8px">
                        <img src="\src\assets\load2.png" class="w-11/12" alt="mockup" loading="lazy" />
                    </div>
                </div>

                <div class="grid grid-cols-12 gap-4 mt-20 pb-2 md:pb-20">
                    <div class="flex justify-center col-span-12 lg:col-span-6 bg-purple-100 p-0 order-1 lg:order-none"
                        style="border-radius: 8px">
                        <img src="\src\assets\load1.png" class="w-11/12" alt="mockup mobile" loading="lazy" />
                    </div>

                    <div
                        class="col-span-12 lg:col-span-6 my-auto flex flex-col lg:items-end text-center lg:text-right gap-4">
                        <div class="leading-none text-surface-900 dark:text-surface-0 text-3xl font-normal">汽车数据详情展示
                        </div>
                        <span class="text-surface-700 dark:text-surface-100 text-2xl leading-normal ml-0 md:ml-2"
                            style="max-width: 650px">还没做</span>
                    </div>
                </div>
            </div> -->
        </div>
    </div>
</template>

<!-- 使用自定义 @keyframes 动效 -->
<style>
@keyframes fade-in {
    from {
        opacity: 0;
        transform: translateY(10%);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.animate-fade-in {
    animation: fade-in 1.5s ease-in-out forwards;
}

.logo-image {
    max-width: 100%;
    height: 50px;
    margin-top: 0%;
    margin-bottom: 0;
}
</style>
