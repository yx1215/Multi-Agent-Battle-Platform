<template>
    <ContentField>
        My Bot
    </ContentField>
</template>

<script>
import ContentField from '@/components/ContentField.vue'
import $ from 'jquery'
import {useStore} from 'vuex'

export default {
    components:{
        ContentField
    },

    setup() {
        const store = useStore();
        $.ajax({
            url: "http://localhost:3000/user/bot/getlist/",
            type: "get",
            data: {
                bot_id: 3,
                title: "My Bot New",
                description: "Bot description New",
                content: "Bot code New",
            },

            headers: {
                Authorization: "Bearer " + store.state.user.token,
            },

            success(resp){
                console.log(resp);
            },

            error(resp){
                console.log(resp);
            }
        })
    }

}
</script>

<style scoped>

</style>