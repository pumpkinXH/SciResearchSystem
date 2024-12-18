<!-- eslint-disable vue/html-self-closing -->
<!-- eslint-disable vue/html-closing-bracket-newline -->
<!-- eslint-disable vue/html-indent -->
<!-- eslint-disable vue/max-attributes-per-line -->
<template>
  <div class="dashboard-container">
    <el-card>
      <div class="dashboard-text">欢迎{{ name }}，卡号：{{ userForm.card }}，您的角色：{{ userForm.roleDesc }}，所属：{{
        userForm.schoolDesc }}</div>
    </el-card>
    <div v-show="showChart">
      <el-row>
        <div class="dashboard-text" style="margin-top: 20px; margin-left: 20px;">用户分析</div>
        <el-col :span="12">
          <el-card>
            <div id="userSchool" style="width:700px; height:350px" />
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card style="margin-left: 10px;">
            <div id="userRole" style="width:600px; height:350px" />
          </el-card>
        </el-col>
      </el-row>
    </div>
    <el-card v-if="userForm.userRoleId !== 1">
      <div class="dashboard-text">起止日期</div>
      <div>
        <span class="svg-container"><svg-icon icon-class="date" /></span>
        项目申报起止日期：
        <span class="date">2022-11-15 至 2023-03-01</span>
        <span class="svg-container" style="margin-left: 75px;"><svg-icon icon-class="date" /></span>
        <span>中期检查起止日期：</span>
        <span class="date">2022-04-19 至 2023-04-26</span>
        <span class="svg-container" style="margin-left: 75px;"><svg-icon icon-class="date" /></span>
        <span>项目总起止日期</span>
        <span class="date">2022-11-15 至 2023-06-12</span>
      </div>
    </el-card>
    <el-row>
      <el-col v-if="userForm.userRoleId == 2">
        <el-card>
          <span class="dashboard-text">校内公告</span>
          <span style="margin-left: 20px;"> <el-button class="el-icon-refresh-right" round @click="refresh" /></span>
          <el-table :data="colNoticeList" stripe style="width: 100%">
            <el-table-column label="序号" width="100">
              <template slot-scope="scope">
                <!--(pageNo-1) * pageSize + index + 1-->
                {{ (colSearchModel.pageNo - 1) * colSearchModel.pageSize + scope.$index + 1 }}
              </template>
            </el-table-column>
            <el-table-column prop="noticeTitle" label="通知标题" width="870">
              <template slot-scope="scope">
                <a style="color: blue; text-decoration: underline;"
                  @click="viewContent(scope.row.noticeId, scope.row.noticeDesc)">{{
                    scope.row.noticeTitle }}</a>.
              </template>
            </el-table-column>
            <el-table-column prop="noticeTime" label="通知时间" width="200"></el-table-column>
          </el-table>
          <!-- 分页组件 -->
          <el-pagination :current-page="colSearchModel.pageNo" :page-sizes="[5, 10, 20, 50]"
            :page-size="colSearchModel.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="colTotal"
            @size-change="colHandleSizeChange" @current-change="colHandleCurrentChange" />
        </el-card>
      </el-col>
      <el-col v-if="userForm.userRoleId == 3 || userForm.userRoleId == 4" :span="12">
        <el-card>
          <span class="dashboard-text">校内公告</span>
          <span style="margin-left: 20px;"> <el-button class="el-icon-refresh-right" round @click="refresh" /></span>
          <el-table :data="colNoticeList" stripe style="width: 100%">
            <el-table-column label="序号" width="60">
              <template slot-scope="scope">
                <!--(pageNo-1) * pageSize + index + 1-->
                {{ (colSearchModel.pageNo - 1) * colSearchModel.pageSize + scope.$index + 1 }}
              </template>
            </el-table-column>
            <el-table-column prop="noticeTitle" label="通知标题" width="370">
              <template slot-scope="scope">
                <a style="color: blue; text-decoration: underline;"
                  @click="viewContent(scope.row.noticeId, scope.row.noticeDesc)">{{
                    scope.row.noticeTitle }}</a>.
              </template>
            </el-table-column>
            <el-table-column prop="noticeTime" label="通知时间" width="140"></el-table-column>
          </el-table>
          <!-- 分页组件 -->
          <el-pagination :current-page="colSearchModel.pageNo" :page-sizes="[5, 10, 20, 50]"
            :page-size="colSearchModel.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="colTotal"
            @size-change="colHandleSizeChange" @current-change="colHandleCurrentChange" />
        </el-card>
      </el-col>
      <el-col :span="12" :style="{ display: userForm.userRoleId >= 3 ? '' : 'none' }">
        <el-card style="margin-left: 10px;">
          <span class="dashboard-text">院内公告</span>
          <span style="margin-left: 20px;"> <el-button class="el-icon-refresh-right" round @click="refresh" /></span>
          <el-table :data="schNoticeList" stripe style="width: 100%">
            <el-table-column label="序号" width="60">
              <template slot-scope="scope">
                <!--(pageNo-1) * pageSize + index + 1-->
                {{ (schSearchModel.pageNo - 1) * schSearchModel.pageSize + scope.$index + 1 }}
              </template>
            </el-table-column>
            <el-table-column prop="noticeTitle" label="通知标题" width="360">
              <template slot-scope="scope">
                <a style="color: blue; text-decoration: underline;"
                  @click="viewContent(scope.row.noticeId, scope.row.noticeDesc)">{{
                    scope.row.noticeTitle }}</a>.
              </template>
            </el-table-column>
            <el-table-column prop="noticeTime" label="通知时间" width="140"></el-table-column>
          </el-table>
          <!-- 分页组件 -->
          <el-pagination :current-page="schSearchModel.pageNo" :page-sizes="[5, 10, 20, 50]"
            :page-size="schSearchModel.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="schTotal"
            @size-change="schHandleSizeChange" @current-change="schHandleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>
    <!-- 通知内容查看弹窗 -->
    <el-dialog v-drag :title="contentTitle" :visible.sync="dialogContentFormVisible" :close-on-click-modal="false" @close="clearForm">
      <el-form :model="noticeForm">
        <el-form-item label="通知内容" :label-width="formLabelWidth">
          <div id="noticeContext" />
        </el-form-item>
        <el-form-item label="通知文件" :label-width="formLabelWidth" prop="noticeFileUrl"
          :style="{ display: noticeForm.noticeFileUrl == null ? 'none' : '' }">
          <el-button type="primary" style="margin-left: 10px;" @click="download(noticeForm.noticeFileUrl)">下载<i
              class="el-icon-download" /></el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import store from '@/store'
