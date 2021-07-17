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
    public List<Role> getAllRole() {
        return roleMapper.selectAll();
    }
}
