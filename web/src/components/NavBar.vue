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
        <ul class="navbar-nav">
            <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="navbarScrollingDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                yixiangxiao
            </a>
            <ul class="dropdown-menu" aria-labelledby="navbarScrollingDropdown">
                <li><router-link class="dropdown-item" :to="{name: 'user_bot_index'}">My Bot</router-link></li>
                <li><hr class="dropdown-divider"></li>
                <li><a class="dropdown-item" href="#">Logout</a></li>
            </ul>
            </li>
        </ul>
        </div>
    </div>
    </nav>
</template>

<script>
import { useRoute } from 'vue-router';
import { computed } from 'vue';

export default {
    setup() {
        const route = useRoute();
        let route_name = computed(() => route.name);
        return {
            route_name
        }
    }
}
</script>

<style scoped>


</style>