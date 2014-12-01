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

//model class for a Flashcard with id, front, back, and deck attributes
@SuppressWarnings("serial")
public class Flashcard implements Serializable {
   private String id;
   private String front;
   private String back;
   private Deck deck;
  
   public static final String FLASHCARD_BUNDLE = "flashcard_bundle";  

   public Flashcard(){
      this.id = "";
      this.front = "";
      this.back = "";
      this.deck = new Deck();
   }
   
   public Flashcard(String id, String name, String info, Deck stack) {
      super();
      this.id = id;
      this.front = name;
      this.back = info;
      this.deck = stack;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getFront() {
      return front;
   }

   public void setfront(String name) {
      this.front = name;
   }

   public String getBack() {
      return back;
   }

   public void setBack(String info) {
      this.back = info;
   }

   public Deck getDeck() {
      return deck;
   }

   public void setDeck(Deck stack) {
      this.deck = stack;
   }

}
