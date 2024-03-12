# Use Case

* use case
* story boards
* scenarios

TODO: add more use cases

```mermaid
flowchart LR
    u[User]
    m[Moderator]
    a[Administrator]

    l[Login]
    u --> l
    m --> l
    a --> l
    
    r[Registration]
    u --> r
    a --> r
    
    p[Post creation]
    m --> p
    
    pm[Post modification]
    m --> pm
    
    c[Commenting]
    u --> c
    
    cm[Comment modification]
    m --> cm
```
