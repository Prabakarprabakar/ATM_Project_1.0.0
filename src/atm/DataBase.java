package atm;
import java.sql.*;

public class DataBase {
    private static final String JDBC_URL="jdbc:mysql://localhost:3306/ATM_Machine";
    private static final String USERNAME="root";
    private static final String PASSWORD="Prabakar@2828";
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println();
        }
        return connection;
    }

    public void readBankDataFromSQLUsingCardNumber(long cardNumber){
        String bankAccountQuery = "SELECT * FROM user_detail WHERE card_number = ?";
        Bank bank = new Bank();

        try (Connection connection = getConnection();
             PreparedStatement bankAccountStatement = connection.prepareStatement(bankAccountQuery)) {
            bankAccountStatement.setLong(1, cardNumber);
            ResultSet bankAccountResult = bankAccountStatement.executeQuery();

            if (bankAccountResult.next()) {
                long cardNo = bankAccountResult.getLong("card_number");
                short pin = bankAccountResult.getShort("pin_number");
                String cardType = bankAccountResult.getString("card_type");
                String expiryDate = bankAccountResult.getString("valid_THRU");
                long accountNo = bankAccountResult.getLong("account_number");
                String accountHolder = bankAccountResult.getString("account_holder_name");
                String bankName = bankAccountResult.getString("bank_name");
                long phoneNumber = bankAccountResult.getLong("phone_number");
                double balance = bankAccountResult.getDouble("balance_amount");
                Card card = new Card(cardNo, pin, cardType, expiryDate);
                BankAccount bankAccount = new BankAccount(accountNo, accountHolder, bankName, phoneNumber, balance, card);

                bank.accessCardDetails().put(cardNo, card);
                bank.accessBankAccountDetails().put(cardNo, bankAccount);
            } else {
                System.out.println("Card not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void readBankDataFromSQLUsingBankAccountNumber(long accountNumber) {
        String bankAccountQuery = "SELECT * FROM user_detail WHERE account_number = ?";
        Bank bank = new Bank();

        try (Connection connection = getConnection();
             PreparedStatement bankAccountStatement = connection.prepareStatement(bankAccountQuery)) {

            bankAccountStatement.setLong(1, accountNumber);
            ResultSet bankAccountResult = bankAccountStatement.executeQuery();

            if (bankAccountResult.next()) {
                long cardNo = bankAccountResult.getLong("card_number");
                short pin = bankAccountResult.getShort("pin_number");
                String cardType = bankAccountResult.getString("card_type");
                String expiryDate = bankAccountResult.getString("valid_THRU");
                long accountNo = bankAccountResult.getLong("account_number");
                String accountHolder = bankAccountResult.getString("account_holder_name");
                String bankName = bankAccountResult.getString("bank_name");
                long phoneNumber = bankAccountResult.getLong("phone_number");
                double balance = bankAccountResult.getDouble("balance_amount");
                Card card = new Card(cardNo, pin, cardType, expiryDate);
                BankAccount bankAccount = new BankAccount(accountNo, accountHolder, bankName, phoneNumber, balance, card);

                bank.accessCardDetails().put(cardNo, card);
                bank.accessBankAccountDetails().put(cardNo, bankAccount);
            } else {
                System.out.println("Bank account not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changePinFromSQL(short newPin, BankAccount bankAccount){
        try (Connection connection = getConnection()) {
            String query = "UPDATE user_detail SET pin_number = ? WHERE card_number = "+bankAccount.card.getCardNumber();
            String pinValidate = "[0-9]{4}";
            if (String.valueOf(newPin).matches(pinValidate)) {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, newPin);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void depositAmountInSQL(double amount, BankAccount bankAccount) {
        try (Connection connection = getConnection()) {
            String query = "SELECT balance_amount FROM user_detail WHERE card_number = ?";
            PreparedStatement selectStatement = connection.prepareStatement(query);
            selectStatement.setLong(1, bankAccount.card.getCardNumber());
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                double balance = resultSet.getDouble("balance_amount");
                double newBalance = balance + amount;
                String updateQuery = "UPDATE user_detail SET balance_amount = ? WHERE card_number = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setDouble(1, newBalance);
                updateStatement.setLong(2, bankAccount.card.getCardNumber());
                updateStatement.executeUpdate();
            } else {
                System.out.println("Unable to find the bank account.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void withDrawAmountFromSQL(double amount,BankAccount bankAccount){
        try (Connection connection = getConnection()) {
            String query = "SELECT balance_amount FROM user_detail WHERE card_number = ?";
            PreparedStatement selectStatement = connection.prepareStatement(query);
            selectStatement.setLong(1, bankAccount.card.getCardNumber());
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                double balance = resultSet.getDouble("balance_amount");
                double newBalance = balance - amount;
                String updateQuery = "UPDATE user_detail SET balance_amount = ? WHERE card_number = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setDouble(1, newBalance);
                updateStatement.setLong(2, bankAccount.card.getCardNumber());
                updateStatement.executeUpdate();
            } else {
                System.out.println("Unable to find the bank account.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addStatementToSQL(BankAccount bankAccount, String transectionType, double amount, String dateAndTime){

    }
}
