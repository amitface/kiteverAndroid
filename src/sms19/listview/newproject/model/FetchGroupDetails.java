package sms19.listview.newproject.model;

import java.io.Serializable;
import java.util.List;

public class FetchGroupDetails {

    @Override
    public String toString() {
        return "FetchGroupDetails [GroupDetails=" + GroupDetails + "]";
    }

    List<GroupDetails> GroupDetails;// Response:
    // {"GroupDetails":[{"group_name":"Filmy","group_id":51589}

    // {"GroupDetails":[{"group_name":"demo","Column1":4}
    // {"GroupDetails":[{"group_name":"newgp","Column1":1},{"group_name":"gfhg","Column1":1}]}

    public List<GroupDetails> getGroupDetails() {
        return GroupDetails;
    }

    public void setGroupDetails(List<GroupDetails> groupDetails) {
        GroupDetails = groupDetails;
    }

    // get size of group added into list
    public int getListSize() {
        int i = GroupDetails.size();
        return i;
    }

    public class GroupDetails implements Serializable {

        private static final long serialVersionUID = 1L;

        // 04-15 17:14:37.614: W/TAG(19943): Response:
        // {"GroupDetails":[{"group_name":"Filmy","group_id":51589},{"group_name":"Cricket","group_id":51590}]}

        public String group_name;
        public String NoOfContacts;
        public String Group_JID;

        @Override
        public String toString() {
            return "GroupDetails [group_name=" + group_name + ", NoOfContacts="
                    + NoOfContacts + ", Group_JID=" + Group_JID
                    + ", EmergencyType=" + EmergencyType
                    + ", EmergencyMessage=" + EmergencyMessage + ", color="
                    + color + ", isSelected=" + isSelected + "]";
        }

        public String getGroup_JID() {
            return Group_JID;
        }

        public void setGroup_JID(String broadcastGroupId) {
            this.Group_JID = broadcastGroupId;
        }

        public String EmergencyType;
        public String EmergencyMessage;
        public final String color = "#236966";
        public boolean isSelected;

        public String getNoOfContacts() {
            return NoOfContacts;
        }

        public void setNoOfContacts(String noOfContacts) {
            NoOfContacts = noOfContacts;
        }

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public String getEmergencyType() {
            return EmergencyType;
        }

        public void setEmergencyType(String emergencyType) {
            EmergencyType = emergencyType;
        }

        public String getEmergencyMessage() {
            return EmergencyMessage;
        }

        public void setEmergencyMessage(String emergencyMessage) {
            EmergencyMessage = emergencyMessage;
        }

    }

}
