package springbook.user.dao;

import springbook.user.domain.Level;
import springbook.user.domain.User;

public interface UserLevelUpgradePolicy {

    public boolean canUpgradeLevel(User user);
}
