/**
 * Author: Uyen Nguyen
 * Date started: 
 * Date Completed: IP
 * Peer Review:  
 * Team members: Buddy Corp
 * Contribution: Uyen Nguyen
 */

package com.comp490.studybuddy.flashcards;

import java.io.Serializable;
import java.util.List;

//model class for a deck with id, name and flashcardDeck attributes
@SuppressWarnings("serial")
public class Deck implements Serializable {
      
   public static final String DECK_BUNDLE = "deck_bundle";  
   
   private String id;
   private String name;
   private List<Flashcard> flashcardDeck;

   public Deck() {
      this.id = "";
      this.name = "";
      this.flashcardDeck = null;
   }

   public Deck(String id, String title, List<Flashcard> flashcardStack) {
      super();
      this.id = id;
      this.name = title;
      this.flashcardDeck = flashcardStack;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String title) {
      this.name = title;
   }

   public List<Flashcard> getFlashcardDeck() {
      return flashcardDeck;
   }

   public void setFlashcardDeck(List<Flashcard> flashcardStack) {
      this.flashcardDeck = flashcardStack;
   }
   
}
