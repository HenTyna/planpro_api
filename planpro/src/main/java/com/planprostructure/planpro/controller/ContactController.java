package com.planprostructure.planpro.controller;

import com.planprostructure.planpro.payload.contacts.AddContactRequest;
import com.planprostructure.planpro.payload.contacts.ContactResponse;
import com.planprostructure.planpro.service.contacts.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<ContactResponse> addContact(@RequestBody AddContactRequest request) {
        try {
            ContactResponse response = contactService.addContact(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ContactResponse>> getContacts() {
        try {
            List<ContactResponse> contacts = contactService.getContacts();
            return ResponseEntity.ok(contacts);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{contactId}")
    public ResponseEntity<ContactResponse> getContact(@PathVariable String contactId) {
        try {
            ContactResponse contact = contactService.getContact(contactId);
            return ResponseEntity.ok(contact);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{contactId}")
    public ResponseEntity<Void> deleteContact(@PathVariable String contactId) {
        try {
            contactService.deleteContact(contactId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<ContactResponse>> findContactsByPhone(@RequestParam String phone) {
        try {
            List<ContactResponse> contacts = contactService.findContactsByPhone(phone);
            return ResponseEntity.ok(contacts);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 