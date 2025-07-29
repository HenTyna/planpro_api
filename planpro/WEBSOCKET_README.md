# WebSocket Real-Time Chat Implementation

## Overview

This implementation adds real-time WebSocket functionality to the PlanPro chat system, enabling instant message delivery, typing indicators, and live conversation updates.

## Features

### Real-Time Message Broadcasting
- **New Messages**: Instantly delivered to all conversation participants
- **Message Edits**: Real-time updates when messages are edited
- **Message Deletes**: Immediate notification when messages are deleted
- **Typing Indicators**: Show when users are typing in conversations

### WebSocket Endpoints

#### Connection Endpoint
```
WebSocket: ws://localhost:8080/ws
SockJS Fallback: http://localhost:8080/ws
```

#### Message Destinations
- **Topics**: `/topic/conversation/{conversationId}` - Broadcast to all conversation participants
- **User Queues**: `/user/{userId}/queue/messages` - Private messages to specific users
- **Application**: `/app/*` - Client-to-server messages

## Client Implementation

### JavaScript/TypeScript Example

```javascript
// Connect to WebSocket
const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function (frame) {
    console.log('Connected to WebSocket');
    
    // Subscribe to conversation messages
    stompClient.subscribe('/topic/conversation/123', function (message) {
        const event = JSON.parse(message.body);
        handleChatEvent(event);
    });
    
    // Subscribe to user-specific messages
    stompClient.subscribe('/user/queue/messages', function (message) {
        const event = JSON.parse(message.body);
        handleUserEvent(event);
    });
});

// Send typing indicator
function sendTypingIndicator(conversationId, isTyping) {
    stompClient.send("/app/typing", {}, JSON.stringify({
        conversationId: conversationId,
        isTyping: isTyping
    }));
}

// Join conversation
function joinConversation(conversationId) {
    stompClient.send("/app/join-conversation", {}, conversationId);
}

// Handle incoming events
function handleChatEvent(event) {
    switch(event.eventType) {
        case 'NEW_MESSAGE':
            displayNewMessage(event);
            break;
        case 'MESSAGE_EDITED':
            updateMessage(event);
            break;
        case 'MESSAGE_DELETED':
            removeMessage(event.messageId);
            break;
        case 'TYPING':
            showTypingIndicator(event);
            break;
    }
}
```

### React Example

```jsx
import { useEffect, useState } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

function ChatComponent({ conversationId }) {
    const [stompClient, setStompClient] = useState(null);
    const [messages, setMessages] = useState([]);
    const [typingUsers, setTypingUsers] = useState([]);

    useEffect(() => {
        const client = new Client({
            webSocketFactory: () => new SockJS('/ws'),
            onConnect: () => {
                console.log('Connected to WebSocket');
                
                // Subscribe to conversation
                client.subscribe(`/topic/conversation/${conversationId}`, (message) => {
                    const event = JSON.parse(message.body);
                    handleEvent(event);
                });
                
                // Join conversation
                client.publish({
                    destination: '/app/join-conversation',
                    body: conversationId
                });
            }
        });

        client.activate();
        setStompClient(client);

        return () => {
            if (client) {
                client.deactivate();
            }
        };
    }, [conversationId]);

    const handleEvent = (event) => {
        switch(event.eventType) {
            case 'NEW_MESSAGE':
                setMessages(prev => [...prev, event]);
                break;
            case 'TYPING':
                if (event.isTyping) {
                    setTypingUsers(prev => [...prev, event.username]);
                } else {
                    setTypingUsers(prev => prev.filter(user => user !== event.username));
                }
                break;
        }
    };

    const sendTypingIndicator = (isTyping) => {
        if (stompClient) {
            stompClient.publish({
                destination: '/app/typing',
                body: JSON.stringify({
                    conversationId: conversationId,
                    isTyping: isTyping
                })
            });
        }
    };

    return (
        <div>
            {/* Messages */}
            {messages.map(message => (
                <div key={message.messageId}>
                    <strong>{message.senderUsername}:</strong> {message.content}
                </div>
            ))}
            
            {/* Typing indicators */}
            {typingUsers.length > 0 && (
                <div className="typing-indicator">
                    {typingUsers.join(', ')} is typing...
                </div>
            )}
            
            {/* Message input */}
            <input 
                onFocus={() => sendTypingIndicator(true)}
                onBlur={() => sendTypingIndicator(false)}
                placeholder="Type a message..."
            />
        </div>
    );
}
```

## API Endpoints

