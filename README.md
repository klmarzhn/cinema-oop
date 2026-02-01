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

## How to Run
- `mvn -q exec:java`
