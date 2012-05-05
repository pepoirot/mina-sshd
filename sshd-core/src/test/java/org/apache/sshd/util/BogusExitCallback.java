package org.apache.sshd.util;

import org.apache.sshd.server.ExitCallback;

public class BogusExitCallback implements ExitCallback {

    public void onExit(int exitValue) {
        onExit(exitValue, null);
    }

    public void onExit(int exitValue, String exitMessage) {
    }

}
