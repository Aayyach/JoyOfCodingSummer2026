# Airline Management Application
A multi-phase Java and Android application designed over the course of five projects in the course "Joy of Coding Java and Android" at Portland State University to manage airlines, flights, and flight searches. This project evolves from a CLI application with data persistence into a full-fledged Android mobile application.

![App Demo](docs/Screen_recording_20260826_232216.gif)

## Overview
The system manages flight scheduling, airport codes, date-time formatting, and data persistence. It validates user input, handles error conditions smoothly, and provides three distinct user interfaces: CLI, REST Web Service, and Android UI.

### Application Capabilities
* **Creation:** Create and manage airlines with multiple flights.
* **Flight Sorting:** Sorts flights alphabetically by departure airport and time.
* **Duration Tracking:** Calculates flight duration in minutes.
* **Multiple interfaces:** CLI, HTTP REST API, and Android UI. 

## Key Features 
* **Android Mobile UI:** Built for the Pixel 9 API 36 emulator. Includes options to create airlines, enter flights, search flights, view formatted output, and read a built-in README.
* **Internal Storage Persistence:** Saves mobile application data to Android internal storage so it survives app restarts.  RESTful Web Service: An AirlineServlet running on Jetty that accepts GET and POST requests to search or add flights across multiple airlines.
* **Text File Storage:** Uses TextDumper and TextParser classes to save and read airline data from text files.  
* **Pretty Printing:** Uses PrettyPrinter class to format flight lists and include calculated durations and human-readable airport names.  
* **Input Validation:** Rejects invalid airport codes, incorrect date/time formats, or flights where arrival occurs before departure, returning clear error messages.  

## Tech Stack
* **Language:** Java
* **Mobile-Access:** Android SDK (Pixel 9 API 36 Emulator), Gradle.
* **Web-Access:** Java Servlet, Jetty Web Container.
* **Build Tools:** Apache Maven (mvnw), Gradle (gradlew).

## Getting Started
### Prerequisites
* Java Development Kit (JDK)
* Android Studio with the Pixel 9 API 36 emulator configured
* Maven wrapper (./mvnw) and Gradle wrapper (./gradlew)

## Build & Run Instructions
## REST Web Application (airline-web)
* **Run all tests and build:**
```sh
./mvnw verify
```

* **Start the Jetty Web Server:**
```sh
./mvnw jetty:run
```

* **Add a flight via CLI client:**
```sh
java -jar target/airline-client.jar -host localhost -port 8080 "Air Dave" 123 PDX 03/19/2026 1:02 pm ORD 03/19/2026 6:22 pm
```

* **Search for flights between airports:**
```sh
java -jar target/airline-client.jar -host localhost -port 8080 -search "Air Dave" PDX LAS
```

### Standalone Text File Utility
* **Save flight info to a text file:**
```sh
java -jar target/airline-1.0.0.jar -textFile data/airline.txt "Air Dave" 456 PDX 07/15/2026 10:30 AM SEA 07/15/2026 11:45 AM
```

* **Pretty-print flights from a file to a console:**
```sh
java -jar target/airline-1.0.0.jar -textFile data/airline.txt -pretty - "Air Dave" 456 PDX 07/15/2026 10:30 AM SEA 07/15/2026 11:45 AM
```

## Android Mobile Application (airline-android)
** To launch the Android application, follow these steps:**
1. Open **Android Studio** and select **Open** to load the airline-android directory located in your project root.
2. Start the **Pixel 9 API 36** emulator from the Device Manager.
3. Deploy and launch the app by clicking the **Run** button (green play icon) in **Android Studio**.

## Author & Acknowledgements

Developed by **Alaa Ayyach** as part of *The Joy of Coding Java and Android* Summer 2026 course at Portland State University.

* [@aayyach](https://github.com/Aayyach)
* [Linkedin](https://www.linkedin.com/in/alaaayyach404/)