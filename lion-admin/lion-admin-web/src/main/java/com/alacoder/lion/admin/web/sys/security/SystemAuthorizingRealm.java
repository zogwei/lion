package com.alacoder.lion.admin.web.sys.security;

import java.io.Serializable;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Service;

import com.alacoder.lion.admin.web.common.utils.Encodes;
import com.alacoder.lion.admin.web.sys.entity.User;


@Service
public class SystemAuthorizingRealm extends AuthorizingRealm {

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {

    	UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
    	
    	User user = new User();
    	user.setName(token.getUsername());
    	user.setPassword(token.getPassword().toString());
    	byte[] salt = Encodes.decodeHex(user.getPassword().substring(0,16));
    
    	return new SimpleAuthenticationInfo(new Principal(user, token.isMobileLogin()), 
				user.getPassword().substring(16), ByteSource.Util.bytes(salt), getName());

    }
    
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    	 String username = (String)principals.getPrimaryPrincipal();
    	 Principal principal = (Principal) getAvailablePrincipal(principals);
    	 String loginName = principal.getLoginName();
    	 
    	SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    	info.addStringPermission("user");
    	info.addStringPermission("user:create");
//        info.setRoles(userService.findRoles(username));
//        info.setStringPermissions(userService.findPermissions(username));

        return info;
    }
    
	/**
	 * 授权用户信息
	 */
	public static class Principal implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private String id; // 编号
		private String loginName; // 登录名
		private String name; // 姓名
		private boolean mobileLogin; // 是否手机登录
		
//		private Map<String, Object> cacheMap;

		public Principal(User user, boolean mobileLogin) {
			this.id = user.getId();
			this.loginName = user.getLoginName();
			this.name = user.getName();
			this.mobileLogin = mobileLogin;
		}

		public String getId() {
			return id;
		}

		public String getLoginName() {
			return loginName;
		}

		public String getName() {
			return name;
		}

		public boolean isMobileLogin() {
			return mobileLogin;
		}

//		@JsonIgnore
//		public Map<String, Object> getCacheMap() {
//			if (cacheMap==null){
//				cacheMap = new HashMap<String, Object>();
//			}
//			return cacheMap;
//		}

		/**
		 * 获取SESSIONID
		 */
//		public String getSessionid() {
//			try{
//				return (String) UserUtils.getSession().getId();
//			}catch (Exception e) {
//				return "";
//			}
//		}
		
		@Override
		public String toString() {
			return id;
		}

	}

}
