#!/bin/bash

ANTLRJAR=antlr-3.5-complete.jar
java -classpath $ANTLRJAR     org.antlr.Tool plp4.g
javac -classpath $ANTLRJAR:.     *.java
