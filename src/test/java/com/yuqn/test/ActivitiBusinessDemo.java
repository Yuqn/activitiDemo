package com.yuqn.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

/**
 * @author: yuqn
 * @Date: 2024/5/11 1:19
 * @description:
 * 第三部分：添加业务key、实例挂起和执行
 * @version: 1.0
 */
public class ActivitiBusinessDemo {

    /**
     * @author: yuqn
     * @Date: 2024/5/10 16:09
     * @description:
     * 添加业务key到activiti表
     * @param: null
     * @return: null
     */
    @Test
    public void addBusinessKey(){
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取runtimeservice
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 启动流程的过程中，添加businesskey
        // 第一个参数：流程定义的key
        // 第二个参数：业务表id
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("myEvection", "1002");
        // 输出
        System.out.println("instance.getBusinessKey() = " + instance.getBusinessKey());
    }

    /**
     * @author: yuqn
     * @Date: 2024/5/10 16:50
     * @description:
     * 全部流程实例的挂起和激活
     * suspend暂停
     * @param: null
     * @return: null
     */
    @Test
    public void suspendAllProcessInstance(){
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取repositoryservice
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3、查询流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myEvection")
                .singleResult();
        // 4、获取读取流程定义的实力是否都是挂起状态
        boolean suspended = processDefinition.isSuspended();
        // 5、获取流程定义的id
        String definitionId = processDefinition.getId();
        if(suspended){
            /*
            * 6、如果是挂起状态，改为激活状态
            * 参数一：流程定义id
            * 参数二：是否激活
            * 参数三：激活时间
            * */
            repositoryService.activateProcessDefinitionById(definitionId,true,null);
            System.out.println("流程定义id：" + definitionId + "已激活");
        }else{
            /*
             * 7、如果是激活状态，改为挂起状态
             * 参数一：流程定义id
             * 参数二：是否暂停
             * 参数三：暂停时间
             * */
            repositoryService.suspendProcessDefinitionById(definitionId,true,null);
            System.out.println("流程定义id：" + definitionId + "已挂起");
        }
    }

    /**
     * @author: yuqn
     * @Date: 2024/5/10 16:50
     * @description:
     * 单个流程实例的挂起和激活
     * suspend暂停
     * @param: null
     * @return: null
     */
    @Test
    public void suspendSingleProcessInstance(){
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、runtimeservice
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3、通过runtimeservice获取流程实例对象
        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId("30001")
                .singleResult();
        // 4、得到当前流程实例的暂停状态
        boolean suspended = instance.isSuspended();
        // 5、获取流程实例id
        String instanceId = instance.getId();
        // 6、如果已经暂停，执行激活操作
        if(suspended){
            runtimeService.activateProcessInstanceById(instanceId);
            System.out.println("流程实例id：" + instanceId + "已经激活");
        }else{
            // 7、如果已经激活，执行暂停操作
            runtimeService.suspendProcessInstanceById(instanceId);
            System.out.println("流程实例id：" + instanceId + "已经暂停");
        }

    }



}
