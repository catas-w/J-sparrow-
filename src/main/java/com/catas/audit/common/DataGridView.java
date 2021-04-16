package com.catas.audit.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataGridView {

    private Integer code = 0;

    private String msg = "";

    private Long count = 0L;

    private Object data;

    public DataGridView(Long count, Object data) {
        this.count = count;
        this.data = data;
    }

    public DataGridView(Object data) {
        this.data = data;
    }

    public DataGridView(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
