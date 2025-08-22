# Railway Deployment Guide for PlanPro

This guide provides step-by-step instructions for deploying your PlanPro application to Railway.

## Prerequisites

1. **Railway Account**: Sign up at [railway.app](https://railway.app)
2. **Git Repository**: Your code should be in a Git repository (GitHub, GitLab, etc.)
3. **Railway CLI** (Optional): Install for local development
   ```bash
   npm install -g @railway/cli
   ```

## Step 1: Create Railway Project

1. **Login to Railway Dashboard**
   - Go to [railway.app](https://railway.app)
   - Sign in with your GitHub/GitLab account

2. **Create New Project**
   - Click "New Project"
   - Select "Deploy from GitHub repo"
   - Choose your PlanPro repository
   - Railway will automatically detect it's a Java/Spring Boot application

## Step 2: Add PostgreSQL Database

1. **Add PostgreSQL Service**
   - In your Railway project dashboard
   - Click "New Service" → "Database" → "PostgreSQL"
   - Railway will create a PostgreSQL database

2. **Get Database Connection Details**
   - Click on the PostgreSQL service
   - Go to "Connect" tab
   - Note down the connection details:
     - Host: `PGHOST`
     - Port: `PGPORT` (usually 5432)
     - Database: `PGDATABASE`
     - Username: `PGUSER`
     - Password: `PGPASSWORD`

## Step 3: Configure Environment Variables

1. **Go to Variables Tab**
   - In your main application service
   - Click "Variables" tab

2. **Add Required Environment Variables**

### Database Configuration
```bash
# Railway automatically provides these from PostgreSQL service
DATABASE_URL=postgresql://username:password@host:port/database
PGHOST=your-railway-postgres-host
PGPORT=5432
PGDATABASE=railway
PGUSER=postgres
PGPASSWORD=your-railway-password

# Alternative format (if DATABASE_URL is not provided)
PROD_DB_HOST=your-railway-postgres-host
PROD_DB_PORT=5432
PROD_DB_NAME=railway
PROD_DB_USERNAME=postgres
PROD_DB_PASSWORD=your-railway-password
```

### Application Configuration
```bash
# Spring Profile
SPRING_PROFILES_ACTIVE=railway

# JWT Configuration
JWT_SECRET=your-super-secret-jwt-key-here

# Password Encryption
PASSWORD_ENCRYPTION_KEY=your-password-encryption-key

# Application URLs
RAILWAY_STATIC_URL=https://your-app-name.railway.app

# Telegram Bot (Optional)
TELEGRAM_BOT_ENABLE=false
TELEGRAM_BOT_USERNAME=your-bot-username
TELEGRAM_BOT_TOKEN=your-bot-token
```

### Security Configuration
```bash
# RSA Keys (if using file-based keys)
# Note: For Railway, consider using environment variables for keys
RSA_PRIVATE_KEY=your-private-key-content
RSA_PUBLIC_KEY=your-public-key-content
```

## Step 4: Configure Build Settings

1. **Build Command**
   ```bash
   ./gradlew build -x test
   ```

2. **Start Command**
   ```bash
   java -jar build/libs/planpro-0.0.1-SNAPSHOT.jar
   ```

3. **Health Check Path**
   ```
   /actuator/health
   ```

## Step 5: Deploy Application

1. **Automatic Deployment**
   - Railway will automatically deploy when you push to your main branch
   - Or manually trigger deployment from the dashboard

2. **Monitor Deployment**
   - Check the "Deployments" tab for build logs
   - Monitor the "Logs" tab for runtime logs

## Step 6: Database Schema Setup

The application will automatically create the database schema using:
- `ddl-auto: update` (for production)
- The PostgreSQL-compatible schema in `schema.sql`

### Manual Schema Setup (if needed)
```sql
-- Connect to your Railway PostgreSQL database
-- The schema.sql file contains all necessary tables
```

## Step 7: Verify Deployment

1. **Check Application Health**
   ```bash
   curl https://your-app-name.railway.app/actuator/health
   ```

2. **Access Swagger Documentation**
   ```
   https://your-app-name.railway.app/swagger-ui.html
   ```

3. **Test API Endpoints**
   ```bash
   # Test user registration
   curl -X POST https://your-app-name.railway.app/api/v1/auth/register \
     -H "Content-Type: application/json" \
     -d '{"username":"testuser","email":"test@example.com","password":"password123"}'
   ```

## Step 8: Custom Domain (Optional)

1. **Add Custom Domain**
   - Go to "Settings" → "Domains"
   - Add your custom domain
   - Configure DNS records as instructed

2. **Update Environment Variables**
   ```bash
   RAILWAY_STATIC_URL=https://your-custom-domain.com
   ```

## Troubleshooting

### Common Issues

1. **Build Failures**
   ```bash
   # Check build logs in Railway dashboard
   # Common issues:
   # - Missing dependencies
   # - Compilation errors
   # - Test failures
   ```

2. **Database Connection Issues**
   ```bash
   # Verify environment variables are set correctly
   # Check DATABASE_URL format
   # Ensure PostgreSQL service is running
   ```

3. **Application Startup Failures**
   ```bash
   # Check application logs
   # Verify all required environment variables
   # Check port configuration
   ```

### Debug Commands

```bash
# Check Railway logs
railway logs

# Check environment variables
railway variables

# Connect to PostgreSQL
railway connect

# Restart service
railway service restart
```

## Environment-Specific Configurations

### Development (Local)
- Profile: `local`
- Database: H2 (in-memory)
- Logging: Debug level
- Swagger: Enabled

### Railway (Production)
- Profile: `railway`
- Database: PostgreSQL
- Logging: Info level
- Swagger: Disabled (or limited)

## Security Considerations

1. **Environment Variables**
   - Never commit secrets to Git
   - Use Railway's secure environment variable storage
   - Rotate secrets regularly

2. **Database Security**
   - Railway provides secure PostgreSQL connections
   - Use strong passwords
   - Enable SSL connections

3. **Application Security**
   - Use strong JWT secrets
   - Enable HTTPS (Railway provides this)
   - Configure CORS properly

## Monitoring and Logs

1. **Railway Dashboard**
   - Real-time logs
   - Performance metrics
   - Error tracking

2. **Application Logs**
   ```bash
   # View logs in Railway dashboard
   # Or use Railway CLI
   railway logs --follow
   ```

3. **Health Checks**
   ```bash
   # Monitor application health
   curl https://your-app-name.railway.app/actuator/health
   ```

## Scaling and Performance

1. **Automatic Scaling**
   - Railway automatically scales based on traffic
   - Configure resource limits in Railway dashboard

2. **Database Optimization**
   - Monitor database performance
   - Add indexes as needed
   - Optimize queries

3. **Caching**
   - Consider adding Redis for caching
   - Implement application-level caching

## Backup and Recovery

1. **Database Backups**
   - Railway provides automatic PostgreSQL backups
   - Configure backup retention in Railway dashboard

2. **Application Backups**
   - Your code is in Git (primary backup)
   - Consider backing up uploaded files

## Cost Optimization

1. **Resource Usage**
   - Monitor resource usage in Railway dashboard
   - Optimize application performance
   - Use appropriate instance sizes

2. **Database Optimization**
   - Optimize queries
   - Use connection pooling effectively
   - Monitor database size

## Next Steps

1. **Set up CI/CD**
   - Configure GitHub Actions for automated testing
   - Set up staging environment

2. **Monitoring**
   - Add application monitoring (e.g., Sentry)
   - Set up alerts for critical issues

3. **Documentation**
   - Update API documentation
   - Create user guides

4. **Testing**
   - Set up automated testing
   - Perform load testing

## Support

- **Railway Documentation**: [docs.railway.app](https://docs.railway.app)
- **Railway Discord**: [discord.gg/railway](https://discord.gg/railway)
- **Spring Boot Documentation**: [spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)

## Quick Reference

### Essential Environment Variables
```bash
SPRING_PROFILES_ACTIVE=railway
DATABASE_URL=postgresql://username:password@host:port/database
JWT_SECRET=your-secret-key
PASSWORD_ENCRYPTION_KEY=your-encryption-key
RAILWAY_STATIC_URL=https://your-app-name.railway.app
```

### Common Commands
```bash
# Deploy to Railway
git push origin main

# Check logs
railway logs

# View variables
railway variables

# Connect to database
railway connect
```

### Health Check URLs
- Application: `https://your-app-name.railway.app/actuator/health`
- Swagger: `https://your-app-name.railway.app/swagger-ui.html`
- API Docs: `https://your-app-name.railway.app/v3/api-docs` 