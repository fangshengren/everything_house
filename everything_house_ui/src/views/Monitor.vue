<template>
  <div id="home">
    <el-row>
      <el-col :span="12">
        <div id="main" style="width:500px; height:400px"></div>
      </el-col>
      <el-col :span="12">
        <div id="pie" style="width:500px; height:400px"></div>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import * as echarts from 'echarts';
import Aside from '@/components/Aside.vue'
import Header from '@/components/Header.vue'
export default {
  name:"Monitor",
  components:{
    Aside,
    Header
  },
  data(){
    return{
      AdminNumber:0,
      UserNumber:0,
    }
  },
  created() {
    this.load();
  },
  mounted() {       //使用mounted的目的是为了等页面元素渲染之后再触发
    var chartDom = document.getElementById('main');//main 中放了一个折线图和柱状图
    var myChart = echarts.init(chartDom);
    var charOption;
    charOption = {
      xAxis: {
        type: 'category',
        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: [150, 230, 224, 218, 135, 147, 260],
          type: 'line'
        },
        {
          data: [150, 230, 224, 218, 135, 147, 260],
          type: 'bar'
        }
      ]
    };
    myChart.setOption(charOption);


    this.renderPieChart();
  },
  methods: {
    async load() {
      try {
        const res1 = await this.request.get("/user/UserNumber");
        this.UserNumber = res1;
        //console.log("UserNumber:", this.UserNumber);

        const res2 = await this.request.get("/user/AdminNumber");
        this.AdminNumber = res2;
        //console.log("AdminNumber:", this.AdminNumber);

        //console.log(this.UserNumber, this.AdminNumber);

        // 加载完数据后重新渲染饼状图
        this.renderPieChart();
      } catch (error) {
        //console.error("Error while loading data:", error);
      }

    },
    renderPieChart() {
      var pieDom = document.getElementById('pie');
      var pieChart = echarts.init(pieDom);
      var pieOption;

      pieOption = {
        title: {
          text: 'Administrators and Users',
          subtext: 'Relative Data',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: 'Number',
            type: 'pie',
            radius: '55%',
            label: {
              normal: {
                show: true,
                position: 'inner',
                textStyle:{
                  fontWeight:300,
                  fontSize:13,
                  color:"#fff"
                },
                formatter: '{d}%' //自定义显示格式(b:name, c:value, d:百分比)
              }
            },
            data: [
              {value: this.AdminNumber, name: 'Administrator'},
              {value: this.UserNumber, name: 'User'},
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };

      pieChart.setOption(pieOption);
    },
  },



}
</script>

