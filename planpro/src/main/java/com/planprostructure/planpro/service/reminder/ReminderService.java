package com.planprostructure.planpro.service.reminder;

import org.springframework.data.domain.Pageable;
import com.planprostructure.planpro.payload.reminder.ReminderRequest;

public interface ReminderService {
   void createReminder(ReminderRequest reminderRequest) throws Throwable;

   void updateReminder(Long id, ReminderRequest reminderRequest) throws Throwable;

   void deleteReminder(Long id) throws Throwable;

   Object getListReminder(Pageable pageRequest) throws Throwable;
}
