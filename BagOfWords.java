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

import java.io.Serializable;
import java.util.HashMap;

/**
 * this class is used to train or to evaluate on a Naive Bayes Classifier
 * @author vitorcezar
 */
public class BagOfWords implements Serializable {
   
    /**
     * mapping for the words the their informations
     */
    private final HashMap< String, WordInformation > wordsMap;
    
    /**
     * number of words on a positive vocabulary
     */
    private int positiveVocabulary;
    
    /**
     * the number of words on a negative vocabulary
     */
    private int negativeVocabulary;
    
    /**
     * this value won't make a difference on naive bayes execution
     */
    private static final int DEFAULT_VALUE = 1;
    
    /**
     * the vocabularies must be zero initially
     */
    public BagOfWords() {
        wordsMap = new HashMap<>();
        negativeVocabulary = 0;
        positiveVocabulary = 0;
    }
    
    /**
     * indicates that a word is present on a positive document
     * @param word word that is being evaluated
     */
    public void setPositive( String word ) {
        positiveVocabulary++;
        addPosOrNeg( word, true );
    }
    
    /**
     * indicates that a word is present on a negative document
     * @param word word that is being evaluated
     */
    public void setNegative( String word ) {
        negativeVocabulary++;
        addPosOrNeg( word, false );
    }
    
    /**
     * returns the probability that the word will occur on a negative document
     * @param word word that is being evaluated
     * @return the probability that the word will occur on a negative document
     */
    public double probabilityAsNegative( String word ) {
        if( wordsMap.containsKey( word ) ) {
            WordInformation wordInformation = wordsMap.get( word );
            return ( double ) ( 1 + wordInformation.getNegativeAmount() ) /
                ( ( double ) ( 2 * negativeVocabulary + positiveVocabulary ) );
        } 
        return DEFAULT_VALUE;
    }
    
    /**
     * returns the probability that the word will occur on a positive document
     * @param word word that is being evaluated
     * @return the probability that the word will occur on a positive document
     */
    public double probabilityAsPositive( String word ) {
        if( wordsMap.containsKey( word ) ) {
            WordInformation wordInformation = wordsMap.get( word );
            return ( double ) ( 1 + wordInformation.getPositiveAmount() ) /
                ( ( double ) ( 2 * positiveVocabulary + negativeVocabulary ) );
        }
        return DEFAULT_VALUE;
    }
    
    /**
     * increment the number of times a word appeared on a positive or negative
     * document
     * @param word word whose information will be modified
     * @param positive indicates if the document is a positive one
     */
    private void addPosOrNeg( String word, boolean positive ) {
        if( wordsMap.containsKey( word ) ) {
            WordInformation wordInformation = wordsMap.get( word );
            if( positive ) {
                wordInformation.addPositive();
            } else {
                wordInformation.addNegative();
            }
        } else {
            WordInformation wordInformation = new WordInformation();
            if( positive ) {
                wordInformation.addPositive();
            } else {
                wordInformation.addNegative();
            }
            wordsMap.put( word, wordInformation );
        }
    }
}
