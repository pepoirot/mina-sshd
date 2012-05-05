package org.apache.sshd.util;

import org.apache.sshd.common.PtyMode;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.Signal;
import org.apache.sshd.server.SignalListener;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;

public class BogusEnvironment implements Environment {

    public Map<String, String> getEnv() {
        return Collections.emptyMap();
    }

    public Map<PtyMode, Integer> getPtyModes() {
        return Collections.emptyMap();
    }

    public void addSignalListener(SignalListener listener, Signal... signal) {
    }

    public void addSignalListener(SignalListener listener, EnumSet<Signal> signals) {
    }

    public void addSignalListener(SignalListener listener) {
    }

    public void removeSignalListener(SignalListener listener) {
    }

}
