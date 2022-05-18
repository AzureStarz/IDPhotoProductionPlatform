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
    <div style="padding-top: 20px">
      <div class="row justify-content-center">
        <video
          id="videoCamera"
          v-show="shot"
          :width="videoWidth"
          :height="videoHeight"
          autoplay
        ></video>
        <canvas
          style="display:none;"
          id="canvasCamera"
          :width="videoWidth"
          :height="videoHeight"
        ></canvas>
        <div class="result_img">
          <img
            v-show="!shot"
            :src="imgSrc"
            alt
            class="tx_img"
            width="100%"
          />
        </div>
      </div>
      <div class="mt-3">
        <el-row class="row justify-content-center">
          <el-button
            class="primary"
            plain
            @click="setImage()"
          >进行拍摄</el-button>
          <el-popconfirm
            @confirm="resetCamera()"
            title="确定要重新拍摄照片么?"
          >
            <el-button
              class="primary"
              slot="reference"
            >重新拍摄</el-button>
          </el-popconfirm>
        </el-row>
        <el-row class="mt-3 row justify-content-center">
          <el-button
            @click="postImg()"
            type="info"
            v-loading.fullscreen.lock="fullscreenLoading"
          >确认上传</el-button>
        </el-row>
      </div>
    </div>
  </section>
</template>
<script>
export default {
  data () {
    return {
      fullscreenLoading: false,
      // 视频调用相关数据开始
      videoWidth: 358 * 1.25,
      videoHeight: 441 * 1.25,
      imgSrc: "",
      thisCancas: null,
      thisContext: null,
      thisVideo: null,
      openVideo: false,
      //视频调用相关数据结束
      postVideoImg: '',// 图片上传后获取的url链接
      shot: true
    };
  },
  mounted () {
    // 第一步打开摄像头
    this.getCompetence() //调用摄像头

  },
  methods: {
    // 第三步、拍照图转换file格式上传，
    // 第四步、获取图片url链接
    // TODO: 将拍摄图片传到后台 进行人像切割
    postImg () {
      /* let formData = new FormData()
      formData.append('file', this.base64ToFile(this.imgSrc, 'png'))
      formData.append("flag", "videoImg")// 额外参数 */
      // console.log(this.imgSrc)
      this.fullscreenLoading = true;
      // TODO: 这个地方读取到的也是BASE64图片 需要传到后端 然后后端返回一个url地址
      let data = this.imgSrc;
      // 保证原图片能够正常存储下来
      var imgs = data.replace(/^data:image\/\w+;base64,/, "");
      let fileName = new Date().valueOf() + '.png';
      let segURL = '/api/seg';
      let params = {
        imgStr: imgs,
        fileName: fileName,
        userId: this.$store.state.userId
      }
      this.$axios.post(segURL, params).then(res => {
        let imgUrl = 'img\\photos\\';
        imgUrl += res.data;
        console.log(imgUrl)
        fabric.Image.fromURL(imgUrl, (img) => {
          // 封装成了fabric格式的图片
          console.log(img)
          this.toCraftPage(img)
        });
      }).catch(error => {
        console.log(error.message);
      })
    },

    // 调用权限（打开摄像头功能）
    getCompetence () {
      var _this = this;
      _this.thisCancas = document.getElementById("canvasCamera");
      _this.thisContext = this.thisCancas.getContext("2d");
      _this.thisVideo = document.getElementById("videoCamera");
      _this.thisVideo.style.display = 'block';
      // 获取媒体属性，旧版本浏览器可能不支持mediaDevices，我们首先设置一个空对象
      if (navigator.mediaDevices === undefined) {
        navigator.mediaDevices = {};
      }
      // 一些浏览器实现了部分mediaDevices，我们不能只分配一个对象
      // 使用getUserMedia，因为它会覆盖现有的属性。
      // 这里，如果缺少getUserMedia属性，就添加它。
      if (navigator.mediaDevices.getUserMedia === undefined) {
        navigator.mediaDevices.getUserMedia = function (constraints) {
          // 首先获取现存的getUserMedia(如果存在)
          var getUserMedia =
            navigator.webkitGetUserMedia ||
            navigator.mozGetUserMedia ||
            navigator.getUserMedia;
          // 有些浏览器不支持，会返回错误信息
          // 保持接口一致
          if (!getUserMedia) {//不存在则报错
            return Promise.reject(
              new Error("getUserMedia is not implemented in this browser")
            );
          }
          // 否则，使用Promise将调用包装到旧的navigator.getUserMedia
          return new Promise(function (resolve, reject) {
            getUserMedia.call(navigator, constraints, resolve, reject);
          });
        };
      }
      var constraints = {
        audio: false,
        video: {
          width: this.videoWidth,
          height: this.videoHeight,
          transform: "scaleX(-1)"
        }
      };
      navigator.mediaDevices
        .getUserMedia(constraints)
        .then(function (stream) {
          // 旧的浏览器可能没有srcObject
          if ("srcObject" in _this.thisVideo) {
            _this.thisVideo.srcObject = stream;
          } else {
            // 避免在新的浏览器中使用它，因为它正在被弃用。
            _this.thisVideo.src = window.URL.createObjectURL(stream);
          }
          _this.thisVideo.onloadedmetadata = function (e) {
            _this.thisVideo.play();
          };
        })
        .catch(err => {
          console.log(err);
        });
    },
    //  第二步、绘制图片（拍照功能）
    setImage () {
      this.shot = false;
      var _this = this;
      // canvas画图
      _this.thisContext.drawImage(
        _this.thisVideo,
        0,
        0,
      );
      // 获取图片base64链接
      var image = this.thisCancas.toDataURL("image/png");
      _this.imgSrc = image;//赋值并预览图片

      //这里是调用上传图片接口===== 
      // this.postImg() // 绘制完图片调用图片上传接口
    },
    resetCamera () {
      this.shot = true;
    },
    // 关闭摄像头
    stopNavigator () {
      this.thisVideo.srcObject.getTracks()[0].stop();
    },

    // base64 转为 file
    base64ToFile (urlData, fileName) {
      let arr = urlData.split(',');
      let mime = arr[0].match(/:(.*?);/)[1];
      let bytes = atob(arr[1]); // 解码base64
      let n = bytes.length
      let ia = new Uint8Array(n);
      while (n--) {
        ia[n] = bytes.charCodeAt(n);
      }
      return new File([ia], fileName, { type: mime });
    },
    // 路由跳转到制作界面
    toCraftPage (img) {
      this.stopNavigator();
      this.$router.push({
        name: "craft",
        params: {
          img: img
        }
      })
    }
  },
  destroyed: function () { // 离开当前页面
    this.stopNavigator() // 关闭摄像头
  }
}
</script>
<style>
.result_img {
  width: 448 px;
  height: 551 px;
  background: #d8d8d8;
}
</style>
