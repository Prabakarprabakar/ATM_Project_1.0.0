package atm;

public class Card {
    private final long cardNo;
    private short pinNo;
    private final String cardType;
    private final String validTHRU;

    public Card(long cardNo, short pinNo, String cardType, String validTHRU) {
        this.cardNo = cardNo;
        this.pinNo = pinNo;
        this.cardType = cardType;
        this.validTHRU = validTHRU;
    }

    public long getCardNumber() {
        return cardNo;
    }

    public boolean setPinNo(short pinNo) {
        String pinValidate = "[0-9]{4}";
        if (String.valueOf(pinNo).matches(pinValidate)) {
            this.pinNo = pinNo;
            return true;
        }
        return false;
    }

    public short getPinNumber() {
        return pinNo;
    }

    public String getCardType() {
        return cardType;
    }

    public String getValidTHRU() {
        return validTHRU;
    }

}