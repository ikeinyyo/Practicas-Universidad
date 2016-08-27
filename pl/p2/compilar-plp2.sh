#!/bin/bash

ANTLRJAR=antlr-3.4-complete.jar
java -classpath $ANTLRJAR     org.antlr.Tool plp2.g
javac -classpath $ANTLRJAR:.     *.java
