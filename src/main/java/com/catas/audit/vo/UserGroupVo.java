package com.catas.audit.vo;

import com.catas.audit.entity.AuthGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupVo extends AuthGroup {

    private Integer page = 1;

    private Integer limit = 10;

    @NotNull(message = "不能为空")
    @Size(min = 1, message = "不能为空")
    private List<Integer> relatedPermList;

}
