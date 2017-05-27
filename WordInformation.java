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

/**
 * this class will save the information related to each word
 * @author vitorcezar
 */
public class WordInformation implements Serializable {
    
    /**
     * counts how many times the word occurred on a positive document
     */
    private int positive;
    
    /**
     * counts how many times the word occurred on a negative document
     */
    private int negative;
    
    /**
     * the negative and positive values must be zero initially
     */
    public WordInformation() {
        positive = 0;
        negative = 0;
    }
    
    /**
     * the word occurred on a positive document
     */
    public void addPositive() {
        positive++;
    }
    
    /**
     * the word occurred on a negative document
     */
    public void addNegative() {
        negative++;
    }
    
    /**
     * returns how many times the word occurred on a negative document
     * @return how many times the word occurred on a negative document
     */
    public int getNegativeAmount() {
        return negative;
    }
    
    /**
     * returns how many times the word occurred on a positive document
     * @return how many times the word occurred on a positive document
     */
    public int getPositiveAmount() {
        return positive;
    }
}
