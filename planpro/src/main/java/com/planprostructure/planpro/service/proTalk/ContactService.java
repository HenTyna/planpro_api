package com.planprostructure.planpro.service.proTalk;

import com.planprostructure.planpro.payload.weTalk.ContactRequest;
import com.planprostructure.planpro.payload.weTalk.ContactResponse;
import com.planprostructure.planpro.payload.weTalk.MyContactResponse;

import java.util.List;
import java.util.Objects;

public interface ContactService {
    List<ContactResponse> getUserContacts() throws Throwable;

    List<ContactResponse> getPendingContactsRequest() throws Throwable;

    void addContact(ContactRequest request) throws Throwable;

    ContactResponse acceptContactRequest(Long contactId) throws Throwable;

    ContactResponse rejectContactRequest(Long contactId) throws Throwable;

    List<MyContactResponse> getMyContact() throws Throwable;
}
