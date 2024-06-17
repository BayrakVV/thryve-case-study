# thryve-case-study

This repository contains the solution for the Thryve case study, demonstrating API test automation for the Thryve main data flow case. The test case includes:
- Generating a Thryve user access token
- Uploading data point to the Thryve warehouse
- Checking the last posted data by a webhook
- Downloading data via Thryve API
- Verifying retrieved data

## Getting Started

### Dependencies
Ensure you have the following installed:
- Latest MacOS
- Java 17, which can be installed via [SDKMAN](https://sdkman.io/)
- The latest version of [Maven](https://maven.apache.org/install.html)

Alternatively, you can run tests in a Docker container. For this, you need only [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed, available for MacOS, Windows, and Linux.



### Setup
Clone this repository:
```bash
git clone https://github.com/BayrakVV/thryve-case-study.git
```
Navigate to the repository root to execute terminal commands:
```bash
cd path/to/repository
```
Create .env file:
```bash
touch .env
```
Then copy the contents of the .env.dist file into the .env file and assign values to the env variables

## How to run tests

### In the terminal
Execute the tests using Maven (this command runs all configured tests and outputs the results):


```bash
mvn test
```

### Inside Docker container
Build the Docker image (this command builds a Docker image named "thryve-test" based on the Dockerfile in your project directory):
```bash
docker build -t thryve-test .
```
Run the tests in a Docker container (this command runs the tests inside a Docker container based on the thryve-test image and removes the container after the tests finish):
```bash
docker run -it --rm --env-file .env thryve-test
```