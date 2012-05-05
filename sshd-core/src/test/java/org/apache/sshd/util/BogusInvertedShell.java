package org.apache.sshd.util;

import org.apache.sshd.common.util.IoUtils;
import org.apache.sshd.server.shell.InvertedShell;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;

public class BogusInvertedShell implements InvertedShell {

    private final OutputStream in;
    private final InputStream out;
    private final InputStream err;

    private Map<String, String> env = null;
    private boolean alive = true;

    public BogusInvertedShell(OutputStream in, InputStream out, InputStream err) {
        this.in = in;
        this.out = out;
        this.err = err;
    }

    public void start(Map<String, String> env) throws IOException {
        this.env = Collections.unmodifiableMap(env);
    }

    public OutputStream getInputStream() {
        return in;
    }

    public InputStream getOutputStream() {
        return out;
    }

    public InputStream getErrorStream() {
        return err;
    }

    public boolean isAlive() {
        return alive;
    }

    public int exitValue() {
        return 0;
    }

    public void destroy() {
        IoUtils.closeQuietly(in);
        IoUtils.closeQuietly(out);
        IoUtils.closeQuietly(err);
    }

    public Map<String, String> getEnv() {
        return env;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

}
