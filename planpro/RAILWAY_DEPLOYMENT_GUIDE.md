# 🚀 GitLab + Railway Deployment Guide for PlanPro API

## Overview
This guide will help you set up automatic deployment of your PlanPro API from GitLab to Railway using GitLab CI/CD.

## Prerequisites
- GitLab repository with your PlanPro API code
- Railway account
- GitLab CI/CD enabled

## Step 1: Set Up Railway Account

### 1.1 Create Railway Account
1. Go to [railway.app](https://railway.app)
2. Click "Sign Up"
3. Choose "Continue with GitLab"
4. Authorize Railway to access your GitLab account

### 1.2 Create Railway Project
1. In Railway dashboard, click "New Project"
2. Select "Deploy from GitLab repo"
3. Choose your PlanPro API repository
4. Railway will create a project and start the first deployment

### 1.3 Add PostgreSQL Database
1. In your Railway project, click "New"
2. Select "Database" → "PostgreSQL"
3. Railway will create a PostgreSQL instance
4. Note the connection details (Railway provides environment variables automatically)

## Step 2: Configure Railway Environment Variables

### 2.1 Set Production Environment Variables
In your Railway app service, go to "Variables" tab and add:

```bash
SPRING_PROFILES_ACTIVE=production
JWT_SECRET=your-super-secret-jwt-key-here-change-this-in-production
TELEGRAM_BOT_USERNAME=planproapp_bot
TELEGRAM_BOT_TOKEN=7845816657:AAEJxT5tVXW88cDYELYli2ttY5glQsJQq7o
RAILWAY_STATIC_URL=https://your-app-name.railway.app
PORT=9090
```

### 2.2 Railway Auto-Environment Variables
Railway automatically provides these for PostgreSQL:
- `DATABASE_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

## Step 3: Generate Railway Token

### 3.1 Create Railway Token
1. Go to Railway dashboard
2. Click your profile → "Account Settings"
3. Go to "Tokens" tab
4. Click "New Token"
5. Give it a name (e.g., "GitLab CI/CD")
6. Copy the token (you won't see it again)

## Step 4: Configure GitLab CI/CD Variables

### 4.1 Add Railway Token to GitLab
1. Go to your GitLab project
2. Navigate to Settings → CI/CD → Variables
3. Click "Add Variable"
4. Configure:
   - **Key**: `RAILWAY_TOKEN`
   - **Value**: Your Railway token from Step 3
   - **Type**: Variable
   - **Environment scope**: All (default)
   - **Protect variable**: ✅ Yes
   - **Mask variable**: ✅ Yes
5. Click "Add Variable"

### 4.2 Optional: Add Additional Variables
You can also add these as GitLab CI/CD variables if needed:
- `JWT_SECRET`
- `TELEGRAM_BOT_TOKEN`

## Step 5: Understanding the GitLab CI/CD Pipeline

The `.gitlab-ci.yml` file creates a pipeline with these stages:

### 5.1 Test Stage
- Runs unit tests
- Builds the application
- Creates artifacts for deployment

### 5.2 Deploy Stage
- Deploys to Railway using Railway CLI
- Manual deployment (requires approval)
- Separate environments for staging and production

### 5.3 Pipeline Triggers
- **Main branch**: Deploys to production
- **Develop branch**: Deploys to staging
- **Other branches**: Only runs tests

## Step 6: Deploy Your Application

### 6.1 First Deployment
1. Push your code to the `main` branch
2. Go to GitLab → CI/CD → Pipelines
3. You'll see a new pipeline running
4. Wait for the test stage to complete
5. Manually trigger the deployment stage

### 6.2 Manual Deployment Process
1. In GitLab pipeline, click on the deploy job
2. Click "Play" button to start deployment
3. Monitor the deployment logs
4. Check Railway dashboard for deployment status

## Step 7: Monitor and Test

### 7.1 Check Deployment Status
- **GitLab**: CI/CD → Pipelines → View logs
- **Railway**: Dashboard → Your app → Deployments

### 7.2 Test Your API
Your API will be available at:
- **Production**: `https://your-app-name.railway.app`
- **Staging**: `https://your-app-name-staging.railway.app`

### 7.3 Test Endpoints
- Health check: `/actuator/health`
- Swagger docs: `/swagger-ui.html`
- API base: `/api/v1/`

## Step 8: Set Up Custom Domain (Optional)

### 8.1 Add Custom Domain
1. In Railway dashboard, go to your app service
2. Click "Settings" tab
3. Scroll to "Domains" section
4. Click "Generate Domain" or add custom domain

### 8.2 Update Environment Variables
Update `RAILWAY_STATIC_URL` with your new domain.

## Step 9: Troubleshooting

### 9.1 Common Issues

#### Pipeline Fails at Test Stage
- Check if all tests pass locally
- Verify `build.gradle` configuration
- Check GitLab CI/CD logs for specific errors

#### Deployment Fails
- Verify `RAILWAY_TOKEN` is correctly set
- Check Railway service name matches in `.gitlab-ci.yml`
- Ensure Railway project is properly configured

#### Database Connection Issues
- Verify Railway PostgreSQL is running
- Check environment variables in Railway dashboard
- Ensure `DATABASE_URL` is correctly set

### 9.2 Useful Commands

#### Check Railway Status
```bash
railway status
```

#### View Railway Logs
```bash
railway logs
```

#### Manual Railway Deployment
```bash
railway up
```

## Step 10: Production Best Practices

### 10.1 Security
- Use strong JWT secrets
- Enable HTTPS (automatic with Railway)
- Regularly rotate secrets
- Use Railway's secrets management

### 10.2 Monitoring
- Set up Railway alerts
- Monitor application logs
- Track performance metrics
- Set up error reporting

### 10.3 Backup Strategy
- Railway PostgreSQL has automatic backups
- Consider additional backup solutions
- Test restore procedures regularly

## Step 11: Advanced Configuration

### 11.1 Multiple Environments
You can create multiple Railway services for different environments:
- Production: `planpro-api`
- Staging: `planpro-api-staging`
- Development: `planpro-api-dev`

### 11.2 Branch Protection
Set up branch protection rules in GitLab:
1. Go to Settings → Repository → Protected Branches
2. Protect `main` branch
3. Require pipeline to succeed before merge

### 11.3 Auto-deployment (Optional)
To enable automatic deployment, remove `when: manual` from the deploy jobs in `.gitlab-ci.yml`.

## Your Final Setup

- **Repository**: GitLab
- **CI/CD**: GitLab CI/CD
- **Hosting**: Railway
- **Database**: Railway PostgreSQL
- **Deployment**: Manual approval required
- **Environments**: Staging (develop) and Production (main)

## Next Steps

1. **Test the pipeline** by pushing to develop branch
2. **Deploy to production** when ready
3. **Set up monitoring** and alerts
4. **Configure your frontend** to use the new API URL
5. **Set up backup** and disaster recovery procedures

## Support

If you encounter issues:
1. Check GitLab CI/CD logs
2. Check Railway deployment logs
3. Verify environment variables
4. Test locally first
5. Check Railway and GitLab documentation

---

**Happy Deploying! 🚀** 