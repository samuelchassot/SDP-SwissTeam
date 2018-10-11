package ch.epfl.swissteam.services;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChatRelationTest {
    @Test
    public void objectCreationYieldExpectedValueOnGets() {
        String user1 = "Martin Latex King";
        String user2 = "Robin des Cailloux";
        String user3 = "Badass Romarin";

        ArrayList<String> users = new ArrayList<>(Arrays.asList(user1,user2,user3));
        ChatRelation relation = new ChatRelation(user1, user2, user3);

        assertEquals(3, relation.getUserIds_().size());
        assertTrue(relation.getUserIds_().equals(users));
    }

    @Test
    public void setValueGivesBackSameWhenGet() {
        String user1 = "Albane la Valaisanne";
        String user2 = "Pierre Caillasse";
        String user3 = "Balou Nounours";
        String CRId = "abu723ddl92ndk";

        ArrayList<String> users = new ArrayList<>(Arrays.asList(user1,user2,user3));
        ChatRelation relation = new ChatRelation();

        relation.setId_(CRId);
        relation.setUserIds_(users);

        assertEquals(3, relation.getUserIds_().size());
        assertTrue(relation.getUserIds_().equals(users));
        assertEquals(CRId, relation.getId_());
    }
}
