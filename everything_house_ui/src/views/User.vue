<template>
    <div>
        <el-main>
          <div style="padding:10px">
            <el-input style="width:15%" suffix-icon="el-icon-search" placeholder="请输入名称搜索" v-model="username"></el-input>
            <el-input style="width:15%" suffix-icon="el-icon-email" placeholder="请输入邮箱搜索" v-model="email"></el-input>
            <el-input style="width:15%" suffix-icon="el-icon-email" placeholder="请输入电话搜索" v-model="phone"></el-input>
            <el-input style="width:15%" suffix-icon="el-icon-email" placeholder="请输入昵称搜索" v-model="nickname"></el-input>
            <el-input style="width:15%" suffix-icon="el-icon-position" placeholder="请输入地址搜索" v-model="address"></el-input>
            <el-button style="margin-left:5px" type="primary" @click="load">搜索</el-button>
            <el-button style="margin-left:5px" type="warning" @click="reset">重置</el-button>
          </div>

          <div style="margin:10px">
            <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus"></i></el-button>
            <el-button type="danger" @click="handledelBatch()">批量删除<i class="el-icon-remove"></i></el-button>
            <el-upload
                :action="baseURL()+'/user/importUsers'" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
              <el-button type="primary" style="margin:10px">导入<i class="el-icon-bottom"></i></el-button>
            </el-upload>
            <el-button type="primary" @click="exp">导出<i class="el-icon-top"></i></el-button>
          </div>

          <el-table
              :data="tableData"
              style="width: 100%"
              @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column
                prop="id"
                label="ID"
                width="80">
            </el-table-column>
            <el-table-column
                prop="username"
                label="用户姓名"
                width="80">
            </el-table-column>
            <el-table-column
                prop="email"
                label="邮箱"
                width="120">
            </el-table-column>
            <el-table-column
                prop="phone"
                label="电话">
            </el-table-column>
            <el-table-column
                prop="nickname"
                label="昵称">
            </el-table-column>
            <el-table-column
                prop="role"
                label="角色">
            </el-table-column>
            <el-table-column
                prop="address"
                label="地址">
            </el-table-column>
            <el-table-column fixed="right" label="操作" width="240">
              <template slot-scope="scope">
                <el-button type="success" size="small" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
                <el-button type="danger" size="small"  icon="el-icon-delete" @click="handleConfirmDelete(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div style="padding:10px">
            <el-pagination
                :current-page="PageNum"
                :page-sizes="PageSizeOption"
                :page-size="PageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange">
            </el-pagination>
          </div>
          <!--弹窗-->
          <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
            <el-form ref="form" label-width="80px" :model="form" :rules="rules">
              <el-form-item label="用户名" prop="username">
                <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
              </el-form-item>
              <el-form-item label="角色">
                <el-select clearable v-model="form.role" placeholder="请选择角色" style="width:100%;">
                  <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.flag">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="昵称" prop="nickname">
                <el-input v-model="form.nickname" placeholder="请输入昵称"></el-input>
              </el-form-item>
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
              </el-form-item>
              <el-form-item label="电话" prop="phone">
                <el-input v-model="form.phone" placeholder="请输入电话"></el-input>
              </el-form-item>
              <el-form-item label="地址" prop="address">
                <el-input v-model="form.address" placeholder="请输入地址"></el-input>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button @click="cancelSubmit">取 消</el-button>
              <el-button type="primary" @click="submitForm()">确 定</el-button>
            </div>
          </el-dialog>
        </el-main>
  </div>
</template>

<script>
import {baseURL} from "@/utils/request";

