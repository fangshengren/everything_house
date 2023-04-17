<template>
  <div id="home">
    <div>
      <el-input
          placeholder="搜索新闻标题"
          v-model="searchTitle"
          @input="load"
      ></el-input>
      <el-table :data="newsList" style="width: 100%">
        <el-table-column prop="title" label="新闻标题"></el-table-column>
        <el-table-column prop="content" label="新闻摘要">
          <template slot-scope="scope">
            <div>{{ scope.row.content.length > 100 ? scope.row.content.substring(0, 100) + '...' : scope.row.content }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="发布时间"></el-table-column>
      </el-table>
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :page-sizes="[5, 10, 20, 50]"
          :total="total"
      ></el-pagination>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "News",
  created() {
    this.load();
  },
  data() {
    return {
      newsList: [],
      currentPage: 1,
      pageSize: 10,
      total: 1,
      searchTitle: "",
    };
  },
  methods: {
    load() {
      this.request.get("/news/page", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          title: this.searchTitle,
        },
      }).then(res => {
        this.newsList = res.data.records;
        this.total = res.data.total;
      })
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.load();
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.load();
    },
  },
};
</script>
