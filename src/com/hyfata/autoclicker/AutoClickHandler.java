package com.hyfata.autoclicker;

import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.ui.settings.AutoClickSettingsUI;
import com.hyfata.autoclicker.ui.settings.LanguageUI;
import com.hyfata.autoclicker.ui.settings.preset.PresetUtils;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AutoClickHandler {
    //Robot 객체 생성
    private static final Robot r;
    static {
        try {
            r = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }
    //짧은 시간 동안 딜레이를 주기 위한 라이브러리
    public static ScheduledExecutorService macroExecutor = Executors.newSingleThreadScheduledExecutor();
    public static boolean isStart = false; //자동 클릭 매크로 작동 여부
    public static AtomicInteger clicks = new AtomicInteger(0); //클릭 수

    private static long delay;

    private static void init() {
        delay = Long.parseLong(AutoClickSettingsUI.delay.getValue().toString());
        AutoClickSettingsUI.setAllEnabled(false);
        LanguageUI.setAllEnabled(false);
        PresetUtils.setAllEnabled(false);
        macroExecutor = Executors.newSingleThreadScheduledExecutor();
    }
    public static void start() {
        init();
        isStart = true;

        TimeUnit timeUnit = getTimeUnit();
        Runnable runnable = getRunnable();

        if (runnable != null) {
            macroExecutor.scheduleAtFixedRate(runnable, 0, delay, timeUnit);
        }
    } // start()

    private static TimeUnit getTimeUnit() {
        String delayUnit = Objects.requireNonNull(AutoClickSettingsUI.delayUnits.getSelectedItem()).toString();
        if (Objects.equals(delayUnit, Locale.getDelayMs())) {
            return TimeUnit.MILLISECONDS;
        }
        return TimeUnit.MICROSECONDS;
    }

    private static Runnable getRunnable() {
        String mouseButton = Objects.requireNonNull(AutoClickSettingsUI.mouseButtons.getSelectedItem()).toString();
        boolean left = mouseButton.equals(Locale.getMouseLeft());
        boolean middle = mouseButton.equals(Locale.getMouseMiddle());
        boolean right = mouseButton.equals(Locale.getMouseRight());

        if (left) {
            return AutoClickHandler::startMacroLeft;
        } else if (middle) {
            return AutoClickHandler::startMacroMiddle;
        } else if (right) {
            return AutoClickHandler::startMacroRight;
        }
        return null;
    }

    //자동 클릭 매크로 실행(메서드 반복)
    private static void startMacroLeft() {
        r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        int current = clicks.incrementAndGet();
    }

    private static void startMacroRight() {
        r.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        r.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        int current = clicks.incrementAndGet();
    }

    private static void startMacroMiddle() {
        r.mousePress(InputEvent.BUTTON2_DOWN_MASK);
        r.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
        int current = clicks.incrementAndGet();
    }
}
