export const DashboardService = {
    getProductsData() {
        return [
            {
                rank: 1,
                series: '比亚迪',
                type: '秦plus',
                sales: 39710
            },
            {
                rank: 2,
                series: '特斯拉',
                type: 'Model Y',
                sales: 36299
            },
            {
                rank: 3,
                series: '比亚迪',
                type: '海鸥',
                sales: 34789
            },
            {
                rank: 4,
                series: '比亚迪',
                type: '宋plus新能源',
                sales: 34306
            },
            {
                rank: 5,
                series: '比亚迪',
                type: '秦L',
                sales: 32466
            },
            {
                rank: 6,
                series: '比亚迪',
                type: '元plus',
                sales: 25594
            },
            {
                rank: 7,
                series: '理想',
                type: '理想L6',
                sales: 24856
            },
            {
                rank: 8,
                series: '大众',
                type: '朗逸',
                sales: 24356
            },
            {
                rank: 9,
                series: '比亚迪',
                type: '海豹06 DM-i',
                sales: 21659
            },
            {
                rank: 10,
                series: '东风日产',
                type: '轩逸',
                sales: 21618
            },
        ];
    },

    getProductsMini() {
        return Promise.resolve(this.getProductsData().slice(0, 5));
    },

    getProductsSmall() {
        return Promise.resolve(this.getProductsData().slice(0, 10));
    },

    getProducts() {
        return Promise.resolve(this.getProductsData());
    },

    getProductsWithOrdersSmall() {
        return Promise.resolve(this.getProductsWithOrdersData().slice(0, 10));
    },

    getProductsWithOrders() {
        return Promise.resolve(this.getProductsWithOrdersData());
    }
};
