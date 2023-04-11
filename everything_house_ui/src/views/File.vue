<template>
  <div id="home">
    <div style="margin: 10px 0px">
      <el-input style="width: 200px;margin-right:10px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>
    <div style="margin: 10px 0px">
      <el-upload :action="uploadUrl" :show-file-list="false" :on-success="handleFileUploadSuccess"
                 :on-error="handleFileUploadError"
                 style="display: inline-block">
        <el-button type="primary" class="ml-5" style="margin-right:10px">上传文件 <i class="el-icon-top"></i></el-button>
      </el-upload>
      <el-button type="danger" slot="reference" @click="handledelBatch()">批量删除 <i class="el-icon-remove-outline"></i></el-button>
    </div>
    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="文件名称"></el-table-column>
      <el-table-column prop="type" label="文件类型"></el-table-column>
      <el-table-column prop="size" label="文件大小(kb)"></el-table-column>
      <el-table-column label="下载">
        <template slot-scope="scope">
          <el-button type="primary" @click="download(scope.row.url)">下载</el-button>
        </template>
      </el-table-column>
      <el-table-column label="启用">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enable" active-color="#13ce66" inactive-color="#ccc" @change="changeEnable(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作"  width="200" align="center">
        <template slot-scope="scope">
            <el-button type="danger" slot="reference" @click="handleConfirmDelete(scope.row)">删除 <i class="el-icon-remove-outline"></i></el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

  </div>
</template>

<script>
export default {
  name: "File",
  data() {
    return {
      uploadUrl: "http://localhost:8084/file/upload",
      tableData: [],
      name: '',
      multipleSelection: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      currentFileSize: 0,
    }
  },
  created() {
    this.load()
  },
  methods: {
    upload(){
      this.request.post("/file/upload").then(res => {
        if (res.code === '200') {
          this.$message.success("操作成功")
        }
      })
    },
    load() {
      this.request.get("/file/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {

        this.tableData = res.data.records
        this.total = res.data.total

      })
    },
    changeEnable(row) {
      this.request.post("/file/update", row).then(res => {
        if (res.code === '200') {
          this.$message.success("操作成功")
        }
      })
    },
    handleConfirmDelete(row) {
      this.$confirm(`确定删除编号为 ${row.id} 的记录吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.del(row.id)
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    del(id) {
      this.request.delete("/file/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    handledelBatch() {
      if(this.multipleSelection==null){
        this.$message({
          type: 'warning',
          message: '请你选择要删除的选项!'
        })
        return;
      }
      let ids = this.multipleSelection.map(v => v.id)
      if(ids.length==0){
        this.$message({
          type: 'warning',
          message: '请你选择要删除的选项!'
        })
      }else{
        this.$confirm(`确定删除编号为 ${ids} 的记录吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.delBatch(ids)
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
      }
    },
    delBatch(ids) {
      this.request.post("/file/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    reset() {
      this.name = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    //处理文件上传报错的一些方法
    handleFileUploadSuccess(res) {
      console.log(res)
      this.load()
    },
    handleFileUploadError(err){
      console.log(err);//打印错误对象以获取更多详细信息

      if(err&&err.type==='error'&&err.loaded===0&&err.total===0){
        this.$message.error('文件大小超过限制（5MB）。请上传较小的文件。');
      }else if(err&&err.response){
        let response=err.response;
        if(response.status===400&& typeof response.data==='string'){
          this.$message.error(response.data);
        }else{
          this.$message.error('上传失败');
        }
      }else{
        this.$message.error('发生未知错误');
      }
    },
    download(url) {
      window.open(url)
    }
  }
}
</script>

<style scoped>

</style>