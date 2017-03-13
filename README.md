# TheAccountant

The software for splitting bills. I am going to use this with my new dormmates. It is designed to run in a safe local area network.

Create a transaction when someone paid a bill. 

Create a transaction when someone paid the money they owned.

Negative balance simply means you owned money to people with positive balance.

## Getting Started

Using a Raspberry Pi 3 for this project and it runs perfectly.

### Prerequisites

Java8 + Maven to compile this project

MySQL + JRE8 + tomcat 8.5.11 to run this on your server

### Installing
To install this software, you may have a little bit knowledge of SQL and be able to set up a tomcat server.
Assume you already have a tomcat + MySQL server set up with correct SQL user privilege.

1.Download the source
* do "git clone https://github.com/JHXSMatthew/TheAccountant"

2.Modify database settings and compile
* the database setting should be in "/main/java/com/github/JHXSMatthew/sql/MySQLConnection.java" you may edit it with
with whatever text editor.Also, do change the password in /main/java/com/github/JHXSMatthew/Config in order to login. 
the default password is 626.
* do "cd TheAccountant" then "maven install" to compile the source.

3.upload .war to your tomcat server
 * .war file should be in /target folder and you may rename it and upload to your tomcat webApps folder.
 
4.??????

5.profit


## Authors

* **JHXSMatthew** 

## License

This project is licensed under the GPL3.0 License - see the [LICENSE.md](LICENSE.md) file for details
