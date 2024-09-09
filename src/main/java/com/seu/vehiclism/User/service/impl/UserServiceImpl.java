//package com.seu.vehiclism.User.service.impl;
//
//import com.seu.vehiclism.User.entity.User;
//import com.seu.vehiclism.User.mapper.UserMapper;
//import com.seu.vehiclism.User.service.IUserService;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import org.springframework.stereotype.Service;
//
///**
// * <p>
// * 用户表 服务实现类
// * </p>
// *
// * @author QTM
// * @since 2024-08-28
// */
//@Service
//public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
//
//}
package com.seu.vehiclism.User.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seu.vehiclism.User.entity.User;
import com.seu.vehiclism.User.mapper.UserMapper;
import com.seu.vehiclism.User.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author QTM
 * @since 2024-08-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private final UserMapper userMapper;
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public boolean existsByUsername(String username) {
        return Optional.ofNullable(this.baseMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>().eq("name", username)))
                .map(user -> true)
                .orElse(false);
    }
    @Override
    public boolean existsByEmail(String email){
        return Optional.ofNullable(this.baseMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>().eq("email", email)))
                .map(user -> true)
                .orElse(false);
    }
    @Override
    public boolean existsByPhone(String phone){
        return Optional.ofNullable(this.baseMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>().eq("phone",phone)))
                .map(user -> true)
                .orElse(false);
    }

    @Override
    public User login(String key, String password) {
        // 构建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        // 使用lambda表达式来构建查询条件
        queryWrapper.eq("email", key).eq("password", password)
                .or()
                .eq("phone", key).eq("password", password);

        // 查询用户
        User user = baseMapper.selectOne(queryWrapper);

        // 返回找到的用户
        return user;
    }
    public Optional<User> findUserById(Long id) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",id);
        User user=baseMapper.selectOne(queryWrapper);
        return Optional.ofNullable(user);
    }
    @Override
    public boolean updateName(Long id, String name) {
        // 查找用户
        Optional<User> optionalUser = findUserById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(name);

            // 使用update方法更新记录
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            boolean result = userMapper.update(user, queryWrapper) > 0;

            return result;
        }
        return false;
    }

    @Override
    public boolean updatePassword(Long id, String password) {
        // 查找用户
        Optional<User> optionalUser = findUserById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(password);

            // 使用update方法更新记录
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            boolean result = userMapper.update(user, queryWrapper) > 0;

            return result;
        }
        return false;
    }

    @Override
    public boolean updateEmail(Long id, String email) {
        // 首先检查是否有其他用户使用了相同的邮箱
        if (isEmailAlreadyUsed(email, id)) {
            throw new IllegalArgumentException("邮箱已被其他用户占用！");
        }
        // 查找用户
        Optional<User> optionalUser = findUserById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEmail(email);

            // 使用update方法更新记录
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            boolean result = userMapper.update(user, queryWrapper) > 0;

            return result;
        }
        return false;
    }
    @Override
    public boolean updatePhone(Long id, String phone) {
        // 首先检查是否有其他用户使用了相同的邮箱
        if (isPhoneAlreadyUsed(phone, id)) {
            throw new IllegalArgumentException("手机号已被其他用户占用！");
        }
        // 查找用户
        Optional<User> optionalUser = findUserById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPhone(phone);

            // 使用update方法更新记录
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            boolean result = userMapper.update(user, queryWrapper) > 0;

            return result;
        }
        return false;
    }
    // 检查邮箱是否已经被其他用户使用
    private boolean isEmailAlreadyUsed(String email, Long id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        queryWrapper.ne("id", id); // 排除当前用户
        return userMapper.selectCount(queryWrapper) > 0;
    }
    // 检查号码是否已经被其他用户使用
    private boolean isPhoneAlreadyUsed(String phone, Long id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        queryWrapper.ne("id", id); // 排除当前用户
        return userMapper.selectCount(queryWrapper) > 0;
    }
}