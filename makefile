JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	D1/serverMain.java \
	D2/ClientMain1.java\
	D3/ClientMain2.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) D1/*.class D2/*.class D3/*.class
