<template>
  <el-container>
    <el-aside>
      <el-form class="handleSave">
        <el-row>
          <base-button
            type="info"
            @click="imgDraw"
            icon="fa fa-plus-square-o"
            v-loading.fullscreen.lock="fullscreenLoading"
          >
            <input
              type="file"
              accept="image/*"
              style="display:none"
              id="uploadfile"
              @change="uploadFile"
            />
            上传图片
          </base-button>
          <base-button
            type="warning"
            icon="fa fa-download"
            @click="downLoad"
          >下载图片</base-button>
        </el-row>
        <el-collapse
          v-model="activeNames"
          @change="handleChange"
        >
          <el-collapse-item
            class="ml-2"
            title="换背景"
            name="2"
          >
            <el-row>
              <p style="font-weight: bold">请上传所需要的背景图片</p>
              <base-button
                type="success"
                @click="uploadButton2"
                icon="fa fa-plus-square-o"
              >
                <input
                  type="file"
                  accept="image/*"
                  style="display:none"
                  id="uploadbackground"
                  @change="uploadBackground"
                />
                上传背景
              </base-button>
            </el-row>
          </el-collapse-item>
          <el-collapse-item
            class="ml-2"
            title="智能调色美颜"
            name="3"
          >
            <el-popconfirm
              @confirm="beautify()"
              title="您需要美颜处理吗~"
            >
              <el-button slot="reference">人像美颜</el-button>
            </el-popconfirm>
            <el-popconfirm
              @confirm="makeup()"
              title="您需要美妆处理吗~"
            >
              <el-button slot="reference">人像美妆</el-button>
            </el-popconfirm>
            <el-popconfirm
              @confirm="beauty_shape()"
              title="您需要人像美型吗~"
            >
              <el-button slot="reference">人像美型</el-button>
            </el-popconfirm>
          </el-collapse-item>
          <el-collapse-item
            class="ml-2"
            title="图像风格迁移"
            name="4"
          >
            <el-radio
              v-model="radio"
              label="1"
            >彩铅</el-radio>
            <el-radio
              v-model="radio"
              label="2"
            >Hana</el-radio>
            <el-radio
              v-model="radio"
              label="3"
            >中央公园</el-radio>
            <el-radio
              v-model="radio"
              label="4"
            >优格</el-radio>
            <el-radio
              v-model="radio"
              label="5"
            >兰桂坊</el-radio>
            <el-radio
              v-model="radio"
              label="6"
            >粉镜</el-radio>
            <el-popconfirm
              class="mr-4"
              @confirm="filter()"
              title="您需要滤镜风格变换吗~"
            >
              <el-button slot="reference">滤镜风格变换</el-button>
            </el-popconfirm>

          </el-collapse-item>
        </el-collapse>
        <el-row>
          <!-- <el-form-item
            inline="inline"
            class="btn-zoom"
            style="margin: 10px 0px"
          >
            <i
              class="el-icon-caret-left"
              @click="zoomIt(0.8)"
            ></i>
            <span> {{ zoomCounter }} % </span>
            <i
              class="el-icon-caret-right"
              @click="zoomIt(1.2)"
            ></i>
          </el-form-item> -->
          <el-button
            type="danger"
            class="btn-reset"
            @click="resetCanvas"
          >重 置</el-button>
        </el-row>
      </el-form>
    </el-aside>
    <el-main style="height: 715px">
      <div class="content-show">
        <div class="row justify-content-center">
          <canvas
            ref="canvas"
            id="editorCanvas"
          ></canvas>
        </div>
      </div>
    </el-main>
  </el-container>
</template>

<script>
import { fabric } from "fabric";
import BaseButton from '../../components/BaseButton.vue';
let editorCanvas = "";
let rect = null;
let size = null;
let originalSize = null;
let targetFileName = "";
let targetOriginImg = "";
let currentImg = null;
let segImg = "";
let diyImg = null;
let diyBackground = null;
fabric.Object.prototype.set({
  cornerStrokeColor: "#66b0ef",
  cornerColor: "#60abec",
  cornerStyle: "rectangele",
  cornerSize: 8,
  borderScaleFactor: 2,
  transparentCorners: false,
  borderColor: "#61abe8",
});

