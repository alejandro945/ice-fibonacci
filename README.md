# Ice Middleware - Fibonacci Calculator 🎁

Distributed programming to calculate the fibonacci sequence. The Fibonacci numbers, commonly denoted Fn , form a sequence, the Fibonacci sequence, in which each number is the sum of the two preceding.

## Build With 🛠️

<div align="left">
    <p>
        <a href="https://code.visualstudio.com" target="_blank"> <img alt="Visual Studio Code" src="https://raw.githubusercontent.com/devicons/devicon/1119b9f84c0290e0f0b38982099a2bd027a48bf1/icons/vscode/vscode-original.svg" height="60" width = "60"></a>
        <a href="https://code.visualstudio.com" target="_blank"> <img alt="Zeroc Ice Middleware" src="https://avatars.githubusercontent.com/u/10439369?s=280&v=4" height="60" width = "60"></a>
        <a href="https://gradle.org" target="_blank"> <img alt="Gradle" src="https://raw.githubusercontent.com/gilbarbara/logos/1f372be75689d73cae89b6de808149b606b879e1/logos/gradle.svg" height="60" width = "60"></a>
    </p>
</div>

## Versioned 📌

<div align="left">
    <a href="https://git-scm.com/" target="_blank"> <img src="https://raw.githubusercontent.com/devicons/devicon/2ae2a900d2f041da66e950e4d48052658d850630/icons/git/git-original.svg" height="60" width = "60"></a>
    <a href="https://github.com/" target="_blank"> <img src="https://img.icons8.com/fluency-systems-filled/344/ffffff/github.png" height="60" width = "60"></a>
</div>

## Configuration 💾

We're going to use a project with two sub-projects to build the Client and Server applications. The requirements for our sub-projects are the same so we'll do all the setup in the subprojects block of the root project, which applies to all sub-projects. Edit the generated build.gradle file to look like the one below:

```gradle
...
"Class-Path": configurations.runtimeClasspath.resolve().collect { it.toURI() }.join(' ')
...
```

In order for it to be independent of the computer on which it is developed, it should look like this:

```gradle
...
"Class-Path": 'opt/Ice-3.7.6/lib/ice-3.7.6.jar'...
```

We must also edit the generated settings.gradle to define our sub-projects:

```gradle
rootProject.name = 'ice'
include 'client'
include 'server'
```

Besides that, we must edit the file config.server located in server/src/main/resources with the host and port where the server will be deployed, the default server values are these:

```properties
Printer.Endpoints=tcp -p 9099
Ice.Default.Host=localhost
```

Finally, we must edit the file config.client located in client/src/main/resources with the values of the server we want to communicate, the default client values are these:

```properties
Printer.Proxy=SimplePrinter:tcp -p 9099
Callback.Client.Endpoints=default -h localhost
Ice.Default.Host=localhost
```

As an **observation**, if you want to run our product remotely, you must change the resource file that is inside the jar (zip) and you must modify the host by the ip and port where the logical interface of your computer is located. We will place an example of how we do it using zerotier as a logical interface identifying the server on the xhgrid9 pc.


### Server

- For your local pc

```properties
Ice.Default.Host=10.147.19.125
```
- For remotes pc

```properties
Ice.Default.Host=192.168.131.49 # hgrid9.icesi.edu.co (DNS resolves ip)
```
### Client

- In your local pc

```properties
Callback.Client.Endpoints=default -h 10.147.19.218 # Your ip in zerotier logic interface
Ice.Default.Host=10.147.19.125 # xhgrid9 (DNS resolves ip)
```

- In remote pc

```properties
Callback.Client.Endpoints=default -h 192.168.131.51 # Or hotsname specific of the remote client (hgrid11.icesi.edu.co)
Ice.Default.Host=hgrid9
```

### Notes 📑

Sending jar files using scp command:

```bash
scp server.jar swarch@xhgrid9:.
scp client.jar swarch@xhgrid10:.
scp client.jar swarch@xhgrid11:.
```

## Compiling the client and server ⭐️

```bash
gradle build
```

## Running product remotely 💥

```bash
java -jar server/build/libs/server.jar 
java -jar client/build/libs/client.jar 
```
![run](./assets/run.png)

## Testing our distributed app local 🐙

- To run client and server, we first start the server in a separate window:
  ![server](./assets/server.png)
- At this point, we wont see anything because the server simply waits for a client to connect to it. We run the client in a different window:
  ![client](./assets/client.png)

Some examples of the client and server communication are documentated below:

* Client:![client](./assets/client_execution.png)
* Server:
  ![client](./assets/server_execution.png)

To get rid of the server, we interrupt it on the command line for now. And, to get rid of the client you must write **exit**.

## **Authors** ✒️

<div align="left">
    <a href="https://github.com/cuatrosr" target="_blank"> <img alt="cuatrosr" src="https://images.weserv.nl/?url=avatars.githubusercontent.com/u/70908378?v=4&h=60&w=60&fit=cover&mask=circle"></a>
    <a href="https://github.com/alejandro945" target="_blank"> <img alt="alejandro945" src="https://images.weserv.nl/?url=avatars.githubusercontent.com/u/64285906?v=4&h=60&w=60&fit=cover&mask=circle"></a>
</div>

---

[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com) [![forthebadge](https://forthebadge.com/images/badges/for-you.svg)](https://forthebadge.com)
