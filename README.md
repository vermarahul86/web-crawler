# web-crawler

Endpoints for all the possible operations:

1. Start crawling : http://localhost:{port}/crawler/start
2. Get status of crawling : http://localhost:{port}/crawler/status
3. Fetch movies list : http://localhost:{port}/crawler/movies

** port -> port you define to run (if executed directly on the personal machine)
** else if executed as docker image, then port you have mentioned in docker-compose.yml

Steps to build docker image, push to remote repo and run the application.
1. docker build --build-arg JAR_FILE=build/libs/*.jar -t {dockerHubId}/webcrawler .
2. docker login
3. docker push
4. docker-compose up

References:
https://www.baeldung.com/dockerizing-spring-boot-application
https://spring.io/guides/gs/spring-boot-docker/
https://docs.docker.com/desktop/windows/