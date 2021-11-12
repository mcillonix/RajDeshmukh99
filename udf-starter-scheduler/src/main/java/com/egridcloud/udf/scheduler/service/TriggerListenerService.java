/**
 * TriggerListenerService.java
 * Created at 2017-06-01
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egridcloud.udf.core.exception.SystemException;
import com.egridcloud.udf.scheduler.ITriggerLog;
import com.egridcloud.udf.scheduler.SchedulerConfig;
import com.egridcloud.udf.scheduler.domain.ScheduledTriggerLog;

/**
 * 描述 : TriggerListenerService
 *
 * @author Administrator
 *
 */
@Service
public class TriggerListenerService {

  /**
   * 描述 : schedulerConfig
   */
  @Autowired
  private SchedulerConfig schedulerConfig;

  /**
   * 描述 : triggerLog
   */
  @Autowired(required = false)
  private ITriggerLog triggerLog;

  /**
   * <p>
   * Description: 保存未正常触发的记录
   * </p>
   * 
   * @param trigger 触发器
   */
  public void saveTriggerMisfired(Trigger trigger) { // 1
    ScheduledTriggerLog tstl = new ScheduledTriggerLog();
    tstl.setLogid(UUID.randomUUID().toString());
    tstl.setScheduledFireTime(null);
    tstl.setFireTime(null);
    tstl.setEndTime(null);
    tstl.setJobRunTime(null);
    tstl.setStatus("misfired");
    tstl.setResult(null);
    tstl.setErrorMsg(null);
    tstl.setTriggerName(trigger.getKey().getName());
    tstl.setTriggerGroup(trigger.getKey().getGroup());
    tstl.setJobName(trigger.getJobKey().getName());
    tstl.setJobGroup(trigger.getJobKey().getGroup());
    tstl.setJobClass(null);
    tstl.setThreadGroupName(Thread.currentThread().getThreadGroup().getName());
    tstl.setThreadId(Long.toString(Thread.currentThread().getId()));
    tstl.setThreadName(Thread.currentThread().getName());
    tstl.setThreadPriority(Long.toString(Thread.currentThread().getPriority()));
    tstl.setScheduledId(null);
    tstl.setScheduledName(null);
    tstl.setCreateDate(new Date());
    if (triggerLog != null) {
      triggerLog.save(tstl);
    }
  }

  /**
   * <p>
   * Description: 准备触发
   * </p>
   * 
   * @param context 执行上下文
   * @throws SchedulerException SchedulerException
   */
  public void saveTriggerFired(JobExecutionContext context) throws SchedulerException {

    // 是否记录执行历史(true记录,false不记录) , 是否详细记录执行历史(true记录,false不记录)
    if (!this.schedulerConfig.getLogFlag() || !this.schedulerConfig.getLogDetailFlag()) {
      return;
    }

    // 获得计划任务实例
    Scheduler s = context.getScheduler();

    // 删除当前ID已经有的记录
    if (triggerLog != null) {
      triggerLog.delete(context.getFireInstanceId());
    }

    // 写入新的记录
    ScheduledTriggerLog tstl = new ScheduledTriggerLog();
    tstl.setLogid(context.getFireInstanceId());
    tstl.setScheduledFireTime(context.getScheduledFireTime());
    tstl.setFireTime(context.getFireTime());
    tstl.setEndTime(null);
    tstl.setJobRunTime(null);
    tstl.setStatus("triggering");
    tstl.setResult(null);
    tstl.setErrorMsg(null);
    tstl.setTriggerName(context.getTrigger().getKey().getName());
    tstl.setTriggerGroup(context.getTrigger().getKey().getGroup());
    tstl.setJobName(context.getJobDetail().getKey().getName());
    tstl.setJobGroup(context.getJobDetail().getKey().getGroup());
    tstl.setJobClass(context.getJobDetail().getJobClass().getName());
    tstl.setThreadGroupName(Thread.currentThread().getThreadGroup().getName());
    tstl.setThreadId(Long.toString(Thread.currentThread().getId()));
    tstl.setThreadName(Thread.currentThread().getName());
    tstl.setThreadPriority(Long.toString(Thread.currentThread().getPriority()));
    tstl.setScheduledId(s.getSchedulerInstanceId());
    tstl.setScheduledName(s.getSchedulerName());
    tstl.setCreateDate(new Date());
    if (triggerLog != null) {
      triggerLog.save(tstl);
    }
  }

