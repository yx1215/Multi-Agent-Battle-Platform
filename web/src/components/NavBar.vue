<template>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="container">
        <router-link class="navbar-brand" :to="{name: 'home'}">King of Bots</router-link>
        <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item">
            <router-link :class="route_name == 'pk_index' ? 'nav-link active': 'nav-link'" aria-current="page" :to="{name: 'pk_index'}">PK</router-link>
            </li>
            <li class="nav-item">
            <router-link :class="route_name == 'record_index' ? 'nav-link active': 'nav-link'"  :to="{name: 'record_index'}">Battle List</router-link>
            </li>
            <li class="nav-item">
            <router-link :class="route_name == 'ranklist_index' ? 'nav-link active': 'nav-link'"  :to="{name: 'ranklist_index'}">Rank</router-link>
            </li>
        </ul>
        <ul class="navbar-nav" v-if="$store.state.user.isLogin">
            <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="navbarScrollingDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                {{$store.state.user.username}}
            </a>
            <ul class="dropdown-menu" aria-labelledby="navbarScrollingDropdown">
                <li><router-link class="dropdown-item" :to="{name: 'user_bot_index'}">My Bot</router-link></li>
                <li><hr class="dropdown-divider"></li>
                <li><a class="dropdown-item" @click="logout">Logout</a></li>
            </ul>
            </li>
        </ul>

        <ul class="navbar-nav" v-else>
            <li class="nav-item">
            <router-link class="nav-link" :to="{name: 'user_account_login'}" role="button" aria-expanded="false">
                login
            </router-link>
            </li>
            <li class="nav-item">
            <router-link class="nav-link" :to="{name: 'user_account_register'}" role="button" aria-expanded="false">
                Register
            </router-link>
            </li>
        </ul>

        </div>
    </div>
    </nav>
</template>

<script>
import { useRoute } from 'vue-router';
import { computed } from 'vue';
import { useStore } from 'vuex';


export default {
    setup() {
        const store = useStore();
        const route = useRoute();
        let route_name = computed(() => route.name);
        const logout = () => {
            store.dispatch("logout");
        }

        return {
            route_name,
            logout,
        }
    }
}
</script>

<style scoped>


</style>