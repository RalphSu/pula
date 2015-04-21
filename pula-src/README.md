# pula
pula system for fun

Make sure sql server running on the localhost listening 1433

pula-sys

> mvn jetty:run -Djetty.port=8125

Accessing http://localhost:8125/app/my/entry

pula-web (depend on pul-sys)

> mvn jetty:run -Djetty.port=8127

Accessing http://localhost:8127/student/
