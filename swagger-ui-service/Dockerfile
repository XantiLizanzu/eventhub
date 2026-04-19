FROM swaggerapi/swagger-ui:latest

# Copy custom configuration
COPY config.json /usr/share/nginx/html/config.json
COPY swagger-initializer.js /usr/share/nginx/html/swagger-initializer.js

# Override the default config to use our custom config
ENV SWAGGER_JSON=/config.json

EXPOSE 8080
