package springbook.user.dao;

import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserLevelUpgradeEventPolicy implements UserLevelUpgradePolicy {

    private static final int MIN_LOGCOUNT_FOR_SILVER = 40;
    private static final int MIN_RECOMMEND_FOR_GOLD = 20;

    @Override
    public boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC: return user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER;
            case SILVER: return user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD;
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level: "+ currentLevel);
        }
    }
}
