

#  OOP Homework  - GwentStone

<div align="center"><img src="https://tenor.com/view/witcher3-gif-9340436.gif" width="500px"></div>

#### Assignment Link: [https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/tema](https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/tema)


## Skel Structure

* src/
  * checker/ - checker files
  * fileio/ - contains classes used to read data from the json files
  * main/
    * **Main** - the Main class runs the checker on your implementation. Add the entry point to your implementation in it. Run Main to test your implementation from the IDE or from command line.
    * **Test** - run the main method from Test class with the name of the input file from the command line and the result will be written
    to the out.txt file. Thus, you can compare this result with ref.
    * **Cards** - contains variables that describe card's attributes and methods used when a card attacks (for Minion card type), uses effect 
    (for Environment card type) or uses ability (for Minion card type)
    * **Deck** - contains useful methods for the first part of the game, when each player chooses the deck, such as 
      * *chooseDeck*
      * *setDecks*
    * **Error** - contains useful methods for creating ObjectNodes used for output, such us 
      * *errorPlaceCard*, 
      * *errorCardUsesAttack*, 
      * *errorUsesEnvironmentCard*, 
      * *errorCardUsesAbility*, 
      * *errorAttackHero*, 
      * *errorHeroAttack*.
    * **GameTable** - contains variables that describe aspects about the table, such as card on table (ArrayList <ArrayList <Cards> > row)
    and the current player. This class also has methods used for actions that take place on the table: 
      * *placeCardOnTable*, 
      * *unfrozeCards*, 
      * *unuseCards*, 
      * *cardUsesAttack*,
      * *cardUsesEffect*, 
      * *cardAttackHero*,
      * *cardUsesAbility*,
      * *heroUseAbility*.
      
       Beside action methods, here can be found some methods for output (debugging)
      * *getFrozenCardsOnTable*,
      * *getCardAtPosition*
      * *getCardsOnTable*
    * **Hero** - contains variables that describe Hero card's attributes and one method *heroEffect*, called when a hero uses its effect
    * **Player** - used to describe a player (a player has a deck, some cards in hand, a hero, some mana, a front and a back row) and a method for debugging
      * *command*
    * **UsefulClass** - as its name is, this class has useful methods used to verify is a row contains something, is a card is Tank, or returns 
    strings for each error message
      * *rowHasTank*
      * *existsCardInRow*
      * *cardIsTank*
      * *generateString*

<div align="center"><img src="https://i.pinimg.com/564x/87/45/6e/87456efa2af00b089bf68351908cdad8.jpg" width="500px"></div>



