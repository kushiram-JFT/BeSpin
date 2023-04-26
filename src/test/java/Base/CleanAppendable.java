package Base;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

final class CleanAppendable implements Appendable {
    private static final CharSequence NL = "\n";
    private final Appendable out;

    public CleanAppendable(Appendable out) {
        this.out = out;
    }

    public CleanAppendable append(CharSequence csq) {
        try {
            this.out.append(csq);
            this.tryFlush();
            return this;
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    public CleanAppendable append(CharSequence csq, int start, int end) {
        try {
            this.out.append(csq, start, end);
            this.tryFlush();
            return this;
        } catch (IOException var5) {
            throw new RuntimeException(var5);
        }
    }

    public CleanAppendable append(char c) {
        try {
            this.out.append(c);
            this.tryFlush();
            return this;
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    public CleanAppendable println() {
        return this.append(NL);
    }

    public CleanAppendable println(CharSequence csq) {
        try {
            this.out.append(csq).append(NL);
            this.tryFlush();
            return this;
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    public void close() {
        try {
            this.tryFlush();
            if (this.out instanceof Closeable) {
                ((Closeable)this.out).close();
            }

        } catch (IOException var2) {
            throw new RuntimeException(var2);
        }
    }

    private void tryFlush() {
        if (this.out instanceof Flushable) {
            try {
                ((Flushable)this.out).flush();
            } catch (IOException var2) {
                throw new RuntimeException(var2);
            }
        }
    }
}
