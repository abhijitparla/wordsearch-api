package com.bonzo.wordsearchapi.services;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class WordGridService {
    private class Cooridate{
        int x;
        int y;
        Cooridate(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    private enum Direction {
        EAST, SOUTH, SOUTHEAST, NORTH, WEST, NORTHEAST, NORTHWEST, SOUTHWEST
    }

    public void fillRestAlphabets(char[][] contents) {
        int gridSize = contents[0].length;
        String alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for(int i = 0; i<gridSize; i++){
            for(int j = 0; j<gridSize; j++){
                if(contents[i][j] == '_'){
                    int randomInt = ThreadLocalRandom.current().nextInt(0,alphabets.length());
                    contents[i][j] = alphabets.charAt(randomInt);
                }
            }
        }
    }
    public void displayGrid(char[][] contents){

        int gridSize = contents[0].length;
        for(int i = 0; i<gridSize; i++){
            for(int j = 0; j<gridSize; j++){
                System.out.print(contents[i][j]+" ");
            }
            System.out.println("");
        }
    }

    public char[][] generateGrid(int gridSize, List<String> words){
        List<Cooridate> coordiantes = new ArrayList<>();
        char[][] contents = new char[gridSize][gridSize];
        for(int i = 0; i < gridSize; i++){
            for(int j = 0; j<gridSize;j++){
                coordiantes.add(new Cooridate(i,j));
                contents[i][j] = '_';
            }
        }

        Collections.shuffle(coordiantes);
        for(String word: words){
            for(Cooridate cooridate: coordiantes){
                int x = cooridate.x;
                int y = cooridate.y;

                Direction desiredDirection = getRightDirection(contents,cooridate, word);
                if(desiredDirection != null){
                    switch (desiredDirection) {
                        case EAST:
                            for(char c: word.toCharArray()){
                                contents[x][y++] = c;
                            }
                            break;
                        case SOUTH:
                            for(char c: word.toCharArray()){
                                contents[x++][y] = c;
                            }
                            break;
                        case SOUTHEAST:
                            for(char c: word.toCharArray()){
                                contents[x++][y++] = c;
                            }
                            break;
                        case NORTH:
                            for(char c: word.toCharArray()){
                                contents[x--][y] = c;
                            }
                            break;
                        case WEST:
                            for(char c: word.toCharArray()){
                                contents[x][y--] = c;
                            }
                            break;
                        case SOUTHWEST:
                            for(char c: word.toCharArray()){
                                contents[x++][y--] = c;
                            }
                            break;
                        case NORTHWEST:
                            for(char c: word.toCharArray()){
                                contents[x--][y--] = c;
                            }
                            break;
                        case NORTHEAST:
                            for(char c: word.toCharArray()){
                                contents[x--][y++] = c;
                            }
                            break;
                    }
                    break;
                }
//                    break;
            }
        }
        fillRestAlphabets(contents);
        return contents;
    }

    private Direction getRightDirection(char[][] contents, Cooridate cooridate, String word) {
        List<Direction> directions = Arrays.asList(Direction.values());
        Collections.shuffle(directions);
        for(Direction direction: directions){
            if(doesFit(contents, word, cooridate, direction)){
                return direction;
            }
        }
        return null;
    }

    private boolean doesFit(char[][] contents, String word, Cooridate cooridate, Direction direction) {
        int wordLength = word.length();
        int gridSize = contents.length;
        switch (direction){
            case EAST:
                if(cooridate.y + wordLength > gridSize){
                    return false;
                }
                for(int i = 0; i<wordLength; i++){
                    if(contents[cooridate.x][cooridate.y+i] != '_') return false;
                }
                break;
            case SOUTH:
                if(cooridate.x + wordLength > gridSize){
                    return false;
                }
                for(int i = 0; i<wordLength; i++){
                    if(contents[cooridate.x+i][cooridate.y] != '_') return false;
                }
                break;
            case SOUTHEAST:
                if(cooridate.y + wordLength > gridSize || cooridate.x + wordLength > gridSize){
                    return false;
                }
                for(int i = 0; i<wordLength; i++){
                    if(contents[cooridate.x+i][cooridate.y+i] != '_') return false;
                }
                break;
            case NORTH:
                if(cooridate.x - wordLength < 0 ){
                    return false;
                }
                for(int i = 0; i<wordLength; i++){
                    if(contents[cooridate.x-i][cooridate.y] != '_') return false;
                }
                break;
            case WEST:
                if(cooridate.y - wordLength < 0 ){
                    return false;
                }
                for(int i = 0; i<wordLength; i++){
                    if(contents[cooridate.x][cooridate.y-i] != '_') return false;
                }
                break;
            case NORTHEAST:
                if(cooridate.y + wordLength > gridSize || cooridate.x - wordLength < 0){
                    return false;
                }
                for(int i = 0; i<wordLength; i++){
                    if(contents[cooridate.x-i][cooridate.y+i] != '_') return false;
                }
                break;
            case NORTHWEST:
                if(cooridate.y - wordLength < 0 || cooridate.x - wordLength < 0){
                    return false;
                }
                for(int i = 0; i<wordLength; i++){
                    if(contents[cooridate.x-i][cooridate.y-i] != '_') return false;
                }
                break;
            case SOUTHWEST:
                if(cooridate.y - wordLength < 0 || cooridate.x + wordLength > gridSize){
                    return false;
                }
                for(int i = 0; i<wordLength; i++){
                    if(contents[cooridate.x+i][cooridate.y-i] != '_') return false;
                }
                break;
        }
        return true;
    }

//    private boolean isGridPlacesEmpty(int x, int y, int length) {
//        boolean flag = true;
//        for(int i = 1; i<=length; i++){
//            if(contents[x][y+i] != '_'){
//                flag = false;
//            }
//        }
//        return flag;
//    }
}
