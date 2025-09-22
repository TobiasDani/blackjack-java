import java.util.ArrayList;
import java.util.Random;

public class Deck {
    private final String[] suits = { "CLUBS", "DIAMONDS", "HEARTS", "SPADES" };
    private final String[] ranks = { "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "JACK", "QUEEN", "KING", "ACE" };
    private ArrayList<Card> cards = new ArrayList<>();

    public Deck() {
        createDeck();
    }

    private void createDeck() {
        for (String suit : suits) {
            for (String rank : ranks) {
                Card card = new Card(suit, rank);
                cards.add(card);
            }
        }
    }

    public void shuffle() {
        Random rand = new Random();

        // Fisher-Yates algorithm for shuffle
        for (int i = cards.size() - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);

            Card temp = cards.get(i);
            cards.set(i,cards.get(j));
            cards.set(j, temp);
        }
    }

    public Card drawCard() {
        if (!cards.isEmpty()) {
            return cards.remove(cards.size()-1);
        }
        return null;
    }
}
