/**
 * JobDetailListener.java
 * Created at 2017-06-01
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.egridcloud.udf.scheduler.service.TriggerListenerService;

/**
 * <p>
 * ClassName: JobListener
 * </p>
 * <p>
 * Description: 作业监听器
 * </p>
 * <p>
 * Author: wangkang
 * </p>
 * <p>
 * Date: 2014年2月8日
 * </p>
 */
public class JobDetailListener implements JobListener {

  /**
   * 描述 : 日志
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(JobDetailListener.class);

  /**
   * 描述 : triggerListenerService
   */
  @Autowired
  private TriggerListenerService triggerListenerService;

  @Override
  public String getName() {
    return "JobDetailListener";
  }

  @Override
  public void jobToBeExecuted(JobExecutionContext context) { // 4
    try {
      this.triggerListenerService.saveJobToBeExecuted(context);
    } catch (SchedulerException e) {
      LOGGER.error("jobToBeExecuted:", e);
    }
  }

  @Override
  public void jobExecutionVetoed(JobExecutionContext context) { // 5
    try {
      this.triggerListenerService.saveJobExecutionVetoed(context);
    } catch (SchedulerException e) {
      LOGGER.error("jobExecutionVetoed:", e);
    }
  }

  @Override
  public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) { // 6
    try {
      this.triggerListenerService.saveJobWasExecuted(context, jobException);
    } catch (SchedulerException e) {
      LOGGER.error("jobWasExecuted:", e);
    }
  }

}