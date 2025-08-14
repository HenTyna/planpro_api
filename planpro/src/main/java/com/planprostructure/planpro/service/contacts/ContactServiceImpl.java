package com.planprostructure.planpro.service.contacts;

import com.planprostructure.planpro.domain.chat.Contacts;
import com.planprostructure.planpro.domain.chatRepo.ContactsRepository;
import com.planprostructure.planpro.domain.users.UserRepository;
import com.planprostructure.planpro.domain.users.Users;
import com.planprostructure.planpro.helper.AuthHelper;
import com.planprostructure.planpro.payload.contacts.AddContactRequest;
import com.planprostructure.planpro.payload.contacts.ContactResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactServiceImpl implements ContactService {

    private final ContactsRepository contactsRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ContactResponse addContact(AddContactRequest request) throws Exception {
        Long currentUserId = AuthHelper.getUserId();
        
        // Check if contact already exists for this user
        if (request.getPhone() != null) {
            Optional<Contacts> existingContact = contactsRepository.findByUserIdAndPhone(currentUserId, request.getPhone());
            if (existingContact.isPresent()) {
                throw new Exception("Contact with this phone number already exists");
            }
        }
        
        Contacts contact = Contacts.builder()
                .id(UUID.randomUUID().toString())
                .userId(currentUserId.toString())
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .photoUrl(request.getPhotoUrl())
                .build();
        
        Contacts savedContact = contactsRepository.save(contact);
        return mapToResponse(savedContact);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactResponse> getContacts() throws Exception {
        Long currentUserId = AuthHelper.getUserId();
        List<Contacts> contacts = contactsRepository.findByUserId(currentUserId);
        return contacts.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ContactResponse getContact(String contactId) throws Exception {
        Long currentUserId = AuthHelper.getUserId();
        Optional<Contacts> contact = contactsRepository.findByUserIdAndContactId(currentUserId, contactId);
        
        if (contact.isEmpty()) {
            throw new Exception("Contact not found");
        }
        
        return mapToResponse(contact.get());
    }

    @Override
    @Transactional
    public void deleteContact(String contactId) throws Exception {
        Long currentUserId = AuthHelper.getUserId();
        Optional<Contacts> contact = contactsRepository.findByUserIdAndContactId(currentUserId, contactId);
        
        if (contact.isEmpty()) {
            throw new Exception("Contact not found");
        }
        
        contactsRepository.delete(contact.get());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactResponse> findContactsByPhone(String phone) throws Exception {
        List<Contacts> contacts = contactsRepository.findByPhone(phone);
        return contacts.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void createContactsForNewUser(String userId, String phoneNumber) throws Exception {
        log.info("Creating contacts for new user: {} with phone: {}", userId, phoneNumber);
        
        // Find all existing users with the same phone number
        List<Users> usersWithSamePhone = userRepository.findByPhoneNumber(phoneNumber);
        
        for (Users existingUser : usersWithSamePhone) {
            // Skip if it's the same user
            // if (existingUser.getId().toString().equals(userId)) {
            //     continue;
            // }
            
            // Create contact for the new user
            Contacts contact = Contacts.builder()
                    .id(UUID.randomUUID().toString())
                    .userId(userId)
                    .name(existingUser.getFirstName() + " " + existingUser.getLastName())
                    .email(existingUser.getEmail())
                    .phone(existingUser.getPhoneNumber())
                    .photoUrl(existingUser.getProfileImageUrl())
                    .build();
            
            contactsRepository.save(contact);
            log.info("Created contact for user {} with existing user {}", userId, existingUser.getId());
        
        }
    }

    private ContactResponse mapToResponse(Contacts contact) {
        return ContactResponse.builder()
                .id(contact.getId())
                .name(contact.getName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .photoUrl(contact.getPhotoUrl())
                .createdAt(contact.getCreatedAt())
                .updatedAt(contact.getUpdatedAt())
                .build();
    }
} 