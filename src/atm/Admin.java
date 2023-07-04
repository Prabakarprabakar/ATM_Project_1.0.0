package atm;

public class Admin {
    private final static int FIXED_AMOUNT = 2000000;
    private final static byte ADD_CASH_TO_ATM = 1;
    private final static byte VIEW_AVAILABLE_CASH_ON_ATM = 2;
    private static final int[] value = {50, 50, 54};
    public static final int[] cash = {500, 200, 100};
    final static int ADMIN_PANEL = 28071999;

    public void printAddedCashToATM() {
        System.out.println("\n----------------------------------------------");
        System.out.println("\t\tAvailable Cash on ATM...");
        System.out.println("----------------------------------------------\n");
        for (int i = 0; i < 3; i++) {
            int temp = cash[i] * value[i];
            System.out.println("\t" + cash[i] + "  x  " + value[i] + "   = " + temp);
        }
        System.out.println("\n\tTotal Available cash on ATM :" + availableCashOnATM());
        System.out.println("\n----------------------------------------------\n");
    }

    public double availableCashOnATM() {
        double amount = 0;
        for (int i = 0; i < 3; i++) {
            int temp = cash[i] * value[i];
            amount = amount + temp;
        }
        return amount;
    }

    public boolean checkAvailableCashOnAtm(double amount) {
        double givenAmount = amount;
        while (givenAmount > 499 && value[0] != 0) {
            givenAmount -= 500;
        }
        while (givenAmount > 199 && value[1] != 0) {
            givenAmount -= 200;
        }
        while (givenAmount > 99 && value[2] != 0) {
            givenAmount -= 100;
        }
        if (givenAmount != 0) {
            System.out.println("No Cash Available on ATM...");
            return false;
        }
        reduceATMAmount(amount);
        return true;
    }

    private void reduceATMAmount(double amount) {
        while (amount > 499 && value[0] != 0) {
            value[0]--;
            amount -= 500;
        }
        while (amount > 199 && value[1] != 0) {
            value[1]--;
            amount -= 200;
        }
        while (amount > 99 && value[2] != 0) {
            value[2]--;
            amount -= 100;
        }
    }

    public void setCashToATM(int[] value) {
        Admin.value[0] += value[0];
        Admin.value[1] += value[1];
        Admin.value[2] += value[2];
    }

    public double getAvailableAmountOnATM(int[] values) {
        double amount = 0;
        for (int i = 0; i < 3; i++) {
            int temp = cash[i] * values[i];
            amount = amount + temp;
        }
        return amount;
    }

    public void operateATM() {
        byte choice;
        boolean adminFlag = true;
        do {
            do {
                System.out.println("\n1.Add Cash on ATM\n2.View Available Cash on ATM\n3.Exit");
                choice = Util.getInstance().getByteInput();
                if (choice > 0 && choice < 4) break;
                else System.out.println("Enter the Valid Choice");
            } while (true);
            //Add Cash on ATM
            if (choice == ADD_CASH_TO_ATM) {
                System.out.println("Add Cash to ATM");
                int[] values = new int[3];
                Util.getInstance().getAmountInput(values);
                if (availableCashOnATM() + getAvailableAmountOnATM(values) < FIXED_AMOUNT) {
                    setCashToATM(values);
                    System.out.println("Cash Deposited successfully...");
                } else
                    System.out.println("No available to dump a cash...");
            }
            //View Available Cash on ATM
            else if (choice == VIEW_AVAILABLE_CASH_ON_ATM)
                printAddedCashToATM();
                //Exit the current Loop
            else
                adminFlag = false;
        } while (adminFlag);
        System.out.println("-----------------------");
        System.out.println("ATM Ready to Action....");
        System.out.println("-----------------------\n");
    }
}
