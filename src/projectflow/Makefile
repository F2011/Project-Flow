.PHONY: run stop build test

run:
	docker compose up -d
	@echo "Waiting for Postgres to be healthy..."
	@until docker compose exec postgres pg_isready -U projectflow -d projectflow > /dev/null 2>&1; do sleep 1; done
	./mvnw install -DskipTests -q
	./mvnw spring-boot:run -pl plugins

stop:
	docker compose down

build:
	./mvnw install -DskipTests -q

test:
	./mvnw test
