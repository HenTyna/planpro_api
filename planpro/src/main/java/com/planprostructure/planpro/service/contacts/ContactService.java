package com.planprostructure.planpro.service.contacts;

import com.planprostructure.planpro.payload.contacts.AddContactRequest;
import com.planprostructure.planpro.payload.contacts.ContactResponse;

import java.util.List;

public interface ContactService {
    
    /**
     * Add a new contact for the current user
     */
    ContactResponse addContact(AddContactRequest request) throws Exception;
    
    /**
     * Get all contacts for the current user
     */
    List<ContactResponse> getContacts() throws Exception;
    
    /**
     * Get a specific contact by ID
     */
    ContactResponse getContact(String contactId) throws Exception;
    
    /**
     * Delete a contact
     */
    void deleteContact(String contactId) throws Exception;
    
    /**
     * Find contacts by phone number (for automatic contact creation after signup)
     */
    List<ContactResponse> findContactsByPhone(String phone) throws Exception;
    
    /**
     * Create contacts automatically for a new user based on existing users with matching phone numbers
     */
    void createContactsForNewUser(String userId, String phoneNumber) throws Exception;
} 