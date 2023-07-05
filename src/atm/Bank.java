package atm;

import java.util.HashMap;
import java.util.Map;

public class Bank {
    private static final HashMap<Long,BankAccount> BANK_ACCOUNT_DETAILS = new HashMap<>();
    private static final HashMap<Long,Card> CARD_DETAILS = new HashMap<>();
//    public void loadUsers() {
//        CARD_DETAILS.put(1231231231231231L,new Card(1231231231231231L, (short) 1234, "Debit", "12/24"));
//        CARD_DETAILS.put(2342342342342342L,new Card(2342342342342342L, (short) 2345, "Credit", "08/25"));
//        CARD_DETAILS.put(3453453453453453L,new Card(3453453453453453L, (short) 3456, "Debit", "01/26"));
//        CARD_DETAILS.put(4564564564564564L,new Card(4564564564564564L, (short) 4567, "credit", "07/28"));
//        CARD_DETAILS.put(5675675675675675L,new Card(5675675675675675L, (short) 5678, "credit", "03/25"));
//
//        BANK_ACCOUNT_DETAILS.put(1231231231231231L,new BankAccount(345000100123456L, "Prabakar", "Canara Bank", 7708402328L, 4000.00, 1231231231231231L));
//        BANK_ACCOUNT_DETAILS.put(2342342342342342L,new BankAccount(121310100020311L, "Sharmili", "SBI Bank", 7708402323L, 30024.00, 2342342342342342L));
//        BANK_ACCOUNT_DETAILS.put(3453453453453453L,new BankAccount(321200010012312L, "Jesi", "Indian Bank", 7708402352L, 325421.00, 3453453453453453L));
//        BANK_ACCOUNT_DETAILS.put(4564564564564564L,new BankAccount(456023432030401L, "Dhanapaul", "ICICI Bank", 7708402377L, 122024.00, 4564564564564564L));
//        BANK_ACCOUNT_DETAILS.put(5675675675675675L,new BankAccount(456023432030402L, "Shanthi", "ICICI Bank", 7708402373L, 100234.00, 5675675675675675L));
//    }
    public HashMap<Long,Card> accessCardDetails() {return CARD_DETAILS;}
    public HashMap<Long,BankAccount> accessBankAccountDetails()
    {
        return BANK_ACCOUNT_DETAILS;
    }
    public Card getCard(long cardNumber){
        if(CARD_DETAILS.containsKey(cardNumber)) {
            return CARD_DETAILS.get(cardNumber);
        }
        return null;
    }

    public BankAccount getBankAccount(Card card) {
        if(BANK_ACCOUNT_DETAILS.containsKey(card.getCardNumber())) {
                return BANK_ACCOUNT_DETAILS.get(card.getCardNumber());
        }
        return null;
    }

    public BankAccount getBankAccount(long accountNumber) {
        for (Map.Entry<Long,BankAccount> bankAccount :BANK_ACCOUNT_DETAILS.entrySet()) {
            if(accountNumber==bankAccount.getValue().getAccountNumber()){
                return bankAccount.getValue();
            }
        }
        return null;
    }
}
