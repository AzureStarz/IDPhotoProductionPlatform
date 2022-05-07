<template>
  <div>
    <hero></hero>
    <TabsSection></TabsSection>
    <div class="row justify-content-center align-items-center">
      <div class="text-center pt-sm pb-md">
        <base-button
          @click="toCraft()"
          type="info"
        >
          开始制作吧!
        </base-button>
      </div>
    </div>
  </div>
</template>
<script>
import Hero from "./components/Hero";
import TabsSection from "./components/JavascriptComponents/TabsSection";

export default {
  name: "components",
  components: {
    Hero,
    TabsSection,
  },
  methods: {
    toCraft () {
      this.authAlert()
      this.$router.push('/diycraft');
    },
    authAlert () {
      if (!this.$store.state.login) {
        this.$message.error({ message: '请登录该平台再进行操作！', duration: 1000 });
      }
    }
  },
  mounted () {
    this.authAlert()
  }
};
</script>
