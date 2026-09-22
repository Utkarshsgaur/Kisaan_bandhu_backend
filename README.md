# Kisan Bandhu Backend — database-compatible rebuild

This project is deliberately built **without Lombok** so constructor/getter/setter annotation-processing problems do not cause the `cannot find symbol` / `not initialized in default constructor` errors seen in the earlier project.

## Important database compatibility
The supplied MySQL schema is `agri_procurement_system`. It contains `users`, `farmers`, `crops`, `procurement_seasons`, `season_crops`, `procurement_centres`, `procurement_schedules`, `bookings`, `waiting_list`, `procurement_records`, and `notifications`.

It does **not** contain password, OTP, QR-pass, payment, support-ticket, or contact-message tables. Therefore:
- Authentication is OTP-based; no fake password column is invented.
- OTP is in-memory for the SIH prototype (`OTP_DEV_MODE=true` returns the OTP in the response). Replace this with an SMS provider later.
- QR pass is generated as a secure signed token on demand; no QR table is required.
- The current schema cannot persist payment/support/contact data. Add those tables before implementing those persistent modules.
- The schema has no `slots` table. A procurement schedule is therefore treated as the bookable time window. Multiple slots require multiple schedule rows or a schema change.

## Run
1. Create/import the database schema provided by your DB teammate.
2. Set `DB_USERNAME` and `DB_PASSWORD` to the MySQL credentials on the machine where MySQL runs.
3. If MySQL is on another laptop, replace `localhost` in `DB_URL` with that laptop's LAN IP and configure MySQL to accept remote connections/firewall access.
4. Set `FRONTEND_ORIGIN` to your frontend URL.
5. Use Java 17 and Maven.
6. Run `mvn clean compile`, then run the Spring Boot application.

## Example environment variables
DB_URL=jdbc:mysql://localhost:3306/agri_procurement_system?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
DB_USERNAME=root
DB_PASSWORD=YOUR_MYSQL_PASSWORD
JWT_SECRET=change-this-to-a-long-random-secret-of-at-least-32-characters
FRONTEND_ORIGIN=http://localhost:5173
OTP_DEV_MODE=true

## First API test
POST `/api/v1/auth/register`
```json
{
  "phoneNumber":"9876543210",
  "fullName":"Test Farmer",
  "village":"Test Village",
  "district":"Test District",
  "state":"Tamil Nadu",
  "pincode":"627357",
  "alternatePhone":""
}
```
Then use the returned JWT as `Authorization: Bearer <token>` for protected APIs.

## Core flow
Register → Login/OTP → crops → centres → schedules → booking → queue → QR → operator arrived → accept/reject → procurement status → notification.
