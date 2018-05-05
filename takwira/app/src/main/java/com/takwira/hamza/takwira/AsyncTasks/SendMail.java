package com.takwira.hamza.takwira.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.takwira.hamza.takwira.mail_manager.mail_background_sender.MailSender;

import javax.activation.DataHandler;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by hamza on 16/08/17.
 */

public class SendMail extends AsyncTask<Void, Void , Void> {


    String subject;
    String body;
    String sender;
    String recipients;
    Session session;
    Context context;

    public SendMail(Context context, String subject, String body, String sender, String recipients, Session session) {
        this.subject = subject;
        this.body = body;
        this.sender = sender;
        this.recipients = recipients;
        this.session = session;
        this.context = context;
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            MimeMessage message = new MimeMessage(session);
            DataHandler handler = new DataHandler(new MailSender.ByteArrayDataSource(body.getBytes(), "text/plain"));
            message.setSender(new InternetAddress(sender));
            message.setSubject(subject);
            message.setDataHandler(handler);
            if (recipients.indexOf(',') > 0)
                message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(recipients));
            else
                message.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(recipients));
            Transport.send(message);
            Toast.makeText(context, "Les cordonnées ont été bien envoyés pour verivification.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.i("echec", e.toString());
            Toast.makeText(context, "Echec d'envoi des cordonnées pour verivification.", Toast.LENGTH_LONG).show();

        }
        return null;
    }


    protected void onPostExecute(Void feed) {

    }
}


