package com.msb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRecord implements Serializable {
    // empno、pid都是主键（联合主键）
    private Integer empno;
    private Integer pid;
    // 组合一个Emp对象作为属性
    private Emp emp;
}