package com.house.everything_house_backend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.house.everything_house_backend.common.Constants;
import com.house.everything_house_backend.entities.Menu;
import com.house.everything_house_backend.controller.dto.UserDTO;
import com.house.everything_house_backend.entities.User;
import com.house.everything_house_backend.exception.ServiceException;
import com.house.everything_house_backend.mapper.RoleMapper;
import com.house.everything_house_backend.mapper.RoleMenuMapper;
import com.house.everything_house_backend.mapper.UserMapper;
import com.house.everything_house_backend.service.ISysUserService;
import com.house.everything_house_backend.utils.MD5Util;
import com.house.everything_house_backend.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
@Primary
public class SysUserServiceImpl extends ServiceImpl<UserMapper, User> implements ISysUserService {
    @Autowired
    private UserMapper userMapper;
    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private MenuServiceImpl menuServiceImpl;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    public static final String EMAIL_CODE_KEY="email-code:";
    @Override
    public List<User> selectPage(Integer pageNum, Integer pageSize) {
        return userMapper.selectPage(pageNum,pageSize);
    }
    @Override
    public List<User> selectPageByUserName(Integer pageNum, Integer pageSize, String userName) {
        return userMapper.selectPageByUserName(pageNum,pageSize,userName);
    }
    @Override
    public int selectNumberOfUser(){
        return userMapper.selectNumberOfUser();
    }
    @Override
    public int selectNumberOfAdmin(){
        return userMapper.selectNumberOfAdmin();
    }
    @Override
    public int insert(User user){
        return userMapper.insertUser(user);
    }
    @Override
    public int selectNumberOfUserName(String queryUserName){
        return userMapper.selectAllByUsername(queryUserName);
    }
    @Override
    public boolean importUsersFromXlsx(List<User> userList) {
        return saveBatch(userList);
    }

    @Override
    public List<User> selectByMultipleCondition(Map<String, Object> params){
        return userMapper.selectByMultipleCondition(params);
    }
    @Override
    public int selectTotalByMultipleCondition(Map<String, Object> params){
        return userMapper.selectTotalByMultipleCondition(params);
    }

    @Override
    public Integer deleteById(Integer id) {
        return userMapper.deleteById(id);
    }
    @Override
    public boolean removeByIds(Long[] ids){
        return userMapper.removeByIds(ids);
    }

    @Override
    public int updateUser(User user) {
//        String salt = MD5Util.generateSalt(8);
//        String hashedPassword = MD5Util.getMD5(user.getPassword()+salt);
//        user.setPassword(hashedPassword);
        return userMapper.updateById(user);
    }

    public UserDTO login(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        User one;
        try{
            one=getOne(queryWrapper);
        }catch (Exception e){
            throw new ServiceException(Constants.CODE_500,"系统错误");//这里假设查询了多于1条记录，就让他报系统错误
        }
        if(one!=null){  //以下是登录判断业务
            // 使用用户输入的密码和数据库中存储的盐值进行哈希计算
            String saltedHashedPassword = MD5Util.getMD5(userDTO.getPassword() + one.getSalt());
            //System.out.println(saltedHashedPassword);System.out.println(one.getPassword());
            // 将计算出的哈希值与数据库中存储的哈希密码进行比较
            if (!saltedHashedPassword.equals(one.getPassword())) {
                throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
            }
            return returnLoginUser(one,userDTO);//返回登录类userDTO
        }else {
            throw new ServiceException(Constants.CODE_600,"用户名或密码错误");
        }
    }

    /**
     * 获取当前用户的菜单列表

     */
    private List<Menu> getRoleMenus(String roleFlag){
        //根据角色标识获取角色Id
        Integer roleId=roleMapper.selectByflag(roleFlag);
        //当前角色Id的所有菜单id集合
        List<Integer> menuIds=roleMenuMapper.selectByRoleId(roleId);
        //查出系统所有菜单
        List<Menu> menus= menuServiceImpl.findMenus("");
        //筛选当前用户菜单
        List<Menu> roleMenus=new ArrayList<>();
        for(Menu menu:menus){
            if(menuIds.contains(menu.getId())){
                roleMenus.add(menu);
            }
            List<Menu> children=menu.getChildren();
            //removeIf移除children里面不在menuIds集合中的元素
            children.removeIf(child->!menuIds.contains(child.getId()));
        }
        return roleMenus;
    }
//    邮箱相关
    @Autowired
    public JavaMailSender javaMailSender;

