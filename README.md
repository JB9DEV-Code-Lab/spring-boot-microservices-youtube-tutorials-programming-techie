# Microservices with Spring Boot

This project is simply to follow [Spring Boot Microservices Project Example on YouTube from Programming Techie channel](https://www.youtube.com/watch?v=lh1oQHXVSc0&list=PLSVW22jAG8pBnhAdq9S8BpLnZ0_jVBj0c).
As I always do in when following a tutorial, I followed the main idea from the playlist, but implementing on my own way,
by changing minor things, adding new features or even changing the proposed feature entirely.

## Features
- Basic Rest API services to make a simple simulating of an order being placed on an ecommerce, by making one service
  communicating with each other either synchronously or not.

## Dependencies
- Java 21
- Docker
- Mongo DB
- MySQL

## Running locally
- Setup local databases (both Mongo DB and MySQL);
- Using terminal just run the command `.mvnw spring-boot:run`;
- Using IntelliJ Idea, just click on the play button on the following files:
  - `InventoryServiceApplication`; 
  - `OrderServiceApplication`; 
  - `ProductServiceApplication`;

> [!NOTE]
> 🚧 This file is under construction, so it may have more details in the future as the project advances, such as:
> - Add more details to the description 🚧
> - Add more details around dependencies 📝
> - Add more details on how to run it locally 🚧
