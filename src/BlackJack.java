import java.util.ArrayList;
import java.util.Scanner;

public class BlackJack {
    private static final Scanner SCN = new Scanner(System.in);

    public static void play() throws InterruptedException {
        boolean playing = true;

        while (playing) {
            round();

            // Wait a second between rounds
            Thread.sleep(1000);

            // Ask player if they want to play again
            System.out.println("\nDo you want to play again? (Y)es or (N)o?");
            String in = SCN.nextLine().toLowerCase();

            // Decide based on first letter
            if (!in.startsWith("y")) {
                playing = false; // exit loop
                System.out.println("Thanks for playing!");
            }
        }
    }

    public static void round() {
        // Creates and shuffles a deck
        Deck deck = new Deck();
        deck.shuffle();

        // Declaring hands
        ArrayList<Card> houseCards = new ArrayList<>();
        ArrayList<Card> playerCards = new ArrayList<>();

        // Drawing starter cards
        houseCards.add(deck.drawCard());
        houseCards.add(deck.drawCard()); // Hidden
        playerCards.add(deck.drawCard());
        playerCards.add(deck.drawCard());

        // Output for player
        System.out.println("\n== New Round ==");
        System.out.println("-- Dealer shows: " + houseCards.get(0).getRank() + " + [HIDDEN]");
        System.out.println("-- Your cards : " + playerCards.get(0).getRank() + " and " + playerCards.get(1).getRank() + "  -> total (" + handValue(playerCards) + ")");

        // Natural blackjack checks
        boolean playerBJ = (playerCards.size() == 2 && handValue(playerCards) == 21);
        boolean dealerBJ = (houseCards.size() == 2 && handValue(houseCards) == 21);
        if (playerBJ || dealerBJ) {
            System.out.println("\n-- Dealer reveals: " + houseCards.get(0).getRank() + " and " + houseCards.get(1).getRank() + "  -> total (" + handValue(houseCards) + ")");
            if (playerBJ && dealerBJ) {
                System.out.println("\n--- Push (both have Blackjack) ---");
            } else if (playerBJ) {
                System.out.println("\n--- Blackjack! You win! ---");
            } else {
                System.out.println("\n--- Dealer has Blackjack. Dealer wins. ---");
            }
            return;
        }

        // Player turn: (H)it or (S)tand
        boolean hitOrNot;
        do {
            System.out.println("\n(H)it or (S)tand?");
            String in = SCN.nextLine().toLowerCase();
            hitOrNot = in.startsWith("h");

            if (hitOrNot) {
                playerCards.add(deck.drawCard());
                System.out.println("-- You drew : " + playerCards.get(playerCards.size() - 1).getRank() + "  -> total (" + handValue(playerCards) + ")");

                if (handValue(playerCards) > 21) {
                    System.out.println("\n--- Bust! Dealer wins. ---");
                    return; // round ends
                }

            } else if (!in.startsWith("s")) {
                // invalid input
                System.out.println("Please type H or S...");
                hitOrNot = true;
            }
        } while (hitOrNot);

        // Dealer reveals and plays
        System.out.println("\n-- Dealer reveals: " + houseCards.get(0).getRank() + " and " + houseCards.get(1).getRank() + "  -> total (" + handValue(houseCards) + ")");

        // Dealer draws to 17
        while (handValue(houseCards) < 17) {
            Card card = deck.drawCard();
            houseCards.add(card);
            System.out.println("-- Dealer draws: " + card.getRank() + "  -> total (" + handValue(houseCards) + ")");

            if (handValue(houseCards) > 21) {
                // If over 21
                System.out.println("\n--- Dealer busts. You win! ---");
                return;
            }
        }

        // Final result
        int finalHouseHand = handValue(houseCards);
        int finalPlayerHand = handValue(playerCards);

        // Output
        System.out.println("\n== Final Totals ==");
        System.out.println("Dealer: " + finalHouseHand);
        System.out.println("Player: " + finalPlayerHand);

        if (finalHouseHand > finalPlayerHand) {
            System.out.println("\n--- Dealer wins. ---");
        } else if (finalPlayerHand > finalHouseHand) {
            System.out.println("\n--- You win! ---");
        } else {
            System.out.println("\n--- Push (tie). ---");
        }
    }

    private static int handValue(ArrayList<Card> hand) {
        int sum = 0;
        int aces = 0;

        // Base values
        for (Card card : hand) {
            switch (card.getRank()) {
                case "TWO": sum += 2; break;
                case "THREE": sum += 3; break;
                case "FOUR": sum += 4; break;
                case "FIVE": sum += 5; break;
                case "SIX": sum += 6; break;
                case "SEVEN": sum += 7; break;
                case "EIGHT": sum += 8; break;
                case "NINE": sum += 9; break;
                case "TEN": sum += 10; break;
                case "JACK": sum += 10; break;
                case "QUEEN": sum += 10; break;
                case "KING": sum += 10; break;
                case "ACE": aces++; break;
                default: break;
            }
        }

        // Add aces as 11, or 1 if busted
        for (int i = 0; i < aces; i++) sum += 11;
        while (sum > 21 && aces > 0) { sum -= 10; aces--; }

        return sum;
    }
}
