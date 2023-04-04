package com.house.everything_house_backend.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.excel.EasyExcel;
import com.house.everything_house_backend.entities.User;
import com.house.everything_house_backend.listener.UserExcelListener;
import com.house.everything_house_backend.mapper.UserMapper;
import com.house.everything_house_backend.service.ISysUserService;
import com.house.everything_house_backend.service.iml.SysUserService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ISysUserService sysUserService;
    //分页查询
    //接口路径user/page?pageNum=1&pageSize=10
    //RequestParam接受前台传过来的第几页，每页显示数
    //使用mybtis-plus实现根据ID查找记录
    @GetMapping("/page2")
    public Map<String,Object> findPageByMultipleCondition(@RequestParam(defaultValue = "1") Integer pageNum,
                                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                                          User user){
        Map<String, Object> params = new HashMap<>();
        params.put("username", user.getUsername());
        params.put("nickname", user.getNickname());
        params.put("phone", user.getPhone());
        params.put("email", user.getEmail());
        params.put("address", user.getAddress());
        params.put("pageNumber", (pageNum - 1) * pageSize);
        params.put("pageSize", pageSize);
        List<User> data=sysUserService.selectByMultipleCondition(params);
        Integer total=sysUserService.selectTotalByMultipleCondition(params);
        Map<String,Object> res=new HashMap<>();
        res.put("data",data);
        res.put("total",total);
        return res;
    }

    @GetMapping("/")
    public List<User> index(){
        return userMapper.findAll();
    }
    @PostMapping
    //这里做了一个单纯的添加的示例，使用的是UserService中的save方法,实现添加和更新
    public Integer insert(@RequestBody User user){
        return sysUserService.insert(user);
    }
    //使用mybtis-plus实现批量删除
    @DeleteMapping("/del/batch/{ids}")
    public boolean deleteBatch(@PathVariable Long[] ids){
        return sysUserService.removeByIds(ids);
    }
    @PutMapping
    //这里做了一个单纯的添加的示例，使用的是UserService中的update方法,实现添加和更新
    public boolean save(@RequestBody User user){
        return sysUserService.save(user);
    }
    @DeleteMapping("/{id}")
    public Integer deleteById(@PathVariable Integer id){
        return  sysUserService.deleteById(id);
    }

    /*导出接口*/
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
        //从数据库查询出所有数据,如果需要获取其他的数据，就调用其他的方法
        List<User> list=userMapper.findAll();
        //通过工具类创建write 写出磁盘路径
        ExcelWriter writer= ExcelUtil.getWriter("D:/用户信息.xlsx");
        //这里的参数也可以设置绝对路径，本项目实现网页的下载，省略下载路径
        //ExcelWriter writer= ExcelUtil.getWriter(true);
        writer.addHeaderAlias("username","用户名");//用别名的方法，实现Excel文件的标题是中文的
        writer.addHeaderAlias("password","密码");
        writer.addHeaderAlias("nickname","昵称");
        writer.addHeaderAlias("email","邮箱");
        writer.addHeaderAlias("phone","电话");
        writer.addHeaderAlias("address","地址");
        writer.addHeaderAlias("created_time","创建时间");
        //一次性写出list内部的对象到excel，强制输出标题
        writer.write(list,true);
        String filename= URLEncoder.encode("用户信息","UTF-8");
        //设置浏览器弹出响应格式,输出xlsx格式，官网也可以查看输出xls格式的方法
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+ filename+".xlsx");
        ServletOutputStream out=response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    /*excel导入*/
    @PostMapping("/importUsers")
    public String importUsers(@RequestParam("file") MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            EasyExcel.read(inputStream, User.class, new UserExcelListener(sysUserService)).sheet().doRead();
            return "导入成功";
        } catch (IOException e) {
            e.printStackTrace();
            return "导入失败";
        }
    }
}
