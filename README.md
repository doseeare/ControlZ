
<p align="center">
  <img
    src="https://github.com/user-attachments/assets/ad4baae8-5f33-44df-8e23-66db73480ce2"
    width="120"
    height="120"
    alt="ctrlz_icon"
  />
</p>


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


https://github.com/user-attachments/assets/f5a21733-7f91-49e2-84e2-a55dac51cfcd

---

### 1) Add debug-panel to host screen
```kotlin
TimeTravelPanelDrawer(enabled = true) {
  //Your app content
  }
```

### 2) Register state in ViewModel

In your `ViewModel` (or any presentation layer), register your `StateFlow` in `TimeTravelRegistry`.

```kotlin
TimeTravelRegistry.register(
    scope = viewModelScope,
    stateFlow = state,
    enabled = true, // usually BuildConfig.DEBUG
    onChanged = { travelState ->
        updateState { travelState }
    }
)
```

---

## 📦 Installation
Add the dependency to your `build.gradle`:

```kotlin
dependencies {
    implementation("io.github.doseeare.controlz:timetravel:0.1.0")
}
