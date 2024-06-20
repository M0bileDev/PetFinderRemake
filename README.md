# PetFinderRemake
## Demo
### The goal of this project was practise and develop programming skills. An application is written in accordance with best practices.
<br></br>
Application is divided into separated modules named <b>features</b> (eg. features/details, features/discover, features/filter as so on...). 
Application contains core module named <b>common</b>. Common module stands for commony shared data with other modules, contains domain models, domain, data , di, presentation and much more. 
Dependencies are configured to use modern solutions like toml.
Last but not least in this very short description, some modules contains test (unit, instrumentation and presentation -> espresso).
<br></br>Interesting parts of application:
1. clean architecture (Layers of Clean Architecture)
2. dependency injection framework (Hilt)
3. solid principles 
4. modularization
5. mvvm
6. tests (unit, instrumentation, ui)
7. firebase integration (fcm)
   
<br></br>Variants:
1. Android View + Kotlin -> ([master](https://github.com/M0bileDev/PetFinderRemake/tree/master)).
2. Android View + Kotlin + RxJava -> ([migration_rxjava](https://github.com/M0bileDev/PetFinderRemake/tree/feature/migration_rxjava)).
3. Compose + Kotlin (soon)
4. Compose + RxJava (soon)
