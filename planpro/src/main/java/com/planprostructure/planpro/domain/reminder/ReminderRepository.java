package com.planprostructure.planpro.domain.reminder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.planprostructure.planpro.payload.reminder.IGetReminder;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    @Query(value="""
           SELECT  
                rm.id AS reminder_id, 
                rm.title, 
                rm.description, 
                rm.due_date, 
                rm.due_time, 
                rm.recurrence_type, 
                rm.category, 
                rm.created_at, 
                rm.last_modified, 
                rm.status, 
                rm.is_starred, 
                rm.priority, 
                rm.tags,
                rm.user_id AS user_id,
                rm.trip_id,
                rm.note_id,
                rm.telegram_user_id,
                rm.is_recurring
                FROM 
                tb_reminder rm
                WHERE 
                rm.user_id = ?1 AND rm.status = '1'
                ORDER BY 
                rm.created_at DESC, rm.last_modified DESC
            
            """, nativeQuery = true)
    Page<IGetReminder> findAllReminder(Long userId, Pageable pageable);
}