import { getPersonInfo } from '@/api/user'
import noticeApi from '@/api/noticeManage'
import * as echarts from 'echarts'
import echartsApi from '@/api/echartsManage'
import E from 'wangeditor'
let editor

export default {
  name: 'Dashboard',
  data() {
    return {
      showChart: false,
      userForm: {},
      colNoticeList: [],
      noticeForm: {},
      formLabelWidth: '100px',
      colSearchModel: {
        pageNo: 1,
        pageSize: 5
      },
      colTotal: 0,
      schNoticeList: [],
      schSearchModel: {
        pageNo: 1,
        pageSize: 5
      },
      schTotal: 0,
      contentTitle: '通知内容',
      dialogContentFormVisible: false
    }
  },
  computed: {
    ...mapGetters([
      'name'
    ])
  },
  created() {
    this.getUserInfo()
    this.getColNoticeList() // 校内公告
    this.getSchNoticeList() // 院内公告
    editor = ''
  },
  mounted() {
    // 用户学院饼状图
    var userSchoolOption = {
      title: {
        text: '各学院用户数',
        subtext: '比例图',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 0
      },
      series: [
        // 画外部描述的饼图
        {
          type: 'pie',
          radius: '70%',
          left: 50,
          top: 50,
          minAngle: 5,
          data: [],
          label: {
            show: true,
            position: 'outside',
            fontSize: 12,
            formatter: `{b}: {c}`
          },
          labelLine: {
            show: true
          }
        },
        // 画内部百分比的饼图
        {
          type: 'pie',
          radius: '70%',
          left: 50,
          top: 50,
          minAngle: 5,
          data: [],
          label: {
            show: true,
            position: 'inside',
            formatter: `{d}%`
          }
        }
      ]
    }
    var userSchoolChartDom = document.getElementById('userSchool')
    var userSchoolChart = echarts.init(userSchoolChartDom)
    echartsApi.userVisBySchool().then(res => {
      userSchoolOption.series[0].data = res.data
      userSchoolOption.series[1].data = res.data
      userSchoolChart.setOption(userSchoolOption)
    })

    // 各个角色的用户数
    var userRoleOption = {
      title: {
        text: '各个角色用户数',
        subtext: '柱状图',
        left: 'center'
      },
      xAxis: {
        type: 'category',
        data: [],
        axisLabel: {
          interval: 0 // 使x轴横坐标全部显示
        }
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: [],
          type: 'bar',
          label: {
            show: true, // 开启显示
            position: 'top' // 在上方显示
          }
        },
        {
          data: [],
          type: 'line'
        }
      ]
    }
    var userRoleChartDom = document.getElementById('userRole')
    var userRoleChart = echarts.init(userRoleChartDom)
    echartsApi.userVisByRole().then(res => {
      userRoleOption.xAxis.data = res.data.x
      userRoleOption.series[0].data = res.data.y
      userRoleOption.series[1].data = res.data.y
      userRoleChart.setOption(userRoleOption)
    })
  },
  methods: {
    getUserInfo() {
      const token = store.getters.token
      getPersonInfo(token).then(response => {
        this.userForm = response.data
        this.schSearchModel.schoolId = response.data.userSchoolId
        if (this.userForm.userRoleId === 1) {
          this.showChart = true
        }
        this.getSchNoticeList()
      })
    },
    getColNoticeList() {
      this.colSearchModel.schoolId = 1
      noticeApi.getNoticeList(this.colSearchModel).then(response => { // then是回调函数
        this.colNoticeList = response.data.rows
        this.colTotal = response.data.total
      })
    },
    clearForm() {
      this.noticeForm = {}
    },
    colHandleSizeChange(pageSize) {
      this.colSearchModel.pageSize = pageSize
      this.getColNoticeList()
    },
    colHandleCurrentChange(pageNo) {
      this.colSearchModel.pageNo = pageNo
      this.getColNoticeList()
    },
    getSchNoticeList() {
      this.schSearchModel.schoolId = this.userForm.userSchoolId
      noticeApi.getNoticeList(this.schSearchModel).then(response => { // then是回调函数
        this.schNoticeList = response.data.rows
        this.schTotal = response.data.total
      })
    },
    schHandleSizeChange(pageSize) {
      this.schSearchModel.pageSize = pageSize
      this.getSchNoticeList()
    },
    schHandleCurrentChange(pageNo) {
      this.schSearchModel.pageNo = pageNo
      this.getSchNoticeList()
    },
    download(url) {
      window.open(url)
      this.$message({
        type: 'success',
        message: '下载成功'
      })
    },
    refresh() {
      this.getColNoticeList()
      this.getSchNoticeList()
    },
    viewContent(id, content) {
      noticeApi.getNoticeById(id).then(response => {
        this.noticeForm = response.data
      })
      this.dialogContentFormVisible = true
      this.$nextTick(() => {
        setTimeout(() => {
          if (!editor) {
            editor = new E('#noticeContext')
            editor.config.menus = []
            editor.config.menubar = []
            editor.config.toolbar = []
            editor.create()
          }
          editor.txt.html(content)
          editor.$textElem.attr('contenteditable', false)
        }, 50)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }

  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}

.svg-container {
  color: #889aa4;
  vertical-align: middle;
  width: 20px;
  display: inline-block;
  cursor: pointer;
  height: 20px;
  size: 500;
  background-size: 100px 100px;
}

.date {
  color: blue
}
</style>
