# 🐾 PetFinderRemake

An Android application for discovering and adopting pets, built with modern Android development best practices. This project is a remake of the [Petfinder](https://www.petfinder.com/) experience using a clean, modular architecture and the latest Jetpack libraries.

---

## 🏗️ Architecture

PetFinderRemake follows **MVVM + Clean Architecture** principles with a fully **modularized** project structure, separating concerns across feature, data, and domain layers.

```
PetFinderRemake/
├── app/            # Application module — DI wiring, navigation host, entry point
├── common/         # Shared UI components, utilities, and base classes
├── features/       # Feature modules (e.g., pet list, pet details, search)
└── logging/        # Centralized logging abstraction
```

### Architecture Layers

- **UI Layer** — Jetpack Compose screens and ViewModels
- **Domain Layer** — Use cases and business logic
- **Data Layer** — Repository implementations, remote/local data sources

---

## 🛠️ Tech Stack

| Category              | Technology                                      |
|-----------------------|-------------------------------------------------|
| **Language**          | Kotlin 100%                                     |
| **UI**                | Jetpack Compose + Material 3                    |
| **Architecture**      | MVVM + Clean Architecture                       |
| **Async**             | Kotlin Coroutines + Flow                        |
| **Dependency Injection** | Hilt                                         |
| **Networking**        | Retrofit + OkHttp                               |
| **Serialization**     | Kotlin Serialization / Gson                     |
| **Image Loading**     | Coil                                            |
| **Navigation**        | Jetpack Navigation (Compose)                    |
| **Build System**      | Gradle (Kotlin DSL)                             |
| **Logging**           | Custom logging module                           |

---

## 🔑 API

This app uses the **[Petfinder API](https://www.petfinder.com/developers/)**.

To run the project, you'll need to register for a free API key and add your credentials:

1. Register at [petfinder.com/developers](https://www.petfinder.com/developers/)
2. Obtain your `API_KEY` and `API_SECRET`
3. Add them to your `local.properties`:

```properties
PETFINDER_API_KEY=your_api_key_here
PETFINDER_API_SECRET=your_api_secret_here
```

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog or newer
- JDK 17+
- Android SDK 24+

### Build & Run

1. Clone the repository:
   ```bash
   git clone https://github.com/M0bileDev/PetFinderRemake.git
   cd PetFinderRemake
   ```

2. Add your Petfinder API credentials to `local.properties` (see above).

3. Open in Android Studio and run on an emulator or device.

---

## ✨ Features

- 🐶 Browse available pets for adoption
- 🔍 Search and filter by type, breed, age, and location
- 🐾 View detailed pet profiles with photos and descriptions
- 📍 Location-based pet discovery
- 🔖 Save favourite pets

---

## 📦 Modularization

The project is split into Gradle modules to achieve:

- **Faster build times** via parallel compilation
- **Clear separation of concerns**
- **Feature isolation** for independent development and testing
- **Reusable common components** shared across features

---

## 🤝 Contributing

Contributions are welcome! Please open an issue or submit a pull request.

---

## 📄 License

```
Copyright 2024 M0bileDev

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
