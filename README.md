# NaiveBayes

A NaiveBayes implementation for text classification, based on Machine Learning, Tom Mitchell. The texts used for training and classification must be between double quotes and the target function must appear on the beginning of the line and separated from the text by comma. It's recommended to remove any kind of punctuation from the text. For examples of training and classification format, see tweets.txt and inputs.txt.

Use 'make' for compilation. The commands are:



java NaiveBayes -t \[training file\] \[object\]: outputs an object that will be used later for classification.

java NaiveBayes -c \[object\] \[file that will be classified\] \[result file\]: outputs a file with the classification results.

java NaiveBayes -a \[classification file results\] \[file with answers\]: prints the percent of correctly classified items.



The commands 'make object' and 'make classify' were created to show how to use this class. The tweets.txt file was taken from http://technobium.com/wordpress/wp-content/uploads/2015/01/tweets.txt and modified.