    @Override
    public void sendCode(String email,String subject){
        String code = getLoginEmailCode(4);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("3164113754@qq.com");
        mailMessage.setSubject(subject);
        mailMessage.setSentDate(new Date());
        mailMessage.setText(code+"           千万不要把验证码发给其他人！！！！！！验证码有效期为5分钟，请以最新的验证码为准！！！");
        mailMessage.setTo(email);
        String key = EMAIL_CODE_KEY+email;
        javaMailSender.send(mailMessage);
        System.out.println(code);
        if(redisTemplate.hasKey(key)){
            redisTemplate.delete(key);
            redisTemplate.opsForValue().set(key,code,300,TimeUnit.SECONDS);
        }else{
            redisTemplate.opsForValue().set(key,code,300,TimeUnit.SECONDS);
        }
    }

    @Override
    public UserDTO loginByEmail(UserDTO userDTO){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",userDTO.getEmail());
        User one;
        try{
            one=getOne(queryWrapper);
        }catch (Exception e){
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        if(one!=null){
            String realCode=(String) redisTemplate.opsForValue().get(EMAIL_CODE_KEY+userDTO.getEmail());
            String code=userDTO.getCode();
            if(!Objects.equals(realCode, code)){
                throw new ServiceException(Constants.CODE_600,"验证码错误");
            }
            return returnLoginUser(one,userDTO);//返回登录类userDTO
        }else {
            // 创建新用户
            User newUser = new User();
            newUser.setEmail(userDTO.getEmail());
            newUser.setUsername("用户"+userDTO.getEmail()); // 或者其他默认值
            newUser.setRole("ROLE_USER"); // 或者其他默认值
            newUser.setAddress("默认地址"); // 或者其他默认值
            newUser.setCreatedTime(new Date());
            String password=generateRandomPassword();

            String salt = MD5Util.generateSalt(8);
            String hashedPassword = MD5Util.getMD5(password+salt);
            newUser.setSalt(salt);
            newUser.setPassword(hashedPassword);

            // 将新用户保存到数据库
            insert(newUser);
            queryWrapper.eq("email",userDTO.getEmail());
            User one2;
            try{
                one2=getOne(queryWrapper);
            }catch (Exception e){
                throw new ServiceException(Constants.CODE_500,"系统错误");
            }
            sendUserDetailEmail("用户"+userDTO.getEmail(),password,userDTO.getEmail());
            return returnLoginUser(one2,userDTO);//返回登录类userDTO
        }
    }

    private UserDTO returnLoginUser(User one,UserDTO userDTO){
        BeanUtil.copyProperties(one,userDTO,true);
        //设置token
        String token= TokenUtils.genToken(one.getId().toString(),one.getPassword().toString());
        userDTO.setToken(token);
        String avatar=one.getAvatar();
        userDTO.setAvatar(avatar);
        int id =one.getId();
        userDTO.setId(id);
        String role=one.getRole();//查询出用户的角色标识，比如ROLE_ADMIN
        //设置用户的菜单列表
        List<Menu> roleMenus=getRoleMenus(role);
        userDTO.setMenus(roleMenus);
        redisTemplate.delete(EMAIL_CODE_KEY+userDTO.getEmail());
        return userDTO;//返回登录类userDTO
    }

    @Override
    public void sendUserDetailEmail(String username,String password,String email){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("3164113754@qq.com");
        mailMessage.setSubject("您的百宝箱初始账号密码信息");
        mailMessage.setSentDate(new Date());
        mailMessage.setText("初始账号:"+username+"             初始密码:"+password);
        mailMessage.setTo(email);
        String key = EMAIL_CODE_KEY+email;
        javaMailSender.send(mailMessage);
    }

    @Override
    public UserDTO bindEmail(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",userDTO.getEmail());
        User one;
        try{
            one=getOne(queryWrapper);
        }catch (Exception e){
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        if(one!=null){
            throw new ServiceException(Constants.CODE_600,"该邮箱已被绑定");
        }else {
            String realCode=(String) redisTemplate.opsForValue().get(EMAIL_CODE_KEY+userDTO.getEmail());
            String code=userDTO.getCode();
            if(!Objects.equals(realCode, code)){
                throw new ServiceException(Constants.CODE_600,"验证码错误");
            }
            User user = getByUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            updateById(user);
            redisTemplate.delete(EMAIL_CODE_KEY+userDTO.getEmail());
            return userDTO;
        }
    }

    @Override
    public UserDTO unbindEmail(UserDTO userDTO) {
        String realCode=(String) redisTemplate.opsForValue().get(EMAIL_CODE_KEY+userDTO.getEmail());
        String code=userDTO.getCode();
        if(!Objects.equals(realCode, code)){
            throw new ServiceException(Constants.CODE_600,"验证码错误");
        }
        User user = getByEmail(userDTO.getEmail());
        user.setEmail(null);
        userMapper.updateUserByIdForChangeEmail(user);
        redisTemplate.delete(EMAIL_CODE_KEY+userDTO.getEmail());
        return userDTO;
    }

    @Override
    public User getByEmail(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",email);
        User one;
        try{
            one=getOne(queryWrapper);
        }catch (Exception e){
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        return one;
    }

    private User getByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User one;
        try{
            one=getOne(queryWrapper);
        }catch (Exception e){
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        return one;
    }

    //    不用密码生成Token的方法，但是方法要统一，后面采用随机生成用户密码的方法，所以以下returnEmailLoginUser方法被弃用
    private UserDTO returnEmailLoginUser(User one,UserDTO userDTO){
        BeanUtil.copyProperties(one,userDTO,true);
        //设置token
        String token = TokenUtils.genEmailLoginToken(one.getId().toString());
        userDTO.setToken(token);
        String avatar=one.getAvatar();
        userDTO.setAvatar(avatar);
        int id =one.getId();
        userDTO.setId(id);
        String role=one.getRole();//查询出用户的角色标识，比如ROLE_ADMIN
        //设置用户的菜单列表
        List<Menu> roleMenus=getRoleMenus(role);
        userDTO.setMenus(roleMenus);
        redisTemplate.delete(EMAIL_CODE_KEY+userDTO.getEmail());
        return userDTO;//返回登录类userDTO
    }

    //verificationSuccess
    @Override
    public boolean emailVerificationSuccess(UserDTO userDTO) {
        String realCode=(String) redisTemplate.opsForValue().get(EMAIL_CODE_KEY+userDTO.getEmail());
        String code=userDTO.getCode();
        if(!Objects.equals(realCode, code)){
            throw new ServiceException(Constants.CODE_600,"验证码错误");
        }
        redisTemplate.delete(EMAIL_CODE_KEY+userDTO.getEmail());
        return true;
    }


    private static String getLoginEmailCode(int n) {
        Random r = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            int type = r.nextInt(3);
            switch (type) {
                case 0:
                    char c0 = (char) (r.nextInt(26) + 65);
                    stringBuilder.append(c0);
                    break;
                case 1:
                    char c1 = (char) (r.nextInt(26) + 97);
                    stringBuilder.append(c1);
                    break;
                case 2:
                    int m = r.nextInt(10);
                    stringBuilder.append(m);
                    break;
                default:
                    break;
            }
        }
        return stringBuilder.toString();
    }

//    生成密码方法(邮箱生成初始密码用)
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String PASSWORD_ALLOWED_CHARS = CHAR_LOWER + CHAR_UPPER + NUMBER;

    private static final int PASSWORD_LENGTH = 15;
    private static SecureRandom random = new SecureRandom();

    public static String generateRandomPassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int randomIndex = random.nextInt(PASSWORD_ALLOWED_CHARS.length());
            char randomChar = PASSWORD_ALLOWED_CHARS.charAt(randomIndex);
            password.append(randomChar);
        }
        return password.toString();
    }



}
