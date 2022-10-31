<template>
    <div class="matchground">
        <div class="row">
            <div class="col-4">
                <div class="user-photo">
                    <img :src="$store.state.user.photo">
                </div>
                <div class="user-name">
                    {{$store.state.user.username}}
                </div>
            </div>
            <div class="col-4">
                <div class="user-select-bot">
                    <select v-model="select_bot" class="form-select" aria-label="Default select example">
                        <option value="-1" selected>Play Myself</option>
                        <option v-for="bot in bots" :key="bot.id" :value="bot.id">
                            {{bot.title}}
                        </option>
                    </select>
                </div>
            </div>
            <div class="col-4">
                <div class="user-photo">
                    <img :src="$store.state.pk.opponent_photo">
                </div>
                <div class="user-name">
                    {{$store.state.pk.opponent_username}}
                </div>
            </div>

            <div class="col-12" style="text-align: center; padding-top: 15vh;">
                <button type="button" class="btn btn-warning btn-lg" @click="clickMatchBtn">{{match_btn_info}}</button>
            </div>
        </div>

    </div>
</template>

<script>

import { ref } from 'vue';
import { useStore } from 'vuex';
import $ from 'jquery';


export default {
    components : {
    },

    setup() {
        const store = useStore();
        let match_btn_info = ref("Find Match");
        let bots = ref([]);
        let select_bot = ref(-1);
        
        const clickMatchBtn = () => {
            if (match_btn_info.value === "Find Match"){
                match_btn_info.value = "Cancel Match";
                console.log(select_bot.value);
                store.state.pk.socket.send(JSON.stringify({
                    event: "start-matching",
                    botId: select_bot.value,
                }))
            } else {
                match_btn_info.value = "Find Match";
                store.state.pk.socket.send(JSON.stringify({
                    event: "stop-matching"
                }))
            }
        }

        const refresh_bots = () => {
            $.ajax({
                url: "http://localhost:3000/user/bot/getlist/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp){
                    bots.value = resp;
                }
            })
        }

        refresh_bots();

        return {
            bots,
            match_btn_info,
            select_bot,
            clickMatchBtn,
            refresh_bots,
        }
    }
}
</script>

<style scoped>
div.matchground {
    width: 60vw;
    height: 70vh;
    margin: 40px auto;
    background-color: rgba(50, 50, 50, 0.5);
}

div.user-photo {
    text-align: center;
    padding-top: 10vh;
}

div.user-photo > img {
    border-radius: 50%;
    width: 20vh;
}

div.user-name {
    padding-top: 2vh;
    text-align: center;
    font-size: 24px;
    font-weight: 600;
    color: white;
}

div.user-select-bot {
    padding-top: 20vh;
}

div.user-select-bot > select {
    width: 60%;
    margin: 0 auto;
}
</style>