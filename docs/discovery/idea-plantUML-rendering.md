## PlantUML Plugin Source Discovery 

This section answers key technical questions about PlantUML integration and internal behavior.

---

## 1. Which libraries are used under the hood?

Main dependencies:

- PlantUML engine (core diagram generation library)
- Graphviz (optional, used for layout in some diagrams)
- Java standard libraries:

Key point:
PlantUML acts as a self-contained rendering engine.

---

## 2. How does PlantUML parse files?

PlantUML does NOT use a traditional parser with exposed AST.

Instead:

- Input is plain text (DSL)
- It is interpreted directly by PlantUML engine
- Internal steps:
    - lexical analysis
    - semantic interpretation
    - internal diagram model creation
    - rendering

Important:
No parsing API is exposed to plugins.

---

## 3. Does it create DAG files?

No external DAG files are created.

However:

- internally PlantUML builds a graph-like structure
- this structure is not exposed outside the engine
- it is used only during rendering

Conclusion:
DAG exists internally but is not accessible.

---

## 4. Does it have mapper in DAG / UML notation?

No external mapping layer exists.

- UML text → internal model mapping is fully handled inside PlantUML
- there is no public API for graph transformation
- plugin cannot access intermediate representation

Implication:
Only input (text) → output (image) is available.

---

## 5. Can we create a benchmark for the plugin?

Yes, benchmarking is possible and recommended.

---

## Metrics

### 1. Render time
Measure time from input → image:

- average render time
- worst-case rendering

---

### 2. Memory usage
Track heap consumption:

- before render
- after render
- during repeated updates

Tools:
- VisualVM
- JProfiler
- IntelliJ profiler

---

### 3. CPU usage
Useful for:

- heavy diagrams
- realtime updates
- large inputs

---

### 4. Realtime metrics (important for debounce systems)

- renders per second
- skipped renders (due to debounce)
- average latency per render

---

### 5. UI responsiveness

Measure:

- delay between typing and preview update
- blocking time on EDT (UI thread)

---

## Conclusion

PlantUML behaves as a black-box rendering engine:

- no AST or DAG access
- no parsing customization
- no internal model exposure

All control must be implemented in plugin layer:
- debounce
- async execution
- timeout handling
- lifecycle management