# web-crawler

Endpoints:

1. Start crawling : http://localhost:8080/crawler/start
2. Get status of crawling : http://localhost:8080/crawler/status
3. Fetch movies list : http://localhost:8080/crawler/movies

docker build --build-arg JAR_FILE=build/libs/*.jar -t rahulverma86/webcrawler .
docker login
docker push
docker-compose up

References:
https://www.baeldung.com/dockerizing-spring-boot-application
https://spring.io/guides/gs/spring-boot-docker/
https://docs.docker.com/desktop/windows/