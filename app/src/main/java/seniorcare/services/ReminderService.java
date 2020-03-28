package seniorcare.services;

import android.content.Context;

import java.util.List;

import seniorcare.models.Reminder;

public class ReminderService {
    private Context context;

    public ReminderService(Context context) {
        this.context = context;
    }

    public long AddSenior(String passcode) {
        // TODO: return the senior user id

        return -1;
    }

    public void SetReminder(Reminder reminder) {
        // TODO: add to online db
    }

    public List<Reminder> GetReminders(long userId) {
        // TODO: get reminders from online db

        return null;
    }
}
