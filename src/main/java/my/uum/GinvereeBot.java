package my.uum;

import dto.CodeMessage;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This is the class for Ginvereebot that will get updates from user.
 */
public class GinvereeBot extends TelegramLongPollingBot {
    /**
     * Declare GeneralController.
     */
    private GeneralController generalController;
    /**
     * Declare static variables for different functions and classes to use.
     */
    public static String thenric, thename, themobile, theemail, thepurpose, theroomid, thebookingid, thestaffid, thebookingidfromroomdetails, thebookingidfromguestdetails;
    /**
     * Declare static variables for different functions and classes to use.
     */
    public static String theupdatednric, theupdatedname, theupdatedmobile, theupdatedemail, theupdatedpurpose;
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
     * Declare static details array for different functions and classes to use.
     */
    public String[] details;

    /**
     * This method uses timer thread to refresh the elements in the arraylist every 5 seconds.
     * Purpose is to import the latest updated data from MySQLite.
     * Methods in GeneralController will be imported in this class.
     */
    public GinvereeBot() {
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
        this.generalController = new GeneralController();
    }

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
//                System.out.println(resultSet.getString(1));
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
//                System.out.println(resultSet.getString(1));
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
     * This method is to update the locally stored state based on the parameters received.
     *
     * @param update Receive actions by user.
     */
    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
//            User user = callbackQuery.getFrom();
            Message message = callbackQuery.getMessage();

