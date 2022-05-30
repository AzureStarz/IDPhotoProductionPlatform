<template>
  <section class="section section-shaped section-lg my-0">
    <div class="shape shape-style-1 bg-gradient-default">
      <span></span>
      <span></span>
      <span></span>
      <span></span>
      <span></span>
      <span></span>
      <span></span>
      <span></span>
      <span></span>
      <span></span>
    </div>
    <div class="container pt-lg-md">
      <div class="row justify-content-center">
        <div class="col-lg-5">
          <card
            type="secondary"
            shadow
            header-classes="bg-white pb-5"
            body-classes="px-lg-5 py-lg-5"
            class="border-0"
          >
            <template>
              <div class="text-muted text-center mb-3">
                <small>Sign in with</small>
              </div>
              <div class="btn-wrapper text-center">
                <base-button type="neutral">
                  <img
                    slot="icon"
                    src="img/icons/common/github.svg"
                  >
                  Github
                </base-button>

                <base-button type="neutral">
                  <img
                    slot="icon"
                    src="img/icons/common/google.svg"
                  >
                  Google
                </base-button>
              </div>
            </template>
            <template>
              <div class="text-center text-muted mb-4">
                <small>Or sign in with credentials</small>
              </div>
              <form role="form">
                <base-input
                  alternative
                  class="mb-3"
                  value=""
                  v-model="username"
                  placeholder="UserName"
                  addon-left-icon="ni ni-email-83"
                >
                </base-input>
                <base-input
                  alternative
                  type="password"
                  value=""
                  v-model="password"
                  placeholder="Password"
                  addon-left-icon="ni ni-lock-circle-open"
                >
                </base-input>
                <base-checkbox>
                  Remember me
                </base-checkbox>
                <div class="text-center">
                  <base-button
                    type="primary"
                    class="my-4"
                    @click="login()"
                  >Sign In</base-button>
                </div>
              </form>
            </template>
          </card>
          <div class="row mt-3">
            <div class="col-6">
              <el-button
                type="text"
                @click="toForgetPWD()"
                class="text-light"
              >
                Forgot password?
              </el-button>
            </div>
            <div class="col-6 text-right">
              <el-button
                type="text"
                @click="toRegister()"
                class="text-light"
              >
                Create new account
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>
<script>
export default {
  data () {
    return {
      username: "",
      password: "",
      userId: ""
    }
  },
  methods: {
    toRegister () {
      this.$router.push('/register');
    },
    toForgetPWD () {
      this.$router.push('/retrievePassword');
    },
    login () {
      let loginUrl = '/api/login';
      /* let params = {
        username: this.username,
        password: this.password
      } */
      /* this.$axios.post(loginUrl, params).then(res => {
        let status = res.data;
        console.log(status)
        if (status == "success") {
          console.log("Login successfully");
          this.$router.push('/');
        }
      }).catch(error => {
        console.log(error.message);
      }) */
      this.$axios.post('/api/login?username=' + this.username + '&password=' + this.password).then((res) => {
        if (res.data === 'wrong') {
          this.$message.error({ message: '账户名或密码输入错误！', duration: 1000 });
        }
        else if (res.data === 'success') {
          this.$axios.get('/api/getUser').then((res) => {
            this.userId = res.data
            this.$message.success({ message: '登录成功！', duration: 1000 });
            this.$router.replace('/');
            console.log(this.userId)
            this.$store.commit('loginFun', this.username);
            sessionStorage.setItem('userName', JSON.stringify(this.username))
            sessionStorage.setItem('login', JSON.stringify(true))
            sessionStorage.setItem('userId', JSON.stringify(this.userId))
            this.$store.commit('setUserId', this.userId);
            console.log(this.$store.state.userId)
            console.log(JSON.parse(sessionStorage.getItem('userName')))
            console.log(JSON.parse(sessionStorage.getItem('login')))
            console.log(JSON.parse(sessionStorage.getItem('userId')))
          })
          this.$store.commit('setToken', JSON.stringify(res.data.token))
          /* this.$store.commit('')
          this.useId */
        }
      })
    }

  }
};
</script>
<style>
</style>
