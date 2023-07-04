package atm;


import java.io.*;
import java.util.Map;

public class FileClass {
    private static final String BANK_DATA = "D://Prabakar//Project Java//ATM_Project - 1.2.5//src//atm//BankData.csv";
    private static final String ADMIN_DATA = "D://Prabakar//Project Java//ATM_Project - 1.2.5//src//atm//AdminData.csv";
    private static final String STATEMENT = "D://Prabakar//Project Java//ATM_Project - 1.2.5//src//atm//Statement//";

    public void writeBankDetailsToCSV() {
        Bank bank = new Bank();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BANK_DATA))) {
            // Write headers
            try {
                writer.write("Card Number,Pin,Type,Expiration Date,Account Number,Holder Name,Bank Name,Phone Number,Balance\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Write bank account details
            for (Map.Entry<Long, BankAccount> entry : bank.accessBankAccountDetails().entrySet()) {
                BankAccount account = entry.getValue();
                Card card = account.card;

                writer.write(card.getCardNumber() + ",");
                writer.write(card.getPinNumber() + ",");
                writer.write(card.getCardType() + ",");
                writer.write(card.getValidTHRU() + ",");
                writer.write(account.getAccountNumber() + ",");
                writer.write(account.getAccountHolder() + ",");
                writer.write(account.getBankName() + ",");
                writer.write(account.getPhoneNumber() + ",");
                writer.write(account.getBalanceAmount() + "\n");
            }

            writer.flush();
            System.out.println("Bank details written to CSV file successfully!");
        } catch (IOException e) {
            System.out.println("Error writing bank details to CSV file: " + e.getMessage());
        }
    }

    public void readBankDetailsFromCSV(long cardNo) {
        Bank bank = new Bank();
        try (BufferedReader reader = new BufferedReader(new FileReader(BANK_DATA))) {
            String line;
            boolean isFirstLine = true; // Skip the first line (header)

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] data = line.split(",");
                if (data.length == 9 && cardNo == Long.parseLong(data[0])) {
                    long cardNumber = Long.parseLong(data[0]);
                    short pin = Short.parseShort(data[1]);
                    String type = data[2];
                    String expirationDate = data[3];
                    long accountNumber = Long.parseLong(data[4]);
                    String holderName = data[5];
                    String bankName = data[6];
                    long phoneNumber = Long.parseLong(data[7]);
                    double balance = Double.parseDouble(data[8]);

                    Card card = new Card(cardNumber, pin, type, expirationDate);
                    BankAccount account = new BankAccount(accountNumber, holderName, bankName, phoneNumber, balance, card);

                    bank.accessCardDetails().put(cardNumber, card);
                    bank.accessBankAccountDetails().put(cardNumber, account);
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading bank details from CSV file: " + e.getMessage());
        }
    }

    public void readBankDetails(long accountNo) {
        Bank bank = new Bank();
        try (BufferedReader reader = new BufferedReader(new FileReader(BANK_DATA))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] data = line.split(",");
                if (data.length == 9 && accountNo == Long.parseLong(data[4])) {
                    long cardNumber = Long.parseLong(data[0]);
                    short pin = Short.parseShort(data[1]);
                    String type = data[2];
                    String expirationDate = data[3];
                    long accountNumber = Long.parseLong(data[4]);
                    String holderName = data[5];
                    String bankName = data[6];
                    long phoneNumber = Long.parseLong(data[7]);
                    double balance = Double.parseDouble(data[8]);

                    Card card = new Card(cardNumber, pin, type, expirationDate);
                    BankAccount account = new BankAccount(accountNumber, holderName, bankName, phoneNumber, balance, card);

                    bank.accessCardDetails().put(cardNumber, card);
                    bank.accessBankAccountDetails().put(cardNumber, account);
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading bank details from CSV file: " + e.getMessage());
        }
    }

    public void depositAmount(double amount, BankAccount bankAccount) {
        try (RandomAccessFile file = new RandomAccessFile(BANK_DATA, "rw")) {
            String line;
            boolean isFirstLine = true, loop = true;

            while ((line = file.readLine()) != null && loop) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] data = line.split(",");
                if (data.length == 9 && bankAccount.card.getCardNumber() == Long.parseLong(data[0])) {
                    try {
                        double newBalance = Double.parseDouble(data[8]) + amount;
                        String updatedBalance = String.format("%.3f", newBalance);
                        long currentPosition = file.getFilePointer();
                        if (file.readLine() == null) {
                            file.seek(currentPosition - data[8].length());
                            file.writeBytes(updatedBalance);
                        } else {
                            file.seek(currentPosition - data[8].length() - 1);
                            file.writeBytes(updatedBalance + "\n");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping line due to invalid balance amount: " + line);
                    }
                    loop = false;
                }
            }
        } catch (IOException e) {
            System.out.println("Error updating amount column in the CSV file: " + e.getMessage());
        }
    }

    public void withdrawAmount(double amount, BankAccount bankAccount) {
        try (RandomAccessFile file = new RandomAccessFile(BANK_DATA, "rw")) {
            String line;
            boolean isFirstLine = true, loop = true;
            while ((line = file.readLine()) != null && loop) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] data = line.split(",");
                if (data.length == 9 && bankAccount.card.getCardNumber() == Long.parseLong(data[0])) {
                    try {
                        double newBalance = Double.parseDouble(data[8]) - amount;
                        String updatedBalance = String.format("%.3f", newBalance);
                        long currentPosition = file.getFilePointer();
                        int count = data[8].length() - updatedBalance.length();
                        StringBuilder spacing = new StringBuilder();
                        if (count > 0) {
                            spacing.append(" ".repeat(count));
                        }
                        if (file.readLine() == null) {
                            file.seek(currentPosition - data[8].length());
                            file.writeBytes(updatedBalance + spacing);
                        } else {
                            file.seek(currentPosition - data[8].length() - 1);
                            file.writeBytes(updatedBalance + spacing);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping line due to invalid balance amount: " + line);
                    }
                    loop = false;
                }
            }
        } catch (IOException e) {
            System.out.println("Error updating amount column in the CSV file: " + e.getMessage());
        }
    }

    public void changePin(short newPin, BankAccount bankAccount) {
        try (RandomAccessFile file = new RandomAccessFile(BANK_DATA, "rw")) {
            String line;
            boolean isFirstLine = true, loop = true;

            while ((line = file.readLine()) != null && loop) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] data = line.split(",");
                if (data.length == 9 && Long.parseLong(data[0]) == bankAccount.card.getCardNumber()) {
                    String pinValidate = "[0-9]{4}";
                    if (String.valueOf(newPin).matches(pinValidate)) {
                        long currentPosition = file.getFilePointer();
                        int movePointer = data[1].length() + data[2].length() + data[3].length() + data[4].length() + data[5].length() + data[6].length() + data[7].length() + data[8].length()+8;
                        file.seek(currentPosition - (movePointer));
                        file.writeBytes(String.valueOf(newPin));
                    }
                    loop = false;
                }
            }
        } catch (IOException e) {
            System.out.println("Error changing PIN: " + e.getMessage());
        }
    }
    public void addStatementsToFile(BankAccount bankAccount, String transectionType, double amount, String dateAndTime){
        try(BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(STATEMENT+bankAccount.card.getCardNumber()+".csv",true))){
            bufferedWriter.write(transectionType+",");
            bufferedWriter.write(amount+",");
            bufferedWriter.write(bankAccount.getBalanceAmount()+",");
            bufferedWriter.write(dateAndTime+"\n");
            bufferedWriter.flush();
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void readStatementsFromFile(BankAccount bankAccount) {
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(STATEMENT+bankAccount.card.getCardNumber()+".csv", "r")) {
            long lengthOfFile = randomAccessFile.length();
            long counterFromEnd = 1L;
            long newlineCounterGoal = 10;
            int newlineCounter = 0;
            long tailPosition = 0L;

            randomAccessFile.seek(lengthOfFile - 1L);
            char currentChar = (char) randomAccessFile.readByte();
            if (currentChar == '\n') {
                newlineCounterGoal++;
            }

            while (counterFromEnd <= lengthOfFile) {
                randomAccessFile.seek(lengthOfFile - counterFromEnd);
                if (randomAccessFile.readByte() == '\n') {
                    newlineCounter++;
                }
                if (newlineCounter == newlineCounterGoal) {
                    tailPosition = randomAccessFile.getFilePointer();
                    break;
                }
                counterFromEnd++;
            }
            randomAccessFile.seek(tailPosition);

            String line;
            while ((line = randomAccessFile.readLine()) != null) {
                bankAccount.statement.add(1,line);
            }
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
