package atm;

import javax.xml.crypto.Data;

public class Main {
    private final static byte FAST_CASH = 1;
    private final static byte WITH_DRAW = 2;
    private final static byte BALANCE_ENQUIRY = 3;
    private final static byte DEPOSIT_AMOUNT = 4;
    private final static byte MINI_STATEMENT = 5;
    private final static byte CHANGE_PIN = 6;
    private final static byte EXIT = 7;
    private final static byte CARD_NUMBER_LENGTH = 16;
    private final static byte INSERT_ATM_CARD = 1;
    private final static byte CARD_LESS_DEPOSIT = 2;
    public static byte language;

    public static void main(String[] args) {
        Bank bank = new Bank();
//        FileClass file = new FileClass();
        DataBase dataBase=new DataBase();
        do {
            System.out.println("\n\t\t\t\t\t$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            System.out.println("\t\t\t\t\t$    Welcome to ZoHo ATM    $");
            System.out.println("\t\t\t\t\t$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n");
            int entry;
            System.out.println("*********************************************************************");
            System.out.println("*\t1.Insert Your ATM Card\t\t1.ATM கார்டை செலுத்தவும்\t\t\t\t*\n*\t2.CardLess Deposit \t\t\t2.கார்டு இல்லாமல் பணம் செலுத்த\t\t\t*");
            System.out.println("*********************************************************************");
            entry = Util.getInstance().getIntInput();
            if (Admin.ADMIN_PANEL == entry) {
                Admin admin = new Admin();
                admin.operateATM();
            } else if (INSERT_ATM_CARD == entry) {
                language = Util.getInstance().chooseLanguage();
                long cardNo;
                boolean cardNoFlag=true;
                do {
                    if (language == Constant.TAMIL_LANGUAGE)
                        System.out.print("உங்கள் 16 இலக்க எண்ணை செலுத்தவும் :");
                    else System.out.print("Enter the 16 Digit Card No :");
                    cardNo = Util.getInstance().longInput();
                    if (String.valueOf(cardNo).length() == CARD_NUMBER_LENGTH)
                        cardNoFlag=false;
                    else {
                        if (language == Constant.TAMIL_LANGUAGE)
                            System.out.println("\u001B[31m சரியான எண்ணை உள்ளிடவும்...\u001B[0m");
                        else System.out.println("\u001B[31m Enter the Valid Card Number...\u001B[0m");
                    }
                } while (cardNoFlag);
                short pinNo = Util.getInstance().getShortInput(language);
//                file.readBankDetailsFromCSV(cardNo);
                dataBase.readBankDataFromSQLUsingCardNumber(cardNo);
                Card card = bank.getCard(cardNo);
                if (card!=null && card.getPinNumber() == pinNo && Atm.getInstance().dateValidate(card.getValidTHRU())) {
                    BankAccount bankAccount = bank.getBankAccount(card);
                    byte choose;
                    boolean homPageFlag = true;
                    do {
                        System.out.println("\n*********************************");
                        if (language == Constant.TAMIL_LANGUAGE)
                            System.out.println("*\t1.விரைவு பணம்\t\t\t\t*\n*\t2.பணம் எடுக்க\t\t\t\t*\n*\t3.இருப்புத்தொகை அறிய\t\t\t*\n*\t4.பணம் செலுத்த\t\t\t\t*\n*\t5.பண பரிவர்த்தனை பட்டியல்\t\t*\n*\t6.குறியீட்டு எண்ணை மாற்ற\t\t*\n*\t7.வெளியேறு\t\t\t\t\t*");
                        else
                            System.out.println("*\t1.Fast Cash\t\t\t\t\t*\n*\t2.Withdraw\t\t\t\t\t*\n*\t3.Balance Enquiry\t\t\t*\n*\t4.Deposit\t\t\t\t\t*\n*\t5.Mini Statement\t\t\t*\n*\t6.Change Pin\t\t\t\t*\n*\t7.Exit\t\t\t\t\t\t*");
                        System.out.println("*********************************\n");
                        choose = Util.getInstance().getByteInput();
                        switch (choose) {
                            case FAST_CASH -> {
                                Atm.getInstance().fastCashWithdraw(bankAccount, language);
                                boolean choice = Atm.getInstance().doYouWantToCheckBalance(language);
                                if (choice) Atm.getInstance().checkBalance(bankAccount, language);
                                Util.getInstance().removeCard(language);
                                homPageFlag = false;
                            }
                            case WITH_DRAW -> {
                                Atm.getInstance().withdrawAmount(bankAccount, language);
                                boolean choice = Atm.getInstance().doYouWantToCheckBalance(language);
                                if (choice) Atm.getInstance().checkBalance(bankAccount, language);
                                Util.getInstance().removeCard(language);
                                homPageFlag = false;
                            }
                            case BALANCE_ENQUIRY -> {
                                Atm.getInstance().checkBalance(bankAccount, language);
                                Util.getInstance().removeCard(language);
                                homPageFlag = false;
                            }
                            case DEPOSIT_AMOUNT -> {
                                int[] value = new int[3];
                                double amount = Util.getInstance().getDepositAmountInput(value, language);
                                Atm.getInstance().depositAmount(bankAccount, language, value, amount);
                                boolean choice = Atm.getInstance().doYouWantToCheckBalance(language);
                                if (choice) Atm.getInstance().checkBalance(bankAccount, language);
                                Util.getInstance().removeCard(language);
                                homPageFlag = false;
                            }
                            case MINI_STATEMENT -> {
                                Atm.getInstance().viewStatement(bankAccount);
                                Util.getInstance().removeCard(language);
                                homPageFlag = false;
                            }
                            case CHANGE_PIN -> {
                                long phoneNo = Util.getInstance().getPhoneNo(language);
                                short newPin = Util.getInstance().getNewPin(language);
                                Atm.getInstance().changePinNumber(bankAccount, language, phoneNo, newPin);
                                Util.getInstance().removeCard(language);
                                homPageFlag = false;
                            }
                            case EXIT ->
                                    homPageFlag = false;
                            default -> {
                                if (language == Constant.TAMIL_LANGUAGE)
                                    System.out.println("\u001B[31m சரியான தேர்வை உள்ளிடவும்...\u001B[0m");
                                else System.out.println("\u001B[31m Enter the Valid Choice...\u001B[0m");
                            }
                        }
                    } while (homPageFlag);
                } else {
                    if (language == Constant.TAMIL_LANGUAGE)
                        System.out.println("\u001B[31m தவறான குறியீட்டு எண்... \u001B[0m");
                    else System.out.println("\u001B[31m Invalid pin Number...\u001B[0m");
                }
                if (language == Constant.TAMIL_LANGUAGE)
                    System.out.println("\n****** இந்த ATM யை பயன்படுத்தியதற்கு நன்றி ******");
                else System.out.println("\n******** Thank you for Using Our ATM ********");
                System.out.println("\t\t\t\t\t\033[2J\033[H\033[3m\uD83D\uDE4F\033[0m");
                System.out.println("*____*____*____*____*____*____*____*____*____*____*\n\n");
            }
            //For Card Less Deposit
            else if (CARD_LESS_DEPOSIT == entry) {
                System.out.println("\nCard Less Deposit");
                System.out.println("-----------------\n");
                Atm.getInstance().cardLessDeposit(language);
                if (language == Constant.TAMIL_LANGUAGE)
                    System.out.println("\n****** இந்த ATM யை பயன்படுத்தியதற்கு நன்றி ******");
                else System.out.println("\n******** Thank you for Using Our ATM ********");
                System.out.println("\t\t\t\t\t\033[2J\033[H\033[3m\uD83D\uDE4F\033[0m");
                System.out.println("*____*____*____*____*____*____*____*____*____*____*\n\n");
            } else {
                System.out.println("\u001B[31m Enter the Valid Choice...\u001B[0m");
            }
        } while (true);
    }

}
