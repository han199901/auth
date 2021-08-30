package com.han.auth.services.impl;

import com.han.auth.entity.Role;
import com.han.auth.entity.User;
import com.han.auth.entity.UserRole;
import com.han.auth.mapper.RoleMapper;
import com.han.auth.mapper.UserMapper;
import com.han.auth.mapper.UserRoleMapper;
import com.han.auth.services.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;

    @Autowired
    public UserRoleServiceImpl(UserMapper userMapper, RoleMapper roleMapper, UserRoleMapper userRoleMapper) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.userRoleMapper = userRoleMapper;
    }


    @Override
    public List<Role> getUserRole(User user) {
        List<UserRole> userRoleList = userRoleMapper.selectByUid(user.getId());
        List<Role> roleList = new ArrayList<>();
        userRoleList.forEach(item -> {
            roleList.add(roleMapper.selectByPrimaryKey(item.getRid()));
        });
        return roleList;
    }

    @Override
    public List<String> getUserRoleName(User user) {
        List<Role> roleList = getUserRole(user);
        List<String> listRoleName = new ArrayList<>();
        roleList.forEach(item -> {
            listRoleName.add(item.getName());
        });
        return listRoleName;
    }

    @Override
    public List<Role> getAllRole() {
        return roleMapper.selectAll();
    }

    @Override
    public int setUserRole(User user, Role role) {
        UserRole userRole = new UserRole();
        userRole.setUid(user.getId());
        userRole.setRid(role.getId());
        userRoleMapper.insert(userRole);
        return 0;
    }
}
