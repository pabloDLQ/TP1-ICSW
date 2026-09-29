# Etapa 1: Build con Node.js
FROM node:20-alpine AS build

WORKDIR /app

# Copiar archivos de dependencias
COPY package*.json ./

# Instalar dependencias
RUN npm ci

# Copiar código fuente
COPY . .

# Build de producción de Next.js
RUN npm run build

# Etapa 2: Runtime con servidor Node ligero
FROM node:20-alpine AS runner

WORKDIR /app

# Configurar como producción
ENV NODE_ENV=production

# Copiar archivos necesarios para ejecutar
COPY --from=build /app/package*.json ./
COPY --from=build /app/.next ./.next
COPY --from=build /app/public ./public
COPY --from=build /app/node_modules ./node_modules

# Exponer puerto de Next.js
EXPOSE 3000

# Ejecutar aplicación
CMD ["npm", "start"]
