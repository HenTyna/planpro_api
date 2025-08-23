# Railway Deployment Guide for PlanPro API

## Overview
This guide helps you deploy the PlanPro API to Railway successfully.

## Prerequisites
- Railway account
- PostgreSQL database (Railway provides this)
- Environment variables configured

## Environment Variables Required

### Database Configuration
```bash
DATABASE_URL=postgresql://username:password@host:port/database?sslmode=require
# OR individual variables:
PROD_DB_HOST=your-db-host
PROD_DB_PORT=5432
PROD_DB_NAME=your-db-name
PROD_DB_USERNAME=your-db-username
PROD_DB_PASSWORD=your-db-password
```

### JWT Configuration
```bash
JWT_SECRET=your-super-secret-jwt-key-here
```

### Password Encryption
```bash
PASSWORD_ENCRYPTION_KEY=your-password-encryption-key
```

### Optional: Telegram Bot
```bash
TELEGRAM_BOT_ENABLE=false
TELEGRAM_BOT_USERNAME=your-bot-username
TELEGRAM_BOT_TOKEN=your-bot-token
```

## Deployment Steps

### 1. Connect to Railway
```bash
# Install Railway CLI
npm install -g @railway/cli

# Login to Railway
railway login

# Link your project
railway link
```

### 2. Set Environment Variables
```bash
# Set database URL
railway variables set DATABASE_URL="your-database-url"

# Set JWT secret
railway variables set JWT_SECRET="your-jwt-secret"

# Set password encryption key
railway variables set PASSWORD_ENCRYPTION_KEY="your-encryption-key"
```

### 3. Deploy
```bash
# Deploy to Railway
railway up
```

## Troubleshooting

### Common Issues

#### 1. Memory Issues
**Symptoms**: Application crashes with OutOfMemoryError
**Solution**: 
- The JVM settings are already optimized for Railway's memory constraints
- Check Railway logs for memory usage
- Consider upgrading to a higher tier if needed

#### 2. Database Connection Issues
**Symptoms**: Connection timeout or SSL errors
**Solution**:
- Ensure DATABASE_URL includes `sslmode=require`
- Check if database is accessible from Railway
- Verify database credentials

#### 3. Port Issues
**Symptoms**: Application doesn't start or health checks fail
**Solution**:
- Railway automatically sets the PORT environment variable
- Application is configured to use `$PORT` or fallback to 8080

#### 4. RSA Key Issues
**Symptoms**: Application fails to start due to missing certificate files
**Solution**:
- RSA keys are commented out in railway.yml
- Application will use HMAC-based JWT (SecurityConfigFallback)
- This is the intended behavior for Railway deployment

### Health Check
The application exposes health endpoints at:
- `/actuator/health` - Basic health check
- `/actuator/health/db` - Database health check

### Logs
View logs in Railway dashboard or use CLI:
```bash
railway logs
```

## Performance Optimizations

### JVM Settings
- MaxRAMPercentage: 40% (conservative for Railway)
- MinRAMPercentage: 20%
- G1GC garbage collector
- String deduplication enabled
- Optimized string concatenation

### Database Connection Pool
- Maximum pool size: 2 (conservative)
- Connection timeout: 20 seconds
- Idle timeout: 5 minutes
- Max lifetime: 10 minutes

### Spring Boot Optimizations
- Lazy initialization enabled
- Second-level cache disabled
- Query cache disabled
- Reduced batch sizes

## Monitoring

### Health Checks
Railway will automatically monitor:
- Application startup
- Health endpoint responses
- Container restarts

### Metrics
Monitor these in Railway dashboard:
- Memory usage
- CPU usage
- Network traffic
- Response times

## Rollback
If deployment fails:
```bash
# List deployments
railway deployments

# Rollback to previous version
railway rollback
```

## Support
If you encounter issues:
1. Check Railway logs first
2. Verify environment variables
3. Test locally with railway profile
4. Check this guide for common solutions 