package my.uum;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is to handle the different inline buttons. Calling these methods, and it will handle the inline buttons.
 */
public class InlineButton {
    /**
     * This method is to create and display a button in the chat.
     *
     * @param text         Word display for user to select.
     * @param callbackData represents an incoming callback query from a callback button in an inline keyboard.
     * @return both text and callbackdata will be on the function on the button in telegram chat.
     */
    public static InlineKeyboardButton button(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }

    /**
     * This method is used when there are different buttons that representing different functions.
     *
     * @param inlineKeyboardButtons get details by inputting one method the parameter of this method.
     * @return display/create rows with functions in them in telegram chat.
     */
    public static List<InlineKeyboardButton> row(InlineKeyboardButton... inlineKeyboardButtons) {
        List<InlineKeyboardButton> row = new LinkedList<>();
        row.addAll(Arrays.asList(inlineKeyboardButtons));
        return row;
    }

    /**
     * This method is to create a linked list as rows.
     *
     * @param rows get rows by inputting one method as the parameter of this method.
     * @return the linked list as collection.
     */
    public static List<List<InlineKeyboardButton>> collection(List<InlineKeyboardButton>... rows) {
        List<List<InlineKeyboardButton>> collection = new LinkedList<>();
        collection.addAll(Arrays.asList(rows));
        return collection;
    }

    /**
     * This method is to create a keyboardMarkup.
     *
     * @param collection get collection by inputting one method as the parameter of this method.
     * @return everything for inline keyboard buttons will be return as keyboardMarkup.
     */
    public static InlineKeyboardMarkup keyboard(List<List<InlineKeyboardButton>> collection) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(collection);
        return keyboardMarkup;
    }
}
