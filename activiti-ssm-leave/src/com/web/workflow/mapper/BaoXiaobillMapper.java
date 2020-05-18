package com.web.workflow.mapper;

import com.web.workflow.pojo.BaoXiaobill;
import com.web.workflow.pojo.BaoXiaobillExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BaoXiaobillMapper {
    int countByExample(BaoXiaobillExample example);

    int deleteByExample(BaoXiaobillExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BaoXiaobill record);

    int insertSelective(BaoXiaobill record);

    List<BaoXiaobill> selectByExample(BaoXiaobillExample example);

    BaoXiaobill selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BaoXiaobill record, @Param("example") BaoXiaobillExample example);

    int updateByExample(@Param("record") BaoXiaobill record, @Param("example") BaoXiaobillExample example);

    int updateByPrimaryKeySelective(BaoXiaobill record);

    int updateByPrimaryKey(BaoXiaobill record);
}