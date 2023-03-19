package top.jolyoulu.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import top.jolyoulu.mybatis.entity.SysUser;
import top.jolyoulu.mybatis.plugin.page.JlPage;

import java.util.List;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/19 12:30
 * @Description
 */
public interface SysUserMapper {

    List<SysUser> list();

    List<SysUser> listById(@Param("id") String id);
}
