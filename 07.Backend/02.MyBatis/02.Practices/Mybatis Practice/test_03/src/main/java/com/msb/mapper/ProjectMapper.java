package com.msb.mapper;

import com.msb.pojo.Project;

public interface ProjectMapper {
    /**
     * 根据项目编号查询一个项目信息及参与该项目的所有员工信息
     * @param pid 项目编号
     * @return 所有信息封装的Project对象
     */
    Project findProjectJoinEmpsByPid(int pid);
}
