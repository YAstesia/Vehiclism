<script setup></script>

<template>
    <transition name="fade" mode="out-in">
      <router-view />
    </transition>
</template>

<style scoped></style>
<style>
/* 添加过渡效果 */
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s;
}
.fade-enter, .fade-leave-to /* .fade-leave-active 在 Vue 2.x 中 */ {
  opacity: 0;
}
</style>

