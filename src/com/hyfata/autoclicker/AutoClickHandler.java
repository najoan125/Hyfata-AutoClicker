package com.hyfata.autoclicker;

import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.ui.settings.autoclick.AutoClickSettingsUI;
import com.hyfata.autoclicker.ui.settings.LanguageUI;
import com.hyfata.autoclicker.ui.settings.preset.PresetUI;
import com.hyfata.autoclicker.utils.SwingUtil;

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
    public static final AtomicInteger clicks = new AtomicInteger(0); //클릭 수
    public static final AtomicInteger limitClicks = new AtomicInteger(0); // limited clicks

    private static void init() {
        limitClicks.set(0);
        AutoClickSettingsUI.getInstance().setAllEnabled(false);
        LanguageUI.getInstance().setAllEnabled(false);
        PresetUI.getInstance().setAllEnabled(false);
        clicked.set(false);
        macroExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    public static void start() {
        init();
        isStart = true;

        long delay = Long.parseLong(AutoClickSettingsUI.getInstance().getDelayUI().getDelay());
        delay *= 500;

        TimeUnit timeUnit = getTimeUnit();
        Runnable runnable = getRunnable();

        if (runnable != null) {
            macroExecutor.scheduleAtFixedRate(runnable, 0, delay, timeUnit);
        } else {
            SwingUtil.showErrorDialog("Error in Mouse button", "Runnable Error");
        }
    } // start()

    private static TimeUnit getTimeUnit() {
        String delayUnit = AutoClickSettingsUI.getInstance().getDelayUI().getSelectedUnit();
        if (Objects.equals(delayUnit, Locale.getDelayMs())) {
            return TimeUnit.MICROSECONDS;
        }
        return TimeUnit.NANOSECONDS;
    }

    private static Runnable getRunnable() {
        String mouseButton = AutoClickSettingsUI.getInstance().getMouseButtonUI().getSelected();
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
            clicks.incrementAndGet();
            limitClicks.incrementAndGet();
        } else {
            r.mouseRelease(button);
            clicked.set(false);
            // TODO: if current click is reached limited click, run stop()
        }
    }
}
