package com.takwira.hamza.takwira.mail_manager.gmail_sender;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

/**
 * Created by hamza on 16/08/17.
 */

public class SendViaGmail {

    public static Intent createEmailIntent(Context appcontext, final String toEmail, final String subject, final String message) {

        Intent sendTo = new Intent(Intent.ACTION_SENDTO);
        String uriText = "mailto:" + Uri.encode(toEmail) +
                "?subject=" + Uri.encode(subject) +
                "&body=" + Uri.encode(message);
        Uri uri = Uri.parse(uriText);
        sendTo.setData(uri);

        List<ResolveInfo> resolveInfos =
                appcontext.getPackageManager().queryIntentActivities(sendTo, 0);

        // Emulators may not like this check...
        if (!resolveInfos.isEmpty())
        {
            return sendTo;
        }

        // Nothing resolves send to, so fallback to send...
        Intent send = new Intent(Intent.ACTION_SEND);

        send.setType("text/plain");
        send.putExtra(Intent.EXTRA_EMAIL,
                new String[] { toEmail });
        send.putExtra(Intent.EXTRA_SUBJECT, subject);
        send.putExtra(Intent.EXTRA_TEXT, message);

        return Intent.createChooser(send,null);
    }
}
