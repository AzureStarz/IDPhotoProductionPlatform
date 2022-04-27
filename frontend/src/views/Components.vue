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
      this.$router.push('/diycraft');
    }
  }
};
</script>
