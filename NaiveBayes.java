/*
 * Copyright (C) 2017 vitorcezar
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Naive Bayes classification program
 * @author vitorcezar
 */
public class NaiveBayes {

    /**
     * prints argument error and exit the program
     */
    private static void printArgumentError() {
        System.out.println( "The arguments passed are incorrect" );
        System.exit( 1 );
    }
    
    /**
     * prints parser error and exit the program
     * @param line the line where the error was found
     */
    private static void printLineError( int line ) {
        System.out.printf( "Line %d is incorrect\n", line );
        System.exit( 1 );
    }
    
    /**
     * prints file error and exit the program
     */
    private static void printFileError( String file ) {
        System.out.printf( "File %s not found\n", file );
        System.exit( 1 );
    }
    
    /**
     * verifies if the arguments passed are correct
     * @param args the arguments
     */
    private static void verifyArguments( String[] args ) {
        if( args.length == 0 ) {
            printArgumentError();
        }
        if( args[ 0 ].equals( "-t" ) ) {
            if( args.length != 3 ) {
                printArgumentError();
            }
        } else if( args[ 0 ].equals( "-c" ) ) {
            if( args.length != 4 ) {
                printArgumentError();
            }
        } else if( args[ 0 ].equals( "-a" ) ) {
            if( args.length != 3 ) {
                printArgumentError();
            }
        } else {
            printArgumentError();
        }
    }
    
    /**
     * train the Naive Bayes classifier with the training file
     * @param classifier the Naive Bayes classifier
     * @param path the training file path
     */
    private static void trainClassifier( NaiveBayesClassifier classifier, 
        String path ) {
        try {
            Scanner trainingScanner = new Scanner( new File( path ) );
            int line = 1;

            while( trainingScanner.hasNext() ) {
                
                // variables to do a regex search
                String lineRead = trainingScanner.nextLine();
                Pattern numberPattern = Pattern.compile( "(\\d)," );
                Pattern textPattern = Pattern.compile( "\"(.*)\"" );
                Matcher numberMatcher = numberPattern.matcher( lineRead );
                Matcher textMatcher = textPattern.matcher( lineRead );

                // parses the file training the classifier
                if( numberMatcher.find() && textMatcher.find() ) {
                    
                    // negative example
                    if( numberMatcher.group( 1 ).equals( "0" ) ) {
                        classifier.setNegativeExample( textMatcher.group( 1 ) );
                    // positive example
                    } else if( numberMatcher.group( 1 ).equals( "1" ) ) {
                        classifier.setPositiveExample( textMatcher.group( 1 ) );
                    // error
                    } else {
                        printLineError( line );
                    }
                // error
                } else {
                    printLineError( line );
                }
                line++;
            }
        } catch( FileNotFoundException e ) {
            printFileError( path );
        }
    }
    
    /**
     * classifies the texts of a file
     * @param classifier the Naive Bayes classifier
     * @param inputFilePath file that will be classified
     * @param outputFile file where the classification will be printed
     */
    private static void classify( NaiveBayesClassifier classifier, 
        String inputFilePath, String outputFile ) {
        try {
            Scanner trainingScanner = new Scanner( new File( inputFilePath ) );
            PrintWriter writer = new PrintWriter( outputFile );
            int line = 1;

            while( trainingScanner.hasNext() ) {
                // variables to do a regex search
                String lineRead = trainingScanner.nextLine();
                Pattern textPattern = Pattern.compile( "\"(.*)\"" );
                Matcher textMatcher = textPattern.matcher( lineRead );

                // parses the file classifying it
                if( textMatcher.find() ) {
                    int classification = 
                        classifier.classify( textMatcher.group( 1 ) );
                    if( classification == NaiveBayesClassifier.TRUE ) {
                        double positiveProbability = 
                            classifier.chanceOfPositive();
                        writer.println( "" + 1 + "," + positiveProbability );
                    } else {
                        double negativeProbability =
                            classifier.chanceOfNegative();
                        writer.println( "" + 0 + "," + negativeProbability );
                    }
                } else {
                    printLineError( line );
                }
                line++;
            }
            writer.close();
        } catch( FileNotFoundException e ) {
            printFileError( inputFilePath );
        }
    }
    
    /**
     * avaliates classification comparing the classification file with the
     * answer
     * @param path1 classification file's path
     * @param path2 answer file's path
     */
    private static void avaliateClassification( String path1, String path2 ) {
        try {
            Scanner scanner2 = new Scanner( new File( path1 ) );
            Scanner scanner1 = new Scanner( new File( path2 ) );
            int right = 0;
            int amount = 0;

            while( scanner1.hasNext() && scanner2.hasNext() ) {
                amount++;
                char classification1 = scanner1.nextLine().charAt( 0 );
                char classification2 = scanner2.nextLine().charAt( 0 );
                
                if( classification1 == classification2 ) {
                    right++;
                }
            }
            System.out.printf( "Prediction: %f\n", ( double ) right /
                ( double ) amount );
        } catch( FileNotFoundException e ) {
            printFileError( path1 + " or " + path2 );
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main( String[] args ) {
        verifyArguments( args );
        
        if( args[ 0 ].equals( "-t" ) ) {
            NaiveBayesClassifier naiveBayes = new NaiveBayesClassifier();
            trainClassifier( naiveBayes, args[ 1 ] );
            NaiveBayesClassifier.saveClassifier( args[ 2 ], naiveBayes );
        } else if( args[ 0 ].equals( "-a" ) ) {
            avaliateClassification( args[ 1 ], args[ 2 ] );
        } else {
            try {
                NaiveBayesClassifier naiveBayes = 
                    NaiveBayesClassifier.loadClassifier( args[ 1 ] );
                classify( naiveBayes, args[ 2 ], args[ 3 ] );
            } catch( Exception e ) {
                printFileError( args[ 1 ] );
            }
        }
    }
}
