# HabitFlow

HabitFlow is a habit tracking web application built with Kotlin and Spring Boot.

The idea of the project is to model a small real-world habit system. A user can create habits, split them into steps, mark progress, see a calendar, and compare progress on a leaderboard. The project is not only storing records: it also calculates streaks, completion rates, weekly progress, calendar statistics, and leaderboard scores.

## What the App Does

- Users can register and log in.
- Each user has a personal habit workspace.
- A habit can be created, edited, completed, or deleted.
- A habit can have smaller steps.
- When all steps are completed, the habit is completed for today.
- Habits can be private or public.
- The app shows a calendar with completed habit days.
- The app shows a leaderboard based on completed habits and streaks.
- Suggested habits can be added to a user's workspace.

## Project Structure

The project is split into four main modules:

- `domain` - core habit models and rules.
- `application` - service interfaces, commands, view models, reports, events, and ranking logic.
- `infrastructure` - JPA entities, repositories, database service implementation, seed data, security adapter, and event listener.
- `presentation` - Spring Boot application, web controllers, HTML pages, CSS styles, and security configuration.

This structure separates the main responsibilities of the system. The UI does not directly work with database entities, and the persistence layer is kept outside the domain/application model.

## OOP and SOLID

The project uses several OOP ideas:

- Models are grouped by responsibility.
- Services expose interfaces such as `HabitTrackerService` and `HabitCalendarService`.
- Dependencies are injected through constructors.
- Business logic is split into smaller classes such as `HabitProgressReporter`, `LeaderboardRanker`, `HabitDraftFactory`, and completion policies.
- The code depends on abstractions where it makes sense, for example `HabitEventPublisher`, `HabitEventListener`, and `CompletionPolicy`.

This helps keep the code easier to extend. For example, a new habit completion rule or leaderboard scoring strategy can be added without rewriting the whole application.

## Design Patterns Used

The project uses these design patterns:

- Strategy: different `CompletionPolicy` implementations are used for daily, weekday, and weekly habits.
- Strategy: `LeaderboardScoringStrategy` controls how leaderboard points are calculated.
- Factory: `CompletionPolicyFactory` chooses the correct completion policy.
- Factory: `HabitDraftFactory` creates validated habit drafts from forms and suggestions.
- Builder: `HabitDraft.Builder` builds and validates habit draft objects.
- Observer: `HabitEventPublisher` notifies listeners when a habit is completed.

These patterns are used for real parts of the app, not just as separate examples.

## Kotlinx Usage

The user interface is written in Kotlin:

- `kotlinx.html` is used to generate server-side HTML pages.
- `kotlin-css` is used to generate CSS styles with `CssBuilder`.

The HTML page code is in:

```text
presentation/src/main/kotlin/com/example/habittracker/presentation/web/pages
```

The CSS style code is in:

```text
presentation/src/main/kotlin/com/example/habittracker/presentation/web/styles
```

## Spring Features

The app uses Spring Boot with:

- Spring MVC controllers
- Spring Security login/logout
- Spring Data JPA repositories
- H2 in-memory database
- Virtual threads enabled in `application.yml`
- A small versioned REST endpoint: `/api/leaderboard`

The versioned API can be called with the `API-Version` header:

```bash
curl -H "API-Version: 1.0" http://localhost:8080/api/leaderboard
curl -H "API-Version: 2.0" http://localhost:8080/api/leaderboard
```

## Demo Data

Demo users are created automatically when the app starts.

Password for all demo users:

```text
password
```

Example usernames:

```text
dmitry
marta
anna
rihards
sofia
```

The app uses an in-memory H2 database, so the demo data is recreated each time the app starts.

## Running the Project

Build and run tests:

```bash
./gradlew build
```

Run the web application:

```bash
./gradlew :presentation:bootRun
```

Open the app:

```text
http://localhost:8080
```

