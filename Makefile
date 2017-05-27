build:
	javac *.java
  
clear:
	rm *.class

object:
	java NaiveBayes -t tweets.txt object.clf

classify:
	java NaiveBayes -c object.clf inputs.txt classification.txt
