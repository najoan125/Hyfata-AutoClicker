package com.hyfata.autoclicker;

import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.autoclicker.ui.settings.AutoClickSettingsUI;
import com.hyfata.autoclicker.ui.settings.LanguageUI;
import com.hyfata.autoclicker.ui.settings.PresetUI;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    public static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    public static boolean isStart = false; //자동 클릭 매크로 작동 여부

    private static boolean left, middle, right;
    private static long delay;

    private static void init() {
        delay = Long.parseLong(AutoClickSettingsUI.delay.getValue().toString());
        AutoClickSettingsUI.setAllEnabled(false);
        LanguageUI.setAllEnabled(false);
        PresetUI.setAllEnabled(false);
        left = false;
        middle = false;
        right = false;
        executorService = Executors.newSingleThreadScheduledExecutor();
    }
    public static void start() {
        init();
        isStart = true;

        String mouseButton = Objects.requireNonNull(AutoClickSettingsUI.mouseButtons.getSelectedItem()).toString();
        String delayUnit = Objects.requireNonNull(AutoClickSettingsUI.delayUnits.getSelectedItem()).toString();

        if (Objects.equals(mouseButton, Locale.getMouseLeft()))
            left = true;
        else if (Objects.equals(mouseButton, Locale.getMouseMiddle()))
            middle = true;
        else if (Objects.equals(mouseButton, Locale.getMouseRight()))
            right = true;

        //자동 클릭 매크로 시작
        if (Objects.equals(delayUnit, Locale.getDelayMs())) {
            if (left)
                executorService.scheduleAtFixedRate(AutoClickHandler::startMacroLeft, 0, delay,
                        TimeUnit.MILLISECONDS);
            else if (middle)
                executorService.scheduleAtFixedRate(AutoClickHandler::startMacroMiddle, 0, delay,
                        TimeUnit.MILLISECONDS);
            else if (right)
                executorService.scheduleAtFixedRate(AutoClickHandler::startMacroRight, 0, delay,
                        TimeUnit.MILLISECONDS);
        } else if (Objects.equals(delayUnit, Locale.getDelayMicros())) {
            if (left)
                executorService.scheduleAtFixedRate(AutoClickHandler::startMacroLeft, 0, delay,
                        TimeUnit.MICROSECONDS);
            else if (middle)
                executorService.scheduleAtFixedRate(AutoClickHandler::startMacroMiddle, 0, delay,
                        TimeUnit.MICROSECONDS);
            else if (right)
                executorService.scheduleAtFixedRate(AutoClickHandler::startMacroRight, 0, delay,
                        TimeUnit.MICROSECONDS);
        }
    } // start()

    //자동 클릭 매크로 실행(메서드 반복)
    private static void startMacroLeft() {
        r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    private static void startMacroRight() {
        r.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        r.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    }

    private static void startMacroMiddle() {
        r.mousePress(InputEvent.BUTTON2_DOWN_MASK);
        r.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
    }
}
