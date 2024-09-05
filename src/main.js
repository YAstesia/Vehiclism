import { createApp } from 'vue';
import App from './App.vue';
import router from './router';

import Aura from '@primevue/themes/aura';
import PrimeVue from 'primevue/config';
import ConfirmationService from 'primevue/confirmationservice';
import ToastService from 'primevue/toastservice';

import '@/assets/styles.scss';
import '@/assets/tailwind.css';

<<<<<<< HEAD
=======
// import ElementPlus from 'element-plus';
// import 'element-plus/dist/index.css';

>>>>>>> ad1fb95686863df7f51a363ab92828ee0dc69030
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
<<<<<<< HEAD
=======
// app.use(ElementPlus);
>>>>>>> ad1fb95686863df7f51a363ab92828ee0dc69030
// app.use(vuetify);
app.mount('#app');
