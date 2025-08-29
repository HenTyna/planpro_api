package com.planprostructure.planpro.payload.reminder;

import java.util.List;

import com.planprostructure.planpro.enums.RecurrenceType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ReminderRequest {
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
    private String status;
    
    @JsonProperty("recurring")  
    private boolean isRecurring;
    
    private String createdAt;
    private String lastModified;
    private RecurrenceType recurrenceType;
    
    @JsonProperty("starred")  
    private boolean isStarred;
    
    private List<String> tags;
    private String reminderStatus;

    @Builder
    public ReminderRequest(Long userId, Long tripId, Long noteId, Long telegramUserId, String title, String description, String dueDate, String dueTime, String category, String priority, String status, boolean isRecurring, String createdAt, String lastModified, RecurrenceType recurrenceType, boolean isStarred, List<String> tags, String reminderStatus) {
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
        this.createdAt = createdAt;
        this.lastModified = lastModified;
        this.recurrenceType = recurrenceType;
        this.isStarred = isStarred;
        this.tags = tags;
        this.reminderStatus = reminderStatus;
    }
}
