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
                    <span class="heading">{{this.list_len}}</span>
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

                    <el-image :src="photo.photoPath">
                    </el-image>

                    <div class="bottom clearfix">
                      <div class="photoname">{{photo.photoName}}</div>
                      <time class="time">{{dateFormat(photo.updateTime) }}</time>
                      <!-- <el-button type="text" class="button" v-on:click="amplificate" >点击放大</el-button> -->
                    </div>

                    <div
                      class="text-center mt-12 deletebutton"
                      width="5px"
                      height="5px"
                    >

                      <el-popconfirm
                        @confirm="deletePhoto(photo.photoID, i)"
                        title="Are you sure to delete this?"
                      >
                        <!-- <div class="deletebutton"> -->
                        <!-- <el-button slot="reference" >delete</i></el-button> -->
                        <el-button
                          slot="reference"
                          size="5"
                          type="danger"
                          icon="el-icon-delete"
                          circle
                        ></el-button>
                        <!-- </div> -->
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

.deletebutton {
  /* margin-right:10px;
	//float:right; */
}

.photoname {
  /* padding-left:5px; */
  font-style: "microsoft yahei";
  text-align: center;
  font-size: 14px;
  padding-bottom: 10px;
}

.time {
  display: block;
  padding-bottom: 10px;

  font-style: "microsoft yahei";
  text-align: center;
  font-size: 14px;
  font-weight: 700;
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
      list_len: 0
    };
  },
  methods: {
    deletePhoto (photoID, idx) {
      console.log(photoID)
      this.$axios.post('/api/deletePhoto?photoID=' + photoID).then((res) => {
        console.log("post")
        this.photos.remove(idx)
      })
    },
    //时间格式化函数，此处仅针对yyyy-MM-dd hh:mm:ss 的格式进行格式化
    dateFormat (time) {
      var date = new Date(time);
      var year = date.getFullYear();
      /* 在日期格式中，月份是从0开始的，因此要加0
       * 使用三元表达式在小于10的前面加0，以达到格式统一  如 09:11:05
       * */
      var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
      var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
      var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
      var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
      var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
      // 拼接
      return year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
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
      let tmp = res.data
      // TODO
      tmp.forEach(element => {
        let name = element.photoName
        let path = 'img/photos/' + name
        element.photoPath = path
      });
      this.photos = tmp
      this.list_len = this.photos.length
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