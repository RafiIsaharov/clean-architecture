-- missed record lectures
day2
01:12:00
2:25:00

--name convention method in the service layer prefixed with:
get = in memory stuff,
fetch = from the outside API
find = from the database
--but it can also mislead if someone doesn't follow this, than it can be mentioned in ADR,  "we should this when we..."

--CODEOWNERS file in the root of the repo to define who should review the PRs

Day 2 - Dependency Inversion Principle
Protecting the domain using technique (enforce boundary) that nothing from outside (infra) would ever be referenced directly from inside.
Domain starts to become Agnostic
At runtime , a Dependency Injection framework will inject an instance of the implementation of the interface adapter into the constructor of the domain service.
--Domain Service should not know about the implementation of the interface adapter, it should only know about the interface adapter.
Infrastructure = "Anti-corruption layer" (adapter) between the domain and the outside world (calls and maps data)
![img.png](img.png)