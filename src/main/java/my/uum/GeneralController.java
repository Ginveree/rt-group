package my.uum;

import dto.CodeMessage;
import enums.CodeMessageType;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class is to control and give responses based on user input.
 *
 * @author Ginveree Kong May Cheng 279045
 */
public class GeneralController {
    /**
     * Declare static arraylist for room ID list for different functions or classes to use.
     */
    public static ArrayList<String> roomIdlist = new ArrayList<String>();
    /**
     * Declare static arraylist for booking ID list for different functions or classes to use.
     */
    public static ArrayList<String> bookingIdlist = new ArrayList<String>();
    /**
     * Declare static arraylist for staff ID list for different functions or classes to use.
     */
    public static ArrayList<String> staffIdlist = new ArrayList<String>();
    /**
     * Declare static variable for different functions or classes to use.
     */
    public static String choice;

    /**
     * This method will retrieve the data in roomDetails into the arraylist.
     */
    public static void retrieveRoomId() {
        int counter = 0;
        ResultSet resultSet = null;
        try (Connection conn = SQLite.connect();
             Statement statement = conn.createStatement();) {
            String selectSql = "SELECT room_Id from roomDetails";
            resultSet = statement.executeQuery(selectSql);
            while (resultSet.next()) {
                roomIdlist.add(resultSet.getString(1));//increment counter
                counter++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will retrieve the data in guestDetails into the arraylist.
     */
    public static void retrieveBooingId() {
        int counter = 0;
        ResultSet resultSet = null;
        try (Connection conn = SQLite.connect();
             Statement statement = conn.createStatement();) {
            String selectSql = "SELECT booking_Id from guestDetails";
            resultSet = statement.executeQuery(selectSql);
            while (resultSet.next()) {
                bookingIdlist.add(resultSet.getString(1));//increment counter
                counter++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will retrieve the data in staffDetails into the arraylist.
     */
    public static void retrieveStaffId() {
        int counter = 0;
        ResultSet resultSet = null;
        try (Connection conn = SQLite.connect();
             Statement statement = conn.createStatement();) {
            String selectSql = "SELECT staff_Id from staffDetails";
            resultSet = statement.executeQuery(selectSql);
            while (resultSet.next()) {
                staffIdlist.add(resultSet.getString(1));//increment counter
                counter++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is the class to respond back to user based on user's actions.
     * We have timer thread to refresh the elements in the arraylist every 5 seconds to import the latest updated data from MySQLite.
     *
     * @param text      Receive the text/action done by user.
     * @param chatId    Receive and record the chat ID of user to avoid conflicts.
     * @param messageId Receive and record the message ID of user to avoid conflicts.
     * @return respond back to user.
     */
    public CodeMessage handle(String text, Long chatId, Integer messageId) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                retrieveBooingId();
                retrieveRoomId();
                retrieveStaffId();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, new Date(), 5000);
        CodeMessage codeMessage = new CodeMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        codeMessage.setSendMessage(sendMessage);

        if (text.equals("/start")) {
            sendMessage.setText("Welcome to GinvereeBot!\n" +
                    "Please select your identity");
            sendMessage.setParseMode("Markdown");
            sendMessage.setReplyMarkup(
                    InlineButton.keyboard(
                            InlineButton.collection(
                                    InlineButton.row(
                                            InlineButton.button("Admin", "admin")),
                                    InlineButton.row(
                                            InlineButton.button("User", "user")))));
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("ginveree123")) {
            sendMessage.setText("Welcome\nPlease enter staff ID");
            sendMessage.setParseMode("Markdown");
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (staffIdlist.contains(text)) {
            sendMessage.setText("What would you like to do?");
            sendMessage.setReplyMarkup(
                    InlineButton.keyboard(
                            InlineButton.collection(
                                    InlineButton.row(
                                            InlineButton.button("Add new room", "update")),
                                    InlineButton.row(
                                            InlineButton.button("Edit room", "edit")),
                                    InlineButton.row(
                                            InlineButton.button("Delete room", "delete"))
                            )));
        } else if (roomIdlist.contains(text)) {
            sendMessage.setText("Please follow the steps below: \n1. Enter you IC number by adding " +
                    "the word nric plus an underscore in front" +
                    "\n2. Press send" +
                    "\n3. Send NRIC DONE ");
            sendMessage.setChatId(chatId);
            sendMessage.setParseMode("Markdown");
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("NRIC DONE")) {
            sendMessage.setText("Thank you for entering your IC number" +
                    "\nPlease follow the steps below: \n1. Enter you name by adding " +
                    "the word name plus an underscore in front" +
                    "\n2. Press send" +
                    "\n3. Send NAME DONE ");
            sendMessage.setParseMode("Markdown");
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("10") || text.equals("20") || text.equals("30") || text.equals("40")) {
            if (text.equals("10")) {
                sendMessage.setText("Available room for " + text + " peoples " +
                        "\nPlease select by inserting the ID:" + "\n\n" + "\t\t\t\tID\t\t\t\t\t\tCapacity\t\t\t\t\t\t\t\tDate\t\t\t" +
                        "\t\t\t\t\t\t\t\t\t\t\t\tTime\n" + SQLite.getCapacity(10));
                sendMessage.setParseMode("Markdown");
                codeMessage.setType(CodeMessageType.MESSSAGE);
            } else if (text.equals("20")) {
                sendMessage.setText("Available room for " + text + " peoples " +
                        "\nPlease select by inserting the ID:" + "\n\n" + "\t\t\t\tID\t\t\t\t\t\tCapacity\t\t\t\t\t\t\t\tDate\t\t\t" +
                        "\t\t\t\t\t\t\t\t\t\t\t\tTime\n" + SQLite.getCapacity(20));
                sendMessage.setParseMode("Markdown");
                codeMessage.setType(CodeMessageType.MESSSAGE);
            } else if (text.equals("30")) {
                sendMessage.setText("Available room for " + text + " peoples " +
                        "\nPlease select by inserting the ID:" + "\n\n" + "\t\t\t\tID\t\t\t\t\t\tCapacity\t\t\t\t\t\t\t\tDate\t\t\t" +
                        "\t\t\t\t\t\t\t\t\t\t\t\tTime\n" + SQLite.getCapacity(30));
                sendMessage.setParseMode("Markdown");
                codeMessage.setType(CodeMessageType.MESSSAGE);
            } else if (text.equals("40")) {
                sendMessage.setText("Available room for " + text + " peoples " +
                        "\nPlease select by inserting the ID:" + "\n\n" + "\t\t\t\tID\t\t\t\t\t\tCapacity\t\t\t\t\t\t\t\tDate\t\t\t" +
                        "\t\t\t\t\t\t\t\t\t\t\t\tTime\n" + SQLite.getCapacity(40));
                sendMessage.setParseMode("Markdown");
                codeMessage.setType(CodeMessageType.MESSSAGE);
            }
        } else if (text.equals("NAME DONE")) {
            sendMessage.setText("Thank you for entering your name" +
                    "\nPlease follow the steps below: \n1. Enter you mobile number by adding " +
                    "the word mobile plus an underscore in front" + "\n2. Press send" + "\n3. Send MOBILE DONE ");
            sendMessage.setParseMode("Markdown");
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("MOBILE DONE")) {
            sendMessage.setText("Thank you for entering your mobile number" +
                    "\nPlease follow the steps below: \n1. Enter you email address by adding " +
                    "the word email plus an underscore in front" + "\n2. Press send" + "\n3. Send EMAIL DONE ");
            sendMessage.setParseMode("Markdown");
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("EMAIL DONE")) {
            sendMessage.setText("Thank you for entering your email address" +
                    "\nPlease follow the steps below: \n1. Enter you purpose of booking this room by adding " +
                    "the word purpose plus an underscore in front" + "\n2. Press send" + "\n3. Send PURPOSE DONE ");
            sendMessage.setParseMode("Markdown");
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("PURPOSE DONE")) {
            sendMessage.setText("Are the details below correct?" + "\nRoom ID               : " + GinvereeBot.theroomid +
                    "\nNRIC                     : " + GinvereeBot.thenric + "\nName                   : " + GinvereeBot.thename +
                    "\nMobile Number  : " + GinvereeBot.themobile + "\nEmail Address    : " + GinvereeBot.theemail +
                    "\nPurpose:\n" + GinvereeBot.thepurpose);
            sendMessage.setParseMode("Markdown");
            codeMessage.setType(CodeMessageType.MESSSAGE);
            sendMessage.setReplyMarkup(
                    InlineButton.keyboard(
                            InlineButton.collection(
                                    InlineButton.row(
                                            InlineButton.button("Yes!", "yes!")),
                                    InlineButton.row(
                                            InlineButton.button("I would like to make some corrections", "corrections"))
                            )));
        } else if (text.equals("corrections")) {
            sendMessage.setText("NRIC                     : " + GinvereeBot.thenric + "\nName                   : " + GinvereeBot.thename +
                    "\nMobile Number  : " + GinvereeBot.themobile + "\nEmail Address    : " + GinvereeBot.theemail +
                    "\nPurpose:\n" + GinvereeBot.thepurpose + "\nPlease select what would you like to edit");
            sendMessage.setChatId(chatId);
            sendMessage.setParseMode("Markdown");
            sendMessage.setReplyMarkup(
                    InlineButton.keyboard(
                            InlineButton.collection(
                                    InlineButton.row(
                                            InlineButton.button("NRIC", "editNRIC")),
                                    InlineButton.row(
                                            InlineButton.button("Name", "editName")),
                                    InlineButton.row(
                                            InlineButton.button("Mobile Number", "editMobile")),
                                    InlineButton.row(
                                            InlineButton.button("Email", "editEmail")),
                                    InlineButton.row(
                                            InlineButton.button("Purpose", "editPurpose"))
                            )));
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("editNRIC")) {
            sendMessage.setText("1. Please enter your NRIC by adding the word editnric plus an underscore in front" +
                    "\n2. Send EDIT THIS NRIC");
            sendMessage.setParseMode("Markdown");
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("editName")) {
            sendMessage.setText("1. Please enter your name by adding the word editname plus an underscore in front" +
                    "\n2. Send EDIT THIS NAME");
            sendMessage.setParseMode("Markdown");
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("editMobile")) {
            sendMessage.setText("1. Please enter your mobile number by adding the word editmobile plus an underscore in front" +
                    "\n2. Send EDIT THIS MOBILE");
            sendMessage.setParseMode("Markdown");
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("editEmail")) {
            sendMessage.setText("1. Please enter your email address by adding the word editemail plus an underscore in front" +
                    "\n2. Send EDIT THIS EMAIL");
            sendMessage.setParseMode("Markdown");
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("editPurpose")) {
            sendMessage.setText("1. Please enter your purpose of booking this room by adding the word editpurpose plus an underscore in front" +
                    "\n2. Send EDIT THIS PURPOSE");
            sendMessage.setParseMode("Markdown");
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("EDIT THIS NRIC")) {
            sendMessage.setText("Are the details below correct?" + "\nRoom ID               : " + GinvereeBot.theroomid +
                    "\nNRIC                     : " + GinvereeBot.thenric + "\nName                   : " + GinvereeBot.thename +
                    "\nMobile Number  : " + GinvereeBot.themobile + "\nEmail Address    : " + GinvereeBot.theemail +
                    "\nPurpose:\n" + GinvereeBot.thepurpose);
            sendMessage.setParseMode("Markdown");
            sendMessage.setReplyMarkup(
                    InlineButton.keyboard(
                            InlineButton.collection(
                                    InlineButton.row(
                                            InlineButton.button("Yes!", "yes!")),
                                    InlineButton.row(
                                            InlineButton.button("I would like to make some corrections", "corrections"))
                            )));
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("EDIT THIS NAME")) {
            sendMessage.setText("Are the details below correct?" + "\nRoom ID               : " + GinvereeBot.theroomid +
                    "\nNRIC                     : " + GinvereeBot.thenric + "\nName                   : " + GinvereeBot.thename +
                    "\nMobile Number  : " + GinvereeBot.themobile + "\nEmail Address    : " + GinvereeBot.theemail +
                    "\nPurpose:\n" + GinvereeBot.thepurpose);
            sendMessage.setParseMode("Markdown");
            sendMessage.setReplyMarkup(
                    InlineButton.keyboard(
                            InlineButton.collection(
                                    InlineButton.row(
                                            InlineButton.button("Yes!", "yes!")),
                                    InlineButton.row(
                                            InlineButton.button("I would like to make some corrections", "corrections"))
                            )));
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("EDIT THIS MOBILE")) {
            sendMessage.setText("Are the details below correct?" + "\nRoom ID               : " + GinvereeBot.theroomid +
                    "\nNRIC                     : " + GinvereeBot.thenric + "\nName                   : " + GinvereeBot.thename +
                    "\nMobile Number  : " + GinvereeBot.themobile + "\nEmail Address    : " + GinvereeBot.theemail +
                    "\nPurpose:\n" + GinvereeBot.thepurpose);
            sendMessage.setParseMode("Markdown");
            sendMessage.setReplyMarkup(
                    InlineButton.keyboard(
                            InlineButton.collection(
                                    InlineButton.row(
                                            InlineButton.button("Yes!", "yes!")),
                                    InlineButton.row(
                                            InlineButton.button("I would like to make some corrections", "corrections"))
                            )));
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("EDIT THIS EMAIL")) {
            sendMessage.setText("Are the details below correct?" + "\nRoom ID               : " + GinvereeBot.theroomid +
                    "\nNRIC                     : " + GinvereeBot.thenric + "\nName                   : " + GinvereeBot.thename +
                    "\nMobile Number  : " + GinvereeBot.themobile + "\nEmail Address    : " + GinvereeBot.theemail +
                    "\nPurpose:\n" + GinvereeBot.thepurpose);
            sendMessage.setParseMode("Markdown");
            sendMessage.setReplyMarkup(
                    InlineButton.keyboard(
                            InlineButton.collection(
                                    InlineButton.row(
                                            InlineButton.button("Yes!", "yes!")),
                                    InlineButton.row(
                                            InlineButton.button("I would like to make some corrections", "corrections"))
                            )));
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("EDIT THIS PURPOSE")) {
            sendMessage.setText("Are the details below correct?" + "\nRoom ID               : " + GinvereeBot.theroomid +
                    "\nNRIC                     : " + GinvereeBot.thenric + "\nName                   : " + GinvereeBot.thename +
                    "\nMobile Number  : " + GinvereeBot.themobile + "\nEmail Address    : " + GinvereeBot.theemail +
                    "\nPurpose:\n" + GinvereeBot.thepurpose);
            sendMessage.setParseMode("Markdown");
            sendMessage.setReplyMarkup(
                    InlineButton.keyboard(
                            InlineButton.collection(
                                    InlineButton.row(
                                            InlineButton.button("Yes!", "yes!")),
                                    InlineButton.row(
                                            InlineButton.button("I would like to make some corrections", "corrections"))
                            )));
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("yes!")) {
            sendMessage.setText("Congratulations!" + "\nYour booking has been confirmed." +
                    "\nPlease take note that your Booking ID is " + GinvereeBot.thebookingidfromroomdetails +
                    "\nHope to see you soon!");
            sendMessage.setParseMode("Markdown");
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("admin")) {
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setText("Please enter password");
            editMessageText.setChatId(chatId);
            editMessageText.setMessageId(messageId);
            editMessageText.setParseMode("Markdown");
            editMessageText.setReplyMarkup(
                    InlineButton.keyboard(
                            InlineButton.collection(
                                    InlineButton.row(
                                            InlineButton.button("Opps! Go for user", "user")))));
            codeMessage.setType(CodeMessageType.EDIT);
            codeMessage.setEditMessageText(editMessageText);
        } else if (text.equals("user")) {
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setText("Welcome!" + "\nWhat would you like to do?");
            editMessageText.setChatId(chatId);
            editMessageText.setMessageId(messageId);
            editMessageText.setParseMode("Markdown");
            editMessageText.setReplyMarkup(
                    InlineButton.keyboard(
                            InlineButton.collection(
                                    InlineButton.row(
                                            InlineButton.button("Book a Room", "book")),
                                    InlineButton.row(
                                            InlineButton.button("Update Personal Details", "update")),
                                    InlineButton.row(
                                            InlineButton.button("Cancel Booking", "cancel")),
                                    InlineButton.row(
                                            InlineButton.button("Opps! Go for Admin", "admin")),
                                    InlineButton.row(
                                            InlineButton.button("Exit", "exit")))));
            codeMessage.setType(CodeMessageType.EDIT);
            codeMessage.setEditMessageText(editMessageText);
        } else if (text.equals("update")) {
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setText("Please enter your booking number");
            editMessageText.setChatId(chatId);
            editMessageText.setMessageId(messageId);
            editMessageText.setParseMode("Markdown");
            codeMessage.setType(CodeMessageType.EDIT);
            codeMessage.setEditMessageText(editMessageText);
            choice = "forupdate";
        } else if (text.equals("book") || text.equals("new")) {
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setText("How many people will be in the room?" +
                    "\n0-10\t\t\t: 10" +
                    "\n10-20\t: 20" +
                    "\n20-30\t: 30" +
                    "\n30-40\t: 40");
            editMessageText.setChatId(chatId);
            editMessageText.setMessageId(messageId);
            editMessageText.setParseMode("Markdown");
            codeMessage.setType(CodeMessageType.EDIT);
            codeMessage.setEditMessageText(editMessageText);
        } else if (text.equals("cancel")) {
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setText("Please enter your booking number");
            editMessageText.setChatId(chatId);
            editMessageText.setMessageId(messageId);
            editMessageText.setParseMode("Markdown");
            codeMessage.setType(CodeMessageType.EDIT);
            codeMessage.setEditMessageText(editMessageText);
            choice = "forcancel";
        } else if (bookingIdlist.contains(text)) {
            if (choice == "forcancel") {
                sendMessage.setText("We will cancel the booking number " +
                        GinvereeBot.thebookingid +
                        "\nAre you sure?");
                sendMessage.setChatId(chatId);
                sendMessage.setParseMode("Markdown");
                sendMessage.setReplyMarkup(
                        InlineButton.keyboard(
                                InlineButton.collection(
                                        InlineButton.row(
                                                InlineButton.button("Yes, I'm sure", "sure")),
                                        InlineButton.row(
                                                InlineButton.button("No, I've changed my mind", "notsure"))
                                )));
                codeMessage.setType(CodeMessageType.MESSSAGE);
            } else if (choice == "forupdate") {
                sendMessage.setText("Please select what would you like to update?");
                sendMessage.setChatId(chatId);
                sendMessage.setParseMode("Markdown");
                sendMessage.setReplyMarkup(
                        InlineButton.keyboard(
                                InlineButton.collection(
                                        InlineButton.row(
                                                InlineButton.button("My NRIC", "updateNRIC")),
                                        InlineButton.row(
                                                InlineButton.button("My Name", "updateName")),
                                        InlineButton.row(
                                                InlineButton.button("My Mobile Number", "updateMobile")),
                                        InlineButton.row(
                                                InlineButton.button("My Email", "updateEmail")),
                                        InlineButton.row(
                                                InlineButton.button("My Purpose", "updatePurpose"))
                                )));
                codeMessage.setType(CodeMessageType.MESSSAGE);
            }
        } else if (text.equals("notsure")) {
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setText("Booking number has not been cancelled. Hope to see you again!");
            editMessageText.setChatId(chatId);
            editMessageText.setMessageId(messageId);
            codeMessage.setType(CodeMessageType.EDIT);
            codeMessage.setEditMessageText(editMessageText);
        } else if (text.equals("updateNRIC")) {
            sendMessage.setText("1. Please enter new NRIC by adding the word updatenric plus an underscore in front" +
                    "\n2. Send UPDATE THIS NRIC");
            sendMessage.setParseMode("Markdown");
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("updateName")) {
            sendMessage.setText("1. Please enter new name by adding the word updatename plus an underscore in front" +
                    "\n2. Send UPDATE THIS NAME");
            sendMessage.setParseMode("Markdown");
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("updateMobile")) {
            sendMessage.setText("1. Please enter new mobile number by adding the word updatemobile plus an underscore in front" +
                    "\n2. Send UPDATE THIS MOBILE");
            sendMessage.setParseMode("Markdown");
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("updateEmail")) {
            sendMessage.setText("1. Please enter new email address by adding the word updateemail plus an underscore in front" +
                    "\n2. Send UPDATE THIS EMAIL");
            sendMessage.setParseMode("Markdown");
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("updatePurpose")) {
            sendMessage.setText("1. Please enter new purpose of booking this room by adding the word updatepurpose plus an underscore in front" +
                    "\n2. Send UPDATE THIS PURPOSE");
            sendMessage.setParseMode("Markdown");
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("UPDATE THIS NRIC")) {
            sendMessage.setText("Congratulations, you have successfully changed your NRIC number!");
            sendMessage.setParseMode("Markdown");
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("UPDATE THIS NAME")) {
            sendMessage.setText("Congratulations, you have successfully changed your name!");
            sendMessage.setParseMode("Markdown");
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("UPDATE THIS MOBILE")) {
            sendMessage.setText("Congratulations, you have successfully changed your mobile number!");
            sendMessage.setParseMode("Markdown");
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("UPDATE THIS EMAIL")) {
            sendMessage.setText("Congratulations, you have successfully changed your email address!");
            sendMessage.setParseMode("Markdown");
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("UPDATE THIS PURPOSE")) {
            sendMessage.setText("Congratulations, you have successfully changed your purpose of booking the room!");
            sendMessage.setParseMode("Markdown");
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("sure")) {
            sendMessage.setText("Booking number for " + GinvereeBot.thebookingid + " has been cancelled." +
                    "\nWould you like to book for another room?");
            sendMessage.setChatId(chatId);
            sendMessage.setParseMode("Markdown");
            sendMessage.setReplyMarkup(
                    InlineButton.keyboard(
                            InlineButton.collection(
                                    InlineButton.row(
                                            InlineButton.button("Yes, I would like to book a new room", "new")),
                                    InlineButton.row(
                                            InlineButton.button("No, thank you", "tq"))
                            )));
            codeMessage.setType(CodeMessageType.MESSSAGE);
        } else if (text.equals("tq") || text.equals("exit") || (text.equals("noneedchanealready"))) {
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setText("Thank you. Hope to see you again!");
            editMessageText.setChatId(chatId);
            editMessageText.setMessageId(messageId);
            codeMessage.setType(CodeMessageType.EDIT);
            codeMessage.setEditMessageText(editMessageText);
        }
        return codeMessage;
    }
}
