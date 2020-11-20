package com.highload.socialNetwork.repos;

import com.highload.socialNetwork.model.ChatRoom;
import com.highload.socialNetwork.model.Client;
import com.highload.socialNetwork.service.DbConnectionProvider;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ChatRoomRepository {
    private final DbConnectionProvider provider;

    public ChatRoomRepository(DbConnectionProvider provider) {
        this.provider = provider;
    }

    public ChatRoom findBySenderIdAndRecipientId(Long senderId, Long recipientId) {
        ChatRoom chatRoom = null;
        try (PreparedStatement preparedStatement = provider.getConnection().prepareStatement("SELECT * FROM chat_room cr where cr.sender_id = ? and cr.recipient_id = ?  LIMIT 1");) {
            preparedStatement.setLong(1, senderId);
            preparedStatement.setLong(2, recipientId);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                chatRoom = new ChatRoom(
                        resultSet.getLong("id"),
                        resultSet.getString("chat_id"),
                        resultSet.getLong("sender_id"),
                        resultSet.getLong("recipient_id")
                );
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return chatRoom;
    }

    public void save(ChatRoom chatRoom) {
        try (PreparedStatement preparedStatement = provider.getConnection().prepareStatement("INSERT INTO chat_room (chat_id,sender_id,recipient_id) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);) {
            int i = 1;
            preparedStatement.setString(i++, chatRoom.getChatId());
            preparedStatement.setLong(i++, chatRoom.getSenderId());
            preparedStatement.setLong(i, chatRoom.getRecipientId());
            preparedStatement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
