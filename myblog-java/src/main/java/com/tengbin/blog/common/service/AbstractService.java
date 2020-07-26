package com.tengbin.blog.common.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tengbin.blog.common.exception.ServiceException;
import com.tengbin.blog.common.mapper.Mapper;
import com.tengbin.blog.modules.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.TooManyListenersException;

/**
 * 基于通用MyBatis Mapper插件的Service接口的实现
 *
 * @author Sunshine
 */
public abstract class AbstractService<T> implements Service<T> {

    /**
     * 自定义mapper接口
     */
    @Autowired
    protected Mapper<T> mapper;

    /**
     * 当前泛型真实类型的Class
     */
    private Class<T> modelClass;


    public AbstractService() {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }

    /**
     * 添加
     *
     * @param model
     */
    @Override
    public int save(T model) {
        return mapper.insertSelective(model);
    }

    /**
     * 批量添加
     *
     * @param models
     */
    @Override
    public int save(List<T> models) {
        return mapper.insertList(models);
    }


    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @Override
    public int deleteByIds(String ids) {
        return mapper.deleteByIds(ids);
    }

    /**
     * 根据主键修改
     *
     * @param model
     * @return
     */
    @Override
    public int update(T model) {
        return mapper.updateByPrimaryKeySelective(model);
    }


    /**
     * 根据model的成员变量查询. value值符合唯一约束
     *
     * @param fieldName
     * @param value
     * @return
     * @throws TooManyListenersException
     */
    @Override
    public T findBy(String fieldName, Object value) throws TooManyListenersException {
        try {
            // 实例化对象
            T model = modelClass.newInstance();

            // 获取类特定的方法
            Field field = modelClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(model, value);

            // 返回查询对象
            return mapper.selectOne(model);

        } catch (ReflectiveOperationException e) {
            // 自定义异常
            throw new ServiceException(e.getMessage(), e);
        }

    }


    /**
     * 根据主键删除
     *
     * @param id
     */
    @Override
    public int deleteById(Long id) {
        return mapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据主键查找
     *
     * @param id
     * @return
     */
    @Override
    public T findById(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 通过多个id值查找
     *
     * @param ids
     * @return
     */
    @Override
    public List<T> findByIds(String ids) {
        return mapper.selectByIds(ids);
    }

    /**
     * 根据条件查询
     *
     * @param queryBuilder
     * @return
     */
    @Override
    public List<T> findByCondition(Condition condition) {
        return mapper.selectByCondition(condition);
    }

    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public List<T> findAll() {
        return mapper.selectAll();
    }
}
