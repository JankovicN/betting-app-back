package rs.ac.bg.fon.constants;

public class Constants {
    public static final String RESOURCE_FOLDER = "src/main/resources/";

    public static final String INVALID_DATA = "INVALID";

    // String constants
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    // Football API specific constants
    public static final String SEASON = "2023";
    public static final Integer BET_365_ID = 8;

    // Waiting for result state
    public static final String WAITING_FOR_RESULTS = "-";

    // Bet state constants
    public static final String BET_NOT_FINISHED = "-";
    public static final String BET_LOSS = "LOSS";
    public static final String BET_WIN = "WIN";

    // Ticket state constants
    public static final String TICKET_UNPROCESSED = "UNPROCESSED";
    public static final String TICKET_PROCESSED = "PROCESSED";
    public static final String TICKET_LOSS = "LOSS";
    public static final String TICKET_WIN = "WIN";
    public static final String TICKET_PAYOUT = "PAYOUT";
    public static final String TICKET_CANCELED = "CANCELED";

    // Fixture state constants
    public static final String FIXTURE_NOT_STARTED = "NS";
    public static final String FIXTURE_IN_PLAY = "IP";
    public static final String FIXTURE_FULL_TIME = "FT";

    // Payment types
    public static final String PAYMENT_DEPOSIT = "DEPOSIT";
    public static final String PAYMENT_WITHDRAW = "WITHDRAW";
    public static final String PAYMENT_PAYOUT = "PAYOUT";
    public static final String PAYMENT_REFUND = "REFUND";
    public static final String PAYMENT_WAGER = "WAGER";

}
