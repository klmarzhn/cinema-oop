# Cinema App (OOP)

## Highlights
- JOIN implementation: `TicketRepositoryImpl.findDetailedByUser` joins `tickets`, `sessions`, `movies`, and `users` to build a full ticket description (movie + session + buyer + purchase info).
- Design patterns added:
  - Singleton: `DbConnectionManager` provides a single DB connection entry point.
  - Factory: `RepositoryFactory` centralizes repository creation.
  - Strategy: `PricingStrategy` (with `TieredPricingStrategy`) controls session pricing.
  - Builder: `TicketDetails.Builder` builds the joined ticket view cleanly.

## How to Run
- `mvn -q exec:java`
