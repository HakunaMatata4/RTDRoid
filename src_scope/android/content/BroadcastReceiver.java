package android.content;

import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public abstract class BroadcastReceiver {
    // private PendingResult mPendingResult;
    private boolean mDebugUnregister;

    public abstract void onReceive(Context context, Intent intent);

    public static class PendingResult {
        /** @hide */
        public static final int TYPE_COMPONENT = 0;
        /** @hide */
        public static final int TYPE_REGISTERED = 1;
        /** @hide */
        public static final int TYPE_UNREGISTERED = 2;

        final int mType;
        final boolean mOrderedHint;
        final boolean mInitialStickyHint;
        final IBinder mToken;

        int mResultCode;
        String mResultData;
        Bundle mResultExtras;
        boolean mAbortBroadcast;
        boolean mFinished;

        /** @hide */
        public PendingResult(int resultCode, String resultData,
                Bundle resultExtras,
                int type, boolean ordered, boolean sticky, IBinder token) {
            mResultCode = resultCode;
            mResultData = resultData;
            mResultExtras = resultExtras;
            mType = type;
            mOrderedHint = ordered;
            mInitialStickyHint = sticky;
            mToken = token;
        }

        /**
         * Version of {@link BroadcastReceiver#setResultCode(int)
         * BroadcastReceiver.setResultCode(int)} for asynchronous broadcast
         * handling.
         */
        public final void setResultCode(int code) {
            checkSynchronousHint();
            mResultCode = code;
        }

        /**
         * Version of {@link BroadcastReceiver#getResultCode()
         * BroadcastReceiver.getResultCode()} for asynchronous broadcast
         * handling.
         */
        public final int getResultCode() {
            return mResultCode;
        }

        /**
         * Version of {@link BroadcastReceiver#setResultData(String)
         * BroadcastReceiver.setResultData(String)} for asynchronous broadcast
         * handling.
         */
        public final void setResultData(String data) {
            checkSynchronousHint();
            mResultData = data;
        }

        /**
         * Version of {@link BroadcastReceiver#getResultData()
         * BroadcastReceiver.getResultData()} for asynchronous broadcast
         * handling.
         */
        public final String getResultData() {
            return mResultData;
        }

        /**
         * Version of {@link BroadcastReceiver#setResultExtras(Bundle)
         * BroadcastReceiver.setResultExtras(Bundle)} for asynchronous broadcast
         * handling.
         */
        public final void setResultExtras(Bundle extras) {
            checkSynchronousHint();
            mResultExtras = extras;
        }

        /**
         * Version of {@link BroadcastReceiver#getResultExtras(boolean)
         * BroadcastReceiver.getResultExtras(boolean)} for asynchronous
         * broadcast handling.
         */
        public final Bundle getResultExtras(boolean makeMap) {
            Bundle e = mResultExtras;
            if (!makeMap)
                return e;
            if (e == null)
                mResultExtras = e = new Bundle();
            return e;
        }

        /**
         * Version of {@link BroadcastReceiver#setResult(int, String, Bundle)
         * BroadcastReceiver.setResult(int, String, Bundle)} for asynchronous
         * broadcast handling.
         */
        public final void setResult(int code, String data, Bundle extras) {
            checkSynchronousHint();
            mResultCode = code;
            mResultData = data;
            mResultExtras = extras;
        }

        /**
         * Version of {@link BroadcastReceiver#getAbortBroadcast()
         * BroadcastReceiver.getAbortBroadcast()} for asynchronous broadcast
         * handling.
         */
        public final boolean getAbortBroadcast() {
            return mAbortBroadcast;
        }

        /**
         * Version of {@link BroadcastReceiver#abortBroadcast()
         * BroadcastReceiver.abortBroadcast()} for asynchronous broadcast
         * handling.
         */
        public final void abortBroadcast() {
            checkSynchronousHint();
            mAbortBroadcast = true;
        }

        /**
         * Version of {@link BroadcastReceiver#clearAbortBroadcast()
         * BroadcastReceiver.clearAbortBroadcast()} for asynchronous broadcast
         * handling.
         */
        public final void clearAbortBroadcast() {
            mAbortBroadcast = false;
        }

        /**
         * Finish the broadcast. The current result will be sent and the next
         * broadcast will proceed.
         */
        public final void finish() {
            // if (mType == TYPE_COMPONENT) {
            // final IActivityManager mgr = ActivityManagerNative.getDefault();
            // if (QueuedWork.hasPendingWork()) {
            // // If this is a broadcast component, we need to make sure any
            // // queued work is complete before telling AM we are done, so
            // // we don't have our process killed before that. We now know
            // // there is pending work; put another piece of work at the end
            // // of the list to finish the broadcast, so we don't block this
            // // thread (which may be the main thread) to have it finished.
            // //
            // // Note that we don't need to use QueuedWork.add() with the
            // // runnable, since we know the AM is waiting for us until the
            // // executor gets to it.
            // QueuedWork.singleThreadExecutor().execute( new Runnable() {
            // @Override public void run() {
            // if (ActivityThread.DEBUG_BROADCAST) Slog.i(ActivityThread.TAG,
            // "Finishing broadcast after work to component " + mToken);
            // sendFinished(mgr);
            // }
            // });
            // } else {
            // if (ActivityThread.DEBUG_BROADCAST) Slog.i(ActivityThread.TAG,
            // "Finishing broadcast to component " + mToken);
            // sendFinished(mgr);
            // }
            // } else if (mOrderedHint && mType != TYPE_UNREGISTERED) {
            // if (ActivityThread.DEBUG_BROADCAST) Slog.i(ActivityThread.TAG,
            // "Finishing broadcast to " + mToken);
            // final IActivityManager mgr = ActivityManagerNative.getDefault();
            // sendFinished(mgr);
            // }
        }

        // public void sendFinished(IActivityManager am) {
        // synchronized (this) {
        // if (mFinished) {
        // throw new IllegalStateException("Broadcast already finished");
        // }
        // mFinished = true;
        //
        // try {
        // if (mResultExtras != null) {
        // mResultExtras.setAllowFds(false);
        // }
        // if (mOrderedHint) {
        // am.finishReceiver(mToken, mResultCode, mResultData, mResultExtras,
        // mAbortBroadcast);
        // } else {
        // // This broadcast was sent to a component; it is not ordered,
        // // but we still need to tell the activity manager we are done.
        // am.finishReceiver(mToken, 0, null, null, false);
        // }
        // } catch (RemoteException ex) {
        // }
        // }
        // }

        void checkSynchronousHint() {
            // Note that we don't assert when receiving the initial sticky
            // value,
            // since that may have come from an ordered broadcast. We'll catch
            // them later when the real broadcast happens again.
            if (mOrderedHint || mInitialStickyHint) {
                return;
            }
            RuntimeException e = new RuntimeException(
                    "BroadcastReceiver trying to return result during a non-ordered broadcast");
            e.fillInStackTrace();
            Log.e("BroadcastReceiver", e.getMessage(), e);
        }
    }
}
