<template>
    <div class="container">
        <div class="row">
            <div class="col-3">
                <div class="card" style="margin-top: 20px;">
                    <div class="card-body">
                        <img :src="$store.state.user.photo" alt="" style="width: 100%;">
                    </div>
                </div>
            </div>
            <div class="col-9">
                <div class="card" style="margin-top: 20px;">
                    <div class="card-header">
                        <span style="font-size: 130%">My Bots</span>
                        <button type="button" class="btn btn-primary float-end" data-bs-toggle="modal" data-bs-target="#add-bot-modal">
                            Create Bot
                        </button>

                        <div class="modal fade" id="add-bot-modal" tabindex="-1">
                            <div class="modal-dialog modal-xl">
                                <div class="modal-content">
                                <div class="modal-header">
                                    <h1 class="modal-title fs-5" id="exampleModalLabel">Create Bot</h1>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <div class="mb-3">
                                        <label for="add-bot-title" class="form-label">Name</label>
                                        <input v-model="botadd.title" type="text" class="form-control" id="add-bot-title" placeholder="Please enter bot name">
                                    </div>
                                    <div class="mb-3">
                                        <label for="add-bot-description" class="form-label">Description</label>
                                        <textarea v-model="botadd.description" class="form-control" id="add-bot-description" rows="3" placeholder="Please enter bot description"></textarea>
                                    </div>
                                    <div class="mb-3">
                                        <label for="add-bot-code" class="form-label">Code</label>
                                        <VAceEditor
                                            v-model:value="botadd.content"
                                            @init="editorInit"
                                            lang="c_cpp"
                                            theme="textmate"
                                            :options="{fontSize: 16}" 
                                            style="height: 300px" />
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <div class="error-message">{{botadd.error_message}}</div>
                                    <button type="button" class="btn btn-primary" @click="add_bot">Create</button>
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                </div>
                                </div>
                            </div>
                        </div>


                    </div>
                    <div class="card-body">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Create Time</th>
                                    <th>Operation</th>
                                </tr>
                            </thead>
                            <tbody>
                                <div class="modal fade" id="update-bot-modal" tabindex="-1">
                                    <div class="modal-dialog modal-xl">
                                        <div class="modal-content">
                                        <div class="modal-header">
                                            <h1 class="modal-title fs-5" id="exampleModalLabel">Update Bot</h1>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                        </div>
                                        <div class="modal-body">
                                            <div class="mb-3">
                                                <label for="update-bot-title" class="form-label">Name</label>
                                                <input v-model="curbot.title" type="text" class="form-control" id="update-bot-title" placeholder="Please enter bot name">
                                            </div>
                                            <div class="mb-3">
                                                <label for="update-bot-description" class="form-label">Description</label>
                                                <textarea v-model="curbot.description" class="form-control" id="update-bot-description" rows="3" placeholder="Please enter bot description"></textarea>
                                            </div>
                                            <div class="mb-3">
                                                <label for="update-bot-code" class="form-label">Code</label>
                                                <VAceEditor
                                                    v-model:value="curbot.content"
                                                    @init="editorInit"
                                                    lang="c_cpp"
                                                    theme="textmate"
                                                    :options="{fontSize: 16}" 
                                                    style="height: 300px" />
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <div class="error-message">{{curbot.error_message}}</div>
                                            <button type="button" class="btn btn-primary" @click="update_bot(curbot)">Update</button>
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                        </div>
                                        </div>
                                    </div>
                                </div> 
                                <tr v-for="bot in bots" :key="bot.id">
                                    <td>{{bot.title}}</td>
                                    <td>{{bot.createtime}}</td>
                                    <td>
                                        <button type="button" class="btn btn-secondary" style="margin-right: 10px" data-bs-toggle="modal" data-bs-target="#update-bot-modal" @click="get_cur_bot(bot)">Update</button>
                                        <button type="button" class="btn btn-danger" @click="remove_bot(bot)">Remove</button>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

            </div>
        </div>
    </div>
</template>

<script>
import { ref, reactive } from 'vue';
import { useStore } from 'vuex';
import $ from 'jquery';
import { Modal } from 'bootstrap/dist/js/bootstrap';
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';


export default {
    components:{
        // ContentField
        VAceEditor,
    },

    setup() {
        ace.config.set(
    "basePath", 
    "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/");

        const store = useStore();
        let bots = ref([]);

        const botadd = reactive({
            title: "",
            description: "",
            content: "",
            error_message: "",
        });

        const curbot = reactive({
            id: "",
            title: "",
            description: "",
            content: "",
            error_message: "",
        });


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

        const add_bot = () => {
            botadd.error_message = "";
            $.ajax({
                url: "http://localhost:3000/user/bot/add/",
                type: "post",
                data: {
                    title: botadd.title,
                    description: botadd.description,
                    content: botadd.content
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp){
                    if (resp.error_message === "success"){
                        botadd.title = "";
                        botadd.description = "";
                        botadd.content = "";
                        Modal.getInstance("#add-bot-btn").hide();
                        refresh_bots();
                    } else {
                        botadd.error_message = resp.error_message;
                    }
                },
            })
        }

        const remove_bot = (bot) => {
            $.ajax({
                url: "http://localhost:3000/user/bot/remove/",
                type: "post",
                data: {
                    bot_id: bot.id,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp){
                    if (resp.error_message === "success"){
                        refresh_bots();
                    } 
                },
            })
        }

        const get_cur_bot = (bot) => {
            curbot.id = bot.id;
            curbot.title = bot.title;
            curbot.description = bot.description;
            curbot.content = bot.content;
            curbot.error_message = "";
        }

        const update_bot = (bot) => {
            bot.error_message = "";
            $.ajax({
                url: "http://localhost:3000/user/bot/update/",
                type: "post",
                data: {
                    bot_id: bot.id,
                    title: bot.title,
                    description: bot.description,
                    content: bot.content
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp){
                    if (resp.error_message === "success"){
                        Modal.getInstance('#update-bot-modal').hide();
                        refresh_bots();
                    } else {
                        bot.error_message = resp.error_message;
                    }
                },
            })
        }

        refresh_bots();

        return {
            bots,
            botadd,
            curbot,
            add_bot,
            remove_bot,
            update_bot,
            get_cur_bot
        }

    }

}
</script>

<style scoped>
div.error-message{
    color: red;
}
</style>