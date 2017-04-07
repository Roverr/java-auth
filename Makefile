all: build cpbin

.PHONY: build
build:
	mvn clean compile assembly:single

.PHONY: cpbin
cpbin:
	mkdir -p ./jar
	cp ./target/authcore-1.0-SNAPSHOT-jar-with-dependencies.jar ./jar/authcore.jar

.PHONY: clean
clean:
	rm -rf ./jar

.PHONY: install
install:
	mvn package
