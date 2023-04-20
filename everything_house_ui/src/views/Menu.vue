<template>
  <div id="website">
    <div style="padding:10px" class="input-container">
      <el-input style="width:250px" suffix-icon="el-icon-search" placeholder="请输入名称搜索" v-model="name"></el-input>
      <el-button style="margin-left:5px" type="primary" @click="load">搜索</el-button>
      <el-button style="margin-left:5px" type="warning" @click="reset">重置</el-button>
    </div>
    <div style="margin:10px" class="button-container">
      <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus"></i></el-button>
      <el-button type="danger" @click="delBatch">批量删除<i class="el-icon-remove"></i></el-button>
    </div>
    <el-table :data="tableData"  row-key="id"
              border default-expand-all @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID " width="80">
      </el-table-column>
      <el-table-column prop="name" label="名称 " >
      </el-table-column>
      <el-table-column prop="path" label="路径 " >
      </el-table-column>
      <el-table-column prop="pagePath" label="页面路径 " >
      </el-table-column>
      <el-table-column  label="图标 " align="center">
        <template slot-scope="scope">
          <i style="font-size:18px"  :class="scope.row.icon"/>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述" >
      </el-table-column>
      <el-table-column fixed="right" width="300px" label="操作" >
        <template slot-scope="scope">
          <el-button type="primary" size="small" icon="el-icon-plus" @click="childmenuAdd(scope.row.id)" v-if="!scope.row.pid && !scope.row.path">新增子菜单</el-button>
          <el-button type="success" size="small" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
          <el-popconfirm style="margin-left:5px"
                         confirm-button-text='确定'
                         cancel-button-text='再想想'
                         icon="el-icon-info"
                         icon-color="red"
                         title="您确定删除吗？"
                         @confirm="handleDelete(scope.row.id)"
          >
            <el-button type="danger" size="small" slot="reference" icon="el-icon-delete" >删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog title="菜单信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small">
        <el-form-item label="名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="路径">
          <el-input v-model="form.path" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="页面路径">
          <el-input v-model="form.pagePath" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="图标">
          <el-select clearable v-model="form.icon" placeholder="请选择" style="width:100%;">
            <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
              <i :class="item.value"/>{{item.name}}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name:"Menu",
  data(){
    return{
      tableData:[],
      name:"",
      dialogFormVisible:false,
      form:{},
      multipleSelection:[],
      options:[]
    }
  },
  created(){
    this.load();
  },
  methods:{
    //搜索重置
    reset(){
      this.name="";
      this.load();
    },
    //打开新增菜单对话框，同时获取图标数据
    handleAdd(){
      this.dialogFormVisible = true;
      this.form={};//如果之前有填过值，可以置空
      this.request.get("/menu/icons").then(res=>{ //不管是新增还是编辑都直接获取图标数据即可，不需要与当前id对应
        console.log(res);
        this.options=res.data;
      })
    },
    //实现新增菜单
    save(){
      this.request.post("/menu",this.form).then(res=>{
        if(res.code=='200'){
          this.$message.success("保存成功");
          this.dialogFormVisible=false;
          this.load();
        }else{
          this.$message.error("保存失败");
        }
      })
    },
    //编辑菜单
    handleEdit(row){  //编辑
      this.form=row;//把当前行的数据赋值给form
      this.dialogFormVisible=true;
      this.request.get("/menu/icons").then(res=>{ //不管是新增还是编辑都直接获取图标数据即可，不需要与当前id对应
        console.log(res);
        this.options=res.data;
      })
    },
    //多行选择
    handleSelectionChange(val){
      console.log(val);
      this.multipleSelection =val;
    },
    //批量删除
    delBatch(){
      let ids=this.multipleSelection.map(v=>v.id);//map这个方法可以实现将multipleSelection中的对象扁平化处理。
      this.request.post("/menu/del/batch",ids).then(res=>{
        if(res.code=='200'){
          this.$message.success("批量删除成功");
          this.load();
        }else{
          this.$message.error("批量删除失败");
        }
      })
    },
    //根据选择行的id删除
    handleDelete(id){
      this.request.delete("/menu/"+id+"").then(res=>{
        if(res.code=='200'){
          this.$message.success("删除成功");
          this.load();
        }else{
          this.$message.error("删除失败");
        }
      })
    },
    //将请求数据封装为一个方法
    load() {
      //使用axios封装的request，获取所有菜单数据
      this.request.get("/menu",{
        params:{
          name:this.name
        }
      }).then(res=>{
        console.log(res);
        this.tableData=res.data;
      })
    },
    //新增子菜单，打开新增菜单对话框,把父菜单的id传递到form中
    childmenuAdd(pid){
      this.dialogFormVisible = true;
      this.form={};//如果之前有填过值，可以置空
      if(pid){
        this.form.pid=pid;//传递父菜单的id
      }
      this.request.get("/menu/icons").then(res=>{ //不管是新增还是编辑都直接获取图标数据即可，不需要与当前id对应
        console.log(res);
        this.options=res.data;
      })
    }

  }
}
</script>
