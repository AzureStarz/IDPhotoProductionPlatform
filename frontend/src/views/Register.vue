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
                <small>Or sign up with credentials</small>
              </div>
              <form role="form">
                <base-input
                  alternative
                  class="mb-3"
                  v-model="username"
                  placeholder="UserName"
                  addon-left-icon="ni ni-hat-3"
                >
                </base-input>
                <base-input
                  alternative
                  class="mb-3"
                  placeholder="Email"
                  :valid="valid"
                  v-model="email"
                  @blur="checkEmailValid()"
                  addon-left-icon="ni ni-email-83"
                >
                </base-input>
                <base-input
                  alternative
                  type="password"
                  placeholder="Password"
                  v-model="password"
                  addon-left-icon="ni ni-lock-circle-open"
                >
                </base-input>
                <div class="text-muted font-italic">
                  <small>password strength:
                    <span
                      v-if="password.length > 5"
                      class="text-success font-weight-700"
                    >strong</span>
                    <span
                      v-if="password.length <= 5"
                      class="font-weight-700"
                    >weak</span>
                  </small>
                </div>
                <base-checkbox v-model="check">
                  <span>I agree with the
                    <a href="#">Privacy Policy</a>
                  </span>
                </base-checkbox>
                <div class="text-center">
                  <base-button
                    type="primary"
                    class="my-4"
                    @click="register()"
                  >Create account</base-button>
                </div>
              </form>
            </template>
          </card>
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
      email: "",
      check: false,
      valid: false
    }
  },
  methods: {
    register () {
      if (this.check) {
        if (this.valid) {
          let registerUrl = '/api/register';
          let params = {
            username: this.username,
            password: this.password,
            email: this.email
          }
          /* this.$axios.post(registerUrl, params).then(res => {
            let status = res.data;
            console.log(status)
            if (status == "success") {
              console.log("register successfully");
              this.$router.push('/login');
            }
          }).catch(error => {
            console.log(error.message);
          }) */
          this.$axios.post(registerUrl, params).then((res) => {
            if (res.data === 'usernameAlready') {
              this.$message.error({ message: '用户名已注册！', duration: 1000 });
            } else if (res.data === 'success') {
              this.$message.success({ message: '注册成功！', duration: 1000 });
              console.log("register successfully");
              this.$router.replace('/login');
            }
          })
        } else {
          this.$message.error({ message: '表单验证不通过！', duration: 1000 })
        }
      } else {
        this.$message.error({ message: '请同意用户协议！', duration: 1000 })
      }
    },
    checkEmailValid () {
      var regEmail = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
      if (this.email != '' && !regEmail.test(this.email)) {
        this.$message({
          message: '邮箱格式不正确',
          type: 'error'
        })
        this.valid = false
      } else {
        this.valid = true
      }
    }
  },
}
</script>
<style>
</style>
