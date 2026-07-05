# Frontend multi-stage Dockerfile (build static assets with Node, serve with nginx)
FROM node:20-alpine AS build
WORKDIR /app

# Install deps
COPY frontend/package.json frontend/package-lock.json ./
RUN npm ci --silent

# Copy source and build
COPY frontend/ ./
RUN npm run build --silent

# Serve with nginx
FROM nginx:stable-alpine
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
