package top.jolyoulu.mybatis.entity;


import java.util.Date;

/**
 * 用户表
 */
public class SysUser {

	/** 用户id */
    private String id;

	/** 登录账号 */
    private String account;


	/** 创建者 */
    private String createBy;


	/** 创建时间 */
    private Date createTime;


	/** 状态（0正常 1删除） */
    private Integer delFlag;


	/** 电子邮箱 */
    private String email;


	/** 头像路径 */
    private String headimgUrl;


	/** 最后登陆时间 */
    private Date lastLoginDate;


	/** 最后登陆IP */
    private String lastLoginIp;


	/** 状态(1：正常  2：冻结 ） */
    private Integer status;


	/** 用户昵称 */
    private String nickname;


	/** 登录密码 */
    private String password;


	/** md5加密盐 */
    private String salt;


	/** 手机号码 */
    private String phone;


	/** 备注信息 */
    private String remarks;


	/** 用户性别 */
    private Integer sex;


	/** 更新者 */
    private String updateBy;


	/** 更新时间 */
    private Date updateTime;

}