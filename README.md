# User Salary Demo

## Running the application
```
gradlew bootRun
```

### Frontend
The index.html is hosted at
```
localhost:8080
```
### Backend
GET retrives the JSON of valid users (0 <= salary <= 4000). POST can accept text/csv or text/plain for direct text csv, or multipart/form-data for csv file upload.

Backend endpoint is at
```
localhost:8080/users
```

H2 embedded database console at
```
localhost:8080/h2-console
```
