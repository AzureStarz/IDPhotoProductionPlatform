<template>
  <div class="profile-page">
    <section class="section-profile-cover section-shaped my-0">
      <div class="shape shape-style-1 shape-primary shape-skew alpha-4">
        <span></span>
        <span></span>
        <span></span>
        <span></span>
        <span></span>
        <span></span>
        <span></span>
      </div>
    </section>
    <section class="section section-skew">
      <div class="container">
        <card
          shadow
          class="card-profile mt--300"
          no-body
        >
          <div class="px-4">
            <div class="row justify-content-center">
              <div class="col-lg-3 order-lg-2">
                <div class="card-profile-image">
                  <a href="#">
                    <img
                      v-lazy="'img/theme/team-4-800x800.jpg'"
                      class="rounded-circle"
                    >
                  </a>
                </div>
              </div>
              <div class="col-lg-4 order-lg-3 text-lg-right align-self-lg-center">
                <div class="card-profile-actions py-4 mt-lg-0">
                  <base-button
                    type="info"
                    size="sm"
                    class="mr-4"
                  >Connect</base-button>
                  <base-button
                    type="default"
                    size="sm"
                    class="float-right"
                  >Message</base-button>
                </div>
              </div>
              <div class="col-lg-4 order-lg-1">
                <div class="card-profile-stats d-flex justify-content-center">
                  <div>
                    <span class="heading">22</span>
                    <span class="description">Friends</span>
                  </div>
                  <div>
                    <span class="heading">{{this.photos.length}}</span>
                    <span class="description">Photos</span>
                  </div>
                  <div>
                    <span class="heading">89</span>
                    <span class="description">Comments</span>
                  </div>
                </div>
              </div>
            </div>
            <div class="text-center mt-6">
              <h3>{{$store.state.userName}}
                <!-- <span class="font-weight-light">, 27</span> -->
              </h3>
              <div class="h6 font-weight-300"><i class="ni location_pin mr-2"></i>四川大学</div>

            </div>
            <span></span>
            <span></span>
            <el-divider
              content-position="center"
              font-size="50px"
            >
              <i
                class="fa fa-history fa-lg"
                aria-hidden="true"
              ></i>
            </el-divider>
            <span></span>
            <span></span>
            <span></span>
            <div class="history-card">
              <el-row>
                <el-col
                  :span="6"
                  v-for="(photo, i) in photos"
                  :key="i"
                >
                  <el-card :body-style="{ padding: '5px' }">
                    <!-- <img :src='src' class="image" alt="" > -->
                    <el-image
                      :src="url"
                      :preview-src-list="srcList"
                    >
                    </el-image>

                    <div class="bottom clearfix">
                      <time class="time">{{ photo.photoName }}</time>
                      <!-- <el-button type="text" class="button" v-on:click="amplificate" >点击放大</el-button> -->
                    </div>
                    <div class="text-center mt-12">
                      <el-popconfirm
                        @confirm="deletePhoto(photo.photoID, i)"
                        title="Are you sure to delete this?"
                      >
                        <el-button slot="reference">Delete</el-button>
                      </el-popconfirm>
                    </div>
                  </el-card>
                </el-col>
              </el-row>
            </div>
          </div>
        </card>
      </div>
    </section>
  </div>
</template>
<style>
.time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.button {
  padding: 0;
  float: right;
}

.image {
  width: 100%;
  display: inline-flex;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both;
}

.history-card {
  display: inline-flex;
}
</style>

<script>
Array.remove = function (array, from, to) {
  var rest = array.slice((to || from) + 1 || array.length);
  array.length = from < 0 ? array.length + from : from;
  return array.push.apply(array, rest);
};
export default {
  data () {
    return {
      photos: null,
      currentDate: new Date(),
      src: "https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png",
      photoVisible: false,
      url: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
      srcList: [
        'https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg',
        'https://fuss10.elemecdn.com/1/8e/aeffeb4de74e2fde4bd74fc7b4486jpeg.jpeg'
      ]

    };
  },
  methods: {
    deletePhoto (photoID, idx) {
      console.log(photoID)
      this.$axios.post('/api/deletePhoto?photoID=' + photoID).then((res) => {
        console.log("post")
        this.photos.remove(idx)

      })
    }
    /* //放大图片
    amplificate () {
      return null;
    },
    showBigImage (imgUrl) {//点击图片函数，点击后，把photoVisible设置成true
      if (imgUrl != "") {
        this.photoVisible = true;
        this.bigImgUrl = e.currentTarget.src;
      }
    } */
  },
  mounted () {
    this.$axios.post('/api/getPhotos?userId=' + this.$store.state.userId).then((res) => {
      this.photos = res.data
      console.log(this.photos)
      /* this.photos = photos */
    })
    /* this.$axios.post('/api/deletePhoto?photoID=' + 1).then((res) => {
      photos = res.data
      console.log(photos)
    }) */
  }
}
</script>>