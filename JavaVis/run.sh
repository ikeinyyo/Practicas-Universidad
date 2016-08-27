export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:lib/bin
#if [[ $OSTYPE == darwin* ]]; then
#	export CLASSPATH=.:./lib/log4j-1.2.15.jar:./lib/colt.jar:./lib/BareBonesBrowserLaunch.jar:./lib/guava-11.0.2.jar:./lib/javassist-3.12.1.GA.jar:./lib/jhall.jar:./lib/kd.jar:./lib/OpenKinec.jar:./lib/org.OpenNI.jar:./lib/reflections-0.9.8.jar:./lib/xercesImpl.jar:./lib/xml-apis.jar:./lib/fmj.jar
#else
#	export CLASSPATH=.:./lib/log4j-1.2.15.jar:./lib/colt.jar:./lib/BareBonesBrowserLaunch.jar:./lib/guava-11.0.2.jar:./lib/javassist-3.12.1.GA.jar:./lib/jhall.jar:./lib/kd.jar:./lib/OpenKinec.jar:./lib/org.OpenNI.jar:./lib/reflections-0.9.8.jar:./lib/xercesImpl.jar:./lib/xml-apis.jar:./lib/fmj.jar
#fi
export CLASSPATH=.:./lib/log4j-1.2.15.jar:./lib/colt.jar:./lib/BareBonesBrowserLaunch.jar:./lib/guava-11.0.2.jar:./lib/javassist-3.12.1.GA.jar:./lib/jhall.jar:./lib/kd.jar:./lib/OpenKinec.jar:./lib/org.OpenNI.jar:./lib/reflections-0.9.8.jar:./lib/xercesImpl.jar:./lib/xml-apis.jar:./lib/fmj.jar
java -d32 -Xms128m -Xmx512m javavis.Gui
