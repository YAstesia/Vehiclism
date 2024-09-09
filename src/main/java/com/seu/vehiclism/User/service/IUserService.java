//package com.seu.vehiclism.User.service;
//
//import com.seu.vehiclism.User.entity.User;
//import com.baomidou.mybatisplus.extension.service.IService;
//
///**
// * <p>
// * 用户表 服务类
// * </p>
// *
// * @author QTM
// * @since 2024-08-28
// */
//public interface IUserService extends IService<User> {
//
//}
package com.seu.vehiclism.User.service;

import com.seu.vehiclism.User.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author QTM
 * @since 2024-08-28
 */
public interface IUserService extends IService<User> {

    /**
     * 根据用户名检查用户是否存在。
     *
     * @param username 用户名
     * @return 如果用户存在则返回true，否则返回false
     */
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    User login(String key, String password);

    boolean updateName(Long id, String name);

    boolean updatePassword(Long id, String password);

    boolean updateEmail(Long id, String email);

    boolean updatePhone(Long id, String phone);
}