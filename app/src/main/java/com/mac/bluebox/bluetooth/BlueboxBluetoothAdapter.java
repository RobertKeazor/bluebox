package com.mac.bluebox.bluetooth;

import java.util.List;

/**
 * Created by anyer on 6/26/15.
 */
public class BlueboxBluetoothAdapter {
    private List<String> friendsList;

    public void findFriends() {

    }

    public List<String> getFriendsList() {
        findFriends();
        return friendsList;
    }
}
