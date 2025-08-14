# Contact API Documentation

## Overview

The Contact API provides functionality to manage user contacts in the PlanPro application. It includes features for adding, retrieving, deleting contacts, and automatic contact creation after user signup based on phone number matching.

## Features

### 1. Contact Management
- **Add Contact**: Create a new contact for the authenticated user
- **Get Contacts**: Retrieve all contacts for the authenticated user
- **Get Contact**: Retrieve a specific contact by ID
- **Delete Contact**: Remove a contact from the user's contact list
- **Search Contacts**: Find contacts by phone number

### 2. Automatic Contact Creation
- **Post-Signup Integration**: Automatically creates contacts for new users based on existing users with matching phone numbers
- **Bidirectional Contact Creation**: When a new user signs up, contacts are created both ways (new user gets existing users as contacts, and existing users get the new user as a contact)

## API Endpoints

### Base URL
```
/api/contacts
```

### 1. Add Contact
**POST** `/api/contacts`

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "1234567890",
  "photoUrl": "http://example.com/photo.jpg"
}
```

**Response:**
```json
{
  "id": "uuid-string",
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "1234567890",
  "photoUrl": "http://example.com/photo.jpg",
  "createdAt": "2024-01-01T10:00:00Z",
  "updatedAt": "2024-01-01T10:00:00Z"
}
```

### 2. Get All Contacts
**GET** `/api/contacts`

**Response:**
```json
[
  {
    "id": "uuid-string",
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "1234567890",
    "photoUrl": "http://example.com/photo.jpg",
    "createdAt": "2024-01-01T10:00:00Z",
    "updatedAt": "2024-01-01T10:00:00Z"
  }
]
```

### 3. Get Specific Contact
**GET** `/api/contacts/{contactId}`

**Response:**
```json
{
  "id": "uuid-string",
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "1234567890",
  "photoUrl": "http://example.com/photo.jpg",
  "createdAt": "2024-01-01T10:00:00Z",
  "updatedAt": "2024-01-01T10:00:00Z"
}
```

### 4. Delete Contact
**DELETE** `/api/contacts/{contactId}`

**Response:** 200 OK (no content)

### 5. Search Contacts by Phone
**GET** `/api/contacts/search?phone=1234567890`

**Response:**
```json
[
  {
    "id": "uuid-string",
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "1234567890",
    "photoUrl": "http://example.com/photo.jpg",
    "createdAt": "2024-01-01T10:00:00Z",
    "updatedAt": "2024-01-01T10:00:00Z"
  }
]
```

## Database Schema

### Contacts Table
```sql
CREATE TABLE contacts (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(255),
    photo_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## Automatic Contact Creation Flow

### Signup Process Integration

When a new user signs up with a phone number:

1. **User Registration**: User is created in the `tb_user` table
2. **Phone Number Analysis**: System searches for existing users with the same phone number
3. **Contact Creation**: For each matching user:
   - Creates a contact for the new user with the existing user's information
   - Creates a reverse contact for the existing user with the new user's information
4. **Logging**: All contact creation activities are logged for debugging

### Example Scenario

**Existing Users:**
- User A: Phone: "1234567890", Name: "Alice Smith"
- User B: Phone: "1234567890", Name: "Bob Johnson"

**New User Signup:**
- User C: Phone: "1234567890", Name: "Charlie Brown"

**Result:**
- User C gets contacts for User A and User B
- User A gets a contact for User C
- User B gets a contact for User C

## Error Handling

### Common Error Responses

**400 Bad Request:**
- Invalid request data
- Contact with same phone number already exists
- Missing required fields

**401 Unauthorized:**
- User not authenticated
- Invalid authentication token

**404 Not Found:**
- Contact not found
- User not found

**500 Internal Server Error:**
- Database connection issues
- Unexpected server errors

## Security Considerations

1. **Authentication Required**: All endpoints require valid authentication
2. **User Isolation**: Users can only access their own contacts
3. **Input Validation**: All input data is validated before processing
4. **SQL Injection Prevention**: Using parameterized queries
5. **XSS Prevention**: Input sanitization and output encoding

## Testing

The implementation includes comprehensive integration tests covering:

- Contact creation, retrieval, and deletion
- Automatic contact creation after signup
- Error handling scenarios
- Security validation

Run tests with:
```bash
./mvnw test -Dtest=ContactServiceIntegrationTest
```

## Usage Examples

### Frontend Integration

```javascript
// Add a new contact
const addContact = async (contactData) => {
  const response = await fetch('/api/contacts', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(contactData)
  });
  return response.json();
};

// Get all contacts
const getContacts = async () => {
  const response = await fetch('/api/contacts', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  return response.json();
};
```

### Backend Integration

```java
// Inject the service
@Autowired
private ContactService contactService;

// Add contact
AddContactRequest request = AddContactRequest.builder()
    .name("John Doe")
    .email("john@example.com")
    .phone("1234567890")
    .build();
ContactResponse contact = contactService.addContact(request);

// Get contacts
List<ContactResponse> contacts = contactService.getContacts();
```

## Future Enhancements

1. **Contact Groups**: Organize contacts into groups/categories
2. **Contact Import/Export**: Bulk import/export contacts
3. **Contact Sync**: Sync contacts with external services
4. **Contact Sharing**: Share contacts between users
5. **Contact Analytics**: Track contact interaction patterns
6. **Contact Blocking**: Block specific contacts
7. **Contact Favorites**: Mark important contacts as favorites 