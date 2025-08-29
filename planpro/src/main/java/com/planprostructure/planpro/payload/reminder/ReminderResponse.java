package com.planprostructure.planpro.payload.reminder;

import com.planprostructure.planpro.enums.RecurrenceType;
import com.planprostructure.planpro.enums.Status;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class ReminderResponse {
    private Long id;
    private Long userId;
    private Long tripId;
    private Long noteId;
    private Long telegramUserId;
    private String title;
    private String description;
    private String dueDate;
    private String dueTime;
    private String category;
    private String priority;
    private Status status;
    private boolean isRecurring;
    private RecurrenceType recurrenceType;
    private boolean isStarred;
    private String createdAt;
    private String lastModified;
    private String tags;
    private String reminderStatus;

    @Builder
    public ReminderResponse(Long id, Long userId, Long tripId, Long noteId, Long telegramUserId, String title, String description, String dueDate, String dueTime, String category, String priority, Status status, boolean isRecurring, RecurrenceType recurrenceType, boolean isStarred, String createdAt, String lastModified, String tags, String reminderStatus) {
        this.id = id;
        this.userId = userId;
        this.tripId = tripId;
        this.noteId = noteId;
        this.telegramUserId = telegramUserId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.category = category;
        this.priority = priority;
        this.status = status;
        this.isRecurring = isRecurring;
        this.recurrenceType = recurrenceType;
        this.isStarred = isStarred;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
        this.tags = tags;
        this.reminderStatus = reminderStatus;
    }
}
