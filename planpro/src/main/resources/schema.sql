-- PlanPro Database Schema
-- This schema defines all tables for the PlanPro travel planning application

-- =====================================================
-- USERS AND AUTHENTICATION
-- =====================================================

-- Users table - Core user information
CREATE TABLE tb_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    telegram_id BIGINT UNIQUE,
    usr_dob VARCHAR(255),
    usr_fn VARCHAR(255),
    usr_ln VARCHAR(255),
    pwd VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    phone VARCHAR(255) UNIQUE NOT NULL,
    sts CHAR(1) DEFAULT '1' NOT NULL, -- StatusUser enum: 1=ACTIVE, 2=INACTIVE, 3=SUSPENDED, 4=DELETED
    reset_token VARCHAR(255),
    reset_token_expiry TIMESTAMP,
    gender VARCHAR(50),
    profile_image_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    change_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_email (email),
    INDEX idx_user_username (username),
    INDEX idx_user_telegram (telegram_id),
    INDEX idx_user_status (sts)
);

-- =====================================================
-- TRIPS AND DESTINATIONS
-- =====================================================

-- Trips table - Main trip information
CREATE TABLE tb_trips (
    trip_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_date VARCHAR(14), -- Format: YYYYMMDDHHMMSS
    end_date VARCHAR(14),   -- Format: YYYYMMDDHHMMSS
    cate VARCHAR(10),       -- CategoryEnums: 1=business, 2=vacation, 3=weekend, 4=family, 5=adventure, 6=roadTrip, 0=other
    sts VARCHAR(10),        -- TripsStatus: 1=PLANNING, 2=BOOKED, 3=INCOMING, 4=IN_PROGRESS, 5=COMPLETED, 6=CANCELLED, 7=HOLD, 0=UNKNOWN
    budget DECIMAL(15,2),
    curr CHAR(10),          -- CurrencyEnum: USD, RIEL, UNKNOWN
    mem TEXT,               -- Travelers (comma-separated)
    accommo TEXT,           -- Accommodation details
    transpo TEXT,           -- Transportation details
    rmk TEXT,               -- Remarks/notes
    img_url VARCHAR(500),
    location VARCHAR(255),
    status CHAR(1) DEFAULT '1', -- Status: 1=NORMAL, 9=DISABLE
    is_cal_event BOOLEAN DEFAULT FALSE,
    is_notify BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    change_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(id) ON DELETE CASCADE,
    INDEX idx_trips_user (user_id),
    INDEX idx_trips_status (status),
    INDEX idx_trips_category (cate),
    INDEX idx_trips_dates (start_date, end_date)
);

-- Destinations table - Trip destinations
CREATE TABLE tb_destinations (
    destination_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id VARCHAR(255),        -- destDate
    trip_id BIGINT NOT NULL,
    destination_name VARCHAR(255),
    days INT,
    activity TEXT,          -- Activities
    status CHAR(1) DEFAULT '1', -- Status: 1=NORMAL, 9=DISABLE
    FOREIGN KEY (trip_id) REFERENCES tb_trips(trip_id) ON DELETE CASCADE,
    INDEX idx_destinations_trip (trip_id),
    INDEX idx_destinations_status (status)
);

-- =====================================================
-- CALENDAR EVENTS
-- =====================================================

-- Calendar table - Calendar events
CREATE TABLE tb_calendar (
    cal_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_title VARCHAR(255) NOT NULL,
    start_date VARCHAR(14) NOT NULL, -- Format: YYYYMMDDHHMMSS
    end_date VARCHAR(14) NOT NULL,   -- Format: YYYYMMDDHHMMSS
    start_time VARCHAR(6) NOT NULL,  -- Format: HHMMSS
    end_time VARCHAR(6) NOT NULL,    -- Format: HHMMSS
    description TEXT,
    location VARCHAR(255),
    trip_id BIGINT,
    user_id BIGINT,
    note_id BIGINT,
    cal_type VARCHAR(10),   -- CalendarEnum: 1=meeting, 2=vacation, 3=workshop, 4=personal, 5=deadline, 6=travel
    status CHAR(1) DEFAULT '1', -- Status: 1=NORMAL, 9=DISABLE
    attendees TEXT,         -- Comma-separated user IDs
    is_notify BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    change_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (trip_id) REFERENCES tb_trips(trip_id) ON DELETE SET NULL,
    FOREIGN KEY (user_id) REFERENCES tb_user(id) ON DELETE CASCADE,
    INDEX idx_calendar_user (user_id),
    INDEX idx_calendar_trip (trip_id),
    INDEX idx_calendar_dates (start_date, end_date),
    INDEX idx_calendar_status (status)
);

