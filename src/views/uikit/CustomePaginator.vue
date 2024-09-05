<template>
    <nav aria-label="Page navigation example">
        <ul class="pagination">
            <li class="page-item" :class="{ disabled: currentPage <= 1 }">
                <a class="page-link" href="#" @click.prevent="changePage(1)">«</a>
            </li>
            <li class="page-item" :class="{ disabled: currentPage <= 1 }">
                <a class="page-link" href="#" @click.prevent="changePage(currentPage - 1)">‹</a>
            </li>
            <!-- 显示当前页码前两页、当前页码、后两页 -->
            <li v-for="page in visiblePages" :key="page" class="page-item" :class="{ active: page === currentPage }">
                <a class="page-link" href="#" @click.prevent="changePage(page)">{{ page }}</a>
            </li>
            <li class="page-item" :class="{ disabled: currentPage >= totalPages }">
                <a class="page-link" href="#" @click.prevent="changePage(currentPage + 1)">›</a>
            </li>
            <li class="page-item" :class="{ disabled: currentPage >= totalPages }">
                <a class="page-link" href="#" @click.prevent="changePage(totalPages)">»</a>
            </li>
        </ul>
    </nav>
</template>

<script>
export default {
    props: {
        currentPage: {
            type: Number,
            required: true
        },
        totalPages: {
            type: Number,
            required: true
        }
    },
    computed: {
        visiblePages() {
            const min = Math.max(this.currentPage - 2, 1);
            const max = Math.min(this.currentPage + 2, this.totalPages);
            const pages = [];

            // 添加前省略号
            if (min > 2) {
                pages.push(...[1, '...']);
            }

            // 添加中间显示的页码
            for (let i = min; i <= max; i++) {
                pages.push(i);
            }

            // 添加后省略号
            if (max < this.totalPages - 1) {
                pages.push('...', this.totalPages);
            }

            return pages;
        }
    },
    methods: {
        changePage(page) {
            if (typeof page === 'string' && page === '...') return;
            this.$emit('page-change', page);
        }
    }
};
</script>

<style scoped>
.pagination {
    display: flex;
    list-style-type: none;
    padding: 0;
    margin: 0;
}

.page-item a {
    color: #007bff;
    text-decoration: none;
}

.page-item.active .page-link {
    z-index: 1;
    color: #fff;
    background-color: #007bff;
    border-color: #007bff;
}

.page-item.disabled .page-link {
    color: #6c757d;
    pointer-events: none;
    cursor: auto;
}
</style>