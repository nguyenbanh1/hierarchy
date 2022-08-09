For simple, I hard-code username and password in memory to authentication ann authorization
that only admin can make and get on hierarchy API.

1. We must install docker
2. Go to the root project
3. Run `./gradlew build`
4. Run `docker build -t hierarchy .`
5. Run `docker-compose -f docker-compose.yml up -d`
6. We can login/authentication
 - Admin(can make hierarchy) : admin/password

`curl --location --request POST 'localhost:8804/authentications' \
--header 'Content-Type: application/json' \
--data-raw '{
"username" : "admin",
"password" : "password"
}'`
 - User(not make anything) : user/password

``curl --location --request POST 'localhost:8804/authentications' \
--header 'Content-Type: application/json' \
--data-raw '{
"username" : "user",
"password" : "password"
}'``

7. When login with admin and then you have {token}
8. You can make hierarchy

`curl --location --request POST 'localhost:8804/hierarchy' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJST0xFX0FETUlOIn1dLCJleHAiOjE2NjAwODY5MzMsImlhdCI6MTY2MDA2ODkzM30.1Dh1hdc9UFEyD69ZML0GqALhQsxuPkCre62W-nvKvEhag875MdMmkzAzwoJu5mlx6yrjKlVbtTQW0H0MZj9zag' \
--header 'Content-Type: application/json' \
--data-raw '{
"Pete": "Nick",
"Barbara": "Nick",
"Nick": "Sophie",
"Sophie": "Jonas"
}'`

9. You can get hierarchy

`curl --location --request GET 'localhost:8804/hierarchy' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJST0xFX0FETUlOIn1dLCJleHAiOjE2NjAwODY5MzMsImlhdCI6MTY2MDA2ODkzM30.1Dh1hdc9UFEyD69ZML0GqALhQsxuPkCre62W-nvKvEhag875MdMmkzAzwoJu5mlx6yrjKlVbtTQW0H0MZj9zag'
`

10. You can get hierarchy's supervisor by name

`curl --location --request GET 'localhost:8804/employees/Nick/supervisors' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJST0xFX0FETUlOIn1dLCJleHAiOjE2NjAwODY5MzMsImlhdCI6MTY2MDA2ODkzM30.1Dh1hdc9UFEyD69ZML0GqALhQsxuPkCre62W-nvKvEhag875MdMmkzAzwoJu5mlx6yrjKlVbtTQW0H0MZj9zag'
`
