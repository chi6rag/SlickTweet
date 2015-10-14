package net.chi6rag.twitchblade;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Follower {
    private final DbConnection connection;
    private final User user;
    private PreparedStatement userFollowPreparedStatement;

    public Follower(User user, DbConnection connection){
        this.user = user;
        this.connection = connection;
    }

    public boolean follow(User userToFollow){
        if( isAnyUserUnsaved(this.user, userToFollow) ) return false;
        boolean hasFollowed = false;
        prepareUserFollowStatement();
        try {
            this.userFollowPreparedStatement.setInt(1, this.user.getId());
            this.userFollowPreparedStatement.setInt(2, userToFollow.getId());
            hasFollowed = this.userFollowPreparedStatement.execute();
        } catch (SQLException e) {
            // e.printStackTrace();
        }
        return hasFollowed;
    }

    private boolean isAnyUserUnsaved(User... users){
        for(int i=0; i<users.length; i++){
            if(users[i].getId() == null) return true;
        }
        return false;
    }

    private void prepareUserFollowStatement(){
        try {
            this.userFollowPreparedStatement = this.connection
                    .prepareStatement("INSERT INTO relationship(follower_id, followed_id) " +
                            "VALUES(?, ?) RETURNING follower_id, followed_id");
        } catch (SQLException e) {
            // e.printStackTrace();
        }
    }

}
