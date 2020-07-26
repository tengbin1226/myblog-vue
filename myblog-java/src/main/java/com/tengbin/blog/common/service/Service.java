package com.tengbin.blog.common.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.TooManyListenersException;

/**
 * Service层,基础接口.(其他Serviceceng接口就继承该接口)
 *
 * @author TengBin
 */
public interface Service<T> {

    /**
     * 添加
     *
     * @param model
     */
    int save(T model);

    /**
     * 批量添加
     *
     * @param models
     */
    int save(List<T> models);

    /**
     * 根据主键删除
     *
     * @param id
     */
    int deleteById(Long id);

    /**
     * 批量删除 eg：ids=“1,2,3,4”
     *
     * @param ids
     */
    int deleteByIds(String ids);

    /**
     * 更新(修改)
     *
     * @param model
     */
    int update(T model);

    /**
     * 根据主键查找
     *
     * @param id
     * @return
     */
    T findById(Long id);

    /**
     * 通过model中某个成员变量名称(非数据库表中的Colum的名称)查找,
     * value值需要符合unique(唯一)约束
     *
     * @param fieldName
     * @param value
     * @return
     * @throws TooManyListenersException
     */
    T findBy(String fieldName, Object value) throws TooManyListenersException;

    /**
     * 通过多个id值查找 eg：ids=“1,2,3,4”
     *
     * @param ids
     * @return
     */
    List<T> findByIds(String ids);

    /**
     * 根据条件查询
     *
     * @param queryBuilder
     * @return
     */
    List<T> findByCondition(Condition condition);

    /**
     * 查询所有
     *
     * @return
     */
    List<T> findAll();

}
