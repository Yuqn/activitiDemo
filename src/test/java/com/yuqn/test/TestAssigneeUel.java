package com.yuqn.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: yuqn
 * @Date: 2024/5/11 15:47
 * @description:
 * 第四部分：基于assignee uel模式测试模块
 * evection-uel.bpmn20.xml
 * @version: 1.0
 */
public class TestAssigneeUel {
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
                .name("出差申请流程-uel")
                .addClasspathResource("bpmn/evection-uel.bpmn20.xml")
                .addClasspathResource("bpmn/evection-uel.png")
                .deploy();
        // 4、输出部署信息
        System.out.println("流程部署id=" + deploy.getId());
        System.out.println("流程部署名字=" + deploy.getName());
    }

    /**
     * @author: yuqn
     * @Date: 2024/5/11 15:52
     * @description:
     * 启动流程引擎
     * @param: null
     * @return: null
     */
    @Test
    public void startAssigneeUel(){
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取runtimeservice
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 设定assignee的值，用来替代uel表达式
        Map<String,Object> assigneeMap = new HashMap<>();
        assigneeMap.put("assignee0","yuqn");
        assigneeMap.put("assignee1","李经理");
        assigneeMap.put("assignee2","王总经理");
        assigneeMap.put("assignee3","赵财务");
        // 启动流程实例
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("myEvection1",assigneeMap);
        // 输出
        System.out.println("流程定义id：" + instance.getProcessDefinitionId());
        System.out.println("流程实例id：" + instance.getId());
        System.out.println("当前活动的id：" + instance.getActivityId());
    }

}
