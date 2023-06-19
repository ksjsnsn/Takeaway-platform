package com.ustlearn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ustlearn.pojo.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
