
package com.seu.vehiclism.User.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seu.vehiclism.CarTirm.entity.CarTirm;
import com.seu.vehiclism.Response;
import com.seu.vehiclism.User.entity.User;
import com.seu.vehiclism.User.service.IUserService;
import com.seu.vehiclism.UserFavoriteCar.service.IUserFavoriteCarService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author QTM
 * @since 2024-08-28
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final IUserService userService;
    private final IUserFavoriteCarService userFavoriteCarService;
//    @Autowired
    public UserController(IUserService userService, IUserFavoriteCarService userFavoriteCarService) {
        this.userService = userService;
        this.userFavoriteCarService = userFavoriteCarService;
    }
    // 用户注册接口
    @PostMapping("/register")
    public Response<Void> register(@RequestBody registerRequest user) {
        User user1 = new User();
        try{
            if (user.getName() == null || user.getName().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空！");
        }
            user1.setName(user.getName());
        if (user.getpwd() == null || user.getpwd().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空！");
        }
            user1.setPassword(user.getpwd());
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("邮箱不能为空！");
        }
            user1.setEmail(user.getEmail());
        if (user.getPhone() == null) {
            throw new IllegalArgumentException("手机号不能为空！");
        }
            user1.setPhone(user.getPhone());
        // 假设这里有一个方法用于检查用户名是否已经存在
        if (userService.existsByPhone(user.getPhone())) {
            throw new IllegalArgumentException("电话已被占用！");
        }
        if(userService.existsByEmail(user.getEmail())){
            throw new IllegalArgumentException("邮箱已被占用！");
        }
        }catch (IllegalArgumentException e){
            return  Response.newFail(e.getMessage());
        }
        userService.save(user1);
        return Response.newSuccess(null,"注册成功！");
    }
    // 用户登录接口
    @PostMapping("/login")
    public Response<User> login(@RequestBody LoginRequest loginRequest) {
        try {
            // 验证邮箱/手机号和密码
            if (loginRequest.getKey() == null || loginRequest.getKey().isEmpty()) {
                throw new IllegalArgumentException("邮箱/手机号不能为空！");
            }
            if (loginRequest.getPwd() == null || loginRequest.getPwd().isEmpty()) {
                throw new IllegalArgumentException("密码不能为空！");
            }

            // 假设这里有一个方法用于验证用户
            User user = userService.login(loginRequest.getKey(), loginRequest.getPwd());

            if (user == null) {
                throw new IllegalArgumentException("该账号不存在，请注册/密码错误，请重新输入");
            }

            return Response.newSuccess(user, "登录成功");
        } catch (IllegalArgumentException e) {
            return Response.newFail(e.getMessage());
        }
    }
    // 修改姓名接口
    @PostMapping("/editName")
    public Response<Void> editName(@RequestBody Map<String,Object> request){
        Long id;
        String idObj = (String) request.get("id");
        String name=(String)request.get("name");
        try{
            id = getaLong(idObj, name);
            boolean success=userService.updateName(id,name);
            if(!success){
                throw new RuntimeException("更新失败！");
            }
            // 更新成功
            return Response.newSuccess(null, "修改成功！");
        }catch (IllegalArgumentException e){
            return Response.newFail(e.getMessage());
        }
    }
    // 修改密码接口
    @PostMapping("/editPassword")
    public Response<Void> editPassword(@RequestBody Map<String,Object> request){
        Long id;
        String idObj = (String) request.get("id");
        String password=(String)request.get("password");
        try{
            id = getaLong(idObj, password);
            boolean success=userService.updatePassword(id,password);
            if(!success){
                throw new RuntimeException("更新失败！");
            }
            // 更新成功
            return Response.newSuccess(null, "修改成功！");
        }catch (IllegalArgumentException e){
            return Response.newFail(e.getMessage());
        }
    }
    @PostMapping("/editEmail")
    public Response<Void> editEmail(@RequestBody Map<String,Object> request){
        Long id;
        String idObj = (String) request.get("id");
        String email=(String)request.get("email");
        try{
            id = getaLong(idObj, email);
            boolean success=userService.updateEmail(id,email);
            if(!success){
                throw new RuntimeException("更新失败！");
            }
            // 更新成功
            return Response.newSuccess(null, "修改成功！");
        }catch (IllegalArgumentException e){
            return Response.newFail(e.getMessage());
        }
    }
    @PostMapping("/editPhone")
    public Response<Void> editPhone(@RequestBody Map<String,Object> request){
        Long id;
        String idObj = (String) request.get("id");
        String phone=(String)request.get("phone");
        try{
            id = getaLong(idObj, phone);
            boolean success=userService.updatePhone(id,phone);
            if(!success){
                throw new RuntimeException("更新失败！");
            }
            // 更新成功
            return Response.newSuccess(null, "修改成功！");
        }catch (IllegalArgumentException e){
            return Response.newFail(e.getMessage());
        }
    }
    private Long getaLong(String idObj, String other) {
        Long id;
        if (!idObj.isEmpty()) {
            id = Long.parseLong((String) idObj); // 将String类型的id转换为Long类型
        }else {
            throw new IllegalArgumentException("ID类型错误！");
        }
        if(id==null||other.isEmpty()){
            throw new IllegalArgumentException("请求参数无效！");
        }
        return id;
    }
    @PostMapping("/likes/pages")
    public Response<IPage<CarTirm>> getLikesByPage(@RequestBody Map<String,Object>request){
        Long id;
        String idObj = (String) request.get("id");
        Integer pageNum=(Integer) request.get("pageNum");
        Integer pageSize=(Integer) request.get("pageSize");
        if (pageNum == null || pageSize == null) {
            return Response.newFail("参数缺失");
        }
        try{
            id = Long.parseLong((String) idObj);
            IPage<CarTirm>favoriteCars= userFavoriteCarService.getUserFavoriteCarByPage(id,pageNum,pageSize);
            return Response.newSuccess(favoriteCars,"查询成功");
        }catch (IllegalArgumentException e){
            return Response.newFail(e.getMessage());
        }
    }
    @PostMapping("/likes")
    public Response<List<CarTirm>> getLikes(@RequestBody Map<String,Object>request){
        Long id;
        String idObj = (String) request.get("id");
        try{
            id = Long.parseLong((String) idObj);
            List<CarTirm>favoriteCars= userFavoriteCarService.getUserFavoriteCar(id);
            return Response.newSuccess(favoriteCars,"查询成功");
        }catch (IllegalArgumentException e){
            return Response.newFail(e.getMessage());
        }
    }

}