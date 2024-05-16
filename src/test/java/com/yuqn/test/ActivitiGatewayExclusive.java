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
 * 第九部分：排他网关
 * 排他网关如果两个条件都满足，则会跑id小的分支
 * 如果都不满足，则会报错
 * evection-candidate.bpmn20.xml
 * @version: 1.0
 */
public class ActivitiGatewayExclusive {
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
                .addClasspathResource("bpmn/evection-exclusive.bpmn20.xml")
                .name("出差申请流程-排他网关")
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
        String key = "evection-exclusive";
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
        String key = "evection-exclusive";
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取taskservice
        TaskService taskService = processEngine.getTaskService();
        // 完成任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskAssignee("yuqn-exclusive-04")
                .singleResult();
        if(task != null){
            // 根据任务id，完成任务
            taskService.complete(task.getId());
        }
    }

    /**
     * @author: yuqn
     * @Date: 2024/5/5 23:41
     * @description:
     * 删除流程部署信息
     * @param: null
     * @return: null
     * 当前流程如果没有全部完成，想要删除的话需要使用特殊方式，原理就是 级联删除
     */
    @Test
    public void deleteDeployMent(){
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 通过引擎来获取 RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 通过部署id删除流程部署信息
        String deploymentId = "172501";

        // 非级联删除
        //repositoryService.deleteDeployment(deploymentId);

        // 级联删除，流程没有完全完成也能删除
        repositoryService.deleteDeployment(deploymentId,true);
    }
}
