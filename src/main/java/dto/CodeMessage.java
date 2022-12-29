package dto;

import enums.CodeMessageType;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

/**
 * This class will declare the every type of message.
 *
 * @author Ginveree Kong May Cheng 279045
 */
public class CodeMessage {
    /**
     * Declaring variable codeMessageType.
     */
    public CodeMessageType type;
    /**
     * Declaring variable sendMessage.
     */
    private SendMessage sendMessage;
    /**
     * Declaring variable editMessageText.
     */
    private EditMessageText editMessageText;

    /**
     * This method is to get the sendMessage's action.
     *
     * @return The sendMessage's action.
     */
    public SendMessage getSendMessage() {
        return sendMessage;
    }

    /**
     * This method is to set the sendMessage's action.
     *
     * @param sendMessage The sendMessage.
     */
    public void setSendMessage(SendMessage sendMessage) {
        this.sendMessage = sendMessage;
    }

    /**
     * This method is to get the editMessageText's action.
     *
     * @return The editMesssageText's action.
     */
    public EditMessageText getEditMessageText() {
        return editMessageText;
    }

    /**
     * This method is to set the editMessageText's action.
     *
     * @param editMessageText The editMessageText's action.
     */
    public void setEditMessageText(EditMessageText editMessageText) {
        this.editMessageText = editMessageText;
    }

    /**
     * This method is to get the type of the codeMessage.
     *
     * @return The codeMessage's type.
     */
    public CodeMessageType getType() {
        return type;
    }

    /**
     * This method is to set the type of the codeMessage.
     *
     * @param type The codeMessage's type.
     */
    public void setType(CodeMessageType type) {
        this.type = type;
    }
}
