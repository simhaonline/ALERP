package edu.nju.alerp.controller;

import edu.nju.alerp.common.ListResponse;
import edu.nju.alerp.common.ResponseResult;
import edu.nju.alerp.dto.AuthDTO;
import edu.nju.alerp.dto.UpdateUserAuthDTO;
import edu.nju.alerp.entity.Auth;
import edu.nju.alerp.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description: 权限Controller
 * @Author: yangguan
 * @CreateDate: 2020-01-08 20:12
 */

@Slf4j
@Validated
@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 新增或更新权限资源
     *
     * @param authDTO
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseResult<Integer> addOrUpdateAuth(@RequestBody AuthDTO authDTO) {
        return ResponseResult.ok(authService.addOrUpdateAuth(authDTO));
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseResult<List<Auth>> queryAuthList() {
        return ResponseResult.ok(authService.findAll());
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ResponseResult<Integer> editUserAuth(@RequestBody List<UpdateUserAuthDTO> updateAuths) {
        return ResponseResult.ok(authService.updateUserAuth(updateAuths));
    }

    @RequestMapping(value = "/initial/{id}", method = RequestMethod.GET)
    public ResponseResult<Integer> initialUserAuthById(@NotNull(message = "id不能为空") @PathVariable("id") Integer id) {
        return ResponseResult.ok(authService.initialUserAuthByUserId(id));
    }

}
