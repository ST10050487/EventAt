package za.co.varsitycollage.st10050487.eventat.Fragments;

public class CardInfo {
    public String cardName;
    public String cardNumber;
    public String expiryDate;
    public String cvv;
    public String username;

    public CardInfo() {
        // Default constructor required for calls to DataSnapshot.getValue(CardInfo.class)
    }

    public CardInfo(String cardName, String cardNumber, String expiryDate, String cvv, String username) {
        this.cardName = cardName;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.username = username;
    }
}