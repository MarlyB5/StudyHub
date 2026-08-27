# 📚 StudyHub

StudyHub is a JavaFX desktop productivity application designed to bring study planning, focus sessions, module management, task tracking, music, and study analytics into one application.

The application allows students to organise their university modules, retrieve workload information directly from UCD, track study time using a Pomodoro timer, manage tasks, monitor progress towards module study targets, and review their study statistics.

---

## ✨ Features

### ⏱️ Pomodoro Timer

StudyHub includes a configurable Pomodoro timer for tracking focused study sessions.

Features include:

- Start, pause and reset functionality
- Custom work-session duration
- Custom break duration
- Completed Pomodoro session tracking
- Lifetime study-time tracking
- Study time persistence between application sessions
- Optional study-module selection
- Automatic attribution of study time to the selected module
- Break and paused time excluded from study totals

Study time recorded through the Pomodoro timer is used throughout the application to generate module progress and study statistics.

---

### 🎓 UCD Module Lookup

StudyHub integrates with the UCD module catalogue using **Jsoup**.

Students can search for a UCD module using its module code, for example:

```text
COMP30860
```

StudyHub retrieves information from the UCD module page, including:

- Module code
- Module name
- Autonomous Student Learning hours
- Contact hours
- Total expected workload

This information can then be used to create a module directly within StudyHub.

This removes the need for students to manually look up and enter their expected module workload.

---

### 📖 Study Module Management

Students can create and manage their university modules.

Each module stores:

- Module code
- Module name
- Autonomous study hours
- Contact hours
- Whether contact hours should be included in the study target
- Total study time recorded for the module

Users can choose between:

```text
Autonomous Study Target
```

or:

```text
Autonomous + Contact Hours Target
```

Modules are persisted locally and automatically restored when StudyHub is restarted.

Duplicate module codes are prevented.

---

### 📊 Study Progress Dashboard

The Dashboard provides a visual overview of progress towards each module's study target.

Each module displays:

- Module code and name
- Hours studied
- Target study hours
- Progress percentage
- JavaFX progress bar

For example:

```text
COMP31020 - Formal Foundations 3

Studied: 12.5h / 76h

████████░░░░░░░░░░░░

16%
```

Progress is calculated automatically from Pomodoro study sessions associated with the module.

The Dashboard refreshes whenever it is opened so that recently recorded study time is immediately reflected.

---

### ✅ Task Management

StudyHub contains a persistent to-do list for managing academic tasks.

Tasks support:

- Task title
- Completion status
- Optional module association
- Due dates
- Priority levels
- Marking tasks complete/incomplete
- Removing tasks
- Clearing an existing module association

Three priority levels are available:

```text
Extra Important
Moderately Important
Not Important
```

Tasks can optionally be associated with one of the student's saved StudyHub modules.

For example:

```text
Finish Assignment
COMP30860
Extra Important
Due: 30/08/2026
```

Tasks are persisted between application sessions.

---

### 📈 Study Statistics

StudyHub records historical study activity and converts it into useful statistics.

The Statistics page displays:

- Study time today
- Study time during the current week
- Lifetime study time
- Number of completed Pomodoro sessions
- Study time over the last seven days
- Study time by module
- Most studied module

A JavaFX `BarChart` visualises study activity across the previous seven days.

Days without study activity are still represented so that the graph always displays a complete seven-day period.

---

### 📅 Daily Study History

StudyHub records when study activity occurs rather than storing only a lifetime total.

Study sessions are grouped by date using ISO-8601 dates:

```text
YYYY-MM-DD
```

For example:

```text
2026-08-25 → 3600 seconds
2026-08-26 → 5400 seconds
2026-08-27 → 7200 seconds
```

This enables StudyHub to calculate daily and weekly statistics while maintaining backward-compatible lifetime totals.

Only active Pomodoro work-session time contributes to study history.

---

### 🎵 Study Playlist

StudyHub includes an integrated music player for use during study sessions.

Playlist functionality includes:

- Play
- Pause and resume
- Previous track
- Next track
- Double-click to play a song
- Add local audio files
- Remove songs
- Automatic next-track playback
- Volume control
- Playback progress tracking
- Shuffle
- Repeat functionality
- Persistent playlist storage

Music playback is managed separately from the individual Playlist view so that playback can continue while navigating through StudyHub.

---

## 💾 Persistent Data

StudyHub uses **Gson** and JSON for local persistence.

Application data is stored through a shared:

```text
AppData
```

object and saved using:

```text
DataManager
```

The persistence architecture is:

```text
JavaFX Views / Application Logic
              ↓
           AppData
              ↓
         DataManager
              ↓
        app-data.json
```

Persisted information includes:

- Pomodoro work duration
- Pomodoro break duration
- Completed work sessions
- Lifetime study time
- Daily study history
- Study modules
- Per-module study time
- Playlist songs
- Playlist state
- Tasks
- Task completion
- Task priorities
- Task due dates
- Task-module associations

StudyHub uses a single persistence system rather than separate save files for individual features.

---

## 🏗️ Project Architecture

The project is organised into feature-specific packages.