export default {
  components: { BaseButton },
  data () {
    return {
      fullscreenLoading: false,
      radio: '1',
      activeNames: ['2'],
      isHide: true,
      checkAll: false,
      isChecked: false,
      isIndeterminate: true,
      backgroundColor: "#ffffff",
      predefineColors: ["#ffffff", "#FF0000", "#000000", "#FFF800", "#00FF0A", "#FD00FF", "#0095FF"],
      checked: true,
      // 模板图片保存数组
      templateImgs: [],
      zoomCounter: 100,
      tipTitle: "",
      imageBase64: "",
      isCollapsed: false,
      backColor: "#eec5c5",
      backGroundStatus: false,
      bgcolor: "#ff0000",
      itemWidth: "230px",
      done: false,
      isOpen: false,
      isMove: false,
      src: "",
      msg: "",
      canvas: null,
      templateData: {},
      mouseFrom: {},
      mouseTo: {},
      moveCount: 1,
    };
  },
  mounted () {
    this.initeditorCanvas();
  },
  methods: {
    handleSizeSelection () {
      let str = "一寸"
      if (this.radio == 1) {
        str = "原尺寸"
      }
      if (this.radio == 2) {
        str = "小一寸"
      }
      if (this.radio == 3) {
        str = "一寸"
      }
      if (this.radio == 4) {
        str = "大一寸"
      }
      if (this.radio == 5) {
        str = "小二寸"
      }
      if (this.radio == 6) {
        str = "二寸"
      }
      if (this.radio == 7) {
        str = "大二寸"
      }
      editorCanvas.remove(rect);
      size = this.acquireSize(str);
      console.log(size)
      rect = this.drawRect(size);
      console.log(rect);
      editorCanvas.add(rect);
    },
    handleChange (val) {
      console.log(val);
    },
    downLoadImage1 () {
      this.done = true
      let base64URl = editorCanvas.toDataURL({
        formart: 'png',
        multiplier: 1
      })
      this.imageBase64 = base64URl
      this.done = false
    },
    saveTemplates () {
      console.log("你点击了模板保存");
      let base64URl = editorCanvas.toDataURL({
        formart: "jpg",
        multiplier: 1,
      });
    },

    addTemplates () {
      console.log("添加模板");
    },
    initD () {
      // 监听鼠标按下
      const obj = editorCanvas.getActiveObject();
      editorCanvas.on("mouse:down", (options) => {
        // 记录当前鼠标的起点坐标
        if (!obj) {
          this.mouseFrom.x = options.e.clientX - editorCanvas._offset.left;
          this.mouseFrom.y = options.e.clientY - editorCanvas._offset.top;
        }
      });
      // 监听鼠标移动
      editorCanvas.on("mouse:move", (options) => {
        // 记录当前鼠标移动终点坐标
        if (!obj) {
          this.mouseTo.x = options.e.clientX - editorCanvas._offset.left
          this.mouseTo.y = options.e.clientY - editorCanvas._offset.top
          this.drawRect();
        }
      });
      editorCanvas.on("mouse:up", (options) => {
        if (!obj) {
          this.mouseFrom.x = options.e.clientX - editorCanvas._offset.left;
          this.mouseFrom.y = options.e.clientY - editorCanvas._offset.top;
          this.doDrawing = false;
          this.canvasObject = null;
          this.mouseFrom = {};
          this.mouseTo = {}
        }
      });
      editorCanvas.on("selection:created", (option) => {
        if (option) {
          this.doDrawing = false;
        }
      })
    },
    getTransformedPosX (x) {
      let zoom = Number(editorCanvas.getZoom())
      return (x - editorCanvas.viewportTransform[4]) / zoom;
    },
    getTransformedPosY (y) {
      let zoom = Number(editorCanvas.getZoom())
      return (y - editorCanvas.viewportTransform[5]) / zoom;
    },
    // 初始化模板编辑画布
    initeditorCanvas () {

      editorCanvas = new fabric.Canvas("editorCanvas", {
        devicePixelRatio: true,
        width: "1185",
        height: "650",
        originX: "center",
        originY: "center",
        backgroundColor: "#ffffff",
        transparentCorners: false,
      });
      // TODO: 这里利用传进来的参数替换“一寸”
      // 根据不同证件照要求创建框框
      // 创建一个矩形对象
      /* size = this.acquireSize("一寸")
      rect = this.drawRect(size)
      editorCanvas.add(rect); */
      if (this.$route.params.img != undefined) {
        currentImg = this.$route.params.img;
        /* let uploadImageUrl = this.$route.params.imgUrl */
        targetFileName = this.$route.params.fileName
        targetOriginImg = this.$route.params.orginImg
        /* let ext = targetFileName.substring(targetFileName.lastIndexOf(".") + 1, targetFileName.length)
        console.log(ext)
        console.log(targetFileName)
        this.getUrlBase64(uploadImageUrl, ext, function (base64) {
          targetImg = base64 // base64.replace(/^data:image\/\w+;base64,/, "");
          targetImg = targetImg.replace(/^data:image\/\w+;base64,/, "");
          console.log(base64);//base64编码值
          console.log(targetImg)
        }); */
        originalSize = { height: currentImg.height, width: currentImg.width };
        currentImg.top = 0;
        currentImg.left = 0;
        currentImg.scaleX = 0.3;
        currentImg.scaleY = 0.3;
        editorCanvas.add(currentImg);
      }
      // editorCanvas.remove(rect);
      editorCanvas.preserveObjectStacking = true;
    },
    // 载入图片
    imgDraw () {
      document.getElementById("uploadfile").click();
    },
    // 载入图片
    uploadButton2 () {
      document.getElementById("uploadbackground").click();
    },
    uploadFile (e) {
      editorCanvas.isDrawingMode = false;
      let file = e.target.files[0];
      console.log(file);
      let reader = new FileReader();
      reader.onload = (e) => {
        this.fullscreenLoading = true;
        // TODO: 这个地方读取到的也是BASE64图片 需要传到后端 然后后端返回一个url地址
        let data = e.target.result;
        // 保证原图片能够正常存储下来
        var imgs = data.replace(/^data:image\/\w+;base64,/, "");
        targetOriginImg = imgs
        let fileName = file.name;
        targetFileName = fileName
        let userId = this.$store.state.userId
        let segURL = '/api/seg';
        let params = {
          imgStr: imgs,
          fileName: fileName,
          userId: userId
        }
        this.$axios.post(segURL, params).then(res => {
          let imgUrl = 'img\\photos\\';
          imgUrl += res.data;
          console.log(imgUrl)
          fabric.Image.fromURL(imgUrl, (img) => {
            // 封装成了fabric格式的图片
            console.log(img);
            editorCanvas.remove(currentImg)
            currentImg = img
            originalSize = { height: currentImg.height, width: currentImg.width };
            currentImg.top = 0;
            currentImg.left = 0;
            currentImg.scaleX = 0.3;
            currentImg.scaleY = 0.3;
            editorCanvas.add(currentImg).renderAll();
            // this.handleSizeSelection();
            this.fullscreenLoading = false;
          });
        }).catch(error => {
          console.log(error.message);
        })
      };
      reader.readAsDataURL(file);
      e.target.value = "";
    },

    uploadBackground (e) {
      editorCanvas.isDrawingMode = false;
      let file = e.target.files[0];
      console.log(file);
      let reader = new FileReader();
      reader.onload = (e) => {
        this.fullscreenLoading = true;
        // TODO: 这个地方读取到的也是BASE64图片 需要传到后端 然后后端返回一个url地址
        let data = e.target.result;
        fabric.Image.fromURL(data, (img) => {
          // 封装成了fabric格式的图片
          console.log(img);
          diyBackground = img;
          editorCanvas.add(diyBackground).renderAll();
          editorCanvas.bringToFront(currentImg);
          this.fullscreenLoading = false;
        });
      };
      reader.readAsDataURL(file);
      e.target.value = "";
    },

    // 下载图片
    /* downLoad () {
      this.done = true;
      const dataURL = editorCanvas.toDataURL({
        width: editorCanvas.width,
        height: editorCanvas.height,
        format: "png",
      });
      const link = document.createElement("a");
      link.download = "个性图片.png";
      link.href = dataURL;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    }, */

    downLoad () {
      this.done = true;
      const dataURL = editorCanvas.toDataURL({
        width: editorCanvas.width,
        height: editorCanvas.height,
        format: "png",
      });
      // 新生成照片的base64格式
      console.log(dataURL);
      let savedImg = dataURL.replace(/^data:image\/\w+;base64,/, "");
      let userId = this.$store.state.userId;
      let saveUrl = "/api/save";
      let paramFileName = targetFileName.split(".");
      let downloadName = paramFileName[0] + '_个性化照片.png';
      let params = {
        imgStr: savedImg,
        fileName: downloadName,
        userId: userId
      }
      this.$axios.post(saveUrl, params).then(res => {
        console.log("saved successfully");
      })
      const link = document.createElement("a");
      link.download = downloadName;
      link.href = dataURL;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    },

    // 画定位框 虚线
    // TODO: 变成虚线
    drawRect (size) {
      let rect = new fabric.Rect({
        stroke: "black",
        strokeDashArray: [3, 1],
        strokeWidth: 2,
        left: editorCanvas.getCenter().left - 2 - size.width / 2, //距离左边的距离
        top: editorCanvas.getCenter().top - 2 - size.height / 2, //距离上边的距离
        fill: "transparent", //填充的颜色
        width: size.width + 2, //矩形宽度
        height: size.height + 2, //矩形高度
        selectable: false, // 定位框不能移动
      });
      return rect;
    },

    // 获得尺寸大小
    acquireSize (sizeName) {
      if (sizeName == "小一寸") {
        this.radio = 2;
        return { height: 390, width: 260 };
      }
      if (sizeName == "一寸") {
        this.radio = 3;
        return { height: 413, width: 295 };
      }
      if (sizeName == "大一寸") {
        this.radio = 4;
        return { height: 567, width: 413 };
      }
      if (sizeName == "小二寸") {
        this.radio = 5;
        return { height: 567, width: 390 };
      }
      if (sizeName == "二寸") {
        this.radio = 6;
        return { height: 579, width: 413 };
      }
      if (sizeName == "大二寸") {
        this.radio = 7;
        return { height: 626, width: 413 };
      }
      if (sizeName == "原尺寸") {
        this.radio = 1;
        return originalSize;
      }
    },

    // 清空画布
    resetCanvas () {
      let children = editorCanvas.getObjects();
      if (children.length > 0) {
        editorCanvas.remove(...children);
      }
      rect = null;
      size = null;
      originalSize = null;
      editorCanvas.setBackgroundColor("#fff");
    },

    // 缩放
    zoomIt (factor) {
      let zoomCounter = this.zoomCounter;
      let cWidth = editorCanvas.width;
      let cHeight = editorCanvas.height;

      /* 同步缩小 */
      if (factor < 1 && zoomCounter > 0) {
        this.zoomCounter -= 20;
        editorCanvas.setWidth(cWidth * factor);
        editorCanvas.setHeight(cHeight * factor);

        const objects = editorCanvas.getObjects();
        for (let i in objects) {
          let scaleX = objects[i].scaleX;
          let scaleY = objects[i].scaleY;
          let left = objects[i].left;
          let top = objects[i].top;
          let tempScaleX = scaleX * factor;
          let tempScaleY = scaleY * factor;
          let tempLeft = left * factor;
          let tempTop = top * factor;
          objects[i].scaleX = tempScaleX;
          objects[i].scaleY = tempScaleY;
          objects[i].left = tempLeft;
          objects[i].top = tempTop;
          objects[i].setCoords();
          let zoomPoint = new fabric.Point(
            editorCanvas.width / 2,
            editorCanvas.height / 2
          );
          editorCanvas.zoomToPoint(zoomPoint, factor);
          editorCanvas.renderAll();
          editorCanvas.calcOffset();
        }
      }

      /* 同步放大 */
      if (factor > 1 && zoomCounter < 100) {
        this.zoomCounter += 20;
        editorCanvas.setWidth(cWidth * factor);
        editorCanvas.setHeight(cHeight * factor);
        const objects = editorCanvas.getObjects();
        for (let i in objects) {
          let scaleX = objects[i].scaleX;
          let scaleY = objects[i].scaleY;
          let left = objects[i].left;
          let top = objects[i].top;
          let tempScaleX = scaleX * factor;
          let tempScaleY = scaleY * factor;
          let tempLeft = left * factor;
          let tempTop = top * factor;
          objects[i].scaleX = tempScaleX;
          objects[i].scaleY = tempScaleY;
          objects[i].left = tempLeft;
          objects[i].top = tempTop;
          objects[i].setCoords();
        }
        let zoomPoint = new fabric.Point(
          editorCanvas.width / 2,
          editorCanvas.height / 2
        );
        editorCanvas.zoomToPoint(zoomPoint, factor);
        editorCanvas.renderAll();
        editorCanvas.calcOffset();
      } else {
        return;
      }
    },

    // 背景颜色
    changeBgColor (value) {
      let mbgColor = value;
      const obj = editorCanvas;
      if (obj) {
        obj.set({
          backgroundColor: mbgColor,
        });
        editorCanvas.renderAll();
      }
      this.templateData.bgColor = mbgColor;
    },
    urlToBlob (the_url, callback) {
      let xhr = new XMLHttpRequest();
      xhr.open("get", the_url, true);
      xhr.responseType = "blob";
      xhr.onload = function () {
        if (this.status == 200) {
          if (callback) {
            callback(this.response);
          }
        }
      };
      xhr.send();
    },
    /**
     * @param imgStr base64图片格式
     * @param fileName 文件名
     * @param userId 登陆用户的ID
     */
    beautify () {
      console.log(targetOriginImg)
      this.fullscreenLoading = true;
      let userId = this.$store.state.userId
      let beautifyURL = '/api/beautify';
      let paramFileName = targetFileName.split(".");
      let params = {
        imgStr: targetOriginImg,
        fileName: paramFileName[0] + '_beautify.jpg',
        userId: userId
      }
      this.$axios.post(beautifyURL, params).then(res => {
        let imgUrl = 'img\\photos\\';
        imgUrl += res.data;
        console.log(imgUrl)

        /* let ext = imgUrl.substring(imgUrl.lastIndexOf(".") + 1, imgUrl.length)
        console.log(ext)
        this.getUrlBase64(imgUrl, ext, function (base64) {
          segImg = base64 // base64.replace(/^data:image\/\w+;base64,/, "");
          segImg = segImg.replace(/^data:image\/\w+;base64,/, "");
        }); */
        this.urlToBlob(imgUrl, function (img_blob) {
          var reader = new FileReader();
          reader.readAsDataURL(img_blob);    // 解析成base64格式
          reader.onload = function () {
            console.log(this.result);        // 解析后的数据，如下图
            segImg = this.result
            segImg = segImg.replace(/^data:image\/\w+;base64,/, "");
          }
        });

        setTimeout(() => {
          console.log(segImg)
          let segURL = '/api/seg';
          let paramFileName = targetFileName.split(".");
          let params = {
            imgStr: segImg,
            fileName: paramFileName[0] + '_plus_beautify.jpg',
            userId: userId
          }
          this.$axios.post(segURL, params).then(res => {
            let imgUrl = 'img\\photos\\';
            imgUrl += res.data;
            console.log(imgUrl)
            fabric.Image.fromURL(imgUrl, (img) => {
              // 封装成了fabric格式的图片
              console.log(img);
              editorCanvas.remove(currentImg)
              currentImg = img
              originalSize = { height: currentImg.height, width: currentImg.width };
              currentImg.top = 0;
              currentImg.left = 0;
              currentImg.scaleX = 0.3;
              currentImg.scaleY = 0.3;
              /* this.handleSizeSelection(); */
              editorCanvas.add(currentImg).renderAll();
              this.fullscreenLoading = false;
            });
          }).catch(error => {
            console.log(error.message);
          })
        }, 1500);



        /* fabric.Image.fromURL(imgUrl, (img) => {
          // 封装成了fabric格式的图片
          console.log(img);
          originalSize = { height: img.height, width: img.width };
          img.top = 0;
          img.left = 0;
          img.scaleX = 0.3;
          img.scaleY = 0.3;
          editorCanvas.add(img).renderAll();
          this.handleSizeSelection();
          this.fullscreenLoading = false;
        }); */
      }).catch(error => {
        console.log(error.message);
      })
    },

    /**
     * @param imgStr base64图片格式
     * @param fileName 文件名
     * @param userId 登陆用户的ID
     */
    makeup () {
      console.log(targetOriginImg)
      this.fullscreenLoading = true;
      let userId = this.$store.state.userId
      let makeupURL = '/api/makeup';
      let paramFileName = targetFileName.split(".");
      let params = {
        imgStr: targetOriginImg,
        fileName: paramFileName[0] + '_makeup.jpg',
        userId: userId
      }
      this.$axios.post(makeupURL, params).then(res => {
        let imgUrl = 'img\\photos\\';
        imgUrl += res.data;
        console.log(imgUrl)

        /* let ext = imgUrl.substring(imgUrl.lastIndexOf(".") + 1, imgUrl.length)
        console.log(ext)
        this.getUrlBase64(imgUrl, ext, function (base64) {
          segImg = base64 // base64.replace(/^data:image\/\w+;base64,/, "");
          segImg = segImg.replace(/^data:image\/\w+;base64,/, "");
        }); */
        this.urlToBlob(imgUrl, function (img_blob) {
          var reader = new FileReader();
          reader.readAsDataURL(img_blob);    // 解析成base64格式
          reader.onload = function () {
            console.log(this.result);        // 解析后的数据，如下图
            segImg = this.result
            segImg = segImg.replace(/^data:image\/\w+;base64,/, "");
          }
        });

        setTimeout(() => {
          console.log(segImg)
          let segURL = '/api/seg';
          let paramFileName = targetFileName.split(".");
          let params = {
            imgStr: segImg,
            fileName: paramFileName[0] + '_plus_makeup.jpg',
            userId: userId
          }
          this.$axios.post(segURL, params).then(res => {
            let imgUrl = 'img\\photos\\';
            imgUrl += res.data;
            console.log(imgUrl)
            fabric.Image.fromURL(imgUrl, (img) => {
              // 封装成了fabric格式的图片
              console.log(img);
              editorCanvas.remove(currentImg)
              currentImg = img
              originalSize = { height: currentImg.height, width: currentImg.width };
              currentImg.top = 0;
              currentImg.left = 0;
              currentImg.scaleX = 0.3;
              currentImg.scaleY = 0.3;
              /* this.handleSizeSelection(); */
              editorCanvas.add(currentImg).renderAll();
              this.fullscreenLoading = false;
            });
          }).catch(error => {
            console.log(error.message);
          })
        }, 1500);



        /* fabric.Image.fromURL(imgUrl, (img) => {
          // 封装成了fabric格式的图片
          console.log(img);
          originalSize = { height: img.height, width: img.width };
          img.top = 0;
          img.left = 0;
          img.scaleX = 0.3;
          img.scaleY = 0.3;
          editorCanvas.add(img).renderAll();
          this.handleSizeSelection();
          this.fullscreenLoading = false;
        }); */
      }).catch(error => {
        console.log(error.message);
      })
    },

    /**
     * @param imgStr base64图片格式
     * @param fileName 文件名
     * @param userId 登陆用户的ID
     */
    beauty_shape () {
      console.log(targetOriginImg)
      this.fullscreenLoading = true;
      let userId = this.$store.state.userId
      let beauty_shapeURL = '/api/beauty_shape';
      let paramFileName = targetFileName.split(".");
      let params = {
        imgStr: targetOriginImg,
        fileName: paramFileName[0] + '_beauty_shape.jpg',
        userId: userId
      }
      this.$axios.post(beauty_shapeURL, params).then(res => {
        let imgUrl = 'img\\photos\\';
        imgUrl += res.data;
        console.log(imgUrl)

        /* let ext = imgUrl.substring(imgUrl.lastIndexOf(".") + 1, imgUrl.length)
        console.log(ext)
        this.getUrlBase64(imgUrl, ext, function (base64) {
          segImg = base64 // base64.replace(/^data:image\/\w+;base64,/, "");
          segImg = segImg.replace(/^data:image\/\w+;base64,/, "");
        }); */
        this.urlToBlob(imgUrl, function (img_blob) {
          var reader = new FileReader();
          reader.readAsDataURL(img_blob);    // 解析成base64格式
          reader.onload = function () {
            console.log(this.result);        // 解析后的数据，如下图
            segImg = this.result
            segImg = segImg.replace(/^data:image\/\w+;base64,/, "");
          }
        });

        setTimeout(() => {
          console.log(segImg)
          let segURL = '/api/seg';
          let paramFileName = targetFileName.split(".");
          let params = {
            imgStr: segImg,
            fileName: paramFileName[0] + '_plus_beauty_shape.jpg',
            userId: userId
          }
          this.$axios.post(segURL, params).then(res => {
            let imgUrl = 'img\\photos\\';
            imgUrl += res.data;
            console.log(imgUrl)
            fabric.Image.fromURL(imgUrl, (img) => {
              // 封装成了fabric格式的图片
              console.log(img);
              editorCanvas.remove(currentImg)
              currentImg = img
              originalSize = { height: currentImg.height, width: currentImg.width };
              currentImg.top = 0;
              currentImg.left = 0;
              currentImg.scaleX = 0.3;
              currentImg.scaleY = 0.3;
              /* this.handleSizeSelection(); */
              editorCanvas.add(currentImg).renderAll();
              this.fullscreenLoading = false;
            });
          }).catch(error => {
            console.log(error.message);
          })
        }, 1500);



        /* fabric.Image.fromURL(imgUrl, (img) => {
          // 封装成了fabric格式的图片
          console.log(img);
          originalSize = { height: img.height, width: img.width };
          img.top = 0;
          img.left = 0;
          img.scaleX = 0.3;
          img.scaleY = 0.3;
          editorCanvas.add(img).renderAll();
          this.handleSizeSelection();
          this.fullscreenLoading = false;
        }); */
      }).catch(error => {
        console.log(error.message);
      })
    },

    /**
     * @param imgStr base64图片格式
     * @param fileName 文件名
     * @param userId 登陆用户的ID
     */
    filter () {
      console.log(targetOriginImg)
      this.fullscreenLoading = true;
      let userId = this.$store.state.userId
      let filterURL = '/api/filter';
      let paramFileName = targetFileName.split(".");
      let params = {
        imgStr: targetOriginImg,
        fileName: paramFileName[0] + '_filter.jpg',
        userId: userId,
        filter_id: this.radio
      }
      this.$axios.post(filterURL, params).then(res => {
        let imgUrl = 'img\\photos\\';
        imgUrl += res.data;
        console.log(imgUrl)

        /* let ext = imgUrl.substring(imgUrl.lastIndexOf(".") + 1, imgUrl.length)
        console.log(ext)
        this.getUrlBase64(imgUrl, ext, function (base64) {
          segImg = base64 // base64.replace(/^data:image\/\w+;base64,/, "");
          segImg = segImg.replace(/^data:image\/\w+;base64,/, "");
        }); */
        this.urlToBlob(imgUrl, function (img_blob) {
          var reader = new FileReader();
          reader.readAsDataURL(img_blob);    // 解析成base64格式
          reader.onload = function () {
            console.log(this.result);        // 解析后的数据，如下图
            segImg = this.result
            segImg = segImg.replace(/^data:image\/\w+;base64,/, "");
          }
        });

        setTimeout(() => {
          console.log(segImg)
          let segURL = '/api/seg';
          let paramFileName = targetFileName.split(".");
          let params = {
            imgStr: segImg,
            fileName: paramFileName[0] + '_plus_filter.jpg',
            userId: userId
          }
          this.$axios.post(segURL, params).then(res => {
            let imgUrl = 'img\\photos\\';
            imgUrl += res.data;
            console.log(imgUrl)
            fabric.Image.fromURL(imgUrl, (img) => {
              // 封装成了fabric格式的图片
              console.log(img);
              editorCanvas.remove(currentImg)
              currentImg = img
              originalSize = { height: currentImg.height, width: currentImg.width };
              currentImg.top = 0;
              currentImg.left = 0;
              currentImg.scaleX = 0.3;
              currentImg.scaleY = 0.3;
              /* this.handleSizeSelection(); */
              editorCanvas.add(currentImg).renderAll();
              this.fullscreenLoading = false;
            });
          }).catch(error => {
            console.log(error.message);
          })
        }, 1500);



        /* fabric.Image.fromURL(imgUrl, (img) => {
          // 封装成了fabric格式的图片
          console.log(img);
          originalSize = { height: img.height, width: img.width };
          img.top = 0;
          img.left = 0;
          img.scaleX = 0.3;
          img.scaleY = 0.3;
          editorCanvas.add(img).renderAll();
          this.handleSizeSelection();
          this.fullscreenLoading = false;
        }); */
      }).catch(error => {
        console.log(error.message);
      })
    },
  }
};
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
}
body {
  font-family: "PingFang SC", "Helvetica Neue", Helvetica, "microsoft yahei",
    arial, STHeiTi, sans-serif;
}

