package com.msb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @BelongsProject: Spring Practice
 * @BelongsPackage: com.msb.pojo
 * @Author: SSS
 * @Description:
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Account implements Serializable {
    private Integer id;
    private String name;
    private Integer money;
}
