# Vaadin Todo Application

A simple todo application built with Vaadin, designed to help users manage their tasks efficiently. This project serves as a starting point for Vaadin-based web applications with a focus on task management.

## Vaadin
Create business web apps on Java using open-source frameworks, featuring a comprehensive UI component library, seamless backend integration, and dedicated first-party support.
No JavaScript or HTML required.

## Project Structure

The repository contains the following key directories and files:

- **.idea/**: Configuration files for IntelliJ IDEA
- **gradle/wrapper/**: Gradle wrapper files for consistent build execution
- **src/**: Source code directory (contains application logic)
- **target/**: Build output directory
- **build.gradle.kts**: Gradle build configuration
- **settings.gradle.kts**: Gradle settings configuration
- **gradlew & gradlew.bat**: Gradle wrapper scripts (Unix and Windows)
- **package.json & package-lock.json**: Node.js dependency management
- **tsconfig.json**: TypeScript configuration
- **vite.config.ts & vite.generated.ts**: Vite build tool configuration

## Prerequisites

- Java 11 or higher
- Node.js (for frontend dependencies)
- Gradle (optional, since Gradle wrapper is included)

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/newbieking/vaadin-todo.git
   cd vaadin-todo
   ```

2. Build the project:
   ```bash
   # Using Gradle wrapper (Unix)
   ./gradlew build

   # Using Gradle wrapper (Windows)
   gradlew.bat build
   ```

3. Run the application:
   ```bash
   # Unix
   path/to/java path/to/jar
   ```

4. Open your browser and navigate to `http://localhost:8080` to use the application.

## Features (Planned/In Progress)

- Add new tasks with descriptions
- Mark tasks as completed
- Delete tasks
- Filter tasks by status (active/completed)
- Persist tasks between sessions

## Technologies Used

- **Vaadin**: Java web framework for building modern single-page applications
- **Gradle**: Build automation tool
- **TypeScript**: For frontend type safety
- **Vite**: Frontend build tool

## Contributing

Feel free to fork this repository, make improvements, and submit pull requests. Any contributions to enhance the functionality or fix issues are welcome.

## License

This project is open-source and available under the [MIT License](LICENSE) (license file to be added).