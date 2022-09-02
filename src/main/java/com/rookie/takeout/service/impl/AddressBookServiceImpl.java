package com.rookie.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rookie.takeout.entity.AddressBook;
import com.rookie.takeout.service.AddressBookService;
import com.rookie.takeout.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;

/**
* @author LENOVO
* @description 针对表【address_book(地址管理)】的数据库操作Service实现
* @createDate 2022-08-31 17:32:30
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService{

}




