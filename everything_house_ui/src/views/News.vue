<template>
  <div class="news" id="website">
    <h1>新闻列表</h1>
    <el-form :inline="true" class="news-search-form">
      <el-form-item label="标题关键词">
        <el-input v-model="searchTitle" placeholder="请输入关键词" @input="search"></el-input>
      </el-form-item>
      <el-form-item>
        <div class="button-container">
        <el-button type="primary" @click="getNews">获取新浪最新十条新闻</el-button>
        <el-button type="primary" @click="delTopTen">删除前十条新闻纪录</el-button>
        <el-button type="primary" @click="delAll">删除全部纪录</el-button>
        </div>
      </el-form-item>
    </el-form>
    <el-table :data="newsList"
              style="width: 100%; min-width: 1200px; overflow-x: auto;"
              border>
      <el-table-column label="图片" prop="img">
        <template slot-scope="scope">
          <div class="news-img">
            <img :src="scope.row.imgUrl" alt="图片加载异常或图片不存在" >
          </div>
        </template>
      </el-table-column>
      <el-table-column label="标题" prop="title"></el-table-column>
      <el-table-column label="链接" prop="url">
        <template slot-scope="scope">
          <a :href="scope.row.url" target="_blank">{{scope.row.url}}</a>
        </template>
      </el-table-column>
      <el-table-column label="发布时间" prop="createTime"></el-table-column>
    </el-table>
    <el-pagination
        @current-change="pageChange"
        :current-page.sync="page"
        :page-size="pageSize"
        layout="prev, pager, next"
        :total="total">
    </el-pagination>
  </div>
</template>

<script>
import request from '@/utils/request';
import dealSqlConfirm from '@/utils/dealSql';

export default {
  name: "News",
  mixins: [dealSqlConfirm],
  data() {
    return {
      newsList: [],
      page: 1,
      pageSize: 10,
      total: 0,
      searchTitle: '',
    };
  },
  created() {
    //this.getNews();
    this.search();
  },
  methods: {
    // 在你的组件中，修改getNews()方法
    async getNews() {
      let loadingInstance;
      try {
        // 显示加载中
        loadingInstance = this.toggleLoading(true);
        const res = await request.get("/news");
        if (res.length > 0) {
          this.newsList = res;
          const res2 = await request.post("/news", JSON.stringify(this.newsList), {
            headers: { "Content-Type": "application/json" },
          });
          await this.search();
        }
        // 关闭加载中
        this.toggleLoading(false);
        this.$message.success("加载成功");
      } catch (error) {
        console.error("Error in getNews():", error);
        this.$message.error("可能是网络问题，请检查网络连接是否正常");
        // 如果出现错误，也要关闭加载中
        this.toggleLoading(false);
      } finally {
        // 确保在任何情况下都关闭加载中
        loadingInstance && loadingInstance.close();
      }
    },

    async search() {
      try {
        const res = await request.get("/news/page", {
          params: {
            pageNum: this.page,
            pageSize: this.pageSize,
            title: this.searchTitle,
          },
        });
        //console.log(res);
        if (res.code == '200') {
          this.newsList = res.data.records;
          //console.log(this.newsList);
          this.total = res.data.total;
          //console.log(this.total);
        }
      } catch (error) {
        console.error('Error in search():', error);
      }
    },
    pageChange(page) {
      this.page = page;
      this.search();
    },
    delTopTen() {
      this.dealConfirmDelete('确定删除前十条记录吗？', () => {
        this.request.delete(`/news/delTopTen`)
            .then(res => {
              if (res) {
                this.$message.success('成功删除前十条记录')
                this.search()
              } else {
                this.$message.error('删除失败')
              }
            })
      })
    },
    delAll() {
      this.dealConfirmDelete('确定删除所有记录吗？', () => {
        this.request.delete(`/news/delAll`)
            .then(res => {
              if (res) {
                this.$message.success('成功删除所有记录')
                this.search()
              } else {
                this.$message.error('删除失败')
              }
            })
      })
    },
  }
};
</script>
