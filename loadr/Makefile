SHELL := /bin/bash

build: ## Builds this project [Default]
	mvn compile && mvn package

kafka-setup:
	docker exec -it kafka /bin/bash /opt/kafka/bin/kafka-topics.sh --create --if-not-exists --topic clearing-payment-t --bootstrap-server localhost:9092

container: clean build ## Builds the docker container for this project
	docker build -t wp-poc-loadr .

clean: ## Cleans the project
	mvn clean