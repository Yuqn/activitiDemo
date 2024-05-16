package com.yuqn.test;

import com.yuqn.pojo.Evection;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;

/**
 * @author: yuqn
 * @Date: 2024/5/12 0:58
 * @description:
 * 第十部分：并行网关
 * 所有分支都要走一遍，才能进入下一个任务，该情况下各分支如果设置条件（如流程变量），并不会生效
 * evection-candidate.bpmn20.xml
 * @version: 1.0
 */
public class ActivitiGatewayParallal {
    /**
     * @author: yuqn
     * @Date: 2024/5/16 18:01
     * @description:
     * 部署流程
     * @param: null
     * @return: null
     */
    @Test
    public void testDeployment(){
        // 创建processengine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取repositoryservice实例
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 使用repositoryservice部署
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/evection-parallel.bpmn20.xml")
                .name("出差申请流程-并行网关")
                .deploy();
        // 输出部署信息
        System.out.println("流程部署id" + deployment.getId());
        System.out.println("流程部署名称" + deployment.getName());
    }

    /**
     * @author: yuqn
     * @Date: 2024/5/12 1:03
     * @description:
     * 启动流程  启动的时候设置流程变量
     * 设置流程变量num
     * 设置任务负责人
     * @param: null
     * @return: null
     */
    @Test
    public void testStartProcess(){
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取runtimeservice
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 流程定义的key
        String key = "evection-parallel";
        // 流程变量的map
        HashMap<String, Object> variables = new HashMap<>();
        // 设置流程变量
        Evection evection = new Evection();
        // 设置出差日期
        evection.setNum(4d);
        // 把流程变量的pojo放入map
        variables.put("evection",evection);
        // 启动流程
        runtimeService.startProcessInstanceByKey(key,variables);
    }

    /**
     * @author: yuqn
     * @Date: 2024/5/12 1:04
     * @description:
     * 完成个人任务
     * @param: null
     * @return: null
     */
    @Test
    public void completTask(){
        // 定义流程key
        String key = "evection-parallel";
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取taskservice
        TaskService taskService = processEngine.getTaskService();
        // 完成任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskAssignee("yuqn-parallel-03")
                .singleResult();
        if(task != null){
            // 根据任务id，完成任务
            taskService.complete(task.getId());
        }
    }

}