a {
  text-decoration: none;
}

.handleSave {
  padding-top: 20px;
}

.el-aside {
  background-color: white;
  color: #333;
  text-align: center;
}

.el-main {
  background-color: #eeeeee;
  color: #333;
  text-align: center;
  line-height: 160px;
}

.el-main .content-show {
  display: flex;
  flex-direction: column;
  item-align: center;
}

.text-edit h6 {
  height: 40px;
  color: black;
}

.text-edit .el-form {
  margin: 15px;
}

.btn-delete {
  min-height: 36px;
}

.template-content {
  padding: 0px;
  margin: 0px;
}

ton-box:hover {
  color: white;
  opacity: 1;
}

.templateContent {
  width: 100%;
  padding: 0px;
  background-color: #fff;
}

.template-upload {
  width: 256px;
  height: 130px;
  border: 2px solid #adadad;
  margin: 10px auto;
  line-height: 130px;
  text-align: center;
  cursor: pointer;
}

.template-img {
  width: 300px;
  height: 130px;
  margin: 0px auto;
  line-height: 130px;
  text-align: center;
  cursor: pointer;
}

#editorCanvas {
  box-shadow: 0 0 25px #cac6c6;
  width: 100%;
  display: block;
  margin: 10px auto;
  height: 100%;
}

