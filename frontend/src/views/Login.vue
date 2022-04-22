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
                @click="toRegister()"
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
  data() {
    return {
      username: "",
      password: ""
    }
  },
  methods: {
    toRegister () {
      this.$router.push('/register');
    },
    login () {
      let loginUrl = '/api/login';
        let params = {
          username: this.username,
          password: this.password
        }
        this.$axios.post(loginUrl, params).then(res => {
          let status = res.data;
          console.log(status)
          if (status == "success") {
            console.log("Login successfully");
            this.$router.push('/');
          }
        }).catch(error => {
          console.log(error.message);
        })
    }
  }
};
</script>
<style>
</style>
