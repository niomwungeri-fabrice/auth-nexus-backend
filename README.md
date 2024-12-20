# Auth Nexus

A modern authentication and authorization service built with Spring Boot and AWS CDK infrastructure as code.

## 🏗️ Architecture

This project consists of two main components:

1. **Java Spring Boot Application** (`/code`)
   - RESTful API for authentication and authorization
   - JWT token-based authentication
   - Global exception handling
   - Swagger/OpenAPI documentation

2. **AWS Infrastructure** (root directory)
   - Infrastructure as Code using AWS CDK
   - TypeScript implementation
   - Automated deployment pipeline

## 🚀 Getting Started

### Prerequisites

- Node.js (v18+)
- Java 23
- Maven
- AWS CLI configured
- Docker (optional)

### Local Development

1. **Setup Infrastructure**
   ```bash
   # Install AWS CDK dependencies
   npm install
   
   # Bootstrap AWS CDK (first-time only)
   cdk bootstrap
   
   # Deploy infrastructure
   cdk deploy
   ```

2. **Run the Application**
   ```bash
   # Navigate to the application directory
   cd code
   
   # Build the application
   mvn clean install
   
   # Run locally
   mvn spring-boot:run
   ```

3. **Access the API**
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - API Base URL: `http://localhost:8080/api/v1`

### Docker Support

To run the application using Docker:

```bash
# Install AWS CDK dependencies
npm install

# Bootstrap AWS CDK (first-time only)
cdk bootstrap

# Deploy infrastructure
cdk deploy
```

2. **Run the Application**
   ```bash
   # Navigate to the application directory
   cd code
   
   # Build the application
   mvn clean install
   
   # Run locally
   mvn spring-boot:run
   ```

3. **Access the API**
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - API Base URL: `http://localhost:8080/api/v1`

### Docker Support

To run the application using Docker:

```bash
# Build the Docker image
docker build -t auth-nexus .

# Run the container
docker run -p 8080:8080 auth-nexus
```

## 📚 Documentation

Detailed documentation can be found in the following locations:
- API Documentation: `/docs/api.md`
- Infrastructure Setup: `/docs/infrastructure.md`
- Security Guidelines: `/docs/security.md`

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request
