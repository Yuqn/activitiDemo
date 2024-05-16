package com.yuqn.test;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * @author: yuqn
 * @Date: 2024/4/28 23:57
 * @description:
 * 第二部分：测试流程部署
 * @version: 1.0
 */
public class ActivitiDemo {
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
                .name("出差信息")
                .addClasspathResource("bpmn/evection.bpmn20.xml")
                .addClasspathResource("bpmn/evection.png")
                .deploy();
        // 4、输出部署信息
        System.out.println("流程部署id=" + deploy.getId());
        System.out.println("流程部署名字=" + deploy.getName());
    }
    /**
     * @author: yuqn
     * @Date: 2024/4/29 23:52
     * @description:
     * 通过zip压缩包部署
     * @param: null
     * @return: null
     */
    @Test
    public void deployProcessByZip(){
        // 1、创建processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取RepositoryServoie
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3、流程部署
        // 读取资源包文件，构成inputStream
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("bpmn/evection.zip");
        // 用inputStream构成zipinputstream
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        // 使用压缩包流程进行流程的部署
        Deployment deploy = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();
        System.out.println("流程部署id= " + deploy.getId());
        System.out.println("流程部署名称= " + deploy.getName());
    }

    /**
     * @author: yuqn
     * @Date: 2024/4/29 17:22
     * @description:
     * 启动流程实例
     * act_hi_actinst 流程实例执行历史
     * act_hi_identitylink 流程的参与用户历史信息
     * act_hi_procinst 流程实例历史信息
     * act_hi_taskinst 流程任务历史信息
     * act_ru_execution 流程正在执行信息
     * act_ru_identitylink 流程的参与用户信息
     * act_ru_task 任务信息
     * @param: null
     * @return: null
     */
    @Test
    public void testStartProcess(){
        // 1、创建processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取runtimeservice
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3、根据流程定义的id启动流程
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("myEvection");
        // 4、输出内容
        System.out.println("流程定义id：" + instance.getProcessDefinitionId());
        System.out.println("流程实例id：" + instance.getId());
        System.out.println("当前活动的id：" + instance.getActivityId());
    }

    /**
     * @author: yuqn
     * @Date: 2024/4/29 18:18
     * @description:
     * 查询个人待执行任务
     * @param: null
     * @return: null
     */
    @Test
    public void testFindPersonalTaskList(){
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取taskService
        TaskService taskService = processEngine.getTaskService();
        // 3、根据 流程key 和 任务负责人 查询任务
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("myEvection")
                .taskAssignee("张三")
                .list();
        // 4、输出
        for (Task task : taskList) {
            System.out.println("流程实例id=" + task.getProcessInstanceId());
            System.out.println("任务id=" + task.getId());
            System.out.println("任务负责人=" + task.getAssignee());
            System.out.println("任务名称=" + task.getName());
        }
    }

    /**
     * @author: yuqn
     * @Date: 2024/4/29 22:54
     * @description:
     * 完成个人任务
     */
    @Test
    public void completTask(){
        // 1、获取控制流引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取TaskService
        TaskService taskService = processEngine.getTaskService();

        // 3、根据任务id完成个人任务
         taskService.complete("30005");

//        // 3、获取jerry-myevection对应任务
//        Task task = taskService.createTaskQuery()
//                .processDefinitionKey("myEvection")
//                .taskAssignee("jerry")
//                .singleResult();
//        // 完成jerry的任务
//        taskService.complete(task.getId());

//        // 3、获取jack-myevection对应任务
//        Task task = taskService.createTaskQuery()
//                .processDefinitionKey("myEvection")
//                .taskAssignee("jack")
//                .singleResult();
//        // 完成jerry的任务
//        taskService.complete(task.getId());

        // 3、获取rose-myevection对应任务
//        Task task = taskService.createTaskQuery()
//                .processDefinitionKey("myEvection")
//                .taskAssignee("rose")
//                .singleResult();
//        // 完成jerry的任务
//        taskService.complete(task.getId());
    }

    /**
     * @author: yuqn
     * @Date: 2024/5/5 23:41
     * @description:
     * 查询流程定义
     * @param: null
     * @return: null
     */
    @Test
    public void queryProcessDefinition(){
        // 获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取repositoryservice
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 获取processdifinitionquery对象
        ProcessDefinitionQuery definitionQuery = repositoryService.createProcessDefinitionQuery();
        //查询当前所有的流程定义，返回流程定义信息的集合
        // processDefinitionKey(里路程定义的key)
        // orderbyprocessdefinitionversion进行排序
        // desc 倒序
        // list 查询出所有内容
        List<ProcessDefinition> definitionList = definitionQuery.processDefinitionKey("myEvection")
                .orderByProcessDefinitionVersion()
                .desc()
                .list();
        // 输出信息
        for (ProcessDefinition processDefinition : definitionList) {
            System.out.println("流程定义id = " + processDefinition.getId());
            System.out.println("流程定义名称 = " + processDefinition.getName());
            System.out.println("流程定义key = " + processDefinition.getKey());
            System.out.println("流程定义版本 = " + processDefinition.getVersion());
            System.out.println("流程部署的id = " + processDefinition.getDeploymentId());
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
        String deploymentId = "112501";

        // 非级联删除
        //repositoryService.deleteDeployment(deploymentId);

        // 级联删除，流程没有完全完成也能删除
        repositoryService.deleteDeployment(deploymentId,true);
    }

    /**
     * @author: yuqn
     * @Date: 2024/5/9 16:41
     * @description:
     * 下载资源文件
     * 方案一：使用activiti提供的api下载资源文件，保存到文件目录
     * 方案二：自己写代码从数据库中下载文件
     * @param: null
     * @return: null
     */
    @Test
    public void getDeployment() throws IOException {
        // 1、得到引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取api、repositoryservice
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3、获取查询对象 processdefinitionquery，查询流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myEvection")
                .singleResult();
        // 4、通过流程定义信息，获取部署id
        String deploymentId = processDefinition.getDeploymentId();
        // 5、通过repositoryservice，传递部署id参数，读取资源信息（png和bpmn）
        // 5.1、获取png图片的流
        String pngName = processDefinition.getDiagramResourceName();
        InputStream pngInput = repositoryService.getResourceAsStream(deploymentId, pngName);
        // 5.2、获取bpmn的流
        String bpmnName = processDefinition.getResourceName();
        InputStream bpmnInput = repositoryService.getResourceAsStream(deploymentId, bpmnName);
        // 6、构造outputstream流
        File pngFile = new File("E:\\myJavaProject/activityPro/evectionflow01.png");
        File bpmnFile = new File("E:\\myJavaProject/activityPro/evectionflow01.bpmn");
        FileOutputStream pngOutStream = new FileOutputStream(pngFile);
        FileOutputStream bpmnOutStream = new FileOutputStream(bpmnFile);
        // 7、输入流、输出流的转换
        IOUtils.copy(pngInput,pngOutStream);
        IOUtils.copy(bpmnInput,bpmnOutStream);
        // 8、关闭流
        pngOutStream.close();
        bpmnOutStream.close();
        pngInput.close();
        bpmnInput.close();
    }


    /**
     * @author: yuqn
     * @Date: 2024/5/9 17:37
     * @description:
     * 流程历史信息查看
     * @param: null
     * @return: null
     */
    @Test
    public void findHistoryInfo(){
        // 获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取Historyservice
        HistoryService historyService = processEngine.getHistoryService();
        // 获取actinst表的查询对象
        HistoricActivityInstanceQuery instanceQuery = historyService.createHistoricActivityInstanceQuery();
        // 查询actinst表
        instanceQuery.processInstanceId("2501");
        instanceQuery.orderByHistoricActivityInstanceStartTime().asc();
        // 查询所有内容
        List<HistoricActivityInstance> activityInstanceList = instanceQuery.list();
        // 输出
        for (HistoricActivityInstance hi : activityInstanceList) {
            System.out.println(hi.getActivityId());
            System.out.println(hi.getActivityName());
            System.out.println(hi.getProcessDefinitionId());
            System.out.println(hi.getProcessInstanceId());
            System.out.println("<------------------------------->");
        }
    }






}
