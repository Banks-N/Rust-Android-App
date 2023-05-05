// Automatically generated by flapigen
package com.example.rustapplication.lib;
import androidx.annotation.NonNull;

public final class Exercise {

    public Exercise(@NonNull String name, @NonNull String initial_date) {
        mNativeObj = init(name, initial_date);
    }
    private static native long init(@NonNull String name, @NonNull String initial_date);

    public final void addEntry(@NonNull Entry entry) {
        long a0 = entry.mNativeObj;
        entry.mNativeObj = 0;

        do_addEntry(mNativeObj, a0);

        JNIReachabilityFence.reachabilityFence1(entry);
    }
    private static native void do_addEntry(long self, long entry);

    public final @NonNull String getLogString() {
        String ret = do_getLogString(mNativeObj);

        return ret;
    }
    private static native @NonNull String do_getLogString(long self);

    public synchronized void delete() {
        if (mNativeObj != 0) {
            do_delete(mNativeObj);
            mNativeObj = 0;
       }
    }
    @Override
    protected void finalize() throws Throwable {
        try {
            delete();
        }
        finally {
             super.finalize();
        }
    }
    private static native void do_delete(long me);
    /*package*/ Exercise(InternalPointerMarker marker, long ptr) {
        assert marker == InternalPointerMarker.RAW_PTR;
        this.mNativeObj = ptr;
    }
    /*package*/ long mNativeObj;
}