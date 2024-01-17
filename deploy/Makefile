.PHONY: build

build:
	docker build  -f Dockerfile . -t antonnv22/app:0.1.0

push:
	docker push antonnv22/app:0.1.0

docker-start:
	docker-compose up -d