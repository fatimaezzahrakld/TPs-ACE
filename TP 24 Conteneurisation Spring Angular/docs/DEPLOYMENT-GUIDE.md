# Guide de Déploiement - Smart House Docker

## 🚀 Déploiement Rapide

### Prérequis
- Docker Desktop installé et démarré
- Git installé
- 4 GB RAM minimum

### Étapes Rapides

```bash
# 1. Cloner le projet
git clone https://github.com/lachgar/smarthouse.git
cd smarthouse

# 2. Copier les Dockerfiles
# Copier Dockerfile.backend vers Smart_Home_back/Dockerfile
# Copier Dockerfile.frontend vers smartHome-front/Dockerfile
# Copier docker-compose.yml à la racine

# 3. Build et lancement
docker-compose build
docker-compose up -d

# 4. Vérifier
docker-compose ps
```

### Accès

- **Frontend**: http://localhost
- **Backend**: http://localhost:8085
- **phpMyAdmin**: http://localhost:8081

---

## 🔧 Configuration Backend

### application.properties

Assurer que ces propriétés sont configurées dans `Smart_Home_back/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://mysql:3306/smart-house
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
server.port=8085
```

> [!IMPORTANT]
> Utiliser `mysql` comme hostname (nom du service Docker), pas `localhost`

---

## 🔧 Configuration Frontend

### environment.ts

Vérifier la configuration API dans `smartHome-front/src/environments/environment.ts`:

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8085/api'
};
```

---

## 📊 Commandes Docker Utiles

### Gestion des Services

```bash
# Démarrer tous les services
docker-compose up -d

# Arrêter tous les services
docker-compose stop

# Redémarrer un service
docker-compose restart backend

# Voir les logs
docker-compose logs -f backend

# Rebuild un service
docker-compose build backend
docker-compose up -d backend
```

### Nettoyage

```bash
# Supprimer les conteneurs
docker-compose down

# Supprimer conteneurs + volumes (⚠️ efface la DB)
docker-compose down -v

# Supprimer toutes les images non utilisées
docker system prune -a
```

### Inspection

```bash
# Entrer dans un conteneur
docker exec -it smarthouse-backend sh

# Voir les processus
docker-compose top

# Voir l'utilisation ressources
docker stats
```

---

## 🐛 Résolution de Problèmes

### Port 80 déjà utilisé

```bash
# Windows
netstat -ano | findstr :80
taskkill /PID <PID> /F

# Linux/Mac
sudo lsof -i :80
sudo kill -9 <PID>
```

### MySQL ne démarre pas

```bash
# Vérifier les logs
docker-compose logs mysql

# Supprimer le volume et recommencer
docker-compose down -v
docker-compose up -d mysql
```

### Backend ne se connecte pas à MySQL

1. Vérifier que MySQL est UP:
   ```bash
   docker-compose ps mysql
   ```

2. Tester la connexion:
   ```bash
   docker exec -it smarthouse-backend sh
   ping mysql
   ```

3. Vérifier les credentials dans `application.properties`

### Build échoue

```bash
# Nettoyer le cache Docker
docker builder prune

# Rebuild sans cache
docker-compose build --no-cache
```

---

## 📈 Monitoring

### Health Checks

```bash
# Backend health
curl http://localhost:8085/actuator/health

# Frontend (devrait retourner HTML)
curl http://localhost
```

### Logs en temps réel

```bash
# Tous les services
docker-compose logs -f

# Un service spécifique
docker-compose logs -f backend
```

---

## 🔒 Sécurité (Production)

> [!WARNING]
> Ces configurations sont pour le développement. En production:

1. **Ne pas utiliser `root` pour MySQL**
   ```yaml
   MYSQL_USER: smarthouse_user
   MYSQL_PASSWORD: <strong_password>
   ```

2. **Utiliser des secrets Docker**
   ```bash
   docker secret create mysql_root_password password.txt
   ```

3. **HTTPS pour le frontend**
   ```yaml
   ports:
     - "443:443"
   # Configurer SSL dans Nginx
   ```

4. **Network isolation**
   ```yaml
   networks:
     frontend:
     backend:
       internal: true
   ```

---

## 📝 Checklist Déploiement

- [ ] Docker Desktop installé et démarré
- [ ] Projet cloné localement
- [ ] Dockerfiles copiés aux bons endroits
- [ ] docker-compose.yml à la racine
- [ ] Configuration backend (application.properties)
- [ ] Configuration frontend (environment.ts)
- [ ] Build réussi (`docker-compose build`)
- [ ] Services démarrés (`docker-compose up -d`)
- [ ] Tous les conteneurs UP (`docker-compose ps`)
- [ ] Frontend accessible (http://localhost)
- [ ] Backend accessible (http://localhost:8085)
- [ ] phpMyAdmin accessible (http://localhost:8081)

---

**Bon déploiement!** 🚀
