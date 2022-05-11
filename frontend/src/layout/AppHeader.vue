<template>
  <header class="header-global">
    <base-nav
      class="navbar-main"
      transparent
      type=""
      effect="light"
      expand
    >
      <router-link
        slot="brand"
        class="navbar-brand mr-lg-5"
        to="/"
      >
        <img
          src="img/brand/white.png"
          alt="logo"
        >
      </router-link>

      <div
        class="row"
        slot="content-header"
        slot-scope="{closeMenu}"
      >
        <div class="col-6 collapse-brand">
          <a href="https://demos.creative-tim.com/vue-argon-design-system/documentation/">
            <img src="img/brand/blue.png">
          </a>
        </div>
        <div class="col-6 collapse-close">
          <close-button @click="closeMenu"></close-button>
        </div>
      </div>

      <ul class="navbar-nav navbar-nav-hover align-items-lg-center">
        <base-dropdown
          v-if="this.$store.state.login"
          class="nav-item"
          menu-classes="dropdown-menu-xl"
        >
          <a
            slot="title"
            href="#"
            class="nav-link"
            data-toggle="dropdown"
            role="button"
          >
            <i class="ni ni-ui-04 d-lg-none"></i>
            <span class="nav-link-inner--text">Craft</span>
          </a>
          <div class="dropdown-menu-inner">
            <router-link
              to="/landing"
              class="media d-flex align-items-center"
            >
              <div class="icon icon-shape bg-gradient-primary rounded-circle text-white">
                <i class="ni ni-spaceship"></i>
              </div>
              <div class="media-body ml-3">
                <h6 class="heading text-primary mb-md-1">快来制作证件照吧！</h6>
                <p class="description d-none d-md-inline-block mb-0">AI自动抠图, 如你所愿
                  为抠图而设计，满足常规作图需求，效果对比一目了然，细节处理绝不含糊</p>
              </div>
            </router-link>
            <router-link
              to="/diycraft"
              class="media d-flex align-items-center"
            >
              <div class="icon icon-shape bg-gradient-warning rounded-circle text-white">
                <i class="ni ni-ui-04"></i>
              </div>
              <div class="media-body ml-3">
                <h5 class="heading text-warning mb-md-1">快来创作你的个性照片吧！</h5>
                <p class="description d-none d-md-inline-block mb-0">AI自动抠图, 如你所愿
                  为抠图而设计，满足常规作图需求，效果对比一目了然，细节处理绝不含糊</p>
              </div>
            </router-link>
          </div>
        </base-dropdown>
        <base-dropdown
          tag="li"
          class="nav-item"
        >
          <a
            slot="title"
            href="#"
            class="nav-link"
            data-toggle="dropdown"
            role="button"
          >
            <i class="ni ni-collection d-lg-none"></i>
            <span class="nav-link-inner--text">Menu</span>
          </a>
          <!-- <router-link
            to="/landing"
            class="dropdown-item"
          >Landing</router-link> -->
          <router-link
            v-if="$store.state.login"
            to="/profile"
            class="dropdown-item"
          >Profile</router-link>
          <router-link
            v-if="!$store.state.login"
            to="/login"
            class="dropdown-item"
          >Login</router-link>
          <router-link
            v-if="!$store.state.login"
            to="/register"
            class="dropdown-item"
          >Register</router-link>
          <button
            @click="logout()"
            v-if="$store.state.login"
            class="dropdown-item"
          >Logout</button>
        </base-dropdown>
      </ul>
      <ul class="navbar-nav align-items-lg-center ml-lg-auto">
        <li class="nav-item">
          <a
            class="nav-link nav-link-icon"
            href="https://www.facebook.com/creativetim"
            target="_blank"
            rel="noopener"
            data-toggle="tooltip"
            title="Like us on Facebook"
          >
            <i class="fa fa-facebook-square"></i>
            <span class="nav-link-inner--text d-lg-none">Facebook</span>
          </a>
        </li>
        <li class="nav-item">
          <a
            class="nav-link nav-link-icon"
            href="https://www.instagram.com/creativetimofficial"
            target="_blank"
            rel="noopener"
            data-toggle="tooltip"
            title="Follow us on Instagram"
          >
            <i class="fa fa-instagram"></i>
            <span class="nav-link-inner--text d-lg-none">Instagram</span>
          </a>
        </li>
        <li class="nav-item">
          <a
            class="nav-link nav-link-icon"
            href="https://twitter.com/creativetim"
            target="_blank"
            rel="noopener"
            data-toggle="tooltip"
            title="Follow us on Twitter"
          >
            <i class="fa fa-twitter-square"></i>
            <span class="nav-link-inner--text d-lg-none">Twitter</span>
          </a>
        </li>
        <li class="nav-item">
          <a
            class="nav-link nav-link-icon"
            href="https://github.com/creativetimofficial/vue-argon-design-system"
            target="_blank"
            rel="noopener"
            data-toggle="tooltip"
            title="Star us on Github"
          >
            <i class="fa fa-github"></i>
            <span class="nav-link-inner--text d-lg-none">Github</span>
          </a>
        </li>
        <li class="nav-item d-none d-lg-block ml-lg-4">
          <div
            v-if="!$store.state.login"
            @click="gotoSignup"
            target="_blank"
            rel="noopener"
            class="btn btn-neutral btn-icon"
          >
            <span class="btn-inner--icon">
              <i class="fa fa-sign-in mr-2"></i>
            </span>
            <span class="nav-link-inner--text">登录/注册</span>
          </div>
          <div
            v-if="$store.state.login"
            @click="gotoProfile"
            target="_blank"
            rel="noopener"
            class="btn btn-neutral btn-icon"
          >
            <span class="btn-inner--icon">
              <i class="fa fa-sign-in mr-2"></i>
            </span>
            <span class="nav-link-inner--text">{{$store.state.userName}}的个人中心</span>
          </div>
        </li>
      </ul>
    </base-nav>
  </header>
</template>
<script>
import BaseNav from "@/components/BaseNav";
import BaseDropdown from "@/components/BaseDropdown";
import CloseButton from "@/components/CloseButton";

export default {
  methods: {
    gotoSignup () {
      this.$router.push('/login')
    },
    gotoProfile () {
      this.$router.push('/profile')
    },
    logout () {
      this.$store.commit('logoutFun')
      this.$router.replace('/')
    }
  },
  mounted () {
    console.log(localStorage.getItem('userName'))
    if (localStorage.getItem('userName')) {
      this.$store.commit('loginFun', localStorage.getItem('userName'))
      this.$store.commit('setUserId', localStorage.getItem('userId'))
    } else {
      this.$store.commit('logoutFun')
    }
  },
  components: {
    BaseNav,
    CloseButton,
    BaseDropdown
  }
};
</script>
<style>
</style>
