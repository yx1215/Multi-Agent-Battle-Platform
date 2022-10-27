import { createRouter, createWebHistory } from 'vue-router'
import PKIndexView from '@/views/pk/PKIndexView'
import RankListIndexView from '@/views/ranklist/RankListIndexView'
import RecordIndexView from '@/views/record/RecordIndexView'
import NotFoundView from '@/views/error/NotFoundView'
import UserBotIndexView from '@/views/user/bot/UserBotIndexView'
import UserAccountLoginView from '@/views/user/account/UserAccountLoginView'
import UserAccountRegisterView from '@/views/user/account/UserAccountRegisterView'
import store from '@/store/index'


const routes = [
{
    path: "/",
    redirect: "/pk/",
    name: "home",
    meta: {
        requestAuth: true,
    }
},
{
    path: "/pk/",
    name: "pk_index",
    component: PKIndexView,
    meta: {
        requestAuth: true,
    }
},
{
    path: "/record/",
    name: "record_index",
    component: RecordIndexView,
    meta: {
        requestAuth: true,
    }
},
{
    path: "/ranklist/",
    name: "ranklist_index",
    component: RankListIndexView,
    meta: {
        requestAuth: true,
    }
},
{
    path: "/user/bot/",
    name: "user_bot_index",
    component: UserBotIndexView,
    meta: {
        requestAuth: true,
    }
},

{
    path: "/user/account/login/",
    name: "user_account_login",
    component: UserAccountLoginView,
    meta: {
        requestAuth: false,
    }
},

{
    path: "/user/account/register/",
    name: "user_account_register",
    component: UserAccountRegisterView,
    meta: {
        requestAuth: false,
    }
},

{
    path: "/404/",
    name: "404",
    component: NotFoundView,
    meta: {
        requestAuth: false,
    }
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

router.beforeEach((to, from, next) => {
    let flag = 1;
    const jwt_token = localStorage.getItem("jwt_token");

    if (jwt_token) {
        store.commit("updateToken", jwt_token);
        store.dispatch("getinfo", {
            success() {

            },
            error() {
                // alert("Invalid Token, please login again.");
                store.commit("logout");
                router.push({ name: 'user_account_login' });
            }
        })
    } else {
        flag = 2;
    }
  
    if (to.meta.requestAuth && !store.state.user.isLogin){
        if (flag == 2){
            next({name: 'user_account_login'});
        } else {
            next()
        }
    } else {
        next();
    }
})

export default router
