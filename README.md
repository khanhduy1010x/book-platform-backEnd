# Book4.0 backend

Spring Boot API for the Book4.0 frontend. On startup it idempotently adds 18 public-domain titles to the `Books` and `Category` tables: five from Project Gutenberg and 13 from Standard Ebooks. Their EPUBs are served by the frontend at `/books/`; metadata for the additional titles is mirrored in `src/main/resources/additional-books.json`.

Configure `BOOK_DB_URL`, `BOOK_DB_USER`, `BOOK_DB_PASSWORD`, and `BOOK_JWT_SIGNER_KEY` before starting. Redis is configured through `BOOK_REDIS_HOST` and `BOOK_REDIS_PORT` (defaults: `localhost`, `6379`). SMS integration uses optional `BOOK_SMS_KEY` and `BOOK_SMS_DEVICE`. The old hard-coded database/JWT/SMS values and the known default admin account have been removed from current source. Rotate any credentials that were previously published in Git history.

```sh
./mvnw -DskipTests package
java -jar target/fshop-0.0.1-SNAPSHOT.jar
```

The API runs on port 9999 by default. A single-container build combining this backend, the frontend and the Clothes Store is described in the sibling `fashion-store` repository.
