<!-- eslint-disable no-return-assign -->
<template>
  <div>
    <el-row>
      <el-col :span="12">
        <el-card>
          <div id="proType" style="width:700px; height:300px" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card style="margin-left: 10px;">
          <div id="deProSchool" style="width:600px; height:300px" />
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-card>
          <div id="proStatus" style="width:700px; height:300px" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card style="margin-left: 10px;">
          <div id="passedPro" style="width:600px; height:300px" />
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
    // 项目成果类别饼状图
    var proTypeOption = {
      title: {
        text: '项目成果类别',
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
          left: 80,
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
          left: 80,
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
    var proTypeChartDom = document.getElementById('proType')
    var proTypeChart = echarts.init(proTypeChartDom)
    echartsApi.proVisByResType().then(res => {
      proTypeOption.series[0].data = res.data
      proTypeOption.series[1].data = res.data
      proTypeChart.setOption(proTypeOption)
    })

    // 项目状态饼状图
    var proStatusOption = {
      title: {
        text: '项目状态',
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
    var proStatusChartDom = document.getElementById('proStatus')
    var proStatusChart = echarts.init(proStatusChartDom)
    echartsApi.proVisByStatus().then(res => {
      proStatusOption.series[0].data = res.data
      proStatusOption.series[1].data = res.data
      proStatusChart.setOption(proStatusOption)
    })

    // 已申报成功项目学院柱状图
    var deProSchoolOption = {
      title: {
        text: '各学院已申报成功项目',
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
    var deProSchoolChartDom = document.getElementById('deProSchool')
    var deProSchoolChart = echarts.init(deProSchoolChartDom)
    echartsApi.deProVisBySchool().then(res => {
      deProSchoolOption.xAxis.data = res.data.x
      deProSchoolOption.series[0].data = res.data.y
      deProSchoolOption.series[1].data = res.data.y
      deProSchoolChart.setOption(deProSchoolOption)
    })

    // 已结题项目学院柱状图
    var passedProSchoolOption = {
      title: {
        text: '各学院已结题项目',
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
    var passedProSchoolChartDom = document.getElementById('passedPro')
    var passedProSchoolChart = echarts.init(passedProSchoolChartDom)
    echartsApi.conProVisBySchool().then(res => {
      passedProSchoolOption.xAxis.data = res.data.x
      passedProSchoolOption.series[0].data = res.data.y
      passedProSchoolOption.series[1].data = res.data.y
      passedProSchoolChart.setOption(passedProSchoolOption)
    })
  }
}
</script>

<style></style>
