package atm;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Atm {
    private final static int BELOW_DEPOSIT = 35000;
    private final static int FIXED_AMOUNT = 2000000;
    private final static byte FIVE_HUNDRED = 1;
    private final static byte ONE_THOUSAND = 2;
    private final static byte TWO_THOUSAND_AND_FIVE_HUNDRED = 3;
    private final static byte FIVE_THOUSAND = 4;
    private final static byte SEVEN_THOUSAND_AND_FIVE_HUNDRED = 5;
    private final static byte TEN_THOUSAND = 6;
    private final static byte MAX_STATEMENTS = 10;
    private final static byte MIN_STATEMENT = 1;
    private final static byte YES = 1;
    private final static byte NO = 2;

    private static Atm object = null;

    public static Atm getInstance() {
        if (object == null)
            object = new Atm();
        return object;
    }

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy    HH:mm:ss");
    Date date = new Date();

    public void changePinNumber(BankAccount bankAccount, byte language, long phoneNo, short newPin) {
        if (phoneNo == bankAccount.getPhoneNumber()) {
            if (bankAccount.card.setPinNo(newPin)) {
                //Rewrite in file
                FileClass file=new FileClass();
                file.changePin(newPin, bankAccount);
                if (language == Constant.TAMIL_LANGUAGE) System.out.print("வெற்றிகரமாக குறியீட்டு எண் மாற்றப்பட்டது\n");
                else System.out.println("Pin changed Successfully\n");
            } else {
                if (language == Constant.TAMIL_LANGUAGE)
                    System.out.print("நான்கு எழுத்து எண்ணை செலுத்தவும் பின்னர் முயற்சிக்கவும்...\n");
                else System.out.println("Enter only four digit try again...\n");
            }
        } else {
            if (language == Constant.TAMIL_LANGUAGE)
                System.out.print("எதோ தவறாக உள்ளது மீண்டும் பின்னர் முயற்சிக்கவும்...\n");
            else System.out.println("Oops!.Something is Wrong try again after Sometimes...\n");
        }
    }

    public boolean dateValidate(String validTHRU) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yy");
        String today = simpleDateFormat.format(date);
        Date myDate = null;
        try {
            date = simpleDateFormat.parse(today);
            myDate = simpleDateFormat.parse(validTHRU);
        } catch (Exception ignored) {
        }
        assert myDate != null;
        return myDate.equals(date) || myDate.after(date);
    }

    public String getAccountNoMasking(long accountNo) {
        String string = String.valueOf(accountNo);
        StringBuilder value = new StringBuilder();
        for (byte i = 0; i < string.length(); i++) {
            if (i >= 0 && i < 9)
                value.append("x");
            else
                value.append(string.charAt(i));
        }
        return value.toString();
    }

    public String getCardNoMasking(long cardNo) {
        String string = String.valueOf(cardNo);
        StringBuilder value = new StringBuilder();
        for (byte i = 0; i < string.length(); i++) {
            if (i > 7 && i < 12)
                value.append("x");
            else
                value.append(string.charAt(i));
        }
        return value.toString();
    }

    public void cashDepositReceipt(int[] value, double amount) {
        System.out.println("\n__________Cash Deposit___________\n");
        for (byte i = 0; i < 3; i++) {
            System.out.println(Admin.cash[i] + " X " + value[i] + " = " + Admin.cash[i] * value[i]);
        }
        System.out.println("\nTotal Deposit Amount : " + amount);
        System.out.println("\n________________________________");
    }

    public String getReferenceNo() {
        StringBuilder referanceNo = new StringBuilder();
        String dateTime = simpleDateFormat.format(date);
        for (byte i = (byte) (dateTime.length() - 1); i > 0; i--) {
            if (dateTime.charAt(i) > 47 && dateTime.charAt(i) < 58)
                referanceNo.append(dateTime.charAt(i));
        }
        return referanceNo.toString();
    }

    public void fastCashWithdraw(BankAccount bankAccount, byte language) {
        Admin admin = new Admin();
        byte fcash;
        double amount = 0;
        boolean flag = true;
        do {
            if (language == Constant.TAMIL_LANGUAGE)
                System.out.println("தொகையை தேர்வு செய்யவும்\n1.500\n2.1000\n3.2500\n4.5000\n5.7500\n6.10000");
            else System.out.println("Choose the Amount\n1.500\n2.1000\n3.2500\n4.5000\n5.7500\n6.10000");
            fcash = Util.getInstance().getByteInput();
            switch (fcash) {
                case FIVE_HUNDRED -> {
                    amount = 500;
                    flag = false;
                }
                case ONE_THOUSAND -> {
                    amount = 1000;
                    flag = false;
                }
                case TWO_THOUSAND_AND_FIVE_HUNDRED -> {
                    amount = 2500;
                    flag = false;
                }
                case FIVE_THOUSAND -> {
                    amount = 5000;
                    flag = false;
                }
                case SEVEN_THOUSAND_AND_FIVE_HUNDRED -> {
                    amount = 7500;
                    flag = false;
                }
                case TEN_THOUSAND -> {
                    amount = 10000;
                    flag = false;
                }
                default -> {
                    if (language == Constant.TAMIL_LANGUAGE)
                        System.out.println("\u001B[31m சரியான தேர்வை உள்ளிடவும்...");
                    else
                        System.out.println("\u001B[31m Enter the Valid Choice... \u001B[0m");
                }
            }
        } while (flag);
        if (bankAccount.getBalanceAmount() < amount) {
            if (language == Constant.TAMIL_LANGUAGE)
                System.out.println("\u001B[31m *******போதிய இருப்பு இல்லை*******\u001B[0m");
            else System.out.println("\u001B[31m ******Insufficient Balance******\u001B[0m");
        } else {
            if (admin.checkAvailableCashOnAtm(amount)) {
                bankAccount.setBalanceAmount((bankAccount.getBalanceAmount() - amount));
                // Rewrite the Balance Amount
                FileClass file=new FileClass();
                file.withdrawAmount(amount,bankAccount);
                try {
                    addStatement(bankAccount, "Fast Cash", amount, simpleDateFormat.format(date));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("\n******************************");
                if (language == Constant.TAMIL_LANGUAGE) System.out.println("*  தொகையை பெற்றுக்கொள்ளவும்  *");
                else System.out.println("*    Collect your Amount    *");
                System.out.println("******************************\n");
            }
        }
    }

    public void addStatement(BankAccount bankAccount, String transectionType, double amount, String format) throws IOException {
        //bankAccount.statement.add(1, "\t" + transectionType + "\t\t\t" + amount + "\t\t\t" + bankAccount.getBalanceAmount() + "\t\t " + format);
        FileClass file=new FileClass();
        file.addStatementsToFile(bankAccount,transectionType,amount,format);
    }

    public void viewStatement(BankAccount bankAccount) {
        FileClass file=new FileClass();
        file.readStatementsFromFile(bankAccount);
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("\t\t\t\t\t\t\t\t\tZoHo ATM LTD\n\n\tMathalamparai - Tenkasi\t\t\t\t\t\t\tATM ID 2807");
        System.out.println("\tREF Number     : " + Atm.getInstance().getReferenceNo());
        System.out.println("\tCard Number    : " + Atm.getInstance().getCardNoMasking(bankAccount.card.getCardNumber()));
        System.out.println("\tAccount Number : " + Atm.getInstance().getAccountNoMasking(bankAccount.getAccountNumber()) + "\n");
        if (bankAccount.statement.size() == MIN_STATEMENT) {
            System.out.println("You have not done any transaction...");
        } else if (bankAccount.statement.size() > MAX_STATEMENTS) {
            for (int i = 0; i <= MAX_STATEMENTS; i++) {
                if(i!=0) {
                    String[] array = bankAccount.statement.get(i).split(",");
                    System.out.println("\t" + array[0] + "\t\t\t" + array[1] + "\t\t\t" + array[2] + "\t\t " + array[3]);
                }else{
                    System.out.println(bankAccount.statement.get(0));
                }
            }
        } else {
            for (int i = 0; i < bankAccount.statement.size(); i++) {
                if(i!=0) {
                    String[] array = bankAccount.statement.get(i).split(",");
                    System.out.println("\t" + array[0] + "\t\t\t" + array[1] + "\t\t\t" + array[2] + "\t\t " + array[3]);
                }else{
                    System.out.println(bankAccount.statement.get(0));
                }
            }
        }

        System.out.println("------------------------------------------------------------------------------------");
    }

    public void depositAmount(BankAccount bankAccount, byte language, int[] value, double amount) {
        Admin admin = new Admin();
        if (admin.availableCashOnATM() < FIXED_AMOUNT) {
            if (amount >= BELOW_DEPOSIT) {
                if (language == Constant.TAMIL_LANGUAGE)
                    System.out.println("\u001B[31m தயவுசெய்து ரூபாய் 35000க்கு கீழே உள்ள தொகையை செலுத்தவும் \u001B[0m");
                else System.out.println("\u001B[31m Please Deposit Amount Below RS.35000 \u001B[0m");
            } else {
                admin.setCashToATM(value);
                bankAccount.setBalanceAmount((bankAccount.getBalanceAmount() + amount));
                // Rewrite the Balance in file
                FileClass file=new FileClass();
                file.depositAmount(amount,bankAccount);
                try {
                    addStatement(bankAccount, "Deposit  ", amount, simpleDateFormat.format(date));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                cashDepositReceipt(value, amount);
            }
        } else {
            System.out.println("Something went wrong!");
        }
    }

    public void withdrawAmount(BankAccount bankAccount, byte language) {
        Admin admin = new Admin();
        double amount;
        boolean flag = false;
        do {
            if (language == Constant.TAMIL_LANGUAGE) System.out.print("தொகையை உள்ளிடவும் :");
            else System.out.print("Enter the Amount :");
            amount = Util.getInstance().getDoubleInput();
            if (amount % 100 != 0 || amount > 20001 || amount <= 0) {
                if (language == Constant.TAMIL_LANGUAGE)
                    System.out.println("\u001B[31m *சரியான தொகையை செலுத்தவும்*\u001B[0m \n");
                else System.out.println("\u001B[31m *Enter the Valid Amount*\u001B[0m \n");
            } else if (bankAccount.getBalanceAmount() < 500 || bankAccount.getBalanceAmount() < amount) {
                if (language == Constant.TAMIL_LANGUAGE)
                    System.out.println("\u001B[31m *போதிய தொகை இல்லை இருப்பை சரி பார்க்கவும் *\u001B[0m \n");
                else System.out.println("\u001B[31m *Invalid Amount please check your Balance*\u001B[0m \n");
                flag = true;
            } else {
                if (admin.checkAvailableCashOnAtm(amount)) {
                    bankAccount.setBalanceAmount(bankAccount.getBalanceAmount() - amount);
                    // Rewrite the Balance Amount
                    FileClass file=new FileClass();
                    file.withdrawAmount(amount,bankAccount);
                    try {
                        addStatement(bankAccount, "Withdraw ", amount, simpleDateFormat.format(date));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("\n******************************");
                    if (language == Constant.TAMIL_LANGUAGE) System.out.println("*  தொகையை பெற்றுக்கொள்ளவும்  *");
                    else System.out.println("*    Collect your Amount    *");
                    System.out.println("******************************\n");
                }
                flag = true;
            }
        } while (!flag);
    }

    public void checkBalance(BankAccount bankAccount, byte language) {
        System.out.println("$$@@$$@@%%@@$$@@$$$$@@$$@@%%@@$$@@$$\n");
        if (language == Constant.TAMIL_LANGUAGE) System.out.println("இருப்பு தொகை :" + bankAccount.getBalanceAmount());
        else System.out.println("Available Balance is :" + bankAccount.getBalanceAmount());
        System.out.println("\n$$@@$$@@%%@@$$@@$$$$@@$$@@%%@@$$@@$$");
    }

    public void cardLessDeposit(byte language) {
        int[] value = new int[3];
        double amount;
        String validateAccountNo = "^[0-9]{15}";
        long accountNo;
        byte choice;
        boolean flag = false;
        do {
            if (language == Constant.TAMIL_LANGUAGE) System.out.print("கணக்கு எண்ணை செலுத்தவும் :");
            else System.out.print("Enter the Account No :");
            accountNo = Util.getInstance().longInput();
            if (String.valueOf(accountNo).matches(validateAccountNo)) flag = true;
            else {
                if (language == Constant.TAMIL_LANGUAGE)
                    System.out.println("\u001B[31m சரியான கணக்கு எண்ணை செலுத்தவும்...\u001B[0m");
                else System.out.println("\u001B[31m Enter the Valid Account No...\u001B[0m");
            }
        } while (!flag);
        Bank bank = new Bank();
        FileClass file=new FileClass();
        file.readBankDetails(accountNo);
        BankAccount bankUser = bank.getBankAccount(accountNo);
        if (bankUser != null) {
            System.out.println("---------------------------------------");
            if (language == Constant.TAMIL_LANGUAGE) System.out.println("\t\tபெயர் \t\t:" + bankUser.getAccountHolder());
            else System.out.println("\t\tName\t\t:" + bankUser.getAccountHolder());
            if (language == Constant.TAMIL_LANGUAGE) System.out.println("\t\tகணக்கு எண்\t:" + bankUser.getAccountNumber());
            else System.out.println("\t\tAccount No\t:" + bankUser.getAccountNumber());
            if (language == Constant.TAMIL_LANGUAGE) System.out.println("\t\tவங்கி பெயர்\t:" + bankUser.getBankName());
            else System.out.println("\t\tBank Name\t:" + bankUser.getBankName());
            if (language == Constant.TAMIL_LANGUAGE) System.out.println("\t\tஅட்டை வகை\t:" + bankUser.card.getCardType());
            else System.out.println("\t\tCard Type\t:" + bankUser.card.getCardType());
            System.out.println("---------------------------------------");
            do {
                if (language == Constant.TAMIL_LANGUAGE) System.out.println("1.தொடர்க \n2.வெளியேறு");
                else System.out.println("1.Continue\n2.Exit");
                choice = Util.getInstance().getByteInput();
                if (choice == YES) {
                    amount = Util.getInstance().getDepositAmountInput(value, language);
                    Admin admin=new Admin();
                    if (admin.availableCashOnATM() < FIXED_AMOUNT) {
                        if (amount >= BELOW_DEPOSIT) {
                            if (language == Constant.TAMIL_LANGUAGE)
                                System.out.println("\u001B[31m தயவுசெய்து ரூபாய் 35000க்கு கீழே உள்ள தொகையை செலுத்தவும் \u001B[0m");
                            else System.out.println("\u001B[31m Please Deposit Amount Below RS.35000 \u001B[0m");
                        } else {
                            admin.setCashToATM(value);
                            bankUser.setBalanceAmount((bankUser.getBalanceAmount() + amount));
                            // Rewrite file Balance Amount
                            file.depositAmount(amount,bankUser);
                            if (language == Constant.TAMIL_LANGUAGE) System.out.println("வெற்றிகரமாக தொகை செலுத்தப்பட்டது...");
                            else System.out.println("Cash Deposited successfully...");
                            try {
                                addStatement(bankUser, "Deposit  ", amount, simpleDateFormat.format(date));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            cashDepositReceipt(value, amount);
                        }
                    } else {
                        System.out.println("Something went wrong!");
                    }
                    flag = false;
                } else if (choice == NO)
                    flag = false;
                else {
                    if (language == Constant.TAMIL_LANGUAGE)
                        System.out.println("\u001B[31m சரியான தேர்வை உள்ளிடவும்...\u001B[0m");
                    else System.out.println("\u001B[31m Enter the Valid Choice...\u001B[0m");
                }
            } while (flag);
        } else {
            if (language == Constant.TAMIL_LANGUAGE)
                System.out.println("\u001B[31m கணக்கு எண்ணை சரி பார்க்கவும்...\u001B[0m");
            else System.out.println("\u001B[31m Please Check Account No...\u001B[0m");
        }
    }

    public boolean doYouWantToCheckBalance(byte language) {
        boolean flag = true;
        byte choice;
        do {
            if (language == Constant.TAMIL_LANGUAGE) System.out.println("இருப்பு தொகையை அறிய வேண்டுமா\n1.ஆம்\n2.இல்லை");
            else System.out.println("Do you want to Check Available Balance \n1.Yes\n2.No");
            choice = Util.getInstance().getByteInput();
            if (choice >= YES && choice <= NO) flag = false;
            else {
                if (language == Constant.TAMIL_LANGUAGE)
                    System.out.println("\u001B[31m சரியான தேர்வை உள்ளிடவும்...\u001B[0m");
                else System.out.println("\u001B[31m Enter the Valid Choice...");
            }
        } while (flag);
        return choice == YES;
    }
}
