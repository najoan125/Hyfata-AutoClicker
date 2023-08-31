package com.hyfata.autoclicker.utils;

import com.hyfata.autoclicker.AutoClicker;
import com.hyfata.autoclicker.locale.Locale;
import com.hyfata.json.JsonReader;
import com.hyfata.json.exceptions.JsonEmptyException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class UpdateUtil {
    private final String latest;
    private final String desc;
    public UpdateUtil() throws JsonEmptyException, IOException {
        JSONObject jObject = JsonReader.readFromURL(AutoClicker.CHECK_URL);
        latest = jObject.getString("latest-version");
        desc = jObject.getString("description");
    }
    public void showUpdateDialog() throws URISyntaxException, IOException {
        if (latest.equals(AutoClicker.APP_VERSION)) {
            return;
        }

        int answer = JOptionPane.showConfirmDialog(null, Locale.getUpdateDesc()+"\n\n"
                        + Locale.getCurrentVersion() + AutoClicker.APP_VERSION + ", "
                        + Locale.getLatestVersion() + latest
                        + "\n" + Locale.getChangeLog() + desc,
                Locale.getUpdateFound(),
                JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION) {
            update();
        }
    }

    private void update() throws URISyntaxException, IOException {
        if (OSValidator.isWindows()) {
            downloadUpdate(latest);
        }
        else {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(AutoClicker.UPDATE_JAR_URL.replace("%s",latest)));
            } else {
                JOptionPane.showMessageDialog(null, Locale.getUpdateNotSupported(),
                        "Not supported", JOptionPane.INFORMATION_MESSAGE);
            }
            System.exit(0);
        }
    }

    private void downloadUpdate(String version) {
        String home = System.getProperty("user.home");
        String file = home + "/Downloads/" + "Hyfata.AutoClick." + version + ".exe";

        String addr = AutoClicker.UPDATE_EXE_URL.replace("%s",version);
        try {
            URL url = new URL(addr);
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(file);

            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
        } catch (IOException e) {
            AutoClicker.showErrorDialog(e, Locale.getDownloadingUpdateError(),"Error downloading update");
        }

        try {
            Runtime.getRuntime().exec(file);
            System.exit(1);
        } catch (IOException e) {
            AutoClicker.showErrorDialog(e, Locale.getRunningUpdateFileError(),"Error running update file");
        }
    }
}
