Run below command to execute the program at project main path

`mvn clean install tomcat7:run`

then you can post our requests to url [](http://localhost:8080/register)  with below Json format


```
{
  "username" : "Test",
  "password" : "A9oo",
  "dateOfBirth" : "1988-02-03",
  "ssn" : "786-45-1245"
}
```
