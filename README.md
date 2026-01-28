
<img width="1024" height="1024" alt="ctrlz_icon" src="https://github.com/user-attachments/assets/ad4baae8-5f33-44df-8e23-66db73480ce2" />


# ControlZ TimeTravel

Bring **Ctrl+Z** to your app state.  
ControlZ TimeTravel is a developer tool that records the history of your application state and lets you **travel back and forth in time**, jump to any saved state, and even inject new states from JSON.  
Perfect for debugging, reproducing bugs, and exploring how your app behaves in different scenarios.

---

## ✨ Features
- 📜 **State history** — every change is recorded.
- ⏪ **Time travel** — step back, step forward, or jump to any point.
- 🧩 **Panel integration** — visualize and navigate states directly in your UI.
- 🛠 **State injection** — load a state from JSON to simulate scenarios.
- ⚡ **Debug-friendly** — enable only in debug builds.

---

## 🎬 Demo

### ⏪ Time travel between states
Navigate freely through your application's state history — step backward, forward, or jump to any moment in time.


https://github.com/user-attachments/assets/9c845ebb-2239-421e-b1b8-711a279702c7

---

### 🛠 Inject state from JSON
Load and inject a custom state from JSON to instantly reproduce edge cases and complex scenarios.

https://github.com/user-attachments/assets/04d78f9a-1cc0-4251-b403-766ffc0ef639

---

## 📦 Installation
Add the dependency to your `build.gradle`:

```kotlin
dependencies {
    implementation("io.github.doseeare.controlz:timetravel:0.1.0")
}