```text
src/main/java/org/

├── app/
│   ├── Main.java
│   └── MenuView.java
│
├── dashboard/
│   └── DashboardView.java
│
├── modules/
│   ├── ModuleManager.java
│   ├── ModuleView.java
│   ├── StudyModule.java
│   ├── UCDModuleData.java
│   └── UCDModuleService.java
│
├── persistence/
│   ├── AppData.java
│   └── DataManager.java
│
├── playlist/
│   ├── MediaManager.java
│   ├── PlaylistManager.java
│   ├── PlaylistView.java
│   └── Song.java
│
├── statistics/
│   ├── StatisticsService.java
│   ├── StatisticsView.java
│   └── StudyDay.java
│
├── timer/
│   ├── PomodoroTimer.java
│   ├── TimerViewer.java
│   └── TimeListener.java
│
└── todo/
    ├── Task.java
    ├── TodoManager.java
    └── TodoView.java
```

> The exact project structure may evolve as development continues.

---

## 🔄 Application Data Flow

StudyHub uses a shared application-data model.

When the application starts:

```text
app-data.json
      ↓
DataManager.loadData()
      ↓
    AppData
      ↓
 ┌────┼───────────────┐
 ↓    ↓       ↓       ↓
Timer Modules Tasks Playlist
      ↓
 Dashboard
      ↓
 Statistics
```

The same `AppData` instance is shared throughout the application.

This prevents individual features from creating conflicting copies of application data.

---

## ⏱️ Study Tracking Architecture

When the Pomodoro timer is running during a work session:

```text
Pomodoro Work Tick
        │
        ├──► Lifetime Study Time
        │
        ├──► Today's Study History
        │
        └──► Selected Module
                 │
                 └──► Module studiedSeconds
```

If no module is selected, the study session still contributes to lifetime and daily study statistics.

Paused and break periods do not contribute to study totals.

---

## 🌐 UCD Module Integration

StudyHub uses **Jsoup** to retrieve publicly available module information from the UCD module catalogue.

The workflow is:

```text
User enters module code
          ↓
   UCDModuleService
          ↓
       Jsoup
          ↓
   UCD Module Page
          ↓
Parse module information
          ↓
    UCDModuleData
          ↓
     ModuleView
          ↓
     StudyModule
```

The retrieved workload information is used to calculate personalised study targets.

---

## 🎨 User Interface

StudyHub is built entirely with **JavaFX**.

The application uses reusable CSS style classes to separate presentation from application logic.

Current styling architecture includes classes for:

```text
page-root
title-label
section-title
subtitle-label
status-label

primary-button
secondary-button
danger-button

card
module-card
summary-card
```

A custom pixel font is bundled with the application and loaded through JavaFX resources.

The application is currently undergoing a custom **pixel-art inspired UI redesign**.

Pages are scrollable where necessary so the interface remains usable at smaller window sizes.

---

## 🛠️ Technologies

| Technology | Usage |
|---|---|
| Java | Core application development |
| JavaFX | Desktop UI |
| JavaFX Media | Music playback |
| JavaFX Charts | Study statistics visualisation |
| Gson | JSON persistence |
| Jsoup | UCD module data retrieval |
| Gradle | Build and dependency management |
| CSS | Application styling |
| JSON | Local application data storage |
| Git / GitHub | Version control |

---

## 🚀 Running StudyHub

### Requirements

- Java 21+
- Gradle

Clone the repository:

```bash
git clone <repository-url>
```

Move into the project:

```bash
cd Studyhub
```

Run:

```bash
./gradlew run
```

On Windows PowerShell:

```powershell
.\gradlew run
```

A clean build can be performed using:

```bash
./gradlew clean build
```

---

## 💡 Example Workflow

A typical StudyHub workflow could look like:

```text
1. Search COMP30860

2. StudyHub retrieves:
   Web Development
   Autonomous Study: 75 hours
   Contact: 25 hours

3. Add the module to StudyHub.

4. Create a task:
   "Finish Web Development assignment"
   Priority: Extra Important
   Due: Friday
   Module: COMP30860

5. Open Pomodoro.

6. Select:
   COMP30860 - Web Development

7. Start studying.

8. StudyHub automatically records:
   → lifetime study time
   → today's study time
   → COMP30860 study time

9. Open Dashboard.
   → View progress towards the module target.

10. Open Statistics.
    → View today's study time
    → weekly study time
    → seven-day chart
    → most studied module
```

---

## 🧠 Key Technical Features

Some of the main technical concepts demonstrated by StudyHub include:

- Object-oriented Java design
- JavaFX scene and view management
- Event-driven programming
- Observable JavaFX collections
- Persistent application state
- JSON serialisation/deserialisation
- Web scraping and HTML parsing
- External data integration
- JavaFX media playback
- JavaFX data visualisation
- Date-based historical data processing
- Shared application-state architecture
- Separation of UI, model, service and persistence responsibilities

---

## 📌 Current Development Status

Core functionality is complete.

Current development is focused primarily on:

- Pixel-art UI redesign
- CSS styling
- Visual consistency
- Final testing and bug fixing
- Screenshots and project demonstration material

---

## 🔮 Possible Future Improvements

StudyHub has deliberately been kept as a local single-user application, but potential future extensions could include:

- Study streaks
- Monthly/yearly statistics
- More advanced study-goal visualisation
- Additional module catalogue integrations
- Task filtering and sorting
- Notifications
- Packaged desktop installers

These are considered future enhancements rather than requirements for the current version.

---

## 📷 Screenshots

Screenshots will be added following completion of the current UI redesign.

```text
[ StudyHub Menu ]

[ Pomodoro Timer ]

[ Study Module Search ]

[ Study Progress Dashboard ]

[ Tasks ]

[ Statistics ]
```

---

## 👩🏾‍💻 Author

**Marly Bah**

Computer Science Student

Built as a personal software engineering project to explore Java desktop development, persistent application state, web data integration, productivity tooling and data visualisation.
