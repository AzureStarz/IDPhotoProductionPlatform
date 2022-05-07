import Vue from 'vue'
import Vuex from 'vuex'

//挂载Vuex
Vue.use(Vuex)

//创建VueX对象
const store = new Vuex.Store({
    state:{
        //存放的键值对就是所要管理的状态
        userName:'',
        login: false,
        token:''  //初始化token
    },
    mutations: {
        loginFun (state, userName) {
            state.userName = userName
            state.login  = true
        },
        setToken(state, token) {
            state.token = token
            localStorage.token = token //同步存储token至localStorage
        },
        logoutFun (state) {
            state.userName = "未登录"
            state.login  = false
        }
    },
    getters : {
        //获取token方法
        //判断是否有token,如果没有重新赋值，返回给state的token
        getToken(state) {
            if (!state.token) {
                state.token = localStorage.getItem('token')
            }
            return state.token
        }
    },
})

export default store