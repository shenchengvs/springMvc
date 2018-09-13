package com.holley.mvc.shiro;

import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.holley.mvc.model.def.CurrentUser;
import com.holley.mvc.service.CommonService;

public class MyShiroRealm extends AuthorizingRealm {

    @Resource
    private CommonService       commonService;
    // 这里因为没有调用后台，直接默认只有一个用户("luoguohui"，"123456")
    private static final String USER_NAME = "luoguohui";
    private static final String PASSWORD  = "123456";

    /*
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        CurrentUser currentUser = (CurrentUser) principals.getPrimaryPrincipal();
        // Set<String> roleNames = new HashSet<String>();
        // roleNames.add("admi1n");// 添加角色
        // permissions.add("/ent/queryEnt"); // 添加权限
        // permissions.add("/ent/uploadFilePage"); // 添加权限
        // permissions.add("/ent/queryJson"); // 添加权限
        // permissions.add("/ent/queryEntInfo"); // 添加权限
        // permissions.add("/ent/queryEntExcel"); // 添加权限
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(currentUser.getPermissions());
        return info;
    }

    /*
     * 登录验证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String pwd = String.valueOf(token.getPassword());
        if (!USER_NAME.equals(token.getUsername())) {
            throw new UnknownAccountException();
        } else if (!PASSWORD.equals(pwd)) {
            throw new IncorrectCredentialsException();
        }
        CurrentUser c = new CurrentUser(USER_NAME);
        Set<String> permissions = c.getPermissions();
        permissions.add("/ent/queryEnt");
        permissions.add("/ent/uploadFilePage"); // 添加权限
        permissions.add("/ent/queryJson"); // 添加权限
        permissions.add("/ent/queryEntInfo"); // 添加权限
        permissions.add("/ent/queryEntExcel"); // 添加权限
        return new SimpleAuthenticationInfo(c, PASSWORD, getName());
    }
}
