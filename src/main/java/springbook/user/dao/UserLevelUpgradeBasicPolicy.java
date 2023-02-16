package springbook.user.dao;

import springbook.user.domain.Level;
import springbook.user.domain.User;


public class UserLevelUpgradeBasicPolicy implements UserLevelUpgradePolicy{
    private static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    private static final int MIN_RECOMMEND_FOR_GOLD = 30;

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
