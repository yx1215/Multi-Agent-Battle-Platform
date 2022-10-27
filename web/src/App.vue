<template>
<navbar />
<router-view/>
</template>

<script>

import Navbar from '@/components/NavBar.vue'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap'


export default {
    components: {
        Navbar
    },
    setup() {
        // $.ajax({
        //     url: "http://localhost:3000/user/account/register/",
        //     type: "post",
        //     data: {
        //         username: " ",
        //         password: "xiao",
        //         confirmedPassword: "xiao"
        //     },
        //     success(resp){
        //         console.log(resp);
        //     },
        //     error(resp){
        //         console.log(resp);
        //     }
        // })

        // $.ajax({
        //     url: "http://localhost:3000/user/account/info/",
        //     type: "get",
        //     headers: {
        //         Authorization: "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMzFlYjU2NDMxODc0Y2RiOTUxYmQwOGYxYTJmY2UzOCIsInN1YiI6IjQiLCJpc3MiOiJzZyIsImlhdCI6MTY2Njg0NDUzOSwiZXhwIjoxNjY4MDU0MTM5fQ.1o2zumbyL9wYhILuJQmGcE6Oi5IlR2IGou927Tc401Y"
        //     },
        //     success(resp){
        //         console.log(resp);
        //     },
        //     error(resp){
        //         console.log(resp);
        //     }
        // })
    }
}

</script>


<style>
body {
    background-image: url("@/assets/images/background.png");
    background-size: cover;
}
</style>
