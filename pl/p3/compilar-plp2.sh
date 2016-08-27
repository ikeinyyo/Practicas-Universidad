#!/bin/bash

ANTLRJAR=antlr-3.5-complete.jar
java -classpath $ANTLRJAR     org.antlr.Tool plp3.g
javac -classpath $ANTLRJAR:.     *.java
