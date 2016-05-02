# 486project

## How to compile
This project uses Gradle to compile. It is broken up into three subprojects: Authority, Client, Shared.

To compile each project, simply run Gradle with the tasks:
- clean
- build
- client:bootRepackage
- authority:bootRepackage

This will generate a .jar file in each sub projects /build/libs folder.

The last two should be included in build itself, but to be sure run them anyway.

## How to run
Included in the project zip are three pre-built jar files.
- authority.jar - To run a local instance of the authority, without full certificates.
- client.jar - To connect to the Authority server provided by our team.
- client-local.jar - To connect to a local authority instance.

In case that our team's Authority is not available, you can use authority.jar and client-local.jar to test the
system locally. First run authority.jar via command line with java -jar authority.jar and wait for it to state that
it is "RUNNING". Then launch client-local.jar with java -jar client-local.jar.

If you wish to run two instances of client.jar or client-local.jar, they must be run in separate folders due to
file locks on the ledger database.

Running client.jar is the same as client-local.jar, with the exception that it depends on our team's authority running
instead of a local instance.

Note: If you intend to test two clients on one machine, we recommend using the local authority and client variants,
due to the loopback issues some computers can have.

## Register to play
To be able to create messages and exchange messages, you must register with the authority your client is connected
with. This is contained within the UI. After registering, you can log in and start working.