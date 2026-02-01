# Cinema App (OOP)

## Highlights
- JOIN implementation: `TicketRepositoryImpl.findDetailedByUser` joins `tickets`, `sessions`, `movies`, and `users` to build a full ticket description (movie + session + buyer + purchase info).
- Design patterns added:
  - Singleton: `DbConnectionManager` provides a single DB connection entry point.
  - Factory: `RepositoryFactory` centralizes repository creation.
  - Strategy: `PricingStrategy` (with `TieredPricingStrategy`) controls session pricing.
  - Builder: `TicketDetails.Builder` builds the joined ticket view cleanly.
- Lambda expressions:
  - Used `forEach` lambdas for clean list output in `SimpleCinemaService`.
  - Used `IntStream.range(...).forEach(...)` to generate sessions from a list.
  - Added small helper lambdas to keep loop logic readable while handling SQL exceptions.
- Role management:
  - Roles: `USER`, `MANAGER`, `ADMIN` with basic access checks in the CLI menu.
  - Manager/Admin can see all tickets; Admin can manage user roles.
- Data validation:
  - Centralized in `ValidationUtils` (names, username, password, positive integers).
  - Applied in `SimpleCinemaService.register`, `login`, and `buyTicket`.
- Entity categories:
  - Movies have a `MovieCategory` (ACTION, DRAMA, SCI_FI, etc.).
  - Stored in `movies.category` and displayed in `showMovies`.

## How to Run
- `mvn -q exec:java`

## Database Note
- Add a `role` column to `users` (string) and set a default value like `USER`.
- Add a `category` column to `movies` (string) and set a default value like `GENERAL`.
- The `genre` column is no longer used (can be dropped if it exists).
