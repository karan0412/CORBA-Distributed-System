cd (add your path to the assignment)


"(add your path to jdk 1.8)"\javac *.java PeerApp/*.java

start orbd -ORBInitialPort 1050 -ORBInitialHost localhost

start java PeerRegister -ORBInitialPort 1050 -ORBInitialHost localhost

