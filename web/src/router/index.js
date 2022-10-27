import { createRouter, createWebHistory } from 'vue-router'
import PKIndexView from '@/views/pk/PKIndexView'
import RankListIndexView from '@/views/ranklist/RankListIndexView'
import RecordIndexView from '@/views/record/RecordIndexView'
import NotFoundView from '@/views/error/NotFoundView'
import UserBotIndexView from '@/views/user/bot/UserBotIndexView'
import UserAccountLoginView from '@/views/user/account/UserAccountLoginView'
import UserAccountRegisterView from '@/views/user/account/UserAccountRegisterView'


const routes = [
{
    path: "/",
    redirect: "/pk/",
    name: "home"
},
{
    path: "/pk/",
    name: "pk_index",
    component: PKIndexView,
},
{
    path: "/record/",
    name: "record_index",
    component: RecordIndexView,
},
{
    path: "/ranklist/",
    name: "ranklist_index",
    component: RankListIndexView,
},
{
    path: "/user/bot/",
    name: "user_bot_index",
    component: UserBotIndexView,
},

{
    path: "/user/account/login/",
    name: "user_account_login",
    component: UserAccountLoginView,
},

{
    path: "/user/account/register/",
    name: "user_account_register",
    component: UserAccountRegisterView,
},

{
    path: "/404/",
    name: "404",
    component: NotFoundView,
},
{
    path: "/:catchAll(.*)",
    redirect: "/404/"
}
]

const router = createRouter({
history: createWebHistory(),
routes
})

export default router
