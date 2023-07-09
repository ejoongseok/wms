## local-docker 환경

### 1. 로컬 환경 구성 with docker-compose

```bash
sh local-docker.sh
```

### 2. Run Application

```bash
cd ..
./gradlew bootRun -Dspring.profiles.active=local-docker
```