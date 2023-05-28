package com.msb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data               // 提供所有属性的 get() 和 set() 方法
@AllArgsConstructor // 提供全参构造方法
@NoArgsConstructor  // 提供无参构造方法
public class Dept implements Serializable {
    private Integer deptno;
    private String dname;
    private String loc;
}