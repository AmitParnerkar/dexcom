# Getting Started

### Reference Documentation

Docker commands to use
* docker compose up -d --force-recreate --build
* docker network inspect backend
* docker logs -f  dexcom-ai
* docker compose exec -it dexcom-ai bash
* docker buildx build --platform linux/amd64,linux/arm64 -t paam0101/dexcom-llm --push .
* docker buildx build --platform linux/amd64,linux/arm64 -t paam0101/dexcom-ai --push .
