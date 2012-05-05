package org.apache.sshd.server.shell;

import org.apache.sshd.util.BogusEnvironment;
import org.apache.sshd.util.BogusExitCallback;
import org.apache.sshd.util.BogusInvertedShell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;

public class InvertedShellWrapperTest {

    private ExecutorService executor;

    @Before
    public void setUp() throws Exception {
        executor = Executors.newSingleThreadExecutor(new ThreadFactory() {
            public Thread newThread(Runnable r) {
                final Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            }
        });
    }

    @After
    public void tearDown() throws Exception {
        if (executor != null) {
            executor.shutdown();
            executor = null;
        }
    }

    @Test
    public void testStreamsAreFlushedBeforeClosing() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        ByteArrayInputStream in = new ByteArrayInputStream("in".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream(50);
        ByteArrayOutputStream err = new ByteArrayOutputStream();

        BogusInvertedShell shell = newShell();
        shell.setAlive(false);

        InvertedShellWrapper wrapper = newShellWrapper(shell, in, out, err, latch);
        wrapper.start(new BogusEnvironment());

        // check the streams were flushed by the IO pumps before exiting
        latch.await(30, TimeUnit.SECONDS);
        assertEquals("in", shell.getInputStream().toString());
        assertEquals("out", out.toString());
        assertEquals("err", err.toString());
    }

    private BogusInvertedShell newShell() {
        ByteArrayOutputStream in = new ByteArrayOutputStream(50);
        ByteArrayInputStream out = new ByteArrayInputStream("out".getBytes());
        ByteArrayInputStream err = new ByteArrayInputStream("err".getBytes());
        return new BogusInvertedShell(in, out, err);
    }

    private InvertedShellWrapper newShellWrapper(BogusInvertedShell shell, ByteArrayInputStream in,
                                                 ByteArrayOutputStream out, ByteArrayOutputStream err,
                                                 final CountDownLatch latch) {
        InvertedShellWrapper wrapper = new InvertedShellWrapper(shell, executor, 50);
        wrapper.setInputStream(in);
        wrapper.setOutputStream(out);
        wrapper.setErrorStream(err);
        wrapper.setExitCallback(new BogusExitCallback() {
            @Override
            public void onExit(int exitValue, String exitMessage) {
                latch.countDown();
            }
        });
        return wrapper;
    }

}