            if (data.equals("admin")) {
                this.sendMsg(this.generalController.handle(data, message.getChatId(), message.getMessageId()));
            } else if (data.equals("user") || data.equals("new")) {
                this.sendMsg(this.generalController.handle(data, message.getChatId(), message.getMessageId()));
            } else if (data.equals("book")) {
                this.sendMsg(this.generalController.handle(data, message.getChatId(), message.getMessageId()));
            } else if (data.equals("yes!")) {
                SQLite.guestDetails(theroomid, thenric, thename, themobile, theemail, thepurpose);
                SQLite.roomBooked(theroomid);
                thebookingidfromguestdetails = SQLite.getBookingIdFromGuestDetails(theroomid);
                SQLite.insertBookingIdIntoRoomDetails(thebookingidfromguestdetails, theroomid); //the new generated booking Id will be inserted to roomDetails
                thebookingidfromroomdetails = SQLite.getBookingIdFromRoomDetails(theroomid); //get the booking number from the roomId (just booked)
                this.sendMsg(this.generalController.handle(data, message.getChatId(), message.getMessageId()));
            } else if (data.equals("update")) {
                this.sendMsg(this.generalController.handle(data, message.getChatId(), message.getMessageId()));
            } else if (data.equals("corrections")) {
                this.sendMsg(this.generalController.handle(data, message.getChatId(), message.getMessageId()));
            } else if (data.equals("editNRIC")) {
                this.sendMsg(this.generalController.handle(data, message.getChatId(), message.getMessageId()));
            } else if (data.equals("editName")) {
                this.sendMsg(this.generalController.handle(data, message.getChatId(), message.getMessageId()));
            } else if (data.equals("editMobile")) {
                this.sendMsg(this.generalController.handle(data, message.getChatId(), message.getMessageId()));
            } else if (data.equals("editEmail")) {
                this.sendMsg(this.generalController.handle(data, message.getChatId(), message.getMessageId()));
            } else if (data.equals("editPurpose")) {
                this.sendMsg(this.generalController.handle(data, message.getChatId(), message.getMessageId()));
            } else if (data.equals("updateNRIC")) {
                this.sendMsg(this.generalController.handle(data, message.getChatId(), message.getMessageId()));
            } else if (data.equals("updateName")) {
                this.sendMsg(this.generalController.handle(data, message.getChatId(), message.getMessageId()));
            } else if (data.equals("updateMobile")) {
                this.sendMsg(this.generalController.handle(data, message.getChatId(), message.getMessageId()));
            } else if (data.equals("updateEmail")) {
                this.sendMsg(this.generalController.handle(data, message.getChatId(), message.getMessageId()));
            } else if (data.equals("updatePurpose")) {
                this.sendMsg(this.generalController.handle(data, message.getChatId(), message.getMessageId()));
            } else if (data.equals("cancel")) {
                this.sendMsg(this.generalController.handle(data, message.getChatId(), message.getMessageId()));
            } else if (data.equals("notsure")) {
                this.sendMsg(this.generalController.handle(data, message.getChatId(), message.getMessageId()));
            } else if (data.equals("sure")) {
                this.sendMsg(this.generalController.handle(data, message.getChatId(), message.getMessageId()));
                SQLite.roomAvailable(SQLite.getRoomIdFromBookingId(thebookingid));
                SQLite.deleteDetails(thebookingid);
                SQLite.deleteBookingIdinRoomDetails(thebookingid);
            } else if (data.equals("tq")) {
                this.sendMsg(this.generalController.handle(data, message.getChatId(), message.getMessageId()));
            } else if (data.equals("exit")) {
                this.sendMsg(this.generalController.handle(data, message.getChatId(), message.getMessageId()));
            } else if (data.equals("10") || data.equals("20") || data.equals("30") || data.equals("40")) {
                this.sendMsg(this.generalController.handle(data, message.getChatId(), message.getMessageId()));
            }
        }

        Message message = update.getMessage();
        Integer messageId = message.getMessageId();
        String text = message.getText();
        User user = message.getFrom();
        System.out.println("messageId: " + messageId + "\t\tUser_Name: " + user.getFirstName() + "\t\tUser_username: " + user.getUserName() + "\t\tMessage: " + text);
        sendMessage.setChatId(String.valueOf(user.getId()));
        sendMessage.setChatId(message.getChatId());

        if (text.equals("/start")) {
            this.sendMsg(this.generalController.handle(text, message.getChatId(), messageId));
        } else if (text.equals("ginveree123")) {
            this.sendMsg(this.generalController.handle(text, message.getChatId(), messageId));
        } else if (text.equals("10") || text.equals("20") || text.equals("30") || text.equals("40")) {
            this.sendMsg(this.generalController.handle(text, message.getChatId(), messageId));
        } else if (roomIdlist.contains(text)) {
            this.sendMsg(this.generalController.handle(text, message.getChatId(), messageId));
            theroomid = text;
        } else if (bookingIdlist.contains(text)) {//for cancelling
            thebookingid = text;
            this.sendMsg(this.generalController.handle(text, message.getChatId(), messageId));
        } else if (staffIdlist.contains(text)) {
            this.sendMsg(this.generalController.handle(text, message.getChatId(), messageId));
            thestaffid = text;
        } else if (text.equals("NRIC DONE")) {
            this.sendMsg(this.generalController.handle(text, message.getChatId(), message.getMessageId()));
        } else if (text.equals("NAME DONE")) {
            this.sendMsg(this.generalController.handle(text, message.getChatId(), message.getMessageId()));
        } else if (text.equals("MOBILE DONE")) {
            this.sendMsg(this.generalController.handle(text, message.getChatId(), message.getMessageId()));
        } else if (text.equals("EMAIL DONE")) {
            this.sendMsg(this.generalController.handle(text, message.getChatId(), message.getMessageId()));
        } else if (text.equals("PURPOSE DONE")) {
            this.sendMsg(this.generalController.handle(text, message.getChatId(), message.getMessageId()));
        } else if (text.equals("UPDATE THIS NRIC")) {
            this.sendMsg(this.generalController.handle(text, message.getChatId(), message.getMessageId()));
            SQLite.menuUpdateNRIC(theupdatednric, thebookingid);
        } else if (text.equals("UPDATE THIS NAME")) {
            this.sendMsg(this.generalController.handle(text, message.getChatId(), message.getMessageId()));
            SQLite.menuUpdateName(theupdatedname, thebookingid);
        } else if (text.equals("UPDATE THIS MOBILE")) {
            this.sendMsg(this.generalController.handle(text, message.getChatId(), message.getMessageId()));
            SQLite.menuUpdateMobile(theupdatedmobile, thebookingid);
        } else if (text.equals("UPDATE THIS EMAIL")) {
            this.sendMsg(this.generalController.handle(text, message.getChatId(), message.getMessageId()));
            SQLite.menuUpdateEmail(theupdatedemail, thebookingid);
        } else if (text.equals("UPDATE THIS PURPOSE")) {
            this.sendMsg(this.generalController.handle(text, message.getChatId(), message.getMessageId()));
            SQLite.menuUpdatePurpose(theupdatedpurpose, thebookingid);
        } else if (text.equals("EDIT THIS NRIC")) {
            this.sendMsg(this.generalController.handle(text, message.getChatId(), message.getMessageId()));
        } else if (text.equals("EDIT THIS NAME")) {
            this.sendMsg(this.generalController.handle(text, message.getChatId(), message.getMessageId()));
        } else if (text.equals("EDIT THIS MOBILE")) {
            this.sendMsg(this.generalController.handle(text, message.getChatId(), message.getMessageId()));
        } else if (text.equals("EDIT THIS EMAIL")) {
            this.sendMsg(this.generalController.handle(text, message.getChatId(), message.getMessageId()));
        } else if (text.equals("EDIT THIS PURPOSE")) {
            this.sendMsg(this.generalController.handle(text, message.getChatId(), message.getMessageId()));
        }

        String message1 = String.valueOf(update.getMessage());
        details = update.getMessage().getText().split("_", 12);
        if (details[0].equals("nric")) {
            message1 = details[1];
            thenric = message1;
        } else if (details[0].equals("name")) {
            message1 = details[1];
            thename = message1;
        } else if (details[0].equals("mobile")) {
            message1 = details[1];
            themobile = message1;
        } else if (details[0].equals("email")) {
            message1 = details[1];
            theemail = message1;
        } else if (details[0].equals("purpose")) {
            message1 = details[1];
            thepurpose = message1;
        }
        //edit
        if (details[0].equals("editnric")) {
            message1 = details[1];
            thenric = message1;
        } else if (details[0].equals("editname")) {
            message1 = details[1];
            thename = message1;
        } else if (details[0].equals("editmobile")) {
            message1 = details[1];
            themobile = message1;
        } else if (details[0].equals("editemail")) {
            message1 = details[1];
            theemail = message1;
        } else if (details[0].equals("editpurpose")) {
            message1 = details[1];
            thepurpose = message1;
        }
        //update
        else if (details[0].equals("updatenric")) {
            message1 = details[1];
            theupdatednric = message1;
        } else if (details[0].equals("updatename")) {
            message1 = details[1];
            theupdatedname = message1;
        } else if (details[0].equals("updatemobile")) {
            message1 = details[1];
            theupdatedmobile = message1;
        } else if (details[0].equals("updateemail")) {
            message1 = details[1];
            theupdatedemail = message1;
        } else if (details[0].equals("updatepurpose")) {
            message1 = details[1];
            theupdatedpurpose = message1;
        }
    }

    /**
     * This method is to send text messages.
     *
     * @param sendMessage Get text messages.
     */
    public void sendMsg(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is to send text messages.
     * We have a total of 2 types of code messages, which is MESSAGE and EDIT.
     *
     * @param codeMessage Get text messages.
     */
    public void sendMsg(CodeMessage codeMessage) {
        try {
            switch (codeMessage.getType()) {
                case MESSSAGE:
                    execute(codeMessage.getSendMessage());
                    break;
                case EDIT:
                    execute(codeMessage.getEditMessageText());
                    break;
                default:
                    break;
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is to get the username of the Bot.
     *
     * @return Bot name GinvereeBot.
     */
    @Override
    public String getBotUsername() {
        // TODO
        return "GinvereeBot";
    }

    /**
     * This method is to get unique authentication token.
     *
     * @return Unique authentication token.
     */
    @Override
    public String getBotToken() {
        // TODO
        return "5903499036:AAHNwUScope7WPIKB_LVeS6wHGQwXqkBMaY";
    }
}