  /**
   * <p>
   * Description: 判断是否否决
   * </p>
   * 
   * @param context 执行上下文
   * @return 是否否决
   * @throws SchedulerException SchedulerException
   */
  public boolean saveVetoJobExecution(JobExecutionContext context) throws SchedulerException {

    // 是否否决
    boolean vetoed = false;

    // 是否记录执行历史(true记录,false不记录) , 是否详细记录执行历史(true记录,false不记录)
    if (!this.schedulerConfig.getLogFlag() || !this.schedulerConfig.getLogDetailFlag()) {
      return vetoed;
    }

    // 获得计划任务实例
    Scheduler s = context.getScheduler();

    // 删除当前ID已经有的记录
    if (triggerLog != null) {
      triggerLog.delete(context.getFireInstanceId());
    }

    // 写入新的记录
    ScheduledTriggerLog tstl = new ScheduledTriggerLog();
    tstl.setLogid(context.getFireInstanceId());
    tstl.setScheduledFireTime(context.getScheduledFireTime());
    tstl.setFireTime(context.getFireTime());
    tstl.setEndTime(null);
    tstl.setJobRunTime(null);
    tstl.setStatus("vetoed(" + vetoed + ")");
    tstl.setResult(null);
    tstl.setErrorMsg(null);
    tstl.setTriggerName(context.getTrigger().getKey().getName());
    tstl.setTriggerGroup(context.getTrigger().getKey().getGroup());
    tstl.setJobName(context.getJobDetail().getKey().getName());
    tstl.setJobGroup(context.getJobDetail().getKey().getGroup());
    tstl.setJobClass(context.getJobDetail().getJobClass().getName());
    tstl.setThreadGroupName(Thread.currentThread().getThreadGroup().getName());
    tstl.setThreadId(Long.toString(Thread.currentThread().getId()));
    tstl.setThreadName(Thread.currentThread().getName());
    tstl.setThreadPriority(Long.toString(Thread.currentThread().getPriority()));
    tstl.setScheduledId(s.getSchedulerInstanceId());
    tstl.setScheduledName(s.getSchedulerName());
    tstl.setCreateDate(new Date());
    if (triggerLog != null) {
      triggerLog.save(tstl);
    }
    return vetoed;
  }

  /**
   * <p>
   * Description: 准备执行作业
   * </p>
   * 
   * @param context 上下文
   * @throws SchedulerException SchedulerException
   */
  public void saveJobToBeExecuted(JobExecutionContext context) throws SchedulerException {

    // 是否记录执行历史(true记录,false不记录) , 是否详细记录执行历史(true记录,false不记录)
    if (!this.schedulerConfig.getLogFlag() || !this.schedulerConfig.getLogDetailFlag()) {
      return;
    }

    // 获得计划任务实例
    Scheduler s = context.getScheduler();

    // 删除当前ID已经有的记录
    if (triggerLog != null) {
      triggerLog.delete(context.getFireInstanceId());
    }

    // 写入新的记录
    ScheduledTriggerLog tstl = new ScheduledTriggerLog();
    tstl.setLogid(context.getFireInstanceId());
    tstl.setScheduledFireTime(context.getScheduledFireTime());
    tstl.setFireTime(context.getFireTime());
    tstl.setEndTime(null);
    tstl.setJobRunTime(null);
    tstl.setStatus("toBeExecuted");
    tstl.setResult(null);
    tstl.setErrorMsg(null);
    tstl.setTriggerName(context.getTrigger().getKey().getName());
    tstl.setTriggerGroup(context.getTrigger().getKey().getGroup());
    tstl.setJobName(context.getJobDetail().getKey().getName());
    tstl.setJobGroup(context.getJobDetail().getKey().getGroup());
    tstl.setJobClass(context.getJobDetail().getJobClass().getName());
    tstl.setThreadGroupName(Thread.currentThread().getThreadGroup().getName());
    tstl.setThreadId(Long.toString(Thread.currentThread().getId()));
    tstl.setThreadName(Thread.currentThread().getName());
    tstl.setThreadPriority(Long.toString(Thread.currentThread().getPriority()));
    tstl.setScheduledId(s.getSchedulerInstanceId());
    tstl.setScheduledName(s.getSchedulerName());
    tstl.setCreateDate(new Date());
    if (triggerLog != null) {
      triggerLog.save(tstl);
    }
  }

