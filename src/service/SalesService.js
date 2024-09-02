import { getSalesData } from '@/api'; // 导入 api.js 中定义的函数

export const SaleService = {
    async getData() {
        return getSalesData();
    },

    async getSalesSmall() {
        const data = await this.getData();
        return data.slice(0, 10);
    },

    async getSalesMedium() {
        const data = await this.getData();
        return data.slice(0, 50);
    },

    async getSalesLarge() {
        const data = await this.getData();
        return data.slice(0, 200);
    },

    async getSalesXLarge() {
        return this.getData();
    },

    getSales(params) {
        const queryParams = params
            ? Object.keys(params)
                  .map((k) => encodeURIComponent(k) + '=' + encodeURIComponent(params[k]))
                  .join('&')
            : '';

        return fetch('https://www.primefaces.org/data/Sales?' + queryParams).then((res) => res.json());
    }
};
