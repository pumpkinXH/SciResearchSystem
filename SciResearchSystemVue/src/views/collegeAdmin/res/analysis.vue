<!-- eslint-disable no-return-assign -->
<template>
  <div>
    <el-row>
      <el-col :span="12">
        <el-card>
          <div id="resType" style="width:700px; height:300px" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card style="margin-left: 10px;">
          <div id="deResSchool" style="width:600px; height:300px" />
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-card>
          <div id="resStatus" style="width:700px; height:300px" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card style="margin-left: 10px;">
          <div id="passedRes" style="width:600px; height:300px" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import echartsApi from '@/api/echartsManage'

export default {
  name: 'Analysis',
  data() {
    return {
    }
  },
  mounted() { // 页面元素渲染之后再触发
    // 成果类别饼状图
    var resTypeOption = {
      title: {
        text: '成果类别',
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
          radius: '60%',
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
          radius: '60%',
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
    var resTypeChartDom = document.getElementById('resType')
    var resTypeChart = echarts.init(resTypeChartDom)
    echartsApi.resVisByType().then(res => {
      resTypeOption.series[0].data = res.data
      resTypeOption.series[1].data = res.data
      resTypeChart.setOption(resTypeOption)
    })

    // 成果状态饼状图
    var resStatusOption = {
      title: {
        text: '成果状态',
        subtext: '比例图',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: '0'
      },
      series: [
        // 画外部描述的饼图
        {
          type: 'pie',
          radius: '60%',
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
          radius: '60%',
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
    var resStatusChartDom = document.getElementById('resStatus')
    var resStatusChart = echarts.init(resStatusChartDom)
    echartsApi.resVisByStatus().then(res => {
      resStatusOption.series[0].data = res.data
      resStatusOption.series[1].data = res.data
      resStatusChart.setOption(resStatusOption)
    })

    // 已提交成果学院柱状图
    var deResSchoolOption = {
      title: {
        text: '各学院已提交鉴定成果',
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
    var deResSchoolChartDom = document.getElementById('deResSchool')
    var deResSchoolChart = echarts.init(deResSchoolChartDom)
    echartsApi.deResVisBySchool().then(res => {
      deResSchoolOption.xAxis.data = res.data.x
      deResSchoolOption.series[0].data = res.data.y
      deResSchoolOption.series[1].data = res.data.y
      deResSchoolChart.setOption(deResSchoolOption)
    })

    // 已通过成果柱状图
    var passedResSchoolOption = {
      title: {
        text: '各学院已通过成果',
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
    var passedResSchoolChartDom = document.getElementById('passedRes')
    var passedResSchoolChart = echarts.init(passedResSchoolChartDom)
    echartsApi.passedResVisBySchool().then(res => {
      passedResSchoolOption.xAxis.data = res.data.x
      passedResSchoolOption.series[0].data = res.data.y
      passedResSchoolOption.series[1].data = res.data.y
      passedResSchoolChart.setOption(passedResSchoolOption)
    })
  }
}
</script>

<style></style>
