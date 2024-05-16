package com.yuqn.test;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

/**
 * @author: yuqn
 * @Date: 2024/5/12 0:58
 * @description:
 * 第八部分：设置组，测试候选人
 * evection-candidate.bpmn20.xml
 * @version: 1.0
 */
public class TestCandidate {
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
                .name("出差申请流程-Candidate")
                .addClasspathResource("bpmn/evection-candidate.bpmn20.xml")
                .deploy();
        // 4、输出部署信息
        System.out.println("流程部署id=" + deploy.getId());
        System.out.println("流程部署名字=" + deploy.getName());
    }

    /**
     * @author: yuqn
     * @Date: 2024/5/14 22:53
     * @description:
     * 启动流程
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
        String key = "evection-candidate";
        // 启动流程
        runtimeService.startProcessInstanceByKey(key);
    }

    /**
     * @author: yuqn
     * @Date: 2024/5/12 1:04
     * @description:
     * 完成个人任务，因为这里的组任务是设置在第二个审批任务，所以先把第一个审批任务完成
     * @param: null
     * @return: null
     */
    @Test
    public void completTask(){
        // 定义流程key
        String key = "evection-candidate";
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取taskservice
        TaskService taskService = processEngine.getTaskService();
        // 完成任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskAssignee("yuqn-candidate-0")
                .singleResult();
        if(task != null){
            // 根据任务id，完成任务
            taskService.complete(task.getId());
        }
    }

    /**
     * @author: yuqn
     * @Date: 2024/5/14 22:57
     * @description:
     * 查询组任务
     * @param: null
     * @return: null
     */
    @Test
    public void findGroupTaskList(){
        // 获取key
        String key = "evection-candidate";
        // 选择任务候选人，组里面的一个
        String candidateUser = "yuqn-candidate-1.0";
        // 获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取taskService
        TaskService taskService = processEngine.getTaskService();
        // 查询组任务
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskCandidateUser(candidateUser)
                .list();
        for (Task task : taskList) {
            System.out.println("-------------------------------");
            System.out.println("流程实例id = " + task.getProcessInstanceId());
            System.out.println("任务id = " + task.getId());
            System.out.println("任务负责人 = " + task.getAssignee());
        }
    }

    /**
     * @author: yuqn
     * @Date: 2024/5/14 23:44
     * @description:
     * 拾取任务，执行后，指定的人就能变为当前审批任务的负责人
     * @param: null
     * @return: null
     */
    @Test
    public void claimTask(){
        // 获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取taskService
        TaskService taskService = processEngine.getTaskService();
        // 当前任务id，为act_ru_task的id值
        String taskId = "162502";
        // 任务候选人
        String candidateUser = "yuqn-candidate-1.0";
        // 查询任务
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskCandidateUser(candidateUser)
                .singleResult();
        if (task != null){
            // 拾取任务
            taskService.claim(taskId,candidateUser);
            System.out.println( "taskId-" + taskId + "- 用户 -" + candidateUser + " - 任务拾取完成");
        }
    }

    /**
     * @author: yuqn
     * @Date: 2024/5/15 22:14
     * @description:
     * 任务归还
     * @param: null
     * @return: null
     */
    @Test
    public void testAssigneeToGroupTask(){
        // 获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取taskservice
        TaskService taskService = processEngine.getTaskService();
        // 获取当前任务id
        String taskId = "162502";
        // 任务负责人
        String assignee = "yuqn-candidate-1.0";
        // 根据key和负责人查询任务
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskAssignee(assignee)
                .singleResult();
        if(task!=null){
            // 归还任务，就是把负责人设置为空
            taskService.setAssignee(taskId,null);
            System.out.println("taskid-" + taskId + "-归还任务完成");
        }
    }

    /**
     * @author: yuqn
     * @Date: 2024/5/15 22:25
     * @description:
     * 任务交接，切换任务候选人
     * @param: null
     * @return: null
     */
    @Test
    public void testAssigneeToCandidateUser(){
        // 获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取taskservice
        TaskService taskService = processEngine.getTaskService();
        // 当前任务id
        String taskId = "162502";
        // 任务负责人
        String assignee = "yuqn-candidate-1.0";
        // 任务候选人
        String candidateUser = "yuqn-candidate-1.1";
        // 根据key和负责人来查询任务
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskAssignee(assignee)
                .singleResult();
        if(task!=null){
            // 交接，任务负责人由 yuqn-candidate-1.0 改为 yuqn-candidate-1.1
            taskService.setAssignee(taskId,candidateUser);
            System.out.println("taskid-" + taskId + "-交接任务完成");
        }
    }



}
