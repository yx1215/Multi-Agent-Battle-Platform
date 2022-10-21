<template>
<div>
    <div>Bot Name: {{botname}}</div>
    <div>Bot Power: {{botpower}}</div>
</div>
<router-view/>
</template>

<script>
import $ from "jquery";
import {ref} from 'vue';


export default {
    name: "App",
    setup: () => {
        let botname = ref("");
        let botpower = ref("");
        $.ajax({
            url: "http://127.0.0.1:3000/pk/getbotinfo/",
            type: "get",
            success: resp => {
                botname.value = resp.name;
                botpower.value = resp.rating;
            }
        })
        return {
            botname,
            botpower
        }
    }
}

</script>


<style>
body {
    background-image: url("@/assets/background.png");
    background-size: cover;
}
</style>
