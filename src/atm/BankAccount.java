package atm;

import java.util.ArrayList;
import java.util.List;

public class BankAccount {
    Card card;
    protected long accountNo;
    protected String accountHolderName;
    public String bankName;
    protected long phoneNo;
    double balanceAmount;
    public ArrayList<String> statement = new ArrayList<>(List.of("\tTransaction Type\tAmount  \tBalance Amount\t\tDate \t\t Time"));
    BankAccount(long accountNo, String accountHolderName, String bankName, long phoneNo, double balanceAmount, Card card) {
        this.accountNo = accountNo;
        this.accountHolderName = accountHolderName;
        this.bankName = bankName;
        this.phoneNo = phoneNo;
        this.balanceAmount = balanceAmount;
        this.card=card;
    }

    public long getAccountNumber() {
        return accountNo;
    }

    public long getPhoneNumber() {
        return phoneNo;
    }

    public void setBalanceAmount(double availableAmount) {
        this.balanceAmount = availableAmount;
    }

    public double getBalanceAmount() {
        return balanceAmount;
    }

    public String getBankName() {
        return bankName;
    }

    public String getAccountHolder() {
        return accountHolderName;
    }

}