export default {

  name: "User",
  data(){
    return {
      roles: null,
      multipleSelection:null,
      tableData:[],
      total: 0,
      PageNum: null,
      PageSize: null,
      PageSizeOption:[ 5, 10, 20, 30, 40],
      username:"",
      email:"",
      address:"",
      phone:"",
      nickname:"",
      dialogFormVisible:false,
      rules: {
        username: [
          { required: true, message: "用户名不能为空", trigger: "blur" }
        ],
        email: [
          { required: true, message: "邮箱不能为空", trigger: "blur" }
        ],
        phone: [
          { required: true, message: "电话不能为空", trigger: "blur" }
        ],
        address: [
          { required: true, message: "地址不能为空", trigger: "blur" }
        ]
      },
    //表单
      form:{},
    }
  },
  created(){
    //请求分页查询数据
    const urlParams = new URLSearchParams(window.location.search);
    this.PageSize = Number(urlParams.get("pageSize")) || 10;
    this.PageNum = Number(urlParams.get("pageNum")) || 1;
    this.load();
  },
  methods: {
    baseURL() {
      return baseURL
    },
    exp(){
      window.open(baseURL+"/user/export");
    },
    handleExcelImportSuccess(){ //实现导入
      this.$message.success("导入成功");
      this.load();
    },
    cancelSubmit(){
      this.dialogFormVisible = false;
      this.$message({
        type: 'info',
        message: '你取消了订单的提交!'
      })
    },
    handleAdd(){
      this.dialogFormVisible = true;
      this.form={};//如果之前有填过值，可以置空
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
    handleConfirmDelete(row) {
      this.$confirm(`确定删除编号为 ${row.id} 的记录吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.handleDelete(row.id)
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    delBatch(ids) {
      this.request.delete(`/user/del/batch/`+ids)
          .then(res => {
            if (res) {
              this.$message.success('批量删除成功')
              this.load()
            } else {
              this.$message.error('批量删除失败')
            }
          })
    },
    handleSelectionChange(val){
      console.log(val);
      this.multipleSelection =val;
    },
    handleDelete(id){
      this.request.delete("/user/"+id+"").then(res=>{
        if(res){
          this.$message.success("删除成功");
          this.load();
        }else{
          this.$message.error("删除失败");
        }
      })
    },
    submitForm(){
      this.$refs["form"].validate(valid=>{
        if (valid) {
          if(this.form.id!=null){
            this.update();
          }
          else{
            this.insert()
          }
        }
      })
    },
    insert(){
      this.form.password = "Aa123456";
      this.request.post("/user",this.form).then(res=>{
        if(res){
          this.$message.success("保存成功");
          this.dialogFormVisible=false;
          this.load();
        }else{
          this.$message.error("保存失败");
        }
      })
    },

    update(){
      this.request.put("/user",this.form).then(res=>{
        if(res>0){
          this.$message.success("更新成功");
          this.dialogFormVisible=false;
          this.load();
        }else{
          this.$message.error("更新失败");
        }
      })
    },

    handleEdit(row){
      console.log(row);
      this.form=row;//把当前行的数据赋值给form
      this.dialogFormVisible=true;
    },
    handleSizeChange(val) {/*传递过来当前是第几页*/
      console.log(`每页 ${val} 条`);
      this.PageSize=val;  //获取当前每页显示条数
      this.load();
    },
    handleCurrentChange(val) {/*传递过来当前是第几页*/
      console.log(`当前页: ${val}`);
      this.PageNum=val;   //获取当前第几页
      this.load();
    },
    reset(){
      this.username="";
      this.email="";
      this.phone="";
      this.nickname="";
      this.address="";
      this.load();
    },
    //将请求数据封装为一个方法
    load() {
      //请求分页查询数据
      //fetch(baseURL+"/user/page?pageNum="+this.pageNum+"&pageSize="+this.pageSize+"").then(res=>res.json()).then(res=>{
      //使用axios封装的request
      this.request.get("/user/page2",{
        params:{
          pageNum: this.PageNum,
          pageSize: this.PageSize,
          username: this.username,
          nickname:this.nickname,
          phone:this.phone,
          email:this.email,
          address:this.address
        }
      }).then(res=>{
        console.log(res)
        this.tableData=res.data
        this.total=res.total
      }),
      this.request.get("/role").then(res=>{
      this.roles=res.data
      })
    },
  },
}

</script>
