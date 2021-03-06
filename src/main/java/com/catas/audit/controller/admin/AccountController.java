package com.catas.audit.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catas.audit.common.DataGridView;
import com.catas.audit.common.ResultObj;
import com.catas.audit.entity.Hostuser;
import com.catas.audit.service.IHostuserService;
import com.catas.audit.vo.AccountVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private IHostuserService hostuserService;

    // 获取所有账户信息
    @RequestMapping("/get-all-account")
    public DataGridView getAllAccount(AccountVo accountVo) {
        IPage<Hostuser> page = new Page<>(accountVo.getPage(), accountVo.getLimit());
        QueryWrapper<Hostuser> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNoneBlank(accountVo.getUsername()), "username", accountVo.getUsername());
        hostuserService.page(page, queryWrapper);

        page.convert(hostuser -> {
            AccountVo vo = new AccountVo();
            BeanUtils.copyProperties(hostuser, vo);
            return vo;
        });

        return new DataGridView(page.getTotal(), page.getRecords());
    }

    @RequiresPermissions("host:edit")
    @RequestMapping("/add")
    public ResultObj addAccount(AccountVo accountVo) {
        try {
            hostuserService.save(accountVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_FAILED;
        }
    }

    @RequiresPermissions("host:edit")
    @RequestMapping("/update")
    public ResultObj updateAccount(AccountVo accountVo) {
        try {
            hostuserService.updateById(accountVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_FAILED;
        }
    }

    @RequiresPermissions("host:edit")
    @RequestMapping("/delete/{id}")
    public ResultObj deleteAccounr(@PathVariable("id") Integer id) {
        try {
            hostuserService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_SUCCESS;
        }
    }
}