  /**
   * <p>
   * Description: 作业执行被否决
   * </p>
   * 
   * @param context 上下文
   * @throws SchedulerException SchedulerException
   */
  public void saveJobExecutionVetoed(JobExecutionContext context) throws SchedulerException {

    // 获得计划任务实例
    Scheduler s = context.getScheduler();

    // 删除当前ID已经有的记录
    if (triggerLog != null) {
      triggerLog.delete(context.getFireInstanceId());
    }

    // 写入新的记录
    ScheduledTriggerLog tstl = new ScheduledTriggerLog();
    tstl.setLogid(context.getFireInstanceId());
    tstl.setScheduledFireTime(context.getScheduledFireTime());
    tstl.setFireTime(context.getFireTime());
    tstl.setEndTime(null);
    tstl.setJobRunTime(null);
    tstl.setStatus("executionVetoed");
    tstl.setResult(null);
    tstl.setErrorMsg(null);
    tstl.setTriggerName(context.getTrigger().getKey().getName());
    tstl.setTriggerGroup(context.getTrigger().getKey().getGroup());
    tstl.setJobName(context.getJobDetail().getKey().getName());
    tstl.setJobGroup(context.getJobDetail().getKey().getGroup());
    tstl.setJobClass(context.getJobDetail().getJobClass().getName());
    tstl.setThreadGroupName(Thread.currentThread().getThreadGroup().getName());
    tstl.setThreadId(Long.toString(Thread.currentThread().getId()));
    tstl.setThreadName(Thread.currentThread().getName());
    tstl.setThreadPriority(Long.toString(Thread.currentThread().getPriority()));
    tstl.setScheduledId(s.getSchedulerInstanceId());
    tstl.setScheduledName(s.getSchedulerName());
    tstl.setCreateDate(new Date());
    if (triggerLog != null) {
      triggerLog.save(tstl);
    }
  }

