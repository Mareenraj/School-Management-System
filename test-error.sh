#!/bin/bash
mvn clean package -DskipTests
java -jar target/API-0.0.1-SNAPSHOT.jar &
PID=$!
echo "Waiting for app to start..."
sleep 15
curl -s -v -X POST http://localhost:8080/api/auth/verify-otp \
-H "Content-Type: application/json" \
-d '{"email":"mareenraj7@gmail.com","otp":"668372"}' > response.txt 2> headers.txt
kill $PID
echo "Response:"
cat response.txt
echo -e "\nHeaders:"
cat headers.txt | grep "< HTTP"
