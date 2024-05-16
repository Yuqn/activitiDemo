package com.yuqn.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;

/**
 * @author: yuqn
 * @Date: 2024/5/11 17:02
 * @description:
 * 第五部分：通过监听器设置任务负责人
 * @version: 1.0
 */
public class TestListener {
    /**
     * @author: yuqn
     * @Date: 2024/4/29 23:52
     * @description:
     * 部署流程
     * @param: null
     * @return: null
     */
    @Test
    public void testDeployment(){
        // 1、创建processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取RepositoryServoie
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3、使用service进行流程部署，定义一个流程的名字，把bpmn和png部署到数据中
        Deployment deploy = repositoryService.createDeployment()
                .name("测试监听器")
                .addClasspathResource("bpmn/evection-listen.bpmn20.xml")
                .deploy();
        // 4、输出部署信息
        System.out.println("流程部署id=" + deploy.getId());
        System.out.println("流程部署名字=" + deploy.getName());
    }

    @Test
    public void startDemoListener(){
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取runtimeservice
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 启动流程实例
        runtimeService.startProcessInstanceByKey("evection-listen");
    }
}
