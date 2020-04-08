package com.example.seniorcare.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.SimpleFormatter;

import com.example.seniorcare.db.sqlite.local.SqLiteLocalDbContext;
import com.example.seniorcare.models.Reminder;
import com.example.seniorcare.models.User;
import com.example.seniorcare.models.UserPassCode;

public class ReminderService {
    private SqLiteLocalDbContext<Reminder> reminderSqLiteLocalDbContext;
    private AlarmManager alarmManager;
    private Context context;

    public ReminderService(
        SqLiteLocalDbContext<Reminder> reminderSqLiteLocalDbContext,
        AlarmManager alarmManager,
        Context context
    ) {
        this.reminderSqLiteLocalDbContext = reminderSqLiteLocalDbContext;
        this.alarmManager = alarmManager;
        this.context = context;
    }

    public void addReminder(Reminder reminder) {
        reminderSqLiteLocalDbContext.insertData(reminder);
    }

    public List<Reminder> getReminders(User user) {
        Reminder reminderSearch = new Reminder();
        reminderSearch.setUserId(user.getUserId());
        return reminderSqLiteLocalDbContext.searchData(reminderSearch);
    }

    public Reminder getReminderById(String ReminderId) {
        return reminderSqLiteLocalDbContext.getByPrimarykey(ReminderId);
    }

    public void removeReminder(Reminder reminder) {
        reminderSqLiteLocalDbContext.deleteData(reminder);
    }

    public void scheduleReminders(User user) {
        List<Reminder> reminders = getReminders(user);

        if (reminders == null || reminders.size() <= 0) {
            return;
        }

        int maxIntentRequestCode = reminders.
                stream()
                .max((reminder1, reminder2) ->
                    reminder1.getIntentRequestCode() - reminder2.getIntentRequestCode())
                .get()
                .getIntentRequestCode();
        maxIntentRequestCode = Math.max(maxIntentRequestCode, 0);

        Calendar calendar = null;
        for (Reminder reminder : reminders) {
            if (reminder.getIntentRequestCode() >= 0) {
                continue;
            }

            if (reminder.getIntentRequestCode() < 0) {
                reminder.setIntentRequestCode(maxIntentRequestCode + 1);
                maxIntentRequestCode += 1;
            }

            calendar = Calendar.getInstance();
            calendar.setTime(reminder.getTime());

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), getPendingIntent(reminder));
            reminderSqLiteLocalDbContext.updateData(reminder);
        }

    }

    public String getReminderDisplayTime(Reminder reminder) {
        return (new SimpleDateFormat("HH:mm")).format(reminder.getTime());
    }

    public PendingIntent getPendingIntent(Reminder reminder) {
        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                reminder.getIntentRequestCode(),
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        return pendingIntent;
    }
}
