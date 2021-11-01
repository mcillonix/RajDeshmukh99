/**
 * ITriggerLog.java
 * Created at 2017-06-01
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler;

import com.egridcloud.udf.scheduler.domain.ScheduledTriggerLog;

/**
 * 描述 : ITriggerLog
 *
 * @author Administrator
 *
 */
public interface ITriggerLog {

  /**
   * 描述 : 记录日志
   *
   * @param log log
   */
  public void save(ScheduledTriggerLog log);

  /**
   * 描述 : 删除记录
   *
   * @param fireInstanceId fireInstanceId
   */
  public void delete(String fireInstanceId);

}