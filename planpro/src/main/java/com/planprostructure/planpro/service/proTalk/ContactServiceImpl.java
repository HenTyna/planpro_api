package com.planprostructure.planpro.service.proTalk;

import com.planprostructure.planpro.domain.proTalk.*;
import com.planprostructure.planpro.domain.users.UserRepository;
import com.planprostructure.planpro.domain.users.Users;
import com.planprostructure.planpro.enums.ContactStatus;
import com.planprostructure.planpro.enums.Status;
import com.planprostructure.planpro.helper.AuthHelper;
import com.planprostructure.planpro.payload.weTalk.ContactRequest;
import com.planprostructure.planpro.payload.weTalk.ContactResponse;
import com.planprostructure.planpro.payload.weTalk.IGetMyContacts;
import com.planprostructure.planpro.payload.weTalk.MyContactResponse;
import com.planprostructure.planpro.properties.FileInfoConfig;
import com.planprostructure.planpro.utils.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final FileInfoConfig fileInfoConfig;

    @Override
    public List<ContactResponse> getUserContacts() throws Throwable {
        Long currentUserId = AuthHelper.getUserId();
        Users currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("Current user not found"));

        // Get all accepted contacts where current user is either user or contactUser
        List<Contact> contactsAsUser = contactRepository.findByUserAndStatus(currentUserId, ContactStatus.ACCEPTED);
        List<Contact> contactsAsContactUser = contactRepository.findByContactUserAndStatus(currentUserId, ContactStatus.ACCEPTED);

        // Combine both lists
        List<Contact> allContacts = Stream.concat(contactsAsUser.stream(), contactsAsContactUser.stream())
                .collect(Collectors.toList());

        return allContacts.stream()
                .map(contact -> {
                    // Determine the other user in the relationship
                    Users otherUser = contact.getUser().getId().equals(currentUserId)
                            ? contact.getContactUser()
                            : contact.getUser();
                    return ContactResponse.fromContact(contact, otherUser);
                })
                .toList();
    }

    @Override
    public List<ContactResponse> getPendingContactsRequest() throws Throwable {
        Long currentUserId = AuthHelper.getUserId();
        Users currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("Current user not found"));

        // Get pending requests where current user is the recipient
        return contactRepository.findByContactUserAndStatus(currentUserId, ContactStatus.PENDING)
                .stream()
                .map(contact -> ContactResponse.fromContact(contact, contact.getUser())) // sender is the other user
                .toList();
    }

    @Override
    @Transactional
    public void addContact(ContactRequest request) throws Throwable {
        Long currentUserId = AuthHelper.getUserId();
        Users currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("Current user not found"));

        Users contactUser = userRepository.findById(request.getContactUserId())
                .orElseThrow(() -> new IllegalArgumentException("Contact user not found with id: " + request.getContactUserId()));

        if (currentUserId.equals(contactUser.getId())) {
            throw new IllegalArgumentException("Cannot add yourself as a contact");
        }

        if (!contactRepository.existsByUserAndContactUser(currentUserId, contactUser.getId())) {
            // Check if reverse contact exists
            contactRepository.findByUserAndContactUser(contactUser, currentUser)
                    .ifPresentOrElse(
                            existingContact -> {
                                if (existingContact.getStatus() == ContactStatus.PENDING) {
                                    existingContact.setStatus(ContactStatus.ACCEPTED);
                                    contactRepository.save(existingContact);
                                }
                            },
                            () -> {
                                Contact newContact = Contact.builder()
                                        .user(currentUser)
                                        .contactUser(contactUser)
                                        .status(ContactStatus.PENDING)
                                        .contactStatus(Status.NORMAL)
                                        .build();
                                contactRepository.save(newContact);
                            }
                    );
        } else {
            throw new IllegalArgumentException("Contact request already exists");
        }
    }

    @Override
    @Transactional
    public ContactResponse acceptContactRequest(Long contactId) throws Throwable {
        Long currentUserId = AuthHelper.getUserId();
        Users currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("Current user not found"));

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found with id: " + contactId));

        if (!contact.getContactUser().getId().equals(currentUserId)) {
            throw new IllegalArgumentException("You are not authorized to accept this contact request");
        }

        if (contact.getStatus() != ContactStatus.PENDING) {
            throw new IllegalArgumentException("Contact request is not pending");
        }

        contact.setStatus(ContactStatus.ACCEPTED);
        contactRepository.save(contact);

        // Create the reverse contact if it doesn't exist
        if (!contactRepository.existsByUserAndContactUser(currentUserId, contact.getUser().getId())) {
            Contact reverseContact = Contact.builder()
                    .user(currentUser)
                    .contactUser(contact.getUser())
                    .status(ContactStatus.ACCEPTED)
                    .contactStatus(Status.NORMAL)
                    .build();
            contactRepository.save(reverseContact);
        }

        return ContactResponse.fromContact(contact, currentUser);
    }

    @Override
    public ContactResponse rejectContactRequest(Long contactId) throws Throwable {
        Long currentUserId = AuthHelper.getUserId();
        Users currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("Current user not found"));

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found with id: " + contactId));

        if (!contact.getContactUser().getId().equals(currentUserId)) {
            throw new IllegalArgumentException("You are not authorized to reject this contact request");
        }

        if (contact.getStatus() != ContactStatus.PENDING) {
            throw new IllegalArgumentException("Contact request is not pending");
        }

        contact.setContactStatus(Status.DISABLE); // Assuming '0' means DISABLE
        contactRepository.save(contact);
        return ContactResponse.fromContact(contact, currentUser);
    }

    @Override
    public List<MyContactResponse> getMyContact() {
        Long currentUserId = AuthHelper.getUserId();

        // Get contacts with conversation and last message in single query
        List<IGetMyContacts> results = contactRepository.findContactsWithConversationInfo(currentUserId);

        return results.stream()
                .map(result -> MyContactResponse.builder()
                        .userId(result.getUserId())
                        .conversationId(result.getConversationId())
                        .avatarUrl(ImageUtil.getImageUrl(fileInfoConfig.getBaseUrl(), result.getAvatarUrl()))
                        .username(result.getUsername())
                        .messageDate(result.getMessageDate())
                        .lastMessage(result.getLastMessage())
                        .lastMessageTime(result.getLastMessageTime())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
