package com.house.everything_house_backend.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.excel.EasyExcel;
import com.house.everything_house_backend.common.Constants;
import com.house.everything_house_backend.common.Result;
import com.house.everything_house_backend.config.AuthAccess;
import com.house.everything_house_backend.controller.dto.UserDTO;
import com.house.everything_house_backend.entities.User;
import com.house.everything_house_backend.exception.ServiceException;
import com.house.everything_house_backend.listener.UserExcelListener;
import com.house.everything_house_backend.mapper.UserMapper;
import com.house.everything_house_backend.service.ISysUserService;
import com.house.everything_house_backend.utils.MD5Util;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ISysUserService sysUserService;
    @Value("${export.exportUrl}")
    private String exportUrl;
    //分页查询
    //接口路径user/page?pageNum=1&pageSize=10
    //RequestParam接受前台传过来的第几页，每页显示数
    //使用mybtis-plus实现根据ID查找记录
    @GetMapping("/page2")
    public Map<String,Object> findPageByMultipleCondition(@RequestParam(defaultValue = "1") Integer pageNum,
                                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                                          User user){
        Map<String, Object> params = new HashMap<>();
        params.put("id", user.getId());
        params.put("username", user.getUsername());
        params.put("nickname", user.getNickname());
        params.put("phone", user.getPhone());
        params.put("email", user.getEmail());
        params.put("address", user.getAddress());
        params.put("avatar", user.getAvatar());
        params.put("role", user.getRole());
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
    @GetMapping("/UserNumber")
    public int selectNumberOfUser(){
        return userMapper.selectNumberOfUser();
    }
    @GetMapping("/AdminNumber")
    public int selectNumberOfAdmin(){
        return userMapper.selectNumberOfAdmin();
    }
    @GetMapping("/{id}")
    public User selectElementById(@PathVariable int id){
        return userMapper.selectById(id);
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
    public int update(@RequestBody User user){
        return sysUserService.updateUser(user);
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
        ExcelWriter writer= ExcelUtil.getWriter(exportUrl);
        //这里的参数也可以设置绝对路径，本项目实现网页的下载，省略下载路径
        //ExcelWriter writer= ExcelUtil.getWriter(true);
        writer.addHeaderAlias("username","用户名");//用别名的方法，实现Excel文件的标题是中文的
        writer.addHeaderAlias("password","密码");
        writer.addHeaderAlias("nickname","昵称");
        writer.addHeaderAlias("email","邮箱");
        writer.addHeaderAlias("phone","电话");
        writer.addHeaderAlias("address","地址");
        writer.addHeaderAlias("creat_time","创建时间");
        writer.addHeaderAlias("avatar","化身");
        writer.addHeaderAlias("role","角色");
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

    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO){
        String username=userDTO.getUsername();//先对userDTO进行是否为空的校验
        String password=userDTO.getPassword();
        //调用hutool工具中的StrUtil函数实现用户名和密码是否为空的判断
        if(StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        UserDTO dto=sysUserService.login(userDTO);
        return Result.success(dto);
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO){
        String username=userDTO.getUsername();
        String password=userDTO.getPassword();
        int x=sysUserService.selectNumberOfUserName(username);
        System.out.println(x);
        if(x>0){
            return Result.error("500","用户名已存在，注册失败");
        }
        else if(x==0){
            if(password.length()<8||password.length()>20){
                return Result.error("500","密码长度不符合要求，请更改您的密码");
            }
            if(username.length()<5||username.length()>20){
                return Result.error("500","用户名长度不符合要求，请更改您的用户名");
            }
            String regex1="[\\s]";
            String regex2="[\u4e00-\u9fa5]";
            Pattern pattern1 = Pattern.compile(regex1);
            Pattern pattern2 = Pattern.compile(regex2);
            if(pattern1.matcher(username).find()){
                return Result.error("500","用户名中含有空格，请更改您的用户名");
            }
            if(pattern1.matcher(password).find()){
                return Result.error("500","密码中含有空格，请更改您的密码");
            }
            if(pattern2.matcher(password).find()){
                return Result.error("500","密码中含有中文，请更改您的密码");
            }
            String salt = MD5Util.generateSalt(8);
            String hashedPassword = MD5Util.getMD5(userDTO.getPassword()+salt);
            User user = new User();
            user.setSalt(salt);
            user.setUsername(userDTO.getUsername());
            user.setPassword(hashedPassword);
            user.setRole("ROLE_USER");
            sysUserService.insert(user);
            return Result.success("注册成功");
        }
        else{
            return Result.error();
        }
    }

    @PostMapping("/sendEmail/{email}")
    public Result sendEmail(@PathVariable String email){
        if (StrUtil.isBlank(email)){
            throw new ServiceException(Constants.CODE_400,"参数错误");
        }
        try{
            sysUserService.sendCode(email,"您的百宝箱验证码消息");
        }catch (ServiceException e){
            return Result.error(Constants.CODE_400,"邮箱不存在");
        }
        return Result.success(email);
    }

    @AuthAccess
    @PostMapping("/sendForgetPasswordEmail/{email}")
    public Result sendForgetPasswordEmail(@PathVariable String email){
        if (StrUtil.isBlank(email)){
            throw new ServiceException(Constants.CODE_400,"参数错误");
        }
        try{
            sysUserService.sendCode(email,"您的百宝箱找回密码验证码消息");
        }catch (ServiceException e){
            return Result.error(Constants.CODE_400,"邮箱不存在");
        }
        return Result.success(email);
    }

    @PostMapping("/sendUnbindEmail/{email}")
    public Result sendUnbindEmail(@PathVariable String email){
        if (StrUtil.isBlank(email)){
            throw new ServiceException(Constants.CODE_400,"参数错误");
        }
        try{
            sysUserService.sendCode(email,"您的百宝箱解绑邮箱验证码消息");
        }catch (ServiceException e){
            return Result.error(Constants.CODE_400,"邮箱不存在");
        }
        return Result.success(email);
    }

    @PostMapping("/bindEmail")
    public Result bindEmail(@RequestBody UserDTO userDTO){
        String email=userDTO.getEmail();//先对userDTO进行是否为空的校验
        String code=userDTO.getCode();
        //调用hutool工具中的StrUtil函数实现用户名和密码是否为空的判断
        if(StrUtil.isBlank(email) || StrUtil.isBlank(code)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        UserDTO dto=sysUserService.bindEmail(userDTO);
        return Result.success(dto);
    }

    @PostMapping("/unbindEmail")
    public Result unbindEmail(@RequestBody UserDTO userDTO){
        String email=userDTO.getEmail();//先对userDTO进行是否为空的校验
        String code=userDTO.getCode();
        //调用hutool工具中的StrUtil函数实现用户名和密码是否为空的判断
        if(StrUtil.isBlank(email) || StrUtil.isBlank(code)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        UserDTO dto=sysUserService.unbindEmail(userDTO);
        return Result.success(dto);
    }

    @PostMapping("/loginByEmail")
    public Result loginByEmail(@RequestBody UserDTO userDTO){
        String email=userDTO.getEmail();//先对userDTO进行是否为空的校验
        String code=userDTO.getCode();
        //调用hutool工具中的StrUtil函数实现用户名和密码是否为空的判断
        if(StrUtil.isBlank(email) || StrUtil.isBlank(code)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        UserDTO dto=sysUserService.loginByEmail(userDTO);
        return Result.success(dto);
    }

    @AuthAccess
    @PostMapping("/checkChangePassword")
    public Result checkChangePassword(@RequestBody UserDTO userDTO){
        String email=userDTO.getEmail();//先对userDTO进行是否为空的校验
        String code=userDTO.getCode();
        String password=userDTO.getPassword();
        //调用hutool工具中的StrUtil函数实现用户名和密码是否为空的判断
        if(StrUtil.isBlank(email) || StrUtil.isBlank(code)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        boolean result=sysUserService.emailVerificationSuccess(userDTO);
        if (!result){
            return Result.error(Constants.CODE_400,"验证码错误");
        }
        if(password.length()<8||password.length()>20){
            return Result.error("500","密码长度不符合要求，请更改您的密码");
        }
        String regex1="[\\s]";
        String regex2="[\u4e00-\u9fa5]";
        Pattern pattern1 = Pattern.compile(regex1);
        Pattern pattern2 = Pattern.compile(regex2);
        if (pattern1.matcher(password).find()){
            return Result.error("500","密码中含有空格，请更改您的密码");
        }
        if (pattern2.matcher(password).find()){
            return Result.error("500","密码中含有中文，请更改您的密码");
        }
        User user = sysUserService.getByEmail(email);
        String salt = MD5Util.generateSalt(8);
        String hashedPassword = MD5Util.getMD5(password+salt);
        user.setPassword(hashedPassword);
        user.setSalt(salt);
        sysUserService.updateUser(user);
        return Result.success("密码修改成功");
    }

}
