package com.yuqn.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.junit.Test;

/**
 * @author: yuqn
 * @Date: 2024/5/11 1:20
 * @description:
 * 第一部分：创建表
 * @version: 1.0
 */
public class TestCreate {
    /**
     * @author: yuqn
     * @Date: 2024/4/28 17:41
     * @description:
     * 使用activiti提供的默认方式来创建mysql的表
     * @param: null
     * @return: null
     */
    @Test
    public void testCreateDbTable(){
        // 需要使用activiti提供的工具类ProcessEngines，使用getDefaultProcessEngine方法
        // getDefaultProcessEngine方法会默认从resources下读取activiti.cfg.xml文件
        // 创建processEngine时，就会创建mysql表

        // 默认方式，自动读取配置activiti.cfg.xml文件
/*        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        repositoryService.createDeployment();*/

        // 自定义方式，可以自定义activiti.cfg.xml名字，以及文件中的 id
        ProcessEngineConfiguration processEngineConfigurationFromInputStream = ProcessEngineConfiguration.
                createProcessEngineConfigurationFromResource("activiti.cfg.xml","processEngineConfiguration");
        // 获取流程引擎对象
        ProcessEngine processEngine = processEngineConfigurationFromInputStream.buildProcessEngine();

        System.out.println(processEngine);
    }
}
