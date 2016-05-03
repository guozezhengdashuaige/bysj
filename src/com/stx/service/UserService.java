package com.stx.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stx.dao.UserDao;
import com.stx.entity.User;

/**
 * 用户，管理员，商户登陆、注册同意管理
 * @author gzzdsg
 * 2016年3月8日
 */

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	/**
	 * 商户登陆用户注册
	 * 
	 * @param user
	 * @param password2
	 * @return	成功返回空，失败返回失败原因
	 */
	public String registMerchantUser(User user, String password2) {
		if(user.getLoginName()==null||user.getLoginName().trim().length()<1){
			return "登陆名不能为空";
		}
		if(user.getNickName()==null||user.getNickName().trim().length()<1){
			return "昵称不能为空";
		}
		if(user.getPassword()==null||user.getPassword().trim().length()<1){
			return "密码不能为空";
		}
		if(password2==null||password2.trim().length()<1){
			return "请再次输入密码";
		}
		if(!user.getPassword().trim().equals(password2)){
			return "两次密码输入不一致";
		}
		/** 设置用户类型为商家 **/
		user.setUserType(3);
		/** 设置登陆开关为商户申请中 **/
		user.setLoginSwitch(3);
		/** 超管标识 **/
		user.setAdminFlag(0);
		/** 设置注册时间 **/
		user.setCreateTime(new Date());
		
		/** 检查登陆名是否已经存在 **/
		if(userDao.existsUserByLoginName(user.getLoginName())){
			return "该登陆名已经存在";
		}
		
		/** 检查昵称是否已经存在 **/
		if(userDao.existsUserByNickName(user.getNickName())){
			return "昵称已经存在";
		}
		
		/** 保存用户信息 **/
		Long userId = userDao.saveUser(user);
		if(userId==null){
			return "注册失败";
		}
		
		return null;
	}
	
	/**
	 * 用户注册
	 * 
	 * @param user
	 * @param password2
	 * @return	成功返回空，失败返回失败原因
	 */
	public String registUser(User user, String password2) {
		if(user.getLoginName()==null||user.getLoginName().trim().length()<1){
			return "登陆名不能为空";
		}
		if(user.getNickName()==null||user.getNickName().trim().length()<1){
			return "昵称不能为空";
		}
		if(user.getPassword()==null||user.getPassword().trim().length()<1){
			return "密码不能为空";
		}
		if(password2==null||password2.trim().length()<1){
			return "请再次输入密码";
		}
		if(!user.getPassword().trim().equals(password2)){
			return "两次密码输入不一致";
		}
		/** 设置用户类型为用户 **/
		user.setUserType(2);
		/** 设置登陆开关为能够登陆**/
		user.setLoginSwitch(1);
		/** 超管标识 **/
		user.setAdminFlag(0);
		/** 设置注册时间 **/
		user.setCreateTime(new Date());
		
		/** 检查登陆名是否已经存在 **/
		if(userDao.existsUserByLoginName(user.getLoginName())){
			return "该登陆名已经存在";
		}
		/** 检查昵称是否已经存在 **/
		if(userDao.existsUserByNickName(user.getNickName())){
			return "昵称已经存在";
		}
		/** 保存用户信息 **/
		Long userId = userDao.saveUser(user);
		if(userId==null){
			return "注册失败";
		}
		return null;
	}

	/**
	 * 获取登陆用户信息
	 * 
	 * @param user
	 * @return
	 */
	public User merchantLogin(User user){
		return userDao.findUser(user);
	}
	
	/**
	 * 修改用户头像
	 * 
	 * @param user
	 */
	public String updateUserHead(User user, File head, String headPath, String headFileName){
		if (headFileName == null) {
			return "头像文件名不能为空。";
		}
		String headEnd = headFileName.substring(headFileName.length() - 4,
				headFileName.length());
		if (!headEnd.equals(".jpg") && !headEnd.equals(".png")) {
			return "头像只支持jpg或png格式。";
		}
		try {
			InputStream is = new BufferedInputStream(new FileInputStream(head));
			OutputStream os = new BufferedOutputStream(new FileOutputStream(
					new File(headPath, user.getLoginName() + headEnd)));
			byte[] buffer = new byte[500];
			while (-1 != (is.read(buffer, 0, buffer.length))) {
				os.write(buffer);
			}
			os.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
			return "头像保存失败。";
		}
		user.setHeadPath(user.getLoginName()+headEnd);
		userDao.updateUserInfo(user);
		return null;
	}
	
	/**
	 * 修改密码
	 * 
	 * @param user	原用户信息
	 * @param oldPassword	旧密码
	 * @param newPassword	新密码
	 * @param newPassword2	确认新密码
	 * @return
	 */
	public String updatePassword(User user, String oldPassword, String newPassword, String newPassword2){
		if(oldPassword==null||oldPassword.trim().length()<1){
			return "旧密码不能为空。";
		}
		if(!oldPassword.equals(user.getPassword())){
			return "旧密码不正确。";
		}
		if(newPassword==null||newPassword.trim().length()<1){
			return "新密码不能为空。";
		}
		if(newPassword2==null||newPassword2.trim().length()<1){
			return "确认密码不能为空。";
		}
		if(!newPassword.equals(newPassword2)){
			return "两次输入新密码不一致";
		}
		user.setPassword(newPassword);
		userDao.updateUserInfo(user);
		return null;
	}
	
	/**
	 * 根据id查询头像
	 * @param id
	 * @return
	 */
	public String queryHeadById(Long id){
		User user = userDao.findUserById(id);
		if(user==null){
			return "head.jpg";
		}
		return user.getHeadPath();
	}
	
}
