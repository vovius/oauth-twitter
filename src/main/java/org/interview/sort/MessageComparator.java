package org.interview.sort;

import org.interview.model.Message;

import java.util.Comparator;

/**
 * Created by sony on 4/13/2017.
 */
public class MessageComparator implements Comparator<Message> {

    @Override
    public int compare(Message o1, Message o2) {
        if (o1 == null)
            return -1;
        if (o2 == null)
            return 1;
        if (o1 == o2)
            return 0;

        int result = o1.getCreationDate().compareTo(o2.getCreationDate());
        if (result == 0)
            result = o1.getId().compareTo(o2.getId());

        return result;

    }
}
