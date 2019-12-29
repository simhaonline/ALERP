package edu.nju.alerp.service.impl;

import edu.nju.alerp.common.cache.Cache;
import edu.nju.alerp.common.conditionSqlQuery.Condition;
import edu.nju.alerp.common.conditionSqlQuery.ConditionFactory;
import edu.nju.alerp.common.conditionSqlQuery.QueryContainer;
import edu.nju.alerp.entity.OperationLog;
import edu.nju.alerp.entity.Product;
import edu.nju.alerp.service.UserService;
import edu.nju.alerp.entity.User;
import edu.nju.alerp.enums.UserStatus;
import edu.nju.alerp.repo.UserRepository;
import edu.nju.alerp.dto.UserDTO;
import edu.nju.alerp.util.DateUtils;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 用户服务层实现
 * @Author: qianen.yin
 * @CreateDate: 2019-12-17 17:41
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    UserRepository userRepository;

    @Resource
    private Cache<Integer, Object> userCache;

    @Override
    public int saveUser(UserDTO userDTO) {
        User user;
        if(userDTO.getId() == null){
            user = User.builder()
                    .name(userDTO.getName())
                    .password(userDTO.getPassword())
                    .phoneNumber(userDTO.getPhoneNumber())
                    .createdAt(DateUtils.getToday())
                    .status(UserStatus.ONJOB.getCode())
                    .build();
        }
        else{
            user = getUser(userDTO.getId());
            user.setName(userDTO.getName());
            user.setPassword(userDTO.getPassword());
            user.setPhoneNumber(userDTO.getPhoneNumber());
            user.setUpdatedAt(DateUtils.getToday());
        }
        User res = userRepository.saveAndFlush(user);
        userCache.put(userDTO.getId(), res);
        return res.getId();
    }

    @Override
    public User getUser(int id) {
        User user = (User) userCache.get(id);
        if (user == null) {
            user = userRepository.getOne(id);
            if (user != null) {
                userCache.put(id, user);
            }
        }
        return user;
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findDistinctByPhoneNumber(phoneNumber);
    }

    @Override
    public boolean deleteUser(int id) {
        User user = getUser(id);
        if (user == null) {
            return false;
        }
        user.setStatus(UserStatus.OFFJOB.getCode());
        user.setDeletedAt(DateUtils.getToday());
        userRepository.save(user);
        userCache.put(id, user);
        return true;
    }

    @Override
    public Page<User> getUserList(Pageable pageable, String name, int status) {
        QueryContainer<User> sp = new QueryContainer<>();
        try {
            sp.add(ConditionFactory.equal("status", status));
            List<Condition> fuzzyMatch = new ArrayList<>();
            fuzzyMatch.add(ConditionFactory.like("name", name));
//            fuzzyMatch.add(ConditionFactory.like("shorthand", name));
            fuzzyMatch.add(ConditionFactory.like("phoneNumber", name));
            sp.add(ConditionFactory.or(fuzzyMatch));
        } catch (Exception e) {
            log.error("Value is null", e);
        }
        return userRepository.findAll(sp, pageable);
    }

    @Override
    public List<User> getUserList() {
        return userRepository.findAll();
    }

    @Override
    public int getIdFromName(String name){
        //fixme:用户中如果有重名的怎么办？
        return userRepository.findDistinctByName(name).getId();
    }
}
