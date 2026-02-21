package unicam.hackhub.application.payment;

public interface PaymentHandler {

    /**
     * Transfers a specified monetary prize to the team's designated account.
     *
     * @param amount   the prize amount to be transferred; must be greater than zero
     * @param teamName the name of the winning team, used to retrieve payment details
     * @throws IllegalArgumentException if the amount is not positive or the team name is null/empty
     * @throws RuntimeException         if the transfer fails due to insufficient funds,
     *                                 invalid account details, or other processing errors
     */
    void transferPrize(double amount, String teamName);
}
