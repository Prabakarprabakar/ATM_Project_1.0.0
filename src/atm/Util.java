package atm;

import java.util.Scanner;

public class Util {
    private static Util object = null;

    public static Util getInstance() {
        if (object == null)
            object = new Util();
        return object;
    }

    private static final Scanner scanner = new Scanner(System.in);

    public long longInput() {
        if (scanner.hasNextLong()) {
            long input = scanner.nextLong();
            scanner.nextLine();
            return input;
        } else {
            scanner.nextLine();
            return 0;
        }
    }

    public byte chooseLanguage() {
        byte language;
        boolean flag = true;
        do {
            System.out.println("\nஉங்கள் மொழியை தேர்ந்தெடுக்கவும்");
            System.out.println("Choose your language");
            System.out.println("\t\t\t\t1.தமிழ் \n\t\t\t\t2.English");
            language = Util.getInstance().getByteInput();
            if (language >= Constant.TAMIL_LANGUAGE && language <= Constant.ENGLISH_LANGUAGE) flag = false;
            else {
                System.out.println("\u001B[31m Enter the Valid Choice...\u001B[0m");
            }
        } while (flag);
        return language;
    }

    public byte getByteInput() {
        if (scanner.hasNextByte()) {
            byte input = scanner.nextByte();
            scanner.nextLine();
            return input;
        } else {
            scanner.nextLine();
            return 0;
        }
    }

    public double getDepositAmountInput(int[] value, byte language) {
        boolean flag;
        if (language == Constant.TAMIL_LANGUAGE) System.out.println("தொகையை செலுத்தவும் \n");
        else System.out.println("Enter the Amount To Deposit \n");
        for (byte i = 0; i < 3; i++) {
            do {
                System.out.print(Admin.cash[i] + " X ");
                flag = true;
                if (scanner.hasNextInt()) {
                    value[i] = getIntInput();
                    if (value[i] >= 0)
                        flag = false;
                    else {

                        if (language == Constant.TAMIL_LANGUAGE)
                            System.out.println("\u001B[31m சரியான தேர்வை உள்ளிடவும்...\u001B[0m");
                        else System.out.println("\u001B[31m Enter the Valid Choice...\u001B[0m");
                    }
                } else {
                    if (language == Constant.TAMIL_LANGUAGE)
                        System.out.println("\u001B[31m சரியான தேர்வை உள்ளிடவும்...\u001B[0m");
                    else System.out.println("\u001B[31m Enter the Valid Choice...\u001B[0m");
                    scanner.nextLine();
                }
            } while (flag);
        }
        double amount = 0;
        for (byte i = 0; i < 3; i++) {
            amount += (value[i] * Admin.cash[i]);
        }
        return amount;
    }

    public int getIntInput() {
        if (scanner.hasNextInt()) {
            int input = scanner.nextInt();
            scanner.nextLine();
            return input;
        } else {
            scanner.nextLine();
            return 0;
        }
    }

    public void getAmountInput(int[] values) {
        boolean flag = true;
        for (byte i = 0; i < 3; i++) {
            do {
                System.out.print(Admin.cash[i] + "  x  ");
                if (scanner.hasNextInt()) {
                    values[i] = scanner.nextInt();
                    flag = false;
                } else {
                    System.out.println("Enter the Valid Input");
                    scanner.nextLine();
                }
            } while (flag);
        }
    }

    public short getShortInput(byte language) {
        boolean flag = true;
        short pinNo = 0;
        do {
            if (language == Constant.TAMIL_LANGUAGE) System.out.print("உங்கள் இரகசிய குறியிட்டு எண்ணை செலுத்தவும் :");
            else System.out.print("Enter the Pin Number :");
            if (scanner.hasNextShort()) {
                pinNo = scanner.nextShort();
                scanner.nextLine();
                flag = false;
            } else {
                if (language == Constant.TAMIL_LANGUAGE)
                    System.out.println("\u001B[31m சரியான தேர்வை உள்ளிடவும்...\u001B[0m");
                else System.out.println("\u001B[31m Enter the Valid Input...\u001B[0m");
                scanner.nextLine();
            }
        } while (flag);
        return pinNo;
    }

    public void removeCard(byte language) {
        System.out.println("\nPress Enter to Remove the Card\n");
        if (language == Constant.TAMIL_LANGUAGE) System.out.println("ATM கார்டை எடுத்துக்கொள்ளவும்\n");
        else System.out.println("Take Your ATM Card\n");
        scanner.nextLine();
    }

    public double getDoubleInput() {
        if (scanner.hasNextDouble()) {
            double input = scanner.nextDouble();
            scanner.nextLine();
            return input;
        } else {
            scanner.nextLine();
            return 0;
        }
    }

    public short getNewPin(byte language) {
        boolean flag = true;
        short newPin = 0;
        do {
            if (language == Constant.TAMIL_LANGUAGE) System.out.print("புதிய குறியிட்டு எண்ணை செலுத்தவும் :");
            else System.out.print("Enter new pin number :");
            if (scanner.hasNextShort()) {
                newPin = scanner.nextShort();
                scanner.nextLine();
                flag = false;
            } else {
                if (language == Constant.TAMIL_LANGUAGE)
                    System.out.println("\u001B[31m சரியான தேர்வை உள்ளிடவும்...\u001B[0m");
                else System.out.println("\u001B[31m Enter the Valid Choice...\u001B[0m");
                scanner.nextLine();
            }
        } while (flag);
        return newPin;
    }

    public long getPhoneNo(byte language) {
        long phoneNo;
        boolean flag = true;
        do {
            if (language == Constant.TAMIL_LANGUAGE) System.out.print("தொலைபேசி எண்ணை செலுத்தவும் :");
            else System.out.print("Enter the Phone No :");
            phoneNo = Util.getInstance().longInput();
            String string = String.valueOf(phoneNo);
            if (string.length() == 10) {
                flag = false;
            } else {
                if (language == Constant.TAMIL_LANGUAGE)
                    System.out.println("\u001B[31m சரியான தேர்வை உள்ளிடவும்...\u001B[0m");
                else System.out.println("\u001B[31m Enter the Valid Choice...\u001B[0m");
            }
        } while (flag);
        return phoneNo;
    }
}
