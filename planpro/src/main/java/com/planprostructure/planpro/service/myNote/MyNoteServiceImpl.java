package com.planprostructure.planpro.service.myNote;

import com.planprostructure.planpro.domain.myNote.MyNotes;
import com.planprostructure.planpro.domain.myNote.MyNotesRepository;
import com.planprostructure.planpro.helper.AuthHelper;
import com.planprostructure.planpro.payload.myNotes.MyNoteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class MyNoteServiceImpl implements MyNoteService{
    private final MyNotesRepository myNotesRepository;

    @Override
    @Transactional
    public void createMyNote(MyNoteRequest myNoteRequest) throws Throwable {

        Long userId = AuthHelper.getUserId();

        if (myNoteRequest.getTitle() == null || myNoteRequest.getContent() == null) {
            throw new IllegalArgumentException("Title and content must not be null");
        }

        MyNotes myNotes = MyNotes.builder()
                .userId(userId)
                .title(myNoteRequest.getTitle())
                .content(myNoteRequest.getContent())
                .createdAt(myNoteRequest.getCreatedAt())
                .updatedAt(myNoteRequest.getUpdatedAt())
                .color(myNoteRequest.getColor())
                .textColor(myNoteRequest.getTextColor())
                .isCalendarEvent(myNoteRequest.isCalendarEvent())
                .isNotify(myNoteRequest.isNotify())
                .isDeleted(false)
                .build();
        myNotesRepository.save(myNotes);

    }

    @Override
    @Transactional
    public void updateMyNote(Long noteId, MyNoteRequest myNoteRequest) throws Throwable {
        var myNote = myNotesRepository.findById(noteId)
                .orElseThrow(() -> new IllegalArgumentException("Note not found with id: " + noteId));
        if (myNoteRequest.getTitle() == null || myNoteRequest.getContent() == null) {
            throw new IllegalArgumentException("Title and content must not be null");
        }

        myNote.setTitle(myNoteRequest.getTitle());
        myNote.setContent(myNoteRequest.getContent());
        myNote.setUpdatedAt(myNoteRequest.getUpdatedAt());
        myNote.setColor(myNoteRequest.getColor());
        myNote.setTextColor(myNoteRequest.getTextColor());
        myNote.setNotify(myNoteRequest.isCalendarEvent());
        myNote.setCalendarEvent(myNoteRequest.isNotify());
        myNote.setDeleted(false);
        myNotesRepository.save(myNote);
    }

    @Override
    @Transactional
    public void deleteMyNote(Long id) throws Throwable {
        var myNote = myNotesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Note not found with id: " + id));
        myNote.setDeleted(true);
        myNotesRepository.save(myNote);
    }

    @Override
    @Transactional(readOnly = true)
    public Object getMyNoteById(Long id) throws Throwable {
        var myNote = myNotesRepository.findByIdAndStatus(id);

        if (myNote == null){
            return Collections.emptyList();
        }
        return MyNoteRequest.builder()
                .id(myNote.getId())
                .title(myNote.getTitle())
                .content(myNote.getContent())
                .createdAt(myNote.getCreatedAt())
                .updatedAt(myNote.getUpdatedAt())
                .color(myNote.getColor())
                .textColor(myNote.getTextColor())
                .isCalendarEvent(myNote.isCalendarEvent())
                .isNotify(myNote.isNotify())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Object getAllMyNotes() throws Throwable {

    Long userId = AuthHelper.getUserId();
    var myNotes = myNotesRepository.findAllByUserIdAndStatus(userId);
    if (myNotes == null){
        return Collections.emptyList();
    }

    return myNotes.stream().map(note -> MyNoteRequest.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .color(note.getColor())
                .textColor(note.getTextColor())
                .isCalendarEvent(note.isCalendarEvent())
                .isNotify(note.isNotify())
                .build()).toList();
    }
}
