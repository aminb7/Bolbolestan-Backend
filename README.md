# bolbolestan backend
Bolbolestan web application server.

## Instructions
### A. Initiate MySQL Docker
1. Run 
   `docker run -d -p 3306:3306 --name=docker-mysql --env="MYSQL_ROOT_PASSWORD=password" --env="MYSQL_PASSWORD=password" --env="MYSQL_DATABASE=BolbolestanDatabase" mysql`.

### B. Run The Server
1. Clone the repository and `cd` into the project directory;
2. Run `docker build -t bolbolestan .`;
3. Run `docker run -t --link docker-mysql:mysql -p 8080:8080 bolbolestan`.

Now enjoy the Bolbolestan web service APIs!