package com.ustlearn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ustlearn.mapper.AddressBookMapper;
import com.ustlearn.pojo.AddressBook;
import com.ustlearn.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
