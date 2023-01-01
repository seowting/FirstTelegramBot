package my.uum;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is for running all the method and functions in the telegram bot including send message to the users,
 * update and received user's message, allow user input information, make a meeting room booking, cancel reserved meeting
 * room, display all the user's booking meeting room list through the telegram bot APIs.
 *
 * @author Wong Seow Ting
 */
public class s278263_A221_bot extends TelegramLongPollingBot {

    /**
     * This attribute is represented the username of the telegram bot.
     */
    private final String botUsername = "s278263_A221_bot";

    /**
     * This attribute is represented the token of the telegram bot.
     */
    private final String botToken = "5829808897:AAEMwgNrKXpgA7e_TNNNNolBK-YZMoQMy7U";

    /**
     * This attribute is represented the IC number of the users.
     */
    private String ICNO;

    /**
     * This attribute is represented the staff ID of the users.
     */
    private String staff_id;

    /**
     * This attribute is represented the name of the users.
     */
    private String name;

    /**
     * This attribute is represented the mobile telephone number of the users.
     */
    private String Mobile_TelNo;

    /**
     * This attribute is represented the email of the users.
     */
    private String email;

    /**
     * This attribute is represented the purpose of booking meeting room of the users.
     */
    private String purpose_booking;

    /**
     * This attribute is represented the booking meeting room date of the users.
     */
    private String Booking_Date;

    /**
     * This attribute is represented the booking meeting room time of the users.
     */
    private String Booking_Time;

    /**
     * This attribute is represented the meeting room ID.
     */
    private String Room_Id;

    /**
     * This attribute is represented a message is sending to the users.
     */
    private String reply = "0";

    /**
     * This method is for getting the username of telegram bot.
     *
     * @return telegram bot username.
     */
    @Override
    public String getBotUsername() {
        return botUsername;
    }

    /**
     * This method is for getting the token of telegram bot.
     *
     * @return telegram bot token.
     */
    @Override
    public String getBotToken() {
        return botToken;
    }

