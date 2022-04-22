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
                    <span class="text-success font-weight-700">strong</span>
                  </small>
                </div>
                <base-checkbox>
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
      password: ""
    }
  },
  methods: {
    register () {
      let registerUrl = '/api/register';
      let params = {
        username: this.username,
        password: this.password
      }
      console.log(params);
      this.$axios.post(registerUrl, params).then(res => {
        let status = res.data;
        console.log(status)
        if (status == "success") {
          console.log("register successfully");
          this.$router.push('/login');
        }
      }).catch(error => {
        console.log(error.message);
      })
    }
  },
}
</script>
<style>
</style>
