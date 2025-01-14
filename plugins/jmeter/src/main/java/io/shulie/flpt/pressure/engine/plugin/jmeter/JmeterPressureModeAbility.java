/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.flpt.pressure.engine.plugin.jmeter;

import io.shulie.flpt.pressure.engine.api.ability.EnginePressureModeAbility;
import io.shulie.flpt.pressure.engine.api.ability.model.*;
import io.shulie.flpt.pressure.engine.api.enums.PressureTestMode;
import io.shulie.flpt.pressure.engine.api.plugin.PressureContext;
import io.shulie.flpt.pressure.engine.plugin.jmeter.consts.JmeterConstants;
import io.shulie.flpt.pressure.engine.util.StringUtils;
import io.shulie.flpt.pressure.engine.util.TryUtils;

/**
 * jmeter支持的压力模式
 *
 * @author lipeng
 * @date 2021-08-03 2:37 下午
 */
public class JmeterPressureModeAbility implements EnginePressureModeAbility {

    /**
     * 实现此方法可具备并发模式能力
     *
     * @param context
     * @return
     */
    @Override
    public ConcurrencyAbility concurrencyModeAbility(PressureContext context) {
        String continuedTime = context.getDuration() + "";
        Integer continuedTimeI = context.getDuration().intValue();
        String rampUp = context.getRampUp() + "";
        Integer rampUpI = TryUtils.tryOperation(
                () -> rampUp == null || rampUp.isEmpty() ? 0 : Integer.parseInt(rampUp));
        String holdTime;
        String pressureMode = context.getPressureMode();
        if (PressureTestMode.getMode(pressureMode) == PressureTestMode.FIXED) {
            holdTime = continuedTime;
        } else {
            holdTime = TryUtils.tryOperation(() -> StringUtils.removePoint(String.valueOf(continuedTimeI - rampUpI)));
        }
        return ConcurrencyAbility.build(JmeterConstants.CONCURRENCY_THREAD_GROUP_NAME)
                .setExpectThroughput(context.getExpectThroughput())
                .setHoldTime(Long.parseLong(holdTime))
                .setSteps(context.getSteps())
                .setRampUp(context.getRampUp())
                .addExtraAttribute("guiclass",
                        "com.blazemeter.jmeter.threads.concurrency.ConcurrencyThreadGroupGui")
                .addExtraAttribute("testclass",
                        "com.blazemeter.jmeter.threads.concurrency.ConcurrencyThreadGroup")
                .addExtraAttribute("testname",
                        "bzm - Concurrency Thread Group")
                .addExtraAttribute("enabled", "true");
    }

    /**
     * 实现此方法可具备并发模式能力
     *
     * @param context
     * @return
     */
    @Override
    public TPSAbility tpsModeAbility(PressureContext context) {
        String continuedTime = context.getDuration() + "";
        Integer continuedTimeI = context.getDuration().intValue();
        String rampUp = context.getRampUp() + "";
        Integer rampUpI = TryUtils.tryOperation(
                () -> rampUp == null || rampUp.isEmpty() ? 0 : Integer.parseInt(rampUp));
        String holdTime;
        String pressureMode = context.getPressureMode();
        if (PressureTestMode.getMode(pressureMode) == PressureTestMode.FIXED) {
            holdTime = continuedTime;
        } else {
            holdTime = TryUtils.tryOperation(() -> StringUtils.removePoint(String.valueOf(continuedTimeI - rampUpI)));
        }
        return TPSAbility.build(JmeterConstants.TPS_THREAD_GROUP_NAME)
                .setTargetTps(String.valueOf(context.getEnginePressureParams().get("tpsTargetLevel")))
                .setHoldTime(Long.parseLong(holdTime))
                .setSteps(context.getSteps())
                .setRampUp(context.getRampUp())
                .addExtraAttribute("guiclass",
                        "com.blazemeter.jmeter.threads.arrivals.ArrivalsThreadGroupGui")
                .addExtraAttribute("testclass",
                        "com.blazemeter.jmeter.threads.arrivals.ArrivalsThreadGroup")
                .addExtraAttribute("testname",
                        "bzm - Arrivals Thread Group")
                .addExtraAttribute("enabled", "true");
    }

    /**
     * 实现此方法可具备流量调试能力
     *
     * @param context
     * @return
     */
    @Override
    public FlowDebugAbility flowDebugModeAbility(PressureContext context) {
        return FlowDebugAbility.build(JmeterConstants.THREAD_GROUP_NAME)
                .addExtraAttribute("guiclass", "ThreadGroupGui")
                .addExtraAttribute("testclass", "ThreadGroup")
                .addExtraAttribute("testname", "线程组")
                .addExtraAttribute("enabled", "true");
    }

    /**
     * 实现此方法可具备脚本调试能力
     *
     * @param context
     * @return
     */
    @Override
    public TryRunAbility tryRunModeAbility(PressureContext context) {
        return TryRunAbility.build(JmeterConstants.THREAD_GROUP_NAME)
                .addExtraAttribute("guiclass", "ThreadGroupGui")
                .addExtraAttribute("testclass", "ThreadGroup")
                .addExtraAttribute("testname", "线程组")
                .addExtraAttribute("enabled", "true")
                .setExpectThroughput(context.getExpectThroughput())
                .setLoops(context.getLoops());
    }

    /**
     * 实现此方法可具备巡检能力
     *
     * @param context
     * @return
     */
    @Override
    public InspectionAbility inspectionModeAbility(PressureContext context) {
        return InspectionAbility.build(JmeterConstants.THREAD_GROUP_NAME)
                .addExtraAttribute("guiclass", "ThreadGroupGui")
                .addExtraAttribute("testclass", "ThreadGroup")
                .addExtraAttribute("testname", "线程组")
                .addExtraAttribute("enabled", "true")
                .setLoops(context.getLoops());
    }

}
