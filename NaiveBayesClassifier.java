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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Naive Bayes for text classification implementation
 * @author vitorcezar
 */
public class NaiveBayesClassifier implements Serializable {
    
    /**
     * Bag of words for words information
     */
    private final BagOfWords bagOfWords;
    
    /**
     * counts the number of positive examples
     */
    private int numberPositiveExample;
    
    /**
     * counts the number of negative examples
     */
    private int numberNegativeExample;
    
    /**
     * the chance of an example be false
     */
    private double falseProbability;
    
    /**
     * the chance of an example be true
     */
    private double trueProbability;
    
    /**
     * definition for invalid probability value
     */
    public final static int INVALID = -1;
    
    /**
     * definition for true classification
     */
    public final static int TRUE = 1;
    
    /**
     * definition for false classification
     */
    public final static int FALSE = 0;
    
    /**
     * class constructor
     */
    public NaiveBayesClassifier() {
        bagOfWords = new BagOfWords();
        numberNegativeExample = 0;
        numberPositiveExample = 0;
        falseProbability = INVALID;
        trueProbability = INVALID;
    }
    
    /**
     * saves this classifier for future classifications
     * @param path the output's path
     * @param naiveBayes the classifier that will be saved
     */
    public static void saveClassifier( String path, 
        NaiveBayesClassifier naiveBayes ) {
        FileOutputStream fileOutputStream = null;
	    ObjectOutputStream objectOutputStream = null;

	    try {
            fileOutputStream = new FileOutputStream( path );
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject( naiveBayes );
        } catch( Exception ex ) {
            ex.printStackTrace();
        } finally {
            if( fileOutputStream != null ) {
                try {
                    fileOutputStream.close();
                } catch( IOException e ) {
                    e.printStackTrace();
                }
            }
            if( objectOutputStream != null ) {
                try {
                    objectOutputStream.close();
                } catch( IOException e ) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * returns a NaiveBayes classifier
     * @param path the path of the classifier
     * @return a NaiveBayes classifier
     */
    public static NaiveBayesClassifier loadClassifier( String path ) 
        throws Exception {
        FileInputStream fileInputStream = new FileInputStream( path );
        ObjectInputStream outputInputStream = 
            new ObjectInputStream( fileInputStream );
        NaiveBayesClassifier naiveBayes =
            ( NaiveBayesClassifier ) outputInputStream.readObject();
        outputInputStream.close();
        fileInputStream.close();
        return naiveBayes;
    }
    
    /**
     * adds a positive training example to the classifier
     * @param example example that will be added
     */
    public void setPositiveExample( String example ) {
        numberPositiveExample++;
        setExample( example, true );
    }
    
    /**
     * adds a negative training example to the classifier
     * @param example example that will be added
     */
    public void setNegativeExample( String example ) {
        numberNegativeExample++;
        setExample( example, false );
    }
    
    /**
     * returns the example's classification
     * @param stringToClassify example that will be evaluated
     * @return the example's classification
     */
    public int classify( String stringToClassify ) {
        int numberExamples = numberPositiveExample + numberNegativeExample;
        double percentTrue = ( double ) ( numberPositiveExample ) /
            ( double ) ( numberExamples );
        double percentFalse = ( double ) ( numberNegativeExample ) /
            ( double ) ( numberExamples );
        falseProbability = probability( stringToClassify, false ) *
            percentFalse;
        trueProbability = probability( stringToClassify, true ) *
            percentTrue;
        
        // returns the classification
        if( trueProbability > falseProbability ) {
            return TRUE;
        } else {
            return FALSE;
        }
    }
    
    /**
     * returns the chance that the example is negative
     * @return the chance that the example is positive or negative
     */
    public double chanceOfPositive() {
        return chance( true );
    }
    
    /**
     * returns the chance that the example is negative
     * @return the chance that the example is positive or negative
     */
    public double chanceOfNegative() {
        return chance( false );
    }
    
    /**
     * returns the chance that the example is positive or negative
     * @param positive indicates if the positive chance must be returned
     * @return the chance that the example is positive or negative
     */
    private double chance( boolean positive ) {
        double total = trueProbability + falseProbability;
        if( total < 0 ) {
            return INVALID;
        } else {
            if( positive ) {
                return trueProbability / total;
            } else {
                return falseProbability / total;
            }
        }
    }
    
    /**
     * returns the probability that the example occurs as a positive or a 
     * negative one
     * @param stringToClassify the example that will be evaluated
     * @param positive returns if the probability to be returned is the chance
     * to be positive
     * @return the probability that the example occurs as a positive or a 
     * negative one
     */
    private double probability( String stringToClassify, boolean positive ) {
        double returnProbability = 1;
        String lowerCase = stringToClassify.toLowerCase();
        String[] words = lowerCase.split( " " );
        
        if( positive ) {
            for( String word : words ) {
                returnProbability *= bagOfWords.probabilityAsPositive( word );
            }
        } else {
            for( String word : words ) {
                returnProbability *= bagOfWords.probabilityAsNegative( word );
            }
        }
        return returnProbability;
    }
    
    /**
     * adds a training example to the classifier
     * @param example example that will be added
     * @param positive indicates if the example is positive
     */
    private void setExample( String example, boolean positive ) {
        String lowerCase = example.toLowerCase();
        String[] words = lowerCase.split( " " );
        for( String word : words ) {
            if( positive ) {
                bagOfWords.setPositive( word );
            } else {
                bagOfWords.setNegative( word );
            }
        }
    }
}