-- =====================================================
-- NOTES
-- =====================================================

-- My Notes table - User notes
CREATE TABLE tb_my_notes (
    note_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_at VARCHAR(255) NOT NULL,
    updated_at VARCHAR(255),
    color VARCHAR(50),
    text_color VARCHAR(50),
    is_deleted BOOLEAN DEFAULT FALSE,
    is_calendar_event BOOLEAN DEFAULT FALSE,
    is_notify BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES tb_user(id) ON DELETE CASCADE,
    INDEX idx_notes_user (user_id),
    INDEX idx_notes_deleted (is_deleted),
    INDEX idx_notes_calendar (is_calendar_event)
);

-- =====================================================
-- FILE MANAGEMENT
-- =====================================================

-- Folders table - File organization
CREATE TABLE tb_folder (
    folder_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    telegram_id BIGINT,
    file_id BIGINT,
    created_at TIMESTAMP NOT NULL,
    status CHAR(1) DEFAULT '1', -- Status: 1=NORMAL, 9=DISABLE
    FOREIGN KEY (user_id) REFERENCES tb_user(id) ON DELETE CASCADE,
    INDEX idx_folder_user (user_id),
    INDEX idx_folder_status (status)
);

-- Files table - File storage
CREATE TABLE tb_file (
    file_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    stored_name VARCHAR(255) NOT NULL,
    file_url VARCHAR(500) NOT NULL,
    file_type VARCHAR(50) NOT NULL, -- FileType: Document, Photo, Video, Audio, Voice, Sticker, Animation, Archive
    mime_type VARCHAR(100),
    file_size BIGINT,
    telegram_id BIGINT,
    folder_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    uploaded_at TIMESTAMP NOT NULL,
    width INT,
    height INT,
    thumbnail_path VARCHAR(500),
    status CHAR(1) DEFAULT '1', -- Status: 1=NORMAL, 9=DISABLE
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (folder_id) REFERENCES tb_folder(folder_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES tb_user(id) ON DELETE CASCADE,
    INDEX idx_file_user (user_id),
    INDEX idx_file_folder (folder_id),
    INDEX idx_file_type (file_type),
    INDEX idx_file_status (status)
);

-- =====================================================
-- CHAT SYSTEM
-- =====================================================

-- Conversations table - Chat conversations
CREATE TABLE conversations (
    id VARCHAR(255) PRIMARY KEY,
    type ENUM('DIRECT', 'GROUP') NOT NULL,
    title VARCHAR(255),
    photo_url VARCHAR(500),
    created_by VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_conversations_type (type),
    INDEX idx_conversations_created_by (created_by)
);

-- Messages table - Chat messages
CREATE TABLE messages (
    id VARCHAR(255) PRIMARY KEY,
    conversation_id VARCHAR(255) NOT NULL,
    message_seq BIGINT,
    sender_id VARCHAR(255) NOT NULL,
    text VARCHAR(4000),
    reply_to VARCHAR(255),
    mentions_json TEXT,
    edited BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status ENUM('SENT', 'DELETED') DEFAULT 'SENT',
    FOREIGN KEY (conversation_id) REFERENCES conversations(id) ON DELETE CASCADE,
    INDEX idx_messages_conv_seq (conversation_id, message_seq),
    INDEX idx_messages_conv_created (conversation_id, created_at DESC),
    INDEX idx_messages_sender (sender_id)
);

-- Conversation Participants table
CREATE TABLE conversation_participants (
    conversation_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (conversation_id, user_id),
    FOREIGN KEY (conversation_id) REFERENCES conversations(id) ON DELETE CASCADE
);

-- Message Reactions table
CREATE TABLE message_reactions (
    message_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    reaction VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (message_id, user_id),
    FOREIGN KEY (message_id) REFERENCES messages(id) ON DELETE CASCADE
);

-- Message Receipts table
CREATE TABLE message_receipts (
    message_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    status ENUM('READ', 'DELIVERED') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (message_id, user_id),
    FOREIGN KEY (message_id) REFERENCES messages(id) ON DELETE CASCADE
);

-- Contacts table
CREATE TABLE contacts (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    contact_user_id VARCHAR(255) NOT NULL,
    nickname VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_contacts_user (user_id),
    INDEX idx_contacts_contact (contact_user_id)
);

-- Block table - User blocking
CREATE TABLE blocks (
    blocker_id VARCHAR(255) NOT NULL,
    blocked_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (blocker_id, blocked_id)
);

-- Attachments table - Message attachments
CREATE TABLE attachments (
    id VARCHAR(255) PRIMARY KEY,
    message_id VARCHAR(255) NOT NULL,
    file_url VARCHAR(500) NOT NULL,
    file_type VARCHAR(50),
    file_name VARCHAR(255),
    file_size BIGINT,
    FOREIGN KEY (message_id) REFERENCES messages(id) ON DELETE CASCADE,
    INDEX idx_attachments_message (message_id)
);

-- =====================================================
-- TELEGRAM BOT (if needed)
-- =====================================================

-- Telegram Bot Sessions table (placeholder for future use)
CREATE TABLE telegram_bot_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    telegram_id BIGINT NOT NULL,
    session_data TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(id) ON DELETE CASCADE,
    UNIQUE KEY uk_telegram_session (telegram_id),
    INDEX idx_telegram_bot_user (user_id)
);

