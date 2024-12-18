<!-- eslint-disable vue/html-closing-bracket-newline -->
<!-- eslint-disable vue/html-indent -->
<!-- eslint-disable vue/max-attributes-per-line -->
<template>
  <div>
    <!--搜索框-->
    <el-card id="search">
      <el-row>
        <el-col :span="20">
          <el-input v-model="searchModel.noticeTitle" placeholder="通知标题" clearable />
          <el-button type="primary" icon="el-icon-search" round @click="getNoticeList">查询</el-button>
        </el-col>
        <el-col :span="4" align="right">
          <el-button type="primary" icon="el-icon-plus" circle @click="openEditUI(null)" />
        </el-col>
      </el-row>
    </el-card>
    <!-- 结果列表 -->
    <el-card>
      <el-table :data="noticeList" stripe style="width: 100%">
        <el-table-column label="序号" width="70">
          <template slot-scope="scope">
            <!--(pageNo-1) * pageSize + index + 1-->
            {{ (searchModel.pageNo - 1) * searchModel.pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="noticeTitle" label="通知标题" width="720">
          <template slot-scope="scope">
            <a style="color: blue; text-decoration: underline;" @click="viewContent(scope.row.noticeId, scope.row.noticeDesc)">{{ scope.row.noticeTitle }}</a>.
          </template>
        </el-table-column>
        <el-table-column prop="schoolDesc" label="通知所属" width="150" />
        <el-table-column prop="noticeTime" label="通知时间" width="150" />
        <el-table-column label="操作" width="150" align="center">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" circle @click="openEditUI(scope.row.noticeId,scope.row.noticeDesc)" />
            <el-button type="danger" icon="el-icon-delete" circle @click="deleteNotice(scope.row)" />
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!-- 分页组件 -->
    <el-pagination :current-page="searchModel.pageNo" :page-sizes="[5, 10, 20, 50]" :page-size="searchModel.pageSize"
      layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />
    <!-- 通知信息编辑对话框 -->
    <el-dialog v-drag :title="title" :visible.sync="dialogFormVisible" :close-on-click-modal="false" @close="clearForm">
      <el-form :model="noticeForm">
        <el-form-item label="通知标题" :label-width="formLabelWidth" prop="noticeTitle">
          <el-input v-model="noticeForm.noticeTitle" autocomplete="off" />
        </el-form-item>
        <el-form-item label="通知内容" :label-width="formLabelWidth">
          <div id="noticeContext" />
        </el-form-item>
        <el-form-item label="通知文件" :label-width="formLabelWidth" prop="noticeFileUrl">
          <el-input v-model="noticeForm.noticeFileUrl" style="width:430px" autocomplete="off" />
          <el-upload :action="'http://' + serverIp + ':9999/files/upload'" :show-file-list="false"
            :on-success="handleFileUploadSuccess" style="display:inline">
            <el-button type="primary" style="margin-left: 10px;">上传<i class="el-icon-top" /></el-button>
          </el-upload>
          <el-button :style="{ display: noticeForm.noticeFileUrl == null ? 'none' : '' }" type="primary"
            style="margin-left: 10px;" @click="download(noticeForm.noticeFileUrl)">下载<i
              class="el-icon-download" /></el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveNotice">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 通知内容查看弹窗 -->
    <el-dialog v-drag :title="contentTitle" :visible.sync="dialogContentFormVisible" :close-on-click-modal="false" @close="clearForm">
      <el-form :model="noticeForm">
        <el-form-item label="通知内容" :label-width="formLabelWidth">
          <div id="noticeDesc" />
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
import noticeApi from '@/api/noticeManage'
import store from '@/store'
import { getPersonInfo } from '@/api/user'
import { serverIp } from '@/serverConfig'
import E from 'wangeditor'
let editor
let descEditor
export default {
  data() {
    return {
      serverIp: serverIp,
      formLabelWidth: '100px',
      noticeForm: {},
      dialogFormVisible: false, // 控制对话框默认不可见，当点击新增按钮后改为可见
      dialogContentFormVisible: false,
      title: '',
      contentTitle: '通知内容',
      total: 0,
      searchModel: {
        pageNo: 1,
        pageSize: 10
      },
      noticeList: [],
      schoolId: null
    }
  },
  created() { // 构造函数
    this.getUserInfo()
    editor = ''
    descEditor = ''
  },
  methods: {
    getUserInfo() {
      const token = store.getters.token
      getPersonInfo(token).then(response => {
        this.searchModel.schoolId = response.data.userSchoolId
        this.schoolId = response.data.userSchoolId
        this.getNoticeList()
      })
    },
    handleSizeChange(pageSize) {
      this.searchModel.pageSize = pageSize
      this.getNoticeList()
    },
    handleCurrentChange(pageNo) {
      this.searchModel.pageNo = pageNo
      this.getNoticeList()
    },
    getNoticeList() {
      noticeApi.getNoticeList(this.searchModel).then(response => { // then是回调函数
        this.noticeList = response.data.rows
        this.total = response.data.total
      })
    },
    viewContent(id, content) {
      noticeApi.getNoticeById(id).then(response => {
        this.noticeForm = response.data
      })
      this.dialogContentFormVisible = true
      this.$nextTick(() => {
        setTimeout(() => {
          if (!descEditor) {
            descEditor = new E('#noticeDesc')
            descEditor.config.menus = []
            descEditor.config.menubar = []
            descEditor.config.toolbar = []
            descEditor.create()
          }
          descEditor.txt.html(content)
          descEditor.$textElem.attr('contenteditable', false)
        }, 50)
      })
    },
    openEditUI(id, content) {
      if (id == null) {
        this.title = '新增通知'
        this.noticeForm.schoolId = this.schoolId
        this.$nextTick(() => {
          if (!editor) {
            editor = new E('#noticeContext')
            editor.config.uploadImgServer = 'http://' + serverIp + ':9999/files/uploadImg' // 富文本编辑器的图片上传
            editor.config.uploadFileName = 'file'
            editor.create()
          }
          editor.txt.html('') // 清除内容
        })
      } else {
        this.title = '修改通知'
        // 根据ID查询项目
        noticeApi.getNoticeById(id).then(response => {
          this.noticeForm = response.data
        })
        this.noticeForm.schoolId = this.schoolId
        this.$nextTick(() => {
          if (!editor) {
            editor = new E('#noticeContext')
            editor.config.uploadImgServer = 'http://' + serverIp + ':9999/files/uploadImg'
            editor.config.uploadFileName = 'file'
            editor.create()
          }
          editor.txt.html(content)
        })
      }
      this.dialogFormVisible = true
    },
    clearForm() {
      this.noticeForm = {}
    },
    saveNotice() {
      // 提交给后台
      this.noticeForm.noticeDesc = editor.txt.html() // 获取富文本信息
      this.noticeForm.noticeTimeString = this.getCurrentDateTime() // 修改时间
      noticeApi.saveNotice(this.noticeForm).then(response => {
        // 成功提示
        this.$message({
          message: response.message,
          type: 'success'
        })
        // 关闭对话框
        this.dialogFormVisible = false
        // 刷新表格
        this.getNoticeList()
      })
    },
    deleteNotice(notice) {
      this.$confirm(`您确认删除${notice.noticeTitle}项目吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => { // 确定
        noticeApi.deleteNoticeById(notice.noticeId).then(response => {
          this.$message({
            type: 'success',
            message: response.message
          })
          this.getNoticeList()
        })
      }).catch(() => { // 取消
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    handleFileUploadSuccess(res) {
      this.noticeForm.noticeFileUrl = res
      this.$message({
        type: 'success',
        message: '上传成功'
      })
      this.dialogFormVisible = false
      this.dialogFormVisible = true
    },
    download(url) {
      window.open(url)
      this.$message({
        type: 'success',
        message: '下载成功'
      })
    },
    getCurrentDateTime() { // 获取当前时间
      const today = new Date()
      const year = today.getFullYear()
      const month = ('0' + (today.getMonth() + 1)).slice(-2)
      const day = ('0' + today.getDate()).slice(-2)
      const hours = ('0' + today.getHours()).slice(-2)
      const minutes = ('0' + today.getMinutes()).slice(-2)
      const seconds = ('0' + today.getSeconds()).slice(-2)
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    }
  }
}
</script>

<style>
#search .el-input {
  width: 150px;
  margin-right: 10px;
}

.el-dialog .el-input {
  width: '%90'
}
</style>
