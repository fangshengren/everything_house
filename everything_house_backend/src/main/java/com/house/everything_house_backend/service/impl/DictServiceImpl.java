package com.house.everything_house_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.house.everything_house_backend.entities.Dict;
import com.house.everything_house_backend.mapper.DictMapper;
import com.house.everything_house_backend.service.IDictService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {
}
