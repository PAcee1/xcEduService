package com.xuecheng.auth.service;

import com.xuecheng.auth.client.UserClient;
import com.xuecheng.framework.domain.ucenter.XcMenu;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    ClientDetailsService clientDetailsService;
    @Autowired
    private UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //取出身份，如果身份为空说明没有认证
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //没有认证统一采用httpbasic认证，httpbasic中存储了client_id和client_secret，开始认证client_id和client_secret
        if(authentication==null){
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(username);
            if(clientDetails!=null){
                //密码
                String clientSecret = clientDetails.getClientSecret();
                return new User(username,clientSecret,AuthorityUtils.commaSeparatedStringToAuthorityList(""));
            }
        }
        if (StringUtils.isEmpty(username)) {
            return null;
        }

        // 远程调用查询用户信息
        XcUserExt userExt = userClient.getUserExt(username);
        if(userExt == null){
            return null;
        }

        /**
         * 添加用户权限
         */
        //从数据库获取权限
        List<XcMenu> permissions = userExt.getPermissions();
        if(permissions == null){
            permissions = new ArrayList<>();
        }
        List<String> user_permission = new ArrayList<>();
        permissions.forEach(item-> user_permission.add(item.getCode()));
        String user_permission_string  = StringUtils.join(user_permission.toArray(), ",");

        //取出正确密码（hash值）
        String password = userExt.getPassword();
        // 拼装用户信息到UserDetails
        // 第三个参数是用户权限
        UserJwt userDetails = new UserJwt(username,
                password,
                AuthorityUtils.commaSeparatedStringToAuthorityList(user_permission_string));

        /**
         * 用户信息封装
         */
        userDetails.setId(userExt.getId());
        userDetails.setUtype(userExt.getUtype());//用户类型
        userDetails.setCompanyId(userExt.getCompanyId());//所属企业
        userDetails.setName(userExt.getName());//用户名称
        userDetails.setUserpic(userExt.getUserpic());//用户头像
        return userDetails;
    }
}