    /**
     * This method is for checking whether contain or received any message from the users and getting the message.
     *
     * @param update Update any message is received from the users.
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();

            if (message.hasText()) {
                String text = message.getText();

                if (text.equalsIgnoreCase("/start") && reply == "0") {
                    sendStart(message);
                } else if (text.equalsIgnoreCase("1. Booking Meeting Room") && reply == "start") {
                    sendBooking(message);
                } else if (text.equalsIgnoreCase("2. Cancel Reserved Meeting Room") && reply == "start") {
                    sendCancel(message);
                } else if (text.equalsIgnoreCase("3. Display User's Booking Meeting Room List") && reply == "start") {
                    sendDisplayList(message);
                } else if (text.equalsIgnoreCase("0")) {
                    sendStart(message);
                } else if (reply == "ic") {
                    ICNO = message.getText();
                    sendInputStaffID(message);
                } else if (reply == "id") {
                    staff_id = message.getText();
                    sendInputName(message);
                } else if (reply == "name") {
                    name = message.getText();
                    sendInputPhoneNo(message);
                } else if (reply == "phoneNo") {
                    Mobile_TelNo = message.getText();
                    sendInputEmail(message);
                } else if (reply == "email") {
                    email = message.getText();
                    sendInputPurpose(message);
                } else if (reply == "purpose") {
                    purpose_booking = message.getText();
                    sendInputDate(message);
                } else if (reply == "date") {
                    Booking_Date = message.getText();
                    sendInputTime(message);
                } else if (reply == "time") {
                    Booking_Time = message.getText();
                    sendInputRoom(message);
                } else if (reply == "room") {
                    Room_Id = message.getText();
                    sendConfirmation(message);
                } else if (reply == "yes1") {
                    SQLite.insertUser(ICNO, staff_id, name, Mobile_TelNo, email);
                    SQLite.insertBooking(staff_id, purpose_booking, Booking_Date, Booking_Time, Room_Id);
                    sendSuccessful(message);
                } else if (reply == "cancelID") {
                    staff_id = message.getText();
                    sendCancelConfirmation(message);
                } else if (reply == "yes2") {
                    SQLite.deleteBooking(staff_id);
                    SQLite.deleteUser(staff_id);
                    sendCancelSuccessful(message);
                } else {
                    sendError(message);
                }
            }
        }
    }

    /**
     * This method is for sending the starting message to the users when the users start to run the telegram bot. This
     * method allow the users choosing would like to make a meeting room booking, cancel reserved meeting room or display
     * all the user's booking meeting room list.
     *
     * @param message Any message from the users.
     * @return Starting message and the selection option that users can choose to do.
     */
    public String sendStart(Message message) {
        SendMessage sendMessage = new SendMessage();
        SendMessage sendMessage2 = new SendMessage();
        sendMessage.setText("Hello! " + message.getFrom().getUserName() + " Welcome to make booking meeting room!");
        sendMessage2.setText("Are you wish to proceed which of the following options?");
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage2.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage2.setChatId(message.getChatId().toString());

        //Keyboard Button
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();
        KeyboardRow keyboardRow3 = new KeyboardRow();

        KeyboardButton keyboardButton1 = new KeyboardButton();
        KeyboardButton keyboardButton2 = new KeyboardButton();
        KeyboardButton keyboardButton3 = new KeyboardButton();

        keyboardButton1.setText("1. Booking Meeting Room");
        keyboardButton2.setText("2. Cancel Reserved Meeting Room");
        keyboardButton3.setText("3. Display User's Booking Meeting Room List");

        keyboardRow1.add(keyboardButton1);
        keyboardRow2.add(keyboardButton2);
        keyboardRow3.add(keyboardButton3);

        keyboardRowList.add(keyboardRow1);
        keyboardRowList.add(keyboardRow2);
        keyboardRowList.add(keyboardRow3);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        sendMessage2.setReplyMarkup(replyKeyboardMarkup);
        try {
            execute(sendMessage);
            execute(sendMessage2);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return reply = "start";
    }

    /**
     * This method is for sending the message to the users which are required the users provide their IC number to make a meeting room booking. This method
     * allow users go back to the main to stop the meeting room booking.
     *
     * @param message Any message from the users.
     * @return Required the users provide their IC number message.
     */
    public String sendBooking(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("*---Booking Meeting Room---*" +
                "\n\nPlease provide your IC number. E.g. 996510101856" +
                "\n\nReply 0: Step back to main. If you don't wish to proceed with the meeting room booking");
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setChatId(message.getChatId().toString());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return reply = "ic";
    }

    /**
     * This method is for sending the message to the users which are required the users provide their staff ID to
     * cancel reserved meeting room. This method allow users go back to the main to stop cancel reserved meeting room.
     *
     * @param message Any message from the users.
     * @return Required the users provide their staff ID message.
     */
    public String sendCancel(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("*---Cancel Reserved Meeting Room---*" +
                "\n\nPlease provide your staff ID. E.g. 278263" +
                "\n\nReply 0: Step back to main. If you don't wish to cancel the reserved meeting room");
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setChatId(message.getChatId().toString());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return reply = "cancelID";
    }

    /**
     * This method is for sending the message to the users which are including all the user's booking meeting room list.
     * This method allow users go back to the main to stop displaying user's booking meeting room list.
     *
     * @param message Any message from the users.
     * @return All the user's booking meeting room list.
     */
    public String sendDisplayList(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("*---Display User's Booking Meeting Room List---*" +
                SQLite.displayUserList() +
                "\n\nReply 0: Step back to main. If you don't wish to display the user's booking meeting room list");
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setChatId(message.getChatId().toString());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return reply = "0";
    }

    /**
     * This method is for sending the message to the users which are required the users provide their staff ID to
     * make a meeting room booking. This method allow users go back to the main to stop the meeting room booking.
     *
     * @param message Any message from the users.
     * @return Required the users provide their staff ID message.
     */
    public String sendInputStaffID(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("*---Booking Meeting Room---*" +
                "\n\nPlease provide your staff ID. E.g. 278263" +
                "\n\nReply 0: Step back to main. If you don't wish to proceed with the meeting room booking");
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setChatId(message.getChatId().toString());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return reply = "id";
    }

    /**
     * This method is for sending the message to the users which are required the users provide their name to
     * make a meeting room booking. This method allow users go back to the main to stop the meeting room booking.
     *
     * @param message Any message from the users.
     * @return Required the users provide their name message.
     */
    public String sendInputName(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("*---Booking Meeting Room---*" +
                "\n\nPlease provide your name. E.g. Seow Ting" +
                "\n\nReply 0: Step back to main. If you don't wish to proceed with the meeting room booking");
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setChatId(message.getChatId().toString());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return reply = "name";
    }

    /**
     * This method is for sending the message to the users which are required the users provide their mobile telephone number to
     * make a meeting room booking. This method allow users go back to the main to stop the meeting room booking.
     *
     * @param message Any message from the users.
     * @return Required the users provide their mobile telephone number message.
     */
    public String sendInputPhoneNo(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("*---Booking Meeting Room---*" +
                "\n\nPlease provide your phone number. E.g. 60165874456" +
                "\n\nReply 0: Step back to main. If you don't wish to proceed with the meeting room booking");
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setChatId(message.getChatId().toString());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return reply = "phoneNo";
    }

    /**
     * This method is for sending the message to the users which are required the users provide their email to
     * make a meeting room booking. This method allow users go back to the main to stop the meeting room booking.
     *
     * @param message Any message from the users.
     * @return Required the users provide their email message.
     */
    public String sendInputEmail(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("*---Booking Meeting Room---*" +
                "\n\nPlease provide your email address. E.g. wst123@gmail.com" +
                "\n\nReply 0: Step back to main. If you don't wish to proceed with the meeting room booking");
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setChatId(message.getChatId().toString());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return reply = "email";
    }

    /**
     * This method is for sending the message to the users which are required the users provide their purpose of booking to
     * make a meeting room booking. This method allow users go back to the main to stop the meeting room booking.
     *
     * @param message Any message from the users.
     * @return Required the users provide their purpose of booking message.
     */
    public String sendInputPurpose(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("*---Booking Meeting Room---*" +
                "\n\nPlease provide your purpose of booking. E.g. To conduct a meeting." +
                "\n\nReply 0: Step back to main. If you don't wish to proceed with the meeting room booking");
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setChatId(message.getChatId().toString());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return reply = "purpose";
    }

    /**
     * This method is for sending the message to the users which are required the users provide their booking date to
     * make a meeting room booking. This method allow users go back to the main to stop the meeting room booking.
     *
     * @param message Any message from the users.
     * @return Required the users provide their booking date message.
     */
    public String sendInputDate(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("*---Booking Meeting Room---*" +
                "\n\nPlease provide your booking date. Format: YYYY-MM-DD" +
                "\n\nReply 0: Step back to main. If you don't wish to proceed with the meeting room booking");
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setChatId(message.getChatId().toString());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return reply = "date";
    }

    /**
     * This method is for sending the message to the users which are required the users provide their booking time to
     * make a meeting room booking. This method allow users go back to the main to stop the meeting room booking.
     *
     * @param message Any message from the users.
     * @return Required the users provide their booking time message.
     */
    public String sendInputTime(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("*---Booking Meeting Room---*" +
                "\n\nPlease provide your booking time. Format: HH:MM:SS" +
                "\n\nReply 0: Step back to main. If you don't wish to proceed with the meeting room booking");
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setChatId(message.getChatId().toString());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return reply = "time";
    }

    /**
     * This method is for sending the message to the users which are required the users provide the room ID they would like to book to
     * make a meeting room booking. This method allow users go back to the main to stop the meeting room booking.
     *
     * @param message Any message from the users.
     * @return Required the users provide the room ID message.
     */
    public String sendInputRoom(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("*---Booking Meeting Room---*" +
                "\n\nPlease choose a meeting room by providing the room ID. E.g. 1001 (Available Room ID: BMU - 1001, BME 2 - 1002,  BME 3 - 1003, ETR 1 - 2001, ETR 2 - 2002, ETR 3 - 2003, ETR 4 - 2004)" +
                "\n\nReply 0: Step back to main. If you don't wish to proceed with the meeting room booking");
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setChatId(message.getChatId().toString());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return reply = "room";
    }

    /**
     * This method is for sending the message to the users which are including all the information provided by the users and
     * the users can choose to confirm the meeting room booking. This method allow users go back to the main to stop the meeting room booking.
     *
     * @param message Any message from the users.
     * @return All the information provided by the users and the confirmation booking meeting room message.
     */
    public String sendConfirmation(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("*---Booking Meeting Room---*" +
                "\n\n***Personal Information***" +
                "\nIC/NO: " + ICNO +
                "\nStaff ID: " + staff_id +
                "\nName: " + name +
                "\nPhone No: " + Mobile_TelNo +
                "\nEmail Address: " + email +
                "\n\n***Booking Details***" +
                "\nBooking Purpose: " + purpose_booking +
                "\nBooking Date: " + Booking_Date +
                "\nBooking Time: " + Booking_Time +
                "\nBooking Room ID: " + Room_Id +
                "\n\nAre you sure you want to make the meeting room booking?" +
                "\n\nReply 1: Yes" +
                "\nReply 0: Step back to main. If you don't wish to proceed with the meeting room booking");
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setChatId(message.getChatId().toString());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return reply = "yes1";
    }

    /**
     * This method is for sending the message to the users which are including the booking information and informed the meeting room booking is successfully to
     * the users. This method allow users go back to the main.
     *
     * @param message Any message from the users.
     * @return Booking information and meeting room booking successful message.
     */
    public String sendSuccessful(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("*---Booking Meeting Room---*" +
                SQLite.displayUserBookingRoom(staff_id) +
                "\n\nYour meeting room booking is successfully!" +
                "\n\nReply 0: Step back to main");
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setChatId(message.getChatId().toString());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return reply = "0";
    }

    /**
     * This method is for sending the message to the users which are including the booking information and
     * the users can choose to confirm cancel reserved meeting room. This method allow users go back to the main to stop cancel reserved meeting room.
     *
     * @param message Any message from the users.
     * @return Booking information and the confirmation cancel reserved meeting room message.
     */
    public String sendCancelConfirmation(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("*---Cancel Reserved Meeting Room---*" +
                SQLite.displayUserBookingRoom(staff_id) +
                "\n\nAre you sure you want to cancel the reserved meeting room?" +
                "\n\nReply 1: Yes" +
                "\n\nReply 0: Step back to main. If you don't wish to cancel the reserved meeting room");
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setChatId(message.getChatId().toString());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return reply = "yes2";
    }

    /**
     * This method is for sending the message to the users which are informed the reserved meeting room is canceled successfully to
     * the users. This method allow users go back to the main.
     *
     * @param message Any message from the users.
     * @return Cancel reserved meeting room successful message.
     */
    public String sendCancelSuccessful(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("*---Cancel Reserved Meeting Room---*" +
                "\n\nYour reserved meeting room is canceled successfully!" +
                "\n\nReply 0: Step back to main.");
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setChatId(message.getChatId().toString());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return reply = "0";
    }

    /**
     * This method is for sending the message to the users which are informed the users they are enter an invalid command.
     *
     * @param message Any message from the users.
     */
    public void sendError(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("I don't understand. Please use the available commands. (If you wish to start talking with me, please enter '/start' command.) ");
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setChatId(message.getChatId().toString());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
