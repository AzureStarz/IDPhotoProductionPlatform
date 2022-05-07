<template>
  <div class="row justify-content-center">
    <div class="col-lg-16">
      <tabs
        fill
        class="flex-column flex-md-row"
      >
        <card
          shadow
          slot-scope="{activeTabIndex}"
        >
          <tab-pane key="tab1">
            <template slot="title">
              <i class="fa fa-id-card-o mr-2"></i>证件照
            </template>
            <Images :imgUrl='imgUrl1'></Images>
          </tab-pane>

          <tab-pane key="tab2">
            <template slot="title">
              <i class="fa fa-coffee mr-2"></i>职业照
            </template>
            <Images :imgUrl='imgUrl2'></Images>
          </tab-pane>

          <tab-pane key="tab3">
            <template slot="title">
              <i class="fa fa-camera-retro mr-2"></i>形象照（半身）
            </template>
            <Images :imgUrl='imgUrl3'></Images>
          </tab-pane>

          <tab-pane key="tab4">
            <template slot="title">
              <i class="fa fa-camera-retro mr-2"></i>形象照（全身）
            </template>
            <Images :imgUrl='imgUrl4'></Images>
          </tab-pane>

          <tab-pane key="tab5">
            <template slot="title">
              <i class="fa fa-graduation-cap mr-2"></i>入学照
            </template>
            <Images :imgUrl='imgUrl5'></Images>
          </tab-pane>

          <tab-pane key="tab6">
            <template slot="title">
              <i class="fa fa-paw mr-2"></i>宠物照
            </template>
            <Images :imgUrl='imgUrl6'></Images>
          </tab-pane>
        </card>
      </tabs>
    </div>
    <!-- <div class="col-lg-6 mt-5 mt-lg-0">
            <div class="mb-3">
                <small class="text-uppercase font-weight-bold">With text</small>
            </div>
            <tabs fill class="flex-column flex-md-row">
                <card shadow>
                    <tab-pane title="Home">
                        <p class="description">Raw denim you probably haven't heard of them jean shorts
                            Austin. Nesciunt tofu stumptown aliqua, retro synth master cleanse. Mustache
                            cliche tempor, williamsburg carles vegan helvetica. Reprehenderit butcher retro
                            keffiyeh dreamcatcher synth.</p>
                        <p class="description">Raw denim you probably haven't heard of them jean shorts
                            Austin. Nesciunt tofu stumptown aliqua, retro synth master cleanse.</p>
                    </tab-pane>

                    <tab-pane title="Profile">
                        <p class="description">Cosby sweater eu banh mi, qui irure terry richardson ex
                            squid. Aliquip placeat salvia cillum iphone. Seitan aliquip quis cardigan
                            american apparel, butcher voluptate nisi qui.</p>
                    </tab-pane>

                    <tab-pane title="Messages">
                        <p class="description">Raw denim you probably haven't heard of them jean shorts
                            Austin. Nesciunt tofu stumptown aliqua, retro synth master cleanse. Mustache
                            cliche tempor, williamsburg carles vegan helvetica. Reprehenderit butcher retro
                            keffiyeh dreamcatcher synth.</p>
                    </tab-pane>
                </card>
            </tabs>
        </div> -->
  </div>
</template>
<script>
import Tabs from "@/components/Tabs/Tabs.vue";
import TabPane from "@/components/Tabs/TabPane.vue";
import Images from "./Images.vue"
import BaseButton from '../../../components/BaseButton.vue';
export default {
  components: {
    Tabs,
    TabPane,
    Images,
    BaseButton
  },
  data () {
    return {
      imgUrl1: 'https://st-gdx.dancf.com/assets/20200102-161850-3a94.png',
      imgUrl2: 'https://st-gdx.dancf.com/assets/20200102-161933-53a5.jpg',
      imgUrl3: 'https://st-gdx.dancf.com/assets/20200102-161819-d8fe.jpg',
      imgUrl4: 'https://st-gdx.dancf.com/assets/20200102-161834-57b6.jpg',
      imgUrl5: 'https://st-gdx.dancf.com/assets/20200102-161606-2bb2.jpg',
      imgUrl6: 'https://st-gdx.dancf.com/assets/20200102-161907-9172.jpg'
    }
  }
};
</script>
<style>
</style>
