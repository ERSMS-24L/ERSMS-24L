# Process View

* Solution architect



* Sequence
* Communication
* Activity
* Timing
* Interaction overview



<figure><img src="../.gitbook/assets/image.png" alt=""><figcaption><p><a href="https://docs.axoniq.io/reference-guide/architecture-overview">https://docs.axoniq.io/reference-guide/architecture-overview</a></p></figcaption></figure>



Micro-service main component communication

* command handling

```mermaid
sequenceDiagram
    actor u as User
    participant c as Controller
    participant a as Aggregate
    participant es as Event Store / Message Queue
    participant eh as Event Handlers

    u ->> c: Command with modification request
    c ->> a: Command routed via Command Gateway
    a ->> es: Fetch all the events with given aggregate id
    es ->> a: Recreate the aggregate state based on those events
    a ->> a: Handle provided command, it may generate events
    a ->> a: Handle new events in appropriate event sourcing handlers (modify aggregate state)
    a ->> es: Store generated valid events
    es ->> eh: Pass events to other event handlers
    a ->> c: No response (Async) or return generated aggregate identifier
    c ->> u: Return result
```

* query service updating the state

```mermaid
sequenceDiagram
    participant es as Event Store / Message Queue
    participant qs as Queue Service
    participant ts as Token Store
    participant p as Projector
    participant r as Repository

    es ->> qs: Service has event handlers for given type
    qs ->> ts: This instance is responsible for handling event with that aggregate id
    qs ->> p: Pass events to all the registered event handlers
    p ->> r: Fetch current state of View from database
    r ->> p: Current View
    p ->> p: Update view based on info from event (in case of failure service, events may be loaded multiple times)
    p ->> r: Save updated projection in database
    r ->> p: Successfull save
    p ->> qs: Event handled
    qs ->> ts: Successfully handled event with given id
```

* querying the query service

```mermaid
sequenceDiagram
    actor u as User
    participant c as Controller
    participant p as Projector
    participant r as Repository

    u ->> c: Query request
    c ->> p: Send query via query gateway to all the registered QueryHandlers
    p ->> r: Fetch appropriate projection using provided query
    r ->> p: Return projection
    p ->> c: Return result
    c ->> u: Return result 
```