.text-setting {
  text-align: center;
  width: 80px;
  font-size: 18px;
  font-weight: 500;
  margin-right: 170px;
}

.text-edit h4,
.side-right h4,
.show h1 {
  text-align: center;
  font-size: 16px;
  color: #585858;
  font-weight: normal;
  margin: 15px auto;
}

.show h1 {
  text-align: center;
  color: #585858;
  padding: 15px;
  font-weight: bold;
  font-size: 24px;
  margin: 15px auto;
}

.btn-zoom {
  display: inline-block;
  line-height: 1;
  height: 40px;
  width: 104px;
  white-space: nowrap;
  -webkit-appearance: none;
  text-align: center;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  outline: 0;
  margin: 0;
  -webkit-transition: 0.1s;
  transition: 0.1s;
  font-weight: 500;
  font-size: 14px;
  border-radius: 4px;
  color: #fff;
  background-color: #6a60e3;
  border-color: #6a60e3;
}

.btn-download,
.btn-save,
.btn-load,
.btn-reset {
  display: inline-block;
  line-height: 1;
  height: 40px;
  width: 104px;
  white-space: nowrap;
  -webkit-appearance: none;
  text-align: center;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  outline: 0;
  margin: 0;
  -webkit-transition: 0.1s;
  transition: 0.1s;
  font-weight: 500;
  padding: 12px 20px;
  font-size: 14px;
  border-radius: 4px;
  color: #fff;
  background-color: #6a60e3;
  margin: 10px 0px 0px 10px;
  border-color: #6a60e3;
}

.btn-reset {
  height: 40px;
  color: #fff;
  width: 98px;
  background-color: #6a60e3;
  margin: 10px 0px 0px 10px;
  border-color: #6a60e3;
}

.el-form-item {
  margin: 15px auto;
}

.el-form {
  height: 70px;
  line-height: 70px;
}

.el-header {
  height: 60px;
}

.el-form--inline .el-form-item__content {
  margin: 0;
  padding: 0;
  height: 60px;
}

.btn_style {
  display: inline-block;
  height: 40px;
  margin-left: 15px;
  width: 120px;
  line-height: 40px;
  white-space: nowrap;
  -webkit-appearance: none;
  text-align: center;
  font-weight: 500;
  background: none;
  padding: 0px 20px 20px;
  font-size: 14px;
  border-radius: 4px;
  color: #fff;
}
</style>


