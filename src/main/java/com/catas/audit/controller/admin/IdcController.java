package com.catas.audit.controller.admin;


import com.catas.audit.common.Constant;
import com.catas.audit.common.DataGridView;
import com.catas.audit.common.ResultObj;
import com.catas.audit.service.IIdcService;
import com.catas.audit.vo.IdcVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/idc")
public class IdcController {

    @Autowired
    private IIdcService idcService;

    @RequestMapping("/get-all")
    public DataGridView getAllIdcInfo(IdcVo idcVo) {
        List<Map<String, Object>> allIdcInfo = idcService.getAllIdcInfo(idcVo);
        return new DataGridView((long) allIdcInfo.size(), allIdcInfo);
    }

    @RequiresPermissions("host:edit")
    @RequestMapping("/add")
    public ResultObj addIdc(IdcVo idcVo) {
        try {
            idcService.save(idcVo);
            return ResultObj.ADD_SUCCESS;
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            return new ResultObj(Constant.ERROR, "名称已存在");
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_FAILED;
        }
    }

    @RequiresPermissions("host:edit")
    @RequestMapping("/update")
    public ResultObj updateIdc(IdcVo idcVo) {
        try {
            idcService.updateById(idcVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            return new ResultObj(Constant.ERROR, "名称已存在");
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_FAILED;
        }
    }

    @RequiresPermissions("host:edit")
    @RequestMapping("/delete/{id}")
    public ResultObj deleteIdc(@PathVariable("id") Integer idcId ) {
        try {
            idcService.removeById(idcId);
            return ResultObj.DELETE_SUCCESS;
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_FAILED;
        }
    }
}
