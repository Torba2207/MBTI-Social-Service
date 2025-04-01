FROM node:22-alpine AS build
WORKDIR /Frontend
COPY m8bti-web-service/package.json m8bti-web-service/package-lock.json ./
RUN npm install
COPY m8bti-web-service ./
RUN npm run build

CMD ["npm", "run", "start"]
EXPOSE 8081
