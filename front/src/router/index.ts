import {createRouter, createWebHistory} from 'vue-router'
// import HomeView from '../views/HomeView.vue'
import WriteView from '../views/WriteView.vue'
import ListView from '../views/ListView.vue'
import ReadView from '../views/ReadView.vue'
import EditView from '../views/EditView.vue';

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: "/",
            name: "list",
            component: ListView
        },
        {
            path: "/write",
            name: "write",
            component: WriteView
        },
        {
            path: "/read/:postId",
            name: "read",
            component: ReadView,
            props: true,
        },
        {
            path: "/edit/:postId",
            name: "edit",
            component: EditView,
            props: true,
        },
    ]
})

export default router
