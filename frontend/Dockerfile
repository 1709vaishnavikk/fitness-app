FROM node:18 as build

WORKDIR /app
COPY . .
RUN npm install
RUN npm run build

# Final nginx stage
FROM nginx:alpine

# Copy the built frontend to nginx's HTML directory
COPY --from=build /app/dist /usr/share/nginx/html

# Copy custom nginx config (this must exist in the same folder as this Dockerfile)
COPY nginx.conf /etc/nginx/conf.d/default.conf