  /**
   * <p>
   * Description: 作业执行完毕
   * </p>
   * 
   * @param context 上下文
   * @param jobException 执行异常
   * @throws SchedulerException 异常信息
   */
  public void saveJobWasExecuted(JobExecutionContext context, JobExecutionException jobException)
      throws SchedulerException {

    // 异常信息
    String exceptionDetail = null;

    // 如果作业异常,则放入信息到result中
    if (null != jobException) {
      Map<String, Object> result = new HashMap<>();
      result.put("status", "error");
      result.put("jobException", jobException);
      context.setResult(result);

      // 获得异常信息
      exceptionDetail = ExceptionUtils.getStackTrace(jobException);
    }

    // 是否记录执行历史(true记录,false不记录) , 是否详细记录执行历史(true记录,false不记录)
    if (!this.schedulerConfig.getLogFlag() || !this.schedulerConfig.getLogDetailFlag()) {
      return;
    }

    // 获得计划任务实例
    Scheduler s = context.getScheduler();

    // 删除当前ID已经有的记录
    if (triggerLog != null) {
      triggerLog.delete(context.getFireInstanceId());
    }

    // 写入新的记录
    ScheduledTriggerLog tstl = new ScheduledTriggerLog();
    tstl.setLogid(context.getFireInstanceId());
    tstl.setScheduledFireTime(context.getScheduledFireTime());
    tstl.setFireTime(context.getFireTime());
    tstl.setEndTime(new Date());
    tstl.setJobRunTime(context.getJobRunTime());
    tstl.setStatus("executed");
    tstl.setResult(null);
    tstl.setErrorMsg(exceptionDetail);
    tstl.setTriggerName(context.getTrigger().getKey().getName());
    tstl.setTriggerGroup(context.getTrigger().getKey().getGroup());
    tstl.setJobName(context.getJobDetail().getKey().getName());
    tstl.setJobGroup(context.getJobDetail().getKey().getGroup());
    tstl.setJobClass(context.getJobDetail().getJobClass().getName());
    tstl.setThreadGroupName(Thread.currentThread().getThreadGroup().getName());
    tstl.setThreadId(Long.toString(Thread.currentThread().getId()));
    tstl.setThreadName(Thread.currentThread().getName());
    tstl.setThreadPriority(Long.toString(Thread.currentThread().getPriority()));
    tstl.setScheduledId(s.getSchedulerInstanceId());
    tstl.setScheduledName(s.getSchedulerName());
    tstl.setCreateDate(new Date());
    if (triggerLog != null) {
      triggerLog.save(tstl);
    }
  }

  /**
   * <p>
   * Description: 触发完成
   * </p>
   * 
   * @param context 上下文
   * @param triggerInstructionCode 状态
   * @throws SystemException 异常
   * @throws SchedulerException SchedulerException
   */
  @SuppressWarnings("unchecked")
  public void saveTriggerComplete(JobExecutionContext context,
      CompletedExecutionInstruction triggerInstructionCode) throws SchedulerException {

    // 状态和异常信息
    String status = "complete";
    String exceptionDetail = null;

    // 获得result
    Object o = context.getResult();
    if (null != o && o instanceof Map) {
      Map<String, Object> result = (Map<String, Object>) o;
      status = result.get("status").toString();
      JobExecutionException jobException = (JobExecutionException) result.get("jobException");

      // 获得异常信息
      exceptionDetail = ExceptionUtils.getStackTrace(jobException);
    }

    // 是否记录执行历史(true记录,false不记录)
    if (!this.schedulerConfig.getLogFlag()) {
      return;
    }

    // 获得计划任务实例
    Scheduler s = context.getScheduler();

    // 删除当前ID已经有的记录
    if (triggerLog != null) {
      triggerLog.delete(context.getFireInstanceId());
    }

    // 写入新的记录
    ScheduledTriggerLog tstl = new ScheduledTriggerLog();
    tstl.setLogid(context.getFireInstanceId());
    tstl.setScheduledFireTime(context.getScheduledFireTime());
    tstl.setFireTime(context.getFireTime());
    tstl.setEndTime(new Date());
    tstl.setJobRunTime(context.getJobRunTime());
    tstl.setStatus(status);
    tstl.setResult(triggerInstructionCode.toString());
    tstl.setErrorMsg(exceptionDetail);
    tstl.setTriggerName(context.getTrigger().getKey().getName());
    tstl.setTriggerGroup(context.getTrigger().getKey().getGroup());
    tstl.setJobName(context.getJobDetail().getKey().getName());
    tstl.setJobGroup(context.getJobDetail().getKey().getGroup());
    tstl.setJobClass(context.getJobDetail().getJobClass().getName());
    tstl.setThreadGroupName(Thread.currentThread().getThreadGroup().getName());
    tstl.setThreadId(Long.toString(Thread.currentThread().getId()));
    tstl.setThreadName(Thread.currentThread().getName());
    tstl.setThreadPriority(Long.toString(Thread.currentThread().getPriority()));
    tstl.setScheduledId(s.getSchedulerInstanceId());
    tstl.setScheduledName(s.getSchedulerName());
    tstl.setCreateDate(new Date());
    if (triggerLog != null) {
      triggerLog.save(tstl);
    }
  }
}
