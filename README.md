# Finite State Automaton (FSA) in Java

## Project Structure
- `src/main/java/Main.java`: Entry point, reads user string.
- `src/main/java/automata/Automata.java`: Core FSA logic (customize states/transitions here).

## How to Compile and Run (Plain Java, no Maven needed)
1. Open terminal in `c:/Users/user/Proyectos/Automata`.
2. Compile:
   ```
   mkdir bin
   javac -d bin src/main/java/*.java src/main/java/automata/*.java
   ```
3. Run:
   ```
   java -cp bin Main
   ```
4. Enter a string (e.g., "1010" for alphabet {0,1}). Outputs ACCEPTED/REJECTED.