### WebSocket Information
```
GET /api/wb/v1/websocket/info
```
Returns WebSocket connection details and endpoints.

### Chat Endpoints (Enhanced with WebSocket)
- `POST /api/wb/v1/chat/messages/{conversationId}` - Send message (triggers WebSocket broadcast)
- `PUT /api/wb/v1/chat/messages/{messageId}` - Edit message (triggers WebSocket broadcast)
- `DELETE /api/wb/v1/chat/messages/{messageId}` - Delete message (triggers WebSocket broadcast)

## Event Types

### ChatMessageEvent
```json
{
    "eventType": "NEW_MESSAGE",
    "messageId": 123,
    "conversationId": 456,
    "senderId": 789,
    "senderUsername": "john_doe",
    "senderDisplayName": "John Doe",
    "senderAvatarUrl": "https://example.com/avatar.jpg",
    "content": "Hello, world!",
    "messageType": "TEXT",
    "fileUrl": null,
    "replyToMessageId": null,
    "isEdited": false,
    "createdAt": "2024-01-01T12:00:00",
    "updatedAt": "2024-01-01T12:00:00"
}
```

### MessageEditEvent
```json
{
    "eventType": "MESSAGE_EDITED",
    "messageId": 123,
    "conversationId": 456,
    "content": "Updated message content",
    "updatedAt": "2024-01-01T12:05:00"
}
```

### MessageDeleteEvent
```json
{
    "eventType": "MESSAGE_DELETED",
    "messageId": 123,
    "conversationId": 456
}
```

### TypingEvent
```json
{
    "eventType": "TYPING",
    "conversationId": 456,
    "userId": 789,
    "username": "john_doe",
    "isTyping": true
}
```

## Configuration

### WebSocket Configuration
The WebSocket configuration is handled by `WebSocketConfig.java`:
- Enables STOMP over WebSocket
- Configures message brokers for topics and queues
- Sets up authentication interceptors
- Handles CORS for WebSocket connections

### Security
- WebSocket connections are authenticated through the `WebSocketAuthInterceptor`
- JWT tokens can be validated for secure connections
- User-specific messages are isolated to prevent unauthorized access

## Error Handling

### Client-Side Error Handling
```javascript
stompClient.onStompError = function (frame) {
    console.error('STOMP error:', frame);
    // Handle reconnection logic
};

socket.onerror = function (error) {
    console.error('WebSocket error:', error);
    // Handle connection errors
};
```

### Server-Side Error Handling
- All WebSocket operations are wrapped in try-catch blocks
- Errors are logged for debugging
- Failed broadcasts don't affect the main application flow

## Performance Considerations

### Connection Management
- Clients should implement reconnection logic
- Use heartbeat mechanisms to detect connection issues
- Implement exponential backoff for reconnection attempts

### Message Optimization
- Messages are broadcast only to relevant conversation participants
- Typing indicators have automatic timeout mechanisms
- Large files should be handled separately from text messages

## Testing

### WebSocket Testing with Postman
1. Use Postman's WebSocket feature to connect to `ws://localhost:8080/ws`
2. Send STOMP frames to test message handling
3. Subscribe to topics to receive broadcast messages

### Unit Testing
```java
@SpringBootTest
@AutoConfigureTestDatabase
class WebSocketServiceTest {
    
    @Autowired
    private WebSocketService webSocketService;
    
    @Test
    void testMessageBroadcasting() {
        // Test message broadcasting functionality
    }
}
```

## Troubleshooting

### Common Issues

1. **Connection Refused**
   - Check if WebSocket endpoint is accessible
   - Verify CORS configuration
   - Ensure proper authentication headers

2. **Messages Not Received**
   - Verify subscription to correct topics
   - Check user permissions for conversations
   - Ensure proper message format

3. **Typing Indicators Not Working**
   - Verify typing event format
   - Check conversation ID validity
   - Ensure proper user authentication

### Debug Logging
Enable debug logging for WebSocket operations:
```properties
logging.level.org.springframework.web.socket=DEBUG
logging.level.com.planprostructure.planpro.service.websocket=DEBUG
```

## Future Enhancements

1. **Message Read Receipts**: Track and broadcast message read status
2. **Online Status**: Show user online/offline status
3. **Message Reactions**: Real-time emoji reactions
4. **Voice Messages**: WebRTC integration for voice chat
5. **File Sharing**: Real-time file transfer progress
6. **Message Search**: Real-time search results
7. **Push Notifications**: Mobile push notifications for offline users 