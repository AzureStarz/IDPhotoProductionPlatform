import Vue from "vue";
import Router from "vue-router";
import AppHeader from "./layout/AppHeader";
import AppFooter from "./layout/AppFooter";
import Components from "./views/Components.vue";
import Landing from "./views/Landing.vue";
import Login from "./views/Login.vue";
import Register from "./views/Register.vue";
import Profile from "./views/Profile.vue";
import CameraPage from "./views/CameraPage.vue";
import Craft from "./views/Craft.vue";
import DIYCraft from "./views/DIYCraft.vue";
import retrievePassword from "./views/retrievePassword.vue"

Vue.use(Router);

export default new Router({
  linkExactActiveClass: "active",
  routes: [
    {
      path: "/",
      name: "components",
      meta: {
        requireAuth: false
      },
      components: {
        header: AppHeader,
        default: Components,
        footer: AppFooter
      }
    },
    {
      path: "/craft",
      name: "craft",
      meta: {
        requireAuth: true
      },
      components: {
        header: AppHeader,
        default: Craft,
        footer: AppFooter
      }
    },
    {
      path: "/diycraft",
      name: "diycraft",
      meta: {
        requireAuth: true
      },
      components: {
        header: AppHeader,
        default: DIYCraft,
        footer: AppFooter
      }
    },
    {
      path: "/landing",
      name: "landing",
      meta: {
        requireAuth: true
      },
      components: {
        header: AppHeader,
        default: Landing,
        footer: AppFooter
      }
    },
    {
      path: "/login",
      name: "login",
      meta: {
        requireAuth: false
      },
      components: {
        header: AppHeader,
        default: Login,
        footer: AppFooter
      }
    },
    {
      path: "/register",
      name: "register",
      meta: {
        requireAuth: false
      },
      components: {
        header: AppHeader,
        default: Register,
        footer: AppFooter
      }
    },
    {
      path: "/profile",
      name: "profile",
      meta: {
        requireAuth: true
      },
      components: {
        header: AppHeader,
        default: Profile,
        footer: AppFooter
      }
    },
    {
      path: "/retrievePassword",
      name: "retrievePassword",
      meta: {
        requireAuth: false
      },
      components: {
        header: AppHeader,
        default: retrievePassword,
        footer: AppFooter
      }
    },
    {
      path: "/camera",
      name: "camera",
      meta: {
        requireAuth: true
      },
      components: {
        header: AppHeader,
        default: CameraPage,
        footer: AppFooter
      }
    }
  ],
  scrollBehavior: to => {
    if (to.hash) {
      return { selector: to.hash };
    } else {
      return { x: 0, y: 0 };
    }
  }
});
