package com.android.internal.telephony;

/**
 * Created by User-10 on 27-Nov-17.
 */

public interface ITelephony {

    boolean endCall();

    void answerRingingCall();

    void silenceRinger();
}
