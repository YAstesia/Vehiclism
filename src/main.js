import { createApp } from 'vue';
import App from './App.vue';
import router from './router';

import Aura from '@primevue/themes/aura';
import PrimeVue from 'primevue/config';
import ConfirmationService from 'primevue/confirmationservice';
import ToastService from 'primevue/toastservice';

import '@/assets/styles.scss';
import '@/assets/tailwind.css';

// import ElementPlus from 'element-plus';
// import 'element-plus/dist/index.css';

// import { createVuetify } from 'vuetify';
// import 'vuetify/styles';

const app = createApp(App);
// const vuetify = createVuetify();

app.use(router);
app.use(PrimeVue, {
    theme: {
        preset: Aura,
        options: {
            darkModeSelector: '.app-dark'
        }
    }
});
app.use(ToastService);
app.use(ConfirmationService);
// app.use(ElementPlus);
// app.use(vuetify);
app.mount('#app');
