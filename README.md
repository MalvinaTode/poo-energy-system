# Energy Network Simulation

A Java-based simulation of an energy distribution network implemented using object-oriented programming principles.

The project models different types of energy producers, consumers, and batteries and simulates how the network responds to changing production, demand, component failures, and energy shortages.

## Project Overview

The simulated grid is managed by the central `GridController` class, which models the network components and stores lists of producers, consumers, and batteries.

The controller also maintains the overall grid state through the `isInBlackout` flag and keeps track of major events using the `istoricEvenimente` event history.

## Grid Management

The main functions used to manage the network include:

- `addProducator` – adds an energy producer after validating the input data, including unique IDs and positive power values.
- `addConsumator` – adds an energy consumer after validating the input data.
- `addBaterie` – adds a battery after validating its unique ID and positive capacity.
- `statusGrid` – displays the detailed status of each component and the overall state of the grid (`STABLE` or `BLACKOUT`).
- `istoricEvenimente` – displays the major events recorded during the simulation.
- `setDefect` – changes the operational status (`true`/`false`) of an existing component.

## Class Hierarchy

The project uses an object-oriented class hierarchy centered around the abstract `ComponentaRetea` class.

### Energy Producers

`ProducatorEnergie` is an abstract class that defines the common behavior of energy producers.

It is extended by three concrete producer types:

- `PanouSolar` – production depends on the solar factor.
- `TurbinaEoliana` – production depends on the wind factor.
- `ReactorNuclear` – provides constant energy production.

Each producer checks its `isStatusOperational` state when calculating production.

If a producer is defective, its energy production is `0`.

### Energy Consumers

`ConsumatorEnergie` is an abstract class containing common consumer attributes such as:

- energy demand
- priority

It is extended by:

- `SistemSuportViata` – priority `1`
- `LaboratorStiintific` – priority `2`
- `SistemIluminat` – priority `3`

The priority is automatically assigned according to the type of consumer.

### Batteries

`Baterie` is a concrete network component responsible for storing and releasing energy.

It provides two main operations:

- `incarca(surplus)` – stores available surplus energy.
- `descarca(cerere)` – releases stored energy to help cover a deficit.

The battery also checks its operational status before contributing energy to the network.

## Grid Simulation

The main simulation logic is implemented in:
simuleazaTick(factorSoare, factorVant)
Each simulation tick follows several steps.

1. Reset Consumer States

All consumers are initially reset to the powered state.

2. Calculate Total Production

The total energy production of all producers is calculated.

Each producer is checked to determine whether it is operational.

The appropriate environmental factor is then applied:

Solar panels → solar factor
Wind turbines → wind factor
Nuclear reactor → constant production

Defective producers contribute 0 energy.

3. Calculate Total Demand

The total energy demand of the network is calculated as the sum of the energy requirements of all currently powered consumers.

4. Calculate the Energy Balance

The difference between production and demand is calculated:

delta = production - demand

Two main situations can occur.

Surplus

If:

delta > 0

the network has more energy than it currently needs.

The surplus is distributed to the batteries.

Each battery is charged until either:

the available surplus is exhausted, or
the battery reaches its capacity.

Any remaining energy that cannot be stored is lost.

Deficit

If:

delta < 0

the network does not produce enough energy to satisfy the current demand.

The simulation first attempts to use the available battery energy to cover the deficit.

If the batteries cannot fully cover the deficit, the system enters the triage stage.

Energy Triage

The executaTriage() function handles situations where the available energy is insufficient.

Consumers are sorted according to their priority using Java's sort() functionality.

The consumers are disconnected in the following order:

Priority 3 → Priority 2

Priority 1 consumers are considered critical and are never disconnected during the triage process.

If the remaining deficit can be solved by disconnecting lower-priority consumers, the grid continues operating.

If there is still not enough energy after the triage process, the network enters BLACKOUT and the system is shut down.

Simulation Summary

At the end of each simulation tick, a summary is displayed containing:

total energy production
total energy demand
battery status
disconnected consumers
current grid state

This makes it possible to follow how the network responds to changes in production, demand, and component failures.

Bonus – Edge Cases

The project also considers several potential edge cases.

Battery Overload

If a battery receives an extremely large amount of energy, the stored value could potentially exceed the limits of the data type used to represent it, resulting in an overflow.

Invalid Environmental Factors

The solar and wind factors are expected to remain within the interval:

[0, 1]

Values outside this interval should be validated because these factors represent normalized environmental conditions.

All Producers Are Defective

If all energy producers become defective, total production becomes 0.

Even if the batteries are initially fully charged, they can only provide energy for a limited amount of time.

Once the stored energy is exhausted, the network will eventually enter a blackout.

Technologies
Java
Object-Oriented Programming
Abstract Classes
Inheritance
Polymorphism
Encapsulation
ArrayList
Sorting
Simulation
State Management
Key Concepts Demonstrated
Object-oriented design
Class hierarchies
Abstract classes
Inheritance and polymorphism
Collection management
Input validation
State management
Sorting objects by priority
Resource allocation
Failure handling
Simulation of real-world systems
Project Structure
.
├── App.java
├── Baterie.java
├── ComponentaRetea.java
├── ConsumatorEnergie.java
├── GridController.java
├── LaboratorStiintific.java
├── PanouSolar.java
├── ProducatorEnergie.java
├── ReactorNuclear.java
├── SistemIluminat.java
├── SistemSuportViata.java
└── TurbinaEoliana.java


