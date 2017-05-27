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

/**
 * 
 * @author vitorcezar
 */
public class NaiveBayes {

    private static void printArgumentError() {
        System.out.println( "The arguments passed are incorrect" );
        System.exit( 1 );
    }
    
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
        } else {
            printArgumentError();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main( String[] args ) {
        verifyArguments( args );
        
        if( args[ 0 ].equals( "-t" ) ) {
            NaiveBayesClassifier naiveBayes = new NaiveBayesClassifier();
            naiveBayes.setPositiveExample( "I'm happy today" );
            naiveBayes.setNegativeExample( "I'm sad today" );
            NaiveBayesClassifier.saveClassifier( args[ 2 ], naiveBayes );
        } else {
            try {
                NaiveBayesClassifier naiveBayes = 
                    NaiveBayesClassifier.loadClassifier( args[ 1 ] );
                if( naiveBayes.classify( "Happy I am" ) == NaiveBayesClassifier.TRUE ) {
                    System.out.printf( "Positive example. Chance %f\n", 
                        naiveBayes.chanceOfPositive() );
                    System.out.printf( "Change of negative is %f\n", 
                        naiveBayes.chanceOfNegative() );
                }
            } catch( Exception e ) {
                System.out.println( "The path is incorrect" );
                System.exit( 1 );
            }
        }
    }
}
