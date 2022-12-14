import router from '@/router/index';
import $ from 'jquery'

export default {
    state: {
        id: "",
        username: "",
        photo: "",
        token: "",
        isLogin: false,
        pulling_info: true,
    },
    getters: {
    },
    mutations: {
        updateUser(state, user){
            state.id = user.id;
            state.username = user.username;
            state.photo = user.photo;
            state.isLogin = user.isLogin;
        },

        updateToken(state, token){
            state.token = token;
        },

        logout(state){
            state.id = "";
            state.username = "";
            state.photo = "";
            state.token = "";
            state.isLogin = false;
        },

        updatePullingInfo(state, pulling_info){
            state.pulling_info = pulling_info;
        }
    },
    actions: {
        login(context, data){
            $.ajax({
                url: "http://localhost:3000/user/account/token/",
                type: "post",
                data: {
                    username: data.username,
                    password: data.password
                },
                success(resp){
                    if (resp.error_message === "success"){
                        localStorage.setItem("jwt_token", resp.token);
                        context.commit("updateToken", resp.token);
                        data.success(resp);
                    } else {
                        data.error(resp);
                    }
                    
                },
                error(resp){
                    data.error(resp);
                }
            })
        },

        getinfo(context, data){
            $.ajax({
                url: "http://localhost:3000/user/account/info/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + context.state.token
                },
                success(resp){
                    if (resp.error_message === "success"){
                        context.commit("updateUser", {
                            ...resp,
                            isLogin: true
                        });
                        data.success(resp);
                    } else {
                        data.error(resp);
                    }

                },
                error(resp){
                    data.error(resp);
                }
            })
        },
        logout(context){
            localStorage.removeItem("jwt_token");
            context.commit("logout");
            router.push({name: 'user_account_login'});
        }
    },
    modules: {
    }
}