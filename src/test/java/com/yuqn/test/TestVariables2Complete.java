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
 * 第七部分：不在启动流程的时候定义流程变量
 * evection-global.bpmn20.xml
 * @version: 1.0
 */
public class TestVariables2Complete {
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
                .name("出差申请流程-variables-complete")
                .addClasspathResource("bpmn/evection-global.bpmn20.xml")
                .deploy();
        // 4、输出部署信息
        System.out.println("流程部署id=" + deploy.getId());
        System.out.println("流程部署名字=" + deploy.getName());
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
        String key = "evection-global";
        // 流程变量的map
        HashMap<String, Object> variables = new HashMap<>();
        // 设定任务负责人
        variables.put("assignee0","yuqn-lobal-complete-0");
        variables.put("assignee1","yuqn-lobal-complete-1");
        variables.put("assignee2","yuqn-lobal-complete-2");
        variables.put("assignee3","yuqn-lobal-complete-3");
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
        String key = "evection-global";

        // 设置流程变量，注：如果当前审批节点完成后，需要分支，则需要设置流程变量了
        Evection evection = new Evection();
        evection.setNum(2d);
        HashMap<String, Object> map = new HashMap<>();
        map.put("evection",evection);

        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取taskservice
        TaskService taskService = processEngine.getTaskService();
        // 完成任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskAssignee("yuqn-lobal-complete-1")
                .singleResult();
        if(task != null){
            // 根据任务id，完成任务，该方法用于没有流程变量的
//            taskService.complete(task.getId());
            // 根据任务id，完成任务，该方法用于有流程变量的
            taskService.complete(task.getId(),map);
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
        String deploymentId = "120001";

        // 非级联删除
        //repositoryService.deleteDeployment(deploymentId);

        // 级联删除，流程没有完全完成也能删除
        repositoryService.deleteDeployment(deploymentId,true);
    }

}
