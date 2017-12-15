package highcouncil.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import highcouncil.domain.Kingdom;
import highcouncil.domain.Player;
import highcouncil.domain.StatHolder;

@Service
public class CodeResolver {
	enum Category { WEALTH, PIETY, FAVOUR, POPULARITY, HEALTH, MILITARY}
	
	public void resolveCode(String code, StatHolder target) {
		if (code == null || StringUtils.isBlank(code)) return;
		String[] tokens = code.split("\\s+");
		for (int i = 0; i<tokens.length; i++) {
			String token = tokens[i];
			if (token.compareToIgnoreCase(Category.WEALTH.name()) == 0) {
				target.setWealth(target.getWealth()+getAdjustment(tokens[++i]));
			}
			if (token.compareToIgnoreCase(Category.MILITARY.name()) == 0) {
				target.setMilitary(target.getMilitary()+getAdjustment(tokens[++i]));
			}
			if (token.compareToIgnoreCase(Category.PIETY.name()) == 0) {
				target.setPiety(target.getPiety()+getAdjustment(tokens[++i]));
			}
			if (token.compareToIgnoreCase(Category.POPULARITY.name()) == 0) {
				target.setPopularity(target.getPopularity()+getAdjustment(tokens[++i]));
			}
			if (token.compareToIgnoreCase(Category.FAVOUR.name()) == 0) {
				Player player = (Player)target;
				player.setFavour(player.getFavour()+getAdjustment(tokens[++i]));
			}
			if (token.compareToIgnoreCase(Category.HEALTH.name()) == 0) {
				Kingdom kingdom = (Kingdom)target;
				kingdom.setHealth(kingdom.getHealth()+getAdjustment(tokens[++i]));
			}
		}
	}
	
	private int getAdjustment(String token) {
		return Integer.parseInt(token);
	}
}
