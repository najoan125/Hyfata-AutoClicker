package com.hyfata.autoclicker;

import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.ui.settings.AutoClickSettingsUI;
import com.hyfata.autoclicker.ui.settings.LanguageUI;
import com.hyfata.autoclicker.ui.settings.preset.PresetUtils;
import com.hyfata.autoclicker.utils.DialogUtil;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
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
    private static ScheduledExecutorService macroExecutor = Executors.newSingleThreadScheduledExecutor();
    private static final AtomicBoolean clicked = new AtomicBoolean(false);
    private static int macroButton;

    public static boolean isStart = false; //자동 클릭 매크로 작동 여부
    public static AtomicInteger clicks = new AtomicInteger(0); //클릭 수

    private static void init() {
        AutoClickSettingsUI.setAllEnabled(false);
        LanguageUI.setAllEnabled(false);
        PresetUtils.setAllEnabled(false);
        clicked.set(false);
        macroExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    public static void start() {
        init();
        isStart = true;

        long delay = Long.parseLong(AutoClickSettingsUI.delay.getValue().toString());
        boolean legacy = true;
        if (delay > 1) {
            delay /= 2L;
            legacy = false;
        }

        TimeUnit timeUnit = getTimeUnit();
        Runnable runnable = getRunnable(legacy);

        if (runnable != null) {
            macroExecutor.scheduleAtFixedRate(runnable, 0, delay, timeUnit);
        } else {
            DialogUtil.showErrorDialog("Error in Mouse button", "Runnable Error");
        }
    } // start()

    private static TimeUnit getTimeUnit() {
        String delayUnit = Objects.requireNonNull(AutoClickSettingsUI.delayUnits.getSelectedItem()).toString();
        if (Objects.equals(delayUnit, Locale.getDelayMs())) {
            return TimeUnit.MILLISECONDS;
        }
        return TimeUnit.MICROSECONDS;
    }

    private static Runnable getRunnable(boolean legacy) {
        String mouseButton = Objects.requireNonNull(AutoClickSettingsUI.mouseButtons.getSelectedItem()).toString();
        boolean left = mouseButton.equals(Locale.getMouseLeft());
        boolean middle = mouseButton.equals(Locale.getMouseMiddle());
        boolean right = mouseButton.equals(Locale.getMouseRight());

        if (left) {
            macroButton = InputEvent.BUTTON1_DOWN_MASK;
        } else if (middle) {
            macroButton = InputEvent.BUTTON2_DOWN_MASK;
        } else if (right) {
            macroButton = InputEvent.BUTTON3_DOWN_MASK;
        } else {
            macroButton = 0;
        }

        if (macroButton != 0) {
            if (legacy) {
                return () -> startLegacyMacro(macroButton);
            }
            return () -> startMacro(macroButton);
        }
        return null;
    }

    public static void stop() {
        macroExecutor.shutdown();
        r.mouseRelease(macroButton);
    }

    //자동 클릭 매크로 실행(메서드 반복)
    private static void startMacro(int button) {
        if (!clicked.get()) {
            r.mousePress(button);
            clicked.set(true);
            int current = clicks.incrementAndGet();
        } else {
            r.mouseRelease(button);
            clicked.set(false);
        }
    }

    private static void startLegacyMacro(int button) {
        r.mousePress(button);
        r.mouseRelease(button);
        int current = clicks.incrementAndGet();
    }
}