-- =====================================================
-- SECURITY (if needed)
-- =====================================================

-- User Sessions table (placeholder for future use)
CREATE TABLE user_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    session_token VARCHAR(255) UNIQUE NOT NULL,
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (user_id) REFERENCES tb_user(id) ON DELETE CASCADE,
    INDEX idx_sessions_user (user_id),
    INDEX idx_sessions_token (session_token),
    INDEX idx_sessions_expires (expires_at)
);

-- =====================================================
-- INDEXES FOR PERFORMANCE
-- =====================================================

-- Additional composite indexes for better query performance
CREATE INDEX idx_trips_user_status ON tb_trips(user_id, status);
CREATE INDEX idx_calendar_user_type ON tb_calendar(user_id, cal_type);
CREATE INDEX idx_notes_user_calendar ON tb_my_notes(user_id, is_calendar_event);
CREATE INDEX idx_files_user_type ON tb_file(user_id, file_type);
CREATE INDEX idx_messages_status_created ON messages(status, created_at);

-- =====================================================
-- COMMENTS AND DOCUMENTATION
-- =====================================================

/*
PlanPro Database Schema Documentation

This schema supports a comprehensive travel planning application with the following features:

1. USER MANAGEMENT:
   - User registration and authentication
   - Telegram integration
   - Role-based access control (USER/ADMIN)
   - User status management

2. TRIP PLANNING:
   - Trip creation and management
   - Destination planning
   - Budget tracking with multiple currencies
   - Trip status tracking (Planning, Booked, In Progress, etc.)
   - Category classification (Business, Vacation, etc.)

3. CALENDAR INTEGRATION:
   - Event scheduling
   - Trip-to-calendar linking
   - Multiple event types (Meeting, Vacation, Workshop, etc.)
   - Notification support

4. NOTE TAKING:
   - Rich text notes
   - Color coding
   - Calendar event integration
   - Soft delete support

5. FILE MANAGEMENT:
   - Hierarchical folder structure
   - Multiple file types (Documents, Photos, Videos, etc.)
   - Telegram file integration
   - Thumbnail support for images

6. CHAT SYSTEM:
   - Direct and group conversations
   - Message threading and reactions
   - File attachments
   - Read receipts
   - Contact management
   - User blocking

7. SECURITY:
   - Session management
   - Password reset functionality
   - User status tracking

ENUM VALUES:
- Status: 1=NORMAL, 9=DISABLE
- StatusUser: 1=ACTIVE, 2=INACTIVE, 3=SUSPENDED, 4=DELETED
- TripsStatus: 1=PLANNING, 2=BOOKED, 3=INCOMING, 4=IN_PROGRESS, 5=COMPLETED, 6=CANCELLED, 7=HOLD, 0=UNKNOWN
- CategoryEnums: 1=business, 2=vacation, 3=weekend, 4=family, 5=adventure, 6=roadTrip, 0=other
- CurrencyEnum: USD, RIEL, UNKNOWN
- CalendarEnum: 1=meeting, 2=vacation, 3=workshop, 4=personal, 5=deadline, 6=travel
- FileType: Document, Photo, Video, Audio, Voice, Sticker, Animation, Archive
- Role: USER, ADMIN
- ConversationType: DIRECT, GROUP
- MessageStatus: SENT, DELETED

DATE FORMATS:
- Standard dates: YYYY-MM-DD HH:MM:SS (TIMESTAMP)
- Compact dates: YYYYMMDDHHMMSS (VARCHAR(14))
- Time only: HHMMSS (VARCHAR(6))
*/
