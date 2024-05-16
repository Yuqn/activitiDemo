package com.yuqn.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @author: yuqn
 * @Date: 2024/5/11 16:42
 * @description:
 * 监听器
 * @version: 1.0
 */
public class MyTaskListener implements TaskListener {
    /**
     * @author: yuqn
     * @Date: 2024/5/11 16:43
     * @description:
     * 指定负责人
     * @param: delegateTask
     */
    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("*****************");
        System.out.println("delegateTask.getName() = " + delegateTask.getName());
        System.out.println("delegateTask.getEventName() = " + delegateTask.getEventName());
        // 如果当前是 创建申请 并且是 create 事件 ，则设置为张三
        if ("创建申请".equals(delegateTask.getName()) &&
                "start".equals(delegateTask.getEventName())){
            delegateTask.setAssignee("张三");
        }
    }
}
