package org.interview.sort;

import org.interview.model.User;

import java.util.Comparator;

/**
 * Created by sony on 4/13/2017.
 */
public class UserComparator implements Comparator<User> {

    @Override
    public int compare(User o1, User o2) {
        if (o1 == null)
            return -1;
        if (o2 == null)
            return 1;
        if (o1 == o2)
            return 0;

        int result = o1.getCreated().compareTo(o2.getCreated());
        if (result == 0) {
            result = o1.getId().compareTo(o2.getId());
        }

        return result;
    }
}
